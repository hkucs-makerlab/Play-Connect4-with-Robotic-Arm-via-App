package hk.hku.cs.fyp_connectfourbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class BuildingActivity extends AppCompatActivity {
    static private String LOG_TAG = BuildingActivity.class.getSimpleName();

    // Receiver
    private static final String FINISH_ACTIVITY_BROADCAST = BuildConfig.APPLICATION_ID + ".FINISH_ACTIVITY_BROADCAST";
    private LocalBroadcastManager localBroadcastManager;

    //local
    private Button mStartTimer, mRefresh;
    private TextView mCountView;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;

    private static final long START_TIME_IN_MILLIS = 600000; //5minute
//    private static final long START_TIME_IN_MILLIS = 10000; // for testing
    private long mTimeLeft = START_TIME_IN_MILLIS;

    private final int[][] smallDiscID = {
            {R.id.smallDisc00, R.id.smallDisc01, R.id.smallDisc02, R.id.smallDisc03, R.id.smallDisc04, R.id.smallDisc05, R.id.smallDisc06},
            {R.id.smallDisc10, R.id.smallDisc11, R.id.smallDisc12, R.id.smallDisc13, R.id.smallDisc14, R.id.smallDisc15, R.id.smallDisc16},
            {R.id.smallDisc20, R.id.smallDisc21, R.id.smallDisc22, R.id.smallDisc23, R.id.smallDisc24, R.id.smallDisc25, R.id.smallDisc26},
            {R.id.smallDisc30, R.id.smallDisc31, R.id.smallDisc32, R.id.smallDisc33, R.id.smallDisc34, R.id.smallDisc35, R.id.smallDisc36},
            {R.id.smallDisc40, R.id.smallDisc41, R.id.smallDisc42, R.id.smallDisc43, R.id.smallDisc44, R.id.smallDisc45, R.id.smallDisc46},
            {R.id.smallDisc50, R.id.smallDisc51, R.id.smallDisc52, R.id.smallDisc53, R.id.smallDisc54, R.id.smallDisc55, R.id.smallDisc56}
    };
    private ImageView[][] smallDiscImgView = new ImageView[6][7];

    private final int[][] pattern1 = {
            {0,0,2,2,2,0,0},
            {0,0,2,2,2,0,0},
            {0,0,2,2,2,0,0},
            {0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0},
    };
    private final int[][] pattern2 = {
            {0,2,2,2,2,2,0},
            {0,0,2,2,2,0,0},
            {0,0,0,2,0,0,0},
            {0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0},
    };
    private final int[][] pattern3 = {
            {0,2,2,2,2,2,0},
            {0,2,0,0,0,2,0},
            {0,2,0,0,0,2,0},
            {0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0},
    };
    private final int[][] pattern4 = {
            {0,2,0,2,0,2,0},
            {0,2,0,2,0,2,0},
            {0,2,0,2,0,2,0},
            {0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0},
    };
    private final int[][] pattern5 = {
            {2,0,0,2,0,0,2},
            {2,0,0,2,0,0,2},
            {2,0,0,2,0,0,2},
            {0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0},
    };
    private final int[][] pattern6 = {
            {0,2,0,2,0,2,0},
            {0,2,0,2,0,2,0},
            {0,0,0,2,0,0,0},
            {0,0,0,2,0,0,0},
            {0,0,0,2,0,0,0},
            {0,0,0,2,0,0,0},
    };
    private final int[][] pattern7 = {
            {2,2,2,2,2,2,2},
            {2,0,0,2,0,0,2},
            {0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0},
    };
    private final int[][] pattern8 = {
            {2,2,2,0,2,2,2},
            {0,2,0,0,0,2,0},
            {0,2,0,0,0,2,0},
            {0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0},
    };
    private final int[][] pattern9 = {
            {0,2,2,2,2,2,0},
            {0,2,2,2,2,2,0},
            {0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0},
    };
    private final int[][] pattern10 = {
            {0,2,0,2,0,2,0},
            {0,2,0,2,0,2,0},
            {0,2,0,2,0,0,0},
            {0,2,0,0,0,0,0},
            {0,2,0,0,0,0,0},
            {0,0,0,0,0,0,0},
    };
    private final int[][] pattern11 = {
            {0,2,0,2,0,2,0},
            {0,2,0,2,0,2,0},
            {0,0,0,2,0,2,0},
            {0,0,0,0,0,2,0},
            {0,0,0,0,0,2,0},
            {0,0,0,0,0,0,0},
    };
    private final int[][] pattern12 = {
            {2,0,2,0,2,0,2},
            {2,0,2,0,2,0,0},
            {2,0,2,0,0,0,0},
            {2,0,0,0,0,0,0},
            {0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0},
    };
    private final int[][] pattern13 = {
            {2,0,2,0,2,0,2},
            {0,0,2,0,2,0,2},
            {0,0,0,0,2,0,2},
            {0,0,0,0,0,0,2},
            {0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0},
    };
    private final int[][] pattern14 = {
            {2,2,2,0,0,2,2},
            {2,2,0,0,0,0,2},
            {2,0,0,0,0,0,0},
            {0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0},
    };
    private final int[][] pattern15 = {
            {2,2,0,0,2,2,2},
            {2,0,0,0,0,2,2},
            {0,0,0,0,0,0,2},
            {0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0},
    };

    private ArrayList<int[][]> patterns = new ArrayList<int[][]>();




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
        setContentView(R.layout.activity_building);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);

        mCountView = findViewById(R.id.countView);
        mStartTimer = findViewById(R.id.startTimer);
        mRefresh = findViewById(R.id.refresh);
        //init smallDiscImgView
        for ( int r = 0; r < 6; r++) {
            for (int c = 0; c < 7; c++) {
                smallDiscImgView[r][c] = findViewById(smallDiscID[r][c]);
            }
        }
        //init patterns arraylist
        patterns.add(pattern1);
        patterns.add(pattern2);
        patterns.add(pattern3);
        patterns.add(pattern4);
        patterns.add(pattern5);
        patterns.add(pattern6);
        patterns.add(pattern7);
        patterns.add(pattern8);
        patterns.add(pattern9);
        patterns.add(pattern10);
        patterns.add(pattern11);
        patterns.add(pattern12);
        patterns.add(pattern13);
        patterns.add(pattern14);
        patterns.add(pattern15);
        Collections.shuffle(patterns);
        int[][] chosen = patterns.get(0);

        //init a pattern
        for ( int r = 0; r < 6; r++) {
            for (int c = 0; c < 7; c++) {
                if (chosen[r][c]==0){
                    smallDiscImgView[r][c].setImageDrawable(getResources().getDrawable(R.drawable.blank_disc));
                } else {
                    smallDiscImgView[r][c].setImageDrawable(getResources().getDrawable(R.drawable.pink_disc));
                }
            }
        }
        mRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.shuffle(patterns);
                int[][] temp = patterns.get(0);
                for ( int r = 0; r < 6; r++) {
                    for (int c = 0; c < 7; c++) {
                        if (temp[r][c]==0){
                            smallDiscImgView[r][c].setImageDrawable(getResources().getDrawable(R.drawable.blank_disc));
                        } else {
                            smallDiscImgView[r][c].setImageDrawable(getResources().getDrawable(R.drawable.pink_disc));
                        }
                    }
                }
            }
        });

        mStartTimer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (mTimerRunning){
                    finishTimer();
                } else {
                    startTimer();
                }
            }
        });
        updateCountDownText();

    }
    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeft,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeft = millisUntilFinished;
                updateCountDownText();
            }
            @Override
            public void onFinish() {
                Toast.makeText(getApplicationContext(), "Time's up!!", Toast.LENGTH_LONG).show();
                mStartTimer.setText("Start");
                mStartTimer.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_baseline_timer_24), null, null, null);
                mRefresh.setEnabled(true);
                mTimerRunning = false;
                mTimeLeft = START_TIME_IN_MILLIS;
                updateCountDownText();

            }
        }.start();
        mTimerRunning = true;
        mStartTimer.setText("FINISH");
        mStartTimer.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_baseline_timer_off_24), null, null, null);
        mRefresh.setEnabled(false);
    }
    private void finishTimer() {
        long timeUsed = START_TIME_IN_MILLIS - mTimeLeft;
        int minutes = (int) (timeUsed / 1000) / 60;
        int seconds = (int) (timeUsed / 1000) % 60;
        String temp = "You used "+minutes+" minutes and "+seconds+" seconds to finish the pattern";
        Toast.makeText(this, temp, Toast.LENGTH_LONG).show();
        mCountDownTimer.cancel();
        mStartTimer.setText("Start");
        mStartTimer.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_baseline_timer_24), null, null, null);
        mRefresh.setEnabled(true);
        mTimerRunning = false;
        mTimeLeft = START_TIME_IN_MILLIS;
        updateCountDownText();
    }


    private void updateCountDownText(){
        int minutes = (int) (mTimeLeft / 1000) / 60;
        int seconds = (int) (mTimeLeft / 1000) % 60;
        String temp = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        mCountView.setText(temp);
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