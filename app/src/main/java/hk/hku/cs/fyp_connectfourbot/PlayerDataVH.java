package hk.hku.cs.fyp_connectfourbot;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PlayerDataVH extends RecyclerView.ViewHolder {

    public TextView rank, name, score;
    public PlayerDataVH(@NonNull View itemView) {
        super(itemView);
        rank = itemView.findViewById(R.id.rank);
        name = itemView.findViewById(R.id.name);
        score = itemView.findViewById(R.id.score);
    }
}
