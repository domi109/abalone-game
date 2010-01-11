package abalone.ai.machinelearning;

import java.util.Random;

import nl.maastrichtuniversity.dke.libreason.genetics.Gene;

public class Weight implements Gene<Double>
{
    private Double value;

	public Weight()
	{
		Random r = new Random();

		value = 2*r.nextDouble()-1;
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

    @Override
	public Gene<Double> clone()
    {
        Weight tTemp = new Weight(value);
        return tTemp;
    }

	@Override
	public String toString()
	{
		return value.toString();
	}
}
