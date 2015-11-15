package com.worddensity.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Utility for dealing with methods associated with stop words
 * @author Aishwarya
 *
 */
public class WordUtil {

	/**
	 * Map of stop words - For O(1) get, using HashSet
	 */
	private static Set<String> stopWords;  

	/**
	 * logger for this class
	 */
	static Logger logger = Logger.getLogger(WordUtil.class.getName());
	/**
	 * source class name
	 */
	private static String sourceClass = WordUtil.class.getName();
	
	/**
	 * Return true if it is a stop word. else return false.
	 * 
	 * @param word
	 * @return
	 */
	public static boolean isStopWord(String word) {
		String sourceMethod = "isStopWord";
		
		logger.entering(sourceClass, sourceMethod);
		logger.exiting(sourceClass, sourceMethod);
		
		return stopWords.contains(word.toLowerCase());
	}

	/**
	 * Populate the stop words
	 * 
	 * @param fileName
	 * @throws IOException
	 */
	public static void populateStopWords(String fileName) throws IOException, NullPointerException {
		
		String sourceMethod = "populateStopWords";
		logger.entering(sourceClass, sourceMethod);
		
		String currentLine;
		// Source - http://www.ranks.nl/stopwords
		// TODO - this source must be automatically populated based on general
		// learning from every crawl
		/*BufferedReader bufferedReader = new BufferedReader(new FileReader(
				fileName));*/
		InputStream inputStream = 
			    WordUtil.class.getClassLoader().getResourceAsStream(fileName);
		BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(inputStream, "UTF-8")));
		
		stopWords = new HashSet<String>();

		while ((currentLine = bufferedReader.readLine()) != null) {
			stopWords.add(currentLine);
		}
		bufferedReader.close();
		logger.exiting(sourceClass, sourceMethod);
	}

	/*
	 * public static void main(String[] args) { try {
	 * populateStopWords("src/com/worddensity/resources/stopWords.txt"
	 * ); } catch (IOException e) { e.printStackTrace(); }
	 * System.out.println(isStopWord("The")); }
	 */
}
