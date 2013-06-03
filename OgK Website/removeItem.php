<?php

session_start();

if (isset($_GET['id']) && isset($_SESSION['cart'])) {
   foreach ($_SESSION['cart'] as $key=>$cartItem) {
       if ($cartItem == $_GET['id']) {
           unset($_SESSION['cart'][$key]);
           break;
       }

   }


}

header('Location: viewCart.php');


?>