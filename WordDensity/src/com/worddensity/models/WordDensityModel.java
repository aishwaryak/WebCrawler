package com.worddensity.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds the details of every word, its density in the web page; also holds the
 * total number of words
 * 
 * @author Aishwarya
 *
 */
public class WordDensityModel {

	private int totalCount;
	private Map<String, Word> countMap;

	public WordDensityModel() {
		countMap = new HashMap<String, Word>();
		totalCount = 0;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public Map<String, Word> getCountMap() {
		return countMap;
	}

	public void setCountMap(Map<String, Word> countMap) {
		this.countMap = countMap;
	}

	@Override
	public String toString() {
		return "Word [totalCount=" + totalCount + ", countMap=" + countMap
				+ "]";
	}

}
