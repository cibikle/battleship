//CooldownTimer.java
//COS325 Project 4: Battleship Game
//Spring '11
//C. Bikle


package battleship;

import javax.swing.*;

public class CooldownTimer implements Runnable
{
//----------CLASS VARIABLES----------
	private JTextField countingField;
	private int time;
	
	private JButton fireBtn;
	
//----------CONSTRUCTOR----------
	public CooldownTimer(int time)
	{
		this.time = time;
		countingField = new JTextField("-.-");
		countingField.setEditable(false);
	}
	
//----------GET TIME----------
	public int getTime()
	{
		return this.time;
	}
	
//----------GET COUNTINGFIELD----------
	public JTextField getCountingField()
	{
		return this.countingField;
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
			for(int j = 10; j >= 0; j--)
			{
				countingField.setText(i+"."+j);
				
				countingField.repaint();
				
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
		
		blink();
		blink();
	}
	
//----------BLINK----------
	public void blink()
	{
		countingField.setText("-.-");
		countingField.repaint();
		try
		{
			Thread.sleep(50);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		countingField.setText("  ");
		countingField.repaint();
		try
		{
			Thread.sleep(50);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		countingField.setText("-.-");
		countingField.repaint();
		
/*		try
		{
			Thread.sleep(50);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		countingField.setText("  ");
		countingField.repaint();
		try
		{
			Thread.sleep(50);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		countingField.setText("-.-");
		countingField.repaint();*/
	}
}