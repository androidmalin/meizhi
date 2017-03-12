package meizhi.meizhi.malin.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;

import c.b.BP;
import c.b.PListener;
import c.b.QListener;
import meizhi.meizhi.malin.BuildConfig;
import meizhi.meizhi.malin.R;
import meizhi.meizhi.malin.utils.AppInfoUtil;
import meizhi.meizhi.malin.utils.DestroyCleanUtil;
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

public class AboutActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNavigationBarColor();
        setContentView(R.layout.about_layout);
        initView();
        initPayView();
        initPayListener();
        initToolBar();
    }


    private void initView() {

        TextView versionName = (TextView) findViewById(R.id.tv_app_version_name);
        findViewById(R.id.rl_git_log).setOnClickListener(this);
        findViewById(R.id.rl_about_back).setOnClickListener(this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_about);

        findViewById(R.id.tv_app_star).setOnClickListener(this);
        findViewById(R.id.tv_git).setOnClickListener(this);

        initData(versionName);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("小姐姐");
    }

    private void setNavigationBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//21
            if (getWindow() == null) return;
            getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
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
        textView.setText(AppInfoUtil.getAppVersionName());
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

            case R.id.tv_app_star: {
                MobclickAgent.onEvent(this, UMengEvent.ClickToAppStore);
                AppInfoUtil.launchAppDetail(this, "");
                break;
            }

            case R.id.rl_about_back: {
                this.finish();
                break;
            }

            case R.id.r_btn_one_rmb: {
                pay(BuildConfig.LOG_DEBUG ? 0.01 : 1);
                break;
            }
            case R.id.r_btn_two_rmb: {
                pay(BuildConfig.LOG_DEBUG ? 0.01 : 2);
                break;
            }
            case R.id.r_btn_four_rmb: {
                pay(BuildConfig.LOG_DEBUG ? 0.01 : 4);
                break;
            }
            case R.id.r_btn_ten_rmb: {
                pay(BuildConfig.LOG_DEBUG ? 0.01 : 10);
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
        DestroyCleanUtil.fixInputMethod(this);
        DestroyCleanUtil.unBindView(getWindow().getDecorView());
        super.onDestroy();
    }


    private String mOrderId;

    private RadioGroup mPriceGroup;
    private RadioButton mRtnOne;
    private RadioButton mRtnTwo;
    private RadioButton mRtnFour;
    private RadioButton mRtnTen;

    private ProgressDialog mProgressDialog;

    private void initPayView() {
        mPriceGroup = (RadioGroup) findViewById(R.id.rg_price);
        mRtnOne = (RadioButton) findViewById(R.id.r_btn_one_rmb);
        mRtnTwo = (RadioButton) findViewById(R.id.r_btn_two_rmb);
        mRtnFour = (RadioButton) findViewById(R.id.r_btn_four_rmb);
        mRtnTen = (RadioButton) findViewById(R.id.r_btn_ten_rmb);
    }

    private void initPayListener() {
        mPriceGroup.setOnCheckedChangeListener(this);
        mRtnOne.setOnClickListener(this);
        mRtnTwo.setOnClickListener(this);
        mRtnFour.setOnClickListener(this);
        mRtnTen.setOnClickListener(this);
    }

    /**
     * 调用支付
     */
    private void pay(double money) {
        if (!checkPackageInstalled("com.eg.android.AlipayGphone", "https://www.alipay.com")) { // 支付宝支付要求用户已经安装支付宝客户端
            Toast.makeText(AboutActivity.this, "请安装支付宝客户端", Toast.LENGTH_SHORT).show();
            return;
        }
        showDialog("正在获取订单...");
        BP.pay("打赏应用", "打赏应用", money, true, new PListener() {
            // 因为网络等原因,支付结果未知(小概率事件),出于保险起见稍后手动查询
            @Override
            public void unknow() {
                query();
                hideDialog();
            }

            // 支付成功,如果金额较大请手动查询确认
            @Override
            public void succeed() {
                query();
                hideDialog();
            }

            // 无论成功与否,返回订单号
            @Override
            public void orderId(String orderId) {
                // 此处应该保存订单号,比如保存进数据库等,以便以后查询
                mOrderId = orderId;
                showDialog("获取订单成功!请等待跳转到支付页面~");
            }

            // 支付失败,原因可能是用户中断支付操作,也可能是网络原因
            @Override
            public void fail(int code, String reason) {
                // 当code为-2,意味着用户中断了操作
                if (code == -2) {
                    Toast.makeText(AboutActivity.this, "支付中断!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AboutActivity.this, "" + reason, Toast.LENGTH_SHORT).show();
                }
                hideDialog();
            }
        });
    }

    // 执行订单查询
    private void query() {
        showDialog("正在查询支付情况...");
        BP.query(mOrderId, new QListener() {
            @Override
            public void succeed(String status) {
                Toast.makeText(AboutActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                hideDialog();
            }

            @Override
            public void fail(int code, String reason) {
                Toast.makeText(AboutActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
                hideDialog();
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (group.getId()) {
            case R.id.rg_price: {
                switch (checkedId) {
                    case R.id.r_btn_one_rmb: {
                        mRtnOne.setTextColor(ContextCompat.getColor(this, android.R.color.white));
                        mRtnTwo.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                        mRtnFour.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                        mRtnTen.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                        break;
                    }

                    case R.id.r_btn_two_rmb: {
                        mRtnTwo.setTextColor(ContextCompat.getColor(this, android.R.color.white));
                        mRtnOne.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                        mRtnFour.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                        mRtnTen.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                        break;
                    }

                    case R.id.r_btn_four_rmb: {
                        mRtnFour.setTextColor(ContextCompat.getColor(this, android.R.color.white));
                        mRtnOne.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                        mRtnTwo.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                        mRtnTen.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                        break;
                    }

                    case R.id.r_btn_ten_rmb: {
                        mRtnTen.setTextColor(ContextCompat.getColor(this, android.R.color.white));
                        mRtnOne.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                        mRtnTwo.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                        mRtnFour.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                        break;
                    }
                }
                break;
            }
        }
    }

    private void showDialog(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mProgressDialog == null) {
                        mProgressDialog = new ProgressDialog(AboutActivity.this);
                        mProgressDialog.setCancelable(true);
                    }
                    mProgressDialog.setMessage(message);
                    mProgressDialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void hideDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mProgressDialog == null || !mProgressDialog.isShowing()) return;
                try {
                    mProgressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 检查某包名应用是否已经安装
     *
     * @param packageName 包名
     * @param browserUrl  如果没有应用市场，去官网下载
     * @return boolean 是否有支付宝客户端
     */
    private boolean checkPackageInstalled(String packageName, String browserUrl) {
        try {
            // 检查是否有支付宝客户端
            getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            // 没有安装支付宝，跳转到应用市场
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=" + packageName));
                startActivity(intent);
            } catch (Throwable ee) {// 连应用市场都没有，用浏览器去支付宝官网下载
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(browserUrl));
                    startActivity(intent);
                } catch (Throwable eee) {
                    Toast.makeText(AboutActivity.this, "您的手机上没有应用市场也没有浏览器", Toast.LENGTH_SHORT).show();
                }
            }
        }
        return false;
    }
}
