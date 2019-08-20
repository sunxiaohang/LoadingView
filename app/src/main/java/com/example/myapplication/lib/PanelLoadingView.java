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

public class PanelLoadingView extends View {
    private final Path longDashPath;
    private final Path shortDashPath;
    private Paint mainPaint;
    //custom attrs
    private int paintColor;
    private int speed;
    private boolean direction;
    private final int STROKE_WIDTH = 3;
    private final int loadingCircleSize = 180;
    private final int loadingCircleRadius = 80;
    private PathMeasure longDashPathMeasure;
    private PathMeasure shortDashPathMeasure;
    private final ValueAnimator valueAnimator;
    private int longDashPhase;
    private int shortDashPhase;

    public PanelLoadingView(Context context) {
        this(context,null);
    }

    public PanelLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PanelLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //initial typeArray
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PanelLoadingView);
        if(typedArray!=null){
            paintColor = typedArray.getColor(R.styleable.PanelLoadingView_loadingViewPanelColor,Color.RED);
            speed = typedArray.getInt(R.styleable.PanelLoadingView_loadingViewPanelSpeed,5);
            if(speed>10)speed=10;
            direction = typedArray.getBoolean(R.styleable.PanelLoadingView_rotatePanelDirection,true);
        }
        //initial base parameter
        mainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mainPaint.setColor(paintColor);
        mainPaint.setStyle(Paint.Style.STROKE);
        mainPaint.setStrokeWidth(STROKE_WIDTH);
        mainPaint.setStrokeCap(Paint.Cap.ROUND);
        longDashPathMeasure = new PathMeasure();
        shortDashPathMeasure = new PathMeasure();

        longDashPath = new Path();
        shortDashPath = new Path();
        Path.Direction pathDirection = direction? Path.Direction.CW: Path.Direction.CCW;
        longDashPath.addCircle(loadingCircleSize / 2, loadingCircleSize / 2, 0.8f*loadingCircleRadius, pathDirection);
        shortDashPath.addCircle(loadingCircleSize / 2, loadingCircleSize / 2, 0.6f*loadingCircleRadius, pathDirection);
        longDashPathMeasure.setPath(longDashPath,false);
        shortDashPathMeasure.setPath(shortDashPath,false);
        
        valueAnimator = ValueAnimator.ofFloat(0,1);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(speed*200);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float fraction = (float) valueAnimator.getAnimatedValue();
                longDashPhase = (int) (fraction*longDashPathMeasure.getLength());
                shortDashPhase = (int) (fraction*shortDashPathMeasure.getLength());
                invalidate();
            }
        });
        valueAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Path circlePath = new Path();
        circlePath.addCircle(loadingCircleSize/2,loadingCircleSize/2,loadingCircleRadius, Path.Direction.CW);
        mainPaint.setStrokeWidth(STROKE_WIDTH);
        mainPaint.setPathEffect(null);
        canvas.drawPath(circlePath,mainPaint);

        DashPathEffect longPathEffect = new DashPathEffect(new float[]{longDashPathMeasure.getLength() * 1 / 5, longDashPathMeasure.getLength() * 3 / 10}, longDashPhase);
        DashPathEffect shortPathEffect = new DashPathEffect(new float[]{shortDashPathMeasure.getLength() * 1 / 5, shortDashPathMeasure.getLength() * 3 / 10}, shortDashPhase);
        
        mainPaint.setStrokeWidth(STROKE_WIDTH);
        mainPaint.setPathEffect(longPathEffect);
        canvas.drawPath(longDashPath,mainPaint);

        mainPaint.setStrokeWidth(STROKE_WIDTH);
        mainPaint.setPathEffect(shortPathEffect);
        canvas.drawPath(shortDashPath,mainPaint);

        mainPaint.setStrokeWidth(7*STROKE_WIDTH);
        canvas.drawPoint(loadingCircleSize/2,loadingCircleSize/2,mainPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(loadingCircleSize,loadingCircleSize);
    }
}
