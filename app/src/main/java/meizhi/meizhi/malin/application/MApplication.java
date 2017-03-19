package meizhi.meizhi.malin.application;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;

import c.b.BP;
import meizhi.meizhi.malin.BuildConfig;
import meizhi.meizhi.malin.utils.AppInfoUtil;
import meizhi.meizhi.malin.utils.CatchUtil;
import meizhi.meizhi.malin.utils.ImageLoaderConfig;
import meizhi.meizhi.malin.utils.ProcessUtil;

/**
 * 类描述:
 * 创建人:malin.myemail@163.com
 * 创建时间:16-5-21 19:31
 * 备注:{@link }
 * 修改人:
 * 修改时间:
 * 修改备注:
 * 版本:
 * 参考：http://blog.csdn.net/guolin_blog/article/details/47028975
 */
public class MApplication extends Application {

    private static RefWatcher mRefWatcher;
    private static final String PAY_KEY = "7f04009c2a16ecfc280f10a691a8cce1";
    private static final String BUGLY_KEY = "ee6ea51102";


    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        if (ProcessUtil.isMainProcess()) {
            initContext();
            initFresco();
            initBugLy();
            initPay();
            initLeakCanary();
        }
    }


    public static Context getContext() {
        return mContext;
    }

    private void initContext() {
        mContext = getApplicationContext();
    }

    private void initFresco() {
        Fresco.initialize(this, ImageLoaderConfig.getInstance().getImagePipelineConfig(getApplicationContext()));
    }

    private void initBugLy() {
        try {
            CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getApplicationContext());
            strategy.setAppChannel(AppInfoUtil.getChannelName());  //设置渠道
            strategy.setAppVersion(AppInfoUtil.getAppVersionName()); //App的版本
            strategy.setAppPackageName(AppInfoUtil.getPackageName());  //App的包名
            strategy.setAppReportDelay(10000);   //BugLy会在启动10s后联网同步数据
            Bugly.init(getApplicationContext(), BUGLY_KEY, BuildConfig.LOG_DEBUG, strategy);
        } catch (Throwable e) {
            CrashReport.postCatchedException(e);
            e.printStackTrace();
        }
    }

    private void initPay() {
        BP.init(PAY_KEY);
    }

    private void initLeakCanary() {
        mRefWatcher = LeakCanary.install(this);
    }

    public static RefWatcher getRefWatcher() {
        return mRefWatcher;
    }

    //指导应用程序在不同的情况下进行自身的内存释放，以避免被系统直接杀掉，提高应用程序的用户体验.
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        CatchUtil.getInstance().releaseMemory(true);
        CatchUtil.getInstance().clearCacheDiskSelf();
    }

}
