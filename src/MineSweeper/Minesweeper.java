package MineSweeper;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Minesweeper implements ActionListener{
	
	JFrame frame = new JFrame("Minesweeper");
	JButton reset = new JButton("Reset");
	JButton[][] buttons = new JButton[20][20];
	//buttons that the user sees and interacts with
	int[][] counts = new int[20][20];
	//the info that corresponds with buttons
	Container grid = new Container();
	final int MINE = 10;
	
	public Minesweeper() {
		frame.setSize(1000, 700);
		frame.setLayout(new BorderLayout());
		frame.add(reset, BorderLayout.NORTH);
		reset.addActionListener(this);
		
		//button grid
		grid.setLayout(new GridLayout(20,20));
		for (int i = 0; i < buttons.length; i++) {
			for (int j = 0; j < buttons[0].length; j++) {
				buttons[i][j] = new JButton();
				buttons[i][j].addActionListener(this);
				//buttons[i][j].addMouseListener(null);
				grid.add(buttons[i][j]);
			}
		}
		frame.add(grid, BorderLayout.CENTER);
		createRandomMines();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	
	public static void main(String[] args) {
		new Minesweeper();
	}

	public void createRandomMines() {
		// creating a list of all possible squares 
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int x = 0; x < counts.length; x++) {
			for (int y = 0; y < counts[0].length; y++) {
				list.add(x*100+y);
				//this allows us to get x by dividing by 100
				// and y by modulo 100
			}
		}
		
		// reset counts array, set 30 mines
		counts = new int[20][20];
		for (int i = 0; i < 30; i++) {
			int choice = (int) (Math.random() * list.size());
			counts[list.get(choice)/100][list.get(choice)%100] = MINE;
			list.remove(choice);
			//ensures you get a new mine every time
		}
		
		//neighbor counts
		for (int x = 0; x < counts.length; x++) {
			for (int y = 0; y < counts[0].length; y++) {	
				if (counts[x][y] != MINE) {
					int neighbor_count = 0;
					//up
					if (y > 0 && counts[x][y-1] == MINE) {
						neighbor_count++;
					}
					// up left  
					if (x > 0 && y > 0 && counts[x-1][y-1] == MINE) {
						neighbor_count++;
					}
					//up right
					if (y > 0 && x < counts.length - 1 && counts[x+1][y-1] == MINE) {
						neighbor_count++;
					}				
					//bottom
					if (y < counts[0].length - 1 && counts[x][y+1] == MINE) {
						neighbor_count++;
					}
					//bottom left
					if (y < counts[0].length - 1 && x > 0 && counts[x-1][y+1] == MINE) {
						neighbor_count++;
					}
					//bottom right
					if (y < counts[0].length - 1 && x < 19 && counts[x+1][y+1] == MINE) {
						neighbor_count++;
					}
					//left
					if (x > 0 && counts[x-1][y] == MINE) {
						neighbor_count++;
					}
					//right
					if (x < counts.length - 1 && counts[x+1][y] == MINE) {
						neighbor_count++;
					}
					
					counts[x][y] = neighbor_count;
				}
			}
		}
	}
	
	public void gameOver() {
		for (int x = 0; x < buttons.length; x++) {
			for(int y = 0; y < buttons[0].length; y++) {
				if (buttons[x][y].isEnabled()){
					if (counts[x][y] != MINE) {
						buttons[x][y].setText(counts[x][y] + "");
						buttons[x][y].setEnabled(false);
					} else {
						buttons[x][y].setText("X");
						buttons[x][y].setForeground(Color.black);
						buttons[x][y].setEnabled(false);
					}
				}
			}
		}
		JOptionPane.showMessageDialog(frame, "YOU LOST! X(");
	}
	
	public void clearZeros(ArrayList<Integer> clear) {
		if (clear.size() == 0) {
			return;
		} else {
			int x = clear.get(0) / 100;
			int y = clear.get(0) % 100;
			clear.remove(0);
			
			// up
			if (y > 0 && buttons[x][y-1].isEnabled()) { 
				buttons[x][y-1].setText(counts[x][y-1] + "");
				buttons[x][y-1].setEnabled(false);
				if(counts[x][y-1] == 0) {
					clear.add(x*100+(y-1));
				}
			}
			//up left
			if (x > 0 && y > 0 && buttons[x-1][y-1].isEnabled()) { 
				buttons[x-1][y-1].setText(counts[x-1][y-1] + "");
				buttons[x-1][y-1].setEnabled(false);
				if(counts[x-1][y-1] == 0) {
					clear.add((x-1)*100+(y-1));
				}
			}
			//up right
			if (x < counts.length - 1  && y > 0 && buttons[x+1][y-1].isEnabled()) { 
				buttons[x+1][y-1].setText(counts[x+1][y-1] + "");
				buttons[x+1][y-1].setEnabled(false);
				if(counts[x+1][y-1] == 0) {
					clear.add((x+1)*100+(y-1));
				}
			}
			//bottom
			if (y < counts[0].length - 1 && buttons[x][y+1].isEnabled()) { 
				buttons[x][y+1].setText(counts[x][y+1] + "");
				buttons[x][y+1].setEnabled(false);
				if(counts[x][y+1] == 0) {
					clear.add(x*100+(y+1));
				}
			}
			//bottom left
			if (x > 0 && y < counts[0].length - 1 && buttons[x-1][y+1].isEnabled()) { 
				buttons[x-1][y+1].setText(counts[x-1][y+1] + "");
				buttons[x-1][y+1].setEnabled(false);
				if(counts[x-1][y+1] == 0) {
					clear.add((x-1)*100+(y+1));
				}
			}
			//bottom right
			if (x < counts.length - 1 && y < counts[0].length - 1 && buttons[x+1][y+1].isEnabled()) { 
				buttons[x+1][y+1].setText(counts[x+1][y+1] + "");
				buttons[x+1][y+1].setEnabled(false);
				if(counts[x+1][y+1] == 0) {
					clear.add((x+1)*100+(y+1));
				}
			}
			//left
			if (x > 0 && buttons[x-1][y].isEnabled()) { 
				buttons[x-1][y].setText(counts[x-1][y] + "");
				buttons[x-1][y].setEnabled(false);
				if(counts[x-1][y] == 0) {
					clear.add((x-1)*100+(y));
				}
			}
			//right 
			if (x < counts.length -1 && buttons[x+1][y].isEnabled()) { 
				buttons[x+1][y].setText(counts[x+1][y] + "");
				buttons[x+1][y].setEnabled(false);
				if(counts[x+1][y] == 0) {
					clear.add((x+1)*100+(y));
				}
			}
			
			clearZeros(clear); // recursive 
		}
	}
	
	public void checkWin() {
		boolean won = true;
		for (int x = 0; x < buttons.length; x++) {
			for(int y = 0; y < buttons[0].length; y++) {
				if (buttons[x][y].isEnabled() && counts[x][y] != MINE) {
					won = false;
				}
			}
		}
		if (won) {
			JOptionPane.showMessageDialog(frame, "YOU WON!:D");
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(reset)) {
			for (int x = 0; x < buttons.length; x++) {
				for(int y = 0; y < buttons[0].length; y++) {
					buttons[x][y].setEnabled(true);
					buttons[x][y].setText("");
				}
			}
			createRandomMines();
		}else { // buttons
			for (int x = 0; x < buttons.length; x++) {
				for(int y = 0; y < buttons[0].length; y++) {
					if (e.getSource().equals(buttons[x][y])) {
//						if(SwingUtilities.isRightMouseButton(null)) {
//							buttons[x][y].setText("!");
//						}
						if(counts[x][y] == MINE) {
							int rightClick = 3;
							int leftlick = 1;
							//buttons[x][y].isRightMouseButton();
									
							buttons[x][y].setForeground(Color.red);
							buttons[x][y].setText("X");
							gameOver();
						} else if (counts[x][y] == 0) {
							buttons[x][y].setText(counts[x][y] + "");
							buttons[x][y].setEnabled(false);
							ArrayList<Integer> clear = new ArrayList<Integer>();
							clear.add(x*100+y);
							clearZeros(clear);
							checkWin();
						}else {
							buttons[x][y].setText(counts[x][y] + "");
							buttons[x][y].setEnabled(false);
							checkWin();
						}
					}
				}
			}
		}
	}
}
