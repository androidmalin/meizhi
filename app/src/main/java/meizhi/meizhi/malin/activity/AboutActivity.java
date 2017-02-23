package meizhi.meizhi.malin.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;

import meizhi.meizhi.malin.R;
import meizhi.meizhi.malin.utils.AppInfoUtil;
import meizhi.meizhi.malin.utils.UMengEvent;

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

    private static final String URL_THANKS = "http://gank.io/api";
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
        TextView mGitLinkTV = (TextView) findViewById(R.id.tv_git);
        TextView mThanksView = (TextView) findViewById(R.id.tv_thanks);
        TextView versionName = (TextView) findViewById(R.id.tv_app_version_name);
        findViewById(R.id.rl_git_log).setOnClickListener(this);
        findViewById(R.id.rl_about_back).setOnClickListener(this);


        mToolbar = (Toolbar) findViewById(R.id.toolbar_about);

        mGitLinkTV.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mGitLinkTV.setOnClickListener(this);

        SpannableStringBuilder spanString = new SpannableStringBuilder(getResources().getString(R.string.thanks_txt));
        spanString.setSpan(new ClickEvent(ContextCompat.getColor(this, R.color.colorPrimary), true), 6, 13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mThanksView.setText(spanString);
        mThanksView.setMovementMethod(LinkMovementMethod.getInstance());

        initData(versionName);
    }

    private void setNavigationBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
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
        textView.setText(AppInfoUtil.getAppVersionName(this, getResources().getString(R.string.app_default_version)));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_git_log: {
                MobclickAgent.onEvent(this, UMengEvent.ClickGithubLogo);
                startBrowser(getResources().getString(R.string.git_mm));
                break;
            }
            case R.id.tv_git: {
                MobclickAgent.onEvent(this, UMengEvent.ClickGithubLink);
                startBrowser(getResources().getString(R.string.git_mm));
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


    private class ClickEvent extends ClickableSpan {

        private int color;
        private boolean isClick;

        public ClickEvent(int color, boolean isClick) {
            this.color = color;
            this.isClick = isClick;
        }

        @Override
        public void onClick(View view) {
            if (view != null) {
                TextView textView = null;
                if (view instanceof TextView) {
                    textView = (TextView) view;
                }
                if (textView != null) {
                    if (isClick) {
                        MobclickAgent.onEvent(AboutActivity.this, UMengEvent.ClickGankLink);
                        startBrowser(URL_THANKS);
                    }
                }
                avoidHintColor(view);
            }
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            //设定的是span超链接的文本颜色 ：动态设置
            ds.setColor(color);
            //文字下划线
            ds.setUnderlineText(false);
            ds.clearShadowLayer();
        }
    }

    /**
     * 更新点击后的背景颜色
     *
     * @param view View
     */
    private void avoidHintColor(View view) {
        if (view instanceof TextView) {
            ((TextView) view).setHighlightColor(ContextCompat.getColor(this, android.R.color.transparent));
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
            // 主动上报开发者Catch的异常 您可能会关注某些重要异常的Catch情况。
            // 我们提供了上报这类异常的接口。
            CrashReport.postCatchedException(e);
            e.printStackTrace();
            Toast.makeText(this, R.string.no_browser_tip, Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
