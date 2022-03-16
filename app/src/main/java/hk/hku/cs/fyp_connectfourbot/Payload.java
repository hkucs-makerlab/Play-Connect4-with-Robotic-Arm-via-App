package hk.hku.cs.fyp_connectfourbot;

import java.io.Serializable;
import java.util.ArrayList;

public class Payload implements Serializable {
    ArrayList<ArrayList<ArrayList<Integer>>> coor;
    int player;

    public Payload(ArrayList<ArrayList<ArrayList<Integer>>> coor, int player) {
        this.coor = coor;
        this.player = player;
    }

    public Payload(){

    }
}
