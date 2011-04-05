package battleship;
import javax.swing.*;
import java.awt.*;

public class ScorePanel extends JPanel {
	private int score;
	private String msg;
	private JLabel label;
	public ScorePanel(){
		score = 0;
		msg = "Your score is: ";
		label = new JLabel(msg+score, SwingConstants.CENTER);
		label.setFont(new Font("SansSerif", Font.BOLD,24));
		add(label);
	}
	public void incScore() {
		score++;
		label.setText(msg+score);
	}
	public void resetScore(){
		score=0;
		label.setText(msg+score);
	}
	/*
	public void paintComponent(Graphics g) {
        super.paintComponent(g);     
        
	}
	*/
}
