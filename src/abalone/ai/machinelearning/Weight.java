package abalone.ai.machinelearning;

import search.genetics.Gene;

import java.util.Random;

public class Weight implements Gene<Double>
{
    private Double value;

	public Weight()
	{
		Random r = new Random();

		value = r.nextDouble();
	}

	public Weight(int limit)
	{
		Random r = new Random();

		value = r.nextDouble() * limit;
	}

    public Weight(Double value)
    {
        this.value = value;
    }

    public void setValue(Double value)
    {
        this.value = value;
    }

    public Double getValue()
    {
        return value;
    }

    public boolean equals(Gene<Double> w)
    {
        //TODO: wrong

        return (value.compareTo(w.getValue()) == 0);
    }

    public Gene clone()
    {
        Weight tTemp = new Weight(value);
        return tTemp;
    }

    // Simple test of the class

    public static void main(String[] args)
    {
        Weight gene = new Weight(0.0);
        Weight gene2 = new Weight(0.000001);
        Weight gene3 = new Weight(2d);
        Weight gene4 = new Weight(3d);

        //gene.setValue(1d);

        System.out.println(gene.equals(gene2));
    }

	public String toString()
	{
		return value.toString();
	}
}
