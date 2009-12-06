package search.tree;

public interface DepthLimitedSearch extends TreeSearch
{
	public void setDepthLimit(int limit);
	public int getDepthLimit();
}
