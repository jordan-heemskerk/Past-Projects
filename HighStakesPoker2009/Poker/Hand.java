/*****************************************
Name: Hand.java
Author: Jordan Heemskerk
Purpose: Conatins methods for manipulating the arrays of cards that are "hands"
Date: Mon. Dec. 15, 2008
******************************************/


import java.util.*;

public class Hand {
    public final static int NO_PAIR = 0,
                            PAIR = 1,
                            TWO_PAIR = 2,
                            TRIPS = 3,
                            STRAIGHT = 4,
                            FLUSH = 5,
                            FULL_HOUSE = 6,
                            QUADS = 7,
                            STRAIGHT_FLUSH = 8,
                            ROYAL_FLUSH = 9;
    public Hand playerHand = new Hand();
    public Hand computerOneHand = new Hand();
    public Hand computerTwoHand = new Hand();
    public Hand computerThreeHand = new Hand();
    public Hand computerFourHand = new Hand();




    public static String getHandStringLevel(int type, int value){
    String handType = "";
    String handValue = "";
    System.out.println("TYYYYPPPPPEEE is " + type);

    switch (value) {
    case 10: handValue = "Jack"; break;
    case 11: handValue = "Queen"; break;
    case 12: handValue = "King";  break;
    case 13: handValue = "Ace";   break;
    default: handValue = Integer.toString((value + 1)) ; break;
    }
    
    switch (type) {
    case 0: handType = "a High Card of " + handValue; break;
    case 1: handType = "a Pair of " + handValue + "'s";  break;
    case 2: handType = "Two Pair, High " + handValue; break;
    case 3: handType = "Three " + handValue + "'s";         break;
    case 4: handType = "a Straight, High " + handValue; break;
    case 5: handType = "a Flush, High " + handValue; break;
    case 6: handType = "a Full House, House full of " + handValue + "'s";  break;
    case 7: handType = "Four " + handValue + "'s";       break;
    case 8: handType = "a Straight Flush, High " + handValue;  break;
    case 9: handType = "a ROYAL FLUSH, High " + handValue; break;
    default: handType = "unknown number "; break;
    }


   return handType;

    }
    
    
    






    public static String handStringValue(Card cardOne, Card cardTwo, Card cardThree, Card cardFour, Card cardFive, String owner) {
        String cardOneS = "";
        String cardTwoS = "";
        String cardThreeS = "";
        String cardFourS = "";
        String cardFiveS = "";
        int[] cardOneData = new int[2];
        cardOneData = Card.getCardValue(cardOne);
        cardOneS = (Card.getCardStringValue(cardOne));
        int[] cardTwoData = new int[2];
        cardTwoData = Card.getCardValue(cardTwo);
        cardTwoS = (Card.getCardStringValue(cardTwo));
        int[] cardThreeData = new int[2];
        cardThreeData = Card.getCardValue(cardThree);
        cardThreeS = (Card.getCardStringValue(cardThree));
        int[] cardFourData = new int[2];
        cardFourData = Card.getCardValue(cardFour);
        cardFourS = (Card.getCardStringValue(cardFour));
        int[] cardFiveData = new int[2];
        cardFiveData = Card.getCardValue(cardFive);
        cardFiveS = (Card.getCardStringValue(cardFive));



        return (owner + "'s hand consists of  a " + cardOneS + " and a " + cardTwoS + " and a " + cardThreeS + " and a " + cardFourS + " and a " + cardFiveS);



    }  public static Card[] makeHand (Card cardOne, Card cardTwo, Card cardThree, Card cardFour, Card cardFive, String person) {
           Card[] handContents = new Card[5];
           handContents[0] = cardOne;
           handContents[1] = cardTwo;
           handContents[2] = cardThree;
           handContents[3] = cardFour;
           handContents[4] = cardFive;

           return handContents;





    }
    public static int[] getHandLevel (Card[] hand) {
           int pairs = 0;
           int straights = 0;
           int flushes = 0;
           int [] returnValue = new int[3];
           int [] cardPos = new int [5];
           int highCard = 0;
           int highHandCard = 0;
           Card firstCard = hand[0];
           Card secCard = hand[1];
           Card thirdCard = hand[2];
           Card fourCard = hand[3];
           Card fiveCard =  hand [4];
           int[] values = new int[5];
           for (int y = 0; y < 5; y++) {
           values[y] = hand[y].value;
           }
           Arrays.sort(values);
           if (values[0] == 9 && values[1] == 10  &&
              values[2] == 11 && values[3] == 12 && values[4] == 13 && firstCard.suit == secCard.suit && firstCard.suit == thirdCard.suit && firstCard.suit == fourCard.suit && firstCard.suit == fiveCard.suit) {
              System.out.println("ROYAL FLUSH");
                    highHandCard = values[4];
              highCard = values[4];


              returnValue[0] = ROYAL_FLUSH;
              returnValue[1] = highHandCard;
              returnValue[2] = highCard;

              return returnValue;
              }




           if (values[0] == values[1] - 1 && values[0] == values[2] - 2 &&
              values[0] == values[3] - 3 && values[0] == values[4] - 4 && firstCard.suit == secCard.suit && firstCard.suit == thirdCard.suit && firstCard.suit == fourCard.suit && firstCard.suit == fiveCard.suit) {
              System.out.println("STRAIGHT FLUSH");

              highHandCard = values[4];
              highCard = values[4];


              returnValue[0] = STRAIGHT_FLUSH;
              returnValue[1] = highHandCard;
              returnValue[2] = highCard;





              return returnValue;
              }




           if (values[0] == values[1] && values[0] == values[2] && values[0] == values[3] || values[4] == values[3] && values[4] == values[2] && values[4] == values[1]) {
               if (values[0] > values[4]) {
               highHandCard = values[4];
               } else highHandCard = values[0];

              highCard = 0; // DOESNT MATTER IN 4 of A KIND

              returnValue[0] = QUADS;
              returnValue[1] = highHandCard;
              returnValue[2] = highCard;


           return returnValue;
           }



           if (values[0] == values[1] && values[0] == values [2] && values[3] == values[4]|| values[4] == values [3] && values[4] == values[2] && values[0] == values[1] ) {

                  highHandCard = values[4];
              highCard = values[4];

              returnValue[0] = FULL_HOUSE;
              returnValue[1] = highHandCard;
              returnValue[2] = highCard;

            return returnValue;
            }
      if (firstCard.suit == secCard.suit && firstCard.suit == thirdCard.suit && firstCard.suit == fourCard.suit && firstCard.suit == fiveCard.suit) {
                highHandCard = values[4];
              highCard = values[4];

              returnValue[0] = FLUSH;
              returnValue[1] = highHandCard;
              returnValue[2] = highCard;

              return returnValue;
           }
           if (values[0] == values[1] - 1 && values[0] == values[2] - 2 &&
              values[0] == values[3] - 3 && values[0] == values[4] - 4) {

              highHandCard = values[4];
              highCard = values[4];


              returnValue[0] = STRAIGHT;
              returnValue[1] = highHandCard;
              returnValue[2] = highCard;



              return returnValue;
              }
            if (values[0] == values[1] && values[0] == values [2] || values[4] == values[3] && values[4] == values[2] || values[1] == values[2] && values [1] == values[3]) {
                cardPos = getAIDropCards(hand, TRIPS);
            Arrays.sort(hand);
            highHandCard = hand[cardPos[2]].value;

            highCard = values[4];


            returnValue[0] = TRIPS;
              returnValue[1] = highHandCard;
              returnValue[2] = highCard;

            //THREE OF A KIND
            //System.out.println("Three of a kind");
            return returnValue;
            }
            if (values[0] ==  values[1] && values[2] == values[3] || values[1] == values[2] && values[3] == values[4] || values[0] == values[1] && values[3] == values [4]) {

            cardPos = getAIDropCards(hand, TWO_PAIR);
            //Arrays.sort(hand);
            //WOOOOOOORRRKKKK NEEDS TOOOO BE DONE HERER!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!




            highHandCard = hand[cardPos[0]].value;

            highCard = values[4];


            returnValue[0] = TWO_PAIR;
              returnValue[1] = highHandCard;
              returnValue[2] = highCard;


            return returnValue;
            }






           if (firstCard.value == secCard.value || firstCard.value == thirdCard.value || firstCard.value == fourCard.value ||
            firstCard.value == fiveCard.value ||secCard.value == thirdCard.value || secCard.value == fourCard.value ||
            secCard.value == fiveCard.value || thirdCard.value == fourCard.value || thirdCard.value == fiveCard.value ||
            fourCard.value == fiveCard.value) {
              //pair
              //System.out.println("Pair");
              pairs++;
              //System.out.println(pairs);

              highCard = values[4];
              int ref = values[0];


              cardPos = getAIDropCards(hand, PAIR);
              highHandCard = hand[cardPos[0]].value;





              returnValue[0] = PAIR;
              returnValue[1] = highHandCard;
              returnValue[2] = highCard;


              return returnValue;
           }





            else  {
             
             returnValue[0] = NO_PAIR;
             returnValue[1] = values[4];



             //System.out.println("High Card");
             return returnValue; }



    }


    public static int[] getAIDropCards (Card[] hand, int handLevel) {
           Card firstCard = hand[0];
           Card secCard = hand[1];
           Card thirdCard = hand[2];
           Card fourCard = hand[3];
           Card fiveCard =  hand [4];
           Card[] sortedCards = hand;
           Arrays.sort(sortedCards);
           int hearts = 0;
           int clubs = 0;
           int spades = 0;
           int diamonds = 0;
           Card referenceCard = new Card(0,0,0);
           int[] values =  new int[5];
           for (int y = 0; y < 5; y++) {
           values[y] = hand[y].value;
           }
           Arrays.sort(values);


           for (int i = 0; i < hand.length; i++) {
               if (hand[i].suit == 0) {
                   spades++;
               }
               if (hand[i].suit == 1) {
                   hearts++;
               }
               if (hand[i].suit == 2) {
                   diamonds++;
               }
               if (hand[i].suit == 3) {
                   clubs++;
               }


           }

             if (values[0] == values[1] - 1 && values[0] == values[2] - 2 &&
              values[0] == values[3] - 3 || values[1] == values[2] - 1 &&
              values[1] == values[3] - 2 && values[1] == values[4] - 3) {
              
              //System.out.println("ALMOST STRAIGHT");
              
              
              
              

              }







           System.out.println("You have " + spades + " spades and " + hearts + " hearts and " +
                                diamonds + " diamonds and " + clubs + " clubs");





          int randNum = (int)(Math.random() * 100);
          if (clubs == 3 || diamonds == 3 || clubs == 3 || spades == 3) {
              if (randNum < 20) {
                 for (int j = 0; j < hand.length; j++) {
            referenceCard = hand[j];
              for (int i = 0; i < hand.length; i++) {
                  if (referenceCard.equalsSuit(hand[i]) && i != j) {
                  for (int p = 0; p < hand.length; p++) {
                     if (referenceCard.equalsSuit(hand[p]) && i != j && j != p && i != p) {

                     System.out.println("Found Same suit at " + i + " and " +  " and " + p  + " and " + j);
                      //return the the cards of the same suit
                     int[] toReturn = {j,p,i};
                     return toReturn;
                     }


                  }




                  }
              }
                 }


              } //if

          }
          
          if (clubs == 4 || diamonds == 4 || clubs == 4 || spades == 4) {
                     for (int j = 0; j < hand.length; j++) {
            referenceCard = hand[j];
              for (int i = 0; i < hand.length; i++) {
                  if (referenceCard.equalsSuit(hand[i]) && i != j) {
                  //System.out.println("found 2 s");
                  //System.out.println("at position" + j + " and " + i);
                  for (int c = 0; c < hand.length; c++) {
                  if (!(hand[c].equalsSuit(hand[j]) && hand[c].equalsValue(hand[i])  && c != i && c != j && j != i))
                  referenceCard = hand[c];
                  for (int a = 0; a < hand.length; a++) {
                  if (referenceCard.equalsSuit(hand[a]) && c != i && c != j && j != i && a != c && a != j && a != i) {
                  //System.out.println("found same suit");
                  //System.out.println("at position" + a + " and " + c);
                  int[] toReturn = {i,j,a,c};
                  return toReturn;
                  }
                  }



                  }

                  }


              }
            }








              //return the the cards of the same suit


          }












             if (handLevel == 3) {
            //TRIPS
              for (int j = 0; j < hand.length; j++) {
            referenceCard = hand[j];
              for (int i = 0; i < hand.length; i++) {
                  if (referenceCard.equalsValue(hand[i]) && i != j) {
                  for (int p = 0; p < hand.length; p++) {
                     if (referenceCard.equalsValue(hand[p]) && i != j && j != p && i != p) {

                    // System.out.println("Found Trips at " + i + " and " +  " and " + p  + " and " + j);
                     int[] toReturn = {j,p,i};
                     return toReturn;
                     }


                  }


                  }


              }
            }






            }


            if (handLevel == 2) {
                    for (int j = 0; j < hand.length; j++) {
            referenceCard = hand[j];
              for (int i = 0; i < hand.length; i++) {
                  if (referenceCard.equalsValue(hand[i]) && i != j) {
                  //System.out.println("FOUNDPAIR");
                 // System.out.println("at position" + j + " and " + i);
                  for (int c = 0; c < hand.length; c++) {
                  if (!(hand[c].equalsValue(hand[j]) && hand[c].equalsValue(hand[i])  && c != i && c != j && j != i))
                  referenceCard = hand[c];
                  for (int a = 0; a < hand.length; a++) {
                  if (referenceCard.equalsValue(hand[a]) && c != i && c != j && j != i && a != c && a != j && a != i) {
                 // System.out.println("FOUNDPAIR");
                 // System.out.println("at position" + a + " and " + c);
                  int[] toReturn = {i,j,a,c};
                  return toReturn;
                  }
                  }
                  


                  }

                  }


              }
            }





            
            }





           //If there is a pair, return the pair position, so that every card but the pair is exchanged


           if (handLevel == 1) {
           // System.out.println("PAIR");
            for (int j = 0; j < hand.length; j++) {
            referenceCard = hand[j];
              for (int i = 0; i < hand.length; i++) {
                  if (referenceCard.equalsValue(hand[i]) && i != j) {
                 // System.out.println("FOUNDPAIR");
                //  System.out.println("at position" + j + " and " + i);
                  int[] toReturn = {j,i};
                  return toReturn;
                  }


              }
            }









            }



        int index = 0;
        int reference = hand[0].value;
        int length = hand.length;
        for (int i = 0; i < length  ; i++) {
            if (hand[i].value >= reference) {
                reference = hand[i].value;
                index = i;
            }

        }
        System.out.println(index);
        if (reference > 10) {
        System.out.println(index);
        int[] toReturn = {index};
        return toReturn;
        } else {
        int[] toReturn = {-1};
        return toReturn;
        }









    }











}