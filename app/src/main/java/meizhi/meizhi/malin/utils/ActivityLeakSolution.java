package meizhi.meizhi.malin.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;

public final class ActivityLeakSolution {
    private static final String TAG = ActivityLeakSolution.class.getSimpleName();

    private ActivityLeakSolution() {
    }

    public static void fixInputMethodManagerLeak(Context context) {
        if (context == null) return;
        InputMethodManager inputMethodManager = null;
        try {
            inputMethodManager = (InputMethodManager) context.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        } catch (Throwable th) {
            th.printStackTrace();
        }
        if (inputMethodManager == null) return;
        String[] strArr = new String[]{"mCurRootView", "mServedView", "mNextServedView"};

        for (String aStrArr : strArr) {
            try {
                Field declaredField = inputMethodManager.getClass().getDeclaredField(aStrArr);
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
                    Log.d(TAG, "fixInputMethodManagerLeak break, context is not suitable, get_context=" + view.getContext() + " dest_context=" + context);
                    return;
                }
            } catch (Throwable th2) {
                Log.e(TAG, th2.getLocalizedMessage());
            }
        }
    }

    public static void fixMesssageLeak(Dialog dialog) {
        if (dialog == null) return;
        for (String declaredField : new String[]{"mDismissMessage", "mCancelMessage", "mShowMessage"}) {
            try {
                Field declaredField2 = Dialog.class.getDeclaredField(declaredField);
                if (declaredField2 == null) continue;
                if (!declaredField2.isAccessible()) {
                    declaredField2.setAccessible(true);
                }
                Object obj = declaredField2.get(dialog);
                if (obj == null || !(obj instanceof Message)) continue;
                Message message = (Message) obj;
                if (message.obj == null) continue;
                message.obj = null;
                message.what = 0;
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    public static void fixAudioManagerLeak(Context context) {
        if (context == null) return;
        try {
            AudioManager audioManager = (AudioManager) context.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            if (audioManager == null) return;
            Field declaredField = audioManager.getClass().getDeclaredField("mContext");
            if (declaredField == null) return;
            declaredField.setAccessible(true);
            declaredField.set(audioManager, null);
        } catch (Throwable th) {
            Log.e(TAG, th.toString());
        }
    }

    private static void fixTextWatcherLeak(TextView textView) {
        if (textView == null) return;
        textView.setHint("");
        try {
            Field declaredField = TextView.class.getDeclaredField("mListeners");
            if (declaredField == null) return;
            if (!declaredField.isAccessible()) {
                declaredField.setAccessible(true);
            }
            Object obj = declaredField.get(textView);
            if (obj == null || !(obj instanceof ArrayList)) return;
            Iterator it = ((ArrayList) obj).iterator();
            while (it.hasNext()) {
                TextWatcher textWatcher = (TextWatcher) it.next();
                it.remove();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    @SuppressLint("PrivateApi")
    private static void fixTextWatcherLeak2() {
        try {
            Field declaredField = Class.forName("android.text.TextLine").getDeclaredField("sCached");
            if (declaredField == null) return;
            declaredField.setAccessible(true);
            Object[] objArr = (Object[]) declaredField.get(null);
            if (objArr == null) {
                Log.e(TAG, "Failed to invoke currentActivityThread");
                return;
            }
            synchronized (objArr) {
                for (Object obj : objArr) {
                    Field declaredField2 = obj.getClass().getDeclaredField("mSpanned");
                    if (declaredField2 == null) continue;
                    declaredField2.setAccessible(true);
                    Spanned spanned = (Spanned) declaredField2.get(obj);
                    if (spanned == null) continue;
                    declaredField2 = spanned.getClass().getDeclaredField("mSpanned");
                    declaredField2.setAccessible(true);
                    spanned = (Spanned) declaredField2.get(spanned);
                    if (!(spanned instanceof SpannableStringBuilder)) continue;
                    ((SpannableStringBuilder) spanned).clearSpans();
                }
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public static void unbindDrawables(Activity activity) {
        if (activity == null || activity.getWindow() == null || activity.getWindow().peekDecorView() == null)
            return;
        try {
            unbindDrawablesAndRecycle(activity, activity.getWindow().peekDecorView().getRootView());
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    private static void recycleView(View view) {
        if (view == null) return;
        try {
            view.setOnClickListener(null);
        } catch (Throwable th) {
            th.printStackTrace();
        }
        try {
            view.setOnCreateContextMenuListener(null);
        } catch (Throwable th) {
            th.printStackTrace();
        }
        try {
            view.setOnFocusChangeListener(null);
        } catch (Throwable th) {
            th.printStackTrace();
        }
        try {
            view.setOnKeyListener(null);
        } catch (Throwable th) {
            th.printStackTrace();
        }
        try {
            view.setOnLongClickListener(null);
        } catch (Throwable th) {
            th.printStackTrace();
        }
        try {
            view.setOnClickListener(null);
        } catch (Throwable th) {
            th.printStackTrace();
        }
        try {
            view.setOnTouchListener(null);
        } catch (Throwable th) {
            th.printStackTrace();
        }
        try {
            Drawable background = view.getBackground();
            if (background != null) {
                background.setCallback(null);
                view.setBackgroundDrawable(null);
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        try {
            view.destroyDrawingCache();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private static void recycleImageView(Activity activity, ImageView imageView) {
        if (imageView == null) return;
        Drawable drawable = imageView.getDrawable();
        if (drawable != null) {
            drawable.setCallback(null);
        }
        imageView.setImageDrawable(null);
        if (drawable == null) return;
        try {
            if (!(drawable instanceof BitmapDrawable)) return;
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            if (width <= 0 || height <= 0) return;
            int width2 = imageView.getWidth();
            int height2 = imageView.getHeight();
            int i = 0;
            if (width2 <= 0 || height2 <= 0) {
                i = width * height;
            } else {
                width = Math.max(Math.round(((float) width) / ((float) width2)), Math.round(((float) height) / ((float) height2)));
                if (width >= 2) {
                    i = (width2 * height2) * ((width * width) - 1);
                }
            }
            if (i > 0) {
                StringBuffer stringBuffer = new StringBuffer(100);
                i = imageView.getId();
                stringBuffer.append(activity.getClass().getName()).append('_').append(i);
                if (i == -1) {
                    ViewParent parent = imageView.getParent();
                    while (i == -1 && parent != null && (parent instanceof ViewGroup)) {
                        i = ((ViewGroup) parent).getId();
                        stringBuffer.append('_');
                        stringBuffer.append(i);
                        parent = parent.getParent();
                    }
                }
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private static void recycleTextView(TextView textView) {
        if (textView == null) return;
        for (Drawable drawable : textView.getCompoundDrawables()) {
            if (drawable == null) continue;
            drawable.setCallback(null);
        }
        textView.setCompoundDrawables(null, null, null, null);
        if (textView instanceof EditText) {
            fixTextWatcherLeak(textView);
        }
        textView.setCursorVisible(false);
    }

    private static void recycleProgressBar(ProgressBar progressBar) {
        if (progressBar == null) return;
        Drawable progressDrawable = progressBar.getProgressDrawable();
        if (progressDrawable != null) {
            progressBar.setProgressDrawable(null);
            progressDrawable.setCallback(null);
        }
        progressDrawable = progressBar.getIndeterminateDrawable();
        if (progressDrawable != null) {
            progressBar.setIndeterminateDrawable(null);
            progressDrawable.setCallback(null);
        }
    }

    private static void recycleListView(ListView listView) {
        if (listView == null) return;
        Drawable selector = listView.getSelector();
        if (selector != null) {
            selector.setCallback(null);
        }
        try {
            if (listView.getAdapter() != null) {
                listView.setAdapter(null);
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        try {
            listView.setOnScrollListener(null);
        } catch (Throwable th) {
            th.printStackTrace();
        }
        try {
            listView.setOnItemClickListener(null);
        } catch (Throwable th) {
            th.printStackTrace();
        }
        try {
            listView.setOnItemLongClickListener(null);
        } catch (Throwable th) {
            th.printStackTrace();
        }
        try {
            listView.setOnItemSelectedListener(null);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private static void recycleFrameLayout(FrameLayout frameLayout) {
        if (frameLayout == null) return;
        Drawable foreground = frameLayout.getForeground();
        if (foreground == null) return;
        foreground.setCallback(null);
        frameLayout.setForeground(null);
    }

    @SuppressLint("ObsoleteSdkInt")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private static void recycleLinearLayout(LinearLayout linearLayout) {
        if (linearLayout != null && VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {//11
            Drawable dividerDrawable;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {//16
                dividerDrawable = linearLayout.getDividerDrawable();
            } else {
                try {
                    Field declaredField = linearLayout.getClass().getDeclaredField("mDivider");
                    declaredField.setAccessible(true);
                    dividerDrawable = (Drawable) declaredField.get(linearLayout);
                } catch (Throwable th) {
                    dividerDrawable = null;
                    th.printStackTrace();
                }
            }
            if (dividerDrawable != null) {
                dividerDrawable.setCallback(null);
                linearLayout.setDividerDrawable(null);
            }
        }
    }

    private static void recycleViewGroup(Activity activity, ViewGroup viewGroup) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            unbindDrawablesAndRecycle(activity, viewGroup.getChildAt(i));
        }
    }

    private static void unbindDrawablesAndRecycle(Activity activity, View view) {
        if (view == null) return;
        recycleView(view);
        if (view instanceof ImageView) {
            recycleImageView(activity, (ImageView) view);
        } else if (view instanceof TextView) {
            recycleTextView((TextView) view);
        } else if (view instanceof ProgressBar) {
            recycleProgressBar((ProgressBar) view);
        } else {
            if (view instanceof ListView) {
                recycleListView((ListView) view);
            } else if (view instanceof FrameLayout) {
                recycleFrameLayout((FrameLayout) view);
            } else if (view instanceof LinearLayout) {
                recycleLinearLayout((LinearLayout) view);
            }
            if (view instanceof ViewGroup) {
                recycleViewGroup(activity, (ViewGroup) view);
            }
        }
    }
}
