import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;


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
		yMin = min(yValues);
		yMax = max(yValues);
		double yRange = Math.abs(yMax-yMin);
		
		JFrame xyWindow = new JFrame();
		JPanel xPanel = new JPanel();
		JPanel yPanel = new JPanel();
		JTextField xTextField = new JTextField();
		JTextField yTextField = new JTextField();
		xyWindow.getContentPane().add(xPanel, "Left");
		xyWindow.getContentPane().add(yPanel, "Right");
		xPanel.add(xTextField);
		yPanel.add(yTextField);
		
		// get x print scale values
		xComponents = Arrays.copyOf(xValues, xValues.length);
		yComponents = Arrays.copyOf(yValues, yValues.length);
		
		for(int j = 0; j < xComponents.length; j++){
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
		for(int k = 0; k < 11; k++){
			yTicks[k] = yMin+yInterval*k;
			System.out.print("yTicks: " + yTicks[k] + '\n');
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
		
		// get x-pixel values
		double xTickPix[] = getxPixelVals(xPixelInterval, xComponents);
		double yTickPix[] = getyPixelVals(yPixelInterval, yTicks);
		double coordinates[][] = plotPoints(yValueToPixelConversionFactor, xPixelInterval, yTickPix);
		int hOffset = getAxisOffset(yComponents);
		int vOffset = getAxisOffset(xComponents);
		// get horizontal offset
		if (getAxisOffset(xComponents) < 0) vOffset = -1*getAxisOffset(xComponents);
		
        
		//Draw axis and scales first
        g.setColor(Color.green);
        // x axis
        g.drawLine(0,windowHeight/2 + hOffset*(int)yPixelInterval,windowWidth,windowHeight/2+ hOffset*(int)yPixelInterval);
        
        // y axis
        g.drawLine(windowWidth/2+vOffset*(int)xPixelInterval,0,windowWidth/2+vOffset*(int)xPixelInterval,windowHeight);
        
        // tick marks
        for(int i = 0;i<yTickPix.length;i++){
            g.drawLine(windowWidth/2-5,(int)yTickPix[i],windowWidth/2+5,(int)yTickPix[i]);
        }
        for(int i = 0;i<xTickPix.length;i++){
            g.drawLine((int)xTickPix[i],windowHeight/2-5,(int)xTickPix[i],windowHeight/2+5);
        }
        
        g.setColor(Color.black);
      
        for(int i = 0;i<coordinates[0].length;i++){
            g.drawOval((int)coordinates[0][i],(int)coordinates[1][i],2,2);
            System.out.println("x = " + coordinates[0][i] + " y = " + coordinates[1][i]);
        }

	}
	
	public int getAxisOffset(double[] values){
		int pCounter = 0;
		int nCounter = 0;
		int offset = 0;
		
		// xOffset calculation
		for(int i = 0; i < values.length; i++){
			if(values[i] < 0) nCounter++;
			else pCounter++;
		}
		offset = pCounter - nCounter;
		if(offset < 0) offset = offset -1;
		else offset = offset+1;
		
		return offset;
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
	
	public double[][] plotPoints(double yConv, double xinterval, double[] yPixTix){ 
		double coordinates[][] = new double[2][xComponents.length];
		double yPixelPoint = 0;
		for(int m = 0; m < xComponents.length; m++){
			if(xComponents[m] < 0) coordinates[0][m] = windowWidth/2 - xinterval*m;
			else coordinates[0][m] = windowWidth/2 + xinterval*m;
			// step 1: get value tick range
			for(int j = 0; j < yTicks.length-1; j++) {
				if(yComponents[m] > yTicks[j] && yComponents[m] < yTicks[j+1]){
					// step 2: find constant k distribution 
					double k = yComponents[j]/yTicks[j+1];
					// step 3: get pixel conversion
					if(yComponents[m] >= 0) yPixelPoint = windowHeight/2 - k*yPixTix[j+1];
					else yPixelPoint = windowHeight/2 - k*yPixTix[j+1];
					System.out.print("yPixels: " + yPixelPoint + " ");
				}
				
			}
			coordinates[1][m] = yPixelPoint;
			
			
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
	public void mousePressed(MouseEvent me) {
		// xTextField and yTextField are in the mini displayXYpairWindow
	    int xInPixels = me.getX();
	    double xValue = xInPixels * xPixelsToValueConversionFactor;
	    String xValueString = String.valueOf(xValue);
	    xTextField.setText("X = " + xValueString);
	  
	    String yValueString = graphingCalculator.calculate(expression,xValueString); 
	    yTextField.setText("Y = " + yValueString);

	    // show mini x,y display window
	    xyWindow.setLocation(me.getX(), me.getY());
	    xyWindow.setVisible(true); 
	    }

	  public void mouseReleased(MouseEvent me) // hide tiny window
	    {
	    // "erase" mini x,y display window	
	    xyWindow.setVisible(false);
	    }

	  public void mouseClicked(MouseEvent me){} // take no action
	  public void mouseEntered(MouseEvent me){} // on these
	  public void mouseExited(MouseEvent  me){} // window events
}
