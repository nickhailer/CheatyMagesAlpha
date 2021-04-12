package edu.up.cs301.game.cheatymages.Players;

import android.view.View;

import java.util.ArrayList;

import edu.up.cs301.game.GameFramework.GameMainActivity;
import edu.up.cs301.game.GameFramework.infoMessage.*;
import edu.up.cs301.game.GameFramework.players.GameHumanPlayer;
import edu.up.cs301.game.GameFramework.utilities.Logger;
import edu.up.cs301.game.R;
import edu.up.cs301.game.cheatymages.Actions.*;
import edu.up.cs301.game.cheatymages.CMGameState;
import edu.up.cs301.game.cheatymages.CMSurfaceView;

public class CMHumanPlayer extends GameHumanPlayer implements View.OnClickListener{

    //0 = normal, 1 = selecting target for a spell card
    //2 = selecting spell for detect magic, 3 = selecting target for detect magic
    //4 = betting, 5 = discarding
    protected int gamePhase;

    // boolean ArrayList checks if fighter has bet on them
    protected boolean[] selectedFighters = new boolean[5];

    protected int[] lastClicked = new int[3];

    // the index of the spell in their hand
    protected int spell;

    // ArrayList of indices of the cards the player wishes to discard
    protected boolean[] selectedSpells = new boolean[8];

    private int layoutId;

    //TODO IMPLEMENT THE SURFACE VIEW
    private CMSurfaceView surfaceView;

    /**
     * constructor
     *
     * @param name the name of the player
     */
    public CMHumanPlayer(String name, int layoutId) {
        super(name);
        this.layoutId = layoutId;
    }

    @Override
    public View getTopView() {
        return null;
    }

    @Override
    public void receiveInfo(GameInfo info) {

        if (surfaceView == null) return;

        if (info instanceof IllegalMoveInfo || info instanceof NotYourTurnInfo) {
            // if the move was out of turn or otherwise illegal, flash the screen
            //TODO IMPLEMENT A FLASH METHOD
            //surfaceView.flash(Color.RED, 50);
        }
        else if (!(info instanceof CMGameState))
            // if we do not have a TTTState, ignore
            return;
        else {
            //TODO THIS MIGHT BREAK STUFF
            if(((CMGameState) info).getPlayerTurn() == -1){
               gamePhase = 4;
            }
            else if(((CMGameState) info).getPlayerTurn() == -2){
                gamePhase = 5;
            }
            else{
                gamePhase = 0;
            }
            surfaceView.setState((CMGameState)info, this.playerNum);
            surfaceView.invalidate();
        }
    }

    @Override
    public void setAsGui(GameMainActivity activity) {
        // Load the layout resource for the new configuration
        activity.setContentView(layoutId);

        // set the surfaceView instance variable
        surfaceView = (CMSurfaceView) myActivity.findViewById(R.id.surfaceView);
        surfaceView.setOnClickListener(this);
    }

    @Override
    public void onClick(View button) {
        if(button.getId() == R.id.betButton){
            if(gamePhase != 4){
                return;
            }
            ArrayList<Integer> bets = new ArrayList<>();
            for(int i = 0; i < 5; i++){
                if(selectedFighters[i]){
                    bets.add(i);
                }
            }
            this.game.sendAction(new BetAction(this, bets));
            return;
        }
        if(button.getId() == R.id.passButton){
            if(gamePhase == 4 || gamePhase == 5){
                return;
            }
            this.game.sendAction(new PassAction(this));
            return;
        }
        if(button.getId() == R.id.f1Button){
            if(gamePhase == 1) {
                this.game.sendAction(new PlaySpellAction(this, spell, 0));
                return;
            }
            if(gamePhase == 3) {
                this.game.sendAction(new DetectMagicAction(this, spell, 0));
            }
            if(gamePhase == 4) {
                int numBets = 0;
                for(int i = 0; i < 5; i++) {
                    if (selectedFighters[i]) {
                        numBets++;
                    }
                }
                if(numBets > 2) {
                    selectedFighters[lastClicked[2]] = false;
                }
                selectedFighters[0] = true;
                lastClicked[2] = lastClicked[1];
                lastClicked[1] = lastClicked[0];
                lastClicked[0] = 0;
            }
        }
        if(button.getId() == R.id.f2Button){
            if(gamePhase == 1) {
                this.game.sendAction(new PlaySpellAction(this, spell, 1));
                return;
            }
            if(gamePhase == 3) {
                this.game.sendAction(new DetectMagicAction(this, spell, 1));
            }
            if(gamePhase == 4) {
                int numBets = 0;
                for(int i = 0; i < 5; i++) {
                    if (selectedFighters[i]) {
                        numBets++;
                    }
                }
                if(numBets > 2) {
                    selectedFighters[lastClicked[2]] = false;
                }
                selectedFighters[1] = true;
            }
        }
        if(button.getId() == R.id.f3Button){
            if(gamePhase == 1) {
                this.game.sendAction(new PlaySpellAction(this, spell, 2));
                return;
            }
            if(gamePhase == 3) {
                this.game.sendAction(new DetectMagicAction(this, spell, 2));
            }
            if(gamePhase == 4) {
                int numBets = 0;
                for(int i = 0; i < 5; i++) {
                    if (selectedFighters[i]) {
                        numBets++;
                    }
                }
                if(numBets > 2) {
                    selectedFighters[lastClicked[2]] = false;
                }
                selectedFighters[2] = true;
            }
        }
        if(button.getId() == R.id.f4Button){
            if(gamePhase == 1) {
                this.game.sendAction(new PlaySpellAction(this, spell, 3));
                return;
            }
            if(gamePhase == 3) {
                this.game.sendAction(new DetectMagicAction(this, spell, 3));
            }
            if(gamePhase == 4) {
                int numBets = 0;
                for(int i = 0; i < 5; i++) {
                    if (selectedFighters[i]) {
                        numBets++;
                    }
                }
                if(numBets > 2) {
                    selectedFighters[lastClicked[2]] = false;
                }
                selectedFighters[3] = true;
            }
        }
        if(button.getId() == R.id.f5Button){
            if(gamePhase == 1) {
                this.game.sendAction(new PlaySpellAction(this, spell, 4));
                return;
            }
            if(gamePhase == 3) {
                this.game.sendAction(new DetectMagicAction(this, spell, 4));
            }
            if(gamePhase == 4) {
                int numBets = 0;
                for(int i = 0; i < 5; i++) {
                    if (selectedFighters[i]) {
                        numBets++;
                    }
                }
                if(numBets > 2) {
                    selectedFighters[lastClicked[2]] = false;
                }
                selectedFighters[4] = true;
            }
        }
        if(button.getId() == R.id.detectMagicButton){
            if(gamePhase == 4 || gamePhase == 5){
                return;
            }
            gamePhase = 2;
        }
        if(button.getId() == R.id.spellC1){
            if(gamePhase == 0){
                spell = 0;
                gamePhase = 1;
                return;
            }
            if(gamePhase == 2){
                spell = 0;
                gamePhase = 3;
                return;
            }
            if(gamePhase == 5){
                if(selectedSpells[0]){
                    selectedSpells[0] = false;
                    return;
                }
                selectedSpells[0] = true;
            }
        }
        if(button.getId() == R.id.spellC2){
            if(gamePhase == 0){
                spell = 1;
                gamePhase = 1;
                return;
            }
            if(gamePhase == 2){
                spell = 1;
                gamePhase = 3;
                return;
            }
            if(gamePhase == 5){
                if(selectedSpells[1]){
                    selectedSpells[1] = false;
                    return;
                }
                selectedSpells[1] = true;
            }
        }
        if(button.getId() == R.id.spellC3){
            if(gamePhase == 0){
                spell = 2;
                gamePhase = 1;
                return;
            }
            if(gamePhase == 2){
                spell = 2;
                gamePhase = 3;
                return;
            }
            if(gamePhase == 5){
                if(selectedSpells[2]){
                    selectedSpells[2] = false;
                    return;
                }
                selectedSpells[2] = true;
            }
        }
        if(button.getId() == R.id.spellC4){
            if(gamePhase == 0){
                spell = 3;
                gamePhase = 1;
                return;
            }
            if(gamePhase == 2){
                spell = 3;
                gamePhase = 3;
                return;
            }
            if(gamePhase == 5){
                if(selectedSpells[3]){
                    selectedSpells[3] = false;
                    return;
                }
                selectedSpells[3] = true;
            }
        }
        if(button.getId() == R.id.spellC5){
            if(gamePhase == 0){
                spell = 4;
                gamePhase = 1;
                return;
            }
            if(gamePhase == 2){
                spell = 4;
                gamePhase = 3;
                return;
            }
            if(gamePhase == 5){
                if(selectedSpells[4]){
                    selectedSpells[4] = false;
                    return;
                }
                selectedSpells[4] = true;
            }
        }
        if(button.getId() == R.id.spellC6){
            if(gamePhase == 0){
                spell = 5;
                gamePhase = 1;
                return;
            }
            if(gamePhase == 2){
                spell = 5;
                gamePhase = 3;
                return;
            }
            if(gamePhase == 5){
                if(selectedSpells[5]){
                    selectedSpells[5] = false;
                    return;
                }
                selectedSpells[5] = true;
            }
        }
        if(button.getId() == R.id.spellC7){
            if(gamePhase == 0){
                spell = 6;
                gamePhase = 1;
                return;
            }
            if(gamePhase == 2){
                spell = 6;
                gamePhase = 3;
                return;
            }
            if(gamePhase == 5){
                if(selectedSpells[6]){
                    selectedSpells[6] = false;
                    return;
                }
                selectedSpells[6] = true;
            }
        }
        if(button.getId() == R.id.spellC8){
            if(gamePhase == 0){
                spell = 7;
                gamePhase = 1;
                return;
            }
            if(gamePhase == 2){
                spell = 7;
                gamePhase = 3;
                return;
            }
            if(gamePhase == 5){
                if(selectedSpells[7]){
                    selectedSpells[7] = false;
                    return;
                }
                selectedSpells[7] = true;
            }
        }
        if(button.getId() == R.id.discardCardsButton){
            if(gamePhase != 5){
                return;
            }
            ArrayList<Integer> discards = new ArrayList<>();
            for(int i = 8; i >= 0; i--){
                if(selectedSpells[i]){
                    discards.add(i);
                }
            }
            game.sendAction(new DiscardCardsAction(this, discards));
        }
    }
}