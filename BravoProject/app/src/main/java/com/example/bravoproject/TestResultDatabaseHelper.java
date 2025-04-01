package com.example.bravoproject;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class TestResultDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Results.db";
    private static final int DATABASE_VERSION = 2;

    private Context context;
    private static final String TABLE_NAME = "test_results";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_PARTICIPANT_ID = "participant_id";
    private static final String COLUMN_CONDITION = "condition";
    private static final String COLUMN_MENU_TYPE = "menu_type";
    private static final String COLUMN_NAV_TIME = "navigation_time_ms";
    private static final String COLUMN_MISCLICKS = "misclicks";
    private static final String COLUMN_COMPLETED = "completed";
    private static final String COLUMN_FATIGUE_SCORE = "fatigue_score";
    private static final String COLUMN_TIMESTAMP = "timestamp";
    private static final String COLUMN_HANDEDNESS = "handedness";
    private static final String COLUMN_FEEDBACK = "feedback";
    private static final String COLUMN_CPU_USAGE = "cpu_usage";
    private static final String COLUMN_MEMORY_USED = "memory_used_kb";

    public TestResultDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_PARTICIPANT_ID + " TEXT, " +
                        COLUMN_CONDITION + " TEXT, " +
                        COLUMN_MENU_TYPE + " TEXT, " +
                        COLUMN_NAV_TIME + " INTEGER, " +
                        COLUMN_MISCLICKS + " INTEGER, " +
                        COLUMN_COMPLETED + " INTEGER, " +
                        COLUMN_FATIGUE_SCORE + " INTEGER, " +
                        COLUMN_CPU_USAGE + " INTEGER, " +
                        COLUMN_MEMORY_USED + " INTEGER, " +
                        COLUMN_TIMESTAMP + " TEXT, " +
                        COLUMN_HANDEDNESS + " TEXT, " +
                        COLUMN_FEEDBACK + " TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertTestResult(TestResult result) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_NAME,
                new String[]{COLUMN_ID},
                COLUMN_PARTICIPANT_ID + "=? AND " +
                        COLUMN_CONDITION + "=? AND " +
                        COLUMN_MENU_TYPE + "=?",
                new String[]{
                        result.getParticipantId(),
                        result.getCondition(),
                        result.getMenuType()
                },
                null, null, null);

        boolean exists = cursor.moveToFirst();
        cursor.close();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PARTICIPANT_ID, result.getParticipantId());
        values.put(COLUMN_CONDITION, result.getCondition());
        values.put(COLUMN_MENU_TYPE, result.getMenuType());
        values.put(COLUMN_NAV_TIME, result.getNavigationTimeMs());
        values.put(COLUMN_MISCLICKS, result.getMisclicks());
        values.put(COLUMN_COMPLETED, result.isCompleted() ? 1 : 0);
        values.put(COLUMN_FATIGUE_SCORE, result.getFatigueScore());
        values.put(COLUMN_CPU_USAGE, result.getCpuUsage());
        values.put(COLUMN_MEMORY_USED, result.getMemoryUsedKB());
        values.put(COLUMN_TIMESTAMP, result.getTimestamp());
        values.put(COLUMN_FEEDBACK, result.getFeedback());
        values.put(COLUMN_HANDEDNESS, result.getHandedness());

        if (exists) {
            db.update(TABLE_NAME, values,
                    COLUMN_PARTICIPANT_ID + "=? AND " +
                            COLUMN_CONDITION + "=? AND " +
                            COLUMN_MENU_TYPE + "=?",
                    new String[]{
                            result.getParticipantId(),
                            result.getCondition(),
                            result.getMenuType()
                    });
        } else {
            db.insert(TABLE_NAME, null, values);
        }

        db.close();
    }

    public List<TestResult> getAllResults() {
        List<TestResult> results = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                results.add(cursorToResult(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return results;
    }

    public List<TestResult> getResultsByParticipant(String participantId) {
        List<TestResult> results = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, null, COLUMN_PARTICIPANT_ID + " = ?",
                new String[]{participantId}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                results.add(cursorToResult(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return results;
    }

    public void deleteAllResults() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

    private TestResult cursorToResult(Cursor cursor) {
        TestResult result = new TestResult();

        result.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
        result.setParticipantId(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PARTICIPANT_ID)));
        result.setCondition(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONDITION)));
        result.setMenuType(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MENU_TYPE)));
        result.setNavigationTimeMs(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_NAV_TIME)));
        result.setMisclicks(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MISCLICKS)));
        result.setCompleted(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COMPLETED)) == 1);
        result.setFatigueScore(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FATIGUE_SCORE)));
        result.setCpuUsage(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_CPU_USAGE)));
        result.setMemoryUsedKB(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_MEMORY_USED)));
        result.setTimestamp(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIMESTAMP)));
        result.setHandedness(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HANDEDNESS)));
        result.setFeedback(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FEEDBACK)));

        return result;
    }

    public boolean isMenuCompleted(String participantId, String condition, String menuType) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{"id"},
                "participant_id = ? AND condition = ? AND menu_type = ? AND completed = 1",
                new String[]{participantId, condition, menuType},
                null, null, null);

        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }

    public void exportToCSV() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return;
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        try (FileOutputStream fos = new FileOutputStream(new File(context.getExternalFilesDir(null), "test_results.csv"));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos))) {

            writer.write("participant_id, condition, menu_type, navigation_time_ms, misclicks, cpu_usage, memory_used_kb, fatigue_score, timestamp, handedness, feedback\n");

            while (cursor.moveToNext()) {
                writer.write(String.format("%s, %s, %s, %d, %d, %d, %d, %d, %s, %s, %s\n",
                        cursor.getString(cursor.getColumnIndex(COLUMN_PARTICIPANT_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_CONDITION)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_MENU_TYPE)),
                        cursor.getLong(cursor.getColumnIndex(COLUMN_NAV_TIME)),
                        cursor.getInt(cursor.getColumnIndex(COLUMN_MISCLICKS)),
                        cursor.getLong(cursor.getColumnIndex(COLUMN_CPU_USAGE)),
                        cursor.getLong(cursor.getColumnIndex(COLUMN_MEMORY_USED)),
                        cursor.getInt(cursor.getColumnIndex(COLUMN_FATIGUE_SCORE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_TIMESTAMP)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_HANDEDNESS)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_FEEDBACK))
                ));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        cursor.close();
    }
}