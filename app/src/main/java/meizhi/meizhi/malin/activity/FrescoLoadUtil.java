package meizhi.meizhi.malin.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 在非主线程里面支持调用
 * Fresco加载工具类
 * Glide处理非主线程和跨进程的时候有问题，一旦有办法处理就删除本方法
 */
public final class FrescoLoadUtil {

    private static volatile FrescoLoadUtil instance;
    private ExecutorService executeBackgroundTask = Executors.newSingleThreadExecutor();

    public static FrescoLoadUtil getInstance() {
        if (instance == null) {
            synchronized (FrescoLoadUtil.class) {
                if (instance == null) {
                    instance = new FrescoLoadUtil();
                }
            }
        }
        return instance;
    }

    //加载直接返回Bitmap
    public final void loadImageBitmap(String url, final int width, final int height, FrescoBitmapCallback<Bitmap> callback) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        try {
            fetch(Uri.parse(url), width, height, callback);
        } catch (Throwable error) {
            error.printStackTrace();
            callback.onFailure(Uri.parse(url), error);
        }
    }

    private void fetch(final Uri uri, final int width, final int height, final FrescoBitmapCallback<Bitmap> callback) throws Exception {

        ResizeOptions resizeOptions;
        if (width != 0 && height != 0) {
            resizeOptions = new ResizeOptions(width, height);
        } else {
            return;
        }
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(uri)
                .setProgressiveRenderingEnabled(false)
                .setResizeOptions(resizeOptions)
                .build();
        DataSource<CloseableReference<CloseableImage>> dataSource = ImagePipelineFactory.getInstance().getImagePipeline().fetchDecodedImage(imageRequest, null);
        dataSource.subscribe(new BaseBitmapDataSubscriber() {
                                 @Override
                                 public void onNewResultImpl(@Nullable final Bitmap bitmap) {
                                     if (callback == null) return;
                                     if (bitmap == null || bitmap.isRecycled()) return;
                                     handlerBackgroundTask(new Callable<Bitmap>() {
                                         @Override
                                         public Bitmap call() throws Exception {
                                             Bitmap resultBitmap = bitmap.copy(bitmap.getConfig(), bitmap.isMutable());
                                             if (resultBitmap != null && !resultBitmap.isRecycled())
                                                 postResult(resultBitmap, uri, callback);
                                             return resultBitmap;
                                         }
                                     });
                                 }

                                 @Override
                                 public void onCancellation(DataSource<CloseableReference<CloseableImage>> dataSource) {
                                     super.onCancellation(dataSource);
                                     if (callback == null) return;
                                     callback.onCancel(uri);
                                 }

                                 @Override
                                 public void onFailureImpl(DataSource dataSource) {
                                     if (callback == null) return;
                                     Throwable throwable;
                                     if (dataSource != null) {
                                         throwable = dataSource.getFailureCause();
                                     } else {
                                         throwable = new Throwable("onFailure");
                                     }
                                     callback.onFailure(uri, throwable);
                                 }
                             },
                UiThreadImmediateExecutorService.getInstance());
    }

    /**
     * 回调工作线程中去
     *
     * @param callable Callable
     * @param <T>      T
     * @return Future
     */
    private <T> Future<T> handlerBackgroundTask(Callable<T> callable) {
        return executeBackgroundTask.submit(callable);
    }

    /**
     * 回调UI线程中去
     *
     * @param result   result
     * @param uri      uri
     * @param callback FrescoBitmapCallback
     * @param <T>      T
     */
    private <T> void postResult(final T result, final Uri uri, final FrescoBitmapCallback<T> callback) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(uri, result);
            }
        });
    }

}