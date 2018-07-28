package meizhi.meizhi.malin.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Toast;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import meizhi.meizhi.malin.R;
import meizhi.meizhi.malin.application.MApplication;
import meizhi.meizhi.malin.network.api.ImageApi;
import meizhi.meizhi.malin.network.services.ImageService;
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
 * 创建时间:2017.2.13
 * 备注:{@link }
 * 修改人:
 * 修改时间:
 * 修改备注:
 * 版本:
 */
public final class ImageDownLoadUtil {

    private static final String TAG = "";

    private static final String FILE_IMAGE = "0MeiZhi";
    private static final int TEMP = 4 * 1024;
    private downLoadListener mDownLoadListener;
    private Subscription mSubscription;

    public void setDownLoadListener(downLoadListener listener) {
        this.mDownLoadListener = listener;
    }

    public interface downLoadListener {
        void downLoadFailure(String msg);

        void downLoadSuccessful(String msg);
    }


    private Context mContext;


    public ImageDownLoadUtil(Context context) {
        this.mContext = context;
    }

    public Subscription downloadFile(String fileUrl, int position, boolean singleClickDown) {

        HashMap<String, String> map = new HashMap<>();
        map.put("url", fileUrl);
        map.put("position", "" + position);

        ImageApi biLiApi = ImageService.getInstance().getDownLoad(getBaseUrl(fileUrl));
        Call<ResponseBody> call = biLiApi.download(fileUrl);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    LogUtil.d(TAG, "server contacted and has file");
                    writeImageToDisk(response.body());
                } else {
                    if (mDownLoadListener == null) return;
                    mDownLoadListener.downLoadFailure(mContext.getString(R.string.down_load_error));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (mDownLoadListener == null) return;
                mDownLoadListener.downLoadFailure(mContext.getString(R.string.down_load_error));
            }
        });

        return mSubscription;
    }

    private void writeImageToDisk(ResponseBody body) {
        mSubscription = createObservable(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        try {
                            mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + mPath)));
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MApplication.getContext(), R.string.down_load_error, Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Boolean isSuccess) {
                        LogUtil.d(TAG, "file download was a success? " + isSuccess);
                        String tip = "\n图片保存在sdcard的" + FILE_IMAGE + "文件夹中";
                        if (mDownLoadListener == null) return;
                        mDownLoadListener.downLoadSuccessful(isSuccess ? mContext.getString(R.string.down_load_success) + tip : mContext.getString(R.string.down_load_error));
                    }
                });
    }


    private Observable<Boolean> createObservable(final ResponseBody body) {
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
                return false;
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    inputStream = null;

                }
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    outputStream = null;

                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
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
            StringBuilder sb = new StringBuilder(128);
            sb.append(!TextUtils.isEmpty(scheme) ? scheme : "")
                    .append("://")
                    .append(!TextUtils.isEmpty(host) ? host : "")
                    .append("/");
            baseUrl = sb.toString();
            LogUtil.d(TAG, "baseUrl:" + baseUrl);

        }
        return baseUrl;
    }

}
