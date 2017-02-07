package meizhi.meizhi.malin.application;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import meizhi.meizhi.malin.BuildConfig;
import meizhi.meizhi.malin.R;
import meizhi.meizhi.malin.utils.AppInfoUtil;
import okhttp3.OkHttpClient;

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

    public static boolean isLoad = false;
    private static final String TAG = MApplication.class.getSimpleName();
    public RefWatcher mRefWatcher;

    private static MApplication application;

    public MApplication() {
        super();
        application = this;
    }

    public OkHttpClient mOkHttpClient;

    public static MApplication getApplication() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isLoad = true;
        int pid = android.os.Process.myPid();

        Log.d(TAG, getProcessName(android.os.Process.myPid()));
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid && appProcess.processName.compareToIgnoreCase(getPackageName()) == 0) {
                if (BuildConfig.DEBUG) {
                } else {
                }
                if (LeakCanary.isInAnalyzerProcess(this)) {
                    // This process is dedicated to LeakCanary for heap analysis.
                    // You should not init your app in this process.
                    return;
                }
                mRefWatcher = LeakCanary.install(this);
                try {
                    CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getApplicationContext());
                    strategy.setAppChannel(AppInfoUtil.getChannelName(this));  //设置渠道
                    strategy.setAppVersion(AppInfoUtil.getAppVersionName(this, getResources().getString(R.string.app_default_version)));      //App的版本
                    strategy.setAppPackageName(AppInfoUtil.getPackageName(this, getResources().getString(R.string.app_package_name)));  //App的包名
                    strategy.setAppReportDelay(10000);   //Bugly会在启动10s后联网同步数据
                    Bugly.init(getApplicationContext(), "ee6ea51102", BuildConfig.DEBUG, strategy);
                } catch (Throwable e) {
                    CrashReport.postCatchedException(e);
                    e.printStackTrace();
                }
            }
        }
    }


    public RefWatcher getRefWatcher(MApplication application) {
        if (application != null && mRefWatcher != null) {
            return application.mRefWatcher;
        } else {
            return LeakCanary.install(this);
        }
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
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
            } catch (IOException exception) {
                CrashReport.postCatchedException(exception);
                exception.printStackTrace();
            }
        }
        return null;
    }

}
