package meizhi.meizhi.malin.utils;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.tencent.bugly.crashreport.CrashReport;

/**
 * author: baiiu
 * date: on 16/7/6 15:02
 * description:
 * https://github.com/baiiu/LearnTechDemo/blob/master/someApplications/MyApplication/src/main/java/com/baiiu/myapplication/module/fastscrooll/FastScrollFragment.java
 */
public class FastScrollLinearLayoutManager extends StaggeredGridLayoutManager {

    public FastScrollLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public FastScrollLinearLayoutManager(int spanCount, int orientation) {
        super(spanCount, orientation);
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {

        LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {

            @Override
            public PointF computeScrollVectorForPosition(int targetPosition) {
                return FastScrollLinearLayoutManager.this.computeScrollVectorForPosition(targetPosition);
            }

            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                //if returned value is 2 ms, it means scrolling 1000 pixels with LinearInterpolation should take 2 seconds.

                float mTime = super.calculateSpeedPerPixel(displayMetrics);
                //v = 10F / displayMetrics.densityDpi;
                //LogUtil.d("滑动1像素毫秒数: " + mTime);

                /*
                  *1个像素要25ms,可用减少
                  * float v = super.calculateSpeedPerPixel(displayMetrics);
                  * return MILLISECONDS_PER_INCH / displayMetrics.densityDpi
                 */


                return mTime;
            }

            @Override
            protected int calculateTimeForScrolling(int dx) {
                //Calculates the time it should take to scroll the given distance (in pixels)

                //Math.ceil(Math.abs(dx) * calculateSpeedPerPixel(context.getResources().getDisplayMetrics())),

                //dx TARGET_SEEK_SCROLL_DISTANCE_PX = 10000,至多10000个像素

                /*很明显,要想滑动快点,即控制此处返回时间:
                 *减少距离.仅仅是假设距离减少,以减少滑动时间
                 */

                if (dx > 3000) {
                    dx = 3000;
                }
                int i = super.calculateTimeForScrolling(dx);
                return i;
            }

            @Override
            public int calculateDyToMakeVisible(View view, int snapPreference) {
                //Calculates the vertical scroll amount necessary to make the given view fully visible inside the RecyclerView.
                //计算离targetPosition还有多远,该方法并没有什么用

                int i = super.calculateDyToMakeVisible(view, snapPreference);
                return i;
            }


            @Override
            protected void updateActionForInterimTarget(Action action) {
                // When the target scroll position is not a child of the RecyclerView, this method calculates
                // a direction vector towards that child and triggers a smooth scroll.
                super.updateActionForInterimTarget(action);
            }


            @Override
            protected void onTargetFound(View targetView, RecyclerView.State state, Action action) {
                //Called when the target position is laid out.
                // This is the last callback SmoothScroller will receive and
                // it should update the provided RecyclerView.SmoothScroller.Action
                // to define the scroll details towards the target view.

                //targetPosition找到后开始减速上行
                super.onTargetFound(targetView, state, action);
            }


            @Override
            protected void onSeekTargetStep(int dx, int dy, RecyclerView.State state, Action action) {
                //RecyclerView will call this method each time it scrolls until it can find the target
                // position in the layout.
                //SmoothScroller should check dx, dy and if scroll should be changed, update the provided
                // RecyclerView.SmoothScroller.Action to define the next scroll.
                //LogUtil.d(dx + ", " + dy + ", " + state + ", " + action);
                super.onSeekTargetStep(dx, dy, state, action);
            }

        };

        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);
    }

    @Override
    public PointF computeScrollVectorForPosition(int targetPosition) {
        return super.computeScrollVectorForPosition(targetPosition);
    }

    @Override
    public void collectAdjacentPrefetchPositions(int dx, int dy, RecyclerView.State state, LayoutPrefetchRegistry layoutPrefetchRegistry) {
        try {
            super.collectAdjacentPrefetchPositions(dx, dy, state, layoutPrefetchRegistry);
        } catch (IllegalArgumentException e) {
            CrashReport.postCatchedException(e);
            LogUtil.e("FastScrollLinearLayoutManager","catch IllegalArgumentException");
        }
    }
/**
 * https://code.google.com/p/android/issues/detail?id=230295
    AndroidRuntime  E  FATAL EXCEPTION: main
    E  Process: meizhi.meizhi.malin, PID: 31432
    E  java.lang.IllegalArgumentException: Pixel distance must be non-negative
    E      at android.support.v7.widget.GapWorker$LayoutPrefetchRegistryImpl.addPosition(GapWorker.java:110)
    E      at android.support.v7.widget.StaggeredGridLayoutManager.collectAdjacentPrefetchPositions(StaggeredGridLayoutManager.java:2109)
    E      at android.support.v7.widget.GapWorker$LayoutPrefetchRegistryImpl.collectPrefetchPositionsFromView(GapWorker.java:94)
    E      at android.support.v7.widget.GapWorker.buildTaskList(GapWorker.java:213)
    E      at android.support.v7.widget.GapWorker.prefetch(GapWorker.java:343)
    E      at android.support.v7.widget.GapWorker.run(GapWorker.java:370)
    E      at android.os.Handler.handleCallback(Handler.java:751)
    E      at android.os.Handler.dispatchMessage(Handler.java:95)
    E      at android.os.Looper.loop(Looper.java:154)
    E      at android.app.ActivityThread.main(ActivityThread.java:6119)
    E      at java.lang.reflect.Method.invoke(Native Method)
    E      at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:886)
    E      at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:776)

 ***/

}
