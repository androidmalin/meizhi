package meizhi.meizhi.malin.utils;

import rx.Subscription;

/**
 * 类描述:清除View控件的资源依赖
 * 创建人:malin.myemail@163.com
 * 创建时间:
 * 备注:{@link }
 * 修改人:
 * 修改时间:
 * 修改备注:
 * 版本:
 */
public final class RxUtils {

    private RxUtils() {
    }

    public static void unSubscribeIfNotNull(Subscription subscription) {
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }
}
