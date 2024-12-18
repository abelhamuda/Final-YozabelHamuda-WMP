package com.example.finalexam;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText etLoginEmail, etLoginPassword;
    private Button btnLogin;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI elements
        etLoginEmail = findViewById(R.id.etLoginEmail);
        etLoginPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Login Button click listener
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etLoginEmail.getText().toString().trim();
                String password = etLoginPassword.getText().toString().trim();

                if (!validateInput(email, password)) {
                    Toast.makeText(LoginActivity.this, "Invalid email or password format", Toast.LENGTH_SHORT).show();
                } else {
                    loginStudent(email, password);
                }
            }
        });
    }

    /**
     * Validates the input for email and password.
     */
    private boolean validateInput(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return false;
        }
        return true;
    }

    /**
     * Handles the student login logic.
     */
    private void loginStudent(String email, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Students WHERE email = ? AND password = ?", new String[]{email, password});

        if (cursor.moveToFirst()) {
            int studentId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();

            // Save login session
            saveSession(studentId);

            // Redirect to EnrollmentActivity
            Intent intent = new Intent(LoginActivity.this, EnrollmentActivity.class);
            intent.putExtra("student_id", studentId);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
        db.close();
    }

    /**
     * Saves the logged-in student's session using SharedPreferences.
     */
    private void saveSession(int studentId) {
        SharedPreferences sharedPreferences = getSharedPreferences("FinalExamApp", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("student_id", studentId);
        editor.apply();
    }
}
