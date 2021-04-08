package edu.up.cs301.cheatymages.Actions;

import java.util.ArrayList;

import edu.up.cs301.GameFramework.actionMessage.GameAction;
import edu.up.cs301.GameFramework.players.GamePlayer;

public class DiscardCardsAction extends GameAction {

    private ArrayList<Integer> discards;

    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     * @param discards the indices of the cards the player wishes to discard
     */
    public DiscardCardsAction(GamePlayer player, ArrayList<Integer> discards) {
        super(player);
        this.discards = discards;
    }

    public ArrayList<Integer> getDiscards() {
        return discards;
    }
}
