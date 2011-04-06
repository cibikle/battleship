//CooldownTimer.java
//COS325 Project 4: Battleship Game
//Spring '11
//C. Bikle


package battleship;

import javax.swing.*;

public class CooldownTimer implements Runnable
{
//----------CLASS VARIABLES----------
	private int time;
	private JButton fireBtn;
	
//----------CONSTRUCTOR----------
	public CooldownTimer(int time)
	{
		this.time = time;
	}
	
//----------GET TIME----------
	public int getTime()
	{
		return this.time;
	}
	
//----------SET FIRE BTN----------
	public void setFireBtn(JButton fireBtn)
	{
		this.fireBtn = fireBtn;
	}
	
//----------RUN----------
	public void run()
	{
//		System.out.println("I'm alive");
		
		fireBtn.setEnabled(false);
		
		for(int i = time; i >= 0; i--)
		{
			for(int j = 9; j >= 0; j--)
			{
				fireBtn.setText(i+"."+j);
				
				fireBtn.repaint();
				
				try
				{
					Thread.sleep(80);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		
		fireBtn.setEnabled(true);
		
		fireBtn.setText("Fire!");
		
//		blink();
//		blink();
	}
	
//----------BLINK----------
	public void blink()
	{
		fireBtn.setText("Fire!");
		fireBtn.repaint();
		
		try
		{
			Thread.sleep(80);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		fireBtn.setText("****");
		fireBtn.repaint();
		
		try
		{
			Thread.sleep(80);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		fireBtn.setText("Fire!");
		fireBtn.repaint();
	}
}