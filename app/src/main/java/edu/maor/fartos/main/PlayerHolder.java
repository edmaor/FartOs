package edu.maor.fartos.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.maor.fartos.FartOS;
import edu.maor.fartos.R;

public class PlayerHolder extends RecyclerView.Adapter<PlayerHolder.ViewHolder> {
    List<Player> players;
    public PlayerHolder() {
        this.players = new ArrayList<>();
    }

    public void addPlayer(Player player) {
        players.add(player);
    }
    public void removePlayer(Player player) {
        players.remove(player);
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Player player = players.get(position);
        holder.playerSprite.setImageResource(player.getPlayer_sprite());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView playerSprite;
        public ViewHolder(@NonNull View view) {
            super(view);
            playerSprite = view.findViewById(R.id.player_sprite);
        }
    }
}
