package com.salama.easysqlparser.util;

public class CharDelimStringTokenizer {
	private StringBuilder _input;
	private char[] _delims;
	private boolean[] _isDelimAsToken;
	
	private int _curPos = 0;
	private int _tokenStart = -1;
	private int _tokenEnd = 0;
	private int _endIndex;
	
	public CharDelimStringTokenizer(StringBuilder input, 
			char[] delims, boolean[] isDelimAsToken) {
		this(input, 0, input.length(), delims, isDelimAsToken);
	}

	/**
	 * 
	 * @param input
	 * @param beginIndex Search range from beginIndex.
	 * @param endIndex Search range to endIndex(exclude endIndex).
	 * @param delims
	 * @param isDelimAsToken
	 */
	public CharDelimStringTokenizer(StringBuilder input, int beginIndex, int endIndex, 
			char[] delims, boolean[] isDelimAsToken) {
		_input = input;
		_delims = delims;
		_isDelimAsToken = isDelimAsToken;
		_tokenEnd = beginIndex;
		_endIndex = endIndex;
	}
	
	public boolean hasMoreTokens() {
		int i,k;
		char c;
		
		//check _curPos, skip continuous delims(move the curPos)
		boolean isHitDelim = false;
		for(_curPos = _tokenEnd; _curPos < _endIndex; _curPos++) {
			c = _input.charAt(_curPos);
			
			isHitDelim = false;
			for(k = 0; k < _delims.length; k++) {
				if(c == _delims[k]) {
					if(_isDelimAsToken[k]) {
						_tokenStart = _curPos;
						_tokenEnd = _curPos + 1;
						return true;
					} else {
						isHitDelim = true;
						break;
					}
				}
			}
			
			if(!isHitDelim) {
				break;
			}
		}
		
		for(i = _curPos; i < _endIndex; i++) {
			c = _input.charAt(i);
			
			for(k = 0; k < _delims.length; k++) {
				if(c == _delims[k]) {
					_tokenStart = _curPos;
					_tokenEnd = i;
					
					return true;
				}
			}
		}
		
		if(_curPos == _endIndex) {
			_tokenStart = -1;
			return false;
		} else {
			_tokenStart = _curPos;
			_tokenEnd = i;
			return true;
		}
	}
	
	public String nextToken() {
		if(_tokenStart < 0) {
			return null;
		} else {
			return _input.substring(_tokenStart, _tokenEnd);
		}
	}
}
