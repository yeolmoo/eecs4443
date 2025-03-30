package com.example.bravoproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FabActivity extends AppCompatActivity {

    private long startTime;
    private int misclicks = 0;
    private boolean menuVisible = false;
    private String menuType = "Floating Action Button";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fab);

        startTime = getIntent().getLongExtra("start_time", SystemClock.elapsedRealtime());

        FloatingActionButton fab = findViewById(R.id.fabSettings);
        View fabMenu = findViewById(R.id.fabMenuLayout);

        fab.setOnClickListener(v -> {
            menuVisible = !menuVisible;
            fabMenu.setVisibility(menuVisible ? View.VISIBLE : View.GONE);
            fab.setImageResource(menuVisible ? R.drawable.ic_close : R.drawable.ic_add);
        });

        setupMisclick(R.id.buttonHome);
        setupMisclick(R.id.buttonSearch);
        setupMisclick(R.id.buttonProfile);
        setupMisclick(R.id.buttonHelp);

        MaterialButton btnSettings = findViewById(R.id.buttonSettings);
        btnSettings.setOnClickListener(v -> {
            long elapsed = SystemClock.elapsedRealtime() - startTime;
            Intent result = new Intent();
            result.putExtra("navigation_time", elapsed);
            result.putExtra("misclicks", misclicks);
            result.putExtra("menu_type", menuType);
            setResult(RESULT_OK, result);
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
}
