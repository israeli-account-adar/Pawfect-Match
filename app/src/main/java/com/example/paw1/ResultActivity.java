package com.example.paw1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class ResultActivity extends AppCompatActivity {

    private TextView resultText;

    // Arrays of available pet breeds
    private String[] catBreeds = {"Maine Coon", "Siamese", "British Shorthair"};
    private String[] dogBreeds = {"Labrador", "Golden Retriever", "German Shepherd", "Bulldog", "Beagle", "Poodle", "Chihuahua"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        resultText = findViewById(R.id.resultText);

        // Retrieve user answers from the quiz
        int[] userAnswers = getIntent().getIntArrayExtra("userAnswers");
        if (userAnswers == null) {
            resultText.setText("Error: No answers received. UserAnswers is NULL.");
            return;
        }


        if (userAnswers != null) {
            int dogScore = getIntent().getIntExtra("dogScore", 0);
            int catScore = getIntent().getIntExtra("catScore", 0);

            resultText.setText("Debug: Dog Score = " + dogScore + ", Cat Score = " + catScore);


// Determine the best pet based on scores
            String petRecommendation = determinePet(dogScore, catScore);
            resultText.setText("🎉 Congratulations! Your best match is a " + petRecommendation + "!");

        } else {
            resultText.setText("Error: No answers received.");
        }
    }

    /**
     * Calculates the total score for dog compatibility based on user's quiz answers.
     */
    private int calculateDogScore(int[] userAnswers) {
        int score = 0;
        // Sample scoring system (adjust weights as needed)
        score += userAnswers[2] * 2;  // Activity level matters more for dogs
        score += userAnswers[4];  // Work from home = better for dogs
        score += userAnswers[6] * 2;  // Social people fit better with dogs
        score += userAnswers[9];  // Frequent travel = lower compatibility with dogs

        return score;
    }

    /**
     * Calculates the total score for cat compatibility based on user's quiz answers.
     */
    private int calculateCatScore(int[] userAnswers) {
        int score = 0;
        // Sample scoring system (adjust weights as needed)
        score += userAnswers[0];  // Living space (cats adapt well to small spaces)
        score += userAnswers[3];  // Routine consistency matters for cats
        score += userAnswers[5] * 2;  // Cleanliness level matters more for cats
        score += userAnswers[8];  // Training interest matters less for cats

        return score;
    }

    /**
     * Determines which pet type is best based on scores and selects a breed.
     */
    private String determinePet(int dogScore, int catScore) {
        Random random = new Random();

        if (dogScore > catScore) {
            return dogBreeds[random.nextInt(dogBreeds.length)]; // Pick a random dog breed
        } else {
            return catBreeds[random.nextInt(catBreeds.length)]; // Pick a random cat breed
        }
    }
}
