import java.awt.Graphics;
import java.awt.Point;
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
	String[] xComponents = new String[10];
	String[] yComponents;
	int windowWidth;
	int windowHeight;
	
	public RefreshGraphPanel(GraphingCalculator gc, String expression, double[] xValues, double[] yValues) throws IllegalArgumentException {
		// TODO Auto-generated constructor stub
		calculator = gc;
		expr = expression;
		double yMin = min(yValues);
		double yMax = max(yValues);
		this.addMouseListener(this);
		
		// get x print scale values
		System.out.print("X values: ");
		for(int j = 0; j < 10; j++){
			xComponents[j] = Double.toString(xValues[j]);
			System.out.print(xComponents[j] + " ");
		}
		
	
		int index = 0;
		// get y scale 
		double yscale = yScale(yMin, yMax)[0]/(yScale(yMin, yMax)[1]-1);
		yComponents = new String[(int) yScale(yMin, yMax)[1]];
		System.out.println("yMin: " + yMin);
		System.out.println("yMax: " + yMax);
		System.out.println("Scale: " +yscale);
		System.out.print('\n' + "y values: ");
		
		// get y print scale values
		for(double m = yMin; m <= yMax; m += yscale){
			yComponents[index] = Double.toString(m);
			System.out.print(yComponents[index] + " ");
			index++;
		}
		
	}
	
	@Override
	public void paint(Graphics g) { // overrides paint() in JPanel 
		windowHeight = getHeight();
		windowWidth = getWidth();
		double xPixelInterval = (windowWidth)/(10-1);
		double yPixelInterval = (windowHeight)/(10-1);
		double xValueToPixelConversionFactor = xPixelInterval/(Double.parseDouble(xComponents[1]) - Double.parseDouble(xComponents[0]));
		double yValueToPixelConversionFactor = yPixelInterval/(Double.parseDouble(yComponents[1]) - Double.parseDouble(xComponents[0]));
		System.out.println('\n' + "Current graph size is " + windowWidth + " x " + windowHeight);
		
		// get x-pixel values
		int[][] xTickPix = getPixelVals(xPixelInterval, xComponents, windowWidth/2);
		int[][] yTickPix = getPixelVals(yPixelInterval, yComponents, windowHeight/2);
		
	}
	
	public int[][] getPixelVals(double interval, String[] values, int reference) {
		int index = 0;
		int pixels[][] = new int[2][values.length];
		for(int i = 0; i < values.length*interval; i+= interval){
			pixels[0][index] = (int)(interval*i+reference);
			index++;
		}
		return pixels;
	}
	
	public double[] yScale(double yMin, double yMax){
		double dPlotRange;
		  int    plotRange, initialIncrement, upperIncrement, 
		         lowerIncrement, selectedIncrement, numberOfYscaleValues,
		         lowestYscaleValue, highestYscaleValue;
		  String zeros = "0000000000";
		  
		  
		  // 1) Determine the RANGE to be plotted.
		  dPlotRange = yMax - yMin;
		  System.out.println("Plot range (Ymax-Ymin) = " + dPlotRange);

		  // 2) Determine an initial increment value.
		  if (dPlotRange > 10)
		     {
			 plotRange = (int)dPlotRange;
			 System.out.println("Rounded plot range = " + plotRange);
		     }
		  else
		     {
			 throw new IllegalArgumentException("Add handling of small plot range!");
		     }
		/*ASSUME*/ // 10 scale values as a starting assumption.
		  initialIncrement = plotRange/10;
		  System.out.println("Initial increment value = " + initialIncrement);
		  // Please excuse this clumsy "math"!
		  String initialIncrementString = String.valueOf(initialIncrement);
		  //System.out.println("InitialIncrementString = " + initialIncrementString + " (length = " + initialIncrementString.length() + ")");

		  // 3) Find even numbers above and below the initial increment. 
		  String leadingDigit = initialIncrementString.substring(0,1);
		  int leadingNumber = Integer.parseInt(leadingDigit);
		  int bumpedLeadingNumber = leadingNumber + 1;
		  String bumpedLeadingDigit = String.valueOf(bumpedLeadingNumber);
		  String upperIncrementString = bumpedLeadingDigit + zeros.substring(0,initialIncrementString.length()-1);
		  String lowerIncrementString = leadingDigit       + zeros.substring(0,initialIncrementString.length()-1);
		  upperIncrement = Integer.parseInt(upperIncrementString);
		  lowerIncrement = Integer.parseInt(lowerIncrementString);
		  System.out.println("Upper increment alternative = " + upperIncrement);
		  System.out.println("Lower increment alternative = " + lowerIncrement);

		  // 4) Pick the upper or lower even increment depending on which is closest.
		  int distanceToUpper = upperIncrement - initialIncrement;
		  int distanceToLower = initialIncrement - lowerIncrement;
		  if (distanceToUpper > distanceToLower)
			  selectedIncrement = lowerIncrement;
		    else
		      selectedIncrement = upperIncrement;
		  System.out.println("The closest even increment (and therefore the one chosen) = " + selectedIncrement);

		  // 5) Determine lowest Y scale value
		  numberOfYscaleValues = 0;
		  lowestYscaleValue    = 0;
		  if (yMin < 0)
		     {
		     for (; lowestYscaleValue > yMin; lowestYscaleValue-=selectedIncrement)
		          numberOfYscaleValues++;
		     }
		  if (yMin > 0)
		     {
			 for (; lowestYscaleValue < yMin; lowestYscaleValue+=selectedIncrement)
			      numberOfYscaleValues++;
		     numberOfYscaleValues--;
		     lowestYscaleValue -= selectedIncrement;
		     }
		  System.out.println("The lowest Y scale value will be " + lowestYscaleValue + ")");
		  
		  
		  // 6) Determine upper Y scale value
		  numberOfYscaleValues = 1;
		  for (highestYscaleValue = lowestYscaleValue; highestYscaleValue < yMax; highestYscaleValue+=selectedIncrement)
			  numberOfYscaleValues++;
		  System.out.println("The highest Y scale value will be " + highestYscaleValue);
		  System.out.println("The number of Y scale click marks will be " + numberOfYscaleValues);
		  if ((numberOfYscaleValues < 5) || (numberOfYscaleValues > 20))
		     {
			 throw new IllegalArgumentException("Number of Y scale click marks is too few or too many!");
		     }
		  
		  // 7) Determine if Y scale will be extended to include the 0 point.
		  if ((lowestYscaleValue < 0) && (highestYscaleValue > 0))
		       System.out.println("The Y scale includes the 0 point.");
		    else // Y scale does not include 0.
		     {   //	Should it be extended to include the 0 point?
		     if ((lowestYscaleValue > 0) && (lowestYscaleValue/selectedIncrement <= 3))
		        {
		    	lowestYscaleValue = 0;
		    	System.out.println("Lower Y scale value adjusted down to 0 to include 0 point. (Additional click marks added.)");
		        }
		     if ((highestYscaleValue < 0) && (highestYscaleValue/selectedIncrement <= 3))
		        {
		     	highestYscaleValue = 0;
		    	System.out.println("Upper Y scale value adjusted up to 0 to include 0 point. (Additional click marks added.)");
		        }
		     }
		  int yScaleValue = lowestYscaleValue;
		  while(yScaleValue < highestYscaleValue)
		       {
			   System.out.print(yScaleValue + ",");
			   yScaleValue += selectedIncrement;
		       }
		  System.out.println(yScaleValue);
		  double retVal[] = {yScaleValue, numberOfYscaleValues};
		  return retVal;
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
