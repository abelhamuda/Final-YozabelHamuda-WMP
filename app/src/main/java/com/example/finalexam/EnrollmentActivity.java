package com.example.finalexam;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EnrollmentActivity extends AppCompatActivity {
    private Spinner spSubjects;
    private Button btnAddSubject, btnViewSummary;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private int studentId;
    private final int MAX_CREDITS = 24;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrollment);

        // Get the logged-in student ID
        studentId = getIntent().getIntExtra("student_id", -1);

        // Initialize UI elements
        spSubjects = findViewById(R.id.spSubjects);
        btnAddSubject = findViewById(R.id.btnAddSubject);
        btnViewSummary = findViewById(R.id.btnFinish);

        // Initialize DatabaseHelper and SQLiteDatabase
        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        // Populate Spinner with subjects
        String[] subjects = {"Math (3 credits)", "English (2 credits)", "Science (4 credits)", "History (3 credits)"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, subjects);
        spSubjects.setAdapter(adapter);

        // Add Subject Button click listener
        btnAddSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedSubject = spSubjects.getSelectedItem().toString();
                int credits = Integer.parseInt(selectedSubject.replaceAll("[^0-9]", ""));

                addSubject(selectedSubject, credits);
            }
        });

        // View Summary Button click listener
        btnViewSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnrollmentActivity.this, SummaryActivity.class);
                intent.putExtra("student_id", studentId);
                startActivity(intent);
            }
        });
    }

    private void addSubject(String subjectName, int credits) {
        // Calculate total credits
        int totalCredits = calculateTotalCredits();
        if (totalCredits + credits > MAX_CREDITS) {
            Toast.makeText(this, "Credit limit exceeded!", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put("student_id", studentId);
        values.put("subject_name", subjectName);
        values.put("credits", credits);

        long result = db.insert("Enrollments", null, values);
        if (result != -1) {
            Toast.makeText(this, "Subject added successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to add subject", Toast.LENGTH_SHORT).show();
        }
    }

    private int calculateTotalCredits() {
        Cursor cursor = db.rawQuery("SELECT SUM(credits) AS total FROM Enrollments WHERE student_id = ?", new String[]{String.valueOf(studentId)});

        int totalCredits = 0;
        if (cursor.moveToFirst()) {
            totalCredits = cursor.getInt(cursor.getColumnIndexOrThrow("total"));
        }

        cursor.close();
        return totalCredits;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
}
