package meizhi.meizhi.malin.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

/**
 * Hack TextLine
 * http://stackoverflow.com/questions/30397356/android-memory-leak-on-textview-leakcanary-leak-can-be-ignored
 * https://github.com/square/leakcanary/blob/master/leakcanary-android/src/main/java/com/squareup/leakcanary/AndroidExcludedRefs.java
 * fix https://github.com/android/platform_frameworks_base/commit/b3a9bc038d3a218b1dbdf7b5668e3d6c12be5ee4
 * <p>
 * blog:
 * http://snzang.leanote.com/post/c321f719cd02
 */
public final class TextLineCleanUtil {
    private TextLineCleanUtil() {
    }

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