package battleship;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class FireListener implements ActionListener 
{
	ClientGUI clientGUI;
	public FireListener( ClientGUI clientGUI )
    {
		this.clientGUI = clientGUI;
	}
	public void actionPerformed( ActionEvent e )
    {
		String row = clientGUI.getCmdPanel().getRowEntry().getText();
		row = row.trim();
		String column = clientGUI.getCmdPanel().getColumnEntry().getText();
		column = column.trim();
		
		String rowCol = row + column;
		
		clientGUI.callShot( rowCol );
	}
}