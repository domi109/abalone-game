package abalone.statistics;

public interface StatisticGenerator 
{
	/**
	 * A method to convert the current state of the object to some long
	 */
	double getCurrentState();
	
	//TODO: make sure that one class can save multiple data about itself
}
