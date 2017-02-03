package meizhi.meizhi.malin.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.umeng.analytics.MobclickAgent;

import meizhi.meizhi.malin.R;
import meizhi.meizhi.malin.fragment.ImageListFragment;

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
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageListFragment mImageListFragment;
    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNavigationBarColor();
        setContentView(R.layout.activity_main);

        MobclickAgent.setDebugMode(true);
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.enableEncrypt(true);

        initView();
        initListener();
        initToolBar();
        setDefaultFragment();
    }

    private void initListener() {
        findViewById(R.id.tv_content).setOnClickListener(this);
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
            case R.id.tv_content: {
                if (mImageListFragment != null) {
                    //mImageListFragment.scrollToTop();
                    startActivity(new Intent(this, AboutActivity.class));
                }
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
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
