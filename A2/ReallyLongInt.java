// CS 0445 Spring 2017
// This is a partial implementation of the ReallyLongInt class.  You need to
// complete the implementations of the remaining methods.  Also, for this class
// to work, you must complete the implementation of the LinkedListPlus class.
// See additional comments below.

public class ReallyLongInt 	extends LinkedListPlus<Integer> 
							implements Comparable<ReallyLongInt>
{
	// Instance variables are inherited.  You may not add any new instance variables
	
	// Default constructor
	private ReallyLongInt()
	{
		super();
	}

	// Note that we are adding the digits here in the FRONT. This is more efficient
	// (no traversal is necessary) and results in the LEAST significant digit first
	// in the list.  It is assumed that String s is a valid representation of an
	// unsigned integer with no leading zeros.
	public ReallyLongInt(String s)
	{
		super();
		char c;
		int digit;
		// Iterate through the String, getting each character and converting it into
		// an int.  Then make an Integer and add at the front of the list.  Note that
		// the add() method (from A2LList) does not need to traverse the list since
		// it is adding in position 1.  Note also the the author's linked list
		// uses index 1 for the front of the list.
		for (int i = 0; i < s.length(); i++)
		{
			c = s.charAt(i);
			if (('0' <= c) && (c <= '9'))
			{
				digit = c - '0';
				this.add(1, new Integer(digit));
			}
			else throw new NumberFormatException("Illegal digit " + c);
		}
	}

	// Simple call to super to copy the nodes from the argument ReallyLongInt
	// into a new one.
	public ReallyLongInt(ReallyLongInt rightOp)
	{
		super(rightOp);
	}
	
	// Method to put digits of number into a String.  Since the numbers are
	// stored "backward" (least significant digit first) we first reverse the
	// number, then traverse it to add the digits to a StringBuilder, then
	// reverse it again.  This seems like a lot of work, but given the
	// limitations of the super classes it is what we must do.
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		if (numberOfEntries > 0)
		{
			this.reverse();
			for (Node curr = firstNode; curr != null; curr = curr.next)
			{
				sb.append(curr.data);
			}
			this.reverse();
		}
		return sb.toString();
	}

	// You must implement the methods below.  See the descriptions in the
	// assignment sheet

	public ReallyLongInt add(ReallyLongInt rightOp)
	{
		ReallyLongInt Z = new ReallyLongInt();	//RLI to be returned
		Node tempX = firstNode;	//node to iterate through this RLI
		Node tempY = rightOp.firstNode;	//node to iterate through the added RTI
		int sum = 0;
		int ones = 0;
		int tens = 0;

		
		if(numberOfEntries > rightOp.numberOfEntries)	//if this RLI is larger
		{				
			for(int i = 0; i < numberOfEntries; i++)
			{
				if(i < rightOp.numberOfEntries)	//while the smaller RLI has digits left
				{
					sum = (tempX.getData() + tempY.getData());	//add the digits together
					
					tempX = tempX.next;
					tempY = tempY.next;
				}
				else	//when the smaller RLI has been added
				{
					sum = tempX.getData();	//carry over the remaining digits from the larger RLI
					tempX = tempX.next;
				}
				
				sum+=tens;	//carry over the tens digits from the last add
				ones = sum%10;	//calculate the ones digit
				tens = sum/10;	//calculate the tens digit
				Z.add(new Integer(ones));	//add the ones digit to the new RLI
			}
		}
		else	//if the added RLI is larger
		{
			for(int i = 0; i < rightOp.numberOfEntries; i++)	
			{
				if(i < numberOfEntries)	//while the smaller RLI has digits left
				{
					sum = (tempX.getData() + tempY.getData());	//add the digits together
					
					tempX = tempX.next;
					tempY = tempY.next;					
				}
				else	//when the smaller RLI has been added
				{
					sum = tempY.getData();	//carry over the remaining digits from the larger RLI
					tempY = tempY.next;
				}
				
				sum+=tens;	//carry over the tens digit from the last add
				ones = sum%10;	//calculate the ones digit
				tens = sum/10;	//calculate the tens digits
				Z.add(new Integer(ones));	//add the ones digit to the new RLI
			}
		}
		if(tens > 0)	//if there was a remaining tens digit from the final add
			Z.add(new Integer(tens));	//add it to the RLI
		
		return Z;	//return the new RLI
	}
	
	public ReallyLongInt subtract(ReallyLongInt rightOp)
	{	
		ReallyLongInt Z = new ReallyLongInt();
		Node tempX = firstNode;
		Node tempY = rightOp.firstNode;
		int result = 0;
		int ones = 0;
		int tens = 0;
		
		if(this.compareTo(rightOp) < 0)	//if the RLI to be subtracted is greater than this RLI, throw an exception
			throw new ArithmeticException("Invalid Difference -- Negative Number"); //this should be an error that is caught by ArithmeticException
		
		for(int i = 0; i < numberOfEntries; i++)
		{
			if(i < rightOp.numberOfEntries)	//while the subtracted RLI has digits left
			{
				result = (tempX.getData() - tempY.getData());	//subtract the digits
				
				tempX = tempX.next;
				tempY = tempY.next;
			}
			else	//when the smaller RLI has been subtracted
			{
				result = tempX.getData();	//carry over the remaining digits from the larger RLI
				tempX = tempX.next;
			}
			
			result+=tens;	//carry over the tens digit from the last subtract
			
			if(result < 0)	//if the result of the subtraction is negative
			{
				Z.add(new Integer(result+10));	//add ten to it
				tens = -1;	//borrow from the tens digit
			}
			else	//otherwise proceed normally
			{
				Z.add(new Integer(result));
				tens = 0;
			}
		}
		
		Z.reverse();	//flip the RLI
		while(Z.firstNode.getData() == 0)	//remove any extraneous 0's
		{
			Z.firstNode = Z.firstNode.next;
			Z.numberOfEntries--;
		}
		Z.reverse();	//flip the RLI back
		return Z;	//return the resultant RLI
	}

	public int compareTo(ReallyLongInt rOp)
	{
		if(numberOfEntries > rOp.numberOfEntries)	//if RLI1 contains more digits that the other return 1
		{
			return 1;
		}
		else if(numberOfEntries < rOp.numberOfEntries) //If RLI1 contains less digits than the other return -1
		{
			return -1;
		}
		else	//If the two RLI's have the same amount of digits
		{
			reverse();	//flip the RLI to compare digits
			rOp.reverse();	//flip the RLI to compare digits
			Node tempX = firstNode;	//create node to traverse RLI
			Node tempY = rOp.firstNode;	//create node to traverse RLI
			do//while there is another node
			{
				if(tempX.getData() > tempY.getData())	//if this RLI has a digit greater than the comparative one, flip them both back and return 1
				{
					reverse();
					rOp.reverse();
					return 1;
				}
				else if(tempX.getData() < tempY.getData())	//if this RLI has a digit less than the comparative one, flip them both back and return -1
				{
					reverse();
					rOp.reverse();
					return -1;
				}
				else	//if this RLI has a digit equal to the comparative one, repeat this process
				{
					tempX = tempX.next;
					tempY = tempY.next;
				}
			}while((tempX != null));
		}
		reverse();	//in the case of equality flip the RLI's back
		rOp.reverse();
		return 0;	//return 0 indicating equality
	}

	public boolean equals(Object rightOp)
	{
		if(this.compareTo((ReallyLongInt) rightOp) == 0)	//if the two have identical digits, they are equal
			return true;
		return false;
	}

	public void multTenToThe(int num)
	{
		reverse();
		for(int i = 0; i < num; i++)	//add indicated number of 0's
			this.add(new Integer (0));
		reverse();
	}

	public void divTenToThe(int num)
	{
		for(int i = 0; i<num; i++)	//divide by(remove digits) the indicated power of ten
			firstNode = firstNode.next;
		numberOfEntries-=num;	//change the numberOfEntries accordingly
	}
}
