package meizhi.meizhi.malin.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
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
import uk.co.senab.photoview.PhotoView;

public class ImagePagerAdapter extends PagerAdapter {


    private static final String TAG = ImagePagerAdapter.class.getSimpleName();
    private ArrayList<ImageBean> mList;
    private Context mContext;
    private int mItemWidth;
    private int mItemHeight;
    private LayoutInflater mLayoutInflater;


    public void setData(ArrayList<ImageBean> mList, Context context) {
        this.mList = mList;
        this.mContext = context;

        mItemWidth = PhoneScreenUtil.getDeviceWidth(mContext);
        mItemHeight = PhoneScreenUtil.getDeviceHeight(mContext);
        mLayoutInflater = LayoutInflater.from(context);
    }

    private downLoadClickListener mDownLoadClickListener;

    public interface downLoadClickListener {
        void downImageListener(String url);
    }

    public void setDownLoadListener(downLoadClickListener listener) {
        this.mDownLoadClickListener = listener;
    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public View instantiateItem(ViewGroup container, final int position) {

        View rootView = mLayoutInflater.inflate(R.layout.item_pager_image, container, false);
        PhotoView photoView = (PhotoView) rootView.findViewById(R.id.photo_view);


        RelativeLayout downLoad = (RelativeLayout) rootView.findViewById(R.id.rl_download);
        downLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDownLoadClickListener == null) return;
                mDownLoadClickListener.downImageListener(mList.get(position).url);
            }
        });

        final ImageView holder = (ImageView) rootView.findViewById(R.id.iv_holder);

        Glide.with(mContext)
                .load(R.drawable.image_loading_holder_two)
                .asGif()
                .override((int) PhoneScreenUtil.dipToPx(mContext, 100.0f), (int) PhoneScreenUtil.dipToPx(mContext, 100.0f))
                .into(holder);

        Glide.with(mContext)
                .load(mList.get(position).url + "?imageView2/0/w/500")
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    //这个用于监听图片是否加载完成
                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.setVisibility(View.GONE);
                        return false;
                    }
                })
                .centerCrop()
                .override(mItemWidth, mItemHeight)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(photoView);


        photoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mDownLoadClickListener != null) {
                    mDownLoadClickListener.downImageListener(mList.get(position).url);
                }
                return false;
            }
        });


        // Now just add PhotoView to ViewPager and return it
        container.addView(rootView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return rootView;
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