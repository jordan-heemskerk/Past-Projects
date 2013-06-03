/*****************************************
Name: HighScoreTransferable.java
Author: Jordan Heemskerk
Purpose: This object contains variables that store high scores/ It is 
         used to import and export the high scores from a text file.
Date: Mon. Dec. 15, 2008
******************************************/



public class HighScoreTransferable
implements Comparable<HighScoreTransferable>

{
  


  String nameS;
  String scoreS;
  int scoreI;

  public HighScoreTransferable (String name, String score) {
    nameS = name;
    scoreS = score;
    try {
    scoreI = Integer.parseInt(score);
    } catch(Exception e) {
    scoreI = -1;
    }
  }
  /***************************
  compareTo()
  Allows an array of HST's to be sorted by their "score" value
  ***************************/
  public int compareTo(HighScoreTransferable hST) {
    try {
      if (this.scoreI < hST.scoreI) return 1;
      else if (this.scoreI == hST.scoreI) return 0;
      else if (this.scoreI > hST.scoreI) return -1;
    } catch (Exception e) { System.out.println("UNABLE TO SORT VALUE");
      return 0;
    }
      return 2;
  }
  /**********************************
  toString()
  Returns the strsing value of this HST
  **********************************/
  public String toString() {

     return this.nameS + "@" + this.scoreI;


  }




}