package hk.hku.cs.fyp_connectfourbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class ReminderActivity extends AppCompatActivity {


    Button readyButton;
    public int player;

    static private String LOG_TAG = ReminderActivity.class.getSimpleName();

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
                Intent backIntent = new Intent(ReminderActivity.this, MainActivity.class);
                backIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(backIntent);
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        readyButton = findViewById(R.id.readyButton);
        player = getIntent().getIntExtra("player", 0);

    }

    public void launchAutoActivity(View view) {
//        Log.d(TAG, "Launch Auto Activity");
        Intent autoModeAct = new Intent(this, AutoActivity.class);
        autoModeAct.putExtra("player", player);
        startActivity(autoModeAct);
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

        Intent intent = new Intent(ReminderActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
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