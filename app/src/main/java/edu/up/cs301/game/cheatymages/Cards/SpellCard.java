package edu.up.cs301.game.cheatymages.Cards;

import java.util.ArrayList;

public class SpellCard extends Card {

    public int mana;
    public int powerMod;
    //d = direct, e = enchant, s = support
    public char spellType;
    public boolean isForbidden;
    //This is a list of size equal to the number of players because cards are shown as
    //face up or down to different players
    public ArrayList<Boolean> isFaceUp;

    public SpellCard(String name, ArrayList<Boolean> isFaceUp, int mana, int powerMod, char spellType,
                     boolean isForbidden) {
        super(name);
        //adds attributes of spell cards
        this.isFaceUp = isFaceUp;
        this.mana = mana;
        this.powerMod = powerMod;
        this.spellType = spellType;
        this.isForbidden = isForbidden;
    }

}