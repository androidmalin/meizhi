package meizhi.meizhi.malin.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.IOException;

//weChat
public final class d {
    public static String q(Context context, int i) {
        Throwable throwable;
        FileInputStream fileInputStream;
        if (context == null || i <= 0) {
            return "";
        }
        try {
            for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningAppProcesses()) {
                if (runningAppProcessInfo.pid == i && runningAppProcessInfo.processName != null && !runningAppProcessInfo.processName.equals("")) {
                    return runningAppProcessInfo.processName;
                }
            }
        } catch (Throwable e2) {
            Log.e("MicroMsg.Util", e2.getLocalizedMessage());
        }
        byte[] bArr = new byte[128];
        try {
            fileInputStream = new FileInputStream("/proc/" + i + "/cmdline");
            try {
                int read = fileInputStream.read(bArr);
                if (read > 0) {
                    int i2 = 0;
                    while (i2 < read) {
                        if (bArr[i2] > Byte.MIN_VALUE || bArr[i2] <= (byte) 0) {
                            read = i2;
                            break;
                        }
                        i2++;
                    }
                    String str = new String(bArr, 0, read);
                    try {
                        fileInputStream.close();
                        return str;
                    } catch (Exception e3) {
                        return str;
                    }
                }
                try {
                    fileInputStream.close();
                } catch (Exception e4) {
                }
                return "";
            } catch (Exception e5) {
                throwable = e5;
                try {
                    Log.e("MicroMsg.Util", throwable.getLocalizedMessage());
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (Exception e6) {
                        }
                    }
                    return "";
                } catch (Throwable th) {
                    throwable = th;
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (Exception e7) {
                        }
                    }
                    throw throwable;
                }
            }
        } catch (Exception e8) {
            throwable = e8;
            fileInputStream = null;
            Log.e("MicroMsg.Util", throwable.getLocalizedMessage());
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return "";
        } catch (Throwable th2) {
            throwable = th2;
            fileInputStream = null;
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }
}
