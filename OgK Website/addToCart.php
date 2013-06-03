<?php
session_start();
//var_dump($_POST);
$productId = $_POST['id'];

//echo $productId . "<br />";
$productId = ((int)($productId/100))*100 + ((int)$_POST['size']);

//echo $productId . "<br />";
if (!isset($_SESSION['cart'])) {
$_SESSION['cart'] = array();
}
$_SESSION['cart'][count($_SESSION['cart'])] = $productId;

header('Location: viewCart.php');


?>