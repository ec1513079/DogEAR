package co.jp.softbank.tech.ap.sca;

import java.io.File;

import co.jp.softbank.tech.ap.sca.BookmarkAdapter.BookmarkDatabaseColumns;

import android.content.ContentValues;
import android.content.Context;
import android.provider.Browser.BookmarkColumns;
import android.telephony.TelephonyManager;
import android.text.format.Time;

public class Util {
	
	public static synchronized void addBookmark(Context context, File file, int page) {

		try {
			if (context == null) throw new NullPointerException();

			long time = System.currentTimeMillis();
			
			ContentValues values = new ContentValues();
			values.put(BookmarkDatabaseColumns.DB_BOOKMARK_COLUMNS_CDATE, time);
			values.put(BookmarkDatabaseColumns.DB_BOOKMARK_COLUMNS_MDATE, time);
			values.put(BookmarkDatabaseColumns.DB_BOOKMARK_COLUMNS_PATH, file.getAbsolutePath());
			values.put(BookmarkDatabaseColumns.DB_BOOKMARK_COLUMNS_TITLE, file.getName());
			values.put(BookmarkDatabaseColumns.DB_BOOKMARK_COLUMNS_PAGE, page);

			BookmarkAdapter dbAdapter = null;
			try {
				dbAdapter = new BookmarkAdapter(context);
				dbAdapter.insert(values);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (dbAdapter != null) dbAdapter.close(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
