package com.example.bravoproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FabActivity extends AppCompatActivity {

    private long startTime;
    private long startCpuTime;
    private long startMemoryUsage;
    private int misclicks = 0;
    private boolean menuVisible = false;
    private String menuType = "Floating Action Button";

    private View fabMenu;
    private FloatingActionButton fabButton;
    private MaterialButton btnSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fab);

        startTime = getIntent().getLongExtra("start_time", SystemClock.elapsedRealtime());
        startCpuTime = Process.getElapsedCpuTime();
        startMemoryUsage = getCurrentMemoryUsageKB();

        fabButton = findViewById(R.id.fabSettings);
        fabMenu = findViewById(R.id.fabMenuLayout);
        btnSettings = findViewById(R.id.buttonSettings);

        fabButton.setOnClickListener(v -> {
            menuVisible = !menuVisible;
            fabMenu.setVisibility(menuVisible ? View.VISIBLE : View.GONE);
            fabButton.setImageResource(menuVisible ? R.drawable.ic_close : R.drawable.ic_add);
        });

        setupMisclick(R.id.buttonHome);
        setupMisclick(R.id.buttonSearch);
        setupMisclick(R.id.buttonProfile);
        setupMisclick(R.id.buttonHelp);

        btnSettings.setOnClickListener(v -> {
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

        });
    }

    private void setupMisclick(int id) {
        MaterialButton btn = findViewById(id);
        btn.setOnClickListener(v -> {
            misclicks++;
            Toast.makeText(this, "Wrong menu", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (isTouchOnFabButton(ev)) {
                return super.dispatchTouchEvent(ev);
            }

            if (menuVisible) {
                if (!isTouchOnAnyFabButton(ev)) {
                    misclicks++;
                    Toast.makeText(this, "Misclick (outside menu)", Toast.LENGTH_SHORT).show();
                }
            } else {
                misclicks++;
                Toast.makeText(this, "Misclick (before opening menu)", Toast.LENGTH_SHORT).show();
            }
        }

        return super.dispatchTouchEvent(ev);
    }

    private boolean isTouchOnFabButton(MotionEvent ev) {
        if (fabButton == null) return false;

        int[] loc = new int[2];
        fabButton.getLocationOnScreen(loc);

        float x = ev.getRawX();
        float y = ev.getRawY();

        float left = loc[0];
        float top = loc[1];
        float right = left + fabButton.getWidth();
        float bottom = top + fabButton.getHeight();

        return x >= left && x <= right && y >= top && y <= bottom;
    }


    private boolean isTouchOnAnyFabButton(MotionEvent ev) {
        int[] ids = {
                R.id.buttonHome,
                R.id.buttonSearch,
                R.id.buttonProfile,
                R.id.buttonHelp,
                R.id.buttonSettings
        };

        float x = ev.getRawX();
        float y = ev.getRawY();

        for (int id : ids) {
            View btn = findViewById(id);
            if (btn == null) continue;

            int[] loc = new int[2];
            btn.getLocationOnScreen(loc);
            float left = loc[0], top = loc[1];
            float right = left + btn.getWidth();
            float bottom = top + btn.getHeight();

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
