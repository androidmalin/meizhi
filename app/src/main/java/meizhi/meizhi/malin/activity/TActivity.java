package meizhi.meizhi.malin.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import meizhi.meizhi.malin.R;
import meizhi.meizhi.malin.utils.PhoneScreenUtil;

/**
 * @author root on 17-3-15.
 */

public class TActivity extends AppCompatActivity {

    private SimpleDraweeView mSVRoundView;
    private SimpleDraweeView mSVLocalView;
    private SimpleDraweeView mSVLocalFileView;
    private SimpleDraweeView mSVJavaRoundView;
    private ImageView mImageView;
    private static final String IMAGE_URL = "http://ww2.sinaimg.cn/large/7a8aed7bgw1es8c7ucr0rj20hs0qowhl.jpg";
    private int mViewWidth;
    private int mViewHeight;
    private String TAG = TActivity.class.getSimpleName();
    private LinearLayout mLinerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tlayout);
        initView();
        loadSVRoundView();
        getBitmap();
        loadLocalData();
        testWH();
        javaRound();
        newSimpleView();
        String dir = getSignSubDir(this, "md5", "level_avatar", true);

        File file = new File(dir, "key1");
        if (!file.exists()) {
            try {
                boolean newFile = file.createNewFile();
                if (!newFile) return;
            } catch (IOException e) {
                return;
            }
        }

        Log.d(TAG, dir);
        Log.d(TAG, file.getAbsolutePath());

        initFileImage();
    }


    private void newSimpleView() {
        SimpleDraweeView simpleDraweeView = new SimpleDraweeView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) PhoneScreenUtil.dipToPx(100.0f), (int) PhoneScreenUtil.dipToPx(100.0f));
        simpleDraweeView.setLayoutParams(layoutParams);
        mLinerLayout.addView(simpleDraweeView, layoutParams);
        FrescoLoadUtil.getInstance().loadImageLocalRes(simpleDraweeView, (int) PhoneScreenUtil.dipToPx(100.0f), (int) PhoneScreenUtil.dipToPx(100.0f), R.drawable.githu_logo);
    }

    private void javaRound() {
        FrescoLoadUtil.getInstance().loadImageLocalRes(mSVJavaRoundView, 100, 100, R.drawable.square_app_icon);
        RoundingParams roundingParams = RoundingParams.fromCornersRadius(50.0f);
        roundingParams.setBorder(ContextCompat.getColor(this, R.color.colorPrimary), 10.0f);
        roundingParams.setRoundAsCircle(true);
        mSVJavaRoundView.getHierarchy().setRoundingParams(roundingParams);
    }


    private void initFileImage() {

        String path = null;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pay_log);
        if (bitmap == null)return;
        String dir = getSignSubDir(this, "md5", "level_avatar", true);
        File file = new File(dir, "key");
        if (!file.exists()) {
            try {
                boolean newFile = file.createNewFile();
                if (!newFile) return;
            } catch (IOException e) {
                Log.e(TAG, "createNewFile failed, file:" + "key" + " url:" + "url");
                return;
            }
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "FileNotFoundException, file:" + "key" + " url:" + "url");
        }
        if (null == out) return;
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        try {
            out.flush();
            Log.d(TAG, file.getPath());
            path = file.getPath();
        } catch (IOException e) {
            Log.e(TAG, "IOException flush, file:" + "key" + " url:" + "url");
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                Log.e(TAG, "IOException close, file:" + "key" + " url:" + "url");
            }
        }


        Log.d(TAG, "LoadImageLocalFile:" + path);
        FrescoLoadUtil.getInstance().LoadImageLocalFile(mSVLocalFileView, 100, 100, path);
    }

    private static String getSignSubDir(Context context, String sign, String subDir, boolean bCreate) {
        String retDir = "";
        String signDir = getSignDir(context, sign, bCreate);
        if (TextUtils.isEmpty(signDir)) {
            return retDir;
        }
        String dir = signDir + File.separator + subDir;
        File fileDir = new File(dir);
        if (fileDir.exists()) {
            retDir = fileDir.getPath();
        } else {
            if (bCreate) {
                if (fileDir.mkdirs()) {
                    retDir = fileDir.getPath();
                }
            }
        }
        return retDir;
    }


    private static String getSignDir(Context context, String sign, boolean bCreate) {
        String ret = "";
        File filesDir = context.getFilesDir();
        String dir = filesDir.getPath() + File.separator + sign;
        File signDir = new File(dir);
        if (signDir.exists()) {
            ret = signDir.getPath();
        } else {
            if (bCreate) {
                if (signDir.mkdirs()) {
                    ret = signDir.getPath();
                }
            }
        }
        return ret;
    }


    private void loadLocalData() {
        FrescoLoadUtil.getInstance().loadImageLocalRes(mSVLocalView, 100, 100, R.drawable.square_app_icon);
    }

    private void loadSVRoundView() {
        FrescoLoadUtil.getInstance().loadImageNetWork(mSVRoundView, 100, 100, IMAGE_URL);
    }

    private void getBitmap() {
        FrescoLoadUtil.getInstance().loadImageBitmap(IMAGE_URL, 100, 100, new FrescoBitmapCallback<Bitmap>() {
            @Override
            public void onSuccess(Uri uri, Bitmap result) {
                Toast.makeText(TActivity.this, "onSuccess", Toast.LENGTH_SHORT).show();
                mImageView.setImageBitmap(result);
            }

            @Override
            public void onFailure(Uri uri, Throwable throwable) {
                Toast.makeText(TActivity.this, "onFailure", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(Uri uri) {
                Toast.makeText(TActivity.this, "onCancel", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void initView() {
        mSVRoundView = (SimpleDraweeView) findViewById(R.id.image_round);
        mSVLocalView = (SimpleDraweeView) findViewById(R.id.sv_local_drawable);
        mImageView = (ImageView) findViewById(R.id.image_image);
        mSVLocalFileView = (SimpleDraweeView) findViewById(R.id.sv_local_file_img);
        mSVJavaRoundView = (SimpleDraweeView) findViewById(R.id.sv_local_file_img3);
        mLinerLayout = (LinearLayout) findViewById(R.id.ll_new_layout);
    }

    private void testWH() {
        mViewWidth = mSVRoundView.getLayoutParams().width;
        mViewHeight = mSVRoundView.getLayoutParams().height;
        Toast.makeText(this, "w:" + mViewWidth + " " + "h:" + mViewHeight, Toast.LENGTH_SHORT).show();
    }

    private void setGreyScale(View v, boolean greyScale) {
        if (greyScale) {
            // Create a paint object with 0 saturation (black and white)
            ColorMatrix cm = new ColorMatrix();
            cm.setSaturation(0);
            Paint greyScalePaint = new Paint();
            greyScalePaint.setColorFilter(new ColorMatrixColorFilter(cm));
            // Create a hardware layer with the greyScale paint
            v.setLayerType(View.LAYER_TYPE_HARDWARE, greyScalePaint);
        } else {
            // Remove the hardware layer
            v.setLayerType(View.LAYER_TYPE_NONE, null);
        }
    }
}
