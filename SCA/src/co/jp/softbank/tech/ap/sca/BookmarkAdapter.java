package co.jp.softbank.tech.ap.sca;

import java.util.ArrayList;
import java.util.LinkedList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.provider.ContactsContract.Contacts.Data;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class BookmarkAdapter extends DatabaseBaseAdapter {
	
	private static final String TABLE_NAME = "bookmark";
	
	public BookmarkAdapter(Context context) {
		super(context, new BookmarkDatabaseHelper(context));
	}
	
	public long insert(ContentValues values) throws SQLException {
		long rowID = mDB.insert(TABLE_NAME, "", values);
		if (rowID > 0) return rowID;
		throw new SQLException("Failed to insert row");
	}
	
	public int delete(long id, String selection, String[] selectionArgs) {
		int count = 0;
		count = mDB.delete(TABLE_NAME, 
				BaseColumns._ID + " = " + id
				+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
		return count;
	}
	
	public Cursor getAllRows() {
		Cursor c = null;
		try {
			c = mDB.rawQuery("select * from " + TABLE_NAME + ";", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return c;
	}
	
	public static class BookmarkDatabaseHelper extends DatabaseBaseHelper {

		public BookmarkDatabaseHelper(Context context) {
			super(context);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			super.onCreate(db);
			db.execSQL(BookmarkDatabaseColumns.SQL_CREATE_TABLE);
		}
	}
	
	public static interface BookmarkDatabaseColumns extends DatabaseBaseColumns {
		
		public static final String DB_BOOKMARK_COLUMNS_CDATE = "cdate";
		public static final String DB_BOOKMARK_COLUMNS_MDATE = "mdate";
		public static final String DB_BOOKMARK_COLUMNS_HASH  = "hash";
		public static final String DB_BOOKMARK_COLUMNS_PAGE  = "page";
		public static final String DB_BOOKMARK_COLUMNS_TITLE = "title";
		public static final String DB_BOOKMARK_COLUMNS_PATH  = "path";
		
		public static final String SQL_CREATE_TABLE	=
			CREATE_TABLE + TABLE_NAME + "(" +
			_ID + " INTEGER NOT NULL PRIMARY KEY,"  +
			DB_BOOKMARK_COLUMNS_CDATE + " INTEGER," +
			DB_BOOKMARK_COLUMNS_MDATE + " INTEGER," +
			DB_BOOKMARK_COLUMNS_HASH  + " TEXT,"    +
			DB_BOOKMARK_COLUMNS_PAGE  + " INTEGER," +
			DB_BOOKMARK_COLUMNS_TITLE + " TEXT, "     +
			DB_BOOKMARK_COLUMNS_PATH  + " TEXT"     + ")";
	}
}
