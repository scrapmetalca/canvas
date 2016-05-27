Readme for Canvas Drawing Application

To build, simple change to the directory containing the file CanvasDraw.java and run the command: javac CanvasDraw.java

To run use the command: java -cp . CanvasDraw

Usage:
Create a canvas
	C 10 10 
Create a rectangle
	L 5 5 10 5
Create a rectangle
	R 3 3 8 8
Flood fill
	B 4 4 x
Help
	H
Quit 
	Q
Any of the above commands take either upper or lower case for the first letter

Error handling
	If characters other than the above are used they are ignored
	If points are outside the canvas the command is terminated
	If line is not vertical or horizontal, it is not drawn
	If points for rectangles or lines are out of ordered that is handled
	If canvas is less than 4 x 4, it is not created
	Improper input throws exception which is caught in the loop
		For example, L a b 2 3

Possible design changes
	Use an enum to handle the different inputs. 
		Determine valid command characters
		Contain count of parameters for each command
	Currently the border is part of the canvas. It could be drawn separately.
		
