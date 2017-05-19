package tv.panda.live.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.util.UriUtil;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 类描述:图片加载在非主线程里面支持调用
 * 创建人:malin.myemail@163.com
 * 创建时间:2017.3.20
 * 备注:{@link }
 * 修改人:
 * 修改时间:
 * 修改备注:
 * 版本:
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


    /**
     * 加载本地应用中data/data/files/文件夹中的图片
     *
     * @param simpleDraweeView  SimpleDraweeView
     * @param width             width
     * @param height            height
     * @param dataDataFilesPath dataDataFilesPath /data/user/0/packageName/files/xyz
     */
    public void loadImageLocalFile(SimpleDraweeView simpleDraweeView, float width, float height, String dataDataFilesPath) {
        if (TextUtils.isEmpty(dataDataFilesPath)) return;
        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_FILE_SCHEME)
                .path(String.valueOf(dataDataFilesPath))
                .build();
        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(uri)
                .setProgressiveRenderingEnabled(true)
                .setResizeOptions(new ResizeOptions((int) width, (int) height))
                .build();
        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setImageRequest(imageRequest)
                .setOldController(simpleDraweeView.getController())
                .setTapToRetryEnabled(false)
                .setAutoPlayAnimations(true)
                .build();
        simpleDraweeView.setController(controller);
    }

    /**
     * 加载本地应用中data/data/files/文件夹中的图片
     *
     * @param simpleDraweeView  SimpleDraweeView
     * @param widthDP           widthDP
     * @param heightDP          heightDP
     * @param dataDataFilesPath dataDataFilesPath /data/user/0/packageName/files/xyz
     */
    public void loadImageLocalFileDP(SimpleDraweeView simpleDraweeView, float widthDP, float heightDP, String dataDataFilesPath) {
        if (widthDP == 0 || heightDP == 0 || TextUtils.isEmpty(dataDataFilesPath)) return;
        int w = Dp2Px(simpleDraweeView.getContext().getApplicationContext(), widthDP);
        int h = Dp2Px(simpleDraweeView.getContext().getApplicationContext(), heightDP);
        loadImageLocalFile(simpleDraweeView, w, h, dataDataFilesPath);
    }

    /**
     * 加载Res文件夹中Drawable中的图片
     *
     * @param simpleDraweeView SimpleDraweeView
     * @param width            width
     * @param height           height
     * @param drawable         drawable
     */
    public void loadImageLocalRes(SimpleDraweeView simpleDraweeView, int width, int height, @DrawableRes int drawable) {
        if (width == 0 || height == 0 || drawable == 0) return;
        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                .path(String.valueOf(drawable))
                .build();
        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(uri)
                .setProgressiveRenderingEnabled(true)
                .setResizeOptions(new ResizeOptions(width, height))
                .build();
        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setImageRequest(imageRequest)
                .setOldController(simpleDraweeView.getController())
                .setTapToRetryEnabled(false)
                .setAutoPlayAnimations(true)
                .build();
        simpleDraweeView.setController(controller);
    }

    /**
     * 加载Res文件夹中Drawable中的图片
     *
     * @param simpleDraweeView SimpleDraweeView
     * @param widthDP          widthDP
     * @param heightDP         heightDP
     * @param drawable         drawable
     */
    public void loadImageLocalResDP(SimpleDraweeView simpleDraweeView, float widthDP, float heightDP, @DrawableRes int drawable) {
        if (widthDP == 0 || heightDP == 0 || drawable == 0) return;
        int w = Dp2Px(simpleDraweeView.getContext().getApplicationContext(), widthDP);
        int h = Dp2Px(simpleDraweeView.getContext().getApplicationContext(), heightDP);
        loadImageLocalRes(simpleDraweeView, w, h, drawable);
    }


    /**
     * 加载一张网络图片
     *
     * @param simpleDraweeView 　SimpleDraweeView
     * @param width            　width
     * @param height           height
     * @param url              http url
     */
    public void loadImageNetWork(SimpleDraweeView simpleDraweeView, int width, int height, String url) {
        loadImageNetWork(simpleDraweeView, width, height, url, null);
    }

    /**
     * 加载一张网络图片
     *
     * @param simpleDraweeView 　SimpleDraweeView
     * @param width            　width
     * @param height           height
     * @param url              http url
     */
    public void loadImageNetWork(SimpleDraweeView simpleDraweeView, int width, int height, String url, ControllerListener<? super ImageInfo> controllerListener) {
        if (TextUtils.isEmpty(url)) {
            Log.e("FrescoLoadUtil", "loadImageNetWork, url is empty!");
            return;
        }

        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(url))
                //这里设置渐进式jpeg开关，记得在fresco初始化时设置progressiveJpegConfig
                .setProgressiveRenderingEnabled(true)
                //在解码之前修改图片尺寸
                //缩放,在解码前修改内存中的图片大小, 配合Downsampling可以处理所有图片,否则只能处理jpg,
                // 开启Downsampling:在初始化时设置.setDownsampleEnabled(true)
                .setResizeOptions(new ResizeOptions(width, height))
                .build();

        PipelineDraweeControllerBuilder builder = Fresco.newDraweeControllerBuilder()
                .setImageRequest(imageRequest)
                //在构建新的控制器时需要setOldController，这可以防止重新分配内存
                .setOldController(simpleDraweeView.getController())
                //tap-to-retry load image
                .setTapToRetryEnabled(false)
                //是否自动开启gif,webp动画,也可以在ControllerListener下手动启动动画
                .setAutoPlayAnimations(true)
                .setControllerListener(controllerListener);
        DraweeController controller = builder.build();
        simpleDraweeView.setController(controller);
    }


    /**
     * 加载一张网络图片
     *
     * @param simpleDraweeView 　SimpleDraweeView
     * @param widthDP          　widthDP
     * @param heightDP         heightDP
     * @param url              http url
     */
    public void loadImageNetWorkDP(SimpleDraweeView simpleDraweeView, float widthDP, float heightDP, String url) {
        loadImageNetWorkDP(simpleDraweeView, widthDP, heightDP, url, null);
    }

    public void loadImageNetWorkDP(SimpleDraweeView simpleDraweeView, float widthDP, float heightDP, String url, ControllerListener<? super ImageInfo> controllerListener) {
        int w = Dp2Px(simpleDraweeView.getContext().getApplicationContext(), widthDP);
        int h = Dp2Px(simpleDraweeView.getContext().getApplicationContext(), heightDP);
        loadImageNetWork(simpleDraweeView, w, h, url, controllerListener);
    }


    //加载直接返回Bitmap

    /**
     * 加载一张网络图片，返回Bitmap
     *
     * @param url      　http url
     * @param width    　ｗ
     * @param height   ｈ
     * @param callback FrescoBitmapCallback<Bitmap>
     */
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

    private int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}