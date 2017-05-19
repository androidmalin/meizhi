package tv.panda.live.image;

import android.text.TextUtils;
import android.webkit.URLUtil;

/**
 * 类描述:判断URL是否为http地址
 * 创建人:malin.myemail@163.com
 * 创建时间:2017.3.16
 * 备注:{@link }
 * 修改人:
 * 修改时间:
 * 修改备注:
 * 版本:
 */
public final class ImageUrlUtils {

    public static boolean isHttpImageUrl(String url) {
        return !TextUtils.isEmpty(url) && (URLUtil.isHttpUrl(url) || URLUtil.isHttpsUrl(url));
    }
}
