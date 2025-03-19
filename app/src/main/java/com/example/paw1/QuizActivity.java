package com.example.paw1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Arrays;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private RecyclerView quizRecyclerView;
    private Button submitButton;
    private QuizAdapter quizAdapter;

    // List of quiz questions
    private List<String> questions = Arrays.asList(
            "How large is your living space?",
            "How much free time do you have each day?",
            "Do you enjoy being physically active?",
            "How consistent is your daily routine?",
            "Do you work or study from home?",
            "How often do you clean your home?",
            "How social and outgoing are you?",
            "How much noise can you tolerate?",
            "Do you enjoy training or teaching new skills?",
            "How often do you travel?"
    );

    // List of possible answers for each question
    private List<String[]> answers = Arrays.asList(
            new String[]{"Small", "Medium", "Large"},
            new String[]{"Little time", "Average time", "A lot of time"},
            new String[]{"No", "Sometimes", "Yes"},
            new String[]{"Inconsistent", "Moderate", "Very consistent"},
            new String[]{"Yes, all the time", "Sometimes", "Rarely or never"},
            new String[]{"Rarely", "Sometimes", "Very frequently"},
            new String[]{"Introverted (Prefer solitude)", "Moderate", "Very social and outgoing"},
            new String[]{"Very quiet", "Some noise is fine", "Loud environments are okay"},
            new String[]{"Not at all", "A little", "I love training animals"},
            new String[]{"Rarely", "Sometimes", "Very frequently"}
    );

    private int[] userAnswers = new int[questions.size()]; // Stores user-selected answers
    private int dogScore = 0; // Dog compatibility score
    private int catScore = 0; // Cat compatibility score

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        quizRecyclerView = findViewById(R.id.quizRecyclerView);
        submitButton = findViewById(R.id.submitButton);

        // Set up RecyclerView
        quizRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        quizRecyclerView.setHasFixedSize(false);

        // Pass data to adapter
        quizAdapter = new QuizAdapter(questions, answers, userAnswers);
        quizRecyclerView.setAdapter(quizAdapter);

        // When the submit button is clicked, calculate the score and move to the results page
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateScores();
                finishQuiz();
            }
        });
    }

    // Calculate the scores based on the answers
    private void calculateScores() {
        for (int i = 0; i < userAnswers.length; i++) {
            int answer = userAnswers[i]; // Get selected answer index

            switch (i) {
                case 0: // Living Space
                    if (answer == 0) catScore += 2; // Small space → better for cats
                    if (answer == 2) dogScore += 2; // Large space → better for dogs
                    break;

                case 2: // Physical Activity
                    if (answer == 0) catScore += 2; // Not active → better for cats
                    if (answer == 2) dogScore += 2; // Very active → better for dogs
                    break;

                case 6: // Sociability
                    if (answer == 0) catScore += 2; // Introverted → better for cats
                    if (answer == 2) dogScore += 2; // Outgoing → better for dogs
                    break;

                case 9: // Travel
                    if (answer == 0) dogScore += 2; // Rarely travel → better for dogs
                    if (answer == 2) catScore += 2; // Travel often → better for cats
                    break;
            }
        }
    }

    // Finish quiz and pass results to the result activity
    private void finishQuiz() {
        Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
        intent.putExtra("dogScore", dogScore);
        intent.putExtra("catScore", catScore);
        intent.putExtra("userAnswers", userAnswers);
        startActivity(intent);
        finish();
    }
}
