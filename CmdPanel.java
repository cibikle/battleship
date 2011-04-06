package battleship;

import javax.swing.*;
import java.awt.event.*;
import java.awt.GridLayout;
//import java.

public class CmdPanel extends JPanel 
{
	private JTextField rowEntry;
	private JTextField columnEntry;
	private JButton fireBtn;
    private JTextField systemMessage;
    private JPanel topPanel;
    private JPanel bottomPanel;
	
	private CooldownTimer cdt;
    
    private int clickCount;
    
	public JTextField getRowEntry() 
    {
		return rowEntry;
	}
    
    public void setRowEntry( String entry )
    {
        rowEntry.setText( entry );
    }
    
	public JTextField getColumnEntry() 
    {
		return columnEntry;
	}
    
    public void setColumnEntry( String entry )
    {
        columnEntry.setText( entry );
    }
    
	public JButton getFireBtn() 
    {
		return fireBtn;
	}
	
	public CooldownTimer getCooldownTimer()
	{
		return cdt;
	}
    
    public void displaySystemMessage( String message )
    {
        systemMessage.setText( message );
    }
    
    private class TextFieldListener implements MouseListener
    {
        //What can I say?  I like StarCraft.
        public void mouseClicked( MouseEvent e )
        {
            if( clickCount % 14 == 0 )
            {
                displaySystemMessage( "I'm about t' overload my aggression inhibitors!" );
                clickCount = 1;
            }
            else if( clickCount % 12 == 0 )
            {
                displaySystemMessage( "Keep it up!  I dare ya!" );
            }
            else if( clickCount % 10 == 0 )
            {
                displaySystemMessage( "...now reap the whirlwind!" );
            }
            else if( clickCount % 8 == 0 )
            {
                displaySystemMessage( "You called down the thunder..." );
            }
            else if( clickCount % 6 == 0 )
            {
                displaySystemMessage( "Finally" );
            }
            else if( clickCount % 4 == 0 )
            {
                displaySystemMessage( "I'm here" );
            }
            else if( clickCount % 2 == 0 )
            {
                displaySystemMessage( "Ghost reportin'" );
            }
            clickCount++;
        }
        
        public void mouseExited( MouseEvent e ) {}
        
        public void mouseEntered( MouseEvent e ) {}
        
        public void mousePressed( MouseEvent e ) {}
        
        public void mouseReleased( MouseEvent e ) {}
    }
    
	public CmdPanel( ActionListener btnListener, int time )
    {
        setLayout( new GridLayout( 2, 1 ) );
		
        topPanel = new JPanel();
		topPanel.add(new JLabel("Row"));
		
		rowEntry = new JTextField(2);
		topPanel.add(rowEntry);
		
		topPanel.add(new JLabel("Column"));
		columnEntry = new JTextField(2);
		topPanel.add(columnEntry);
		
		//
		cdt = new CooldownTimer(time);
		//
		
		fireBtn = new JButton("Fire!");
		fireBtn.addActionListener(btnListener);
		topPanel.add(fireBtn);
        //Works best if you hear it in the voice of StarCraft Ghosts.
        systemMessage = new JTextField( "Call the shot", 25 );
        systemMessage.setEditable( false );
        systemMessage.addMouseListener( new TextFieldListener() );
        clickCount = 1;
        
        bottomPanel = new JPanel();
        bottomPanel.add( systemMessage );
        
        add( topPanel );
        add( bottomPanel );
	}
}
