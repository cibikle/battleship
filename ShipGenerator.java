/*
 * Jeffrey Cox
 * Battleship Shared Code
 * 4/2/11
 */

//Modified 4/11/11 by C. Bikle

package battleship;

import java.util.Random;


public class ShipGenerator
{
	//----------CLASS VARIABLES----------
	private int numPlayers = 0;
	private boolean[][] spaceUsed = new boolean[rows][cols];
	private static final int shipsPerPlayer = 5;
	private static final int rows = 26;
	private static final int cols = 39;
	
	//----------CONSTRUCTOR----------
	ShipGenerator(int numPlayers)
	{
		this.numPlayers = numPlayers;
		
		for(int i = 0; i < rows; i++)
		{
			for(int j = 0; j < cols; j++)
			{
				spaceUsed[i][j] = false;
			}
		}
		
	}
	
	//----------GENERATE SHIPS----------
	public Ship[] generateShips()
	{
		Ship[] ships = new Ship[numPlayers * shipsPerPlayer];
		
		int shipLength = 0;
		int shipCounter = 0;
		int x = numPlayers;
		
		for(int j = shipsPerPlayer; j > 1; j--)
		{
			for(int i = 0; i < numPlayers; i++)
			{
				shipLength = j;
				ships[shipCounter] = new Ship(fitShip(shipLength), (i+1), shipLength);
				shipCounter++;
			}
			
			if(j == 3)
			{
				for(int i = 0; i < numPlayers; i++)
				{
					shipLength = j;
					ships[shipCounter] = new Ship(fitShip(shipLength), (i+1), shipLength);
					shipCounter++;
				}
			}
		}
		
		//----------Making 5-long ships----------
		/*		for(int i = 0; i < numPlayers; i++)
		 {		
		 shipLength = 5;
		 ships[shipCounter] = new Ship(fitShip(shipLength), (i+1), shipLength);
		 shipCounter++;
		 }
		 
		 //----------Making 4-long ships----------
		 for(int i = 0; i < numPlayers; i++)
		 {
		 shipLength = 4;
		 ships[shipCounter] = new Ship(fitShip(shipLength), (i+1), shipLength);
		 shipcounter++;
		 }
		 
		 //----------Making 3-long ships----------
		 for(int i = 0; i < numPlayers; i++)
		 {
		 shipLength = 3;
		 ships[shipCounter] = new Ship(fitShip(shipLength), (i+1), shipLength);
		 shipCounter++;
		 }
		 
		 //----------Making 2nd 3-long ships----------
		 for(int i = 0; i < numPlayers; i++)
		 {
		 shipLength = 3;
		 ships[shipCounter] = new Ship(fitShip(shipLength), (i+1), shipLength);
		 shipCounter++;
		 }
		 
		 //----------Making 2-long ships----------
		 for(int i = 0; i < numPlayers; i++)
		 {
		 shipLength=2;
		 ships[shipCounter] = new Ship(fitShip(shipLength), (i+1), shipLength);
		 shipCounter++;
		 }
		 
		 /*int counter=0;
		 for (int i=0; i<rows; i++){
		 for (int j=0; j<cols; j++){
		 if (spaceused[i][j]==true){
		 System.out.print(1 );
		 counter++;
		 }else System.out.print(0 );
		 }
		 System.out.println();
		 }
		 System.out.println(counter);
		 ships[0].printCoords();
		 ships[1].printCoords();
		 ships[2].printCoords();
		 ships[3].printCoords();
		 ships[4].printCoords();*/
		return ships;
	}
	
	//----------FIT SHIP----------
	public Coordinates[] fitShip(int shipLength)
	{
		Random generator = new Random();
		Coordinates[] coords = new Coordinates[shipLength];
		boolean success = false;
		
		while(success == false)
		{
			int shipRow = generator.nextInt(rows); //starting row of ship to be generated
			int shipCol = generator.nextInt(cols); //starting column of ship to be generated
			int tryCount = 0;
			
			if(spaceUsed[shipRow][shipCol] == true) continue;
			
			else
			{
				int[] shipRows = new int[shipLength];//rows of ship to be made, i.e. shiprows, or shrows
				shipRows[0] = shipRow;  
				int[] shipCols = new int[shipLength];//columns of ship to be made, i.e., shipcolumns or shcolumns
				shipCols[0] = shipCol; 
				int direction = generator.nextInt(4)+1;
				
				for(int j = 0; j < 4; j++)
				{
					success = true;
					
					if(direction == 1)
					{
						for(int k = 1; k < shipLength; k++)
						{
							if((shipCol + k) > (cols - 1))
							{
								success = false;
								continue;
							}
							
							if(spaceUsed[shipRow][shipCol + k] == true)
							{
								success = false;
								continue;
							}
							else
							{
								shipRows[k] = shipRow;
								shipCols[k] = shipCol + k;
							}
						}
						
						if(success == true)
						{
							break;
						}
					}
					else if(direction == 2)
					{
						for(int k = 1; k < shipLength; k++)
						{
							if((shipCol - k) <= 0)
							{
								success = false;
								continue;
							}
							
							if(spaceUsed[shipRow][shipCol - k] == true)
							{
								success = false;
								continue;
							}
							else
							{
								shipRows[k] = shipRow;
								shipCols[k] = shipCol - k;
							}
						}
						
						if(success == true)
						{
							break;
						}
					}
					else if(direction == 3)
					{
						for(int k = 1; k < shipLength; k++)
						{
							if((shipRow + k) > (rows - 1))
							{
								success = false;
								continue;
							}
							
							if(spaceUsed[shipRow + k][shipCol] == true)
							{
								success = false;
								continue;
							}
							else
							{
								shipRows[k] = shipRow + k;
								shipCols[k] = shipCol;
							}
						}
						
						if(success == true)
						{
							break;
						}
					}
					else if(direction == 4)
					{
						for(int k = 1; k < shipLength; k++)
						{
							if((shipRow - k) <= 0)
							{
								success = false;
								continue;
							}
							
							if(spaceUsed[shipRow - k][shipCol] == true)
							{
								success = false;
								continue;
							}
							else
							{
								shipRows[k] = shipRow - k;
								shipCols[k] = shipCol;
							}
						}
						
						if(success == true)
						{
							break;
						}
					}
					
					tryCount++;
					
					direction++;
					
					if(direction == 5)
						direction = 1;
					//System.out.println("direction "+direction);
				}
				
				if(success == true)
				{
					for(int j = 0; j < shipLength; j++)
					{
						coords[j] = new Coordinates(shipRows[j], shipCols[j]);
						spaceUsed[shipRows[j]][shipCols[j]] = true;
						
					}
				}
				
				
			}
			
		}
		return coords;
	}
}
