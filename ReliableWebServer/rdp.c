#include <stdio.h>
#include<string.h>
#include "rdp.h"
#include <time.h>
#include <stdlib.h>
#include <sys/time.h>
#include <netinet/in.h>
#include<sys/socket.h>
#include <sys/select.h>
#include<sys/types.h>
#include<unistd.h>
#include<errno.h>
#include<fcntl.h>
#include <unistd.h>
#include <termios.h>


#define HEADER_LINE_BUF 100 //max chars of an RDP Header

#define HEADER_SIZE (73+(sizeof(int)*4)) //maximum length of all RDP headers


RDP_Helper * newRDPh() {
  RDP_Helper * RDPh = (RDP_Helper*) malloc(sizeof(RDPh));
  memset ((char *) RDPh, 0, sizeof (RDPh));
//  RDPh.dest_addr = &si_dest;
//  RDPh.src_addr = &si_src;
//  RDPh.sockfd = s;
 // RDPh.addrlen = sizeof (si_dest);
  RDPh->seqno = (int *) malloc (sizeof (int));
  *(RDPh->seqno) = 0;
  RDPh->bytes_r = (int *) malloc (sizeof (int));
  RDPh->bytes_ru = (int *) malloc (sizeof (int));
  RDPh->packets_ru = (int *) malloc (sizeof (int));
  RDPh->packets_r = (int *) malloc (sizeof (int));
  RDPh->SYN_r = (int *) malloc (sizeof (int));
  RDPh->FIN_r = (int *) malloc (sizeof (int));
  RDPh->RST_r = (int *) malloc (sizeof (int));
  RDPh->ACK_s = (int *) malloc (sizeof (int));
  RDPh->RST_s = (int *) malloc (sizeof (int));
  RDPh->seconds = (int *) malloc (sizeof (int));
return RDPh;


}


/*******void add_packet_char(char *packet_data, char text, int *current_position)***********
*Adds a character to a string
*************************************/
void add_packet_char (char *packet_data, char text, int *current_position) {
  int i = 0;
  int text_length = 1;
  char *packet_pos = packet_data + *current_position;
  for (i = 0; i < text_length; i++)
    {
      *packet_pos = text;
      packet_pos++;
    }
  *current_position += text_length;
} //add_packet_char


/*******char* getTimeStr()***********
*
*returns a formatted string of the current time
*************************************/
char * getTimeStr () {
  char *time_str = (char *) malloc (200 * sizeof (char));
  //load the local time   
  struct timeval tv;
  struct timezone tz;
  struct tm *tm;
  gettimeofday (&tv, &tz);
  tm = localtime (&tv.tv_sec);
  sprintf (time_str, "%d:%02d:%02d.%d\0", tm->tm_hour, tm->tm_min, tm->tm_sec,
	   (int) tv.tv_usec);

  return time_str;
} //getTimeStr()

/*******void terminate_str(char *packet_data, int current_position)***********
*
*null terminates a string at the given position
********************************************************/
void terminate_str (char *packet_data, int current_position) {
  packet_data += (current_position);
  *packet_data = '\0';
} //terminate_str


/*****void terminate_str(char *packet_data, int current_position)***********
*Adds a string to packet
*****************************************************/
void add_packet_text (char *packet_data, char *text, int *current_position, int text_length) {
  int i = 0;
  char *packet_pos = packet_data += *current_position;
  for (i = 0; i < text_length; i++)
    {
      *packet_pos = text[i];
      packet_pos++;
    }
  *current_position += text_length;
} //add_packet_text()

int type_atoi (char *type) {
  if (strncmp (type, "ACK", 3) == 0)
    return ACK;
  if (strncmp (type, "DAT", 3) == 0)
    return DAT;
  if (strncmp (type, "SYN", 3) == 0)
    return SYN;
  if (strncmp (type, "FIN", 3) == 0)
    return FIN;
  if (strncmp (type, "RST", 3) == 0)
    return RST;
  return 0;
} //type_atoi

char * build_rdp_packet_header (int type, int seqno, int len, int winsize) {
  char *packet_data = (char *) malloc (HEADER_SIZE);
  if (packet_data == 0)
    {
      perror ("Malloc error in build_rdp_packet_header");
      return 0;
    }

  int packet_length = 0;

  add_packet_text (packet_data, "Magic: CSC361L\n", &packet_length, 15);

  switch (type)
    {
    case ACK:
      add_packet_text (packet_data, "Type: ACK\n", &packet_length, 10);
      break;
    case DAT:
      add_packet_text (packet_data, "Type: DAT\n", &packet_length, 10);
      break;
    case SYN:
      add_packet_text (packet_data, "Type: SYN\n", &packet_length, 10);
      break;
    case FIN:
      add_packet_text (packet_data, "Type: FIN\n", &packet_length, 10);
      break;
    case RST:
      add_packet_text (packet_data, "Type: RST\n", &packet_length, 10);
      break;
    }

  char text[HEADER_LINE_BUF];
  if (type != ACK) {
      memset (text, 0, HEADER_LINE_BUF);
      sprintf (text, "Sequence: %d\n", seqno);
      //printf("text: %s\n", text);
      add_packet_text (packet_data, text, &packet_length, strlen (text));
  }
  if (type == ACK) {
      memset (text, 0, HEADER_LINE_BUF);

      sprintf (text, "Acknowledgment: %d\n", seqno);
      add_packet_text (packet_data, text, &packet_length, strlen (text));
  }
  memset (text, 0, HEADER_LINE_BUF);
  sprintf (text, "Payload: %d\n", len);
  add_packet_text (packet_data, text, &packet_length, strlen (text));



  memset (text, 0, HEADER_LINE_BUF);
  sprintf (text, "Window: %d\n\n", winsize);	//EXTRA LINE PUT IN TO SIGNAL END OF HEADER
  add_packet_text (packet_data, text, &packet_length, strlen (text));


  terminate_str (packet_data, packet_length);	//null terminate string

  return packet_data;
}

char * build_rdp_packet_body (char *packet_head, char *body) {
  char *packet_data = (char *) malloc (sizeof (char) * PACKET_SIZE);

  int head_len = (int) strlen (packet_head);
  int body_len = (int) strlen (body);
  int i = 0;
  for (i = 0; i < head_len; i++) {
      packet_data[i] = packet_head[i];
  }

  int j = 0;
  for (j = 0; j < body_len; j++) {
      packet_data[i] = body[j];
      i++;
  }

  terminate_str (packet_data, i);
  return packet_data;

}

int print_log_message (char event, RDP_Helper * RDPh, int type, int seqack, int winlen) {

  char *type_str = malloc (10 * sizeof (char));
  switch (type)
    {
    case ACK:
      type_str = "ACK";
      break;
    case DAT:
      type_str = "DAT";
      break;
    case SYN:
      type_str = "SYN";
      break;
    case FIN:
      type_str = "FIN";
      break;
    case RST:
      type_str = "RST";
      break;
    }



  // determine destination ip
  int dest_port, src_port = 0;
  char dest_ip[INET_ADDRSTRLEN], src_ip[INET_ADDRSTRLEN];

  dest_port = ntohs ((RDPh->dest_addr)->sin_port);
  inet_ntop (AF_INET, &((RDPh->dest_addr)->sin_addr), dest_ip, sizeof (dest_ip));

  src_port = ntohs ((RDPh->src_addr)->sin_port);
  inet_ntop (AF_INET, &((RDPh->src_addr)->sin_addr), src_ip, sizeof (src_ip));

  char *time_str = getTimeStr ();

  printf ("%s %c %s:%d %s:%d %s %d %d\n", time_str, event, src_ip, src_port, dest_ip, dest_port, type_str, seqack, winlen);

}

int waitForACK (RDP_Helper * RDPh) {
  struct timeval tv;

  tv.tv_sec = 0;
  tv.tv_usec = 100000;

  setsockopt (RDPh->sockfd, SOL_SOCKET, SO_RCVTIMEO, (char *) &tv,
	      sizeof (struct timeval));
  char incoming_packet_data[PACKET_SIZE];
  if (recvfrom
      (RDPh->sockfd, incoming_packet_data, PACKET_SIZE, 0,
       (struct sockaddr *) (RDPh->dest_addr),
       (socklen_t *) & (RDPh->addrlen)) == -1)
    {
      return 1;
      //perror("Error at recvfrom()");
    }

 
  char *ack_c = extract_header_value ("Acknowledgment", incoming_packet_data);
  if (ack_c == 0) {
    return 1;
  }
  if (atoi (ack_c) == *(RDPh->seqno) + 1) {
      *(RDPh->ACK_s) += 1;

      print_log_message ('r', RDPh, ACK, atoi (ack_c), 0);
      free (ack_c);
      return 0;

  }

  return 1;

}


int rdp_connect (RDP_Helper * RDPh, int flag) {

  int ack = 1;

  *(RDPh->seqno) = 0;

  //build SYN and send
  char *packet = build_rdp_packet_header (SYN, *(RDPh->seqno), 0, 0);
  char *data = "\n";
  packet = build_rdp_packet_body (packet, data);
  print_log_message ('s', RDPh, SYN, *(RDPh->seqno), 0);
  while (ack) {
      *(RDPh->SYN_r) += 1;
      if (sendto(RDPh->sockfd, packet, strlen (packet), 0, (struct sockaddr *) (RDPh->dest_addr), RDPh->addrlen) == -1) {
	perror ("Error on sendto()");
       }
      ack = waitForACK (RDPh);
  } //while


  return 0;

}

int rdp_send (char *data, RDP_Helper * RDPh) {
  //build request and send
  int ack = 1;
  *(RDPh->seqno) = 1;
  int data_per_packet = PACKET_SIZE - HEADER_SIZE;
  char *packet;
  int num_packets = (strlen (data) / (data_per_packet - 1)) + 1;
  char *data_for_this_packet =
    (char *) malloc (sizeof (char) * data_per_packet);


  while (num_packets > 0)  {

      strncpy (data_for_this_packet, data, data_per_packet);
      data_for_this_packet[data_per_packet - 1] = '\0';
      int this_seqno = *(RDPh->seqno);
      packet = build_rdp_packet_header (DAT, *(RDPh->seqno), (int) strlen (data_for_this_packet), 0);
      packet = build_rdp_packet_body (packet, data_for_this_packet);
      *(RDPh->seqno) += strlen (data_for_this_packet);
      ack = 1;
      int first = 1;
      char log = 'S';

      while (ack) {

	  *(RDPh->bytes_r) += strlen (data_for_this_packet);
	  *(RDPh->packets_r) += 1;
	  log = 'S';
	  if (first) {
	      *(RDPh->bytes_ru) += strlen (data_for_this_packet);
	      *(RDPh->packets_ru) += 1;
	      log = 's';
	      first = 0;
	  }
	  if (sendto(RDPh->sockfd, packet, strlen (packet), 0, (struct sockaddr *) (RDPh->dest_addr), RDPh->addrlen) == -1) {
	    perror ("Error on sendto()");
	  }

	  print_log_message (log, RDPh, DAT, this_seqno, strlen (data_for_this_packet));
	  ack = waitForACK (RDPh);
     } //waiting for ack			
     
     //update values
     memset (data_for_this_packet, 0, data_per_packet);
     data += (data_per_packet - 1);
     num_packets--;
     *(RDPh->seqno) += 1;

    }//looping over all packets
  return 0; //finished sending all packets
} //rdp send


int rdp_send_ack (RDP_Helper * RDPh) {
  char *packet = build_rdp_packet_header (ACK, *(RDPh->seqno), 0, 0);
  char *data = "";
  packet = build_rdp_packet_body (packet, data);
  print_log_message ('s', RDPh, ACK, *(RDPh->seqno), 0);
  if (sendto(RDPh->sockfd, packet, strlen (packet) + 1, 0, (struct sockaddr *) RDPh->dest_addr, RDPh->addrlen) == -1) {
    perror ("Error on sendto()");
  }
  *(RDPh->ACK_s) += 1;
}//rdp_send_ack


int rdp_close (RDP_Helper * RDPh, int flag) {
  int ack = 1;
  char *packet = build_rdp_packet_header (FIN, *(RDPh->seqno), 0, 0);
  print_log_message ('s', RDPh, FIN, *(RDPh->seqno), 0);
  char *body = "\n";
  packet = build_rdp_packet_body (packet, body);
  ack = 1;

  while (ack) {
      *(RDPh->FIN_r) += 1;
      if (flag == 0) {
	  if (sendto(RDPh->sockfd, packet, strlen (packet), 0, (struct sockaddr *) RDPh->dest_addr, RDPh->addrlen) == -1) {
	    perror ("Error on sendto()");
	  }

      } else {
	  if (sendto(RDPh->sockfd, packet, strlen (packet), 0, (struct sockaddr *) (RDPh->dest_addr), RDPh->addrlen) == -1) {
	    perror ("Error on sendto()");
          }

      } //else
      ack = waitForACK (RDPh); //recv ack
  } //waiting for ack on FUN

}//rdp_close


char * extract_header_value (char *header_field, char *packet)
{
  int packet_length = strlen (packet);
  char *packet_copy = (char *) malloc (sizeof (char) * strlen (packet) + 1);
  memset (packet_copy, 0, strlen (packet) + 1);
  strncpy (packet_copy, packet, strlen (packet));
  char *packet_line = strtok (packet_copy, "\n");
  char *toReturn;
  while (packet_line != NULL) {
      int i = 0;
      for (i = 0; i < strlen (packet_line); i++) {
	  if (packet_line[i] == ':') {
	    break;
	  }
      } //for
      char *header = (char *) malloc (sizeof (char) * (i + 2));
      memset (header, 0, (i + 2));
      int j = 0;
      for (j = 0; j < i; j++) {
	  header[j] = packet_line[j];
      }//for
      header[j + 1] = '\0';
      if (strncmp (header, header_field, strlen (header_field)) == 0) {
	  int value_length = strlen (packet_line) - i;
	  toReturn = (char *) malloc (sizeof (char) * (value_length + 2));
	  memset (toReturn, 0, (value_length + 2) * sizeof (char));
	  int j = 0;
	  for (i = i + 2; i < strlen (packet_line); i++) {
	      // printf("j = %d\n", j);
	      // printf("%c", packet_line[i]);
	      //printf("\n");
	      toReturn[j] = packet_line[i];
	      j++;
	  }
	  toReturn[j + 1] = '\0';
       }
      packet_line = strtok (NULL, "\n");
  } //looping over all headers				
  return toReturn;
} //extract_header

char * extract_payload (char *packet) {
  char *start = strstr (packet, "\n\n");
  char *payload_c = extract_header_value ("Payload", packet);
  int length = atoi (payload_c);
  free (payload_c);
  char *toReturn = (char *) malloc (sizeof (char) * (length + 2));
  int i = 0;
  start += 2;
  //length=2;
  for (i = 0; i < length; i++) {
      toReturn[i] = start[i];
      if (toReturn[i] == '\0') {
	return toReturn;
      }
  }
  toReturn[i + 1] = '\0';
  return toReturn;
}

char * rdp_recv (RDP_Helper * RDPh_in, int s, char *buf, int BUFLEN) {

  int final_size = 1024;
  int final_pos = 0;
  char *final_msg = (char *) malloc (sizeof (char) * final_size);
  char *pay = "";

  struct timeval tv;

  tv.tv_sec = 0;
  tv.tv_usec = 0;

  setsockopt (RDPh_in->sockfd, SOL_SOCKET, SO_RCVTIMEO, (char *) &tv,
	      sizeof (struct timeval));

  for (;;) {
      if (recvfrom(RDPh_in->sockfd, buf, BUFLEN, 0, (struct sockaddr *) (RDPh_in->dest_addr), &(RDPh_in->addrlen)) == -1) {
	  perror ("Error at recvfrom() 409");
      }
      *(RDPh_in->packets_r) += 1;


      int duplicate = 0;
      int payload = 0;
      char *payload_c = extract_header_value ("Payload", buf);
      int recv_seqno = 0;
      char *recv_seqno_c = extract_header_value ("Sequence", buf);
      payload = atoi (payload_c);
      //free (payload_c);
      recv_seqno = atoi (recv_seqno_c);
      //free (recv_seqno_c);
      RDP_Helper RDPh;
      memset ((char *) &RDPh, 0, sizeof (RDPh));
      RDPh.dest_addr = RDPh_in->dest_addr;
      RDPh.src_addr = RDPh_in->src_addr;
      RDPh.sockfd = RDPh_in->sockfd;
      RDPh.addrlen = RDPh_in->addrlen;
      RDPh.ACK_s = RDPh_in->ACK_s;
      RDPh.seqno = (int *) malloc (sizeof (int));
      RDPh.seqno = RDPh_in->seqno;
      //check for duplicate
      if (*(RDPh.seqno) != recv_seqno) {
	  duplicate = 1;
      } else {
	  *(RDPh.seqno) += payload + 1;
      } //else 
      char *type = extract_header_value ("Type", buf);
      pay = extract_payload (buf);
      if (duplicate) {
	  print_log_message ('R', RDPh_in, type_atoi (type), *(RDPh.seqno), payload);
      } else {
	  print_log_message ('r', RDPh_in, type_atoi (type), *(RDPh.seqno), payload);
      }
      //ensure recieved everything the packet says is attached
      if (strlen (pay) == payload) {
	  rdp_send_ack (&RDPh);
      }
      *(RDPh_in->bytes_r) += strlen (pay);
      if (strcmp (type, "DAT") == 0 && !duplicate) {
	  *(RDPh_in->packets_ru) += 1;
	  *(RDPh_in->bytes_ru) += strlen (pay);
	  if (final_pos + strlen (pay) >= final_size) {
	      final_msg = realloc (final_msg, (2 * final_size) * sizeof (char));
	      final_size = 2 * final_size;
	  }
	  strncpy (final_msg + final_pos, pay, strlen (pay));
	  final_pos += strlen (pay);
	  memset (buf, 0, BUFLEN);
      }

      if (strcmp (type, "FIN") == 0) {
	//Last packet to recieve
	break;
      }
      memset (buf, 0, BUFLEN);
  } //recieveing packets				
  free (pay);
  return final_msg;
}//rdprecv
