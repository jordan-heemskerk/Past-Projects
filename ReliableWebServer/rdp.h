#ifndef RDP
#define RDP



#define DAT 0
#define ACK 1
#define SYN 2
#define FIN 3
#define RST 4
#define PACKET_SIZE 256
typedef struct {
  int sockfd;
  const struct sockaddr_in *dest_addr;
  const struct sockaddr_in *src_addr;
  int addrlen;
  int* seqno;


  int* bytes_r;
  int* bytes_ru;
  int* packets_r;
  int* packets_ru;
  int* SYN_r;
  int* FIN_r;
  int* RST_r;
  int* ACK_s;
  int* RST_s;
  int* seconds;
	


} RDP_Helper;

void terminate_str(char* packet_data, int current_position);
char* build_rdp_packet_header(int type, int seqno, int len, int winsize);
void add_packet_text(char* packet_data, char* text, int* current_position, int text_length);
char* build_rdp_packet_body(char* packet_head, char* body);
int waitForACK(RDP_Helper* RDPh);
char* extract_header_value(char* header_field, char* packet);
char* rdp_recv(RDP_Helper* RDPh_in, int s, char* buf, int BUFLEN);
RDP_Helper* newRDPh();
#endif


