package meizhi.meizhi.malin.utils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.facebook.drawee.drawable.ProgressBarDrawable;

import meizhi.meizhi.malin.R;
import meizhi.meizhi.malin.application.MApplication;

public class CircleProgressBarDrawable extends ProgressBarDrawable {
    private static final String TAG = "CircleProgress";
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int mLevel = 0;
    private int maxLevel = 10000;

    private ProgressListener mListener;


    public void setProgressListener(ProgressListener listener) {
        this.mListener = listener;
    }

    @Override
    protected boolean onLevelChange(int level) {
        mLevel = level;
        Log.d(TAG, "onLevelChange:" + level);
        invalidateSelf();
        return true;
    }

    @Override
    public void draw(Canvas canvas) {
        if (getHideWhenZero() && mLevel == 0) {
            return;
        }
        drawBar(canvas, maxLevel, ContextCompat.getColor(MApplication.getInstance(), android.R.color.white));
        drawBar(canvas, mLevel, ContextCompat.getColor(MApplication.getInstance(), R.color.colorPrimary));//进度颜色
    }

    private void drawBar(Canvas canvas, int level, int color) {
        Rect bounds = getBounds();
        RectF rectF = new RectF((float) (bounds.right * .4), (float) (bounds.bottom * .4), (float) (bounds.right * .6), (float) (bounds.bottom * .6));
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(6);
        if (level != 0) {
            if (mListener != null) {
                mListener.update(level);
            }
            canvas.drawArc(rectF, 0, (float) (level * 360 / maxLevel), false, mPaint);
        }
    }

}