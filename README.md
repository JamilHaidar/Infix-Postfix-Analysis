# Infix-Postfix-Analysis

In this project I create a project that converts an arithmetic expression to postfix notation then evaluates it. The program takes into consideration the PEMDAS

## Objective
The objective of this project is to create a program that can convert an arithmetic expression in [**inflix notation**](https://www.tutorialspoint.com/data_structures_algorithms/expression_parsing.htm) to [**postfix notation**](https://www.tutorialspoint.com/data_structures_algorithms/expression_parsing.htm). This program should then evaluate the postfix notation of the input and return the final calculated value of the given arithmetic expression.

This is done in two phases; the first phase is the [**lexical analysis**](https://en.wikipedia.org/wiki/Lexical_analysis) where the program splits the input expression into parsable format, and the second phase is the [**parsing phase**](https://en.wikipedia.org/wiki/Parsing#Computer_languages) where the formatted input is evaluated and a value is returned to the user.

----
## Procedure

### First Phase
We first need to convert the input from inflix notation into postfix notation.
For example:
```
1+2*3 -> 1 2 3 * +
```
We can split the problem to simpler problems:
* **First**: Check for valid input. This includes counting the number of opening parenthesis '(' and closing parenthesis ')'
* **Second**: iterate through each character of the input which gives several cases:
  * First case would be if the character is a whitespace or a number, we add it to the output prefix string
  * Second case is if we reach an opening parenthesis, we push it into a stack
  * Third case is encountering a closed parenthesis. Then, we need to pop all previous operators from the stack till we reach an opening parenthesis
  * The Final and default case would be if we get an operator, we pop all previous operators until we reach an opening parenthesis, the stack is empty, or the next operator has higher precedence. We add these previous operators to our output string then we push this operator to the stack.
* **Third**: After iterating through all characters, we add the remaining operators in the stack to our output prefix string.
----
### Second Phase
For the second phase, we need to parse the prefix string obtained.
The steps for doing this are:
* **First**: We create an iterator that splits our prefix to integers and character operators
* **Second**: We iterate through our tokens, which two several cases:
  * First case would be if the token is an integer value, we push its value onto a stack that holds integers.
  * Second case is if the token is a character operator we do several steps:
    1. First we pop the top two values in the stack, namely x and y
    2. We evaluate y operator x using a case structure for every operator
    3. We push the evaluated result back into our stack
   For example:
   ```
   1 2 3 * - 4 5 * +    
   ```
   Character | Stack
   :----------:|:------:
   1|1
   2|2
   []() |1
   3|3
    []()|2
    []()|1
   *|6
   []()|1
   -|-5
   4|4
   []()|-5
   5|5
   []()|4
   []()|-5
   *|20
   []()|-5
   +|15
   
   Output: 15
   
* **Third**: Finally, after iterating through all the tokens, we pop the top value in our stack and that is our evaluated result.
----
## Problems Faced
* In the **first** implementation problems faced were:
 * validating the input is a **must** else the program crashes. For example, must know that x( means x*(, (x)(x) means x * x and we can't have )x and cant start or end with an operator unless starts with '(' or ends with ')'
 * each **exponent** after another exponent has a **higher precedence**. 
   ```
   For example 2^3^3= 2^27 != 8^3 so we must first evaluate 3^3 then 2^27
   ```
   This problem can be solved by giving the exponent an **increasing "precedence"** value for example we give + - a value of 1, 
   / * % a value of 2, ^ gets a value of 3 and increases with each occurence of a ^
   This could result in errors since the value is not being decremented after finishing evaluating a power and moving on to something
   else but that's a risk I chose to overlook.
* In the **second** implementation, the main problem is deciding whether to treat the result after every operation as a double or as an
  integer. Moreover, if it's implemented as one, we must decide if we should use int, double or long, BigInteger or BigDecimal for
  possibly large values. I chose to ignore this in my implementation and went with int as my data type.
* Perhaps the **four** most annoying problems I faced were:
  1. First noticing that there's a **-** and a **–** which could result in invalid outputs if
  not taken into consideration. 
  2. The second problem was realizing that when testing my code it crashes due to **lack of spaces** between
  integers or operators which is a must for input. 
  3. The third problem was realizing that even if I had an input of equal **amounts of integers and operators+1**, it could crash since at
    some point the stack will be empty; for example, 13 * 6 will result in a crash although the
    operators and operands are matched. This was fixed by incrementing an int named **stacksize** whenever encountering an int and
    decrementing by 2 when encountering an operator, then creating a flag that **triggers** and exits the validation loop with a false
    validation value whenever stacksize drops below 0.
  4. If I had an input of 1 2 – 4 5 ^ 3* 6 * 7 2 2 ^ ^ / - instead of 1 2 – 4 5 ^ 3 * 6 * 7 2 2 ^ ^ / - , the validation loop will return true but in the program **crashes** since it's using a **scanner** that scans for int or string values (encountering 3* will lead to a logic error that eventually crashes the program). To avoid this, I searched while validating for such cases and tried to insert a 
  " " whitespace there. However, java will not allow me to **pass the input string by reference** in order to edit it (plus it's *immutable*). The problem here lies in creating a string that is valid and won't crash the program and passing it to the evaluation method while also returning the validation boolean from the same function. Unlike python, java doesn't allow **returning multiple variables** (as far as I know) so I created a simple **object** that will store the boolean and the string then returned that object.
