/*****************************************
Name: Wait.java
Author: Jordan Heemskerk
Purpose: Contain methods for stopping the current thread and pausing the game.
Date: Mon. Dec. 15, 2008
******************************************/


public class Wait {
  public static void oneSec() {
     try {
       Thread.currentThread().sleep(1000);
       }
     catch (InterruptedException e) {
       e.printStackTrace();
       }
     }  
  public static void manySec(long s) {
     try {
       Thread.currentThread().sleep(s * 1000);
       }
     catch (InterruptedException e) {
       e.printStackTrace();
       }
     }
     
     
  public static void forInput() {
  try {
    Poker.moved = false;
       do {
       Thread.currentThread().sleep(1);
      } while (!Poker.moved);
       Poker.moved = false;}
     catch (InterruptedException e) {
       e.printStackTrace();
       }

     }

  }

