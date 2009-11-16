package search.genetics;

public interface Gene<T extends Comparable<T>>
{
	public void setValue(T value);
	public T getValue();
	public boolean equals(Gene<T> g2);
	public Gene<T> clone();
}
