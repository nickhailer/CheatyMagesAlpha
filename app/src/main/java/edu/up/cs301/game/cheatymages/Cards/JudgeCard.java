package edu.up.cs301.game.cheatymages.Cards;

import java.util.ArrayList;

public class JudgeCard extends Card {

    private int manaLimit;
    //d = dispel, e = eject
    private char judgementType;
    private ArrayList<Character> disallowedSpells;

    public JudgeCard(String name, int manaLimit, char judgementType, ArrayList<Character> disallowedSpells){
        super(name);
        this.manaLimit = manaLimit;
        this.judgementType = judgementType;
        this.disallowedSpells = disallowedSpells;
    }

    public int getManaLimit() {
        return manaLimit;
    }

    public char getJudgementType() {
        return judgementType;
    }

    public ArrayList<Character> getDisallowedSpells() {
        return disallowedSpells;
    }
}