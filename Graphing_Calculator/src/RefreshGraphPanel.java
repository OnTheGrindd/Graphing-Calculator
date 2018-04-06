import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;

import javax.swing.JPanel;


public class RefreshGraphPanel extends JPanel implements MouseListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	GraphingCalculator calculator;
	String expr;
	double[] xComponents = new double[11];
	double[] yTicks = new double[11];
	double[] yComponents;
	double yMax, yMin, yInterval; 
	int windowWidth;
	int windowHeight;
	
	public RefreshGraphPanel(GraphingCalculator gc, String expression, double[] xValues, double[] yValues) throws IllegalArgumentException {
		// TODO Auto-generated constructor stub
		calculator = gc;
		expr = expression;
		yMin = max(yValues);
		yMax = min(yValues);
		double yRange = yMax-yMin;
		
		// get x print scale values
		xComponents = Arrays.copyOf(xValues, xValues.length);
		yComponents = Arrays.copyOf(yValues, yValues.length);
		
		System.out.print("X values: ");
		for(int j = 0; j < xComponents.length; j++){
			System.out.print(xComponents[j] + " ");
		}
		
		// get y scale
		if(yRange >= 10) yInterval = yRange/10;
		
		// handle ranges less than 10
		else {
			yInterval = 0.025;
			double normInterval = yRange/10;
			for(int j = 2; j < 40; j++){
				if(Math.abs(normInterval - j*0.025)< Math.abs(normInterval - yInterval)) yInterval = j*0.025;
			}
		}
		
		// get y-ticks
		System.out.println("yTicks");
		for(int k = 0; k < 11; k++){
			yTicks[k] = yMin+yInterval*k;
			System.out.print(yTicks[k] + " ");
		}
//		yComponents = new double[yTicks.length];
//		yComponents = Arrays.copyOf(yTicks, yTicks.length);
	
		this.addMouseListener(this);
	}
	
	@Override
	public void paint(Graphics g) { // overrides paint() in JPanel 
		windowHeight = getHeight();
		windowWidth = getWidth();
		double xPixelInterval = (windowWidth)/(10);
		double yPixelInterval = (windowHeight)/(10);
		double xValueToPixelConversionFactor = xPixelInterval/((xComponents[1]) - (xComponents[0]));
		double yValueToPixelConversionFactor = yPixelInterval/yInterval;
		System.out.println('\n' + "Current graph size is " + windowWidth + " x " + windowHeight);
		
		// get x-pixel values
		double xTickPix[] = getxPixelVals(xPixelInterval, xComponents);
		double yTickPix[] = getyPixelVals(yPixelInterval, yTicks);
		double coordinates[][] = plotPoints(yValueToPixelConversionFactor, xPixelInterval);
        
		//Draw axis and scales first
        g.setColor(Color.green);
        g.drawLine(0,windowHeight/2,windowWidth,windowHeight/2);
        
        g.drawLine(windowWidth/2,0,windowWidth/2,windowHeight);
        for(int i = 0;i<yTickPix.length;i++){
            g.drawLine(windowWidth/2-5,(int)yTickPix[i],windowWidth/2+5,(int)yTickPix[i]);
        }
        for(int i = 0;i<xTickPix.length;i++){
            g.drawLine((int)xTickPix[i],windowHeight/2-5,(int)xTickPix[i],windowHeight/2+5);
        }
        g.setColor(Color.black);
        for(int i = 0;i<coordinates.length;i++){
            g.drawOval((int)coordinates[0][i],(int)coordinates[1][i],2,2);
            System.out.println("x = " + coordinates[0][i] + " y = " + coordinates[1][i]);
        }
       

	}

	public double[] getxPixelVals(double interval, double[] values) {
		int index = 0;
		double pixels[] = new double[values.length];
		for(int i = 0; i < values.length*interval; i+= interval){
			pixels[index] = (int)(i);
			index++;
		}
		return pixels;
	}
		
	public double[] getyPixelVals(double interval, double[] values) {
		int index = 0;
		double pixels[] = new double[values.length];
		System.out.println("value lenght: " + values.length + '\n' + "w/interval: " + values.length*interval);
		for(int i = 0; i < values.length*interval; i+= interval){
			pixels[index] = (int)(i); 
			System.out.println("value index: " + index + " ," + i);
			index++;
			
		}
		return pixels;
	}
	
	public double[][] plotPoints(double yConv, double xinterval){ 
		double coordinates[][] = new double[2][xComponents.length];
		for(int m = 0; m< xComponents.length; m++){
			if(xComponents[m] < 0) coordinates[0][m] = windowWidth/2 - xinterval*m;
			else coordinates[0][m] = windowWidth/2 + xinterval*m;
			if(yComponents[m] < 0) coordinates[1][m] = windowHeight/2 + (yComponents[m]-yMin)*yConv;
			else coordinates[1][m] = windowHeight/2 - (yComponents[m]-yMin)*yConv;
		}
		return coordinates;
	}
	
	public double max(double[] values) {
		double max = values[0];
		for(int k = 1; k < values.length; k++) if(values[k] > max) max = values[k];
		return max;
	}
	
	public double min(double[] values) {
		double min = values[0];
		for(int l = 1; l < values.length; l++) if(values[l] < min) min = values[l];
		return min;
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
