package project;

import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;

public class InfixToPostfixConverter {
	private final static char[] validChar = { '+', '-', '*', '/', '%', '^', '(', ')', '–' };
	private static Stack<Character> operators;
	private static int powerValue;

	public static void main(String args[]) throws IOException {
		powerValue = 2;
		operators = new Stack<Character>();
		String infix;

		// create the scanner to get input
		Scanner scan = new Scanner(System.in);
		Validation v;
		// get input from user and validate
		while (true) {
			System.out.println("Please enter expression: ");
			infix = scan.nextLine();
			if (!(v = validate(infix)).bool) {
				System.out.println("\nInvalid input. Please try again..");
			} else
				break;
		}
		scan.close();
		// output final postfix
		System.out.println("The expression in postfix is: " + convertToPostfix(v.s));

	}

	// validates the input
	private static Validation validate(String s) {
		if (s.length() == 0)//check for empty string
			return new Validation(false, "");
		if (!Character.isDigit(s.charAt(0)) && s.charAt(0) != '(')//check for invalid opening
			return new Validation(false, "");
		if (!Character.isDigit(s.charAt(s.length() - 1)) && s.charAt(s.length() - 1) != ')')//check for invalid closer
			return new Validation(false, "");
		String str = "";

		// count number of '(' and ')'
		int opener = 0;
		int closer = 0;
		for (int i = 0; i < s.length(); i++) {
			System.out.print(s.charAt(i));

			if (s.charAt(i) == ' ') {
				str += s.charAt(i);
				continue;//skip white spaces
			}
			if (Character.isDigit(s.charAt(i))) {
				str += s.charAt(i);
				continue;//skip digits
			}
			for (int j = 0; j < validChar.length; j++) {
				if (closer > opener)//at any point if closers>openers, invalid input example:2 +5)( +6, openers==closers but not in good order
					return new Validation(false, "");
				if (s.charAt(i) == validChar[j]) {
					boolean check = true;//if we have 2+5(6) valid input but 2+5**2 invalid or 2+5*-2 invalid (no negative numbers)
					if (validChar[j] == '(') {
						check = false;
						opener++;
						if(i!=0)
						if (Character.isDigit(s.charAt(i - 1))|| s.charAt(i-1)==')')//if we have a number and opening parenthesis example: 2(5) it's 2*(5)
							str += '*';
					} else if (validChar[j] == ')') {
						if (i != s.length() - 1) {
							if (Character.isDigit(s.charAt(i + 1))) {//if we have a closing parenthesis and a number example )6 invalid input
								return new Validation(false, "");
							}
							check = false;
						}
						closer++;
					}
					str += s.charAt(i);
					if (check)//if current symbol is operator and previous is also operator, invalid input
						if (precedence(s.charAt(i - 1)) != 0)
							return new Validation(false, "");
					break;
				}
				// if can't find symbol in operators and it's not a number then invalid input
				if (j == (validChar.length - 1))
					return new Validation(false, "");
			}
		}
		return new Validation(closer == opener, str);// if all checks out, the remaining constraint is openers==closers
	}

	// checks for operator
	private static boolean isOperator(Character symbol) {
		return !(Character.isDigit(symbol));
	}

	// returns precedence value
	private static int precedence(char operator) {
		if (operator == '+' || operator == '-')
			return 1;
		if (operator == '*' || operator == '/' || operator == '%')
			return 2;
		if (operator == '^') {
			return ++powerValue;
		}
		return 0;
	}

	// converts to postfix
	private static String convertToPostfix(String infix)
	// converter method
	{
		char symbol;
		String postfix = "";

		// iterate through all characters from input and process t
		for (int i = 0; i < infix.length(); i++) {
			symbol = infix.charAt(i);
			// check for white spaces and ignore them
			if (symbol == ' ') {
				postfix += " ";
				continue;
			}

			// if it is a number / symbol, add it to the string
			if (!isOperator(symbol))
				postfix = postfix + symbol;
			// first check for ( and push to stack
			else if (symbol == '(') {
				postfix += " ";
				operators.push(symbol);
			} // then check for ) and pop everything from the last '(' till the ')' to the
				// postfix string
			else if (symbol == ')') {
				postfix += " ";
				while (operators.peek() != '(') {
					postfix = postfix + operators.pop();
				}
				operators.pop(); // remove '('
			} // if simple operator, print operators before it that have greater precedence
				// until find a '(' or stack is empty
			else {
				postfix += " ";
				while (!operators.isEmpty() && operators.peek() != '('
						&& precedence(operators.peek()) >= precedence(symbol))
					postfix = postfix + operators.pop() + " ";
				operators.push(symbol);
			}
		}
		// finally, we pop everything left in the stack
		postfix+=" ";
		while (!operators.isEmpty())
			postfix += operators.pop() + " ";
		
		return postfix.replaceAll("\\s{2,}"," ").trim();// remove multiple spaces and clean up
	}

	private static class Validation {
		private static boolean bool;
		private static String s;

		public Validation(boolean bool, String s) {
			this.bool = bool;
			this.s = s;
		}
	}
}
