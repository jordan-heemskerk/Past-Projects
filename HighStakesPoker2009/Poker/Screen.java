/*****************************************
Name: Screen.java
Author: Jordan Heemskerk
Purpose: Contain methods for grapics in the game
Date: Mon. Dec. 15, 2008
******************************************/

import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import javax.swing.*;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;

//import java.awt.event.MouseEvent;
public class Screen
extends JPanel
implements WindowListener
{
       static boolean winClose = false;
       static String player2Name = "";
       static Image[] cardPicsPlayer = new Image[6];
       static Image[] cardPicsComp1 = new Image[6];
       static Image[] cardPicsComp2 = new Image[6];
       static Image[] cardPicsComp3 = new Image[6];
       static Image[] cardPicsComp4 = new Image[6];
       static int pos1, pos2, pos3, pos4, pos5, pos6, pos7, pos8, pos9, pos10, pos11, pos12 = 0;
       static String stringToDraw = "";
       static Image background, optionsBackground;
       static JPanel panel = new Screen();
       static JPanel optionsPanel = new JPanel();
       static int player1am, comp1am, comp2am, comp3am, comp4am = 0;
       static int player1amBet, comp1amBet, comp2amBet, comp3amBet, comp4amBet = 0;
       static boolean player1isFolded, comp1isFolded, comp2isFolded, comp3isFolded, comp4isFolded = false;
       static String playerName = "";
       static int potTotal = 0;
       static String currentString = "";
       static String[] messages = new String[5];
       static Image splashScreen;
       static JTextField raiseAm = new JTextField("Raise Amount");
       static JTextField raiseAmountInput = new JTextField();
       static String callButtonText = "Call";
       static JList messageList = new JList();
       //static JScrollPane messageScroll = new JScrollPane(messageList);
       static JButton callButton = new JButton("Call");
       static boolean mainGameIsVisible = true;
       static Image instructionButtonImage;
       static JButton instructionButton;
       static JFrame frame = new JFrame ("Poker");
       

       
       static Player player1 = Poker.player;
       static Player comp1 = Poker.comp1;
       static Player comp2 = Poker.comp2;
       static Player comp3 = Poker.comp3;
       static Player comp4 = Poker.comp4;
        static JButton back = new JButton ("Back");
       static int gameStage = 0;
       static final int MENU = 0,
                        PLAYING = 1,
                        CREDITS = 2,
                        INSTRUCTIONS = 3;
       
       static Image clickableCardImage;
       static ImageIcon clickableCardImageIcon;

       static ImageIcon[] clickableCard = new ImageIcon[5];
       static JButton[] clickableCardButton = new JButton[5];

       static JPanel cardButtonPanel = new JPanel();
       
       static JButton clickCardButton1 = new JButton();
       static JButton clickCardButton2 = new JButton();
       static JButton clickCardButton3 = new JButton();
       static JButton clickCardButton4 = new JButton();
       static JButton clickCardButton5 = new JButton();



       static boolean clickCardButton1isSelected = false;
       static boolean clickCardButton2isSelected = false;
       static boolean clickCardButton3isSelected = false;
       static boolean clickCardButton4isSelected = false;
       static boolean clickCardButton5isSelected = false;


       static ImageIcon clickCard1;
       static ImageIcon clickCard2;
       static ImageIcon clickCard3;
       static ImageIcon clickCard4;
       static ImageIcon clickCard5;


       static JButton start1Button;
       static JButton start2Button;
       static JButton creditsButton;


      /***********************************
      createAndShowGUI()
      This method paints all swing components 
      to the screen and connects most images
      ************************************/
       public void createAndShowGUI() {
         frame.setSize(1000,1000);
         Toolkit tk = Toolkit.getDefaultToolkit();
         background = tk.getImage("Images/background.gif");
         splashScreen = tk.getImage("Images/splashScreen.gif");
         instructionButtonImage = tk.getImage("Images/instructionsBut.gif");
         optionsBackground = tk.getImage("Images/optionsBackground.gif");
         panel.setPreferredSize(new Dimension(1000, 700 ));
  
  
         if (gameStage == 0) {
  
           ImageIcon instructionButtonIcon = new ImageIcon(instructionButtonImage);
          instructionButton = new JButton("Instructions");
          start1Button = new JButton ("Single Player Campaign");
          start2Button = new JButton ("Coopertive Campaign");
          creditsButton = new JButton ("Credits");
  
  
         instructionButton.setLocation(25,282);
         start1Button.setLocation(25,372);
         start2Button.setLocation(25, 462);
         creditsButton.setLocation(25, 552);
         start1Button.setSize(350,88);
         start2Button.setSize(350,88);
         creditsButton.setSize(350,88);
         instructionButton.setSize(350,88);
          panel.add(instructionButton);
          panel.add(start1Button);
          panel.add(start2Button);
          panel.add(creditsButton);

             instructionButton.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent evt) {
                  instructionButtonActionPerformed(evt);
              }
          });
             start1Button.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent evt) {
                  start1ButtonActionPerformed(evt);
              }
          });
             start2Button.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent evt) {
                  start2ButtonActionPerformed(evt);
              }
          });
             creditsButton.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent evt) {
                  creditsButtonActionPerformed(evt);
              }
          });
  
  
  
         } else if (gameStage == 1) {
         instructionButton.setVisible(false);
         start1Button.setVisible(false);
         start2Button.setVisible(false);
         creditsButton.setVisible(false);
         JButton menuButton = new JButton("Menu");
         panel.add(menuButton);
         menuButton.setSize(65,20);
         menuButton.setLocation(0,0);
         menuButton.setVisible(false);
  
        JPanel startButton = new JPanel();
        JButton labelForCards = new JButton("Choose Cards To Drop");
  
        JButton dropCards = new JButton("Drop Cards");
  
        clickableCardImage = tk.getImage("Images/Cards/clickableCard.gif");
        clickableCardImageIcon = new ImageIcon(clickableCardImage);
  
  
  
  
        if (Poker.isPlayer1Turn) clickCard1 = new ImageIcon(getImageForIcon(Card.getCardStringValue(Poker.playerHand[0])));
        if (Poker.isPlayer2Turn) clickCard1 = new ImageIcon(getImageForIcon(Card.getCardStringValue(Poker.comp1Hand[0])));
       clickCardButton1 = new JButton(clickCard1);
        clickCardButton1.setSize(36,48);
        //System.out.println(clickCardButton1.getSize());
        clickCardButton1.setLocation(10,10);
  
        //System.out.println(clickCardButton1.getLocation());
  
  
  
        //clickableCardImage = tk.getImage("Images/Cards/clickableCard.gif");
        if(Poker.isPlayer1Turn)clickCard2 = new ImageIcon(getImageForIcon(Card.getCardStringValue(Poker.playerHand[1])));
         if (Poker.isPlayer2Turn) clickCard1 = new ImageIcon(getImageForIcon(Card.getCardStringValue(Poker.comp1Hand[1])));
         clickCardButton2 = new JButton(clickCard2);
        clickCardButton2.setSize(36,48);
        //System.out.println(clickCardButton1.getSize());
        clickCardButton2.setLocation(10,58);
  
  
  
  
  
  
  
          if(Poker.isPlayer1Turn) clickCard3 = new ImageIcon(getImageForIcon(Card.getCardStringValue(Poker.playerHand[2])));
         if (Poker.isPlayer2Turn) clickCard1 = new ImageIcon(getImageForIcon(Card.getCardStringValue(Poker.comp1Hand[2])));
         clickCardButton3 = new JButton(clickCard3);
        clickCardButton3.setSize(36,48);
        //System.out.println(clickCardButton1.getSize());
        clickCardButton3.setLocation(10,58);
  

          if(Poker.isPlayer1Turn) clickCard4 = new ImageIcon(getImageForIcon(Card.getCardStringValue(Poker.playerHand[3])));
       if (Poker.isPlayer2Turn) clickCard1 = new ImageIcon(getImageForIcon(Card.getCardStringValue(Poker.comp1Hand[3])));
       clickCardButton4 = new JButton(clickCard4);
        clickCardButton4.setSize(36,48);
        clickCardButton4.setLocation(10,58);
  
            if(Poker.isPlayer1Turn) clickCard5 = new ImageIcon(getImageForIcon(Card.getCardStringValue(Poker.playerHand[4])));
         if (Poker.isPlayer2Turn) clickCard1 = new ImageIcon(getImageForIcon(Card.getCardStringValue(Poker.comp1Hand[4])));
         clickCardButton5 = new JButton(clickCard5);
        clickCardButton5.setSize(36,48);
        clickCardButton5.setLocation(10,58);
  

        //ADDING ACTION LISTNERS TO ALL OF THE 5 BUTTONS FOR CHOOSING CARDS... ALSO FOR THE CHOOSE CARDS TO DROP BUTTON

          clickCardButton1.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent evt) {
                  clickCardButton1ActionPerformed(evt);
              }
          });
  
             clickCardButton2.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent evt) {
                  clickCardButton2ActionPerformed(evt);
              }
          });
  
             clickCardButton3.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent evt) {
                  clickCardButton3ActionPerformed(evt);
              }
          });
  
             clickCardButton4.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent evt) {
                  clickCardButton4ActionPerformed(evt);
              }
          });
  
             labelForCards.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent evt) {
                  labelForCardsActionPerformed(evt);
              }
          });
  
             clickCardButton5.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent evt) {
                  clickCardButton5ActionPerformed(evt);
              }
          });


        JButton raiseButton = new JButton("Raise by");
        JButton foldButton = new JButton("Fold");
  
  
        JButton soundButton =  new JButton("Sound On/Off");
  
  
        JLabel messageTitle = new JLabel("Message List");
  
         messageTitle.setLocation(800,0);
         messageTitle.setSize(200,15);
         messageTitle.setText("Message List");
         JScrollPane messageScroll = new JScrollPane(messageList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
         messageScroll.setPreferredSize(new Dimension(200,15));
         messageScroll.getViewport().add(messageList);

         optionsPanel.setLayout(null);
         optionsPanel.setLocation(0,610);
         optionsPanel.setSize(1000,100);
  
  
  
         messageList.setSize(300,85);
         messageList.setLocation(700,15);
  
         raiseButton.setSize(90, 45);
         raiseButton.setLocation(150,45);
         raiseAm.setSize(20,20);
         soundButton.setSize (150,45);
         soundButton.setLocation(0,45);
         callButton.setSize(150,45);
         callButton.setLocation(150,0);
         foldButton.setSize(150,45);
         foldButton.setLocation(0,0);
         raiseAmountInput.setSize(60,45);
         raiseAmountInput.setLocation(240,45);
            
         /*     menuButton.addActionListener (new ActionListener() {
            public void actionPerformed (ActionEvent evt) {
              menuButtonActionPerformed(evt);
            }
          });         */
  
  
  
  
         callButton.addActionListener( new ActionListener () {
           public void actionPerformed(ActionEvent evt) {
            callButtonActionPerformed(evt);
           }
         });
  
  
         raiseButton.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent evt) {
                  raiseButtonActionPerformed(evt);
              }
          });
  
          soundButton.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent evt) {
                  soundButtonActionPerformed(evt);
              }
          });
          foldButton.addActionListener (new ActionListener() {
            public void actionPerformed (ActionEvent evt) {
              foldButtonActionPerformed(evt);
            }
          });


         optionsPanel.add(raiseButton);
         optionsPanel.add(callButton);
         optionsPanel.add(foldButton);
         optionsPanel.add(raiseAmountInput);
         optionsPanel.add(soundButton);
         optionsPanel.add(messageList);
         optionsPanel.add(messageTitle);
         optionsPanel.add(cardButtonPanel);
          optionsPanel.add(dropCards);

         if  (Poker.droppingCards == true) {
         dropCards.setSize(300,90);
         dropCards.setLocation(0,0);
         raiseButton.setVisible(false);
         callButton.setVisible(false);
         foldButton.setVisible(false);
         raiseAmountInput.setVisible(false);
         soundButton.setVisible(false);
         dropCards.setVisible(true);
         System.out.println("lolzzzzzzzz");
  
         }
         cardButtonPanel.add(clickCardButton1);
         cardButtonPanel.add(clickCardButton2);
         cardButtonPanel.add(clickCardButton3);
         cardButtonPanel.add(clickCardButton4);
         cardButtonPanel.add(clickCardButton5);
         cardButtonPanel.add(labelForCards);
         cardButtonPanel.setLocation(300,0);
         cardButtonPanel.setSize(400,200);

          } else if (gameStage == 3) {
         instructionButton.setVisible(false);
         start1Button.setVisible(false);
         start2Button.setVisible(false);
         creditsButton.setVisible(false);

         }
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.add (panel);
         panel.add(optionsPanel);
         frame.addWindowListener(this);
         panel.setLayout(null);
         frame.pack();
         frame.setLocationRelativeTo(null);
         frame.setVisible(true);
         }

         /*****************************************
         callButtonActionPerformed()
         Is called when the "call" or "check" button
         is clicked.
         ******************************************/
         public void  callButtonActionPerformed(ActionEvent evt) {
         if (Poker.isPlayer1Turn) {
           if (Poker.playerCall()) {
             Poker.moved = true;
             Poker.successfulMove = true;
           }
           Poker.isPlayer1Turn = false;
         }
         if (Poker.isPlayer2Turn) {
           Poker.moved = true;
           Poker.successfulMove = true;
           Poker.makeCompBet(Poker.betToCall, 1);
           Poker.isPlayer2Turn = false;
         }

       }
       /***********************************
       instructionButtonActionPerformed()
       Is called when the "instructios" button
       is clicked.
       ***********************************/
       public void  instructionButtonActionPerformed(ActionEvent evt) {

          gameStage = 3;
     instructionButton.setVisible(false);
       start1Button.setVisible(false);
       start2Button.setVisible(false);
       creditsButton.setVisible(false);

          Poker.moved = true;

          panel.add(back);
          back.setSize(100, 20);
          back.setLocation(900,0);
          back.setVisible(true);
          back.addActionListener (new ActionListener() {
          public void actionPerformed (ActionEvent evt) {
            backActionPerformed(evt);
          }
        });








       }
       
       /*********************
       backActionPerformed()
       This method is called when the "back button is clicked
       **********************/
       public void backActionPerformed( ActionEvent evt) {
        back.setVisible(false);
       Poker.moved = true;


       }

       /********************************
       start1ButtonActionPerformed()
       This method is called when  the player clicks the
       "Single Player Campaign" Button
       *********************************/
       public void start1ButtonActionPerformed (ActionEvent evt) {
           Poker.numPlayers = 1;
           gameStage = PLAYING;
           Poker.moved = true;

       }
        /********************************
       start2ButtonActionPerformed()
       This method is called when  the player clicks the
       "Cooperative Campaign" Button
       *********************************/
        public void start2ButtonActionPerformed (ActionEvent evt) {
               Poker.numPlayers = 2;
           gameStage = PLAYING;
           Poker.moved = true;


       }
         /********************************
       creditsButtonActionPerformed()
       This method is called when  the player clicks the
       "Credits" Button
       *********************************/
        public void creditsButtonActionPerformed (ActionEvent evt) {
        gameStage = 4;
         instructionButton.setVisible(false);
       start1Button.setVisible(false);
       start2Button.setVisible(false);
       creditsButton.setVisible(false);

          Poker.moved = true;

          panel.add(back);
          back.setSize(100, 20);
          back.setLocation(900,0);
          back.setVisible(true);
          back.addActionListener (new ActionListener() {
          public void actionPerformed (ActionEvent evt) {
            backActionPerformed(evt);
          }
        });
        System.out.println("By me... yah!");
       }

         /*********************************
         These next empty methods are not used
         **********************************/
         public void windowDeactivated(WindowEvent wevt){}
         public void windowActivated(WindowEvent wevt){}
         public void windowDeiconified(WindowEvent wevt) {       }
         public void windowIconified(WindowEvent wevt) {       }
         public void windowClosed(WindowEvent wevt) {      }
         public void windowClosing(WindowEvent wevt) {
           if (gameStage == PLAYING) {
             if (!winClose) {
               JOptionPane.showMessageDialog(frame, "Please wait while your score is saved");
               Poker.writeNewHighscore(playerName, player1am);
               if (Poker.numPlayers == 2) {
               Poker.writeNewHighscore(player2Name, comp1am);
    
             }
    
    
             winClose = true;
    
             JOptionPane.showMessageDialog(frame, "Your score was saved!");
             }


           }


         }
         public void windowOpened(WindowEvent wevt) {       }





       /********************
       updateMessagesScreen()
       This method updates the messages list
       ********************/
       public static void updateMessagesScreen() {
           messageList.setListData(messages);
           update();
       }

       /********************
       update()
       Updates all components of the screen
       ********************/
       public static void update() {

              panel.repaint();
              optionsPanel.repaint();
              cardButtonPanel.repaint();
              


       }

       /**************************
       updateClickCards ()
       This method changes the state 
       of the card pics when the player 
       is deciding which ones to drop.
       ***************************/
       public static void updateClickCards () {
           if(Poker.isPlayer1Turn)clickCardButton1.setIcon(new ImageIcon(getImageForIcon(Card.getCardStringValue(Poker.playerHand[0]))));
            if (Poker.isPlayer2Turn) clickCardButton1.setIcon ( new ImageIcon(getImageForIcon(Card.getCardStringValue(Poker.comp1Hand[0]))));

            if(Poker.isPlayer1Turn)  clickCardButton2.setIcon(new ImageIcon(getImageForIcon(Card.getCardStringValue(Poker.playerHand[1]))));
            if (Poker.isPlayer2Turn) clickCardButton2.setIcon ( new ImageIcon(getImageForIcon(Card.getCardStringValue(Poker.comp1Hand[1]))));

            if(Poker.isPlayer1Turn)  clickCardButton3.setIcon(new ImageIcon(getImageForIcon(Card.getCardStringValue(Poker.playerHand[2]))));
            if (Poker.isPlayer2Turn) clickCardButton3.setIcon ( new ImageIcon(getImageForIcon(Card.getCardStringValue(Poker.comp1Hand[2]))));

            if(Poker.isPlayer1Turn)  clickCardButton4.setIcon(new ImageIcon(getImageForIcon(Card.getCardStringValue(Poker.playerHand[3]))));
            if (Poker.isPlayer2Turn) clickCardButton4.setIcon ( new ImageIcon(getImageForIcon(Card.getCardStringValue(Poker.comp1Hand[3]))));

            if(Poker.isPlayer1Turn)  clickCardButton5.setIcon(new ImageIcon(getImageForIcon(Card.getCardStringValue(Poker.playerHand[4]))));
            if (Poker.isPlayer2Turn) clickCardButton5.setIcon ( new ImageIcon(getImageForIcon(Card.getCardStringValue(Poker.comp1Hand[4]))));
                         clickCardButton1isSelected = false;
                                    clickCardButton2isSelected = false;
                                               clickCardButton3isSelected = false;
                                                          clickCardButton4isSelected = false;
                                                                     clickCardButton5isSelected = false;


       }
       /*******************************
       clickCardButton1ActionPerformed()
       Called when the first card button is clicked
       ********************************/
       public void clickCardButton1ActionPerformed(ActionEvent evt) {

           if(!clickCardButton1isSelected) {
           clickCardButton1.setIcon(clickableCardImageIcon);
           clickCardButton1isSelected = true;
           } else {
           if(Poker.isPlayer1Turn)clickCardButton1.setIcon(new ImageIcon(getImageForIcon(Card.getCardStringValue(Poker.playerHand[0]))));
           if (Poker.isPlayer2Turn) clickCardButton1.setIcon(new ImageIcon(getImageForIcon(Card.getCardStringValue(Poker.comp1Hand[0]))));
           clickCardButton1isSelected = false;
           }
           update();
       }

       public void foldButtonActionPerformed(ActionEvent evt) {
          System.out.println(Poker.isPlayer1Turn);
          System.out.println(Poker.isPlayer2Turn);
          if (Poker.isPlayer1Turn) {
          Poker.player.fold();
          update();
           Poker.moved = true;
         Poker.successfulMove = true;
          Poker.isPlayer1Turn = false;

          }
          if (Poker.isPlayer2Turn) {
          //comp1.isfolded = true;
          Poker.comp1.fold();
          update();
          Poker.moved = true;
          Poker.successfulMove = true;
          Poker.isPlayer2Turn = true;
          Poker.isPlayer2Turn = false;
          }

       }
         /*******************************
       clickCardButton2ActionPerformed()
       Called when the second card button is clicked
       ********************************/
         public void clickCardButton2ActionPerformed(ActionEvent evt) {
        if(!clickCardButton2isSelected) {
           clickCardButton2.setIcon(clickableCardImageIcon);
           clickCardButton2isSelected = true;
           } else {
          if(Poker.isPlayer1Turn) clickCardButton2.setIcon(new ImageIcon(getImageForIcon(Card.getCardStringValue(Poker.playerHand[1]))));
           if (Poker.isPlayer2Turn) clickCardButton2.setIcon(new ImageIcon(getImageForIcon(Card.getCardStringValue(Poker.comp1Hand[1]))));
           clickCardButton2isSelected = false;
           }
           update();
       }

        /*******************************
       clickCardButton3ActionPerformed()
       Called when the third card button is clicked
       ********************************/
         public void clickCardButton3ActionPerformed(ActionEvent evt) {
            if(!clickCardButton3isSelected) {
           clickCardButton3.setIcon(clickableCardImageIcon);
           clickCardButton3isSelected = true;
           } else {
          if(Poker.isPlayer1Turn) clickCardButton3.setIcon(new ImageIcon(getImageForIcon(Card.getCardStringValue(Poker.playerHand[2]))));
           if (Poker.isPlayer2Turn) clickCardButton3.setIcon (new ImageIcon(getImageForIcon(Card.getCardStringValue(Poker.comp1Hand[2]))));
           clickCardButton3isSelected = false;
           }
           update();
       }

          /*******************************
       clickCardButton4ActionPerformed()
       Called when the fourth card button is clicked
       ********************************/
         public void clickCardButton4ActionPerformed(ActionEvent evt) {
           if(!clickCardButton4isSelected) {
           clickCardButton4.setIcon(clickableCardImageIcon);
           clickCardButton4isSelected = true;
           } else {
         if (Poker.isPlayer1Turn)  clickCardButton4.setIcon(new ImageIcon(getImageForIcon(Card.getCardStringValue(Poker.playerHand[3]))));
          if (Poker.isPlayer2Turn) clickCardButton4.setIcon( new ImageIcon(getImageForIcon(Card.getCardStringValue(Poker.comp1Hand[3]))));
           clickCardButton4isSelected = false;
           }
           update();
       }
          /*******************************
       clickCardButton5ActionPerformed()
       Called when the fifth card button is clicked
       ********************************/
         public void clickCardButton5ActionPerformed(ActionEvent evt) {
           System.out.println(Poker.isPlayer1Turn);
           System.out.println(Poker.isPlayer2Turn);
            if(!clickCardButton5isSelected) {
           clickCardButton5.setIcon(clickableCardImageIcon);
           clickCardButton5isSelected = true;
           } else {

           if(Poker.isPlayer1Turn)  clickCardButton5.setIcon(new ImageIcon(getImageForIcon(Card.getCardStringValue(Poker.playerHand[4]))));
           if (Poker.isPlayer2Turn) clickCardButton5.setIcon( new ImageIcon(getImageForIcon(Card.getCardStringValue(Poker.comp1Hand[4]))));
           clickCardButton5isSelected = false;
           }

           update();
       }

       /********************************
       labelForCardsActionPerformed()
       This method is called when the Drop Cards button is clicked
       ********************************/

       public void labelForCardsActionPerformed (ActionEvent evt) {
       Poker.dropCards();
       Poker.moved = true;
       }

       /**********************************
       raiseButtonActionPerformed()
       This method is called when the raise button is clicked
       *************************************/
       public void raiseButtonActionPerformed(ActionEvent evt) {
           int amount = 0;
           try {
           amount = Integer.parseInt(raiseAmountInput.getText());
           } catch (Exception e) {
           Poker.updateMessages("That was an invalid number to raise by");
           Poker.successfulMove = false;
           return;
           }
           if (Poker.isPlayer1Turn) {

                         if(Poker.makePlayerBet(amount, 1)) {
                       Poker.moved = true;
                       Poker.successfulMove = true;
               } else {
                 Poker.successfulMove = false;
               }



                  Poker.isPlayer1Turn = false;
                 } else if (Poker.isPlayer2Turn) {

                    if(Poker.makePlayerBet(amount, 2)) {
                       Poker.moved = true;
                       Poker.successfulMove = true;
                       Poker.isPlayer2Turn = false;
               } else {
                 Poker.successfulMove = false;
               }






           } else {
               Poker.updateMessages("Sorry, it is not your turn");

           }




       }
       /*************************
       soundBUttonActionPerformed()
       This is called when the sound on/off button is clicked
       **************************/
       public void soundButtonActionPerformed(ActionEvent evt) {
          Poker.updateMusic();
       }

       /***********************
       paintHand()
       Assigns the card pictures to the hands
       *************************/
       public static void paintHand (int playerNum,Card[] cards, int pos1in, int pos2in, boolean shown) {
              Toolkit tk = Toolkit.getDefaultToolkit();
              if (playerNum == 1) {

              if (shown) {
              //Card test = new Card(3,3);
              int y = 0;
              for (int x = 0; x < 5; x++) {
              cardPicsPlayer[y] = tk.getImage("Images/Cards/" + Card.getCardStringValue(cards[y]) + ".gif");
              y++;
              }
              pos1 = pos1in;
              pos2 = pos2in;
              } else {
              int y = 0;
              for (int x = 0; x < 5; x++) {
              cardPicsPlayer[y] = tk.getImage("Images/Cards/b1fv.gif");
              y++;
              pos1 = pos1in;
              pos2 = pos2in;
              }
              }
              }
              if (playerNum == 2) {
                       if (shown) {
                        //Card test = new Card(3,3);
                        int y = 0;
                        for (int x = 0; x < 5; x++) {
                        cardPicsComp1[y] = tk.getImage("Images/Cards/" + Card.getCardStringValue(cards[y]) + ".gif");
                        y++;
                        }
                        pos5 = pos1in;
                        pos6 = pos2in;
                        } else {
                        int y = 0;
                        for (int x = 0; x < 5; x++) {
                        cardPicsComp1[y] = tk.getImage("Images/Cards/b1fv.gif");
                        y++;
                        pos5 = pos1in;
                        pos6 = pos2in;
                        }
              }
              }   if (playerNum == 3) {
                       if (shown) {
                        //Card test = new Card(3,3);
                        int y = 0;
                        for (int x = 0; x < 5; x++) {
                        cardPicsComp2[y] = tk.getImage("Images/Cards/" + Card.getCardStringValue(cards[y]) + ".gif");
                        y++;
                        }
                        pos7 = pos1in;
                        pos8 = pos2in;
                        } else {
                        int y = 0;
                        for (int x = 0; x < 5; x++) {
                        cardPicsComp2[y] = tk.getImage("Images/Cards/b1fv.gif");
                        y++;
                        pos7 = pos1in;
                        pos8 = pos2in;
                        }
              }
              }    if (playerNum == 4) {
                       if (shown) {
                        //Card test = new Card(3,3);
                        int y = 0;
                        for (int x = 0; x < 5; x++) {
                        cardPicsComp3[y] = tk.getImage("Images/Cards/" + Card.getCardStringValue(cards[y]) + ".gif");
                        y++;
                        }
                        pos9 = pos1in;
                        pos10 = pos2in;
                        } else {
                        int y = 0;
                        for (int x = 0; x < 5; x++) {
                        cardPicsComp3[y] = tk.getImage("Images/Cards/b1fv.gif");
                        y++;
                        pos9 = pos1in;
                        pos10 = pos2in;
                        }
              }
              }     if (playerNum == 5) {
                       if (shown) {
                        //Card test = new Card(3,3);
                        int y = 0;
                        for (int x = 0; x < 5; x++) {
                        cardPicsComp4[y] = tk.getImage("Images/Cards/" + Card.getCardStringValue(cards[y]) + ".gif");

                        y++;
                        }
                        pos11 = pos1in;
                        pos12 = pos2in;
                        } else {
                        int y = 0;
                        for (int x = 0; x < 5; x++) {
                        cardPicsComp4[y] = tk.getImage("Images/Cards/b1fv.gif");
                        y++;
                        pos11 = pos1in;
                        pos12 = pos2in;
                        }
              }
              }



       }
       
       /**************************
       getImageForIcon()
       This method just returns the picture 
       of a card defined by the string value of the card
       *****************************/
       public static Image getImageForIcon (String nameOfCard) {
           Toolkit tk = Toolkit.getDefaultToolkit();
           return tk.getImage("Images/Cards/" + nameOfCard + "_small.jpg");


       }





      /*****************************
      paintComponent()
      Paints the screen
      *******************************/

       public void paintComponent(Graphics g) {

        super.paintComponent(g);   // calls the paintComponent method of JPanel to display the background
        g.setFont(new Font("SansSerif", Font.BOLD, 20));   // set font
        if (gameStage == 3) {
        Toolkit tk =  Toolkit.getDefaultToolkit();
        Image instructions = tk.getImage("Images/instructions.gif");
        g.drawImage(instructions, 0 ,0, this);

        }
        if (gameStage == 4) {
        Toolkit tk =  Toolkit.getDefaultToolkit();
        Image credits = tk.getImage("Images/credits.gif");
        g.drawImage(credits, 0 ,0, this);

        
        }






       if (gameStage == MENU) {
         Toolkit tk = Toolkit.getDefaultToolkit();
         Image back;
         back = tk.getImage("Images/mainMenu.gif");
         g.drawImage(back, 0,0, this);
         g.setColor(Color.white);
         g.drawString("Leaderboard", 820, 85);
         g.setFont(new Font("SansSerif", Font.BOLD, 16));
         int posX = 795;
         int posY = 100;
         HighScoreTransferable[] hScores = new HighScoreTransferable[30];
         hScores = Poker.updateHighScores();
         //hScores = Poker.updateHighScores();
         for (int i =0; i < 30; i++) {

           g.drawString((i+1) + ". " + Poker.HST[i].nameS, 765, 120 + (i*18) );





         }
         for (int i = 0; i < 30; i++) {
         if (!Poker.HST[i].scoreS.equals("0")) {
         g.drawString("$" + Poker.HST[i].scoreS, 885, 120 + (i*18) );
         }
         }







        }  if (gameStage == PLAYING) {


        g.setFont(new Font("SansSerif", Font.BOLD, 16));

        if (!mainGameIsVisible)
        g.drawImage(splashScreen,0,0,this);

        if (mainGameIsVisible) {
        g.setColor(Color.black);

        //g.drawString("Result is " + result,50,50);  // display
        g.drawImage(background,0,0,this);
        g.drawImage(optionsBackground,0,650,this);

        int pos1out = pos1;
        int pos2out = pos2;
        int pos5out = pos5;
        int pos6out = pos6;
        int pos7out = pos7;
        int pos8out = pos8;
        int pos9out = pos9;
        int pos10out = pos10;
        int pos11out = pos11;
        int pos12out = pos12;
        int lol = 0;

        if (!Poker.player.isfolded) {

        lol = 1;
        //player cards
        for (int x = 0; x < 5; x++) {
            pos1out = pos1 + (lol*45);
            g.drawImage(cardPicsPlayer[x], pos1out, pos2out, this);
            lol++;
        }
        }

        if (!Poker.comp1.isfolded) {
        lol = 1;//computer1

        for (int z = 0; z < 5; z++) {
            pos5out = pos5 + (lol*35);
            g.drawImage(cardPicsComp1[z], pos5out, pos6out, this);
            lol++;

        }
        }
        if (!Poker.comp2.isfolded) {
        lol = 1;//computer2
          for (int t = 0; t < 5; t++) {
            pos7out = pos7 + (lol*35);
            g.drawImage(cardPicsComp2[t], pos7out, pos8out, this);
            lol++;
        }
        }
          lol = 1;
          //computer3
          if (!Poker.comp3.isfolded) {
          for (int a = 0;a < 5; a++) {
            pos9out = pos9 + (lol*35);
            g.drawImage(cardPicsComp3[a], pos9out, pos10out, this);
            lol++;
        }
          }
          lol = 1;//computer4
          if (!Poker.comp4.isfolded) {
          for (int b = 0; b < 5; b++) {
            pos11out = pos11 + (lol*35);
            g.drawImage(cardPicsComp4[b], pos11out, pos12out, this);
            lol++;
        }
          }

        //g.drawString(stringToDraw, pos3, pos4);
        g.drawString("Pot: $" + potTotal, 450,75);

        System.out.println(Poker.player.name);
         System.out.println(playerName);
        g.drawString(playerName ,400,450);  // display
        g.drawString("$" + player1am, 400, 465);
        g.drawString("Bet This Round: $" + player1amBet, 400, 480);


        g.drawString("Napolean", 56 , 150);

        if (!Poker.comp2.isBankrupt) {
         g.drawString("$" + comp2am, 56, 165);
        } else {
         g.drawString("BANKRUPT!", 56,165);
         }
        g.drawString("Bet This Round: $" + comp2amBet, 56, 180);
          if (Poker.numPlayers == 2) {
        g.drawString(player2Name, 56,335);
        } else{
        g.drawString("Forrester", 56, 335);
        }
        if (!Poker.comp1.isBankrupt) {
        g.drawString("$" + comp1am, 56, 350);
        } else {
          g.drawString("BANKRUPT!", 56,350);
          }
        g.drawString("Bet This Round : $" + comp1amBet, 56, 365);
        g.drawString("Fabio", 685, 150);
        if (!Poker.comp3.isBankrupt) {
        g.drawString("$" + comp3am, 685, 165);
        } else {
           g.drawString("BANKRUPT!", 685,165);
          }

        g.drawString("Bet This Round : $" + comp3amBet, 685, 180);

        g.drawString("Jesus", 685, 335);
        if (!Poker.comp4.isBankrupt) {
        g.drawString("$" + comp4am, 685, 350);
        } else {
           g.drawString("BANKRUPT!", 685,350);
          }
        g.drawString("Bet This Round : $" + comp4amBet, 685, 365);


        g.setColor(Color.white);
        g.drawString("Sound Off", 790, 730);
        

        g.drawString(currentString, 350, 350);





        }

       }
        }
}