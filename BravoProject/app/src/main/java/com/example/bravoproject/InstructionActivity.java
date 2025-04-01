package com.example.bravoproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class InstructionActivity extends AppCompatActivity {

    private String menuName, menuType, participantId, condition;

    private TextView taskText;
    private Button startButton, backButton;

    private static final int MENU_ACTIVITY_REQUEST = 200;

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

        taskText.setText(String.format("Task: use %s to navigate to Settings", menuType));

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
                startActivityForResult(intent, MENU_ACTIVITY_REQUEST);
            } catch (ClassNotFoundException e) {
                Toast.makeText(InstructionActivity.this, "Activity not found: " + menuName, Toast.LENGTH_SHORT).show();
            }
        });

        backButton.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MENU_ACTIVITY_REQUEST && resultCode == RESULT_OK && data != null) {

            setResult(RESULT_OK, data);
            finish();
        }
    }
}
