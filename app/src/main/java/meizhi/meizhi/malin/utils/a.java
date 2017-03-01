package meizhi.meizhi.malin.utils;

import android.content.Context;
import android.util.Log;

import meizhi.meizhi.malin.application.MApplication;

public final class a {
    private static float density = -1.0f;

    public static float getDensity(Context context) {
        if (context == null) {
            context = MApplication.getInstance().getApplicationContext();
        }
        if (density < 0.0f) {
            density = context.getResources().getDisplayMetrics().density;
        }
        return density;
    }

    public static int fromDPToPix(Context context, int i) {
        return Math.round(getDensity(context) * ((float) i));
    }

    public static int U(Context context, int i) {
        return Math.round(((float) i) / getDensity(context));
    }

    public static int dA(Context context) {
        if (context != null) {
            return context.getResources().getDisplayMetrics().widthPixels;
        }
        Log.e("MicroMsg.ResourceHelper", "get widthPixels but context is null");
        return 0;
    }

    public static int dB(Context context) {
        if (context != null) {
            return context.getResources().getDisplayMetrics().heightPixels;
        }
        Log.e("MicroMsg.ResourceHelper", "get heightPixels but context is null");
        return 0;
    }
}
