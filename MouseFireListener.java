//Implementation of MouseListener class to allow firing by clicking on ocean tiles
//Andrew Bikle

package battleship;

import java.awt.event.*;

public class MouseFireListener implements MouseListener
{
    ClientGUI clientGUI;
	public MouseFireListener( ClientGUI clientGUI )
    {
		this.clientGUI = clientGUI;
	}
    
    public void mouseClicked( MouseEvent e )
    {
        OceanTile clickedTile = (OceanTile) e.getSource();
        String rowCol = clickedTile.getYCoord();
        rowCol += clickedTile.getXCoord();
        clientGUI.callShot( rowCol );
    }
    
    public void mouseExited( MouseEvent e ) {}
    
    public void mouseEntered( MouseEvent e ) 
    {
        OceanTile enteredTile = (OceanTile) e.getSource();
        String row = enteredTile.getYCoord();
        String col = Integer.toString( enteredTile.getXCoord() );
        clientGUI.getCmdPanel().setRowEntry( row );
        clientGUI.getCmdPanel().setColumnEntry( col );
    }
    
    public void mousePressed( MouseEvent e ) {}
    
    public void mouseReleased( MouseEvent e ) {}
}