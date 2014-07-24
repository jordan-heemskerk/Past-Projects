#include <arpa/inet.h>
#include <netinet/in.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <unistd.h>
#include "rdp.h"



#include <netinet/in.h>
#include<stdio.h>
#include<stdlib.h>
#include<sys/socket.h>
#include <sys/select.h>
#include<sys/types.h>
#include<unistd.h>
#include<string.h>
#include<errno.h>
#include<time.h>
#include<fcntl.h>
#include <unistd.h>
#include <termios.h>


#define BUFLEN 512
#define NPACK 10
#define PORT 9930
char *processRequest (RDP_Helper * RDPh, char *buffer, int buf_len,
		      char *root_dir);

int
main (int argc, char *argv[])
{

  if (argc != 4)
    {
      printf ("Usage: ./rwsc server_ip server_port root_directory\n");
      return -1;
    }

  char *server_ip = argv[1];
  char *server_port = argv[2];
  char *root_dir = argv[3];


  time_t start, stop;
  time (&start);

  struct sockaddr_in si_src, si_dest;
  int s, i, slen = sizeof (si_src);
  char buf[BUFLEN];

//create socket
  if ((s = socket (AF_INET, SOCK_DGRAM, IPPROTO_UDP)) == -1)
    perror ("Error creating socket");


//set parameters
//set parameters for socket
  memset ((char *) &si_src, 0, sizeof (si_src));
  si_src.sin_family = AF_INET;	//IPv4
  si_src.sin_addr.s_addr = inet_addr (server_ip);
  si_src.sin_port = htons (atoi (server_port));

  memset ((char *) &si_dest, 0, sizeof (si_dest));
  si_dest.sin_family = AF_INET;	//IPv4
  si_dest.sin_addr.s_addr = inet_addr (server_ip);
  si_dest.sin_port = htons (atoi (server_port));
//bind socket
  if (bind (s, (struct sockaddr *) &si_src, sizeof (si_src)) == -1)
    perror ("Error on bind");

  RDP_Helper RDPh;
  memset ((char *) &RDPh, 0, sizeof (RDPh));
  RDPh.dest_addr = &si_dest;
  RDPh.src_addr = &si_src;
  RDPh.sockfd = s;
  RDPh.addrlen = sizeof (si_src);
  RDPh.seqno = (int *) malloc (sizeof (int));
  *(RDPh.seqno) = 0;
  RDPh.bytes_r = (int *) malloc (sizeof (int));
  RDPh.bytes_ru = (int *) malloc (sizeof (int));
  RDPh.packets_ru = (int *) malloc (sizeof (int));
  RDPh.packets_r = (int *) malloc (sizeof (int));
  RDPh.SYN_r = (int *) malloc (sizeof (int));
  RDPh.FIN_r = (int *) malloc (sizeof (int));
  RDPh.RST_r = (int *) malloc (sizeof (int));
  RDPh.ACK_s = (int *) malloc (sizeof (int));
//RDPh.ACK_s = 0;
  RDPh.RST_s = (int *) malloc (sizeof (int));
  RDPh.seconds = (int *) malloc (sizeof (int));


  char *result = rdp_recv (&RDPh, s, buf, BUFLEN);
  *(RDPh.seqno) = 0;

  rdp_connect (&RDPh, 1);
  char *response = processRequest (&RDPh, result, strlen (result), root_dir);
  //printf("%s\n", response);

  rdp_send (response, &RDPh);
  //free(response);
  rdp_close (&RDPh, 1);
  //printf("%s\n", result);

  printf ("\ntotal data bytes sent: %d\n", *(RDPh.bytes_r));
  printf ("unique data bytes sent: %d\n", *(RDPh.bytes_ru));
  printf ("total packets sent: %d\n", *(RDPh.packets_r));
  printf ("unique packets sent: %d\n", *(RDPh.packets_ru));
  printf ("SYN packets sent: %d\n", *(RDPh.SYN_r));
  printf ("FIN packets sent: %d\n", *(RDPh.FIN_r));
  printf ("RST packets sent: %d\n", *(RDPh.RST_r));
  printf ("ACK packets recieved: %d\n", *(RDPh.ACK_s));
  printf ("RST packets revieved: %d\n", *(RDPh.RST_s));

  time (&stop);

  printf ("total time duration (seconds): %0.2f\n\n", difftime (stop, start));


  close (s);
  return 0;

}

//get a 3 char month name from integer
void
getMonth (char *mon, int src)
{

  switch (src)
    {
    case 0:
      strcpy (mon, "Jan");
      break;
    case 1:
      strcpy (mon, "Feb");
      break;
    case 2:
      strcpy (mon, "Mar");
      break;
    case 3:
      strcpy (mon, "Apr");
      break;
    case 4:
      strcpy (mon, "May");
      break;
    case 5:
      strcpy (mon, "Jun");
      break;
    case 6:
      strcpy (mon, "Jul");
      break;
    case 7:
      strcpy (mon, "Aug");
      break;
    case 8:
      strcpy (mon, "Sep");
      break;
    case 9:
      strcpy (mon, "Oct");
      break;
    case 10:
      strcpy (mon, "Nov");
      break;
    case 11:
      strcpy (mon, "Dec");
      break;
    }				//switch
}				//getMonth

//get a 3 char day of the week name from an integer
void
getDay (char *day, int src)
{
  switch (src)
    {
    case 0:
      strcpy (day, "Sun");
      break;
    case 1:
      strcpy (day, "Mon");
      break;
    case 2:
      strcpy (day, "Tue");
      break;
    case 3:
      strcpy (day, "Wed");
      break;
    case 4:
      strcpy (day, "Thu");
      break;
    case 5:
      strcpy (day, "Fri");
      break;
    case 6:
      strcpy (day, "Sat");
      break;
    }				//switch
}				//getDay

char *
processRequest (RDP_Helper * RDPh, char *buffer, int buf_len, char *root_dir)
{

  int tosend_size = 1024;
  char *tosend = malloc (sizeof (char) * 1024);
  int tosend_pos = 0;
  char *client_ip = "192.168.0.100";
  char *port = "9930";
  int num = 0;
  int socket_fd = 0;
  //load the local time   
  time_t now;
  struct tm *ts;
  time (&now);			//load the current time
  ts = localtime (&now);	//this way can access different objects of current time

  //get day and month names
  char mon[4];
  getMonth (mon, ts->tm_mon);
  char day[4];
  getDay (day, ts->tm_wday);


  int bad = 0;			//error flag

  //extract first line
  char *start = buffer;
  char *nl = strchr (buffer, '\n');
  int fl_len = nl - buffer;
  char *first_line = (char *) malloc ((fl_len + 1) * sizeof (char));
  int j = 0;
  for (j = 0; j < fl_len; j++)
    {
      first_line[j] = buffer[j];
    }
  first_line[fl_len] = '\0';



  //check that it is a proper GET request
  if (strncasecmp (buffer, "GET ", 4) != 0)
    {
      bad = 1;
    }


  //extract the path of file
  while (*buffer != ' ' && *buffer != '\n')
    {
      buffer++;			//skip to start of path
    }

  if (*buffer == '\n')
    bad = 8;
  buffer++;
  char *end = strchr (buffer, ' ');	//find end of path
  int path_len = end - buffer;	//get length
  char *path = (char *) malloc ((path_len + 1) * sizeof (char));	//allocate resources
  strncpy (path, buffer, path_len);	//path now contains path
  path[path_len] = '\0';


  if (path_len <= 0)
    bad = 9;

  //extract the HTTP version
  buffer = end;
  buffer++;
  end = strchr (buffer, '\n');
  int http_len = end - buffer;
  char *http = (char *) malloc ((http_len + 1) * sizeof (char));
  strncpy (http, buffer, http_len);


  if (http_len <= 0)
    bad = 10;


  //check that we are using version 1.0, otherwise throw an error
  if (strncasecmp (http, "HTTP/1.0", 8) != 0)
    {
      bad = 2;
    }

  //strip out ..'s
  if (strstr (path, "..") != NULL)
    {
      //printf(".. present in path");
      bad = 4;
    }

  //default is index.html if / is provided
  if (path[0] == '/' && path_len == 1)
    {
      path = (char *) malloc (12 * sizeof (char));
      strncpy (path, "/index.html", 11);
      path_len = 11;
      path[11] = '\0';
    }



  //concat requested resource to root directory
  char *full_path =
    malloc ((strlen (root_dir) + strlen (path) + 1) * sizeof (char));
  char *temp_path = full_path + strlen (root_dir);
  strncpy (full_path, root_dir, strlen (root_dir));
  strncpy (temp_path, path, strlen (path));
  full_path[strlen (root_dir) + strlen (path)] = '\0';

  //a directory is requested - throw an error
  if (full_path[strlen (root_dir) + strlen (path) - 1] == '/')
    {
      bad = 3;
    }



  char *response;
  int i;
  FILE *fp = fopen (full_path, "r");	//open file for reading
  if (bad > 0)
    {
     printf("BAD!\n");
      //all above arrows are covered by the invalid request
      response = "HTTP/1.0 400 Invalid Request\0";
     // write (socket_fd, "HTTP/1.0 400 Invalid Request\n\n", 29);


    }
  else if (fp <= 0)
    {
    
      if (errno == ENOENT)
	{
	  //File not found
	  response = "HTTP/1.0 404 Not Found\0";
	//  write (socket_fd, "HTTP/1.0 404 Not Found\n\n", 23);
	  bad = 6;
	}
      if (errno == EACCES)
	{
	  printf("YO\n");
	  //permissions issues
	  response = "HTTP/1.0 403 Forbidden\0";
	//  write (socket_fd, "HTTP/1.0 403 Forbidden\n\n", 23);
	  bad = 7;
	}
    }
  else
    {

      //the request is valid if we get to here
      //get the date
      char date[36];
      sprintf (date, "Date: %s, %.2d %s %.2d %.2d:%.2d:%.2d %s\n", day,
	       ts->tm_mday, mon, 1900 + ts->tm_year, ts->tm_hour, ts->tm_min,
	       ts->tm_sec, ts->tm_zone);


      response = "HTTP/1.0 200 OK\0";

      //output the headers
      //write(socket_fd, "HTTP/1.0 200 OK\n",16);
      //write(socket_fd, date, 36);
      //write(socket_fd, "Content-Type: text/html\n\n",25);

      add_packet_text (tosend, "HTTP/1.0 200 OK\n", &tosend_pos, 16);
      add_packet_text (tosend, date, &tosend_pos, 36);
      add_packet_text (tosend, "Content-Type: text/html\n\n", &tosend_pos,
		       25);


      //printf("tosend: %s\n", tosend);

      char cont[1];
      char c = (char) fgetc (fp);




      //write the file contents
      while (1)
	{
	  if (tosend_pos == tosend_size)
	    {
	      tosend_size *= 2;
	      tosend = realloc (tosend, sizeof (int) * tosend_size);
	    }
	  //printf("%c", (char)c);
	  if (c == EOF)
	    break;
	  add_packet_char (tosend, c, &tosend_pos);
	  c = (char) fgetc (fp);

	}
      //printf("\n");
      terminate_str (tosend, tosend_pos);



      //rdp_connect(RDPh);


      response = tosend;
      //  rdp_send(tosend, RDPh);
      // rdp_close(RDPh);
      //printf("tosend: %s\n", tosend);

    }

  //log to the console
  //printf("%d ", num);  
  //printf("%d ", 1900+ts->tm_year);
  //printf("%s ", mon); 
  //printf("%.2d %.2d:%.2d:%.2d ", ts->tm_mday, ts->tm_hour, ts->tm_min, ts->tm_sec);
  //printf("%s:%d ",  client_ip, port);
  //printf("GET %s HTTP/1.0; ", path);  //definately had some issues here, couldn't get the entire first line of the inital request to be printed
  //printf("%s; ", response); 

  //show the requested resource if it was sent, otherwise don't
  if (!bad)
    {
      //printf("%s\n",tosend);
    }
  else
    {
      //printf ("error: %d\n", bad);
    }
	
  return response;
}				//processRequest
