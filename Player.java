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
			inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			outToClient = new DataOutputStream(connectionSocket.getOutputStream());
		}
		catch (Exception e)
		{
			e.printStackTrace();
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
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}