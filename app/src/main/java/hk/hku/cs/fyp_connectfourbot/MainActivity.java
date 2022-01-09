package hk.hku.cs.fyp_connectfourbot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;

import org.opencv.android.JavaCameraView;

public class MainActivity extends AppCompatActivity{
    private static String TAG = "MainActivity";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void launchAutoActivity(View view) {
        Log.d(TAG, "Launch Auto Activity");
        Intent autoModeAct = new Intent(this, AutoActivity.class);
        startActivity(autoModeAct);
    }

    public void launchManualActivity(View view) {
        Log.d(TAG, "Launch Manual Activity");
        Intent manualModeAct = new Intent(this, ManualActivity.class);
        startActivity(manualModeAct);
    }
}