package meizhi.meizhi.malin.utils;

import meizhi.meizhi.malin.BuildConfig;
import okhttp3.logging.HttpLoggingInterceptor;

public final class PLog implements HttpLoggingInterceptor.Logger {
    private static volatile PLog instance;

    private PLog() {
    }

    public static PLog instance() {
        if (null == instance) {
            synchronized (PLog.class) {
                if (null == instance) {
                    instance = new PLog();
                }
            }
        }
        return instance;
    }

    @Override
    public void log(String message) {
        httpLog("OkHttp", message);
    }

    private void httpLog(String tag, String msg) {
        if (BuildConfig.LOG_DEBUG) return;
        LogUtil.i(tag, msg);
    }
}