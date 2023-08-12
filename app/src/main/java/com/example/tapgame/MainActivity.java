package com.example.tapgame;

import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;
    public class MainActivity extends AppCompatActivity {
        private TextView highScoreView;
        private String name = "John Doe";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        highScoreView = findViewById(R.id.highScoreDisplay);
        try {
            displayHighScores();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    public void start(View v) {


        saveName(findViewById(R.id.nameInput));
        Intent intent = new Intent(this, GameActivity.class);
       intent.putExtra("name",name) ;
        // Start the target activity
        startActivity(intent);
    }

    private void saveName(TextView v){
      name=  v.getText().toString();

    }

        private void displayHighScores() throws JSONException {
            SharedPreferences sharedPreferences = getSharedPreferences("HighScores", MODE_PRIVATE);
            String highScoresJson = sharedPreferences.getString("highScores", "[]");

            JSONArray jsonArray = new JSONArray((highScoresJson));
            StringBuilder highScoresText = new StringBuilder();
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject highScore = jsonArray.getJSONObject(i);
                    String playerName = highScore.getString("playerName");
                    int score = highScore.getInt("score");


                    highScoresText.append(playerName)
                            .append(": ").append(score)
                            .append("\n");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
Log.d("nopity",highScoresText.toString());
            // Display the high scores in the TextView

            highScoreView.setText(highScoresText.toString());

            // Now you can iterate through the jsonArray and display the high scores in your UI
            }
        }
//
