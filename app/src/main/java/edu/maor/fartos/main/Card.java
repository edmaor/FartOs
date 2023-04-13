package edu.maor.fartos.main;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.maor.fartos.R;

public class Card implements Serializable {
    public enum Effect {
        AMBLE(28, R.string.amble, R.string.amble_desc, R.drawable.card_amble_1, R.drawable.card_amble_2, R.drawable.card_amble_3),
        JOGGING(18, R.string.jogging, R.string.jogging_desc, R.drawable.card_jogging_1, R.drawable.card_jogging_2),
        SPRINT(10, R.string.sprint, R.string.sprint_desc, R.drawable.card_sprint_1, R.drawable.card_sprint_2),
        SWITCHER(3, R.string.switcher, R.string.switcher_desc, R.drawable.card_switcher_1, R.drawable.card_switcher_2),
        HINDER(4, R.string.hinder, R.string.hinder_desc, R.drawable.card_hinder_1, R.drawable.card_hinder_2, R.drawable.card_hinder_3),
        INTIMIDATION(3, R.string.intimidation, R.string.intimidation_desc, R.drawable.card_intimidation_1),
        KNOCK_BACK(2, R.string.knock_back, R.string.knock_back_desc , R.drawable.card_knock_back),
        TRICKER(2, R.string.tricker, R.string.tricker_desc, R.drawable.card_tricker);
        private final int count;
        private final int name;
        private final int description;
        private final int[] sprites;

        Effect(int count, int name, int description, int ... sprites) {
            this.count = count;
            this.name = name;
            this.description = description;
            this.sprites = sprites;
        }
        public int getName() {
            return name;
        }
        public int getCount() { return count; }
        public int getDescription() {
            return description;
        }
        public int[] getSprites() {
            return sprites;
        }
    }
    private final Effect effect;
    private final int cardSprite;

    private Card(Effect effect) {
        this.effect = effect;
        int[] sprites = getSprites();
        cardSprite = sprites[(int) (Math.random() * sprites.length)];
    }

    public Effect getEffect() {return effect;}
    public int getName() {return effect.name;}
    public int getSprite() {
        return cardSprite;
    }
    public int getDescription() {
        return effect.getDescription();
    }

    public int[] getSprites() {
        return effect.getSprites();
    }

    public static List<Card> generateDeck() {
        ArrayList<Card> deck = new ArrayList<>(70);
        for (Effect e : Effect.values()) {
            for (int i = 0; i < e.getCount(); i++)
                deck.add(new Card(e));
        }
        Collections.shuffle(deck);
        return deck;
    }
}
