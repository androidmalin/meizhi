package meizhi.meizhi.malin.network.services;

import java.io.IOException;

import meizhi.meizhi.malin.network.api.ImageApi;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 类描述:
 * 创建人:malin.myemail@163.com
 * 创建时间:2017.1.31 17:18
 * 备注:{@link }
 * 修改人:
 * 修改时间:
 * 修改备注:
 * 版本:
 */

public final class ImageService {

    private static final String BASE_URL = "https://mengmengdajson.oss-cn-shanghai.aliyuncs.com";

    private ImageService() {
    }

    private static volatile ImageService instance = null;

    public static ImageService getInstance() {
        if (instance == null) {
            synchronized (ImageService.class) {
                if (instance == null) {
                    instance = new ImageService();
                }
            }
        }
        return instance;
    }

    private volatile static ImageApi mImageApi = null;

    public ImageApi getImageList() {
        if (mImageApi == null) {
            synchronized (ImageService.class) {
                if (mImageApi == null) {
                    mImageApi = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .client(getOkHttpClientSign())
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .build()
                            .create(ImageApi.class);
                }
            }
        }
        return mImageApi;
    }

    /**
     * 下载
     *
     * @param hostURL
     * @return
     */
    private volatile static ImageApi mImageApiDownLoad = null;

    public ImageApi getDownLoad(String hostURL) {
        if (mImageApiDownLoad == null) {
            synchronized (ImageService.class) {
                if (mImageApiDownLoad == null) {
                    mImageApiDownLoad = new Retrofit.Builder()
                            .baseUrl(hostURL)
                            .build()
                            .create(ImageApi.class);
                }
            }
        }
        return mImageApiDownLoad;
    }

    /**
     * OkHttp拦截器
     *
     * @return OkHttpClient
     */
    private static OkHttpClient getOkHttpClientSign() {
        OkHttpClient okHttpClient = null;
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new HttpHeaderInterceptor())
                .build();
        return okHttpClient;
    }


    /**
     * 设置Http Header
     */
    public static class HttpHeaderInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            Request newRequest = originalRequest.newBuilder()
                    .addHeader("Referer", "http://www.malin.xyz")
                    .build();
            return chain.proceed(newRequest);
        }
    }
}
