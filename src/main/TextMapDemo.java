package main;

import java.io.*;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import lib.TextMap;

public class TextMapDemo {
	final private static String[] library = new String[] { "signs", "abundantly", "he", "very", "had", "saw", "cattle",
			"that", "evening", "grass", "moved", "whose", "seasons", "doesn't", "male", "land", "hath", "also", "God",
			"man", "you'll", "creepeth", "fruit", "and", "fruitful", "wherein", "their", "own", "divide", "seed",
			"earth", "thing", "so", "created", "upon", "living", "you're", "under", "moveth", "life", "there", "them",
			"form", "beast", "heaven", "for", "you", "dominion", "waters", "which", "him", "creeping", "light", "fifth",
			"was", "every", "divided", "together", "place", "brought", "sea", "have", "female", "face", "rule", "them",
			"replenish", "blessed", "meat", "night", "I", "fourth"};

	private static String generateText(int lines) {
		Random rand = new Random();
		String string = "";
		System.out.println("----------");
		for (int l = 0; l < lines; l++) {
			if (lines >= 10 && l % (lines / 10) == 0)
				System.out.print("+");
			int words = rand.nextInt(20);
			for (int w = 0; w < words; w++) {
				if (w > 0)
					string += " ";
				int i = rand.nextInt(library.length);
				string += library[i];
			}
			string += ".\n";
		}
		System.out.print("\n");
		return string;
	}

	private static long operation1(TextMap textMap) {
		long total = 0;
		for (String word : library) {
			long time = System.nanoTime();
			textMap.contains(word);
			total += System.nanoTime() - time;
		}
		return total / library.length;
	}

	private static long operation2(TextMap textMap) {
		long total = 0;
		for (String word : library) {
			long time = System.nanoTime();
			textMap.getOccurances(word);
			total += System.nanoTime() - time;
		}
		return total / library.length;
	}

	private static long operation3(TextMap textMap) {
		long total = 0;
		for (String word : library) {
			long time = System.nanoTime();
			textMap.countOccurances(word, textMap.getRandomLine());
			total += System.nanoTime() - time;
		}
		return total / library.length;
	}

	private static long operation4(TextMap textMap) {
		long total = 0;
		for (int i = 0; i < library.length; i++) {
			long time = System.nanoTime();
			textMap.getWords(textMap.getRandomLine());
			total += System.nanoTime() - time;
		}
		return total / library.length;
	}

	private static void test() {
		for (int i = 0; i < 6; i++) {
			int lines = (int) Math.pow(10, i);
			System.out.println("Generating " + lines + " lines of text...");
			String text = generateText(lines);
			System.out.println("Generation completed.");
			System.out.println();

			System.out.println("Building data structure...");
			long time = System.nanoTime();
			TextMap textMap = new TextMap(text);
			time = System.nanoTime() - time;
			System.out.println("Build time: " + time / 1e6 + " ms");
			System.out.println();

			System.out.println("Testing operation 1...");
			time = operation1(textMap);
			System.out.println("Average completion time: " + time / 1e6 + " ms");
			System.out.println();

			System.out.println("Testing operation 2...");
			time = operation2(textMap);
			System.out.println("Average completion time: " + time / 1e6 + " ms");
			System.out.println();

			System.out.println("Testing operation 3...");
			time = operation3(textMap);
			System.out.println("Average completion time: " + time / 1e6 + " ms");
			System.out.println();

			System.out.println("Testing operation 4...");
			time = operation4(textMap);
			System.out.println("Average completion time: " + time / 1e6 + " ms");
			System.out.println();
		}
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		
		System.out.println(generateText(1));

		System.out.print("Enter 'test' to perform a stress test or enter a word from the following to test:\n\t");
		for (String word : library)
			System.out.print(word + " ");
		System.out.println();

		String word = "";
		while (word == "") {
			word = scanner.next();
			boolean contains = false;
			for (String w : library)
				if (word.equals(w))
					contains = true;
			if (!contains && !word.equals("test")) {
				word = "";
				System.out.println("Invalid entry, please try again:");
			}
		}

		if (word.equals("test"))
			test();
		else {
			TextMap textMap = new TextMap(generateText(1000));

			long time = 0;
			int line = 500;

			// Operation 1
			System.out.print("Starting operation 1...\n");

			time = System.nanoTime();
			boolean exists = textMap.contains(word);
			time = System.nanoTime() - time;

			System.out.print("The text does ");
			if (!exists)
				System.out.print("not ");
			System.out.print("contain the word '" + word + "'.\n");
			System.out.print("Operation 1 completed in " + time / 1e6 + " ms\n\n");

			// Operation 2
			System.out.print("Starting operation 2...\n");

			time = System.nanoTime();
			Set<Integer> lines = textMap.getOccurances(word);
			time = System.nanoTime() - time;

			System.out.print("The word '" + word + "' appears on line(s):\n");
			for (int l : lines)
				System.out.print(l + " ");
			System.out.println();
			System.out.print("Operation 2 completed in " + time / 1e6 + " ms\n\n");

			// Operation 3
			System.out.print("Starting operation 3...\n");

			time = System.nanoTime();
			int count = textMap.countOccurances(word, line);
			time = System.nanoTime() - time;

			System.out.print("The word '" + word + "' appears ");
			System.out.print(count);
			System.out.print(" time(s) on line " + line + ".\n");
			System.out.print("Operation 3 completed in " + time / 1e6 + " ms\n\n");

			// Operation 4
			System.out.print("Starting operation 4...\n");

			time = System.nanoTime();
			Set<String> words = textMap.getWords(line);
			time = System.nanoTime() - time;

			System.out.print("The following words appear on line " + line + ": ");
			for (String w : words)
				System.out.print(w + " ");
			System.out.println();
			System.out.print("Operation 4 completed in " + time / 1e6 + " ms\n\n");
		}

		scanner.close();
	}
}
