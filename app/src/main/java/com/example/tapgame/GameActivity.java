package com.example.tapgame;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
public class GameActivity extends AppCompatActivity {

    private int count = 0;
    private TextView timerTextView;
    private TextView counterView;
    private Button buttonView;
    private String name;
    private long timeLeftMillis = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent i = getIntent();
        name = i.getStringExtra("name");
        timerTextView = findViewById(R.id.timer
        );

        buttonView = findViewById(R.id.tapButton);
        counterView = findViewById(R.id.ScoreView);


        CountDownTimer countDownTimer = new CountDownTimer(timeLeftMillis, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftMillis = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {

                timerTextView.setText("Timer expired!");
                buttonView.setEnabled(false);


                updateHighScores(name,count);
                Intent intent = new Intent(GameActivity.this, MainActivity.class);

                // Start the target activity
                startActivity(intent);
            }
        };

        countDownTimer.start();
    }


    private void updateTimer() {
        int seconds = (int) (timeLeftMillis / 999);
        timerTextView.setText("Time remaining: " + seconds + " seconds");
    }

    public void increment(View v) {
        count = count + 1;
        counterView.setText("Score: " + String.valueOf(count));

    }
    private void updateHighScores(String playerName, int score) {
        SharedPreferences sharedPreferences = getSharedPreferences("HighScores", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Retrieve the existing high scores JSON array
        String highScoresJson = sharedPreferences.getString("highScores", "[]");
        JSONArray jsonArray = new JSONArray();

        try {
            jsonArray = new JSONArray(highScoresJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Create a JSON object for the new high score
        JSONObject highScoreObject = new JSONObject();
        try {
            highScoreObject.put("playerName", playerName);
            highScoreObject.put("score", score);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Add the new high score to the JSON array
        jsonArray.put(highScoreObject);

        // Sort the high scores array by score in descending order
        JSONArray sortedArray = new JSONArray();
        List<JSONObject> jsonValues = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                jsonValues.add(jsonArray.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(jsonValues, new Comparator<JSONObject>() {
            private static final String KEY_NAME = "score";

            @Override
            public int compare(JSONObject a, JSONObject b) {
                int valA = 0;
                int valB = 0;
                try {
                    valA = (int) a.get(KEY_NAME);
                    valB = (int) b.get(KEY_NAME);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return Integer.compare(valB, valA);
            }
        });

        for (int i = 0; i < jsonValues.size(); i++) {
            sortedArray.put(jsonValues.get(i));
        }

        // Keep only the top 5 high scores
        while (sortedArray.length() > 5) {
            sortedArray.remove(sortedArray.length() - 1);
        }

        Log.i("stringss", String.valueOf((sortedArray)));

        // Save the updated high scores JSON array
        editor.putString("highScores", sortedArray.toString());
        editor.apply();
    }

    // ...

}