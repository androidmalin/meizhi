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

import com.tencent.bugly.crashreport.CrashReport;

import java.lang.reflect.Field;

import static android.content.Context.INPUT_METHOD_SERVICE;

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
        InputMethodManager inputMethodManager = (InputMethodManager) context.getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);
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
                CrashReport.postCatchedException(th);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void unBindView(View view) {
        if (view != null) {
            Drawable drawable;
            int i;
            //1.
            try {
                view.setOnClickListener(null);
            } catch (Throwable e) {
                CrashReport.postCatchedException(e);
            }

            //2.
            try {
                view.setOnCreateContextMenuListener(null);
            } catch (Throwable e) {
                CrashReport.postCatchedException(e);
            }

            //3.
            try {
                view.setOnFocusChangeListener(null);
            } catch (Throwable e) {
                CrashReport.postCatchedException(e);
            }

            //4.
            try {
                view.setOnKeyListener(null);
            } catch (Throwable e) {
                CrashReport.postCatchedException(e);
            }

            //5.
            try {
                view.setOnLongClickListener(null);
            } catch (Throwable e) {
                CrashReport.postCatchedException(e);
            }

            //6.
            try {
                view.setOnTouchListener(null);
            } catch (Throwable e) {
                CrashReport.postCatchedException(e);
            }

            //7.
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                    view.setOnApplyWindowInsetsListener(null);
                }
            } catch (Throwable e) {
                CrashReport.postCatchedException(e);
            }

            //8.
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    view.setOnContextClickListener(null);
                }
            } catch (Throwable e) {
                CrashReport.postCatchedException(e);
            }


            //9.
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    view.setOnScrollChangeListener(null);
                }
            } catch (Throwable e) {
                CrashReport.postCatchedException(e);
            }

            //10.
            try {
                view.setOnDragListener(null);
            } catch (Throwable e) {
                CrashReport.postCatchedException(e);
            }

            //11.
            try {
                view.setOnGenericMotionListener(null);
            } catch (Throwable e) {
                CrashReport.postCatchedException(e);
            }

            //12.
            try {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB_MR2) {//13
                    view.setOnHoverListener(null);
                }
            } catch (Throwable e) {
                CrashReport.postCatchedException(e);
            }

            //13.
            try {
                view.setOnSystemUiVisibilityChangeListener(null);
            } catch (Throwable e) {
                CrashReport.postCatchedException(e);
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
                    CrashReport.postCatchedException(e);
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
                    CrashReport.postCatchedException(e);
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
                } catch (Throwable e) {
                    CrashReport.postCatchedException(e);
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
                    CrashReport.postCatchedException(e);
                }
            }

            //ListView
            if (view instanceof ListView) {
                ListView listView = (ListView) view;

                try {
                    listView.setAdapter(null);
                } catch (Throwable e) {
                    CrashReport.postCatchedException(e);
                }

                try {
                    listView.setOnScrollListener(null);
                } catch (Throwable e) {
                    CrashReport.postCatchedException(e);
                }

                try {
                    listView.setOnItemClickListener(null);
                } catch (Throwable e) {
                    CrashReport.postCatchedException(e);
                }

                try {
                    listView.setOnItemLongClickListener(null);
                } catch (Throwable e) {
                    CrashReport.postCatchedException(e);
                }

                try {
                    listView.setOnItemSelectedListener(null);
                } catch (Throwable e) {
                    CrashReport.postCatchedException(e);
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
                    CrashReport.postCatchedException(e);
                }
            }


            //WebView
            if (view instanceof WebView) {

                WebView webView = (WebView) view;
                try {
                    (webView).stopLoading();
                } catch (Throwable ignored) {
                    CrashReport.postCatchedException(ignored);
                }

                try {
                    (webView).removeAllViews();
                } catch (Throwable ignored) {
                    CrashReport.postCatchedException(ignored);
                }

                try {
                    (webView).setWebChromeClient(null);
                } catch (Throwable ignored) {
                    CrashReport.postCatchedException(ignored);
                }

                try {
                    (webView).setWebViewClient(null);
                } catch (Throwable ignored) {
                    CrashReport.postCatchedException(ignored);
                }

                try {
                    (webView).destroy();
                } catch (Throwable ignored) {
                    CrashReport.postCatchedException(ignored);
                }

                try {
                    if (null != view.getParent() && view.getParent() instanceof ViewGroup) {
                        ((ViewGroup) view.getParent()).removeView(view);
                    }
                } catch (Throwable ignored) {
                    CrashReport.postCatchedException(ignored);
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
                    CrashReport.postCatchedException(ignored);
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
    }
}
