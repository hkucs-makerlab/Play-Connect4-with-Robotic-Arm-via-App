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
    //
    static final String HOME = G1+" X0 Y120 Z120";
    static final String REST = G1+" X0 Y40 Z70";
    static final String BOTTOM = G1+" X0 Y100 Z0";
    static final String END_STOP =G1+ " X0 Y19.5 Z134";
    static final String PICK = M3+" T35";
    static final String PLACE = M5+" T10";

    static final double MINX = -200;
    static final double MAXX = 200;
    static final double MINY = 50;
    static final double MAXY = 220;
    static final double MINZ = -20;
    static final double MAXZ = 160;
    static final double MAXGRIPOPEN = 50;
    static final double MAXGRIPCLOSE = 35;


    private double mPosX=0, mPosY=120, mPosZ=120;

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
        if (mPosY >MAXY){mPosY=MAXY;}
        return getPayload(G1 + " X" + mPosX + " Y" + mPosY + " Z" + mPosZ);
    }
    public byte[] moveNY(int offset) {
        mPosY -= offset;
        if (mPosY <MINY){mPosY=MINY;}
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


    public byte[] goHome() {
        mPosX=0;
        mPosY=120;
        mPosZ=120;
        return getPayload(HOME);
    }
    public byte[] goRest() {
        mPosX=0;
        mPosY=40;
        mPosZ=70;
        return getPayload(REST);
    }
    public byte[] goBottom() {
        mPosX=0;
        mPosY=100;
        mPosZ=0;
        return getPayload(BOTTOM);
    }
    public byte[] goEndStop() {
        mPosX=0;
        mPosY=19.5;
        mPosZ=134;
        return getPayload(END_STOP);
    }

    public byte[] pick() {
        return getPayload(PICK);
    }
    public byte[] place() {
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
