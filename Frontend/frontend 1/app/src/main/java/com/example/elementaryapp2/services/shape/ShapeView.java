package com.example.elementaryapp2.services.shape;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class ShapeView extends View {
    public static final int SHAPE_CIRCLE = 0;
    public static final int SHAPE_SQUARE = 1;
    public static final int SHAPE_RECTANGLE = 2;
    public static final int SHAPE_TRIANGLE = 3;

    private int shapeType;
    private int shapeColor;
    private Paint paint;

    public ShapeView(Context context, int shapeType, int shapeColor) {
        super(context);
        this.shapeType = shapeType;
        this.shapeColor = shapeColor;
        init();
    }

    public ShapeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(shapeColor);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        int size = Math.min(width, height);

        switch (shapeType) {
            case SHAPE_CIRCLE:
                canvas.drawCircle(width / 2f, height / 2f, size / 2f, paint);
                break;
            case SHAPE_SQUARE:
                canvas.drawRect(0, 0, size, size, paint);
                break;
            case SHAPE_RECTANGLE:
                canvas.drawRect(0, 0, width, height, paint);
                break;
            case SHAPE_TRIANGLE:
                Path path = new Path();
                path.moveTo(width / 2f, 0);
                path.lineTo(0, height);
                path.lineTo(width, height);
                path.close();
                canvas.drawPath(path, paint);
                break;
        }
    }
}

