package com.example.paw1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private GoogleSignInClient googleSignInClient;

    private LinearLayout loginHeader;
    private ImageView userPhoto;
    private TextView userStatusBig;
    private TextView userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Firebase init
        FirebaseApp.initializeApp(this);
        FirebaseCrashlytics.getInstance().log("MainActivity started");
        auth = FirebaseAuth.getInstance();

        // Google client (for Google logout)
        googleSignInClient = GoogleSignIn.getClient(
                this,
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build()
        );

        // Views
        loginHeader   = findViewById(R.id.loginHeader);
        userPhoto     = findViewById(R.id.userPhoto);
        userStatusBig = findViewById(R.id.userStatusBig);
        userEmail     = findViewById(R.id.userEmail);

        Button startQuizButton = findViewById(R.id.startQuizButton);
        Button signupButton    = findViewById(R.id.signupButton);
        Button findShelters    = findViewById(R.id.findSheltersButton);
        Button logoutBtn       = findViewById(R.id.logoutBtn); // add this to activity_main.xml

        startQuizButton.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, QuizActivity.class)));

        signupButton.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, SignupActivity.class)));

        findShelters.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, SheltersActivity.class)));

        if (logoutBtn != null) {
            logoutBtn.setOnClickListener(v -> {
                // Firebase sign out
                auth.signOut();
                // Google sign out (no-op if not signed in with Google)
                googleSignInClient.signOut();

                // Back to login screen
                Intent intent = new Intent(MainActivity.this, GoogleSignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateHeader();
    }

    private void updateHeader() {
        FirebaseUser u = auth.getCurrentUser();
        if (u == null) {
            loginHeader.setVisibility(View.GONE);
            return;
        }

        loginHeader.setVisibility(View.VISIBLE);
        userStatusBig.setText("Logged in");
        userEmail.setText(u.getEmail() != null ? u.getEmail() : "");

        Uri photo = u.getPhotoUrl();
        if (photo != null) {
            Glide.with(this)
                    .load(photo)
                    .circleCrop()
                    .into(userPhoto);
        }
    }
}
