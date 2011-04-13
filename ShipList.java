package battleship;
//ShipList.java
//COS325 Project 4: Battleship Game
//Spring '11
//C. Bikle


import java.util.*;

public class ShipList
{
//----------CLASS VARIABLES----------
	static ShipList ref;
//	private Ship[] ships;
	
	private ArrayList<Ship> ships;
	
	//et al
	
//----------CONSTRUCTOR----------
	private ShipList()
	{
		ships = new ArrayList<Ship>();
	}
	
//----------GET SHIP LIST----------
	public static synchronized ShipList getShipList(int gameSize)
	{
		if(ref == null)
			ref = new ShipList();
		return ref;
	}
	
//----------GET SHIP LIST----------
	public static synchronized ShipList getShipList()
	{
		if(ref == null)
			ref = new ShipList();
		return ref;
	}
	
//----------CLONE----------
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
	
//----------ADD----------
	public boolean add(Ship ship, int shipNumber) throws ShipOverlapException
	{
		if(ships.get(shipNumber) == null)
			ships.add(shipNumber, ship);
		else
			throw new ShipOverlapException();
		
		return true;
	}
	
//----------ADD IGNORE EXCEPTION----------
	public boolean addIgnoreException(Ship ship, int shipNumber)
	{
		ships.add(shipNumber, ship);
		
		return true;
	}
	
//----------LENGTH----------
	public int length()
	{
		return this.ships.size();
	}
	
//----------GET----------
	public Ship get(int shipNumber)
	{
		return this.ships.get(shipNumber);
	}
	
//----------REMOVE--------
	public void remove(int shipNumber)
	{
		ships.remove(ships.get(shipNumber));
	}
	
}