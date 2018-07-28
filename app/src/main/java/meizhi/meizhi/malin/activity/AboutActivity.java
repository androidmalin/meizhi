package meizhi.meizhi.malin.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import meizhi.meizhi.malin.R;
import meizhi.meizhi.malin.application.MApplication;
import meizhi.meizhi.malin.utils.AppInfoUtil;
import meizhi.meizhi.malin.utils.DestroyCleanUtil;

/**
 * 类描述: 关于页面
 * 创建人:malin.myemail@163.com
 * 创建时间:17-2-3.21:09
 * 备注:{@link }
 * 修改人:
 * 修改时间:
 * 修改备注:
 * 版本:
 */

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNavigationBarColor();
        setContentView(R.layout.about_layout);
        initView();
        initToolBar();
    }


    private void initView() {

        TextView versionName = findViewById(R.id.tv_app_version_name);
        findViewById(R.id.rl_git_log).setOnClickListener(this);
        findViewById(R.id.rl_about_back).setOnClickListener(this);

        mToolbar = findViewById(R.id.toolbar_about);

        findViewById(R.id.tv_app_star).setOnClickListener(this);
        findViewById(R.id.tv_git).setOnClickListener(this);
        initData(versionName);
    }

    private void setNavigationBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//21
            if (getWindow() == null) return;
            getWindow().setNavigationBarColor(ContextCompat.getColor(MApplication.getContext(), R.color.colorPrimary));
        }
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

    private void initData(TextView textView) {
        textView.setText("V" + AppInfoUtil.getAppVersionName());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_git_log: {
                startBrowser(getResources().getString(R.string.git_mm));
                break;
            }

            case R.id.tv_git: {
                startBrowser(getResources().getString(R.string.git_mm));
                break;
            }

            case R.id.tv_app_star: {
                AppInfoUtil.launchAppDetail(this, "");
                break;
            }

            case R.id.rl_about_back: {
                this.finish();
                break;
            }
            default: {
                break;
            }
        }
    }

    private void startBrowser(String url) {
        if (TextUtils.isEmpty(url)) return;
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            startActivity(intent);
        } catch (Throwable e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.no_browser_tip, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        DestroyCleanUtil.fixInputMethod(this);
        DestroyCleanUtil.fixTextLineCacheLeak();
        DestroyCleanUtil.unBindView(getWindow().getDecorView());
        super.onDestroy();
    }
}
