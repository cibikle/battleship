//ShipGenerator.java
//COS325 Project 4: Battleship Game
//Spring '11
//C. Bikle

package battleship;

import java.util.Random;

public class ShipGenerator
{
//----------CLASS VARIABLES----------
	private static final int ROWS = 26;
	private static final int COLS = 39;
	private static Random ayn = new Random();
	private int maxSize;
	private int numOfPlayers;
	
//----------CONSTRUCTOR----------
	public ShipGenerator(int numOfPlayers, int maxSize)
	{
		this.numOfPlayers = numOfPlayers;
		this.maxSize = maxSize;
	}
	
//----------GET MAX SIZE----------
	public int getMaxSize()
	{
		return maxSize;
	}
	
//----------GET NUMBER OF PLAYERS----------
	public int getNumOfPlayers()
	{
		return numOfPlayers;
	}
	
//----------SET MAX SIZE----------
	public void getMaxSize(int mS)
	{
		maxSize = mS;
	}
	
//----------SET NUMBER OF PLAYERS----------
	public void getNumOfPlayers(int num)
	{
		numOfPlayers = num;
	}

//----------GENERATE COORDINATES----------//returns array of Coordinates guaranteed to fit on the field of play
	public Coordinates[] generateCoordinates(int shipLength)
	{
		int x = -1;
		int y = -1;
		int direction = -1;
		Coordinates[] coords = null;
		
		int sectionsCheckOut = 0;
		
		while(sectionsCheckOut < shipLength)
		{
//			System.out.println("sectionsCheckOut: "+sectionsCheckOut);
			
			x = ayn.nextInt(COLS);
			y = ayn.nextInt(ROWS);
			direction = ayn.nextInt(4);
			
			try
			{
				coords = formALine(new Coordinates(x, y), direction, shipLength);
			}
			catch(Exception e)
			{
				System.out.println(e);
				sectionsCheckOut = 0;
				continue;
			}
			
			for(int i = 0; i < coords.length; i++)
			{
				try
				{
					if(ServerGlobalMap.getServerGlobalMap().testShipSection(coords[i]))
					{
						sectionsCheckOut++;
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
					
					sectionsCheckOut = 0;
				}
			}
		}
		
		return coords;
	}
	
//----------FORM A LINE----------//returns a set of coordinates stretching from the startPoint
	// in one of four directions
	private Coordinates[] formALine(Coordinates startPoint, int direction, int shipLength) throws Exception
	{
		Coordinates[] coords = new Coordinates[shipLength];
		char dir = Integer.toString(direction).charAt(0);
		int x = startPoint.getX();
		int y = startPoint.getY();
		int xa = 0;
		int ya = 0;
		
		System.out.println("dir: "+dir);
		
		switch(dir)
		{
			case '0':
				ya = 1;
				break;
				
			case '1':
				xa = 1;
				break;
				
			case '2':
				ya = -1;
				break;
				
			case '3':
				xa = -1;
				break;
				
			default:
				throw new Exception("an error in formALine() in ShipGenerator: the 'direction' variable had an illegal value");
		}
		
		for(int i = 1; i < coords.length; i++)
		{
			coords[i] = new Coordinates((x += xa), (y += ya));
			
			System.out.println(coords[i]);
		}
		
		coords[0] = startPoint;
		
		return coords;
	}
	
//----------GENERATE SHIP----------//returns a ship guaranteed to fit on the field of play
	public Ship generateShip(int size, int playerNumber)
	{
		return new Ship(generateCoordinates(size), playerNumber, size);
	}
	
//----------GENERATE SHIPS----------//returns a fleet of ships guaranteed to fit on the field of play and not overlap
	public Ship[] generateShips(int numOfPlayers)
	{
		Ship[] whoCalledInTheFleet = new Ship[numOfPlayers * 5];
		int size = maxSize;
		int playerNumber = 0;
		int counter = maxSize-1;
		ServerGlobalMap sgm = ServerGlobalMap.getServerGlobalMap();
		
		for(int i = 0; i < whoCalledInTheFleet.length; i++)
		{
			whoCalledInTheFleet[i] = generateShip(size, playerNumber);
			
			while(!sgm.addShip(whoCalledInTheFleet[i], i))
			{
				whoCalledInTheFleet[i] = generateShip(size, playerNumber);
			}
			
			size--;
			counter--;
			
			if(counter == 1)
			{
				size = 3;
			}
			else if(counter == -1)
			{
				size = maxSize;
				counter = maxSize-1;
			}
		}
		
		for(int i = 0; i < whoCalledInTheFleet.length; i++)
		{
			sgm.removeShip(whoCalledInTheFleet[i], i);
		}
		
		return whoCalledInTheFleet;
	}
	
//----------MAIN----------
	public static void main(String[] args)
	{
/*		System.out.println("starting");
		
		int number = 1;
		
		if(args.length > 0)
			number = Integer.parseInt(args[0]);
		
		Ship[] x = generateShips(number);
		
//		System.out.println("continuing");
		
		for(int i = 0; i < x.length; i++)
			for(int j = 0; j < x[i].size; j++)
				System.out.println("ship["+i+"]: "+x[i].getCoords()[j]);
		
		ServerGlobalMap sgm = ServerGlobalMap.getServerGlobalMap();
		
		for(int i = 0; i < x.length; i++)
		{
			sgm.addShip(x[i], i);
		}
		
		System.out.println(sgm.reportAll());*/
		
		System.out.println("starting");
		
		int number = 1;
		
		if(args.length > 0)
			number = Integer.parseInt(args[0]);
		
		ShipGenerator utopiaPlanitia = new ShipGenerator(number, 5);
		
		Ship[] x = utopiaPlanitia.generateShips(utopiaPlanitia.getNumOfPlayers());
		
		//		System.out.println("continuing");
		
		ShipList janes = ShipList.getShipList();
		
		for(int i = 0; i < x.length; i++)
		{
			for(int j = 0; j < x[i].size; j++)
				System.out.println("ship["+i+"]: "+x[i].getCoords()[j]);
			janes.addIgnoreException(x[i], i);
		}
		
		ServerGlobalMap sgm = ServerGlobalMap.getServerGlobalMap();
		
		for(int i = 0; i < janes.length(); i++)
		{
			System.out.println("**"+sgm.addShip(janes.get(i), i));
		}
		
		
		
		System.out.println(sgm.reportAll());
		
		x = utopiaPlanitia.generateShips(1);
		
		int eric = janes.length();
		
		for(int i = 0; i < x.length; i++)
		{
			try
			{
				System.out.println("***"+janes.length());
				janes.addIgnoreException(x[i], (i+eric));
			}
			catch(Exception e)
			{
				System.out.println(e);
			}
			
			System.out.println(sgm.addShip(janes.get(i+eric), (i+eric)));
		}
		
		System.out.println(sgm.reportAll());
	}
}