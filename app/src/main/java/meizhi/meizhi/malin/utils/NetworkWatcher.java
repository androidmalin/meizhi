package meizhi.meizhi.malin.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.DetailedState;
import android.os.Handler;
import android.os.HandlerThread;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

/**
 * 网络工具
 */
public final class NetworkWatcher {


    private static boolean wifiAvailable = false;
    private static final String ACTION_CONN_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";
    private static Context mContext = null;
    private static boolean mIsInited = false;
    private static Handler mHandler = null;

    private NetworkWatcher() {
    }


    private static BroadcastReceiver netStatusReceive = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (NetworkWatcher.mHandler != null && NetworkWatcher.mContext != null) {
                NetworkWatcher.mHandler.post(new Runnable() {
                    public void run() {
                        try {
                            NetworkWatcher.checkConnInfo(NetworkWatcher.mContext);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    };

    public static void init(Context context) {
        if (!mIsInited) {
            mContext = context;
            HandlerThread handlerThread = new HandlerThread("NetworkWatcher");
            handlerThread.start();
            mHandler = new Handler(handlerThread.getLooper());
            try {
                mContext.registerReceiver(netStatusReceive, new IntentFilter(ACTION_CONN_CHANGE));
                checkConnInfo(mContext);
                mIsInited = true;
            } catch (SecurityException e) {
                mIsInited = false;
            }
        }
    }

    public static boolean isWifiAvailable() {
        return wifiAvailable;
    }

    public static void uninit() {
        if (mContext != null && mIsInited) {
            try {
                mContext.unregisterReceiver(netStatusReceive);
                mIsInited = false;
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    private static void checkConnInfo(Context context) {
        checkConnInfo(((ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo());
    }

    private static void checkConnInfo(NetworkInfo networkInfo) {
        boolean z = false;
        if (networkInfo != null && !networkInfo.getTypeName().toLowerCase(Locale.US).contains("mobile_mms")) {
            if (networkInfo.getDetailedState() == DetailedState.CONNECTED) {
                if (1 == networkInfo.getType() && !wifiAvailable) {
                    if (!wifiNeedsAuth()) {
                        z = true;
                    }
                    wifiAvailable = z;
                }
            } else if ((networkInfo.getDetailedState() == DetailedState.DISCONNECTED || networkInfo.getDetailedState() == DetailedState.FAILED || networkInfo.getDetailedState() == DetailedState.IDLE || networkInfo.getDetailedState() == DetailedState.SUSPENDED) && 1 == networkInfo.getType()) {
                wifiAvailable = false;
            }
        }
    }

    private static boolean wifiNeedsAuth() {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL("http://3gimg.qq.com/ping.html").openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setInstanceFollowRedirects(false);
            httpURLConnection.setReadTimeout(60000);
            httpURLConnection.connect();
            byte[] bArr = new byte[8];
            httpURLConnection.getInputStream().read(bArr);
            httpURLConnection.getInputStream().close();
            httpURLConnection.disconnect();
            if ("Poduct3G".equals(new String(bArr))) {
                return false;
            }
            return true;
        } catch (Throwable e) {
            return true;
        }
    }
}
