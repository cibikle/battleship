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
	
	private final char HOST_INFO_PROMPT_CODE = 4;
	private final char PORT_INFO_PROMPT_CODE = 5;
	private final char USERNAME_INFO_PROMPT_CODE = 6;
	private final char PROMPT_COMPLETE_CODE = (char)-1;
	
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
	private String infoPrompt(char code)
	{
		String input = null;
		
		while(code != PROMPT_COMPLETE_CODE)
		{
			switch (code)
			{
				case HOST_INFO_PROMPT_CODE:
					System.out.print("Please enter a valid hostname: ");
					break;
					
				case PORT_INFO_PROMPT_CODE:
					System.out.print("Please enter a valid port number: ");
					break;
					
				case USERNAME_INFO_PROMPT_CODE:
					System.out.print("Please enter a valid username: ");
					break;
					
				default:
					break;
			}
			
			try
			{
				input = inFromUser.readLine();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			if(input.equalsIgnoreCase("quit") || input.equalsIgnoreCase("cancel"))
				quit();
			
			switch (code)
			{
				case HOST_INFO_PROMPT_CODE:
					if(setRemoteHostName(input))
						code = PROMPT_COMPLETE_CODE;
					break;
					
				case PORT_INFO_PROMPT_CODE:
					if(setPortNumber(input))
						code = PROMPT_COMPLETE_CODE;
					break;
					
				case USERNAME_INFO_PROMPT_CODE:
					if(setUserName(input))
						code = PROMPT_COMPLETE_CODE;
					break;
					
				default:
					break;
			}
		}
		
		return input;
	}
	
//----------SET HOST NAME----------
	private boolean setRemoteHostName(String s)
	{
		if(s == null || "".equals(s))
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
		if(s == null || "".equals(s))
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
	
//----------CONFIRM CONNECTION----------
	private void confirmConnection(String input)
	{
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
			System.out.print("Connection established: ");
			System.out.println(remoteHostName+" on "+port);
		}
		else
		{
			System.out.println("Connection failed\n"+"input: "+input);
			System.exit(8);
		}
	}
	
//----------SAY ELO----------
	private boolean sayElo(String input)
	{
		boolean success = false;
		
		try
		{
			outToServer.writeBytes(Codes.ELO + " " + username + Codes.CRLF);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		try
		{
			input = inFromServer.readLine();
			System.out.println(input);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		if(Codes.ELO_FIRST.equals(input))
		{
			System.out.print("name accepted; enter game size: ");
			
			while(!acquireSiz(input))
			{}
			
			success = true;
		}
		else if(Codes.ELO_NOT_FIRST.equals(input))
		{
			System.out.println("name accepted");
			
			success = true;
		}
		else if(Codes.ELO_SERVER_FULL.equals(input))
		{
			System.out.println("server full; try again later");
			System.exit(11);
		}
		else if(Codes.ELO_NAME_TAKEN.equals(input))
		{
			System.out.println("name already taken");
			infoPrompt(USERNAME_INFO_PROMPT_CODE);
		}
		else
		{
			System.out.println("stuff got mussed up; check on or near line 409 in Client");
			System.exit(12);
		}
		
		return success;
	}
	
//----------ACQUIRE SIZ----------
	private boolean acquireSiz(String input)
	{
		boolean success = false;
		
		try
		{
			String s = inFromUser.readLine();
			
			System.out.println("s: "+s);
			
			outToServer.writeBytes(Codes.SIZ +" "+ s+Codes.CRLF);
			
//			success = true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		try
		{
			input = inFromServer.readLine();
			
			System.out.println(input);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		if(Codes.OK.equals(input))
			success = true;
		
		return success;
	}
	
//----------RUN----------
	private void run()
	{
		String input = "";
		
		confirmConnection(input);
		
		while(!sayElo(input))
		{}
		
		cgui = new ClientGUI(outToServer);
		//somehow need to lock the gui so people can't start screwing around until the game begins
		
		try
		{
			input = inFromServer.readLine();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		while(input != null)
		{
			//do something
			System.out.println(input);
			
			try
			{
				input = inFromServer.readLine();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
//----------QUIT----------
	private void quit()
	{
		System.out.println("good bye");
		
		try
		{
			if(clientSocket != null)
				if(!clientSocket.isClosed())
					clientSocket.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		System.exit(10);
	}
	
//----------MAIN----------
	public static void main(String[] args) throws IOException
	{
		String[] parsedArgs = parseArgs(toArrayList(args));
		
		Client player = new Client(parsedArgs);
		
//		quit();
		clientSocket.close();
	}
}