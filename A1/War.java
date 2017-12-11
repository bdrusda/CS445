import java.util.*;
public class War
{
	static MultiDS<Card> deck, P1, PD1, P2, PD2, stakes;
	static Card P1PC, P1WW, P1WC, P2PC, P2WW, P2WC;
	static int roundLim, roundNum, staked, transferSize;
	static boolean endGame = false;
	public static void main (String [] args)
	{
		roundLim = 0;
		roundNum = 0;
		try
		{
			roundLim = Integer.parseInt(args[0]);	//Get a round limit from user
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			roundLim = 9999;
			System.out.println("No round limit specified, defaulted to:"+roundLim);	//Default to set value if not entered
		}
		
		deck = new MultiDS<Card>(52);	//Create the deck
		P1 = new MultiDS<Card>(52);		//Create the first player's hand
		PD1 = new MultiDS<Card>(52);	//Create the first player's discard pile
		P2 = new MultiDS<Card>(52);		//Create the second player's hand
		PD2 = new MultiDS<Card>(52);	//Create the second player's discard pile
		stakes = new MultiDS<Card>(52);	//Create the cards at stake
		
		fillDeck();	//Fill the deck
		deck.shuffle();	//Shuffle the deck
		
		Card dealer;
		
		System.out.println("Now dealing the cards.");	//Deal cards to players
		while(!deck.empty())
		{
			dealer = deck.removeItem();
			P1.addItem(dealer);
			dealer = deck.removeItem();
			P2.addItem(dealer);
		}
		
		System.out.println("Here is Player 1's Hand:\n"+P1.toString()+"\n");	//Output both players' hands
		System.out.println("Here is Player 2's Hand:\n"+P2.toString()+"\n");
		
		System.out.println("Starting the war:");	//Begin the actual game
		
		//		P1 has cards in hand or discard, P2 has cards in hand or discard AND the round limit has not been reached
		while((((!P1.empty() || !PD1.empty()) && (!P2.empty() || !PD2.empty())) && (roundNum < roundLim)) && !endGame)
		{
			stakes.clear();
			staked = 2;
			if(!P1.empty() && !P2.empty())	//If both players have cards
			{
				P1PC = P1.removeItem();	//Put the cards up against each other
				P2PC = P2.removeItem();
				stakes.addItem(P1PC);
				stakes.addItem(P2PC);
				
				if(P1PC.compareTo(P2PC) > 0)	//If player one has the better card
				{
					System.out.println("Player 1 Wins Round "+roundNum+": "+P1PC.toString()+" beats "+P2PC.toString()+" : "+staked+" Cards");
					for(int i = 0; i < staked; i++)
						PD1.addItem(stakes.removeItem());
				}
				else if(P1PC.compareTo(P2PC) < 0)	//If player two has the better card
				{
					System.out.println("Player 2 Wins Round "+roundNum+": "+P2PC.toString()+" beats "+P1PC.toString()+" : "+staked+" Cards");
					for(int i = 0; i < staked; i++)
						PD2.addItem(stakes.removeItem());
				}
				else	//If there is a draw
				{
					war();
				}

				roundNum++;	//Increment the round number
			}
			else	//If one player is out of cards, refill their hand
				fillHand();	
		}
		
		if((P1.size()+PD1.size()) > (P2.size()+PD2.size()))	//If player one has more cards at the end
			System.out.println("Player 1 has "+(P1.size() + PD1.size())+" cards to Player 2's "+(P2.size() + PD2.size())+".  Player 1 is the winner!");
		else if((P1.size()+PD1.size()) < (P2.size()+PD2.size()))	//If player two has more cards at the end
			System.out.println("Player 2 has "+(P2.size() + PD2.size())+" cards to Player 1's "+(P1.size() + PD1.size())+".  Player 2 is the winner!");
		else	//If there is a draw
			System.out.println("Round limit reached, there has been a draw!");
	}
	
	public static void fillDeck()	//Fill the deck with the 52 standard cards
	{
		for(Card.Suits s: Card.Suits.values())
		{
			for(Card.Ranks r: Card.Ranks.values())
				deck.addItem(new Card (s, r));
		}
	}
	
	public static void war()	//War scenario
	{
			staked+=4;
			System.out.println("\tWAR: "+P1PC.toString()+" ties "+P2PC.toString());
			
			warFillHand();	//Assure the players have enough cards in their hands to continue a war
			if(endGame)	//If one player does  not have enough cards left end game
			{
			
			}
			else	
			{
				P1WW = P1.removeItem();
				P2WW = P2.removeItem();
				stakes.addItem(P1WW);
				stakes.addItem(P2WW);
				System.out.println("\tPlayer 1: "+P1WW.toString()+" and Player 2: "+P2WW.toString()+" are at stake!");	//Announce the cards at stake
					
				P1WC = P1.removeItem();
				P2WC = P2.removeItem();
				stakes.addItem(P1WC);
				stakes.addItem(P2WC);
				if(P1WC.compareTo(P2WC) > 0)	//If player one wins
				{
					System.out.println("Player 1 Wins Round "+roundNum+": "+P1WC.toString()+" beats "+P2WC.toString()+" : "+staked+" Cards");	
					for(int i = 0; i < staked; i++)
						PD1.addItem(stakes.removeItem());
				}
				else if(P1WC.compareTo(P2WC) < 0)	//If player two wins
				{
					System.out.println("Player 2 Wins Round "+roundNum+": "+P2WC.toString()+" beats "+P1WC.toString()+" : "+staked+" Cards");
					for(int i = 0; i < staked; i++)
						PD2.addItem(stakes.removeItem());
				}
				else	//If there is another draw
				{
					war();
				}
			}
	}
	
	public static void fillHand()	//Refill the players hand from his discard pile
	{	
		if(P1.empty())
		{
			transferSize = PD1.size();
			System.out.println("\tPlayer 1's hand is empty.  Adding from discard pile and reshuffling.");
			for(int i = 0; i < transferSize; i++)
				P1.addItem(PD1.removeItem());
			P1.shuffle();
		}
		else
		{
			transferSize = PD2.size();
			System.out.println("\tPlayer 2's hand is empty.  Adding from discard pile and reshuffling.");
			for(int i = 0; i < transferSize; i++)
				P2.addItem(PD2.removeItem());
			P2.shuffle();
		}
	}
	
	public static void warFillHand()	//Assure both players have enough cards to continue the war
	{	
		if(P1.size() < 2)	//If player one does not have enough cards, take his full discard pile
		{
			transferSize = PD1.size();
			System.out.println("\tPlayer 1's hand does not have enough cards for a war.  Adding from discard pile.");
			for(int i = 0; i < transferSize; i++)
				P1.addItem(PD1.removeItem());
			PD1.toString();
		}
		
		if(P2.size() < 2)	//If player two does not have enough cards, take his full discard pile
		{
			transferSize = PD2.size();
			System.out.println("\tPlayer 2's hand does not have enough cards for a war.  Adding from discard pile.");
			for(int i = 0; i < transferSize; i++)
				P2.addItem(PD2.removeItem());
		}
		
		if(P1.size() < 2 || P2.size() < 2)	//If either player doesn't have enough cards for a war, transfer the staked cards to the winner and end the game
		{
			System.out.println("\tNot enough cards for a war.");

			if((P1.size()+PD1.size()) < (P2.size()+PD2.size()))
			{
				transferSize = P1.size();
				for(int i = 0; i < transferSize; i++)
					stakes.addItem(P1.removeItem());
				transferSize = PD1.size();
				for(int i = 0; i < transferSize; i++)
					stakes.addItem(PD1.removeItem());
				transferSize = stakes.size();
				for(int i = 0; i < transferSize; i++)
					PD2.addItem(stakes.removeItem());
			}
			else
			{
				transferSize = P2.size();
				for(int i = 0; i < transferSize; i++)
					stakes.addItem(P2.removeItem());
				transferSize = PD2.size();
				for(int i = 0; i < transferSize; i++)
					stakes.addItem(PD2.removeItem());
				transferSize = stakes.size();
				for(int i = 0; i < transferSize; i++)
					PD1.addItem(stakes.removeItem());
			}

			endGame = true;
		}
	}
}