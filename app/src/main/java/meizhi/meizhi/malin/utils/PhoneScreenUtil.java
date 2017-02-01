package meizhi.meizhi.malin.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.lang.reflect.Method;

/**
 * 类描述: 手机设备信息获取工具类
 * 创建人:malin.myemail@163.com
 * 创建时间:2017/01/31 18:20
 * 备注:{@link }
 * 修改人:
 * 修改时间:
 * 修改备注:
 * 版本:
 */
public class PhoneScreenUtil {

    /**
     * 获取DisplayMetrics
     *
     * @param context:context
     * @return DisplayMetrics
     */
    private static DisplayMetrics obtain(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    /**
     * 获取屏幕宽度
     *
     * @param context Context
     * @return width
     */
    public static int getDeviceWidth(Context context) {
        DisplayMetrics outMetrics = obtain(context);
        return outMetrics.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @param context Context
     * @return height
     */
    public static int getDeviceHeight(Context context) {
        DisplayMetrics outMetrics = obtain(context);
        return outMetrics.heightPixels;
    }


    /**
     * 获取设备屏幕密度,像素的比例
     *
     * @param context Context
     * @return density
     */
    private static float getDeviceDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }


    /**
     * 讲px值转变成dip
     *
     * @param context context
     * @param px      px
     * @return dp
     */
    public static float pxToDip(Context context, float px) {
        return px / getDeviceDensity(context) + 0.5f;
    }

    /**
     * 将dip值转成px
     *
     * @param context Context
     * @param dip     dip
     * @return px
     */
    public static float dipToPx(Context context, float dip) {
        return dip * getDeviceDensity(context) + 0.5f;
    }


    /**
     * 获取虚拟按键高度
     *
     * @param context Context
     * @return int
     */
    public static int getNavigationBarHeight(Context context) {
        int navigationBarHeight = 0;
        try {
            Resources rs = context.getResources();
            if (rs != null) {
                int id = rs.getIdentifier("navigation_bar_height", "dimen", "android");
                if (id > 0 && checkDeviceHasNavigationBar(context)) {
                    navigationBarHeight = rs.getDimensionPixelSize(id);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return navigationBarHeight;
    }

    /**
     * 判断有没有虚拟按键
     *
     * @param context Context
     * @return boolean
     */
    private static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        try {
            Resources rs = context.getResources();
            int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
            if (id > 0) {
                hasNavigationBar = rs.getBoolean(id);
            }
            Class<?> systemPropertiesClass = Class.forName("android.os.SystemProperties");
            if (systemPropertiesClass == null) return hasNavigationBar;
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return hasNavigationBar;
    }

}