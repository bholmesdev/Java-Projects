import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;


public class Platform {
	private int x1, y1, x2, y2,
		x3, y3, x4, y4,
		type;
	private int indexColor, indexColorShading, index;
	private int[] platformX, platformY;
	private Color platColor;
	private Rectangle rect;
	public Platform(Point sideOne, Point sideTwo, int type, int indexColor) {
		this.indexColor = indexColor;
		index = indexColor;
		indexColorShading = 0;
		platColor = new Color(250-(this.indexColor*10), 150-(this.indexColor*10), 30);
		
		x1 = (int)(sideOne.getX());
		y1 = (int)(sideOne.getY());
		x2 = (int)(sideTwo.getX());
		y2 = (int)(sideTwo.getY());
		this.type = type;
		
		//DEFINES THE SHAPE
		if(type == 1) {
			int sideLength = (x2-x1)/8;
			x3 = x1+sideLength;
			x4 = x2-sideLength;
			y3 = y1-sideLength*2;
			y4 = y2-sideLength*2;
		}
		else {
			x3 = x1;
			x4 = x2;
			int sideLength = x2-x1;
			y3 = y1-sideLength;
			y4 = y2-sideLength;
		}
		
		rect = new Rectangle(x3, y3, x4-x3, y2-y3);
		
		//DEFINES POLYGON
		platformX = new int[]{x1, x2, x4, x3};
		platformY = new int[]{y1, y2, y4, y3};
	}
	public void draw(Graphics2D g2) {
		g2.setColor(platColor);
		g2.fillPolygon(platformX, platformY, 4);
		g2.setColor(Color.black);
		g2.drawPolygon(platformX, platformY, 4);
		if(index == 0 && type == 1) {
			g2.setColor(platColor.darker());
			g2.fillRect(x1, y1, x2-x1, 30);
			g2.setColor(Color.black);
			g2.drawRect(x1, y1, x2-x1, 30);
		}
		rect.setLocation(x3, y3);
		//g2.setColor(Color.black);
		//g2.draw(rect);
	}
	public void moveFirst() {
		x1--;
		x2++;
		y1+=2;
		y2+=2;
		defineShape();
	}
	public void move(Point sideOne, Point sideTwo, int counter) {
		//for when the stage should move forward for horizontal blocks
		x1 = (int)(sideOne.getX());
		y1 = (int)(sideOne.getY());
		x2 = (int)(sideTwo.getX());
		y2 = (int)(sideTwo.getY());
		
		defineShape();
		
		if(counter%3 == 1) {
			indexColorShading++;
			platColor = new Color(250-(indexColor*10)+indexColorShading, 150-(indexColor*10)+indexColorShading, 30);
		}
		rect = new Rectangle(x3, y3, x4-x3, y2-y3);
	}
	public void moveDown(int counter, int amount) {
		//for when the stage should move down for vertical blocks
		y1+=amount;
		y2+=amount;
		
		defineShape();
		
		if(counter%4 == 0) {
			indexColorShading++;
			platColor = new Color(250-(indexColor*10)+indexColorShading, 150-(indexColor*10)+indexColorShading, 30);
		}
	}
	public void defineShape() {
		//makes the platform either horizontal or vertical when necessary
		if(type == 1) {
			int sideLength = (x2-x1)/8;
			x3 = x1+sideLength;
			x4 = x2-sideLength;
			y3 = y1-sideLength*2;
			y4 = y2-sideLength*2;
		}
		else {
			x3 = x1;
			x4 = x2;
			int sideLength = x2-x1;
			y3 = y1-sideLength;
			y4 = y2-sideLength;
		}
		platformX = new int[]{x1, x2, x4, x3};
		platformY = new int[]{y1, y2, y4, y3};
	}
	public boolean die() {
		//starts falling animation when a platform is removed
		y1+=5;
		y2+=5;
		y3+=5;
		y4+=5;
		platformX = new int[]{x1, x2, x4, x3};
		platformY = new int[]{y1, y2, y4, y3};
		//tells driver when the platform should be removed from the array
		if(y3 > 700)
			return true;
		else
			return false;
	}
	public void setIndex(int i) {
		index = i;
	}
	public Point getPointOne() {
		return new Point(x3, y3);
	}
	public Point getPointTwo() {
		return new Point(x4, y4);
	}
	public int getWidth() {
		return x4 - x3;
	}
	public int getHeight() {
		return y2 - y3;
	}
	public int getType() {
		return type;
	}
	public Rectangle getRect() {
		return rect;
	}
}
