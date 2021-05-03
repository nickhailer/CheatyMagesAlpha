package edu.up.cs301.game.cheatymages.Players;

import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
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

    ArrayList<Integer> selectedFighters = new ArrayList<>();

    protected boolean detectMagic;

    protected boolean[] selectedSpells = new boolean[8];

    private int selectedSpell = -1;

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
            int myColor = Color.rgb(255, 0, 0);
            flash(myColor, 10000);
            return;
        }
        else if (!(info instanceof CMGameState)) {
            // if we do not have a TTTState, ignore
            return;
        }
        else {
            surfaceView.setState((CMGameState) info, this.playerNum);
            playerTurn = ((CMGameState) info).getPlayerTurn();
            surfaceView.invalidate();
        }
    }

    @Override
    public void setAsGui(GameMainActivity activity) {
        // Load the layout resource for the new configuration
        activity.setContentView(layoutId);

        // set the cmSurfaceView instance variable
        surfaceView = (CMSurfaceView) myActivity.findViewById(R.id.cmSurfaceView);
        surfaceView.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if (motionEvent.getAction() != MotionEvent.ACTION_UP) return true;

        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        String item = surfaceView.mapPositionToItem(x, y);

        switch (item) {
            case "Bet":
                ArrayList<Integer> bets = new ArrayList<>();
                for (int i = 0; i < selectedFighters.size(); i++) {
                    bets.add(selectedFighters.get(i));
                }
                if(selectedFighters.size() < 1) {
                    break;
                }
                this.game.sendAction(new BetAction(this, bets));
                //Tells human player that they placed their bet
                Toast betMessage = Toast.makeText(getActivity(), "You placed your bet ", Toast.LENGTH_SHORT);
                betMessage.setGravity(Gravity.TOP, 0,100);
                betMessage.show();
                break;
            case "Pass":
                this.game.sendAction(new PassAction(this));
                //prints a message to the screen that you passed
                Toast passMessage = Toast.makeText(getActivity(), "You Passed", Toast.LENGTH_SHORT);
                passMessage.setGravity(Gravity.TOP, 0,100);
                passMessage.show();
                detectMagic = false;
                break;
            case "Detect Magic":
                detectMagic = true;
                //TODO UNCOMMENT THIS ONCE IMPLEMENT
                //surfaceView.
                Toast detectMagicMessage = Toast.makeText(getActivity(), "You used detect magic", Toast.LENGTH_SHORT);
                detectMagicMessage.setGravity(Gravity.TOP, 0,100);
                detectMagicMessage.show();
                break;
            case "Discard":
                ArrayList<Integer> discards = new ArrayList<>();
                for (int i = 7; i >= 0; i--) {
                    if (selectedSpells[i]) {
                        discards.add(i);
                    }
                }

                game.sendAction(new DiscardCardsAction(this, discards));
                surfaceView.clearSpellSelections();
                Arrays.fill(selectedSpells, false);
                // resets all bets
                for(int i = selectedFighters.size() - 1; i >= 0; i--){
                    selectedFighters.remove(i);
                }
                detectMagic = false;

                //prints a message to the screen that you discarded cards
                Toast discardMessage = Toast.makeText(getActivity(), "You discarded " + discards.size() + " cards", Toast.LENGTH_SHORT);
                discardMessage.setGravity(Gravity.TOP, 0,100);
                discardMessage.show();
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
        if(playerTurn >= 0){
            if(detectMagic) {
                detectMagic = false;
                this.game.sendAction(new DetectMagicAction(this, selectedSpell, idx));
            }
            Toast betMessage = Toast.makeText(getActivity(), "You played a spell card on fighter " + (idx+1), Toast.LENGTH_SHORT);
            betMessage.setGravity(Gravity.TOP, 0,100);
            betMessage.show();
            detectMagic = false;
            this.game.sendAction(new PlaySpellAction(this, selectedSpell, idx));
        }
        else if(playerTurn == -1){

            //if the fighter is already selected just deselect it
            if(selectedFighters.contains(idx)){
                surfaceView.selectFighter(selectedFighters.remove(selectedFighters.indexOf(idx)), false);
            }
            else {

                //if there are more than 3 fighters selected remove the oldest one
                if (selectedFighters.size() > 2) {
                    surfaceView.selectFighter(selectedFighters.remove(0), false);
                }

                selectedFighters.add(idx);
                surfaceView.selectFighter(idx, true);
            }
        }
    }

    private void clickSpell(int idx) {
        //If its the discarding phase the spell will unhighlight if it was already selected
        //or highlight if it was not selected already
        if(playerTurn == -2){
            selectedSpells[idx] = !selectedSpells[idx];
            surfaceView.selectSpell(idx, selectedSpells[idx]);
        }
        else if (playerTurn >= 0){
            selectedSpell = idx;
            surfaceView.clearSpellSelections();
            surfaceView.selectSpell(idx, true);
        }
    }
}