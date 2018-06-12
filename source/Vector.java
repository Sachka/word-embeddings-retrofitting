package msvs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class Vector {
	
	private static int INDEX;
	final private static Map<String, Integer> INDEXER;
	final private static Map<String, Vector> VECTORS;
	
	private final List<Double> value;
	private final String word;
	
	static {
		INDEX = 0;
		INDEXER = new HashMap<>();
		VECTORS = new HashMap<>();
	}
	
	private Vector (final String word) {
		this.word = word;
		this.value = new ArrayList<>();
		VECTORS.put(word, this);
	}
	
	public static Vector buildOrRetrieve(final String word) {
		Vector vector = VECTORS.get(word);
		if (vector == null) {
			vector = new Vector(word);
		}
		return vector;
	}
	
	public static Collection<Vector> getVectors() {
		return VECTORS.values();
	}
	
	public String getWord() {
		return this.word;
	}
	
	public Double[] getValue() {
		return this.value.toArray(new Double[]{});
	}
	
	public void update(final String contextWord) {
		if (! INDEXER.keySet().contains(contextWord)) {
			INDEXER.put(contextWord, INDEX++);
			VECTORS.values().stream().forEach(vector ->	vector.value.add(0.));
		}
		this.value.set(INDEXER.get(contextWord), this.value.get(INDEXER.get(contextWord)) + 1);
	}
	
	public Double cos (final Vector other) {
		return IntStream.range(0, INDEXER.size())
				.mapToObj(i -> this.value.get(i) * other.value.get(i))
				.reduce((a, b) -> a+b)
				.get()
			/ (Math.sqrt(
					this.value.stream()
						.map(a ->a*a)
						.reduce((a, b) ->a+b)
						.get())
				+ Math.sqrt(
					other.value.stream()
						.map(a ->a*a)
						.reduce((a, b)->a+b)
						.get()));
	}
	
	public static Double cos (final Vector one, final Vector other) {
		return one.cos(other);
	}
	
}
