package edu.up.cs301.game.cheatymages.Cards;
import java.util.ArrayList;


public class Card {

    //This is a list of size equal to the number of players because cards are shown as
    //face up or down to different players
    public ArrayList<Boolean> isFaceUp;
    public String name;

    public Card(String name, ArrayList<Boolean> isFaceUp) {
        //general attributes of all cards
        this.name = name;
        this.isFaceUp = isFaceUp;
    }
}