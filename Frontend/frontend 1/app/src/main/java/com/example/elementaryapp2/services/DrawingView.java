package com.example.elementaryapp2.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

public class DrawingView extends View {
    private Paint paint;
    private Canvas canvas;
    private Bitmap bitmap;

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(30);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    private float lastX, lastY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Start drawing from the initial touch point
                canvas.drawPoint(x, y, paint);
                lastX = x;
                lastY = y;
                invalidate();
                performClick();
                return true;

            case MotionEvent.ACTION_MOVE:
                // Connect consecutive points to create a line
                float dx = Math.abs(x - lastX);
                float dy = Math.abs(y - lastY);
                if (dx >= 1 || dy >= 1) {
                    canvas.drawLine(lastX, lastY, x, y, paint);
                    lastX = x;
                    lastY = y;
                    invalidate();
                    performClick();
                }
                return true;

            default:
                return super.onTouchEvent(event);
        }
    }

    @Override
    public boolean performClick() {
        super.performClick();
        // Handle click actions here if needed
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        int size = Math.min(width, height); // Get the smaller dimension

        // Set both width and height to the smaller dimension to make it square
        setMeasuredDimension(size, size);
    }

    public void clearCanvas() {
        canvas.drawColor(Color.WHITE);
        invalidate();
    }

    public Bitmap getBitmap() {
        return bitmap.copy(Bitmap.Config.ARGB_8888, true);
    }
}


