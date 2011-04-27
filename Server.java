// Paul McLain
// COS325 -- Networking
// Battleship Project
// Server.java

package battleship;

//modified by C. Bikle 4/12/11

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.*;

public class Server
{
	private int port; 
	private ServerSocket serverSocket; 
	private int numPlayers; 
	private int serverLimit; 
	private ArrayList<String> playerList; 
	private ArrayList<InetAddress> IPAddrs; 
	private ServerGlobalMap sgm; 
	private ShipList shipList; 
	private Semaphore firstPlayerSema = new Semaphore(1, true);
	private Lock lock = new ReentrantLock();
	
	private static final int DEFAULT_PORT = 8011;

//----------DEFAULT CONSTRUCTOR----------
	Server()
	{
		port = DEFAULT_PORT; 
		
		numPlayers = 0;
		serverLimit = 0; 
		playerList = new ArrayList<String>(); 
		IPAddrs = new ArrayList<InetAddress>(); 
		sgm = ServerGlobalMap.getServerGlobalMap(); 
		shipList = ShipList.getShipList();
		
		try
		{
			serverSocket = new ServerSocket(port);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		} 
	}
	
//----------ALTERNATIVE CONSTRUCTOR----------
	Server(int port)
	{
		this.port = port; 
		
		numPlayers = 0;
		serverLimit = 0; 
		playerList = new ArrayList<String>(); 
		IPAddrs = new ArrayList<InetAddress>(); 
		sgm = ServerGlobalMap.getServerGlobalMap(); 
		shipList = ShipList.getShipList();
		
		try
		{
			serverSocket = new ServerSocket(port);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		} 
	}
	
	// awkward implementation of a copy constructor
	Server(Server s)
	{ 
		this.port = s.port;
		this.serverSocket = s.serverSocket; 
		this.numPlayers = s.numPlayers; 
		this.playerList = s.playerList; 
		this.sgm = s.sgm; 
		this.shipList = s.shipList; 
	}
	
	// this might have to be threaded so
	// that multiple clients can connect
	// but the variables will need to be locked
	// and somehow protected.
	
//----------RUN SERVER----------
	public void runServer() throws IOException
	{
		System.out.println("Server running on port " + port);
		
		while(true)
		{ 
			Socket connectionSocket = serverSocket.accept();
			
			// check first to see who our client is IP-wise
			// we should write the IP address to match the name 
			
			InetAddress ip = connectionSocket.getInetAddress();
			
			if(!ipInSystem(ip))
			{
				IPAddrs.add(ip);
				
				System.out.println(ip);
			}
			
			Player p = new Player(connectionSocket, this);
			Thread t = new Thread(p);
			t.start();
		}
	}
	
	// slop
/*	private void placeShips(int playerNumber) throws ShipOutOfBoundsException, ShipOverlapException {
		// ships range from 2 to 5 long
		Random r = new Random(); 
		Coordinates c[][] = new Coordinates[5][];
		int[] sizes = { 2, 3, 3, 4, 5 };
		for ( int i = 0; i < c.length; i++ ) {
			c[i] = new Coordinates[sizes[i]]; 
			int h = r.nextInt(26);
			int w = r.nextInt(39);
			
			while ( (h + sizes[i] > 26 || h - sizes[i] < 0) || (w + sizes[i] > 39 || w - sizes[i] < 0 )) {
				h = r.nextInt(25);
				w = r.nextInt(38);
			}
			
			for ( int j = 0; j < c[i].length; j++ ) { 
				c[i][j] = new Coordinates(h, w); 
				if ( i % 2 == 0 ) {
					h++;
				}
				else {
					w++;
				}
			}
			
			Ship s = new Ship(c[i], i, c[i].length);
			this.shipList.add(s, i); 
 		}
		
		for ( int i = 0; i < shipList.length(); i++ ) {
			this.sgm.addShip(shipList.get(i), i); 
		}
	}*/
	
//----------IP IN SYSTEM----------
	private boolean ipInSystem(InetAddress ip)
	{ 
		if(IPAddrs.contains(ip))
		{
			return true; 
		}
		
		return false; 
	}
	
//----------MSG----------
	private int msg(String msg)
	{
		String remainder = msg.substring(3, msg.length()); 
		
		if(remainder.indexOf("|") != -1)
		{
			String text = remainder.substring(remainder.indexOf("|")+1, remainder.length());
			System.out.println("001 " + text); 	
			return 001; 
		}
		
		return 251;
	}
	
//----------ELO----------
	private String elo(String msg)
	{
		String name = msg.substring(3, msg.length()).trim();
		boolean foundName = false;
		
		if(playerList.contains(name))
		{
			foundName = true; 
		}
		
		if(foundName != true)
		{ 
			if(firstPlayerSema.tryAcquire())
			{
				playerList.add(name);
				System.out.println("310 Added first player " + name + ". Send SIZ");
				numPlayers = 1; 
				
				return Codes.ELO_FIRST;
			}
			else 
			{ 
				if(playerList.size() == serverLimit)
				{ 
					System.out.println("390 Server full"); 
					
					return Codes.ELO_SERVER_FULL;
				}
				else
				{ 
					playerList.add(name);
					numPlayers++; 
					System.out.println("350 Added player " + name);
					
					return Codes.ELO_NOT_FIRST;
				}
			}
		}
		else if(foundName == true)
		{
			System.out.println("351 Username " + name + " already taken");
			
			return Codes.ELO_NAME_TAKEN; 
		}
		
		return Codes.NOT_OK; 
	}
	
	/**
	 * @author Adam Clason
	 * Checks if a ship was hit with the coordinate parameters 
	 * supplied by the string paramater, and sets its boolean flag 
	 * for the coordinate's ship status. Returns the corresponding 
	 * response code. 
	 * @param msg
	 * @return whether or not a ship was hit. 
	 */
//----------FIR----------
	private String fir(String msg)
	{
		String response = Codes.NOT_OK;
		
		String rowCol = "";
		
		try
		{
			//
			// Get the row and column string from the fire command 
			//
			 rowCol = msg.substring(msg.indexOf(' '), msg.length()).trim(); 
			//
			// Get the row and column as integers so we 
			// can see if the coordinates passed from the 
			// client hit a ship on the server map 
			//
			char row = rowCol.charAt(0);
			String col = rowCol.substring(1);
			row = Character.toUpperCase(row);
			int rowIdx = (row - 'A') + 1;         // skip the header
			int colIdx = Integer.valueOf(col);   // skip the header
			//
			// Create a coordinate object that can be 
			// passed to the server map 
			//
			Coordinates coord = new Coordinates(colIdx, rowIdx); 
			//
			// Ask the server global map if the coordinates intersect
			// a ship on it's map. 
			//
			int reportIndicator = sgm.report(coord);
			//
			// A -1 is returned from the server map if the
			// supplied coordinate does not intersect a ship. 
			// Otherwise, the number returned is the index in 
			// the array of ships where the intersected ship is located.
			//
			if(reportIndicator >= 0)
			{
				Ship hitShip = shipList.get(reportIndicator);
				//
				// Get the hit ships coordinates. We need to do this 
				// in order to check if that coordinate has already been hit.
				//
				Coordinates[] hitShipCoords = hitShip.getCoords(); 
				//
				// Find which coordinate was hit 
				//
				for(int i = 0; i < hitShipCoords.length; i++)
				{
					// 
					// Check if the ship is already sunk. If not, see
					// if the coordinate is a hit (meaning that the coordinates
					// overlap and the coordinate has not already been hit.
					//
					if((!hitShip.isSunk) && hitShipCoords[i].isHit(coord))
					{
						response = Codes.FIR_HIT; 
						hitShip.checkSunk();
						if(hitShip.isSunk() == true)
						{
							// notify the client whose ship was sunk
						}
					}
				}
			}
			else
			{
				response = Codes.FIR_MISS; 
			}
		}
		catch (Exception ex)
		{
			response = Codes.NOT_OK; 
//			throw new Exception("Error processing targetted coordinates!");
		}
		
		return response + " " + rowCol; 
	}
	
//----------BYE----------
	private int bye(String msg)
	{
		// TODO: remove this user's ships
		// and inform other users of the change
		return 900;
	}
	
//----------SIZ----------
	private String siz(String msg)
	{
		System.out.println("size: " + this.serverLimit);
		
		lock.lock();
		
		try
		{
			firstPlayerSema.release();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		
		if(firstPlayerSema.tryAcquire())
		{ 
			try
			{
				lock.unlock();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			try
			{
				int size = Integer.parseInt(msg.substring(3, msg.length()).trim());
				this.serverLimit = size; 
				System.out.println("250 OK");
				return Codes.OK; 
			}
			catch(Exception e)
			{ 
				System.out.println("252 Not a valid number"); 
				return Codes.SIZ_NAN;
			}
		}
		else
		{ 
			try
			{
				lock.unlock();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			System.out.println("251 Not First Player");
			return Codes.NOT_OK; 
		}
	}
	
	
//----------PROCESS MESSAGE----------
	public String processMessage(String msg)
	{ 
		System.out.println(Thread.currentThread().getId()+" msg: "+msg);
		
		if(msg.length() < 3)
		{ 
			System.err.println("251 Message too short"); 
			return Codes.NOT_OK; 
		}
		
		// reacting to various codes found in the 
		// battleship protocol (bsProtocol for short)
		
		String m = msg.substring(0, 3);
		
		if(m.equalsIgnoreCase("ELO"))
		{ 
			return elo(msg);
		}
		
		if(m.equalsIgnoreCase("SIZ"))
		{
			return siz(msg);
		}
		
		if(m.equalsIgnoreCase("BYE"))
		{
			// TODO: disconnect particular client 
			// and remove their ships
			// I don't see a remove ship option
//			return bye(msg);
		}
		
		if(m.equalsIgnoreCase("MSG"))
		{ 
//			return msg(msg);
		}
		
		if(m.equalsIgnoreCase("FIR"))
		{
			// someone else can handle this I think
			
			return fir(msg);
		}
		
		// something went bad wrong
		return Codes.NOT_OK;
	}
	
//----------MAIN----------
	public static void main(String args[]) throws IOException
	{
		System.out.println("starting");
		Server s = new Server();
		
		if(args.length > 0)
		{
			int portNum = 0;
			
			try
			{
				portNum = Integer.parseInt(args[0]);
			}
			catch(NumberFormatException e)
			{
				e.printStackTrace();
			}
			
			if(portNum != 0 && portNum != DEFAULT_PORT)
				s = new Server(portNum);
		}
		
		s.runServer();
	}
}
