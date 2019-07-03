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
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import meizhi.meizhi.malin.R;
import meizhi.meizhi.malin.view.WindowInsetsHandler;


public class InsetsRecyclerView extends RecyclerView implements WindowInsetsHandler {


    private final int padding;

    public InsetsRecyclerView(Context context) {
        this(context, null);
    }

    public InsetsRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InsetsRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.InsetsRecyclerView);
        padding = a.getDimensionPixelOffset(R.styleable.InsetsRecyclerView_padding, 0);
        a.recycle();
        setPadding(padding, padding, padding, padding);

        ViewCompat.setOnApplyWindowInsetsListener(this, new androidx.core.view.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                final int l = padding + insets.getSystemWindowInsetLeft();
                final int r = padding + insets.getSystemWindowInsetRight();
                final int b = padding + insets.getSystemWindowInsetBottom();
                setPadding(l, padding, r, b);
                return insets.consumeSystemWindowInsets();
            }
        });
    }

    @Override
    public boolean onApplyWindowInsets(Rect insets) {
        final int l = padding + insets.left;
        final int r = padding + insets.right;
        final int b = padding + insets.bottom;
        setPadding(l, padding, r, b);
        return true;
    }

}
