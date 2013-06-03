 /*****************************************
Name: Deck.java
Author: Jordan Heemskerk
Purpose: The deck object is used to contain an array of cards.
         This class contains kmethods for manipulating this array.
Date: Mon. Dec. 15, 2008
******************************************/



 public class Deck {

    public static Card[] deck;   // An array of 52 Cards, representing the deck.
    public static int cardsUsed; // How many cards have been dealt from the deck.
    /****************************************
    createNewDeck()
    Makes a new array of cards that is like a deck
    ****************************************/
    public void createNewDeck() {
           // Create an unshuffled deck of cards.
       deck = new Card[52];
       int cardCt = 0; // How many cards have been created so far.
       for ( int suit = 0; suit <= 3; suit++ ) {
          for ( int value = 1; value <= 13; value++ ) {
             deck[cardCt] = new Card(value,suit, 0);
             cardCt++;
          }
       }
       cardsUsed = 0;
    }
    /*********************************
    shuffle()
    Randomizes the order of a deck of cards.
    *********************************/
    public static void shuffle() {
        for ( int i = 51; i > 0; i-- ) {
            int rand = (int)(Math.random()*(52));
            Card temp = deck[i];
            deck[i] = deck[rand];
            deck[rand] = temp;
        }
        cardsUsed = 0;
    }

       public int cardsLeft() {
          // As cards are dealt from the deck, the number of cards left
          // decreases.  This function returns the number of cards that
          // are still left in the deck.
        return 52 - cardsUsed;
    }
    /***********************************
    dealCard()
    Returns (deals) a card.
    ************************************/
    public static Card dealCard() {
           if (cardsUsed == 52){

           shuffle();
           }
        cardsUsed++;
        return deck[cardsUsed - 1];
    }





}






