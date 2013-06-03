<?php
/************************************
Name: viewLegal.php
Author: Jordan Heemskerk
Date: 26/01/11
Purpose: To display legal documents to a user of ogkclothing.com
         
         "show" variable:
           privacy - privacy policy
           service - terms and conditions of service
           usage - rules of usage

***************************************/
session_start();

include "headers/main.txt";

echo "<div class='main'>";

if ($_GET["show"] == "privacy") {
echo "<h1>Privacy Policy</h1>";
}
if ($_GET["show"] == "service") {
echo "<h1>Terms and Conditions of Service</h1>";
}
if ($_GET["show"] == "usage") {
echo "<h1>Rules of Usage</h1>";
}


echo "</div>";

include "footers/main.txt";


?>