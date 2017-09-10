import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class enemyBullet
{
    private int x, y, Vel = 0, yVel = 0, playerX;
    private Rectangle enemyBulletRect;
    private Image enemyBullet;
    private boolean shot = false, MEDIC = false, playerDead = false;
    private Random ran = new Random();
    public enemyBullet(int bugX, int bugY)
    {
        x = bugX + 13;
        y = bugY + 10;

        enemyBullet = Toolkit.getDefaultToolkit().getImage(getClass().getResource("enemy bullet.gif"));
        enemyBullet = enemyBullet.getScaledInstance(7, 30, 1);
        enemyBulletRect = new Rectangle (x, y, 7, 30);
    }

    public void draw(Graphics2D g2)
    {
        if (y >= 500 && playerDead == false)
            g2.drawImage(enemyBullet, x, y, null);
    }

    public Rectangle getRect()
    {
        return enemyBulletRect;
    }

    public void fire()
    {
        shot = true;
        yVel = ran.nextInt(5)+5;
        Vel = ran.nextInt(10)-5;
    }

    public void sendLoc(int bugX, int bugY)
    {
        x+=Vel;
        y+=yVel;
        if (shot == false)
        {
            x = bugX+13;
            y = bugY+10;
        }
        if (y >= 750)
        {
            shot = false;
            Vel = 0;
            yVel = 0;
        }
        if (playerDead = false)
            enemyBulletRect.setLocation(x, y);
    }

    public void reset()
    {
        y = -30;
    }

    public void shipDead()
    {
        playerDead = true;
        enemyBulletRect.setLocation (1000, 1000);
    }

    public Rectangle getRec()
    {
        return enemyBulletRect;
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
