import java.io.*;
import java.util.*;

// Simple drawing canvas
// Questions
// 	Where is x=0, y=0? Top Left?
// Error handling
// 	non-integer values
// 	values outside of canvas
// 	canvas too small ( smaller than 4 x 4
// 	points out of order (x1 < x0 or y1 < y0)
// 	points off canvas - less than 0 or greater than width or height
// Other features
//	handle lower case for R, B, L, C
//	Help command - H or h

public class CanvasDraw {
	public static void main(String[] args) throws IOException {
		CanvasDraw s = new CanvasDraw();
		s.draw();
	}

	public void draw() {
		Console cnsl = null;
		boolean canvasSet = false;
		String result = "";
		Integer width = 0, height = 0;
		Integer x0 = null, y0 = null, x1 = null, y1 = null;
		char[][] canvas = new char[0][0];

		// creates a console object
		cnsl = System.console();
		// run until the program exits
		while (1 == 1) {
			try {
				String str = cnsl.readLine("enter command: ");
				StringTokenizer tok = new StringTokenizer(str, " ");
				ArrayList<String> a = new ArrayList<String>();
				while (tok.hasMoreTokens()) {
					a.add(tok.nextToken());
				}
				System.out.println();
				String action = a.get(0);

				// Check to see if canvas exists (exclude c and h commands)
				if (!action.equals("C") 
					&& !action.equals("c") 
					&& !action.equals("Q") 
					&& !action.equals("q") 
					&& !canvasSet) {
					System.out.println("Canvas not created yet.");
					continue;
				} else {	
					switch (action) {
						case "c": 
						case "C": 
							if (a.size() == 3) {
								canvasSet = true;
								System.out.println("Canvas: " + a.get(1) + " x " + a.get(2));
								width = Integer.parseInt(a.get(1));
								height = Integer.parseInt(a.get(2));
								if( width < 4 || height < 4 ) {
									System.out.println("Improper arguments for Canvas");
									break;
								}
								canvas = new char[height+2][width+2];
								initCanvas(canvas, width, height);
							} else {
								System.out.println("Improper arguments for Canvas");
							}
							break;
						case "l":
						case "L":
							result = handleLine(canvas, a, width, height);
							if( !result.equals("")) {
								 System.out.println(result);
							}
							break;
						case "r":
						case "R":
							result = handleRectangle(canvas, a, width, height);
							if( !result.equals("")) {
								 System.out.println(result);
							}
							break;
						case "b": 
						case "B": 
							result = handleBucket(canvas, a);
							if( !result.equals("")) {
								 System.out.println(result);
							}
							break;
						case "q":
						case "Q":
							System.exit(0);
							break;
						case "h":
						case "H":
							System.out.println("Create Canvas:C 5 10");
							System.out.println("Create Line:L 5 5 5 8");
							System.out.println("Create Rectangle: R 5 5 10 10");
							System.out.println("Bucket Fill:B 5 10 c");
							System.out.println("Help: H 5 10 c");
						default:
							System.out.println("Unknown command");
					}
				}
				showCanvas(canvas, height);
			} catch (NumberFormatException ex) {
				System.out.println(ex);
			}
		}
	}

	boolean initCanvas (char[][] canvas, Integer width, Integer height) {
		// Initialize entire canvas to ' '
		for( int i=0; i<width+2; i++) {
			for( int j=0; j<height+2; j++) {
				canvas[j][i] = ' ';
			}
		}
		for( int i=0; i<width+2; i++) {
			canvas[0][i] = '-';
			canvas[height+1][i] = '-';
		}
		for( int j=1; j<height+1; j++) {
			canvas[j][0] = '|';
			canvas[j][width+1] = '|';
		}
		return true;
	}

	String handleLine(char[][] canvas, ArrayList a, Integer width, Integer height) {
		Integer x0, y0, x1, y1 = null;
		if (a.size() != 5) {
			return "Not enough or too many arguments";
		}
		x0 = Integer.parseInt((String)a.get(1));
		y0 = Integer.parseInt((String)a.get(2));
		x1 = Integer.parseInt((String)a.get(3));
		y1 = Integer.parseInt((String)a.get(4));
		if (((x0 == x1) && (y0 == y1)) || ((x0 != x1) && (y0 != y1))) {
			return "Improper coordinates for line";
		}
		// Assure that x0 and x1 are on the canvas
		if( x0 < 0 || x0 > width || y0 < 0 || y0 > height || x1 < 0 || x1 > width || y1 < 0 || y1 > height) {
			return "Line exceeds canvas size";
		}
		// Assure that x0 < x1 and y0 < y1
		if( x0 > x1 ) {
			Integer temp = x1;
			x1 = x0;
			x0 = temp;
		}
		if( y0 > y1 ) {
			Integer temp = y1;
			y1 = y0;
			y0 = temp;
		}
		addLine(canvas, x0, y0, x1, y1);
		return "";
	}

	String handleRectangle(char[][] canvas, ArrayList a, Integer width, Integer height) {
		Integer x0, y0, x1, y1 = null;
		if (a.size() != 5) {
			return "Not enough or too many arguments";
		}
		x0 = Integer.parseInt((String)a.get(1));
		y0 = Integer.parseInt((String)a.get(2));
		x1 = Integer.parseInt((String)a.get(3));
		y1 = Integer.parseInt((String)a.get(4));
		// Make sure coordinates are in correct order 
		// Assure that x0 and x1 are on the canvas
		if( x0 < 0 || x0 > width || y0 < 0 || y0 > height || x1 < 0 || x1 > width || y1 < 0 || y1 > height) {
			return "Rectangle is off canvas";
		}
		if (x0 > x1) {
			Integer temp = x1;
			x1 = x0;
			x0 = temp;
		}
		if (y0 > y1) {
			Integer temp = y1;
			y1 = y0;
			y0 = temp;
		}
		addRectangle(canvas, x0, y0, x1, y1);
		return "";
	}

	String handleBucket(char[][] canvas, ArrayList a) {
		if (a.size() != 4) {
			return "Not enough or too many arguments";
		}
		Integer x0 = Integer.parseInt((String)a.get(1));
		Integer y0 = Integer.parseInt((String)a.get(2));
		char color = ((String)a.get(3)).charAt(0);
		boolean[][] visited = new boolean[canvas.length][canvas[0].length];
		floodFill(canvas, visited, x0, y0, color);
		return "";
	}
	
	// Floodfill algorithm uses recursion to find all points
		void floodFill(char[][] canvas, boolean[][] visited, int r, int c, char color) {
		// Quit if off the canvas:
		if(r < 1 || r >= canvas.length - 1 || c < 1 || c >= canvas[0].length - 1) return;
		 
		// Quit if visited
		if(visited[r][c]) return;
		visited[r][c] = true;
		 
		// Quit if hit non empty point
		if(canvas[r][c] != ' ') return;
		 
		// Set color
		canvas[r][c] = color;
		
		//recursively fill in all directions
		floodFill(canvas, visited, r+1, c, color);
		floodFill(canvas, visited, r-1, c, color);
		floodFill(canvas, visited, r, c+1, color);
		floodFill(canvas, visited, r, c-1, color);
	}

	String addLine(char[][] canvas, Integer x0, Integer y0, Integer x1, Integer y1) {
		for( int i=x0; i<=x1; i++) {
			for( int j=y0; j<=y1; j++) {
				canvas[j][i] = 'x';
			}
		}		
		return "";
	}

	String addRectangle(char[][] canvas, Integer x0, Integer y0, Integer x1, Integer y1) {
		for( int i=x0; i<=x1; i++) {
			canvas[y0][i] = 'x';
			canvas[y1][i] = 'x';
		}		
		for( int i=y0; i<=y1; i++) {
			canvas[i][x0] = 'x';
			canvas[i][x1] = 'x';
		}		
		return "Created Rectangle";
	}

	void showCanvas(char[][] canvas, Integer height) {
		for (int i=0; i<height+2; i++) {
			String str = new String(canvas[i]);
			System.out.println(str);
		}
		System.out.println();
		return;
	}
}
