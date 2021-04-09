package edu.up.cs301.game.cheatymages.Cards;

import java.util.ArrayList;
import java.util.Random;
import edu.up.cs301.game.cheatymages.Cards.*;

public class Decks {

    //The decks containing the cards
    private ArrayList<SpellCard> spellDeck;
    private ArrayList<FighterCard> fighterDeck;
    private ArrayList<JudgeCard> judgeDeck;

    //Random number generator
    protected Random rng;

    /**
     * Decks Constructor
     */
    public Decks(int numPlayers){

        spellDeck = new ArrayList<>();
        fighterDeck = new ArrayList<>();
        judgeDeck = new ArrayList<>();

        ArrayList<Boolean> isFaceUp = new ArrayList<>();
        for(int i = 0; i < numPlayers; i++){
            isFaceUp.add(false);
        }

        for(int i = 0; i < 2; i++){
            spellDeck.add(new SpellCard("Magic Missile", isFaceUp, 0, -2, 'd', false));
            spellDeck.add(new SpellCard("Cure", isFaceUp, 0, 2, 'd', false));
        }
        for(int i = 0; i < 8; i++){
            spellDeck.add(new SpellCard("Healing", isFaceUp, 1, 4, 'd', false));
            spellDeck.add(new SpellCard("Fireball", isFaceUp, 1, -4, 'd', false));
            spellDeck.add(new SpellCard("Strengthen", isFaceUp, 2, 3, 'e', false));
            spellDeck.add(new SpellCard("Weaken", isFaceUp, 2, -3, 'e', false));
            spellDeck.add(new SpellCard("Regeneration", isFaceUp, 3, 3, 'd', false));
            spellDeck.add(new SpellCard("Blizzard", isFaceUp, 4, -6, 'd', false));
            spellDeck.add(new SpellCard("Might", isFaceUp, 4, 5, 'e', false));
            spellDeck.add(new SpellCard("Slow", isFaceUp, 6, -4, 'e', false));
        }
        spellDeck.add(new SpellCard("Mana Boost", isFaceUp, 5, 0, 'e', false));
        spellDeck.add(new SpellCard("Mana Seal", isFaceUp, -5, 0, 'e', false));
        spellDeck.add(new SpellCard("Giant Growth", isFaceUp, 10, 12, 'e', true));
        spellDeck.add(new SpellCard("Shrink", isFaceUp, 10, -12, 'e', true));

        ArrayList<Character> disallowsMoria = new ArrayList<>();
        disallowsMoria.add('s');
        ArrayList<Character> disallowsAdoth = new ArrayList<>();
        disallowsAdoth.add('s');
        disallowsAdoth.add('f');
        ArrayList<Character> disallowsZapp = new ArrayList<>();
        disallowsZapp.add('f');
        ArrayList<Character> disallowsOrlair = new ArrayList<>();
        disallowsOrlair.add('d');

        judgeDeck.add(new JudgeCard("Moria", 12, 'd', disallowsMoria));
        judgeDeck.add(new JudgeCard("Adoth", 10, 'e', disallowsAdoth));
        judgeDeck.add(new JudgeCard("Zapp", 15, 'd', disallowsZapp));
        judgeDeck.add(new JudgeCard("Orlair", 12, 'e', disallowsOrlair));
        //Lawty has no disallows
        judgeDeck.add(new JudgeCard("Lawty", 12, 'd', new ArrayList<Character>()));
        //Tad doesn't do anything
        judgeDeck.add(new JudgeCard("Tad", 999999, 'd', new ArrayList<Character>()));

        fighterDeck.add(new FighterCard("Goblin", 1, 10, false));
        fighterDeck.add(new FighterCard("Orc", 2, 8, false));
        fighterDeck.add(new FighterCard("Skeleton", 3, 7, true));
        fighterDeck.add(new FighterCard("Lizardman", 4, 6, false));
        fighterDeck.add(new FighterCard("Ghost", 5, 5, true));
        fighterDeck.add(new FighterCard("Succubus", 6, 5, false));
        fighterDeck.add(new FighterCard("Dark Elf", 7, 4, false));
        fighterDeck.add(new FighterCard("Minotaur", 8, 4, false));
        fighterDeck.add(new FighterCard("Demon", 9, 3, false));
        fighterDeck.add(new FighterCard("Dragon", 10, 3, false));

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