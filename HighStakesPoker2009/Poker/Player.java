/*****************************************
Name: Player.java
Author: Jordan Heemskerk
Purpose: A player object is used to store 
         attributes specific to each player 
         in the game.
Date: Mon. Dec. 15, 2008
******************************************/


import java.util.*;
public class Player {
    public  boolean isfolded;
    public  Card[] hand;
    public  int amount;
    public  int identifier;
    public  int highestCardValue;
    public  boolean isBankrupt;
    public String name;
    public boolean human = false;


    public Player (Card[] inHand, boolean inFolded, int inAmount) {
        hand = inHand;
        isfolded = inFolded;
        amount = inAmount;
        isBankrupt = false;
        //identifier = inIdentifier;
    }
    /***************************************
    setHuman()
    Sets this player to be human instead of AI
    ****************************************/
    public void setHuman (boolean humanIn) {
      this.human = humanIn;
    }
    /*********************************
    signIn()
    Signs in the player with a name and an amount
    **********************************/
    public void signIn(String nameI, int amountI) {
      this.name = nameI;
      this.amount = amountI;

    }
    /************************
    fold()
    Sets a player to folded
    ************************/
    public void fold() {
      isfolded = true;
    }
    /*************************
    getHighestCardValue() 
    Returns this players highest card value
    ************************/
    public void getHighestCardValue (Card[] handIn ) {
         int[] values = new int[5];
           for (int y = 0; y < 5; y++) {
           values[y] = handIn[y].value;
           }
           Arrays.sort(values);


        System.out.println(values[values.length - 1]);
        this.highestCardValue = values[values.length - 1];

    }
}