package hk.hku.cs.fyp_connectfourbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.InstallCallbackInterface;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.InstallCallbackInterface;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;


public class BoardActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2{
    static private String LOG_TAG = BoardActivity.class.getSimpleName();

    // Receiver
    private static final String FINISH_ACTIVITY_BROADCAST = BuildConfig.APPLICATION_ID + ".FINISH_ACTIVITY_BROADCAST";
    private LocalBroadcastManager localBroadcastManager;
    JavaCameraView javaCameraView;
    TextView textView;
    Button changeText;
    Mat mRGBA, mRGBAT;
    public ArrayList<ArrayList<ArrayList<Integer>>> trueBoard = new ArrayList<>();
    public int count = 10;
    public String realStr = "";

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

    BaseLoaderCallback baseLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case BaseLoaderCallback.SUCCESS:{
                    javaCameraView.enableView();
                    break;
                }
                default: {
                    super.onManagerConnected(status);
                }
            }

        }

        @Override
        public void onPackageInstall(int operation, InstallCallbackInterface callback) {
            super.onPackageInstall(operation, callback);
        }
    };

    private static final String TAG = "BoardActivity";

    static {
        if (OpenCVLoader.initDebug()){
            Log.d(TAG, "OpenCV Config successful 1");
        }
        else {
            Log.d(TAG, "OpenCV Config failed");
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        textView = findViewById(R.id.textView2);
        textView.setText("Change");
        changeText = findViewById(R.id.update);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        javaCameraView = (JavaCameraView) findViewById(R.id.cameraview1);
        javaCameraView.setVisibility(SurfaceView.VISIBLE);
        javaCameraView.setCvCameraViewListener(BoardActivity.this);
        Coordinates coor = (Coordinates) getIntent().getSerializableExtra("boardCoor");
        if(coor != null){
            trueBoard = coor.coor;
            Log.d(TAG, "Data retrieved");
        }
    }


    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

        Mat input = inputFrame.rgba();
//        Log.d(TAG, "You're in board");
        Imgproc.cvtColor(input, input, Imgproc.COLOR_BGR2HSV);
        if (count != 0){
//            Log.i(TAG, String.valueOf("Count:"+count));
            count--;

        }
        else{
            count = 10;
            Log.i(TAG, String.valueOf("--------------------------------S-------------------------------------"));

            String boardStr = "Board State: \n";
            for (int x=0; x < trueBoard.size() ; x++ ) {
                String rowStr = "";
                for (int y = 0; y < trueBoard.get(x).size(); y++){
                    Point p = new Point((int) trueBoard.get(x).get(y).get(0), (int) trueBoard.get(x).get(y).get(1));
//                    Imgproc.circle(input, p, 0, new Scalar(255, 0, 0), 5);
                    double[] pixel = input.get(trueBoard.get(x).get(y).get(1), trueBoard.get(x).get(y).get(0));
                    rowStr = getColor(pixel) + " " +rowStr;
//                    rowStr = getColor(pixel) +"("+pixel[0]+ ","+ pixel[1]+ ","+ pixel[2]+")"+ " " +rowStr;
                    Log.i(TAG, String.valueOf("RGB: "+ getColor(pixel)));
//                    Log.i(TAG, String.valueOf("Pixel: "+ pixel[0]+ ","+ pixel[1]+ ","+ pixel[2]));
//                    if (getColor(pixel) == "G"){
//                        Imgproc.circle(input, p, 0, new Scalar(0, 128, 0), 5);
//                    }
//                    else if (getColor(pixel) == "R"){
//                        Imgproc.circle(input, p, 0, new Scalar(128, 0, 0), 5);
//                    }
//                    else if (getColor(pixel) == "X") {
//                        Imgproc.circle(input, p, 0, new Scalar(225, 225, 225), 5);
//                    }

                }
                boardStr += rowStr + "\n";
//                Log.i(TAG, String.valueOf("--------------------------------R"+x+"-------------------------------------"));
            }
            realStr = boardStr;
            Log.i(TAG, boardStr);
//            textView.setText(boardStr);
            Log.i(TAG, String.valueOf("--------------------------------End-------------------------------------"));
        }
        return inputFrame.rgba();
    }

    public String getColor(double[] rgbValue){
        int Hue = (int) rgbValue[0];
        if ( Hue > 100 ){
            return "P";
        }
        else if (Hue < 100 && Hue >30){
            return "G";
        }
        else{
            return "X";
        }

    }

    public void changeText(View view){
        textView.setText(realStr);
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        //Get the width and height to be displayed on the screen
        Log.d(TAG, "Width: "+Integer.toString(width));
        Log.d(TAG, "Height: "+Integer.toString(height));
        mRGBA = new Mat(height, width, CvType.CV_8UC4);
    }

    @Override
    public void onCameraViewStopped() {
        //Actions when the camera stop
        mRGBA.release();
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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (javaCameraView != null){
            javaCameraView.disableView();
        }
    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    protected void onResume() {

        super.onResume();

        if (OpenCVLoader.initDebug()){
            Log.d(TAG, "OpenCV Config successful");
            baseLoaderCallback.onManagerConnected(BaseLoaderCallback.SUCCESS);
        }
        else {
            Log.d(TAG, "OpenCV Config failed");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, baseLoaderCallback);
        }
    }
}