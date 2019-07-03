package meizhi.meizhi.malin.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;
import meizhi.meizhi.malin.R;
import meizhi.meizhi.malin.fragment.ImageListFragment;
import meizhi.meizhi.malin.utils.CatchUtil;
import meizhi.meizhi.malin.utils.DestroyCleanUtil;

/**
 * 类描述:
 * 创建人:malin.myemail@163.com
 * 创建时间:2017/01/31 18:21
 * 备注:{@link }
 * 修改人:
 * 修改时间:
 * 修改备注:
 * 版本:
 * https://www.jianshu.com/p/28ca4cbe190c
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, ImageListFragment.itemClickListener {

    private static final String TAG = "FFFF";
    private static final String TAG1 = MainActivity.class.getSimpleName();
    private ImageListFragment mImageListFragment;
    private Toolbar mToolbar;
    private Activity mActivity;
    private static final String IMAGE_FRAGMENT_KEY = "ImageListFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        Log.d(TAG, TAG1 + "[onCreate] BEGIN");
        setNavigationBarColor();
        setContentView(R.layout.activity_main);
        initView();
        initToolBar();
        initListener();
        initFragment(savedInstanceState);
        Log.d(TAG, TAG1 + "[onCreate] END");

    }

    private void initFragment(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mImageListFragment = (ImageListFragment) getSupportFragmentManager().getFragment(savedInstanceState, IMAGE_FRAGMENT_KEY);
            mImageListFragment.setOnItemClickListener(this);
        } else {
            setDefaultFragment();
        }
    }

    private void setDefaultFragment() {
        mImageListFragment = ImageListFragment.newInstance();
        mImageListFragment.setOnItemClickListener(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_content_layout, mImageListFragment, "ImageListFragment");
        fragmentTransaction.commitAllowingStateLoss();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mImageListFragment != null) {
            getSupportFragmentManager().putFragment(outState, IMAGE_FRAGMENT_KEY, mImageListFragment);
        }
    }

    private void setNavigationBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            if (window == null) return;
            // Translucent status bar
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void initView() {
        mToolbar = findViewById(R.id.toolbar);
        mActivity = MainActivity.this;
    }

    private void initListener() {
        findViewById(R.id.tv_about_content).setOnClickListener(this);
        findViewById(R.id.view_top).setOnClickListener(this);
    }

    private void initToolBar() {
        if (mToolbar != null) {
            mToolbar.setTitle("");
            mToolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.transparent));//标题颜色
            mToolbar.setSubtitle("");
            mToolbar.setSubtitleTextColor(ContextCompat.getColor(this, android.R.color.transparent));//副标题颜色
            mToolbar.setLogo(null);
            mToolbar.setNavigationIcon(null);
            setSupportActionBar(mToolbar);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_about_content: {
                startActivity(new Intent(this, AboutActivity.class));
                break;
            }

            case R.id.view_top: {
                doubleClick();
                break;
            }

            default: {
                break;
            }
        }
    }


    @Override
    public void onPause() {
        Log.d(TAG, TAG1 + "[onPause] BEGIN");
        super.onPause();
        Log.d(TAG, TAG1 + "[onPause] END");
        CatchUtil.getInstance().releaseMemory(true);
    }

    //存储时间的数组
    private long[] mHits = new long[2];

    /**
     * 双击事件响应
     */
    public void doubleClick() {
        /*
         * arraycopy,拷贝数组
         * src 要拷贝的源数组
         * srcPos 源数组开始拷贝的下标位置
         * dst 目标数组
         * dstPos 开始存放的下标位置
         * length 要拷贝的长度（元素的个数）
         */
        //实现数组的移位操作，点击一次，左移一位，末尾补上当前开机时间（cpu的时间）
        System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
        mHits[mHits.length - 1] = SystemClock.uptimeMillis();
        //双击事件的时间间隔500ms
        if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
            //双击后具体的操作
            if (mImageListFragment == null) return;
            mImageListFragment.scrollToTop();
        }
    }

    @Override
    public void onItemClick(int position, ArrayList<String> list) {
        try {
            Intent intent = new Intent(this, ImageLargeActivity.class);
            intent.putExtra("position", position);
            intent.putStringArrayListExtra("datas", list);
            startActivityForResult(intent, 1000);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, TAG1 + "[onDestroy] BEGIN");
        CatchUtil.getInstance().releaseMemory(true);
        DestroyCleanUtil.fixInputMethod(this);
        DestroyCleanUtil.fixTextLineCacheLeak();
        DestroyCleanUtil.unBindView(getWindow().getDecorView());
        super.onDestroy();
        Log.d(TAG, TAG1 + "[onDestroy] END");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) return;
        switch (resultCode) {
            case 2000: {
                int position = data.getIntExtra("currentPosition", -1);
                if (position != -1) {
                    if (mActivity == null || mActivity.isFinishing() || mImageListFragment == null)
                        return;
                    mImageListFragment.scrollPosition(position);
                }
                break;
            }

            default: {
                break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, TAG1 + "[onPostCreate] BEGIN");
        super.onPostCreate(savedInstanceState);
        Log.d(TAG, TAG1 + "[onPostCreate] END");
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        Log.d(TAG, TAG1 + "[onAttachFragment] BEGIN");
        super.onAttachFragment(fragment);
        Log.d(TAG, TAG1 + "[onAttachFragment] END");
    }

    @Override
    protected void onStart() {
        Log.d(TAG, TAG1 + "[onStart] BEGIN");
        super.onStart();
        Log.d(TAG, TAG1 + "[onStart] END");
    }

    @Override
    protected void onResume() {
        Log.d(TAG, TAG1 + "[onResume] BEGIN");
        super.onResume();
        Log.d(TAG, TAG1 + "[onResume] END");
    }

    @Override
    public void onAttachedToWindow() {
        Log.d(TAG, TAG1 + "[onAttachedToWindow] BEGIN");
        super.onAttachedToWindow();
        Log.d(TAG, TAG1 + "[onAttachedToWindow] END");
    }

    @Override
    protected void onPostResume() {
        Log.d(TAG, TAG1 + "[onPostResume] BEGIN");
        super.onPostResume();
        Log.d(TAG, TAG1 + "[onPostResume] END");
    }


    @Override
    protected void onRestart() {
        Log.d(TAG, TAG1 + "[onRestart] BEGIN");
        super.onRestart();
        Log.d(TAG, TAG1 + "[onRestart] END");
    }

    @Override
    protected void onStop() {
        Log.d(TAG, TAG1 + "[onStop] BEGIN");
        super.onStop();
        Log.d(TAG, TAG1 + "[onStop] END");
    }
}
