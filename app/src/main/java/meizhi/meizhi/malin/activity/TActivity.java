package meizhi.meizhi.malin.activity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import meizhi.meizhi.malin.R;

/**
 * @author root on 17-3-15.
 */

public class TActivity extends AppCompatActivity {

    private SimpleDraweeView simpleDraweeView;
    private ImageView mImageView;
    private static final String IMAGE_URL = "http://ww2.sinaimg.cn/large/7a8aed7bgw1es8c7ucr0rj20hs0qowhl.jpg";
    private int mViewWidth;
    private int mViewHeight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tlayout);
        simpleDraweeView = (SimpleDraweeView) findViewById(R.id.image_tt);
        mImageView = (ImageView) findViewById(R.id.image_image);

        FrescoLoadUtil.getInstance().loadImageBitmap(IMAGE_URL, 100, 100, new FrescoBitmapCallback<Bitmap>() {
            @Override
            public void onSuccess(Uri uri, Bitmap result) {
                Toast.makeText(TActivity.this, "onSuccess", Toast.LENGTH_SHORT).show();
                mImageView.setImageBitmap(result);
            }

            @Override
            public void onFailure(Uri uri, Throwable throwable) {
                Toast.makeText(TActivity.this, "onFailure", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(Uri uri) {
                Toast.makeText(TActivity.this, "onCancel", Toast.LENGTH_SHORT).show();
            }
        });

//        int ww = simpleDraweeView.getMeasuredWidth();
//        int hh = simpleDraweeView.getMeasuredHeight();
//
//        Toast.makeText(this, "w:"+ww+" "+"h:"+hh, Toast.LENGTH_SHORT).show();


        mViewWidth = simpleDraweeView.getLayoutParams().width;
        mViewHeight = simpleDraweeView.getLayoutParams().height;
//        simpleDraweeView.post(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(TActivity.this, "w:"+simpleDraweeView.getWidth()+" "+"h:"+simpleDraweeView.getHeight(), Toast.LENGTH_SHORT).show();
//            }
//        });

//        ViewTreeObserver vto = simpleDraweeView.getViewTreeObserver();
//        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            public boolean onPreDraw() {
//                mViewWidth = simpleDraweeView.getMeasuredWidth();
//                mViewHeight = simpleDraweeView.getMeasuredHeight();
//                return true;
//            }
//        });
        Toast.makeText(this, "w:"+mViewWidth+" "+"h:"+mViewHeight, Toast.LENGTH_SHORT).show();
        loadImgCode(simpleDraweeView,IMAGE_URL,100,100);
    }




    private void loadImgCode(SimpleDraweeView simpleDraweeView, String url, int w, int h) {
        mViewWidth = simpleDraweeView.getLayoutParams().width;
        mViewHeight = simpleDraweeView.getLayoutParams().height;
        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(url))
                //这里设置渐进式jpeg开关，记得在fresco初始化时设置progressiveJpegConfig
                .setProgressiveRenderingEnabled(true)
                //在解码之前修改图片尺寸
                //缩放,在解码前修改内存中的图片大小, 配合Downsampling可以处理所有图片,否则只能处理jpg,
                // 开启Downsampling:在初始化时设置.setDownsampleEnabled(true)
                .setResizeOptions(new ResizeOptions(w, h))
                .build();

        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setImageRequest(imageRequest)
                //在构建新的控制器时需要setOldController，这可以防止重新分配内存
                .setOldController(simpleDraweeView.getController())
                //tap-to-retry load image
                .setTapToRetryEnabled(false)
                //是否自动开启gif,webp动画,也可以在ControllerListener下手动启动动画
                .setAutoPlayAnimations(true)
                .build();
        simpleDraweeView.setController(controller);
    }

}
