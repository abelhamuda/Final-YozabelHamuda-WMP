package com.example.finalexam;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button btnLogin, btnRegister;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("FinalExamApp", MODE_PRIVATE);

        // Check if a user is already logged in
        if (isLoggedIn()) {
            // Redirect to EnrollmentActivity
            Intent intent = new Intent(MainActivity.this, EnrollmentActivity.class);
            intent.putExtra("student_id", getLoggedInUserId());
            startActivity(intent);
            finish();
        }

        // Set button listeners
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Redirect to LoginActivity
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Redirect to RegisterActivity
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });
    }

    private boolean isLoggedIn() {
        // Check if a user ID is stored in SharedPreferences
        return sharedPreferences.contains("student_id");
    }

    private int getLoggedInUserId() {
        // Retrieve the logged-in user ID from SharedPreferences
        return sharedPreferences.getInt("student_id", -1);
    }
}
