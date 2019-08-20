package com.example.myapplication.lib;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

public class LoadingView extends View {
    private Paint paint;
    private Path path;
    private float pathLength;
    private Path dstPath;
    private float progress;
    private final int loadingViewSize = 180;
    private final int loadingViewRadius = 80;
    private PathMeasure pathMeasure;
    public LoadingView(Context context) {
        this(context,null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        progress = 0;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(12);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        paint.setStrokeCap(Paint.Cap.ROUND);
        path = new Path();
        dstPath = new Path();
        path.addCircle(loadingViewSize/2,loadingViewSize/2,loadingViewRadius, Path.Direction.CW);
        pathMeasure = new PathMeasure();
        pathMeasure.setPath(path,true);
        pathLength = pathMeasure.getLength();
        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0,1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                progress = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setDuration(1000);
        valueAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        dstPath.reset();
        float stop = pathLength*progress;
        float start =0;
        if(progress>=0.5){
            start = 2*stop-pathLength;
        }
        pathMeasure.getSegment(start,stop,dstPath,true);
        canvas.drawPath(dstPath,paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(loadingViewSize,loadingViewSize);
    }
}
