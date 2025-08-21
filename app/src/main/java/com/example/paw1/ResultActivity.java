package com.example.paw1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.Map;

public class ResultActivity extends AppCompatActivity {

    // Keep this so QuizActivity can compile (even if we don't use it here)
    public static final String EXTRA_RESULT_KEY = "RESULT_KEY";

    private TextView resultText;
    private ImageView resultImage;

    private static final Map<String, String> IMAGE = new HashMap<String, String>() {{
        put("Golden Retriever",   "https://drive.google.com/uc?id=1Z_CfJAFQkGaeLmDvGi-lA40AEwMoxbZY");
        put("German Shepherd",    "https://drive.google.com/uc?id=1rSkuKhC3RX4Vd69pd_koy5VoUOEVFfK0");
        put("Poodle",             "https://drive.google.com/uc?id=1QVAAqsLrJrc5fP8ixrVLMkksj5AbwHx8");
        put("Beagle",             "https://drive.google.com/uc?id=11lhlhsOxuOFNdk29ieDe8_tbYB45tIK-");
        put("Bulldog",            "https://drive.google.com/uc?id=1cyXRRRdz0TPf2abRo0ZulrPQypwcyYzR");
        put("Chihuahua",          "https://drive.google.com/uc?id=12XbUOU61aifNFPK7w7pRKueuZqhtplUO");
        put("Maine Coon",         "https://drive.google.com/uc?id=12y0OiMamDSUIcX-P84VhS6FYRw2nviWQ");
        put("Siamese",            "https://drive.google.com/uc?id=1CJeHeiZgBght7Ev1t8t-6h5teyOvL_vX");
        put("British Shorthair",  "https://drive.google.com/uc?id=1KwnXSJzApaH4BDa-X3YNUX7JJ8uLpeHc");
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        resultText  = findViewById(R.id.resultText);
        resultImage = findViewById(R.id.resultImageView);
        Button saveReturnButton = findViewById(R.id.saveReturnButton);

        int dogScore  = getIntent().getIntExtra("dogScore", 0);
        int catScore  = getIntent().getIntExtra("catScore", 0);
        int[] answers = getIntent().getIntArrayExtra("userAnswers");

        String petType = (dogScore > catScore) ? "dog" : "cat";

        int countHi = 0, countLo = 0;
        if (answers != null) {
            for (int a : answers) {
                if (a == 2) countHi++;
                if (a == 0) countLo++;
            }
        }
        ActivityLevel activity = ActivityLevel.MEDIUM;
        if (countHi - countLo >= 2) activity = ActivityLevel.HIGH;
        else if (countLo - countHi >= 2) activity = ActivityLevel.LOW;

        int dogMargin = dogScore - catScore;

        String breed = pickBreed(petType, activity, dogMargin);

        resultText.setText("🎯 Your best match: " + breed);
        String url = IMAGE.get(breed);
        if (url != null && !url.isEmpty()) {
            resultImage.setVisibility(ImageView.VISIBLE);
            Glide.with(this).load(url).centerCrop().into(resultImage);
        } else {
            resultImage.setImageDrawable(null);
        }

        saveReturnButton.setOnClickListener(v -> {
            startActivity(new Intent(ResultActivity.this, SheltersActivity.class));
            finish();
        });
    }

    private enum ActivityLevel { LOW, MEDIUM, HIGH }

    private String pickBreed(String petType, ActivityLevel activity, int dogMargin) {
        if ("dog".equals(petType)) {
            if (activity == ActivityLevel.HIGH)   return "German Shepherd";
            if (activity == ActivityLevel.LOW)    return "Bulldog";
            if (dogMargin >= 4)  return "Golden Retriever";
            if (dogMargin <= 1)  return "Beagle";
            return "Golden Retriever";
        } else {
            if (activity == ActivityLevel.HIGH)   return "Siamese";
            if (activity == ActivityLevel.LOW)    return "British Shorthair";
            return "Maine Coon";
        }
    }
}
