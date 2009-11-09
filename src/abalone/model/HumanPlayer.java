package abalone.model;
import java.io.Serializable;


public class HumanPlayer implements Player, Serializable
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
