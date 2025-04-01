package com.example.bravoproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ConditionSelectActivity extends AppCompatActivity {

    private Button buttonSitting, buttonWalking, buttonCompleteAll;
    private TextView textParticipant;

    private static final String PREFS_NAME = "ParticipantPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_condition_select);

        buttonSitting = findViewById(R.id.buttonSitting);
        buttonWalking = findViewById(R.id.buttonWalking);
        buttonCompleteAll = findViewById(R.id.buttonCompleteAll);
        textParticipant = findViewById(R.id.textParticipant);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String participantId = prefs.getString("current_name", "anonymous");

        // Show current participant name
        textParticipant.setText("Participant: " + participantId);

        buttonSitting.setOnClickListener(v -> startTest("sitting"));
        buttonWalking.setOnClickListener(v -> startTest("walking"));
        buttonCompleteAll.setOnClickListener(v -> confirmCompletion());
    }

    private void startTest(String condition) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String participantId = prefs.getString("current_name", "anonymous");

        // Save condition
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("condition", condition);
        editor.apply();

        Intent intent = new Intent(this, MenuTestActivity.class);
        intent.putExtra("participant_id", participantId);
        intent.putExtra("condition", condition);
        startActivity(intent);
    }

    private void confirmCompletion() {
        new AlertDialog.Builder(this)
                .setTitle("Finish All Tests?")
                .setMessage("Are you sure you have completed all required tasks?\nYour data will be saved and you'll be returned to the home screen.")
                .setPositiveButton("Yes, I'm done", (dialog, which) -> {
                    // Clear SharedPreferences
                    SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.clear();
                    editor.apply();

                    // Navigate to MainActivity
                    Intent intent = new Intent(ConditionSelectActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("No", null)
                .show();
    }
}
