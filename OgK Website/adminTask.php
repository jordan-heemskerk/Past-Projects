<?php

 session_start();


 if ($_SESSION["loggedIn"] == 1) {
   include "headers/admin.txt";

   if ($_POST['task'] == "updateProductCatalog") {

$lines = file($_FILES['url']['tmp_name']);

$xml = new SimpleXMLElement("<products>\n</products>");
$products = array();
foreach ($lines as $lineNum => $lineContent) {
  $products[$lineNum] = array();
  $productInfo = explode(",", $lineContent);
  //echo "<pre>" . var_dump($productInfo) . "</pre>";
  if (!$productInfo[0] == "") {
  $products[$lineNum]["id"] = trim($productInfo[0]);
  $products[$lineNum]["name"] = trim($productInfo[1]);
  $products[$lineNum]["color"] = trim($productInfo[2]);
  $products[$lineNum]["category"] = trim($productInfo[3]);
  $products[$lineNum]["sex"] = trim($productInfo[4]);
  $products[$lineNum]["price"] = trim($productInfo[5]);
  $products[$lineNum]["descriptionLocation"] = trim($productInfo[6]);
  $products[$lineNum]["imageLocation"] = trim($productInfo[7]);
  $products[$lineNum]["sell"] = trim($productInfo[8]);
  $products[$lineNum]["size"] = trim($productInfo[9]);
  
  $product = $xml->addChild("product");

  $name = $product->addChild("name",  $products[$lineNum]["name"]);

  $category = $product->addChild("category",  $products[$lineNum]["category"]);
   //num attribute for category
   if (strcasecmp($products[$lineNum]["category"], "Tees") == 0) {
       $category->addAttribute("num", "1");
   }
   if (strcasecmp($products[$lineNum]["category"], "Hoodies") == 0) {
       $category->addAttribute("num", "2");
   }
   if (strcasecmp($products[$lineNum]["category"], "Crews") == 0) {
       $category->addAttribute("num", "3");
   }
   
  $sex = $product->addChild("sex", $products[$lineNum]["sex"]);
   
   //num attribute for sex
   if (strcasecmp($products[$lineNum]["sex"], "Girls") == 0) {
       $sex->addAttribute("num", "1");
   }
   if (strcasecmp($products[$lineNum]["sex"], "Guys") == 0) {
       $sex->addAttribute("num", "2");
   }
   if (strcasecmp($products[$lineNum]["sex"], "Uni") == 0) {
       $sex->addAttribute("num", "3");
   }
   
   $id =  $product->addChild("id", $products[$lineNum]["id"]);
   

   $size =  $product->addChild("size", $products[$lineNum]["size"]);
    //num attribute for size
   if (strcasecmp($products[$lineNum]["size"], "XS") == 0) {
       $size->addAttribute("num", "1");
   }
   if (strcasecmp($products[$lineNum]["size"], "S") == 0) {
       $size->addAttribute("num", "2");
   }
   if (strcasecmp($products[$lineNum]["size"], "M") == 0) {
       $size->addAttribute("num", "3");
   }
   if (strcasecmp($products[$lineNum]["size"], "L") == 0) {
       $size->addAttribute("num", "4");
   }
   if (strcasecmp($products[$lineNum]["size"], "XL") == 0) {
       $size->addAttribute("num", "5");
   }



   $price =  $product->addChild("price", $products[$lineNum]["price"]);
   $descriptionLocation =  $product->addChild("descriptionLocation", $products[$lineNum]["descriptionLocation"]);
   $imageLocation =  $product->addChild("imageLocation", $products[$lineNum]["imageLocation"]);
   $sell =  $product->addChild("sell", $products[$lineNum]["sell"]);
   $color =  $product->addChild("color", $products[$lineNum]["color"]);


  }

}



//Format XML to save indented tree rather than one line
$dom = new DOMDocument('1.0');
$dom->preserveWhiteSpace = false;
$dom->formatOutput = true;
$dom->loadXML($xml->asXML());
//Echo XML - remove this and following line if echo not desired

if ($dom->save("xml/productCatalog.xml")) {
echo "<div class='main'><a href='admin.php'>Back To Admin Page</a><br />The following code was generated and saved to xml/productCatalog.xml:    ";

} else  {
echo "An error occurred while saving the following code to xml/productCatalog.xml:";
}
echo "<pre>";
echo htmlentities($dom->saveXML());
echo "</pre><a href='admin.php'>Back To Admin Page</a></div>";


}

if ($_POST['task'] == "changePassword") {
    $users = simplexml_load_file("xml/users.xml");
    $success = false;
    foreach ($users as $key => $value) {
        if ($value->name == $_SESSION['username']) {
            if ($_POST['oldPassword'] == $value->pass) {
                $value->pass = $_POST['newPassword'];
                $success = true;
            }
        }


    }
    $users->asXML("xml/users.xml");
    echo "<div class='main'> <a href='admin.php'>Back To Admin Page</a>";
    if ($success) {
       echo "<p>Your password has been changed.</p>";
    } else {
        echo "<p>Your password has not been changed. Please try again.</p>";
    }
    echo "<a href='admin.php'>Back To Admin Page</a></div>";


} //task change password

if ($_POST['task'] == 'sendEmail') {
      echo "<div class='main'>";
       echo "<a href='admin.php'>Back To Admin Page</a><br />";

    $good = simplexml_load_file('xml/goodEmail.xml');
    $bad = simplexml_load_file('xml/badEmail.xml');
    $ok = true;
    foreach ($good->email as $goodKey=>$goodValue) {
       $ok = true;
       foreach ($bad->email as $badKey => $badValue) {
           if (strcasecmp($badValue, $goodValue) == 0) {
              $ok = false;
              echo "MAIL TO: " . $badValue . " was rejected by ogkclothing.com";
           } //if
       }// foreach bad
       if ($ok) {
           $to = $goodValue;
           $subject = $_POST['subject'];
           $message = $_POST['body'];
           $headers = "From: OGK Clothing <news@ogkclothing.com>\r\nReply-To: ogkclothing@gmail.com";
           $headers .= "MIME-Version: 1.0\r\n";
           $headers .= "Content-Type: text/html; charset=ISO-8859-1\r\n";

           $mail_sent = @mail( $to, $subject, $message, $headers );
           if ($mail_sent) {
            echo "MAIL SUCCESSFUL TO: " . $to . "<br />";
           }  //if sent

       } //if ok

    }//foreach good
       echo "<br /><a href='admin.php'>Back To Admin Page</a></div>";


} ///jackpot

if ($_POST['task'] == "searchForProduct") {
    include "functionLib.php";
    $key = $_POST["key"];

    echo "<div class='main'> <a href='admin.php'>Back To Admin Page</a>";
    echo "<p>Searching for " . $key . "</p>";
    echo "<h1>Results:</h1>";
    $productsFound = searchProducts($key);


    foreach ($productsFound as $num => $product) {
      echo "<div class='adminProduct'>";
        echo generateAdminHTMLForProduct ($product);
        echo "</div>";
    }


    echo "<a href='admin.php'>Back To Admin Page</a></div>";


} //task change password

if ($_POST['task'] == "createNewNews") {
    include "functionLib.php";
    
    $uploaddir = 'files/news/';
    $uploadfile = $uploaddir . basename($_FILES['image']['name']);


    if (move_uploaded_file($_FILES['image']['tmp_name'], $uploadfile)) {
      echo "File is valid, and was successfully uploaded.\n";
    } else {
      echo "Possible file upload attack!\n";
      $uploadfile="nodirectory";
    }





    echo "<div class='main'>";
     echo "<a href='admin.php'>Back To Admin Page</a></div>";

    //echo rawNewsToHTML($_POST['body']);
    $newsXML = simplexml_load_file("xml/news.xml");

    foreach ($newsXML as $news) {
       if ($news['main'] == "true") {
           $news['main'] = "false";
       }
    }




    $news = $newsXML->addChild("news");
    $news->addAttribute("main", "true");
    $news->addChild("date", date("m/d/y"));
    $news->addChild("title", $_POST['title']);
    
    
    $paras = explode("\n", $_POST['body']);


    $content = $news->addChild("content");
    foreach ($paras as $para) {
        $content->addChild("paragraph", $para);
    }


    $news->addChild("id" , $newsXML["num"] + 1);
    $news->addChild("user", $_SESSION['alias']);
    $news->addChild("imageURL", $uploadfile);
    $newsXML["num"] = $newsXML["num"] + 1;   
    $dom = new DOMDocument('1.0');
    $dom->loadXML($newsXML->asXML());
    $dom->formatOutput = true;
    $dom->save('xml/news.xml');


    echo "<p>That news has been posted.</p>";

     echo "<a href='admin.php'>Back To Admin Page</a></div>";


    echo "</div>";






}

if ($_POST['task']=='txnQuery') {

  //include "classLib.php"; //include the class library for genTXN class; included in functionLib.php so uneeded to redeclare
   include "functionLib.php"; //function library, class library
//echo var_dump($_POST);

 switch ($_POST['startDateMonth']) {
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

$startTS = mktime(0, 0, 0, $monthNum, (int)$_POST['startDateDay'], (int)$_POST['startDateYear']);
 switch ($_POST['endDateMonth']) {
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
$endTS = mktime(0, 0, 0, $monthNum, (int)$_POST['endDateDay'], (int)$_POST['endDateYear']);


payPalTimestamp("13:59:02 Nov 12, 2010 PST");

$NEWEST_TO_OLDEST = 0;
$OLDEST_TO_NEWEST = 1;
$NUM_ITEMS = 2;
$HIGHEST_TOTAL = 3;
$LOWEST_TOTAL = 4;

$sort = -1;

switch ($_POST['sortBy']) {
case 'newtoold': $sort = $NEWEST_TO_OLDEST; break;
case 'oldtonew': $sort = $OLDEST_TO_NEWEST; break;
case 'numItems': $sort = $NUM_ITEMS; break;
case 'Htotal': $sort = $HIGHEST_TOTAL; break;
case 'Ltotal': $sort = $LOWEST_TOTAL; break;
default: $sort = 5; break;
}


$showDate = ($_POST['date'] == 'true' ? true : false);
$showTxnType = ($_POST['txnType'] == 'true' ? true : false);
$showTxnTotal = ($_POST['txnTotal'] == 'true' ? true : false);
$showItems = ($_POST['items'] == 'true' ? true : false);
$showName = ($_POST['name'] == 'true' ? true : false);
$showAddress = ($_POST['address'] == 'true' ? true : false);
$showTotal = ($_POST['showTotal'] == 'true' ? true : false);


$transactions = array();
$txns = simplexml_load_file("xml/txn.xml");
$total = 0.0;
$count = 0;
foreach ($txns as $key => $value) {
  $txn = new genTXN();
$txn->createFromXML($value->asXML());

if (strcasecmp($txn->getType(), "OgK") == 0) {
    //echo "OGL";
    //echo "<br /> " . ogkTimestamp($txn->getDate()) . "     " . $startTS . "     " . $endTS . " <br />";
    if (ogkTimestamp($txn->getDate()) > $startTS && ogkTimestamp($txn->getDate()) < $endTS) {

     $transactions[$count] = $txn;
     $count ++;
     $total += $txn->getTotal();
    }

} else {
  
  if (payPalTimestamp($txn->getDate()) > $startTS && payPalTimestamp($txn->getDate()) < $endTS) {
     $transactions[$count] = $txn;
     $count ++;
     $total += $txn->getTotal();
    }


}

unset($txn);
}

echo "<div class='main'>";
echo "<br /><a href='admin.php'>Back To Admin Page</a>";
echo "<h1>Your Query Returned These Results</h1>";
echo $showTotal ? "<span id='total'>Total: $" . $total . " of " . $count . " transactions.</span>" : "" ;
echo "<table>";
echo "<tr>";
echo ($showDate ?  "<th>Date</th>" : "" );
echo ($showTxnType ?  "<th>Transaction Type</th>" : "" );
echo ($showTxnTotal ?  "<th>Transaction Total</th>" : "" );
echo ($showItems ?  "<th>Items</th>" : "" );
echo ($showName ?  "<th>Name</th>" : "" );
echo ($showAddress ?  "<th>Address</th>" : "" );
echo "</tr>";





if ($sort == $NEWEST_TO_OLDEST) {
 usort($transactions, "newtooldcomp");
}
if ($sort == $OLDEST_TO_NEWEST) {
 usort($transactions, "oldtonewcomp");
}
if ($sort == $NUM_ITEMS) {
  usort($transactions, "numitemscomp");
}
if ($sort == $HIGHEST_TOTAL) {
  usort($transactions, "highcomp");
}
if ($sort == $LOWEST_TOTAL) {
  usort($transactions, "lowcomp");
}


 // var_dump($transactions);
 foreach ($transactions as $txn) {
 echo "<tr>";
setlocale(LC_MONETARY, 'en_CA');

echo ($showDate ?  "<td>" . $txn->getDate() . "</td>" : "" );
echo ($showTxnType ?  "<td>" . $txn->getType() . "</td>" : "" );
echo ($showTxnTotal ?  "<td>" . money_format('%i', ((double)$txn->getTotal())) . "</td>" : "" );
echo ($showItems ?  "<td>" . $txn->getItems() . "</td>" : "" );
echo ($showName ?  "<td>" . $txn->getName() . "</td>" : "" );
echo ($showAddress ?  "<td>" . $txn->getAddress() . "</td>" : "" );


echo "</tr>";
 }




echo "</table>";

echo "<br /><a href='admin.php'>Back To Admin Page</a>";

echo "</div>";

}

if ($_POST['task']=='createNewTxn') {
   $monthNum = 0;
    switch ($_POST['txnDateMonth']) {
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

    $xml = simplexml_load_file('xml/txn.xml');
    $txn = $xml->addChild("transaction");
    $txn->addChild("type", "OgK");
    $txn->addChild("txn_date", $_POST['txnDateDay'] . "/" . $monthNum . "/" . $_POST['txnDateYear']);
    $items = $txn->addChild("items");







     $total = 0.00;
     $products = simplexml_load_file('xml/productCatalog.xml');
     $ids = $_POST['id'];
     foreach ($ids as $key => $value) {
       foreach ($products->product as  $key1 => $value1) {
             if ($value == $value1->id) {
                $item = $items->addChild("item");
                $item->addChild("name", $value1->name);
                $item->addChild("id", $value1->id);
                $item->addChild("quantity", $_POST['quantity'][$key]);
                $item->addChild("item_gross", (double)$value1->price * $_POST['quantity'][$key]);
                $total = $total + (double)$value1->price * $_POST['quantity'][$key];
             }//if

  
        }//foreach
     } ///foreach
     
     $txn->addChild("txn_total", $total);
     $txn->addChild("profit", $total);
     $txn->addChild("by", $_SESSION['alias']);
        $xml->asXML("xml/txn.xml");

     echo "<div class='main'>";
          echo "<a href='admin.php'>Back To Admin Page</a><br /><p>The following was recorded. Thank You. </p><p>";


     echo htmlentities($txn->asXML());


    echo "</p><br /><a href='admin.php'>Back To Admin Page</a></div>";

   echo "</div>";
    







}






include "footers/main.txt";
} else {
header('Location: index.php');

}






?>




