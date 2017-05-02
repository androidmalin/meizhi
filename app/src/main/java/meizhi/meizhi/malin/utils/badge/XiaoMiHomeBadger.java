package meizhi.meizhi.malin.utils.badge;

import android.annotation.TargetApi;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class XiaoMiHomeBadger extends ShortcutBadger {
    public static final String TAG = "XiaoMiHomeBadger";
    private static int hasExtraNotf;
    private static Field mENField;
    private static Field mMCField;
    private static Class mMINFClass;
    private static int miui6Flag = 0;

    public XiaoMiHomeBadger(Context context) {
        super(context);
    }

    @TargetApi(19)
    protected void executeBadge(int i) {
        if (!hasExtraNotf()) {
            Intent intent = new Intent("android.intent.action.APPLICATION_MESSAGE_UPDATE");
            if (this.mContext != null) {
                intent.putExtra("android.intent.extra.update_application_component_name", this.mContext.getPackageName() + "/" + ShortcutBadger.getLauncherClassName(this.mContext));
                intent.putExtra("android.intent.extra.update_application_message_text", String.valueOf(i == 0 ? "" : Integer.valueOf(i)));
                this.mContext.sendBroadcast(intent);
            }
        }
    }

    public List<String> getSupportLaunchers() {
        return Arrays.asList(new String[]{"com.miui.home"});
    }

    public static void setExtraNotf(int i, Notification notification) {
        try {
            Object newInstance = mMINFClass.newInstance();
            if (i > ShortcutBadger.MAX_BADGE_COUNT) {
                i = ShortcutBadger.MAX_BADGE_COUNT;
            }
            mMCField.set(newInstance, Integer.valueOf(i));
            mENField.set(notification, newInstance);
        } catch (Throwable e) {
            Log.e(TAG, "MIUI set badge error" + e.toString());
        }
    }

    public static boolean hasExtraNotf() {
        if (hasExtraNotf == 1) {
            return true;
        }
        if (hasExtraNotf == -1) {
            return false;
        }
        try {
            mMINFClass = Class.forName("android.app.MiuiNotification");
            mMCField = mMINFClass.getDeclaredField("messageCount");
            mMCField.setAccessible(true);
            mENField = Notification.class.getField("extraNotification");
            hasExtraNotf = 1;
            return true;
        } catch (Exception e) {
            hasExtraNotf = -1;
            return false;
        }
    }

    public static boolean isMIUI6() {
        Object readLine;
        Throwable e;
        Throwable th;
        if (miui6Flag == 0) {
            miui6Flag = -1;
            Object obj = "";
            BufferedReader bufferedReader = null;
            BufferedReader bufferedReader2;
            try {
                ProcessBuilder processBuilder = new ProcessBuilder(new String[]{"getprop", "ro.miui.ui.version.name"});
                if (processBuilder != null) {
                    Process start = processBuilder.start();
                    if (start != null) {
                        bufferedReader2 = new BufferedReader(new InputStreamReader(start.getInputStream()), 1024);
                        try {
                            readLine = bufferedReader2.readLine();
                            try {
                                bufferedReader2.close();
                                obj = readLine;
                                bufferedReader = bufferedReader2;
                            } catch (IOException e2) {
                                e = e2;
                                try {
                                    Log.e(TAG, "isMIUI6 Unable to read miui6" + e.toString());
                                    if (bufferedReader2 != null) {
                                        try {
                                            bufferedReader2.close();
                                        } catch (Throwable e3) {
                                            Log.e(TAG, "isMIUI6 Exception while closing" + e3);
                                        }
                                    }
                                    miui6Flag = 1;
                                    if (miui6Flag == 1) {
                                        return false;
                                    }
                                    return true;
                                } catch (Throwable th2) {
                                    th = th2;
                                    obj = readLine;
                                    bufferedReader = bufferedReader2;
                                    if (bufferedReader != null) {
                                        try {
                                            bufferedReader.close();
                                        } catch (Throwable e32) {
                                            Log.e(TAG, "isMIUI6 Exception while closing" + e32.toString());
                                        }
                                    }
                                    miui6Flag = 1;
                                    throw th;
                                }
                            } catch (Exception e4) {
                                obj = readLine;
                                bufferedReader = bufferedReader2;
                                try {
                                    Log.e(TAG, "isMIUI6 other ex" + e4.toString());
                                    if (bufferedReader != null) {
                                        try {
                                            bufferedReader.close();
                                        } catch (Throwable e322) {
                                            Log.e(TAG, "isMIUI6 Exception while closing" + e322.toString());
                                        }
                                    }
                                    miui6Flag = 1;
                                    if (miui6Flag == 1) {
                                        return true;
                                    }
                                    return false;
                                } catch (Throwable th3) {
                                    th = th3;
                                    if (bufferedReader != null) {
                                        bufferedReader.close();
                                    }
                                    if (!TextUtils.isEmpty(obj.toString()) && "V6".equalsIgnoreCase(obj.toString())) {
                                        miui6Flag = 1;
                                    }
                                    throw th;
                                }
                            }
                        } catch (IOException e5) {
                            readLine = obj;
                            Log.e(TAG, "isMIUI6 Unable to read miui6" + e5.toString());
                            if (bufferedReader2 != null) {
                                bufferedReader2.close();
                            }
                            if (!TextUtils.isEmpty(readLine.toString()) && "V6".equalsIgnoreCase(readLine.toString())) {
                                miui6Flag = 1;
                            }
                            if (miui6Flag == 1) {
                                return false;
                            }
                            return true;
                        } catch (Exception e6) {
                            bufferedReader = bufferedReader2;
                            Log.e(TAG, "isMIUI6 other ex" + e6.toString());
                            if (bufferedReader != null) {
                                bufferedReader.close();
                            }
                            if (!TextUtils.isEmpty(obj.toString()) && "V6".equalsIgnoreCase(obj.toString())) {
                                miui6Flag = 1;
                            }
                            if (miui6Flag == 1) {
                                return true;
                            }
                            return false;
                        } catch (Throwable th4) {
                            th = th4;
                            bufferedReader = bufferedReader2;
                            if (bufferedReader != null) {
                                bufferedReader.close();
                            }
                            miui6Flag = 1;
                        }
                    }
                }
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (Throwable e3222) {
                        Log.e(TAG, "isMIUI6 Exception while closing" + e3222.toString());
                    }
                }
                if (!TextUtils.isEmpty(obj.toString()) && "V6".equalsIgnoreCase(obj.toString())) {
                    miui6Flag = 1;
                }
            } catch (IOException e7) {
                bufferedReader2 = null;
                readLine = obj;
                Log.e(TAG, "isMIUI6 Unable to read miui6" + e7.toString());
                if (bufferedReader2 != null) {
                    try {
                        bufferedReader2.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                miui6Flag = 1;
                if (miui6Flag == 1) {
                    return false;
                }
                return true;
            } catch (Exception e8) {
                Log.e(TAG, "isMIUI6 other ex" + e8.toString());
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                miui6Flag = 1;
                if (miui6Flag == 1) {
                    return true;
                }
                return false;
            }
        }
        if (miui6Flag == 1) {
            return true;
        }
        return false;
    }
}
