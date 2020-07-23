package rus.ya.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Game extends JPanel implements ActionListener{
	public static final int SCALE = 32;
	public static final int WIDTH = 20;
	public static final int HEIGHT = 20;
	
	public static boolean[][] walls = new boolean[WIDTH][HEIGHT];
	
	public static int MinaX = 999;
	public static int MinaY = 999;
	
	public static int bullN = 0;
	public static int[] bullX = new int[100];
	public static int[] bullY = new int[100];
	public static int[] bullD = new int[100];
	public static boolean[] bullL = new boolean[100];
	
	Me me = new Me(15,15);
	Enemy en = new Enemy(2,2);
	Timer t = new Timer(100, this);
	
	public Game(){
		t.start();
		addKeyListener(new Keyboard());
		setFocusable(true);
	}
	
	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setResizable(false);
		f.setSize(WIDTH*SCALE+7, HEIGHT*SCALE+30);
		f.setLocationRelativeTo(null);
		f.add(new Game());
		f.setVisible(true);
		
		for (int i=0; i<100; i++)
		{
			bullL[i] = false;
		}
		
		Walls();
	}
	
	private Color color(int red, int green, int blue) {
		return new Color(red,green,blue);
	}
	
	public void paint(Graphics g){
		g.setColor(color(150,150,150));
		g.fillRect(0,0,WIDTH*SCALE, HEIGHT*SCALE);
		g.setColor(color(255,216,0));
		for(int xx = 0;xx<=WIDTH*SCALE;xx+=SCALE){
			g.drawLine(xx, 0, xx,SCALE*HEIGHT );
		}
		for(int yy = 0;yy<=HEIGHT*SCALE;yy+=SCALE){
			g.drawLine(0, yy, HEIGHT*SCALE,yy );
		}
		g.setColor(color(20,30,150));
		if(me.life==true)g.fillRect(me.MeX*SCALE+1, me.MeY*SCALE+1, SCALE-1, SCALE-1);
		g.setColor(color(200,0,0));
		if(en.life==true)g.fillRect(en.EnX*SCALE+1, en.EnY*SCALE+1, SCALE-1, SCALE-1);
		g.setColor(color(255,0,0));
		if(me.life==true){
			if(me.dir==4)g.drawLine(me.MeX*SCALE+16, me.MeY*SCALE+7, me.MeX*SCALE+16, me.MeY*SCALE+1);
			if(me.dir==3)g.drawLine(me.MeX*SCALE+7, me.MeY*SCALE+16, me.MeX*SCALE+1, me.MeY*SCALE+16);
			if(me.dir==2)g.drawLine(me.MeX*SCALE+16, me.MeY*SCALE+25, me.MeX*SCALE+16, me.MeY*SCALE+31);
			if(me.dir==1)g.drawLine(me.MeX*SCALE+25, me.MeY*SCALE+16, me.MeX*SCALE+31, me.MeY*SCALE+16);
		}
		if((MinaX!=999)&(MinaY!=999)){
			g.setColor(color(100,15,100));
			g.fillRect(MinaX*SCALE+1,MinaY*SCALE+1,SCALE-1,SCALE-1);
		}
		g.setColor(color(50,200,50));
		for (int i=0; i<bullN; i++)
		{
			if (bullL[i]==true)
			{
				g.fillRect(bullX[i]*SCALE+11, bullY[i]*SCALE+11, 10, 10);
			}
		}
		g.setColor(color(0,0,0));
		for(int i=0;i<WIDTH;i++){
			for (int j=0; j<HEIGHT; j++)
			{
				if (walls[i][j]==true) g.fillRect(i*SCALE+1,j*SCALE+1,SCALE-1,SCALE-1);
			}
		}
	}
	public static void Walls(){
		for(int i=0;i<WIDTH;i++){
			for (int j=0; j<HEIGHT; j++)
			{
				walls[i][j] = false;
			}
		}
		for(int i=0;i<100;i++){
			int p = (int) (Math.random()*WIDTH);
			int g = (int) (Math.random()*HEIGHT);
			walls[p][g] = true;
		}
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(me.MeX>WIDTH-1) me.MeX=WIDTH-1;
		if(me.MeX<0) me.MeX=0;
		if(me.MeY>HEIGHT-1) me.MeY=HEIGHT-1;
		if(me.MeY<0) me.MeY=0;
		if(en.EnX>WIDTH-1) en.EnX=WIDTH-1;
		if(en.EnX<0) en.EnX=0;
		if(en.EnY>HEIGHT-1) en.EnY=HEIGHT-1;
		if(en.EnY<0) en.EnY=0;
		if(MinaX>WIDTH|MinaX<0|MinaY>HEIGHT|MinaY<0){MinaX=999;MinaY=999;}
		
		if (walls[me.MeX][me.MeY]==true || walls[en.EnX][en.EnY]==true) {
			Walls();
		}
		
		if((en.EnX==MinaX)&(en.EnY==MinaY)){en.life=false;MinaX=999;MinaY=999;}
		if((me.MeX==MinaX)&(me.MeY==MinaY)){me.life=false;MinaX=999;MinaY=999;}
		
		repaint();
		
		if((en.hod==true)&(en.life==true)){
			if (bullN>99) bullN=99;
			for (int i=0; i<bullN; i++)
			{
				if (bullL[i]==true)
				{
					if (bullX[i]==en.EnX && bullY[i]==en.EnY)
					{
						bullL[i] = false;
						bullN--;
						en.life = false;
						break;
					}
					for (int j=0; j<1; j++)
					{
						if (bullD[i]==4)
						{
							bullY[i]--;
						}
						if (bullD[i]==3)
						{
							bullX[i]--;
						}
						if (bullD[i]==2)
						{
							bullY[i]++;
						}
						if (bullD[i]==1)
						{
							bullX[i]++;
						}
						if (bullX[i]>=WIDTH || bullX[i]<0 || bullY[i]>=HEIGHT || bullY[i]<0)
						{
							bullL[i] = false;
							break;
						}
						if (walls[bullX[i]][bullY[i]]==true)
						{
							walls[bullX[i]][bullY[i]] = false;
							bullL[i] = false;
							break;
						}
						if (bullX[i]==en.EnX && bullY[i]==en.EnY)
						{
							bullL[i] = false;
							en.life = false;
							break;
						}
					}
				}
			}
			repaint();
			FindPath();
			en.hod=false;
			me.hod=true;
			repaint();
		}
		if(en.life==false){
			if (bullN>99) bullN=99;
			for (int i=0; i<bullN; i++)
			{
				if (bullL[i]==true)
				{
					for (int j=0; j<3; j++)
					{
						if (bullD[i]==4)
						{
							bullY[i]--;
						}
						if (bullD[i]==3)
						{
							bullX[i]--;
						}
						if (bullD[i]==2)
						{
							bullY[i]++;
						}
						if (bullD[i]==1)
						{
							bullX[i]++;
						}
						if (bullX[i]>=WIDTH || bullX[i]<0 || bullY[i]>=HEIGHT || bullY[i]<0)
						{
							bullL[i] = false;
							bullN--;
							break;
						}
						if (walls[bullX[i]][bullY[i]]==true)
						{
							walls[bullX[i]][bullY[i]] = false;
							bullL[i] = false;
							bullN--;
							break;
						}
						if (bullX[i]==en.EnX && bullY[i]==en.EnY)
						{
							bullL[i] = false;
							bullN--;
							en.life = false;
							break;
						}
					}
				}
			}
			repaint();
			me.hod=true;
		}
	}
	
	public void FindPath()
	{
		int[][] step = new int[WIDTH][HEIGHT];
		for (int i=0; i<WIDTH; i++)
		{
			for (int j=0 ;j<HEIGHT; j++)
			{
				step[i][j] = 999;
			}
		}
		step[en.EnX][en.EnY] = 0;
		int c=0;
		while (step[me.MeX][me.MeY]==999)
		{
			for (int i=0; i<WIDTH; i++)
			{
				for (int j=0; j<HEIGHT; j++)
				{
					if (step[i][j]==c)
					{
						if (i+1<WIDTH)
						{
							if (step[i+1][j]==999 && walls[i+1][j]==false)
							{
								step[i+1][j] = c+1;
							}
						}
						if (i-1>=0)
						{
							if (step[i-1][j]==999 && walls[i-1][j]==false)
							{
								step[i-1][j] = c+1;
							}
						}
						if (j+1<HEIGHT)
						{
							if (step[i][j+1]==999 && walls[i][j+1]==false)
							{
								step[i][j+1] = c+1;
							}
						}
						if (j-1>=0)
						{
							if (step[i][j-1]==999 && walls[i][j-1]==false)
							{
								step[i][j-1] = c+1;
							}
						}
					}
				}
			}
			c++;
			if (c>400)
			{
				Walls();
				while (walls[me.MeX][me.MeY]==true || walls[en.EnX][en.EnY]==true) {
					Walls();
				}
				for (int i=0; i<WIDTH; i++)
				{
					for (int j=0 ;j<HEIGHT; j++)
					{
						step[i][j] = 999;
					}
				}
				c=0;
			}
		}
		int corX=me.MeX, corY=me.MeY;
		while (true)
		{
			if (c==1)
			{
				if (corX==me.MeX && corY==me.MeY)
				{
					me.life = false;
				}
				en.EnX = corX;
				en.EnY = corY;
				break;
			}
			for (int w=0; w<1; w++)
			{
				if (corX+1<WIDTH)
				{
					if (step[corX+1][corY]==c-1)
					{
						corX++;
						c--;
						break;
					}
				}
				if (corX-1>=0)
				{
					if (step[corX-1][corY]==c-1)
					{
						corX--;
						c--;
						break;
					}
				}
				if (corY+1<HEIGHT)
				{
					if (step[corX][corY+1]==c-1)
					{
						corY++;
						c--;
						break;
					}
				}
				if (corY-1>=0)
				{
					if (step[corX][corY-1]==c-1)
					{
						corY--;
						c--;
						break;
					}
				}
			}
		}
	}
	
	private class Keyboard extends KeyAdapter{
		public void keyPressed(KeyEvent kEvnt){
			int key = kEvnt.getKeyCode();
			
			if((me.hod==true)&(me.life==true)){
				if(key==KeyEvent.VK_RIGHT){
					if((en.life==true)&(en.EnX==me.MeX+1)&(en.EnY==me.MeY)){}
					else if(me.MeX==WIDTH-1){}
					else{
						if(walls[me.MeX+1][me.MeY]==false){
							me.hod=false;
							me.MeX++;
							en.hod=true;
						}
					}
				}
				if(key==KeyEvent.VK_DOWN){
					if((en.life==true)&(en.EnX==me.MeX)&(en.EnY==me.MeY+1)){}
					else if(me.MeY==HEIGHT-1){}
					else {
						if(walls[me.MeX][me.MeY+1]==false){
							me.hod=false;
							me.MeY++;
							en.hod=true;
						}
					}
				}
				if(key==KeyEvent.VK_LEFT){
					if((en.life==true)&(en.EnX==me.MeX-1)&(en.EnY==me.MeY)){}
					else if(me.MeX==0){}
					else {
						if(walls[me.MeX-1][me.MeY]==false){
							me.hod=false;
							me.MeX--;
							en.hod=true;
						}
					}
				}
				if(key==KeyEvent.VK_UP){
					if((en.life==true)&(en.EnX==me.MeX)&(en.EnY==me.MeY-1)){}
					else if(me.MeY==0){}
					else {
						if(walls[me.MeX][me.MeY-1]==false){
							me.hod=false;
							me.MeY--;
							en.hod=true;
						}
					}
				}
				if(key==KeyEvent.VK_D){
					me.dir=1;
				}
				if(key==KeyEvent.VK_S){
					me.dir=2;
				}
				if(key==KeyEvent.VK_A){
					me.dir=3;
				}
				if(key==KeyEvent.VK_W){
					me.dir=4;
				}
				if(key==KeyEvent.VK_SHIFT){
					me.hod=false;
					bullN++;
					bullL[bullN-1] = true;
					bullX[bullN-1] = me.MeX;
					bullY[bullN-1] = me.MeY;
					bullD[bullN-1] = me.dir;
					en.hod=true;
				}
				if(key==KeyEvent.VK_SPACE){
					me.hod=false;
					en.hod=true;
				}
				if(key==KeyEvent.VK_CONTROL){
					if(me.dir==1){
						if(me.MeX==WIDTH-1){}
						else {
							if(walls[me.MeX+1][me.MeY]==false){
								MinaX=me.MeX+1;
								MinaY=me.MeY;
								me.hod=false;
								en.hod=true;
							}
						}
					}
					if(me.dir==2){
						if(me.MeY==HEIGHT-1){}
						else {
							if(walls[me.MeX][me.MeY+1]==false){
								MinaX=me.MeX;
								MinaY=me.MeY+1;
								me.hod=false;
								en.hod=true;
							}
						}
					}
					if(me.dir==3){
						if(me.MeX==0){}
						else {
							if(walls[me.MeX-1][me.MeY]==false){
								MinaX=me.MeX-1;
								MinaY=me.MeY;
								me.hod=false;
								en.hod=true;
							}
						}
					}
					if(me.dir==4){
						if(me.MeY==0){}
						else {
							if(walls[me.MeX][me.MeY-1]==false){
								MinaX=me.MeX;
								MinaY=me.MeY-1;
								me.hod=false;
								en.hod=true;
							}
						}
					}
				}
			}
		}
	}
}
