package edu.up.cs301.game.cheatymages;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

import edu.up.cs301.game.GameFramework.LocalGame;
import edu.up.cs301.game.GameFramework.actionMessage.GameAction;
import edu.up.cs301.game.GameFramework.players.GamePlayer;
import edu.up.cs301.game.cheatymages.Actions.*;

class CMLocalGame extends LocalGame {

    /**
     * CM Local Game Constructor
     * @param numPlayers
     */
    public CMLocalGame(int numPlayers){
        super();
        super.state = new CMGameState(numPlayers);
    }

    @Override
    protected void sendUpdatedStateTo(GamePlayer p) {

    }

    @Override
    //I don't think this function is actually usable in our game because of how playerTurn works
    //Perhaps it might still be used (or must be used depending on the framework
    //Regardless it must stay here for the code to run even if unused
    protected boolean canMove(int playerIdx) {

        return false;
    }

    @Override
    protected String checkIfGameOver() {
        //The round number should be 4 if the game is over
        if(((CMGameState) state).getRoundNum() > 3){

            //Gets a list of players in order of gold
            int[] leaderBoard = getSortedIndices(((CMGameState) state).getGold());

            //TODO IT MIGHT MAKE MORE SENSE FOR THIS TO ADD THE NAMES OF THE PLAYERS
            //Adds each player's number to the return string in order of gold
            String returnStr = "";
            for(int i = 0; i < leaderBoard.length - 1; i++){
                returnStr += Integer.toString(leaderBoard[i]) + ", ";
            }
            returnStr += Integer.toString(leaderBoard[leaderBoard.length - 1]);

            return returnStr;
        }
        return null;
    }

    /**
     * Performs a bubble sort on an array of numbers
     * @param arr the array you wish to sort
     * @return an array of indices corresponding to the elements in arr sorted using arr
     */
    //This is a helper function used in checkIfGameOver
    //Not the most efficient or elegant solution but for this instance it works just fine
    private int[] getSortedIndices(int[] arr) {

        int[] indices = new int[arr.length];
        int temp;
        //tracks if the sort has swapped during the current sweep
        boolean hasSwapped = true;

        //initializes the indices array
        for (int i = 0; i < arr.length; i++) {
            indices[i] = i;
        }

        //does sweeps until it finishes a sweep without doing any swaps
        while(hasSwapped == true) {
            hasSwapped = false;

            //loops through each pair of elements in indices
            for(int i = 1; i < arr.length; i++) {

                //if the current pair of elements are in the wrong order they are swapped
                if(arr[indices[i] - 1] > arr[indices[i]]){
                    temp = indices[i];
                    indices[i] = indices[i-1];
                    indices[i-1] = temp;
                    hasSwapped = true;
                }
            }
        }
        return indices;
    }

    /**
     * Checks an array list for duplicates
     * @param arr the array list you wish to check for duplicates
     * @return true if the array list has duplicates
     */
    private boolean hasDuplicates(ArrayList<Integer> arr){

        ArrayList<Integer> newArr = new ArrayList<Integer>();

        for(int e : arr){
            if(newArr.contains(e)){
                return true;
            }
            newArr.add(e);
        }

        return false;
    }

    @Override
    protected boolean makeMove(GameAction action) {

        //This is for code cleanliness
        CMGameState cmState = (CMGameState) state;
        int playerId = getPlayerIdx(action.getPlayer());

        if(action instanceof PassAction){

            //If it's this player's turn they can pass
            if(cmState.getPlayerTurn() == playerId){
                cmState.pass();
                return true;
            }

        }

        if(action instanceof PlaySpellAction){

            //Again for cleanliness
            PlaySpellAction spellAction = (PlaySpellAction) action;

            //If it is not the player's turn the move is invalid
            if(cmState.getPlayerTurn() != playerId){
                return false;
            }

            //If the spell index is out of range in the player's hand the move is invalid
            if(spellAction.getSpell() < 0 || spellAction.getSpell() >= cmState.getHands()[playerId].size()){
                return false;
            }

            //TODO CURRENTLY CODE ASSUMES THE SPELL IS TARGETING A FIGHTER
            //If the target is invalid the mode is invalid
            if(spellAction.getTarget() < 0 || spellAction.getTarget() >= 5){
                return false;
            }

            if(action instanceof DetectMagicAction){
                cmState.detectMagic(playerId, spellAction.getSpell(), spellAction.getTarget());
            }
            else{
                cmState.playSpellCard(playerId, spellAction.getSpell(), spellAction.getTarget());
            }

        }

        if(action instanceof BetAction){

            BetAction betAction = (BetAction) action;

            //If it's not the betting phase the move is invalid
            if(cmState.getPlayerTurn() != -1){
                return false;
            }

            //If there's more than 3 bets the move is invalid
            if(betAction.getBets().size() > 3){
                return false;
            }

            //If there are any duplicate bets the move is invalid
            if(hasDuplicates(betAction.getBets())){
                return false;
            }

            //Checks if the bets are valid indices in the fighters array
            for(int i : betAction.getBets()){
                if(i < 0 || i >= 5){
                    return false;
                }
            }

            cmState.placeBet(playerId, betAction.getBets());

        }

        if(action instanceof DiscardCardsAction){

            DiscardCardsAction discardAction = (DiscardCardsAction) action;

            //If there's any duplicates the action is invalid
            if(hasDuplicates(discardAction.getDiscards())){
                return false;
            }

            //Checks if all the card indices are valid
            for(int i : discardAction.getDiscards()){
                if(i < 0 || i >= cmState.getHands()[playerId].size()){
                    return false;
                }
            }

            //TODO THIS CODE SHOULD BE REDUNDANT AND REMOVED EVENTUALLY
            //Checks to make sure the cards are listed in the correct order
            ArrayList<Integer> temp = discardAction.getDiscards();
            Collections.sort(temp);
            Collections.reverse(temp);
            if(!discardAction.getDiscards().equals(temp)){
                return false;
            }

            //TODO THIS FUNCTIONS RETURN VALUE SHOULD BE SOMEHOW SENT TO THE UI TO FLASH THE REVEALED CARDS
            cmState.discardCards(playerId, discardAction.getDiscards());

        }

        return false;
    }
}
