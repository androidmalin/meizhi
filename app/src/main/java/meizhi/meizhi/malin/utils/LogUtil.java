package meizhi.meizhi.malin.utils;

import android.util.Log;

import meizhi.meizhi.malin.BuildConfig;
/**
 * 类描述:
 * 创建人:malin.myemail@163.com
 * 创建时间:2017.2.28 20:22
 * 备注:{@link }
 * 修改人:
 * 修改时间:
 * 修改备注:
 * 版本:
 */
public class LogUtil {
    private static int logLevel = Log.VERBOSE;

    public static void i(String tag, String msg) {
        if (!BuildConfig.LOG_DEBUG) return;
        if (logLevel <= Log.INFO)
            android.util.Log.i(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (!BuildConfig.LOG_DEBUG) return;
        if (logLevel <= Log.ERROR)
            android.util.Log.e(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (!BuildConfig.LOG_DEBUG) return;
        if (logLevel <= Log.DEBUG)
            android.util.Log.d(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (!BuildConfig.LOG_DEBUG) return;
        if (logLevel <= Log.VERBOSE)
            android.util.Log.v(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (!BuildConfig.LOG_DEBUG) return;
        if (logLevel <= Log.WARN)
            android.util.Log.w(tag, msg);
    }
}