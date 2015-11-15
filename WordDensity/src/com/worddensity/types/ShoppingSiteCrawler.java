package com.worddensity.types;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jsoup.nodes.Document;

import com.worddensity.constants.WordDensityConstants;
import com.worddensity.models.Word;
import com.worddensity.models.WordDensityModel;
import com.worddensity.utils.CountUtil;
import com.worddensity.utils.ParseUtil;

/**
 * Crawler methods for shopping sites specifically
 * @author Aishwarya
 * @version 1.2
 */
public class ShoppingSiteCrawler {
	
	/**
	 * Logger for this class
	 */
	static Logger logger = Logger.getLogger(ShoppingSiteCrawler.class.getName());
	/**
	 * source class name
	 */
	private static String sourceClass = ShoppingSiteCrawler.class.getName();
	
	/**
	 * Method to parse contents in a shopping web site
	 * @param retrievedPageContents
	 * @param retrievedDoc
	 */
	public void parseForShoppingSite(String retrievedPageContents,Document retrievedDoc) {
		String sourceMethod = "parseForShoppingSite";
		logger.entering(sourceClass, sourceMethod);
		
		Set<String> headerOneWords = null; 
		try {
			headerOneWords= ParseUtil.returnHeaderOneContents(retrievedDoc); //Find the string in the header <h1> string
		} catch(NullPointerException exception) {
			logger.log(Level.SEVERE, WordDensityConstants.EXCEPTION_STRING+exception.getMessage());
		}
		
		if(headerOneWords == null) {
			logger.log(Level.WARNING, WordDensityConstants.NO_HEADER_CONTENTS);
		} else {
			Set<String> possibleKeyWords = generatePossibleWordCombinations(headerOneWords);
			
			try {
				analyseShoppingWordFrequency(retrievedPageContents, possibleKeyWords); //Data analysis function
			} catch (IOException exception) {
				logger.log(Level.SEVERE, WordDensityConstants.EXCEPTION_STRING+exception.getMessage());
			}
		}
		logger.exiting(sourceClass, sourceMethod);
	}
	
	
	/**
	 * Analyses the word frequency in the page - Returns the top 10 frequently used words in the page
	 * @param retrievedPageContents
	 * @throws IOException
	 */
	private void analyseShoppingWordFrequency(String retrievedPageContents,Set<String> possibleKeyWords) throws IOException {
		String sourceMethod = "analyseShoppingWordFrequency";
		logger.entering(sourceClass, sourceMethod);
		
		WordDensityModel wordDensityModel = readWordsToMap(retrievedPageContents,possibleKeyWords); //read the words and their total count to the WordDensity model
		displayKeyWordsForShops(wordDensityModel); //Display those that fall within the optimal keyword range
		logger.exiting(sourceClass, sourceMethod);
	}
	
	/**
	 * Generate a combination of all words in a string
	 * @param headerOneWords
	 * @return
	 */
	private Set<String> generatePossibleWordCombinations(Set<String> headerOneWords) {
		String sourceMethod = "generatePossibleWordCombinations";
		logger.entering(sourceClass, sourceMethod);
		
		Set<String> possibleWordCombinations = new LinkedHashSet<String>();
		for(String header:headerOneWords) {
			//headerOneWords is a list of header strings 
			//so 'header' gives u one header string
			//splitting this would give us several words in the header
			String[] splitWords = header.split(" ");
			possibleWordCombinations.addAll(Arrays.asList(splitWords));
		}
		
		logger.exiting(sourceClass, sourceMethod);
		return possibleWordCombinations;
	}
	
	/**
	 * Read the words of a line into a map based on a possible set of keywords 
	 * (from some title line in the web page)
	 * @param retrievedPageContents
	 * @param possibleKeyWords
	 * @return
	 * @throws IOException
	 */
	private WordDensityModel readWordsToMap(String retrievedPageContents,Set<String> possibleKeyWords) throws IOException {
		
		String sourceMethod = "readWordsToMap";
		logger.entering(sourceClass, sourceMethod);
		
		WordDensityModel wordDensityModel = new WordDensityModel();
		
		ByteArrayInputStream bytesInputStream = new ByteArrayInputStream(retrievedPageContents.getBytes(StandardCharsets.UTF_8));
		BufferedReader reader = new BufferedReader(new InputStreamReader(bytesInputStream));
		
		int totalWordCount = 0; //Indicates the total number of words in the document 	
		Map<String, Word> countMap = new HashMap<String, Word>();
		
        String line; //the line that is read
        while ((line = reader.readLine()) != null) {
        	 line = line.trim();
        	 if(!line.equals("")) {
 	        	int lineWordCount = line.split(" ").length; //Count the number of words in the line
 	        	totalWordCount+=lineWordCount; //Add it up to the total - used for word density calculation
 	        	
            	for(String keyword : possibleKeyWords) {
            		//System.out.println("keywords"+keyword);
	                if(line.toLowerCase().trim().contains(keyword.toLowerCase().trim())) {
	                	Word wordObj = countMap.get(keyword);
		                if (wordObj == null) {
		                    wordObj = new Word();
		                    wordObj.setWord(keyword);
		                    wordObj.setCount(0);
		                    countMap.put(keyword, wordObj);
		                }
		                int numCount = CountUtil.countWordsinLine(keyword.toLowerCase().trim(), line.toLowerCase().trim());
		                wordObj.setCount(wordObj.getCount()+numCount);//Update Incremented count
		                countMap.put(keyword, wordObj);
	                }
            	}
        	}
        }	
        
        wordDensityModel.setCountMap(countMap); 
        wordDensityModel.setTotalCount(totalWordCount);
        
        reader.close();
        
        logger.exiting(sourceClass, sourceMethod);
        return wordDensityModel;
	}
	
	/**
	 * Displays the sorted key word list for shopping sites
	 * compareTo is based on the word count from the header string
	 * @param wordDensityModel
	 */
	public void displayKeyWordsForShops(WordDensityModel wordDensityModel) {
		
		String sourceMethod = "displayKeyWordsForShops";
		logger.entering(sourceClass, sourceMethod);
		
		Map<String,Word> countMap = wordDensityModel.getCountMap();
        int totalCount = wordDensityModel.getTotalCount();
		int iterator = 0;
        int maxWordsToDisplay = 10;
      
        System.out.println(WordDensityConstants.WORDS_FETCHED);
        System.out.println(WordDensityConstants.DASH);
        SortedSet<Word> sortedWords = new TreeSet<Word>(countMap.values());
         
         for (Word word : sortedWords) {
             if (iterator >= maxWordsToDisplay) {
             	//Display until ten words
                 break;
             }
             word.setWordDensity(word.getCount()/totalCount);
             if(word.getWordDensity()<=WordDensityConstants.OPTIMAL_KEYWORD_DENSITY) { 
            	 System.out.println(word.getWord());
             }
             iterator++;
         }
         
         logger.exiting(sourceClass, sourceMethod);
	}
	

}
