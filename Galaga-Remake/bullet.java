import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class bullet
{
    private int x, y, Vel = 0, shootVel = 0, playerX;
    private Rectangle bulletRect;
    private Image bullet;
    private boolean shot = false, MEDIC = false;
    public bullet()
    {
        x = 372;
        y = 615;

        bullet = Toolkit.getDefaultToolkit().getImage(getClass().getResource("bullet.gif"));
        bullet = bullet.getScaledInstance(7, 30, 1);
        bulletRect = new Rectangle (x, y, 7, 30);
    }

    public void draw(Graphics2D g2)
    {
        g2.drawImage(bullet, x, y, null);
        if (y <= -30)
        {
            shot = false;
            shootVel = 0;
            y = 615;
            x = playerX+22;
        }

        x+=Vel;
        y+=shootVel;
    }

    public Rectangle getRect()
    {
        return bulletRect;
    }

    public boolean fired()
    {
        return shot;
    }

    public void fire()
    {
        shot = true;
        Vel = 0;
        if (MEDIC == false)
            shootVel = -20;
        bulletRect.setLocation(x, y);
    }

    public void sendX(int planeX, boolean dead)
    {
        bulletRect.setLocation(x, y);
        MEDIC = dead;
        playerX = planeX;
        if (shot == false && dead == false)
            x = playerX+22;
        else if (dead)
            x = 1000;
    }

    public void reset()
    {
        y = -30;
    }
}
