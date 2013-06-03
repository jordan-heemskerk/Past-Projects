#!/usr/local/bin/perl
# wdwrite
# this is the CGI that will take the # applet info and write it to a file

open(OUT, ">> highScore.txt");
print "content-type: text/plain\n\n";

while (<>) {
    print OUT $_;
    print $_;
}
close (OUT);
exit 0;
