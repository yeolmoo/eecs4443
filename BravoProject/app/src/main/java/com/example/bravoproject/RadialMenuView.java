package com.example.bravoproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Arrays;
import java.util.List;

public class RadialMenuView extends View {

    private Paint paint;
    private Paint textPaint;
    private Paint centerTextPaint;
    private int selectedIndex = -1;
    private boolean isLongPressed = false;

    private float centerX, centerY;
    private float downX, downY;

    private OnMenuSelectedListener listener;

    private List<String> menuItems = Arrays.asList("Home", "Search", "Settings", "Profile", "Help");

    public RadialMenuView(Context context) {
        super(context);
        init();
    }

    public RadialMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(32f);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);

        centerTextPaint = new Paint();
        centerTextPaint.setColor(Color.DKGRAY);
        centerTextPaint.setTextSize(36f);
        centerTextPaint.setTextAlign(Paint.Align.CENTER);
        centerTextPaint.setFakeBoldText(true);
        centerTextPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int radius = Math.min(width, height) / 2 - 20;

        centerX = width / 2f;
        centerY = height / 2f;

        RectF rect = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        float sweepAngle = 360f / menuItems.size();

        for (int i = 0; i < menuItems.size(); i++) {
            paint.setColor(i == selectedIndex ? 0xFF9C27B0 : 0xFFCCCCCC);
            canvas.drawArc(rect, i * sweepAngle, sweepAngle, true, paint);

            // draw label
            float angle = (float) Math.toRadians(i * sweepAngle + sweepAngle / 2);
            float textRadius = radius * 0.6f;
            float textX = centerX + (float) (textRadius * Math.cos(angle));
            float textY = centerY + (float) (textRadius * Math.sin(angle)) + 10;
            canvas.drawText(menuItems.get(i), textX, textY, textPaint);
        }

        // center instruction
        canvas.drawText("Long press and drag to Settings", centerX, centerY, centerTextPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = touchX;
                downY = touchY;
                isLongPressed = false;
                postDelayed(() -> {
                    isLongPressed = true;
                    updateSelection(touchX, touchY);
                    invalidate();
                }, 300); // long press threshold
                return true;

            case MotionEvent.ACTION_MOVE:
                if (isLongPressed) {
                    updateSelection(touchX, touchY);
                    invalidate();
                }
                return true;

            case MotionEvent.ACTION_UP:
                if (isLongPressed && selectedIndex != -1) {
                    if (listener != null) {
                        listener.onMenuSelected(menuItems.get(selectedIndex));
                    }
                }
                isLongPressed = false;
                selectedIndex = -1;
                invalidate();
                return true;
        }
        return super.onTouchEvent(event);
    }

    private void updateSelection(float x, float y) {
        float dx = x - centerX;
        float dy = y - centerY;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        if (distance < 100) {
            selectedIndex = -1;
            return;
        }

        double angle = Math.toDegrees(Math.atan2(dy, dx));
        angle = (angle + 360) % 360;

        int section = (int) (angle / (360f / menuItems.size()));
        selectedIndex = section;
    }

    public void setOnMenuSelectedListener(OnMenuSelectedListener listener) {
        this.listener = listener;
    }

    public interface OnMenuSelectedListener {
        void onMenuSelected(String menu);
    }


    public boolean isMenuOpen() {
        return isLongPressed;
    }


    public boolean isTouchOnItem(float x, float y, String itemLabel) {
        if (!isLongPressed) return false;

        float dx = x - centerX;
        float dy = y - centerY;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        if (distance < 100) return false;

        double angle = Math.toDegrees(Math.atan2(dy, dx));
        angle = (angle + 360) % 360;

        int index = (int) (angle / (360f / menuItems.size()));
        if (index < 0 || index >= menuItems.size()) return false;

        return menuItems.get(index).equals(itemLabel);
    }
}
