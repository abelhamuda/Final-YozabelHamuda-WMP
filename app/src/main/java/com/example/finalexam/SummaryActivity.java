package com.example.finalexam;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SummaryActivity extends AppCompatActivity {
    private TextView tvSummary;
    private DatabaseHelper dbHelper;
    private int studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        // Initialize UI elements
        tvSummary = findViewById(R.id.tvSummary);

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Get the logged-in student ID
        studentId = getIntent().getIntExtra("student_id", -1);

        // Display the enrollment summary
        displayEnrollmentSummary();
    }

    private void displayEnrollmentSummary() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT subject_name, credits FROM Enrollments WHERE student_id = ?",
                new String[]{String.valueOf(studentId)}
        );

        StringBuilder summary = new StringBuilder("Enrolled Subjects:\n\n");
        int totalCredits = 0;

        while (cursor.moveToNext()) {
            String subjectName = cursor.getString(cursor.getColumnIndexOrThrow("subject_name"));
            int credits = cursor.getInt(cursor.getColumnIndexOrThrow("credits"));
            totalCredits += credits;

            summary.append("- ").append(subjectName).append(" (").append(credits).append(" credits)\n");
        }

        summary.append("\nTotal Credits: ").append(totalCredits);

        tvSummary.setText(summary.toString());
        cursor.close();
        db.close();
    }
}
