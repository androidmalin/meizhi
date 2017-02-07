package meizhi.meizhi.malin.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

import meizhi.meizhi.malin.R;
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
    private LayoutInflater mLayoutInflater;
    private String mImageUrl;

    public void setData(ArrayList<ImageBean> mList, Context context) {
        this.mList = mList;
        this.mContext = context;

        mItemWidth = PhoneScreenUtil.getPhoneWidth(mContext);
        mItemHeight = PhoneScreenUtil.getPhoneHeight(mContext);
        mLayoutInflater = LayoutInflater.from(context);
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


    @Override
    public View instantiateItem(ViewGroup container, final int position) {

        View mRootView = mLayoutInflater.inflate(R.layout.item_pager_image, container, false);
        ImageView photoView = (ImageView) mRootView.findViewById(R.id.photo_view);
        mAttacher = new PhotoViewAttacher(photoView);
        mAttacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                // 隐藏Toolbar等操作
                if (mPhotoViewTapListener == null) return;
                mPhotoViewTapListener.viewTapListener(view, x, y);
            }
        });
        mAttacher.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (mDownLoadClickListener != null) {
                    String utrImg = UrlUtils.getUrl(mList.get(position).url, UrlUtils.large);
                    mDownLoadClickListener.downImageListener(utrImg, position, false);
                }
                return false;
            }
        });
        mAttacher.update();

        mLayoutParams = photoView.getLayoutParams();
        mLayoutParams.width = mItemWidth;
        mLayoutParams.height = mItemHeight;
        Log.d(TAG, "mItemWidth:" + mItemWidth);
        Log.d(TAG, "mItemHeight:" + mItemHeight);

        photoView.setLayoutParams(mLayoutParams);

        RelativeLayout downLoad = (RelativeLayout) mRootView.findViewById(R.id.rl_download);
        downLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDownLoadClickListener == null) return;
                String utrImg = UrlUtils.getUrl(mList.get(position).url, UrlUtils.large);
                mDownLoadClickListener.downImageListener(utrImg, position, true);
            }
        });

        final ImageView imgHolder = (ImageView) mRootView.findViewById(R.id.iv_holder);

        Glide.with(mContext)
                .load(R.drawable.image_loading_holder)
                .asGif()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .override((int) PhoneScreenUtil.dipToPx(mContext, 100.0f), (int) PhoneScreenUtil.dipToPx(mContext, 100.0f))
                .into(imgHolder);

        if (mList != null && mList.get(position) != null && mList.get(position).url != null) {
            mImageUrl = UrlUtils.getUrl(mList.get(position).url, UrlUtils.large);
            Glide.with(mContext)
                    .load(mImageUrl)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            imgHolder.setVisibility(View.GONE);
                            return false;
                        }

                        //这个用于监听图片是否加载完成
                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            imgHolder.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .centerCrop()
                    .override(mItemWidth, mItemHeight)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(photoView);
        } else {
            imgHolder.setVisibility(View.GONE);
        }


        // Now just add PhotoView to ViewPager and return it
        container.addView(mRootView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return mRootView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}