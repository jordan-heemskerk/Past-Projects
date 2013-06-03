<?php

  
  
  

//define the receiver of the email
$to = 'ogkclothing@gmail.com';
//define the subject of the email
$subject = "What's New At OGK Clothing";
//create a boundary string. It must be unique 
//so we use the MD5 algorithm to generate a random hash
$random_hash = md5(date('r', time()));
//define the headers we want passed. Note that they are separated with \r\n
$headers = "From: OGK Clothing <news@ogkclothing.com>\r\nReply-To: ogkclothing@gmail.com";
//add boundary string and mime type specification
$headers .= "MIME-Version: 1.0\r\n";
$headers .= "Content-Type: text/html; charset=ISO-8859-1\r\n";
//define the body of the message.
ob_start(); //Turn on output buffering
?>



<html>
    <head>

    <style type="text/css">
    .main {

       padding: 10px;
       margin-right: 200px;
       margin-left: 200px;
       background-image:url('http://www.ogkclothing.com/img/main_background.jpg');
background-repeat:repeat;
       height: 500px;
       margin-top: 30px;
       text-align: justify;


    }

    body {
    background-color: black;
       color: white;
       font-family: Century Gothic, sans-serif;

    }

   .centerIMG {
   margin-left: auto;
   margin-right: auto;

   border: none;


   }
   
   .msgfoot {
      margin-left: 200px;
      margin-right: 200px;
      font-size: 10;
   

   }
   
   p {

   font-size: 11;

   }

    </style>

    <title>OGK Clothing - News Update</title>



    </head>


    <body >

    <center>
 <a href="http://www.ogkclothing.com/" target="_blank"><img src="http://www.ogkclothing.com/img/email/top.png" class="centerIMG" /></a>
           </center>
     <div class="main">
     <h1>What's New At OGK Clothing</h1>
     <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer nec odio. Praesent libero. Sed cursus ante dapibus diam. Sed nisi. Nulla quis sem at nibh elementum imperdiet. Duis sagittis ipsum. Praesent mauris. Fusce nec tellus sed augue semper porta. Mauris massa. Vestibulum lacinia arcu eget nulla. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. </p>
     <img src="http://www.ogkclothing.com/img/products/02020101.jpg" width="200px" style="float:right; padding: 5px;"/>
<p>Curabitur sodales ligula in libero. Sed dignissim lacinia nunc. Curabitur tortor. Pellentesque nibh. Aenean quam. In scelerisque sem at dolor. Maecenas mattis. Sed convallis tristique sem. Proin ut ligula vel nunc egestas porttitor. Morbi lectus risus, iaculis vel, suscipit quis, luctus non, massa. Fusce ac turpis quis ligula lacinia aliquet. </p>

<p>Mauris ipsum. Nulla metus metus, ullamcorper vel, tincidunt sed, euismod in, nibh. Quisque volutpat condimentum velit. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Nam nec ante. Sed lacinia, urna non tincidunt mattis, tortor neque adipiscing diam, a cursus ipsum ante quis turpis. Nulla facilisi. Ut fringilla. Suspendisse potenti. Nunc feugiat mi a tellus consequat imperdiet. Vestibulum sapien. Proin quam. Etiam ultrices. Suspendisse in justo eu magna luctus suscipit. </p>
 <img src="http://www.ogkclothing.com/img/products/03020101.jpg" width="200px" style="float:left; padding: 5px;"/>
<p>Sed lectus. Integer euismod lacus luctus magna. Quisque cursus, metus vitae pharetra auctor, sem massa mattis sem, at interdum magna augue eget diam. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Morbi lacinia molestie dui. Praesent blandit dolor. Sed non quam. In vel mi sit amet augue congue elementum. Morbi in ipsum sit amet pede facilisis laoreet. Donec lacus nunc, viverra nec, blandit vel, egestas et, augue. Vestibulum tincidunt malesuada tellus. Ut ultrices ultrices enim. Curabitur sit amet mauris. Morbi in dui quis est pulvinar ullamcorper. </p>

<p>Nulla facilisi. Integer lacinia sollicitudin massa. Cras metus. Sed aliquet risus a tortor. Integer id quam. Morbi mi. Quisque nisl felis, venenatis tristique, dignissim in, ultrices sit amet, augue. Proin sodales libero eget ante. Nulla quam. Aenean laoreet. Vestibulum nisi lectus, commodo ac, facilisis ac, ultricies eu, pede. </p>



     </div>

     <p class="msgfoot">You are recieving this email becuase you are on OGK Clothing's Mailing List. To be permanately removed <a href="http://www.ogkclothing.com/removeMe.php">click here</a>.</p>
    </body>





</html>

<?
//copy current buffer contents into $message variable and delete current output buffer
$message = ob_get_clean();
//send the email
$mail_sent = @mail( $to, $subject, $message, $headers );
//if the message is sent successfully print "Mail sent". Otherwise print "Mail failed" 
echo $mail_sent ? "Mail sent" : "Mail failed";

 ?>