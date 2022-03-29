package hk.hku.cs.fyp_connectfourbot;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.makerlab.bt.BluetoothConnect;

public class SelectActivity extends AppCompatActivity {
    private static String TAG = "SelectActivity";

    private BluetoothConnect mBluetoothConnect;
    private static RobotArmGcode mRobotArmGcode = new RobotArmGcode();
    AlertDialog.Builder builder;

//    Button humanPlayer;
//    Button robotPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        humanPlayer = findViewById(R.id.youButton);
//        robotPlayer = findViewById(R.id.robotButton);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        builder = new AlertDialog.Builder(SelectActivity.this);
    }


    public void clickedYouButton(View view){
        launchReminderActivity(1);
    }

    public void clickedRobotButton(View view){
        launchReminderActivity(0);
    }

    public void launchReminderActivity(int player) {
//        Log.d(TAG, "Launch Auto Activity");
        Intent readyModeAct = new Intent(this, ReminderActivity.class);
        readyModeAct.putExtra("player", player);
        startActivity(readyModeAct);
    }

    //do autohome while player is selecting play order
    @Override
    protected void onStart() {
        super.onStart();
        MainActivity activity = MainActivity.getInstance();
        mBluetoothConnect = activity.getBluetoothConnect();
        if (mBluetoothConnect != null) {
            mBluetoothConnect.send(mRobotArmGcode.autoHome());
        }
    }

    @Override
    public void onBackPressed(){
        //Back to MainActivity instead of prevActivity

        //Prompt
//        builder.setMessage("Are you sure?").setTitle("ALERT").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface arg0, int arg1) {
//                Intent intent = new Intent(SelectActivity.this, MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                finish();
//            }
//
//        }).setNegativeButton("Cancel", doNothing());
//        AlertDialog dialog = builder.create();
//        dialog.show();

        Intent intent = new Intent(SelectActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

}