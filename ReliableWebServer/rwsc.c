#include <arpa/inet.h>
#include <netinet/in.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <unistd.h>
#include <string.h>
#include <stdlib.h>
#include <libgen.h>
#include <time.h>

#include <fcntl.h>
#include "rdp.h"

#define BUFLEN 512
#define NPACK 10
#define PORT 9930
#define SRV_IP "10.10.1.100"



int
main (int argc, char *argv[])
{

  if (argc != 6)
    {
      printf
	("Usage: ./rwsc client_ip client_port server_ip server_port URL\n");
      return -1;
    }
  time_t start, stop;
  time (&start);
  char *client_ip = argv[1];
  char *client_port = argv[2];
  char *server_ip = argv[3];
  char *server_port = argv[4];
  char *URL = argv[5];

  struct sockaddr_in si_dest, si_src;
  int s, i, slen = sizeof (si_dest);
  char buf[BUFLEN];

  //create a socket
  if ((s = socket (AF_INET, SOCK_DGRAM, IPPROTO_UDP)) == -1)
    perror ("Error creating socket");


//set parameters for socket
  memset (&si_src, 0, sizeof (si_src));
  si_src.sin_family = AF_INET;	//IPv4
  si_src.sin_addr.s_addr = inet_addr (client_ip);
  si_src.sin_port = htons (atoi (client_port));

  memset ((char *) &si_dest, 0, sizeof (si_dest));
  si_dest.sin_family = AF_INET;	//IPv4
  si_dest.sin_addr.s_addr = inet_addr (server_ip);
  si_dest.sin_port = htons (atoi (server_port));

//bind to the socket

  if (bind (s, (struct sockaddr *) &si_src, sizeof (si_src)) != 0)
    {
      perror ("error on bind");
    }



  if (inet_aton (server_ip, &si_dest.sin_addr) == 0)
    {
      perror ("inet_aton() failed\n");
      exit (1);
    }
  si_dest.sin_port = htons (atoi (server_port));


  char *body;
  char *packet;
  RDP_Helper RDPh;
  memset ((char *) &RDPh, 0, sizeof (RDPh));
  RDPh.dest_addr = &si_dest;
  RDPh.src_addr = &si_src;
  RDPh.sockfd = s;
  RDPh.addrlen = sizeof (si_dest);
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
  RDPh.RST_s = (int *) malloc (sizeof (int));
  RDPh.seconds = (int *) malloc (sizeof (int));




  char *request = malloc (sizeof (char) * (strlen (URL) + 15));
  sprintf (request, "GET %s HTTP/1.0\n\n", URL);

  rdp_connect (&RDPh, 0);
  rdp_send (request, &RDPh);
  rdp_close (&RDPh, 0);

  *(RDPh.seqno) = 0;
  char *HTTP_response = rdp_recv (&RDPh, s, buf, BUFLEN);
  printf ("\ntotal data bytes recieved: %d\n", *(RDPh.bytes_r));
  printf ("unique data bytes recieved: %d\n", *(RDPh.bytes_ru));
  printf ("total packets recieved: %d\n", *(RDPh.packets_r));
  printf ("unique packets recieved: %d\n", *(RDPh.packets_ru));
  printf ("SYN packets recieved: %d\n", *(RDPh.SYN_r));
  printf ("FIN packets recieved: %d\n", *(RDPh.FIN_r));
  printf ("RST packets recieved: %d\n", *(RDPh.RST_r));
  printf ("ACK packets sent: %d\n", *(RDPh.ACK_s));
  printf ("RST packets sent: %d\n", *(RDPh.RST_s));

  time (&stop);

  printf ("total time duration (seconds): %0.2f\n\n", difftime (stop, start));
  
  char* fl = HTTP_response;
  char* first_line_http = fl;
  char * result = strstr(HTTP_response, "\n");
 char * breaker = strstr(HTTP_response, "\n\n");


   if (result != NULL) fl = malloc(sizeof(char)* (result-HTTP_response));
   else {printf("%s\n", HTTP_response); return;}
//char* first_line_http = fl;

  /*
  while (HTTP_response != breaker) {
 	if (result != NULL && HTTP_response < result) {
		*fl = *HTTP_response;
		fl++;
	}
        printf("%c", *HTTP_response);
	HTTP_response++;
  }*/

*fl = '\0';

  if (strstr(first_line_http, "200") != NULL) {
printf("\nFile has been retrieved successfully and is saved in current directory.\n");
  FILE *file;
  char *filename = basename (URL);
  if (strncmp (filename, "/", 1) == 0)
    filename = "index.html";

  file = fopen (filename, "w");	/* apend file (add text to
				   a file or create a file if it does not exist. */

  breaker += 2;

  while (*breaker != '\0')
    {
      //printf("%c", *breaker);
      fprintf (file, "%c", *breaker);	/*writes */

      breaker++;
    }
  char nl = '\n';
//fprintf(file,"\n"); /*wr
  printf ("\n");
  fclose (file);		/*done! */

} //if 200 OK



/*
//send data
  for (i=0; i<NPACK; i++) {
    printf("Sending packet %d\n", i);
    sprintf(buf, "This is packet %d\n", i);
    if (sendto(s, buf, BUFLEN, 0, &si_dest, slen)==-1)
      perror("Error on sendto()");
  }*/

  close (s);
  return 0;
}
