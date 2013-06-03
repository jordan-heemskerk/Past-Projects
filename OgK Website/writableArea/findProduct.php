<?php
include "functionLib.php";
$key = $_GET['key'];
$productsFound = searchProducts($key);




$toReturn = "";

$invalid = true;
if ($_GET['info']='id') {
    $arraykey = str_split($key, 2);
    
    if (isset($arraykey[4])) {
       switch ((int)$arraykey[4]) {
         case 1: $toReturn = $toReturn . "XS "; $invalid = false;
         break;
         case 2: $toReturn = $toReturn . "S "; $invalid = false;
         break;
         case 3: $toReturn = $toReturn . "M "; $invalid = false;
         break;
         case 4: $toReturn = $toReturn . "L "; $invalid = false;
         break;
         case 5: $toReturn = $toReturn . "XL "; $invalid = false;
         break;
         default: $invalid = true;
       }

    }




switch ((int)$arraykey[1]) {
  case 1:
    $toReturn = $toReturn . "Girls "; $invalid = false;
    if (isset($arraykey[2])) {
      switch ((int)$arraykey[2]) {

        case 1: $toReturn = $toReturn . "OgK Tree "; $invalid = false;
          switch ((int)($arraykey[3])) {

            case 1: $toReturn = $toReturn . "White "; $invalid = false;
            break;

            case 2: $toReturn = $toReturn . "Blue "; $invalid = false;
            break;

            //default: $toReturn = "Invalid ID";
          }
          break;

        case 2: $toReturn = $toReturn . "OgK Fade "; $invalid = false;
          switch ((int)($arraykey[3])) {
  
          
          }
          break;
        case 3: $toReturn = $toReturn . "OgK Classic "; $invalid = false;
          switch ((int)($arraykey[3])) {
          
          
          }
          break;
        //default: $toReturn = "Invalid ID";
      } //switch
    } //if

    break;
    
    

  case 2:
    $toReturn = $toReturn . "Guys "; $invalid = false;
    
    
    break;
  
  //default: $toReturn = "Invalid ID";
} //switch


    
    
    
    
      if (isset($arraykey[0])) {
       switch (((int)$arraykey[0])) {
        case 1: $toReturn = $toReturn . "Tee "; $invalid = false;
        break;
        case 2: $toReturn = $toReturn . "Hoodie "; $invalid = false;
        break;
        case 3: $toReturn = $toReturn . "Crew "; $invalid = false;
        //default: $toReturn = "Invalid ID";
      } //switch
    } //if isset array[1]
    

    
    



}
if ($invalid) {
$toReturn = "invalid";
}
 if (isset($arraykey[4])){
 $xml = simplexml_load_file('xml/productCatalog.xml');
 foreach ($xml->product as  $key => $value) {
     echo $value->id . "<br />";
     if ((int)$value->id == (int)$key) {
        $toReturn = $toReturn . " Good To Go!";

     } //if


 }//foreach
 }

echo $toReturn;








?>