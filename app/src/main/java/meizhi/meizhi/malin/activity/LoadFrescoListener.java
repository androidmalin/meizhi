package meizhi.meizhi.malin.activity;

import android.graphics.Bitmap;

/**
 * @author  root on 17-3-15.
 */

public interface LoadFrescoListener {
    void onSuccess(Bitmap bitmap);

    void onFail();
}
