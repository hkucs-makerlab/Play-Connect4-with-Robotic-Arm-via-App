package hk.hku.cs.fyp_connectfourbot;

import java.io.Serializable;
import java.util.ArrayList;

public class Coordinates implements Serializable {
    ArrayList<ArrayList<ArrayList<Integer>>> coor;

    public Coordinates(ArrayList<ArrayList<ArrayList<Integer>>> coor) {
        this.coor = coor;
    }

    public Coordinates(){

    }
}
