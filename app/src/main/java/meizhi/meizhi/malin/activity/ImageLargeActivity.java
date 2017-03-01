package meizhi.meizhi.malin.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import meizhi.meizhi.malin.R;
import meizhi.meizhi.malin.adapter.ImageLargeAdapter;
import meizhi.meizhi.malin.utils.CatchUtil;
import meizhi.meizhi.malin.utils.ImageDownLoadUtil;
import meizhi.meizhi.malin.utils.LogUtil;
import meizhi.meizhi.malin.utils.RxUtils;
import rx.Subscription;

/**
 * 类描述:
 * 创建人:malin.myemail@163.com
 * 创建时间:2017/02/13 20:13
 * 备注:{@link }
 * 修改人:
 * 修改时间:
 * 修改备注:
 * 版本:
 */
public class ImageLargeActivity extends AppCompatActivity implements ImageLargeAdapter.itemClickListener,
        ImageLargeAdapter.downLoadClickListener, ImageDownLoadUtil.downLoadListener {

    private int mPosition;
    private Window mWindow;
    private View mDecorView;
    private Context mContext;
    private ImageLargeAdapter mAdapter;
    private ImageDownLoadUtil mImageDownLoadUtil;
    private Subscription mSubscription;
    private RecyclerView mRecyclerView;
    private ArrayList<String> mList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView();
        setStatusBarColor();
        setContentView(R.layout.image_large_activity);
        initData();
        initView();
        initSetData();
    }


    /**
     * RecyclerView滑动监听，可以获取当前显示View的Position。
     */
    private void initListener() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int first = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                    int currentIndex = first;
                    LogUtil.d("initListener","currentIndex:"+currentIndex);
                }
            }
        });
    }


    private void initContentView() {
        mWindow = getWindow();
        if (mWindow == null) this.finish();
        mDecorView = mWindow.getDecorView();
        if (mDecorView == null) this.finish();
    }

    private void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (mWindow == null) return;
            int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            mDecorView.setSystemUiVisibility(option);
            mWindow.setStatusBarColor(Color.TRANSPARENT);
            mWindow.setNavigationBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            hideSystemUI();
        }
        super.onWindowFocusChanged(hasFocus);
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

    private void initSetData() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new ImageLargeAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new ScrollListener());
        mAdapter.addData(mList);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setDownLoadListener(this);
        mRecyclerView.scrollToPosition(mPosition);

    }

    private class ScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
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

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_large_layout);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerView);
    }


    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            mPosition = intent.getIntExtra("position", 0);
            mList = intent.getStringArrayListExtra("datas");
        }
        mImageDownLoadUtil = new ImageDownLoadUtil(this);
        mImageDownLoadUtil.setDownLoadListener(this);
        mContext = ImageLargeActivity.this;
    }

    @Override
    public void itemOnClick(String imageUrl, int position) {
        mPosition = position;
        setResultData();
        finish();
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
    public void downImageListener(String url, int position, boolean singleClickDown) {
        if (mImageDownLoadUtil == null) return;
        mSubscription = mImageDownLoadUtil.downloadFile(url, position, singleClickDown);
    }

    @Override
    public void downLoadFailure(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mContext, "" + msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void downLoadSuccessful(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mContext, "" + msg, Toast.LENGTH_SHORT).show();
            }
        });
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
        RxUtils.unSubscribeIfNotNull(mSubscription);
        super.onDestroy();
    }

}
