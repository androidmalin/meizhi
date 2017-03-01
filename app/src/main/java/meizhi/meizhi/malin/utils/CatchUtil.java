package meizhi.meizhi.malin.utils;

import android.app.ActivityManager;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;

import meizhi.meizhi.malin.application.MApplication;

/**
 * 图片缓存工具类
 */
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


    public void displayBriefMemory() {
        // getMemoryClass 所获得的大小不受largeHeap配置影响，永远是heapgrowthlimit中大小。
        // getLargeMemoryClass则为heapsize大小，两者单位都为M。
        ActivityManager activityManager = (ActivityManager) MApplication.getInstance().getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        int singleAppM = activityManager.getMemoryClass();//adb pull /system/build.prop && cat build.prop | grep "dalvik.vm.heapgrowthlimit"
        int singleAppMLarge = activityManager.getLargeMemoryClass();//cat build.prop | grep "dalvik.vm.heapsize"
        LogUtil.d(TAG, "APP内存:" + (singleAppM) + "M");
        LogUtil.d(TAG, "APP内存largeHeap:" + (singleAppMLarge) + "M");

        //应用程序最大可用内存
        //Runtime.getRuntime().maxMemory() 这个参数对应到build.prop中的信息就是在
        // 未设置largeHeap为true时会返回heapgrowthlimit的大小，
        //而设置了largeHeap为true后，则返回heapsize大小。单位为Bytes
        int maxMemory = ((int) Runtime.getRuntime().maxMemory()) / 1024 / 1024;

        //获取APP当前所分配的内存heap空间大小
        long totalMemory = ((int) Runtime.getRuntime().totalMemory()) / 1024 / 1024;

        //获取当前可用内存,当被耗尽时会自动扩张,但是不会超过maxMemory
        long freeMemory = ((int) Runtime.getRuntime().freeMemory()) / 1024 / 1024;

        LogUtil.d(TAG, "Current=" + totalMemory + "M" + " " + "free=" + freeMemory + "M" + " max=" + singleAppM + "M" + " " + "max2=" + maxMemory + "M");
    }

//        adb pull /system/build.prop ./
//        cat build.prop | grep dalvik
//        dalvik.vm.heapstartsize=8m
//        表示应用程序启动后为其分配的初始大小为8m,这里分配的内存容量会影响到整个系统对RAM的使用程度，和第一次使用应用程序时的流畅程序。这个值越大，
//        系统消耗RAM则越快，但是应用程序打开后的反应也越快。值越小，系统的RAM剩余则越多，但是程序在启动后会比较慢。

//        dalvik.vm.heapgrowthlimit=192m  这就是所谓的单个应用可使用的最大内存堆大小。
//        这里分配的内存容量会影响到整个系统对RAM的使用程序，和程序在运行一段时间后的反应速度。
//        这个值越大，系统消耗RAM则越快，但是程序会运行的非常稳定，尤其是游戏和视频程序的内容加载速度可以大幅度提升。
//        值越小，系统的RAM剩余则越多，但是程序会很卡，尤其是游戏在切换场景Loading的时候会花费很多的时间。
//        若应用程序需要使用超过这个值的内存时，将会触发系统的*收集器，系统和程序就会卡顿。


//        dalvik.vm.heapsize=512m 此项表示应用在manifest中配置android:largeHeap="true"时可使用的最大内存堆大小。
//        dalvik.vm.heaptargetutilization=0.75
//        该项用来设置当前理想的堆内存利用率。其取值位于0与1之间。当GC进行完垃圾回收之后，Dalvik的堆内存会进行相应的调整，
//        通常结果是当前存活的对象的大小与堆内存大小做除法，得到的值为这个选项的设置，即这里的0.75。注意，这只是一个参考值， Dalvik虚拟机也可以忽略此设置

//        dalvik.vm.heapminfree=512k 用来设置单次堆内存调整的最小值
//        dalvik.vm.heapmaxfree=8m 单次堆内存调整的最大值


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
            ActivityManager manager = (ActivityManager) MApplication.getInstance().getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
            if (manager == null) return;
            int singleAppM = manager.getMemoryClass();
            long totalMemory = ((int) Runtime.getRuntime().totalMemory()) / 1024 / 1024;

            float clearTotal = singleAppM * 0.4f;
            LogUtil.d(TAG, "Current=" + totalMemory + "M" + " max=" + singleAppM + "M");
            //Nexus6P 192M*0.4 = 76.8M
            if (totalMemory * 1.0f >= clearTotal) {
                Fresco.getImagePipeline().clearMemoryCaches();
                LogUtil.d(TAG, "大于" + clearTotal + "M,开始释放内存");
            }
        }
    }
}