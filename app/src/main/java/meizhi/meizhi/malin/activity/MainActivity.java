package meizhi.meizhi.malin.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import meizhi.meizhi.malin.BuildConfig;
import meizhi.meizhi.malin.R;
import meizhi.meizhi.malin.fragment.ImageListFragment;
import meizhi.meizhi.malin.network.bean.ImageBean;
import meizhi.meizhi.malin.utils.GlideCatchUtil;
import meizhi.meizhi.malin.utils.UMengEvent;

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
public class MainActivity extends AppCompatActivity implements View.OnClickListener, ImageListFragment.itemClickListener {

    private ImageListFragment mImageListFragment;
    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNavigationBarColor();
        setContentView(R.layout.activity_main);

        MobclickAgent.setDebugMode(BuildConfig.DEBUG);
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.enableEncrypt(true);

        initView();
        initListener();
        initToolBar();
        setDefaultFragment();
    }

    private void initListener() {
        findViewById(R.id.tv_about_content).setOnClickListener(this);
        findViewById(R.id.view_top).setOnClickListener(this);
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
    }


    private void setNavigationBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
    }

    private void setDefaultFragment() {
        if (mImageListFragment == null) {
            Bundle bundle = new Bundle();
            mImageListFragment = ImageListFragment.newInstance(bundle);
        }
        mImageListFragment.setOnItemClickListener(this);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_content_layout, mImageListFragment, "ImageListFragment");
        fragmentTransaction.commit();
    }

    private void initToolBar() {
        if (mToolbar != null) {
            mToolbar.setTitle("");
            mToolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.transparent));//标题颜色
            mToolbar.setSubtitle("");
            mToolbar.setSubtitleTextColor(ContextCompat.getColor(this, android.R.color.transparent));//副标题颜色
            mToolbar.setLogo(null);
            mToolbar.setNavigationIcon(null);//导航图标,最左边的图标
            setSupportActionBar(mToolbar);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.tv_about_content: {
            MobclickAgent.onEvent(this, UMengEvent.ClickAbout);
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
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        GlideCatchUtil.getInstance().releaseMemory(true);
        super.onPause();
        MobclickAgent.onPause(this);
    }

    //存储时间的数组
    private long[] mHits = new long[2];

    /**
     * 双击事件响应
     */
    public void doubleClick() {
        /**
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
            MobclickAgent.onEvent(this, UMengEvent.DoubleClickTop);
            mImageListFragment.scrollToTop();
        }
    }

    @Override
    public void itemClickListener(int position, ArrayList<ImageBean> list) {
        try {
            MobclickAgent.onEvent(this, UMengEvent.ClickImageToBigImage);
            Intent intent = new Intent(this, ImageDetailActivity.class);
            intent.putExtra("position", position);
            intent.putParcelableArrayListExtra("datas", list);
            startActivityForResult(intent, 1000);
        } catch (Throwable e) {
            CrashReport.postCatchedException(e);
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        GlideCatchUtil.getInstance().releaseMemory(true);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) return;
        switch (resultCode) {
        case 2000: {
            int position = data.getIntExtra("currentPosition", -1);
            if (position != -1) {
                if (mImageListFragment == null) return;
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
}
