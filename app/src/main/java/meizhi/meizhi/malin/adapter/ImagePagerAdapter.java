package meizhi.meizhi.malin.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import meizhi.meizhi.malin.network.bean.ImageBean;
import meizhi.meizhi.malin.utils.PhoneScreenUtil;
import uk.co.senab.photoview.PhotoView;

public class ImagePagerAdapter extends PagerAdapter {


    private ArrayList<ImageBean> mList;
    private int mCurrent;
    private Context mContext;
    private int mItemWidth;
    private int mItemHeight;

    public void setData(ArrayList<ImageBean> mList, int current, Context context) {
        this.mList = mList;
        this.mCurrent = current;
        this.mContext = context;

        mItemWidth = PhoneScreenUtil.getDeviceWidth(mContext);
        mItemHeight = PhoneScreenUtil.getDeviceHeight(mContext);
    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(container.getContext());

        Glide.with(mContext)
                .load(mList.get(position).url + "?imageView2/0/w/500")
                .asBitmap()
                .centerCrop()
                .override(mItemWidth, mItemHeight)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(photoView);

        // Now just add PhotoView to ViewPager and return it
        container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return photoView;
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