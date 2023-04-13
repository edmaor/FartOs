package edu.maor.fartos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import edu.maor.fartos.main.Card;
import edu.maor.fartos.main.CardHolder;
import edu.maor.fartos.main.Player;
import edu.maor.fartos.main.PlayerHolder;

public class FartOS extends AppCompatActivity {
    public static List<Player> players;
    private static List<CardHolder> cardHolders;

    // Map
    ViewGroup mapLayout;
        // Tiles, PlayerHolders
    private static final int[] rTiles = {R.id.tile_goal, R.id.tile_1, R.id.tile_2, R.id.tile_3, R.id.tile_4, R.id.tile_5, R.id.tile_6};
    private final RecyclerView[] tiles = new RecyclerView[rTiles.length];
    private static final PlayerHolder[] playerHolders = new PlayerHolder[rTiles.length];

    // Utils
    static RecyclerView handRecyclerView;
    public static int round = 0, turn = 0;
    private static boolean[] cardsLeft;
    Player winner;
    private static boolean special = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fart_os);
        getSupportActionBar().hide();

        handleInit();
    }
    @Override
    protected void onStart() {
        super.onStart();
        startFartOS();
    }

    // Handlers
    private void handleInit() {
        // Initialise card holder
        handRecyclerView = findViewById(R.id.card_holder);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        handRecyclerView.setLayoutManager(layoutManager);

        // Initialise Map and map recyclers
        mapLayout = findViewById(R.id.map_layout);
        for (int i = 0; i < rTiles.length; i++) {
            tiles[i] = mapLayout.findViewById(rTiles[i]);
            LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            playerHolders[i] = new PlayerHolder();
            tiles[i].setLayoutManager(llm);
            tiles[i].setAdapter(playerHolders[i]);
        }
        startFartOS();
    }

    // Utility methods -> checkSpecial, checkWin, handlePosition
    private boolean checkWin() {
        // Check lasting players
        if (players.size() == 1) {
            winner = players.get(0);
            return true;
        }

        // Check for any player on position 14
        for (Player player : players) {
            if (player.position == 14) {
                winner = player;
                return true;
            }
        }

        // Any other return false
        return false;
    }
    private static int handlePosition(int position) {
        // If player passed save house
        if (position > 7 ) position = position - (position - 7) * 2;
        return position;
    }

    // Game methods - FartOS, Rounds, Turns, Cards, NextPlayer, Move
    private void startFartOS() {
        // Get intent to know the number of players
        Player.gid = 0;
        turn = -1;
        Intent i = getIntent();
        int playerCount = i.getIntExtra("PLAYER_COUNT", 3);

        // Initialise players and cardHolders
        players = new ArrayList<>(playerCount);
        cardHolders = new ArrayList<>(playerCount);
        cardsLeft = new boolean[playerCount];
        for (int pc = 0; pc < playerCount; pc++) {
            players.add(pc, new Player());
            cardHolders.add(pc, new CardHolder(players.get(pc), getSupportFragmentManager()));
            playerHolders[0].addPlayer(players.get(pc));
        }

        // Rounds
        for (int r = 0; r < 5; r++) {
            round = r;
            playRound();
        }
        // Set winner the most advanced player
        winner = players.get(0);
        for (Player player : players) {
            if (winner.position < player.position) winner = player;
        }
    }
    private static void nextPlayer(){
        turn ++;
        if (turn >= players.size()) turn = 0;

        CardHolder cardHolder = cardHolders.get(turn);
        cardHolder.updateHand();
        if (cardHolder.getItemCount() <= 0) {
            cardsLeft[turn] = false;
            int counter = 0;
            for (boolean cl : cardsLeft) {
                if (cl) break;
                counter++;
            }
            if (counter == players.size()) playRound();
            nextPlayer();
        }
        handRecyclerView.setAdapter(cardHolder);
    }
    private static void playRound() {
        // Initialise deck and give hands
        int cardCount = 6;
        if (players.size() >= 5) cardCount = 5;
        List<Card> deck = Card.generateDeck();
        for (int p = 0; p < players.size(); p++) {
            List<Card> hand = deck.subList(p*cardCount, p*cardCount+cardCount);
            players.get(p).setHand(hand);
            cardHolders.get(p).updateHand();
            cardsLeft[p] = true;
        }
        nextPlayer();
    }
    public static void playCard(int p1, int p2, Card card) {
        int auxiliar = -1;
        boolean valid = false;
        if (p1 == p2) auxiliar = 1;

        Player pl1 = null, pl2 = null;
        for (Player player : players) {
            if (player.id == p1) pl1 = player;
            if (player.id == p2) pl2 = player;
        }
        assert pl1 != null && pl2 != null;

        // Adapt movement if is intimidated
        int movement = pl2.isIntimidated();
        if (movement == -1 ) movement += 1;

        switch (card.getEffect()) {
            case AMBLE:
                movement += 1;
                valid = move(pl2, movement);
                break;
            case JOGGING:
                movement += 2;
                valid = move(pl2, auxiliar * movement);
                break;
            case SPRINT:
                movement += 3;
                valid = move(pl2, auxiliar * movement);
                break;
            case INTIMIDATION:
                // Attacked player
                if (auxiliar == -1) {
                    pl2.intimidate();
                    valid = true;
                }
                break;
            case KNOCK_BACK:
                // Todo : not showing player on tile 0 when going back
                // Goes back to the last save tile, cannot be self
                if (auxiliar == -1) {
                    int pos = pl2.position;
                    if (pos > 7) move(pl2, 7-pos);
                    else move(pl2, -pos);
                    valid = true;
                }
                break;
            case SWITCHER:
                // Switches positions between two players, cannot be self
                if (auxiliar == -1) {
                    switcher(pl1, pl2);
                    valid = true;
                }
                break;
            case TRICKER:
                // Switches hands between two players, cannot be self
                if (auxiliar == -1) {
                    pl1.getHand().remove(card);
                    List<Card> hand = pl1.getHand();
                    pl1.setHand(pl2.getHand());
                    pl2.setHand(hand);
                    valid = true;
                }
                break;
            case HINDER:
                // Objective looses a card, cannot be self
                if (auxiliar == -1) {
                    pl2.discardOne();
                    valid = true;
                }
                break;
        }

        if (valid) {
            pl1.getHand().remove(card);
            nextPlayer();
        }
    }

    public static void switcher(Player pl1, Player pl2) {
        int p1Pos = pl1.position, p2Pos = pl2.position;


        // Check that the players have a different starting position
        if (p1Pos != p2Pos) {
            // Switch positions
            int auxiliary = pl1.position;
            pl1.position = pl2.position;
            pl2.position = auxiliary;

            // Handle positions
            p1Pos = handlePosition(p1Pos);
            p2Pos = handlePosition(p2Pos);

            // Handle recyclerview positions and update views
            playerHolders[p2Pos].addPlayer(pl1);
            playerHolders[p2Pos].notifyItemInserted(playerHolders[p2Pos].getItemCount()-1);
            playerHolders[p1Pos].removePlayer(pl1);
            playerHolders[p1Pos].notifyDataSetChanged();

            playerHolders[p1Pos].addPlayer(pl2);
            playerHolders[p1Pos].notifyItemInserted(playerHolders[p1Pos].getItemCount()-1);
            playerHolders[p2Pos].removePlayer(pl2);
            playerHolders[p2Pos].notifyDataSetChanged();

        }
    }

    public static boolean move(Player player, int count) {
        boolean valid = false;
        // Positions --> player | temporal
        int pPos = player.position;
        pPos = handlePosition(pPos);
        int tPos = pPos;

        // Move
        tPos += count;

        // Validate player position
        if (tPos < 0) tPos = 0;
        // If player moves further from the special tile, movement is invalid
        if ((pPos < 7 && tPos > 7) || (pPos > 7 && tPos < 7)) return false;

        // Validate movement
        int pos = tPos;
        tPos = handlePosition(tPos);
        // Check that the player has moved
        if (pos != player.position) {
            // Check if new position is a non-limited tile
            if (tPos == 0) {
                valid = true;
                playerHolders[tPos].addPlayer(player);
            }
            // If player enters the special tile
            else if (tPos == 7) {
                valid = true;
                if (!special) special = true;
                // Todo : call special movement
            }
            // Check number of players in the tile
            else if (playerHolders[tPos].getItemCount() < 2) {
                valid = true;
                playerHolders[tPos].addPlayer(player);
                playerHolders[tPos].notifyItemInserted(playerHolders[tPos].getItemCount()-1);
            }

            if (valid) {
                if (player.position != 7) {
                    playerHolders[pPos].removePlayer(player);
                    playerHolders[pPos].notifyDataSetChanged();
                }
                player.position = pos;
            }

        }
        return valid;
    }

}