package com.example.elementaryapp2.services;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextPaint;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;

public class DottedTextView extends AppCompatTextView {
    private TextPaint dotPaint;
    private Path textPath;
    private float textSize;
    private boolean isTextChanged = false;

    public DottedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        textSize = getTextSize(); // Get text size from XML
        dotPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        dotPaint.setStyle(Paint.Style.STROKE); // Outline mode
        dotPaint.setStrokeWidth(textSize / 50); // Adjust stroke width
        dotPaint.setPathEffect(new DashPathEffect(new float[]{textSize / 50, textSize / 50}, 0)); // Dotted effect
        dotPaint.setColor(getCurrentTextColor()); // Match text color
        dotPaint.setTextSize(textSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (textPath == null || isTextChanged) {
            textPath = new Path();
            // Properly position the text
            getPaint().getTextPath(getText().toString(), 0, getText().length(), 0, getTextSize(), textPath);
            isTextChanged = false;
        }

        // Draw clean, single-layer dotted text
        canvas.drawPath(textPath, dotPaint);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        isTextChanged = true;
        invalidate(); // Redraw the view
    }

}



