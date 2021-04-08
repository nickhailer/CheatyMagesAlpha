package edu.up.cs301.cheatymages.Actions;

import java.util.ArrayList;

import edu.up.cs301.GameFramework.actionMessage.GameAction;
import edu.up.cs301.GameFramework.players.GamePlayer;

public class BetAction extends GameAction {

    private ArrayList<Integer> bets;

    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     * @param bets the indices of the fighters the player is betting on
     */
    public BetAction(GamePlayer player, ArrayList<Integer> bets) {
        super(player);
        this.bets = bets;
    }

    public ArrayList<Integer> getBets() {
        return bets;
    }
}