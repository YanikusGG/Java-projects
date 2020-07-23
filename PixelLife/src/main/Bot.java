package main;

import java.util.Random;

public class Bot {
	
	public int botX;
	public int botY;
	public int life = 40;
	public int[] mind = new int[64];
	public int pointer = 0;
	public int angle;
	
	Random r = new Random();
	
	public Bot(int x, int y)
	{
		botX = x;
		botY = y;
		angle = r.nextInt(8);
		for (int i=0; i<64; i++)
		{
			mind[i] = r.nextInt(64);
		}
		mind[0] = 17;
		mind[1] = 9;
		mind[2] = 1;
		mind[3] = 61;
		mind[4] = 1;
		mind[5] = 1;
		mind[6] = 26;
		mind[7] = 57;
		mind[8] = 56;
		mind[10] = 54;
	}
	
}
