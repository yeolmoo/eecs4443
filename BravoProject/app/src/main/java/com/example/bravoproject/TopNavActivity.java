package com.example.bravoproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.os.SystemClock;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TopNavActivity extends AppCompatActivity {

    private long startTime;
    private long startCpuTime;
    private long startMemoryUsage;
    private int misclickCount = 0;
    private final String menuType = "Top Navigation";

    private BottomNavigationView nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);

        nav = findViewById(R.id.topNavigationView);

        startTime = getIntent().getLongExtra("start_time", SystemClock.elapsedRealtime());
        startCpuTime = Process.getElapsedCpuTime();
        startMemoryUsage = getCurrentMemoryUsageKB();

        nav.setOnItemSelectedListener(this::handleMenuClick);
    }

    private boolean handleMenuClick(@NonNull MenuItem item) {
        String selected = item.getTitle().toString().trim();

        if (selected.equalsIgnoreCase("Settings")) {
            long elapsed = SystemClock.elapsedRealtime() - startTime;
            long cpuUsed = Process.getElapsedCpuTime() - startCpuTime;
            long memoryUsed = getCurrentMemoryUsageKB() - startMemoryUsage;

            Intent resultIntent = new Intent();
            resultIntent.putExtra("navigation_time", elapsed);
            resultIntent.putExtra("misclicks", misclickCount);
            resultIntent.putExtra("menu_type", menuType);
            resultIntent.putExtra("cpu_usage", cpuUsed);
            resultIntent.putExtra("memory_used_kb", memoryUsed);
            setResult(RESULT_OK, resultIntent);
            finish();

        } else {
            misclickCount++;
            Toast.makeText(this, "Wrong menu", Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN && !isTouchOnNavItem(ev)) {
            misclickCount++;
            Toast.makeText(this, "Misclick (outside menu)", Toast.LENGTH_SHORT).show();
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isTouchOnNavItem(MotionEvent ev) {
        int menuSize = nav.getMenu().size();
        float x = ev.getRawX();
        float y = ev.getRawY();

        for (int i = 0; i < menuSize; i++) {
            MenuItem item = nav.getMenu().getItem(i);
            View itemView = nav.findViewById(item.getItemId());
            if (itemView == null) continue;

            int[] location = new int[2];
            itemView.getLocationOnScreen(location);
            float left = location[0];
            float top = location[1];
            float right = left + itemView.getWidth();
            float bottom = top + itemView.getHeight();

            if (x >= left && x <= right && y >= top && y <= bottom) {
                return true;
            }
        }

        return false;
    }

    private long getCurrentMemoryUsageKB() {
        Runtime runtime = Runtime.getRuntime();
        return (runtime.totalMemory() - runtime.freeMemory()) / 1024;
    }
}
