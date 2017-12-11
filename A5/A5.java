/*
	Brendan Drusda
	Assignment 5
	Due APril 17th
*/

import java.io.*;
import java.util.*;

public class A5
{
	public static void main(String[] args)
	{
//Reading in the tree
		if(args.length < 1)															//check that a tree file was provided
		{
			System.out.println("No tree file inputted by user - exiting program");	//if it was not
			System.exit(0);															//exit the program
		}
		
		File input = new File(args[0]);												//create a new file given the name provided by the user
		Scanner read = null;														//create a scanner
		try																			
		{
			read = new Scanner(input);												//attempt to create a scanner with the given file
		}
		catch(FileNotFoundException e)												
		{
			System.out.println("Input file not found - exiting program");			//if the file does not exist
			System.exit(0);															//exit the program
		}

		BinaryNode root = new BinaryNode();											//create the root node
		
		createTree(root, read);														//create the tree recursively
		
		System.out.println("The Huffman Tree has been restored");					//inform the user that the tree has successfully been restored
			
		Scanner in = new Scanner(System.in);
		while(1<2)
		{
		
			String[] table = createTable(root);											//create the Huffman Table
			alphabetize(table);															//after alphabetizing the table
				
			System.out.println("\nPlease choose from the following:");					//provide the user with options
			System.out.println("1) Encode a text string");
			System.out.println("2) Decode a Huffman string");
			System.out.println("3) Quit");
			
			int ans = in.nextInt();														//get the user's reply
			if(ans == 1)	//Encode dialog
			{
				System.out.println("Enter a string from the following characters:");	//prompt the user for a string
				for(int i = 0; i<table.length; i++)										//show the user
					System.out.print(table[i].charAt(0));								//the available characters
				System.out.println();
				in.nextLine();														
				String[] output = encode(table, in.nextLine());							//encode the string
				
				if(!output[0].equals("There was an error in your text string"))			//check for an error
					System.out.println("Huffman String:");								//otherwise print out
				for(int i = 0; i<output.length; i++)									//the encoded
					System.out.println(output[i]);										//string
				
			}
			else if(ans == 2)	//Decode dialog
			{
//Create and display the table
				System.out.println("Here is the encoding table:");							
		
				for(int i = 0; i<table.length; i++)											//display it
					System.out.println(table[i]);
//Decode the code					
				System.out.println("Please enter a Huffman string (one line, no spaces)");	//prompt the user for a Huffman String
				in.nextLine();
				String output = decode(root, in.nextLine());
				if(output.equals("There was an error in your Huffman string"))
					System.out.println(output);
				else
					System.out.println("Text String:\n"+output);
			}
			else if(ans == 3)																//Exit
			{
				System.out.println("Good-bye");												//inform user that the program is exiting
				System.exit(0);																//exit program
			}
			else
				System.out.println("Invalid input - please enter 1, 2, or 3");				//request a valid input
		}
	}
	
	public static void createTree(BinaryNode curr, Scanner in)		//Method to create a Huffman tree starting with a root node and a scanner with instructional data
	{
		String line = in.nextLine();								//get the next line of the tree
		if(line.charAt(0) == 'I')									//if the line indicates that this is an interior node
		{
			curr.setData('\0');											//ensure that it is set to a "dummy" value
			BinaryNode lc = new BinaryNode();							//create a left child
			BinaryNode rc = new BinaryNode();							//create a right child
			curr.setLeftChild(lc);										//set the left child
			curr.setRightChild(rc);										//set the right child
			createTree(lc, in);											//recurse to set the left child's subtree's values
			createTree(rc, in);											//recurse to set the right child's subtree's values
		}
		else														//if the line indicates that this is a leaf node
		{
			curr.setData(line.charAt(2));								//set the value equal to the second portion of the line
		}
	}
//Creating Table
	public static String[] createTable(BinaryNode root)		//Method to call method to display the Huffman Table
	{
		StringBuilder bits = new StringBuilder();			//Stringbuilder containing each code
		String[] results = new String[getLeafs(root)];		//String array containing the codes
		return createTable(root, bits, results);			
	}
	public static String[] createTable(BinaryNode curr, StringBuilder bits, String[] results)	//Method to display Huffman Table
	{
		if(curr.hasLeftChild())																	//if this is not a leaf node
		{
			bits.append("0");																	//append a 0 (indicative of going down to left child)
			createTable(curr.getLeftChild(), bits, results);									//recursively go to the left child
			bits.deleteCharAt(bits.length()-1);													//remove the appended 0 in order to go to right child

			bits.append("1");																	//append a 1 (indicative of going down to right child)
			createTable(curr.getRightChild(), bits, results);									//recursively go to the right child
			bits.deleteCharAt(bits.length()-1);													//remove the appended 1 in order to go to left child
		}
		else																					//if it is a leaf node
		{
			int i = 0;																			//keep track of what spots in the array are filled
			while(i<results.length)																
			{
				if(results[i] == null)															//if this spot in the array is open
				{
					results[i] = (curr.getData()+": "+bits.toString());							//add the code to this spot
					break;																		//and exit the loop
				}
				else																			//if this spot in the array is not open
					i++;																		//move on to the next spot in the array
			}
		}
		
		return results;																			//return the string array
	}
	
	public static int getLeafs(BinaryNode root)		//Method to call method to get the number of leaf nodes
	{
		return getLeafs(root, 0);					
	}
	
	public static int getLeafs(BinaryNode curr, int numLeaves)	//Method to get the number of leaf nodes
	{
		int leaves = numLeaves;									//set the number of leaf nodes recorded
		
		if(curr.hasLeftChild())									//if this is not a leaf node
		{
			leaves = getLeafs(curr.getLeftChild(), leaves);		//recursively check the left child
			leaves = getLeafs(curr.getRightChild(), leaves);	//recursively check the right child
		}
		else													//if this is a leaf node
		{
			leaves++;											//add one to the count
		}
		return leaves;											//return the number of leaf nodes
	}
	public static void alphabetize(String[] toAlph)			//Method to alphabetize the Huffman Table
	{	
		String temp;										//String to temporarily hold the values being swapped
		
		for(int i = 0; i<toAlph.length; i++)				//loop to iterate through the entire set of codes
		{
			for(int j = 1; j<toAlph.length; j++)			//loop to compare each code
			{
				if(toAlph[j-1].compareTo(toAlph[j]) > 0)	//compare each code to the one before it(BubbleSort)
				{											//if the left value is greater than the right value
					temp = toAlph[j-1];						//save the left value
					toAlph[j-1] = toAlph[j];				//set the left value equal to the right value
					toAlph[j] = temp;						//set the rigth value equal to the original left value(through the temp)
				}
			}
		}
	}
//Encoding Text String
	public static String[] encode(String[] table, String bits)	//Method to call method to encode string
	{
		return encode(table, bits, 0);
	}
	public static String[] encode(String[] table, String word, int loc)		//Method to encode string
	{
		String[] ans = new String[word.length()];							//array to hold encoded string values
		for(int i = 0; i<table.length; i++)									//check each value in the table
		{
			if(word.charAt(loc) == (table[i]).charAt(0))					//if the character matches the table value
			{
				if(loc == word.length()-1)									//and this is the last character
				{
					ans[loc] = table[i].substring(3,(table[i].length()));	//add the code
					return ans;												//and return the answer
				}
				else														//if it is not the last character
				{
					ans = encode(table, word, loc+1);						//recursively encode the rest of the string
					ans[loc] = table[i].substring(3,(table[i].length()));	//add the code
				}
			}
		}

		for(int i = loc; i<ans.length; i++)									//check the answer array -- only check the values at the current location and beyond
		{
			if(ans[i] == null)												//determine if any spots are null
			{
				ans = new String[1];										//if so create a new string
				ans[0] = "There was an error in your Huffman string";		//giving the error message
				break;														//exit the loop
			}
		}
		return ans;															//return the encoded string or the error message
	}
//Decoding a Huffman String
	public static String decode(BinaryNode root, String bits)			//Method to call method to decode a Huffman string
	{
		String ans = decode(root, root, bits, 0).toString();
		if(ans.contains("There was an error in your Huffman string"))	//if there was an error
			return "There was an error in your Huffman string";	
		return ans;														//convert the StringBuilder to a String and return it
	}
	
	public static StringBuilder decode(BinaryNode root, BinaryNode curr, String bits, int loc)	//Method to decode a Huffman string
	{
		StringBuilder ans = new StringBuilder();												//Create a StringBuilder to store the decoded string
		if(curr.hasLeftChild())																	//if this is not a leaf node
		{
			if(loc+1 > bits.length())
				return new StringBuilder("There was an error in your Huffman string");
			if(bits.charAt(loc) == '0')															//if the next piece of the string is a 0
				ans.append(decode(root, curr.getLeftChild(), bits, loc+1));						//move on to the left child
			else if(bits.charAt(loc) == '1')													//if the next piece of the string is a 1
				ans.append(decode(root, curr.getRightChild(), bits, loc+1));					//move on to the right child	
			else																				//if the next piece of the string is something else
				return new StringBuilder("There was an error in your Huffman string");			//return an invalid parameter
		}
		else																					//if this is a leaf node
		{
			ans.append(curr.getData());															//append the character stored in this node
			if(loc < bits.length())															//if the full string has not been traversed
				ans.append(decode(root, root, bits, loc));										//recursively decode the next character
		}
		return ans;																				//return the completed StringBuilder
	}
}