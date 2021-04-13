package edu.up.cs301.game.cheatymages.Players;

import android.util.Log;
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

    // keeps track of what turn it was the last time you were updated information
    protected int playerTurn;

    // boolean ArrayList checks if fighter has bet on them
    protected boolean[] selectedFighters = new boolean[5];

    protected boolean detectMagic;

    protected int[] lastClicked = new int[3];

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

        if (surfaceView == null){
            return;
        }

        if (info instanceof IllegalMoveInfo || info instanceof NotYourTurnInfo) {
            // if the move was out of turn or otherwise illegal, flash the screen
            //TODO IMPLEMENT A FLASH METHOD
            //surfaceView.flash(Color.RED, 50);
        }
        else if (!(info instanceof CMGameState)) {
            // if we do not have a TTTState, ignore
            return;
        }
        else {
            surfaceView.setState((CMGameState) info, this.playerNum);
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
        int buttonId = button.getId();
        if(buttonId == R.id.betButton){
            ArrayList<Integer> bets = new ArrayList<>();
            for(int i = 0; i < 5; i++){
                if(selectedFighters[i]){
                    bets.add(i);
                }
            }
            this.game.sendAction(new BetAction(this, bets));
        }
        else if(buttonId == R.id.passButton){
            this.game.sendAction(new PassAction(this));
            detectMagic = false;
        }
        else if(buttonId == R.id.detectMagicButton){
            detectMagic = true;
            //TODO UNCOMMENT THIS ONCE IMPLEMENT
            //surfaceView.selectDetectMagic()
        }
        else if(buttonId == R.id.discardCardsButton){
            ArrayList<Integer> discards = new ArrayList<>();
            for(int i = 8; i >= 0; i--){
                if(selectedSpells[i]){
                    discards.add(i);
                }
            }
            game.sendAction(new DiscardCardsAction(this, discards));
            detectMagic = false;
        }
        else if(buttonId == R.id.f1Button){
            clickFighter(0);
        }
        else if(buttonId == R.id.f2Button){
            clickFighter(1);
        }
        else if(buttonId == R.id.f3Button){
            clickFighter(2);
        }
        else if(buttonId == R.id.f4Button){
            clickFighter(3);
        }
        else if(buttonId == R.id.f5Button){
            clickFighter(4);
        }
        else if(buttonId == R.id.spellC1){
            clickSpell(0);
        }
        else if(buttonId == R.id.spellC2){
            clickSpell(1);
        }
        else if(buttonId == R.id.spellC3){
            clickSpell(2);
        }
        else if(buttonId == R.id.spellC4){
            clickSpell(3);
        }
        else if(buttonId == R.id.spellC5){
            clickSpell(4);
        }
        else if(buttonId == R.id.spellC6){
            clickSpell(5);
        }
        else if(buttonId == R.id.spellC7){
            clickSpell(6);
        }
        else if(buttonId == R.id.spellC8){
            clickSpell(7);
        }
        else{
            Log.d("CMHumanPlayer", "Unknown Button ID");
        }
    }

    private void clickFighter(int idx){
        int numSelectedSpells = 0;
        int spell = -1;
        for(int i = 0; i < selectedSpells.length; i++){
            if(selectedSpells[i]){
                spell = i;
                numSelectedSpells++;
            }
        }

        if(numSelectedSpells == 1){
            detectMagic = false;
            this.game.sendAction(new PlaySpellAction(this, spell, idx));
        }

        else if(detectMagic) {
            detectMagic = false;
            this.game.sendAction(new DetectMagicAction(this, spell, idx));
        }

        else{

            int numSelectedFighters = 0;
            for(boolean isSelected : selectedFighters) {
                if (isSelected) {
                    numSelectedFighters++;
                }
            }

            if(numSelectedFighters > 2) {
                selectedFighters[lastClicked[2]] = false;
                surfaceView.selectSpell(idx, false);
            }

            selectedFighters[idx] = true;
            surfaceView.selectSpell(idx, true);

            lastClicked[2] = lastClicked[1];
            lastClicked[1] = lastClicked[0];
            lastClicked[0] = idx;
        }
    }

    private void clickSpell(int idx){
        if(playerTurn == -2){
            selectedSpells[idx] = !selectedSpells[idx];
            surfaceView.selectSpell(idx, !selectedSpells[idx]);
        }
        else if (playerTurn >= 0){
            int numSelectedSpells = 0;
            for (boolean isSelected : selectedSpells) {
                if (isSelected) {
                    numSelectedSpells++;
                }
            }

            if(numSelectedSpells > 0){
                surfaceView.clearSpellSelections();
            }

            selectedSpells[idx] = true;
            surfaceView.selectSpell(idx, true);
        }
    }
}