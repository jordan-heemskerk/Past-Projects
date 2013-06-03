<?php
$txns = simplexml_load_file("xml/txn.xml");
   $total;
   foreach ($txns->transaction as $key => $value) {

       echo "<p>Transaction from";
       if ($value->type == "OgK") {
       echo " a personal sale ";
       }  else {
       echo " the OgK Website " ;
       }

       echo "on: ";
       echo $value->date;
       echo $value->txn_date;
       echo " totalling $";
       echo $value->txn_total;
       echo ".";
       echo "Items sold were: ";
       foreach ($value->items->item as $key2=>$item) {
          echo $item->name . ", ";
       }
       echo "that is all.";
       echo "</p>";
       $total += (double)$value->txn_total;

   }

   echo "We have made $" . $total . " so far!";





?>