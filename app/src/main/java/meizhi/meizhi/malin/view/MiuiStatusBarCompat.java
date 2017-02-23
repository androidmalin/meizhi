/*
 * Mr.Mantou - On the importance of taste
 * Copyright (C) 2015-2016  XiNGRZ <xxx@oxo.ooo>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package meizhi.meizhi.malin.view;

import android.app.Activity;
import android.view.Window;

import com.tencent.bugly.crashreport.CrashReport;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import meizhi.meizhi.malin.utils.MIUIUtils;

public class MiuiStatusBarCompat {

    public static void enableLightStatusBar(Activity activity) {
        if (activity == null || activity.isFinishing()) return;
        if (!MIUIUtils.isMIUI(activity.getApplicationContext())) return;
        try {
            Class<?> layout = Class.forName("android.view.MiuiWindowManager$LayoutParams");

            int transparent = layout.getField("EXTRA_FLAG_STATUS_BAR_TRANSPARENT").getInt(layout);
            int darkMode = layout.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE").getInt(layout);

            Method setExtraFlags = activity.getWindow().getClass().getMethod("setExtraFlags", int.class, int.class);

            setExtraFlags.invoke(activity.getWindow(), transparent | darkMode, transparent | darkMode);
        } catch (Throwable e) {
            CrashReport.postCatchedException(e);
        }
    }

    //修改小米 MIUI
    public static boolean setMiuiStatusBarDarkMode(Activity activity, boolean darkMode) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkMode ? darkModeFlag : 0, darkModeFlag);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


}
