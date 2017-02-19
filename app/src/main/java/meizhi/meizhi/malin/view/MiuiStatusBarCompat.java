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

import android.view.Window;

import java.lang.reflect.Method;

public class MiuiStatusBarCompat {

    public static void enableLightStatusBar(Window window) {
        try {
            final Class layout = Class.forName("android.view.MiuiWindowManager$LayoutParams");

            final int transparent = layout.getField("EXTRA_FLAG_STATUS_BAR_TRANSPARENT").getInt(layout);
            final int darkMode = layout.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE").getInt(layout);

            final Method setExtraFlags = window.getClass().getMethod("setExtraFlags", int.class, int.class);

            setExtraFlags.invoke(window, transparent | darkMode, transparent | darkMode);
        } catch (Exception ignored) {
        }
    }

}
