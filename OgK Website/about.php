<?php
  /*******************************************************
  Name: about.php
  Date: 20/09/2010
  Author: Jordan Heemskerk
  Purpose: PHP script dicattaes what is displayed on about.php, 
  just includes the necessary files and conatiners for now, may
  do more later but will update comments if more functionality 
  is necessary.
  ********************************************************/
  session_start(); //entire site uses session variables
  
  
  //build page
  include "headers/main.txt"; //header file
  echo "<div class='main'>"; ///container
  include "bodies/about.txt"; //body file, contains most of site
  echo "</div>";
  include "footers/main.txt"; //footer file, linear across all pages.
?>