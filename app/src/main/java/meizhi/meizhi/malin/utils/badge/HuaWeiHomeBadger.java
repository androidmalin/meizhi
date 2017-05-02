package meizhi.meizhi.malin.utils.badge;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import java.util.Arrays;
import java.util.List;


public class HuaWeiHomeBadger extends ShortcutBadger {
    private static final String TAG = "HuaWeiHomeBadger";

    public HuaWeiHomeBadger(Context context) {
        super(context);
    }

    @TargetApi(11)
    protected void executeBadge(int i) {
        try {
            Class loadClass = this.mContext.createPackageContext("com.huawei.android.launcher", 3).getClassLoader().loadClass("com.huawei.android.launcher.LauncherProvider");
            if (((Boolean) loadClass.getMethod("isSupportChangeBadgeByCallMethod", new Class[0]).invoke(loadClass.newInstance(), new Object[0])).booleanValue()) {
                String launcherClassName = ShortcutBadger.getLauncherClassName(this.mContext);
                if (launcherClassName != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("package", this.mContext.getPackageName());
                    bundle.putString("class", launcherClassName);
                    bundle.putInt("badgenumber", i);
                    this.mContext.getContentResolver().call(Uri.parse("content://com.huawei.android.launcher.settings/badge/"), "change_badge", null, bundle);
                }
            }
        } catch (Exception e) {
            showSupportedBadge(this.mContext, i);
        }
    }

    public List<String> getSupportLaunchers() {
        return Arrays.asList(new String[]{"com.huawei.android.launcher"});
    }

    public static void showSupportedBadge(Context context, int i) {
        showAppBadge(context, "com.qzone", "com.tencent.sc.activity.SplashActivity", i);
    }

    public static void showAppBadge(Context context, String str, String str2, int i) {
        if (Build.MANUFACTURER.equalsIgnoreCase("Huawei")) {
            if (i > ShortcutBadger.MAX_BADGE_COUNT) {
                i = ShortcutBadger.MAX_BADGE_COUNT;
            }
            if (str == null) {
                str = "com.qzone";
            }
            if (str2 == null) {
                str2 = "com.tencent.sc.activity.SplashActivity";
            }
            try {
                Bundle bundle = new Bundle();
                bundle.putString("package", str);
                bundle.putString("class", str2);
                bundle.putInt("badgenumber", i);
                context.getContentResolver().call(Uri.parse("content://com.huawei.android.launcher.settings/badge/"), "change_badge", null, bundle);
            } catch (Exception e) {
                Log.e(TAG, "no support Badger");
            }
        }
    }
}
