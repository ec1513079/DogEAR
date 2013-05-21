package co.jp.softbank.tech.ap.sca;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DatabaseBaseAdapter {
	
	static final String DATABASE_NAME = "sca.db";
	static final int DATABASE_VERSION = 1;
	
	protected Context mContext;
	protected DatabaseBaseHelper mHelper;
	protected SQLiteDatabase mDB;
	
	public DatabaseBaseAdapter(Context context, DatabaseBaseHelper helper, boolean readonly) {
		try {
			mContext = context;
			mHelper  = helper;
			mDB      = readonly ? helper.getReadableDatabase() : helper.getWritableDatabase();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				close(false);
			} catch (Exception e2) {
			}
		}
	}
	
	public void close(boolean commit) {
		if (mDB != null) {
			mDB.close();
			mDB = null;
		}
		if (mHelper != null) {
			mHelper.close();
			mHelper = null;
		}
	}
	
	public static class DatabaseBaseHelper extends SQLiteOpenHelper {

		public DatabaseBaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {	
		}
	}
	
	public static interface DatabaseBaseColumns extends BaseColumns {
		public static final String CREATE_TABLE = "CREATE TABLE ";
		public static final String DROP_TABLE   = "DROP TABLE IF EXISTS ";
	}
}
