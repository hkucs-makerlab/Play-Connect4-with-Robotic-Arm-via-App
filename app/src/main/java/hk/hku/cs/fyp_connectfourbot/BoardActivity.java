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

import com.example.cppexample.CppActivity;

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
import java.util.Timer;
import java.util.TimerTask;


public class BoardActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2{
    static private String LOG_TAG = BoardActivity.class.getSimpleName();

    // Receiver
    private static final String FINISH_ACTIVITY_BROADCAST = BuildConfig.APPLICATION_ID + ".FINISH_ACTIVITY_BROADCAST";
    private LocalBroadcastManager localBroadcastManager;
    JavaCameraView javaCameraView;
    TextView textView;
    Button changeText;
    Mat mRGBA, mRGBAT;
    public int count = 50;
    public String realStr = "";

    public ArrayList<ArrayList<ArrayList<Integer>>> trueBoard = new ArrayList<>();
    public ArrayList<ArrayList<Integer>> preState = new ArrayList<>();
    public ArrayList<ArrayList<Integer>> currentState = new ArrayList<>();
    public ArrayList<Integer> validPos = new ArrayList<>();

    public boolean initialReading = false;

    private CppActivity mCppActivity = new CppActivity();
    private TextView mTextView;
    public String sequence = "";
    public String firstPlayer = "";


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
        mCppActivity.setNativeAssetManager(getAssets());
        mTextView = findViewById(R.id.score);
        textView = findViewById(R.id.textView2);
        changeText = findViewById(R.id.update);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        javaCameraView = (JavaCameraView) findViewById(R.id.cameraview1);
        javaCameraView.setVisibility(SurfaceView.VISIBLE);
        javaCameraView.setCvCameraViewListener(BoardActivity.this);
        preState = initBoardState();
        currentState = initBoardState();
        Coordinates coor = (Coordinates) getIntent().getSerializableExtra("boardCoor");
        if(coor != null){
            trueBoard = coor.coor;
            Log.d(TAG, "Data retrieved");
        }
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(5000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateText();
                                getBestMove();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();
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
            count = 50;
//            Log.i(TAG, String.valueOf("--------------------------------S-------------------------------------"));

            String boardStr = "Board State: \n";
            for (int x=0; x < trueBoard.size() ; x++ ) {
                String rowStr = "";
                for (int y = 0; y < trueBoard.get(x).size(); y++){
                    Point p = new Point((int) trueBoard.get(x).get(y).get(0), (int) trueBoard.get(x).get(y).get(1));
//                    Imgproc.circle(input, p, 0, new Scalar(255, 0, 0), 5);
                    double[] pixel = input.get(trueBoard.get(x).get(y).get(1), trueBoard.get(x).get(y).get(0));
                    rowStr = getColor(pixel) + " " +rowStr;
                    if(!initialReading){
                        constructBoardState(getColor(pixel), x, y, 0);
                    }
                    constructBoardState(getColor(pixel), x, y, 1);

//                    rowStr = getColor(pixel) +"("+pixel[0]+ ","+ pixel[1]+ ","+ pixel[2]+")"+ " " +rowStr;
//                    Log.i(TAG, String.valueOf("RGB: "+ getColor(pixel)));
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
            if(!initialReading){
                getInitValidPos();
            }
            realStr = boardStr;

//            Log.i(TAG, boardStr);
//            textView.setText(boardStr);
//            Log.i(TAG, String.valueOf("--------------------------------End-------------------------------------"));
            initialReading = true;
            getSequence();

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

    public void displayBoard(){
        //display the board state in the virtual board
    }

    public void displayHints(){

    }

    public void changeText(View view){
//        Log.d(TAG, "Sequence: "+sequence);
        if (sequence != ""){
            mTextView.setText(mCppActivity.mystringFromJNI(sequence));
        }
    }

    public void getBestMove(){
        Log.d(TAG, "Sequence: "+sequence);
        if (sequence != ""){
            mTextView.setText(mCppActivity.mystringFromJNI(sequence));
        }
    }

    public void updateText(){
        textView.setText(realStr);
    }

    public void getSequence(){
        //compare the current board state and previous board state to output the sequence
        ArrayList<Integer> tempCol = new ArrayList<>();
        for ( int col = 0; col < 7; col++){
            for (int row = 0; row < 6; row++){
                if (currentState.get(col).get(row) - preState.get(col).get(row) > 0 && validate(col, row)){
                    tempCol = preState.get(col);
                    tempCol.set(row, currentState.get(col).get(row));
                    preState.set(col, tempCol);
                    sequence += String.valueOf(6-col+1);
                }
            }
        }
    }

    public void getInitValidPos(){
        //check the board to know the valid positions
        for ( int col = 0; col < 7; col++){
            boolean posConfirm = false;
            for (int row = 0; row < 6; row++){

                if(!posConfirm && preState.get(col).get(row)==0){
                    Log.i(TAG, String.valueOf("pos:"+col+","+row));
                    validPos.add(row);
                    posConfirm = true;
                }
            }
        }
    }

    public boolean validate(int col, int row){
        Log.d(TAG, "row: "+row+"real row:"+String.valueOf(currentState.get(col).get(row)));
        if(validPos.get(col) == row){
            Log.i(TAG, String.valueOf("pos:"+col+","+row));
            validPos.set(col, row+1);
            return true;
        }
        return false;
    }

    public ArrayList<ArrayList<Integer>> initBoardState(){
        ArrayList<ArrayList<Integer>> tempBoard = new ArrayList<>();
        for ( int col = 0; col < 7; col++){
            ArrayList<Integer> tempCol = new ArrayList<>();
            for (int row = 0; row < 6; row++){
                tempCol.add(0);
            }
            tempBoard.add(tempCol);
        }
        Log.i(TAG, String.valueOf("boardState.size():"+tempBoard.size()));
        Log.i(TAG, String.valueOf("boardState.get(0).size():"+tempBoard.get(0).size()));
        return tempBoard;
    }

    public void constructBoardState(String color, int x, int y, int board){
        //column first
        int code = 0;
        int posX = 5 - x;
        int posY = y;
        ArrayList<Integer> tempCol = new ArrayList<>();
        if(color == "G"){
            code = 1;
        }
        else if(color == "P"){
            code = 2;
        }
        if (board == 0){
//            Log.i(TAG, String.valueOf("X:"+posX+", Y:"+posY+", code:"+code));
            tempCol = preState.get(posY);
            tempCol.set(posX, code);
            preState.set(posY, tempCol);
        }
        else if(board == 1){
            tempCol = currentState.get(posY);
            tempCol.set(posX, code);
            currentState.set(posY, tempCol);
//            Log.i(TAG, String.valueOf("X:"+posX+", Y:"+posY+", code:"+currentState.get(posY).get(posX)));
        }


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