/**************************************************************
File: sws.c
Author: Jordan Heemskerk
Student Number: V00158837
Date: Sunday, June 16, 2013
Purpose: A simple web server. Only servers HTML content.
***************************************************************/

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

#define MAXBUFFERLEN 256


//get a 3 char month name from integer
void getMonth(char * mon, int src) {
	
	switch(src){
		case 0: strcpy(mon, "Jan"); break;
		case 1: strcpy(mon, "Feb"); break;
		case 2: strcpy(mon, "Mar"); break;
		case 3: strcpy(mon, "Apr"); break;
		case 4: strcpy(mon, "May"); break;
		case 5: strcpy(mon, "Jun"); break;
		case 6: strcpy(mon, "Jul"); break;
		case 7: strcpy(mon, "Aug"); break;
		case 8: strcpy(mon, "Sep"); break;
		case 9: strcpy(mon, "Oct"); break;
		case 10: strcpy(mon, "Nov"); break;
		case 11: strcpy(mon, "Dec"); break;
	} //switch
}//getMonth

//get a 3 char day of the week name from an integer
void getDay(char * day, int src) {
	switch(src){
		case 0: strcpy(day, "Sun"); break;
		case 1: strcpy(day, "Mon"); break;
		case 2: strcpy(day, "Tue"); break;
		case 3: strcpy(day, "Wed"); break;
		case 4: strcpy(day, "Thu"); break;
		case 5: strcpy(day, "Fri"); break;
		case 6: strcpy(day, "Sat"); break;
	} //switch
} //getDay


/************************************************************************************************************
int processRequest(char * buffer, int buf_len, int socket_fd, char * root_dir, char * client_ip, int port)
	
	buffer - contains an HTTP Request String
	buf_len - length of the forementioned string
	socket_fd - file descriptor for a TCP socket
	root_dir - root directory to serve files from
	num - the sequence number for this request
	client_ip - a string containing human-readable ip address of client
	port - integer containing the client's port number

	
This function processes an HTTP request. It logs to stdout when it does so. 
It sends out the response to the client with the appropriate content. 

*************************************************************************************************************/
int processRequest(char * buffer, int buf_len, int socket_fd, char * root_dir, int num, char * client_ip, int port) {
	
	//load the local time	
	time_t now;
	struct tm * ts;
	time(&now); //load the current time
	ts = localtime(&now); //this way can access different objects of current time
	
	//get day and month names
	char mon[4];
	getMonth(mon, ts->tm_mon);
	char day[4];
	getDay (day, ts->tm_wday);
	
	
	int bad = 0; //error flag
	
	//extract first line
	char * start = buffer;
	char * nl = strchr(buffer, '\n');
	int fl_len = nl - buffer;
	char * first_line = (char *) malloc((fl_len+1)*sizeof(char));
	int j = 0;
	for (j =0 ;j < fl_len; j++) {
		first_line[j] = buffer[j];
	}
	first_line[fl_len] = '\0';
	
	
	
	//check that it is a proper GET request
	if (strncasecmp(buffer, "GET ", 4) != 0){
		bad = 1;
	}
	
	
	//extract the path of file
	while (*buffer != ' '  && *buffer != '\n') {
		buffer++; //skip to start of path
	}

	if (*buffer == '\n') bad = 8;
	buffer++;
	char * end = strchr(buffer, ' '); //find end of path
	int path_len = end - buffer; //get length
	char * path = (char *) malloc((path_len+1)*sizeof(char)); //allocate resources
	strncpy(path, buffer, path_len); //path now contains path
	path[path_len] = '\0';
	
	if (path_len <= 0) bad = 9;
	
	//extract the HTTP version
	buffer = end;
	buffer++;
	end = strchr(buffer, '\n');
	int http_len = end -buffer;
	char * http = (char *) malloc((http_len+1)*sizeof(char));
	strncpy(http, buffer, http_len);
	

	if (http_len <= 0) bad = 10;


	//check that we are using version 1.0, otherwise throw an error
	if (strncasecmp(http, "HTTP/1.0", 8) != 0) {
		bad = 2;
	}
	
	//strip out ..'s
	if (strstr(path, "..") != NULL) {
		//printf(".. present in path");
		bad = 4;
	}
	
	//default is index.html if / is provided
	if(path[0] == '/' && path_len == 1) {
		path = (char *) malloc(12*sizeof(char));
		strncpy(path, "/index.html", 11);
		path_len = 11;
		path[11] = '\0';
	}
	
	
	
	//concat requested resource to root directory
	char * full_path = malloc((strlen(root_dir) + strlen(path) + 1)*sizeof(char));
	char * temp_path = full_path + strlen(root_dir);
	strncpy(full_path, root_dir, strlen(root_dir));
	strncpy(temp_path, path, strlen(path));
	full_path[strlen(root_dir) + strlen(path)] = '\0';
	
	//a directory is requested - throw an error
	if (full_path[strlen(root_dir) + strlen(path)-1] == '/') {
		bad = 3;
	}
	

	
	char * response;
	int fp, i;
	fp = open(full_path, O_RDONLY); //open file for reading
	if (bad > 0) {
		//all above arrows are covered by the invalid request
		response = "HTTP/1.0 400 Invalid Request\0";
		write(socket_fd, "HTTP/1.0 400 Invalid Request\n\n",29);
	
	
	} else if (fp < 0) {
		if (errno == ENOENT) {
			//File not found
			response = "HTTP/1.0 404 Not Found\0";
			write(socket_fd, "HTTP/1.0 404 Not Found\n\n",23);
			bad = 6;
		}		
		if (errno == EACCES) {
			//permissions issues
			response = "HTTP/1.0 403 Forbidden\0";
			write(socket_fd, "HTTP/1.0 403 Forbidden\n\n",23);
			bad = 7;
		}
	}  else {
	
		//the request is valid if we get to here
		//get the date
		char date[36];
		sprintf(date, "Date: %s, %.2d %s %.2d %.2d:%.2d:%.2d %s\n", day, ts->tm_mday, mon, 1900+ts->tm_year, ts->tm_hour, ts->tm_min, ts->tm_sec, ts->tm_zone);
		
		
		response = "HTTP/1.0 200 OK\0";
		
		//output the headers
		write(socket_fd, "HTTP/1.0 200 OK\n",16);
		write(socket_fd, date, 36);
		write(socket_fd, "Content-Type: text/html\n\n",25);
		char cont[1];
		char c;
		
		//write the file contents
		while (read(fp, &c, 1) ==  1) {
			write(socket_fd, &c, 1);
		}
	
	}

	//log to the console
	printf("%d ", num);  
	printf("%d ", 1900+ts->tm_year);
	printf("%s ", mon); 
	printf("%.2d %.2d:%.2d:%.2d ", ts->tm_mday, ts->tm_hour, ts->tm_min, ts->tm_sec);
	printf("%s:%d ",  client_ip, port);
	printf("GET %s HTTP/1.0; ", path);  //definately had some issues here, couldn't get the entire first line of the inital request to be printed
	printf("%s; ", response); 
	
	//show the requested resource if it was sent, otherwise don't
	if (!bad) {
		printf("%s\n", path);
	} else { 
		printf("\n"); 
	}   
}//processRequest



int main(int argc, char *argv[]) {
  
	int request_num = 0; //keep track of the sequence numbers
    int socket_file_desc, new_socket_desc, port_num; 
    socklen_t server_len, client_len;
    char buffer[MAXBUFFERLEN];
    struct sockaddr_in server_address, client_address;
    int num_bytes;
    int * response_len;
    


    //error check the input
    if (argc < 3) {
         printf( "Usage: %s <port> <root_dir>\n", argv[0] );
         return -1;
    } 
  
    port_num = atoi(argv[1]); //parse port number from CLI input to int

    //Create socket -> error checking
    if ((socket_file_desc = socket(AF_INET, SOCK_STREAM, 0)) > 0) {
        //printf("A TCP socket has been created\n");
    } else {
        printf("Failed to create TCP socket\n");
        return -1;
    }
    
	//set port and IP address
    server_address.sin_family = AF_INET; //IPv4
    server_address.sin_addr.s_addr = inet_addr("10.10.1.100");
    server_address.sin_port = htons(port_num);
	
	
	int option_val_true = 1;
	setsockopt(socket_file_desc, SOL_SOCKET, SO_REUSEADDR, &option_val_true, sizeof(option_val_true));//allow port reuse
    
	
	//bind the socket to address
	if (bind(socket_file_desc, (struct sockaddr*) &server_address, sizeof (server_address)) == 0) {
        //printf("Socket has been binded to provided port\n");
    } else {
        printf("Error binding socket");
        return -1;
    }
	
	printf("sws is running on TCP port %d and serving %s \npress q to quit... \n", port_num, argv[2]);
	
	struct termios oldSettings, newSettings; // for key press
    while (1) {
	
		//-------------------------------begin key press code-----------------------
		int select_result;
		fd_set read_fds;
		struct timeval timeout;
		
		/**************************
		modify terminal settings to override pressing enter - found this idea on http://www.cplusplus.com/forum/unices/11910/, author stated he examined the linux kernel source to 
		determine it. This overrides the line buffer on stdin, removing the need to press enter. It would also be applicable if one was trying to create a game like pong from the 
		command line.
		********************/
		tcgetattr( fileno( stdin ), &oldSettings );
		newSettings = oldSettings;
		newSettings.c_lflag &= (~ICANON & ~ECHO);
		tcsetattr( fileno( stdin ), TCSANOW, &newSettings );
		
		
		//set timeout
		timeout.tv_sec = 0; 
		timeout.tv_usec = 500; //and 500 microseconds
		
		//file descriptors to watch
		FD_ZERO( &read_fds );
		FD_SET( fileno(stdin), &read_fds );
		
		//file descriptor state change
		select_result = select( 1, &read_fds, NULL, NULL, &timeout );
		
		if( select_result > 0 )
        {
			//key is pressed, check if q
            char c;
            read( fileno( stdin ), &c, 1 );
			if (c=='q') break;
        }			
		
		tcsetattr( fileno( stdin ), TCSANOW, &oldSettings ); //restore terminal settings to stdin line buffered
		//---------------------------done key press code---------------------------
		
		// --------------------- server code ----------------------------
		int oth_select_result;
		fd_set sock_fds;
		
		//listen for connections
		if (listen(socket_file_desc, 10) < 0) {
            printf("Error on listen");
            return -1;        
        }  
		
		FD_ZERO (&sock_fds);
		FD_SET(socket_file_desc, &sock_fds);
		
		oth_select_result = select (FD_SETSIZE, &sock_fds, NULL, NULL, &timeout);
		
                
		if (oth_select_result > 0) {
			//connection is ready
			if ((new_socket_desc = accept(socket_file_desc, (struct sockaddr *) &client_address, &client_len)) < 0) {
				printf("Error on accept %s\n", strerror(errno));
				//return -1;        
			}
			
			//determine client ip address and port
			int port = 0;
			char client_ip[INET_ADDRSTRLEN];
			getpeername(socket_file_desc,(struct sockaddr*)&client_address,&client_len);
			port = ntohs(client_address.sin_port);
			inet_ntop(AF_INET, &(client_address.sin_addr), client_ip, sizeof(client_ip));
			
			
			//client is connected
			if (new_socket_desc > 0) {
				//printf("Client is connected\n");
				recv(new_socket_desc, buffer, MAXBUFFERLEN, 0);
				processRequest(buffer, MAXBUFFERLEN, new_socket_desc, argv[2], request_num, client_ip, port); //serve the request
				request_num++;
				close(new_socket_desc); //close the new socket
				
			}
		}
		 //-------------------done server code----------------------------
    }
    
	
	//the q key has been pressed
	
	
    close(socket_file_desc); //close the socket we are listening on

    tcsetattr( fileno( stdin ), TCSANOW, &oldSettings ); //restore terminal settings to line buffered stdin

    return 0; //successful execution


} //main
