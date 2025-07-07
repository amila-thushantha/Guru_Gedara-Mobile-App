package com.example.elementaryapp2.services.shape;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ShapeBoxView extends LinearLayout {
    private int shapeType;
    private int color;
    private int count;
    private Paint borderPaint;

    private static final int SHAPE_SIZE = 50; // Fixed size for shapes
    private static final int SHAPE_MARGIN = 5; // Margin around shapes
    private static final int COLUMNS = 4; // Shapes per row
    private static final int MAX_COUNT = 18; // Maximum shapes allowed
    private static final int MAX_ROWS = 5; // Maximum rows needed

    public ShapeBoxView(Context context, int shapeType, int color, int count) {
        super(context);
        this.shapeType = shapeType;
        this.color = color;
        this.count = Math.min(count, MAX_COUNT); // Limit count to max 18
        init();
    }

    public ShapeBoxView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        setPadding(10, 10, 10, 10);
        setBackgroundColor(Color.WHITE);

        // Border paint for box
        borderPaint = new Paint();
        borderPaint.setColor(Color.BLACK);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(5);

        // Shape container (Fixed height to fit max 18 shapes)
        GridLayout shapeContainer = new GridLayout(getContext());
        shapeContainer.setColumnCount(COLUMNS);
        shapeContainer.setRowCount(MAX_ROWS);

        int shapeGridHeight = (SHAPE_SIZE + SHAPE_MARGIN * 2) * MAX_ROWS; // Fixed height
        int shapeGridWidth = (SHAPE_SIZE + SHAPE_MARGIN * 2) * COLUMNS;
        LinearLayout.LayoutParams shapeContainerParams = new LinearLayout.LayoutParams(
                shapeGridWidth, shapeGridHeight
        );
        shapeContainer.setLayoutParams(shapeContainerParams);

        // Center the GridLayout within its container
        LinearLayout shapeContainerWrapper = new LinearLayout(getContext());
        shapeContainerWrapper.setGravity(Gravity.CENTER);
        shapeContainerWrapper.setLayoutParams(new LayoutParams(shapeGridWidth, shapeGridHeight));
        shapeContainerWrapper.addView(shapeContainer);

        // Add shapes to grid
        for (int i = 0; i < count; i++) {
            ShapeView shapeView = new ShapeView(getContext(), shapeType, color);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = SHAPE_SIZE;
            params.height = SHAPE_SIZE;
            params.setMargins(SHAPE_MARGIN, SHAPE_MARGIN, SHAPE_MARGIN, SHAPE_MARGIN);
            shapeView.setLayoutParams(params);
            shapeContainer.addView(shapeView);
        }

        // Divider (Ensure it's visible)
        View divider = new View(getContext());
        LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, 4
        );
        divider.setLayoutParams(dividerParams);
        divider.setBackgroundColor(Color.BLACK);

        // Count text at the bottom (Ensure it's visible)
        TextView countTextView = new TextView(getContext());
        countTextView.setText(String.valueOf(count));
        countTextView.setTextSize(18);
        countTextView.setTextColor(Color.BLACK);
        countTextView.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams countTextParams = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT
        );
        countTextParams.setMargins(0, 10, 0, 10);
        countTextView.setLayoutParams(countTextParams);

        // Add views in correct order
        addView(shapeContainerWrapper); // Wrapping GridLayout to allow centering
        addView(divider);               // Divider should be clearly visible now
        addView(countTextView);         // Count should now be properly displayed
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0, 0, getWidth(), getHeight(), borderPaint);
    }
}
