package edu.up.cs301;

import junit.framework.TestCase;

import org.junit.Test;

import java.util.ArrayList;
import edu.up.cs301.game.cheatymages.CMGameState;
import edu.up.cs301.tictactoe.infoMessage.TTTState;

import static org.junit.Assert.assertTrue;

public class CMGameStateTest extends TestCase {

    @Test
    public void testPlaceBet() {
        CMGameState state = new CMGameState(3);
        ArrayList<Integer> bet0 = new ArrayList<>();
        ArrayList<Integer> bet1 = new ArrayList<>();
        ArrayList<Integer> bet2 = new ArrayList<>();
        bet0.add(1);
        bet0.add(2);
        bet0.add(3);
        bet1.add(3);
        bet1.add(4);
        bet2.add(5);
        state.placeBet(0, bet0);
        assertEquals(state.getPlayerTurn(), -1);
        state.placeBet(1, bet1);
        assertEquals(state.getPlayerTurn(), -1);
        state.placeBet(2, bet2);
        assertNotSame(state.getPlayerTurn(), -1);
        assertEquals((int) state.getBets()[0].get(0), 1);
        assertEquals((int) state.getBets()[0].get(1), 2);
        assertEquals((int) state.getBets()[0].get(2), 3);
        assertEquals((int) state.getBets()[1].get(0), 3);
        assertEquals((int) state.getBets()[1].get(1), 4);
        assertEquals((int) state.getBets()[2].get(0), 5);
    }
    @Test
    public void testPass() {
        CMGameState state = new CMGameState(3);
        ArrayList<Integer> bets = new ArrayList<>();
        bets.add(1);
        bets.add(2);
        bets.add(3);
    }
    @Test
    public void testPlaySpellCard() {
        CMGameState state = new CMGameState(3);
        state.setPlayerTurn(0);
        assertEquals(state.getHands()[0].size(), 8);
        assertEquals(state.getAttachedSpells()[0].size(), 0);
        state.playSpellCard(0, 0, 0);
        assertEquals(state.getHands()[0].size(), 7);
        assertEquals(state.getAttachedSpells()[0].size(), 1);
    }
    @Test
    public void testDetectMagic() {

    }

    /**
     * Tests the discardCards method to see if it works properly when player
     * discards 0, 4, and 8 cards
     * @author Tyler Uyeno
     */
    @Test
    public void testDiscardCards() {
        CMGameState state = new CMGameState(3);
        //////Edge case testing if player discards 0 cards//////

        //Checking hand size before
        assertEquals(state.getHands()[0].size(), 8);
        //discards no cards
        ArrayList<Integer> discardTest1 = new ArrayList<>();
        state.discardCards(0, discardTest1);
        //Makes sure hand size remains the same
        assertEquals(state.getHands()[0].size(), 8);
        //Makes sure no cards are added to discard pile
        assertEquals(state.getDiscardPile().size(), 0);

        //////Normal case testing if player discards 4 cards//////
        assertEquals(state.getHands()[0].size(), 8);
        //Selecting 4 cards from hand to discard
        ArrayList<Integer> discardTest2 = new ArrayList<>();
        discardTest2.add(7);
        discardTest2.add(5);
        discardTest2.add(4);
        discardTest2.add(1);
        //Player 0 discards cards
        state.discardCards(0, discardTest2);
        //Hand size should be the same since the cards are being replaced
        assertEquals(state.getHands()[0].size(), 8);
        //Checks if cards are put into discard pile
        assertEquals(state.getDiscardPile().size(), 4);

        //////Edge case testing if player discards 8 cards//////
        assertEquals(state.getHands()[0].size(), 8);
        //Selecting all 8 cards from hand to discard
        ArrayList<Integer> discardTest3 = new ArrayList<>();
        discardTest3.add(7);
        discardTest3.add(6);
        discardTest3.add(5);
        discardTest3.add(4);
        discardTest3.add(3);
        discardTest3.add(2);
        discardTest3.add(1);
        discardTest3.add(0);
        //Player 0 discards cards
        state.discardCards(0, discardTest3);
        //Hand size should only be 4 now since you can only redraw 4 cards
        assertEquals(state.getHands()[0].size(), 4);
        //Checks if cards are put into discard pile
        //Should be 10 now since 4 was discarded earlier
        assertEquals(state.getDiscardPile().size(), 12);

    }

}