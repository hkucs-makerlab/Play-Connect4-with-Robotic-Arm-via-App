package hk.hku.cs.fyp_connectfourbot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GameEndActivity extends AppCompatActivity {

    int score =0;
    int finishStatus;
    TextView scoreView;
    TextView gameEndView;
    EditText playerNameView;
    ImageView mImageView;
    Button submitButton;
    static private String TAG = GameEndActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_end);
        score = getIntent().getIntExtra("score", 20);
        finishStatus = getIntent().getIntExtra("finishStatus", 0);
        scoreView = findViewById(R.id.gameEndScore);
        gameEndView = findViewById(R.id.gameEndView);
        mImageView = findViewById(R.id.gameEndImageView);
        playerNameView = findViewById(R.id.playerNameView);
        submitButton = findViewById(R.id.submitButton);

        scoreView.setText(String.valueOf(score));
        if (finishStatus == 0){
            mImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_icons8_sad_96));
            gameEndView.setText("You Lose!!");
        }
        else if (finishStatus == 1){
            mImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_icons8_happy_96));
            gameEndView.setText("You Win!!");
        }
        else if (finishStatus == 2){
            mImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_icons8_neutral_96));
            gameEndView.setText("Draw!!");
        }
        DBPlayerData dbPlayerData = new DBPlayerData();
        submitButton.setOnClickListener(v->{
            Log.i(TAG, "Clicked");

            PlayerData pd = new PlayerData(playerNameView.getText().toString(), score);
            if(pd != null){
                Log.i(TAG, String.valueOf(pd.getName()+pd.getScore()));
            }
            dbPlayerData.add(pd).addOnSuccessListener(suc->{
                Toast.makeText(this, "Record is inserted", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(GameEndActivity.this, ScoreBoardActivity.class);
                startActivity(intent);
            }).addOnFailureListener(er->{
                Toast.makeText(this, ""+er.getMessage(), Toast.LENGTH_SHORT).show();
            });
        });
    }

    @Override
    public void onBackPressed(){
        //Back to MainActivity instead of prevActivity
        Intent intent = new Intent(GameEndActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

}