package meizhi.meizhi.malin.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import meizhi.meizhi.malin.R;
import meizhi.meizhi.malin.adapter.DepthPageTransformer;
import meizhi.meizhi.malin.adapter.ImagePagerAdapter;
import meizhi.meizhi.malin.network.api.ImageApi;
import meizhi.meizhi.malin.network.bean.ImageBean;
import meizhi.meizhi.malin.network.services.ImageService;
import meizhi.meizhi.malin.utils.HackyViewPager;
import meizhi.meizhi.malin.utils.RxUtils;
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
public class ImageDetailActivity extends AppCompatActivity implements ImagePagerAdapter.downLoadClickListener {
    private static final String TAG = ImageDetailActivity.class.getSimpleName();
    private static final String FILE_IMAGE = "0MeZhi";
    private static final int TEMP = 4 * 1024;
    private ArrayList<ImageBean> mList;
    private int mPosition;
    private Subscription mSubscription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarColor();
        setNavigationBarColor();
        setContentView(R.layout.activity_image_detail);
        Intent intent = getIntent();
        if (intent != null) {
            mPosition = intent.getIntExtra("position", 0);
            mList = intent.getParcelableArrayListExtra("datas");
        }
        ViewPager mViewPager = (HackyViewPager) findViewById(R.id.view_pager_iv);
        ImagePagerAdapter adapter = new ImagePagerAdapter();
        adapter.setDownLoadListener(this);
        adapter.setData(mList, this);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(mPosition);
        mViewPager.setPageTransformer(true, new DepthPageTransformer());
    }

    private void StatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void setNavigationBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
    }

    @Override
    public void downImageListener(String url) {
        downloadFile(url);
    }


    private void downloadFile(final String fileUrl) {
        ImageApi biLiApi = ImageService.getInstance().getDownLoadService(ImageApi.class, getBaseUrl(fileUrl));
        Call<ResponseBody> call = biLiApi.download(fileUrl);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "server contacted and has file");
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
                Log.d(TAG, "scheme:" + scheme);
            }
            if (!TextUtils.isEmpty(host)) {
                Log.d(TAG, "host:" + host);
            }
            StringBuilder sb = new StringBuilder();
            sb.append(!TextUtils.isEmpty(scheme) ? scheme : "")
                    .append("://")
                    .append(!TextUtils.isEmpty(host) ? host : "")
                    .append("/");
            baseUrl = sb.toString();
            Log.d(TAG, "baseUrl:" + baseUrl);

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
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(ImageDetailActivity.this, R.string.down_load_error, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Boolean isSuccess) {
                        Log.d(TAG, "file download was a success? " + isSuccess);
                        String tip = "\n图片保存在sdcard的" + FILE_IMAGE + "文件夹中";
                        Toast.makeText(ImageDetailActivity.this, isSuccess ? getString(R.string.down_load_success) + tip : getString(R.string.down_load_error), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public Observable<Boolean> createObservable(final ResponseBody body) {
        return Observable.defer(new Func0<Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call() {
                return Observable.just(writeResponseBodyToDisk(body));
            }
        });
    }


    private String mPath;

    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            File appDir = new File(Environment.getExternalStorageDirectory(), FILE_IMAGE);
            if (!appDir.exists()) {
                appDir.mkdir();
            }
            String fileName = System.currentTimeMillis() + ".jpg";

            mPath = appDir.getAbsolutePath() + File.separator + fileName;
            File futureStudioIconFile = new File(appDir, fileName);
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[TEMP];
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);
                Log.d(TAG, futureStudioIconFile.getAbsolutePath());
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
                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                    Log.d(TAG, "file download: " + futureStudioIconFile.getAbsolutePath());
                }
                outputStream.flush();
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Throwable e) {
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxUtils.unSubscribeIfNotNull(mSubscription);
    }
}
