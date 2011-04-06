


package battleship;

import javax.swing.*;

public class CooldownTimer implements Runnable
{
//----------CLASS VARIABLES----------
	private JTextField countingField;
	private int time;
	
//	private 
	
//----------CONSTRUCTOR----------
	public CooldownTimer(int time)
	{
		this.time = time;
		countingField = new JTextField("//");
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
	
//----------RUN----------
	public void run()
	{
		System.out.println("I'm alive");
		
		for(int i = time; i >= 0; i--)
		{
			countingField.setText(i+"");
			
			countingField.repaint();
			
			try
			{
				Thread.sleep(1000);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		countingField.setText("!!");
	}
}