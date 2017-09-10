import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class player
{
    private int x, y, Vel = 0, lives;
    private Rectangle playerRect;
    private Image player, explosion;
    private bullet ammo[] = new bullet[2];
    private boolean dead = false;
    private long timer;
    public player()
    {
        x = 350;
        y = 600;
        for (int x = 0; x < ammo.length; x++)
            ammo[x] = new bullet();
        lives = 3;

        player = Toolkit.getDefaultToolkit().getImage(getClass().getResource("player.gif"));
        player = player.getScaledInstance(50, 50, 1);
        explosion = Toolkit.getDefaultToolkit().getImage(getClass().getResource("explosion.gif"));
        explosion = explosion.getScaledInstance(50, 50, 1);
        playerRect = new Rectangle (x, y, 50, 50);
    }

    public void draw(Graphics2D g2)
    {
        for (int x = 0; x < ammo.length; x++)
            ammo[x].draw(g2);
        if (dead == false)
            g2.drawImage(player, x, y, null);
        else 
        {
            g2.drawImage(explosion, x, y, null);
        }

        x+=Vel;
    }

    public Rectangle getRect()
    {
        return playerRect;
    }

    public Rectangle getBulletRect1()
    {
        return ammo[0].getRect();
    }

    public Rectangle getBulletRect2()
    {
        return ammo[1].getRect();
    }

    public void moveLeft()
    {
        Vel = -10;
    }

    public void moveRight()
    {
        Vel = 10;
    }

    public void fire()
    {
        if (ammo[0].fired())
            ammo[1].fire();
        ammo[0].fire();
    }

    public void updateBulletX()
    {
        for (int index = 0; index < ammo.length; index++)
            ammo[index].sendX(x, dead);
        if (dead == false)
            playerRect.setLocation(x, y);
    }

    public void resetBullet1()
    {
        ammo[0].reset();
    }

    public void resetBullet2()
    {
        ammo[1].reset();
    }

    public void stop()
    {
        Vel = 0;
    }

    public void die()
    {
        playerRect.setLocation(1000, 1000);
        dead = true;
    }
}
