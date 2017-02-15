package meizhi.meizhi.malin.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
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
import meizhi.meizhi.malin.application.MApplication;
import meizhi.meizhi.malin.network.bean.ImageBean;
import meizhi.meizhi.malin.utils.CircleProgressBarDrawable;
import meizhi.meizhi.malin.utils.LogUtil;
import meizhi.meizhi.malin.utils.PhoneScreenUtil;
import meizhi.meizhi.malin.utils.ProgressListener;
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
    private ArrayList<ImageBean> mList;
    private LayoutInflater mInflater;
    private int mItemWidth;
    private int mItemHeight;
    private Context mContext;
    private GenericDraweeHierarchyBuilder hierarchyBuilder;
    private CircleProgressBarDrawable mCircleProgressBarDrawable;

    public ImageLargeAdapter(Activity context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mItemWidth = PhoneScreenUtil.getPhoneWidth(MApplication.getInstance());
        mItemHeight = PhoneScreenUtil.getPhoneHeight(MApplication.getInstance());


        hierarchyBuilder = new GenericDraweeHierarchyBuilder(mContext.getResources());
        mCircleProgressBarDrawable =  new CircleProgressBarDrawable();

        //设置淡入淡出动画持续时间
        hierarchyBuilder.setFadeDuration(200)
                //设置占位图及它的缩放方式
                .setPlaceholderImage(ContextCompat.getDrawable(MApplication.getInstance(), R.drawable.place_holder_bg), ScalingUtils.ScaleType.FOCUS_CROP)
                .setActualImageScaleType(ScalingUtils.ScaleType.FOCUS_CROP)
                .setActualImageFocusPoint(new PointF(0.5f, 0.5f))

                //设置失败图，它默认的缩放类型是CENTER_INSIDE
                .setFailureImage(ContextCompat.getDrawable(MApplication.getInstance(), R.drawable.fail_image), ScalingUtils.ScaleType.CENTER)
                //重试
                .setRetryImage(ContextCompat.getDrawable(MApplication.getInstance(), R.drawable.icon_retry), ScalingUtils.ScaleType.CENTER)
                //设置正在加载图及其缩放类型
                //ScalingUtils.ScaleType.FOCUS_CROP
                //ScalingUtils.ScaleType.CENTER
                //ScalingUtils.ScaleType.CENTER_INSIDE
                .setProgressBarImage(mCircleProgressBarDrawable, ScalingUtils.ScaleType.FIT_CENTER);

        mCircleProgressBarDrawable.setProgressListener(new ProgressListener() {
            @Override
            public void update(int number) {
                
            }
        });
    }

    public void addData(List<ImageBean> list) {
        if (mList == null) {
            mList = new ArrayList<>();
        }
        mList.addAll(list);
        notifyDataSetChanged();
    }


    public void clearData() {
        if (mList != null) {
            mList.clear();
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
        if (position >= 0 && position < mList.size()) {
            mList.remove(position);
            notifyItemRemoved(position);
            if (position != mList.size()) {
                notifyItemRangeChanged(position, mList.size() - position);
            }
        }
    }

    private downLoadClickListener mDownLoadClickListener;

    public interface downLoadClickListener {
        void downImageListener(String url, int position, boolean singleClickDown);
    }

    public void setDownLoadListener(downLoadClickListener listener) {
        this.mDownLoadClickListener = listener;
    }


    public int getDataSize() {
        return mList == null ? 0 : mList.size();
    }

    public ArrayList<ImageBean> getData() {
        if (mList == null) {
            mList = new ArrayList<>();
        }
        return mList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewHolder itemViewHolder;
        View view = mInflater.inflate(R.layout.image_large_list_item, parent, false);
        itemViewHolder = new ItemViewHolder(view);
        itemViewHolder.largeImage = (SimpleDraweeView) view.findViewById(R.id.image_large_item_img);
        return itemViewHolder;
    }

    private String imageUrl;

    private ViewGroup.LayoutParams mLayoutParams;

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            mLayoutParams = itemViewHolder.largeImage.getLayoutParams();
            mLayoutParams.width = mItemWidth;
            mLayoutParams.height = mItemHeight;
            itemViewHolder.largeImage.setLayoutParams(mLayoutParams);
            final int pos = getRealPosition(holder);
            ImageBean bean = mList.get(pos);

            if (bean == null || TextUtils.isEmpty(bean.url)) return;
            imageUrl = UrlUtils.getUrl(bean.url, UrlUtils.large);

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
                    if (mDownLoadClickListener != null) {
                        if (mList != null && position < mList.size() && mList.get(position).url != null) {
                            String utrImg = mList.get(position).url;
                            mDownLoadClickListener.downImageListener(utrImg, position, false);
                        }
                    }
                    return true;
                }
            });
        }
    }

    private int getRealPosition(RecyclerView.ViewHolder holder) {
        return holder.getLayoutPosition();
    }

    private void loadImgCode(SimpleDraweeView simpleDraweeView, String url) {

//        AnimationDrawable animationDrawable = new AnimationDrawable();
//        Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.loading_bar);
//        if (drawable != null) {
//            animationDrawable.addFrame(drawable, 200);
//            animationDrawable.setOneShot(false);
//        }

//        simpleDraweeView.setHierarchy(hierarchyBuilder.build());


        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(url))
                //这里设置渐进式jpeg开关，记得在fresco初始化时设置progressiveJpegConfig
                .setProgressiveRenderingEnabled(true)
                //在解码之前修改图片尺寸
                //缩放,在解码前修改内存中的图片大小, 配合Downsampling可以处理所有图片,否则只能处理jpg,
                // 开启Downsampling:在初始化时设置.setDownsampleEnabled(true)
                .setResizeOptions(new ResizeOptions(mItemWidth, mItemHeight))
                .setRotationOptions(RotationOptions.autoRotate())
                .build();


        ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable anim) {
                if (imageInfo == null) return;
                QualityInfo qualityInfo = imageInfo.getQualityInfo();
                LogUtil.d(TAG, "" + imageInfo.getWidth() + "x" + imageInfo.getHeight() + " Quality:" + qualityInfo.getQuality() + " isOfGoodEnoughQuality:" + qualityInfo.isOfGoodEnoughQuality() + " isOfFullQuality:" + qualityInfo.isOfFullQuality());
            }

            @Override
            public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                LogUtil.e(TAG, "Error loading" + id + "\n" + throwable.getLocalizedMessage());
            }
        };

        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
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

    /**
     * 重新启动动画
     * 首先得停止帧动画，然后才能重新执行帧动画，否则没有效果
     *
     * @param drawable
     */
    private void restartAnimation(AnimationDrawable drawable) {
        if (drawable == null) return;
        if (drawable.isRunning()) {
            drawable.stop();
        }
        drawable.start();
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {
        private SimpleDraweeView largeImage;

        private ItemViewHolder(View itemView) {
            super(itemView);
        }
    }
}
