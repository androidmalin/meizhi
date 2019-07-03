package meizhi.meizhi.malin.adapter;

import android.app.Activity;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.ArrayList;
import java.util.List;

import meizhi.meizhi.malin.R;
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
public class ImageLargeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = ImageLargeAdapter.class.getSimpleName();
    private ArrayList<String> mList;
    private LayoutInflater mInflater;
    private int mItemWidth;
    private int mItemHeight;

    public ImageLargeAdapter(Activity context) {
        mInflater = LayoutInflater.from(context);
        mItemWidth = PhoneScreenUtil.getPhoneWidth();
        mItemHeight = PhoneScreenUtil.getPhoneHeight();
    }

    public void addData(List<String> list) {
        if (mList == null) {
            mList = new ArrayList<>();
        }
        mList.addAll(list);
        notifyDataSetChanged();
    }

    private itemClickListener mItemClickListener;


    public interface itemClickListener {
        void itemOnClick(String imageUrl, int position);
    }

    public void setOnItemClickListener(itemClickListener listener) {
        this.mItemClickListener = listener;
    }

    private downLoadClickListener mDownLoadClickListener;

    public interface downLoadClickListener {
        void downImageListener(String url, int position, boolean singleClickDown);
    }

    public void setDownLoadListener(downLoadClickListener listener) {
        this.mDownLoadClickListener = listener;
    }


    public ArrayList<String> getData() {
        if (mList == null) {
            mList = new ArrayList<>();
        }
        return mList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.image_large_list_item, parent, false);
        return new ItemViewHolder(view);
    }

    private String imageUrl;


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            ViewGroup.LayoutParams layoutParams = itemViewHolder.largeImage.getLayoutParams();
            layoutParams.width = mItemWidth;
            layoutParams.height = mItemHeight;
            itemViewHolder.largeImage.setLayoutParams(layoutParams);
            final int pos = getRealPosition(holder);
            String bean = mList.get(pos);

            if (TextUtils.isEmpty(bean)) return;
            imageUrl = bean;

            loadImgCode(itemViewHolder.largeImage, imageUrl);
            itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener == null || imageUrl == null) return;
                    mItemClickListener.itemOnClick(imageUrl, pos);
                }
            });

            itemViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (mDownLoadClickListener == null) return true;
                    if (mList == null || pos >= mList.size() || TextUtils.isEmpty(mList.get(pos)))
                        return true;
                    String utrImg = mList.get(pos);
                    mDownLoadClickListener.downImageListener(utrImg, pos, false);
                    return true;
                }
            });
        }
    }

    private int getRealPosition(RecyclerView.ViewHolder holder) {
        return holder.getLayoutPosition();
    }

    private void loadImgCode(SimpleDraweeView simpleDraweeView, String url) {


        String lowUrl = UrlUtils.getUrl(url, UrlUtils.orj360);

        ImageRequest imageRequestLow = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(lowUrl))
                .setResizeOptions(new ResizeOptions(mItemWidth, mItemHeight))
                .build();

        String imageUrlHigh = UrlUtils.getUrl(url, UrlUtils.large);
        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(imageUrlHigh))
                .setResizeOptions(new ResizeOptions(mItemWidth, mItemHeight))
                .build();


        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setLowResImageRequest(imageRequestLow)
                .setImageRequest(imageRequest)
                //在构建新的控制器时需要setOldController，这可以防止重新分配内存
                .setOldController(simpleDraweeView.getController())
                .setTapToRetryEnabled(true)
                .setAutoPlayAnimations(true)
                .build();
        simpleDraweeView.setController(controller);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {
        private SimpleDraweeView largeImage;

        private ItemViewHolder(View itemView) {
            super(itemView);
            largeImage = itemView.findViewById(R.id.image_large_item_img);
        }
    }

    public void clearData() {
        if (mList != null) {
            mList.clear();
        }
        notifyDataSetChanged();
    }


    public void destroyData() {
        if (mList != null) {
            mList.clear();
            mList = null;
        }
    }

    public int getDataSize() {
        return mList == null ? 0 : mList.size();
    }

    public void removeData(int position) {
        if (position >= 0 && position < mList.size()) {
            mList.remove(position);
            notifyItemRemoved(position);
            if (position != mList.size()) {
                notifyItemRangeChanged(position, mList.size() - position);
            }
        }
    }
}
