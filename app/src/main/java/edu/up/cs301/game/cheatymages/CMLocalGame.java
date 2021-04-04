package edu.up.cs301.game.cheatymages;

import edu.up.cs301.game.GameFramework.LocalGame;
import edu.up.cs301.game.GameFramework.actionMessage.GameAction;
import edu.up.cs301.game.GameFramework.players.GamePlayer;

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

    @Override
    protected boolean makeMove(GameAction action) {
        return false;
    }
}
