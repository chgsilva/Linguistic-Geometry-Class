Student: Carlos Henrique Goncalves e Silva
Student ID: 104668154

SPECIFICATION
----------
Project 1. Due 02/09/15
Write a program for computation of distances in ABG.
Input: set X (a 2D or 3D table with or without obstacles), an element p for which the
distances should be calculated, location of the element, reachability relations for
the element p (given as a formula or a small table).
Output: a table of distances.
Algorithm: for the 8x8 set X without obstacles generate 15x15 tables and use them;
for different X use direct computation.
Print distances for sample locations for all the chess pieces (for 8x8 (and larger)
board with and without obstacles): Pawn (assume that Pawn can move straight ahead only), Knight, Bishop, Rook, Queen, and King. Print all your 15x15 tables.
##########################END OF SPECIFICATION##########################################

The input of this program should be written in the Data.txt file, ant it must be the follow :

The first line will be the the size of the Set. Example : 8x8 in the case of 2d and 8x8x8 in the case of 3d.
if there are some obstacles, it should the written the position here. For example, if there is a obstacle on 3x1 and 3x2 it should be put "3x1 3x2". If there is no obstacle, write no.
Following by the location of the Element. Ex: "3x3x3"
And finally, the reachability. The reachability will be enter by the number that correspond to the piece (once it will not have a arbitrary piece).
	0://ROOK				
	1://KING
	2://BISHOP
	3://QUEEN
	4://KNIGHT
	5://PAWN

When The table is 2d, without obstacle, all of the 15x15 tables are printed as output. As requested in the project description. Otherwise will be used the direct computation.

Example of input:
-------------------------	
	5x5x5
	2x2x4
	3x3x3
	0
-------------------------
In this imput, It asks for the distances for a table 3D, of size 5x5x5, with a obstacle in the 2x2x4. The piece is located in the 3x3x3 and its a Rook.

The Output for 3D tables is the following: Each layer of the 3d is printed separately. 
OutPut Example for 3D Tables:
-------------------------
    3 2 3   
    2 1 2 
    3 2 3 

    2 1 2 
    1 0 1 
    2 1 2 

    3 2 3 
    2 1 2 
    3 2 3
-------------------------
The 0 space is where the Rook is located, and the numbers are the distances.


Another example of input:	
-------------------------	
	8x8
	no
	4x4
	0
-------------------------


TO RUN THIS PROJECT:

Put the following files "P1j.zip" and the "Data.txt" in the same directory, then open a terminal on this directory and type
the following command
$ java -jar P1j.zip

and the output should be printed on terminal.
To run with other values, just change the Data.txt and type the command again.

Any doubts, please contact me.
Best Regards,
Carlos Silva