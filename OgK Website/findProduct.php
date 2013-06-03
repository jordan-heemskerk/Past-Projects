<?php
include "functionLib.php";
$key = $_GET['key'];
$productsFound = searchProducts($key);




$toReturn = "";

$invalid = true;
if ($_GET['info']=='id') {
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


switch ((int)$arraykey[0]) {
  case 1: //TEES

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
  
              case 1: $toReturn = $toReturn . "Pink "; $invalid = false;
              break;
  
              case 2: $toReturn = $toReturn . "White "; $invalid = false;
              break;
  
              //default: $toReturn = "Invalid ID";
            }
            break;
          case 3: $toReturn = $toReturn . "OgK Classic "; $invalid = false;
            switch ((int)($arraykey[3])) {
              
              case 1: $toReturn = $toReturn . "Purple "; $invalid = false;
              break;
  
              case 2: $toReturn = $toReturn . "Black "; $invalid = false;
              break;
            
            
            }
            break;
          //default: $toReturn = "Invalid ID";
        } //switch
      } //if
  
      break;
      
      
  
    case 2:
      $toReturn = $toReturn . "Guys "; $invalid = false;
       switch ((int)$arraykey[2]) {
  
          case 1: $toReturn = $toReturn . "Stay OgK "; $invalid = false;
            switch ((int)($arraykey[3])) {
  
              case 1: $toReturn = $toReturn . "Purple "; $invalid = false;
              break;
  
              case 2: $toReturn = $toReturn . "White "; $invalid = false;
              break;
  
              //default: $toReturn = "Invalid ID";
            }
            break;
  
          case 2: $toReturn = $toReturn . "OgK Cursive "; $invalid = false;
            switch ((int)($arraykey[3])) {
  
              case 1: $toReturn = $toReturn . "White "; $invalid = false;
              break;
  
              case 2: $toReturn = $toReturn . "Black "; $invalid = false;
              break;
  
              //default: $toReturn = "Invalid ID";
            }
            break;
          case 3: $toReturn = $toReturn . "OgK Classic "; $invalid = false;
            switch ((int)($arraykey[3])) {
  
              case 1: $toReturn = $toReturn . "Black "; $invalid = false;
              break;
  
              case 2: $toReturn = $toReturn . "Purple "; $invalid = false;
              break;
  
            
            }
            break;
          //default: $toReturn = "Invalid ID";
        } //switch
      
      
      break;
    
    //default: $toReturn = "Invalid ID";
  } //switch
  break;
  
  
  case 2: //hoodies
  
  switch ((int)$arraykey[1]) {
    case 2:
      $toReturn = $toReturn . "Guys "; $invalid = false;
      if (isset($arraykey[2])) {
        switch ((int)$arraykey[2]) {

          case 1: $toReturn = $toReturn . "OgK Neon "; $invalid = false;
            switch ((int)($arraykey[3])) {

              case 1: $toReturn = $toReturn . "Black "; $invalid = false;
              break;

            }

        }
      }
  }

  break;

    case 3: //crews

  switch ((int)$arraykey[1]) {
    case 2:
      $toReturn = $toReturn . "Guys "; $invalid = false;
      if (isset($arraykey[2])) {
        switch ((int)$arraykey[2]) {

          case 1: $toReturn = $toReturn . "OgK Cross "; $invalid = false;
            switch ((int)($arraykey[3])) {

              case 1: $toReturn = $toReturn . "Black "; $invalid = false;
              break;

            }

        }
      }
  }

  break;
       }








      if (isset($arraykey[0])) {
       switch (((int)$arraykey[0])) {
        case 1: $toReturn = $toReturn . "Tee "; $invalid = false;
        break;
        case 2: $toReturn = $toReturn . "Hoodie "; $invalid = false;
        break;
        case 3: $toReturn = $toReturn . "Crewneck "; $invalid = false;
        //default: $toReturn = "Invalid ID";
      } //switch
    } //if isset array[1]




if ($invalid) {
$toReturn = "invalid";
}
 if (isset($arraykey[4])){
 $xml = simplexml_load_file('xml/productCatalog.xml');
 foreach ($xml->product as  $key1 => $value) {
     //echo $value->id . " : " . $key . "<br />";
     if ((int)$value->id == (int)$key) {
       //echo "JACKPOT";
        $toReturn = $toReturn . "<button type='button' onclick='addCurrentIdToCart()'>Add</button>";

     } //if


 }//foreach

 }
 echo $toReturn;


}


 if ($_GET['info'] == 'html') {
   $key = $_GET['key'];
 $toReturn = "";
 $xml = simplexml_load_file('xml/productCatalog.xml');
 foreach ($xml->product as  $key1 => $value) {
     //echo $value->id . " : " . $key . "\n";
     if ((int)$value->id == (int)$key) {
       //echo "JACKPOT";
       $toReturn =  $toReturn . "name=" . $value->sex . " " . $value->color . " " . $value->name . " " . $value->size . " " . $value->category .  "&";
       $toReturn =  $toReturn . "id=" . $value->id .  "&";
        $toReturn =  $toReturn . "quantity=" . $_GET['q'] .  "&";
         $toReturn =  $toReturn . "price=" . $value->price .  "&";
         $toReturn =  $toReturn . "gross=" . $value->price*$_GET['q']  .  "&";







     } //if


 }//foreach

  echo $toReturn;





 }










?>