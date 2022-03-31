package hk.hku.cs.fyp_connectfourbot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class ScoreBoardActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RVAdapter adapter;
    DBPlayerData dbp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);
        recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter = new RVAdapter(this);
        recyclerView.setAdapter(adapter);
        dbp = new DBPlayerData();
        loadData();

    }

    private void loadData() {
        dbp.get().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ArrayList<PlayerData> pds = new ArrayList<>();
                ArrayList<PlayerData> sortedPds = new ArrayList<>();
                for (DataSnapshot data: snapshot.getChildren()){
                    PlayerData pd = data.getValue(PlayerData.class);
                    pds.add(pd);
                }
                sortedPds = sort(pds);
                adapter.setItems(sortedPds);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

            public ArrayList<PlayerData> sort(ArrayList<PlayerData> pds){
                ArrayList<PlayerData> pdsCopy = pds;
                int size = pdsCopy.size();
                for (int i = 0; i < size; i++){
                    int minIndex = i;
                    for (int j = i + 1; j < size ; j++){
                        if (pdsCopy.get(minIndex).getScore() > pdsCopy.get(j).getScore()){
                            minIndex = j;
                        }
                    }
                    PlayerData tempPd = pdsCopy.get(i);
                    pdsCopy.set(i, pdsCopy.get(minIndex));
                    pdsCopy.set(minIndex, tempPd);
                }
                Collections.reverse(pdsCopy);
                return pdsCopy;
            }
        });
    }
}