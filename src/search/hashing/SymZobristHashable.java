package search.hashing;

import java.nio.ByteBuffer;
import java.util.List;

public interface SymZobristHashable extends ZobristHashable
{
	public List<ByteBuffer> symmetryHashes();
}
