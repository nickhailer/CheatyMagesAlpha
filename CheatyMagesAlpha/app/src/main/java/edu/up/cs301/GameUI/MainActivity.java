package edu.up.cs301.GameUI;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import edu.up.cs301.game.R;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CMInterface theView = (CMInterface)findViewById(R.id.surfaceView);

        Button passButton = (Button)findViewById(R.id.passButton);
        passButton.setOnClickListener(theView);

        Button betButton = (Button)findViewById(R.id.betButton);
        betButton.setOnClickListener(theView);

        Button detectMagicButton = (Button)findViewById(R.id.detectMagicButton);
        detectMagicButton.setOnClickListener(theView);

        Button discardCardsButton = (Button)findViewById(R.id.discardCardsButton);
        discardCardsButton.setOnClickListener(theView);


    }

}
