package abalone.exec;

public interface StatisticGenerator 
{
	/**
	 * A method to convert the current state of the object to some long
	 */
	long getCurrentState();
	
	//TODO: make sure that one class can save multiple data about itself
}
