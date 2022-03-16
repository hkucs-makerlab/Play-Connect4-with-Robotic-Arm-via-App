package hk.hku.cs.fyp_connectfourbot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SelectActivity extends AppCompatActivity {


    Button humanPlayer;
    Button robotPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        humanPlayer = findViewById(R.id.youButton);
        robotPlayer = findViewById(R.id.robotButton);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
    }


    public void clickedYouButton(View view){
        launchAutoActivity(1);
    }

    public void clickedRobotButton(View view){
        launchAutoActivity(0);
    }

    public void launchAutoActivity(int player) {
//        Log.d(TAG, "Launch Auto Activity");
        Intent autoModeAct = new Intent(this, AutoActivity.class);
        autoModeAct.putExtra("player", player);
        startActivity(autoModeAct);
    }
}