//Creates a 'com system' GUI for the Battleship program.
//Written by Jed Jones

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class ChatWindow extends JFrame
{
	//Needs to be able to get the username here
	private String userName = " uN ";
	
	private JTextArea previousChat;
	private JTextArea currentChat;
	private JButton sendButton;
		
	public ChatWindow()
	{
		setTitle("Battleship Com");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		add(buildPanelRead(), BorderLayout.NORTH);
		add(buildPanelTalk(), BorderLayout.CENTER);
		add(buildPanelButton(), BorderLayout.SOUTH);
		
		pack();
		setVisible(true);
	}
	
	//Listens for the send button, calls send chat and clears the box
	private class SendButtonListener implements ActionListener 
	{
    	public void actionPerformed(ActionEvent e)
    	{
    		String input = currentChat.getText();
	    	sendChat(input);
	    	currentChat.setText("");
    	}
    }

	//Network code in here?
	public void sendChat(String line)
	{
		previousChat.append(userName + ": " + line + "\n");
	} 
		
	//Creates the scroll pane with the previous chat stuff
	private JPanel buildPanelRead() 
	{		
		previousChat = new JTextArea(20,20);
		previousChat.setEditable(false);
		previousChat.setLineWrap(true);
		JScrollPane scrollPane = new JScrollPane(previousChat);
		scrollPane.setBorder(BorderFactory.createTitledBorder("Tactical Data Feed"));
			
		JPanel panel = new JPanel();
		panel.add(scrollPane);
		return panel;
	}	
	
	//Creates the scroll pane where chat stuff can be typed 
	private JPanel buildPanelTalk() 
	{
		currentChat = new JTextArea(3,20);
		currentChat.setEditable(true);
		currentChat.setLineWrap(true);
		JScrollPane scrollPane = new JScrollPane(currentChat);
	
		JPanel panel = new JPanel();
		panel.add(scrollPane);
		return panel;
	}
	
	//Creates the 'send' button
	private JPanel buildPanelButton() 
	{
		sendButton = new JButton("Send");
		sendButton.addActionListener(new SendButtonListener());
		
		JPanel panel = new JPanel();
		panel.add(sendButton);
		return panel;
	}
	
	public static void main(String[] args)
	{
		ChatWindow l = new ChatWindow();
	}
}
