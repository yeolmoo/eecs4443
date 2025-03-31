package com.example.bravoproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class InstructionActivity extends AppCompatActivity {
    private String menuName, menuType, participantId, condition;

    private TextView taskText;
    private Button startButton, backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);

        menuName = getIntent().getStringExtra("menu_name");
        menuType = getIntent().getStringExtra("menu_type");
        participantId = getIntent().getStringExtra("participant_id");
        condition = getIntent().getStringExtra("condition");
        taskText = findViewById(R.id.taskText);
        startButton = findViewById(R.id.startButton);
        backButton = findViewById(R.id.backButton);
        taskText.setText(String.format("Task: use %s to navigate to", menuType));

        startButton.setOnClickListener(v -> {
            try {
                String className = "com.example.bravoproject." + menuName;
                Class<?> activityClass = Class.forName(className);
                Intent intent = new Intent(this, activityClass);
                intent.putExtra("participant_id", participantId);
                intent.putExtra("condition", condition);
                intent.putExtra("menu_name", menuName);
                intent.putExtra("menu_type", menuType);
                intent.putExtra("start_time", SystemClock.elapsedRealtime());
                startActivity(intent);
                finish();
            } catch (ClassNotFoundException e) {
                Toast.makeText(InstructionActivity.this, "Activity not found: " + menuName, Toast.LENGTH_SHORT).show();
            }
        });

        backButton.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }
}