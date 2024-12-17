package com.babila.tic_tac_toeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        boolean flag = true;
        new Thread(
                () -> {
                    // Initialize the Google Mobile Ads SDK on a background thread.
                    MobileAds.initialize(this, initializationStatus -> {});
                })
                .start();
    }

    public void playRegularGame(View view){
        playGame(view, false, false);
    }

    public void play3StepsGame(View view){
        playGame(view, true, false);
    }

    public void playRegularGameWithOpponentEasy(View view){
        playGame(view, false, true, true);
    }

    public void playRegularGameWithOpponentHard(View view){
        playGame(view, false, true, false);
    }

    private void playGame(View view, boolean threeStepsGameFlag, boolean Opponent){
        Intent intent = new Intent(this, gamePage.class);
        intent.putExtra("threeStepsGameFlag", threeStepsGameFlag);
        intent.putExtra("opponent", Opponent);
        startActivity(intent);
    }

    private void playGame(View view, boolean threeStepsGameFlag, boolean Opponent, boolean easyLevel){
        Intent intent = new Intent(this, gamePage.class);
        intent.putExtra("threeStepsGameFlag", threeStepsGameFlag);
        intent.putExtra("opponent", Opponent);
        intent.putExtra("easyLevel", easyLevel);
        startActivity(intent);
    }

    private Switch mySwitch;
    private boolean switchState = true;
}