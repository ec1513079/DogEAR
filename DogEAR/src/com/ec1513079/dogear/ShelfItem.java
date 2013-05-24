package com.ec1513079.dogear;

public class ShelfItem {

	public enum Type {
		DIR,
		DOC,
		LINK,
		PARENT,
	};

	final public Type     type;
	final public String   name;
	final public String[] label;
	
	/* DIRのみ */
	final String children;
	final String password;
	
	/* DOCのみ */
	final public String inner_path;
	final public String sha1;
	final public String ext;
	final public int    page;
	final public double size;
	final public String datetime;
	final public String modified_date;
	final public String thumbnail;

	/* LINKのみ */
	final public String[] target;
	
	public ShelfItem (Type t, String n, String p) {
		type  = t;
		name  = n;
		label = null;
		
		children = null;
		password = null;
		
		inner_path    = null;
		sha1          = null;
		ext           = null;
		page          = 0;
		size          = 0;
		datetime      = null;
		modified_date = null;
		thumbnail     = null;
		
		target = null;
	}
}
