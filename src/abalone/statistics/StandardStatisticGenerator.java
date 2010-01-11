package abalone.statistics;

public class StandardStatisticGenerator implements StatisticGenerator 
{
	double stat;
	
	public StandardStatisticGenerator()
	{
	}
	
	public double getCurrentState()
	{
		return stat;
	}
	
	public void increment()
	{
		stat++;
	}
	
	public void setStat(double s)
	{
		stat = s;
	}
}
