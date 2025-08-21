package com.example.paw1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        EditText firstName = findViewById(R.id.firstName);
        EditText lastName  = findViewById(R.id.lastName);
        EditText email     = findViewById(R.id.email);
        EditText password  = findViewById(R.id.passwordEditText);
        Button submitBtn   = findViewById(R.id.submitBtn);

        Button signInHereBtn = findViewById(R.id.signInHereBtn);
        if (signInHereBtn != null) {
            signInHereBtn.setText("Already have an account? Sign in here.");
            signInHereBtn.setOnClickListener(v -> {
                Intent i = new Intent(SignupActivity.this, GoogleSignInActivity.class);
                startActivity(i);
            });
        }

        submitBtn.setOnClickListener(v -> {
            String f = firstName.getText().toString().trim();
            String l = lastName.getText().toString().trim();
            String e = email.getText().toString().trim();
            String p = password.getText().toString().trim();

            if (f.isEmpty() || l.isEmpty() || e.isEmpty() || p.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                mAuth.createUserWithEmailAndPassword(e, p)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        String uid = user.getUid();
                                        mDatabase.child(uid).child("firstName").setValue(f);
                                        mDatabase.child(uid).child("lastName").setValue(l);
                                        mDatabase.child(uid).child("email").setValue(e);
                                    }
                                    Toast.makeText(SignupActivity.this, "Signup successful", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(SignupActivity.this, "Signup failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
