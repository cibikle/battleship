package battleship;

import java.awt.BorderLayout;

import javax.swing.*;
import java.util.Date;

public class ClientGUI extends JFrame
{
	public static final int tileSize = 25;
	private OceanDisplay oceanDisplay;
	private ScorePanel scorePanel;
	private CmdPanel cmdPanel;
    private long lastTimeFired;
    private final static String CRLF = "\r\n";
    // Firing delay is in miliseconds
    // Will be set by server in final version
    private int firingDelay = 5000;
    
    private boolean allShipsSunk;
	
	public int getFiringDelay()
	{
		return firingDelay;
	}
    
	public OceanDisplay getOceanDisplay() 
    {
		return oceanDisplay;
	}
    
	public ScorePanel getScorePanel() 
    {
		return scorePanel;
	}
    
	public CmdPanel getCmdPanel() 
    {
		return cmdPanel;
	}
    
    //Should be called by a 600 message from server
    //currently firing delay is hardcoded in a final int; fix for final version
    public void setFiringDelay( String message )
    {
        message = message.trim();
        firingDelay = Integer.parseInt( message );
    }
	
	public void callShot( String rowCol )
    { 
		if( !inbounds( rowCol ) )
		{
			cmdPanel.displaySystemMessage("Coordinate(s) out of bounds!");
			
			return;
		}
		
        if( !allShipsSunk )
        {
            long curTime = new Date().getTime();
            if( lastTimeFired + firingDelay <= curTime )
            {
                String message = "FIR " + rowCol + CRLF;
                // Send message into the œther.
                String response = "100 And this would be the response.";
                response = response.substring( 0, 3 );
                try
                {
                    if( Integer.parseInt( response ) == 100 )
                    {
                        oceanDisplay.mapMiss( rowCol, true );
                        cmdPanel.displaySystemMessage( "Miss!" );
                    }
                    else if( Integer.parseInt( response ) == 150 || Integer.parseInt( response ) == 190 )
                    {
                        oceanDisplay.mapHit( rowCol, true );
                        cmdPanel.displaySystemMessage( "Hit!" );
                        scorePanel.incScore();
                    }
                }
                catch( NumberFormatException nfe )
                {
                    System.err.println( "Received the following bad response: " + response );
                    nfe.printStackTrace();
                }
                lastTimeFired = curTime;
				
				Thread timerThread = new Thread(cmdPanel.getCooldownTimer());
				cmdPanel.getCooldownTimer().setFireBtn(cmdPanel.getFireBtn());
				timerThread.start();
				
				
            }
            else 
            {
                cmdPanel.displaySystemMessage( "Firing delay in effect; please wait " + 
											  ( ( ( lastTimeFired + firingDelay ) - curTime ) / 1000 ) + 
                                              " seconds." );
            }
        }
        else
        {
            cmdPanel.displaySystemMessage( "You have no ships left to fire with." );
        }
		
    }
	
	private boolean inbounds(String rowCol)
	{
		int[] prsd = OceanDisplay.parseRowCol(rowCol);
		
		if(prsd[0] < 0 || prsd[0] >= OceanDisplay.rows
		   || prsd[1] < 0 || prsd[1] >= OceanDisplay.columns)
			return false;
		
		return true;
	}
    
    //Should be called once you've got a 700 message from server
    public void placeShips( String shipLocations )
    {
        System.out.println( shipLocations );
        shipLocations = shipLocations.trim();
        String ships[] = shipLocations.split( "/" );
        for( int i = 0; i < ships.length; i++ )
        {
            String shipSections[] = ships[i].split( ":" );
            for( int j = 0; j < shipSections.length; j++ )
            {
                oceanDisplay.mapShip( shipSections[j], true );
            }
        }
    }
    
    //Should be called by a 500 message from server
    public void shipHit( String location )
    {
        location = location.trim();
        oceanDisplay.mapHit( location, true );
        cmdPanel.displaySystemMessage( "Your ship at " + location + " has been hit!" );
    }
    
    //Should be called by a 505 message from server
    public void shipSunk( String location )
    {
        location = location.trim();
        oceanDisplay.mapHit( location, true );
        cmdPanel.displaySystemMessage( "A ship of yours has been sunk at " + location );
    }
    
    //Should be called by a 555 message from server
    public void allShipsSunk()
    {
        cmdPanel.displaySystemMessage( "All your ships have been sunk." );
        allShipsSunk = true;
    }
    
	public ClientGUI() 
    {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Battleship");
		scorePanel = new ScorePanel();
		add(scorePanel, BorderLayout.NORTH);
		oceanDisplay = new OceanDisplay( new MouseFireListener( this ) );
		add(oceanDisplay);
		
		cmdPanel = new CmdPanel(new FireListener(this), (firingDelay/1000));
		
		add(cmdPanel, BorderLayout.SOUTH);
		setSize(tileSize*OceanDisplay.columns,tileSize*OceanDisplay.rows);
		setVisible(true);
        
        lastTimeFired = 0;
        allShipsSunk = false;
    }
    
	public static void main(String[] args) 
    {
		ClientGUI cg = new ClientGUI();
	}
}