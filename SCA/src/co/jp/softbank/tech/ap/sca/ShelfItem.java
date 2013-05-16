package co.jp.softbank.tech.ap.sca;

public class ShelfItem {

	public enum Type {
		PARENT, DIR, DOC
	}

	final public Type type;
	final public String name;
	final public String path;

	public ShelfItem (Type t, String n, String p) {
		type = t;
		name = n;
		path = p;
	}
}
