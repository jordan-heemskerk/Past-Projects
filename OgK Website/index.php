<?php
session_start();




include "headers/home.txt";

include "bodies/home.txt";

$newsXML = simplexml_load_file("xml/news.xml");

foreach ($newsXML->news as $news) {
    if ((string)$news['main'] == "true") {
    if (file_exists($news->imageURL)) {
      echo "<img src='" . $news->imageURL . "' align='left' width='100' height='100'>";
    }
    echo "<h2>" . $news->title . "</h2>";
    echo "<h4>" . $news->date . "</h4>";  
    foreach ($news->content->paragraph as $para) {
       echo "<p class='news'>" . $para . "</p>";
    }


    echo "<p class='newsFooter'>Written By: " . $news->user . " on " . $news->date . "</p>";
    }
} //foreach newsXML







echo "</div></div></div></div>";

include "footers/main.txt";







?>
