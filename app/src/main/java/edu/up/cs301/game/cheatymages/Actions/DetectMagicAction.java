package edu.up.cs301.game.cheatymages.Actions;

import edu.up.cs301.game.GameFramework.players.GamePlayer;

//NOTE THIS IS A SUBCLASS OF PLAYSPELLACTION INSTEAD OF GAMEACTION FOR A REASON
//USING DETECT MAGIC IS TECHNICALLY A FORM OF PLAYING A SPELL ACCORDING TO THE RULES
//IN CASE ANY SPELLS/RULES INTERACT WITH WHENEVER A SPELL IS PLAYED THIS CAN BE USED
public class DetectMagicAction extends PlaySpellAction {
    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     * @param spell  the index of the spell in their hand
     * @param target the index of the fighter being targeted
     */
    public DetectMagicAction(GamePlayer player, int spell, int target) {
        super(player, spell, target);
    }
}
