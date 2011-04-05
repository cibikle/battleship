//Coordinates.java
//COS325 Project 4: Battleship Game
//Spring '11
//C. Bikle

package battleship;

public class Coordinates
{
	private int x;
	private int y;
	private boolean isHit;
	
	public Coordinates(int x, int y)
	{
		this.x = x;
		this.y = y;
		
		this.isHit = false;
	}
	
	public int getX()
	{
		return this.x;
	}
	
	public int getY()
	{
		return this.y;
	}
	
	public boolean isHit(Coordinates c)
	{
		if(c.getX() == this.x && c.getY() == this.y && isHit == false)
		{
			isHit = true;
			return isHit;
		}
		else
			return false;
		
		//messy, but there needs to be a way to communicate that 
		//while a second shell may have hit the ship, it doesn't count as a hit
	}
	
	public boolean getIsHit()
	{
		return isHit;
	}
	
	public String toString()
	{
		String s = this.x+" x "+this.y;
		
		return s;
	}
}