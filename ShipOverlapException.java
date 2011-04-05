package battleship;
//ShipOverlapException.java
//COS325 Project 4: Battleship Game
//Spring '11
//C. Bikle

public class ShipOverlapException extends Exception
{
	public ShipOverlapException(String msg)
	{
		super(msg);
	}
	
	public ShipOverlapException()
	{
		super();
	}
}