package edu.up.cs301.game.cheatymages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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

    //Holds each player's gold
    private int[] gold;

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

        this.decks = new Decks(numPlayers);

        this.hands = new ArrayList[numPlayers];
        for(int i = 0; i < numPlayers; i++){
            this.hands[i] = new ArrayList<SpellCard>();
        }
        fillPlayerHands();

        this.gold = new int[numPlayers];
        for(int i = 0; i < numPlayers; i++){
            this.gold[i] = 2;
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
    private void fillPlayerHands(){
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
     * Draws cards for the player at the end of the round
     * @param id the id of the player drawing cards
     */
    private void drawCards(int id){
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
        for(int j = 0; j < drawAmount; j++){
            if(hands[id].size() >= handSize){
                break;
            }
            hands[id].add(decks.drawSpellCard());
        }
    }

    /**
     * Replaces the fighters in play
     */
    private void resetFighters(){
        FighterCard temp;
        for(int i = 0; i < 5; i++){
            temp = decks.drawFighterCard();
            decks.addFighterCard(fighters[i]);
            discardCardsFromFighter(i);
            fighters[i] = temp;
        }
    }

    /**
     * Removes all spell cards from a fighter and places them in the discard pile
     * @param fighter the index of the target fighter
     */
    private void discardCardsFromFighter(int fighter){
        for(int i = attachedSpells[fighter].size() - 1; i >= 0; i--){
            discardPile.remove(attachedSpells[fighter].get(i));
        }
    }

    /**
     * Replaces the current judge
     */
    private void resetJudge(){
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
        endRound();
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
        ArrayList<Boolean> isFaceUp;
        for(int i = 0; i < attachedSpells[target].size(); i++){
            //gets the array determining whether the card is face up or not
            isFaceUp = attachedSpells[target].get(i).getFaceUp();
            //sets the card to be face up for the player only
            isFaceUp.set(id, true);
            //sends updated array back to the spell card
            attachedSpells[target].get(i).setFaceUp(isFaceUp);
        }
    }

    private void applyJudgement(){
        //Iterates through each fighter and checks their mana total
        int manaTotal;
        for(int i = 0; i < 5; i++){
            //Adds up the mana of all attached spells
            manaTotal = 0;
            for(int j = 0; j < attachedSpells[i].size(); j++){
                manaTotal += attachedSpells[i].get(j).getMana();
            }
            //Checks if mana total is above the judge's mana limit
            if(manaTotal > judge.getManaLimit()){
                //Applies judge's judgement
                if(judge.getJudgementType() == 'd'){
                    discardCardsFromFighter(i);
                }
                else if(judge.getJudgementType() == 'e'){
                    //To "eject" a fighter they are replaced by a dummy fighter
                    FighterCard dummyFighter = new FighterCard("Dummy", -999, 0, false);
                    discardPile.add(fighters[i]);
                    fighters[i] = dummyFighter;
                }
            }
        }
    }

    private int findWinner(){
        //Iterates through each fighter and finds the winner
        int maxPower = -999;
        int winningFighter = -1;
        int power;
        for(int i = 1; i < 5; i++){
            power = fighters[i].getPower();
            //Calculates the power of a given fighter
            for(int j = 0; j < attachedSpells[i].size(); j++){
                power += attachedSpells[i].get(j).getPowerMod();
            }
            //Checks if this fighter beats the current winner
            if(power > maxPower){
                maxPower = power;
                winningFighter = i;
            }
            //Breaks ties using base power
            else if(power == maxPower){
                if(fighters[i].getPower() > fighters[winningFighter].getPower()){
                    winningFighter = i;
                }
            }
        }
        return winningFighter;
    }

    private void awardGold(int winner){
        for(int i = 0; i < numPlayers; i++){
            //Checks if player had a winning bet
            for(int j = 0; j < bets[i].size(); j++){
                if(bets[i].get(j) == winner){
                    //Awards gold to player
                    if(bets[i].size() == 3){
                        gold[i] += fighters[winner].getPrizeMoney() * 2;
                    }
                    else if(bets[i].size() == 2){
                        gold[i] += fighters[winner].getPrizeMoney();
                    }
                    else if(bets[i].size() == 1){
                        gold[i] += Math.ceil( (double)(fighters[winner].getPrizeMoney()) / 2);
                    }
                    break;
                }
            }
        }
    }

    private void endRound(){

        //Applies judge's judgement
        applyJudgement();

        //Finds the winning fighter and awards gold to the players
        awardGold(findWinner());

        //Moves game to the next round
        roundNum++;
        if(roundNum > 3){
            endGame();
            return;
        }
        playerTurn = -2;

    }

    /**
     * Discards the chosen cards from a player's hand
     * @param id the id of the player discarding
     * @param discards the cards you wish to get rid of listed from greatest index to smallest
     * @return true if all players have finished discarding
     */
    public boolean discardCards(int id, ArrayList<Integer> discards){

        //TODO THIS CAN BE REMOVED IF DISCARDS IS ALWAYS FROM GREATEST TO SMALLEST INT
        Collections.sort(discards);
        Collections.reverse(discards);

        //Removes cards from your hand and adds them to the discard pile
        for(int i = 0; i < discards.size(); i++){
            discardPile.add(hands[id].remove((int) discards.get(i)));
        }
        //Draws you new cards
        drawCards(id);

        //Checks if all players have finished discarding
        finishedDiscarding++;
        if(finishedDiscarding < numPlayers){
            return false;
        }

        //Moves game to the betting phase
        playerTurn = -1;
        return true;

    }

    //TODO: IMPLEMENT END GAME FUNCTION
    private void endGame(){

    }

}