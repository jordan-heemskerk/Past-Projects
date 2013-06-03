<?php
session_start();
include "headers/main.txt";
echo "<div class='main'><h1>Archived News</h1>";
$newsXML = simplexml_load_file("xml/news.xml");
$bah = count ($newsXML->news);
  for ($i = $bah; $i >= 0; $i--) {
    $news = $newsXML->news;
    if ($news[$i]->title != "") {
      if (file_exists($news[$i]->imageURL)) {
        echo "<img src='" . $news[$i]->imageURL . "' align='left' width='100' height='100'>";
      }
      echo "<h2>" . $news[$i]->title . "</h2>";
      echo "<h4>" . $news[$i]->date . "</h4>";
       foreach ($news[$i]->content->paragraph as $para) {
       echo "<p class='news'>" . $para . "</p>";
    }
      echo "<p class='newsFooter'>Written By: " . $news[$i]->user . " on " . $news[$i]->date . "</p>";
      echo "<hr align='center' width='800'>";
    }
} //foreach newsXML

echo "</div>"; //main
include "footers/main.txt";

?>