package meizhi.meizhi.malin.activity;

import android.net.Uri;

/**
 * 2017.03.15
 *
 * @param <T>
 */
public interface FrescoBitmapCallback<T> {

    void onSuccess(Uri uri, T result);

    void onFailure(Uri uri, Throwable throwable);

    void onCancel(Uri uri);
}