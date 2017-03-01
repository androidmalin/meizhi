package meizhi.meizhi.malin.network.api;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

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
public interface ImageApi {

    //http://mengmengdajson.oss-cn-shanghai.aliyuncs.com/image_1.json
    @GET("{page}")
    Observable<List<String>> getImageList(
            @Path("page") String page
    );

    @Streaming
    @GET
    Call<ResponseBody> download(@Url String imageUrl);
}
