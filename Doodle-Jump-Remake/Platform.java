import java.awt.*;
import java.util.*;

public class Platform {
    private int x, y, width, height;
    private Rectangle myRectangle;
    Random ran = new Random();

    public Platform(int ex, int why, int len, int ht) {
        x = ex;
        y = why;
        width = len;
        height = ht;
        myRectangle = new Rectangle(x, y, width, height);
    }

    public void down() {
        y += 4;
        myRectangle.setLocation(x, y);
    }

    public void drawmySelf(Graphics2D d) {
        d.setColor(Color.green.darker());
        d.fillRect(x, y, width, height);
    }

    public void reset() {
        y = 0;
        x = ran.nextInt(450);
        myRectangle.setLocation(x, y);
    }

    public Rectangle getRect() {
        return myRectangle;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
