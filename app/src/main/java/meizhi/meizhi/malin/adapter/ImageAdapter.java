package meizhi.meizhi.malin.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import meizhi.meizhi.malin.R;
import meizhi.meizhi.malin.fragment.ImageListFragment;
import meizhi.meizhi.malin.network.bean.ImageBean;
import meizhi.meizhi.malin.utils.PhoneScreenUtil;
import meizhi.meizhi.malin.utils.UrlUtils;

/**
 * 类描述:
 * 创建人:malin.myemail@163.com
 * 创建时间:2017/01/31 18:21
 * 备注:{@link }
 * 修改人:
 * 修改时间:
 * 修改备注:
 * 版本:
 */
public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<ImageBean> mData;
    private LayoutInflater mInflater;
    private int mItemWidth;
    private int mItemHeight;
    private ImageListFragment mFragment;

    public ImageAdapter(Activity context, ImageListFragment fragment) {
        mInflater = LayoutInflater.from(context);
        mItemWidth = (int) ((PhoneScreenUtil.getPhoneWidth(context) * 1.0f - PhoneScreenUtil.dipToPx(context, 100.f)) / 2.0f);
        mItemHeight = (int) (mItemWidth * 4.0f / 3.0f);
        mFragment = fragment;
    }

    public void addData(List<ImageBean> list) {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        mData.addAll(list);
        notifyDataSetChanged();
    }


    public void clearData() {
        if (mData != null) {
            mData.clear();
        }
        notifyDataSetChanged();
    }

    private itemClickListener mItemClickListener;


    public interface itemClickListener {
        void itemOnClick(String imageUrl, int position);
    }

    public void setOnItemClickListener(itemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public void removeData(int position) {
        if (position >= 0 && position < mData.size()) {
            mData.remove(position);
            notifyItemRemoved(position);
            if (position != mData.size()) {
                notifyItemRangeChanged(position, mData.size() - position);
            }
        }
    }


    public int getDataSize() {
        return mData == null ? 0 : mData.size();
    }

    public ArrayList<ImageBean> getData() {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        return mData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewHolder itemViewHolder;
        View view = mInflater.inflate(R.layout.image_list_item, parent, false);
        itemViewHolder = new ItemViewHolder(view);
        itemViewHolder.head = (ImageView) view.findViewById(R.id.xy_room_fans_avatar_iv);
        return itemViewHolder;
    }

    private String imageUrl;

    private ViewGroup.LayoutParams mLayoutParams;

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            mLayoutParams = itemViewHolder.head.getLayoutParams();
            mLayoutParams.width = mItemWidth;
            mLayoutParams.height = mItemHeight;
            itemViewHolder.head.setLayoutParams(mLayoutParams);
            final int pos = getRealPosition(holder);
            final ImageBean bean = mData.get(pos);

            if (bean == null || TextUtils.isEmpty(bean.url)) return;

            imageUrl = UrlUtils.getUrl(bean.url, UrlUtils.orj360);

            Glide.with(mFragment)
                    .load(imageUrl)
                    .asBitmap()
                    .centerCrop()
                    .override(mItemWidth, mItemHeight)
                    //.diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(itemViewHolder.head);
            itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener == null || imageUrl == null) return;
                    mItemClickListener.itemOnClick(imageUrl, pos);
                }
            });
        }
    }

    private int getRealPosition(RecyclerView.ViewHolder holder) {
        return holder.getLayoutPosition();
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView head;

        private ItemViewHolder(View itemView) {
            super(itemView);
        }
    }
}
