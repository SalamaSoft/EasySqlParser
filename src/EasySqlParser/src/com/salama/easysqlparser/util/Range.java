package com.salama.easysqlparser.util;

public class Range {
	public int start;
	
	/**
	 * the next position of the last char in target string
	 */
	public int end;
	
	public Range() {
		
	}
	
	public Range(int s, int e) {
		start = s;
		end = e;
	} 
}
