package com.example.elementaryapp2.services.shape;

import android.content.Context;
import android.graphics.Color;
import android.widget.LinearLayout;
import java.util.Random;

public class ShapeGenerator {
    private Context context;
    private LinearLayout layoutContainer;
    private Random random = new Random();

    public ShapeGenerator(Context context, LinearLayout layoutContainer) {
        this.context = context;
        this.layoutContainer = layoutContainer;
    }

    public void generateShapes(int shapeType, int color, int count) {
        layoutContainer.removeAllViews(); // Clear previous shapes

        // Create and add ShapeBoxView
        ShapeBoxView shapeBoxView = new ShapeBoxView(context, shapeType, color, count);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT  // Height adjusts
        );

        shapeBoxView.setLayoutParams(params);
        layoutContainer.addView(shapeBoxView);
    }

    public int getRandomColor() {
        return Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }
}
