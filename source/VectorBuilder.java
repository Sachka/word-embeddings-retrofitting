package msvs;

import java.util.Collection;
import java.util.List;

public abstract class VectorBuilder {

	private VectorBuilder() {
		super();	
	}
	
	final public static Collection<Vector> build(final VectorBuilder builder, final String unparsedText) {
		for (final String sentence : builder.splitIntoSentences(unparsedText)) {
			List<String> tokens = builder.toTokens(sentence);
			for (String token : tokens) {
				Vector v = Vector.buildOrRetrieve(token);
				tokens.stream().filter(tok -> ! tok.equals(token)).forEach(tok -> v.update(tok));
			}
		}
		return Vector.getVectors();
	}
	
	public abstract List<String> splitIntoSentences(final String unparsedText);
	
	public abstract List<String> toTokens(final String sentence);
	
}
