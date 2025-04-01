package com.example.bravoproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavActivity extends AppCompatActivity {

    private long startTime;
    private long startCpuTime;
    private long startMemoryUsage;
    private int misclickCount = 0;
    private String menuType = "Bottom Navigation";

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        TextView textInstruction = findViewById(R.id.textInstruction);

        startTime = getIntent().getLongExtra("start_time", SystemClock.elapsedRealtime());
        startCpuTime = Process.getElapsedCpuTime();
        startMemoryUsage = getCurrentMemoryUsageKB();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            String selected = item.getTitle().toString();

            if (selected.equals("Settings")) {
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
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN && !isTouchOnSettingsItem(ev)) {
            misclickCount++;
            Toast.makeText(this, "Misclick recorded", Toast.LENGTH_SHORT).show();
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isTouchOnSettingsItem(MotionEvent ev) {
        float x = ev.getRawX();
        float y = ev.getRawY();

        for (int i = 0; i < bottomNavigationView.getMenu().size(); i++) {
            int itemId = bottomNavigationView.getMenu().getItem(i).getItemId();
            View itemView = bottomNavigationView.findViewById(itemId);
            if (itemView != null) {
                int[] location = new int[2];
                itemView.getLocationOnScreen(location);
                float left = location[0], top = location[1];
                float right = left + itemView.getWidth();
                float bottom = top + itemView.getHeight();

                if (x >= left && x <= right && y >= top && y <= bottom) {
                    String title = bottomNavigationView.getMenu().getItem(i).getTitle().toString();
                    return title.equals("Settings");
                }
            }
        }

        return false;
    }

    private long getCurrentMemoryUsageKB() {
        Runtime runtime = Runtime.getRuntime();
        return (runtime.totalMemory() - runtime.freeMemory()) / 1024;
    }
}
