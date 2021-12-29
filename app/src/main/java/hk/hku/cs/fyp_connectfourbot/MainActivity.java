package hk.hku.cs.fyp_connectfourbot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import org.opencv.android.OpenCVLoader;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "MainActivity";

    static {
        if (OpenCVLoader.initDebug()){
            Log.d(TAG, "Config successful");
        }
        else {
            Log.d(TAG, "GG");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}