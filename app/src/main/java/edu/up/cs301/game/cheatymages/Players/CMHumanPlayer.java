package edu.up.cs301.game.cheatymages.Players;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import edu.up.cs301.game.GameFramework.GameMainActivity;
import edu.up.cs301.game.GameFramework.infoMessage.*;
import edu.up.cs301.game.GameFramework.players.GameHumanPlayer;
import edu.up.cs301.game.R;
import edu.up.cs301.game.cheatymages.Actions.*;
import edu.up.cs301.game.cheatymages.CMGameState;
import edu.up.cs301.game.cheatymages.CMSurfaceView;

public class CMHumanPlayer extends GameHumanPlayer implements View.OnTouchListener {

    // keeps track of what turn it was the last time you were updated information
    protected int playerTurn;

    // boolean ArrayList checks if fighter has bet on them
    protected boolean[] selectedFighters = new boolean[5];

    protected boolean detectMagic;

    protected int[] lastClicked = new int[3];

    // ArrayList of indices of the cards the player wishes to discard
    protected boolean[] selectedSpells = new boolean[8];

    private int layoutId;

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
        surfaceView.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if (motionEvent.getAction() != MotionEvent.ACTION_UP) return true;

        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        String item = surfaceView.mapPositionToItem(x, y);
        //TODO REPLACE THIS WITH AN ACTUAL INDICATION YOU PRESSED SOMETHING
        Log.d("CMHumanPlayer", "You pressed " + item);

        switch (item) {
            case "Bet":
                ArrayList<Integer> bets = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    if (selectedFighters[i]) {
                        bets.add(i);
                    }
                }
                this.game.sendAction(new BetAction(this, bets));
                break;
            case "Pass":
                this.game.sendAction(new PassAction(this));
                detectMagic = false;
                break;
            case "Detect Magic":
                detectMagic = true;
                //TODO UNCOMMENT THIS ONCE IMPLEMENT
                //surfaceView.selectDetectMagic()
                break;
            case "Discard":
                ArrayList<Integer> discards = new ArrayList<>();
                for (int i = 7; i >= 0; i--) {
                    if (selectedSpells[i]) {
                        discards.add(i);
                    }
                }
                game.sendAction(new DiscardCardsAction(this, discards));
                detectMagic = false;
                break;
            case "Fighter 1":
                clickFighter(0);
                break;
            case "Fighter 2":
                clickFighter(1);
                break;
            case "Fighter 3":
                clickFighter(2);
                break;
            case "Fighter 4":
                clickFighter(3);
                break;
            case "Fighter 5":
                clickFighter(4);
                break;
            case "Spell 1":
                clickSpell(0);
                break;
            case "Spell 2":
                clickSpell(1);
                break;
            case "Spell 3":
                clickSpell(2);
                break;
            case "Spell 4":
                clickSpell(3);
                break;
            case "Spell 5":
                clickSpell(4);
                break;
            case "Spell 6":
                clickSpell(5);
                break;
            case "Spell 7":
                clickSpell(6);
                break;
            case "Spell 8":
                clickSpell(7);
                break;
        }
        return true;
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

            //TODO THERE IS A BUG HERE WHERE YOU CAN SELECT MORE THAN 3 SOMEHOW I DONT FUCKING KNOW MAN

            if(selectedFighters[idx]){
                selectedFighters[idx] = false;
                surfaceView.selectFighter(idx, false);

                for(int i = idx + 1; i < 3; i++){
                    lastClicked[idx] = lastClicked[idx + 1];
                }
                lastClicked[2] = 0;
                return;
            }

            if(numSelectedFighters > 2) {
                selectedFighters[lastClicked[2]] = false;
                surfaceView.selectFighter(lastClicked[2], false);
            }

            selectedFighters[idx] = true;
            surfaceView.selectFighter(idx, true);

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