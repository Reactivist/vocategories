package com.example.zachary.vocategories;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.CountDownTimer;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Spinner catChoice;
    private TextView voiceInput;
    private TextView speakButton;
    private TextView scoreDisplay;
    private TextView countdownDisplay;
    private Button resetButton;
    private Button startButton;
    private CountDownTimer countDownTimer;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private int score = 0;
    private String category = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        catChoice = (Spinner) findViewById(R.id.categoryChoice);
        String[] categories = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        catChoice.setAdapter(adapter);

        voiceInput = (TextView) findViewById(R.id.voiceInput);
        voiceInput.setMovementMethod(new ScrollingMovementMethod());
        speakButton = (TextView) findViewById(R.id.btnSpeak);
        scoreDisplay = (TextView) findViewById(R.id.scoreDisplay);
        countdownDisplay = (TextView) findViewById(R.id.countdownDisplay);
        resetButton = (Button) findViewById(R.id.resetButton);
        startButton = (Button) findViewById(R.id.startButton);

        speakButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                askSpeechInput();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                resetGame();
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startGame();
            }
        });

    }

    private void askSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Speak a Word, Please!");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
    }

    // Receiving speech input
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String firstLetter = result.get(0);
                    firstLetter = firstLetter.substring(0, 1);

                    if(firstLetter.equalsIgnoreCase(category)) {
                        voiceInput.append("\n" + result.get(0));
                        int scoreAmount = result.get(0).length();
                        score += scoreAmount;
                        scoreDisplay.setText(Integer.toString(score));
                    } else {
                        Toast.makeText(getApplicationContext(), "INCORRECT, TRY AGAIN", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            }

        }
    }

    private void startGame() {
        category = catChoice.getSelectedItem().toString();

        countDownTimer = new CountDownTimer(90000, 1000) {

            public void onTick(long millisUntilFinished) {
                countdownDisplay.setText("Seconds Remaining: " + millisUntilFinished / 1000 + "s");
            }

            public void onFinish() {
                speakButton.setClickable(false);
                countdownDisplay.setText("Time's Up!");
            }
        }.start();
    }

    private void resetGame() {

        score = 0;
        scoreDisplay.setText(Integer.toString(score));
        voiceInput.setText("");
        countDownTimer.cancel();
        countdownDisplay.setText("Seconds Remaining: 60s");
        speakButton.setClickable(true);

        countDownTimer = new CountDownTimer(90000, 1000) {

            public void onTick(long millisUntilFinished) {
                countdownDisplay.setText("Seconds Remaining: " + millisUntilFinished / 1000 + "s");
            }

            public void onFinish() {

                countdownDisplay.setText("Time's Up!");
            }
        };
    }
}
