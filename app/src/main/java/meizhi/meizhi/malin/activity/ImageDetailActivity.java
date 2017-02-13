package meizhi.meizhi.malin.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;

import meizhi.meizhi.malin.R;
import meizhi.meizhi.malin.adapter.DepthPageTransformer;
import meizhi.meizhi.malin.adapter.ImagePagerAdapter;
import meizhi.meizhi.malin.application.MApplication;
import meizhi.meizhi.malin.network.bean.ImageBean;
import meizhi.meizhi.malin.utils.CatchUtil;
import meizhi.meizhi.malin.utils.HackyViewPager;
import meizhi.meizhi.malin.utils.ImageDownLoadUtil;
import meizhi.meizhi.malin.utils.PhoneScreenUtil;
import meizhi.meizhi.malin.utils.RxUtils;
import meizhi.meizhi.malin.utils.UMengEvent;
import rx.Subscription;

/**
 * 类描述:
 * 创建人:malin.myemail@163.com
 * 创建时间:2017/01/31 18:21
 * 备注:{@link }
 * 修改人:
 * 修改时间:
 * 修改备注:
 * 版本:
 */
public class ImageDetailActivity extends AppCompatActivity implements ImagePagerAdapter.downLoadClickListener,
        ImagePagerAdapter.photoViewTapListener, View.OnClickListener, ImagePagerAdapter.imageDownLoadListener, ImageDownLoadUtil.downLoadListener {
    private static final String TAG = ImageDetailActivity.class.getSimpleName();
    private static final String FILE_IMAGE = "0MeZhi";
    private static final int TEMP = 4 * 1024;
    private ArrayList<ImageBean> mList;
    private int mPosition;
    private Subscription mSubscription;
    private Window mWindow;
    private View mDecorView;
    private ProgressBar mProgressBar;
    private ViewStub mStubEmpty;
    private View mLayoutEmpty;
    private Context mContext;
    private ViewPager mViewPager;
    private ImageDownLoadUtil mImageDownLoadUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView();
        initStatusBarColor();
        setContentView(R.layout.activity_image_detail);
        initData();
        initViews();
        initListener();
    }

    private void initContentView() {
        mWindow = getWindow();
        if (mWindow == null) this.finish();
        mDecorView = mWindow.getDecorView();
        if (mDecorView == null) this.finish();
    }


    private void initStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (mDecorView == null) return;
            int option = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            mDecorView.setSystemUiVisibility(option);
            if (mWindow == null) return;
            mWindow.setStatusBarColor(Color.TRANSPARENT);
            mWindow.setNavigationBarColor(Color.TRANSPARENT);
        }
    }


    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            mPosition = intent.getIntExtra("position", 0);
            mList = intent.getParcelableArrayListExtra("datas");
        }
        mContext = ImageDetailActivity.this;

        mImageDownLoadUtil = new ImageDownLoadUtil(this);
        mImageDownLoadUtil.setDownLoadListener(this);
    }

    private void initViews() {
        mViewPager = (HackyViewPager) findViewById(R.id.view_pager_iv);
        mProgressBar = (ProgressBar) findViewById(R.id.pd_img);
        mStubEmpty = (ViewStub) findViewById(R.id.view_stub_img_error);
        ImagePagerAdapter adapter = new ImagePagerAdapter();
        adapter.setDownLoadListener(this);
        adapter.setPhotoViewTapListener(this);
        adapter.setProgressBarListener(this);
        adapter.setData(mList, this);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(mPosition);
        mViewPager.setPageTransformer(true, new DepthPageTransformer());
    }


    private void initListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPosition = position;
                CatchUtil.getInstance().releaseMemory(false);
                MobclickAgent.onEvent(mContext, UMengEvent.ScrollNumber);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public void viewTapListener(View view, float x, float y) {
        setResultData();
        finish();
    }

    private void showOrHideSystemUI() {
        if (mDecorView == null) return;
        if ((mDecorView.getSystemUiVisibility() & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0) {
            MobclickAgent.onEvent(mContext, UMengEvent.SingleTapImageHideUi);
            hideSystemUI();
        } else {
            MobclickAgent.onEvent(mContext, UMengEvent.SingleTapImageShowUi);
            showSystemUI();
        }
    }

    private void hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (mDecorView == null) return;
            mDecorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );

        }
    }

    private void showSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (mDecorView == null) return;
            mDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    @Override
    public void downImageListener(String url, int position, boolean singleClickDown) {
        if (mImageDownLoadUtil == null) return;
        mSubscription = mImageDownLoadUtil.downloadFile(url, position, singleClickDown);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        CatchUtil.getInstance().releaseMemory(true);
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        CatchUtil.getInstance().releaseMemory(true);
        RxUtils.unSubscribeIfNotNull(mSubscription);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        setResultData();
        super.onBackPressed();
    }

    private void setResultData() {
        Intent intent = new Intent();
        intent.putExtra("currentPosition", mPosition);
        setResult(2000, intent);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            default: {
                break;
            }
        }
    }


    @Override
    public void downLoadFailure(int position, String url) {
        if (mProgressBar != null && mProgressBar.isShown()) {
            mProgressBar.setVisibility(View.GONE);
        }
        if (mPosition == position) {
            inflateEmptyStubIfNeeded();
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("position", "" + position);
        map.put("url", url);
        MobclickAgent.onEvent(this, UMengEvent.ImageLoadError, map);
    }

    @Override
    public void downLoadSuccess(int position, String url) {
        if (mProgressBar != null && mProgressBar.isShown()) {
            mProgressBar.setVisibility(View.GONE);
        }
        if (mLayoutEmpty != null) {
            mLayoutEmpty.setVisibility(View.GONE);
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("position", "" + position);
        map.put("url", url);
        MobclickAgent.onEvent(this, UMengEvent.ImageLoadSuccess, map);
    }

    @Override
    public void downLoadPrepare(int position, String url) {
        if (mProgressBar != null) {
            if (mPosition == position) {
                mProgressBar.setVisibility(View.VISIBLE);
            }
        }
        if (mLayoutEmpty != null) {
            mLayoutEmpty.setVisibility(View.GONE);
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
        if (mLayoutEmpty == null) return;


        //android.view.ViewGroup$LayoutParams cannot be cast to android.view.ViewGroup$MarginLayoutParams
        ViewGroup.LayoutParams mLayoutParams = mLayoutEmpty.getLayoutParams();
        mLayoutParams.width = PhoneScreenUtil.getPhoneWidth(MApplication.getInstance());
        mLayoutParams.height = PhoneScreenUtil.getPhoneHeight(MApplication.getInstance());
        mLayoutEmpty.setLayoutParams(mLayoutParams);
        mLayoutEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(mContext, UMengEvent.ImageLoadErrorClickFinishPage);
                setResultData();
                finish();
            }
        });
    }

    @Override
    public void downLoadFailure(String msg) {
        Toast.makeText(mContext, "" + msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void downLoadSuccessful(String msg) {
        Toast.makeText(mContext, "" + msg, Toast.LENGTH_SHORT).show();
    }
}

