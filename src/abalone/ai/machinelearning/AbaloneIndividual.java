package abalone.ai.machinelearning;

import search.genetics.Gene;
import search.genetics.GeneticIndividual;
import search.genetics.Genotype;

import java.util.Random;

public class AbaloneIndividual implements GeneticIndividual
{
    Genotype phenotype;
    public static double mutationRate = 0.1;
    private double fitness;

    public AbaloneIndividual(Genotype genotype)
    {
        phenotype = genotype;
    }

    public double getFitness()
    {
        //TODO: define fitness
        return fitness;
    }

    public Genotype getPhenotype()
    {
        return phenotype;
    }

    public boolean equalsGenetic(GeneticIndividual j)
    {
        if(phenotype.size() != j.getPhenotype().size())
        {
            return false;
        }

        for (int i = 0; i < phenotype.size(); i++)
        {
            if (!(phenotype.get(i).equals(j.getPhenotype().get(i))))
            {
                return false;
            }
        }

        return true;
    }

    public GeneticIndividual reproduceWith(GeneticIndividual j)
    {
        return null;
    }

    public void mutate()
    {
        Random r = new Random();

        for (Gene g : phenotype)
        {
            double value = (Double) g.getValue();
            value *= ((1 - mutationRate) + (2 * mutationRate * r.nextDouble()));
            g.setValue(value);
        }
    }

    public int compareTo(GeneticIndividual genInd)
    {
        return Double.compare(fitness, genInd.getFitness());
    }
}
