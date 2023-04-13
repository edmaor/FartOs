package edu.maor.fartos.main;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.maor.fartos.FartOS;
import edu.maor.fartos.R;

public class CardDetailPlayerHolder extends RecyclerView.Adapter<CardDetailPlayerHolder.ViewHolder> {
    List<Player> players;
    public int playerId;
    private int old;

    public CardDetailPlayerHolder(List<Player> players) {
        this.players = new ArrayList<>(players);
        playerId = -1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_detail_player_item, parent, false);
        return new CardDetailPlayerHolder.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Player player = players.get(position);
        holder.playerSprite.setImageResource(player.getPlayer_sprite());
        holder.playerPos.setText(String.valueOf(player.position));

        if (player == FartOS.players.get(FartOS.turn)) {
            playerId = player.id;
            holder.itemView.setBackgroundColor(Color.parseColor("#5557A2"));
            holder.playerPos.setTextColor(Color.parseColor("#EDDECD"));
        }
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    // VIEW_HOLDER
    public class ViewHolder extends RecyclerView.ViewHolder {
        protected ImageView playerSprite;
        protected TextView playerPos;

        public ViewHolder(@NonNull View view) {
            super(view);

            playerSprite = view.findViewById(R.id.cdpi_sprite);
            playerPos = view.findViewById(R.id.cdpi_tile_pos);
            view.setOnClickListener( v -> {
                playerId = players.get(getAdapterPosition()).id;
                view.setBackgroundColor(Color.parseColor("#5557A2"));
                playerPos.setTextColor(Color.parseColor("#EDDECD"));
            });
        }
    }
}
