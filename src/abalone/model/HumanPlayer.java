package abalone.model;


public class HumanPlayer extends Player
{
	private static final long serialVersionUID = -6049398987397186067L;
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
