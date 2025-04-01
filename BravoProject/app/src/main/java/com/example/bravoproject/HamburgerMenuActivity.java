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
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class HamburgerMenuActivity extends AppCompatActivity {

    private long startTime;
    private long startCpuTime;
    private long startMemoryUsage;
    private int misclicks = 0;
    private String menuType = "Hamburger Menu";

    private DrawerLayout drawerLayout;
    private NavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hamburger_menu);

        drawerLayout = findViewById(R.id.drawerLayout);
        navView = findViewById(R.id.navView);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        startTime = getIntent().getLongExtra("start_time", SystemClock.elapsedRealtime());
        startCpuTime = Process.getElapsedCpuTime();
        startMemoryUsage = getCurrentMemoryUsageKB();

        navView.setNavigationItemSelectedListener(this::handleMenuClick);
    }

    private boolean handleMenuClick(@NonNull MenuItem item) {
        String selected = item.getTitle().toString();

        if (selected.equals("Settings")) {
            long elapsed = SystemClock.elapsedRealtime() - startTime;
            long endCpuTime = Process.getElapsedCpuTime();
            long cpuUsage = endCpuTime - startCpuTime;
            long endMemoryUsage = getCurrentMemoryUsageKB();
            long memoryUsed = endMemoryUsage - startMemoryUsage;

            Intent feedbackIntent = new Intent(this, ConditionSelectActivity.class);
            feedbackIntent.putExtra("feedback_mode", true);
            feedbackIntent.putExtra("navigation_time", elapsed);
            feedbackIntent.putExtra("misclicks", misclicks);
            feedbackIntent.putExtra("menu_type", menuType);
            feedbackIntent.putExtra("cpu_usage", cpuUsage);
            feedbackIntent.putExtra("memory_used_kb", memoryUsed);
            startActivity(feedbackIntent);
            finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View drawerView = drawerLayout.findViewById(R.id.navView);
            if (!isTouchOnSettingsItem(ev, drawerView)) {
                misclicks++;
                Toast.makeText(this, "Misclick recorded", Toast.LENGTH_SHORT).show();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isTouchOnSettingsItem(MotionEvent ev, View drawerView) {
        float x = ev.getRawX();
        float y = ev.getRawY();

        for (int i = 0; i < navView.getMenu().size(); i++) {
            MenuItem item = navView.getMenu().getItem(i);
            if (item.getTitle().toString().equals("Settings")) {
                View itemView = navView.findViewById(item.getItemId());
                if (itemView != null) {
                    int[] loc = new int[2];
                    itemView.getLocationOnScreen(loc);
                    float left = loc[0], top = loc[1];
                    float right = left + itemView.getWidth();
                    float bottom = top + itemView.getHeight();
                    if (x >= left && x <= right && y >= top && y <= bottom) {
                        return true;
                    }
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
