package search;


public class Action
{
	private String name;
	private double cost;
	
	public Action()
	{
		this("action");
	}

	public Action(String name)
	{
		setName(name);
		setCost(1);
		
	}
	
	public String getName()
	{
		return name;
	}
	
	@Override
	public String toString()
	{
		return name+"["+cost+"]";
	}
	
	public void setName(String desc)
	{
		name = desc;
	}

	public void setCost(double cost)
	{
		this.cost = cost;
	}

	public double getCost()
	{
		return cost;
	}
		
}
