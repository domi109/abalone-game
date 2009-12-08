package abalone.ai.machinelearning;

public class MatchStats
{
	private boolean winner;
	private int pushedMarbles, lostMarbles;
	private int numberOfPlies;

	public MatchStats(boolean winner, int pushedMarbles, int lostMarbles, int numberOfPlies)
	{
		this.winner = winner;
		this.pushedMarbles = pushedMarbles;
		this.lostMarbles = lostMarbles;
		this.numberOfPlies = numberOfPlies;
	}

	public int getNumberOfPlies()
	{
		return numberOfPlies;
	}

	public boolean isWinner()
	{
		return winner;
	}

	public int getPushedMarbles()
	{
		return pushedMarbles;
	}

	public int getLostMarbles()
	{
		return lostMarbles;
	}
}
