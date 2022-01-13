package hk.hku.cs.fyp_connectfourbot;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.makerlab.bt.BluetoothConnect;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;


public class ControllerFragment extends Fragment implements
        View.OnClickListener {
    static private String LOG_TAG = ControllerFragment.class.getSimpleName();
    static public final boolean D = BuildConfig.DEBUG;

    private BluetoothConnect mBluetoothConnect;
    private Timer mDataSendTimer = null;



    private static RobotArmGcode mRobotArmGcode = new RobotArmGcode();

    public static RobotArmGcode getmRobotArmGcode() {
        return mRobotArmGcode;
    }

    private static Queue<byte[]> mQueue = new LinkedList<>();
    
    public static Queue<byte[]> getmQueue() {
        return mQueue;
    }

    private final int imageButtonID[] = {
            0, // dummy value
            R.id.forwardButton, R.id.backwardButton, R.id.leftButton,R.id.rightButton,
    };
    private final int buttonID[] = {
            0,0,0,0,0, // dummy value, corrspond to length of imageButtonID[]
            R.id.upButton, R.id.downButton,
            R.id.placeButton, R.id.pickButton,
    };

    public ControllerFragment() {
        // Required empty public constructor
    }

    public static ControllerFragment newInstance()  {
        return new ControllerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_controller, container, false);
        for (int i = 1; i < imageButtonID.length; i++) {
            ImageButton imgButton = rootView.findViewById(imageButtonID[i]);
            if (imgButton != null) {
                imgButton.setOnClickListener(this);
            }
            Log.e(LOG_TAG, "onCreateView()");
        }
        for (int i = 1; i < buttonID.length; i++) {
            Button button = rootView.findViewById(buttonID[i]);
            if (button != null) {
                button.setOnClickListener(this);
            }
            Log.e(LOG_TAG, "onCreateView()");
        }

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        MainActivity activity = MainActivity.getInstance();
        mBluetoothConnect = activity.getBluetoothConnect();
        mDataSendTimer = new Timer();
        mDataSendTimer.scheduleAtFixedRate(new DataSendTimerTask(), 1000, 250);

        mQueue.clear();
        mQueue.add(mRobotArmGcode.setStepperOff());
        mQueue.add(mRobotArmGcode.goHome());
        mQueue.add(mRobotArmGcode.setStepperOn());

        if (D)
            Log.e(LOG_TAG, "onStart()");
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mDataSendTimer != null) {
            mDataSendTimer.cancel();
        }
        mBluetoothConnect = null;
        if (D)
            Log.e(LOG_TAG, "onStop()");
    }

    @Override
    public void onClick(View view) {
        int buttonClicked = -1;
        boolean isImgButton = false;
        for (int i = 1; i < imageButtonID.length; i++) {
            if (view.getId() == imageButtonID[i]) {
                buttonClicked = i;
//                Toast.makeText(view.getContext(), "buttonClicked:" + i, Toast.LENGTH_SHORT).show();
                isImgButton = true;
                break;
            }
        }
        if (isImgButton == false){
            for (int i = 5; i < buttonID.length; i++) {
                if (view.getId() == buttonID[i]) {
                    buttonClicked = i;
//                    Toast.makeText(view.getContext(), "buttonClicked:" + i, Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }

        synchronized (mQueue) {
            switch (buttonClicked) {
                case 1:
                    Log.e(LOG_TAG, "forwardButton");
                    mQueue.add(mRobotArmGcode.moveY(20));
                    break;
                case 2:
                    Log.e(LOG_TAG, "backwardButton");
                    mQueue.add(mRobotArmGcode.moveNY(20));
                    break;
                case 3:
                    Log.e(LOG_TAG, "leftButton");
                    mQueue.add(mRobotArmGcode.moveX(20));
                    break;
                case 4:
                    Log.e(LOG_TAG, "rightButton");
                    mQueue.add(mRobotArmGcode.moveNX(20));
                    break;
                case 5:
                    Log.e(LOG_TAG, "upButton");
                    mQueue.add(mRobotArmGcode.moveZ(20));
                    break;
                case 6:
                    Log.e(LOG_TAG, "downButton");
                    mQueue.add(mRobotArmGcode.moveNZ(20));
                    break;
                case 7:
                    Log.e(LOG_TAG, "placeButton");
                    mQueue.add(mRobotArmGcode.place());
                    break;
                case 8:
                    Log.e(LOG_TAG, "pickButton");
                    mQueue.add(mRobotArmGcode.pick());
                    break;
            }
        }

    }

    class DataSendTimerTask extends TimerTask {
        private String LOG_TAG = DataSendTimerTask.class.getSimpleName();

        @Override
        public void run() {
            if (mBluetoothConnect == null) {
                return;
            }
            synchronized (mQueue) {
                if (!mQueue.isEmpty()) {
                    mBluetoothConnect.send(mQueue.remove());
                    Log.e(LOG_TAG, "DataSendTimerTask.run() - send");
                }
            }
        }
    }

}