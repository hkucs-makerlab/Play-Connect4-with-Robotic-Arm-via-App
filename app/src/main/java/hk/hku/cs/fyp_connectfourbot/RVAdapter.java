package hk.hku.cs.fyp_connectfourbot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;

    ArrayList<PlayerData> list = new ArrayList<>();

    public RVAdapter(Context ctx){
        this.context = ctx;
    }
    public void setItems(ArrayList<PlayerData> pd){
        list.addAll(pd);
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view =  LayoutInflater.from(context).inflate(R.layout.card_item, parent, false);
        return new PlayerDataVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PlayerDataVH vh = (PlayerDataVH) holder;
        PlayerData pd = list.get(position);
        vh.rank.setText(String.valueOf(position+1));
        vh.name.setText(pd.getName());
        vh.score.setText(String.valueOf(pd.getScore()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
