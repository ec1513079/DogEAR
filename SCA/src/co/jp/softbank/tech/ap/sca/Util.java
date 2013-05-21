package co.jp.softbank.tech.ap.sca;

import java.io.File;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.Browser.BookmarkColumns;
import android.telephony.TelephonyManager;
import android.text.format.Time;

public class Util {
	
	public static synchronized void addBookmark(Context context, File file, int page) {

		try {
			if (context == null) throw new NullPointerException();

			long time = System.currentTimeMillis();
			
			ContentValues values = new ContentValues();
			values.put(BookmarkProvider.BookmarkDatabaseColumns.DB_BOOKMARK_COLUMNS_CDATE, time);
			values.put(BookmarkProvider.BookmarkDatabaseColumns.DB_BOOKMARK_COLUMNS_MDATE, time);
			values.put(BookmarkProvider.BookmarkDatabaseColumns.DB_BOOKMARK_COLUMNS_PATH,  file.getAbsolutePath());
			values.put(BookmarkProvider.BookmarkDatabaseColumns.DB_BOOKMARK_COLUMNS_TITLE, file.getName());
			values.put(BookmarkProvider.BookmarkDatabaseColumns.DB_BOOKMARK_COLUMNS_PAGE,  page);
			
			context.getContentResolver().insert(Uri.parse(BookmarkProvider.CONTENT_URI), values);

		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
}
