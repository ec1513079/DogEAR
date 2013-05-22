package co.jp.softbank.tech.ap.sca;

import java.util.List;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;

public class BrowseHistoryProvider extends ContentProvider {

    private static final String AUTHORITY = "co.jp.softbank.tech.ap.sca.BrowseHistoryProvider";
    private static final UriMatcher sUriMatcher;
    static {
    	sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, BrowseHistoryDatabaseColumns.TABLE_NAME, 0);
    }
    public static final String CONTENT_URI = "content://" + AUTHORITY + "/" + BrowseHistoryDatabaseColumns.TABLE_NAME;

	public static interface BrowseHistoryDatabaseColumns extends BaseColumns {
		
		public static final String CREATE_TABLE = "CREATE TABLE ";
		public static final String DROP_TABLE   = "DROP TABLE IF EXISTS ";
		
		public static final String TABLE_NAME   = "history";

		public static final String DB_COLUMNS_CDATE = "cdate";
		public static final String DB_COLUMNS_MDATE = "mdate";
		public static final String DB_COLUMNS_HASH  = "hash";
		public static final String DB_COLUMNS_TITLE = "title";
		public static final String DB_COLUMNS_PATH  = "path";
		
		public static final String SQL_CREATE_TABLE	=
			CREATE_TABLE + TABLE_NAME + "(" +
			_ID + " INTEGER NOT NULL PRIMARY KEY,"  +
			DB_COLUMNS_CDATE + " INTEGER," +
			DB_COLUMNS_MDATE + " INTEGER," +
			DB_COLUMNS_HASH  + " TEXT,"    +
			DB_COLUMNS_TITLE + " TEXT, "     +
			DB_COLUMNS_PATH  + " TEXT"     + ")";
		
		public static final String SQL_DROP_TABLE = 
				DROP_TABLE + TABLE_NAME;
	}

    private SQLiteOpenHelper mSQLiteOpenHelper;

	@Override
	public boolean onCreate() {
		mSQLiteOpenHelper = new SCADatabaseOpenHelper(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		if(sUriMatcher.match(uri) < 0)
			throw new IllegalArgumentException("unknown uri : " + uri);
		
		SQLiteDatabase db = mSQLiteOpenHelper.getReadableDatabase();
		Cursor cursor = db.query(
				uri.getPathSegments().get(0),
				projection,
				appendSelection(uri, selection),
                appendSelectionArgs(uri, selectionArgs),
				null,
				null,
				sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		if(sUriMatcher.match(uri) < 0)
			throw new IllegalArgumentException("unknown uri : " + uri);
		
        SQLiteDatabase db = mSQLiteOpenHelper.getWritableDatabase();
        final long rowId = db.insertOrThrow(uri.getPathSegments().get(0), null, values);
        Uri returnUri = Uri.parse(CONTENT_URI + "/" + rowId);
        getContext().getContentResolver().notifyChange(returnUri, null);
        return returnUri;
    }
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		if(sUriMatcher.match(uri) < 0)
			throw new IllegalArgumentException("unknown uri : " + uri);
		
        SQLiteDatabase db = mSQLiteOpenHelper.getWritableDatabase();
        final int count = db.delete(
        		uri.getPathSegments().get(0),
        		appendSelection(uri, selection),
        		appendSelectionArgs(uri, selectionArgs));
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

	@Override
	 public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		if(sUriMatcher.match(uri) < 0)
			throw new IllegalArgumentException("unknown uri : " + uri);
		
        SQLiteDatabase db = mSQLiteOpenHelper.getWritableDatabase();
        final int count = db.update(
        		uri.getPathSegments().get(0),
        		values,
        		appendSelection(uri, selection),
        		appendSelectionArgs(uri, selectionArgs));
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
	}

	@Override
	public String getType(Uri uri) {
		if(sUriMatcher.match(uri) < 0)
			throw new IllegalArgumentException("unknown uri : " + uri);
		
		return null;
	}
	
	/**
     * Uriで_idの指定があった場合, selectionにそれを連結して返す.
     * 
     * @param uri Uri
     * @param selection 絞り込み条件
     * @return _idの条件が連結されたselection
     */
    private String appendSelection(Uri uri, String selection) {
        List<String> pathSegments = uri.getPathSegments();
        if (pathSegments.size() == 1) {
            return selection;
        }
        return BaseColumns._ID + " = ?" + (selection == null ? "" : " AND (" + selection + ")");
    }
     
    /**
     * Uriで_idの指定があった場合, selectionArgsにそれを連結して返す.
     * 
     * @param uri Uri
     * @param selectionArgs 絞り込み条件の引数
     * @return _idの条件が連結されたselectionArgs
     */
    private String[] appendSelectionArgs(Uri uri, String[] selectionArgs) {
        List<String> pathSegments = uri.getPathSegments();
        if (pathSegments.size() == 1) {
            return selectionArgs;
        }
        if (selectionArgs == null || selectionArgs.length == 0) {
            return new String[] {pathSegments.get(1)};
        }
        String[] returnArgs = new String[selectionArgs.length + 1];
        returnArgs[0] = pathSegments.get(1);
        System.arraycopy(selectionArgs, 0, returnArgs, 1, selectionArgs.length);
        return returnArgs;
    }
}
