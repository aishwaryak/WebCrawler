package com.worddensity.types;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jsoup.nodes.Document;

import com.worddensity.constants.WordDensityConstants;
import com.worddensity.models.Word;
import com.worddensity.models.WordDensityModel;
import com.worddensity.utils.CountUtil;
import com.worddensity.utils.ParseUtil;
import com.worddensity.utils.WordUtil;

/**
 * Crawler methods for blogs specifically
 * @author Aishwarya
 * @version 1.2
 *
 */
public class BlogSiteCrawler {
	
	/**
	 * Logger for this class
	 */
	static Logger logger = Logger.getLogger(BlogSiteCrawler.class.getName());
	
	/**
	 * source class name
	 */
	static String sourceClass = BlogSiteCrawler.class.getName();
	
	/**
	 * Method to parse contents in a blog/article/news web page
	 * @param retrievedPageContents
	 * @param retrievedDoc
	 */
	public void parseForBlogs(String retrievedPageContents,
			Document retrievedDoc) {
		String sourceMethod = "parseForBlogs";
		logger.entering(sourceClass, sourceMethod);
		Set<String> articleWords = null;
		try {
			articleWords= ParseUtil.returnArticleContents(retrievedDoc); //Find the string in the <article> string
		} catch(NullPointerException exception) {
			logger.log(Level.SEVERE, WordDensityConstants.EXCEPTION_STRING+exception.getMessage());
		}
		if(articleWords == null) {
			logger.log(Level.WARNING, WordDensityConstants.NO_WORD_TO_DISPLAY);
		} else {
			try {
				analyseBlogWordFrequency(retrievedPageContents); //Data analysis function
			}catch (IOException exception) {
				logger.log(Level.SEVERE, WordDensityConstants.EXCEPTION_STRING+exception.getMessage());
			}
		}
		logger.exiting(sourceClass, sourceMethod);	
	}
	
	/**
	 * Analyses the word frequency in the page - based on word density
	 * @param retrievedPageContents
	 * @throws IOException
	 */
	private void analyseBlogWordFrequency(String retrievedPageContents) throws IOException {
		String sourceMethod = "analyseBlogWordFrequency";
		logger.entering(sourceClass, sourceMethod);
		WordUtil.populateStopWords(WordDensityConstants.STOP_WORDS_FILENAME); //populate a hashmap of all the stop words - O(1) retrieval

		WordDensityModel wordDensityModel = readWordsToMap(retrievedPageContents); //read the words and their total count to the WordDensity model
		displayKeyWordsForArticles(wordDensityModel); //Display those that fall within the optimal keyword range
		logger.exiting(sourceClass, sourceMethod);
	}
	
	
	/**
	 * Reads words of a line into a map - frequency updated on occurence of every word
	 * @param retrievedPageContents
	 * @return
	 * @throws IOException
	 */
	private WordDensityModel readWordsToMap(String retrievedPageContents) throws IOException {
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
 	        	line = line.replaceAll("[^a-zA-Z]+", " "); //Add all characters one wants to remove here 
 	        	String[] words = line.split(" ");//Split words based on spaces and other extraneous characters
 	        	int lineWordCount = words.length; //Count the number of words in the line
 	        	totalWordCount+=lineWordCount; //Add it up to the total - used for word density calculation
 	        	for(String word:words) {
 	        		if(!WordUtil.isStopWord(word) && word.length()>1) { 
 	        			//Put into map only if it is not a stop word
 	        			Word wordObj = countMap.get(word.toLowerCase()); //to avoid repetition - use lower case
 	 	                if (wordObj == null) {
 	 	                    wordObj = new Word();
 	 	                    wordObj.setWord(word);
 	 	                    wordObj.setCount(0);
 	 	                    countMap.put(word, wordObj);
 	 	                }
 	 	                int numCount = CountUtil.countWordsinLine(word.toLowerCase().trim(), line.toLowerCase().trim());
 	 	                wordObj.setCount(wordObj.getCount()+numCount); //Update incremented count
 	 	                countMap.put(word, wordObj);
 	        		}
 	        	}
        	 }
        }
        
        wordDensityModel.setCountMap(countMap);
        wordDensityModel.setTotalCount(totalWordCount);
        
        reader.close();
        logger.exiting(sourceMethod, sourceMethod);
        return wordDensityModel;
	}
	
	/**
	 * Displays the sorted key word list for blog sites
	 * compareTo is based on the word density
	 * @param wordDensityModel
	 */
	public void displayKeyWordsForArticles(WordDensityModel wordDensityModel) {
		
		String sourceMethod = "displayKeyWordsForArticles";
		logger.entering(sourceClass, sourceMethod);
		
		Map<String,Word> countMap = wordDensityModel.getCountMap();
        int totalCount = wordDensityModel.getTotalCount();
        
        Map<String,Double> finalMap = new HashMap<String,Double>();//To store the final map that has the filtered optimal word density
        
        Set<String> keySet = countMap.keySet();
        for (Iterator<String> i = keySet.iterator(); i.hasNext();) {
            String key = (String) i.next();
            Word value = (Word) countMap.get(key);
            value.setWordDensity(value.getCount()/totalCount*100);
            if(value.getWordDensity()<=WordDensityConstants.OPTIMAL_KEYWORD_DENSITY) { 
            	finalMap.put(value.getWord(), value.getWordDensity());
            }
          }//To store all those words with lesser than optimal word density in 'finalMap'
        
        System.out.println(WordDensityConstants.WORDS_FETCHED);
        System.out.println(WordDensityConstants.DASH);
        
        keySet = finalMap.keySet();
        for (Iterator<String> i = keySet.iterator(); i.hasNext();) {
        	String key = (String) i.next();
        	System.out.println(key);
        }//To print those fetched words
        
        logger.exiting(sourceClass, sourceMethod);
	}
	
}
