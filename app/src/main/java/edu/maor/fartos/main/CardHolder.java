package edu.maor.fartos.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.maor.fartos.CardDetail;
import edu.maor.fartos.R;

public class CardHolder extends RecyclerView.Adapter<CardHolder.ViewHolder> {
    private final Player player;
    private List<Card> hand;
    private FragmentManager fm;
    public CardHolder(Player player, FragmentManager fm) {
        this.fm = fm;
        this.player = player;
    }

    public void updateHand() {
        this.hand = player.getHand();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_deck_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Card card = hand.get(position);
        holder.cardName.setText(card.getName());
        holder.cardSprite.setImageResource(card.getSprite());
    }

    public List<Card> getHand() {
        return hand;
    }
    @Override
    public int getItemCount() {
        return hand.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView cardName;
        ImageView cardSprite;
        public ViewHolder(@NonNull View view) {
            super(view);

            cardName = view.findViewById(R.id.card_item_name);
            cardSprite = view.findViewById(R.id.card_item_image);
            view.setOnClickListener(v -> {
                // Prepare Card dialog
                int position = getAdapterPosition();
                DialogFragment cardDetail = new CardDetail();
                Card card = hand.get(position);

                Bundle bundle = new Bundle();
                bundle.putSerializable("CARD",card);
                bundle.putInt("PLAYER", player.id);
                cardDetail.setArguments(bundle);
                cardDetail.show(fm, "CARD");
            });
        }
    }
}
