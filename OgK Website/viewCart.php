<?php

session_start();
include "functionLib.php";


include "headers/main.txt";
 echo "<form action='https://www.paypal.com/cgi-bin/webscr' method='post'><div id='addressBox' class='addressBox'>
   <h1>Checkout</h1>
   
   <p>OGK Clothing uses PayPal for its security and reputation among online payment services. You may pay with your credit card or a PayPal account. There is no need to have a PayPal account if you are paying by credit card. When you click 'Proceed To PayPal' you will be directed to the PayPal website to complete the rest of your transaction. After your transaction is completed, you will be returned to OGKClothing.com. Thank you.</p>
   <div class='floatRight'>
   <!-- PayPal Logo --><table border='0' cellpadding='0' cellspacing='0' align='right'><tr><td align='center'></td></tr>";
echo '<tr><td align="center"><a href="#" onclick="javascript:window.open(' . "'" . " https://www.paypal.com/cgi-bin/webscr?cmd=xpt/Marketing/popup/OLCWhatIsPayPal-outside','olcwhatispaypal','toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=yes, resizable=yes, width=400, height=350" . "'" . ');"><img  src="https://www.paypal.com/en_US/i/bnr/vertical_solution_PPeCheck.gif" border="0" alt="Solution Graphics"></a></td></tr></table><!-- PayPal Logo -->
      </div>'; echo "



   <p><strong>Please complete the following.</strong></p>
   First Name: <input type='text' name='first_name' /> <br />
   Last Name: <input type='text' name='last_name' /> <br />
 <!--  Address 1: <input type='text' name='address1' /> <br />
   Address 2: <input type='text' name='address2' /> <br />
   City: <input type='text' name='city' /> <br />
   State/Province/Territory(2 Char.): <input type='text' name='city' size='2' length='2'/> <br />
   Postal Code: <input type='text' name='zip' /><br />
   Country: <select name='country'><option value='CA'>Canada</option><option value='US' >United States</option></select><br /> -->
   Email: <input type='text' name='email' /><br />
 <!-- Phone: (<input type='text' name='night_phone_a' size='3' length='3'>) <input type='night_phone_b' size='7' length='7' / ><br /> -->
   <input type='submit' value='Proceed To PayPal' /> <input type='button' value='Cancel' onclick='cancelAddress()' />






 </div>

 ";
echo "<div class='main'><h1>Your Cart</h1>";
if( count($_SESSION['cart']) == 0) unset ($_SESSION['cart']);

if (isset($_SESSION['cart']) || count($_SESSION['cart']) != 0) {
$cart = $_SESSION['cart'];
$products = unserialize(serializedProductsToObjects());
echo '
<a href="emptyCart.php">Empty Cart</a><br /><br />

<table class="cartTable">
<tr>
<th>Product Description</th>
<th>Color</th>
<th>Size</th>
<th>Availability</th>
<th>Item Price</th>
<th>Remove</th>
</tr>
';
$totalPrice = 0;
$itemNum = 1;
$toEncrypt = array();
foreach ($cart as $num=>$id) {
    //echo $id . "<br />";
    foreach ($products as $product) {
       $sizes = unserialize($product->getSerializedSizes());
       foreach ($sizes as $size=>$color) {
           foreach ($color as $colorValue=>$properties) {
               if ($properties["id"] == $id) {
                echo "<tr>";
                echo "<td>";
                echo $product->getSexValue() . " " . $product->getName() . " " . $product->getCategoryValue();
                echo "</td><td>";
                echo $colorValue;
                echo "</td><td>";
                echo getSizeValue($size);
                echo "</td><td>In Stock</td><td>$";
                echo $product->getPrice();
                $totalPrice += $product->getPrice();
                echo "</td>";
                echo "<td><a href='removeItem.php?id=" . $id . "'>Remove This</a></td>";
                echo "</tr>";
                
                //PayPal Variables
                
                //echo "<input type='hidden' name='item_name_" . $itemNum . "' value='" . $product->getSexValue() . " " . getSizeValue($size) . " " . $colorValue . " " . $product->getName() . " " . $product->getCategoryValue() . "' />";
                //echo "<input type='hidden' name='item_number_" . $itemNum . "' value='" . $properties['id'] .  "' />";
                //echo "<input type='hidden' name='amount_" . $itemNum . "' value='" . $product->getPrice() .  "' />";
                
                $toEncrypt['item_name_' . $itemNum] = $product->getSexValue() . " " . getSizeValue($size) . " " . $colorValue . " " . $product->getName() . " " . $product->getCategoryValue();
                $toEncrypt['item_number_' . $itemNum] = $properties['id'];
                $toEncrypt['amount_' . $itemNum] = $product->getPrice();





                $itemNum++;
               }

           } //foreach color

       } //foreach sizes
    }//foreach productws




} //foreach
$toEncrypt['cert_id'] = 'Z4QZG6RXYR2KG';
$toEncrypt['cmd'] = '_cart';
$toEncrypt['upload'] = '1';
$toEncrypt['business'] = 'jordanh@shaw.ca';
$toEncrypt['currency_code'] = 'CAD';
echo '
<tr>
<td colspan="4" align="right">Total:</td>
<td>$' . money_format('%i',$totalPrice) . ' CAD</td>';
echo '
<td>
 <input type="hidden" name="encrypted" value="' .  generateEncryptedPayPalData($toEncrypt) . '" />   <input type="button" onclick="populateAddress()" value="Checkout">
 <input type="hidden" name="cmd" value="_s-xclick" />

</td></tr>
</table>
</form>


<a href="emptyCart.php">Empty Cart</a>


';









           } else {
             
             echo "<h2>Your cart is empty :(</h2>";
           };


echo "</div>";






include "footers/main.txt";

?>