package meizhi.meizhi.malin.utils.badge;

import android.content.Context;
import android.content.Intent;

import java.util.Arrays;
import java.util.List;

public class SamsungHomeBadger extends ShortcutBadger {
    public SamsungHomeBadger(Context context) {
        super(context);
    }

    protected void executeBadge(int i) {
        String launcherClassName = ShortcutBadger.getLauncherClassName(this.mContext);
        if (launcherClassName != null) {
            Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
            intent.putExtra("badge_count", i);
            if (this.mContext != null) {
                intent.putExtra("badge_count_package_name", this.mContext.getPackageName());
                intent.putExtra("badge_count_class_name", launcherClassName);
                this.mContext.sendBroadcast(intent);
            }
        }
    }

    public List<String> getSupportLaunchers() {
        return Arrays.asList(new String[]{"com.sec.android.app.launcher", "com.sec.android.app.twlauncher"});
    }
}
