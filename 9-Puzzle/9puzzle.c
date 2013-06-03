/* 

   NAME: JORDAN HEEMSKERK



   9puzzle.c
   CSC 225 - Spring 2013


   The assignment is to implement the solve_9puzzle() function below, which
   takes a 9puzzle board (as a 3x3 array, where the empty space is denoted
   by the value 0) and returns the number of moves required to solve the board
   (or -1 if the board is not solvable). Sections 3 & 5 of part 3 of the lab notes
   might be helpful in implementing this task.
   
   On the input board
     1 2 3
     4 5 0
     7 8 6
   the solve_9puzzle() function would return 1, since only one move (moving the
   6 tile up one row) is needed to solve the board. If given a solved board,
   solve_9puzzle() should return 0, since no moves are necessary. For full marks,
   your implementation should always return the _minimum_ number of moves needed.
   
   To aid in building the game graph for the puzzle, functions have been provided
   to convert each 3x3 board to an index and convert indices back into boards. As
   a result, you can create a graph using only vertex indices (and convert back and
   forth between actual boards as needed) instead of having to attach boards to each vertex
   or find the vertex corresponding to a board. See the comments in the code
   below for details.
   
   Do not change the name or parameters of the solve_9puzzle() function.
   You may add other functions as necessary. Do not use global variables or
   goto statements.
   
   To compile your code (producing a binary called "9puzzle"), use a command like
    gcc -o 9puzzle 9puzzle.c
   For marking purposes, your code must compile and run using the above command
   (without any special options) on one of the lab machines in ECS 242. (If it does
   not compile/run on a lab machine, it will not be marked.)
   
   This template includes some testing code to help verify the implementation.
   Input boards can be provided with standard input or read from a file.
   
   To provide test inputs with standard input, run the program with
	./9puzzle
   (assuming the executable name is '9puzzle')
   To terminate the input, use Ctrl-D (which signals EOF).
   
   To read test inputs from a file (e.g. boards.txt), run the program with
    ./9puzzle boards.txt
	
   The input format for both methods is the same. Input consists
   of a series of 9-puzzle boards, with the '0' character representing the empty square.
   For example, a sample board with the middle square empty is
   
    1 2 3
    4 0 5
    6 7 8
   
   And a solved board is
   
    1 2 3
    4 5 6
    7 8 0
   
   An input file can contain an unlimited number of boards; each will be processed separately.

*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

//The total number of possible boards is 9! = 1*2*3*4*5*6*7*8*9 = 362880
#define NUM_BOARDS 362880

typedef int Board[3][3];

typedef struct {
	int x;
	int y;
} Point;

typedef int Graph[NUM_BOARDS][4]; /*Max number of edges for a node is 4 (up,down,left,right)*/

typedef struct node {
	int val;
	struct node *next;
} Node;

typedef struct queue {
	Node* front;
	Node* back;
	int count;
} Queue;


//Given a board, find the board id number
int getIDFromBoard(Board b);

//Given an ID number, find the corresponding board and copy it into the provided array
void getBoardFromID(int id, Board outputBoard);
void build9PuzzleGraph(Graph G);
/* solve_9puzzle(B)
	Given a valid 9-puzzle board (with the empty space represented by the value 0,
	return the number of moves needed to solve the board or -1 if the board is not
	solvable. For full marks, the returned value should be the minimum possible
	number of moves to solve the board.
*/

/*Enqueue integer into Queue*/
void enqueue(Queue *Q, int value) {
	if (Q->back == 0) {
		Node* new = malloc(sizeof(Node));
		new->next = 0;
		new->val = value;
		Q->back = new;
		Q->front = new;
		Q->count = 1;
	} else {
		Node* new = malloc(sizeof(Node));
		new->next = 0;
		new->val = value;
		Q->back->next = new;
		Q->back = new;
		Q->count += 1;
	}
}

/*Dequeue integer from Queue*/
int dequeue(Queue *Q) {
	
	if (Q->count > 0) {
		if(Q->front == 0) return -1;
		int toReturn = Q->front->val;
		Node* old = Q->front;
		Q->front = Q->front->next;
		if(Q->front == 0) {
			Q->back = 0;
		}
		free(old);
		Q->count -= 1;
		return toReturn;
	}
	
}

/*Initialize a new graph*/
void initGraph(Graph G) {
	int i,j = 0;
	for(i = 0; i< NUM_BOARDS; i++) {
		for (j = 0; j < 4; j++) {
			G[i][j] = -1;
		}
	}
}

/*solve a 9puzzle*/
int solve_9puzzle(Board B){
    Graph G;
    initGraph(G);
	build9PuzzleGraph(G);
	return minLenPath(G, 0, getIDFromBoard(B));
}

/*BFS algorithm for computing the length of min path between vertices u and v of graph G*/
int minLenPath(Graph G, int u, int v) {
	int covered[NUM_BOARDS] = {0};
	int P[NUM_BOARDS];
	int j = 0;
	int count = 0;
	int current = -1;
	Queue queue;
	
	for (j=0; j < NUM_BOARDS; j++) P[j] = -2; /*Initialize spanning subtree with -2 which is escape character*/
    P[u] = -1; /*Set start node as -1*/	
	enqueue(&queue, u);
    
    /*While the queue is not empty, process each vertice*/	
    while (queue.count != 0) {
		current = dequeue(&queue);
		covered[current] = 1;
		int i = 0;
        /*to process vertice, add to P and queue up edges that are not visited*/
		while (G[current][i] != -1 && i < 4) {
			if (covered[G[current][i]] == 1) {
				i++;
				continue;	
			}
			P[G[current][i]] = current;
			enqueue(&queue, G[current][i]);
			i++;
		}
	}
	int i = 0; /*Counts the moves*/
	current = v;
	i++;
    /*Follow spanning tree to node count the edges*/
	while (P[current] != -1) {
		current = P[current];
		if (current == -2) return -1;
		i++;
	}
	return i-1;
}

/*Find the location of the empty spot on the board*/
Point findZero(Board B) {
	Point toReturn;
	int i = 0;
	int j = 0;
	for (i = 0; i < 3; i++) {
		for (j = 0; j < 3; j++) {
			if (B[i][j] == 0) {
				toReturn.x = i;
				toReturn.y = j;
			}
		}
	}
	return toReturn;
}

/*Move a tile on a board*/
void moveTile (Board initial, Board final, Point zero, Point toMove) {
	int tileBeingMoved = initial[toMove.x][toMove.y];
	int i,j;
	for (i = 0; i < 3; i++) {
		for (j = 0; j < 3; j++) {
			if (i == zero.x && j == zero.y) {
				final[i][j] = tileBeingMoved;
			} else if (i == toMove.x && j == toMove.y) {
				final[i][j] = 0;
			} else {
				final[i][j] = initial[i][j];
			}
		
		}
	}


}

/*Add an edge between vertice v and u in graph G*/
void addEdge (Graph G, int v, int u) {
	int i = 0;
	while(G[v][i] != -1) {
		i++;
	}
	G[v][i] = u;
}

/*Build the 9puzzle graph*/
void build9PuzzleGraph(Graph G) {
	int v = 0; /*counter*/
	Board currentBoard;
	Board modifiedBoard;
	Point zeroPoint;
	Point toMove;
	int modBoardID = 0;
    
    /*Loop over all 9! possible board configurations*/
	for (v = 0; v < NUM_BOARDS; v++) {
		getBoardFromID(v, currentBoard);
		zeroPoint = findZero(currentBoard);
        
        /*add edges if a move is possible*/
		if (zeroPoint.x > 0) {
           
			toMove.x = zeroPoint.x-1;
			toMove.y = zeroPoint.y;
			moveTile(currentBoard, modifiedBoard, zeroPoint, toMove);
			modBoardID = getIDFromBoard(modifiedBoard);
			addEdge(G, v, modBoardID);
		}
		if (zeroPoint.x < 2) {
            
			toMove.x = zeroPoint.x+1;
			toMove.y = zeroPoint.y;
			moveTile(currentBoard, modifiedBoard, zeroPoint, toMove);
			modBoardID = getIDFromBoard(modifiedBoard);
			addEdge(G, v, modBoardID);
		}
		if (zeroPoint.y > 0) {
            
			toMove.x = zeroPoint.x;
			toMove.y = zeroPoint.y-1;
			moveTile(currentBoard, modifiedBoard, zeroPoint, toMove);
			modBoardID = getIDFromBoard(modifiedBoard);
			addEdge(G, v, modBoardID);
		}
		if (zeroPoint.y < 2) {

			toMove.x = zeroPoint.x;
			toMove.y = zeroPoint.y+1;
			moveTile(currentBoard, modifiedBoard, zeroPoint, toMove);
			modBoardID = getIDFromBoard(modifiedBoard);
			addEdge(G, v, modBoardID);
		}
	
	}
}
/* Board/Index conversion functions
   These should be treated as black boxes (i.e. don't modify them, don't worry about
   understanding them). The conversion scheme used here is adapted from
     W. Myrvold and F. Ruskey, Ranking and Unranking Permutations in Linear Time,
     Information Processing Letters, 79 (2001) 281-284. 
*/

/* getIDFromBoard(B)
   Given a 3x3 board B, return the board index (between 0 and NUM_BOARDS-1)
*/
int getIDFromBoard(Board B){
	int i,tmp,s,n;
	int *P = &B[0][0];
	int PI[9];
	for (i = 0; i < 9; i++)
		PI[P[i]] = i;
	int id = 0;
	int multiplier = 1;
	for(n = 9; n > 1; n--){
		s = P[n-1];
		P[n-1] = P[PI[n-1]];
		P[PI[n-1]] = s;
		
		tmp = PI[s];
		PI[s] = PI[n-1];
		PI[n-1] = tmp;
		id += multiplier*s;
		multiplier *= n;
	}
	return id;
}

/* getBoardFromID(id,outputBoard)
   Given a board index, return the corresponding board (in the outputBoard array)
*/
void getBoardFromID(int id, Board outputBoard){
	int *P = &outputBoard[0][0];
	int i,n,tmp;
	for (i = 0; i < 9; i++)
		P[i] = i;
	for (n = 9; n > 0; n--){
		tmp = P[n-1];
		P[n-1] = P[id%n];
		P[id%n] = tmp;
		id /= n;
	}
}

int main(int argc, char** argv){
	/* Code to test your implementation */
	/* You may modify this, but nothing in this function will be marked */

	int i,j,valuesRead;
	
	Board B;
	

	int graphNum = 0;
	FILE* infile;

    

	if (argc > 1){
		//If a file argument was provided on the command line, read from the file
		infile = fopen(argv[1],"r");
		if (!infile){
			printf("Unable to open %s\n",argv[1]);
			return 0;
		}
		printf("Reading input values from %s.\n",argv[1]);
	}else{
		//Otherwise, read from standard input
		infile = stdin;
		printf("Reading input values from stdin.\n");
	}
	
	//Read boards until EOF is encountered (or an error occurs)
	while(1){
		graphNum++;
		printf("Reading board %d\n",graphNum);
		valuesRead = 0;
		for (i = 0; i < 3; i++)
			for (j = 0; j < 3; j++){
				valuesRead += fscanf(infile,"%d",&B[i][j]);
			}
		if (valuesRead < 9){
			printf("Board %d contains too few values.\n",graphNum);
			break;
		}
		int solution_moves = solve_9puzzle(B);
		if (solution_moves == -1)
			printf("Board %d: Not solvable.\n",graphNum);
		else
			printf("Board %d: Solvable in %d moves.\n",graphNum,solution_moves);
			
	}
	return 0;
}
