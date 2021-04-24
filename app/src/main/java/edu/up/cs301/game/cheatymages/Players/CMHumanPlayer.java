package edu.up.cs301.game.cheatymages.Players;

import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

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

    // boolean ArrayList contains selected fighters
    ArrayList<Integer> selectedFighters = new ArrayList<>();
    // boolean ArrayList contains unselected fighters
    ArrayList<Integer> unselectedFighters = new ArrayList<>();

    protected boolean detectMagic;

    // ArrayList of indices of the cards the player wishes to discard
    ArrayList<Integer> selectedSpells = new ArrayList<>();
    // boolean ArrayList contains unselected fighters
    ArrayList<Integer> unselectedSpells = new ArrayList<>();

    private int layoutId;

    private CMSurfaceView cmSurfaceView;

    /**
     * constructor
     *
     * @param name the name of the player
     */
    public CMHumanPlayer(String name, int layoutId) {
        super(name);
        this.layoutId = layoutId;

        // Setting all fighters to unselected
        for(int i=0; i<5; i++) {
            unselectedFighters.add(i);
        }

        // Setting all spells to unselected
        for(int i=0; i<8; i++) {
            unselectedSpells.add(i);
        }

    }

    @Override
    public View getTopView() {
        return null;
    }

    @Override
    public void receiveInfo(GameInfo info) {

        if (cmSurfaceView == null){
            return;
        }

        if (info instanceof IllegalMoveInfo || info instanceof NotYourTurnInfo) {
            // if the move was out of turn or otherwise illegal, flash the screen
            //TODO IMPLEMENT A FLASH METHOD
            int myColor = Color.rgb(255, 0, 0);
            flash(myColor, 10000);
            return;
        }
        else if (!(info instanceof CMGameState)) {
            // if we do not have a TTTState, ignore
            return;
        }
        else {
            playerTurn = ((CMGameState) info).getPlayerTurn();
            cmSurfaceView.setState((CMGameState) info, this.playerNum);
            cmSurfaceView.invalidate();
        }
    }

    @Override
    public void setAsGui(GameMainActivity activity) {
        // Load the layout resource for the new configuration
        activity.setContentView(layoutId);

        // set the cmSurfaceView instance variable
        cmSurfaceView = (CMSurfaceView) myActivity.findViewById(R.id.cmSurfaceView);
        cmSurfaceView.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if (motionEvent.getAction() != MotionEvent.ACTION_UP) return true;

        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        String item = cmSurfaceView.mapPositionToItem(x, y);

        switch (item) {
            case "Bet":
                //Tells human player that they placed their bet
                Toast betMessage = Toast.makeText(getActivity(), "You placed your bet ", Toast.LENGTH_SHORT);
                betMessage.setGravity(Gravity.TOP, 0,100);
                betMessage.show();

                ArrayList<Integer> bets = new ArrayList<>();
                for (int i = 0; i < selectedFighters.size(); i++) {
                    bets.add(selectedFighters.get(i));
                }
                if(selectedFighters.size() < 1) {
                    break;
                }
                this.game.sendAction(new BetAction(this, bets));
                break;
            case "Pass":
                //prints a message to the screen that you passed
                Toast passMessage = Toast.makeText(getActivity(), "You Passed", Toast.LENGTH_SHORT);
                passMessage.setGravity(Gravity.TOP, 0,100);
                passMessage.show();
                this.game.sendAction(new PassAction(this));
                detectMagic = false;
                break;
            case "Detect Magic":
                detectMagic = true;
                //TODO UNCOMMENT THIS ONCE IMPLEMENT
                //cmSurfaceView.selectDetectMagic()
                break;
            case "Discard":
                ArrayList<Integer> discards = new ArrayList<>();
                for(int i = selectedSpells.size() - 1; i >= 0; i--) {
                    if(selectedSpells.size() != 0) {
                        int max = Collections.max(selectedSpells);
                        discards.add(max);
                        selectedSpells.remove(selectedSpells.indexOf(max));
                    }
                }
                //prints a message to the screen that you discarded cards
                Toast discardMessage = Toast.makeText(getActivity(), "You discarded " + discards.size() + " cards", Toast.LENGTH_SHORT);
                discardMessage.setGravity(Gravity.TOP, 0,100);
                discardMessage.show();

                game.sendAction(new DiscardCardsAction(this, discards));

                //Resets selected and unselected spells after discard action
                selectedSpells.clear();

                // Setting all spells to unselected
                for(int i=0; i<8; i++) {
                    unselectedSpells.add(i);
                }


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
        if(selectedSpells.size() == 1){
            //Tells human player that they played a spell card
            Toast betMessage = Toast.makeText(getActivity(), "You played a spell card on fighter " + (idx+1), Toast.LENGTH_SHORT);
            betMessage.setGravity(Gravity.TOP, 0,100);
            betMessage.show();
            detectMagic = false;
            this.game.sendAction(new PlaySpellAction(this, selectedSpells.get(0), idx));
        }
        /*
        else if(detectMagic) {
            detectMagic = false;
            this.game.sendAction(new DetectMagicAction(this, spell, idx));
        }

         */
        else{
            //removes fighter if the fighter was clicked again
            if(selectedFighters.contains(idx)){
                selectedFighters.remove(selectedFighters.indexOf(idx));
                unselectedFighters.add(idx);
                cmSurfaceView.selectFighter(idx, false);
            }
            //if there's 3 fighters selected remove oldest one and selects new one
            else if(selectedFighters.size() > 2) {
                cmSurfaceView.selectFighter(selectedFighters.get(0), false);
                selectedFighters.remove(0);
                unselectedFighters.add(idx);
                selectedFighters.add(idx);
                unselectedFighters.remove(selectedFighters.indexOf(idx));
                cmSurfaceView.selectFighter(idx, true);
            }
            //adds to selected
            else {
                selectedFighters.add(idx);
                unselectedFighters.remove(selectedFighters.indexOf(idx));
                cmSurfaceView.selectFighter(idx, true);
            }
        }
    }

    private void clickSpell(int idx) {
        //If its the discarding phase the spell will unhighlight if it was already selected
        //or highlight if it was not selected already
        if (playerTurn == -2) {
            //removes spell if the fighter was clicked again
            if (selectedSpells.contains(idx)) {
                selectedSpells.remove(selectedSpells.indexOf(idx));
                unselectedSpells.add(idx);
                cmSurfaceView.selectSpell(idx, false);
            }
            //adds to selected
            else {
                selectedSpells.add(idx);
                unselectedSpells.remove(selectedSpells.indexOf(idx));
                cmSurfaceView.selectSpell(idx, true);
            }
        }
        //If its a players turn
        else if (playerTurn >= 0) {
            //removes spell if the fighter was clicked again
            if (selectedSpells.contains(idx)) {
                selectedSpells.remove(selectedSpells.indexOf(idx));
                unselectedSpells.add(idx);
                cmSurfaceView.selectSpell(idx, false);
            }
            // if there's more than one card selected remove old card and select new one
            else if (selectedSpells.size() > 0) {
                cmSurfaceView.selectSpell(selectedSpells.get(0), false);
                selectedSpells.remove(0);
                unselectedSpells.add(idx);
                selectedSpells.add(idx);
                unselectedSpells.remove(selectedSpells.indexOf(idx));
                cmSurfaceView.selectSpell(idx, true);
            }
            //adds to selected
            else {
                selectedSpells.add(idx);
                unselectedSpells.remove(selectedSpells.indexOf(idx));
                cmSurfaceView.selectSpell(idx, true);
            }
        }
    }

}