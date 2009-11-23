package abalone.exec;

import abalone.adt.KeyValuePair;
import java.util.ArrayDeque;
import java.lang.Long;

public class Statistic 
{
	
	private String stepName;
	private String statName;
	private long step;
	private StatisticGenerator statGen;
	private ArrayDeque stats;
	
	/**
	 * Create a new statistic of a certain object that implements
	 * the StatisticGenerator interface
	 * @param gen an object that implements StatisticGenerator
	 */
	public Statistic(StatisticGenerator a)
	{
		stats = new ArrayDeque<KeyValuePair>();
		statGen = a;
	}
	
	/**
	 * Set the name for the y-axis (what kind of steps do you take?)
	 */
	public void setStepName(String name)
	{
		stepName = name;
	}
	
	/**
	 * Set the name for the x-axis (what info are you gathering)
	 */
	public void setStatName(String name)
	{
		statName = name;
	}
	
	/**
	 * A ticker that lets the "time" or whatever you're plotting against, pass
	 */
	public void step()
	{
		step++;
	}
	
	/**
	 * Call this every time you need the statistic to be recorded
	 */
	public void stat()
	{
		long state = statGen.getCurrentState()
		KeyValuePair<Long,Long> pair = new KeyValuePair<Long, Long>(new Long(step), new Long(state));
		stats.add(pair);
	}
	
	/**
	 * Call this when the statistic gathering is done and
	 * the statistic will be saved to a file
	 */
	public void save()
	{
		/* need to generate an image that qt can show
		 * and maybe also save the raw data
		 */
	}
}
