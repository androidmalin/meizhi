package meizhi.meizhi.malin.network.services;

import meizhi.meizhi.malin.network.api.ImageApi;
import okhttp3.OkHttpClient;
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
                            .baseUrl("http://gank.io")
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
                .build();
        return okHttpClient;
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
