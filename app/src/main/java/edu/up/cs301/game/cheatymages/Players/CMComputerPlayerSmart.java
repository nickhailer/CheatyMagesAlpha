package edu.up.cs301.game.cheatymages.Players;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import edu.up.cs301.game.GameFramework.infoMessage.GameInfo;
import edu.up.cs301.game.GameFramework.infoMessage.GameState;
import edu.up.cs301.game.GameFramework.players.GameComputerPlayer;
import edu.up.cs301.game.cheatymages.Actions.BetAction;
import edu.up.cs301.game.cheatymages.Actions.DiscardCardsAction;
import edu.up.cs301.game.cheatymages.Actions.PassAction;
import edu.up.cs301.game.cheatymages.Actions.PlaySpellAction;
import edu.up.cs301.game.cheatymages.CMGameState;

public class CMComputerPlayerSmart extends GameComputerPlayer {

    protected Random rng;

    protected ArrayList<Integer> nonBets = new ArrayList<>();

    /**
     * constructor
     * @param name the player's name (e.g., "John")
     */
    public CMComputerPlayerSmart(String name){
        super(name);
        rng = new Random();
    }

    @Override
    protected void receiveInfo(GameInfo info) {
        //Makes sure the info message is a game state before sending an action
        if(!(info instanceof GameState)){
            return;
        }

        CMGameState state = (CMGameState) info;

        //Simulates the computer thinking
        sleep(0.5);

        //If it's betting phase place a random bet
        if(state.getPlayerTurn() == -1){
            ArrayList<Integer> bets = new ArrayList<>();
            for(int i = 0; i < rng.nextInt(3) + 1; i++){
                bets.add(rng.nextInt(5));
            }
            game.sendAction(new BetAction(this, bets));
            return;
        }

        //If it's discarding phase choose to discard no cards
        if(state.getPlayerTurn() == -2){
            game.sendAction(new DiscardCardsAction(this, new ArrayList<Integer>()));
            return;
        }

        int playerTurn = ((CMGameState) info).getPlayerTurn()-1;
        int numBets= state.getBets()[playerNum].size();
        // the index of the fighter that has the highest power level that it did not bet on
        int highestPowerIdx = findMaxPowerIdx(state);
        int handSize = state.getHands()[playerNum].size();
        int attachedSize = state.getAttachedSpells()[highestPowerIdx].size();

        if(handSize > 0 && playerTurn >= 0){
            // if the judgement is eject, play spell cards on fighter with highest power that it did not bet on
            if(state.getJudge().getJudgementType() == 'e' && attachedSize < 6){
                game.sendAction(new PlaySpellAction(this, rng.nextInt(handSize), highestPowerIdx));
                return;
            }
            // Finds positive spell card
            for(int i = 0; i < handSize; i++) {
                // finds a positive spell card and plays it on a card they bet on
                if(state.getHands()[playerNum].get(i).getPowerMod() > 0) {
                    int idx = i;
                    game.sendAction(new PlaySpellAction(this, idx, state.getBets()[playerNum].get(rng.nextInt(numBets))));
                    return;
                }
            }
            for(int i = 0; i < handSize; i++) {
                // find negative spell card and play it on a card you didn't bet on
                if(state.getHands()[playerNum].get(i).getPowerMod() < 0) {
                    int idx = i;
                    game.sendAction(new PlaySpellAction(this, idx, randomNonBetFighter(state)));
                    return;
                }
            }
        }
        //If you can't do anything else pass
        game.sendAction(new PassAction(this));
    }

    /**
     * findMaxPowerIdx is a helper method that finds the fighter card
     * with the highest power and returns their index
     * @param state CMGameState
     * @return index of fighter with the highest power
     */
    public int findMaxPowerIdx(CMGameState state){
        int maxIdx = -999;
        int max = -999;
        // set everything to non bets
        for(int i = 0; i < 5; i++) {
            nonBets.add(i);
        }
        // removes the cards that were bet on
        for(int i = 0; i < state.getBets()[playerNum].size(); i++) {
            nonBets.remove(nonBets.indexOf(state.getBets()[playerNum].get(i)));
        }
        // finds fighter with highest power
        for(int i = 0; i < nonBets.size(); i++){
            if(state.getFighter(nonBets.get(i)).getPower() > max){
                Log.i("power", String.valueOf(state.getFighter(nonBets.get(i)).getPower()));
                for(int j = 0; j < state.getFighterArray().length; j++) {
                    if(state.getFighterArray()[j].getPower() ==  state.getFighter(nonBets.get(i)).getPower()){
                        maxIdx = j;
                    }
                }
                max = state.getFighter(nonBets.get(i)).getPower();
            }
        }
        return maxIdx;
    }

    /**
     * randomNonBetFighter is a helper method that finds the fighter cards
     * that player did not bet on and selects a random one
     * @param state CMGameState
     * @return index of random fighter they did not bet on
     */
    public int randomNonBetFighter(CMGameState state) {
        // set everything to non bets
        for(int i = 0; i < 5; i++) {
            nonBets.add(i);
        }
        // removes the cards that were bet on
        for(int i = 0; i < state.getBets()[playerNum].size(); i++) {
            nonBets.remove(state.getBets()[playerNum].get(i));
        }
        // selects random card it did not bet on
        return nonBets.get(rng.nextInt(nonBets.size()));
    }

}
