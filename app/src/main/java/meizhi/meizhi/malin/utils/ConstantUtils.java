package meizhi.meizhi.malin.utils;

/**
 * 类描述:黑名单
 * 创建人:malin.myemail@163.com
 * 创建时间:17-2-2 17:22
 * 备注:{@link }
 * 修改人:
 * 修改时间:
 * 修改备注:
 * 版本:
 */
public class ConstantUtils {
    private static final String IMAGE = "http://7xi8d6.com1.z0.glb.clouddn.com/2017-01-20-030332.jpg";
    private static final String IMAGE_1 = "http://ww3.sinaimg.cn/orj360/610dc034jw1f8qd9a4fx7j20u011hq78.jpg";
    private static final String IMAGE_1_large = "http://ww3.sinaimg.cn/large/610dc034jw1f8qd9a4fx7j20u011hq78.jpg";
    private static final String IMAGE_2 = "http://ww3.sinaimg.cn/large/610dc034gw1f4fkmatcvdj20hs0qo78s.jpg";

    public static String getRightUrl(String url,boolean large) {
        String result;
        switch (url) {
            case IMAGE: {
                if (large){
                    result = IMAGE_1_large;
                }else{
                    result = IMAGE_1;
                }
                break;
            }
            default: {
                result = url;
            }
        }
        return result;
    }
}
