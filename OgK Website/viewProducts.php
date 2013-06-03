<?
session_start();
//determine what filters are present

include "headers/main.txt";
echo "<div class='main' style='height: 400px'>";

include "functionLib.php";
$products = unserialize(serializedProductsToObjects());

if ($_GET["filter"] == "2" || strcasecmp($_GET["filter"], "guys") == 0) echo "<h1>Guys Products</h1>";
if ($_GET["filter"] == "1" || strcasecmp($_GET["filter"], "girls") == 0) echo "<h1>Girls Products</h1>";
if (!isset($_GET["filter"]) || strcasecmp($_GET["filter"], "all") == 0)   echo "<h1>All Products</h1>";
echo "<div class='productMenu'>";

echo "<h4>Choose One:</h4>";
echo "<span class='productLink' onclick='goTo(" . '"' . "viewProducts.php?filter=" . $_GET['filter'] . "&category=1" . '"' . ")'>Tees</span><br />";
echo "<span class='productLink' onclick='goTo(" . '"' . "viewProducts.php?filter=" . $_GET['filter'] . "&category=2" . '"' . ")'>Hoodies</span><br />";
echo "<span class='productLink' onclick='goTo(" . '"' . "viewProducts.php?filter=" . $_GET['filter'] . "&category=3" . '"' . ")'>Crew Necks</span><br />";

echo "</div>";
$category = $_GET['category'];
echo "<div class='scroll' id='scroll'>";
echo "<div class='productIcons' id='productIcons'>";

if (!isset($_GET['category']) && $_GET["filter"] == "2") {
   echo "<img src='img/guysPromo1.jpg' /> <script type='text/javascript'>document.getElementById('scroll').style.overflow = 'visible';</script>";
}
if (!isset($_GET['category']) && $_GET["filter"] == "1") {
   echo "<img src='img/girlsPromo1.jpg' /> <script type='text/javascript'>document.getElementById('scroll').style.overflow = 'visible';</script>";
}


//if ($_GET['category'] == 2) {



//  } //hoodies
//if ($_GET['category'] == 3) {
 // }

  
  //
  $num = 0;
foreach ($products as $value) {



       if (!isset($_GET["filter"])  || strcasecmp($_GET["filter"], "all") == 0) {
          echo "<div class='productIconContainer' name='" . $value->getCategoryNum() . "'>";
          echo generateDefaultHTMLForProduct ($value);
            echo "</div>";
     } else {

       if ($_GET["filter"] == "2" || strcasecmp($_GET["filter"], "guys") == 0) {
         if ($value->getSexNum() == 2 &&  $value->getCategoryNum() == $category || $value->getSexNum() == 3 &&  $value->getCategoryNum() == $category) {
            // echo "<div class='productIconContainer' name='" . $value->getCategoryNum() . "'>";
             $num += substr_count(generateIconHTMLForProduct ($value),"<img");
            echo generateIconHTMLForProduct ($value);
           // echo "</div>";
         }
       } //guys
       if ($_GET["filter"] == "1" || strcasecmp($_GET["filter"], "girls") == 0) {
         if ($value->getSexNum() == 1 &&  $value->getCategoryNum() == $category|| $value->getSexNum() == 3 && $value->getCategoryNum() == $category) {
             //echo "<div class='productIconContainer' name='" . $value->getCategoryNum() . "'>";
             $num += substr_count(generateIconHTMLForProduct ($value),"<img");
            echo generateIconHTMLForProduct ($value);
              //echo "</div>";
         }
       } //girls
     }//else







}
echo "<script type='text/javascript'>

   //alert(document.getElementById('scroll').style.overflow);

  </script>

";


echo "   <script type='text/javascript' >
 $(document).ready(
        function(){

          $('#productIcons').css('width' , '" . $num*220 . "');

        });
        

   </script> ";
echo "</div>";
 echo "</div>";
echo "</div>";



include "footers/main.txt";

?>

