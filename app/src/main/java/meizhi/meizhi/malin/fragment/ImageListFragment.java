package meizhi.meizhi.malin.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import meizhi.meizhi.malin.R;
import meizhi.meizhi.malin.adapter.ImageAdapter;
import meizhi.meizhi.malin.network.api.ImageApi;
import meizhi.meizhi.malin.network.services.ImageService;
import meizhi.meizhi.malin.utils.CatchUtil;
import meizhi.meizhi.malin.utils.DestroyCleanUtil;
import meizhi.meizhi.malin.utils.EndlessRecyclerOnScrollListener;
import meizhi.meizhi.malin.utils.RxUtils;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
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
    private static final String TAG = "Life";
    private static final String TAG1 = ImageListFragment.class.getSimpleName();
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
    private Subscription mSubscription;
    private Subscription mSubscription2;
    private GridLayoutManager mStaggeredGridLayoutManager;
    private View mRootView;

    public static ImageListFragment newInstance() {
        return new ImageListFragment();
    }


    @Override
    public void onAttach(Context context) {
        Log.d(TAG, TAG1 + "[onAttach] BEGIN");
        super.onAttach(context);
        Log.d(TAG, TAG1 + "[onAttach] END");
        //如果要获取Activity对象，不建议调用getActivity()，而是在onAttach()中将Context对象强转为Activity对象
        mActivity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //返回Fragment的UI布局，需要注意的是inflate()的第三个参数是false，因为在Fragment内部实现中，会把该布局添加到container中，如果设为true，那么就会重复做两次添加，则会抛如下异常
        Log.d(TAG, TAG1 + "[onCreateView]");
        mRootView = inflater.inflate(R.layout.image_list_layout, container, false);
        initView();
        initData();
        initListener();
        initLoad();
        return mRootView;
    }


    private void initView() {
        mSwipeRefreshLayout = mRootView.findViewById(R.id.img_swipe_refresh);
        mRecyclerView = mRootView.findViewById(R.id.img_list_rv);
        mStubError = mRootView.findViewById(R.id.view_stub_error);
        mStubEmpty = mRootView.findViewById(R.id.view_stub_empty);
    }


    private void initData() {
        mActivity = getActivity();
        mAdapter = new ImageAdapter(mActivity);
        mStaggeredGridLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);

        mRecyclerView.setAdapter(mAdapter);
        mEndlessListener = new EndlessRecyclerOnScrollListener(mStaggeredGridLayoutManager) {
            @Override
            public void onLoadMore(final int currentPage) {
                mEndlessListener.setLoadMoreFlag(true);
                //设置正在加载更多
                if (mIsHasData) {
                    mAdapter.changeMoreStatus(ImageAdapter.LOADING_MORE);
                    getFangs(currentPage);
                }
                //delayLoadMoreData(currentPage);
            }
        };
        mRecyclerView.addOnScrollListener(mEndlessListener);
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.colorAccent
        );
        mSwipeRefreshLayout.setOnRefreshListener(() -> mHandler.postDelayed(() -> {
            mEndlessListener.setLoadMoreFlag(false);
            getFangs(1);
        }, 1000));
    }


    /**
     * 延时加载下一页，同时显示SwipeRefresh
     *
     * @param currentPage currentPage
     */
    private void delayLoadMoreData(final int currentPage) {
        showLoadingView(true);
        mHandler.postDelayed(() -> getFangs(currentPage), 1000);
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
            mLayoutError.setOnClickListener(v -> {
                mLayoutError.setVisibility(View.GONE);
                if (mAdapter != null) {
                    mAdapter.clearData();
                }
                initLoad();
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
        if (mLayoutEmpty != null) {
            mLayoutEmpty.setOnClickListener(v -> {
                showEmptyView(false);
                initLoad();
            });
        }
    }

    private void initLoad() {
        showLoadingView(true);
        if (mHandler == null) return;
        mHandler.postDelayed(() -> getFangs(1), 1000);
    }

    private void showLoadingView(final boolean isShow) {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(isShow));
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
        Log.d(TAG, TAG1 + "[onDestroy] BEGIN");
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
        Log.d(TAG, TAG1 + "[onDestroy] END");
    }

    public interface itemClickListener {
        void onItemClick(int position, ArrayList<String> list);
    }

    private itemClickListener mItemClickListener;

    public void setOnItemClickListener(itemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    @Override
    public void itemOnClick(int position) {
        if (mActivity == null || mActivity.isFinishing()) return;
        mItemClickListener.onItemClick(position, mAdapter.getData());
    }

    public Observable<Boolean> createObservable(final ArrayList<String> list1, final ArrayList<String> list2) {
        return Observable.defer(() -> Observable.just(isContain(list1, list2)));
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
//        if (mRecyclerView == null) return;
//        int[] lastVisibleItemPositions = mStaggeredGridLayoutManager.findFirstVisibleItemPositions(null);
//        if (lastVisibleItemPositions != null && lastVisibleItemPositions.length > 0) {
//            int firstVisibleItemPosition = lastVisibleItemPositions[0];
//            int mVisibleCount = mStaggeredGridLayoutManager.getItemCount();
//            if (firstVisibleItemPosition > mVisibleCount) {
//                mRecyclerView.scrollToPosition(mVisibleCount);
//            }
//            mRecyclerView.smoothScrollToPosition(0);
//        }
    }

    public void scrollPosition(int pos) {
        if (mRecyclerView == null) return;
        mRecyclerView.scrollToPosition(pos);
    }


    @Override
    public void onPause() {
        CatchUtil.getInstance().releaseMemory(true);
        Log.d(TAG, TAG1 + "[onPause] BEGIN");
        super.onPause();
        Log.d(TAG, TAG1 + "[onPause] END");
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, TAG1 + "[onDestroyView] BEGIN");
        super.onDestroyView();
        Log.d(TAG, TAG1 + "[onDestroyView] END");
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, TAG1 + "[onCreate] BEGIN");
        super.onCreate(savedInstanceState);
        Log.d(TAG, TAG1 + "[onCreate] END");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, TAG1 + "[onViewCreated] BEGIN");
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, TAG1 + "[onViewCreated] END");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, TAG1 + "[onActivityCreated] BEGIN");
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, TAG1 + "[onActivityCreated] END");
    }

    @Override
    public void onStart() {
        Log.d(TAG, TAG1 + "[onStart] BEGIN");
        super.onStart();
        Log.d(TAG, TAG1 + "[onStart] END");
    }

    @Override
    public void onResume() {
        Log.d(TAG, TAG1 + "[onResume] BEGIN");
        super.onResume();
        Log.d(TAG, TAG1 + "[onResume] END");
    }


    @Override
    public void onStop() {
        Log.d(TAG, TAG1 + "[onStop] BEGIN");
        super.onStop();
        Log.d(TAG, TAG1 + "[onStop] END");
    }


    @Override
    public void onDetach() {
        Log.d(TAG, TAG1 + "[onDetach] BEGIN");
        super.onDetach();
        Log.d(TAG, TAG1 + "[onDetach] END");
    }
}
