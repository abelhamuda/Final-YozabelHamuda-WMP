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

        studentId = getIntent().getIntExtra("student_id", -1);

        spSubjects = findViewById(R.id.spSubjects);
        btnAddSubject = findViewById(R.id.btnAddSubject);
        btnViewSummary = findViewById(R.id.btnFinish);

        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        String[] subjects = {"Introduction to Programming (3 credits)", "Data Structures and Algorithms (3 credits)", "Database Systems (3 credits)", "Computer Networks (3 credits)", "Operating Systems (3 credits)", "Software Engineering (3 credits)", "Artificial Intelligence (3 credits)", "Web Development (3 credits)", "Cybersecurity Fundamentals (3 credits)", "Mobile Application Development (3 credits)"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, subjects);
        spSubjects.setAdapter(adapter);

        btnAddSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedSubject = spSubjects.getSelectedItem().toString();
                int credits = Integer.parseInt(selectedSubject.replaceAll("[^0-9]", ""));

                addSubject(selectedSubject, credits);
            }
        });

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
