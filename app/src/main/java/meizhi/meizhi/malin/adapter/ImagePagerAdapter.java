package meizhi.meizhi.malin.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;

import meizhi.meizhi.malin.network.bean.ImageBean;
import meizhi.meizhi.malin.utils.PhoneScreenUtil;
import meizhi.meizhi.malin.utils.UrlUtils;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ImagePagerAdapter extends PagerAdapter {


    private static final String TAG = ImagePagerAdapter.class.getSimpleName();
    private ArrayList<ImageBean> mList;
    private Context mContext;
    private int mItemWidth;
    private int mItemHeight;
    private String mImageUrl;

    public void setData(ArrayList<ImageBean> mList, Context context) {
        this.mList = mList;
        this.mContext = context;

        mItemWidth = PhoneScreenUtil.getPhoneWidth(mContext);
        mItemHeight = PhoneScreenUtil.getPhoneHeight(mContext);
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

    private ViewGroup.LayoutParams mLayoutParams;

    private PhotoViewAttacher mAttacher;
    private ImageView photoView;

    private imageDownLoadListener mImageDownLoadListener;

    public interface imageDownLoadListener {
        void downLoadFailure(int position,String url);

        void downLoadSuccess(int position,String url);

        void downLoadPrepare(int position,String url);
    }

    public void setProgressBarListener(imageDownLoadListener listener) {
        this.mImageDownLoadListener = listener;
    }


    @Override
    public View instantiateItem(ViewGroup container, final int position) {
        photoView = new ImageView(mContext);
        mAttacher = new PhotoViewAttacher(photoView);
        mAttacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
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
        mAttacher.update();

        mLayoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mLayoutParams.width = mItemWidth;
        mLayoutParams.height = mItemHeight;

        photoView.setLayoutParams(mLayoutParams);
        if (mList != null && mList.get(position) != null && mList.get(position).url != null) {
            mImageUrl = UrlUtils.getUrl(mList.get(position).url, UrlUtils.large);
            if (mImageDownLoadListener != null) {
                mImageDownLoadListener.downLoadPrepare(position,mImageUrl);
            }

            Glide.with(mContext).load(mImageUrl)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .centerCrop()
                    .override(mItemWidth, mItemHeight)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            if (mImageDownLoadListener != null) {
                                mImageDownLoadListener.downLoadFailure(position,mImageUrl);
                            }
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            if (mImageDownLoadListener != null) {
                                mImageDownLoadListener.downLoadSuccess(position,mImageUrl);
                            }
                            return false;
                        }
                    })
                    .into(new GlideDrawableImageViewTarget(photoView) { //加载失败
                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            super.onLoadFailed(e, errorDrawable);
                            //LogUtil.d(TAG, "onLoadFailed");
                        }

                        @Override
                        public void onLoadStarted(Drawable placeholder) {
                            super.onLoadStarted(placeholder);
                            //LogUtil.d(TAG, "onLoadStarted");
                            if (mImageDownLoadListener != null) {
                                mImageDownLoadListener.downLoadPrepare(position,mImageUrl);
                            }
                        }

                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                            super.onResourceReady(resource, animation);
                            //LogUtil.d(TAG, "onResourceReady");
                        }
                    });

//            Glide.with(mContext)
//                    .load(mImageUrl)
//                    .listener(new RequestListener<String, GlideDrawable>() {
//                        @Override
//                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                            if (mImageDownLoadListener != null) {
//                                mImageDownLoadListener.downLoadFailure(position,mImageUrl);
//                            }
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                            if (mImageDownLoadListener != null) {
//                                mImageDownLoadListener.downLoadSuccess(position,mImageUrl);
//                            }
//                            return false;
//                        }
//                    })
//                    .centerCrop()
//                    .override(mItemWidth, mItemHeight)
//                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
//                    .into(photoView);
        }
        // Now just add PhotoView to ViewPager and return it
        container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return photoView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        //releaseImageView(view);
        container.removeView(view);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    private void releaseImageView(View view) {
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