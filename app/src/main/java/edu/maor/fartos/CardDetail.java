package edu.maor.fartos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.maor.fartos.main.Card;
import edu.maor.fartos.main.CardDetailPlayerHolder;

public class CardDetail extends DialogFragment {
    TextView name, description;
    ImageView sprite;
    Button btnAtack;
    RecyclerView players;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.card_detail, null);

        builder.setView(v);

        name = v.findViewById(R.id.card_detail_name);
        sprite = v.findViewById(R.id.card_detail_image);
        description = v.findViewById(R.id.card_detail_description);
        btnAtack = v.findViewById(R.id.cd_atack);
        players = v.findViewById(R.id.cd_players);

        assert getArguments() != null;
        Card card = (Card) getArguments().get("CARD");
        int playerId = getArguments().getInt("PLAYER");
        CardDetailPlayerHolder ph = new CardDetailPlayerHolder(FartOS.players);

        handlePlayers(ph, getContext());

        name.setText(card.getName());
        sprite.setImageResource(card.getSprite());
        description.setText(card.getDescription());

        btnAtack.setOnClickListener(a -> {
            int i = ph.playerId;
            if (i != -1) FartOS.playCard(playerId, i, card );
            this.dismiss();
        });

        return builder.create();
    }

    private void handlePlayers(CardDetailPlayerHolder ph, Context context) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        players.setLayoutManager(layoutManager);
        players.setAdapter(ph);
    }
}
