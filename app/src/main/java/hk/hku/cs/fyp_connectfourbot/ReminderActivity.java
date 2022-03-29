package hk.hku.cs.fyp_connectfourbot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ReminderActivity extends AppCompatActivity {


    Button readyButton;
    public int player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
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
}