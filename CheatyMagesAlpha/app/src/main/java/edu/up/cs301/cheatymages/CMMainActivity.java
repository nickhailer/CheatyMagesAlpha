package edu.up.cs301.cheatymages;

import android.os.Bundle;
import android.widget.Button;

import edu.up.cs301.GameFramework.GameMainActivity;
import edu.up.cs301.GameFramework.LocalGame;
import edu.up.cs301.GameFramework.gameConfiguration.GameConfig;
import edu.up.cs301.GameFramework.infoMessage.GameState;
import edu.up.cs301.cheatymages.CMInterface;
import edu.up.cs301.game.R;


public class CMMainActivity extends GameMainActivity {

    @Override
    public GameConfig createDefaultConfig() {
        return null;
    }

    @Override
    public LocalGame createLocalGame(GameState gameState) {
        return null;
    }

    /*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CMInterface theView = (CMInterface) findViewById(R.id.surfaceView);

        Button passButton = (Button) findViewById(R.id.passButton);
        //passButton.setOnClickListener(theView);

        Button betButton = (Button) findViewById(R.id.betButton);
        //betButton.setOnClickListener(theView);

        Button detectMagicButton = (Button) findViewById(R.id.detectMagicButton);
        //detectMagicButton.setOnClickListener(theView);

        Button discardCardsButton = (Button) findViewById(R.id.discardCardsButton);
        //discardCardsButton.setOnClickListener(theView);

        Button fighterC1 = (Button) findViewById(R.id.f1Button);
        Button fighterC2 = (Button) findViewById(R.id.f2Button);
        Button fighterC3 = (Button) findViewById(R.id.f3Button);
        Button fighterC4 = (Button) findViewById(R.id.f4Button);
        Button fighterC5 = (Button) findViewById(R.id.f5Button);
    }

     */

}
