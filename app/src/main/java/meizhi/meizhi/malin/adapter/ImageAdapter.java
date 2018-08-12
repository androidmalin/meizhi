package meizhi.meizhi.malin.adapter;

import android.app.Activity;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

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
public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = ImageAdapter.class.getSimpleName();
    private ArrayList<String> mData;
    private LayoutInflater mInflater;
    private static int mItemWidth;
    private static int mItemHeight;

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;


    public static final int LOADING_MORE = 1;  //加载中
    public static final int NO_LOAD_MORE = 2;  //数据全部加载完毕
    public static final int NO_LOAD_ERROR = 3;  //加载失败
    private int mLoadMoreStatus = 1;  //上拉加载更多状态-默认为0


    public ImageAdapter(Activity context) {
        mInflater = LayoutInflater.from(context);
        mItemWidth = (int) ((PhoneScreenUtil.getPhoneWidth() * 1.0f - PhoneScreenUtil.dipToPx(100.f)) / 2.0f);
        mItemHeight = (int) (mItemWidth * 4.0f / 3.0f);
    }

    public void addData(List<String> list) {
        if (mData == null) mData = new ArrayList<>();
        mData.addAll(list);
    }


    public void clearData() {
        if (mData == null) return;
        mData.clear();
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

    public ArrayList<String> getData() {
        if (mData == null) mData = new ArrayList<>();
        return mData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = mInflater.inflate(R.layout.image_list_item, parent, false);
            return new ItemViewHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            View footView = mInflater.inflate(R.layout.load_foot_view_layout, parent, false);
            return new FooterViewHolder(footView);
        }
        return null;
    }

    private String imageUrlLarge;

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            final int pos = getRealPosition(holder);
            String bean = mData.get(pos);

            if (TextUtils.isEmpty(bean)) return;
            imageUrlLarge = bean;

            loadImgCode(itemViewHolder.head, imageUrlLarge);
            itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener == null || imageUrlLarge == null) return;
                    mItemClickListener.itemOnClick(pos);
                }
            });
        } else if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            switch (mLoadMoreStatus) {
                case LOADING_MORE: {
                    footerViewHolder.mLoadProgressBar.setVisibility(View.VISIBLE);
                    footerViewHolder.mTvLoadText.setText(R.string.load_more_loading);
                    break;
                }
                case NO_LOAD_MORE: {
                    footerViewHolder.mLoadProgressBar.setVisibility(View.INVISIBLE);
                    footerViewHolder.mTvLoadText.setText(R.string.load_more_no_data);
                    break;
                }

                case NO_LOAD_ERROR: {
                    footerViewHolder.mLoadProgressBar.setVisibility(View.INVISIBLE);
                    footerViewHolder.mTvLoadText.setText(R.string.load_more_error_txt);
                }

            }
        }
    }

    private int getRealPosition(RecyclerView.ViewHolder holder) {
        return holder.getLayoutPosition();
    }

    private void loadImgCode(SimpleDraweeView simpleDraweeView, String url) {


        String lowUrl = UrlUtils.getUrl(url, UrlUtils.thumbnail);

        ImageRequest imageRequestLow = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(lowUrl))
                .setResizeOptions(new ResizeOptions(mItemWidth, mItemHeight))
                .build();


        String imageHigh = UrlUtils.getUrl(url, UrlUtils.orj360);
        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(imageHigh))
                //在解码之前修改图片尺寸
                //缩放,在解码前修改内存中的图片大小, 配合Downsampling可以处理所有图片,否则只能处理jpg,
                // 开启Downsampling:在初始化时设置.setDownsampleEnabled(true)
                .setResizeOptions(new ResizeOptions(mItemWidth, mItemHeight))
                .build();

        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setLowResImageRequest(imageRequestLow)
                .setImageRequest(imageRequest)
                //在构建新的控制器时需要setOldController，这可以防止重新分配内存
                .setOldController(simpleDraweeView.getController())
                //tap-to-retry load image
                .setTapToRetryEnabled(true)
                //是否自动开启gif,webp动画,也可以在ControllerListener下手动启动动画
                .setAutoPlayAnimations(true)
                .build();
        simpleDraweeView.setController(controller);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {

        if (position + 1 == getItemCount()) {
            //最后一个item设置为footerView
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {
        private SimpleDraweeView head;

        private ItemViewHolder(View itemView) {
            super(itemView);
            head = itemView.findViewById(R.id.iv_item_list_img);
            ViewGroup.LayoutParams mLayoutParams = head.getLayoutParams();
            mLayoutParams.width = mItemWidth;
            mLayoutParams.height = mItemHeight;
            head.setLayoutParams(mLayoutParams);
        }
    }


    private class FooterViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar mLoadProgressBar;
        private TextView mTvLoadText;

        private FooterViewHolder(View itemView) {
            super(itemView);
            mTvLoadText = itemView.findViewById(R.id.tv_foot_view);
            mLoadProgressBar = itemView.findViewById(R.id.pd_foot_view);
        }
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();

        if (manager == null || !(manager instanceof GridLayoutManager)) return;
        final GridLayoutManager gridManager = ((GridLayoutManager) manager);
        gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                //当前位置是FooterView的位置，那么该item占据2个单元格，正常情况下占据1个单元格
                return getItemViewType(position) == TYPE_FOOTER ? gridManager.getSpanCount() : 1;
            }
        });
    }


    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp == null || !(lp instanceof StaggeredGridLayoutManager.LayoutParams)) return;
        StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
        p.setFullSpan(holder.getLayoutPosition() == getDataSize());
    }

    private Handler mHandler = new Handler();
    private Runnable mRunnable;

    /**
     * 更新加载更多状态
     *
     * @param status status
     */
    public void changeMoreStatus(int status) {
        mLoadMoreStatus = status;
        mRunnable = new Runnable() {
            public void run() {
                notifyDataSetChanged();
            }
        };
        mHandler.post(mRunnable);
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


    public void destroyData() {
        if (mData != null) {
            mData.clear();
            mData = null;
        }
        if (mHandler != null) {
            if (mRunnable != null) {
                mHandler.removeCallbacks(mRunnable);
            }
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
            mRunnable = null;
        }
    }

}
