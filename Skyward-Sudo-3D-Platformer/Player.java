import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

public class Player {
	private int x, y,
	width, height, shapeType;
	private boolean plusRight;
	private Color fill;
	private Rectangle rect;
	public Player(Point one, Point two, boolean color) {
		if(color == true) {
			x = (int)(one.getX()+5);
			y = (int)(one.getY()+2);
		}
		else {
			x = (int)(one.getX()-120);
			y = (int)(one.getY()+2); 
		}
		shapeType = 1;
		plusRight = true;
		width = 130;
		height = 40;
		if(color == true)
			fill = new Color(250, 98, 75);
		else
			fill = new Color(75, 130, 250);

		rect = new Rectangle(x+5, y+5, width-10, height-10);
	}
	public void draw(Graphics2D g2) {
		g2.setColor(fill);
		g2.fillOval(x, y, width, height);
		g2.setColor(Color.black);
		g2.drawOval(x, y, width, height);
		rect.setLocation(x+5, y+5);
		//g2.draw(rect);
	}
	public void move(Point center, int width, int height) {
		if(x>=320)
			plusRight = false;
		if(x<50)
			plusRight = true;
		if(plusRight == true) {
			//algorithm for rotating in a elliptical orbit (based off of algebraic formula for an ellipse, solved for y)
			y = (int)(Math.sqrt(Math.pow(height, 2)-((Math.pow(height, 2)*Math.pow(x+65-center.getX(), 2))/Math.pow(width, 2))) + center.getY())-height/2;
			x+=6;
			//repositions circle for undefined values of y in the orbit
			if(!(y>0 && y<700))
				y = (int)(center.getY() - height/2);		
		}
		else {
			//same formula, but it becomes negative so the circle orbits the other way
			y = (int)(-Math.sqrt(Math.pow(height, 2)-((Math.pow(height, 2)*Math.pow(x+65-center.getX(), 2))/Math.pow(width, 2))) + center.getY())-height/2;
			x-=6;
			if(!(y>0 && y<700))
				y = (int)(center.getY() - height/2);
		}
		//repositions circle for undefined values of x in the orbit
		if(x <= (int)(center.getX() - width*1.5)) {
			this.width = width;
			this.height = height;
		}
		//determines when to change direction of orbit (at the edges)
		else if(x+width/2 < center.getX()) {
			if(shapeType == 1) {
				this.width+=2;
				this.height++;
			}
		}
		else {
			if(shapeType == 1) {
				this.width-=2;
				this.height--;
			}
		}
		rect = new Rectangle(x+5, y+5, this.width, this.height);
	}
	public void updatePos(Point one, int width, int height, int type) {
		shapeType = type;
		if(width == height) {
			x = (int)(one.getX() + 20);
			y = (int)(one.getY() + 20);
			this.width = width-40;
			this.height = height - 40;
		}
		else {
			x = (int)(one.getX() + 5);
			y = (int)(one.getY() + 2);
			this.width = width-10;
			this.height = height - 4;
		}
		rect = new Rectangle(x+5, y+5, this.width, this.height);
	}
	public void reset() {
		plusRight = true;
	}
	public void changeShape(int type) {
		if(type == 1) {
			width = 180;
			height = 60;
		}
		else {
			width = 140;
			height = 140;
		}
		shapeType = type;
	}
	public Point getCenter() {
		return new Point(x+width/2, y+height/2);
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public Rectangle getRect() {
		return rect;
	}
}
