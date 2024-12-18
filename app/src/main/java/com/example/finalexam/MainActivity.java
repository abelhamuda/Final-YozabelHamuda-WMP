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

        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        sharedPreferences = getSharedPreferences("FinalExamApp", MODE_PRIVATE);

        if (isLoggedIn()) {
            Intent intent = new Intent(MainActivity.this, EnrollmentActivity.class);
            intent.putExtra("student_id", getLoggedInUserId());
            startActivity(intent);
            finish();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });
    }

    private boolean isLoggedIn() {
        return sharedPreferences.contains("student_id");
    }

    private int getLoggedInUserId() {
        return sharedPreferences.getInt("student_id", -1);
    }
}
