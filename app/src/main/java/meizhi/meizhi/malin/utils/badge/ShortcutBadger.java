package meizhi.meizhi.malin.utils.badge;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

public abstract class ShortcutBadger {
    private static final List<Class<? extends ShortcutBadger>> BADGERS = new LinkedList();
    public static final int MAX_BADGE_COUNT = 99;
    public static final int MIN_BADGE_COUNT = 0;
    private static String TAG = "ShortcutBadger";
    private static ShortcutBadger mShortcutBadger;
    protected Context mContext;

    protected abstract void executeBadge(int i);

    protected abstract List<String> getSupportLaunchers();

    static {
        BADGERS.add(XiaoMiHomeBadger.class);
        BADGERS.add(SamsungHomeBadger.class);
        BADGERS.add(HuaWeiHomeBadger.class);
    }

    private ShortcutBadger() {
    }

    protected ShortcutBadger(Context context) {
        this.mContext = context;
    }

    public static void setBadge(Context context, int i) {
        if (i >= MIN_BADGE_COUNT && i <= MAX_BADGE_COUNT) {
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.HOME");
            if (context != null) {
                PackageManager packageManager = context.getPackageManager();
                if (packageManager != null) {
                    try {
                        ResolveInfo resolveActivity = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
                        String str = "no home package";
                        if (!(resolveActivity == null || resolveActivity.activityInfo == null)) {
                            str = resolveActivity.activityInfo.packageName;
                        }
                        ShortcutBadger shortcutBadger = getShortcutBadger(str, context);
                        if (shortcutBadger != null) {
                            shortcutBadger.executeBadge(i);
                        }
                    } catch (Exception e) {
                        Log.w(TAG, "ShortcutBadger exception message:" + e.getMessage());
                    }
                }
            }
        }
    }

    private static ShortcutBadger getShortcutBadger(String str, Context context) {
        if (mShortcutBadger != null) {
            return mShortcutBadger;
        }
        if (Build.MANUFACTURER.toLowerCase().contains("meizu")) {
            return null;
        }
        for (Class constructor : BADGERS) {
            try {
                ShortcutBadger shortcutBadger = (ShortcutBadger) constructor.getConstructor(new Class[]{Context.class}).newInstance(new Object[]{context});
                if (!(shortcutBadger == null || shortcutBadger.getSupportLaunchers() == null || !shortcutBadger.getSupportLaunchers().contains(str))) {
                    mShortcutBadger = shortcutBadger;
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mShortcutBadger;
    }

    protected static String getLauncherClassName(Context context) {
        if (context == null) {
            return "";
        }
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setPackage(context.getPackageName());
        intent.addCategory("android.intent.category.LAUNCHER");
        ResolveInfo resolveActivity = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (resolveActivity == null) {
            resolveActivity = packageManager.resolveActivity(intent, 0);
        }
        String str = "";
        if (resolveActivity == null || resolveActivity.activityInfo == null) {
            return str;
        }
        return resolveActivity.activityInfo.name;
    }
}
