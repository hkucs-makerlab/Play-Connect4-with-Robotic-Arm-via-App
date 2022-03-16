package hk.hku.cs.fyp_connectfourbot;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.makerlab.bt.BluetoothConnect;
import com.makerlab.bt.BluetoothScan;
import com.makerlab.ui.BluetoothDevListActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.opencv.android.JavaCameraView;

//public class MainActivity extends AppCompatActivity{
//    private static String TAG = "MainActivity";
//
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//
//    }
//
//}

public class MainActivity extends AppCompatActivity implements Runnable, BluetoothConnect.ConnectionHandler {
    private int mImageIndex = 0;
//    private String[] mTestImages = {"test1.png", "test2.jpg", "test3.png"};

    private ImageView mImageView;
    private Button mButtonDetect;
    private ProgressBar mProgressBar;
    private Bitmap mBitmap = null;
    private float mImgScaleX, mImgScaleY, mIvScaleX, mIvScaleY, mStartX, mStartY;

    //bluetooth variable
    private ImageButton mBluetoothButton;
    static public final boolean D = BuildConfig.DEBUG;
    static public final int REQUEST_BT_GET_DEVICE = 1112;
    static public final String BLUETOOTH_REMOTE_DEVICE = "bt_remote_device";
    static private String LOG_TAG = MainActivity.class.getSimpleName();

    private BluetoothConnect mBluetoothConnect;
    private BluetoothScan mBluetoothScan;

    private SharedPreferences mSharedPref;
    private String mSharedPrefFile = "hk.hku.cs.fyp_connectfourbot";

    private static final String FINISH_ACTIVITY_BROADCAST = BuildConfig.APPLICATION_ID + ".FINISH_ACTIVITY_BROADCAST";
    private LocalBroadcastManager localBroadcastManager;
    private static MainActivity instance;




    public static String assetFilePath(Context context, String assetName) throws IOException {
        File file = new File(context.getFilesDir(), assetName);
        if (file.exists() && file.length() > 0) {
            return file.getAbsolutePath();
        }

        try (InputStream is = context.getAssets().open(assetName)) {
            try (OutputStream os = new FileOutputStream(file)) {
                byte[] buffer = new byte[4 * 1024];
                int read;
                while ((read = is.read(buffer)) != -1) {
                    os.write(buffer, 0, read);
                }
                os.flush();
            }
            return file.getAbsolutePath();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }

        setContentView(R.layout.activity_main);

//        try {
//            mBitmap = BitmapFactory.decodeStream(getAssets().open(mTestImages[mImageIndex]));
//        } catch (IOException e) {
//            Log.e("Object Detection", "Error reading assets", e);
//            finish();
//        }

//        mImageView = findViewById(R.id.imageView);
//        mImageView.setImageBitmap(mBitmap);
//        mResultView = findViewById(R.id.resultView);
//        mResultView.setVisibility(View.INVISIBLE);             beware!!

//        final Button buttonTest = findViewById(R.id.testButton);
//        buttonTest.setText(("Test Image 1/3"));
//        buttonTest.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                mResultView.setVisibility(View.INVISIBLE);
//                mImageIndex = (mImageIndex + 1) % mTestImages.length;
//                buttonTest.setText(String.format("Text Image %d/%d", mImageIndex + 1, mTestImages.length));
//
//                try {
//                    mBitmap = BitmapFactory.decodeStream(getAssets().open(mTestImages[mImageIndex]));
//                    mImageView.setImageBitmap(mBitmap);
//                } catch (IOException e) {
//                    Log.e("Object Detection", "Error reading assets", e);
//                    finish();
//                }
//            }
//        });


//        final Button buttonSelect = findViewById(R.id.selectButton);
//        buttonSelect.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                mResultView.setVisibility(View.INVISIBLE);
//
//                final CharSequence[] options = { "Choose from Photos", "Take Picture", "Cancel" };
//                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                builder.setTitle("New Test Image");
//
//                builder.setItems(options, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int item) {
//                        if (options[item].equals("Take Picture")) {
//                            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                            startActivityForResult(takePicture, 0);
//                        }
//                        else if (options[item].equals("Choose from Photos")) {
//                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
//                            startActivityForResult(pickPhoto , 1);
//                        }
//                        else if (options[item].equals("Cancel")) {
//                            dialog.dismiss();
//                        }
//                    }
//                });
//                builder.show();
//            }
//        });

        //bluetooth
        mBluetoothButton = findViewById(R.id.bluetooth);

        mBluetoothConnect = new BluetoothConnect(this);
        mBluetoothConnect.setConnectionHandler(this);

        mSharedPref = getSharedPreferences(mSharedPrefFile, MODE_PRIVATE);
        String bluetothDeviceAddr = mSharedPref.getString(BLUETOOTH_REMOTE_DEVICE, null);

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        instance = this;

        if (bluetothDeviceAddr != null) {
            //Log.e(LOG_TAG, "onCreate(): found share perference");
            mBluetoothScan = new BluetoothScan(this);
            BluetoothDevice mBluetoothDevice = mBluetoothScan.getBluetoothDevice(bluetothDeviceAddr);
            mBluetoothConnect.connectBluetooth(mBluetoothDevice);
            if (D)
                Log.e(LOG_TAG, "onCreate() - connecting bluetooth device");
        } else {
            if (D)
                Log.e(LOG_TAG, "onCreate()");
        }

        if (mBluetoothConnect.isConnected()){ //if connected already
            enableMainActivityButtons(true);
            enableConnectButton(false);
        }else {
            //if bluetooth not connected
            enableMainActivityButtons(false);
            enableConnectButton(true);
        }


//        final Button buttonLive = findViewById(R.id.auto);
//
//        buttonLive.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                final Intent intent = new Intent(MainActivity.this, AutoActivity.class);
//                startActivity(intent);
//            }
//        });

//        mButtonDetect = findViewById(R.id.detectButton);
//        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
//        mButtonDetect.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                mButtonDetect.setEnabled(false);
//                mProgressBar.setVisibility(ProgressBar.VISIBLE);
//                mButtonDetect.setText(getString(R.string.run_model));
//
//                mImgScaleX = (float)mBitmap.getWidth() / PrePostProcessor.mInputWidth;
//                mImgScaleY = (float)mBitmap.getHeight() / PrePostProcessor.mInputHeight;
//
//                mIvScaleX = (mBitmap.getWidth() > mBitmap.getHeight() ? (float)mImageView.getWidth() / mBitmap.getWidth() : (float)mImageView.getHeight() / mBitmap.getHeight());
//                mIvScaleY  = (mBitmap.getHeight() > mBitmap.getWidth() ? (float)mImageView.getHeight() / mBitmap.getHeight() : (float)mImageView.getWidth() / mBitmap.getWidth());
//
//                mStartX = (mImageView.getWidth() - mIvScaleX * mBitmap.getWidth())/2;
//                mStartY = (mImageView.getHeight() -  mIvScaleY * mBitmap.getHeight())/2;
//
//                Thread thread = new Thread(MainActivity.this);
//                thread.start();
//            }
//        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        mBitmap = (Bitmap) data.getExtras().get("data");
                        Matrix matrix = new Matrix();
                        matrix.postRotate(90.0f);
                        mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
                        mImageView.setImageBitmap(mBitmap);
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                mBitmap = BitmapFactory.decodeFile(picturePath);
                                Matrix matrix = new Matrix();
                                matrix.postRotate(90.0f);
                                mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
                                mImageView.setImageBitmap(mBitmap);
                                cursor.close();
                            }
                        }
                    }
                    break;
            }
        }

        //bluetooth
        if (requestCode == REQUEST_BT_GET_DEVICE) {
            if (resultCode == RESULT_OK) {
                BluetoothDevice bluetoothDevice = data.getParcelableExtra(BluetoothDevListActivity.EXTRA_KEY_DEVICE);
                if (bluetoothDevice != null) {
                    mBluetoothConnect.connectBluetooth(bluetoothDevice);
                    if (D)
                        Log.e(LOG_TAG, "onActivityResult() - connecting");
                }
            } else if (resultCode == RESULT_CANCELED) {
                if (D)
                    Log.e(LOG_TAG, "onActivityResult() - canceled");
            }
        }
    }

    private void enableConnectButton(boolean flag) {
        if (flag){
            mBluetoothButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_bluetooth_24));
            mBluetoothButton.setTag(R.drawable.ic_baseline_bluetooth_24);
        } else{
            mBluetoothButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_bluetooth_disabled_24));
            mBluetoothButton.setTag(R.drawable.ic_baseline_bluetooth_disabled_24);
        }
    }

    @Override
    public void run(){

    }

    public void launchSelectActivity(View view){
        Intent selectModeAct = new Intent(this, SelectActivity.class);
        startActivity(selectModeAct);
    }

    public void launchAutoActivity(View view) {
//        Log.d(TAG, "Launch Auto Activity");
        Intent autoModeAct = new Intent(this, AutoActivity.class);
        startActivity(autoModeAct);
    }
//
    public void launchManualActivity(View view) {
//        Log.d(TAG, "Launch Manual Activity");
        Intent manualModeAct = new Intent(this, ControllerActivity.class);
        startActivity(manualModeAct);
    }

    public void launchBuildingActivity(View view) {
//        Log.d(TAG, "Launch Building Activity");
        Intent buildingModeAct = new Intent(this, BuildingActivity.class);
        startActivity(buildingModeAct);
    }

    public void launchDrawingActivity(View view) {
        Intent drawingAct = new Intent(this, DrawingActivity.class);
        startActivity(drawingAct);
    }
    
    public void launchControllerActivity(View view) {
//        Log.d(TAG, "Launch Controller Activity");
        Intent controllerAct = new Intent(this, ControllerActivity.class);
        startActivity(controllerAct);
    }

    public void launchBluetoothActivity(View view) {
//        if (mBluetoothButton.getDrawable() == getResources().getDrawable(R.drawable.ic_baseline_bluetooth_24)){
//            Log.d(MainActivity.class.getSimpleName(), "it is a ic_baseline_bluetooth_24");
//        }
        if ((Integer)mBluetoothButton.getTag() == R.drawable.ic_baseline_bluetooth_24){ //click to connect bluetooth
            Log.d(MainActivity.class.getSimpleName(), "it is a ic_baseline_bluetooth_24");

            if (mBluetoothConnect.isConnected()) {
                mBluetoothConnect.disconnectBluetooth();
            }
            Intent intent = new Intent(this, BluetoothDevListActivity.class);
            startActivityForResult(intent, REQUEST_BT_GET_DEVICE);

//            mBluetoothButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_bluetooth_disabled_24));
//            mBluetoothButton.setTag(R.drawable.ic_baseline_bluetooth_disabled_24);

        } else { //click to disconnect bluetooth
            Log.d(MainActivity.class.getSimpleName(), "it is a ic_baseline_bluetooth_disabled_24");
            mBluetoothConnect.disconnectBluetooth();
//            closeControlFragment();
            enableMainActivityButtons(false);
            enableConnectButton(true);
            removeSharePerf();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBluetoothConnect.disconnectBluetooth();
        if (D)
            Log.e(LOG_TAG, "onDestroy()");
    }

    public BluetoothConnect getBluetoothConnect() {
        return mBluetoothConnect;
    }

    @Override
    public void onConnect(BluetoothConnect instant) {
        runOnUiThread(new Thread() {
            public void run() {
                Toast.makeText(getApplicationContext(), "Connecting", Toast.LENGTH_SHORT).show();
            }
        });
        if (D)
            Log.e(LOG_TAG, "onConnect() - Connecting");
    }

    @Override
    public void onConnectionSuccess(BluetoothConnect instant) {
        SharedPreferences.Editor preferencesEditor = mSharedPref.edit();
        preferencesEditor.putString(BLUETOOTH_REMOTE_DEVICE, mBluetoothConnect.getDeviceAddress());
        preferencesEditor.apply();
        if (D)
            Log.e(LOG_TAG, "onConnectionSuccess() - connected");
        runOnUiThread(new Thread() {
            public void run() {
                runOnUiThread(new Thread() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
                        if (mBluetoothButton != null){
                            enableMainActivityButtons(true);
                            enableConnectButton(false);
                        }
//                        displayControlFragment();
                    }
                });
            }
        });
    }

    @Override
    public void onConnectionFail(BluetoothConnect instant) {
        runOnUiThread(new Thread() {
            public void run() {
                Toast.makeText(getApplicationContext(),
                        "Connecting fail!",
                        Toast.LENGTH_LONG).show();
            }
        });
        removeSharePerf();
        if (D)
            Log.e(LOG_TAG, "onConnectionFail()");
        runOnUiThread(new Thread() {
            public void run() {
//                closeControlFragment();
                enableMainActivityButtons(false);
                enableConnectButton(true);
                Toast.makeText(getApplicationContext(), "Failed to connect!", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onDisconnected(BluetoothConnect instant) {
        runOnUiThread(new Thread() {
            public void run() {
                Toast.makeText(getApplicationContext(), "Disconnected!", Toast.LENGTH_LONG).show();
//                closeControlFragment();
                Intent finishActIntent = new Intent(FINISH_ACTIVITY_BROADCAST);
                localBroadcastManager.sendBroadcast(finishActIntent);
                enableMainActivityButtons(false);
                enableConnectButton(true);
            }
        });
        if (D)
            Log.e(LOG_TAG, "onDisconnected()");
    }

    private void removeSharePerf() {
        SharedPreferences.Editor preferencesEditor = mSharedPref.edit();
        preferencesEditor.remove(BLUETOOTH_REMOTE_DEVICE);
        preferencesEditor.apply();
    }

    private void enableMainActivityButtons(boolean flag) {
        Button MainActivityButton = findViewById(R.id.auto);
//        MainActivityButton.setEnabled(flag);
        MainActivityButton.setEnabled(true);
        MainActivityButton = findViewById(R.id.controller);
        MainActivityButton.setEnabled(flag);
//        MainActivityButton = findViewById(R.id.draw);
//        MainActivityButton.setEnabled(flag);
        MainActivityButton = findViewById(R.id.build);
        MainActivityButton.setEnabled(flag);
    }

    public static MainActivity getInstance() {
        return instance;
    }


}