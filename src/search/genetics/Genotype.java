package search.genetics;

import java.util.ArrayList;

import abalone.ai.machinelearning.Weight;


public class Genotype extends ArrayList<Gene>
{
	private ArrayList<Gene> possibleValues;
	
	public Genotype(ArrayList<Gene> possibleValues)
	{
		this.possibleValues = possibleValues;
	}

	public Genotype(int length)
	{
		for (int i = 0; i < length; i++)
		{
			this.add(new Weight());
		}
	}
	
	public Genotype crossover(Genotype g2, int coPoint)
	{
		Genotype newGT = this.subSequence(0, coPoint);
		newGT.addAll(g2.subSequence(coPoint+1, this.size()-1));
		return newGT;
	}
	
	public Genotype subSequence(int firstGene, int lastGene)
	{
		Genotype newGenotype = new Genotype(possibleValues);
		newGenotype.addAll(super.subList(firstGene,lastGene+1));
		return newGenotype;
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
