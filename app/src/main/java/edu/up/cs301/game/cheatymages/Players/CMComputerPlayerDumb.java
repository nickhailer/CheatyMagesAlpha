package edu.up.cs301.game.cheatymages.Players;

import java.util.ArrayList;
import java.util.Random;

import edu.up.cs301.game.GameFramework.LocalGame;
import edu.up.cs301.game.cheatymages.Actions.*;
import edu.up.cs301.game.GameFramework.infoMessage.*;
import edu.up.cs301.game.GameFramework.players.GameComputerPlayer;
import edu.up.cs301.game.cheatymages.CMGameState;

public class CMComputerPlayerDumb extends GameComputerPlayer {

    protected Random rng;

    /**
     * constructor
     *
     * @param name the player's name (e.g., "John")
     */
    public CMComputerPlayerDumb(String name) {
        super(name);
        rng = new Random();
    }

    @Override
    protected void receiveInfo(GameInfo info) {

        //If it's not your turn don't do anything
        if(info instanceof NotYourTurnInfo){
            return;
        }

        //Makes sure the info message is a game state before sending an action
        if(!(info instanceof GameState)){
            return;
        }

        CMGameState state = (CMGameState) info;

        //Simulates the computer thinking
        sleep(1);

        //If it's betting phase place a random bet
        if(state.getPlayerTurn() == -1){
            ArrayList<Integer> bets = new ArrayList<>();
            for(int i = 0; i < rng.nextInt(3) + 1; i++){
                bets.add(rng.nextInt(5));
            }
            game.sendAction(new BetAction(this, bets));
        }

        //If it's discarding phase choose to discard no cards
        if(state.getPlayerTurn() == -2){
            game.sendAction(new DiscardCardsAction(this, new ArrayList<Integer>()));
        }

        //The dumb AI has a 10% chance of passing
        if(rng.nextInt(10) == 0){
            game.sendAction(new PassAction(this));
            return;
        }

        //Otherwise play a spell from your hand
        int handSize = state.getHands()[playerNum].size();
        if(handSize > 0){
            game.sendAction(new PlaySpellAction(this, rng.nextInt(handSize), rng.nextInt(5)));
            return;
        }

        //If you can't do anything else pass
        game.sendAction(new PassAction(this));
        return;
    }
}
