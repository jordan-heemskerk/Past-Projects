<?php
/****************************************************************************
Name: register.php
Author: Jordan Heemskerk
Date: 12/01/2011
Purpose: register.php will be passed post variables that allow for the
creation of users. Listed below:

register - True: yes, register a new user
         - False: no, do nothing
first_name - the first name of the new user
last_name - the last name of the new user
email - a validated (javascript) email for the user
password - a pre-encrypted (javascript) md5 has password for the new user
*****************************************************************************/

$vars = $_POST;   //copy from post array

//ensuer we want to create a new user
if (strcasecmp($vars['register'], "true") == 0) {

$xml = simplexml_load_file("xml/ogk_users.xml");  //load xml
$id = (int)($xml->attributes()->num) + 1;    //create id
$xml->attributes()->num = $id; //update id

//write new xml
$user = $xml->addChild("user");
$user->addChild("id", $id);
$user->addChild("firstName", $vars['first_name']);
$user->addChild("lastName", $vars['last_name']);
$user->addChild("email", $vars['email']);
$user->addChild("password", $vars['password']);
$activity = $user->addChild("activity");
$activity->addChild("itemsRated");

//save new xml
$xml->asXML("xml/ogk_users.xml");

include "headers/main.txt";

echo "<div class='main'><h1>Thank You</h1> <p>You may now sign in and access all features of the OgK Website. </p> <a href='login.php'>Login</a>";

echo "</div>";

include "footers/main.txt";


} else {

echo "Error: register value is null or false";

}






?>