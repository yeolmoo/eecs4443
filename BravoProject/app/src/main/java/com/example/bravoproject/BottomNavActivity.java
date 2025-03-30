package com.example.bravoproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavActivity extends AppCompatActivity {

    private long startTime;
    private int misclickCount = 0;
    private String menuType = "Bottom Navigation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        TextView textInstruction = findViewById(R.id.textInstruction);

        startTime = getIntent().getLongExtra("start_time", SystemClock.elapsedRealtime());

        bottomNavigationView.setOnItemSelectedListener(item -> {
            String selected = item.getTitle().toString();

            if (selected.equals("Settings")) {
                long elapsed = SystemClock.elapsedRealtime() - startTime;
                Intent resultIntent = new Intent();
                resultIntent.putExtra("navigation_time", elapsed);
                resultIntent.putExtra("misclicks", misclickCount);
                resultIntent.putExtra("menu_type", menuType);
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                misclickCount++;
                Toast.makeText(this, "Wrong menu", Toast.LENGTH_SHORT).show();
            }
            return true;
        });
    }
}
