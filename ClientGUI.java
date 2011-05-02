package battleship;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.Dimension;

import java.io.DataOutputStream;

import javax.swing.*;
import java.util.Date;

import java.io.IOException;

public class ClientGUI extends JFrame
{
	public static final int tileSize = 25;
	private OceanDisplay oceanDisplay;
	private ScorePanel scorePanel;
	private CmdPanel cmdPanel;
    private long lastTimeFired;
    private final static String CRLF = "\r\n";
    // Firing delay is in miliseconds
	//defaults to 5 sec; asks the server if it has a different firing delay to use
    private int firingDelay = 5000;
	
	private DataOutputStream outToServer;
    
    private boolean allShipsSunk;
	
	private boolean gameBegun = false;
	
//methods
	
	public void beginGame()
	{
		gameBegun = true;
	}
	
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
	
//----------REQUEST FIRING DELAY----------
	private void requestFiringDelay()
	{
		try
		{
			outToServer.writeBytes("FDC"+Codes.CRLF);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
//----------REQUEST SHIP PLACEMENT----------
	private void requestShipPlacement()
	{
//		try
//		{
//			outToServer.writeBytes(/8request*/);
//		}
//		catch(IOException e)
//		{
//			e.printStackTrace();
//		}
	}
    
    //Should be called by a 600 message from server
    //currently firing delay is hardcoded in a final int; fix for final version
    public void setFiringDelay( String message )
    {
        message = message.trim();
        firingDelay = Integer.parseInt( message );
		
		cmdPanel.setCooldownTimer(firingDelay/1000);
		
		System.out.println("firing delay, hopefully server's (6000): "+this.firingDelay);
    }
	
	public void callShot( String rowCol )
    { 
		if(!gameBegun)
		{
			cmdPanel.displaySystemMessage("Game has not begun!");
			return;
		}
		
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
				
				try
				{
					outToServer.writeBytes(message);
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
				
//				String response = inFromServer.readLine();
				
//                String response = "100 And this would be the response.";
 /*               response = response.substring( 0, 3 );
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
                }*/
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
	
//----------BYE----------
	public void bye(String msg)
	{
		cmdPanel.displaySystemMessage("Player "+msg+" has left the game");
	}
	
//----------END----------
	public void end()
	{
		cmdPanel.displaySystemMessage("All other players have left the game");
	}
	
//----------WON----------
	public void won(String msg)
	{
		cmdPanel.displaySystemMessage("Player "+msg+" has won!");//<<add score
	}
	
//----------DISPLAY SYS MSG----------
	public void displaySysMsg(String msg)
	{
		cmdPanel.displaySystemMessage(msg);
		
		if(msg.contains("joined"))//eg Player Angle O'Saxon joined the game (2/2)
		{
			if(msg.contains("(") && msg.contains(")"))
			{
				String fraction = msg.substring((msg.indexOf("(")+1), (msg.indexOf(")"))).trim();
				System.out.println(fraction);
				String[] numbers = fraction.split("/");
				
				if(numbers[0] == numbers[1])
				{
					try
					{
						outToServer.writeBytes(Codes.BGN);
					}
					catch(IOException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
	}
	
//----------DISPLAY PLAYER MSG----------
	public void displayPlayerMsg(String msg)
	{
		System.out.println(msg);
	}
    
	public ClientGUI(DataOutputStream outToServer) 
    {
		this.outToServer = outToServer;
		
		//add requests for Ship Placement(<<), Firing Delay(√)
		//at which points should these be called?
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Battleship");
		scorePanel = new ScorePanel();
		add(scorePanel, BorderLayout.NORTH);
		oceanDisplay = new OceanDisplay( new MouseFireListener( this ) );
		add(oceanDisplay);
		
		requestFiringDelay();
		
		System.out.println("firing delay, probably default (5000): "+this.firingDelay);
		
		cmdPanel = new CmdPanel(new FireListener(this), (this.firingDelay/1000));
		
		add(cmdPanel, BorderLayout.SOUTH);
		setSize(tileSize * OceanDisplay.columns
				, tileSize * OceanDisplay.rows);
		
		centerOnScreen();
		
		setVisible(true);
        
        lastTimeFired = 0;
        allShipsSunk = false;
		
		System.out.println("firing delay, possibly default (5000): "+this.firingDelay);
    }
	
	//borrowed from Calif. Speedway project
	private void centerOnScreen() {
		Toolkit toolkit = getToolkit();
		Dimension size = toolkit.getScreenSize();
		setLocation(size.width / 2 - getWidth() / 2, size.height / 2
					- getHeight() / 2);
	}
	
	private void bottomCorner() {
		Toolkit toolkit = getToolkit();
		Dimension size = toolkit.getScreenSize();
		setLocation(size.width - getWidth(), size.height - getHeight());
	}
    //--
	
	public static void main(String[] args) 
    {
//		ClientGUI cg = new ClientGUI();
	}
}