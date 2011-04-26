//Codes.java
//COS325 Project 4: Battleship Game
//Spring '11
//C. Bikle

package battleship;

import java.net.*;
import java.io.*;

public class Player implements Runnable
{
//----------CLASS VARIABLES-----------
	private Socket connectionSocket;
	private BufferedReader inFromClient;
	private DataOutputStream outToClient;
	private Server s;
	
	
//----------CONSTRUCTOR----------
	public Player(Socket conn, Server s)
	{
		this.s = s;
		connectionSocket = conn;
		
		try
		{
			// we read as a series of bytes -- for our purposes a String
			// and send the response message -- for our purposes an int
			// back to the client as an String
			
			inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			outToClient = new DataOutputStream(connectionSocket.getOutputStream());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			
			System.exit(0);
		}
	}
	
//----------RUN----------
	public void run()
	{
		try
		{
			outToClient.writeBytes(Codes.ON_JOIN + Codes.CRLF);
			
			String read = inFromClient.readLine();
			
			while(read != null)
			{
				String response = s.processMessage(read);
				System.out.println(Thread.currentThread().getId()+" "+response);
				outToClient.writeBytes((response) + Codes.CRLF);
				read = inFromClient.readLine();
				
				System.out.println(Thread.currentThread()+" read: "+read);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}