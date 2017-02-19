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

package meizhi.meizhi.malin.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;

import meizhi.meizhi.malin.R;
import meizhi.meizhi.malin.view.WindowInsetsHandler;


public class InsetsToolbar extends Toolbar implements WindowInsetsHandler {


    public InsetsToolbar(Context context) {
        this(context, null);
    }

    public InsetsToolbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.toolbarStyle);
    }

    public InsetsToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ViewCompat.setOnApplyWindowInsetsListener(this, new android.support.v4.view.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                final int l = insets.getSystemWindowInsetLeft();
                final int t = insets.getSystemWindowInsetTop();
                final int r = insets.getSystemWindowInsetRight();
                setPadding(l, t, r, 0);
                return insets.consumeSystemWindowInsets();
            }
        });
    }

    @Override
    public boolean onApplyWindowInsets(Rect insets) {
        setPadding(insets.left, insets.top, insets.right, 0);
        return true;
    }

}
