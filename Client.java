//Client.java
//COS325 Project 4: Battleship Game
//Spring '11
//C. Bikle

package battleship;

import java.io.*;
import java.net.*;
import java.util.*;

public class Client
{
//----------CLASS VARIABLES----------
	private int port;
	
	private String username;
	private String remoteHostName;
	private String localHostName;
	
	private static Socket clientSocket;
	
	private ClientGUI cgui;
	
	private BufferedReader inFromUser;
	private DataOutputStream outToServer;
	private BufferedReader inFromServer;
	
	private int FIRING_DELAY;
	
	private final int HOST_INFO_PROMPT_CODE = 4;
	private final int PORT_INFO_PROMPT_CODE = 5;
	private final int USERNAME_INFO_PROMPT_CODE = 6;
	private final int PROMPT_COMPLETE_CODE = -1;
	
//----------CONSTRUCTOR----------
	public Client(String[] processedArgs)
	{
		this.inFromUser = new BufferedReader(new InputStreamReader(System.in));
		
		try
		{
			if(!setRemoteHostName(processedArgs[0]))
			{
				throw new Exception();
			}
		}
		catch(Exception e)
		{
//			System.out.println(processedArgs[0]);
			
			e.printStackTrace();
			
			infoPrompt(HOST_INFO_PROMPT_CODE);
		}
		
		try
		{
			if(!setPortNumber(processedArgs[1]))
				throw new Exception();
		}
		catch(Exception e)
		{
//			System.out.println(processedArgs[1]);
			
			e.printStackTrace();
			
			infoPrompt(PORT_INFO_PROMPT_CODE);
		}
		
		try
		{
			if(!setUserName(processedArgs[2]))
			   throw new Exception();
		}
		catch(Exception e)
		{
//			System.out.println(processedArgs[2]);
			
			e.printStackTrace();
			
			infoPrompt(USERNAME_INFO_PROMPT_CODE);
		}
		
		try
		{
			this.localHostName = InetAddress.getLocalHost().getHostName();
		}
		catch(UnknownHostException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		
		this.clientSocket = createSocket(this.remoteHostName, this.port);
		
		try
		{
			this.outToServer = new DataOutputStream(clientSocket.getOutputStream());
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(2);
		}
		
		try
		{
			this.inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(3);
		}
		
		System.out.println("ready");
		
		run();
	}

//----------CREATE SOCKET----------
	private Socket createSocket(String remoteHostName, int port)
	{
		Socket sock = null;
		
		while(true)
		{
			try
			{
				sock = new Socket(remoteHostName, port);
				
				break;
			}
			catch(UnknownHostException e)
			{
				e.printStackTrace();
				
				remoteHostName = infoPrompt(HOST_INFO_PROMPT_CODE);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				
				remoteHostName = infoPrompt(HOST_INFO_PROMPT_CODE);
				port = Integer.parseInt(infoPrompt(PORT_INFO_PROMPT_CODE));
			}
		}
		
		return sock;
	}
	
//----------PARSE ARGS----------
	private static String[] parseArgs(ArrayList<String> rawArgs)
	{
/*		for(String s : rawArgs)
			System.out.println(s);*/
		
		String[] processedArgs = new String[rawArgs.size()];
		
		if(rawArgs.isEmpty())
			return null;
		
		try
		{
			if(rawArgs.contains("-h"))
				processedArgs[0] = rawArgs.get((rawArgs.indexOf("-h")+1));
		}
		catch(IndexOutOfBoundsException e)
		{
			e.printStackTrace();
		}
			
		try
		{
			if(rawArgs.contains("-p"))
				processedArgs[1] = rawArgs.get((rawArgs.indexOf("-p")+1));
		}
		catch(IndexOutOfBoundsException e)
		{
			e.printStackTrace();
		}
		
		try
		{
			if(rawArgs.contains("-u"))
				processedArgs[2] = rawArgs.get((rawArgs.indexOf("-u")+1));
		}
		catch(IndexOutOfBoundsException e)
		{
			e.printStackTrace();
		}
		
/*		for(String s : processedArgs)
			System.out.println(s);*/
		
		return processedArgs;
	}
	
//----------TO ARRAY LIST----------
	private static ArrayList<String> toArrayList(String[] arhs)
	{
		ArrayList<String> als = new ArrayList<String>();
		
		for(int i = 0; i < arhs.length; i++)
		{
			als.add(arhs[i]);
		}
		
		return als;
	}
	
//----------INFO PROMPT----------
	private String infoPrompt(int code)
	{
		String input = null;
		
		while(code == HOST_INFO_PROMPT_CODE)//hostname
		{
			System.out.print("Please enter a valid hostname: ");
			
			try
			{
				input = inFromUser.readLine();
				
				if(setRemoteHostName(input))
					code = PROMPT_COMPLETE_CODE;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
			
		while(code == PORT_INFO_PROMPT_CODE)//port
		{
			System.out.print("Please enter a valid port number: ");
			
			try
			{
				input = inFromUser.readLine();
				
				if(setPortNumber(input))
					code = PROMPT_COMPLETE_CODE;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
			
		while(code == USERNAME_INFO_PROMPT_CODE)//username
		{
			System.out.print("Please enter a valid username: ");
			
			
			try
			{
				input = inFromUser.readLine();
				
				if(setUserName(input))
					code = PROMPT_COMPLETE_CODE;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		return input;
		
		//consider revising to use switch
	}
	
//----------SET HOST NAME----------
	private boolean setRemoteHostName(String s)
	{
		if(s == null)
			return false;
		
		try
		{
			this.remoteHostName = s;
		}
		catch(Exception e)
		{
			return false;
		}
		
		return true;
	}
	
//----------SET PORT NUMBER----------
	private boolean setPortNumber(String s)
	{
		if(s == null)
			return false;
		
		try
		{
			this.port = Integer.parseInt(s);
		}
		catch(Exception e)
		{
			return false;
		}
		
		return true;
	}
	
//----------SET USER NAME----------
	private boolean setUserName(String s)
	{
		if(s == null)
			return false;
		
		try
		{
			this.username = s;
		}
		catch(Exception e)
		{
			return false;
		}
		
		return true;
	}
	
//----------RUN----------
	private void run()
	{
		String input = "";
		
		try
		{
			input = inFromServer.readLine();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(7);
		}
		
		if(Codes.ON_JOIN.equals(input))
		{
			System.out.print("Connection established:  ");
			System.out.println(remoteHostName+" on "+port);
		}
		else
		{
			System.out.println("Connection failed\n"+"input: "+input);
			System.exit(8);
		}
		
		try
		{
			outToServer.writeBytes("ELO"+" "+username+" "+localHostName+'\n');
			
			input = inFromServer.readLine();
			System.out.println(input);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
//----------MAIN----------
	public static void main(String[] args) throws IOException
	{
		String[] parsedArgs = parseArgs(toArrayList(args));
		
		Client player = new Client(parsedArgs);
		
		clientSocket.close();
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
		
/*		portNum = Integer.parseInt(args[1]);
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
		
		clientSocket.close();*/
	}
}