// CS 0445 Spring 2017 
// LinkedListPlus<T> class partial implementation

// See the commented methods below.  You must complete this class by
// filling in the method bodies for the remaining methods.  Note that you
// may NOT add any new instance variables, but you may use method variables
// as needed.

public class LinkedListPlus<T> extends A2LList<T>
{
	// Default constructor simply calls super()
	public LinkedListPlus()
	{
		super();
	}
	
	// Copy constructor.  This is a "deepish" copy so it will make new
	// Node objects for all of the nodes in the old list.  However, it
	// is not totally deep since it does NOT make copies of the objects
	// within the Nodes -- rather it just copies the references.
	public LinkedListPlus(LinkedListPlus<T> oldList)
	{
		super();
		if (oldList.getLength() > 0)
		{
			// Special case for first Node since we need to set the
			// firstNode instance variable.
			Node temp = oldList.firstNode;		// front of old list
			Node newNode = new Node(temp.data);	// copy the data
			firstNode = newNode;				// set front of new list
			
			// Now we traverse the old list, appending a new Node with
			// the correct data to the end of the new list for each Node
			// in the old list.  Note how the loop is done and how the
			// Nodes are linked.
			Node currNode = firstNode;
			temp = temp.next;
			while (temp != null)
			{
				currNode.next = new Node(temp.data);
				temp = temp.next;
				currNode = currNode.next;
			}
			numberOfEntries = oldList.numberOfEntries;
		}			
	}

	// Make a StringBuilder then traverse the nodes of the list, appending the
	// toString() of the data for each node to the end of the StringBuilder.
	// Finally, return the StringBuilder as a String.
	public String toString()
	{
		StringBuilder b = new StringBuilder();
		for (Node curr = firstNode; curr != null; curr = curr.next)
		{
			b.append(curr.data.toString());
			b.append(" ");
		}
		return b.toString();
	}

	// Shift left num positions.  This will shift out the first num Nodes
	// of the list, with Node num+1 now being at the front.
	public void leftShift(int num)
	{
		if (num >= this.getLength())	// If we shift more than the number of
		{								// Nodes, just initialize to empty
			firstNode = null;
			numberOfEntries = 0;
		}
		else if (num > 0)
		{
			Node temp = firstNode;		// Start at front
			for (int i = 0; i < num-1; i++)	 // Get to node BEFORE the one that 
				temp = temp.next;		// should be the new firstNode
			firstNode = temp.next;		// Set firstNode to Node after temp
			temp.next = null;		    // Disconnect old prefix of list (just
										// to be extra cautious)
			numberOfEntries = numberOfEntries - num;	// Update logical size
		}
	}

	// Remove num items from the end of the list
	public void rightShift(int num)
	{
		if(num <= 0)	//if the number is zero or less do nothing
		{
		
		}
		else if(num >= this.getLength())	//if the number is greater than the size of the list, clear everything
		{
			firstNode = null;
			numberOfEntries = 0;
		}
		else
		{
			Node temp = firstNode;		//start at the first node
			for(int i = 0; i<(numberOfEntries-(num+1)); i++)	//navigate to the node number specified
				temp = temp.next;		//get to the new last node
			temp.next = null;	//terminate the remaining nodes after it
			
			numberOfEntries = numberOfEntries - num;	//adjust the number of entries accordingly
		}
	}

	// Remove from the front and add at the end.  Note that this method should
	// not create any new Node objects -- it is simply moving them.  If num
	// is negative it should still work, actually doing a right rotation.
	Node temp;
	Node temp2;
	public void leftRotate(int num)
	{
		if(num == 0)	//if there is zero rotation do nothing
		{
		
		}
		else if(num < 0)	//if rotation is negative rotate right instead
		{
			rightRotate(Math.abs(num));
		}
		else
		{
			for(int i = 0; i<num; i++)	//rotate desired amount of times
			{
				temp = firstNode;
				temp2 = temp.next;	
				for(int j = 0; j<numberOfEntries-1; j++)	//get to the last node
				{
					temp = temp.next;	//get to the last spot
				}
				temp.next = firstNode;	//move the firstNode after the last spot
				temp.next.next = null;	//set this node to be the new last node
				firstNode = temp2;	//set the node that was originally second to the last spot
			}
		}
	}

	// Remove from the end and add at the front.  Note that this method should
	// not create any new Node objects -- it is simply moving them.  If num
	// is negative it should still work, actually doing a left rotation.
	public void rightRotate(int num)
	{
		if(num == 0)	//if there is zero rotation do nothing
		{
		
		}
		else if(num < 0)
		{
			leftRotate(Math.abs(num));	//if rotation is negative rotate left instead
		}
		else
		{
			for(int i = 0; i<num; i++)	//rotate desired amount of times
			{
				for(Node prev = firstNode; prev.next != null; prev = prev.next)	//reach the last node
				{
					temp = prev;	//save the second to last node
					temp2 = prev.next;	//save the last node
				}
				
				
				temp.next = null;	//make the second to last node the last one
				temp = firstNode;	//save the first node
				firstNode = temp2;	//make the original last node first
				temp2.next = temp;	//insert the original first node after the new first
			}
		}
	}
	
	// Reverse the nodes in the list.  Note that this method should not create
	// any new Node objects -- it is simply moving them.
	T tempData;
	public void reverse()
	{
		temp = firstNode;
		for(int i = 1; i < (numberOfEntries/2)+1; i++)	//access the data for the first half of the list
		{
			temp2 = firstNode;
			for(int j = i; j < numberOfEntries; j++)	//access data for the second half of the array
				temp2 = temp2.next;

			tempData = temp.data;	//get the data for the firstNode+i
			temp.data = temp2.data;	//set firstNode+i's data to NE-i
			temp2.data = tempData;	//set NE-i's data to firstNode+i
			
			temp = temp.next;		//go to the next node in the first half
		}
	}		
}
