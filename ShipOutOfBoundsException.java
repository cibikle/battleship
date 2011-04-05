package battleship;
//ShipOutOfBoundsException.java
//COS325 Project 4: Battleship Game
//Spring '11
//C. Bikle

public class ShipOutOfBoundsException extends Exception
{
	public ShipOutOfBoundsException(String msg)
	{
		super(msg);
	}
	
	public ShipOutOfBoundsException()
	{
		super();
	}
}