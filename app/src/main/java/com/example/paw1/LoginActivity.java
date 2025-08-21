package com.example.paw1;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    private EditText email;
    private EditText password;
    private Button loginBtn;
    private Button registerBtn; // ensure activity_login.xml has @+id/registerBtn

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        email       = findViewById(R.id.loginEmail);
        password    = findViewById(R.id.loginPassword);
        loginBtn    = findViewById(R.id.loginBtn);
        registerBtn = findViewById(R.id.registerBtn);

        loginBtn.setOnClickListener(v -> signIn());
        registerBtn.setOnClickListener(v -> register());
    }

    private void signIn() {
        String e = email.getText().toString().trim();
        String p = password.getText().toString().trim();

        if (!validate(e, p)) return;

        auth.signInWithEmailAndPassword(e, p)
                .addOnSuccessListener(r -> {
                    Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show();
                    navigateToMain();
                })
                .addOnFailureListener(err ->
                        Toast.makeText(this, "Login failed: " + err.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

    private void register() {
        String e = email.getText().toString().trim();
        String p = password.getText().toString().trim();

        if (!validate(e, p)) return;

        auth.createUserWithEmailAndPassword(e, p)
                .addOnSuccessListener(r -> {
                    Toast.makeText(this, "Registered!", Toast.LENGTH_SHORT).show();
                    navigateToMain();
                })
                .addOnFailureListener(err ->
                        Toast.makeText(this, "Registration failed: " + err.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

    private boolean validate(String e, String p) {
        if (TextUtils.isEmpty(e) || TextUtils.isEmpty(p)) {
            Toast.makeText(this, "Enter email and password", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(e).matches()) {
            Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (p.length() < 6) {
            Toast.makeText(this, "Password must be ≥ 6 chars", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void navigateToMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
