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

@SuppressWarnings("ALL")
public final class ImageService {

    private static final String TAG = ImageService.class.getSimpleName();
    private static final String BASE_URL = "http://mengmengdajson.oss-cn-shanghai.aliyuncs.com";

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

    public ImageApi getLogin() {
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

    /**
     * 下载
     *
     * @param tClass
     * @param hostURL
     * @param <T>
     * @return
     */
    public <T> T getDownLoadService(Class<T> tClass, String hostURL) {
        return new Retrofit.Builder()
                .baseUrl(hostURL)
                .client(getOkHttpClientSign())
                .build()
                .create(tClass);

    }

}
