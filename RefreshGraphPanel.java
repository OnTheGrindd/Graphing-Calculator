import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;


public class RefreshGraphPanel extends JPanel implements MouseListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	GraphingCalculator calculator;
	String expr;
	double[] xComponents;
	double[] yComponents;
	
	public RefreshGraphPanel(GraphingCalculator gc, String expression, double[] xValues, double[] yValues) throws IllegalArgumentException {
		// TODO Auto-generated constructor stub
		calculator = gc;
		expr = expression;
		yComponents = yValues;
		xComponents = xValues;
		this.addMouseListener(this);
	}
	
	@Override
	public void paint(Graphics g) { // overrides paint() in JPanel 
		int windowWidth = getWidth();
		int windowHeight = getHeight();
		System.out.println("Current graph size is " + windowWidth + " x " + windowHeight);
		// paint the graph
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
