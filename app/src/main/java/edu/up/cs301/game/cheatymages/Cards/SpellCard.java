package edu.up.cs301.game.cheatymages.Cards;

import java.util.ArrayList;

public class SpellCard extends Card {

    private int mana;
    private int powerMod;
    //d = direct, e = enchant, s = support
    private char spellType;
    private boolean isForbidden;
    //This is a list of size equal to the number of players because cards are shown as
    //face up or down to different players
    private ArrayList<Boolean> isFaceUp;

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

    public ArrayList<Boolean> getFaceUp() {
        return isFaceUp;
    }

    public void setFaceUp(ArrayList<Boolean> isFaceUp) {
        this.isFaceUp = isFaceUp;
    }
}