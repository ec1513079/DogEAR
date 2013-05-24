package com.ec1513079.dogear;

public class ShelfItem {

	public enum Type {
		PARENT,
		DIR,
		DOC,
	};

	final public Type   type;
	final public String name;
	final public String inner_path;
	
	public ShelfItem (Type t, String n, String p) {
		type       = t;
		name       = n;
		inner_path = p;
	}
}
