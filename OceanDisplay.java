package battleship;

import java.awt.*;
import javax.swing.*;

public class OceanDisplay extends JPanel 
{
    
	public static final int rows = 27;
	public static final int columns = 40;
	public static final int hgap = 1;
	public static final int vgap = 1;
	private OceanTile ocean[][];
    
	public OceanDisplay( MouseFireListener listener ) 
    {
		setLayout( new GridLayout( rows,columns, hgap, vgap) );
		ocean = new OceanTile[rows][columns];
        String rowLabels = " ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		for( int i= 0; i < rows; i++ )
        {
			for( int j=0; j < columns; j++ )
            {
				ocean[i][j] = new OceanTile( j, rowLabels.substring( i,i+1 ) );
                ocean[i][j].addMouseListener( listener );
				ocean[i][j].setBackground( Color.BLUE );
				add( ocean[i][j] );
			}
		}
		for( int i=1; i < rows; i++ )
        {
			String rowLabel = rowLabels.substring( i,i+1 );
			ocean[i][0].setString( rowLabel );
		}
	    for( int j=1; j < columns; j++ )
        {
	    	ocean[0][j].setString( String.valueOf(j) );
	    }

        //ocean[20][20].setShip(true);
        //ocean[20][21].setShip(true);
        //ocean[20][22].setShip(true);
	}

	public void mapHit(String rowCol, boolean choice)
    {
		int[] parsed = parseRowCol(rowCol);
		ocean[parsed[0]][parsed[1]].setHit(choice);
		repaint();
	}
	
	public void mapMiss(String rowCol, boolean choice)
    {
		int[] parsed = parseRowCol(rowCol);
		ocean[parsed[0]][parsed[1]].setMiss(choice);
		repaint();
	}
	
	public void mapShip(String rowCol, boolean choice)
    {
		int[] parsed = parseRowCol(rowCol);
		ocean[parsed[0]][parsed[1]].setShip(choice);
		repaint();
	}

	public static int[] parseRowCol(String rowCol)
	{
		rowCol = rowCol.trim();
		char row = rowCol.charAt(0);
		String col = rowCol.substring(1);
		row = Character.toUpperCase(row);
		int rowIdx = (row - 'A')+1; // skip the header
		int colIdx = Integer.valueOf(col); // skip the header
		
		int[] coords = {rowIdx, colIdx};
		
		return coords;
	}
}

