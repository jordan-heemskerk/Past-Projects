<?php
session_start();

$users = simplexml_load_file("xml/users.xml");

$username = $_POST['username'];

foreach ($users as $key=>$value) {
  if (strcasecmp($value->pass, $_POST["password"]) == 0) ;

  if (strcasecmp($value->name, $_POST["username"]) ==  0 && strcasecmp($value->pass, $_POST["password"]) == 0) {

      //echo var_dump($value->alias[0][0]);
      $_SESSION['loggedIn'] = 1;
      $_SESSION['alias'] = (string)$value->alias;
      $_SESSION['username'] = (string)$value->name;
  }
}

if ($_SESSION['loggedIn'] == 1) {
  include "headers/admin.txt";
  echo "<div class='main'><h3>Welcome, " . $_SESSION['alias'] . "</h3>";
  echo '
   <div style="margin-left: auto; margin-right:auto; width: 700px; id="stats">
  
   </div>
   <br />

  ';
  echo "<div width='900px'>";
  include "bodies/admin.txt";
  echo "</div>";
  echo "</div>";
  include "footers/main.txt";


} else {


//"<script type='text/javascript'>alert('Incorrect Username/Password Combination')";

header('Location: index.php');

}







?>