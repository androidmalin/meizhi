package meizhi.meizhi.malin.application;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;

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

    public RefWatcher mRefWatcher;

    private static MApplication application;

    public static MApplication getInstance() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        if (ProcessUtil.isMainProcess()) {
            initFresco();
            initBugLy();
            initLeakCanary();
        }
    }

    /**
     * BugLy初始化
     */
    private void initBugLy() {
        try {
            CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getApplicationContext());
            strategy.setAppChannel(AppInfoUtil.getChannelName(this));  //设置渠道
            strategy.setAppVersion(AppInfoUtil.getAppVersionName(this)); //App的版本
            strategy.setAppPackageName(AppInfoUtil.getPackageName());  //App的包名
            strategy.setAppReportDelay(10000);   //BugLy会在启动10s后联网同步数据
            Bugly.init(getApplicationContext(), "ee6ea51102", BuildConfig.LOG_DEBUG, strategy);
        } catch (Throwable e) {
            CrashReport.postCatchedException(e);
            e.printStackTrace();
        }
    }

    private void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        mRefWatcher = LeakCanary.install(this);
    }

    private void initFresco() {
        Fresco.initialize(this, ImageLoaderConfig.getInstance().getImagePipelineConfig(this));
    }


    public RefWatcher getRefWatcher(MApplication application) {
        if (application != null && mRefWatcher != null) {
            return application.mRefWatcher;
        } else {
            return LeakCanary.install(this);
        }
    }

    //指导应用程序在不同的情况下进行自身的内存释放，以避免被系统直接杀掉，提高应用程序的用户体验.
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        CatchUtil.getInstance().releaseMemory(true);
        CatchUtil.getInstance().clearCacheDiskSelf();
    }

}
