//Coordinates.java
//COS325 Project 4: Battleship Game
//Spring '11
//C. Bikle

package battleship;

public class Coordinates
{
//----------CLASS VARIABLE----------
	private int column;
	private int row;
	private boolean isHit;
	
//----------CONSTRUCTOR(INT, CHAR)----------
	public Coordinates(int column, char row)
	{
		this.column = column;
		this.row = rowToInt(row);
		
		this.isHit = false;
	}
	
//----------CONSTRUCTOR(INT, INT)----------
	public Coordinates(int column, int row)
	{
		this.column = column;
		this.row = row;
		
		this.isHit = false;
	}
	
//----------CONSTRUCTOR(STRING)----------
	public Coordinates(String rowCol)
	{
		int[] parsedIdx = parseRowCol(rowCol);
		
		this.column = parsedIdx[1];
		this.row = parsedIdx[0];
		
		this.isHit = false;
	}
	
//----------GET COLUMN----------
	public int getColumn()
	{
		return this.column;
	}
	
//----------GET ROW----------
	public int getRow()
	{
		return this.row;
	}
	
//----------GET ROW AS CHAR----------
	public char getRowAsChar()
	{
		return rowToChar(this.row);
	}
	
//----------IS HIT----------
	public boolean isHit(Coordinates c)
	{
		if(c.getColumn() == this.column && c.getRow() == this.row && isHit == false)
		{
			isHit = true;
			return isHit;
		}
		else
			return false;
		
		//messy, but there needs to be a way to communicate that 
		//while a second shell may have hit the ship, it doesn't count as a hit
	}
	
//----------GET IT HIT----------
	public boolean getIsHit()
	{
		return isHit;
	}
	
//----------TO STRING----------
	public String toString()
	{
		String s = this.row+" x "+this.column;
		
		return s;
	}
	
//----------GET ROW COL----------
	public String getRowCol()
	{
		return toRowCol(this.column, this.row);
	}
	
//----------TO ROW COL(INT, INT)----------
	public static String toRowCol(int col, int row)
	{
		String rowCol = "";
		
		rowCol = rowToChar(row)+""+col;
		
		return rowCol;
	}
	
//----------ROW TO CHAR(INT)----------
	public static char rowToChar(int row)
	{
		return Codes.ROW_LABELS.charAt(row);
	}
	
//----------ROW TO INT(CHAR)----------
	public static int rowToInt(char row)
	{
		return (int)(row - 'A');
	}
	
//----------PARSE ROW COL(STRING)----------
	public static int[] parseRowCol(String rowCol)
	{
		rowCol = rowCol.trim();
		char row = Character.toUpperCase(rowCol.charAt(0));
		String col = rowCol.substring(1);
		
		int[] idx = {(row - 'A'), Integer.parseInt(col)};
		
		return idx;
	}
}