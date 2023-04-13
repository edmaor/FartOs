package edu.maor.fartos;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button[] playercBtn = new Button[4];
    int[] buttons = {R.id.bnt_p3, R.id.bnt_p4, R.id.bnt_p5, R.id.bnt_p6};

    Button start;

    int playerCount = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handleInit();
    }

    private void handleInit(){
        int np = 3;
        for (int i = 0; i < 4; i++) {
            int fnp = np;
            playercBtn[i] = findViewById(buttons[i]);
            playercBtn[i].setOnClickListener(v -> handlePlayerCount(fnp));
            np++;
        }

        start = findViewById(R.id.btn_start);
        start.setOnClickListener(v -> {
            Intent i = new Intent(this, FartOS.class);
            i.putExtra("PLAYER_COUNT", playerCount);
            startActivity(i);
        });
    }

    @SuppressLint("ResourceAsColor")
    private void handlePlayerCount(int np) {
        playercBtn[playerCount-3].setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.theme_button)));
        playerCount = np;
        playercBtn[playerCount-3].setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.theme_blue)));
    }
}