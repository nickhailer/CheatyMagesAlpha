package edu.up.cs301.game.cheatymages.Cards;
import java.util.ArrayList;

public class SpellCard extends Card {

    public int mana;
    public int powerMod;
    //d = direct, e = enchant, s = support
    public char spellType;
    public boolean isForbidden;
    public String cardText;


    public SpellCard(String name, ArrayList<Boolean> isFaceUp, int mana, int powerMod, String cardText, char spellType,
                     boolean isForbidden) {
        //adds attributes of spell cards
        super(name, isFaceUp);
        this.mana = mana;
        this.powerMod = powerMod;
        this.cardText = cardText;
        this.spellType = spellType;
        this.isForbidden = isForbidden;
    }

}