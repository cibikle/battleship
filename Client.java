//Client.java
//COS325 Project 4: Battleship Game
//Spring '11
//C. Bikle

package battleship;

import java.io.*;
import java.net.*;

public class Client
{
//----------CLASS VARIABLES----------
	private int port;
	private String username;
	private String remoteHostName;
	private String localHostName;
	
	private Socket clientSocket;
	
	private ClientGUI cgui;
	
//----------CONSTRUCTOR----------
	public Client()
	{
		
	}
	
//----------MAIN----------
	public static void main(String[] args) throws IOException
	{
		if(args.length != 2)
		{
			System.out.println();
		}
	}
}