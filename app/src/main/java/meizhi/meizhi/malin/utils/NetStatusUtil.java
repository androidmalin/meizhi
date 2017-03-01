package meizhi.meizhi.malin.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.util.List;

import meizhi.meizhi.malin.application.MApplication;

public final class NetStatusUtil {
    private static final String TAG = "MicroMsg.NetStatusUtil";
    private static final int WIFI_INT = 1;
    private static final int TWO_G_INT = 2;
    private static final int THREE_G_INT = 3;
    private static final int FOUR_G_INT = 4;
    private static final int OTHER_INT = 0;

    /**
     * 网络是否连接 判断当前网络是否存在，并可用于数据传输
     * ++++
     *
     * @return boolean
     */
    public static boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) MApplication.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }
        boolean isConnected;
        try {
            isConnected = connectivityManager.getActiveNetworkInfo().isConnected();
        } catch (Throwable e) {
            isConnected = false;
        }
        return isConnected;
    }

    /**
     * 得到网络类型，2G,3G,4G,WIFI,其他
     *
     * @return 2G, 3G, 4G, WIFI
     */
    public static int getWifiStatusCode() {
        if (isWifi()) {
            return WIFI_INT;
        }
        if (is4G()) {
            return FOUR_G_INT;
        }
        if (is3G()) {
            return THREE_G_INT;
        }
        if (is2G()) {
            return TWO_G_INT;
        }
        return OTHER_INT;
    }


    private static int getNetType() {
        ConnectivityManager connectivityManager = (ConnectivityManager) MApplication.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return -1;
        }
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null) {
            return -1;
        }
        if (activeNetworkInfo.getType() == 1) {
            return 0;
        }
        Log.d(TAG, "activeNetInfo extra=" + activeNetworkInfo.getExtraInfo() + " " + "type=" + activeNetworkInfo.getType());
        if (activeNetworkInfo.getExtraInfo() != null) {
            if (activeNetworkInfo.getExtraInfo().equalsIgnoreCase("uninet")) {
                return 1;
            }
            if (activeNetworkInfo.getExtraInfo().equalsIgnoreCase("uniwap")) {
                return 2;
            }
            if (activeNetworkInfo.getExtraInfo().equalsIgnoreCase("3gwap")) {
                return 3;
            }
            if (activeNetworkInfo.getExtraInfo().equalsIgnoreCase("3gnet")) {
                return 4;
            }
            if (activeNetworkInfo.getExtraInfo().equalsIgnoreCase("cmwap")) {
                return 5;
            }
            if (activeNetworkInfo.getExtraInfo().equalsIgnoreCase("cmnet")) {
                return 6;
            }
            if (activeNetworkInfo.getExtraInfo().equalsIgnoreCase("ctwap")) {
                return 7;
            }
            if (activeNetworkInfo.getExtraInfo().equalsIgnoreCase("ctnet")) {
                return 8;
            }
            if (activeNetworkInfo.getExtraInfo().equalsIgnoreCase("LTE")) {
                return 10;
            }
        }
        return 9;
    }

    /**
     * 2G
     *
     * @return 2G
     */
    private static boolean is2G() {
        try {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) MApplication.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            if (activeNetworkInfo == null) {
                return false;
            }
            if (activeNetworkInfo.getType() == 1) {
                return false;
            }
            if (activeNetworkInfo.getSubtype() == 2 || activeNetworkInfo.getSubtype() == 1 || activeNetworkInfo.getSubtype() == 4) {
                return true;
            }
            return false;
        } catch (Throwable e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
        return false;
    }

    /**
     * is4G
     *
     * @return is4G
     */
    private static boolean is4G() {
        try {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) MApplication.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            if (activeNetworkInfo == null) {
                return false;
            }
            if (activeNetworkInfo.getType() == 1) {
                return false;
            }
            if (activeNetworkInfo.getSubtype() >= 13) {
                return true;
            }
            return false;
        } catch (Throwable e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
        return false;
    }

    /**
     * is3G
     *
     * @return is3G
     */
    private static boolean is3G() {
        try {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) MApplication.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            if (activeNetworkInfo == null) {
                return false;
            }
            if (activeNetworkInfo.getType() == 1) {
                return false;
            }
            if (activeNetworkInfo.getSubtype() >= 5 && activeNetworkInfo.getSubtype() < 13) {
                return true;
            }
            return false;
        } catch (Throwable e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
        return false;
    }


    private static boolean isWifi() {
        return getNetType() == 0;
    }

    public static boolean isWifiOr4G() {
        int netType = getNetType();
        if (netType == 0 || netType == 10) {
            Log.d(TAG, "NetWork is wifi or 4g network");
            return true;
        }
        Log.d(TAG, "NetWork is mobile network");
        return false;
    }

    public static boolean isWap() {
        return isWap(getNetType());
    }

    private static boolean isWap(int i) {
        if (i == 2 || i == 5 || i == 7 || i == 3) {
            return true;
        }
        return false;
    }


    public static void startSettingIntent(Context context, int i) {
        Intent intent;
        switch (i) {
            case 1://开发者选项
                try {
                    intent = new Intent("/");
                    intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.DevelopmentSettings"));
                    intent.setAction("android.intent.action.VIEW");
                    context.startActivity(intent);
                    return;
                } catch (Throwable e) {
                    searchIntentByClass(context, "DevelopmentSettings");
                    return;
                }
            case 2:
                try {
                    intent = new Intent("/");
                    intent.setComponent(new ComponentName("com.android.providers.subscribedfeeds", "com.android.settings.ManageAccountsSettings"));
                    intent.setAction("android.intent.action.VIEW");
                    context.startActivity(intent);
                    return;
                } catch (Throwable e2) {
                    try {
                        intent = new Intent("/");
                        intent.setComponent(new ComponentName("com.htc.settings.accountsync", "com.htc.settings.accountsync.ManageAccountsSettings"));
                        intent.setAction("android.intent.action.VIEW");
                        context.startActivity(intent);
                        return;
                    } catch (Throwable e3) {
                        searchIntentByClass(context, "ManageAccountsSettings");
                        return;
                    }
                }
            case 3:
                try {
                    intent = new Intent();
                    intent.setAction("android.settings.WIFI_IP_SETTINGS");
                    context.startActivity(intent);
                    return;
                } catch (Throwable e4) {
                    searchIntentByClass(context, "AdvancedSettings");
                    return;
                }
            default:
                return;
        }
    }


    private static Intent searchIntentByClass(Context context, String str) {
        try {
            PackageManager packageManager = context.getPackageManager();
            List installedPackages = packageManager.getInstalledPackages(0);
            if (installedPackages != null && installedPackages.size() > 0) {
                Log.e(TAG, "package  size" + installedPackages.size());
                for (int i = 0; i < installedPackages.size(); i++) {
                    try {
                        Log.e(TAG, "package " + ((PackageInfo) installedPackages.get(i)).packageName);
                        Intent intent = new Intent();
                        intent.setPackage(((PackageInfo) installedPackages.get(i)).packageName);
                        List queryIntentActivities = packageManager.queryIntentActivities(intent, 0);
                        int size = queryIntentActivities != null ? queryIntentActivities.size() : 0;
                        if (size > 0) {
                            try {
                                Log.e(TAG, "activityName count " + size);
                                for (int i2 = 0; i2 < size; i2++) {
                                    ActivityInfo activityInfo = ((ResolveInfo) queryIntentActivities.get(i2)).activityInfo;
                                    if (activityInfo.name.contains(str)) {
                                        Intent intent2 = new Intent("/");
                                        intent2.setComponent(new ComponentName(activityInfo.packageName, activityInfo.name));
                                        intent2.setAction("android.intent.action.VIEW");
                                        context.startActivity(intent2);
                                        return intent2;
                                    }
                                }
                                continue;
                            } catch (Throwable e) {
                                Log.e(TAG, e.getLocalizedMessage());
                            }
                        } else {
                            continue;
                        }
                    } catch (Throwable e2) {
                        Log.e(TAG, e2.getLocalizedMessage());
                    }
                }
            }
        } catch (Throwable e22) {
            Log.e(TAG, e22.getLocalizedMessage());
        }
        return null;
    }
}
