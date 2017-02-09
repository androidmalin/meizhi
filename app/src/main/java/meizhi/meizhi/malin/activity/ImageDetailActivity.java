package meizhi.meizhi.malin.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import meizhi.meizhi.malin.R;
import meizhi.meizhi.malin.adapter.DepthPageTransformer;
import meizhi.meizhi.malin.adapter.ImagePagerAdapter;
import meizhi.meizhi.malin.network.api.ImageApi;
import meizhi.meizhi.malin.network.bean.ImageBean;
import meizhi.meizhi.malin.network.services.ImageService;
import meizhi.meizhi.malin.utils.GlideCatchUtil;
import meizhi.meizhi.malin.utils.HackyViewPager;
import meizhi.meizhi.malin.utils.LogUtil;
import meizhi.meizhi.malin.utils.RxUtils;
import meizhi.meizhi.malin.utils.UMengEvent;
import okhttp3.HttpUrl;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

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
public class ImageDetailActivity extends AppCompatActivity implements ImagePagerAdapter.downLoadClickListener,
        ImagePagerAdapter.photoViewTapListener, View.OnClickListener, ImagePagerAdapter.imageDownLoadListener {
    private static final String TAG = ImageDetailActivity.class.getSimpleName();
    private static final String FILE_IMAGE = "0MeZhi";
    private static final int TEMP = 4 * 1024;
    private ArrayList<ImageBean> mList;
    private int mPosition;
    private Subscription mSubscription;
    private static final int INITIAL_DELAY = 1000;
    private Window mWindow;
    private View mDecorView;
    private View mContentView;
    private static final int MESSAGE_HIDE = 0;
    private ProgressBar mProgressBar;
    private RelativeLayout mDownLoadLayout;
    private TextView mTvPage;
    private ImageView mImgError;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg == null) return;
            switch (msg.what) {
                case MESSAGE_HIDE: {
                    hideSystemUI();
                    break;
                }
                default: {
                    break;
                }
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initStatusBarColor();
        setContentView(mContentView);
        Intent intent = getIntent();
        if (intent != null) {
            mPosition = intent.getIntExtra("position", 0);
            mList = intent.getParcelableArrayListExtra("datas");
        }
        ViewPager viewPager = (HackyViewPager) mContentView.findViewById(R.id.view_pager_iv);
        mDownLoadLayout = (RelativeLayout) mContentView.findViewById(R.id.rl_download_img);
        mDownLoadLayout.setOnClickListener(this);
        mProgressBar = (ProgressBar) mContentView.findViewById(R.id.pd_img);
        mTvPage = (TextView) mContentView.findViewById(R.id.tv_page);
        mImgError = (ImageView) mContentView.findViewById(R.id.iv_img_error);

        ImagePagerAdapter adapter = new ImagePagerAdapter();
        adapter.setDownLoadListener(this);
        adapter.setPhotoViewTapListener(this);
        adapter.setProgressBarListener(this);
        adapter.setData(mList, this);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(mPosition);
        viewPager.setPageTransformer(true, new DepthPageTransformer());

        mTvPage.setText((mPosition + 1) + "/" + mList.size());

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPosition = position;
                if (mList != null && mList.size() > 0) {
                    mTvPage.setText((mPosition + 1) + "/" + mList.size());
                }
                GlideCatchUtil.getInstance().releaseMemory(false);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initView() {
        mWindow = getWindow();
        if (mWindow == null) this.finish();
        mDecorView = mWindow.getDecorView();
        if (mDecorView == null) this.finish();
        final ViewGroup nullParent = null;
        mContentView = LayoutInflater.from(this).inflate(R.layout.activity_image_detail, nullParent);
        if (mContentView == null) this.finish();
    }


    private void initStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (mDecorView == null) return;
            int option = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            mDecorView.setSystemUiVisibility(option);
            if (mWindow == null) return;
            mWindow.setStatusBarColor(Color.TRANSPARENT);
            mWindow.setNavigationBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void viewTapListener(View view, float x, float y) {
        //状态栏，导航栏显示和隐藏
        if (mDecorView == null) return;
        if ((mDecorView.getSystemUiVisibility() & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0) {
            hideSystemUI();
        } else {
            showSystemUI();
        }
    }

    private void hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (mDecorView == null) return;
            mDecorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );

        }
        mDownLoadLayout.setVisibility(View.GONE);
        mTvPage.setVisibility(View.GONE);
    }

    private void showSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (mDecorView == null) return;
            mDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, android.R.color.transparent));
        }

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mDownLoadLayout.setVisibility(View.VISIBLE);
                mTvPage.setVisibility(View.VISIBLE);
            }
        }, 100);

    }


    private void delayedHide(int delay) {
        mHandler.removeMessages(MESSAGE_HIDE);
        mHandler.sendEmptyMessageDelayed(MESSAGE_HIDE, delay);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            delayedHide(INITIAL_DELAY);
        } else {
            mHandler.removeMessages(MESSAGE_HIDE);
            showSystemUI();
        }
    }

    @Override
    public void downImageListener(String url, int position, boolean singleClickDown) {
        downloadFile(url, position, singleClickDown);
    }


    private void downloadFile(final String fileUrl, int position, boolean singleClickDown) {

        HashMap<String, String> map = new HashMap<>();
        map.put("url", fileUrl);
        map.put("position", "" + position);
        MobclickAgent.onEvent(this, singleClickDown ? UMengEvent.ClickDownLoad : UMengEvent.LongClickDownLoad, map);


        ImageApi biLiApi = ImageService.getInstance().getDownLoadService(ImageApi.class, getBaseUrl(fileUrl));
        Call<ResponseBody> call = biLiApi.download(fileUrl);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    LogUtil.d(TAG, "server contacted and has file");
                    writeImageToDisk(response.body());
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ImageDetailActivity.this, getString(R.string.down_load_error), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ImageDetailActivity.this, getString(R.string.down_load_error), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * 获取BaseUrl
     *
     * @param urlString Url
     * @return BaseUrl
     */
    private synchronized static String getBaseUrl(String urlString) {
        HttpUrl httpUrl = HttpUrl.parse(urlString);
        //http://qn-apk.wdjcdn.com/4/b8/77572fdd42790d77bc600c710169cb84.apk
        //scheme http
        //host qn-apk.wdjcdn.com
        //baseUrl scheme :// host

        String baseUrl = null;

        if (httpUrl != null) {
            String scheme = httpUrl.scheme();
            String host = httpUrl.host();

            if (!TextUtils.isEmpty(scheme)) {
                LogUtil.d(TAG, "scheme:" + scheme);
            }
            if (!TextUtils.isEmpty(host)) {
                LogUtil.d(TAG, "host:" + host);
            }
            StringBuilder sb = new StringBuilder();
            sb.append(!TextUtils.isEmpty(scheme) ? scheme : "")
                    .append("://")
                    .append(!TextUtils.isEmpty(host) ? host : "")
                    .append("/");
            baseUrl = sb.toString();
            LogUtil.d(TAG, "baseUrl:" + baseUrl);

        }
        return baseUrl;
    }


    private void writeImageToDisk(ResponseBody body) {
        mSubscription = createObservable(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        try {
                            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + mPath)));
                        } catch (Throwable e) {
                            CrashReport.postCatchedException(e);
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(ImageDetailActivity.this, R.string.down_load_error, Toast.LENGTH_SHORT).show();
                        CrashReport.postCatchedException(e);
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Boolean isSuccess) {
                        LogUtil.d(TAG, "file download was a success? " + isSuccess);
                        String tip = "\n图片保存在sdcard的" + FILE_IMAGE + "文件夹中";
                        Toast.makeText(ImageDetailActivity.this, isSuccess ? getString(R.string.down_load_success) + tip : getString(R.string.down_load_error), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public Observable<Boolean> createObservable(final ResponseBody body) {
        return Observable.defer(new Func0<Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call() {
                boolean is = writeResponseBodyToDisk(body);
                return Observable.just(is);
            }
        });
    }


    private String mPath;

    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            File appDir = new File(Environment.getExternalStorageDirectory(), FILE_IMAGE);
            if (!appDir.exists()) {
                if (!appDir.mkdir()) {
                    if (!appDir.mkdir()) {
                        return false;
                    }
                }
            }
            String fileName = System.currentTimeMillis() + ".jpg";

            mPath = appDir.getAbsolutePath() + File.separator + fileName;
            File fileImg = new File(appDir, fileName);
            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[TEMP];
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                inputStream = body.byteStream();
                if (inputStream == null)
                    return false;
                outputStream = new FileOutputStream(fileImg);
                LogUtil.d(TAG, fileImg.getAbsolutePath());
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;

                    //W: file download: 36671219 of 36671219
                    //W: file download: /storage/emulated/0/Android/data/com.malin.animation/files/weixin.apk
                    //E: file download was a success? true
                    LogUtil.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                    LogUtil.d(TAG, "file download: " + fileImg.getAbsolutePath());
                }
                outputStream.flush();
                return true;
            } catch (IOException e) {
                CrashReport.postCatchedException(e);
                return false;
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        CrashReport.postCatchedException(e);
                    }
                    inputStream = null;

                }
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        CrashReport.postCatchedException(e);
                    }
                    outputStream = null;

                }
            }
        } catch (Throwable e) {
            CrashReport.postCatchedException(e);
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        GlideCatchUtil.getInstance().releaseMemory(true);
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        GlideCatchUtil.getInstance().releaseMemory(true);
        RxUtils.unSubscribeIfNotNull(mSubscription);
        if (mHandler == null) return;
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
        super.onDestroy();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_download_img: {
                if (mList == null || mPosition >= mList.size() || mList.get(mPosition).url == null)
                    return;
                downloadFile(mList.get(mPosition).url, mPosition, true);
                break;
            }

            default: {
                break;
            }
        }
    }

    @Override
    public void downLoadFailure() {
        if (mProgressBar != null && mProgressBar.isShown()) {
            mProgressBar.setVisibility(View.GONE);
        }
        mImgError.setVisibility(View.VISIBLE);
        Glide.with(this)
                .load(R.mipmap.ic_launcher)
                .asBitmap()
                .centerCrop()
                .into(mImgError);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setNavigationBarColor();
            }
        }, 1001);
    }

    private void setNavigationBarColor() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (mDecorView == null) return;
            mDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
    }

    @Override
    public void downLoadSuccess() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, android.R.color.transparent));
        }
        if (mProgressBar != null && mProgressBar.isShown()) {
            mProgressBar.setVisibility(View.GONE);
        }
        mImgError.setVisibility(View.GONE);
    }

    @Override
    public void downLoadPrepare() {
        if (mProgressBar != null && !mProgressBar.isShown()) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }
}

