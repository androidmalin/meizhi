package meizhi.meizhi.malin.utils;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * 类描述:
 * 创建人:malin.myemail@163.com
 * 创建时间:2017.1.31 17:18
 * 备注:{@link }
 * 修改人:
 * 修改时间:
 * 修改备注:
 * 版本:
 * Custom Scroll listener for RecyclerView.
 * Based on implementation https://gist.github.com/ssinss/e06f12ef66c51252563e
 * https://gist.github.com/nesquena/d09dc68ff07e845cc622#gistcomment-1984451
 * https://guides.codepath.com/android/Endless-Scrolling-with-AdapterViews-and-RecyclerView
 */
public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {

    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 20; // The minimum amount of items to have below your current scroll position before loading more.
    private int totalItemCount;

    private int currentPage = 1;

    private boolean isBottom = false;
    private boolean isLoadMore = true;

    private boolean mIsSuccess = true;
    private boolean mIsZero = false;

    private RecyclerView.LayoutManager mLayoutManager;


    public EndlessRecyclerOnScrollListener(LinearLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
    }

    public EndlessRecyclerOnScrollListener(GridLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
        visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
    }

    public EndlessRecyclerOnScrollListener(StaggeredGridLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
        visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (dy < 0) return;
        isBottom = isSlideToBottom(recyclerView);
        int lastVisibleItemPosition = 0;
        totalItemCount = mLayoutManager.getItemCount();

        if (mLayoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) mLayoutManager).findLastVisibleItemPositions(null);
            // get maximum element within the list
            lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions);
        } else if (mLayoutManager instanceof GridLayoutManager) {
            lastVisibleItemPosition = ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        } else if (mLayoutManager instanceof LinearLayoutManager) {
            lastVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        }

        if (loading && (totalItemCount > previousTotal)) {
            loading = false;
            previousTotal = totalItemCount;
        }
        if (!loading && ((lastVisibleItemPosition + 1) + visibleThreshold) >= totalItemCount) {
            currentPage++;
            onLoadMore(currentPage);
            loading = true;
        }
    }

    private boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        if (isLoadMore && isBottom && !mIsSuccess && newState == RecyclerView.SCROLL_STATE_IDLE) {
            onLoadMore(currentPage);
            return;
        }

        if (isLoadMore && isBottom && mIsZero && newState == RecyclerView.SCROLL_STATE_IDLE) {
            currentPage++;
            onLoadMore(currentPage);
            loading = true;
        }
        if (newState == RecyclerView.SCROLL_STATE_SETTLING || newState == RecyclerView.SCROLL_STATE_DRAGGING) {
            if (!Fresco.getImagePipeline().isPaused()) {
                Fresco.getImagePipeline().pause();
            }
        } else {
            if (Fresco.getImagePipeline().isPaused()) {
                Fresco.getImagePipeline().resume();
            }
        }
    }


    public void setLoadMoreFlag(boolean is) {
        this.isLoadMore = is;
    }

    /**
     * 网络请求成功的标示
     *
     * @param isOk isOk
     */
    public void setErrorFlag(boolean isOk) {
        this.mIsSuccess = isOk;
    }


    public void setZeroFlag(boolean isZero) {
        mIsZero = isZero;
    }

    public void resetCurrentPage() {
        mIsSuccess = true;
        mIsZero = false;
        previousTotal = 0;
        loading = true;
        visibleThreshold = 20;
        totalItemCount = 0;
        currentPage = 1;
        isBottom = false;
        isLoadMore = true;
    }

    //Start loading
    public abstract void onLoadMore(int currentPage);

    private int getLastVisibleItem(int[] lastVisibleItemPositions) {
        int maxSize = 0;
        for (int i = 0; i < lastVisibleItemPositions.length; i++) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i];
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i];
            }
        }
        return maxSize;
    }
}