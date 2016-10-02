import javax.swing.JFrame;

public class Display{

	private JFrame frame;
	
	public Display( int width, int height){
		frame = new JFrame("Maze Runner");
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		
		frame.setVisible(true);
	}
	
	public JFrame getFrame() {
		return frame;
	}
}
