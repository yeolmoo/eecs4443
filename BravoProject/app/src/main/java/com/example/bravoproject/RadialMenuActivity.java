package com.example.bravoproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RadialMenuActivity extends AppCompatActivity {

    private long startTime;
    private long startCpuTime;
    private long startMemoryUsage;
    private int misclicks = 0;

    private final String menuType = "Radial Menu";
    private final String SETTINGS_ITEM = "Settings";

    private RadialMenuView radialMenu;
    private boolean selectionHandled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radial_menu);

        startTime = getIntent().getLongExtra("start_time", SystemClock.elapsedRealtime());
        startCpuTime = Process.getElapsedCpuTime();
        startMemoryUsage = getCurrentMemoryUsageKB();

        radialMenu = findViewById(R.id.radialMenu);

        radialMenu.setOnMenuSelectedListener(selectedItem -> {
            selectionHandled = true;
            if (selectedItem != null && selectedItem.equals(SETTINGS_ITEM)) {
                long elapsed = SystemClock.elapsedRealtime() - startTime;
                long cpuUsed = Process.getElapsedCpuTime() - startCpuTime;
                long memoryUsed = getCurrentMemoryUsageKB() - startMemoryUsage;

                Intent resultIntent = new Intent();
                resultIntent.putExtra("navigation_time", elapsed);
                resultIntent.putExtra("misclicks", misclicks);
                resultIntent.putExtra("menu_type", menuType);
                resultIntent.putExtra("cpu_usage", cpuUsed);
                resultIntent.putExtra("memory_used_kb", memoryUsed);
                setResult(RESULT_OK, resultIntent);
                finish();

            } else {
                misclicks++;
                Toast.makeText(this, "Wrong menu. Try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {

            if (!selectionHandled && !isTouchOnSettings(ev)) {
                misclicks++;
                Toast.makeText(this, "Misclick recorded", Toast.LENGTH_SHORT).show();
            }
            selectionHandled = false;
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isTouchOnSettings(MotionEvent ev) {
        if (radialMenu == null || !radialMenu.isMenuOpen()) return false;
        return radialMenu.isTouchOnItem(ev.getRawX(), ev.getRawY(), SETTINGS_ITEM);
    }

    private long getCurrentMemoryUsageKB() {
        Runtime runtime = Runtime.getRuntime();
        return (runtime.totalMemory() - runtime.freeMemory()) / 1024;
    }
}
