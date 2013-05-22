package co.jp.softbank.tech.ap.sca;

import co.jp.softbank.tech.ap.sca.BookmarkProvider.BookmarkDatabaseColumns;
import co.jp.softbank.tech.ap.sca.BrowseHistoryProvider.BrowseHistoryDatabaseColumns;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.KeyEvent.DispatcherState;

public class SCADatabaseOpenHelper extends SQLiteOpenHelper {

	static final String DATABASE_NAME  = "sca.db";
	static final int DATABASE_VERSION  = 2;

	public SCADatabaseOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(BookmarkDatabaseColumns.SQL_CREATE_TABLE);
		db.execSQL(BrowseHistoryDatabaseColumns.SQL_CREATE_TABLE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(BookmarkDatabaseColumns.SQL_DROP_TABLE);
		db.execSQL(BrowseHistoryDatabaseColumns.SQL_DROP_TABLE);
		onCreate(db);
	}
}
