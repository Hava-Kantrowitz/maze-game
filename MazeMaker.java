import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Path2D;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;
 
public class MazeMaker extends JPanel implements ActionListener {
	
	/**
	 * Data type for the direction
	 * @author havak
	 *
	 */
    enum Dir {
        N(1, 0, -1), S(2, 0, 1), E(4, 1, 0), W(8, -1, 0);//the directions
        final int bit;//given cell
        final int dx;//the movement in the x direction
        final int dy;//the movement in the y directions
        Dir opposite;//the direction opposite the given direction
 
        // initializes all opposite values
        static {
            N.opposite = S;
            S.opposite = N;
            E.opposite = W;
            W.opposite = E;
        }
 
        Dir(int bit, int dx, int dy) {//the direction enum
            this.bit = bit;//the given cell
            this.dx = dx;//the movement in x
            this.dy = dy;//the movement in y
        }
    };
    
    final int nCols;//number of columns in maze
    final int nRows;//number of rows in maze
    final int cellSize = 25;//pixel size of each cells
    final int margin = 25;//pixel size of the margin
    final int[][] maze;//the maze
    final int DELAY = 10;//the repaint speed
    private Timer timer;//the timer
    Dots playerDot;//the player piece
    Dots startDot;//the start location
    Dots finalDot;//the end location
    boolean gameOver = false;//whether the game is over
 
    /**
     * Creates the maze generator and maze solver
     * @param size the size of the board, how many rows/columns it would be
     */
    public MazeMaker(int size) {
        setPreferredSize(new Dimension(650, 650));//set the size
        setBackground(Color.white);//set the background color
        nCols = size;//set the number of rows and cols to the given size
        nRows = size;//making it a square
        maze = new int[nRows][nCols];//initialize the maze with the correct number of rows and columns
        generateMaze(0, 0);//generate the maze starting at coordinate (0,0)
        initDots();
        setFocusable(true);
 
        addKeyListener(new TAdapter());
        timer = new Timer(DELAY, this);
		timer.start();
    }
    
    /**
     * Initializes the dots on the board
     */
    public void initDots() {
    	int offset = margin + cellSize / 2;//create the animation offset
        
        startDot = new Dots(Color.blue, offset-5, offset-5, 10, 10);
        playerDot = new Dots(Color.pink, offset-5, offset-5, 10, 10);
        int x = offset + (nCols - 1) * cellSize;//set the x value
        int y = offset + (nRows - 1) * cellSize;//set the y value
        finalDot = new Dots(Color.green, x-5, y-5, 10, 10);
    }
 
    @Override
    /**
     * Paints the maze and the solution
     * @param g the graphics input
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);//super it up to the component
        if(!gameOver) {
        drawMaze(g);
        drawDots(g);
        }
        else {
        	drawGameOver(g);
        }
        
        Toolkit.getDefaultToolkit().sync();
    }
    
    /**
     * Draws the maze
     * @param g the graphics input
     */
    public void drawMaze(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;//make it a 2d drawing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);//turn on rendering hints
 
        g2d.setStroke(new BasicStroke(5));//set the size of the stroke
        g2d.setColor(Color.black);//set the color to black
 
        // draw maze
        for (int r = 0; r < nRows; r++) {//for each row and columns
            for (int c = 0; c < nCols; c++) {
 
                int x = margin + c * cellSize;//the x is the column number adjusted for cell size and margin
                int y = margin + r * cellSize;//the y is the row number adjusted for cell size and margin
 
                if ((maze[r][c] & 1) == 0) // Draws north
                    g2d.drawLine(x, y, x + cellSize, y);
 
                if ((maze[r][c] & 2) == 0) // Draws south
                    g2d.drawLine(x, y + cellSize, x + cellSize, y + cellSize);
 
                if ((maze[r][c] & 4) == 0) // Draws east
                    g2d.drawLine(x + cellSize, y, x + cellSize, y + cellSize);
 
                if ((maze[r][c] & 8) == 0) // Draws west
                    g2d.drawLine(x, y, x, y + cellSize);
            }
        }
        
        
 
    }
    
    /**
     * Draws the dots in the maze
     * @param g the graphics input
     */
    public void drawDots(Graphics g) {
    	startDot.drawDot(g);
    	playerDot.drawDot(g);
    	finalDot.drawDot(g);
    }
    
    /**
     * Draws the game over screen
     * @param g the graphics input
     */
    private void drawGameOver(Graphics g) {
		String msg = "Game Over. You solved the maze!";
		Font small = new Font("Helvetica", Font.BOLD, 30);
		FontMetrics fm = getFontMetrics(small);
		
		g.setColor(Color.black);
		setBackground(Color.green);
		g.setFont(small);
		g.drawString(msg, 90, 350);
	}
 
    /**
     * Generates a maze using the recursive backtracking algorithm
     * @param r the row the current cell is in
     * @param c the column the current cell is in
     */
    void generateMaze(int r, int c) {
        Dir[] dirs = Dir.values();//create a new list of directions
        Collections.shuffle(Arrays.asList(dirs));//shuffle the list of directions
        for (Dir dir : dirs) {//for each direction in the directions list
            int nc = c + dir.dx;//adjust the new column
            int nr = r + dir.dy;//adjust the new row
            if (withinBounds(nr, nc) && maze[nr][nc] == 0) {//if it is within the bounds and not equal to 0
                maze[r][c] |= dir.bit;//set the row and column
                maze[nr][nc] |= dir.opposite.bit;//set the new row and column
                generateMaze(nr, nc);//recursively generate the maze 
            }
        }
    }
 
    /**
     * Determines if the cell at the given row and column is within the bounds of the board
     * @param r the row of the current cell
     * @param c the column of the current cell
     * @return true if within bounds, false otherwise
     */
    boolean withinBounds(int r, int c) {
        return c >= 0 && c < nCols && r >= 0 && r < nRows;//returns true if it is within the rounds of the board, false otherwise
    }
    
    /**
     * Moves the player dot
     */
    public void updateDots() {
    	playerDot.move();
    }
    
    /**
     * Determines if the player dot has collided with the final dot to end the game
     */
    public void checkDotCollisions() {
    	
    	Rectangle playerBounds = playerDot.getBounds();
    	Rectangle finalBounds = finalDot.getBounds();
    	if(playerBounds.intersects(finalBounds)) {
    		gameOver = true;
    	}
    	
    }
    
    /**
     * Determines if the player dot has collided with the wall
     */
    public void checkWallCollisions() {
    	
    	for (int row = 0; row < maze.length; row++) {
    		for(int col = 0; col < maze.length; col++) {
    			if(playerDot.getX() == row && playerDot.getY() == col) {
    				playerDot.stopMotion();
    			}
    		}
    	}
    }
    
    /**
     * Determines whether the game has finished and responds to it
     */
    private void isGameOver() {
		if(gameOver == true) {
			timer.stop();
		}
	}
 
    /**
     * Main function to set up the board and maze
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame f = new JFrame();//create the new frame
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//set the close operation
            f.setTitle("Maze Game");//set the title
            f.setResizable(false);//not resizable
            f.add(new MazeMaker(24), BorderLayout.CENTER);//add the maze to the board
            f.pack();//pack size
            f.setLocationRelativeTo(null);//set location
            f.setVisible(true);//set the maze to visible
        });
    }
    
  /**
   * Class to handle key events
   * @author havak
   *
   */
  	private class TAdapter extends KeyAdapter{
  		@Override
  		public void keyReleased(KeyEvent e) {
  			playerDot.keyReleased(e);
  		}
  		
  		@Override
  		public void keyPressed(KeyEvent e) {
  			playerDot.keyPressed(e);
  		}
  	}

@Override
/**
 * Actions to be performed every instance of the timer
 */
public void actionPerformed(ActionEvent arg0) {
	isGameOver();//decide if game is over
	updateDots();//if not, update dots
	checkDotCollisions();//determine any collisions
	checkWallCollisions();
	repaint();//repaint
	
}

}
