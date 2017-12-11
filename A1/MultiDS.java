
import java.util.Random;

public class MultiDS<T> implements PrimQ<T>, Reorder
{
	public T[] phys;
	public int log = 0;
	
	public MultiDS (int length)				//Constructor for the MultiDS
	{
		//@SupressWarnings("unchecked")		//Suppress the unchecked warning
		phys = (T[]) new Object[length];
	}
	
	//PrimQ Methods
	public boolean addItem(T item)			//Adds an item to the array
	{
		if(!full())							//If the array is not full add the item
		{
			phys[log] = item;
			log++;							//Adjust the logical size of the array accordingly
			return true;
		}
		
		return false;
	}
	
	public T removeItem()					//Removes an item from the array
	{
		if(empty())							//If the array is empty do nothing
			return null;

		T temp = phys[0];					//Move the oldest item in the array to a temporary object
		shiftLeft();						//Shift the array to fill in the blank spot
		phys[log-1] = null;					//Account for the removed digit in the array
		log--;								//Adjust the logical size of the array accordingly
		return temp;						//Return the temporary object
	}
	
	public boolean full()					//Checks if the array is full
	{
		if(log == phys.length)				//If the logical size of the array is equal to that of the physical return true
			return true;
		return false;						//If it is less than physical return false
	}
	
	public boolean empty()					//Checks if they array is empty
	{
		if(log <= 0)						//If the logical size of the array is less than or equal to 0 return true
			return true;
		return false;						//If it is greater than 0 return false
	}
	
	public int size()						//Checks the size of the array
	{
		return log;							//Return the logical size of the array
	}
	
	public void clear()						//Clears the array
	{
		for(int i = 0; i < log; i++)		//Set every location in the array to null
			phys[i] = null;					
		log = 0;							//Reset the logical size of the array to 0
	}
	
	//Reorder Methods
	public void reverse()					//Flips the array
	{
		T switchMe;
		for(int i = 0; i<log/2; i++)
		{
			switchMe = phys[i];				//Select left half item and save it to a temp object
			phys[i] = phys[log-i-1];		//Move the corresponding right half object to the location of the left half
			phys[log-i-1] = switchMe;		//Move the temp object to the location of the right half
		}
	}
	
	public void shiftRight()				//Shifts all objects in the array to the right
	{
		T tempT = phys[log-1];				//Select spot logical and save it to a temporary location
		for(int i = 1; i<log; i++)			//Move logical-2 item to logical-1, repeat until the item in spot 0 is placed in 1
			phys[log-i] = phys[log-i-1];
		phys[0] = tempT;				 	//Move the temporary object to spot 0
	}
	
	public void shiftLeft()					//Shifts all objects in the array to the left
	{
		T tempT = phys[0];					//Select spot logical and save it to a temporary location
		for(int i = 0; i<log-1; i++)		//Move 1 item to 0, repeat until the item in spot 1 is placed in 0
			phys[i] = phys[i+1];
		phys[log-1] = tempT;				//Move the temporary object to spot logical-1
	}
	
	public void shuffle()					//Randomly reorders the array
	{
		boolean numAdded, numUsed = false;
		int tempNum = -1;
		int [] usedNum = new int[log];
		T[] cloned = (T[]) new Object[log];
		Random Overhand = new Random();

		for(int i = 0; i < log; i++)			//Clone the array
			cloned[i] = phys[i];
		
		tempNum = Overhand.nextInt(log);
		phys[0] = cloned[tempNum];
		usedNum[0] = tempNum;
		
		for(int i = 1; i < log; i++)			//for each object in the array
		{
			numAdded = false;
			while(!numAdded)					
			{
				numUsed = false;
				tempNum = Overhand.nextInt(log);//Randomly select an object in the array
	
				for(int j = 1; j < usedNum.length; j++)
				{
					if(usedNum[j] == tempNum)	//If the number has been used
					{	
						numUsed = true;			//Set this boolean to true
					}
				}
				if(!numUsed)					//If this is false
				{
					phys[i] = cloned[tempNum];	//Move that object to the i-th position
					usedNum[i] = tempNum;		//Add the number to the list of those used
					numAdded = true;			//Acknowledge that the number has been added and proceed
				}
			}
		}
	}
	
	public String toString()				//Creates a readable output of the array's information
	{	
		StringBuilder SB = new StringBuilder();
		SB.append("Contents:");
		for(int i = 0; i < log; i++)
			SB.append(" "+phys[i]);
		return SB.toString();
	}
}