package com.example.myapplication.lib;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class StarLoadingView extends View {
    private final float pathMeasureLength;
    private Path path;
    private Path circlePath;
    private Paint paint;
    private PathMeasure pathMeasure;
    private PathMeasure circlePathMeasure;
    private int loadingViewSize = 180;
    private int loadingViewRadius = 80;
    private Path tempPath;
    private float fraction;
    private ValueAnimator valueAnimator;

    public StarLoadingView(Context context) {
        this(context,null);
    }

    public StarLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public StarLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        tempPath = new Path();
        int center = loadingViewSize/2;
        //initial target path
        PointF[] pointFS = new PointF[5];
        path = new Path();
        pathMeasure = new PathMeasure();
        circlePath = new Path();
        circlePath.addCircle(loadingViewSize/2,loadingViewSize/2,loadingViewRadius, Path.Direction.CW);
        circlePathMeasure = new PathMeasure(circlePath,false);
        float circleLength = circlePathMeasure.getLength();
        float[] pos = new float[2];
        circlePathMeasure.getPosTan(circleLength*3/20,pos,new float[2]);
        pointFS[0] = new PointF(pos[0],pos[1]);
        for (int i = 1; i < 5; i++) {
            circlePathMeasure.getPosTan(circleLength*3/20+i*circleLength/5,pos,new float[2]);
            pointFS[i] = new PointF(pos[0],pos[1]);
        }
        path.moveTo(pointFS[2].x,pointFS[2].y);
        RectF rectF = new RectF(center-loadingViewRadius,center-loadingViewRadius,center+loadingViewRadius,center+loadingViewRadius);
        path.arcTo(rectF,198,358);
        path.lineTo(pointFS[4].x,pointFS[4].y);
        path.lineTo(pointFS[1].x,pointFS[1].y);
        path.lineTo(pointFS[3].x,pointFS[3].y);
        path.lineTo(pointFS[0].x,pointFS[0].y);
        path.lineTo(pointFS[2].x,pointFS[2].y);

        pathMeasure.setPath(path,false);
        pathMeasureLength = pathMeasure.getLength();

        //initial paint
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(5);
        //valueAnimator
        valueAnimator = ValueAnimator.ofFloat(0,1);
        valueAnimator.setDuration(2000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                fraction = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        tempPath.reset();
        pathMeasure.getSegment(0,fraction*pathMeasureLength,tempPath,true);
        canvas.drawPath(tempPath,paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(loadingViewSize,loadingViewSize);
    }
}
