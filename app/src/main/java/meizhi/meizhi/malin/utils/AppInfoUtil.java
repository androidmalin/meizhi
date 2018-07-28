package meizhi.meizhi.malin.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;


import meizhi.meizhi.malin.R;
import meizhi.meizhi.malin.application.MApplication;

/**
 * 类描述:App信息获取的工具类
 * 创建人:malin.myemail@163.com
 * 创建时间:16-10-09
 * 备注:{@link }
 * 修改人:
 * 修改时间:
 * 修改备注:
 * 版本:
 */

public final class AppInfoUtil {
    private AppInfoUtil() {
    }

    /**
     * 获取application中<meta-data>的值，渠道名
     *
     * @return Channel Name
     */
    public static String getChannelName() {
        String channelName;
        try {
            ApplicationInfo appInfo = MApplication.getContext().getPackageManager().getApplicationInfo(ProcessUtil.getAppPackageName(), PackageManager.GET_META_DATA);
            channelName = appInfo.metaData.getString("UMENG_CHANNEL");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            channelName = "Exception Channel";
        }
        return channelName;
    }

    /**
     * get app version name
     *
     * @return Version Name
     */
    public static String getAppVersionName() {
        String versionName;
        try {
            PackageInfo applicationInfo = MApplication.getContext().getPackageManager().getPackageInfo(ProcessUtil.getAppPackageName(), 0);
            versionName = applicationInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            versionName = MApplication.getContext().getResources().getString(R.string.app_default_version);
        }
        return versionName;
    }

    /**
     * get app package name
     *
     * @return Package Name
     */
    public static String getPackageName() {
        return ProcessUtil.getAppPackageName();
    }

    /**
     * 启动到应用商店app详情界面
     *
     * @param activity  Activity
     * @param marketPkg 应用商店包名 ,如果为""则由系统弹出应用商店列表供用户选择,否则调转到目标市场的应用详情界面，某些应用商店可能会失败
     */
    public static void launchAppDetail(Activity activity, String marketPkg) {
        try {
            if (activity == null || activity.isFinishing()) return;
            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (!TextUtils.isEmpty(marketPkg)) {
                intent.setPackage(marketPkg);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
