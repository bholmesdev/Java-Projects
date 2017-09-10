import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
public class Driver extends JFrame implements Runnable, KeyListener
{
	private ArrayList<Platform> plats = new ArrayList<Platform>();
	Player one, two;
	private int counter, limit, counterMoveDown, tSleep, score, highScore;
	private boolean done, oneRotating, twoRotating, moving;
	Scanner sc;
	Container con = getContentPane();
	Thread t = new Thread(this);
	public Driver()
	{
		con.setLayout(new FlowLayout());
		t.start();
		counter = -1;
		counterMoveDown = 0;
		score = 0;
		highScore = 0;
		limit = 0;
		tSleep = 7;
		done = true;
		oneRotating = false;
		twoRotating = true;
		moving = false;
		plats.add(new Platform(new Point(160, 550), new Point(340, 550), 1, 0));		
		one = new Player(plats.get(0).getPointOne(), plats.get(0).getPointTwo(), true);
		two = new Player(plats.get(0).getPointOne(), plats.get(0).getPointTwo(), false);
		JOptionPane.showMessageDialog(null, "--------WELCOME TO SKYWARD--------\nHit the spacebar when the rotating circle is\nabove the next platform. If you miss, it's \ngame over!\nPS: The speed increases as time progresses");
		for(int i=1; i<15; i++) {
			int chooseType = (int)(Math.random()*9);
			if(chooseType < 6)
				plats.add(new Platform(plats.get(i-1).getPointOne(), plats.get(i-1).getPointTwo(), 1, i));
			else
				plats.add(new Platform(plats.get(i-1).getPointOne(), plats.get(i-1).getPointTwo(), 2, i));
		}
		//for saving highscore to a document to be read in at start of program
		sc = new Scanner(System.in);
		try {
			sc = new Scanner(new FileReader("highscore.txt"));
			highScore = Integer.parseInt(sc.nextLine());
		}
		catch(Exception e) {
			highScore = 0;
		}
		sc.close();
		addKeyListener(this);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	public void run()
	{
		try{
			while(true)
			{
				t.sleep(tSleep);
				if(moving == false) {
					if(twoRotating)
						two.move(one.getCenter(), one.getWidth(), one.getHeight());
					else
						one.move(two.getCenter(), two.getWidth(), two.getHeight());
				}
				if(counter >= 0 && counter < limit) {
					moving = true;
					
					//decides whether the stage should move down or forward given what platform is in front
					if((plats.get(0).getType() == 2 && plats.get(1).getType() == 2) || (plats.get(0).getType() == 2 && plats.get(1).getType() == 1))
						counter = limit;
					
					//the first platform moves so all other platforms may be based of of its location
					plats.get(0).moveFirst();
					for(int i=1; i<plats.size(); i++) {
						plats.get(i).move(plats.get(i-1).getPointOne(), plats.get(i-1).getPointTwo(), counter);
					}

					//to update the position of the circles
					if(oneRotating) {
						one.updatePos(plats.get(1).getPointOne(), plats.get(1).getWidth(), plats.get(1).getHeight(), plats.get(1).getType());
						two.updatePos(plats.get(0).getPointOne(), plats.get(0).getWidth(), plats.get(0).getHeight(), plats.get(0).getType());
					}
					else {
						one.updatePos(plats.get(0).getPointOne(), plats.get(0).getWidth(), plats.get(0).getHeight(), plats.get(0).getType());
						two.updatePos(plats.get(1).getPointOne(), plats.get(1).getWidth(), plats.get(1).getHeight(), plats.get(1).getType());
					}
					counter++;
				}
				//once the platforms are done moving forward
				if(counter >= limit) {
					plats.get(1).setIndex(0);
					plats.get(0).die();
					//decides whether the stage should move down
					if(((plats.get(1).getType() == 2 && plats.get(0).getType() == 2) || plats.get(0).getType() == 2) && counterMoveDown < 18) {
						for(Platform i: plats) {
							i.moveDown(counterMoveDown, 10);
							if(counterMoveDown == 1)
								i.moveDown(counterMoveDown, 7);
						}
						//updates circle position
						if(oneRotating) {
							one.updatePos(plats.get(1).getPointOne(), plats.get(1).getWidth(), plats.get(1).getHeight(), plats.get(1).getType());
							two.updatePos(plats.get(0).getPointOne(), plats.get(0).getWidth(), plats.get(0).getHeight(), plats.get(0).getType());
						}
						else {
							one.updatePos(plats.get(0).getPointOne(), plats.get(0).getWidth(), plats.get(0).getHeight(), plats.get(0).getType());
							two.updatePos(plats.get(1).getPointOne(), plats.get(1).getWidth(), plats.get(1).getHeight(), plats.get(1).getType());
						}
						counterMoveDown++;
					}
					else {
						counterMoveDown = 20;
					}
					
					//for determining of the circle should be horizontal or vertical in shape
					if(plats.get(0).getType() != plats.get(1).getType()) {
						if(oneRotating)
							two.changeShape(plats.get(1).getType());
						else
							one.changeShape(plats.get(1).getType());
					}

					//for removing the first platform
					if(plats.get(0).die() == true && counterMoveDown == 20) {
						counterMoveDown = 0;
						plats.remove(0);
						for(int i=0; i<plats.size(); i++)
							plats.get(i).setIndex(i);
						int chooseType = (int)(Math.random()*9);
						if(chooseType < 6)
							plats.add(new Platform(plats.get(plats.size()-1).getPointOne(), plats.get(plats.size()-1).getPointTwo(), 1, plats.size()));
						else {
							if(plats.get(plats.size()-2).getType() == 2 && plats.get(plats.size()-1).getType() == 2)
								plats.add(new Platform(plats.get(plats.size()-1).getPointOne(), plats.get(plats.size()-1).getPointTwo(), 1, plats.size()));
							else
								plats.add(new Platform(plats.get(plats.size()-1).getPointOne(), plats.get(plats.size()-1).getPointTwo(), 2, plats.size()));
						}
						counter = -1;
						limit = 0;
						done = true;
						moving = false;

						//for changing which circle rotates
						if(oneRotating) {
							two.reset();
							twoRotating = true;
							oneRotating = false;
						}
						else {
							one.reset();
							oneRotating = true;
							twoRotating = false;
						}
					}
				}
				repaint();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public void update(Graphics g)
	{
		paint(g);
	} 
	public void paint(Graphics gr)
	{
		Image i=createImage(getSize().width, getSize().height);
		Graphics2D g2 = (Graphics2D)i.getGraphics();
		GradientPaint background = new GradientPaint(0, 0, new Color(179, 54, 80), 0, 500, new Color(212, 153, 76));
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setPaint(background);
		g2.fillRect(0, 0, 500, 700);
		for(int index = plats.size()-1; index>=0; index--) {
			plats.get(index).draw(g2);
		}
		one.draw(g2);
		two.draw(g2);
		g2.setColor(Color.white);
		g2.drawString("Score "+score, 10, 40);
		g2.drawString("HighScore "+highScore, 10, 60);
		g2.dispose();
		gr.drawImage(i, 0, 0, this);
	}
	public void resetGame() {
		plats.clear();
		counter = -1;
		counterMoveDown = 0;
		limit = 0;
		score = 0;
		tSleep = 7;
		done = true;
		oneRotating = false;
		twoRotating = true;
		moving = false;
		plats.add(new Platform(new Point(160, 550), new Point(340, 550), 1, 0));		
		one = new Player(plats.get(0).getPointOne(), plats.get(0).getPointTwo(), true);
		two = new Player(plats.get(0).getPointOne(), plats.get(0).getPointTwo(), false);
		for(int i=1; i<15; i++) {
			int chooseType = (int)(Math.random()*9);
			if(chooseType < 6)
				plats.add(new Platform(plats.get(i-1).getPointOne(), plats.get(i-1).getPointTwo(), 1, i));
			else
				plats.add(new Platform(plats.get(i-1).getPointOne(), plats.get(i-1).getPointTwo(), 2, i));
		}
	}
	public static void main(String[] args)
	{
		Driver frame = new Driver();
		frame.setSize(500, 700);
		frame.setVisible(true);
	}
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == 32) {
			if(done == true && (plats.get(1).getRect().intersects(one.getRect()) || plats.get(1).getRect().intersects(two.getRect()))) {
				counter = 0;
				limit=30;
				if(score%13 == 12 && tSleep > 2) {
					tSleep--;
				}
				if(plats.get(1).getRect().intersects(one.getRect()))
					oneRotating = true;
				else
					twoRotating = true;
				done = false;
				score++;
				if(score > highScore)
					highScore = score;
			}
			else {
				JOptionPane.showMessageDialog(null, "-GAME OVER-\nHit OK to reset");
				try {
					//SAVES HIGHSCORE TO A TXT FILE FOR READING AT START OF GAME
					FileWriter data = new FileWriter("highscore.txt");
					BufferedWriter out = new BufferedWriter(data);
					out.write(""+highScore);
					out.close();
				}
				catch(Exception error) {
					System.err.println("Error message: "+error);
				}
				resetGame();
			}
		}
	}
	public void keyReleased(KeyEvent e) {

	}
	public void keyTyped(KeyEvent e) {

	}
}