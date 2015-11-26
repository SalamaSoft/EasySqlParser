package com.salama.easysqlparser.query;

import java.util.ArrayList;
import java.util.List;

import com.salama.easysqlparser.base.IValueStack;
import com.salama.easysqlparser.util.SqlParseException;

public class LiteralsValueStack implements IValueStack {
	public final static String SYMBOL_LITERALS = "$lite";

	private List<String> _literalsValueList = new ArrayList<String>();
	
	public static String getNameByIndex(int index) {
		return SYMBOL_LITERALS + index;
	}
	
	public void addValue(String value) {
		_literalsValueList.add(value);
	}
	
	public void addValue(int index, String value) {
		_literalsValueList.add(index, value);
	}

	@Override
	public String getValue(String name) throws SqlParseException {
		if(name.startsWith(SYMBOL_LITERALS)) {
			int index = Integer.parseInt(name.substring(SYMBOL_LITERALS.length()));
			
			if (index < _literalsValueList.size()) {
				return _literalsValueList.get(index);
			} else {
				//invalid index
				throw new SqlParseException("Invalid index in name:" + name);
			}
		} else {
			throw new SqlParseException("Invalid name:" + name);
		}
	}

	@Override
	public void setValue(String name, String value) throws SqlParseException {
		if(name.startsWith(SYMBOL_LITERALS)) {
			int index = Integer.parseInt(name.substring(SYMBOL_LITERALS.length()));
			if(index == _literalsValueList.size()) {
				_literalsValueList.add(value);
			} else if (index < _literalsValueList.size()) {
				_literalsValueList.set(index, value);
			} else {
				//invalid index
				throw new SqlParseException("Invalid index in name:" + name);
			}
		} else {
			throw new SqlParseException("Invalid name:" + name);
		}
	}

}
