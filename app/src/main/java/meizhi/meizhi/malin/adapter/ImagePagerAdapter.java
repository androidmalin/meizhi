package meizhi.meizhi.malin.adapter;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;
import java.util.List;

import me.relex.photodraweeview.Attacher;
import me.relex.photodraweeview.OnViewTapListener;
import me.relex.photodraweeview.PhotoDraweeView;
import meizhi.meizhi.malin.network.bean.ImageBean;
import meizhi.meizhi.malin.utils.PhoneScreenUtil;
import meizhi.meizhi.malin.utils.UrlUtils;

public class ImagePagerAdapter extends PagerAdapter {

    private ArrayList<ImageBean> mList;
    private Context mContext;
    private int mItemWidth;
    private int mItemHeight;
    private String mImageUrl;

    private List<PhotoDraweeView> mViews = new ArrayList<>();

    private PhotoDraweeView mImageViewItem;

    public void setData(ArrayList<ImageBean> mList, Context context) {
        this.mList = mList;
        this.mContext = context;

        mItemWidth = PhoneScreenUtil.getPhoneWidth(mContext);
        mItemHeight = PhoneScreenUtil.getPhoneHeight(mContext);

        mLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mLayoutParams.width = mItemWidth;
        mLayoutParams.height = mItemHeight;
        mLayoutParams.gravity = Gravity.CENTER;

        for (int i = 0; i < 4; i++) {
            mImageViewItem = new PhotoDraweeView(mContext);
            mImageViewItem.setLayoutParams(mLayoutParams);
            mViews.add(mImageViewItem);
        }
    }

    public ArrayList<ImageBean> getData() {
        if (mList == null) {
            mList = new ArrayList<>();
        }
        return mList;
    }

    private downLoadClickListener mDownLoadClickListener;

    public interface downLoadClickListener {
        void downImageListener(String url, int position, boolean singleClickDown);
    }

    public void setDownLoadListener(downLoadClickListener listener) {
        this.mDownLoadClickListener = listener;
    }


    public interface photoViewTapListener {
        void viewTapListener(View view, float x, float y);
    }

    private photoViewTapListener mPhotoViewTapListener;

    public void setPhotoViewTapListener(photoViewTapListener listener) {
        this.mPhotoViewTapListener = listener;
    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    private FrameLayout.LayoutParams mLayoutParams;

    private Attacher mAttacher;

    private imageDownLoadListener mImageDownLoadListener;

    public interface imageDownLoadListener {
        void downLoadFailure(int position, String url);

        void downLoadSuccess(int position, String url);

        void downLoadPrepare();
    }

    public void setProgressBarListener(imageDownLoadListener listener) {
        this.mImageDownLoadListener = listener;
    }


    private ImageRequest mImageRequest;
    private PipelineDraweeController mPipelineDraweeController;
    private PhotoDraweeView mPhotoDraweeView;

    @Override
    public View instantiateItem(ViewGroup container, final int position) {


        int i = position % 4;
        mPhotoDraweeView = mViews.get(i);
        mPhotoDraweeView.setLayoutParams(mLayoutParams);
        if (mList != null && mList.get(position) != null && mList.get(position).url != null) {
            mImageUrl = UrlUtils.getUrl(mList.get(position).url, UrlUtils.large);
            if (mImageDownLoadListener != null) {
                mImageDownLoadListener.downLoadPrepare();
            }
            mImageRequest = ImageRequestBuilder
                    .newBuilderWithSource(Uri.parse(mImageUrl))
                    //这里设置渐进式jpeg开关，记得在fresco初始化时设置progressiveJpegConfig
                    .setProgressiveRenderingEnabled(true)
                    //在解码之前修改图片尺寸
                    .setResizeOptions(new ResizeOptions(mItemWidth, mItemHeight))
                    .setRotationOptions(RotationOptions.autoRotate())
                    .build();


            mPipelineDraweeController = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                    .setImageRequest(mImageRequest)
                    //在构建新的控制器时需要setOldController，这可以防止重新分配内存
                    .setOldController(mPhotoDraweeView.getController())
                    //tap-to-retry load image
                    .setTapToRetryEnabled(true)
                    //是否自动开启gif,webp动画,也可以在ControllerListener下手动启动动画
                    .setAutoPlayAnimations(true)
                    .setControllerListener(new BaseControllerListener<ImageInfo>() {
                        @Override
                        public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                            super.onFinalImageSet(id, imageInfo, animatable);
                            if (imageInfo == null) {
                                return;
                            }
                            mPhotoDraweeView.update(imageInfo.getWidth(), imageInfo.getHeight());
//                            mPhotoDraweeView.update(mItemWidth, mItemHeight);
                            mImageDownLoadListener.downLoadSuccess(position, mImageUrl);
                        }

                        @Override
                        public void onFailure(String id, Throwable throwable) {
                            super.onFailure(id, throwable);
                            mImageDownLoadListener.downLoadFailure(position, mImageUrl);
                        }
                    })
                    .build();
            mPhotoDraweeView.setController(mPipelineDraweeController);

            mAttacher = mPhotoDraweeView.getAttacher();
            if (mAttacher != null) {
                mAttacher.setOnViewTapListener(new OnViewTapListener() {
                    @Override
                    public void onViewTap(View view, float x, float y) {
                        if (mPhotoViewTapListener == null) return;
                        mPhotoViewTapListener.viewTapListener(view, x, y);
                    }
                });
                mAttacher.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        if (mDownLoadClickListener != null) {
                            if (mList != null && position < mList.size() && mList.get(position).url != null) {
                                String utrImg = mList.get(position).url;
                                mDownLoadClickListener.downImageListener(utrImg, position, false);
                            }
                        }
                        return false;
                    }
                });

            }

        }
        // Now just add PhotoView to ViewPager and return it

        ViewGroup parent = (ViewGroup) mPhotoDraweeView.getParent();
        if (parent != null) {
            parent.removeView(mPhotoDraweeView);
        }

        container.addView(mPhotoDraweeView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return mPhotoDraweeView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        int i = position % 4;
        ImageView imageView = mViews.get(i);
        container.removeView(imageView);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    private void releaseImageView(View view) {
        if (view == null) return;
        if (view instanceof ImageView) {
            try {
                ImageView imageView = (ImageView) view;
                Drawable drawable = imageView.getDrawable();
                if (drawable != null) {
                    drawable.setCallback(null);
                }
                imageView.setImageDrawable(null);
                imageView.setImageBitmap(null);
            } catch (Throwable e) {
                CrashReport.postCatchedException(e);
            }
        }
    }
}