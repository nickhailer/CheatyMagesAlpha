package edu.up.cs301.game.cheatymages.Cards;

import java.util.ArrayList;

public class SpellCard extends Card {

    private int mana;
    private int powerMod;
    //d = direct, e = enchant, s = support
    private char spellType;
    private boolean isForbidden;
    private boolean isFaceUp;

    public SpellCard(String name, int mana, int powerMod, char spellType,
                     boolean isForbidden) {
        super(name);
        //adds attributes of spell cards
        isFaceUp = (spellType != 'e');
        this.mana = mana;
        this.powerMod = powerMod;
        this.spellType = spellType;
        this.isForbidden = isForbidden;
    }

    public int getMana() {
        return mana;
    }

    public int getPowerMod() {
        return powerMod;
    }

    public char getSpellType() {
        return spellType;
    }

    public boolean isForbidden() {
        return isForbidden;
    }

    public boolean isFaceUp() {
        return isFaceUp;
    }
}