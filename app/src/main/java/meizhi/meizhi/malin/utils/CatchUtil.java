package meizhi.meizhi.malin.utils;

import android.app.ActivityManager;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.io.File;
import java.math.BigDecimal;

import meizhi.meizhi.malin.application.MApplication;

/**
 * 图片缓存工具类
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
public class CatchUtil {
    private static final String TAG = CatchUtil.class.getSimpleName();
    private static volatile CatchUtil instance;

    public static CatchUtil getInstance() {
        if (instance == null) {
            synchronized (CatchUtil.class) {
                if (instance == null) {
                    instance = new CatchUtil();
                }
            }
        }
        return instance;
    }

    // 获取Glide磁盘缓存大小
    public String getCacheSize(String path) {
        try {
            return getFormatSize(getFolderSize(new File(path)));
        } catch (Exception e) {
            e.printStackTrace();
            return "获取失败";
        }
    }

    // 获取指定文件夹内所有文件大小的和
    private long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File aFileList : fileList) {
                if (aFileList.isDirectory()) {
                    size = size + getFolderSize(aFileList);
                } else {
                    size = size + aFileList.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    // 格式化单位
    private static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte";
        }
        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }
        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }
        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

    // 按目录删除文件夹文件方法
    private boolean deleteFolderFile(String filePath, boolean deleteThisPath) {
        try {
            File file = new File(filePath);
            if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (File file1 : files) {
                    deleteFolderFile(file1.getAbsolutePath(), true);
                }
            }
            if (deleteThisPath) {
                if (!file.isDirectory()) {
                    file.delete();
                } else {
                    if (file.listFiles().length == 0) {
                        file.delete();
                    }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public void displayBriefMemory(Context context) {

//        adb pull /system/build.prop ./
//        cat build.prop | grep dalvik
//        dalvik.vm.heapstartsize=8m 为一个应用初始分配的堆大小，越大意味着应用第一次启动时越流畅，但也意味着内存耗用越快。
//        dalvik.vm.heapgrowthlimit=192m  这就是所谓的单个应用可使用的最大内存堆大小。
//        dalvik.vm.heapsize=512m 此项表示应用在manifest中配置android:largeHeap="true"时可使用的最大内存堆大小。
//        dalvik.vm.heaptargetutilization=0.75
//        dalvik.vm.heapminfree=512k
//        dalvik.vm.heapmaxfree=8m

        // getMemoryClass 所获得的大小不受largeHeap配置影响，永远是heapgrowthlimit中大小。
        // getLargeMemoryClass则为heapsize大小，两者单位都为M。
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        int singleAppM = activityManager.getMemoryClass();
        int singleAppMLarge = activityManager.getLargeMemoryClass();
        //LogUtil.d(TAG, "APP内存:" + (singleAppM) + "M");
        //LogUtil.d(TAG, "APP内存largeHeap:" + (singleAppMLarge) + "M");

        // maxMemory=512M,totalMemory=13M,freeMemory=2M
        //应用程序最大可用内存
        //Runtime.getRuntime().maxMemory() 这个参数对应到build.prop中的信息就是在未设置largeHeap为true时会返回heapgrowthlimit的大小，
        //而设置了largeHeap为true后，则返回heapsize大小。单位为Bytes
        int maxMemory = ((int) Runtime.getRuntime().maxMemory()) / 1024 / 1024;

        //获取APP当前所分配的内存heap空间大小
        long totalMemory = ((int) Runtime.getRuntime().totalMemory()) / 1024 / 1024;
        //获取当前可用内存,当被耗尽时会自动扩张,但是不会超过maxMemory
        long freeMemory = ((int) Runtime.getRuntime().freeMemory()) / 1024 / 1024;
        //LogUtil.d(TAG, "---> maxMemory=" + maxMemory + "M,totalMemory=" + totalMemory + "M,freeMemory=" + freeMemory + "M");

        LogUtil.d(TAG, "Current=" + totalMemory + "M" + "free=" + freeMemory + "M" + " max=" + singleAppM + "M" + " " + "max2=" + maxMemory);
    }


    public void clearCacheDiskSelf() {
        Fresco.getImagePipeline().clearCaches();
    }

    /**
     * 清理内存
     */
    public void releaseMemory(boolean rightNow) {
        if (rightNow) {
            LogUtil.d(TAG, "立刻释放内存");
            Fresco.getImagePipeline().clearMemoryCaches();
        } else {
            ActivityManager manager = (ActivityManager) MApplication.getInstance().getSystemService(Context.ACTIVITY_SERVICE);
            int singleAppM = manager.getMemoryClass();
            long totalMemory = ((int) Runtime.getRuntime().totalMemory()) / 1024 / 1024;

            float clearTotal = singleAppM * 0.4f;
            LogUtil.d(TAG, "Current=" + totalMemory + "M" + " max=" + singleAppM + "M");
            //Nexus6P 96M 192M
            if (totalMemory * 1.0f >= clearTotal) {
                Fresco.getImagePipeline().clearMemoryCaches();
                LogUtil.d(TAG, "大于" + clearTotal + "M,开始释放内存");
            }
        }
    }
}