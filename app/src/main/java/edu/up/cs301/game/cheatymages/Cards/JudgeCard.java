package edu.up.cs301.game.cheatymages.Cards;

import java.util.ArrayList;

public class JudgeCard extends Card {

    public int manaLimit;
    //d = dispel, e = eject
    public char judgementType;
    public ArrayList<Character> disallowedSpells;

    public JudgeCard(String name, int manaLimit, char judgementType, ArrayList<Character> disallowedSpells){
        super(name);
        this.manaLimit = manaLimit;
        this.judgementType = judgementType;
        this.disallowedSpells = disallowedSpells;
    }

}