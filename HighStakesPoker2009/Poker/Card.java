/*****************************************
Name: Card.java
Author: Jordan Heemskerk
Purpose: A card object that is used throughut the game
Date: Mon. Dec. 15, 2008
******************************************/

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

public class Card
implements Comparable<Card> {
public final static int SPADES = 0,
                            HEARTS = 1,
                            DIAMONDS = 2,
                            CLUBS = 3;
                            
    public final static int ACE = 13,
                            JACK = 10,
                            QUEEN = 11,
                            KING = 12;

    public final int suit;

    public final int value;
    
    public int handPosition; //the position in the hand that the card occupies

    public Card(int theValue, int theSuit, int thePosition) {

        value = theValue;
        suit = theSuit;
        handPosition = thePosition;
    }
    /***********************
    getCardValue()
    Returns the value of the card
    ***********************/
    public static int[] getCardValue(Card card) {
        int[] cardValue = new int[2];
        cardValue[0] = card.suit;
        cardValue[1] = card.value;
        return cardValue;
    }
    
    /*****************************
    compareTo()
    The method that allows an array of cards to be sorted
    ******************************/
    public int compareTo (Card card) {
      if( this.value < card.value) {
        return 1;
      }
      if (this.value ==  card.value) {
        return 0;
      }
      if (this.value > card.value) {
        return -1;
      }
       return 10;
    }

   /************************
   equalsValue()
   Returns true if the card value is equal to another card value.
   *************************/

    public boolean equalsValue(Card card) {
       if (this.value == card.value) {
         return true;
       }
       return false;



    }
    /************************
   equalsValue()
   Returns true if the card suit is equal to another card suit.
   *************************/
    public boolean equalsSuit(Card card) {
      if (this.suit == card.suit) {
        return true;
      }
      return false;
    }

    /********************************
    getCardStringValue()
    Returns the cards string value, such as "king of hearts"
    ********************************/
    public static String getCardStringValue(Card card) {
        int[] cardData = new int[2];
        String suit = "";
        String value = "";
        cardData = getCardValue(card);
        switch (cardData[0]) {
          case Card.SPADES: suit = "spades"; break;
          case Card.DIAMONDS: suit = "diamonds"; break;
          case Card.CLUBS: suit = "clubs"; break;
          case Card.HEARTS: suit = "hearts"; break;
          default: suit = "unknown suit"; break;
        }
        switch (cardData[1]) {
          case Card.ACE: value = "ace"; break;
          case Card.KING: value = "king"; break;
          case Card.QUEEN: value = "queen"; break;
          case Card.JACK: value = "jack"; break;
          default: value = Integer.toString(cardData[1] + 1); break;
        }
        return value + " of " + suit;



    }
} //CArd

