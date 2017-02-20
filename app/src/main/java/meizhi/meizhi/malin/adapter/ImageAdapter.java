package meizhi.meizhi.malin.adapter;

import android.app.Activity;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.image.QualityInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.ArrayList;
import java.util.List;

import meizhi.meizhi.malin.R;
import meizhi.meizhi.malin.network.bean.ImageBean;
import meizhi.meizhi.malin.utils.LogUtil;
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
    private static final String TAG = ImageAdapter.class.getSimpleName();
    private ArrayList<ImageBean> mData;
    private LayoutInflater mInflater;
    private int mItemWidth;
    private int mItemHeight;

    public ImageAdapter(Activity context) {
        mInflater = LayoutInflater.from(context);
        mItemWidth = (int) ((PhoneScreenUtil.getPhoneWidth(context) * 1.0f - PhoneScreenUtil.dipToPx(context, 100.f)) / 2.0f);
        mItemHeight = (int) (mItemWidth * 4.0f / 3.0f);
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
        void itemOnClick(int position);
    }

    public void setOnItemClickListener(itemClickListener listener) {
        this.mItemClickListener = listener;
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
        itemViewHolder.head = (SimpleDraweeView) view.findViewById(R.id.iv_item_list_img);
        return itemViewHolder;
    }

    private String imageUrlLarge;

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
            ImageBean bean = mData.get(pos);

            if (bean == null || TextUtils.isEmpty(bean.url)) return;
            imageUrlLarge = bean.url;

            loadImgCode(itemViewHolder.head, imageUrlLarge);
            itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener == null || imageUrlLarge == null) return;
                    mItemClickListener.itemOnClick(pos);
                }
            });
        }
    }

    private int getRealPosition(RecyclerView.ViewHolder holder) {
        return holder.getLayoutPosition();
    }

    private void loadImgCode(SimpleDraweeView simpleDraweeView, String url) {


        String lowUrl = UrlUtils.getUrl(url, UrlUtils.thumbnail);

        ImageRequest imageRequestLow = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(lowUrl))
                .setProgressiveRenderingEnabled(true)
                .setResizeOptions(new ResizeOptions(mItemWidth, mItemHeight))
                .setRotationOptions(RotationOptions.autoRotate())
                .build();


        String imageHigh = UrlUtils.getUrl(url, UrlUtils.orj360);
        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(imageHigh))
                //这里设置渐进式jpeg开关，记得在fresco初始化时设置progressiveJpegConfig
                .setProgressiveRenderingEnabled(true)
                //在解码之前修改图片尺寸
                //缩放,在解码前修改内存中的图片大小, 配合Downsampling可以处理所有图片,否则只能处理jpg,
                // 开启Downsampling:在初始化时设置.setDownsampleEnabled(true)
                .setResizeOptions(new ResizeOptions(mItemWidth, mItemHeight))
                .setRotationOptions(RotationOptions.autoRotate())
                .build();


        ControllerListener<ImageInfo> controllerListener = new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(
                    String id,
                    @Nullable ImageInfo imageInfo,
                    @Nullable Animatable anim) {
                if (imageInfo == null) {
                    return;
                }
                QualityInfo qualityInfo = imageInfo.getQualityInfo();
                LogUtil.d(TAG, "" + imageInfo.getWidth() + "x" + imageInfo.getHeight() + " Quality:" + qualityInfo.getQuality() + " isOfGoodEnoughQuality:" + qualityInfo.isOfGoodEnoughQuality() + " isOfFullQuality:" + qualityInfo.isOfFullQuality());
            }

            @Override
            public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                LogUtil.e(TAG, "Error loading:" + id + " " + throwable.getLocalizedMessage());
            }
        };

        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setLowResImageRequest(imageRequestLow)
                .setImageRequest(imageRequest)
                //在构建新的控制器时需要setOldController，这可以防止重新分配内存
                .setOldController(simpleDraweeView.getController())
                //tap-to-retry load image
                .setTapToRetryEnabled(true)
                //是否自动开启gif,webp动画,也可以在ControllerListener下手动启动动画
                .setAutoPlayAnimations(true)
                .setControllerListener(controllerListener)
                .build();
        simpleDraweeView.setController(controller);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {
        private SimpleDraweeView head;

        private ItemViewHolder(View itemView) {
            super(itemView);
        }
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

}
