/*	
	Brendan Drusda
	CS0445
	Word Search
*/	

import java.util.Scanner;
import java.io.*;

public class Assig3
{
	public static void main(String[] args)
	{
		Scanner in = new Scanner(System.in);									//User input stream
		
//Create the board	
		Scanner reader;															//File input stream
		File file;														
		String line;													
			
		System.out.print("Please enter grid filename:");						//Prompt user for input file
		
		while(1<2)
		{
			file = new File(in.nextLine());										//Get input file from user
			try
			{
				reader = new Scanner(file);										//Attempt to get inputted file
				break;															//Exit the selection process
			}
			catch(FileNotFoundException e)
			{
				System.out.println("File not found: please retry");				//If the file cannot be found, alert the user
			}
		}
		
		String[] dimensions = (reader.nextLine()).split(" ");					//Get the dimensions of the board

		int rows = Integer.parseInt(dimensions[0]);									//first number is rows
		int columns = Integer.parseInt(dimensions[1]);								//second number is columns
		
		char[][] board = new char[rows][columns];								//Create the board
		
		for(int i = 0; i<rows; i++)												//Cycle through rows
		{
			line = reader.nextLine();											//Get next line of data
			for(int j = 0; j<line.length(); j++)								//Cycle through columns
			{
				board[i][j] = Character.toLowerCase(line.charAt(j));			//Fill in the board
			}
		}

//Display the board
		for(int i = 0; i<rows; i++)												//Cycle through rows
		{
			for(int j = 0; j<columns; j++)										//Cycle through columns
				System.out.print(board[i][j]+" ");								//Print out each character
			System.out.println();												//At the end of the line move to the next one 
		}
		System.out.println();
		
//Get words to search for
		System.out.println("Please enter phrase (sep. by single spaces):");		//Prompt for search words
		String[] words = in.nextLine().toLowerCase().split(" ");					//Split the resulting search into individual words
		
		while(!(words[0].equals("")))
		{
			System.out.print("Looking for:");									//Output the list
			for(int i = 0; i<words.length; i++)										//of words that
				System.out.print(" "+words[i]);											//are being located
			System.out.println();
			System.out.println("Containing "+words.length+" words");			
			
			System.out.print("The phrase:");
			for(int i = 0; i<words.length; i++)								
				System.out.print(" "+words[i]);										
			System.out.println();

//Search for the Phrase		
			String[] found = new String[words.length];
			for(int r = 0; r < rows && found[found.length-1] == null; r++)									//Start the search for the phrase
			{
				for(int c = 0; c < columns && found[found.length-1]==null; c++)							//at the beginning of the board and traverse it
				{
					found = findPhrase(r, c, words, 0, board, 1);					//Actually search starting at coordinates r,c
					if(found[0] == "was not found")
						found = findPhrase(r, c, words, 0, board, 2);					//Actually search starting at coordinates r,c
					if(found[0] == "was not found")
						found = findPhrase(r, c, words, 0, board, 3);					//Actually search starting at coordinates r,c
					if(found[0] == "was not found")
						found = findPhrase(r, c, words, 0, board, 4);					//Actually search starting at coordinates r,c

					if(found[0] != "was not found")														//If the phrase is found
					{					
						for(int i = 0; i < found.length; i++)
							System.out.println(found[i]);
					}
				}
			}

//Output results
			if(found[0] == "was not found")
				System.out.println(found[0]);
			else
			{
				for(int i = 0; i < rows; i++)										//Display the board
				{
					for(int j = 0; j < columns; j++)			
						System.out.print(board[i][j]+" ");								
					System.out.println();
				}
			}
			
			for(int i = 0; i < rows; i++)
			{
				for(int j = 0; j < columns; j++)
					board[i][j] = Character.toLowerCase(board[i][j]);				//Shift the letters back to lower case
			}
			
			System.out.println();
			System.out.println("Please enter phrase (sep. by single spaces)");
			words = in.nextLine().toLowerCase().split(" ");
		}
	}

//Find an individual word
	public static int[] findWord(int rows, int columns, String word, int location, char[][] board, int dir)
	{
//Check boundaries
		boolean finished = false;
		int[] returns = new int[] {0, 0, 0};												//Array containing the final row, final column, and if the word was found or not
		
		if(rows >= board.length || rows < 0 || columns >= board[0].length || columns < 0)	//Check boundaries
			return new int[] {rows,columns,0};														
		else if((board[rows][columns] != word.charAt(location))) 							//If the character doesn't match return false		
			return new int[] {rows,columns,0};
		else																				//If it does
		{
//Search for word char by char
			board[rows][columns] = Character.toUpperCase(board[rows][columns]);				//Make the character uppercase
			if(location == word.length()-1)													//If this was the last character
			{
				returns[0] = rows;																//save the final spots
				returns[1] = columns;															
				returns[2] = 1;																	//Method is finished
			}
			else																			//Otherwise move on to the next one
			{
				int dr = 0;																	//change in rows
				int dc = 0;																	//change in columns
				
				if(dir == 1)																//if searching left
					dc = 1;
				else if(dir == 2)															//if searching down
					dr = 1;
				else if(dir == 3)															//if searching right
					dc = -1;
				else																		//if searching up
					dr = -1;
					
				returns = findWord(rows+dr, columns+dc, word, location+1, board, dir);		//One method call to move in any direction
				
				if(returns[2] == 0)															//If the word was not found
					board[rows][columns] = Character.toLowerCase(board[rows][columns]);		//Reset the letters to lowercase
			}
		}
		return returns;	
	}
	
//Find a full phrase		
	public static String[] findPhrase(int rows, int columns, String[] words, int location, char[][] board, int dir)
	{
		int[] result = new int[3];													//Array containing final row, final column, and if a word was found
		boolean found;
		String[] output = new String[words.length+1];								//Array containing the program's output
		String toString = "";														//The individual word's output
		result[0] = -1;																//Default row initialization
		result[1] = -1;																//Default column initialization
		result[2] = -1;																//Default found initialization
		output[0] = "was not found";												//Default output initialization
		
		if(location == 0)	//If this is the first word in the phrase search in all directions starting at the current block
		{
			if(dir == 1)
				result = findWord(rows, columns, words[location], 0, board, 1);			//Right
			if(dir == 2)
				result = findWord(rows, columns, words[location], 0, board, 2);		//Down
			if(dir == 3)
				result = findWord(rows, columns, words[location], 0, board, 3);		//Left
			if(dir == 4)
				result = findWord(rows, columns, words[location], 0, board, 4);		//Up
		}
		else				//If this is not the first word, search in all directions starting one block ahead in said direction
		{
			if(dir == 1)
				result = findWord(rows, columns+1, words[location], 0, board, 1);	//Right
			if(!(result[2] == 1) && dir == 2)
				result = findWord(rows+1, columns, words[location], 0, board, 2);	//Down
			if(!(result[2] == 1) && dir == 3)
				result = findWord(rows, columns-1, words[location], 0, board, 3);	//Left	
			if(!(result[2] == 1) && dir == 4)
				result = findWord(rows-1, columns, words[location], 0, board, 4);	//Up	
		}
		
		if(result[2] == 1)	//If the word was found
		{
			int dr = 0;
			int dc = 0;
			if(location != 0)
			{
				if(dir == 1)
					dc = 1;
				if(dir == 2)
					dr = 1;
				if(dir == 3)
					dc = -1;
				if(dir == 4)
					dr = -1;
			}
			
			toString = (words[location]+": ("+(rows+dr)+","+(columns+dc)+") to ("+result[0]+","+result[1]+")");	//Create output string when the word was found

			if(location == words.length-1)																//If this was the final word in the phrase
				output[0] = "was found:";																	//Change the initial message of the output accordingly
			else																						//If there are more words to find
			{
				output = findPhrase(result[0], result[1], words, location+1, board, 1);					//Initially search right
				if(output[0] == "was not found")														//If the word was not found
					output = findPhrase(result[0], result[1], words, location+1, board, 2);					//Search down
				if(output[0] == "was not found")														//Not found
					output = findPhrase(result[0], result[1], words, location+1, board, 3);					//Search left
				if(output[0] == "was not found")														//Not found
					output = findPhrase(result[0], result[1], words, location+1, board, 4);					//Search up
			}
		}
		
		if(output[0] == "was not found")																//If the word was not found in any direction
		{
			if(location == 0)																			//If this is the first word
			{
				for(int i = result[0]; i >= rows; i--)													//Revert all letters to lowercase
				{
					if(result[0] >= board.length-1 && i==result[0])
						i = board.length-1;
					board[i][columns] = Character.toLowerCase(board[i][columns]);	
				}
				for(int i = result[1]; i >= columns; i--)
				{
					if(result[0] >= board[0].length-1 && i==result[0])
						i = board[0].length-1;
					board[rows][i] = Character.toLowerCase(board[rows][i]);	
				}
			}
			else																						//must be handled separately so the last letter of the previous word is not erased
			{
				for(int i = result[0]-1; i > rows; i--)													//Revert all letters to lowercase
					board[i][columns] = Character.toLowerCase(board[i][columns]);		
				for(int i = result[1]-1; i > columns; i--)
					board[rows][i] = Character.toLowerCase(board[rows][i]);	
			}
		}
		else																							//If the word was found
			output[location+1] = toString;																//Set the output string

		return output;
	}
}