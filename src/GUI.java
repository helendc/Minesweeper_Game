import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class GUI extends JFrame {
	
	Date startDate = new Date();
	public int date_x = 1100;
	public int date_y = 5;
	public int sec = 0;
	
	boolean hit_mine = false;
	
	int spacing = 5;
	
	public int mouse_x = -100;
	public int mouse_y = -100;
	
	public int reset_x = 605;
	public int reset_y = 5;
	
	Random rand = new Random();
	
	int num_of_neighbors = 0;
	
	int[][] mines = new int[16][9];
	int[][] neighbors = new int[16][9];
	boolean[][] revealed = new boolean[16][9];
	boolean[][] flagged = new boolean[16][9];
	
	public GUI() {
		//this will be the window 
		this.setTitle("MineSweeper");
		this.setSize(1286, 839);
		//dimensions account for border size 
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);
		
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 9; j++) {
				if(rand.nextInt(100) < 20) {
					mines[i][j] = 1;
				} else {
					mines[i][j] = 0 ;
				}
				revealed[i][j] = false;
			}
		}
		
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 9; j++) {
				num_of_neighbors = 0;
				for (int m = 0; m < 16; m++) {
					for (int n = 0; n < 9; n++) {
						if(isNeighbor(i,j,m,n)) {
							num_of_neighbors++;
						}
					}
				}
				neighbors[i][j] = num_of_neighbors;
			}
		}
		
		Board board = new Board();
		// relate the board to GUI
		this.setContentPane(board);
		
		Move move = new Move();
		this.addMouseMotionListener(move);
		
		Click click = new Click();
		this.addMouseListener(click);
	}

	public class Board extends JPanel {
		public void paintComponent(Graphics g) {
			g.setColor(new Color(51, 153, 255));
			g.fillRect(0, 0, 1280, 809);
			for(int i = 0; i < 16; i++) {
				for (int j = 0; j < 9; j++) {
					g.setColor(new Color(0, 0, 153));
//					if(mines[i][j] == 1) {
//						g.setColor(Color.RED);
//					}
					if(revealed[i][j] && mines[i][j] != 1) {
						g.setColor(Color.white);
					} 
					if (mouse_x >= spacing+i*80 && mouse_x < spacing+i*80+80-2*spacing
							&& mouse_y >= spacing+j*80+80+30 && mouse_y < spacing+j*80+80+30+80-2*spacing) {
						g.setColor(Color.white);
					}
					g.fillRect(spacing+i*80, spacing+j*80+80, 80-2*spacing, 80-2*spacing);
					if(revealed[i][j]) {
						if (neighbors[i][j] != 0 && mines[i][j] == 0) {
							
							if(neighbors[i][j] == 1) {
								g.setColor(Color.blue);
							} else if (neighbors[i][j] == 2) {
								g.setColor(Color.green);
							} else if(neighbors[i][j] == 3) {
								g.setColor(Color.yellow);
							} else if(neighbors[i][j] == 4) {
								g.setColor(Color.red);
							} else if(neighbors[i][j] == 5) {
								g.setColor(new Color(153, 0, 0));
							} else if(neighbors[i][j] == 6) {
								g.setColor(new Color(102, 0, 153));
							} else if(neighbors[i][j] == 7) {
								g.setColor(Color.magenta);
							} else if(neighbors[i][j] == 8) {
								g.setColor(Color.black);
							} 
							
							g.setFont(new Font("Tahoma", Font.BOLD, 40));
							g.drawString(Integer.toString(neighbors[i][j]), i*80+30, j*80+80+55);
							
						} else if(mines[i][j] == 1) {
							g.setColor(Color.red);
							g.fillRect(i*80+30, j*80+80+20, 20, 40);
							g.fillRect(i*80+20, j*80+80+10+20, 40, 20);
							g.fillRect(i*80+5+20, j*80+80+5+20, 30, 30);
							hit_mine = true;
						}
					}
				}
			}
			
			//reset button paint 
			g.setColor(new Color(0,0,204));
			g.fillRect(reset_x, reset_y, 100, 70);
			g.setColor(Color.BLACK);
			g.setFont(new Font("Tahoma", Font.BOLD, 20));
			g.drawString("RESET", reset_x+15, reset_y+40);
			// add 
			
			//timer paint
			g.setColor(new Color(0,0,204));
			g.fillRect(date_x, date_y, 100, 70);
			sec = (int) ((new Date().getTime() - startDate.getTime()) / 1000);
			
			if(sec > 999) {
				sec=999;
			}
			g.setColor(Color.BLACK);
			g.setFont(new Font("Tahoma", Font.BOLD, 30));
			if(sec < 10) {
				g.drawString("00" + Integer.toString(sec), date_x+37, date_y+45);

			}else if (sec < 100){
				g.drawString("0" + Integer.toString(sec), date_x+37, date_y+45);
			} else {
				g.drawString(Integer.toString(sec), date_x+37, date_y+45);
			}
		}
	}
	
	public class Move implements MouseMotionListener {

		@Override
		public void mouseDragged(MouseEvent arg0) {
			
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			mouse_x = e.getX();
			mouse_y = e.getY();
		}
		
	}
	
	public class Click implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) { 
			
			if(inBoxX() != -1 && inBoxY() != -1) {
				System.out.println("the mouse is in the [" + inBoxX() + " , " + inBoxY() + "], Num of neighbors: " + neighbors[inBoxX()][inBoxY()]);
				revealed[inBoxX()][inBoxY()] = true;
				
			} else {
				System.out.println("pointer not in a box");
			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public void reset() {
		if (hit_mine) {
			//reveal only mines
		}
	}
	
	public int inBoxX() {
		for(int i = 0; i < 16; i++) {
			for (int j = 0; j < 9; j++) {
				if (mouse_x >= spacing+i*80 && mouse_x < spacing+i*80+80-2*spacing
						&& mouse_y >= spacing+j*80+80+30 && mouse_y < spacing+j*80+80+30+80-2*spacing) {
					return i;
				}
			}
		}
		return -1;
	}
	
	public int inBoxY() {
		for(int i = 0; i < 16; i++) {
			for (int j = 0; j < 9; j++) {
				if (mouse_x >= spacing+i*80 && mouse_x < spacing+i*80+80-2*spacing
						&& mouse_y >= spacing+j*80+80+30 && mouse_y < spacing+j*80+80+30+80-2*spacing) {
					return j;
				}
			}
		}
		return -1;
	}
	
	public boolean isNeighbor(int boxX, int boxY, int neighborX, int neighborY) {
		if(boxX - neighborX < 2 && boxX - neighborX > -2
				&& boxY - neighborY < 2 && boxY - neighborY > -2
				&& mines[neighborX][neighborY] == 1
				&& mines[boxX][boxY] != 1) {
			return true;
		}else {
			return false;
		}
	}
	
}
