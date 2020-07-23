package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class LifeMain extends JPanel implements ActionListener{
	
	public static final int SCALE = 16;//16
	public static final int WIDTH = 50;//50
	public static final int HEIGHT = 30;//30
	
	public static int generations = 1;
	public static int lifeTime = 0;
	public static int lastLifeTime = 0;
	
	public static int[][] map = new int[WIDTH][HEIGHT];
	
	static Bot[] bots = new Bot[64];
	
	Timer t = new Timer(10, this);
	static Random r = new Random();
	
	Font fontLife = new Font("Arial",Font.BOLD,12);
	Font fontGeneration = new Font("Arial",Font.BOLD,16);
	
	public LifeMain()
	{
		StartMap();
		t.start();
		setFocusable(true);
	}
	
	public static void main(String[] args)
	{
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setResizable(false);
		f.setSize(WIDTH*SCALE+100, HEIGHT*SCALE+100);
		f.setLocationRelativeTo(null);
		f.add(new LifeMain());
		f.setVisible(true);
	}
	
	public static void StartMap()
	{
		for (int i=0; i<WIDTH; i++)
		{
			for (int j=0; j<HEIGHT; j++)
			{
				map[i][j] = 0;
			}
		}
		for (int i=0; i<WIDTH; i++)
		{
			map[i][0] = 2;
			map[i][HEIGHT-1] = 2;
		}
		for (int i=0; i<HEIGHT; i++)
		{
			map[0][i] = 2;
			map[WIDTH-1][i] = 2;
		}
		int temp1=0, temp2=0;
		for (int i=0; i<64; i++)
		{
			while (map[temp1][temp2]!=0)
			{
				temp1 = r.nextInt(WIDTH);
				temp2 = r.nextInt(HEIGHT);
				if (map[temp1][temp2]==0)
				{
					bots[i] = new Bot(temp1,temp2);
					map[temp1][temp2] = 1;
					break;
				}
			}
		}
		temp1=0;
		temp2=0;
		for (int i=0; i<100; i++)
		{
			while (map[temp1][temp2]!=0)
			{
				temp1 = r.nextInt(WIDTH);
				temp2 = r.nextInt(HEIGHT);
				if (map[temp1][temp2]==0)
				{
					map[temp1][temp2] = 3;
					break;
				}
			}
		}
		temp1=0;
		temp2=0;
		for (int i=0; i<100; i++)
		{
			while (map[temp1][temp2]!=0)
			{
				temp1 = r.nextInt(WIDTH);
				temp2 = r.nextInt(HEIGHT);
				if (map[temp1][temp2]==0)
				{
					map[temp1][temp2] = 4;
					break;
				}
			}
		}
	}
	
	private Color color(int red, int green, int blue)
	{
		return new Color(red,green,blue);
	}
	
	public void paint(Graphics g)
	{
		g.setColor(color(30,30,30));
		g.fillRect(0, 0, WIDTH*SCALE+100, HEIGHT*SCALE+100);
		g.setColor(color(0,0,0));
		g.fillRect(20,20,WIDTH*SCALE, HEIGHT*SCALE);
		g.setColor(color(130,130,130));
		for(int xx = 20;xx<=WIDTH*SCALE+20;xx+=SCALE){
			g.drawLine(xx, 20, xx,SCALE*HEIGHT+20);
		}
		for(int yy = 20;yy<=HEIGHT*SCALE+20;yy+=SCALE){
			g.drawLine(20, yy, WIDTH*SCALE+20,yy);
		}
		for (int i=0; i<WIDTH; i++)
		{
			for (int j=0; j<HEIGHT; j++)
			{
				if (map[i][j]==0)
				{
					g.setColor(color(0,0,0));
					g.fillRect(i*SCALE+21, j*SCALE+21, SCALE-1, SCALE-1);
				}
				if (map[i][j]==1)
				{
					g.setColor(color(0,0,220));
					g.fillRect(i*SCALE+21, j*SCALE+21, SCALE-1, SCALE-1);
				}
				if (map[i][j]==2)
				{
					g.setColor(color(100,100,100));
					g.fillRect(i*SCALE+21, j*SCALE+21, SCALE-1, SCALE-1);
				}
				if (map[i][j]==3)
				{
					g.setColor(color(0,220,0));
					g.fillRect(i*SCALE+21, j*SCALE+21, SCALE-1, SCALE-1);
				}
				if (map[i][j]==4)
				{
					g.setColor(color(220,0,0));
					g.fillRect(i*SCALE+21, j*SCALE+21, SCALE-1, SCALE-1);
				}
			}
		}
		g.setFont(fontLife);
		g.setColor(color(255,255,255));
		for (int i=0; i<64; i++)
		{
			if (bots[i].life>0)
			{
				if (bots[i].life>9) g.drawString(Integer.toString(bots[i].life), bots[i].botX*SCALE+22, bots[i].botY*SCALE+33);
				else g.drawString(Integer.toString(bots[i].life), bots[i].botX*SCALE+26, bots[i].botY*SCALE+33);
			}
		}
		g.setColor(color(0,100,50));
		g.fillRect(WIDTH*SCALE-100, HEIGHT*SCALE+30, 100, 25);
		g.setFont(fontGeneration);
		g.setColor(color(255,255,255));
		g.drawString(Integer.toString(generations), WIDTH*SCALE-70, HEIGHT*SCALE+47);
		g.setColor(color(50,200,100));
		g.fillRect(WIDTH*SCALE-250, HEIGHT*SCALE+30, 100, 25);
		g.setFont(fontGeneration);
		g.setColor(color(255,255,255));
		g.drawString(Integer.toString(lastLifeTime), WIDTH*SCALE-240, HEIGHT*SCALE+47);
	}
	
	public void actionPerformed(ActionEvent arg0)
	{
		lifeTime++;
		for (int i=0; i<64; i++)
		{
			int sum=0;
			for (int r=0; r<64; r++)
			{
				if (bots[r].life>0) sum++;
			}
			if (sum==8)
			{
				NextGeneration();
				break;
			}
			
			if (bots[i].life>0)
			{
				for (int j=0; j<10; j++)
				{
					if (bots[i].mind[bots[i].pointer]<8)
					{
						BotMove(i);
						repaint();
						break;
					}
					else if (bots[i].mind[bots[i].pointer]<16)
					{
						BotTake(i);
						repaint();
						break;
					}
					else if (bots[i].mind[bots[i].pointer]<24)
					{
						BotSee(i);
					}
					else if (bots[i].mind[bots[i].pointer]<32)
					{
						BotRotate(i);
					}
					else
					{
						BotGoTo(i);
					}
				}
				bots[i].life--;
				if (bots[i].life<=0)
				{
					map[bots[i].botX][bots[i].botY] = 0;
				}
			}
		}
	}
	
	public void NextGeneration()
	{
		int[][] best = new int[8][64];
		int n=0;
		for (int i=0; i<64; i++)
		{
			if (bots[i].life>0)
			{
				for (int j=0; j<64; j++)
				{
					best[n][j] = bots[i].mind[j];
				}
				n++;
			}
		}
		StartMap();
		for (int i=0; i<8; i++)
		{
			for (int j=0; j<8; j++)
			{
				for (int m=0; m<64; m++)
				{
					bots[i*8+j].mind[m] = best[i][m];
				}
			}
		}
		for (int i=0; i<8; i++)
		{
			bots[i*8+7].mind[r.nextInt(64)] = r.nextInt(64);
		}
		generations++;
		lastLifeTime = lifeTime;
		lifeTime = 0;
		repaint();
	}
	
	public void Spawn(int n)
	{
		int temp1=0, temp2=0;
		while (map[temp1][temp2]!=0)
		{
			temp1 = r.nextInt(WIDTH);
			temp2 = r.nextInt(HEIGHT);
			if (map[temp1][temp2]==0)
			{
				map[temp1][temp2] = n;
				break;
			}
		}
	}
	
	public void BotMove(int i)
	{
		int h = bots[i].angle+bots[i].mind[bots[i].pointer];
		if (h>7) h -= 8;
		if (h==0)
		{
			if (map[bots[i].botX][bots[i].botY-1]==0)
			{
				map[bots[i].botX][bots[i].botY-1] = 1;
				map[bots[i].botX][bots[i].botY] = 0;
				bots[i].botY--;
				bots[i].pointer += 5;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX][bots[i].botY-1]==1)
			{
				bots[i].pointer += 3;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX][bots[i].botY-1]==2)
			{
				bots[i].pointer += 2;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX][bots[i].botY-1]==3)
			{
				map[bots[i].botX][bots[i].botY-1] = 1;
				map[bots[i].botX][bots[i].botY] = 0;
				Spawn(3);
				bots[i].botY--;
				bots[i].life += 10;
				bots[i].pointer += 4;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX][bots[i].botY-1]==4)
			{
				map[bots[i].botX][bots[i].botY-1] = 0;
				map[bots[i].botX][bots[i].botY] = 0;
				Spawn(4);
				bots[i].life = 0;
				bots[i].pointer += 1;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
		}
		if (h==1)
		{
			if (map[bots[i].botX+1][bots[i].botY-1]==0)
			{
				map[bots[i].botX+1][bots[i].botY-1] = 1;
				map[bots[i].botX][bots[i].botY] = 0;
				bots[i].botY--;
				bots[i].botX++;
				bots[i].pointer += 5;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX+1][bots[i].botY-1]==1)
			{
				bots[i].pointer += 3;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX+1][bots[i].botY-1]==2)
			{
				bots[i].pointer += 2;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX+1][bots[i].botY-1]==3)
			{
				map[bots[i].botX+1][bots[i].botY-1] = 1;
				map[bots[i].botX][bots[i].botY] = 0;
				Spawn(3);
				bots[i].botY--;
				bots[i].botX++;
				bots[i].life += 10;
				bots[i].pointer += 4;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX+1][bots[i].botY-1]==4)
			{
				map[bots[i].botX+1][bots[i].botY-1] = 0;
				map[bots[i].botX][bots[i].botY] = 0;
				Spawn(4);
				bots[i].life = 0;
				bots[i].pointer += 1;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
		}
		if (h==2)
		{
			if (map[bots[i].botX+1][bots[i].botY]==0)
			{
				map[bots[i].botX+1][bots[i].botY] = 1;
				map[bots[i].botX][bots[i].botY] = 0;
				bots[i].botX++;
				bots[i].pointer += 5;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX+1][bots[i].botY]==1)
			{
				bots[i].pointer += 3;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX+1][bots[i].botY]==2)
			{
				bots[i].pointer += 2;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX+1][bots[i].botY]==3)
			{
				map[bots[i].botX+1][bots[i].botY] = 1;
				map[bots[i].botX][bots[i].botY] = 0;
				Spawn(3);
				bots[i].botX++;
				bots[i].life += 10;
				bots[i].pointer += 4;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX+1][bots[i].botY]==4)
			{
				map[bots[i].botX+1][bots[i].botY] = 0;
				map[bots[i].botX][bots[i].botY] = 0;
				Spawn(4);
				bots[i].life = 0;
				bots[i].pointer += 1;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
		}
		if (h==3)
		{
			if (map[bots[i].botX+1][bots[i].botY+1]==0)
			{
				map[bots[i].botX+1][bots[i].botY+1] = 1;
				map[bots[i].botX][bots[i].botY] = 0;
				bots[i].botY++;
				bots[i].botX++;
				bots[i].pointer += 5;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX+1][bots[i].botY+1]==1)
			{
				bots[i].pointer += 3;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX+1][bots[i].botY+1]==2)
			{
				bots[i].pointer += 2;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX+1][bots[i].botY+1]==3)
			{
				map[bots[i].botX+1][bots[i].botY+1] = 1;
				map[bots[i].botX][bots[i].botY] = 0;
				Spawn(3);
				bots[i].botY++;
				bots[i].botX++;
				bots[i].life += 10;
				bots[i].pointer += 4;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX+1][bots[i].botY+1]==4)
			{
				map[bots[i].botX+1][bots[i].botY+1] = 0;
				map[bots[i].botX][bots[i].botY] = 0;
				Spawn(4);
				bots[i].life = 0;
				bots[i].pointer += 1;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
		}
		if (h==4)
		{
			if (map[bots[i].botX][bots[i].botY+1]==0)
			{
				map[bots[i].botX][bots[i].botY+1] = 1;
				map[bots[i].botX][bots[i].botY] = 0;
				bots[i].botY++;
				bots[i].pointer += 5;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX][bots[i].botY+1]==1)
			{
				bots[i].pointer += 3;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX][bots[i].botY+1]==2)
			{
				bots[i].pointer += 2;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX][bots[i].botY+1]==3)
			{
				map[bots[i].botX][bots[i].botY+1] = 1;
				map[bots[i].botX][bots[i].botY] = 0;
				Spawn(3);
				bots[i].botY++;
				bots[i].life += 10;
				bots[i].pointer += 4;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX][bots[i].botY+1]==4)
			{
				map[bots[i].botX][bots[i].botY+1] = 0;
				map[bots[i].botX][bots[i].botY] = 0;
				Spawn(4);
				bots[i].life = 0;
				bots[i].pointer += 1;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
		}
		if (h==5)
		{
			if (map[bots[i].botX-1][bots[i].botY+1]==0)
			{
				map[bots[i].botX-1][bots[i].botY+1] = 1;
				map[bots[i].botX][bots[i].botY] = 0;
				bots[i].botY++;
				bots[i].botX--;
				bots[i].pointer += 5;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX-1][bots[i].botY+1]==1)
			{
				bots[i].pointer += 3;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX-1][bots[i].botY+1]==2)
			{
				bots[i].pointer += 2;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX-1][bots[i].botY+1]==3)
			{
				map[bots[i].botX-1][bots[i].botY+1] = 1;
				map[bots[i].botX][bots[i].botY] = 0;
				Spawn(3);
				bots[i].botY++;
				bots[i].botX--;
				bots[i].life += 10;
				bots[i].pointer += 4;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX-1][bots[i].botY+1]==4)
			{
				map[bots[i].botX-1][bots[i].botY+1] = 0;
				map[bots[i].botX][bots[i].botY] = 0;
				Spawn(4);
				bots[i].life = 0;
				bots[i].pointer += 1;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
		}
		if (h==6)
		{
			if (map[bots[i].botX-1][bots[i].botY]==0)
			{
				map[bots[i].botX-1][bots[i].botY] = 1;
				map[bots[i].botX][bots[i].botY] = 0;
				bots[i].botX--;
				bots[i].pointer += 5;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX-1][bots[i].botY]==1)
			{
				bots[i].pointer += 3;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX-1][bots[i].botY]==2)
			{
				bots[i].pointer += 2;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX-1][bots[i].botY]==3)
			{
				map[bots[i].botX-1][bots[i].botY] = 1;
				map[bots[i].botX][bots[i].botY] = 0;
				Spawn(3);
				bots[i].botX--;
				bots[i].life += 10;
				bots[i].pointer += 4;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX-1][bots[i].botY]==4)
			{
				map[bots[i].botX-1][bots[i].botY] = 0;
				map[bots[i].botX][bots[i].botY] = 0;
				Spawn(4);
				bots[i].life = 0;
				bots[i].pointer += 1;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
		}
		if (h==7)
		{
			if (map[bots[i].botX-1][bots[i].botY-1]==0)
			{
				map[bots[i].botX-1][bots[i].botY-1] = 1;
				map[bots[i].botX][bots[i].botY] = 0;
				bots[i].botY--;
				bots[i].botX--;
				bots[i].pointer += 5;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX-1][bots[i].botY-1]==1)
			{
				bots[i].pointer += 3;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX-1][bots[i].botY-1]==2)
			{
				bots[i].pointer += 2;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX-1][bots[i].botY-1]==3)
			{
				map[bots[i].botX-1][bots[i].botY-1] = 1;
				map[bots[i].botX][bots[i].botY] = 0;
				Spawn(3);
				bots[i].botY--;
				bots[i].botX--;
				bots[i].life += 10;
				bots[i].pointer += 4;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX-1][bots[i].botY-1]==4)
			{
				map[bots[i].botX-1][bots[i].botY-1] = 0;
				map[bots[i].botX][bots[i].botY] = 0;
				Spawn(4);
				bots[i].life = 0;
				bots[i].pointer += 1;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
		}
	}
	
	public void BotTake(int i)
	{
		int h = bots[i].angle + bots[i].mind[bots[i].pointer] - 8;
		if (h>7) h -= 8;
		if (h==0)
		{
			if (map[bots[i].botX][bots[i].botY-1]==0)
			{
				bots[i].pointer += 5;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX][bots[i].botY-1]==1)
			{
				bots[i].pointer += 3;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX][bots[i].botY-1]==2)
			{
				bots[i].pointer += 2;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX][bots[i].botY-1]==3)
			{
				map[bots[i].botX][bots[i].botY-1] = 0;
				Spawn(3);
				bots[i].life += 10;
				bots[i].pointer += 4;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX][bots[i].botY-1]==4)
			{
				map[bots[i].botX][bots[i].botY-1] = 3;
				Spawn(4);
				bots[i].pointer += 1;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
		}
		if (h==1)
		{
			if (map[bots[i].botX+1][bots[i].botY-1]==0)
			{
				bots[i].pointer += 5;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX+1][bots[i].botY-1]==1)
			{
				bots[i].pointer += 3;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX+1][bots[i].botY-1]==2)
			{
				bots[i].pointer += 2;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX+1][bots[i].botY-1]==3)
			{
				map[bots[i].botX+1][bots[i].botY-1] = 0;
				Spawn(3);
				bots[i].life += 10;
				bots[i].pointer += 4;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX+1][bots[i].botY-1]==4)
			{
				map[bots[i].botX+1][bots[i].botY-1] = 3;
				Spawn(4);
				bots[i].pointer += 1;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
		}
		if (h==2)
		{
			if (map[bots[i].botX+1][bots[i].botY]==0)
			{
				bots[i].pointer += 5;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX+1][bots[i].botY]==1)
			{
				bots[i].pointer += 3;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX+1][bots[i].botY]==2)
			{
				bots[i].pointer += 2;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX+1][bots[i].botY]==3)
			{
				map[bots[i].botX+1][bots[i].botY] = 0;
				Spawn(3);
				bots[i].life += 10;
				bots[i].pointer += 4;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX+1][bots[i].botY]==4)
			{
				map[bots[i].botX+1][bots[i].botY] = 3;
				Spawn(4);
				bots[i].pointer += 1;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
		}
		if (h==3)
		{
			if (map[bots[i].botX+1][bots[i].botY+1]==0)
			{
				bots[i].pointer += 5;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX+1][bots[i].botY+1]==1)
			{
				bots[i].pointer += 3;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX+1][bots[i].botY+1]==2)
			{
				bots[i].pointer += 2;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX+1][bots[i].botY+1]==3)
			{
				map[bots[i].botX+1][bots[i].botY+1] = 0;
				Spawn(3);
				bots[i].life += 10;
				bots[i].pointer += 4;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX+1][bots[i].botY+1]==4)
			{
				map[bots[i].botX+1][bots[i].botY+1] = 3;
				Spawn(4);
				bots[i].pointer += 1;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
		}
		if (h==4)
		{
			if (map[bots[i].botX][bots[i].botY+1]==0)
			{
				bots[i].pointer += 5;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX][bots[i].botY+1]==1)
			{
				bots[i].pointer += 3;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX][bots[i].botY+1]==2)
			{
				bots[i].pointer += 2;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX][bots[i].botY+1]==3)
			{
				map[bots[i].botX][bots[i].botY+1] = 0;
				Spawn(3);
				bots[i].life += 10;
				bots[i].pointer += 4;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX][bots[i].botY+1]==4)
			{
				map[bots[i].botX][bots[i].botY+1] = 3;
				Spawn(4);
				bots[i].pointer += 1;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
		}
		if (h==5)
		{
			if (map[bots[i].botX-1][bots[i].botY+1]==0)
			{
				bots[i].pointer += 5;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX-1][bots[i].botY+1]==1)
			{
				bots[i].pointer += 3;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX-1][bots[i].botY+1]==2)
			{
				bots[i].pointer += 2;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX-1][bots[i].botY+1]==3)
			{
				map[bots[i].botX-1][bots[i].botY+1] = 0;
				Spawn(3);
				bots[i].life += 10;
				bots[i].pointer += 4;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX-1][bots[i].botY+1]==4)
			{
				map[bots[i].botX-1][bots[i].botY+1] = 3;
				Spawn(4);
				bots[i].pointer += 1;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
		}
		if (h==6)
		{
			if (map[bots[i].botX-1][bots[i].botY]==0)
			{
				bots[i].pointer += 5;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX-1][bots[i].botY]==1)
			{
				bots[i].pointer += 3;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX-1][bots[i].botY]==2)
			{
				bots[i].pointer += 2;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX-1][bots[i].botY]==3)
			{
				map[bots[i].botX-1][bots[i].botY] = 0;
				Spawn(3);
				bots[i].life += 10;
				bots[i].pointer += 4;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX-1][bots[i].botY]==4)
			{
				map[bots[i].botX-1][bots[i].botY] = 3;
				Spawn(4);
				bots[i].pointer += 1;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
		}
		if (h==7)
		{
			if (map[bots[i].botX-1][bots[i].botY-1]==0)
			{
				bots[i].pointer += 5;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX-1][bots[i].botY-1]==1)
			{
				bots[i].pointer += 3;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX-1][bots[i].botY-1]==2)
			{
				bots[i].pointer += 2;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX-1][bots[i].botY-1]==3)
			{
				map[bots[i].botX-1][bots[i].botY-1] = 0;
				Spawn(3);
				bots[i].life += 10;
				bots[i].pointer += 4;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX-1][bots[i].botY-1]==4)
			{
				map[bots[i].botX-1][bots[i].botY-1] = 3;
				Spawn(4);
				bots[i].pointer += 1;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
		}
	}
	
	public void BotSee(int i)
	{
		int h = bots[i].angle + bots[i].mind[bots[i].pointer] - 16;
		if (h>7) h -= 8;
		if (h==0)
		{
			if (map[bots[i].botX][bots[i].botY-1]==0)
			{
				bots[i].pointer += 5;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX][bots[i].botY-1]==1)
			{
				bots[i].pointer += 3;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX][bots[i].botY-1]==2)
			{
				bots[i].pointer += 2;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX][bots[i].botY-1]==3)
			{
				bots[i].pointer += 4;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX][bots[i].botY-1]==4)
			{
				bots[i].pointer += 1;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
		}
		if (h==1)
		{
			if (map[bots[i].botX+1][bots[i].botY-1]==0)
			{
				bots[i].pointer += 5;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX+1][bots[i].botY-1]==1)
			{
				bots[i].pointer += 3;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX+1][bots[i].botY-1]==2)
			{
				bots[i].pointer += 2;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX+1][bots[i].botY-1]==3)
			{
				bots[i].pointer += 4;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX+1][bots[i].botY-1]==4)
			{
				bots[i].pointer += 1;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
		}
		if (h==2)
		{
			if (map[bots[i].botX+1][bots[i].botY]==0)
			{
				bots[i].pointer += 5;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX+1][bots[i].botY]==1)
			{
				bots[i].pointer += 3;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX+1][bots[i].botY]==2)
			{
				bots[i].pointer += 2;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX+1][bots[i].botY]==3)
			{
				bots[i].pointer += 4;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX+1][bots[i].botY]==4)
			{
				bots[i].pointer += 1;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
		}
		if (h==3)
		{
			if (map[bots[i].botX+1][bots[i].botY+1]==0)
			{
				bots[i].pointer += 5;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX+1][bots[i].botY+1]==1)
			{
				bots[i].pointer += 3;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX+1][bots[i].botY+1]==2)
			{
				bots[i].pointer += 2;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX+1][bots[i].botY+1]==3)
			{
				bots[i].pointer += 4;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX+1][bots[i].botY+1]==4)
			{
				bots[i].pointer += 1;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
		}
		if (h==4)
		{
			if (map[bots[i].botX][bots[i].botY+1]==0)
			{
				bots[i].pointer += 5;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX][bots[i].botY+1]==1)
			{
				bots[i].pointer += 3;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX][bots[i].botY+1]==2)
			{
				bots[i].pointer += 2;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX][bots[i].botY+1]==3)
			{
				bots[i].pointer += 4;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX][bots[i].botY+1]==4)
			{
				bots[i].pointer += 1;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
		}
		if (h==5)
		{
			if (map[bots[i].botX-1][bots[i].botY+1]==0)
			{
				bots[i].pointer += 5;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX-1][bots[i].botY+1]==1)
			{
				bots[i].pointer += 3;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX-1][bots[i].botY+1]==2)
			{
				bots[i].pointer += 2;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX-1][bots[i].botY+1]==3)
			{
				bots[i].pointer += 4;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX-1][bots[i].botY+1]==4)
			{
				bots[i].pointer += 1;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
		}
		if (h==6)
		{
			if (map[bots[i].botX-1][bots[i].botY]==0)
			{
				bots[i].pointer += 5;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX-1][bots[i].botY]==1)
			{
				bots[i].pointer += 3;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX-1][bots[i].botY]==2)
			{
				bots[i].pointer += 2;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX-1][bots[i].botY]==3)
			{
				bots[i].pointer += 4;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX-1][bots[i].botY]==4)
			{
				bots[i].pointer += 1;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
		}
		if (h==7)
		{
			if (map[bots[i].botX-1][bots[i].botY-1]==0)
			{
				bots[i].pointer += 5;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX-1][bots[i].botY-1]==1)
			{
				bots[i].pointer += 3;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX-1][bots[i].botY-1]==2)
			{
				bots[i].pointer += 2;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX-1][bots[i].botY-1]==3)
			{
				bots[i].pointer += 4;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
			if (map[bots[i].botX-1][bots[i].botY-1]==4)
			{
				bots[i].pointer += 1;
				if (bots[i].pointer>63) bots[i].pointer -= 64;
			}
		}
	}
	
	public void BotRotate(int i)
	{
		bots[i].angle += bots[i].mind[bots[i].pointer] - 24;
		if (bots[i].angle>7) bots[i].angle -= 8;
		bots[i].pointer++;
		if (bots[i].pointer>63) bots[i].pointer -= 64;
	}
	
	public void BotGoTo(int i)
	{
		bots[i].pointer += bots[i].mind[bots[i].pointer];
		if (bots[i].pointer>63) bots[i].pointer -= 64;
	}
}
