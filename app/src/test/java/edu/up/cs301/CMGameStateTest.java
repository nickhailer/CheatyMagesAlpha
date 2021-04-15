package edu.up.cs301;

import junit.framework.TestCase;

import org.junit.Test;

import java.util.ArrayList;
import edu.up.cs301.game.cheatymages.CMGameState;

public class CMGameStateTest extends TestCase {


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


    public void testPlaySpellCard() {
        CMGameState state = new CMGameState(3);
        state.setPlayerTurn(2);
        assertEquals(state.getHands()[0].size(), 8);
        assertEquals(state.getAttachedSpells()[0].size(), 0);
        state.playSpellCard(0, 0, 0);
        assertEquals(state.getHands()[0].size(), 7);
        assertEquals(state.getAttachedSpells()[0].size(), 1);
        assertEquals(state.getPlayerTurn(), 0);
    }

    @Test
    public void testDetectMagic() {
    }

    @Test
    public void testDiscardCards() {
    }

}