/*
	Input and Variable Setup
		Get from user: size of array, # trials
		
		Random - must randomly generate random integers -- same integers for all tests, different ones for different trials
		Sorted - successive integers 1 up
		Reverse - decreasing integers length down
	Timing
		Use System.nanoTime()
		long start = System.nanoTime() (starts AFTER arrays are filled)
		long end = System.nanoTime()
		long delta = end-start
		Divide by 1 billion to get time in seconds if wanted
		
		Add times together and average out

	Output
		Algorithm: Algorithm Name
		Array Size: Size
		Order: (random, sorted, reverse)
		Number of Trials: num
		Average time: avg of trials sec.
		
	Trace Output
		Automatically happens when array size <= 20
		OUTPUT ABOVE FOR EACH TRIAL OF EACH ALGORITHM(IE 10 TIMES PER ALGORITHM)
		
	Excel Stuff
*/

import java.util.Scanner;
import java.util.Random;
import java.util.Arrays;
import java.io.*;

public class Assig4
{
	public static void main (String[] args)
	{
		Scanner in = new Scanner(System.in);
		System.out.print("Enter array size:");
		int arrSize = in.nextInt();
		System.out.print("Enter number of trials:");
		int numTrials = in.nextInt();
		in.nextLine();
		System.out.print("Enter output file name:");											//GET THE OUTPUT NAME
		String output = in.nextLine();
		System.out.println();
		
		PrintWriter out = null;
		
		try
		{
			out = new PrintWriter(output);
		}
		catch(IOException e)
		{
			System.out.println("Error in creating PrintWriter");
		}
		
//Filling the arrays
		int[] randomBase = new int[arrSize];
		int[] sortedBase = new int[arrSize];
		int[] reverseBase = new int[arrSize];
		
		Random gen = new Random();
		
		for(int i = 0; i<arrSize; i++)
		{
			randomBase[i] = gen.nextInt(arrSize);
			sortedBase[i] = i;
			reverseBase[i] = arrSize-i;
		}
		
//Random
		if(arrSize<=100000)
		{
			quickSort(randomBase, "Random", numTrials, false, out);		//Simple QuickSort
			System.out.println("QS complete");
		}
		medianOfThree(randomBase, "Random", numTrials, 5, out);		//Median of Three (Base case 5)
			System.out.println("MT5 complete");
		medianOfThree(randomBase, "Random", numTrials, 20, out);		//Median of Three (Base case 20)
			System.out.println("MT20 complete");		
		medianOfThree(randomBase, "Random", numTrials, 100, out);	//Median of Three (Base case 100)
			System.out.println("MT100 complete");		
		quickSort(randomBase, "Random", numTrials, true, out);		//Random Pivot (Base case 5)
			System.out.println("RP complete");		
		mergeSort(randomBase, "Random", numTrials, out);				//MergeSort
			System.out.println("MS complete");		
		
//Sorted
		if(arrSize<=100000)
		{
			quickSort(sortedBase, "Sorted", numTrials, false, out);		//Simple QuickSort		
			System.out.println("QS complete");	
		}
		medianOfThree(sortedBase, "Sorted", numTrials, 5, out);		//Median of Three (Base case 5)
					System.out.println("MT5 complete");
		medianOfThree(sortedBase, "Sorted", numTrials, 20, out);		//Median of Three (Base case 20)
					System.out.println("MT20 complete");
		medianOfThree(sortedBase, "Sorted", numTrials, 100, out);	//Median of Three (Base case 100)
					System.out.println("MT100 complete");
		quickSort(sortedBase, "Sorted", numTrials, true, out);		//Random Pivot (Base case 5)
					System.out.println("RP complete");
		mergeSort(sortedBase, "Sorted", numTrials, out);				//MergeSort
					System.out.println("MS complete");
		
//Reverse
		if(arrSize<=100000)
		{
			quickSort(reverseBase, "Reverse", numTrials, false, out);	//Simple QuickSort
				System.out.println("QS complete");
		}
		medianOfThree(reverseBase, "Reverse", numTrials, 5, out);	//Median of Three (Base case 5)
			System.out.println("MT5 complete");
		medianOfThree(reverseBase, "Reverse", numTrials, 20, out);	//Median of Three (Base case 20)
			System.out.println("MT20 complete");
		medianOfThree(reverseBase, "Reverse", numTrials, 100, out);	//Median of Three (Base case 100)
			System.out.println("MT100 complete");
		quickSort(reverseBase, "Reverse", numTrials, true, out);	//Random Pivot (Base case 5)
			System.out.println("RP complete");
		mergeSort(reverseBase, "Reverse", numTrials, out);			//MergeSort
			System.out.println("MS complete");
		
		out.close();
	}
	
//Implementing Methods
	private static void swap(int[] a, int i, int j)	//Swap is used in multiple sorting methods
	{
		int temp = a[i];
		a[i] = a[j];
		a[j] = temp; 
	} 

	//QuickSort + Random Pivot
	public static void quickSort(int[] a, String type, int numTrials, boolean RP, PrintWriter out)
	{
		String trialName;
		if(!RP)
			trialName = "Simple QuickSort";
		else
			trialName = "Random Pivot (5)";
		long start;
		long finish;
		long delta = 0;
		int[] currTrial;
		for(int i = 0; i<numTrials; i++)
		{
			currTrial = Arrays.copyOf(a, a.length);
			
			if(a.length <= 20)
			{
				System.out.println("Algorithm: "+trialName+"\nArray Size: "+a.length+"\nOrder: "+type);
				for(int j = 0; j<a.length; j++)
					System.out.print(currTrial[j]+" ");
				System.out.println();
			}
			
			start = System.nanoTime();
			quickSort(currTrial, currTrial.length, RP);
			finish = System.nanoTime();
			delta += finish-start;	

			if(a.length <= 20)
			{
				for(int j = 0; j<a.length; j++)
					System.out.print(currTrial[j]+" ");
				System.out.println();
			}
		}
		double seconds = ((double)delta)/((double)numTrials)/1000000000;
		out.println("Algorithm: "+trialName+"\nArray Size: "+a.length+"\nOrder: "+type+"\nNumber of Trials: "+numTrials+"\nAverage Time: "+seconds+" seconds\n");
	}
	public static void quickSort(int[] a, int n, boolean RP)
	{
		quickSort(a, 0, n-1, RP);
	}

	public static void quickSort(int[] a, int first, int last, boolean RP)
	{
		if (first < last)
		{
			if(last - first + 1 < 5)
				insertionSort(a, first, last);
			else
			{
				int pivotIndex = partition(a, first, last, RP);

				quickSort(a, first, pivotIndex-1, RP);
				quickSort(a, pivotIndex+1, last, RP);
			}
		}
	}

	private static int partition(int[] a, int first, int last, boolean RP)
	{
		int pivotIndex;
		if(RP)
		{
			Random gen = new Random();
			pivotIndex = gen.nextInt(last-first)+first;
		}
		else
			pivotIndex = last;
		
		int pivot = a[pivotIndex];
		
		a[pivotIndex] = a[last];	//will do nothing if standard quickSort
		a[last] = pivot;			//will move random pivot index to the last spot if random

		int indexFromLeft = first; 
		int indexFromRight = last - 1; 

		boolean done = false;
		while (!done)
		{
			while (a[indexFromLeft] < pivot)
				indexFromLeft++;

			while ((a[indexFromRight] > pivot) && indexFromRight > first)
				indexFromRight--;

			if (indexFromLeft < indexFromRight)
			{
				swap(a, indexFromLeft, indexFromRight);
				indexFromLeft++;
				indexFromRight--;
			}
			else 
				done = true;
		}
		
		swap(a, pivotIndex, indexFromLeft);
		pivotIndex = indexFromLeft;

		return pivotIndex; 
	}

	//Median of Three
	public static void medianOfThree(int[] a, String type, int numTrials, int baseCase, PrintWriter out)
	{
		long start;
		long finish;
		long delta = 0;
		int[] currTrial;
		for(int i = 0; i<numTrials; i++)
		{
			currTrial = Arrays.copyOf(a, a.length);
			
			if(a.length <= 20)
			{
				System.out.println("Algorithm: Median Of Three ("+baseCase+")\nArray Size: "+a.length+"\nOrder: "+type);
				for(int j = 0; j<a.length; j++)
					System.out.print(currTrial[j]+" ");
				System.out.println();
			}			
			
			start = System.nanoTime();
			medianOfThree(currTrial, currTrial.length, baseCase);
			finish = System.nanoTime();
			delta += finish-start;	

			if(a.length <= 20)
			{
				for(int j = 0; j<a.length; j++)
					System.out.print(currTrial[j]+" ");
				System.out.println();
			}
		}
		double seconds = ((double)delta)/((double)numTrials)/1000000000;
		out.println("Algorithm: Median of Three ("+baseCase+")\nArray Size: "+a.length+"\nOrder: "+type+"\nNumber of Trials: "+numTrials+"\nAverage Time: "+seconds+" seconds\n");
	}
	
	public static void medianOfThree(int[] a, int n, int MIN_SIZE)
	{
		medianOfThree(a, 0, n-1, MIN_SIZE);
	}
	
	public static void medianOfThree(int[] a, int first, int last, int MIN_SIZE)
	{
	  if (last - first + 1 < MIN_SIZE)
	    insertionSort(a, first, last);
	  else
	  {
	    int pivotIndex = medianOfThreePartition(a, first, last);

	    medianOfThree(a, first, pivotIndex - 1);
	    medianOfThree(a, pivotIndex + 1, last);
	  }
	} 
	
	private static int medianOfThreePartition(int[] a, int first, int last)
	{
	  int mid = (first + last)/2;
	  sortFirstMiddleLast(a, first, mid, last);

	  swap(a, mid, last - 1);
	  int pivotIndex = last - 1;
	  int pivot = a[pivotIndex];
	  
	  int indexFromLeft = first + 1; 
	  int indexFromRight = last - 2; 
	  boolean done = false;
	  while (!done)
	  {
	    while (a[indexFromLeft] < pivot)
	      indexFromLeft++;

	    while (a[indexFromRight] > pivot)
	      indexFromRight--;
	      
	    assert a[indexFromLeft] >= pivot && 
	           a[indexFromRight] <= pivot;
	           
	    if (indexFromLeft < indexFromRight)
	    {
	      swap(a, indexFromLeft, indexFromRight);
	      indexFromLeft++;
	      indexFromRight--;
	    }
	    else 
	      done = true;
	  }
	  
	  swap(a, pivotIndex, indexFromLeft);
	  pivotIndex = indexFromLeft;

	  return pivotIndex; 
	}
	
	private static void sortFirstMiddleLast(int[] a, int first, int mid, int last)
	{
	  order(a, first, mid);
	  order(a, mid, last);
	  order(a, first, mid);
	} 
	
	private static void order(int[] a, int i, int j)
	{
	  if (a[i] > a[j])
	    swap(a, i, j);
	} 

	public static void insertionSort(int[] a, int n)
	{
		insertionSort(a, 0, n - 1);
	}

	public static void insertionSort(int[] a, int first, int last)
	{
		int unsorted, index;
		
		for (unsorted = first + 1; unsorted <= last; unsorted++)
		{
		
			int firstUnsorted = a[unsorted];
			
			insertInOrder(firstUnsorted, a, first, unsorted - 1);
		}
	}

	private static void insertInOrder(int element, int[] a, int begin, int end)
	{
		int index;
		
		for (index = end; (index >= begin) && (element < a[index]); index--)
		{
			a[index + 1] = a[index];
		} 

		a[index + 1] = element;
	}
	
	//MergeSort
	public static void mergeSort(int[] a, String type, int numTrials, PrintWriter out)
	{	
		long start;
		long finish;
		long delta = 0;
		int[] currTrial;
		for(int i = 0; i<numTrials; i++)
		{
			currTrial = Arrays.copyOf(a, a.length);
			
			if(a.length <= 20)
			{
				System.out.println("Algorithm: MergeSort\nArray Size: "+a.length+"\nOrder: "+type);
				for(int j = 0; j<a.length; j++)
					System.out.print(currTrial[j]+" ");
				System.out.println();
			}
			
			start = System.nanoTime();
			mergeSort(currTrial, currTrial.length);
			finish = System.nanoTime();
			delta += finish-start;	
			
			if(a.length <= 20)
			{
				for(int j = 0; j<a.length; j++)
					System.out.print(currTrial[j]+" ");
				System.out.println();
			}
		}
		double seconds = ((double)delta)/((double)numTrials)/1000000000;
		out.println("Algorithm: MergeSort\nArray Size: "+a.length+"\nOrder: "+type+"\nNumber of Trials: "+numTrials+"\nAverage Time: "+seconds+" seconds\n");
	}
	public static void mergeSort(int[] a, int n)
	{
		mergeSort(a, 0, n - 1);
	}

	public static void mergeSort(int[] a, int first, int last)
	{
		int[] tempArray = new int[a.length];
		mergeSort(a, tempArray, first, last);
	}
	
	private static void mergeSort(int[] a, int[] tempArray, int first, int last)
	{
	   if (first < last)
	   {
			int mid = (first + last)/2;
			mergeSort(a, tempArray, first, mid); 
			mergeSort(a, tempArray, mid + 1, last);
			
			if (a[mid] - a[mid + 1] > 0)
	     	 	merge(a, tempArray, first, mid, last); 
	   }
	}
	
	private static void merge(int[] a, int[] tempArray, int first, int mid, int last)
	{
		int beginHalf1 = first;
		int endHalf1 = mid;
		int beginHalf2 = mid + 1;
		int endHalf2 = last;

		int index = beginHalf1; 
		
		for (; (beginHalf1 <= endHalf1) && (beginHalf2 <= endHalf2); index++)
		{ 
	      if (a[beginHalf1]- a[beginHalf2] <= 0)
	      {  
	      	tempArray[index] = a[beginHalf1];
	        beginHalf1++;
	      }
	      else
	      {  
	      	tempArray[index] = a[beginHalf2];
	        beginHalf2++;
	      }
	   } 

	   for (; beginHalf1 <= endHalf1; beginHalf1++, index++)
	      tempArray[index] = a[beginHalf1];

		for (; beginHalf2 <= endHalf2; beginHalf2++, index++)
	      tempArray[index] = a[beginHalf2];
		
	   for (index = first; index <= last; index++)
	      a[index] = tempArray[index];
	}
}