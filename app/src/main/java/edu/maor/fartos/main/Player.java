package edu.maor.fartos.main;

import java.util.ArrayList;
import java.util.List;

import edu.maor.fartos.R;

public class Player {
    public static int gid;
    private final static int[] sprites = {R.drawable.player_1, R.drawable.player_2, R.drawable.player_3,
            R.drawable.player_4, R.drawable.player_5, R.drawable.player_6};
    public int id;
    private List<Card> hand = new ArrayList<>();
    private final int playerSprite;
    public int position = 0;
    public boolean intimidated = false;

    public Player() {
        id = gid;
        this.playerSprite = sprites[id];
        gid += 1;
    }

    // Hand actions
    public Card discardOne(){
        int s = hand.size();
        return hand.remove((int)(Math.random() * s));
    }
    public void setHand(List<Card> hand) {
        this.hand = new ArrayList<>(hand);
    }
    public List<Card> getHand() {
        return hand;
    }

    public int getPlayer_sprite() {
        return playerSprite;
    }

    public int isIntimidated() {
        if (intimidated) return -1;
        else return 0;
    }
    public void intimidate() {
        this.intimidated = !this.intimidated;
    }
}
