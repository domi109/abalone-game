package search.optimization.genetics;

import java.util.ArrayList;
import java.util.Random;


public class Genotype extends ArrayList<Gene>
{
	private ArrayList<Gene> possibleValues;
	public static double mutationRate=0.1;
	
	public Genotype(ArrayList<Gene> possibleValues)
	{
		this.possibleValues = possibleValues;
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
	
	public void mutate()
	{
		Random r = new Random();
		for(int i = 0; i<this.size();i++)
		{
			if(r.nextDouble() < mutationRate)
			{
				this.set(i, genRandomGene());
			}
		}
		/*
		int i = (int)Math.round((Math.random()*(this.size()-1)));
		this.set(i, genRandomGene());
		*/
	}
	
	public Gene genRandomGene()
	{
		Random r = new Random();
		Gene newGene = possibleValues.get(r.nextInt(possibleValues.size()));
		return newGene;	
	}
}
