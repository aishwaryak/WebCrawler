package com.worddensity.constants;

/**
 * Constants for the web crawler
 * @author Aishwarya
 *
 */
public interface WordDensityConstants {

	/**
	 * Jsoup user agent
	 */
	public static final String JSOUP_USER_AGENT = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36";
	
	/**
	 * The optimal word density - I have assumed it to be 0.1 
	 */
	public static final double OPTIMAL_KEYWORD_DENSITY = 0.1;
	
	/**
	 * The filename+location that has the list of stop words
	 */
	public static final String STOP_WORDS_FILENAME = "com/worddensity/resources/stopWords.txt";
	
	/**
	 * List of typical shopping sites
	 */
	public static final String[] shoppingSites = {"amazon","ebay"};

	/**
	 * Url hitting exception
	 */
	public static final String URL_EXCEPTION = "Exception hitting url : ";

	/**
	 * No page contents
	 */
	public static final String NO_PAGE_CONTENTS = "No contents in the page";

	/**
	 * Parsing a shopping site
	 */
	public static final String PARSE_SHOPPINGSITE = "Parsing a shopping site";

	/**
	 * Parsing a blog/news site
	 */
	public static final String PARSING_BLOGSITE = "Parsing a blog/article";

	/**
	 * No information to display
	 */
	public static final String NO_WORD_TO_DISPLAY = "No information in the blog to parse.";

	/**
	 * Exception string
	 */
	public static final String EXCEPTION_STRING = "Exception : ";

	/**
	 * Words fetched
	 */
	public static final String WORDS_FETCHED = "Topics found:";

	/**
	 * Dashes - for display
	 */
	public static final String DASH = "================================";

	/**
	 * No header contents to parse
	 */
	public static final String NO_HEADER_CONTENTS = "No header information to parse.";

	/**
	 * Parsing string
	 */
	public static final String PARSE_STRING = "Parsing..";

	/**
	 * Unable to hit the url
	 */
	public static final String FAILURE_URL = "Failure hitting the url.";
}
