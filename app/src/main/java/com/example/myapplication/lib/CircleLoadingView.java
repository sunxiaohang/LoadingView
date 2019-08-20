package com.example.myapplication.lib;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.example.myapplication.R;

public class CircleLoadingView extends View {
    private Path path;
    private Paint paint;
    private final int loadingViewSize = 180;
    private final int loadingViewRadius = 80;

    private int loadingViewColor;
    private int loadingViewSpeed;
    private DashPathEffect dashPathEffect;
    private PathMeasure pathMeasure;
    private int phase;
    private ValueAnimator valueAnimator;
    public CircleLoadingView(Context context) {
        this(context,null);
    }

    public CircleLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleLoadingView);
        if(typedArray!=null){
            loadingViewColor = typedArray.getColor(R.styleable.CircleLoadingView_loadingViewCircleColor, Color.RED);
            loadingViewSpeed = typedArray.getInt(R.styleable.CircleLoadingView_loadingViewCircleSpeed,5);
        }
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(8);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(loadingViewColor);
        path = new Path();
        path.addCircle(loadingViewSize/2,loadingViewSize/2,loadingViewRadius, Path.Direction.CW);
        pathMeasure = new PathMeasure(path,false);
        valueAnimator = ValueAnimator.ofFloat(0,1);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setDuration(loadingViewSpeed*200);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float fraction = (float) valueAnimator.getAnimatedValue();
                phase = (int) (fraction*pathMeasure.getLength());
                invalidate();
            }
        });
        valueAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        dashPathEffect = new DashPathEffect(new float[]{pathMeasure.getLength()/4,pathMeasure.getLength()*3/4},phase);

        paint.setColor(Color.GRAY);
        paint.setPathEffect(null);
        canvas.drawCircle(loadingViewSize/2,loadingViewSize/2,loadingViewRadius,paint);

        paint.setPathEffect(dashPathEffect);
        paint.setColor(loadingViewColor);
        canvas.drawPath(path,paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(loadingViewSize,loadingViewSize);
    }
}
