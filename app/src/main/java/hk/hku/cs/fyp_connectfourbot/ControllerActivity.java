package hk.hku.cs.fyp_connectfourbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Queue;

public class ControllerActivity extends AppCompatActivity implements
        View.OnClickListener{

    static private String LOG_TAG = ControllerActivity.class.getSimpleName();

    SwitchCompat stepperSwitch;
    Button homeButton, bottomButton, restButton, endStopButton;

    Queue<byte[]> mQueue = ControllerFragment.getmQueue();
    RobotArmGcode mRobotArmGcode = ControllerFragment.getmRobotArmGcode();



    // Receiver
    private static final String FINISH_ACTIVITY_BROADCAST = BuildConfig.APPLICATION_ID + ".FINISH_ACTIVITY_BROADCAST";
    private LocalBroadcastManager localBroadcastManager;


    BroadcastReceiver FinishReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            String action = intent.getAction();
            Log.e(LOG_TAG, "onReceiveFinishReceiver()");
            Log.e(LOG_TAG, action);
            if (action.equals(FINISH_ACTIVITY_BROADCAST)) {
                Log.e(LOG_TAG, "it is FINISH_ACTIVITY_BROADCAST");
//                Toast.makeText(getApplicationContext(), "finish me", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);

        stepperSwitch = (SwitchCompat) findViewById(R.id.stepperSwitch);
        stepperSwitch.setOnCheckedChangeListener(stepperListener);
        stepperSwitch.setChecked(true);

        homeButton = (Button) findViewById(R.id.homeButton);
        homeButton.setOnClickListener(this);
        bottomButton = (Button) findViewById(R.id.bottomButton);
        bottomButton.setOnClickListener(this);
        restButton = (Button) findViewById(R.id.restButton);
        restButton.setOnClickListener(this);
        endStopButton = (Button) findViewById(R.id.endStopButton);
        endStopButton.setOnClickListener(this);
    }

    private CompoundButton.OnCheckedChangeListener stepperListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                mQueue.add(mRobotArmGcode.setStepperOn());
//                Toast.makeText(getApplicationContext(), "Stepper On", Toast.LENGTH_SHORT).show();
            } else{
//                Toast.makeText(getApplicationContext(), "Stepper Off", Toast.LENGTH_SHORT).show();
                mQueue.add(mRobotArmGcode.setStepperOff());
            }
        }
    };

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.homeButton) {
//            Toast.makeText(getApplicationContext(), "homeButton", Toast.LENGTH_SHORT).show();
            mQueue.add(mRobotArmGcode.goHome());
        }
        else if (view.getId() == R.id.bottomButton) {
//            Toast.makeText(getApplicationContext(), "bottomButton", Toast.LENGTH_SHORT).show();
            mQueue.add(mRobotArmGcode.goBottom());
        }
        else if (view.getId() == R.id.restButton) {
//            Toast.makeText(getApplicationContext(), "restButton", Toast.LENGTH_SHORT).show();
            mQueue.add(mRobotArmGcode.goRest());
        }
        else if (view.getId() == R.id.endStopButton) {
//            Toast.makeText(getApplicationContext(), "endStopButton", Toast.LENGTH_SHORT).show();
            mQueue.add(mRobotArmGcode.goEndStop());
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        localBroadcastManager.registerReceiver(FinishReceiver, new IntentFilter(FINISH_ACTIVITY_BROADCAST));
    }

    @Override
    protected void onStop() {
        super.onStop();
        localBroadcastManager.unregisterReceiver(FinishReceiver);
    }
}