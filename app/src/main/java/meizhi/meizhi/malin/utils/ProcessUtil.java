package meizhi.meizhi.malin.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;

import com.tencent.bugly.crashreport.CrashReport;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

import meizhi.meizhi.malin.R;
import meizhi.meizhi.malin.application.MApplication;

/**
 * 类描述:
 * 创建人:malin.myemail@163.com
 * 创建时间:16-03-03 19:10
 * 备注:{@link }
 * 修改人:
 * 修改时间:
 * 修改备注:
 * 版本:
 */

public final class ProcessUtil {

    private static String processName = null;
    private static String mPackageName = null;

    public static boolean isMainProcess() {
        String processName = getAppMainProcessName();
        String packageName = getAppPackageName();
        return !TextUtils.isEmpty(processName) && !TextUtils.isEmpty(packageName) && packageName.equals(processName);
    }

    /**
     * 获取APP的包名
     *
     * @return AppPackageName
     */
    public static String getAppPackageName() {
        if (!TextUtils.isEmpty(mPackageName)) return mPackageName;
        mPackageName = MApplication.getInstance().getApplicationContext().getPackageName();
        if (!TextUtils.isEmpty(mPackageName)) return mPackageName;
        mPackageName = MApplication.getInstance().getApplicationContext().getResources().getString(R.string.app_package_name);
        if (!TextUtils.isEmpty(mPackageName)) return mPackageName;
        return "meizhi.meizhi.malin";
    }

    /**
     * 获取APP主进程的Name
     *
     * @return APP主进程的Name
     */
    private static String getAppMainProcessName() {
        if (processName != null) return processName;
        processName = getProcessNameViaCmdLine();
        if (!TextUtils.isEmpty(processName)) return processName;
        processName = getProcessNameViaManager();
        if (!TextUtils.isEmpty(processName)) return processName;
        return null;
    }

    /**
     * 获取进程名
     *
     * @return APP主进程的Name
     */
    private static String getProcessNameViaManager() {
        int myPid = android.os.Process.myPid();
        if (myPid <= 0) return null;
        ActivityManager activityManager = (ActivityManager) MApplication.getInstance().getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager == null) return null;
        List<ActivityManager.RunningAppProcessInfo> runningApps = activityManager.getRunningAppProcesses();
        if (runningApps == null || runningApps.isEmpty()) return null;
        try {
            for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
                if (procInfo != null && procInfo.pid == myPid) {
                    return procInfo.processName;
                }
            }
        } catch (Throwable e) {
            CrashReport.postCatchedException(e);
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取进程号对应的进程名
     *
     * @return 进程名
     */
    private static String getProcessNameViaCmdLine() {
        int myPid = android.os.Process.myPid();
        if (myPid <= 0) return null;
        FileReader fileReader = null;
        BufferedReader reader = null;
        try {
            fileReader = new FileReader("/proc/" + myPid + "/cmdline");
            reader = new BufferedReader(fileReader);
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
                if (fileReader != null) {
                    fileReader.close();
                }
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
}
