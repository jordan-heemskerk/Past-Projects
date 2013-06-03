<?php
session_start();
include "functionLib.php";
include "headers/main.txt";

$productId = $_GET["id"];



$products = unserialize(serializedProductsToObjects());




echo "<div class='main' style='height:500px'>";
//echo "<h1>View Product</h1>";

foreach ($products as $product) {

    $sizes = unserialize($product->getSerializedSizes());
    foreach ($sizes as $size=>$color) {
        foreach ($color as $colorValue=>$properties) {


          if ($properties['id'] == $productId) {

             echo "<img onclick='preview(" . '"' .  $properties['imageLocation'] . '"' . ")'  style='float:right; cursor: pointer;' height='250' width='250' src='" . $properties['imageLocation'] . "' />";
             $num = 2;
             while ($num > 0) {
               //echo $num;
               $newFile = dirname($properties['imageLocation']) . "/" . basename($properties['imageLocation'], ".gif") . "_" . $num . "." .  pathinfo($properties['imageLocation'], PATHINFO_EXTENSION); //parent dir, base name, _, num, file ext.
               //echo $newFile;
                 $num++;
                 if (file_exists($newFile)) {
                   echo "<img style='float:right; cursor: pointer;' onclick='preview(\"" .  $newFile . "\")' height='250' width='250' src='" . $newFile . "' />";

                 }  else {
                   break;
                 }
                 if ($num > 25) break;  //backup to ensure the loop eventually ends.



             }

             echo "<h2>";
             echo $product->getName() . " - $" . $product->getPrice();;
             echo "</h2>";
             echo "<p>Click the images to the right to enlarge and get a better view</p>";
             echo "<h3>";
            // echo $product->getPrice();
             echo "</h3>";

             echo "<p>";
            error_reporting(E_ALL & ~E_WARNING); //tunr off warnings
           echo file_get_contents($sizes["3"][$colorValue]["descriptionLocation"]);
           error_reporting(E_ALL); //tunr on warnings
           echo "</p>";
            echo "<form action='addToCart.php' method='post' onsubmit='return checkCartAdd(this)'>";
        

            echo "<select name='size'>\n<option value='0'>Select Size</option>";

            foreach ($sizes as $sizeNum=>$whoCares) {
               if (strcasecmp($product->getSell($sizeNum), "Yes") == 0) {
               switch ($sizeNum) {
                case 1:
                echo "<option value='" . $sizeNum . "'>Extra Small</option>";
                break;
                case 2:
                echo "<option value='" . $sizeNum . "'>Small</option>";
                break;
                case 3:
                echo "<option value='" . $sizeNum . "'>Medium</option>";
                break;
                case 4:
                echo "<option value='" . $sizeNum . "'>Large</option>";
                break;
                case 5:
                echo "<option value='" . $sizeNum . "'>Extra Large</option>";
                break;
               }
               }
            }

            echo "</select><br /><br /><br />";
            echo "<input type='hidden' name='id' value='" . $_GET['id'] . "' />";
            echo "<button type='submit' style='padding: 2px;'>Add To Cart</button>";
            echo "</form><br /><br />"; 

           // echo "<p>



          }
        }

    }



}





echo "</div>";


include "footers/main.txt";








?>