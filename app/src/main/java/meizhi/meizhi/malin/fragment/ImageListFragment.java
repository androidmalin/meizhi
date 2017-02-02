package meizhi.meizhi.malin.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
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

import java.util.ArrayList;

import meizhi.meizhi.malin.R;
import meizhi.meizhi.malin.activity.ImageDetailActivity;
import meizhi.meizhi.malin.adapter.ImageAdapter;
import meizhi.meizhi.malin.network.api.ImageApi;
import meizhi.meizhi.malin.network.bean.ImageBean;
import meizhi.meizhi.malin.network.bean.ImageInfo;
import meizhi.meizhi.malin.network.services.ImageService;
import meizhi.meizhi.malin.utils.EndlessRecyclerOnScrollListener;
import meizhi.meizhi.malin.utils.RxUtils;
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
    private static final int NUMBER = 10;
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
        mSwipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.xy_swipe_fans_refresh);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.xy_rooom_fans_rv);
        mStubError = (ViewStub) mRootView.findViewById(R.id.view_stub_error);
        mStubEmpty = (ViewStub) mRootView.findViewById(R.id.view_stub_empty);
    }


    private void initData() {
        mActivity = getActivity();
        mAdapter = new ImageAdapter(mActivity, ImageListFragment.this);
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
        mEndlessListener = new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore(final int currentPage) {
                mEndlessListener.setLoadMoreFlag(true);
                getFangs(currentPage);
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

    private void getFangs(final int currentPage) {
        ImageApi aip = ImageService.getInstance().getLogin();
        Observable<ImageInfo> observable = aip.getkey(NUMBER, currentPage);
        mSubscription = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ImageInfo>() {
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
                            inflateErrorStubIfNeeded();
                        }
                        //TODO：上报错误
                    }

                    @Override
                    public void onNext(ImageInfo imageInfo) {
                        showLoadingView(false);
                        showErrorView(false);
                        setSuccessFlag(true);
                        if (imageInfo == null || imageInfo.results == null) return;

                        if (currentPage == 1) {
                            if (imageInfo.results.size() == 0) {
                                inflateEmptyStubIfNeeded();
                            } else {
                                showEmptyView(false);
                            }

                            mSubscription2 = createObservable(imageInfo.results, mAdapter.getData())
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
//                            if (!isContain(imageInfo.results, mAdapter.getData())) {
//                                mAdapter.clearData();
//                            } else {
//                                Toast.makeText(mActivity, "暂无新数据！", Toast.LENGTH_SHORT).show();
//                                return;
//                            }

                            if (!isContain) {
                                mEndlessListener.resetCurrentPage();
                                mAdapter.clearData();
                            } else {
                                return;
                            }

                        } else {
                            setZeroFlag(imageInfo.results.size() == 0);
                            showEmptyView(false);
                        }
                        mAdapter.addData(imageInfo.results);
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
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        RxUtils.unSubscribeIfNotNull(mSubscription);
        RxUtils.unSubscribeIfNotNull(mSubscription2);
    }

    @Override
    public void itemOnClick(String imageUrl, int position) {
        if (mActivity == null || mActivity.isFinishing()) return;
        try {
            Intent intent = new Intent(mActivity, ImageDetailActivity.class);
            intent.putExtra("position", position);
            intent.putParcelableArrayListExtra("datas", mAdapter.getData());
            mActivity.startActivity(intent);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public Observable<Boolean> createObservable(final ArrayList<ImageBean> list1, final ArrayList<ImageBean> list2) {
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
    private boolean isContain(ArrayList<ImageBean> list1, ArrayList<ImageBean> list2) {
        String url;
        int num = 0;
        for (int i = 0; i < list1.size(); i++) {
            url = list1.get(i).url;
            for (int j = 0; j < list2.size(); j++) {
                String url2 = list2.get(j).url;
                if (url != null && url2 != null && url.equals(url2)) {
                    num++;
                }
            }
        }
        return num == list1.size();
    }

}
