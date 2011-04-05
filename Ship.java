package battleship;
//Ship.java
//COS325 Project 4: Battleship Game
//Spring '11
//C. Bikle


public class Ship
{
	boolean isSunk;
	Coordinates[] coords;
	int size;
	int playerNumber;
	
	public Ship(Coordinates[] coords, int playerNumber, int size)
	{
		isSunk = false;
		
		this.coords = coords;
		this.playerNumber = playerNumber;
		this.size = size;
	}
	
	public Ship(int playerNumber)
	{
		isSunk = false;
		
		this.playerNumber = playerNumber;
		
		this.coords = null;
	}
	
	public void setCoords(Coordinates[] coords)
	{
		this.coords = coords;
		this.size = this.coords.length;
	}
	
	public Coordinates[] getCoords()
	{
		return this.coords;
	}
	
	public Coordinates getCoords(int section)
	{
		return this.coords[section];
	}
	
	public boolean isSunk()
	{
		return isSunk;
	}
	
	public void checkSunk()
	{
		int i = 0;
		
		while(coords[i].getIsHit())
		{
			i++;
		}
		
		if(i == (coords.length-1))
			isSunk = true;
	}
}