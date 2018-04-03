/** 11-13-12 This program is used to test an algorithm 
 * for determining what the "Click Points" should be on
 * the Y Scale - the Y axis - of the Calculator Graph.
 *  
 * We are given X values which the user has set by
 * manipulating the range of x values and the increment
 * they would like to see plotted between the x values.
 * So we will use the PROVIDED array of x values as the
 * X scale values to be printed at the "click marks" along
 * the X scale. (And if x = 0 is CLOSE - within a couple
 * extra click marks added on the left or right of the
 * X scale - we will extend the X scale to include 0.)
 * 
 * The Y scale is a different story! The Y values provided
 * correspond to the provided X values, and are therefore
 * PLOT points for the graph, but they are certainly NOT
 * the values to be shown on the Y scale at click marks!
 * The Y scale should have nice easy-to-read numbers
 * with a nice increment between the numbers like 1 or
 * 5 or 10 or 50 or 100 or 200 or 1000 or 2000 etc. 
 * 
 * The EXTENT of the Y scale values is determined by the
 * RANGE of the y values. So our first task is to review
 * all the y values, find the smallest and the largest
 * value, and then subtract them to determine the RANGE
 * of Y values that must be shown on the Y scale. 
 * 
 * It is then our challenge, as the graphing program, to
 * determine a pleasing and appropriate set of Y scale
 * values to be PRINTED along the Y axis. (Again - these
 * are NOT the PLOT POINTS you will use to draw the
 * graph!) The user will view the X scale values (which
 * ARE plot points) and the Y scale values (which we
 * will determine with an algorithm) to roughly interpret
 * the values along the plotted curve. 
 * 
 * We also provide a very nice feature for the user if
 * they want to view very precise X,Y value pairs along
 * the curve by simply clicking on it!
 * 
 * This program accepts two command line parameters which
 * are taken to be the min and max Y values found in the
 * provided array of Y values, and then determines the
 * Y scale values to be printed. (And, like on the X
 * scale, if the Y=0 point is determined to be within a
 * few click marks on the bottom or top of the Y scale,
 * then the Y scale will be extended to include the 0
 * point. 
 */
public class CalculateYscalePrintValues
{
public static void main(String[] args)
  {
  if (args.length != 2)
     {
	 System.out.println("Provide two numeric values which are the min and max Y values to be plotted.");
	 System.out.println("This program will determine appropriate Y scale values to be printed on the Y axis."); 
	 return;
     }
  double yMin, yMax, dPlotRange;
  int    plotRange, initialIncrement, upperIncrement, 
         lowerIncrement, selectedIncrement, numberOfYscaleValues,
         lowestYscaleValue, highestYscaleValue;
  String zeros = "0000000000";
  try {
	  yMin = Double.parseDouble(args[0]);
	  yMax = Double.parseDouble(args[1]);
	  if (yMin > yMax)
	     {
		 double temp = yMax;
		 yMax = yMin;
		 yMin = temp;
	     }
	  System.out.println("Entered values are: Ymin = " + yMin + " Ymax = " + yMax);
      }
  catch(NumberFormatException nfe)
      {
	  System.out.println("Both input parms must be numeric.");
	  return;
      }
  
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
	 return;
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
	 return;
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

  }      
}
