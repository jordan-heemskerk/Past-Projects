<?php
/**********************************************************************************
Name: functionLib.php
Author: Jordan Heemskerk
Creation Date: 11/08/2010
Update Date:11/08/2010
Purpose: This file is a container for all function used across many files in the OGK
Clothing website.
***********************************************************************************/

//-------------Includes---------------------

include "classLib.php"; //primary classes

//------------Includes---------------------


//------------Functions---------------------

function serializedProductsToObjects() {
    $objectsProducts = array();
    $xmlProducts = simplexml_load_file("xml/productCatalog.xml");
    $productExists = false;
    $productExistsIndex = 0;
    foreach ($xmlProducts->product as $key => $xmlProduct) {
      //check to see if product base has already been created in $objectsProducts
      foreach ($objectsProducts as $key2 => $value2) {
          if (strcasecmp((string)$value2->getName(), $xmlProduct->name) == 0 && strcasecmp($xmlProduct->sex->attributes()->num, $value2->getSexNum()) == 0) {
              $productExists = true;
              $productExistsIndex = $key2;
          } //if
      } //foreach
      if ($productExists) {
          $objectsProducts[$productExistsIndex]->defineNewSKU((string)$xmlProduct->color, (int)$xmlProduct->size->attributes()->num, (int)$xmlProduct->id, (string)$xmlProduct->descriptionLocation, (string)$xmlProduct->imageLocation, (string)$xmlProduct->sell);
      } else {
          $sizeOfObjectsProducts = count($objectsProducts);
          $objectsProducts[$sizeOfObjectsProducts + 1] = new Product ((string)$xmlProduct->name, (int)$xmlProduct->category->attributes()->num, (int)$xmlProduct->sex->attributes()->num, (string)$xmlProduct->price);
          $objectsProducts[$sizeOfObjectsProducts + 1]->defineNewSKU((string)$xmlProduct->color, (int)$xmlProduct->size->attributes()->num, (int)$xmlProduct->id, (string)$xmlProduct->descriptionLocation, (string)$xmlProduct->imageLocation, (string)$xmlProduct->sell);
      }
       $productExists = false;
       $productExistsIndex = 0;
    } //foreach $xmlProduct
     foreach ($objectsProducts as $value) {

        //echo var_dump($value) . "<br /><br /><br />" ;
     }
    return serialize($objectsProducts);
} //productsToObjects

function generateDefaultHTMLForProduct ($product) {
    $color = "";
    $toReturn = "";
    if ($product instanceof Product) {
         $sizes = unserialize($product->getSerializedSizes());
         foreach ($sizes["3"] as $key2 => $value2) {



           $toReturn = $toReturn . "<img class='productImage' height='150' width='150' src='" . $sizes["3"][$key2]["imageLocation"] . "' />";

         }
         $toReturn = $toReturn . "<h2 class='productName'>" . $product->getName() .  "</h2>";

         $toReturn = $toReturn . "<h3 class='productType'>" . $product->getSexValue() . " " . $product->getCategoryValue() . "</h3>";
          $toReturn = $toReturn . "<p class='productPrice'>&#36;" . $product->getPrice() . "</p>";
          $toReturn = $toReturn . "<p class='productSizes'>Sizes Available: | ";
          foreach ($sizes as $key2=>$value2) {
                $toReturn = $toReturn . getSizeValue($key2) . " | ";
         }

          $toReturn = $toReturn . "</p>";
         foreach ($sizes["3"] as $key2 => $value2) {
           $toReturn = $toReturn . "<p class='productDescription'>";
           error_reporting(E_ALL & ~E_WARNING); //tunr off warnings
           $toReturn = $toReturn . file_get_contents($sizes["3"][$key2]["descriptionLocation"]);
           error_reporting(E_ALL); //tunr on warnings
           $toReturn = $toReturn . "</p>";


         }


         return $toReturn;
    } else {
        return "Not a Product";
    }


} //generateDeafultHTMLForProduct

function generateIconHTMLForProduct ($product) {
    $color = "";
    $toReturn = "";
    if ($product instanceof Product) {
         $sizes = unserialize($product->getSerializedSizes());





          foreach ($sizes["3"] as $key2 => $value2) {

             if ($key2 != "sell") {
           $toReturn = $toReturn . "<div class='productIcon' id='" . $value2['id'] . "' onclick='goTo(" .  '"' . "viewProduct.php?id=" . $value2['id'] . '")' . "'  >\n";

              //echo "<div class='productIconImage' >";
              $toReturn = $toReturn . "<img  height='200' width='200' src='" . $sizes["3"][$key2]["imageLocation"] . "' onmouseover='showIconInfo(" . '"' . $value2['id'] . '"' . ")' onmouseout='hideIconInfo(" . '"' . $value2['id'] . '"' . ")' />\n";
             // echo "</div>";
              //echo "<div class='productIconInfo' id='icon" . $value2['id'] . "'>";
             $toReturn = $toReturn . "<span class='productIconInfo' id='icon" . $value2['id'] . "'>" . $key2 . " " . $product->getName();
              //$toReturn = $toReturn . " "  . $product->getCategoryValue() . "";
              $toReturn = $toReturn . " <br />$" . $product->getPrice() . "</span>\n";

              //echo "</div>";
               $toReturn = $toReturn . "</div>\n";

             }// not sell
          }




         return $toReturn;


    } else {
        return "Not a Product";
    }


} //generateDeafultHTMLForProduct

function generateAdminHTMLForProduct ($product) {
    $color = "";
    $toReturn = "";
    if ($product instanceof Product) {
         $sizes = unserialize($product->getSerializedSizes());
         foreach ($sizes["3"] as $key2 => $value2) {



           $toReturn = $toReturn . "<img class='productImage' height='150' width='150' src='" . $sizes["3"][$key2]["imageLocation"] . "' />";

         }
         $toReturn = $toReturn . "<h2 class='productName'>" . $product->getName() . "</h2>";
         $toReturn = $toReturn . "<h3 class='productType'>" . $product->getSexValue() . " " . $product->getCategoryValue() . "</h3>";
          $toReturn = $toReturn . "<p class='productPrice'>&#36;" . $product->getPrice() . "</p>";
          $toReturn = $toReturn . "<p class='productSizes'>Sizes Available: | ";
          foreach ($sizes as $key2=>$value2) {
                $toReturn = $toReturn . getSizeValue($key2) . " | ";
         }

          $toReturn = $toReturn . "</p>";
         foreach ($sizes["3"] as $key2 => $value2) {

           $toReturn = $toReturn . "<p class='productDescription'>";
           error_reporting(E_ALL & ~E_WARNING); //tunr off warnings
           $toReturn = $toReturn . file_get_contents($sizes["3"][$key2]["descriptionLocation"]);
           error_reporting(E_ALL); //tunr on warnings
           $toReturn = $toReturn . "</p>";


         }

         foreach ($sizes as $sizeNum => $color) {
            foreach ($color as $colorValue =>$details) {

              $toReturn = $toReturn . "<h3>Admin Viewable Details For: <span style='color: red'>" . $product->getName() . " " . $product->getSexValue() . " " . getSizeValue($sizeNum) . " " . $colorValue . "</span></h3>";
              $toReturn = $toReturn . "<p>Image Location: " . $sizes["3"][$key2]["imageLocation"] . "</p>";
              $toReturn = $toReturn . "<p>Description Location: " . $sizes["3"][$key2]["descriptionLocation"] . "</p>";

            }

         }

         return $toReturn;
    } else {
        return "Not a Product";
    }


} //generateAdminHTMLForProduct


function getSizeValue ($num) {
   switch ($num) {
          case 1:
            return "XS";

          case 2:
            return "S";

          case 3:
            return "M";

          case 4:
            return "L";

          case 5:
            return "XL";

      } //switch

}  //getSizeValue

function searchProducts ($key) {

    $numericalData = false;
    $productsToReturn = array();
    if (is_numeric($key)) $numericalData = true;
    $products = unserialize(serializedProductsToObjects());
    if (strcasecmp ($key, "!all") == 0) {
      return $products;
    }
    foreach ($products as $product) {
       if ($numericalData) {

          if (strcasecmp($product->getPrice(), $key) == 0) {

             $productsToReturn[count($productsToReturn) + 1] = $product;
             continue;
          } //if price

          foreach (unserialize($product->getSerializedSizes()) as $size => $color) {
              foreach ($color as $value) {
                //echo $value['id'] . "   :    " . $key . "<br />";
                if (strcasecmp($value['id'], $key) == 0 or (int)$value['id'] == (int)$key) {
                    //echo "jackpot";
                    $productsToReturn[count($productsToReturn) + 1] = $product;
                    continue;
                }
              } //colors

          } //foreach sizes
       } else {

           if (strcasecmp($product->getName(), $key) == 0) {
             $productsToReturn[count($productsToReturn) + 1] = $product;
             continue;
           } //if name
           $keywords = explode(" ", $product->getName());
           $found = false;
           foreach ($keywords as $keyword) {
               if (strcasecmp($keyword, $key) == 0) {
                 $productsToReturn[count($productsToReturn) + 1] = $product;
                 $found = true;
                 break;
             } //if name

           } //foreach keyword
           if ($found) {
              continue;
           }
           
            foreach (unserialize($product->getSerializedSizes()) as $size => $color) {
              foreach ($color as $colorValue => $value) {
                if (strcasecmp($colorValue, $key) == 0) {
                  $productsToReturn[count($productsToReturn) + 1] = $product;
                  $found = true;
                  break;
                }

              } //colors
                if ($found) break;
            } //foreach sizes

             if ($found) continue;
           if (strcasecmp($product->getCategoryValue(), $key) == 0 || strcasecmp($product->getCategoryValue() . "s", $key) == 0) {
             $productsToReturn[count($productsToReturn) + 1] = $product;
             continue;
           } //if category
           if (strcasecmp($product->getSexValue(), $key) == 0) {
             $productsToReturn[count($productsToReturn) + 1] = $product;
             continue;
           } //if category


       } //else



    }//foreach products



     return $productsToReturn;
} //searchProducts()


function generateEncryptedPayPalData($input) {

        $MY_KEY_FILE = "paypal/my-prvkey.pem";
	$MY_CERT_FILE  = "paypal/mypubcert.pem";
	$PAYPAL_CERT_FILE = "paypal/paypalcert.pem";
	$OPENSSL = "/usr/bin/openssl";


	if (!file_exists($MY_KEY_FILE)) {
		echo "ERROR: MY_KEY_FILE $MY_KEY_FILE not found\n";
	}
	if (!file_exists($MY_CERT_FILE)) {
		echo "ERROR: MY_CERT_FILE $MY_CERT_FILE not found\n";
	}
	
	if (!file_exists($PAYPAL_CERT_FILE)) {
		echo "ERROR: PAYPAL_CERT_FILE $PAYPAL_CERT_FILE not found\n";
	}


	//Assign Build Notation for PayPal Support
	$input['bn']= 'OGKClothing_ShoppingCart_WPS_CA';

	$data = "";
	foreach ($input as $key => $value) {
		if ($value != "") {
			//echo "Adding to blob: $key=$value\n";
			$data .= "$key=$value\n";
		}
	}

	$openssl_cmd = "($OPENSSL smime -sign -signer $MY_CERT_FILE -inkey $MY_KEY_FILE " .
						"-outform der -nodetach -binary <<_EOF_\n$data\n_EOF_\n) | " .
						"$OPENSSL smime -encrypt -des3 -binary -outform pem $PAYPAL_CERT_FILE";

	exec($openssl_cmd, $output, $error);

	if (!$error) {
		return implode("\n",$output);
	} else {
		return "ERROR: encryption failed";
	}








}


function rawNewsToHTML ($input) {
  $paras = explode("\n" , $input);
  foreach ($paras as $para) {


    $output = $output . "<p>" . $para . "</p>";



  }//foreach

  //$output = implode("<br />", $paras);

  return $output;






} //rawNewsToHTML

function payPalTimestamp($paypal) {

   $data = explode(" ",$paypal); //split paypal date sting into parts
    //diregard first part
   // var_dump ($data);

    $day = str_replace(",", "", $data[2]);

    switch ($data[1]) {
    case "Jan": $monthNum = 1; break;
    case "Feb": $monthNum = 2; break;
    case "Mar": $monthNum = 3; break;
    case "Apr": $monthNum = 4; break;
    case "May": $monthNum = 5; break;
    case "Jun": $monthNum = 6; break;
    case "Jul": $monthNum = 7; break;
    case "Aug": $monthNum = 8; break;
    case "Sep": $monthNum = 9; break;
    case "Oct": $monthNum = 10; break;
    case "Nov": $monthNum = 11; break;
    case "Dec": $monthNum = 12; break;
    } //swicth

    $year = $data[3];

     $ts = mktime(0, 0, 0, $monthNum, $day, $year);
  // $ts = strtotime($paypal);
     // echo $day;
      //echo $monthNum;
      //echo $year;
  return $ts;



}

function ogkTimestamp ($ogk) {

$data = explode("/", $ogk);
$dayNum = $data[0];
$monthNum = $data[1];
$yearNum = $data[2];
 //echo "!!!";
 //echo $dayNum;
 //echo $monthNum;
 //echo $yearNum;
 //echo "!!!!";
$ts = mktime(0, 0, 0, $monthNum, $dayNum, $yearNum);
//echo $ts;
  //echo date("l dS \of F Y h:i:s A", $ts);
return  $ts;

}


//-----------comparators for genTXN-----------------

       function newtooldcomp($a, $b) {
          if (is_a($a, genTXN)) {
          if (strcasecmp($a->getType(),"OgK") == 0) {
             //echo "OGK";
             //echo $a->getType();
             $aTime = ogkTimestamp ($a->getDate());
          }  else {
            $aTime = payPalTimestamp ($a->getDate());
          }

           if (strcasecmp($b->getType(),"OgK") == 0) {
            //  echo "OGK";
             $bTime = ogkTimestamp ($b->getDate());
          }  else {
            $bTime = payPalTimestamp ($b->getDate());
          }

          //echo $a->getDate() . "     " . $b->getDate() . "     ";
          //echo $aTime  . "  " . $bTime . "  : ";
           if ($aTime == $bTime) {
           //echo "equal <br />";
           return 0;
          }

          if ($aTime < $bTime) {
          // echo "less <br />";
           return 1;

          }  else {
          // echo "more <br />";
           return -1;

          }


          }//
          else {
          //echo "DAMN!";

          }


       } //newtoold
       
         function oldtonewcomp($a, $b) {
            if (is_a($a, genTXN)) {
          if (strcasecmp($a->getType(),"OgK") == 0) {
             //echo "OGK";
             //echo $a->getType();
             $aTime = ogkTimestamp ($a->getDate());
          }  else {
            $aTime = payPalTimestamp ($a->getDate());
          }

           if (strcasecmp($b->getType(),"OgK") == 0) {
            //  echo "OGK";
             $bTime = ogkTimestamp ($b->getDate());
          }  else {
            $bTime = payPalTimestamp ($b->getDate());
          }

          //echo $a->getDate() . "     " . $b->getDate() . "     ";
          //echo $aTime  . "  " . $bTime . "  : ";
           if ($aTime == $bTime) {
           //echo "equal <br />";
           return 0;
          }

          if ($aTime > $bTime) {
           //echo "less <br />";
           return 1;

          }  else {
          // echo "more <br />";
           return -1;

          }


          }//
          else {
          //echo "DAMN!";

          }

       } //oldtonew

       function numitemscomp( $a, $b) {

           if (substr_count($a->getItems(), "<li>") == substr_count($b->getItems(), "<li>")) {
            return 0;
          }
          if (substr_count($a->getItems(), "<li>") < substr_count($b->getItems(), "<li>")) {
             return 1;
          }  else {
             return -1;
          }






       }//numitemscomp
       function highcomp($a, $b) {
        // echo $a->getTotal() . "   " . $b->getTotal() . " <br />";
           if ((double)$a->getTotal() == (double)$b->getTotal()) {return 0; }
           if ((double)$a->getTotal() < (double)$b->getTotal()) {
           //  echo "jackpot";
             return 1;
           } else {
           //  echo "meh";
             return -1;
           }



       }
       
        function lowcomp($a, $b) {
         //echo $a->getTotal() . "   " . $b->getTotal() . " <br />";
           if ((double)$a->getTotal() == (double)$b->getTotal()) {return 0; }
           if ((double)$a->getTotal() > (double)$b->getTotal()) {
             //echo "jackpot";
             return 1;
           } else {
            // echo "meh";
             return -1;
           }



       }



       //-----------------------------------------



//--------------Functions-----------------------







?>