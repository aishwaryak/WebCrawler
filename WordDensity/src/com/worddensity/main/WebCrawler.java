package com.worddensity.main;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jsoup.nodes.Document;

import com.worddensity.constants.WordDensityConstants;
import com.worddensity.types.BlogSiteCrawler;
import com.worddensity.types.ShoppingSiteCrawler;
import com.worddensity.utils.ParseUtil;

/**
 * The main class that has gets inputs and calls appropriate crawlers
 * @author Aishwarya
 * @version 1.5
 *
 */
public class WebCrawler {

	/**
	 * Logger for this class
	 */
	static Logger logger = Logger.getLogger(WebCrawler.class.getName());
	/**
	 * source class name
	 */
	static String sourceClass = WebCrawler.class.getName();
	
	public static void main(String[] args) {
		String sourceMethod="main";
		logger.entering(sourceClass, sourceMethod);
		/*String url = "";
		if(args.length ==0 ||  args.length>1) {
			logger.log(Level.SEVERE,"Invalid parameters");
			return;
		}
		url = args[0];*/
		
		String url = "http://www.amazon.com/Cuisinart-CPT-122-Compact-2-Slice-Toaster/dp/B009GQ034C/ref=sr_1_1?s=kitchen&ie=UTF8&qid=1431620315&sr=1-1&keywords=toaster";
		
		/**
		 * The contents of the page in text
		 */
		String retrievedPageContents = "";
		
		/**
		 * The contents of the page in Document format
		 */
		Document retrievedDoc = null;
		/**
		 * Check whether it is a shopping site
		 */
		boolean isShoppingFlag = false;
		
		
		//Retrieve doc and its contents
		try {
			retrievedDoc = ParseUtil.retrieveContentsAsDoc(url);
			retrievedPageContents = ParseUtil.convertDelimiter(retrievedDoc);
		} catch (IOException exception) {
			logger.log(Level.SEVERE, WordDensityConstants.URL_EXCEPTION+exception.getMessage());
		}
		if(retrievedPageContents.length() <=0) {
			//If no contents are retrieved
			logger.log(Level.WARNING, WordDensityConstants.NO_PAGE_CONTENTS);
		}
		
		//Check what type of web page it is 
		//and correspondingly call specific methods
		for(String shoppingSite : WordDensityConstants.shoppingSites) {
			if(url.contains(shoppingSite)) {
				//shopping site
				logger.log(Level.INFO, WordDensityConstants.PARSE_SHOPPINGSITE);
				ShoppingSiteCrawler shoppingSiteCrawler = new ShoppingSiteCrawler();
				shoppingSiteCrawler.parseForShoppingSite(retrievedPageContents,retrievedDoc);
				isShoppingFlag = true;
				break;
			}
		}
		if(!isShoppingFlag) {
			//blog/articles/news
			logger.log(Level.INFO, WordDensityConstants.PARSING_BLOGSITE);
			BlogSiteCrawler blogSiteCrawler = new BlogSiteCrawler();
			blogSiteCrawler.parseForBlogs(retrievedPageContents,retrievedDoc);
		}
		logger.exiting(sourceClass, sourceMethod);
	}
	







	

	
	
	

	
	
	

}

