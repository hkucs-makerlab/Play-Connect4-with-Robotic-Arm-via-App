package hk.hku.cs.fyp_connectfourbot;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class DBPlayerData {
    private DatabaseReference databaseReference;
    static private String TAG = "DB";
    public DBPlayerData(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(PlayerData.class.getSimpleName());
    }

    public Task<Void> add(PlayerData pd){
        Log.i(TAG, "tried to push");
        return databaseReference.push().setValue(pd);
    }

    public Query get(){
        return databaseReference.orderByKey();
    }
}
