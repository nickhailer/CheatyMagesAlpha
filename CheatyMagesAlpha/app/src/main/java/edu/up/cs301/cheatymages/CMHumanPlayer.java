package edu.up.cs301.cheatymages.Players;

import android.graphics.Color;
import android.view.View;

import edu.up.cs301.GameFramework.GameMainActivity;
import edu.up.cs301.GameFramework.infoMessage.GameInfo;
import edu.up.cs301.GameFramework.infoMessage.IllegalMoveInfo;
import edu.up.cs301.GameFramework.infoMessage.NotYourTurnInfo;
import edu.up.cs301.GameFramework.players.GameHumanPlayer;
import edu.up.cs301.GameFramework.utilities.Logger;
import edu.up.cs301.cheatymages.CMGameState;
import edu.up.cs301.tictactoe.infoMessage.TTTState;

public class CMHumanPlayer extends GameHumanPlayer {

    //TODO IMPLEMENT THE SURFACE VIEW
    private CMSurfaceView surfaceView;

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
}
