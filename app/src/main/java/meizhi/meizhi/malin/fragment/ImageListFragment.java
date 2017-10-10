package meizhi.meizhi.malin.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import com.squareup.leakcanary.RefWatcher;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import meizhi.meizhi.malin.R;
import meizhi.meizhi.malin.adapter.ImageAdapter;
import meizhi.meizhi.malin.application.MApplication;
import meizhi.meizhi.malin.network.api.ImageApi;
import meizhi.meizhi.malin.network.services.ImageService;
import meizhi.meizhi.malin.utils.CatchUtil;
import meizhi.meizhi.malin.utils.DestroyCleanUtil;
import meizhi.meizhi.malin.utils.EndlessRecyclerOnScrollListener;
import meizhi.meizhi.malin.utils.FastScrollLinearLayoutManager;
import meizhi.meizhi.malin.utils.RxUtils;
import meizhi.meizhi.malin.utils.UMengEvent;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

/**
 * 类描述: 图片瀑布流列表
 * 创建人:malin.myemail@163.com
 * 创建时间:2017/01/31 18:20
 * 备注:{@link }
 * 修改人:
 * 修改时间:
 * 修改备注:
 * 版本:
 */
public class ImageListFragment extends Fragment implements ImageAdapter.itemClickListener {

    private static final String TAG = ImageListFragment.class.getSimpleName();
    private View mLayoutError;
    private View mLayoutEmpty;
    private ViewStub mStubError;
    private ViewStub mStubEmpty;
    private ImageAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private Handler mHandler = new Handler();
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private EndlessRecyclerOnScrollListener mEndlessListener;
    private Activity mActivity;
    private static final int NUMBER = 50;
    private Subscription mSubscription;
    private Subscription mSubscription2;

    private View mRootView;

    public static ImageListFragment newInstance(Bundle bundle) {
        ImageListFragment logFragment = new ImageListFragment();
        logFragment.setArguments(bundle);
        return logFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView != null) {
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (parent != null) {
                parent.removeView(mRootView);
            }
        }
        try {
            mRootView = inflater.inflate(R.layout.image_list_layout, container, false);
        } catch (InflateException e) {
            CrashReport.postCatchedException(e);
            e.printStackTrace();
            if (getActivity() != null) {
                getActivity().finish();
            }
        }
        initView();
        initData();
        initListener();
        initLoad();
        return mRootView;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }


    private void initView() {
        mSwipeRefreshLayout = mRootView.findViewById(R.id.img_swipe_refresh);
        mRecyclerView = mRootView.findViewById(R.id.img_list_rv);
        mStubError = mRootView.findViewById(R.id.view_stub_error);
        mStubEmpty = mRootView.findViewById(R.id.view_stub_empty);
    }


    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;

    private void initData() {
        mActivity = getActivity();
        mAdapter = new ImageAdapter(mActivity);
        mStaggeredGridLayoutManager = new FastScrollLinearLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);

        mRecyclerView.setAdapter(mAdapter);
        mEndlessListener = new EndlessRecyclerOnScrollListener(mStaggeredGridLayoutManager) {
            @Override
            public void onLoadMore(final int currentPage) {
                mEndlessListener.setLoadMoreFlag(true);
                //设置正在加载更多
                if (mIsHasData) {
                    mAdapter.changeMoreStatus(ImageAdapter.LOADING_MORE);
                    MobclickAgent.onEvent(mActivity, UMengEvent.PullToLoadMore);
                    getFangs(currentPage);
                }
                //delayLoadMoreData(currentPage);
            }
        };
        mRecyclerView.addOnScrollListener(mEndlessListener);
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.colorAccent
        );
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MobclickAgent.onEvent(mActivity, UMengEvent.img_list_fresh);
                        mEndlessListener.setLoadMoreFlag(false);
                        getFangs(1);
                    }
                }, 1000);
            }
        });
    }


    /**
     * 延时加载下一页，同时显示SwipeRefresh
     *
     * @param currentPage currentPage
     */
    private void delayLoadMoreData(final int currentPage) {
        showLoadingView(true);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getFangs(currentPage);
            }
        }, 1000);
    }


    private void initListener() {
        mAdapter.setOnItemClickListener(this);
    }

    boolean isContain = false;
    private boolean mIsHasData = true;

    private void getFangs(final int currentPage) {
        CatchUtil.getInstance().releaseMemory(false);
        ImageApi aip = ImageService.getInstance().getImageList();
        String path = "image_" + currentPage + ".json";
        Observable<List<String>> observable = aip.getImageList(path);
        mSubscription = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<String>>() {
                    @Override
                    public void onCompleted() {
                        if (currentPage == 1) {
                            mEndlessListener.setLoadMoreFlag(true);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showLoadingView(false);
                        showEmptyView(false);
                        setSuccessFlag(false);
                        setZeroFlag(false);
                        if (currentPage == 1) {
                            mEndlessListener.setLoadMoreFlag(true);
                            if (mAdapter.getDataSize() == 0) {
                                inflateErrorStubIfNeeded();
                            }
                        }
                        mAdapter.changeMoreStatus(ImageAdapter.NO_LOAD_ERROR);
                        CrashReport.postCatchedException(e);
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<String> list) {
                        showLoadingView(false);
                        showErrorView(false);
                        setSuccessFlag(true);
                        if (list == null) return;

                        if (currentPage == 1) {
                            if (list.size() == 0) {
                                inflateEmptyStubIfNeeded();
                            } else {
                                showEmptyView(false);
                            }

                            mSubscription2 = createObservable((ArrayList<String>) list, mAdapter.getData())
                                    .subscribeOn(Schedulers.computation())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Subscriber<Boolean>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }

                                        @Override
                                        public void onNext(Boolean aBoolean) {
                                            isContain = aBoolean;
                                        }
                                    });
                            if (!isContain) {
                                mEndlessListener.resetCurrentPage();
                                mAdapter.clearData();
                            } else {
                                return;
                            }

                        } else {
                            setZeroFlag(list.size() == 0);
                            showEmptyView(false);
                        }
                        mAdapter.addData(list);
                        //设置回到上拉加载更多
                        if (list.size() == 0) {
                            mIsHasData = false;
                            mAdapter.changeMoreStatus(ImageAdapter.NO_LOAD_MORE);
                        } else {
                            mIsHasData = true;
                            mAdapter.changeMoreStatus(ImageAdapter.LOADING_MORE);
                        }
                    }
                });
    }


    private void setSuccessFlag(boolean isSuccess) {
        if (mEndlessListener == null) return;
        mEndlessListener.setErrorFlag(isSuccess);
    }

    private void setZeroFlag(boolean isZero) {
        if (mEndlessListener == null) return;
        mEndlessListener.setZeroFlag(isZero);
    }

    private void inflateErrorStubIfNeeded() {
        if (mLayoutError == null && mStubError != null) {
            mLayoutError = mStubError.inflate();
            mLayoutError.setVisibility(View.VISIBLE);
            mLayoutError.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mLayoutError.setVisibility(View.GONE);
                    if (mAdapter != null) {
                        mAdapter.clearData();
                    }
                    initLoad();
                }
            });
        } else {
            if (mLayoutError != null) {
                mLayoutError.setVisibility(View.VISIBLE);
            }
        }
    }

    private void inflateEmptyStubIfNeeded() {
        if (mLayoutEmpty == null && mStubEmpty != null) {
            mLayoutEmpty = mStubEmpty.inflate();
            mLayoutEmpty.setVisibility(View.VISIBLE);
        } else {
            if (mLayoutEmpty != null) {
                mLayoutEmpty.setVisibility(View.VISIBLE);
            }
        }
        mLayoutEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEmptyView(false);
                initLoad();
            }
        });
    }

    private void initLoad() {
        showLoadingView(true);
        if (mHandler == null) return;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getFangs(1);
            }
        }, 1000);
    }

    private void showLoadingView(final boolean isShow) {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(isShow);
                }
            });
        }
    }

    private void showErrorView(boolean isShow) {
        if (mLayoutError != null) {
            mLayoutError.setVisibility(isShow ? View.VISIBLE : View.GONE);
        }
    }

    private void showEmptyView(boolean isShow) {
        if (mLayoutEmpty != null) {
            mLayoutEmpty.setVisibility(isShow ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        DestroyCleanUtil.fixInputMethod(mActivity);
        DestroyCleanUtil.fixTextLineCacheLeak();
        CatchUtil.getInstance().releaseMemory(true);
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        RxUtils.unSubscribeIfNotNull(mSubscription);
        RxUtils.unSubscribeIfNotNull(mSubscription2);

        View rootView = mRootView.findViewById(R.id.fl_root_image_list_layout);
        DestroyCleanUtil.unBindView(rootView);

        if (mAdapter != null) {
            mAdapter.destroyData();
        }
        super.onDestroy();
    }

    public interface itemClickListener {
        void itemClickListener(int position, ArrayList<String> list);
    }

    private itemClickListener mItemClickListener;

    public void setOnItemClickListener(itemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    @Override
    public void itemOnClick(int position) {
        if (mActivity == null || mActivity.isFinishing()) return;
        mItemClickListener.itemClickListener(position, mAdapter.getData());
    }

    public Observable<Boolean> createObservable(final ArrayList<String> list1, final ArrayList<String> list2) {
        return Observable.defer(new Func0<Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call() {
                return Observable.just(isContain(list1, list2));
            }
        });
    }

    /**
     * 判断集合1是否包含在集合2中
     *
     * @param list1 ArrayList1
     * @param list2 ArrayList2
     * @return boolean
     */
    private boolean isContain(ArrayList<String> list1, ArrayList<String> list2) {
        String url;
        int num = 0;
        for (int i = 0; i < list1.size(); i++) {
            url = list1.get(i);
            for (int j = 0; j < list2.size(); j++) {
                String url2 = list2.get(j);
                if (url != null && url2 != null && url.equals(url2)) {
                    num++;
                }
            }
        }
        return num == list1.size();
    }

    public void scrollToTop() {
        if (mRecyclerView == null) return;
        int[] lastVisibleItemPositions = mStaggeredGridLayoutManager.findFirstVisibleItemPositions(null);
        if (lastVisibleItemPositions != null && lastVisibleItemPositions.length > 0) {
            int firstVisibleItemPosition = lastVisibleItemPositions[0];
            int mVisibleCount = mStaggeredGridLayoutManager.getItemCount();
            if (firstVisibleItemPosition > mVisibleCount) {
                mRecyclerView.scrollToPosition(mVisibleCount);
            }
            mRecyclerView.smoothScrollToPosition(0);
        }
    }

    public void scrollPosition(int pos) {
        if (mRecyclerView == null) return;
        mRecyclerView.scrollToPosition(pos);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("ImageListFragment");
    }

    @Override
    public void onPause() {
        CatchUtil.getInstance().releaseMemory(true);
        super.onPause();
        MobclickAgent.onPageEnd("ImageListFragment");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RefWatcher refWatcher = MApplication.getRefWatcher();
        if (refWatcher == null) return;
        refWatcher.watch(this);
    }
}
