package com.example.bravoproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class SwipeMenuActivity extends AppCompatActivity {

    private long startTime;
    private int misclicks = 0;
    private String menuType = "Swipe Menu";
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_menu);

        drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.navView);


        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        startTime = getIntent().getLongExtra("start_time", SystemClock.elapsedRealtime());

        navigationView.setNavigationItemSelectedListener(item -> handleMenuClick(item));



    }

    private boolean handleMenuClick(@NonNull MenuItem item) {
        String selected = item.getTitle().toString();

        if (selected.equals("Settings")) {
            long elapsed = SystemClock.elapsedRealtime() - startTime;
            Intent result = new Intent();
            result.putExtra("navigation_time", elapsed);
            result.putExtra("misclicks", misclicks);
            result.putExtra("menu_type", menuType);
            setResult(RESULT_OK, result);          finish();
        } else {
            misclicks++;
            Toast.makeText(this, "Wrong menu", Toast.LENGTH_SHORT).show();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
