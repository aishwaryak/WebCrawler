package com.worddensity.utils;

import java.util.logging.Logger;

/**
 * Utility for counting words
 * @author Aishwarya
 *
 */
public class CountUtil {

	/**
	 * logger for this class
	 */
	static Logger logger = Logger.getLogger(CountUtil.class.getName());
	/**
	 * source class name
	 */
	private static String sourceClass = CountUtil.class.getName();
	
	/**
	 * To count the occurrences of a word in a single line
	 * @param word
	 * @param line
	 * @return
	 */
	public static int countWordsinLine(String word,String line){
		
		String sourceMethod = "countWordsinLine";
		logger.entering(sourceClass, sourceMethod);
		
		int count = 0;
		
		while (line.indexOf(word)>-1){
		    line = line.replaceFirst(word, "");
		    count++;
		}
		
		logger.exiting(sourceClass, sourceMethod);
		return count ;
	}

	public static int countTotalWords(String retrievedPageContents) {
		
		String sourceMethod = "countTotalWords";
		logger.entering(sourceClass, sourceMethod);
		
		int count = 0;
		String[] splitWords = retrievedPageContents.split(" ");
		count = splitWords.length;
		//System.out.println(retrievedPageContents);
		
		logger.exiting(sourceClass, sourceMethod);
		return count;
	}
}
