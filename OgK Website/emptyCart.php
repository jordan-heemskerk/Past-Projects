<?php

session_start();

unset($_SESSION['cart']);

header('Location: viewCart.php');


?>