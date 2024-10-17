package com.gelora.absensi.support;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.gelora.absensi.R;

public class FuelGaugeView extends View {

    private Bitmap gaugeBitmap;
    private Bitmap scaledGaugeBitmap;
    private Paint needlePaint;
    private float needleAngle = 0; // Angle for the needle

    public FuelGaugeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // Load the gauge bitmap
        gaugeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.fuel_gauge);
        needlePaint = new Paint();
        needlePaint.setColor(0xFFFF0000); // Color of the needle (red)
        needlePaint.setStrokeWidth(5);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // Scale the gaugeBitmap to fit the view size while maintaining aspect ratio
        scaledGaugeBitmap = Bitmap.createScaledBitmap(gaugeBitmap, w, h, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (scaledGaugeBitmap == null) return;

        // Draw the scaled gauge image
        canvas.drawBitmap(scaledGaugeBitmap, 0, 0, null);

        // Calculate the needle's position using the scaled bitmap size
        float centerX = scaledGaugeBitmap.getWidth() / 2;
        float centerY = scaledGaugeBitmap.getHeight() * 0.70f; // Move the needle's base lower
        float needleLength = scaledGaugeBitmap.getHeight() * 0.55f; // Make the needle slightly longer

        // Calculate the needle's end point based on the angle
        float endX = centerX + needleLength * (float) Math.cos(Math.toRadians(needleAngle));
        float endY = centerY - needleLength * (float) Math.sin(Math.toRadians(needleAngle));

        // Draw the needle
        canvas.drawLine(centerX, centerY, endX, endY, needlePaint);
    }


    // Method to set the fuel level (0 to 1)
    public void setFuelLevel(float level) {
        needleAngle = 180 * level; // Assuming E = 0 and F = 1
        invalidate(); // Redraw the view
    }
}
