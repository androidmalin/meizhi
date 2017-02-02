package meizhi.meizhi.malin.utils;

import android.support.v7.widget.RecyclerView;

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
 */
public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    public static String TAG = "EndlessScrollListener";

    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 6; // The minimum amount of items to have below your current scroll position before loading more.
    private int firstVisibleItem;
    private int visibleItemCount;
    private int totalItemCount;

    private int currentPage = 1;

    private RecyclerViewPositionHelper mRecyclerViewHelper;
    private boolean isBottom = false;
    private boolean isLoadMore = true;

    private boolean mIsSuccess = true;
    private boolean mIsZero = false;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        isBottom = isSlideToBottom(recyclerView);
        dy = 1000;
        if (dy > 0) {
            mRecyclerViewHelper = RecyclerViewPositionHelper.createHelper(recyclerView);
            visibleItemCount = recyclerView.getChildCount();
            totalItemCount = mRecyclerViewHelper.getItemCount();
            firstVisibleItem = mRecyclerViewHelper.findFirstVisibleItemPosition();

            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;
                }
            }
            if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                // End has been reached
                // Do something
                currentPage++;
                onLoadMore(currentPage);
                loading = true;
            }
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
        visibleThreshold = 6;
        firstVisibleItem = 0;
        visibleItemCount = 0;
        totalItemCount = 0;
        currentPage = 1;
        isBottom = false;
        isLoadMore = true;
    }

    //Start loading
    public abstract void onLoadMore(int currentPage);
}