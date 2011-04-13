package battleship;
//ServerGlobalMap.java
//COS325 Project 4: Battleship Game
//Spring '11
//C. Bikle



public class ServerGlobalMap
{
//----------CLASS VARIABLES----------
	static ServerGlobalMap ref;
	private int[][] grid;
	private static final int height = 26;//rows
	private static final int width = 39;//columns
	
//----------CONSTRUCTOR----------
	private ServerGlobalMap()
	{
		grid = new int[height][width];
		
		this.setToEmpty();
	}
	
//----------GET SERVER GLOBAL MAP----------
	public static synchronized ServerGlobalMap getServerGlobalMap()
	{
		if(ref == null)
			ref = new ServerGlobalMap();
		return ref;
	}

//----------CLONE----------
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
	
//----------SET TO EMPTY----------
	private void setToEmpty()
	{
		for(int i = 0; i < grid.length; i++)
		{
			for(int j = 0; j < grid[0].length; j++)
			{
				grid[i][j] = -1;
			}
		}
	}
	
//----------SET SHIP SECTION----------
	private void setShipSection(Coordinates c, int shipNumber)
	{
		this.grid[c.getY()][c.getX()] = shipNumber;
	}
	
//----------TEST SHIP SECTION----------
	public boolean testShipSection(Coordinates c) throws ShipOutOfBoundsException, ShipOverlapException
	{
		if(!coordinatesInBounds(c))
		{
			throw new ShipOutOfBoundsException();
		}
		
		if(!shipClear(c))
		{
			throw new ShipOverlapException();
		}
		
		return true;
	}
	
//----------ADD SHIP----------
	public boolean addShip(Ship ship, int shipNumber)
	{
		if(shipCoordsIsNull(ship))
		{
			return false;
		}
		
		Coordinates[] shipCoords = ship.getCoords();
		
		for(int i = 0; i < shipCoords.length; i++)
		{
			try
			{
				testShipSection(shipCoords[i]);
			}
			catch(ShipOverlapException e)
			{
				return false;
			}
			catch(ShipOutOfBoundsException e)
			{
				return false;
			}
		}
		
		for(int i = 0; i < shipCoords.length; i++)
		{
			setShipSection(shipCoords[i], shipNumber);
		}
		
		return true;
	}
	
//----------REMOVE SHIP----------
	public boolean removeShip(Ship ship, int shipNumber)
	{
		Coordinates[] shipCoords = ship.getCoords();
		
		int x = -1;
		int y = -1;
		
		boolean report = false;
		
		for(int i = 0; i < shipCoords.length; i++)
		{
			x = shipCoords[i].getX();
			y = shipCoords[i].getY();
			
			if(this.grid[y][x] == shipNumber)
				this.grid[y][x] = -1;
			
			if(i == shipCoords.length-1)
				report = true;
		}
		
		return report;
	}
	
//----------COORDINATES IN BOUNDS----------
	private boolean coordinatesInBounds(Coordinates c)
	{
		System.out.println(c);
		
		if(c.getX() < 0 || c.getX() > this.grid[0].length)
			return false;
		if(c.getY() < 0 || c.getY() > this.grid.length)
			return false;
		return true;
	}
	
//----------SHIP CLEAR----------
	private boolean shipClear(Coordinates c)
	{
		if(this.grid[c.getY()][c.getX()] == -1)
			return true;
		else
			return false;
	}
	
//----------REPORT----------
	public int report(Coordinates c)
	{
//		int sector = this.grid[c.getY()][c.getX()];
//		int report = sector;
		
//		return report;
		
		return this.grid[c.getY()][c.getX()];
	}
	
//----------REPORT ALL----------
	public String reportAll()
	{
		String report = "";
		String sector;
		
		for(int i = 0; i < grid.length; i++)
		{
			for(int j = 0; j < grid[0].length; j++)
			{
				sector = toString(this.report(new Coordinates(j,i)));
				
				report += /*Character.toString(*/sector/*)*/;
			}
			report += "\n";
		}
		
		return report;
	}
	
//----------GET IMPACT----------//not recommended
	public int getImpact(Coordinates c) throws ArrayIndexOutOfBoundsException
	{
		int sector = this.grid[c.getY()][c.getX()];
		int report = sector;
		
		if(sector >= 0)
		{
			report = sector;
			sector = -1;
		}
		
		this.grid[c.getY()][c.getX()] = sector;
		
		return report;
	}
	
//----------TO String----------
	private String toString(int n)
	{
		String tmp = Integer.toString(n);
		
		if(tmp.charAt(0) == '-')
			tmp = "-";
		
		return tmp;
		//'-1' gets turned into a String with a length of two
		//therefore, when charAt(0) is returned, only the dash gets returned
	}
	
//----------SHIP COORDS IS NULL----------
	private boolean shipCoordsIsNull(Ship ship)
	{
		if(ship.coords == null)
			return true;
		else
			return false;
	}
	
//----------MAIN----------
	public static void main(String[] args)
	{
		
/*		ServerGlobalMap sgm = getServerGlobalMap();
		
		ShipList janesList = ShipList.getShipList(4);
		
		Coordinates[][] shipCoords = new Coordinates[4][];
		
		
		for(int i = 0; i < shipCoords.length; i++)
		{
			if(i < 2)
			{
				shipCoords[i] = new Coordinates[4];
			}
			else
			{
				shipCoords[i] = new Coordinates[1];
			}
		}
		

		
		for(int i = 0; i < shipCoords[0].length; i++)
		{
			shipCoords[0][i] = new Coordinates(i, 9);
		}
		
		for(int i = 0; i < shipCoords[1].length; i++)
		{
			shipCoords[1][i] = new Coordinates(9, i);
		}
		
		for(int i = 0; i < shipCoords[2].length; i++)
		{
			shipCoords[2][i] = new Coordinates(0,1);
		}
		
		for(int i = 0; i < shipCoords[3].length; i++)
		{
			shipCoords[3][i] = new Coordinates(19, i);
		}
		
		//
		
		for(int i = 0; i < janesList.length(); i++)
		{
			janesList.addIgnoreException(new Ship(shipCoords[i], i, shipCoords[i].length), i);
		}
		
		for(int i = 0; i < janesList.length(); i++)
		{
			sgm.addShip(janesList.get(i), i);
		}
		
		System.out.println(sgm.reportAll());*/
		
		
		/*
		 get servergolbalmap
		 
		 create shiplist
		 
		 create coordinates[] = coordinates(sizeOfShip)
		 
		 create ship(playerNumber)
		 
		 loop{
		 generate two random numbers (one for x; one for y)
		 
		 fill out one axis with sequential numbers starting with x;
		 the other axis will be y at every point
		 
		 use testShipSection to make sure all coordinates are valid
		 if not, start again
		 
		 }
		 
		 set the coordinates on the ship
		 
		 add ship to shiplist
		 
		 use shiplist in loop to add ship(s) to serverglobalmap
		 
		 */
		
		
		ServerGlobalMap sgm = getServerGlobalMap();
		
		ShipList janes = ShipList.getShipList(2);
		
		Coordinates[] shipACoords = new Coordinates[5];
		Coordinates[] shipBCoords = new Coordinates[5];
		
		for(int i = 0; i < shipACoords.length; i++)
		{
			shipACoords[i] = new Coordinates(i, 5);
			shipBCoords[i] = new Coordinates(10, i);
		}
		
		Ship shipA = new Ship(shipACoords, 0, shipACoords.length);
		Ship shipB = new Ship(shipBCoords, 1, shipBCoords.length);
		
		janes.addIgnoreException(shipA, 0);
		janes.addIgnoreException(shipB, 1);
		
		
		for(int i = 0; i < janes.length(); i++)
		{
			sgm.addShip(janes.get(i), i);
		}
		
		System.out.println(sgm.reportAll());
		
		sgm.removeShip(janes.get(0), 0);
		
		System.out.println(sgm.reportAll());
	}
}