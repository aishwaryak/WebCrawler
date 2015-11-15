package com.worddensity.utils;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import com.worddensity.constants.WordDensityConstants;

/**
 * Utility class that holds methods for parsing
 * @author Aishwarya
 *
 */
public class ParseUtil {

	/**
	 * logger for this class
	 */
	static Logger logger = Logger.getLogger(ParseUtil.class.getName());
	/**
	 * source class name
	 */
	private static String sourceClass = ParseUtil.class.getName();
	
	/**
	 * Retieve contents of the page in the form of a document
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static Document retrieveContentsAsDoc(String url) throws IOException {
		String sourceMethod = "retrieveContentsAsDoc";
		logger.entering(sourceClass, sourceMethod);
		
		int responseVariable = 0;
		Document document = null;
		Connection connection = Jsoup.connect(url).userAgent(WordDensityConstants.JSOUP_USER_AGENT)
				.timeout(12000);
		Response response = connection.execute();
		responseVariable = response.statusCode();
		if (responseVariable == 200) {
			logger.log(Level.INFO, WordDensityConstants.PARSE_STRING);
			document = connection.get();
		} else {
			logger.log(Level.WARNING, WordDensityConstants.FAILURE_URL);
		}
		
		logger.exiting(sourceClass, sourceMethod);
		return document;
	}
	
	/**
	 * Returns the string contained in < h1 > tag
	 * @param document
	 * @return
	 * @throws NullPointerException
	 */
	public static Set<String> returnHeaderOneContents(Document document) throws NullPointerException {
		
		  String sourceMethod = "returnHeaderOneContents";
		  logger.entering(sourceClass, sourceMethod);
		  
		  Set<String> headerStrings = new TreeSet<String>();
	      Elements headers = document.select("h1"); //Could be null - Throws NullPointerException
	      
	      for (Element header : headers) {
	        headerStrings.add(Jsoup.parse(header.text()).text());
	      }
	      
	      logger.exiting(sourceClass, sourceMethod);
	      return headerStrings;
	}
	
	/**
	 * Returns the string contained in '< h2 >' tag in a web page
	 * @param document
	 * @return
	 * @throws NullPointerException
	 */
	public static Set<String> returnHeaderTwoContents(Document document) throws NullPointerException {
		
		  String sourceMethod = "returnHeaderTwoContents";
		  logger.entering(sourceClass,sourceMethod);
		  
		  Set<String> headerStrings = new TreeSet<String>();
	      Elements headers = document.select("h2");
	      
	      for (Element header : headers) {
	        headerStrings.add(Jsoup.parse(header.text()).text());
	      }
	      
	      logger.exiting(sourceClass,sourceMethod);
	      return headerStrings;
	}
	
	/**
	 * Proceses documents and transforms to text;
	 * Converts '< br >' and next-line tags to \n in java
	 * @param document
	 * @return
	 */
	public static String convertDelimiter(Document document) {
		
		String sourceMethod = "convertDelimiter";
		logger.entering(sourceClass,sourceMethod);
		
	    document.outputSettings(new Document.OutputSettings().prettyPrint(false));//makes html() preserve linebreaks and spacing
	    document.select("br").append("\\n");
	    document.select("p").prepend("\\n\\n");
	    String s = document.html().replaceAll("\\\\n", "\n");
	    s = Jsoup.parse(s).text();
	    
	    logger.exiting(sourceClass, sourceMethod);
	    return Jsoup.clean(s, "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));
	}

	/**
	 * Returns the string contained in < article > tag in a web page
	 * @param document
	 * @return
	 * @throws NullPointerException
	 */
	public static Set<String> returnArticleContents(Document document) {
		
		String sourceMethod = "returnArticleContents";
		logger.entering(sourceClass, sourceMethod);
		
		Set<String> articleStrings = new TreeSet<String>();
		Elements articles = document.select("article");
	      
		for (Element article : articles) {
			articleStrings.add(Jsoup.parse(article.text()).text());
		}
		logger.exiting(sourceClass, sourceMethod);
		return articleStrings;
	}
	
}
