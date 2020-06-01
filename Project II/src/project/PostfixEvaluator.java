package project;

import java.util.Scanner;
import java.util.Stack;

public class PostfixEvaluator {
	private final static char[] validChar = { '+', '-', '*', '/', '%', '^', '–' };

	public static void main(String[] args) {
		// create the scanner to get input
		Scanner scan = new Scanner(System.in);
		String postfix;
		// get input from user and validate
		Validation v;
		while (true) {
			System.out.println("Please enter expression: ");
			postfix = scan.nextLine();
			if(postfix.isEmpty())continue;
			if (!(v = validate(postfix)).bool) {
				System.out.println("\nInvalid input. Please try again..");
			} else
				break;

		}
		scan.close();
		// output calculated value
		System.out.println("The evaluated value is: " + evaluatePostfixExpression(v.s));
	}
	private static Validation validate(String s) {
		String out = "";
		int stacksize = 0;
		boolean opflag = true;
		boolean isempty = false;
		boolean firstflag=true;
		for (int i = 0; i < s.length(); i++) { // iterate through every character
			if (stacksize < 0) {
				isempty = true;
				break;
			}
			if (s.charAt(i) == ' ') {
				opflag = true;
				continue; // skip whitespaces
			}

			if (Character.isDigit(s.charAt(i))) {
				if (opflag) {
					if (i != 0)
						out += " ";
					stacksize++;
					opflag = false;
				}
				out += s.charAt(i);
				continue; // skip numbers
			}
			for (int j = 0; j < validChar.length; j++) {
				if (s.charAt(i) == validChar[j]) {
					opflag = true;
					if(firstflag) {
						firstflag=false;
						stacksize--;
					}
					stacksize--;
					out += " ";
					out += s.charAt(i);
					break; // skip if valid
				}

				// if can't find symbol in operators and it's not a number then invalid input
				if (j == (validChar.length - 1))
					return new Validation(false, "");
			}
		}
		if (stacksize < 0)
			isempty = true;
		System.out.println(out);
		return new Validation(!isempty, out);
	}

	private static int calculate(int num1, int num2, Character operator) {
		switch (operator) { // evaluate simple operations
		case '+':
			return num1 + num2;
		case '-':
			return num1 - num2;
		case '–':
			return num1 - num2;
		case '*':
			return num1 * num2;
		case '/':
			return num1 / num2;
		case '%':
			return num1 % num2;
		default:
			return (int) Math.pow(num1, num2);
		}
	}

	private static int evaluatePostfixExpression(String exp) {
		Stack<Integer> evaluator = new Stack<Integer>();
		Scanner symbol = new Scanner(exp);

		while (symbol.hasNext()) { // iterate through string(no need for ')' flag)
			if (symbol.hasNextInt()) { // check if next value is an integer
				int x = symbol.nextInt();
				evaluator.push(x); // if int, add to stack
			} else { // if operator, calculate y op x
				int num2 = evaluator.pop();
				int num1 = evaluator.pop();
				char operator = symbol.next().charAt(0);
				evaluator.push(calculate(num1, num2, operator));
			}
		}
		symbol.close();
		return evaluator.pop();
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
