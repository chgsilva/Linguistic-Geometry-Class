Student: Carlos Henrique Goncalves e Silva

SPECIFICATION
----------
Project 2. Due 02/23/15
Write a program for computation of trajectories in ABG.
Input: set X (a 2D or 3D table with or without obstacles), an element p for which the trajectories should be calculated, relations of reachability for this element (if necessary*), location of the element (the start of trajectories), the end of trajectories, the length of trajectories.
*) Remark: If these are usual relations of reachability of a chess piece for a standard chess board they do not have to be defined explicitly. Instead you can refer to them by the respective number of the table 15x15, which can be stored in advance, e. g., the numbers 1-6.
Algorithm: you can use your program from ass-t 1, grammars Gt(1) and Gt(2). Output:
Print trajectories for sample locations for all the chess pieces (for 8x8 board with and without obstacles): Pawn (assume that Pawn can move straight ahead only), Knight, Bishop, Rook, Queen, King. In your examples trajectories should be shortest and non-shortest.
Include at least one example of computation of trajectories for the element with unusual relations of reachability (different from chess pieces) and unusual board defined by you as part of the input of this program.

The output should be both: a graph "drawn on the board" and a list of coordinates of locations (stops) along those trajectories. A reasonable explanation (or a proof) that all the required trajectories have been generated should be included.
In particular, as a simple test generate all the trajectories for the King from a5 to h5 of the length 7. You do not have to print them all algebraically but print their total number (show them all on a graph).
IMPUT 
--------------
The input of this program should be written in the Data.txt file, ant it must be the follow :

The first line will be the the size of the Set. Example : 8x8 in the case of 2d and 8x8x8 in the case of 3d.
if there are some obstacles, it should the written the position here. For example, if there is a obstacle on 3x1 and 3x2 it should be put "3x1 3x2". If there is no obstacle, write no.
Following by the location of the Element (the start of the trajectory). Ex: "3x3x3" 
And then, in the forth line, should be written the end of the trajectory
The next line is the length of the trajectory 
And finally, the reachability. The reachability will be enter by the number that correspond to the piece or the arbitrary reachability for the arbitrary piece.
	0://ROOK				
	1://KING
	2://BISHOP
	3://QUEEN
	4://KNIGHT
	5://PAWN
	//ANY DIFFERENT VALUE WILL BE THE INTERPRETED AS THE ARBITRARY PIECE
	
	The output: The output will be displayed as a connected graph and each arrow means one move from one piece to the other.
	The list of coordinates of locations is not showed because it is implicited on the arrows from the graph, but if you prefer that I print them, just email me and I will do it.
	
RUN THIS PROJECT:
------------

Put the following files "P1j.zip" and the "Data.txt" in the same directory, then open a terminal on this directory and type
the following command
$ java -jar P1j.zip

and the output should be printed on terminal.
To run with other values, just change the Data.txt and type the command again.

Any doubts, please contact me.
Best Regards,
Carlos Silva
