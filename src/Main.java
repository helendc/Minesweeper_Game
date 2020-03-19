
public class Main implements Runnable {

	GUI gui = new GUI();
	
	public static void main(String[] args) {
		new Thread(new Main()).start();
		//new thread starts when game begins 
	}
	
	@Override
	public void run() {
		while(true) {
			gui.repaint();
		}
	}

}
