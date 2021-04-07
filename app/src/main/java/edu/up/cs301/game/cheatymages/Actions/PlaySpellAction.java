package edu.up.cs301.game.cheatymages.Actions;

import edu.up.cs301.game.GameFramework.actionMessage.GameAction;
import edu.up.cs301.game.GameFramework.players.GamePlayer;

public class PlaySpellAction extends GameAction {

    private int spell;
    private int target;

    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     * @param spell the index of the spell in their hand
     * @param target the index of the fighter being targeted
     */
    public PlaySpellAction(GamePlayer player, int spell, int target) {
        super(player);
        this.spell = spell;
        this.target = target;
    }

    public int getSpell() {
        return spell;
    }

    public int getTarget() {
        return target;
    }
}
