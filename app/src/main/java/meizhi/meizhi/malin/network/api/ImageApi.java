package meizhi.meizhi.malin.network.api;

import meizhi.meizhi.malin.network.bean.ImageInfo;
import retrofit2.http.GET;
import retrofit2.http.Path;
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

//分类数据: http://gank.io/api/data/数据类型/请求个数/第几页
//
//        数据类型： 福利 | Android | iOS | 休息视频 | 拓展资源 | 前端 | all
//        请求个数： 数字，大于0
//        第几页：数字，大于0

@SuppressWarnings("ALL")
public interface ImageApi {
    @GET("api/data/%E7%A6%8F%E5%88%A9/{num}/{page}")
    Observable<ImageInfo> getkey(
            @Path("num") int num,
            @Path("page") int page
    );
}
