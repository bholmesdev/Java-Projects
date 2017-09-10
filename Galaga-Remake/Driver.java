import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Driver extends JFrame implements Runnable, KeyListener
{
    Container con = getContentPane();
    Thread t = new Thread(this);

    player player = new player();
    waveOne grid1[][] = new waveOne[10][2];
    waveTwo grid2[][] = new waveTwo[8][2];
    waveThree grid3[] = new waveThree[4];
    //ArrayList ammo;
    Random ran = new Random();
    long timer, startTimer;
    boolean goBee = true, start = true, drawStart = false, WINNER = false, gameovah = false;
    int backY=-750, score = 0;
    Clip shoot, intro, explosion;
    Image startPic, background, winner, gameover;

    public Driver()
    {
        try {
            URL url = this.getClass().getClassLoader().getResource("shoot.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            shoot = AudioSystem.getClip();
            shoot.open(audioIn);

            URL url2 = this.getClass().getClassLoader().getResource("intro.wav");
            AudioInputStream audioIn2 = AudioSystem.getAudioInputStream(url2);
            intro = AudioSystem.getClip();
            intro.open(audioIn2);

            URL url3 = this.getClass().getClassLoader().getResource("explosion.wav");
            AudioInputStream audioIn3 = AudioSystem.getAudioInputStream(url3);
            explosion = AudioSystem.getClip();
            explosion.open(audioIn3);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        //theme.loop(1000);

        for (int x = 0; x < grid1.length; x++)
            for (int y = 0; y < grid1[x].length; y++)
            {
                grid1[x][y] = new waveOne(x, y);
        }
        for (int x = 0; x < grid2.length; x++)
            for (int y = 0; y < grid2[x].length; y++)
            {
                grid2[x][y] = new waveTwo(x, y);
        }
        for (int x = 0; x < grid3.length; x++)
            grid3[x] = new waveThree(x);

        startPic = Toolkit.getDefaultToolkit().getImage(getClass().getResource("start.png"));
        background = Toolkit.getDefaultToolkit().getImage(getClass().getResource("stars.png"));
        winner = Toolkit.getDefaultToolkit().getImage(getClass().getResource("winner.png"));
        gameover = Toolkit.getDefaultToolkit().getImage(getClass().getResource("gameover.png"));

        con.setLayout(new FlowLayout());
        addKeyListener(this);
        t.start();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void run()
    {
        try{
            while(true)
            {
                t.sleep(30);

                backY+=3;
                if (backY >= 0)
                    backY = -750;
                player.updateBulletX();
                for (int x = 0; x < grid1.length; x++)
                    for (int y = 0; y < grid1[x].length; y++)
                    {
                        grid1[x][y].updateBullet();
                }
                for (int x = 0; x < grid2.length; x++)
                    for (int y = 0; y < grid2[x].length; y++)
                    {
                        grid2[x][y].updateBullet();
                }
                for (int x = 0; x < grid3.length; x++)
                    grid3[x].updateBullet();

                if (start)
                {
                    intro.start();
                    startTimer = System.currentTimeMillis()+7000;
                    start = false;
                }
                if (startTimer > System.currentTimeMillis())
                    drawStart = true;
                else
                {
                    intro.stop();
                    drawStart = false;

                    for (int x = 0; x < grid1.length; x++)
                        for (int y = 0; y < grid1[x].length; y++)
                        {
                            if (grid1[x][y].flying())
                                grid1[x][y].increaseVel();
                    }
                    for (int x = 0; x < grid2.length; x++)
                        for (int y = 0; y < grid2[x].length; y++)
                        {
                            if (grid2[x][y].flying())
                                grid2[x][y].increaseVel();
                    }
                    for (int x = 0; x < grid3.length; x++)
                        if (grid3[x].flying())
                            grid3[x].increaseVel();

                    //RANDOM FIGHTER FALLING
                    if (goBee)
                        timer = System.currentTimeMillis()+1300+ran.nextInt(1000);

                    if (timer > System.currentTimeMillis())
                    {                    
                        goBee = false;
                    }
                    else
                    {
                        int chooseArray = ran.nextInt(3)+1;
                        int heGoesY = ran.nextInt(2);

                        //to make sure it doesn't tell dead bugs to fly
                        int urDead1 = 0; int urDead2 = 0; int urDead3 = 0;
                        int allDead = 0;
                        for (int x = 0; x < grid1.length; x++)
                            for (int y = 0; y < grid1[x].length; y++)
                            {
                                if (grid1[x][y].dead() || grid1[x][y].flying())
                                    urDead1++;
                                if (grid1[x][y].dead())
                                    allDead++;
                        }
                        for (int x = 0; x < grid2.length; x++)
                            for (int y = 0; y < grid2[x].length; y++)
                            {
                                if (grid2[x][y].dead() || grid2[x][y].flying())
                                    urDead2++;
                                if (grid2[x][y].dead())
                                    allDead++;
                        }
                        for (int x = 0; x < grid3.length; x++)
                        {
                            if (grid3[x].dead() || grid3[x].flying())
                                urDead3++;
                            if (grid3[x].dead())
                                allDead++;
                        }

                        //to make sure it doesn't go to arrays where everything is dead
                        if (urDead1==20)
                        {
                            if (allDead == 39)
                                chooseArray = 4;
                            else
                            {
                                chooseArray = 2;
                                if (urDead2 == 16)
                                {
                                    chooseArray = 3;
                                }
                            }
                        }
                        if (urDead2==16)
                        {
                            if (allDead == 39)
                                chooseArray = 4;
                            else
                            {
                                chooseArray = 3;
                                if (urDead3 == 4)
                                {
                                    chooseArray = 1;
                                }
                            }
                        }
                        if (urDead3==4)
                        {
                            if (allDead == 39)
                                chooseArray = 4;
                            else
                            {
                                chooseArray = 1;
                                if (urDead1 == 20)
                                {
                                    chooseArray = 2;
                                }
                            }
                        }
                        if (allDead == 40)
                            chooseArray = 5;

                        if (chooseArray == 1)
                        {
                            int heGoesX = ran.nextInt(10);
                            while (grid1[heGoesX][heGoesY].flying() || grid1[heGoesX][heGoesY].dead())
                            {
                                heGoesX = ran.nextInt(10);
                                heGoesY = ran.nextInt(2);
                            }
                            grid1[heGoesX][heGoesY].fly();
                        }
                        else if (chooseArray == 2)
                        {
                            int heGoesX = ran.nextInt(8);
                            while (grid2[heGoesX][heGoesY].flying() || grid2[heGoesX][heGoesY].dead())
                            {
                                heGoesX = ran.nextInt(8);
                                heGoesY = ran.nextInt(2);
                            }
                            grid2[heGoesX][heGoesY].fly();
                        }

                        else if (chooseArray == 3)
                        {
                            int heGoesX = ran.nextInt(4);
                            while (grid3[heGoesX].flying() || grid3[heGoesX].dead())
                                heGoesX = ran.nextInt(4);
                            grid3[heGoesX].fly();
                        }
                        else if (chooseArray == 4)
                        {}
                        else
                        {
                            WINNER = true;
                        }

                        goBee = true;
                    }
                }

                //COLLISION DETECTION
                for (int x = 0; x < grid1.length; x++)
                    for (int y = 0; y < grid1[x].length; y++)
                    {
                        grid1[x][y].move();
                        if (grid1[x][y].getRect().intersects(player.getBulletRect1()))
                        {
                            grid1[x][y].die();
                            player.resetBullet1();
                            score+=100;
                            explosion.loop(1);
                        }
                        if (grid1[x][y].getRect().intersects(player.getBulletRect2()))
                        {
                            grid1[x][y].die();
                            player.resetBullet2();
                            score+=100;
                            explosion.loop(1);
                        }
                        if (grid1[x][y].getRect().intersects(player.getRect()))
                        {
                            grid1[x][y].die();
                            player.die();
                            gameovah =true;
                        }

                        Rectangle waveOneRec[] = grid1[x][y].getBulletRec();
                        for (int indexRec = 0; indexRec < waveOneRec.length; indexRec++)
                        {
                            if (player.getRect().intersects(waveOneRec[indexRec]))
                            {
                                player.die();
                                gameovah =true;
                            }
                        }
                }    
                for (int x = 0; x < grid2.length; x++)
                    for (int y = 0; y < grid2[x].length; y++)
                    {
                        grid2[x][y].move();
                        if (grid2[x][y].getRect().intersects(player.getBulletRect1()))
                        {
                            grid2[x][y].die();
                            player.resetBullet1();
                            score+=100;
                            explosion.loop(1);
                        }
                        if (grid2[x][y].getRect().intersects(player.getBulletRect2()))
                        {
                            grid2[x][y].die();
                            player.resetBullet2();
                            score+=100;
                            explosion.loop(1);
                        }
                        if (grid2[x][y].getRect().intersects(player.getRect()))
                        {
                            grid2[x][y].die();
                            player.die();
                            gameovah =true;
                        }

                        Rectangle waveTwoRec[] = grid2[x][y].getBulletRec();
                        for (int indexRec = 0; indexRec < waveTwoRec.length; indexRec++)
                        {
                            if (player.getRect().intersects(waveTwoRec[indexRec]))
                            {
                                player.die();
                                gameovah =true;
                            }
                        }
                }
                for (int x = 0; x < grid3.length; x++)
                {
                    grid3[x].move();
                    if (grid3[x].getRect().intersects(player.getBulletRect1()))
                    {
                        grid3[x].die();
                        player.resetBullet1();
                        score+=100;
                        explosion.loop(1);
                    }
                    if (grid3[x].getRect().intersects(player.getBulletRect2()))
                    {
                        grid3[x].die();
                        player.resetBullet2();
                        score+=100;
                        explosion.loop(1);
                    }
                    if (grid3[x].getRect().intersects(player.getRect()))
                    {
                        grid3[x].die();
                        player.die();
                        gameovah =true;
                    }

                    Rectangle waveThreeRec[] = grid3[x].getBulletRec();
                    for (int indexRec = 0; indexRec < waveThreeRec.length; indexRec++)
                    {
                        if (player.getRect().intersects(waveThreeRec[indexRec]))
                        {
                            player.die();
                            gameovah =true;
                        }
                    }
                }

                //MOVE SIDE TO SIDE
                if (grid1[0][0].getX() < 75)
                {
                    for (int x = 0; x < grid1.length; x++)
                        for (int y = 0; y < grid1[x].length; y++)
                        {
                            grid1[x][y].switchVel();
                    }
                    for (int x = 0; x < grid2.length; x++)
                        for (int y = 0; y < grid2[x].length; y++)
                        {
                            grid2[x][y].switchVel();
                    }
                    for (int x = 0; x < grid3.length; x++)
                        grid3[x].switchVel();
                }
                else if (grid1[0][0].getX() > 175)
                {
                    for (int x = 0; x < grid1.length; x++)
                        for (int y = 0; y < grid1[x].length; y++)
                        {
                            grid1[x][y].switchVel();
                    }
                    for (int x = 0; x < grid2.length; x++)
                        for (int y = 0; y < grid2[x].length; y++)
                        {
                            grid2[x][y].switchVel();
                    }
                    for (int x = 0; x < grid3.length; x++)
                        grid3[x].switchVel();
                }
                repaint();
            }
        }

        catch(Exception e){}
    }

    public void update(Graphics g)
    {
        paint(g);
    } 

    public void paint(Graphics gr)
    {
        Image i=createImage(getSize().width, getSize().height);
        Graphics2D g2 = (Graphics2D)i.getGraphics();
        g2.drawImage(background, 0, backY, this);
        player.draw(g2);

        for (int x = 0; x < grid1.length; x++)
            for (int y = 0; y < grid1[x].length; y++)
            {
                grid1[x][y].draw(g2);
        }
        for (int x = 0; x < grid2.length; x++)
            for (int y = 0; y < grid2[x].length; y++)
            {
                grid2[x][y].draw(g2);
        }
        for (int x = 0; x < grid3.length; x++)
            grid3[x].draw(g2);

        if (drawStart)
            g2.drawImage(startPic, 55, 125, this);
        else if (WINNER)
            g2.drawImage(winner, 0, 0, this);
        else if (gameovah)
            g2.drawImage(gameover, 0, 0, this);
        g2.setColor(Color.white);
        g2.drawString("Score: "+score, 50, 50);

        g2.dispose();
        gr.drawImage(i, 0, 0, this);
    }

    public void keyPressed(KeyEvent e)
    {  
        if (e.getKeyCode() == 65 && drawStart == false)
        {
            player.moveLeft();
            if (gameovah || WINNER)
                System.exit(0);
        }
        if (e.getKeyCode() == 68 && drawStart == false)
            player.moveRight();
        if (e.getKeyCode() == 87 && drawStart == false)
        {
            shoot.loop(1);
            player.fire();
        }
        //if (e.getKeyCode() == 77 && mute == 0)
        //mute = 1;
    }

    public void keyReleased(KeyEvent e)
    {
        if (e.getKeyCode() == 65)
            player.stop();
        if (e.getKeyCode() == 68)
            player.stop();
    }

    public void keyTyped(KeyEvent e)
    {
    }

    public static void main(String[] args)
    {
        Driver frame = new Driver();
        frame.setSize(750, 750);
        frame.setVisible(true);
    }
}

