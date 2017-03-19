package meizhi.meizhi.malin.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.facebook.common.logging.FLog;
import com.facebook.common.memory.MemoryTrimType;
import com.facebook.common.memory.MemoryTrimmable;
import com.facebook.common.memory.MemoryTrimmableRegistry;
import com.facebook.common.memory.NoOpMemoryTrimmableRegistry;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.ProgressiveJpegConfig;
import com.facebook.imagepipeline.image.ImmutableQualityInfo;
import com.facebook.imagepipeline.image.QualityInfo;

import meizhi.meizhi.malin.BuildConfig;
import okhttp3.OkHttpClient;

/**
 * 类描述:
 * 创建人:malin.myemail@163.com
 * 创建时间:2017.2.13
 * 备注:{@link }
 * 修改人:
 * 修改时间:
 * 修改备注:
 * 版本:
 */
public final class ImageLoaderConfig {

    private ImageLoaderConfig() {
    }

    private static volatile ImageLoaderConfig instance = null;

    public static ImageLoaderConfig getInstance() {
        if (instance == null) {
            synchronized (ImageLoaderConfig.class) {
                if (instance == null) {
                    instance = new ImageLoaderConfig();
                }
            }
        }
        return instance;
    }


    public ImagePipelineConfig getImagePipelineConfig(Context context) {

        // 当内存紧张时采取的措施
        MemoryTrimmableRegistry memoryTrimmableRegistry = NoOpMemoryTrimmableRegistry.getInstance();
        memoryTrimmableRegistry.registerMemoryTrimmable(new MemoryTrimmable() {
            @Override
            public void trim(MemoryTrimType trimType) {
                final double suggestedTrimRatio = trimType.getSuggestedTrimRatio();

                if (MemoryTrimType.OnCloseToDalvikHeapLimit.getSuggestedTrimRatio() == suggestedTrimRatio
                        || MemoryTrimType.OnSystemLowMemoryWhileAppInBackground.getSuggestedTrimRatio() == suggestedTrimRatio
                        || MemoryTrimType.OnSystemLowMemoryWhileAppInForeground.getSuggestedTrimRatio() == suggestedTrimRatio) {
                    // 清除内存缓存
                    Fresco.getImagePipeline().clearMemoryCaches();
                }
            }
        });


        if (BuildConfig.LOG_DEBUG) {
            FLog.setMinimumLoggingLevel(FLog.VERBOSE);
        } else {
            FLog.setMinimumLoggingLevel(FLog.ERROR);
        }

        //替换网络实现为OkHttp3
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

        //网络实现层用okHttp3
        ImagePipelineConfig mImagePipelineConfig = OkHttpImagePipelineConfigFactory.newBuilder(context, okHttpClient)
                // 要不要向下采样,它处理图片的速度比常规的裁剪scaling更快，
                .setDownsampleEnabled(true) //在解码时改变图片的大小，支持PNG、JPG以及WEBP格式的图片，与ResizeOptions配合使用
                .setBitmapsConfig(Bitmap.Config.RGB_565)// 若不是要求忒高清显示应用，就用使用RGB_565吧（默认是ARGB_8888)
                //设置Jpeg格式的图片支持渐进式显示
                .setProgressiveJpegConfig(new ProgressiveJpegConfig() {
                    @Override
                    public int getNextScanNumberToDecode(int scanNumber) {
                        return scanNumber + 2;
                    }

                    public QualityInfo getQualityInfo(int scanNumber) {
                        boolean isGoodEnough = (scanNumber >= 5);
                        return ImmutableQualityInfo.of(scanNumber, isGoodEnough, false);
                    }
                })
                .setMemoryTrimmableRegistry(memoryTrimmableRegistry) // 报内存警告时的监听
                .build();
        return mImagePipelineConfig;
    }

}