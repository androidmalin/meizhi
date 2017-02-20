package meizhi.meizhi.malin.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public final class MIUIUtils {

    // 检测MIUI  
    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";

    public static boolean isMIUI(Context context) {
        if (SPUtils.contains(context, "isMIUI")) {
            return (boolean) SPUtils.get(context, "isMIUI", false);
        }

        Properties prop = new Properties();
        boolean isMIUI;
        try {
            prop.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
        isMIUI = prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null
                || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
                || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null;
        SPUtils.put(context, "isMIUI", isMIUI);//保存是否MIUI
        return isMIUI;
    }
}  