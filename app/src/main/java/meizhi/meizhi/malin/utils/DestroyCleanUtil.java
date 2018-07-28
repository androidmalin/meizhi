/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 malin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */
package meizhi.meizhi.malin.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

/**
 * 类描述:清除View控件的资源依赖
 * 创建人:malin.myemail@163.com
 * 创建时间:
 * 备注:{@link }
 * 修改人:
 * 修改时间:
 * 修改备注:
 * 版本:
 */

@SuppressLint("NewApi")
@SuppressWarnings("deprecation")
public final class DestroyCleanUtil {
    private static final String CIRCLE_CLASS = "android.support.v4.widget.CircleImageView";

    private DestroyCleanUtil() {
    }

    public static void fixInputMethod(Context context) {
        if (context == null) return;
        InputMethodManager inputMethodManager = null;
        try {
            inputMethodManager = (InputMethodManager) context.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        } catch (Throwable th) {
            th.printStackTrace();
        }
        if (inputMethodManager == null) return;
        String[] strArr = new String[]{"mCurRootView", "mServedView", "mNextServedView"};
        for (int i = 0; i < 3; i++) {
            try {
                Field declaredField = inputMethodManager.getClass().getDeclaredField(strArr[i]);
                if (declaredField == null) continue;
                if (!declaredField.isAccessible()) {
                    declaredField.setAccessible(true);
                }
                Object obj = declaredField.get(inputMethodManager);
                if (obj == null || !(obj instanceof View)) continue;
                View view = (View) obj;
                if (view.getContext() == context) {
                    declaredField.set(inputMethodManager, null);
                } else {
                    return;
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void unBindView(View view) {
        if (view == null) return;
        Drawable drawable;
        int i;
        //1.
        try {
            view.setOnClickListener(null);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        //2.
        try {
            view.setOnCreateContextMenuListener(null);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        //3.
        try {
            view.setOnFocusChangeListener(null);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        //4.
        try {
            view.setOnKeyListener(null);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        //5.
        try {
            view.setOnLongClickListener(null);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        //6.
        try {
            view.setOnTouchListener(null);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        //7.
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                view.setOnApplyWindowInsetsListener(null);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        //8.
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                view.setOnContextClickListener(null);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }


        //9.
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                view.setOnScrollChangeListener(null);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        //10.
        try {
            view.setOnDragListener(null);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        //11.
        try {
            view.setOnGenericMotionListener(null);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        //12.
        try {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB_MR2) {//13
                view.setOnHoverListener(null);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        //13.
        try {
            view.setOnSystemUiVisibilityChangeListener(null);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        /**
         * @see SwipeRefreshLayout#onDetachedFromWindow()
         */
        if (view.getBackground() != null && !view.getClass().getName().equals(CIRCLE_CLASS)) {
            try {
                view.getBackground().setCallback(null);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {//16
                    view.setBackgroundDrawable(null);
                } else {
                    view.setBackground(null);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        //ImageView
        if (view instanceof ImageView) {
            try {
                ImageView imageView = (ImageView) view;
                drawable = imageView.getDrawable();
                if (drawable != null) {
                    drawable.setCallback(null);
                }
                imageView.setImageDrawable(null);
                imageView.setImageBitmap(null);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        //TextView
        if (view instanceof TextView) {
            try {
                TextView textView = (TextView) view;
                textView.setCompoundDrawables(null, null, null, null);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    textView.setCompoundDrawablesRelative(null, null, null, null);
                }
                textView.setCursorVisible(false);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        //ImageButton
        if (view instanceof ImageButton) {
            try {
                ImageButton imageButton = (ImageButton) view;
                drawable = imageButton.getDrawable();
                if (drawable != null) {
                    drawable.setCallback(null);
                }
                imageButton.setImageDrawable(null);
                imageButton.setImageBitmap(null);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        //ListView
        if (view instanceof ListView) {
            ListView listView = (ListView) view;

            try {
                listView.setAdapter(null);
            } catch (Throwable e) {
                e.printStackTrace();
            }

            try {
                listView.setOnScrollListener(null);
            } catch (Throwable e) {
                e.printStackTrace();
            }

            try {
                listView.setOnItemClickListener(null);
            } catch (Throwable e) {
                e.printStackTrace();
            }

            try {
                listView.setOnItemLongClickListener(null);
            } catch (Throwable e) {
                e.printStackTrace();
            }

            try {
                listView.setOnItemSelectedListener(null);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }


        //RecyclerView
        if (view instanceof RecyclerView) {
            try {
                RecyclerView recyclerView = (RecyclerView) view;
                recyclerView.setAdapter(null);
                recyclerView.setChildDrawingOrderCallback(null);
                recyclerView.setOnScrollListener(null);
                recyclerView.addOnScrollListener(null);
                recyclerView.removeOnScrollListener(null);
                recyclerView.setRecyclerListener(null);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }


        //WebView
        if (view instanceof WebView) {

            WebView webView = (WebView) view;
            try {
                webView.stopLoading();
            } catch (Throwable ignored) {
                ignored.printStackTrace();
            }

            try {
                webView.removeAllViews();
            } catch (Throwable ignored) {
                ignored.printStackTrace();
            }

            try {
                webView.setWebChromeClient(null);
            } catch (Throwable ignored) {
                ignored.printStackTrace();
            }

            try {
                webView.setWebViewClient(null);
            } catch (Throwable ignored) {
                ignored.printStackTrace();
            }

            try {
                webView.destroy();
            } catch (Throwable ignored) {
                ignored.printStackTrace();
            }

            try {
                if (null != view.getParent() && view.getParent() instanceof ViewGroup) {
                    ((ViewGroup) view.getParent()).removeView(view);
                }
            } catch (Throwable ignored) {
                ignored.printStackTrace();
            }

        }


        //SurfaceView
        if (view instanceof SurfaceView) {
            try {
                SurfaceView surfaceView = (SurfaceView) view;
                SurfaceHolder holder = surfaceView.getHolder();
                if (holder != null) {
                    Surface surface = holder.getSurface();
                    if (surface != null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                            surface.release();
                        }
                    }
                }
            } catch (Throwable ignored) {
                ignored.printStackTrace();
            }
        }


        view.destroyDrawingCache();
        view.clearAnimation();

        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = (viewGroup).getChildCount();
            for (i = 0; i < childCount; i++) {
                unBindView((viewGroup).getChildAt(i));
            }
        }
    }

    /**
     * Hack TextLine
     * http://stackoverflow.com/questions/30397356/android-memory-leak-on-textview-leakcanary-leak-can-be-ignored
     * https://github.com/square/leakcanary/blob/master/leakcanary-android/src/main/java/com/squareup/leakcanary/AndroidExcludedRefs.java
     * fix https://github.com/android/platform_frameworks_base/commit/b3a9bc038d3a218b1dbdf7b5668e3d6c12be5ee4
     * <p>
     * blog:
     * http://snzang.leanote.com/post/c321f719cd02
     */
    public static void fixTextLineCacheLeak() {
        //if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) return;
        try {
            Field textLineCached;
            textLineCached = Class.forName("android.text.TextLine").getDeclaredField("sCached");
            if (textLineCached == null) return;
            textLineCached.setAccessible(true);

            // Get reference to the TextLine sCached array.
            Object cached = textLineCached.get(null);
            if (cached == null) return;
            // Clear the array.
            for (int i = 0, size = Array.getLength(cached); i < size; i++) {
                Array.set(cached, i, null);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
