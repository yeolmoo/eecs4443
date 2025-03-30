package com.example.bravoproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TopNavActivity extends AppCompatActivity {

    private long startTime;
    private int misclickCount = 0;
    private String menuType = "Top Navigation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);

        startTime = getIntent().getLongExtra("start_time", SystemClock.elapsedRealtime());

        setupButton(R.id.buttonHome);
        setupButton(R.id.buttonSearch);
        setupButton(R.id.buttonSettings);
        setupButton(R.id.buttonProfile);
        setupButton(R.id.buttonHelp);
    }

    private void setupButton(int id) {
        BottomNavigationView topNav = findViewById(R.id.topNavigationView);
        topNav.setOnItemSelectedListener(item -> {
            String selected = item.getTitle().toString();
            if (selected.equals("Settings")) {
                long elapsed = SystemClock.elapsedRealtime() - startTime;
                Intent result = new Intent();
                result.putExtra("navigation_time", elapsed);
                result.putExtra("misclicks", misclickCount);
                result.putExtra("menu_type", "Top Navigation");
                setResult(RESULT_OK, result);
                finish();
            } else {
                misclickCount++;
                Toast.makeText(this, "Wrong menu", Toast.LENGTH_SHORT).show();
            }
            return true;

        });
    }
}
