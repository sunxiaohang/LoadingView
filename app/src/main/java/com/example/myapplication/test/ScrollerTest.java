package com.example.myapplication.test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Scroller;

public class ScrollerTest extends View implements View.OnClickListener {
    private Paint paint;
    private Scroller scroller;

    public ScrollerTest(Context context) {
        this(context,null);
    }

    public ScrollerTest(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ScrollerTest(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        scroller = new Scroller(context);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.GRAY);
        setOnClickListener(this);

    }

    @Override
    public void computeScroll() {
        if(scroller!=null&&scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(),scroller.getCurrY());
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(300,300,200,paint);
    }

    @Override
    public void onClick(View view) {
        scroller.forceFinished(true);
        scroller.startScroll(0,0,100,0);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(600,600);
    }
}
