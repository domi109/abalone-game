package search.genetics;

import java.util.ArrayList;
import java.util.Collection;

import abalone.ai.machinelearning.Weight;


public class Genotype extends ArrayList<Gene>
{
	private static final long serialVersionUID = 4110730467197626434L;

	public Genotype()
	{
		super();
	}

	public Genotype(Collection<? extends Gene> c)
	{
		super(c);
	}

	public Genotype(int initialCapacity)
	{
		super(initialCapacity);
	}

	public String toString()
	{
		String str = "";

		for (Gene g : this)
		{
			str += g.toString() + " ";
		}

		return str;
	}
}
