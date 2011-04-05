package battleship;

import java.awt.*;
import javax.swing.*;

public class OceanTile extends JPanel {
	String s;
	boolean hit;
	boolean miss;
	boolean ship;
    int xCoord;
    String yCoord;
    
	public OceanTile(String s) {
		super();
		this.s = s;
	}
	public OceanTile( int x, String y ) {
		this.s = null;
		hit = false;
		miss = false;
		ship = false;
        xCoord = x;
        yCoord = y;
	}
	public void paintComponent(Graphics g) {
        super.paintComponent(g);       
        if(s != null) {
        	g.setFont(new Font("SansSerif", Font.BOLD,14));
        	g.setColor(Color.white);
        	g.drawString(s, 5, 15);
        }
        if(hit){
        	g.setColor(Color.RED);
        	g.fillArc(5, 5, 15, 15, 0, 360);
        }
        if(miss){
        	g.setColor(Color.WHITE);
        	g.fillArc(5, 5, 15, 15, 0, 360);
        }
        if(ship){
        	g.setColor(Color.GRAY);
        	Dimension dim = getSize();
        	g.fillRect(0, 0, dim.width, dim.height);
        }
    }  
    
    public int getXCoord()
    {
        return this.xCoord;
    }
    
    public String getYCoord()
    {
        return this.yCoord;
    }
    
	public void setHit(boolean hit) {
		this.hit = hit;
	}
	public void setMiss(boolean miss) {
		this.miss = miss;
	}
	public void setString(String s){
		this.s = s;
	}
	public void setShip(boolean ship) {
		this.ship = ship;
	}
	public Dimension getPreferredSize() {
        return new Dimension(200,200);
    }
}
