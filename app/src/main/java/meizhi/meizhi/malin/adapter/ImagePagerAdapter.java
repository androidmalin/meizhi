package meizhi.meizhi.malin.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import meizhi.meizhi.malin.R;
import meizhi.meizhi.malin.network.bean.ImageBean;
import meizhi.meizhi.malin.utils.PhoneScreenUtil;
import uk.co.senab.photoview.PhotoView;

public class ImagePagerAdapter extends PagerAdapter {


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
                Toast.makeText(mContext, "downLoad", Toast.LENGTH_SHORT).show();
            }
        });

        photoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(mContext, "" + position, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        Glide.with(mContext)
                .load(mList.get(position).url + "?imageView2/0/w/500")
                .asBitmap()
                .centerCrop()
                .override(mItemWidth, mItemHeight)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(photoView);

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