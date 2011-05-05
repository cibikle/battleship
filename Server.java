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
	private Boolean gameBegun = false;
	private Starter s;
	private ShipGenerator utopiaPlanetia;
	private Ship[] ships;
	private int curPlayer = 0;
	
	private static final int SHIP_MAX_SIZE = 5;
	private static final int SHIPS_PER_PLAYER = 5;
	private static final int SERVER_MAX = 8;
	
	private static final int FIRING_DELAY = 2000;//in millis
	
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
		
		s = new Starter(this);
		Thread starter = new Thread(s);
		starter.start();
		
		while(true)
		{ 
			Socket connectionSocket = serverSocket.accept();
			
			// check first to see who our client is IP-wise
			// we should write the IP address to match the name 
			
			InetAddress ip = connectionSocket.getInetAddress();
			
			System.out.println(ip);
			
			if(!ipInSystem(ip))
			{
				IPAddrs.add(ip);
			}
			
			Player p = new Player(connectionSocket, this);
			Thread t = new Thread(p);
			t.start();
			
			s.addClient(new DataOutputStream(connectionSocket.getOutputStream()));
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
		
		String response = Codes.NOT_OK;
		
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
				
				response = Codes.ELO_FIRST;
			}
			else 
			{ 
				if(numPlayers == serverLimit)
				{ 
					System.out.println("390 Server full"); 
					
					response = Codes.ELO_SERVER_FULL;
				}
				else if(serverLimit == 0)
				{
					response = Codes.NOT_OK+" SIZ not set";
				}
				else
				{ 
					playerList.add(name);
					numPlayers++; 
					System.out.println("350 Added player " + name);
					
					response = Codes.ELO_NOT_FIRST;
				}
			}
		}
		else if(foundName == true)
		{
			System.out.println("351 Username " + name + " already taken");
			
			response = Codes.ELO_NAME_TAKEN; 
		}
		
		if(numPlayers == serverLimit && serverLimit > 1)
		{
			System.out.println("yo");
			begin("");
		}
		
		return response; 
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
			Coordinates coord = new Coordinates(colIdx, row); 
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
				System.out.println("*");
				
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
						System.out.println("***");
						
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
				
				if(size > SERVER_MAX)
					throw new Exception();
				
				this.serverLimit = size;
				System.out.println("250 OK");
				utopiaPlanetia = new ShipGenerator(serverLimit, SHIP_MAX_SIZE, SHIPS_PER_PLAYER);
				ships = utopiaPlanetia.generateShips(serverLimit);
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
	
//----------FIRING DELAY----------
	private String fd(String msg)
	{
		return Codes.FIRING_DELAY_CODE+" "+FIRING_DELAY;
	}
	
//----------BEGIN----------
	private String begin(String msg)
	{
		System.out.println("hi");
		if(numPlayers == serverLimit && gameBegun == false)
		{
			synchronized (gameBegun)
			{
				gameBegun.notify();
			}
			return Codes.OK;
		}
		else
			return Codes.NOT_OK;
	}
	
//----------SHIP PLACEMENT----------
	private String shipPlacement()
	{
		//how do we figure out which player is which?
		
		int x = curPlayer * SHIPS_PER_PLAYER;
		String ship = "";
		
		for(int j = x; j < (SHIPS_PER_PLAYER * (curPlayer + 1)); j++)
		{
			for(int i = 0; i < ships[j].coords.length; i++)
			{
				ship += toChar(ships[j].coords[i].getRow()) + "" + ships[j].coords[i].getColumn() + ":";
			}
			
			ship = ship.substring(0, ship.lastIndexOf(":"));
			
			ship += "/";
		}
		
		curPlayer++;
		
		return Codes.SHIP_PLACEMENT+ " " + ship.substring(0, ship.lastIndexOf("/"));
	}
	
//----------TO CHAR----------
	private char toChar(int row)
	{
		System.out.println("row: "+row);
		
		char result;
		
		result = Codes.ROW_LABELS.charAt(row);
		
		System.out.println("result: "+result);
		
		return result;
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
			return fir(msg);
		}
		if(m.equalsIgnoreCase("FDC"))
			return fd(msg);
		if(m.equalsIgnoreCase(Codes.BGN))
			return begin(msg);
		if(m.equalsIgnoreCase(Codes.SHP))
			return shipPlacement();
		
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
	
	private class Starter implements Runnable
	{
		ArrayList<DataOutputStream> toClients = new ArrayList<DataOutputStream>();
		Server s;
		
		public Starter(Server s)
		{
			System.out.println("Starter constructed");
			this.s = s;
		}
		
		public void addClient(DataOutputStream clientStream)
		{
			System.out.println("added client stream");
			toClients.add(clientStream);
		}
		
		private void contactClients(String msg)
		{
			for(DataOutputStream outToClient : toClients)
			{
				System.out.println("contacting "+outToClient);
				try
				{
					outToClient.writeBytes(msg+Codes.CRLF);
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
			
			gameBegun = true;
		}
		
		public void run()
		{
			System.out.println("Starter started");
			synchronized (s.gameBegun)
			{
				try
				{
					System.out.println("Starter waiting");
					s.gameBegun.wait();
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
					System.exit(14);
				}
				
				System.out.println("Starter woken");
			}
			
			try
			{
				System.out.println("Starter sleeping");
				Thread.sleep(2000);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
				System.exit(15);
			}
			
			System.out.println("Starter awake");
			
			contactClients(Codes.BEGIN);
		}
	}
}
