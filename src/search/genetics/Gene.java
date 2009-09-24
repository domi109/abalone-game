package search.genetics;

public interface Gene
{
	public void setValue(Object value);
	public Object getValue();
	public boolean equals(Gene g2);
	public Object clone();
}
