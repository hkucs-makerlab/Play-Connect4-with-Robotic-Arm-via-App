package hk.hku.cs.fyp_connectfourbot;

public class PlayerData {

    private String name;
    private int score;
    public PlayerData(){}
    public PlayerData(String name, int score){
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
