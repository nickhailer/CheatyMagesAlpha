package edu.up.cs301.game.cheatymages;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import edu.up.cs301.game.GameFramework.GameMainActivity;
import edu.up.cs301.game.GameFramework.LocalGame;
import edu.up.cs301.game.GameFramework.gameConfiguration.GameConfig;
import edu.up.cs301.game.GameFramework.gameConfiguration.GamePlayerType;
import edu.up.cs301.game.GameFramework.infoMessage.GameState;
import edu.up.cs301.game.GameFramework.players.GamePlayer;
import edu.up.cs301.game.R;
import edu.up.cs301.game.cheatymages.Players.*;


public class CMMainActivity extends GameMainActivity {

    CMGameState gameState = new CMGameState(3);

    protected int currGold = 2;
    protected int p2currGold = 2;
    protected int p3currGold = 2;

    protected int p2currBets = 0;
    protected int p3currBets = 0;

    protected int p2currHandSize = 0;
    protected int p3currHandSize = 0;


    Button[] cardButtons;

    /**
     *  Creates a default configuration of the game
     * @return default configuration
     */
    @Override
    public GameConfig createDefaultConfig() {

        ArrayList<GamePlayerType> playerTypes = new ArrayList<>();

        playerTypes.add(new GamePlayerType("Human Player") {
            @Override
            public GamePlayer createPlayer(String name) {
                return new CMHumanPlayer(name);
            }
        });

        playerTypes.add(new GamePlayerType("Computer Player Dumb") {
            @Override
            public GamePlayer createPlayer(String name) {
                return new CMComputerPlayerDumb(name);
            }
        });

        //TODO ONCE IMPLEMENTED ADD OTHER PLAYER TYPES HERE

        GameConfig defaultConfig = new GameConfig(playerTypes, 3,6, "Cheaty Mages", 5213);

        //TODO CHANGE THIS TO BE 1 HUMAN PLAYER AND 2 SMART AIS ONCE IMPLEMENTED
        defaultConfig.addPlayer("P1", 0);
        defaultConfig.addPlayer("P2", 1);
        defaultConfig.addPlayer("P3", 1);

        defaultConfig.setRemoteData("Remote Player", "", 0);

        return defaultConfig;
    }

    /**
     *
     * @param gameState
     *              The desired gameState to start at or null for new game
     *
     * @return
     */
    @Override
    public LocalGame createLocalGame(GameState gameState) {
        //TODO FIX THIS I DONT KNOW HOW BUT IT WILL ALWAYS ASSUME NEW GAMES HAVE 3 PLAYERS
        if(gameState == null){
            return new CMLocalGame(3);
        }
        return new CMLocalGame(((CMGameState)gameState).getNumPlayers());
    }

    /**
     * Sets up all the buttons and updates the TextViews and Buttons
     * according to the CMGameState
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        CMInterface theView = (CMInterface) findViewById(R.id.surfaceView);
        Button passButton = (Button) findViewById(R.id.passButton);
        Button betButton = (Button) findViewById(R.id.betButton);
        Button detectMagicButton = (Button) findViewById(R.id.detectMagicButton);
        Button discardCardsButton = (Button) findViewById(R.id.discardCardsButton);
        Button playSpellButton = (Button) findViewById(R.id.playSpellButton);

        // Setting all fighter buttons id
        Button fighterC1 = (Button) findViewById(R.id.f1Button);
        Button fighterC2 = (Button) findViewById(R.id.f2Button);
        Button fighterC3 = (Button) findViewById(R.id.f3Button);
        Button fighterC4 = (Button) findViewById(R.id.f4Button);
        Button fighterC5 = (Button) findViewById(R.id.f5Button);



        // Setting all Spell Card buttons id
        Button spellC1 = (Button) findViewById(R.id.spellC1);
        Button spellC2 = (Button) findViewById(R.id.spellC2);
        Button spellC3 = (Button) findViewById(R.id.spellC3);
        Button spellC4 = (Button) findViewById(R.id.spellC4);
        Button spellC5 = (Button) findViewById(R.id.spellC5);
        Button spellC6 = (Button) findViewById(R.id.spellC6);
        Button spellC7 = (Button) findViewById(R.id.spellC7);
        Button spellC8 = (Button) findViewById(R.id.spellC8);



        //array of all the buttons
        cardButtons = new Button[] {spellC1, spellC2, spellC3, spellC4, spellC5, spellC6, spellC7, spellC8};

        // Setting up player/game information id
        TextView roundNum = (TextView) findViewById(R.id.roundNum);
        TextView goldTotal = (TextView) findViewById(R.id.yourGold);
        TextView otherPlayerNum = (TextView) findViewById(R.id.playerNum);

        // Setting up other players gold, bets, and handsize TextViews id
        TextView p2Gold= (TextView) findViewById(R.id.p2Gold);
        TextView p3Gold= (TextView) findViewById(R.id.p3Gold);
        TextView p2Bets = (TextView) findViewById(R.id.p2Bets);
        TextView p3Bets = (TextView) findViewById(R.id.p3Bets);
        TextView p2HandSize= (TextView) findViewById(R.id.p2HandSize);
        TextView p3HandSize= (TextView) findViewById(R.id.p3HandSize);


        // Updating the roundNumber
        if(gameState.getRoundNum() == 1) {
            roundNum.setText("Round 1");
        }
        else if(gameState.getRoundNum() == 2) {
            roundNum.setText("Round 2");
        }
        else if(gameState.getRoundNum() ==3) {
            roundNum.setText("Round 3");
        }

        // Updating your gold total
        if(gameState.getGold()[0] > currGold) {
            currGold = gameState.getGold()[0];
            goldTotal.setText(currGold);
        }

        /////////Updating the number of players on opponent info table/////////

        // Updating the opponents gold
        if(gameState.getGold()[1] > p2currGold) {
            p2currGold = gameState.getGold()[1];
            p2Gold.setText(p2currGold);
        }
        if(gameState.getGold()[2] > p3currGold) {
            p3currGold = gameState.getGold()[2];
            p3Gold.setText(p3currGold);
        }

        // Updating opponents handSize
        if(gameState.getHands()[1].size() != p2currHandSize) {
            p2currHandSize = gameState.getHands()[1].size();
            p2HandSize.setText(p2currHandSize);
        }
        if(gameState.getHands()[2].size() != p3currHandSize) {
            p3currHandSize = gameState.getHands()[2].size();
            p3HandSize.setText(p3currHandSize);
        }

        // Updating opponents Bets
        if(gameState.getBets()[1].get(1) != p2currBets) {
            p2currBets = gameState.getBets()[1].get(1);
            p2Bets.setText(p2currBets);
        }
        if(gameState.getBets()[2].get(2) != p3currBets) {
            p3currBets = gameState.getBets()[2].get(2);
            p3Bets.setText(p3currBets);
        }

        // Set all spell card buttons to be invisible
        for(int i=0; i<cardButtons.length; i++) {
            cardButtons[i].setVisibility(View.INVISIBLE);
        }
        // Create a for loop to run number of cards amount of times setting them visible
        for(int i=0; i<gameState.getHands().length; i++) {
            cardButtons[i].setVisibility(View.VISIBLE);
        }

    }



}
