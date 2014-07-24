CC=gcc

all:
	
	$(CC) rwss.c rdp.c -o rwss
	$(CC) rwsc.c rdp.c -o rwsc

debug_client:
	$(CC) -g rwsc.c rdp.c 
	mv a.out rwsc.out
	gdb rwsc.out
debug_server:
	$(CC) -g rwss.c rdp.c 
	mv a.out rwss.out
	gdb rwss.out
valgrind_server:
	$(CC) -g rwss.c rdp.c -o ./rwss 
	valgrind ./rwss 10.10.1.100 8080 www 
clean:
	rm -f *.out
	rm -f *.o
	rm -f rwss
	rm -f rwsc
