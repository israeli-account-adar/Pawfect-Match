package com.example.paw1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizActivity extends AppCompatActivity {

    private RecyclerView quizRecyclerView;
    private Button submitButton;
    private QuizAdapter quizAdapter;

    private final List<String> questions = new ArrayList<>();
    private final List<String[]> answers = new ArrayList<>();
    private int[] userAnswers;
    private int dogScore = 0;
    private int catScore = 0;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        quizRecyclerView = findViewById(R.id.quizRecyclerView);
        submitButton = findViewById(R.id.submitButton);

        quizRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        quizRecyclerView.setHasFixedSize(false);

        db = FirebaseFirestore.getInstance();

        loadQuestionsFromFirestore();

        submitButton.setOnClickListener(v -> {
            calculateScores();
            finishQuiz();
        });
    }

    private void loadQuestionsFromFirestore() {
        db.collection("quiz_questions")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();

                        // Sort by document ID (expecting "0","1","2",...)
                        docs.sort((a, b) -> a.getId().compareTo(b.getId()));

                        for (DocumentSnapshot doc : docs) {
                            String questionText = doc.getString("question");
                            @SuppressWarnings("unchecked")
                            List<String> answerList = (List<String>) doc.get("answers");
                            if (questionText != null && answerList != null) {
                                questions.add(questionText);
                                answers.add(answerList.toArray(new String[0]));
                            }
                        }

                        userAnswers = new int[questions.size()];
                        for (int i = 0; i < userAnswers.length; i++) userAnswers[i] = -1;

                        quizAdapter = new QuizAdapter(questions, answers, userAnswers);
                        quizRecyclerView.setAdapter(quizAdapter);
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to load quiz data.", Toast.LENGTH_SHORT).show()
                );
    }

    private void calculateScores() {
        // Your existing scoring rules
        for (int i = 0; i < userAnswers.length; i++) {
            int answer = userAnswers[i];
            switch (i) {
                case 0:
                case 2:
                case 6:
                    if (answer == 0) catScore += 2;
                    if (answer == 2) dogScore += 2;
                    break;
                case 9:
                    if (answer == 0) dogScore += 2;
                    if (answer == 2) catScore += 2;
                    break;
            }
        }
    }

    private void finishQuiz() {
        int totalScore = dogScore + catScore;
        int maxScore = 8; // 4 scoring questions × 2 points each
        int percent = (int) ((totalScore / (float) maxScore) * 100);
        String petType = (dogScore > catScore) ? "dog" : "cat"; // result key (optional)

        // Analytics event
        Bundle bundle = new Bundle();
        bundle.putInt("quiz_score_percent", percent);
        FirebaseAnalytics.getInstance(this).logEvent("quiz_completed", bundle);

        // Persist result under signed-in user (optional)
        List<Integer> answersList = new ArrayList<>();
        for (int a : userAnswers) answersList.add(a);

        Map<String, Object> resultData = new HashMap<>();
        resultData.put("dogScore", dogScore);
        resultData.put("catScore", catScore);
        resultData.put("percent", percent);
        resultData.put("petType", petType);
        resultData.put("userAnswers", answersList);
        resultData.put("timestamp", System.currentTimeMillis());

        FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
        if (u != null) {
            FirebaseFirestore.getInstance()
                    .collection("users").document(u.getUid())
                    .collection("quiz_results")
                    .add(resultData)
                    .addOnSuccessListener(docRef ->
                            Log.d("Firestore", "Saved user result: " + docRef.getId()))
                    .addOnFailureListener(e ->
                            Log.e("Firestore", "Failed to save user result", e));
        } else {
            Toast.makeText(this, "Please sign in to save your quiz result.", Toast.LENGTH_SHORT).show();
        }

        // Navigate to single-breed ResultActivity (uses dogScore/catScore/userAnswers)
        Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
        intent.putExtra("dogScore", dogScore);
        intent.putExtra("catScore", catScore);
        intent.putExtra("userAnswers", userAnswers);
        // optional, harmless if unused:
        intent.putExtra(ResultActivity.EXTRA_RESULT_KEY, petType);
        startActivity(intent);
        finish();
    }
}
