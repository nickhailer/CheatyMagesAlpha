package edu.up.cs301.game.cheatymages.Cards;

import java.util.ArrayList;
import java.util.Random;
import edu.up.cs301.game.cheatymages.Cards.*;

public class Decks {

    private ArrayList<SpellCard> spellDeck;
    private ArrayList<FighterCard> fighterDeck;
    private ArrayList<JudgeCard> judgeDeck;

    protected Random rng;

    public Decks(){
        rng = new Random();
    }

    public SpellCard drawSpellCard(){
        return spellDeck.remove(rng.nextInt(spellDeck.size()));
    }

    public FighterCard drawFighterCard(){
        return fighterDeck.remove(rng.nextInt(fighterDeck.size()));
    }

    public JudgeCard drawJudgeCard(){
        return judgeDeck.remove(rng.nextInt(judgeDeck.size()));
    }

    public void addSpellCard(SpellCard card){
        spellDeck.add(card);
    }

    public void addFighterCard(FighterCard card){
        fighterDeck.add(card);
    }

    public void addJudgeCard(JudgeCard card){
        judgeDeck.add(card);
    }

}