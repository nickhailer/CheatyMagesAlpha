package edu.up.cs301.cheatymages.Cards;

public class FighterCard extends Card {

    // Instance Variables
    private int power;
    private int prizeMoney;
    private boolean isUndead;

    /**
     * Fighter Card Constructor
     * @param name
     * @param power
     * @param prizeMoney
     * @param isUndead
     */
    public FighterCard(String name, int power, int prizeMoney, boolean isUndead) {
        super(name);
        this.power = power;
        this.prizeMoney = prizeMoney;
        this.isUndead = isUndead;
    }

    public int getPower() {
        return power;
    }

    public int getPrizeMoney() {
        return prizeMoney;
    }

    public boolean isUndead() {
        return isUndead;
    }
}