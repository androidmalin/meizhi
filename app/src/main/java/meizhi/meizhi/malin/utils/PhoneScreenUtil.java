package meizhi.meizhi.malin.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.lang.reflect.Method;

import meizhi.meizhi.malin.application.MApplication;

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
     * @return DisplayMetrics
     */
    private static DisplayMetrics obtain() {
        WindowManager wm = (WindowManager) MApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    /**
     * 获取屏幕宽度
     *
     * @return width
     */
    private static int getDeviceWidth() {
        DisplayMetrics outMetrics = obtain();
        return outMetrics.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @return height
     */
    private static int getDeviceHeight() {
        DisplayMetrics outMetrics = obtain();
        return outMetrics.heightPixels;
    }


    /**
     * 获取设备屏幕密度,像素的比例
     *
     * @return density
     */
    private static float getDeviceDensity() {
        return MApplication.getContext().getResources().getDisplayMetrics().density;
    }


    /**
     * 讲px值转变成dip
     *
     * @param px px
     * @return dp
     */
    public static float pxToDip(float px) {
        return px / getDeviceDensity() + 0.5f;
    }

    /**
     * 将dip值转成px
     *
     * @param dip dip
     * @return px
     */
    public static float dipToPx(float dip) {
        return dip * getDeviceDensity() + 0.5f;
    }


    /**
     * 得到手机的高，包括状态栏和导航栏
     *
     * @return int
     */
    public static int getPhoneHeight() {
        return getDeviceHeight() + getNavigationBarHeight();
    }

    /**
     * 得到手机的高
     *
     * @return int
     */
    public static int getPhoneWidth() {
        return getDeviceWidth();
    }

    /**
     * 获取虚拟按键高度
     *
     * @return int
     */
    public static int getNavigationBarHeight() {
        int navigationBarHeight = 0;
        try {
            Resources rs = MApplication.getContext().getResources();

            if (rs == null) return navigationBarHeight;
            int id = rs.getIdentifier("navigation_bar_height", "dimen", "android");
            if (id > 0 && checkDeviceHasNavigationBar()) {
                navigationBarHeight = rs.getDimensionPixelSize(id);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return navigationBarHeight;
    }

    /**
     * 判断有没有虚拟按键
     *
     * @return boolean
     */
    private static boolean checkDeviceHasNavigationBar() {
        boolean hasNavigationBar = false;
        try {
            Resources rs = MApplication.getContext().getResources();
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