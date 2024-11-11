package com.library.libraryapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText editTextEmail, editTextPassword;
    Button buttonLogin;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView textView, forgotPassword;

    private static boolean isRedirecting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        buttonLogin = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.registerNow);
        forgotPassword = findViewById(R.id.forgot_password);


        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null && !isRedirecting) {
            isRedirecting = true;
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        textView.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish();
        });

        buttonLogin.setOnClickListener(v -> {
            String email = Objects.requireNonNull(editTextEmail.getText()).toString().trim();
            String password = Objects.requireNonNull(editTextPassword.getText()).toString().trim();

            if (validateInputs(email, password)) {
                loginUser(email, password);
            }
        });

        forgotPassword.setOnClickListener(v -> showForgotPasswordDialog());
    }

    private boolean validateInputs(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(LoginActivity.this, "Введите почту", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(LoginActivity.this, "Введите корректный email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "Введите пароль", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void loginUser(String email, String password) {
        progressBar.setVisibility(View.VISIBLE);
        Log.d("LoginActivity", "Attempting to login with email: " + email);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        Log.d("LoginActivity", "Авторизация успешна");
                        getSharedPreferences("UserPrefs", MODE_PRIVATE)
                                .edit()
                                .putBoolean("isLoggedIn", true)
                                .apply();
                        Toast.makeText(getApplicationContext(), "Успешно", Toast.LENGTH_SHORT).show();
                        navigateToMainActivity();
                    } else {
                        Log.e("LoginActivity", "Ошибка в авторизации: " + Objects.requireNonNull(task.getException()).getMessage());
                        Toast.makeText(LoginActivity.this, "Неверно введен логин или пароль", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void showForgotPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_forgot, null);
        EditText emailBox = dialogView.findViewById(R.id.emailBox);

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        dialogView.findViewById(R.id.btnReset).setOnClickListener(v -> {
            String userEmail = emailBox.getText().toString().trim();
            if (TextUtils.isEmpty(userEmail) || !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                Toast.makeText(LoginActivity.this, "Введите корректный email", Toast.LENGTH_SHORT).show();
                return;
            }
            mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Проверьте вашу почту", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    Toast.makeText(LoginActivity.this, "Ошибка сброса пароля", Toast.LENGTH_SHORT).show();
                    Log.e("LoginActivity", "Ошибка сброса пароля: " + Objects.requireNonNull(task.getException()).getMessage());
                }
            });
        });
        dialogView.findViewById(R.id.btnCancel).setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}
