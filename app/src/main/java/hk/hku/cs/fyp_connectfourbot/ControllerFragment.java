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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ControllerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ControllerFragment extends Fragment implements
        View.OnClickListener {
    static private String LOG_TAG = ControllerFragment.class.getSimpleName();
    private final int imageButtonID[] = {
            0, // dummy value
            R.id.forwardButton, R.id.backwardButton, R.id.leftButton,R.id.rightButton,
    };
    private final int buttonID[] = {
            0,0,0,0,0, // dummy value, corrspond to length of imageButtonID[]
            R.id.upButton, R.id.downButton,
            R.id.placeButton, R.id.pickButton,
    };


/*
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public static ControllerFragment newInstance(String param1, String param2) {
        ControllerFragment fragment = new ControllerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
*/
    public ControllerFragment() {
        // Required empty public constructor
    }

    public static ControllerFragment newInstance()  {
        return new ControllerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
    public void onClick(View view) {
        int buttonClicked = -1;
        boolean isImgButton = false;
        for (int i = 1; i < imageButtonID.length; i++) {
            if (view.getId() == imageButtonID[i]) {
                buttonClicked = i;
                Toast.makeText(view.getContext(), "buttonClicked:" + i, Toast.LENGTH_SHORT).show();
                isImgButton = true;
                break;
            }
        }
        if (isImgButton == false){
            for (int i = 5; i < buttonID.length; i++) {
                if (view.getId() == buttonID[i]) {
                    buttonClicked = i;
                    Toast.makeText(view.getContext(), "buttonClicked:" + i, Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
        switch (buttonClicked) {
            case 1:
                Log.e(LOG_TAG, "forwardButton");
                break;
            case 2:
                Log.e(LOG_TAG, "backwardButton");
                break;
            case 3:
                Log.e(LOG_TAG, "leftButton");
                break;
            case 4:
                Log.e(LOG_TAG, "rightButton");
                break;
            case 5:
                Log.e(LOG_TAG, "upButton");
                break;
            case 6:
                Log.e(LOG_TAG, "downButton");
                break;
            case 7:
                Log.e(LOG_TAG, "placeButton");
                break;
            case 8:
                Log.e(LOG_TAG, "pickButton");
                break;
        }
    }

}