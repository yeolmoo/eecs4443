package com.example.bravoproject;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class MenuTestActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MenuAdapter adapter;
    private ArrayList<MenuItem> menuList;
    private String participantId, condition;
    private TestResultDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_test);

        participantId = getIntent().getStringExtra("participant_id");
        condition = getIntent().getStringExtra("condition");
        dbHelper = new TestResultDatabaseHelper(this);

        recyclerView = findViewById(R.id.recyclerViewMenus);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        TextView textCondition = findViewById(R.id.textCondition);
        if (Objects.equals(condition, "sitting")) {
            textCondition.setText("Sitting Test");
        } else {
            textCondition.setText("Walking Test");
        }

        menuList = new ArrayList<>();
        menuList.add(new MenuItem("Hamburger Menu", "HamburgerMenuActivity"));
        menuList.add(new MenuItem("Top Navigation", "TopNavActivity"));
        menuList.add(new MenuItem("Bottom Navigation", "BottomNavActivity"));
        menuList.add(new MenuItem("Floating Action Button", "FabActivity"));
        menuList.add(new MenuItem("Radial Menu", "RadialMenuActivity"));

        adapter = new MenuAdapter(this, menuList, participantId, condition);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            long navTime = data.getLongExtra("navigation_time", 0);
            int misclicks = data.getIntExtra("misclicks", 0);
            long cpuUsage = data.getLongExtra("cpu_usage", 0);
            long memoryUsed = data.getLongExtra("memory_used_kb", 0);
            String menuType = data.getStringExtra("menu_type");

            showFeedbackDialog(navTime, misclicks, cpuUsage, memoryUsed, menuType);
        }
    }

    private void showFeedbackDialog(long navTime, int misclicks, long cpuUsage, long memoryUsedKb, String menuType) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_feedback_seekbar, null);
        TextView resultSummary = dialogView.findViewById(R.id.textResultSummary);
        TextView fatigueLabel = dialogView.findViewById(R.id.textFatigueLabel);
        EditText inputText = dialogView.findViewById(R.id.feedbackInput);
        SeekBar fatigueSeekBar = dialogView.findViewById(R.id.fatigueSeekBar);

        String resultText = String.format(
                "Menu: %s\nTime: %d ms\nMisclicks: %d\nCPU: %d ms\nMemory: %d KB",
                menuType, navTime, misclicks, cpuUsage, memoryUsedKb
        );
        resultSummary.setText(resultText);

        fatigueSeekBar.setMin(1);
        fatigueSeekBar.setMax(5);
        fatigueSeekBar.setProgress(3);
        fatigueLabel.setText("Fatigue Level: 3");

        fatigueSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                fatigueLabel.setText("Fatigue Level: " + progress);
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        new AlertDialog.Builder(this)
                .setTitle("Feedback & Fatigue")
                .setView(dialogView)
                .setPositiveButton("Save and Continue", (dialog, which) -> {
                    int fatigue = Math.max(1, fatigueSeekBar.getProgress());
                    String feedback = inputText.getText().toString();

                    TestResult result = new TestResult();
                    result.setParticipantId(participantId);
                    result.setCondition(condition);
                    result.setMenuType(menuType);
                    result.setNavigationTimeMs(navTime);
                    result.setMisclicks(misclicks);
                    result.setCpuUsage(cpuUsage);
                    result.setMemoryUsedKB(memoryUsedKb);
                    result.setCompleted(true);
                    result.setComfortScore(0);
                    result.setFatigueScore(fatigue);
                    result.setFeedback(feedback);
                    result.setBatteryStart(0);
                    result.setBatteryEnd(0);
                    result.setTimestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));

                    dbHelper.insertTestResult(result);
                    Toast.makeText(this, "Result saved!", Toast.LENGTH_SHORT).show();
                })
                .setCancelable(false)
                .show();
    }
}