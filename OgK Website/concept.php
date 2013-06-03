<?php
/****************************************************************************************
Name: 
concept.php

Author: 
Jordan Heemskerk

Date:
26/01/11

Purpose:
The file concept.php is used to generate HTML/XHTML to
display the OgK Concept System to a user. It is run and accessed 
through variables in the URL.

        Mandatory Variables:
                  1. display
                     "main" - display the concept.php homepage
                     "girls" - display girls section
                     "guys" - display guys section
                     "(any id)" - the statistics and page for the concept
                     "registerScreen" - show the user a register screen
                  
                  2. rate
                     "true" - rate a concept
                     "false" - do not rate a concept
        
        Non-Mandatory Variables
                  1. id
                     ID of item to rate, if rate is true

                  2. as
                     1 - I'd wear it
                     -1 - I wouldn't wear it
                     
***********************************************************************************************/


session_start();

$vars = $_GET;

if (strcasecmp($vars["display"], "registerScreen") == 0) {
   include "headers/main.txt";
   echo "<div class='main'>";
   include "bodies/concept_register.txt";
   echo "</div>";
   include "footers/main.txt";

} // if registerScreen





?>