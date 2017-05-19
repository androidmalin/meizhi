package tv.panda.live.image;

import android.net.Uri;

/**
 * 类描述:Bitmap图片加载的回调
 * 创建人:malin.myemail@163.com
 * 创建时间:2017.3.15
 * 备注:{@link }
 * 修改人:
 * 修改时间:
 * 修改备注:
 * 版本:
 */
public interface FrescoBitmapCallback<T> {

    void onSuccess(Uri uri, T result);

    void onFailure(Uri uri, Throwable throwable);

    void onCancel(Uri uri);
}