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

    public void launchControllerActivity(View view) {
        Log.d(TAG, "Launch Controller Activity");
        Intent controllerAct = new Intent(this, ControllerActivity.class);
        startActivity(controllerAct);
    }

    public void launchManualActivity(View view) {
        Log.d(TAG, "Launch Manual Activity");
        Intent manualModeAct = new Intent(this, ControllerActivity.class);
        startActivity(manualModeAct);
    }

    public void launchBuildingActivity(View view) {
        Log.d(TAG, "Launch Building Activity");
        Intent buildingModeAct = new Intent(this, BuildingActivity.class);
        startActivity(buildingModeAct);
    }

    public void launchDrawingActivity(View view) {
    }
}