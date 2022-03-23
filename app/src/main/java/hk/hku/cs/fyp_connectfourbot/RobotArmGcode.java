package hk.hku.cs.fyp_connectfourbot;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/*
    https://www.thingiverse.com/thing:1718984
    https://en.wikipedia.org/wiki/G-code
 */
public class RobotArmGcode {
    static final String LINE_BREAK="\r\n";
    //
    static final String M17 = "M17"; //stepper on
    static final String M18 = "M18"; //stepper off
    static final String M3 = "M3"; //grepper on
    static final String M5 = "M5"; //grepper off
    static final String G1 = "G1";   // move steppers
    static final String G28 = "G28"; // auto home
    //
    static final String HOME = G1+" X0 Y225 Z180";
    static final String REST = G1+" X0 Y145 Z70";
    static final String BOTTOM = G1+" X0 Y170 Z0";
    static final String END_STOP =G1+" X0 Y70 Z134";
    static final String PICK = M3+" T-5";
    static final String PLACE = M3+" T45";

//    static final String COL1 = G1+" X0 Y120 Z180";
//    static final String COL2 = G1+" X0 Y160 Z170";
//    static final String COL3 = G1+" X0 Y190 Z160";
//    static final String COL4 = G1+" X0 Y225 Z150";
//    static final String COL5 = G1+" X0 Y255 Z150";
//    static final String COL6 = G1+" X0 Y285 Z140";
//    static final String COL7 = G1+" X0 Y315 Z135";

    static final String MOVE_LEFT = G1+" X-90 Y225 Z180";
    static final String DISC_POS = G1+" X-90 Y210 Z-65";


    static final double MINX = -200;
    static final double MAXX = 200;
    static final double MINZ = -80;
    static final double MAXZ = 210;


    private double mPosX=0, mPosY=225, mPosZ=180;

    private static final String TAG = RobotArmGcode.class.getSimpleName();

    public byte[] setStepperOn() {
        return getPayload(M17);
    }
    public byte[] setStepperOff() {
        return getPayload(M18);
    }

    public byte[] moveX(int offset) {
        mPosX += offset;
        if (mPosX >MAXX){mPosX=MAXX;}
        return getPayload(G1 + " X" + mPosX + " Y" + mPosY + " Z" + mPosZ);
    }
    public byte[] moveNX(int offset) {
        mPosX -= offset;
        if (mPosX <MINX){mPosX=MINX;}
        return getPayload(G1 + " X" + mPosX + " Y" + mPosY + " Z" + mPosZ);
    }

    public byte[] moveY(int offset) {
        mPosY += offset;
//        if (mPosY >MAXY){mPosY=MAXY;}
        return getPayload(G1 + " X" + mPosX + " Y" + mPosY + " Z" + mPosZ);
    }
    public byte[] moveNY(int offset) {
        mPosY -= offset;
//        if (mPosY <MINY){mPosY=MINY;}
        return getPayload(G1 + " X" + mPosX + " Y" + mPosY + " Z" + mPosZ);
    }

    public byte[] moveZ(int offset) {
        mPosZ += offset;
        if (mPosZ >MAXZ){mPosZ=MAXZ;}
        return getPayload(G1 + " X" + mPosX + " Y" + mPosY + " Z" + mPosZ);
    }
    public byte[] moveNZ(int offset) {
        mPosZ -= offset;
        if (mPosZ <MINZ){mPosZ=MINZ;}
        return getPayload(G1 + " X" + mPosX + " Y" + mPosY + " Z" + mPosZ);
    }

    public byte[] toCol(int col) {
        mPosX = 0;
        Log.i(TAG, "toCol");
        switch (col) {
            case 1:
                mPosY=120;
                mPosZ=185;
                break;
            case 2:
                mPosY=160;
                mPosZ=170;
                break;
            case 3:
                mPosY=190;
                mPosZ=160;
                break;
            case 4:
                mPosY=225;
                mPosZ=150;
                break;
            case 5:
                mPosY=255;
                mPosZ=150;
                break;
            case 6:
                mPosY=285;
                mPosZ=140;
                break;
            case 7:
                mPosY=315;
                mPosZ=135;
                break;
        }
        return getPayload(G1 + " X" + mPosX + " Y" + mPosY + " Z" + mPosZ);
    }

    public byte[] autoHome() {
        mPosX=0;
        mPosY=225;
        mPosZ=180;
        Log.i(TAG, "autoHome");
        return getPayload(G28);
    }
    public byte[] goLeft() {
        mPosX=-105;
        mPosY=225;
        mPosZ=180;
        Log.i(TAG, "goLeft");
        return getPayload(MOVE_LEFT);
    }
    public byte[] goDiscPos() {
        mPosX=-105;
        mPosY=225;
        mPosZ=-45;
        Log.i(TAG, "goDiscPos");
        return getPayload(DISC_POS);
    }


    public byte[] goHome() {
        mPosX=0;
        mPosY=225;
        mPosZ=180;
        Log.i(TAG, "goHome");
        return getPayload(HOME);
    }
    public byte[] goRest() {
        mPosX=0;
        mPosY=145;
        mPosZ=70;
        return getPayload(REST);
    }
    public byte[] goBottom() {
        mPosX=0;
        mPosY=170;
        mPosZ=0;
        return getPayload(BOTTOM);
    }
    public byte[] goEndStop() {
        mPosX=0;
        mPosY=70;
        mPosZ=134;
        return getPayload(END_STOP);
    }

    public byte[] pick() {
        Log.i(TAG, "pick");
        return getPayload(PICK);
    }
    public byte[] place() {
        Log.i(TAG, "place");
        return getPayload(PLACE);
    }

    private byte[] getPayload(String gcode) {
        try {
            gcode+=LINE_BREAK;
            byte[] data = gcode.getBytes("iso8859-1");
            Log.e(TAG, "code " + gcode);
//            Log.e(TAG, "code bytes iso8859-1 " + Arrays.toString(data));
            return data;
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
}
