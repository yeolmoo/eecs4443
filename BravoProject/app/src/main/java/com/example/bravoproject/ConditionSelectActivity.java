package com.example.bravoproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ConditionSelectActivity extends AppCompatActivity {

    private Button buttonSitting, buttonWalking;
    private TextView textParticipant;

    private static final String PREFS_NAME = "ParticipantPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_condition_select);

        buttonSitting = findViewById(R.id.buttonSitting);
        buttonWalking = findViewById(R.id.buttonWalking);
        textParticipant = findViewById(R.id.textParticipant);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String participantId = prefs.getString("current_name", "anonymous");

        // show current particpants name
        textParticipant.setText("Participant: " + participantId);

        buttonSitting.setOnClickListener(v -> startTest("sitting"));
        buttonWalking.setOnClickListener(v -> startTest("walking"));
    }

    private void startTest(String condition) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String participantId = prefs.getString("current_name", "anonymous");

        // save preference
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("condition", condition);
        editor.apply();

        Intent intent = new Intent(this, MenuTestActivity.class);
        intent.putExtra("participant_id", participantId);
        intent.putExtra("condition", condition);
        startActivity(intent);
    }
}
