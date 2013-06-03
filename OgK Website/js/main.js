 function goToAdmin() {
     document.getElementById("darkenBackground").style.display = "block";
    document.getElementById("loginBox").style.visibility = "visible";
     document.getElementById("loginBox").style.display = "block";

}

function cancelButton() {
   document.getElementById("darkenBackground").style.display = "none";
    document.getElementById("loginBox").style.visibility = "hidden";
     document.getElementById("loginBox").style.display = "none";

}

function checkAdmin(form) {
 

  document.getElementById("loginBox").style.visibility = "hidden";
     document.getElementById("loginBox").style.display = "none";
    
   form.password.value = hex_md5(form.password.value);

   return true;
}




        var check = 0;


        function changeLogo() {

      $("#logo").fadeOut(1000 ,function(){
       if (check == 0) {
           $("#logo").attr("src","img/ogkLogo2.gif");
          check =1;
          } else {
           $("#logo").attr("src","img/ogkLogo.gif");
           check = 0;
          }
          $("#logo").fadeIn(1000);
});




        }
              var picNum = 1;
              var timer;
                function changePicture() {



               if (picNum == 0) {

                  document.getElementById("changer").innerHTML = '<img src="img/Iheartogkbutton2.jpg"  /> ';

                  picNum = 1;

               } else if (picNum == 1) {


                   document.getElementById("changer").innerHTML = '<img src="img/headphonesOGKSite2.jpg"  />';
                  picNum = 2;

               }
               else if (picNum == 2) {


                   document.getElementById("changer").innerHTML = '<div style="float: right; "><a href="http://www.twitter.com/origkuruption"><img src="http://twitter-badges.s3.amazonaws.com/follow_us-c.png" style="margin-left: auto; margin-right: auto;" alt="Follow origkuruption on Twitter"/></a><br /><br /><iframe src="http://www.facebook.com/plugins/like.php?href=http%3A%2F%2Fwww.facebook.com%2Fpages%2FOriginal-Kuruption%2F149429638407919%3Fcreated&amp;layout=standard&amp;show_faces=false&amp;width=200&amp;action=like&amp;font=verdana&amp;colorscheme=dark&amp;height=35" scrolling="no" frameborder="0" style="border:none; overflow:hidden; width:200px; height:50px;" allowTransparency="true"></iframe></div><h1>Social Networking</h1><h2>Join us on Facebook; Follow us on Twitter.</h2><p>...Then you\'ll get off Facebook and do your homework... Haha. Sure...';
                  picNum = 0;

               }
               /* else if (picNum == 3) {


                   document.getElementById("changer").innerHTML = '<img src="img/pic3.jpg" width="800" />';
                  picNum = 0;

               }  */




      }

function changePass(form) {
     if (form.newPassword.value == form.confirmPassword.value) {
     form.oldPassword.value = hex_md5(form.oldPassword.value);
     form.newPassword.value = hex_md5(form.newPassword.value);
     form.confirmPassword.value = hex_md5(form.confirmPassword.value);
     } else {
         alert("Your confirmation password was not the same as you new password. Please try again.");
         window.location = "admin.php";

     }





}

function toggle (idToToggle, header) {
    if ( document.getElementById(idToToggle).style.display == "block" ) {
       document.getElementById(idToToggle).style.display = "none";
       header.innerHTML = " + " + idToToggle;
    } else {
    document.getElementById(idToToggle).style.display = "block";
    header.innerHTML = " - " + idToToggle;
    }


}

function showIconInfo (idNum) {


var id = "icon" + idNum;


document.getElementById(id).style.display = 'block';

}

function hideIconInfo (idNum) {
var id = "icon" + idNum;

document.getElementById(id).style.display = 'none';

}

function goTo (url) {


window.location = url;
}


function populateAddress() {
   document.getElementById("darkenBackground").style.display = "block";
   document.getElementById("addressBox").style.visibility = "visible";
   document.getElementById("addressBox").style.display = "block";



}


function cancelAddress() {
 document.getElementById("darkenBackground").style.display = "none";
    document.getElementById("addressBox").style.visibility = "hidden";
     document.getElementById("addressBox").style.display = "none";

}

function onloadEvents() {

}

function checkCartAdd(form) {
if (form.size.value == 0) {
alert("Please Select a Size");
return false;
} else {
return true;
}



}

function loadProductXML() {
if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
  xmlhttp=new XMLHttpRequest();
} else {// code for IE6, IE5
  xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
} //else
xmlhttp.open("GET","http://ogkclothing.com/xml/productCatalog.xml",true);
xmlhttp.send();
xmlDoc=xmlhttp.responseXML;


} //load product xml


function searchProductsForID() {

key = document.getElementById("idEnter").value;
if (window.XMLHttpRequest)
  {// code for IE7+, Firefox, Chrome, Opera, Safari
  xmlhttp=new XMLHttpRequest();
  }
else
  {// code for IE6, IE5
  xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
  }

xmlhttp.open("GET","findProduct.php?key="+key+"&info=id",false);
xmlhttp.send();

document.getElementById("sug").innerHTML = xmlhttp.responseText;

}

function addCurrentIdToCart() {

id = document.getElementById("idEnter").value;
//alert(id);
quantity = document.getElementById("quantity").value;
numItems = document.getElementById("numItems").value;
form1 = document.getElementById("txnForm");
table = document.getElementById("itemTable");
//document.write(form1.innerHTML);
form1.innerHTML = form1.innerHTML + "<input type='hidden' name='quantity[" + numItems + "]' value='" + quantity + "' /><input type='hidden' name='id[" + numItems + "]' value='" + id + "' />";
//document.write("JACKPOT" + form1.innerHTML);

key = document.getElementById("idEnter").value;
if (window.XMLHttpRequest)
  {// code for IE7+, Firefox, Chrome, Opera, Safari
  xmlhttp=new XMLHttpRequest();
  }
else
  {// code for IE6, IE5
  xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
  }

xmlhttp.open("GET","findProduct.php?key="+id+"&info=html&q="+quantity,false);
xmlhttp.send();


var info = xmlhttp.responseText.split("&");
var temp;
var values = [];
for (x in info) {
   values[x] = (info[x].split("="))[1];
  // alert(values[x] + "x=" + x);
}

var row = table.insertRow(table.rows.length);

var cell1 = row.insertCell(0);
  var textNode1 = document.createTextNode(values[0]);
  cell1.appendChild(textNode1);
  
  var cell2 = row.insertCell(1);
  var textNode2 = document.createTextNode(values[1]);
  cell2.appendChild(textNode2);

  var cell3 = row.insertCell(2);
  var textNode3 = document.createTextNode(values[2]);
  cell3.appendChild(textNode3);

  var cell4 = row.insertCell(3);
  var textNode4 = document.createTextNode(values[3]);
  cell4.appendChild(textNode4);

  var cell5 = row.insertCell(4);
  var textNode5 = document.createTextNode(values[4]);
  cell5.appendChild(textNode5);


document.getElementById("itemTable").innerHTML =  document.getElementById("itemTable").innerHTML + "<tr><td>" + values[0] +"</td><td>" + values[1] +"</td><td>" + values[2] +"</td><td>" + values[3] +"</td><td>" + values[4] +"</td></tr>";





document.getElementById("quantity").value = 1;
document.getElementById("numItems").value =  parseInt(document.getElementById("numItems").value + 1);
//alert(xmlhttp.responseText);
}

function preview (url) {

window.open("preview.php?url="+url,'mywindow', "width=600,height=600");





}


