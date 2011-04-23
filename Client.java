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
	private static String username;
	private static String remoteHostName;
	private static String localHostName;
	
	private static Socket clientSocket;
	
	private ClientGUI cgui;
	
//----------CONSTRUCTOR----------
	public Client()
	{
		
	}
	
//----------MAIN----------
	public static void main(String[] args) throws IOException
	{
		String sentence = "";
		String modSentence = "";
		
		int portNum = 0;
		
/*		if ( args.length > 0 )
		{
//			int portNum = 0;
			
			try
			{
				portNum = Integer.parseInt(args[0]);
			}
			catch(NumberFormatException e)
			{
				e.printStackTrace();
			}
		}*/
		
		portNum = Integer.parseInt(args[1]);
		remoteHostName = args[0];
		
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		
		clientSocket = new Socket(remoteHostName, portNum);
		
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		
		System.out.println("ready");
		
		modSentence = inFromServer.readLine();
		
		System.out.println("FROM SERVER: " + modSentence);
		
		while(true)
		{
			sentence = inFromUser.readLine();
			
			if(sentence.equals("quit"))
			{
				break;
			}
			
			outToServer.writeBytes(sentence+"\n");
			modSentence = inFromServer.readLine();
			
			System.out.println("FROM SERVER: " + modSentence);
		}
		
		clientSocket.close();
	}
}