package com.example.con_cu_tim.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.con_cu_tim.R;

public class CircularProgressView extends View {
    private Paint mInnerPaint, mOuterPaint;
    private float mBorderWidth;
    private float mProgress;
    private int mStartAngle;

    public CircularProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        float size = 5f;
        // Khởi tạo Paint cho hình tròn trong
        mInnerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mInnerPaint.setStyle(Paint.Style.FILL);

        // Khởi tạo Paint cho viền
        mOuterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOuterPaint.setStyle(Paint.Style.STROKE);
        mOuterPaint.setStrokeWidth(size);

        // Khởi tạo giá trị ban đầu
        mBorderWidth = size;
        mStartAngle = 0;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth = getSuggestedMinimumWidth() + (int) mBorderWidth * 2;
        int desiredHeight = getSuggestedMinimumHeight() + (int) mBorderWidth * 2;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width, height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(desiredWidth, widthSize);
        } else {
            width = desiredWidth;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(desiredHeight, heightSize);
        } else {
            height = desiredHeight;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float centerX = getWidth() / 2;
        float centerY = getHeight() / 2;
        float radius = Math.min(centerX, centerY) - mBorderWidth / 2;

        if(mProgress>0){
            mInnerPaint.setColor(getContext().getResources().getColor(R.color.correct_background));
        }else if(mProgress==-100){
            mInnerPaint.setColor(getContext().getResources().getColor(R.color.purple_200));
        }else{
            mInnerPaint.setColor(getContext().getResources().getColor(R.color.grey));
        }
        // Vẽ hình tròn trong
        canvas.drawCircle(centerX, centerY, radius, mInnerPaint);

        // Vẽ viền
        RectF rectF = new RectF(mBorderWidth / 2, mBorderWidth / 2, getWidth() - mBorderWidth / 2, getHeight() - mBorderWidth / 2);
        if(mProgress>0){
            float sweepAngle = 360 * mProgress / 100;
            mOuterPaint.setColor(getContext().getResources().getColor(R.color.progressbar_process));
            canvas.drawArc(rectF, mStartAngle, -sweepAngle, false, mOuterPaint);
        }else{
            mOuterPaint.setColor(getContext().getResources().getColor(R.color.grey));
        }
    }

    public void setProgress(float progress) {
        mProgress = progress;
        invalidate();
    }
}
