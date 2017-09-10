import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class waveTwo
{
    private int x, y, width, height, Vel, yVel, originalYPos, originalXPos, flyingVel;
    private Rectangle redRect;
    private Image red, explosion;
    private boolean comingBack = false, flying = false, dead = false, shootOnce = true;
    private Clip falling;
    private Random ran = new Random();
    private enemyBullet ammo[] = new enemyBullet[3];
    private Rectangle[] ammoRec = new Rectangle[3];
    public waveTwo(int xDimension, int yDimension)
    {
        try {
            URL url = this.getClass().getClassLoader().getResource("falling.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            falling = AudioSystem.getClip();
            falling.open(audioIn);

            /*URL url2 = this.getClass().getClassLoader().getResource("DW theme.wav");
            AudioInputStream audioIn2 = AudioSystem.getAudioInputStream(url2);
            theme = AudioSystem.getClip();
            theme.open(audioIn2);*/
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        width = 50;
        height = width;
        x = xDimension*width+175;
        y = yDimension*height+200;
        originalXPos = xDimension*width+175;
        originalYPos = yDimension*height+200;
        Vel = -1;
        yVel = 0;

        red = Toolkit.getDefaultToolkit().getImage(getClass().getResource("waveTwo.gif"));
        red = red.getScaledInstance(50, 50, 1);
        explosion = Toolkit.getDefaultToolkit().getImage(getClass().getResource("explosion.gif"));
        explosion = explosion.getScaledInstance(50, 50, 1);
        redRect = new Rectangle (x, y, width, height);

        for (int index = 0; index < ammo.length; index++)
            ammo[index] = new enemyBullet(x, y);

        ammoRec[0] = new Rectangle(ammo[0].getRec());
        ammoRec[1] = new Rectangle(ammo[1].getRec());
        ammoRec[2] = new Rectangle(ammo[2].getRec());
    }

    public void draw(Graphics2D g2)
    {
        for (int index = 0; index < ammo.length; index++)
            ammo[index].draw(g2);
        if (dead == false)
            g2.drawImage(red, x, y, null);
        else
            g2.drawImage(explosion, x, y, null);
    }

    public void switchVel()
    {
        Vel*=-1;
    }

    public void move()
    {
        if (flying == false)
            x+=Vel;
        else if (flying)
            x+=flyingVel;
        y+=yVel;
        originalXPos+=Vel;
        if (y >= 500 && shootOnce)
        {
            for (int index = 0; index < ammo.length; index++)
                ammo[index].fire();
            shootOnce = false;
        }
        if (y >=750)
        {
            flying = false;
            comingBack = true;
            y=-50;
            x=originalXPos;
            yVel = 5;
            shootOnce = true;
        }
        else if (y == originalYPos && comingBack)
        {
            yVel = 0;
            comingBack = false;
        }

        if (dead == false)
            redRect.setLocation(x, y);

        for (int index = 0; index < ammoRec.length; index ++)
        {
            ammoRec[0].setLocation(ammo[0].getX(), ammo[0].getY());
            ammoRec[1].setLocation(ammo[1].getX(), ammo[1].getY());
            ammoRec[2].setLocation(ammo[2].getX(), ammo[2].getY());
        }
    }

    public void fly()
    {
        //falling.loop(1);

        comingBack = false;
        flying = true;
        yVel = 8+ran.nextInt(3);
        flyingVel = ran.nextInt(10)-5;
        if (dead == false)
            redRect.setLocation(x, y);
        for (int index = 0; index < ammoRec.length; index ++)
        {
            ammoRec[0].setLocation(ammo[0].getX(), ammo[0].getY());
            ammoRec[1].setLocation(ammo[1].getX(), ammo[1].getY());
            ammoRec[2].setLocation(ammo[2].getX(), ammo[2].getY());
        }
    }    

    public void increaseVel()
    {
        if (flyingVel < 0 && y > 500+ran.nextInt(40))
            flyingVel--;
        else if (flyingVel >= 0 && y > 500+ran.nextInt(40))
            flyingVel++;
    }

    public void updateBullet()
    {
        for (int index = 0; index < ammo.length; index++)
            ammo[index].sendLoc(x, y);
    }

    public void die()
    {
        for (int index = 0; index < ammo.length; index++)
            ammo[index].shipDead();
        dead = true;
        flying = false;
        //Vel = 0;
        yVel = 0;
        redRect.setLocation(1000, 1000);
    }

    public Rectangle getRect()
    {
        return redRect;
    }
    
    public Rectangle[] getBulletRec()
    {
        return ammoRec;
    }

    public int getX()
    {
        return x;
    }

    public boolean flying()
    {
        return flying;
    }

    public boolean dead()
    {
        return dead;
    }
}
