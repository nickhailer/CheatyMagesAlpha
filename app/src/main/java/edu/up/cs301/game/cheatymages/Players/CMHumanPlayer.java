package edu.up.cs301.game.cheatymages.Players;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import edu.up.cs301.game.GameFramework.GameMainActivity;
import edu.up.cs301.game.GameFramework.infoMessage.GameInfo;
import edu.up.cs301.game.GameFramework.infoMessage.GameState;
import edu.up.cs301.game.GameFramework.infoMessage.IllegalMoveInfo;
import edu.up.cs301.game.GameFramework.infoMessage.NotYourTurnInfo;
import edu.up.cs301.game.GameFramework.players.GameHumanPlayer;
import edu.up.cs301.game.GameFramework.utilities.Logger;
import edu.up.cs301.game.R;
import edu.up.cs301.game.cheatymages.Actions.BetAction;
import edu.up.cs301.game.cheatymages.Actions.DetectMagicAction;
import edu.up.cs301.game.cheatymages.Actions.DiscardCardsAction;
import edu.up.cs301.game.cheatymages.Actions.PassAction;
import edu.up.cs301.game.cheatymages.Actions.PlaySpellAction;
import edu.up.cs301.game.cheatymages.CMGameState;
import edu.up.cs301.game.cheatymages.CMInterface;
import edu.up.cs301.game.cheatymages.CMMainActivity;
import edu.up.cs301.tictactoe.infoMessage.TTTState;

public class CMHumanPlayer extends GameHumanPlayer implements View.OnClickListener{

    CMGameState gameState = new CMGameState(3);

    // Checks if fighter card has been selected
    public boolean f1Clicked = false;
    public boolean f2Clicked = false;
    public boolean f3Clicked = false;
    public boolean f4Clicked = false;
    public boolean f5Clicked = false;

    // Checks if fighter has bet on them
    public boolean f1HasBet = false;
    public boolean f2HasBet = false;
    public boolean f3HasBet = false;
    public boolean f4HasBet = false;
    public boolean f5HasBet = false;

    // boolean ArrayList checks if fighter has bet on them
    public boolean[] hasBet = new boolean[] {f1HasBet, f2HasBet, f3Clicked, f4Clicked, f5HasBet};

    // the index of the spell in their hand
    public int spellCardIndex = -1;

    // the index of the fighter being targeted
    public int target = -1;

    // ArrayList of indices of the cards the player wishes to discard
    ArrayList<Integer> usedCards = new ArrayList<>();


    //TODO IMPLEMENT THE SURFACE VIEW
    private CMInterface surfaceView;

    /**
     * constructor
     *
     * @param name the name of the player
     */
    public CMHumanPlayer(String name) {
        super(name);
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
            //IMLPEMENT THESE STUFF
            surfaceView.setState((CMGameState)info);
            surfaceView.invalidate();
        }
    }

    @Override
    public void setAsGui(GameMainActivity activity) {

    }

    @Override
    public void onClick(View button) {
        switch(button.getId()) {
            case R.id.betButton:
                if(f1Clicked) {
                    f1HasBet = true;
                }
                else if(f2Clicked) {
                    f2HasBet = true;
                }
                else if(f3Clicked) {
                    f3HasBet = true;
                }
                else if(f4Clicked) {
                    f4HasBet = true;
                }
                else if(f5Clicked){
                    f5HasBet = true;
                }
                BetAction bAction = new BetAction(this, gameState.getBets()[0]);
                this.game.sendAction(bAction);
                break;
            case R.id.passButton:
                PassAction pAction = new PassAction(this);
                this.game.sendAction(pAction);
                break;
            case R.id.playSpellButton:
                PlaySpellAction sAction = new PlaySpellAction(this, spellCardIndex, target);
                this.game.sendAction(sAction);
                break;
            case R.id.detectMagicButton:
                DetectMagicAction dmAction = new DetectMagicAction(this, spellCardIndex, target );
                this.game.sendAction(dmAction);
                break;
            case R.id.discardCardsButton:

                DiscardCardsAction dcAction = new DiscardCardsAction(this, usedCards);
                this.game.sendAction(dcAction);
                break;
            case R.id.f1Button:
                f1Clicked = true;
                f2Clicked = false;
                f3Clicked = false;
                f4Clicked = false;
                f5Clicked = false;
                target = 0;
                break;
            case R.id.f2Button:
                f1Clicked = false;
                f2Clicked = true;
                f3Clicked = false;
                f4Clicked = false;
                f5Clicked = false;
                target = 1;
                break;
            case R.id.f3Button:
                f1Clicked = false;
                f2Clicked = false;
                f3Clicked = true;
                f4Clicked = false;
                f5Clicked = false;
                target = 2;
                break;
            case R.id.f4Button:
                f1Clicked = false;
                f2Clicked = false;
                f3Clicked = false;
                f4Clicked = true;
                f5Clicked = false;
                target = 3;
                break;
            case R.id.f5Button:
                f1Clicked = false;
                f2Clicked = false;
                f3Clicked = false;
                f4Clicked = false;
                f5Clicked = true;
                target = 4;
                break;
            case R.id.spellC1:
                spellCardIndex = 0;
                usedCards.add(0);
                break;
            case R.id.spellC2:
                spellCardIndex = 1;
                usedCards.add(1);
                break;
            case R.id.spellC3:
                spellCardIndex = 2;
                usedCards.add(2);
                break;
            case R.id.spellC4:
                spellCardIndex = 3;
                usedCards.add(3);
                break;
            case R.id.spellC5:
                spellCardIndex = 4;
                usedCards.add(4);
                break;
            case R.id.spellC6:
                spellCardIndex = 5;
                usedCards.add(5);
                break;
            case R.id.spellC7:
                spellCardIndex = 6;
                usedCards.add(6);
                break;
            case R.id.spellC8:
                spellCardIndex = 7;
                usedCards.add(7);
                break;
            default:
                break;
        }
    }
}