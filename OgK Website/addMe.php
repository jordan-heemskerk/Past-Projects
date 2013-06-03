<?php

if (isset($_POST['name'])) {
   $xml = simplexml_load_file("xml/goodEmail.xml");
   $newEmail = $xml->addChild("email", $_POST['email']);
   $newEmail->addAttribute("name", $_POST['name']);
   $xml->asXML("xml/goodEmail.xml");

   include "headers/main.txt";

echo "

<div class='main'>
<h1>Thank You</h1>
<p>You have been added to our emailing list!</p>




";


}   else {

include "headers/main.txt";

echo "

<div class='main'>
<h1>Add Yourself To Our Mailing List</h1>
<p>Enter your email below to be added to our emailing list.</p> <p>Thanks,</p> <p>The OgK Crew</p>
<form action='addMe.php' method='post'>
Name: <input type='text' name='name' /><br />
Email: <input type='text' name='email' /><br />
<input type='submit' name='submit' />
</form>




";




echo "</div>";


}
include "footers/main.txt";



?>