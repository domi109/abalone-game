package search.tree.games.minimax.hashing;

public class MinimaxHashEntry
{
	private int precision;
	private Double value;
	
	public MinimaxHashEntry(int precision, double value)
	{
		this.precision = precision;
		this.value = value;
	}
	
	public Double getValue()
	{
		return value;
	}

	public void setValue(Double value)
	{
		this.value = value;
	}

	public int getPrecision()
	{
		return precision;
	}
	
	public void setPrecision(int precision)
	{
		this.precision = precision;
	}
	
	@Override
	public String toString()
	{
		return "("+value+","+precision+")";
	}
}
