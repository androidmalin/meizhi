package meizhi.meizhi.malin.application;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.facebook.common.logging.FLog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import meizhi.meizhi.malin.utils.CatchUtil;
import meizhi.meizhi.malin.utils.ImageLoaderConfig;
import meizhi.meizhi.malin.utils.ProcessUtil;

/**
 * 类描述:
 * 创建人:malin.myemail@163.com
 * 创建时间:16-5-21 19:31
 * 备注:{@link }
 * 修改人:
 * 修改时间:
 * 修改备注:
 * 版本:
 * 参考：http://blog.csdn.net/guolin_blog/article/details/47028975
 */
public class MApplication extends Application {

    private static RefWatcher mRefWatcher;


    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        initContext();
        if (ProcessUtil.isMainProcess()) {
            initFresco();
            initLeakCanary();
        }
    }


    public static Context getContext() {
        return mContext;
    }

    private void initContext() {
        mContext = getApplicationContext();
    }

    private void initFresco() {
        Fresco.initialize(this, ImageLoaderConfig.getInstance().getImagePipelineConfig(getApplicationContext()));
        FLog.setMinimumLoggingLevel(FLog.ERROR);
    }


    private void initLeakCanary() {
        mRefWatcher = LeakCanary.install(this);
    }

    public static RefWatcher getRefWatcher() {
        return mRefWatcher;
    }

    //指导应用程序在不同的情况下进行自身的内存释放，以避免被系统直接杀掉，提高应用程序的用户体验.
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        CatchUtil.getInstance().releaseMemory(true);
        CatchUtil.getInstance().clearCacheDiskSelf();
    }

}
