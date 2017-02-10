package meizhi.meizhi.malin.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;
import java.util.List;

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

    private List<ImageView> mViews = new ArrayList<>();

    private ImageView mImageViewItem;

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
            mImageViewItem = new ImageView(mContext);
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

    private PhotoViewAttacher mAttacher;

    private imageDownLoadListener mImageDownLoadListener;

    public interface imageDownLoadListener {
        void downLoadFailure(int position, String url);

        void downLoadSuccess(int position, String url);

        void downLoadPrepare();
    }

    public void setProgressBarListener(imageDownLoadListener listener) {
        this.mImageDownLoadListener = listener;
    }


    @Override
    public View instantiateItem(ViewGroup container, final int position) {


        int i = position % 4;
        ImageView imageView = mViews.get(i);

        mAttacher = new PhotoViewAttacher(imageView);
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
        imageView.setLayoutParams(mLayoutParams);
        if (mList != null && mList.get(position) != null && mList.get(position).url != null) {
            mImageUrl = UrlUtils.getUrl(mList.get(position).url, UrlUtils.large);
            if (mImageDownLoadListener != null) {
                mImageDownLoadListener.downLoadPrepare();
            }
            Glide.with(mContext)
                    .load(mImageUrl)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            if (mImageDownLoadListener != null) {
                                mImageDownLoadListener.downLoadFailure(position, mImageUrl);
                            }
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            if (mImageDownLoadListener != null) {
                                mImageDownLoadListener.downLoadSuccess(position, mImageUrl);
                            }
                            return false;
                        }
                    })
                    .centerCrop()
                    .override(mItemWidth, mItemHeight)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(imageView);
        }
        // Now just add PhotoView to ViewPager and return it

        ViewGroup parent = (ViewGroup) imageView.getParent();
        if (parent != null) {
            parent.removeView(imageView);
        }

        container.addView(imageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return imageView;
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