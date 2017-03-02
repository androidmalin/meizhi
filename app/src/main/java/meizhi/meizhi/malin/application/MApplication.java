package meizhi.meizhi.malin.application;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

import meizhi.meizhi.malin.BuildConfig;
import meizhi.meizhi.malin.R;
import meizhi.meizhi.malin.utils.AppInfoUtil;
import meizhi.meizhi.malin.utils.CatchUtil;
import meizhi.meizhi.malin.utils.ImageLoaderConfig;

/**
 * 类描述:
 * 创建人:malin.myemail@163.com
 * 创建时间:16-5-21 19:31
 * 备注:{@link }
 * 修改人:
 * 修改时间:
 * 修改备注:
 * 版本:
 */
@SuppressWarnings("ALL")
public class MApplication extends Application {

    public RefWatcher mRefWatcher;

    private static MApplication application;

    public MApplication() {
        super();
        application = this;
    }


    public static MApplication getInstance() {
        return application;
    }

    private Handler mHander = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();
        mHander.postDelayed(new Runnable() {
            @Override
            public void run() {
                String processName = getAppMainProcessName();
                if (!TextUtils.isEmpty(processName) && processName.compareToIgnoreCase(getAppPackageName()) == 0) {
                    initFresco();
                    initLeack();
                    initBugly();
                }
            }
        }, 300);
    }

    /**
     * Bugly初始化
     */
    private void initBugly() {
        try {
            CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getApplicationContext());
            strategy.setAppChannel(AppInfoUtil.getChannelName(this));  //设置渠道
            strategy.setAppVersion(AppInfoUtil.getAppVersionName(this, getResources().getString(R.string.app_default_version))); //App的版本
            strategy.setAppPackageName(AppInfoUtil.getPackageName(this, getResources().getString(R.string.app_package_name)));  //App的包名
            strategy.setAppReportDelay(10000);   //Bugly会在启动10s后联网同步数据
            Bugly.init(getApplicationContext(), "ee6ea51102", BuildConfig.LOG_DEBUG, strategy);
        } catch (Throwable e) {
            CrashReport.postCatchedException(e);
            e.printStackTrace();
        }
    }

    private void initLeack() {
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


    /**
     * 获取APP的包名
     *
     * @return
     */
    private String getAppPackageName() {
        String name;
        name = getPackageName();
        if (!TextUtils.isEmpty(name)) return name;
        name = getResources().getString(R.string.app_name);
        if (!TextUtils.isEmpty(name)) return name;
        return "meizhi.meizhi.malin";
    }

    /**
     * 获取APP主进程的Name
     *
     * @return
     */
    private String getAppMainProcessName() {
        String name;
        name = getProcessNameViaCmdLine();
        if (!TextUtils.isEmpty(name)) return name;
        name = getProcessNameViaManager();
        return name;
    }

    /**
     * 获取进程名
     *
     * @return
     */
    private String getProcessNameViaManager() {
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = activityManager.getRunningAppProcesses();
        if (runningApps == null || runningApps.isEmpty()) return null;
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == android.os.Process.myPid()) {
                return procInfo.processName;
            }
        }
        return null;
    }


    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessNameViaCmdLine() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + android.os.Process.myPid() + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable e) {
            CrashReport.postCatchedException(e);
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Throwable e) {
                CrashReport.postCatchedException(e);
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    //指导应用程序在不同的情况下进行自身的内存释放，以避免被系统直接杀掉，提高应用程序的用户体验.
    @Override
    public void onTrimMemory(int level) {
        CatchUtil.getInstance().releaseMemory(true);
        CatchUtil.getInstance().clearCacheDiskSelf();
        super.onTrimMemory(level);
    }
}
