package meizhi.meizhi.malin.utils;

import com.tencent.bugly.crashreport.CrashReport;

/**
 * 类描述:
 * 创建人:malin.myemail@163.com
 * 创建时间:17-2-2. 12:55
 * 备注:{@link }
 * 修改人:
 * 修改时间:
 * 修改备注:
 * 版本:
 */
public final class UrlUtils {

//    thumbnail.jpg   80x120
//    orj360.jpg      360x540
//    large.jpg 	  640x960
//加载失败
//http://ww1.sinaimg.cn/large/images/default_large.gif 240*180
//http://ww1.sinaimg.cn/large/e7a91d45ly1fd6kgq271pj20j60srjy7 Location: ./images/default_large.gif http 302
//http://img.hb.aicdn.com/b7af7e3622ed3f83d0d82ce30cc36a7e1d5f675a27fd8-5OhNMK_

    public static final int thumbnail = 1;
    public static final int orj360 = 6;
    public static final int large = 14;

    public static final String thumbnail_ = "thumbnail";
    public static final String orj360_ = "orj360";
    public static final String large_ = "large";


//    http://wwx.sinaimg.cn/thumbnail/xxx.jpg（缩略图）
//    http://wwx.sinaimg.cn/small/xxx.jpg （小图）
//    http://wwx.sinaimg.cn/large/xxx.jpg （最大的图）
    private UrlUtils() {

    }

    //youPaiYun
//http://img.hb.aicdn.com/a5ad0cad00bb135be7e22aa59f4d76b3a24c345a48d13-kDRRas_
//http://img.hb.aicdn.com/a5ad0cad00bb135be7e22aa59f4d76b3a24c345a48d13-kDRRas_/fw/300/gifto/true/progressive/true/format/webp
    public static String getUrl(String url, int type) {
        String result;
        try {
            if (url.contains("sinaimg.cn")) {
                if (url.contains(large_)) {
                    result = url.replaceFirst(large_, getImageFlag(type));
                } else {
                    result = url;
                }
            } else {
                result = url;
            }
        } catch (Exception e) {
            CrashReport.postCatchedException(e);
            e.printStackTrace();
            result = url;
        }
        return result;
    }

    private static String getImageFlag(int type) {
        String flag = "";
        switch (type) {
            case thumbnail: {
                flag = thumbnail_;
                break;
            }
            case orj360: {
                flag = orj360_;
                break;
            }
            case large: {
                flag = large_;
                break;
            }
            default: {
                break;
            }
        }
        return flag;
    }
}
