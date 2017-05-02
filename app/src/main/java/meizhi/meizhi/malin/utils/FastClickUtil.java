package meizhi.meizhi.malin.utils;


/**
 * 类描述:防止按钮连续点击
 * 创建人:malin.myemail@163.com
 * 创建时间:2016/9/12 16:55
 * 备注:{@link }
 * 修改人:
 * 修改时间:
 * 修改备注:
 * 版本:
 */
public class FastClickUtil {

    private FastClickUtil() {
    }

    private static long lastClickTime;
    private static final int MIN_CLICK_DELAY_TIME = 100;//ms

    public synchronized static boolean isFastClick() {
        synchronized (FastClickUtil.class) {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - lastClickTime < MIN_CLICK_DELAY_TIME) {
                return true;
            }
            lastClickTime = currentTimeMillis;
            return false;
        }
    }
}