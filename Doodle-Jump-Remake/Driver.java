import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Driver extends JFrame implements Runnable, MouseMotionListener {
    Container con = getContentPane();
    Thread t = new Thread(this);
    Image player, platform, background;

    Platform platforms[] = new Platform[22];
    Rectangle pageRec;
    Doodle jumper;
    Random ran = new Random();
    int doodleVel = 15, flag = 0, restart = 0;
    int score = 0, highscore = 0;
    int beginning = 1;

    public Driver() {
        addMouseMotionListener(this);
        jumper = new Doodle();
        platforms[0] = new Platform(ran.nextInt(450), 0, 50, 10);
        platforms[1] = new Platform(ran.nextInt(450), 40, 50, 10);
        platforms[2] = new Platform(ran.nextInt(450), 50, 50, 10);
        platforms[3] = new Platform(ran.nextInt(450), 80, 50, 10);
        platforms[4] = new Platform(ran.nextInt(450), 100, 50, 10);
        platforms[5] = new Platform(ran.nextInt(450), 160, 50, 10);
        platforms[6] = new Platform(ran.nextInt(450), 175, 50, 10);
        platforms[7] = new Platform(ran.nextInt(450), 200, 50, 10);
        platforms[8] = new Platform(ran.nextInt(450), 240, 50, 10);
        platforms[9] = new Platform(ran.nextInt(450), 275, 50, 10);
        platforms[10] = new Platform(ran.nextInt(450), 320, 50, 10);
        platforms[11] = new Platform(ran.nextInt(450), 335, 50, 10);
        platforms[12] = new Platform(ran.nextInt(450), 360, 50, 10);
        platforms[13] = new Platform(ran.nextInt(450), 400, 50, 10);
        platforms[14] = new Platform(ran.nextInt(450), 440, 50, 10);
        platforms[15] = new Platform(ran.nextInt(450), 480, 50, 10);
        platforms[16] = new Platform(ran.nextInt(450), 520, 50, 10);
        platforms[17] = new Platform(ran.nextInt(450), 560, 50, 10);
        platforms[18] = new Platform(ran.nextInt(450), 600, 50, 10);
        platforms[19] = new Platform(ran.nextInt(450), 635, 50, 10);
        platforms[20] = new Platform(ran.nextInt(450), 680, 50, 10);
        platforms[21] = new Platform(ran.nextInt(450), 735, 50, 10);

        player = Toolkit.getDefaultToolkit().getImage(getClass().getResource("player.png"));
        player = player.getScaledInstance(50, 50, 1);
        platform = Toolkit.getDefaultToolkit().getImage(getClass().getResource("platform.png"));
        platform = platform.getScaledInstance(50, 10, 1);
        background = Toolkit.getDefaultToolkit().getImage(getClass().getResource("background.png"));
        background = background.getScaledInstance(500, 750, 1);

        pageRec = new Rectangle(0, 0, 500, 750);
        con.setLayout(new FlowLayout());
        t.start();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void run() {
        try {
            while (true) {
                t.sleep(30);

                jumper.bounce(doodleVel);
                if (beginning == 1) {
                    JOptionPane.showMessageDialog(null,
                            "WELCOME TO OBJECT ORIENTED DOODLE JUMP! Move the mouse about the screen to control your character.");
                    beginning = 0;
                }

                for (int index = 0; index < platforms.length; index++) {
                    platforms[index].down();
                    if (!pageRec.contains(platforms[index].getRect())) {
                        platforms[index].reset();
                    }
                    if ((jumper.getRect().intersects(platforms[index].getRect())) && doodleVel > 0) {
                        doodleVel = -12;
                        flag = 1;
                        score++;
                        if (score > highscore)
                            highscore = score;
                    }
                }
                if (flag == 1)
                    doodleVel++;
                if (!pageRec.intersects(jumper.getRect()) && jumper.getY() > 0) {
                    JOptionPane.showMessageDialog(null,
                            "GAME OVER. Your score was " + score + ". Press OK to play again.");
                    restart = 1;
                }
                if (restart == 1) {
                    jumper.reset();
                    score = 0;
                    doodleVel = 15;
                    restart = 0;
                }
                repaint();
            }
        } catch (Exception e) {
        }
    }

    public void update(Graphics g) {
        paint(g);
    }

    public void paint(Graphics gr) {
        Image i = createImage(getSize().width, getSize().height);
        Graphics2D g2 = (Graphics2D) i.getGraphics();
        g2.drawImage(background, 0, 0, this);

        for (int index = 0; index < platforms.length; index++) {
            g2.drawImage(platform, platforms[index].getX(), platforms[index].getY(), this);
        }
        g2.drawImage(player, jumper.getX(), jumper.getY(), this);

        g2.setColor(Color.black);
        g2.drawString("Score: " + score + "  Highscore: " + highscore, 30, 50);

        g2.dispose();
        gr.drawImage(i, 0, 0, this);
    }

    public void mouseMoved(MouseEvent me) {
        int xChange = me.getX();
        jumper.move(xChange);
    }

    public void mouseDragged(MouseEvent me) {
    }

    public static void main(String[] args) {
        Driver frame = new Driver();
        frame.setSize(500, 750);
        frame.setVisible(true);
    }
}