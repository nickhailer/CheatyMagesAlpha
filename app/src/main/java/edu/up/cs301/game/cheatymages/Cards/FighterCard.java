package edu.up.cs301.game.cheatymages.Cards;

public class FighterCard extends Card {

    // Instance Variables
    public int power;
    public int prizeMoney;
    public boolean isUndead;

    /**
     * Fighter Card Constructor
     * @param name
     * @param numPlayers
     * @param power
     * @param prizeMoney
     * @param isUndead
     */
    public FighterCard(String name, int numPlayers, int power, int prizeMoney, boolean isUndead) {
        super(name);
        this.power = power;
        this.prizeMoney = prizeMoney;
        this.isUndead = isUndead;
    }

}