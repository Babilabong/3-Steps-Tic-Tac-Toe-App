package com.babila.tic_tac_toeapp;

import androidx.appcompat.app.AlertDialog;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class gamePage extends AppCompatActivity {

    private GameBoard gameBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        TextView playerTurnText = findViewById(R.id.textView5);
        boolean flag = getIntent().getBooleanExtra("threeStepsGameFlag",false);
        boolean opponentFlag = getIntent().getBooleanExtra("opponent", false);
        boolean easyLevel = false;
        if(opponentFlag)
            easyLevel = getIntent().getBooleanExtra("easyLevel", false);

        GameBoard gameBoard = findViewById(R.id.gameBoard2);
        gameBoard.setUp(playerTurnText, flag, opponentFlag, easyLevel, this);

    }

    public void showWinnerDialog(int winner, int xPoint, int oPoint){
        String winnerText;
        if(winner == -1){
            winnerText = "Tie Game";
        } else if (winner%2 == 0) {
            winnerText = "X Win The Game";
        }
        else{
            winnerText = "O Win The Game";
        }

        new AlertDialog.Builder(this)
                .setTitle(winnerText)
                .setMessage("X points = " + xPoint + "\nO points = " + oPoint)
                .setPositiveButton("Play Again", (dialog, which) -> dialog.dismiss())
                .setCancelable(false)
                .show();
    }

}