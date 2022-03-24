package hk.hku.cs.fyp_connectfourbot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class GameEndActivity extends AppCompatActivity {

    int score;
    int finishStatus;
    TextView scoreView;
    TextView gameEndView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_end);
        score = getIntent().getIntExtra("score", 0);
        finishStatus = getIntent().getIntExtra("finishStatus", 0);
        scoreView = findViewById(R.id.score);
        scoreView.setText(String.valueOf(score));
        if (finishStatus == 0){
            gameEndView.setText("You Lose");
        }
        else if (finishStatus == 1){
            gameEndView.setText("You Win!!");
        }
        else if (finishStatus == 2){
            gameEndView.setText("Draw!!");
        }
    }
}