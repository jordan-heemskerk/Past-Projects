<?php
/****************************************************************
Name: paypal_ipn.php
Author: Jordan Heemskerk
Date: 27/09/2010
Purpose: This form takes the variables given from the Instant Payment
Notification tool from PayPal, and organizes them, then stores them in
paypal.xml and transactionMaster.xml.
*****************************************************************/

include "classLib.php";

$process = 0;  ///Process the payemnt or line up for manual investigation.

// read the post from PayPal system and add 'cmd'
$req = 'cmd=_notify-validate';

foreach ($_POST as $key => $value) {
  $value = urlencode(stripslashes($value));
  $req .= "&$key=$value";
}  //foreach

// post back to PayPal system to validate
$header .= "POST /cgi-bin/webscr HTTP/1.0\r\n";
$header .= "Content-Type: application/x-www-form-urlencoded\r\n";
$header .= "Content-Length: " . strlen($req) . "\r\n\r\n";

//$fp = fsockopen ('ssl://www.sandbox.paypal.com', 443, $errno, $errstr, 30); //--TESTING W/ SANDBOX---
$fp = fsockopen ('ssl://www.paypal.com', 443, $errno, $errstr, 30);       //REAL USE


/*// assign posted variables to local variables
$item_name = $_POST['item_name'];
$item_number = $_POST['item_number'];
$payment_status = $_POST['payment_status'];
$payment_amount = $_POST['mc_gross'];
$payment_currency = $_POST['mc_currency'];
$txn_id = $_POST['txn_id'];
$receiver_email = $_POST['receiver_email'];
$payer_email = $_POST['payer_email'];   */

if (!$fp) {
  // HTTP ERROR
} else {
  fputs ($fp, $header . $req);
  while (!feof($fp)) {
    $res = fgets ($fp, 1024);
    if (strcmp ($res, "VERIFIED") == 0) {
      // check the payment_status is Completed
      // check that txn_id has not been previously processed
      // check that receiver_email is your Primary PayPal email
      // check that payment_amount/payment_currency are correct
      // process payment
    
      $process= 1;

    } //if
    else if (strcmp ($res, "INVALID") == 0) {
      // log for manual investigation
    } //elseif
  } //while
  fclose ($fp);
} //else


if ($process == 1) {
   
   $ipn = new IPN();
   $ipn->IPNLoadArray($_POST);

   $dom = new DomDocument();
   $dom->load("xml/txn.xml");
   $txn = new DomDocument();
   $txn->loadXML($ipn->asXML());
   
   $ipntxnNode = $txn->getElementsByTagName("transaction")->item(0);
   
   $node = $dom->importNode($ipntxnNode, true);
   $dom->documentElement->appendChild($node);
   $dom->preserveWhiteSpace = false;
   $dom->formatOutput = true;
   $dom->save("xml/txn.xml");





  


} //if

?>