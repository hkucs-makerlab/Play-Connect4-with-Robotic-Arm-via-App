package hk.hku.cs.fyp_connectfourbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class ControllerActivity extends AppCompatActivity implements
        View.OnClickListener{

    static private String LOG_TAG = ControllerFragment.class.getSimpleName();

    SwitchCompat stepperSwitch;
    Button homeButton, bottomButton, restButton, endStopButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);

        stepperSwitch = (SwitchCompat) findViewById(R.id.stepperSwitch);
        stepperSwitch.setOnCheckedChangeListener(stepperListener);
        stepperSwitch.setChecked(true);

        homeButton = (Button) findViewById(R.id.homeButton);
        homeButton.setOnClickListener(this);
        bottomButton = (Button) findViewById(R.id.bottomButton);
        bottomButton.setOnClickListener(this);
        restButton = (Button) findViewById(R.id.restButton);
        restButton.setOnClickListener(this);
        endStopButton = (Button) findViewById(R.id.endStopButton);
        endStopButton.setOnClickListener(this);
    }

    private CompoundButton.OnCheckedChangeListener stepperListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                Toast.makeText(getApplicationContext(), "Stepper On", Toast.LENGTH_SHORT).show();
            } else{
                Toast.makeText(getApplicationContext(), "Stepper Off", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.homeButton) {
            Toast.makeText(getApplicationContext(), "homeButton", Toast.LENGTH_SHORT).show();
        }
        else if (view.getId() == R.id.bottomButton) {
            Toast.makeText(getApplicationContext(), "bottomButton", Toast.LENGTH_SHORT).show();
        }
        else if (view.getId() == R.id.restButton) {
            Toast.makeText(getApplicationContext(), "restButton", Toast.LENGTH_SHORT).show();
        }
        else if (view.getId() == R.id.endStopButton) {
            Toast.makeText(getApplicationContext(), "endStopButton", Toast.LENGTH_SHORT).show();
        }

    }
}