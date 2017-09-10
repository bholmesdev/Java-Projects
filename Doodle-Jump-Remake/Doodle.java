import java.awt.*;
public class Doodle
{
    private int x, y;
    private Rectangle theRectangle;
    private int yVel = -10;
    private int flag = 1;
    public Doodle()
    {
        x = 225;
        y = 300;
        y+=yVel;
        theRectangle = new Rectangle(x, y+40, 35, 10);
    }
    
    public void move(int change)
    {
        x = change - 25;
        theRectangle.setLocation(x, y+40);
    }

    public void bounce(int jump)
    {
        y+=jump;
        theRectangle.setLocation(x, y+40);
    }

    public Rectangle getRect()
    {
        return theRectangle;
    }
    
    public void reset()
    {
        x = 225;
        y = 300;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }
}
