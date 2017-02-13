package meizhi.meizhi.malin.activity;

import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.image.QualityInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import meizhi.meizhi.malin.R;
import meizhi.meizhi.malin.application.MApplication;
import meizhi.meizhi.malin.utils.LogUtil;
import meizhi.meizhi.malin.utils.PhoneScreenUtil;

/**
 * @author root on 17-2-13.
 */

public class TestActivity extends AppCompatActivity {

    private static final String TAG = TestActivity.class.getSimpleName();
    SimpleDraweeView mSimpleDraweeView;

    private int mItemWidth;
    private int mItemHeight;
    private static final String IMAGE_URL = "http://ww3.sinaimg.cn/large/610dc034gw1fbsfgssfrwj20u011h48y.jpg";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);
        mSimpleDraweeView = (SimpleDraweeView) findViewById(R.id.simple_fresco_img);

        mItemWidth = PhoneScreenUtil.getPhoneWidth(MApplication.getInstance());
        mItemHeight = PhoneScreenUtil.getPhoneHeight(MApplication.getInstance());

        loadImgCode(mSimpleDraweeView, IMAGE_URL);
    }

    private void loadImgCode(SimpleDraweeView simpleDraweeView, String url) {

        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(url))
                //这里设置渐进式jpeg开关，记得在fresco初始化时设置progressiveJpegConfig
                .setProgressiveRenderingEnabled(true)
                //在解码之前修改图片尺寸
                //缩放,在解码前修改内存中的图片大小, 配合Downsampling可以处理所有图片,否则只能处理jpg,
                // 开启Downsampling:在初始化时设置.setDownsampleEnabled(true)
                .setResizeOptions(new ResizeOptions(mItemWidth, mItemHeight))
                .setRotationOptions(RotationOptions.autoRotate())
                .build();


        ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(
                    String id,
                    @Nullable ImageInfo imageInfo,
                    @Nullable Animatable anim) {
                if (imageInfo == null) {
                    return;
                }
                QualityInfo qualityInfo = imageInfo.getQualityInfo();
                LogUtil.d(TAG, "" + imageInfo.getWidth() + "x" + imageInfo.getHeight() + " Quality:" + qualityInfo.getQuality() + " isOfGoodEnoughQuality:" + qualityInfo.isOfGoodEnoughQuality() + " isOfFullQuality:" + qualityInfo.isOfFullQuality());
            }

            @Override
            public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                LogUtil.e(TAG, "Error loading" + id + "\n" + throwable.getLocalizedMessage());
            }
        };

        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setImageRequest(imageRequest)
                //在构建新的控制器时需要setOldController，这可以防止重新分配内存
                .setOldController(simpleDraweeView.getController())
                //tap-to-retry load image
                .setTapToRetryEnabled(true)
                //是否自动开启gif,webp动画,也可以在ControllerListener下手动启动动画
                .setAutoPlayAnimations(true)
                .setControllerListener(controllerListener)
                .build();
        simpleDraweeView.setController(controller);
    }
}
