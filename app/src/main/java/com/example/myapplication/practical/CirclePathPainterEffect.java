package com.example.myapplication.practical;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

public class CirclePathPainterEffect extends View implements View.OnClickListener {
    private Path path;
    private PathMeasure pathMeasure;
    private float fraction =0f;
    private PathEffect pathEffect;
    private Paint paint;
    private ValueAnimator valueAnimator;
    public CirclePathPainterEffect(Context context) {
        this(context,null);
    }

    public CirclePathPainterEffect(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CirclePathPainterEffect(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        path = new Path();
        path.reset();
        path.addCircle(120,120,100, Path.Direction.CW);

        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(7);
        pathMeasure = new PathMeasure(path,false);
        final float length = pathMeasure.getLength();
        valueAnimator = ValueAnimator.ofFloat(0,1);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                fraction = (float) valueAnimator.getAnimatedValue();
                pathEffect = new DashPathEffect(new float[]{length,length},fraction*length);
                paint.setPathEffect(pathEffect);
                invalidate();
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                pathEffect = new DashPathEffect(new float[]{length,length},0);
                paint.setPathEffect(pathEffect);
                invalidate();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        valueAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path,paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(240,240);
    }
}
