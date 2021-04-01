package edu.up.cs301.game.cheatymages;

import java.util.ArrayList;
import java.util.Random;

import edu.up.cs301.game.GameFramework.infoMessage.GameState;
import edu.up.cs301.game.cheatymages.Cards.*;

public class CMGameState extends GameState{

    //Holds which round number it is
    private int roundNum;

    //Holds who's turn it is
    //0 = player 1 , 1 = player 2, etc.
    //-1 = start of a round, -2 = end of a round
    private int playerTurn;

    //Holds each player's hand of cards
    private ArrayList<SpellCard>[] hands;

    //Holds each player's coins
    private int[] playerCoins;

    //Holds each player's bets
    private ArrayList<Integer>[] bets;

    //Holds the fighters currently in play
    private FighterCard[] fighters;

    //Holds on spells placed on fighters in play
    private ArrayList<SpellCard>[] attachedSpells;

    //Holds the judge currently in play
    private JudgeCard judge;

    //Holds the cards currently in the discard pile
    private ArrayList<Card> discardPile;

    //Holds the decks of cards not currently in play
    private Decks decks;

    //Checks if everyone has placed their bets
    private int betsPlaced;

    //Checks if everyone has discarded their cards
    private int finishedDiscarding;

    //Checks if everyone has passed consecutively
    private int consecutivePasses;

    //The number of players
    protected int numPlayers;

    //Random number generator
    protected Random rng;

    /**
     * Constructor
     * @param numPlayers the number of players
     */
    public CMGameState(int numPlayers){
        super();

        this.numPlayers = numPlayers;

        this.decks = new Decks();

        this.hands = new ArrayList[numPlayers];
        for(int i = 0; i < numPlayers; i++){
            this.hands[i] = new ArrayList<SpellCard>();
        }
        fillPlayerHands();

        this.playerCoins = new int[numPlayers];
        for(int i = 0; i < numPlayers; i++){
            this.playerCoins[i] = 2;
        }

        this.bets = new ArrayList[numPlayers];
        for(int i = 0; i < numPlayers; i++){
            this.bets[i] = new ArrayList<Integer>();
        }

        this.roundNum = 1;
        this.playerTurn = -1;

        this.fighters = new FighterCard[5];
        for(int i = 0; i < 5; i++){
            this.fighters[i] = decks.drawFighterCard();
        }

        judge = decks.drawJudgeCard();

        this.attachedSpells = new ArrayList[5];
        for(int i = 0; i < 5; i++){
            this.attachedSpells[i] = new ArrayList<SpellCard>();
        }

        this.discardPile = new ArrayList<Card>();

        betsPlaced = 0;

        finishedDiscarding = 0;

        consecutivePasses = 0;

        rng = new Random();

    }

    /**
     * Fills each player's hand at the start of the game
     */
    public void fillPlayerHands(){
        int handSize;
        if(numPlayers == 6){
            handSize = 5;
        }
        if(numPlayers == 5){
            handSize = 6;
        }
        else{
            handSize = 8;
        }
        for(int i = 0; i < numPlayers; i++){
            while(hands[i].size() < handSize){
                hands[i].add(decks.drawSpellCard());
            }
        }
    }

    /**
     * Draws cards for each player at the end of the round
     */
    public void drawCards(){
        int handSize;
        int drawAmount;
        if(numPlayers <= 4) {
            handSize = 8;
            drawAmount = 4;
        }
        else{
            handSize = 6;
            drawAmount = 3;
        }
        for(int i = 0; i < numPlayers; i++) {
            for(int j = 0; j < drawAmount; j++){
                if(hands[i].size() >= handSize){
                    break;
                }
                hands[i].add(decks.drawSpellCard());
            }
        }
    }

    /**
     * Replaces the fighters in play
     */
    public void resetFighters(){
        FighterCard temp;
        for(int i = 0; i < 5; i++){
            temp = decks.drawFighterCard();
            decks.addFighterCard(fighters[i]);
            for(int j = 0; j < attachedSpells[i].size(); j++){
                discardPile.add(attachedSpells[i].get(j));
            }
            attachedSpells[i] = new ArrayList<SpellCard>();
            fighters[i] = temp;
        }
    }

    /**
     * Replaces the current judge
     */
    public void resetJudge(){
        discardPile.add(judge);
        judge = decks.drawJudgeCard();
    }

    /**
     * Makes a player place a bet
     * @param id the id of the player who is placing the bet
     * @param bets the bets this player wishes to place
     * @return true if everyone has finished placing their bets
     */
    public boolean placeBet(int id, ArrayList<Integer> bets){
        this.bets[id] = bets;
        betsPlaced++;
        if(betsPlaced < numPlayers){
            return false;
        }
        playerTurn = rng.nextInt(numPlayers);
        betsPlaced = 0;
        return true;
    }

    /**
     * Makes a player pass their turn
     * @return true if the round is over
     */
    public boolean pass(){
        //increments pass streak counter
        consecutivePasses++;
        //if not all players have passed consecutively
        if(consecutivePasses < numPlayers){
            //increments player turn
            playerTurn = (playerTurn + 1) % numPlayers;
            return false;
        }
        //resets pass streak counter
        consecutivePasses = 0;
        //ends round
        playerTurn = -2;
        combatPhase();
        return true;
    }

    /**
     * Plays a spell card from your hand onto a target
     * @param id the id of the player using the spell
     * @param spell the index of the spell in your hand
     * @param target the fighter you wish to use the spell on (1-5)
     */
    public void playSpellCard(int id, int spell, int target){
        //breaks pass streak
        consecutivePasses = 0;
        //increments player turn
        playerTurn = (playerTurn + 1) % numPlayers;
        //removes spell from your hand and attaches it to the target
        attachedSpells[target].add(hands[id].remove(spell));
        //increments player turn
        playerTurn = (playerTurn + 1) % numPlayers;
    }

    /**
     * Removes a spell from your hand and reveals all spell on a fighter
     * @param id the id of the player using the spell
     * @param spell the index of the spell in your hand
     * @param target the fighter you wish to use the spell on (1-5)
     */
    public void detectMagic(int id, int spell, int target){
        //breaks pass streak
        consecutivePasses = 0;
        //increments player turn
        playerTurn = (playerTurn + 1) % numPlayers;
        //discards target spell from your hand
        discardPile.add(hands[id].remove(spell));
        //reveals all cards on target fighter for the player
        for(int i = 0; i < attachedSpells[target].size(); i++){
            attachedSpells[target].get(i).isFaceUp.set(id, true);
        }
    }

    public void combatPhase(){

    }

}