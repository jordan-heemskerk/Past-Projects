/*****************************************
Name: Poker.java
Author: Jordan Heemskerk
Purpose: The main class for the game. This runs the game and
         calls all the methods to allow the game to run smoothly.
         It also contains most of the AI formuals.
Date: Mon. Dec. 15, 2008
******************************************/




import  sun.audio.*;
import  java.io.*;
import java.net.*;
import javax.sound.sampled.*;
import javax.swing.*;
import java.util.*;






public class Poker {

           final static int FOLD = 0,
                     CALL = 1,
                     RAISE = 2,
                     BLUFF = 3,
                     CHECK = 4;


           static HighScoreTransferable HST[] = new HighScoreTransferable[30]; //Store the high score values
           static String sound = "";
           static Card[] playerHand = new Card[5]; //players hand
           static Card[] comp1Hand = new Card[5];  //comp1 or player 2's hand
           static Card[] comp2Hand = new Card[5];
           static Card[] comp3Hand = new Card[5];
           static Card[] comp4Hand = new Card[5];
           static Deck gameDeck = new Deck();  //an array of cards
           static int betToCall = 0;
           static Player player;
           static Player comp1;
           static Player comp2;
           static Player comp3;
           static Player comp4;
           static Screen screen = new Screen();
           static boolean soundOn = true;  //For the sound On/off button
           static boolean moved = false;   // used when waiting for input
           static Clip clip;
           static boolean isPlayerMove = false;   //is it the players move?
           static boolean isPlayer2Move = false;  //is it the 2nd players move?
           static boolean successfulMove = true;
           static boolean droppingCards;   //Are we in the dropping cards stage of the game
           static int numPlayers = 0;
           static boolean isPlayer1Turn = false;
           static boolean isPlayer2Turn = false;

  /*******************
  main()
  Pretty much creates 
  variables and starts 
  other methods that 
  run the game.
  *********************/



  public static void main(String[] args) {
  boolean restartSound =  false;

  
  
  
  sound = "sound1.wav"; // change to spooky, danting sound
  playSound();
  comp1 = new Player(comp1Hand, false, 5000);
  comp2 = new Player(comp2Hand, false, 5000);
  comp3 = new Player(comp3Hand, false, 5000);
  comp4 = new Player(comp4Hand, false, 5000);
  player = new Player(playerHand, false, 5000);
  
  do {
    Screen.cardButtonPanel.setVisible(false);
    Screen.gameStage = 0;
    screen.createAndShowGUI();
    Screen.update();
    updateHighScores();
    Wait.forInput();
    if (Screen.gameStage == 3) {
      Screen.update();
      Wait.forInput();
      restartSound =  false;
    } //game stage 3
    else if (Screen.gameStage == 4) {
      Screen.update();
      Wait.forInput();
      restartSound =  false;
    } //game stage 4
    
    else if ( Screen.gameStage == 1) {
      Screen.frame.dispose();
      Poker.deal(); //dummy deal... a null pointer exception is thrown becuz the hands have not been intialized
      Poker.getPlayerName();
      Screen.gameStage = 1;
      Poker.screen.createAndShowGUI();
      Screen.update();
      Poker.newGame(numPlayers);
      restartSound =  true;
      JOptionPane.showMessageDialog(screen.frame, "Please wait while your score is saved");
      Poker.writeNewHighscore(Screen.playerName, Screen.player1am);
      if (Poker.numPlayers == 2) {
        Poker.writeNewHighscore(Screen.player2Name, Screen.comp1am);
      } //if
      JOptionPane.showMessageDialog(screen.frame, "Your score was saved!");
      if(restartSound) {
        clip.stop();
        clip.flush();
        sound = "sound1.wav";
        playSound();
      } // if
    } // if 2 players
    Screen.optionsPanel.setVisible(false);
  } while (true);
  } //main
  /********************************
  newGame()
  This method runs the basic format 
  of a poker game.
  *********************************/

  public static void newGame(int players) {
  clip.stop();
  clip.flush();
  clip.close();
  sound = "sound3.wav";
  playSound();
  Screen.update();
  assignMoney();
  comp1 = new Player(comp1Hand, false, 5000);
  comp2 = new Player(comp2Hand, false, 5000);
  comp3 = new Player(comp3Hand, false, 5000);
  comp4 = new Player(comp4Hand, false, 5000);
  player = new Player(playerHand, false, 5000);
  Screen.cardButtonPanel.setVisible(false);
  do {

    if (players == 1) {
      player.setHuman(true);
    } else if (players == 2 ) {
      player.setHuman(true);
      comp1.setHuman(true);
    }
    Screen.optionsPanel.setVisible(true);
    betToCall = 0;
    Screen.comp1amBet = 0;
    Screen.comp2amBet = 0;
    Screen.comp3amBet = 0;
    Screen.comp4amBet = 0;
    Screen.player1amBet = 0;
    
    
    deal();
    
    
    
    
    paintAllHands(false);
    
    if (players == 1) Screen.paintHand(1, playerHand, 350,505, true);
    else if (players == 2) {
      Screen.paintHand(1, playerHand, 350,505, true);
      Screen.paintHand(2, comp1Hand, 25,375, true);
    } //else if 2 players
    Screen.update();
    ante(50);
    updateMessages("Everyone antes $50");
    Screen.update();
    bettingRound(comp1Hand, comp2Hand, comp3Hand, comp4Hand);
    isPlayer1Turn = true;
    //comp1Hand = TRIPS;
    Screen.updateClickCards();
    if (!player.isfolded) {
      updateMessages(Screen.playerName + ", please choose what cards to drop");
      Screen.cardButtonPanel.setVisible(true);
      Wait.forInput();
      isPlayer1Turn = false;
    }//if
    if (players == 2 && !comp1.isfolded) {
      updateMessages(Screen.player2Name + ", please choose what cards to drop");
      isPlayer2Turn = true;
      Screen.updateClickCards();
      if(!comp1.isfolded && players == 2)Screen.cardButtonPanel.setVisible(true);
      Wait.forInput();
      isPlayer2Turn = false;
    } //if
    
    paintAllHands(false);
    Screen.cardButtonPanel.setVisible(false);
    if (players == 2) {
      Screen.paintHand(1, playerHand, 350,505, true);
      Screen.paintHand(2, comp1Hand, 25,375, true);
    }
    Screen.update();
    dropAICards();
    Screen.update();
    paintAllHands(false);
    if (players == 2) {
      Screen.paintHand(1, playerHand, 350,505, true);
      Screen.paintHand(2, comp1Hand, 25,375, true);
    }
    Screen.update();
    bettingRound(comp1Hand, comp2Hand, comp3Hand, comp4Hand);
    Screen.update();
    paintAllHands(true);
    if (players == 2) {
      Screen.paintHand(1, playerHand, 350,505, true);
      Screen.paintHand(2, comp1Hand, 25,375, true);
    }
    Screen.update();
    determineWinner();
    Screen.update();
    if (!(Screen.player1am <= 0)) {
      player.isfolded = false;
    } else { 
      player.isfolded = true;
      player.isBankrupt = true;
    }
    if (!(Screen.comp1am <= 0)){
      comp1.isfolded = false; 
    } else {
      comp1.isBankrupt = true;
      comp1.isfolded = true;
    }
    if (!(Screen.comp2am <= 0)){
      comp2.isfolded = false;
    } else {
      comp2.isBankrupt = true;
      comp2.isfolded = true;
    }
    if (!(Screen.comp3am <= 0)){
      comp3.isfolded = false; 
    } else {
      comp3.isBankrupt = true;
      comp3.isfolded = true;
    }
    if (!(Screen.comp4am <= 0)){
      comp4.isfolded = false;
    }else {
      comp4.isBankrupt = true;
      comp4.isfolded = true;
    }
  } while (players == 2  ?  !player.isBankrupt && !comp1.isBankrupt || !comp2.isBankrupt && !comp3.isBankrupt && !comp4.isBankrupt : !player.isBankrupt || !comp1.isBankrupt && !comp2.isBankrupt && !comp3.isBankrupt && !comp4.isBankrupt );



  }//newGame


  /**************************************
  determineWinner() 
  This method finds who the winner of the 
  round is.
  ***************************************/


  public static void determineWinner() {
  int[] playerHandLevel = new int[3]; // These arrays of integers include three numbers:
  int[] comp1HandLevel = new int[3];  // The first is their hand rank, such as Pair(1)
  int[] comp2HandLevel = new int[3];  // The Second is the highest card in their pair or whatever
  int[] comp3HandLevel = new int[3];  // The third is the highest card overall throughout their whole hand
  int[] comp4HandLevel = new int[3];  //
  int highestHandLevel = 0;//The highest hand Level (such as Four of a kind)
  int highestHandCard = 0; //The highest hand card is the highest of all the highest cards in the pair ow whatever
  int highestCard = 0;  // the highest of the highest cards in each persons hand
  int playersWithHighHandLevel = 0;  // the numbers of players with the highest hand level
  int[] pairPos =  new int[5];   // the position of the pairs in a persons hand
  int currentHighestCardValue = 0;
  playerHandLevel = Hand.getHandLevel(playerHand); // the players hand level
  int[] valuesOfHighCard = new int[5]; // the combined values of each persons hand stored as an array
  int playersWithHighValue = 0; // the highest  of all the highest cards in the pair or whatever the person has
  int ref2 = 0; // references for searching through loops
  int ref3 = 0; // another reference
  String winner = "";
  //check to make sure people aren't folded
  if (player.isfolded && comp1.isfolded && comp2.isfolded && comp3.isfolded) {
    updateMessages("Jesus won this round");
    winner = "Jesus won this round";
    Screen.comp4am +=Screen.potTotal;
    Screen.potTotal = 0;
  }
  if (player.isfolded && comp1.isfolded && comp2.isfolded && comp4.isfolded) {
    updateMessages("Fabio won this round");
    winner = "Fabio won this round";
    Screen.comp3am +=Screen.potTotal;
    Screen.potTotal = 0;
  }
  if  (player.isfolded && comp1.isfolded && comp3.isfolded && comp4.isfolded) {
    updateMessages("Napolean won this round");
    winner = "Napolean won this round";
    Screen.comp2am +=Screen.potTotal;
    Screen.potTotal = 0;
  }
  if (numPlayers == 1 && player.isfolded && comp2.isfolded && comp3.isfolded && comp4.isfolded) {
    if (numPlayers == 1) {
      updateMessages("Forrester won this round");
      winner = "Forrester won this round";
    } else  {
      updateMessages(Screen.player2Name + ", you won this round");
      winner = Screen.player2Name + ", you won this round";
    }
    Screen.comp1am +=Screen.potTotal;
    Screen.potTotal = 0;
  }
  //System.out.println("PLAYER HAND LEVEL" + playerHandLevel);
  comp1HandLevel = Hand.getHandLevel(comp1Hand);
  comp2HandLevel = Hand.getHandLevel(comp2Hand);
  comp3HandLevel = Hand.getHandLevel(comp3Hand);
  comp4HandLevel = Hand.getHandLevel(comp4Hand);
  highestHandLevel = playerHandLevel[0];
  highestHandLevel = comp1HandLevel[0] > highestHandLevel ? comp1HandLevel[0] : highestHandLevel;
  highestHandLevel = comp2HandLevel[0] > highestHandLevel ? comp2HandLevel[0] : highestHandLevel;
  highestHandLevel = comp3HandLevel[0] > highestHandLevel ? comp3HandLevel[0] : highestHandLevel;
  highestHandLevel = comp4HandLevel[0] > highestHandLevel ? comp4HandLevel[0] : highestHandLevel;
  
  
  if(playerHandLevel[0] == highestHandLevel) playersWithHighValue++;
  if(comp1HandLevel[0] == highestHandLevel) playersWithHighValue++;
  if(comp2HandLevel[0] == highestHandLevel) playersWithHighValue++;
  if(comp3HandLevel[0] == highestHandLevel) playersWithHighValue++;
  if(comp4HandLevel[0] == highestHandLevel) playersWithHighValue++;
  
  if  (playersWithHighValue > 1) {
    if (playerHandLevel [0] == highestHandLevel) {
      valuesOfHighCard[0] = playerHandLevel[1];
    }
    if (comp1HandLevel [0] == highestHandLevel) {
      valuesOfHighCard[1] = comp1HandLevel[1];
    }
    if (comp2HandLevel [0] == highestHandLevel) {
      valuesOfHighCard[2] = comp2HandLevel[1];
    }
    if (comp3HandLevel [0] == highestHandLevel) {
      valuesOfHighCard[3] = comp3HandLevel[1];
    }
    if (comp4HandLevel [0] == highestHandLevel) {
      valuesOfHighCard[4] = comp4HandLevel[1];
    }

    int highestLevel = 0;

    
    
    
    ref2 = valuesOfHighCard[0];
    int i = 0;
    //find highest values of high cards
    for (; i < 5; i++ ){
      if (valuesOfHighCard[i] > ref2 && i != 0) {
        ref2 = valuesOfHighCard[i];
      }
    }
    //Make People win
    if (playerHandLevel[1] == ref2 && !player.isfolded) {
      updateMessages(Screen.playerName + ", you won this round");
      winner = Screen.playerName + ", you won this round";
      Screen.player1am +=Screen.potTotal;
      Screen.potTotal = 0;
    }
    if (comp1HandLevel[1] == ref2 && !comp1.isfolded) {
      if (numPlayers == 1) {
        updateMessages("Forrester won this round");
        winner = "Forrester won this round";
      } else  {
        updateMessages(Screen.player2Name + ", you won this round");
        winner = Screen.player2Name + ", you won this round";
      }
      Screen.comp1am +=Screen.potTotal;
      Screen.potTotal = 0;
    }
    if (comp2HandLevel[1] == ref2 && !comp2.isfolded) {
      updateMessages("Napolean won this round");
      winner = "Napolean won this round";
      Screen.comp2am +=Screen.potTotal;
      Screen.potTotal = 0;
    }
    if (comp3HandLevel[1] == ref2 && !comp3.isfolded) {
      updateMessages("Fabio won this round");
      winner = "Fabio won this round";
      Screen.comp3am +=Screen.potTotal;
      Screen.potTotal = 0;
    }
    if (comp4HandLevel[1] == ref2 && !comp4.isfolded) {
    updateMessages("Jesus won this round");
    winner = "Jesus won this round";
    Screen.comp4am +=Screen.potTotal;
    Screen.potTotal = 0;
    }
  } else  /*if only one person has the highest hand rank*/   {
  if (playerHandLevel[0] == highestHandLevel && !player.isfolded) {
    updateMessages("You won this round");
    winner = Screen.playerName + ", you won this round";
    Screen.player1am +=Screen.potTotal;
    Screen.potTotal = 0;
  }
  if (comp1HandLevel[0] == highestHandLevel && !comp1.isfolded) {
    if (numPlayers == 1) {
      updateMessages("Forrester won this round");
      winner = "Forrester won this round";
    } else  {
      updateMessages(Screen.player2Name + ", you won this round");
      winner = Screen.player2Name + ", you won this round";
    }
  Screen.comp1am +=Screen.potTotal;
  Screen.potTotal = 0;
  }
  if (comp2HandLevel[0] == highestHandLevel && !comp2.isfolded) {
    updateMessages("Napolean won this round");
    JOptionPane.showMessageDialog(screen.frame, "Napolean won this round");
    Screen.comp2am +=Screen.potTotal;
    Screen.potTotal = 0;
  }
  if (comp3HandLevel[0] == highestHandLevel && !comp3.isfolded) {
    updateMessages("Fabio won this round");
    winner = "Fabio won this round";
    Screen.comp3am +=Screen.potTotal;
    Screen.potTotal = 0;
  }
  if (comp4HandLevel[0] == highestHandLevel && !comp4.isfolded) {
    updateMessages("Jesus won this round");
    winner = "Jesus won this round";
    Screen.comp4am +=Screen.potTotal;
    Screen.potTotal = 0;
  }
  
  
  }
  
  
  
  System.out.println(playerHandLevel[0]);
  System.out.println(playerHandLevel[1]);
  System.out.println(playerHandLevel[2]);
  JTextArea output = new JTextArea();
  String player2Name = numPlayers == 2 ? Screen.player2Name : "Forrester";
  output.setText(  winner + "\n" +
               displayDetailedHand(Screen.playerName, playerHandLevel) + "\n" +
               displayDetailedHand(player2Name, comp1HandLevel) + "\n" +
               displayDetailedHand("Napolean", comp2HandLevel)+ "\n" +
               displayDetailedHand("Fabio", comp3HandLevel)+ "\n" +
               displayDetailedHand("Jesus", comp4HandLevel));
  
  JOptionPane.showMessageDialog(screen.frame, output);
  
  }//determineWinner



  /*************************************
  *displayerDetailedHand:
  *Takes String and a hand level and
  *returns it in a string form
  ************************************/
  public static String displayDetailedHand(String player, int[] level) {
    String name = player;
    String handLevel = "";
    String card = Integer.toString(level[1]);
    handLevel = Hand.getHandStringLevel(level[0], level[1]);
    return name + " has " + handLevel;
  }
  
  /**********************************
  DropAICards()
  This method performs the actual 
  dropping of the AI players cards.
  ***********************************/
  
  
  
  public static void dropAICards() {
  int[] cardsNotToDrop = new int[5];
  //cardsNotToDrop = Hand.getAIDropCards(comp1Hand, (Hand.getHandLevel(comp1Hand)[0]));
  int[] cardsToDrop =  new int[5];
  int numCardsDrop = 0;
  boolean drop1 = true;
  boolean drop2 = true;
  boolean drop3 = true;
  boolean drop4 = true;
  boolean drop5 = true;
  
  
  for (int x = 0; x < 4; x++ ) {
  numCardsDrop = 0;
  drop1 = true;
  drop2 = true;
  drop3 = true;
  drop4 = true;
  drop5 = true;
  
  
  if (x == 0 && !comp1.human) {
  cardsNotToDrop = Hand.getAIDropCards(comp1Hand, Hand.getHandLevel(comp1Hand)[0]);
  }
  
  
  if (x == 1) {
  cardsNotToDrop = Hand.getAIDropCards(comp2Hand, Hand.getHandLevel(comp2Hand)[0]);
  }
  
  if (x == 2) {
  cardsNotToDrop = Hand.getAIDropCards(comp3Hand, Hand.getHandLevel(comp3Hand)[0]);
  }
  
  if (x == 3) {
  cardsNotToDrop = Hand.getAIDropCards(comp4Hand, Hand.getHandLevel(comp4Hand)[0]);
  }
  
  
  for (int i =0; i <  cardsNotToDrop.length; i++) {
  if(cardsNotToDrop[i] == 0) {
    drop1 = false;
    break;
  }
  
  }
  for (int i =0; i <  cardsNotToDrop.length; i++) {
  if(cardsNotToDrop[i] == 1) {
    drop2 = false;
    break;
  }
  
  }
  for (int i =0; i <  cardsNotToDrop.length; i++) {
  if(cardsNotToDrop[i] == 2) {
    drop3 = false;
    break;
  }
  
  }
  for (int i =0; i <  cardsNotToDrop.length; i++) {
  if(cardsNotToDrop[i] == 3) {
    drop4 = false;
    break;
  }
  
  }
  for (int i =0; i <  cardsNotToDrop.length; i++) {
  if(cardsNotToDrop[i] == 4) {
    drop5 = false;
    break;
  }
  
  }
  
  if (x == 0 && !comp1.isfolded && !comp1.human ) {
  if (drop1) {
  comp1Hand[0] = Deck.dealCard();
  numCardsDrop++;
  }
  if (drop2) {
  comp1Hand[1] = Deck.dealCard();
  numCardsDrop++;
  }
  if (drop3) {
  comp1Hand[2] = Deck.dealCard();
  numCardsDrop++;
  }
  if (drop4) {
  comp1Hand[3] = Deck.dealCard();
  numCardsDrop++;
  }
  if (drop5) {
  comp1Hand[4] = Deck.dealCard();
  numCardsDrop++;
  }
  updateMessages("Forrester dropped " + numCardsDrop + " cards");
  Wait.manySec(2);
  }
  
  if (x == 1 && !comp2.isfolded) {
  if (drop1) {
  comp2Hand[0] = Deck.dealCard();
  numCardsDrop++;
  }
  if (drop2) {
  comp2Hand[1] = Deck.dealCard();
  numCardsDrop++;
  }
  if (drop3) {
  comp2Hand[2] = Deck.dealCard();
  numCardsDrop++;
  }
  if (drop4) {
  comp2Hand[3] = Deck.dealCard();
  numCardsDrop++;
  }
  if (drop5) {
  comp2Hand[4] = Deck.dealCard();
  numCardsDrop++;
  }
  updateMessages("Napolean dropped " + numCardsDrop + " cards");
  Wait.manySec(2);
  
  }
  
  
  if (x == 2 && !comp3.isfolded) {
  if (drop1) {
  comp3Hand[0] = Deck.dealCard();
  numCardsDrop++;
  }
  if (drop2) {
  comp3Hand[1] = Deck.dealCard();
  numCardsDrop++;
  }
  if (drop3) {
  comp3Hand[2] = Deck.dealCard();
  numCardsDrop++;
  }
  if (drop4) {
  comp3Hand[3] = Deck.dealCard();
  numCardsDrop++;
  }
  if (drop5) {
  comp3Hand[4] = Deck.dealCard();
  numCardsDrop++;
  }
  updateMessages("Fabio dropped " + numCardsDrop + " cards");
  Wait.manySec(2);
  }
  
  if (x == 3 && !comp4.isfolded) {
  if (drop1) {
  comp4Hand[0] = Deck.dealCard();
  numCardsDrop++;
  }
  if (drop2) {
  comp4Hand[1] = Deck.dealCard();
  numCardsDrop++;
  }
  if (drop3) {
  comp4Hand[2] = Deck.dealCard();
  numCardsDrop++;
  }
  if (drop4) {
  comp4Hand[3] = Deck.dealCard();
  numCardsDrop++;
  }
  if (drop5) {
  comp4Hand[4] = Deck.dealCard();
  numCardsDrop++;
  }
  updateMessages("Jesus dropped " + numCardsDrop + " cards");
  Wait.manySec(2);
  }
  
  
  
  }

  }     /*************************************************
        playSound()
        This method creates a new thread for playing sounds 
        while executing the main game thread as well.
        **************************************************/



         public static synchronized void playSound() {
    new Thread(new Runnable() {
      public void run() {
        try {
          clip = AudioSystem.getClip();
          File file = new File("Sounds/" + sound);
          AudioInputStream inputStream = AudioSystem.getAudioInputStream(file);
          clip.open(inputStream);
          clip.loop(Clip.LOOP_CONTINUOUSLY);
          //System.out.println(soundOn = true);
          if (soundOn) {
          clip.start();
          } else {
             clip.stop();

          }
        } catch (Exception e) {
          System.err.println(e.getMessage());
        }
      }
    }).start();
  }
    


 /*********************************************
 ante()
 This special method is for anteing. It is like making a bet,
 but, the Amount Bet values are not updated
 ***********************************************/



  public static void ante(int amount) {
      if (!player.isfolded) {
      Screen.potTotal = Screen.potTotal + amount;
          //Screen.player1amBet = Screen.player1amBet + amount;
          Screen.player1am = Screen.player1am - amount;
        //  System.out.println(amount);
      }
      if (!comp1.isfolded) {
       Screen.potTotal = Screen.potTotal + amount;
              //Screen.comp1amBet = Screen.comp1amBet + amount;
              Screen.comp1am = Screen.comp1am - amount;

              Screen.update();
      }

      if (!comp2.isfolded) {
              Screen.potTotal = Screen.potTotal + amount;
              //Screen.comp2amBet = Screen.comp2amBet + amount;
              Screen.comp2am = Screen.comp2am - amount;

              Screen.update();
      }

      if (!comp3.isfolded) {

              Screen.potTotal = Screen.potTotal + amount;
              //Screen.comp3amBet = Screen.comp3amBet + amount;
              Screen.comp3am = Screen.comp3am - amount;

              Screen.update();
      } if (!comp4.isfolded) {

              Screen.potTotal = Screen.potTotal + amount;
              //Screen.comp4amBet = Screen.comp4amBet + amount;
              Screen.comp4am = Screen.comp4am - amount;

              Screen.update();
      }




  }


 /*****************************
 updateMusic()
 This method updates the music and sounds in the game
 ******************************/

  public static void updateMusic() {
    if (soundOn) {
    clip.stop();
    soundOn = false;
    } else {
      clip.start();
      soundOn = true;
    }

  }
  
  /***************************************
  Deal()
  This method deals new cards to all players
  ******************************************/


  public static void deal() {
          gameDeck.createNewDeck();
            gameDeck.shuffle();
            playerHand = Hand.makeHand(Deck.dealCard(), Deck.dealCard(), Deck.dealCard(), Deck.dealCard(), Deck.dealCard(), "Player");







            comp1Hand = Hand.makeHand(Deck.dealCard(), Deck.dealCard(), Deck.dealCard(), Deck.dealCard(), Deck.dealCard(), "Computer 1");
            comp1Hand[0].handPosition = 1;
            comp1Hand[1].handPosition = 2;
            comp1Hand[2].handPosition = 3;
            comp1Hand[3].handPosition = 4;
            comp1Hand[4].handPosition = 5;

            comp2Hand = Hand.makeHand(Deck.dealCard(), Deck.dealCard(), Deck.dealCard(), Deck.dealCard(), Deck.dealCard(), "Computer 2");
            comp2Hand[0].handPosition = 1;
            comp2Hand[1].handPosition = 2;
            comp2Hand[2].handPosition = 3;
            comp2Hand[3].handPosition = 4;
            comp2Hand[4].handPosition = 5;

            comp3Hand = Hand.makeHand(Deck.dealCard(), Deck.dealCard(), Deck.dealCard(), Deck.dealCard(), Deck.dealCard(), "Computer 3");
            comp3Hand[0].handPosition = 1;
            comp3Hand[1].handPosition = 2;
            comp3Hand[2].handPosition = 3;
            comp3Hand[3].handPosition = 4;
            comp3Hand[4].handPosition = 5;

            comp4Hand = Hand.makeHand(Deck.dealCard(), Deck.dealCard(), Deck.dealCard(), Deck.dealCard(), Deck.dealCard(), "Computer 4");
            comp4Hand[0].handPosition = 1;
            comp4Hand[1].handPosition = 2;
            comp4Hand[2].handPosition = 3;
            comp4Hand[3].handPosition = 4;
            comp4Hand[4].handPosition = 5;
         }


  public static boolean playerCall() {
      if (betToCall != 0) {
        makePlayerBet(betToCall, 1);
        return true;
        }
      else {
        return true;
      }

  }
  
   /*********************************************
   paintAllHands()
   This method will paint all the hands to the screen
   *************************************************/
    public static void paintAllHands (boolean shown) {
    if (!player.isfolded) {
    Screen.paintHand(1, playerHand, 350,505, true);

            }
            if (!comp1.isfolded) {

            Screen.paintHand(2, comp1Hand, 25, 375, shown );
            }
            if (!comp2.isfolded) {
            Screen.paintHand(3, comp2Hand, 25, 200, shown );
            }
            if (!comp3.isfolded) {
            Screen.paintHand(4, comp3Hand, 650, 200, shown );
            }
            if (!comp4.isfolded) {
            Screen.paintHand(5, comp4Hand, 650, 375, shown );
    }       }

    /********************************************
    bettingRound()
    Starts a new betting round
    ***********************************************/




    public static void bettingRound(Card[] comp1hand, Card[] comp2hand, Card[] comp3hand, Card[] comp4hand) {
      int player1bet = 0;
      int comp1bet = 0;
      int comp2bet = 0;
      int comp3bet = 0;
      int comp4bet = 0;
      int randNum = 0;
      double potTotal = 0;
      double rateOfReturn = 0;
      double potOdds = 0;
      int raiseAm = 0;
      int action = 0;

      if (betToCall == 0) {
      Screen.callButton.setText("Check");
      Screen.update();
      } else {
      Screen.callButton.setText("Call (" + betToCall + ")");
      Screen.update();
      }

      if (!player.isfolded) {
       isPlayer1Turn = true;
      //System.out.println(player.name);
      updateMessages(Screen.playerName + ", its your turn");
      do {
      moved = false;
      isPlayerMove = true;
      //isPlayer1Move = true;
      Wait.forInput();
      //player1bet = (Integer.parseInt(JOptionPane.showInputDialog("Make your bet")));
      //isPlayerMove = false;
      } while (!successfulMove);

      Screen.update();
      makePlayerBet(player1bet, 1);


      if (betToCall == 0) {
      Screen.callButtonText = "Check";
      } else {
      Screen.callButton.setText("Call (" + betToCall + ")");
      Screen.update();
      }
      //System.out.println("Pot odds = " + getPotOdds(comp1Hand));
      //comp1

      }


      if (numPlayers == 2 && !comp1.isfolded) {
          updateMessages(Screen.player2Name + ", its your turn");
          successfulMove = false;
          do {
          isPlayer2Move = true;
          isPlayer2Turn = true;
          Wait.forInput();
          //player1bet = (Integer.parseInt(JOptionPane.showInputDialog("Make your bet")));
          isPlayer2Move = false;
          } while (!successfulMove);
        
        

      }

      if (!comp1.isfolded && numPlayers == 1) {
      updateMessages("Forrester is thinking");
      Wait.manySec(2);
      action = getAIAction(getPotOdds(comp1hand));
      //updateMessages(Integer.toString(action));
      makeMove(comp1hand, action, comp1, 1);
      Wait.manySec(2);
      } else if (numPlayers == 1) {
        System.out.println("1 player");

      } else { updateMessages("Forrester is folded"); }
      if (!comp2.isfolded) {
      updateMessages("Napolean is thinking");
      Wait.manySec(2);

      action = getAIAction(getPotOdds(comp2hand));
      //updateMessages(Integer.toString(action));
      makeMove(comp2hand, action, comp2, 2);

      Wait.manySec(2);
      } else { updateMessages("Napolean is folded"); }
      if (!comp3.isfolded) {
      updateMessages("Fabio is thinking");
      Wait.manySec(2);


      action = getAIAction(getPotOdds(comp3hand));
      //updateMessages(Integer.toString(action));
      makeMove(comp3hand, action, comp3, 3);

      Wait.manySec(2);
      } else { updateMessages("Fabio is folded"); }
      if (!comp4.isfolded) {
      updateMessages("Jesus is thinking");
     Wait.manySec(2);

      action = getAIAction(getPotOdds(comp4hand));
      //updateMessages(Integer.toString(action));
      makeMove(comp4hand, action, comp4, 4);
      } else { updateMessages("Jesus is folded"); }





  }

  /***************************************************
  makeMove()
  This method actually executes the move that the AI 
  player was advised to take.
  ****************************************************/
  public static void makeMove (Card[] hand, int action, Player playerToMove, int identifier) {
     long raiseAm = 0;
     if (action == FOLD) {
      System.out.println("FOLD");
          playerToMove.isfolded = true;
          //if (playerToMove.isfolded) {System.out.println("folded"); }
          playerToMove.fold();
           updateMessages("...he folds");
           playerToMove.fold();
           //System.out.println(playerToMove.isfolded);
          Screen.update();
      }
      if (action == CALL) {

      System.out.println("CALL");
       updateMessages("...he calls");

      makeCompBet(betToCall, identifier);
      }
      if (action == RAISE) {
      System.out.println("RAISE");

      //determine raise

          System.out.println(playerToMove.amount);
          raiseAm = betToCall + (Math.round(((Hand.getHandLevel(hand)[0] + 1) * 50) * (Math.random() + 0.75)));
          System.out.println(raiseAm);

      System.out.println(raiseAm);
       updateMessages("...he raises by $" + raiseAm);
      makeCompBet((int)raiseAm, identifier);
      betToCall = (int)raiseAm;
      }
      if (action == BLUFF) {
      System.out.println("BLUFF");


          raiseAm = Math.round(((50 + 1) * 50) * (Math.random() + 0.75));

      updateMessages("...he raises by $" + raiseAm);
      }
      if (action == CHECK) {
      System.out.println("CHECK");
      updateMessages("...he checks");
      }


  }

   /**************************************************
   getAIAction() 
   This method returns an integer that describes the 
   move that an AI player should do by using a mathematical
   formula and their current hand level.
   ***************************************************/


   public static int getAIAction (double potOdds) {
      Random gen = new Random();
      int randNum = gen.nextInt(100);
      System.out.println(betToCall);
      if (betToCall == 0) {
      if (potOdds < 1.0) {
      return CHECK;
      } else {
      return RAISE;
      }
      }




      if (potOdds < 0.8 ) {
        //System.out.print(randNum);
        if (randNum <= 5) {
          //RAISE BLUFF
          //System.out.println("RAISE, BLUFF");
          return BLUFF;
        }
        if (randNum > 5) {
          //FOLD
          //System.out.println("fold");
          return FOLD;

        }
      }//if 0.8
      if (potOdds < 1.0) {

        if (randNum >= 20) {
           //fold
           //System.out.println("fold");
           return FOLD;
        }
        if (randNum < 20 && randNum > 5) {
           //RAISE BLUFF
           //System.out.println("raise");
           return BLUFF;
        }
        if (randNum < 5) {
        // CALL
        //System.out.println ("call");
        return CALL;
        }

      } // potOdds < 1.0
      if (potOdds < 1.3) {
         if (randNum <= 60) {
           //CALL
           //System.out.println("call");
           return CALL;

         }
         if (randNum > 60) {
            //RAISE
            //System.out.println ("raise");
            return RAISE;
         }

      } // potOdds are less that 1.3
      if (potOdds >= 1.3) {
         if (randNum <= 30) {
            //CALL
            //System.out.println("call");
            return CALL;
         }
         if (randNum > 31) {
            //RAISE
            //System.out.println("raise");
            return RAISE;
         }
      }

      return 5;
   }

   /***********************************
   *assignMoney:                      *
   *assigns some moolah to the AI     *
   *players so we can win it off 'em  *
   ************************************/
   public static void assignMoney() {
     if(numPlayers == 1) Screen.comp1am = 5000;
     Screen.comp2am = 5000;
     Screen.comp3am = 5000;
     Screen.comp4am = 5000;
   }
   
   /**************************************
   getPotOdds:
   Gets a card array (a hand), and 
   returns the odds of the person winning
   ****************************************/
   public static double getPotOdds(Card[] hand) {
      double potTotal = 0;
      double potOdds = 0;
      double rateOfReturn = 0;
      potTotal = Screen.potTotal;
      potOdds =  betToCall / (betToCall + potTotal); //more formulas for AI
      rateOfReturn = Hand.getHandLevel(hand)[0] / potOdds;//formula for rateOfReturn
      return rateOfReturn;
   }
   /*********************************
   makePlayerBet:
   Changes lots of integers and amounts
   to make a bet for a player
   **********************************/
   public static boolean makePlayerBet (int amount, int playerNum) {
          if (playerNum == 1) {
          if (amount != 0 && Screen.player1am >= amount) {
          Screen.potTotal = Screen.potTotal + amount;
          Screen.player1amBet = Screen.player1amBet + amount;
          Screen.player1am = Screen.player1am - amount;
          System.out.println(amount);
          betToCall = amount;
          System.out.println(betToCall);
          Screen.update();
          return true;
          } else {
          if (Screen.player1am < amount) {
          updateMessages("You don't have enough money to make that move");
          }
          return false;
          }
          }
          if (playerNum == 2) {
              if (amount != 0 && Screen.comp1am >= amount) {
          Screen.potTotal = Screen.potTotal + amount;
          Screen.comp1amBet = Screen.comp1amBet + amount;
          Screen.comp1am = Screen.comp1am - amount;
          //System.out.println(amount);
          betToCall = amount;
          //System.out.println(betToCall);
          Screen.update();
          return true;
          } else {
          if (Screen.comp1am <= amount) {
          updateMessages("You don't have enough money to make that move");
          }
          return false;
          }
          
          
          
          }
          return false;

   }
   /*******************************************
   makeCompBet:
   makes an AI bet by changing lots of amounts
   *******************************************/
   
   public static void makeCompBet (int amount, int compNum) {
       if (amount != 0 ) {
           if (compNum == 1) {
              Screen.potTotal = Screen.potTotal + amount;
              Screen.comp1amBet = Screen.comp1amBet + amount;
              Screen.comp1am = Screen.comp1am - amount;
              //betToCall = amount;
              Screen.update();
           }
           if (compNum == 2) {
              Screen.potTotal = Screen.potTotal + amount;
              Screen.comp2amBet = Screen.comp2amBet + amount;
              Screen.comp2am = Screen.comp2am - amount;
              //betToCall = amount;
              Screen.update();
           }
            if (compNum == 3) {
              Screen.potTotal = Screen.potTotal + amount;
              Screen.comp3amBet = Screen.comp3amBet + amount;
              Screen.comp3am = Screen.comp3am - amount;
              //betToCall = amount;
              Screen.update();
           }
              if (compNum == 4) {
              Screen.potTotal = Screen.potTotal + amount;
              Screen.comp4amBet = Screen.comp4amBet + amount;
              Screen.comp4am = Screen.comp4am - amount;
              //betToCall = amount;
              Screen.update();
           }


       }

       }
       /*******************************
       updateMessages:
       Adds a new string to the messages
       and gets rid of the oldest one
       ********************************/
        public static void updateMessages(String newString) {
           String[] tempStrings = new String[5];
           String[] strings = new String[5];
           strings = Screen.messages;
           tempStrings[0] = newString;
           for (int x = 0; x < 4 ; x++ ) {
               tempStrings[x+1] = strings[x];
           }
           Screen.messages = tempStrings;
            for (int y = 0; y < 5 ; y++ ) {
             if (tempStrings[y] != null) {

             } else {
             }

           }
           Screen.updateMessagesScreen();






  }
  /***************************************************
  dropCards()
  This method finds which cards the player
  would like to drop and then drops the selected ones
  ****************************************************/
  public static void dropCards() {
  if (isPlayer1Turn) {
  if (Screen.clickCardButton1isSelected) {
  playerHand[0] = Deck.dealCard();
  }
  if(Screen.clickCardButton2isSelected){
  playerHand[1] = Deck.dealCard();
  }
  if (Screen.clickCardButton3isSelected) {
  playerHand[2] = Deck.dealCard();
  }
  if (Screen.clickCardButton4isSelected) {
  playerHand[3] = Deck.dealCard();
  }

  if (Screen.clickCardButton5isSelected) {
  playerHand[4] = Deck.dealCard();
  }
  }

  if (isPlayer2Turn) {
   if (Screen.clickCardButton1isSelected) {
  comp1Hand[0] = Deck.dealCard();
   }
  if(Screen.clickCardButton2isSelected){
  comp1Hand[1] = Deck.dealCard();
  }
  if (Screen.clickCardButton3isSelected) {
  comp1Hand[2] = Deck.dealCard();
  }
  if (Screen.clickCardButton4isSelected) {
  comp1Hand[3] = Deck.dealCard();
  }

  if (Screen.clickCardButton5isSelected) {
  comp1Hand[4] = Deck.dealCard();
  }



  }
  paintAllHands(false);
  if (numPlayers == 2) {
      Screen.paintHand(1, playerHand, 350,505, true);
      Screen.paintHand(2, comp1Hand, 25,375, true);


  }
  Screen.cardButtonPanel.setVisible(false);
  Screen.update();
  
  if(isPlayer1Turn) isPlayer1Turn = false;
  if(isPlayer2Turn) isPlayer2Turn = false;








  }
  /********************************************
  getPlayerName()
  This method gets the players name, and then sees 
  if the player's score is saved. If it is then the player is
  "signed in", other than that it gives that player $5000
  **********************************************/

  public static void getPlayerName() {
  clip.stop();
  clip.flush();
  clip.close();
  sound = "sound2.wav";
  playSound();
  boolean playerFound = false;
  boolean player2Found = false;
  String name = JOptionPane.showInputDialog("Player 1, please enter your name.");
  String name2 = "";
  if (numPlayers == 2) name2 = JOptionPane.showInputDialog("Player 2, please enter your name.");
  try {
  for (int i = 0; i < 30; i++ ){
   // System.out.println(name);
   // System.out.println(HST[i].nameS);
  if(HST[i].nameS.equals(name)) {
    System.out.println(HST[i].scoreI);
  player.signIn(HST[i].nameS, HST[i].scoreI);
  Screen.playerName = HST[i].nameS;
  Screen.player1am = HST[i].scoreI;
  playerFound = true;
  }
  else {
  //System.out.println("hihiihi" + i);
  }
    }
      for (int i = 0; i < 30; i++ ){
   // System.out.println(name);
   // System.out.println(HST[i].nameS);
  if(HST[i].nameS.equals(name2)) {
    System.out.println(HST[i].scoreI);
  comp1.signIn(HST[i].nameS, HST[i].scoreI);

  Screen.player2Name = HST[i].nameS;
  Screen.comp1am = HST[i].scoreI;
  player2Found = true;
  }
  else {
  //System.out.println("hihiihi" + i);
  }
    }
    if (!playerFound ||  player.amount <= 0) {
    if (!playerFound)JOptionPane.showMessageDialog(screen.frame, "Hello, " + name + ". I'm sorry but it appears that you have not played this game before. \n Since this is your first play, you will be given $5000 to get started.\n Please try not to lose it all on the first game!");
    player.signIn(name, 5000);
    Screen.playerName = name;
  Screen.player1am = 5000;

    }
    System.out.println(player2Found);
    if (numPlayers == 2 && !player2Found || numPlayers == 2 && comp1.amount <= 0) {
    if (!player2Found)JOptionPane.showMessageDialog(screen.frame, "Hello, " + name2 + ". I'm sorry but it appears that you have not played this game before. \n Since this is your first play, you will be given $5000 to get started.\n Please try not to lose it all on the first game!");
    comp1.signIn(name2, 5000);
      Screen.player2Name = name2;
  Screen.comp1am = 5000;
    }
  } catch (Exception e) {
  JOptionPane.showMessageDialog(Screen.panel, "Hey, just a heads up: your name was not located in users, you'll have to start with $5000");
  player.signIn(name, 5000);
  }


  }
  /**********************************
  updateHighScores()
  This method gets the high scores from 
  a text file (highScores.txt)
  ***********************************/


  public static HighScoreTransferable[] updateHighScores() {
    //HighScoreTransferable HST[] = new HighScoreTransferable[25];
    String[] highScoreStrings = new String[30];
    String[][] hSSR = new String[2][30];
    for (int i = 0; i < 30; i++ ){
    //highScoreStrings[i] = " ";
    }
    int length = 0;
    try {
    BufferedReader in = new BufferedReader(new FileReader("highScores.txt"));


    for (int i =0; i < highScoreStrings.length; i++) {


    highScoreStrings[i] = in.readLine();
    //System.out.println(highScoreStrings[i]);

    }

    } catch (IOException e) {
    highScoreStrings[0] = "Sorry, the high scores could not be loaded";
    JOptionPane.showMessageDialog(Screen.frame, "High Scores could not be loaded");


    }
    String whiteSpace= "";
    int wSNeeded = 0;

    for (int i = 0; i < 30; i++ ) {

       if (highScoreStrings[i] != null) {
       HST[i] = new HighScoreTransferable(highScoreStrings[i].substring(0, (highScoreStrings[i].indexOf('@'))),
                highScoreStrings[i].substring(highScoreStrings[i].indexOf('@') + 1));

        //System.out.println(HST[i].scoreI);
       } else {
         HST[i] = new HighScoreTransferable(" "," ");
       }




    }



      Arrays.sort(HST);







    return HST;
    }
    /***************************************
    writeNewHighscore()
    This method writes a new highscore to the 
    text file that stores the high scores. (highScores.txt)
    ****************************************/

    public static void writeNewHighscore(String user, int score) {
        boolean newUser = true;  //Assuem by default that it is a new user
        for (int i = 0; i < HST.length; i++ ) {
          if(HST[i].nameS.equals(user)) {
              //HST[i].scoreS = score;
              HST[i].scoreI = score;
              newUser = false;
              //System.out.println("FOUND " + user);
          } else {

          }
        }


        String toWrite = "";
        try {
        BufferedWriter out = new BufferedWriter(new FileWriter("highScores.txt"));
        toWrite = user + "@" + score;

        for (int i = 0; i < HST.length; i++) {
         if (HST[i].scoreI != -1 && HST[i].nameS != null) {
        out.write(HST[i].toString());
        //System.out.println("Writing " + HST[i].toString());
        out.newLine();
         }
        }
        if (newUser) {
        out.write(toWrite);
        out.newLine();
        }
        out.close();
        } catch (Exception e) {
        System.out.println("Ooopss: Error writing high score");
        }

    }




  }






