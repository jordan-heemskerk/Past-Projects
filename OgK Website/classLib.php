<?php

/*****************************************************************************
Name: classLib.php
Author: Jordan Heemskerk
Creation Date: 11/08/2010
Update Date: 11/08/2010
Purpose: This php file is used to store the basic classes used by the OGK
Clothing website.
******************************************************************************/

class Product {

    private $name = "undefined";
    private $categoryNum = -1;
    private $categoryValue = "undefined";
    private $sexNum = -1;
    private $sexValue = "undefined";
    private $price = -1;
    private $sizes = array();


    function __construct ($nameIn, $categoryNumIn, $sexNumIn, $priceIn) {
        $this->name = $nameIn;
        $this->categoryNum = $categoryNumIn;
        $this->sexNum = $sexNumIn;
        $this->price = $priceIn;

        switch ($this->categoryNum) {
          case 1:
             $this->categoryValue = "Tee";
             break;
          case 2:
             $this->categoryValue = "Hoodie";
             break;
          case 3:
             $this->categoryValue = "Crew Neck";
             break;
          default:
             $this->categoryValue = "Category Number Unrecognized";
             break;
        } //switch categoryNum

        switch ($this->sexNum) {
          case 1:
             $this->sexValue = "Girls";
             break;
          case 2:
             $this->sexValue = "Guys";
             break;
          case 3:
             $this->sexValue = "Unisex";
             break;
          default:
             $this->sexValue = "Sex Number Unrecognized";
             break;
        } //switch sexNum

    } //__construct()


    //-----------Operation Methods---------------

    function defineNewSKU ($color, $sizeNum, $id, $description, $image, $sell) {

      $this->sizes[$sizeNum][$color]["id"] = $id;
      $this->sizes[$sizeNum][$color]["descriptionLocation"] = $description;
      $this->sizes[$sizeNum][$color]["imageLocation"] = $image;
      $this->sizes[$sizeNum]["sell"] = $sell;


    } //defineNewSKU

    //-----------Opertaion Methods--------------
    
    //-------------Get Methods--------------

    function getName() {
       return $this->name;
    } //getName
    function getCategoryNum() {
       return $this->categoryNum;
    } //categoryNum
    function getCategoryValue() {
       return $this->categoryValue;
    } //categoryValue
    function getSexNum() {
       return $this->sexNum;
    } //sexNum
    function getSexValue() {
       return $this->sexValue;
    } //sexValue
    function getPrice() {
       return $this->price;
    } //descriptionLocation
    function getSerializedSizes() {
       return serialize($this->sizes);
    } //descriptionLocation
    
    function getSell($sizeNum) {
        return $this->sizes[$sizeNum]["sell"];

    }

    //--------------Get Methods---------------
    
    //--------------Set Methods---------------
    
    function setName ($in) {
        $this->name = $in;
    } //setName
    function setCategoryNum ($in) {
        $this->categoryNum = $in;
    } //setcategoryNum
    function setCategoryValue ($in) {
        $this->categoryValue = $in;
    } //setCategoryvalue
    function setSexNum ($in) {
        $this->sexNum = $in;
    } //setSexNum
    function setSexValue ($in) {
        $this->sexValue = $in;
    } //setCategoryvalue
    function setPrice ($in) {
        $this->price = $in;
    } //setPrice

    //----------------Set Methods-----------------








} //Product

/*******************************************************************
Class: IPN
Author: Jordan Heemskerk
Date: 17/09/2010
Note: Most properties of IPN will be public for easy access, 
though this is not responsible coding, the mere amount of 
properties contained in such class makes creating get and set
functions a wasteful task. *txn = transaction
****************************************************************/
class IPN {
     public static $error = "UNSET_IPN_VARIABLE_ERROR";
     public static $noData = "!NODATA!";

     public $txn_type; //Transaction Type

     //TRANSACTION AND NOTIFICATION RELATED VARIABLES
     public $business; //Business, or primary account email
     public $notify_version; //Message's version number
     public $parent_txn_id; //Original transaction id if a failed txn occurs
     public $receipt_id; //ID of the receipt
     public $resend; //Whether this IPN was a resend
     public $test_ipn;  //Whether this was a test. Equal to 1 if true.
     public $verify_sign; //Encrypted string used to validatethe authenticity of the transaction

     //BUYER INFORMATION VARIABLES
     public $address_country; //Customer's country
     public $address_city; //Customer's city
     public $address_country_code; //Coutnry code
     public $address_name; //Name used with address
     public $address_state; //State (Province in our case)
     public $address_status; //Confirmed address? Paypal side.
     public $address_street; //Customers street
     public $address_zip; //Customer's zip (postal) code
     public $contact_phone; //Customers telephone number provided
     public $first_name; //Customer First Name
     public $last_name; //Customer Last Name
     public $payer_business_name; //Customer is another business; their business name
     public $payer_email; //Customers email
     public $payer_id; //Unique customer ID

     //PAYMENT INFORMATION VARIABLES
     public $auth_amount; //Authorization amount
     public $auth_exp; //Authorization expiration date (HH:MM:SS DD Mmm YY,YYYY)
     public $auth_status; //Status of authorization
     public $exchange_rate; //Exchange rate if another currency is used
     public $invoice; //Our backend invoice number that is passed through

     public $itemName = array ();
     public $itemId = array ();

     public $mc_currency; //Currency of a payment
     public $mc_fee; //transaction fee taken by paypal, mc_gross - mc_fee is equal to the amount given to the reciever_email account (or us).
     public $mc_gross; //Full amount of payment before tansaction fee is taken off
     public $memo; //Memo as entered by a customer on the paypal site.
     public $num_cart_items; //If a transaction is a cart, the number of items in a cart.
     public $payer_status; //Whether the customer has a VERFIFIED PayPal Account
     public $payment_date; //Time/Date stamp generated by paypal, format: HH:MM:SS DD Mmm YY,YYYY PST

     public $item_gross = array();
     

     public $payment_fee = "depricated"; //use $mc_fee instead
     public $payment_status; //Possible values: Canceled_reversal, Completed, Created, Denied, Expired, Failed, Pending, Refunded, Reversed, Processed, Voided
     public $payment_type; //Either echeck or instant

     public $quantity = array();

      function  __construct() {
         $this->txn_type = $error; //Transaction Type

     //TRANSACTION AND NOTIFICATION RELATED VARIABLES
     $this->business = $error; //Business, or primary account email
     $this->notify_version = $error; //Message's version number
     $this->parent_txn_id = $error; //Original transaction id if a failed txn occurs
     $this->receipt_id = $error; //ID of the receipt
     $this->resend = $error; //Whether this IPN was a resend
     $this->test_ipn = $error;  //Whether this was a test. Equal to 1 if true.
     $this->verify_sign = $error; //Encrypted string used to validatethe authenticity of the transaction

     //BUYER INFORMATION VARIABLES
     $this->address_country = $error; //Customer's country
     $this->address_city = $error; //Customer's city
     $this->address_country_code = $error; //Coutnry code
     $this->address_name = $error; //Name used with address
     $this->address_state = $error; //State (Province in our case)
     $this->address_status = $error; //Confirmed address? Paypal side.
     $this->address_street = $error; //Customers street
     $this->address_zip = $error; //Customer's zip (postal) code
     $this->contact_phone = $error; //Customers telephone number provided
     $this->first_name = $error; //Customer First Name
     $this->last_name = $error; //Customer Last Name
     $this->payer_business_name = $error; //Customer is another business; their business name
     $this->payer_email = $error; //Customers email
     $this->payer_id = $error; //Unique customer ID

     //PAYMENT INFORMATION VARIABLES
     $this->auth_amount = $error; //Authorization amount
     $this->auth_exp = $error; //Authorization expiration date (HH:MM:SS DD Mmm YY,YYYY)
     $this->auth_status = $error; //Status of authorization
     $this->exchange_rate = $error; //Exchange rate if another currency is used
     $this->invoice = $error; //Our backend invoice number that is passed through

     $this->itemName = array ();
     $this->itemId = array ();

     $this->mc_currency = $error; //Currency of a payment
     $this->mc_fee = $error; //transaction fee taken by paypal, mc_gross - mc_fee is equal to the amount given to the reciever_email account (or us).
     $this->mc_gross = $error; //Full amount of payment before tansaction fee is taken off
     $this->memo = $error; //Memo as entered by a customer on the paypal site.
     $this->num_cart_items = $error; //If a transaction is a cart, the number of items in a cart.
     $this->payer_status = $error; //Whether the customer has a VERFIFIED PayPal Account
     $this->payment_date = $error; //Time/Date stamp generated by paypal, format: HH:MM:SS DD Mmm YY,YYYY PST

     $this->item_gross = array();
     

     $this->payment_fee = "depricated"; //use $mc_fee instead
     $this->payment_status = $error; //Possible values: Canceled_reversal, Completed, Created, Denied, Expired, Failed, Pending, Refunded, Reversed, Processed, Voided
     $this->payment_type = $error; //Either echeck or instant

     $this->quantity = array();






     
     } //contructor

     public function IPNLoadArray ($in) {

     $this->txn_type = $in['txn_type']; //Transaction Type

     //TRANSACTION AND NOTIFICATION RELATED VARIABLES
      $this->business = $in['business']; //Business, or primary account email
      $this->notify_version = $in['notify_version']; //Message's version number
      $this->parent_txn_id = $in['parent_txn_id']; //Original transaction id if a failed txn occurs
      $this->receipt_id = $in['receipt_id']; //ID of the receipt
      $this->resend = $in['resend']; //Whether this IPN was a resend
      $this->test_ipn = $in['test_ipn'];  //Whether this was a test. Equal to 1 if true.
      $this->verify_sign = $in['verify_sign']; //Encrypted string used to validatethe authenticity of the transaction

     //BUYER INFORMATION VARIABLES
      $this->address_country = $in['address_country']; //Customer's country
      $this->address_city = $in['address_city']; //Customer's city
      $this->address_country_code = $in['address_country_code']; //Coutnry code
      $this->address_name = $in['address_name']; //Name used with address
      $this->address_state = $in['address_state']; //State (Province in our case)
      $this->address_status = $in['address_status']; //Confirmed address? Paypal side.
      $this->address_street = $in['address_street']; //Customers street
      $this->address_zip = $in['address_zip']; //Customer's zip (postal) code
      $this->contact_phone = $in['contact_phone']; //Customers telephone number provided
      $this->first_name = $in['first_name']; //Customer First Name
      $this->last_name = $in['last_name']; //Customer Last Name
      $this->payer_business_name = $in['payer_business_name']; //Customer is another business; their business name
      $this->payer_email = $in['payer_email']; //Customers email
      $this->payer_id = $in['payer_id']; //Unique customer ID

     //PAYMENT INFORMATION VARIABLES
      $this->auth_amount = $in['auth_amount']; //Authorization amount
      $this->auth_exp = $in['auth_exp']; //Authorization expiration date (HH:MM:SS DD Mmm YY,YYYY)
      $this->auth_status = $in['auth_status']; //Status of authorization
      $this->exchange_rate = $in['exchange_rate']; //Exchange rate if another currency is used
      $this->invoice = $in['invoice']; //Our backend invoice number that is passed through

      foreach ($in as $key => $value) {

         $words = explode("_", $key);
         if (strcasecmp($words[0], "item") == 0) {
            $charArr = str_split($words[1]);
            if ($charArr[1] == "a") {
              $this->itemName[count($itemName)] = $value;
            } //if a
            if ($charArr[1] == "u") {
              $this->itemId[count($itemNum)] = $value;
            } //if u
         } //if key begins with item

      } //foreach

      $this->mc_currency = $in['mc_currency']; //Currency of a payment
      $this->mc_fee = $in['mc_fee']; //transaction fee taken by paypal, mc_gross - mc_fee is equal to the amount given to the reciever_email account (or us).
      $this->mc_gross = $in['mc_gross']; //Full amount of payment before tansaction fee is taken off
      $this->memo = $in['memo']; //Memo as entered by a customer on the paypal site.
      $this->num_cart_items = $in['num_cart_items']; //If a transaction is a cart, the number of items in a cart.
      $this->payer_status = $in['payer_status']; //Whether the customer has a VERFIFIED PayPal Account
      $this->payment_date = $in['payment_date']; //Time/Date stamp generated by paypal, format: HH:MM:SS DD Mmm YY,YYYY PST
      $this->mc_currency = $in['mc_currency']; //CAD
      $this->mc_fee = $in['mc_fee']; //Deductible fee that paypal takes
      $this->mc_gross = $in['mc_gross']; //Full amount of payment before the mc_fee is deducted

      foreach ($in as $key => $value) {
         $words = explode("_", $key);
         if (strcasecmp($words[0], "mc") == 0 && strcasecmp($words[1], "gross") == 0 && isset($words[2])) {
           $this->item_gross[count($this->item_gross)] = $value;
         } //if key begins with item

      } //foreach


      //$payment_fee = "depricated"; //use $mc_fee instead
      $this->payment_status = $in['payment_status']; //Possible values: Canceled_reversal, Completed, Created, Denied, Expired, Failed, Pending, Refunded, Reversed, Processed, Voided
      $this->payment_type = $in['payment_type']; //Either echeck or instant

        foreach ($in as $key => $value) {

         if (preg_match("/quantity/", $key)) {
           $this->quantity[count($this->quantity)] = $value;
         } //if key begins with item

      } //foreach









     }//IPNLoadArray()

     public function asXML () {


       //Use the Document Object Model to create an XML tree and then convert it to a string and return it.
       $dom = new DomDocument();
       $txn = $dom->createElement("transaction");
       $dom->appendChild($txn);
       
       $pp = $dom->createElement("type");
          $txn->appendChild($pp);
          $pp->appendChild($dom->createTextNode("PayPal"));

       if (strcasecmp($this->test_ipn,"1") == 0) {
          $test = $dom->createElement("testIPN");
          $txn->appendChild($test);
          $test->appendChild($dom->createTextNode("TRUE"));

       } //if

       $date = $dom->createElement("date");
       $txn->appendChild($date);
        $dateText = $dom->createTextNode($this->payment_date);
       $date->appendChild($dateText);

       $total = $dom->createElement("txn_total");
       $txn->appendChild($total);
       $total->appendChild($dom->createTextNode($this->mc_gross));

       $profit = $dom->createElement("profit");
       $txn->appendChild($profit);
       $profit->appendChild($dom->createTextNode(($this->mc_gross - $this->mc_fee)));

       $items = $dom->createElement("items");
       $txn->appendChild($items);

      foreach ($this->itemName as $key => $value) {
           //key's of each array should correspond to the same item.

           $item = $dom->createElement("item");
           $items->appendChild($item);


           $value2 = $dom->createElement("name");
           $item->appendChild($value2);
           $value2->appendChild($dom->createTextNode($value));

           $id = $dom->createElement("id");
           $item->appendChild($id);
           $id->appendChild($dom->createTextNode($this->itemId[$key]));

           $item_gross = $dom->createElement("item_gross");
           $item->appendChild($item_gross);
            $item_gross->appendChild($dom->createTextNode($this->item_gross[$key]));



           $quantity = $dom->createElement("quantity");
           $item->appendChild($quantity);
           $quantity->appendChild($dom->createTextNode($this->quantity[$key]));

      }//foreach item

       $fname = $dom->createElement("first_name");
       $txn->appendChild($fname);
       $fname->appendChild($dom->createTextNode($this->first_name));

       $lname = $dom->createElement("last_name");
       $txn->appendChild($lname);
       $lname->appendChild($dom->createTextNode($this->last_name));

       $phone = $dom->createElement("phone");
       $txn->appendChild($phone);
       $phone->appendChild($dom->createTextNode($this->contact_phone));

       //------address---------
       $add = $dom->createElement("address");
       $txn->appendChild($add);

       $cou = $dom->createElement("country");
       $add->appendChild($cou);
       $cou->appendChild($dom->createTextNode($this->address_country));

       $city = $dom->createElement("city");
       $add->appendChild($city);
       $city->appendChild($dom->createTextNode($this->address_city));

       $state = $dom->createElement("state");
       $add->appendChild($state);
       $state->appendChild($dom->createTextNode($this->address_state));

       $street = $dom->createElement("street");
       $add->appendChild($street);
       $street->appendChild($dom->createTextNode($this->address_street));

       $zip = $dom->createElement("zip");
       $add->appendChild($zip);
       $zip->appendChild($dom->createTextNode($this->address_zip));

       $name = $dom->createElement("name");
       $add->appendChild($name);
       $name->appendChild($dom->createTextNode($this->address_name));
       //---------address-------------


        $dom->preserveWhiteSpace = false;
        $dom->formatOutput = true;
        return $dom->saveXML();
     }  //saveAsXML











} //IPN

/*****************************
Class: GenTXN
Author:Jordan
Date: 01/12/2010
Purpose: Store basic properties of a single txn, allow to operate on them
********************************/

class genTXN {
      private$date;
      private $txnType;
      private $txnTotal;
      private $items;
      private $name;
      private $address;
      public $xml;

      function __construct ($indate="", $intxnType="", $intxnTotal="", $initems="", $inname="", $inaddress="") {
         $this->date = $indate;
         $this->txnType = $intxnType;
         $this->txnTotal = $intxnTotal;
         $this->items = $initems;
         $this->name = $inname;
         $this->address = $inaddress;

      } //__construct

      //get methods
       function getDate() {
          return $this->date;
       } //getDate
       function getType() {
          return $this->txnType;
       } //getType
         function getTotal() {
          return $this->txnTotal;
       } //getTotal
         function getItems() {
          return $this->items;
       } //getItems
         function getName() {
          return $this->name;
       } //getName
       function getAddress() {
          return $this->address;
       } //getName
       
       function createFromXML ($inXML) {
           $this->items = "<ul>";
           $xml = simplexml_load_string ($inXML);
           if ($xml->type == 'PayPal') {
           $this->date = $xml->date;
           $this->txnType="PayPal";
           $this->txnTotal=$xml->profit;
           $this->name = $xml->first_name . " " . $xml->last_name;
           $this->address = $xml->address->street . ", " .  $xml->address->city . ", " . $xml->address->state . ", " . $xml->address->country . ", " . $xml->address->zip;

           }
           if ($xml->type == 'OgK') {
           $this->date = $xml->txn_date;
           $this->txnType="OgK";
           $this->txnTotal=$xml->txn_total;
           $this->name = "No Name";
           $this->address = "No Address";
           }

           foreach ($xml->items->item as $key => $value) {
             $this->items .= "<li>" . $value->name . "</li>";

           }

           $this->items .= "</ul>";




       }
       
       
       //COMAPATORS ARE LOCATED IN FUNCTIONLIB.PHP !!!!










}//genTXN


?>