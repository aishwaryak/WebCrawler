package com.worddensity.models;

/**
 * Class to store every word and the frequency count
 * 
 * @author Aishwarya
 *
 */
public class Word implements Comparable<Word> {

	/**
	 * Denotes a particular word found in the web page
	 */
	String word;
	/**
	 * Number of times this word has occured in the web page
	 */
	int count;
	/**
	 * The word density for the particular word
	 */
	double wordDensity;

	@Override
	public int hashCode() {
		return word.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return word.equals(((Word) obj).word);
	}

	@Override
	public int compareTo(Word b) {
		return b.count - count;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public double getWordDensity() {
		return wordDensity;
	}

	public void setWordDensity(double wordDensity) {
		this.wordDensity = wordDensity;
	}

	@Override
	public String toString() {
		return "Word [word=" + word + ", count=" + count + "]";
	}

}
