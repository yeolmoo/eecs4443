package com.example.bravoproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RadialMenuActivity extends AppCompatActivity {

    private long startTime;
    private int misclicks = 0;
    private final String menuType = "Radial Menu";
    private final String SETTINGS_ITEM = "Settings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radial_menu);

        startTime = getIntent().getLongExtra("start_time", SystemClock.elapsedRealtime());

        RadialMenuView radialMenu = findViewById(R.id.radialMenu);
        radialMenu.setOnMenuSelectedListener(index -> {
            if (index == SETTINGS_ITEM) {
                long elapsed = SystemClock.elapsedRealtime() - startTime;
                Intent result = new Intent();
                result.putExtra("navigation_time", elapsed);
                result.putExtra("misclicks", misclicks);
                result.putExtra("menu_type", menuType);
                setResult(RESULT_OK, result);
                finish();
            } else {
                misclicks++;
                Toast.makeText(this, "Wrong menu. Try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
