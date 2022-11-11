package lib;

import java.util.HashMap;
import java.util.Set;

final class Count<T> extends HashMap<T, Integer> {
	private static final long serialVersionUID = 1L;
};

final class LineMap extends HashMap<Integer, Count<String>> {
	private static final long serialVersionUID = 1L;
};

final class WordMap extends HashMap<String, Count<Integer>> {
	private static final long serialVersionUID = 1L;
};

public class TextMap {
	final private static String LINE_DELIMITER = "\n";
	final private static String WORD_DELIMITER = " ";
	final private static String WORD_PATTERN = "^[^\\w]+|[^\\w]+$";

	private static String clean(String word) {
		return word.replaceAll(WORD_PATTERN, "").toLowerCase();
	}

	private LineMap lines = new LineMap();
	private WordMap words = new WordMap();

	public TextMap() {

	}

	public TextMap(String text) {
		build(text);
	}

	public void clear() {
		lines.clear();
		words.clear();
	}

	public void build(String text) {
		int lineNumber = 0;
		for (String line : text.split(LINE_DELIMITER)) {
			lineNumber++;
			for (String word : line.split(WORD_DELIMITER)) {
				insert(clean(word), lineNumber);
			}
		}

	}

	private void insert(String word, int line) {
		// System.out.println("Inserting '" + word + "' from line " + line);

		// Check if map contains key for line
		if (lines.containsKey(line)) {

			int wordCount = 0;
			Count<String> words = lines.get(line);

			// Get count if key for word exists
			if (words.containsKey(word))
				wordCount = words.get(word);

			// Overwrite count object for line
			words.put(word, ++wordCount);

		} else {
			// Create new count object with one entry
			Count<String> count = new Count<String>();
			count.put(word, 1);

			// Insert into hash map
			lines.put(line, count);
		}

		// Check if map contains key for word
		if (words.containsKey(word)) {
			int lineCount = 0;
			Count<Integer> lines = words.get(word);

			// Get count if key for word exists
			if (lines.containsKey(line))
				lineCount = lines.get(line);

			// Overwrite count object for word
			lines.put(line, ++lineCount);

		} else {
			// Create new count object with one entry
			Count<Integer> count = new Count<Integer>();
			count.put(line, 1);

			// Insert into hash map
			words.put(word, count);
		}
	}

	public int getRandomLine() {
		return (int) (Math.random() * lines.size());
	}

	public boolean contains(String word) {
		return words.containsKey(clean(word));
	}

	public Set<Integer> getOccurances(String word) {
		if (!words.containsKey(clean(word)))
			return null;
		return words.get(clean(word)).keySet();
	}

	public int countOccurances(String word, int line) {
		if (!lines.containsKey(line))
			return 0;
		if (!lines.get(line).containsKey(clean(word)))
			return 0;
		return lines.get(line).get(clean(word));
	}

	public Set<String> getWords(int line) {
		if (!lines.containsKey(line))
			return null;
		return lines.get(line).keySet();
	}

	@Override
	public String toString() {
		String str = "TextMap:[";
		str += "\n\tlines: {";
		for (int line : lines.keySet()) {
			str += "\n\t\t" + line + ": {";
			for (String word : lines.get(line).keySet()) {
				if (!str.endsWith("{"))
					str += ", ";
				str += "\n\t\t\t" + word + ": " + lines.get(line).get(word);
			}
			str += "\n\t\t},";
		}
		str += "\n\t},";
		str += "\n\twords: {";
		for (String word : words.keySet()) {
			str += "\n\t\t" + word + ": { ";
			for (int line : words.get(word).keySet()) {
				if (!str.endsWith("{ "))
					str += ", ";
				str += line + ": " + words.get(word).get(line);
			}
			str += " },";
		}
		str += "\n\t}";
		str += "\n]\n";
		return str;
	}
}
