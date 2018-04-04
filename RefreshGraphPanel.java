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
		String[] xscl = new String[10];
		String[] yscl = new String[10];
		expr = expression;
		yComponents = yValues;
		xComponents = xValues;
		this.addMouseListener(this);
		
		// convert xvalues to string
		for(int j = 0; j < 10; j++){
			xscl[j] = Double.toString(xValues[j]);
		}
		int index = 0;
		double yscale = yScale(min(yValues), max(yValues));
		for(int m = (int)(min(yValues)); m <= (int)(max(yValues)); m += yscale){
			yscl[index] = Double.toString(m);
			index++;
		}
		
	}
	
	@Override
	public void paint(Graphics g) { // overrides paint() in JPanel 
		int windowWidth = getWidth();
		int windowHeight = getHeight();
		System.out.println("Current graph size is " + windowWidth + " x " + windowHeight);
		// paint the graph
	}
	
	public double yScale(double yMin, double yMax){
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
			 System.out.println("Add handling of small plot range!");
			 return 0;
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
			 System.out.println("Number of Y scale click marks is too few or too many!");
			 return 0;
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
		  return yScaleValue;
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
