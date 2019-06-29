import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;

import javax.swing.JPanel;

public class Dots extends JPanel{
	
	protected int x;
	protected int y;
	protected int width;
	protected int height;
	protected Color color;
	protected boolean visible;
	protected Graphics2D g2d;
	private int dx;
	private int dy;
	
	public Dots(Color color, int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.color = color;
		visible = true;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawDot(g);
	}
	
	public void drawDot(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;//cast the graphics into 2d graphics
		Ellipse2D e = new Ellipse2D.Double(x, y, width, height);//create a 2d ellipse at the given coordinates
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//RH makes graphics
		//look better by enabling hints, turning antialias on makes things look more lifelike but code slower, antialias off is
		//less lifelike but faster performance
		
		rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHints(rh);//sets the 2d graphics to the rendering style created
		g2d.setStroke(new BasicStroke(5));//Stroking a Shape is like tracing its outline with a marking pen of the appropriate size and shape. 
		//The area where the pen would place ink is the area enclosed by the outline Shape.
		g2d.setColor(color);
		g2d.draw(e);
		
	}
	
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public void setVisible(Boolean visible) {
		this.visible = visible;
	}
	
	//rectangle bounds needed for collision detection!
	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}
	
	public void move() {
		x += dx;
		y += dy;
	}
	
	//key pressed and key released controlled with key event
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			
			if(key == KeyEvent.VK_LEFT) {
				dx = -1;
			}
			
			if(key == KeyEvent.VK_RIGHT) {
				dx = 1;
			}
			
			if(key == KeyEvent.VK_UP) {
				dy = -1;
			}
			
			if(key == KeyEvent.VK_DOWN) {
				dy = 1;
			}
		}
		
		public void keyReleased(KeyEvent e) {
			int key = e.getKeyCode();
			
			if(key == KeyEvent.VK_LEFT) {
				dx = 0;
			}
			
			if(key == KeyEvent.VK_RIGHT) {
				dx = 0;
			}
			
			if(key == KeyEvent.VK_UP) {
				dy = 0;
			}
			
			if(key == KeyEvent.VK_DOWN) {
				dy = 0;
			}
		}

}
