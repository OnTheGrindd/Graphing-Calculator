import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.String;
import java.util.ArrayList;

import static java.lang.Math.pow;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Utilities;



public class GraphingCalculator implements Calculator, ActionListener, KeyListener {
	
	JFrame      CalcWindow        = new JFrame();
	JTextArea   AnswerArea        = new JTextArea();
	JTextArea   QuestionArea      = new JTextArea();
	JTextField  AnswerField     = new JTextField();
	JTextField  xIncField		= new JTextField();
	JTextField  XField          = new JTextField();
	JTextField  QuestionField     = new JTextField();
	JScrollPane LeftScrollPane    = new JScrollPane(QuestionArea);
	//JScrollPane RightScrollPane = new JScrollPane(AnswerArea); 
	

	JPanel CalcPanel = new JPanel();
	JPanel labelPanel = new JPanel();
	JLabel QuestionLabel  = new JLabel("     Equation to be solved    ");
	JLabel XLabel  = new JLabel("     X   =    ");
	JLabel xIncLabel = new JLabel("X Scale: ");
	JPanel bottomPanel = new JPanel();
	JButton RecallButton= new JButton("RECALL");
	JButton EnterButton = new JButton("ENTER");
	JButton ClearButton = new JButton("CLEAR");
	JTextField ERRORsField =new JTextField("ERRORS");   	
	public char operators[] = {'(', ')', '^', 'r', '*', '/', '+', '-'};
	public char numbers[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
	public char symbols[] = {'e', 'p', 'x', 'u'};
	
	public GraphingCalculator() throws Exception {
	   // TODO Auto-generated constructor stub
	   System.out.println("RUNNING EXPRESSION CALCULATOR");
       CalcWindow.getContentPane().add(CalcPanel, "Center");
       xIncField.setPreferredSize(new Dimension(80, 20));
       CalcPanel.add(xIncLabel);
       CalcPanel.add(xIncField);
       CalcPanel.add(LeftScrollPane);
       CalcPanel.add(LeftScrollPane);
       LeftScrollPane.setPreferredSize( new Dimension( 700, 300));
       CalcWindow.getContentPane().add(bottomPanel, "South");
       bottomPanel.setLayout(new GridLayout(1,4));
       bottomPanel.add(EnterButton);
       bottomPanel.add(RecallButton);
       bottomPanel.add(ClearButton);
       bottomPanel.add(ERRORsField);
       ERRORsField.setBackground(Color.pink);
       ERRORsField.setEditable(false);
       
       CalcWindow.getContentPane().add(labelPanel, "North");
       labelPanel.setLayout(new GridLayout(2,3)); // 1 row, 2 cols
       labelPanel.add(QuestionLabel);
       labelPanel.add(QuestionField);    
       labelPanel.add(XLabel);
       labelPanel.add(XField);
     

       CalcWindow.setTitle("EX: CACLULATOR"); // show chatName in title bar
       //AnswerArea.setEditable(false);
       QuestionArea.setEditable(false);
       //UNCOMMENT THESE SO YOU CAN CALL THE ACTION LISTENER

       RecallButton.addActionListener(this);
       EnterButton.addActionListener(this);
       ClearButton.addActionListener(this);
       QuestionField.addKeyListener(this);
           
       
       	CalcWindow.setLocation(100,100); // x,y
       	CalcWindow.setSize(700, 400);  // width, height
       	
       	CalcWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // terminate if user closes window	
        //SplitPanel setDividerLocation(225);
        CalcWindow.setBackground(Color.cyan);
        EnterButton.setBackground(Color.yellow);
        ClearButton.setBackground(Color.yellow);
        //AnswerArea.setFont(new Font(Font.BOLD));
        CalcWindow.setVisible(true);   // show it
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Authors: Joshua Beerel, Rody Braswell, Adrian Wierzbinski, Nash York");
		try {
			new GraphingCalculator();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public double calculate(String expression, String x) throws Exception {
		// TODO Auto-generated method stub
		int size;
		ArrayList <String> parsedExpression = stringParser(expression, x);
		size = parsedExpression.size();
		String[] a = new String[size];
		
		// fill string type with parsed expression
		for(int k = 0; k < size; k++){a[k] = parsedExpression.get(k).substring(0);}

		String[] answer = PEMDAS(a);
	
		double result = Double.parseDouble(answer[0]);
		
		return result;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if((e.getSource() == EnterButton)) {
			try {
				double solution = calculate(QuestionField.getText(), XField.getText());
				ERRORsField.setText("");
				ERRORsField.setBackground(Color.white);
				QuestionArea.append(QuestionField.getText() + " = " + Double.toString(solution));
				QuestionArea.append(System.lineSeparator());
				QuestionArea.setCaretPosition(QuestionArea.getDocument().getLength());
				if(xIncField.getText().length() > 0) NewGraph();
			} catch (Exception s) {
				ERRORsField.setText(s.getMessage());
				ERRORsField.setBackground(Color.pink);
			}
		}
		
		if(e.getSource() == ClearButton){
			QuestionField.setText(" ");
            ERRORsField.setText(" ");
            ERRORsField.setBackground(Color.white);   
            XField.setText(" ");
		}
		
		if(e.getSource() == RecallButton){
			int end = QuestionArea.getDocument().getLength();
			int start;
			try {
				start = Utilities.getRowStart(QuestionArea, end);
				while (start == end)
				{
				    end--;
				    start = Utilities.getRowStart(QuestionArea, end);
				}
				QuestionField.setText(QuestionArea.getText(start, end - start));
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}
	
	// gets x and corresponding y values for given expression and returns values in 2x2 array

	public double[][] getValues(String xIn, String xInc) throws Exception {
		double increment = Double.parseDouble(xInc.trim());
		double xVal = Double.parseDouble(xIn.trim());
		double vals[][] = new double[10][10];
		int index = 0;

			
		for(double i = xVal; i < (xVal + 10*increment); i += increment){
			vals[0][index] = i;
			vals[1][index] = calculate(QuestionField.getText(), String.valueOf(i));
			index++;
		}
		return vals;
	}
	
	public ArrayList<String> stringParser(String expression, String x) throws Exception {
		String userInput = QuestionField.getText().trim();
		String xInput = x.trim();
		
		if(userInput.isEmpty()) throw new IllegalArgumentException("No expression provided.");
		
		// x field number format error check
		if(xInput.length() > 0 && userInput.contains("x")) {
			try {
				//if(xInput.charAt(0) == '-') xInput = xInput.substring(1);
				Double.parseDouble(xInput);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("X value must be a number. Error: " + e.getMessage());
			}
		}
		else if(xInput.length() == 0 && userInput.contains("x")) { // check if x was used in expression but value not provided or vice versa 
			throw new IllegalArgumentException("No x value provided.");
		}
		else if(xInput.length() > 0 && !userInput.contains("x")) throw new IllegalArgumentException("Expecting x in expression.");
		
		// check unary errors
		if(userInput.contains("-+") ||  userInput.contains("++") || userInput.contains("+-")) {
			throw new IllegalArgumentException("Unary Operators err.");
		}
		
		// check if user didn't enter an expression
		if(!containsAny(userInput, operators)) throw new IllegalArgumentException("Input is not an expression.");
		
		char inputArray[] = userInput.toCharArray();
		// does expression contain invalid characters?
		for (int i = 0; i < userInput.length(); i++) {
			if(!containsAny(Character.toString(inputArray[i]), numbers) && !containsAny(Character.toString(inputArray[i]), operators) && !containsAny(Character.toString(inputArray[i]), symbols) && inputArray[i] != ' ') {
				throw new IllegalArgumentException("Invalid characters in expression.");
			}
			
		}
		if((userInput.contains("p") || userInput.contains("i")) && !userInput.contains("pi")) throw new IllegalArgumentException("ivalid syntax: 'p', 'i'");
		// check operator usage... WRONG: (' ')    RIGHT: ('##+#')
		if(userInput.contains("( )") || userInput.contains("()")) throw new IllegalArgumentException("There is an empty set of parenthesis.");
		// Is there an equal number of open and close parenthesis? 
		if(charOccurence(userInput, '(') != charOccurence(userInput, ')')) throw new IllegalArgumentException("Missing parenthesis.");
		
		// check if character order rules violated
		solveSymbol(userInput);
		
		// replace x with value
		userInput = replaceX(userInput, xInput);
		ArrayList <String> usr = toSubString(userInput);
		// check for operator errors
		
		return usr;
	}

	// prepare expression for calculation
	public ArrayList <String> toSubString (String input) throws Exception{
        int i = 0;
        int b = 0;
        char exceptions[] = {')', '^', '*', '/', '+', '-'};
        char n[] = input.toCharArray();
        ArrayList <String> tokens = new ArrayList < String  > (0);
        checkSpaces(n);
        while (i < input.length()){	      	
            if(containsAny(Character.toString(n[i]), operators) || containsAny(Character.toString(n[i]), symbols)){      	
            	tokens.add(input.substring(b, i).trim());
                if (input.charAt(i) == 'p') {
                    tokens.add(input.substring(i, i+2).trim());
                    i++;
                }
                else {
                	tokens.add(input.substring(i, i+1).trim());
                }
                b = i + 1;
            }       
            else if((!containsAny(input.substring(i), operators) && !containsAny(input.substring(i), symbols)) && i <= input.length() - 1) {
            	tokens.add(input.substring(i).trim());
            	break;
            }
            i++;
        }
        // if null strings exist in array, remove them
        for(int j = 0; j < tokens.size(); j++){
        	if(tokens.contains("")) tokens.remove("");
        }
        
      //check for spaces
        for(int j = 0; j < tokens.size(); j++){
        	if (tokens.get(j).contains(" ")) throw new IllegalArgumentException("Invlalid syntax: <space> symbol 2");
        }
        
        for(int d = 0; d < tokens.size(); d++) {
    		if(tokens.get(d).charAt(0) == ' ') tokens.set(d, tokens.get(d).substring(1));
        }
        
        if(containsAny(tokens.get(0), exceptions)) throw new IllegalArgumentException("Unary operator err 1");
        if(operatorErrors(tokens)) throw new IllegalArgumentException("Unary operator err 2");
        
        tokens = handleUnary(tokens);
     
        return tokens;
    }
	
	public void checkSpaces(char[] str) throws Exception {
		char exceptions[] = {'^', '*', '/'};
		for(int i = 0; i < str.length; i++) {
			if(containsAny(Character.toString(str[i]), exceptions) && (str[i-1] == ' ' || str[i+1] == ' ')) throw new IllegalArgumentException("Invlalid syntax: <space> symbol 3");
			if((str[i] == 'r' || containsAny(Character.toString(str[i]), exceptions))  && (i == str.length || str[i+1] == ' ')) throw new IllegalArgumentException("Invlalid syntax: <space> symbol 4");
		}
	}
	
	public ArrayList<String> handleUnary(ArrayList< String > tokens){
		// solve unary operator when replacing x
        for(int m = 0; m < tokens.size(); m++) {
        	if(tokens.get(m).equals("u") && m != 0) {
        		if(tokens.get(m-1).equals("+")) {
        			tokens.set(m, "-");
        			System.out.println(tokens);
        			tokens.remove(m-1);
        			System.out.println(tokens);
        		}
        		else if(tokens.get(m-1).equals("-")) {
        			tokens.set(m,  "+");
        			tokens.remove(m-1);
        			
        		}
        		else {
        			tokens.set(m, "-");
        			tokens.set(m+1, "-"+tokens.get(m+1));
        			tokens.remove(m);
        			System.out.println(tokens);
        		}
        		
        	} else if(tokens.get(m).equals("u") && m == 0){
        		tokens.set(m, "-").trim();
        		tokens.set(m+1, "-"+tokens.get(m+1));
    			tokens.remove(m);
        		System.out.println(tokens);
        	}
        	if(tokens.get(m).equals("pi")) tokens.set(m,  "3.14159");
        	if(tokens.get(m).equals("e")) tokens.set(m,  "2.718"); 	
        }
        
        for(int m = 0; m < tokens.size(); m++) {
        	if(tokens.get(m).equals("-") && tokens.get(m+1).equals("-")) {
        		tokens.remove(m);
        		tokens.set(m, "+");
        	}
        }

        return tokens;
	}
	
	public static boolean containsAny(String str, char[] searchChars) {
	      if (str == null || str.length() == 0 || searchChars == null || searchChars.length == 0) {
	          return false;
	      }
	      for (int i = 0; i < str.length(); i++) {
	          char ch = str.charAt(i);
	          for (int j = 0; j < searchChars.length; j++) {
	              if (searchChars[j] == ch) {
	                  return true;
	              }
	          }
	      }
	      return false;
	  }
	
	public boolean operatorErrors(ArrayList < String > expression) throws Exception{
		ArrayList < String > newList = new ArrayList < String > (0);
		int highbound = 0;
		int lowbound = 0;
		int iterator;
		//find first operator
		for(lowbound = 0; lowbound < expression.size(); lowbound++) {
			if(containsAny(expression.get(lowbound), operators)) break;
		}
		// find second operator
		for(highbound = lowbound + 1; highbound < expression.size(); highbound++) {
			if(containsAny(expression.get(highbound), operators)) break;
		}
		
		//find error
		for(iterator = lowbound; iterator < highbound; iterator++){
			if((!containsAny(expression.get(iterator), numbers) || !containsAny(expression.get(iterator), symbols)) && 
					expression.get(iterator).contains(" ")) return true;
		}
		
		if(highbound < expression.size()-1){
			newList.addAll(expression.subList(highbound, expression.size()-1));
			operatorErrors(newList);
		}
		return false;
	}
	
	public String replaceX(String input, String x) {
		
		if(x.contains("-")) x = x.replace('-', 'u');
		input = input.replaceAll("x", x);
		return input;
	}
	
	public String removeCharAt(String s, int pos) {
	     return s.substring(0, pos) + s.substring(pos + 1);
	}
	
	public static int charOccurence(String expression, char character) {
		int count = 0;
		for (int i = 0; i < expression.length(); i++) {
			if(expression.charAt(i) == character) count++;
		}
		return count;
	}
	
	
	
	
	public void solveSymbol (String Expression){
		char exceptions[] = {' ', '/','*','^','-','+','r', '('};
		String copy = Expression;
        int i, j, k, m;
        int l = 0;
        if(copy.contains("x")){
        	if(copy.indexOf("x") != 0) {
        		while(copy.contains("x")){
			        i = copy.indexOf("x", 1);
			        if(!containsAny(Character.toString(copy.charAt(i-1)), exceptions )){
			            throw new IllegalArgumentException("Missing operand at " + copy.substring(i-1, i+1));
			        }
			        copy = copy.substring(copy.indexOf('x') + 1);
        		}
        	}
        	
        }
        
        
        if(copy.contains("pi")){
        	copy = Expression;
        	if(copy.indexOf("pi") != 0){
		        while(copy.contains("pi")) {
	        		j = copy.indexOf("pi", 1);
			        if(!containsAny(Character.toString(copy.charAt(j-1)), exceptions )){
			            throw new IllegalArgumentException("Missing operand at " + copy.substring(j-1, j+1));
			        }
			        copy = copy.substring(copy.indexOf('p') + 2);
		        }
        	}
        }
        
        if(copy.contains("e")){
        	copy = Expression;
        	if(copy.indexOf("e") != 0) {
		        while(copy.contains("e")){
	        		k = copy.indexOf("e", 1);
			        if(!containsAny(Character.toString(copy.charAt(k-1)), exceptions )){
			            throw new IllegalArgumentException("Missing operand at " + copy.substring(k-1, k+1));
			        }
			        copy = copy.substring(copy.indexOf('e') + 1);
		        }
        	}
        }
        
        if(copy.contains("(")){
        	copy = Expression;
        	if(copy.indexOf("(") != 0){
	            while(copy.contains("(")) {
	        		m = copy.indexOf("(", 1);
		            if(!containsAny(Character.toString(copy.charAt(m-1)), exceptions )){
		                throw new IllegalArgumentException("Missing operand at " + copy.substring(m-1, m+1));
		            }
		            copy = copy.substring(copy.indexOf('(') + 1);
	            }
        	}
        }
        copy = Expression;
        if(copy.contains(")")){
        	if(copy.indexOf(")") != copy.length() - 1) {
        		while(copy.contains(")")){
	        		l = copy.indexOf(")", 1);
	        		if(l < copy.length()) {
			            if(!containsAny(Character.toString(copy.charAt(l+1)), exceptions)) {
			                throw new IllegalArgumentException("Missing operand at " + copy.substring(l-1, l+1));
			            }
			            copy = copy.substring(copy.indexOf(')') + 1);
		        	}
	        		if(copy.lastIndexOf(')') == copy.length()-1) break;
        		}
        		
        	}
        }
    }
	
	public int[] AddToString(int[] arr, int index) {
		// TODO Auto-generated method stub
		int[] tempArr = new int[arr.length+1];
        System.arraycopy(arr, 0, tempArr, 0, arr.length);
        tempArr[arr.length+1] = index;
        return tempArr;
	}


	
	public String[] PEMDAS(String[] expression) throws Exception {
		//Declare local variables
        int numberOfParenthesis_O = 0,indexOfParenthesis_O = 0,indexOfParenthesis_C = expression.length - 1;
        // Check the expression for most inside parenthesis
        int i;
        for(i = 0;i <expression.length;i++){
            if(expression[i].contains(")")){
                indexOfParenthesis_C = i;
                break;
            }
        }
        for(i = 0;i <expression.length;i++){
            if(expression[i].contains("(")) {
                if(i> indexOfParenthesis_C){
                    break;
                }
                indexOfParenthesis_O = i;
                numberOfParenthesis_O++;
            }
        }
        //System.out.println("Here");
        //checks if the expression is solved
        if(expression.length ==1){
            return expression;
        }
        else if(numberOfParenthesis_O >0){
        // remove the parenthesis from the expression
        expression = Combine(indexOfParenthesis_O,indexOfParenthesis_C,expression);
        // if parenthesis are present recurse the function
        }
        else{
                try{expression[0]=Evaluate(expression);}catch(Exception f){System.out.println("Called from PEMDAS: " + f.getMessage());}
        }
        //System.out.println("HerePEMDAS");
        if(numberOfParenthesis_O >0){
        //System.out.println("HerePEMDAS");
            
            expression = PEMDAS(expression);
        }
        // otherwise return a string of length 1
        
        return expression;
	}

	
	public String Evaluate(String[] topMost) {
		// TODO Auto-generated method stub
		// initializes indexes for each operation;
        int indexOfP[] ; int indexOfM[] = {}; int indexOfA[]= {}; int i;int j =0;
        double sum = 0;
        
        //counts how many of ^ and r are in the array
        for(i = 0;i < topMost.length;i++){
            if(topMost[i].contains("^") || topMost[i].contains("r")){
                j++;
            }
        }
        indexOfP = new int[j];
        j=0;
        
        
        //makes a pointer to each place
        for(i = 0;i < topMost.length;i++){
            if(topMost[i].contains("^") || topMost[i].contains("r")){
                indexOfP[j] = i;
                j+=1;
            }
        }
    
        double FirstNumber, SecondNumber;
        // power and root function
        for(i = 0;i< indexOfP.length;i++){
            if(topMost[indexOfP[i]].contains("^")){
                FirstNumber = Float.parseFloat(topMost[indexOfP[i]-1]);
                
                SecondNumber = Double.parseDouble(topMost[indexOfP[i]+1]);
               
                sum = pow(FirstNumber,SecondNumber);//System.out.println(SecondNumber + " "+sum+ " Power");
                
                topMost[indexOfP[i]-1] = Double.toString(sum);
            }
            if(topMost[indexOfP[i]].contains("r")){
                FirstNumber = Double.parseDouble(topMost[indexOfP[i]-1]);
                SecondNumber = Double.parseDouble(topMost[indexOfP[i]+1]);
                sum = pow(FirstNumber,1/SecondNumber);
                topMost[indexOfP[i]-1] = Double.toString(sum);
            }
            //System.out.println("Here");
            //System.out.println(indexOfP[i]);
            topMost = removeChar(topMost,indexOfP[i]);
            topMost = removeChar(topMost,indexOfP[i]);
            //System.out.print("PR");
            //for(int k =0;k<topMost.length;k++)System.out.print(topMost[k]+" ");
            for(j=0;j<indexOfP.length;j++){
                indexOfP[j] -= 2;
            }
        }
        //counts the number of * and /
        j=0;
        for(i = 0;i < topMost.length;i++){
            if(topMost[i].contains("*") || topMost[i].contains("/")){
                j++;
            }
        }
        indexOfM = new int[j];
        j=0;
        
        
        //makes a pointer to each place
        for(i = 0;i < topMost.length;i++){
            if(topMost[i].contains("*") || topMost[i].contains("/")){
                indexOfM[j] = i;
                j+=1;
            }
        }
        // multiplication and division
        for(i = 0;i< indexOfM.length;i++){
            if(topMost[indexOfM[i]].contains("*")){
                FirstNumber = Float.parseFloat(topMost[indexOfM[i]-1]);
	            SecondNumber = Double.parseDouble(topMost[indexOfM[i]+1]);
                sum = FirstNumber * SecondNumber;
                topMost[indexOfM[i]-1] = Double.toString(sum);
            }
            if(topMost[indexOfM[i]].contains("/")){
                FirstNumber = Double.parseDouble(topMost[indexOfM[i]-1]);
                SecondNumber = Double.parseDouble(topMost[indexOfM[i]+1]);
                sum = FirstNumber / SecondNumber;
                topMost[indexOfM[i]-1] = Double.toString(sum);
            }
            //System.out.println(indexOfM[i]);
            //System.out.println(i);
            //for(int k =0;k<topMost.length;k++)System.out.print(topMost[k]+" ");
            topMost = removeChar(topMost,indexOfM[i]);
            topMost = removeChar(topMost,indexOfM[i]);
            //System.out.print("MD");
            //for(int k =0;k<topMost.length;k++)System.out.print(topMost[k]+" ");
            
            for(j=0;j<indexOfM.length;j++){
                indexOfM[j] -= 2;
            }
        }
        j=0;
        //counts the number of + and -
        j=0;
        
        for(i = 0;i < topMost.length;i++){
            int L = topMost[i].compareTo("-");
            if(topMost[i].contains("+") || L==0){
                j++;
            }
        }
        
        indexOfA = new int[j];
        j=0;
        
        //makes a pointer to each place
        for(i = 0;i < topMost.length;i++){
            int L = topMost[i].compareTo("-");
            if(topMost[i].contains("+") || L==0){    
                indexOfA[j] = i;
                j+=1;
            }
        }
        
           
        for(i = 0;i< indexOfA.length;i++){
            if(topMost[indexOfA[i]].contains("+")){
                FirstNumber = Float.parseFloat(topMost[indexOfA[i]-1]);
                SecondNumber = Double.parseDouble(topMost[indexOfA[i]+1]);
                sum = FirstNumber + SecondNumber;
                topMost[indexOfA[i]-1] = Double.toString(sum);
            }
            if(topMost[indexOfA[i]].contains("-")){
            //	for(int u = 0; u < topMost.length; u++) System.out.println("IndexA: " + indexOfA[i] + '\n' + topMost[u] + " ");
                FirstNumber = Double.parseDouble(topMost[indexOfA[i]-1]);
                SecondNumber = Double.parseDouble(topMost[indexOfA[i]+1]);
                sum = FirstNumber - SecondNumber;
                topMost[indexOfA[i]-1] = Double.toString(sum);
            }
            topMost = removeChar(topMost,indexOfA[i]);
            topMost = removeChar(topMost,indexOfA[i]);
            //System.out.print("AS");
            //for(int k =0;k<topMost.length;k++)System.out.print(topMost[k]+" ");
            for(j=0;j<indexOfA.length;j++){
                indexOfA[j] -= 2;
            }
        }
        //for(int k =0;k<topMost.length;k++)System.out.print(topMost[k]+" ");
        String[] out = topMost;
        //for(int k =0;k<out.length;k++)System.out.print(out[k]+" ");
        return out[0];
	}

	
	public String[] Combine(int indexOpen, int indexClose, String[] expression) {
		// TODO Auto-generated method stub
		// calculate the size of the inside of the parenthesis
        int size = indexClose - indexOpen - 1 , k = 0;
                
        String[] topMost = new String[size];
        //copy the expression into the new array
     
                
        for(int i = indexOpen + 1; i <= indexClose-1;i++){
            topMost[k] = expression[i];
            k++;
        }
                
        // deletes the parenthesis portion from the string
        expression = removeArray(expression,indexOpen,indexClose);
                
        // replaces the location of the parenthesis with a number
        try{expression[indexOpen] = Evaluate(topMost);}catch(Exception f){System.out.println("Called From Combine");}
        return expression;
	}

	public String[] removeArray(String[] s, int indexOpen, int indexClose) {
		// TODO Auto-generated method stub
		String[] First = new String[indexOpen+1];
        String[] newArray = new String[s.length - (indexClose-indexOpen)];
        int i;
        for(i = 0;i < newArray.length;i++){
            if(i < First.length){
            newArray[i] = s[i];
            }
            else{
            newArray[i] = s[i+(indexClose-indexOpen)];
            }
        }
        return newArray;
	}

	
	public String[] removeChar(String[] s, int pos) {
		// TODO Auto-generated method stub
		String[] First = new String[pos ];
        String[] NewArray = new String[s.length-1];
        int i;
        for(i = 0;i < NewArray.length;i++){
                if(i < First.length){
                    NewArray[i] = s[i];
                }
                else{
                    NewArray[i] = s[i+1];
                }    
            }    
        return NewArray;
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		    
  
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode()==KeyEvent.VK_ENTER){
            EnterButton.doClick();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void NewGraph () throws IllegalArgumentException, Exception{
		JFrame graphWindow			= new JFrame();						//window to pop up after hitting enter
		
		graphWindow.setLocation(500,0); 									// x,y
		graphWindow.setSize(500, 200);  									// width, height 
		graphWindow.setTitle(QuestionField.getText());			//title must be the expression graphed.
		graphWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);	//given to us in part 3 of instructions.
		RefreshGraphPanel graphPanel = new RefreshGraphPanel(this, QuestionField.getText(), getValues(XField.getText(), xIncField.getText())[0], getValues(XField.getText(), xIncField.getText())[1]);		
		graphWindow.getContentPane().add(graphPanel,  "Center");			//centers the graph panel in the window (step 4)
		graphWindow.setVisible(true); 
		
		
		
	}


}
