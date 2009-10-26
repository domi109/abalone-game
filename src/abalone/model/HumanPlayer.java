package abalone.model;


public class HumanPlayer implements Player
{
	private String name;
	
	public HumanPlayer(String name)
	{
		this.name = name;
	}
	
	@Override
	public String getName()
	{
		return this.name;
	}

}
