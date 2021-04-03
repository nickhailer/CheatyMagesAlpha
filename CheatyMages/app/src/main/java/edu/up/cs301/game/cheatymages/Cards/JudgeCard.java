package edu.up.cs301.game.cheatymages.Cards;
import java.util.ArrayList;

public class JudgeCard extends Card {

    public int manaLimit;
    //d = dispel, e = eject
    public char judgementType;
    public ArrayList<Character> disallowedSpells;

    public JudgeCard(String name, int numPlayers, int manaLimit, char judgementType, ArrayList<Character> disallowedSpells){
        //Code from https://stackoverflow.com/a/16195084
        super(name, new ArrayList<Boolean>() {{
            for(int i = 0; i < numPlayers; i++){
                add(true);
            }
        }});
        this.manaLimit = manaLimit;
        this.judgementType = judgementType;
        this.disallowedSpells = disallowedSpells;
    }

}