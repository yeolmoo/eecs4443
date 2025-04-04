package com.example.bravoproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private RadioGroup handednessGroup;
    private RadioButton radioLeft, radioRight;
    private EditText editFirstName;
    private Button buttonNext;
  //  private Button buttonDownloadData;

    private static final String PREFS_NAME = "ParticipantPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handednessGroup = findViewById(R.id.handednessGroup);
        radioLeft = findViewById(R.id.radioLeft);
        radioRight = findViewById(R.id.radioRight);
        editFirstName = findViewById(R.id.editFirstName);
        buttonNext = findViewById(R.id.buttonNext);
       // buttonDownloadData = findViewById(R.id.buttonDownloadData);

        buttonNext.setOnClickListener(v -> {
            String name = editFirstName.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
                return;
            }

            int selectedId = handednessGroup.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(this, "Please select your handedness", Toast.LENGTH_SHORT).show();
                return;
            }

            String handedness = (selectedId == R.id.radioLeft) ? "Left" : "Right";

            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            int count = 1;
            String uniqueName = name;

            while (prefs.contains("name_" + uniqueName)) {
                count++;
                uniqueName = name + count;
            }

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("current_name", uniqueName);
            editor.putString("name_" + uniqueName, "used");
            editor.putString("handedness_" + uniqueName, handedness);
            editor.apply();

            // Go to ConditionSelectActivity
            Intent intent = new Intent(this, ConditionSelectActivity.class);
            startActivity(intent);
        });

//        buttonDownloadData.setOnClickListener(v -> {
//            downloadDataAsCSV();
//        });

        editFirstName.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(editFirstName.getWindowToken(), 0);
                }
                return true;
            }
            return false;
        });
    }

//    private void downloadDataAsCSV() {
//        TestResultDatabaseHelper dbHelper = new TestResultDatabaseHelper(this);
//        String filePath = dbHelper.exportToCSV();
//
//        if (filePath != null) {
//            Toast.makeText(this, "Data exported to:\n" + filePath, Toast.LENGTH_LONG).show();
//        } else {
//            Toast.makeText(this, "Export failed", Toast.LENGTH_SHORT).show();
//        }
//    }

}
