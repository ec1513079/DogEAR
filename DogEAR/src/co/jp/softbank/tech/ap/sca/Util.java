package co.jp.softbank.tech.ap.sca;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Browser.BookmarkColumns;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.format.Time;

public class Util {
	
	/*
	 * Networkユーティリティ
	 */
	
	public static String createRequestBody(Context context) {
		
		String androidId   = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
		String bundleModel = Build.MODEL;
		String sysversion  = Build.VERSION.RELEASE;
		
		JSONObject json = new JSONObject();
		try {
			json.put("app_id", 1);
			json.put("login", Util.getUserName(context));
			json.put("appversion", getApplicationVersion(context));
			json.put("version", 1);
			json.put("debug", true);
			json.put("udid", androidId);
			json.put("hwversion", bundleModel);
			json.put("sysversion", sysversion);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return json.toString();
	}
	
	/*
	 * SQlite用ユーティリティ
	 */
	
	public static synchronized void addBookmark(Context context, File file, int page) {

		try {
			if (context == null) throw new NullPointerException();

			long time = System.currentTimeMillis();
			
			ContentValues values = new ContentValues();
			values.put(BookmarkProvider.BookmarkDatabaseColumns.DB_COLUMNS_CDATE, time);
			values.put(BookmarkProvider.BookmarkDatabaseColumns.DB_COLUMNS_MDATE, time);
			values.put(BookmarkProvider.BookmarkDatabaseColumns.DB_COLUMNS_PATH,  file.getAbsolutePath());
			values.put(BookmarkProvider.BookmarkDatabaseColumns.DB_COLUMNS_TITLE, file.getName());
			values.put(BookmarkProvider.BookmarkDatabaseColumns.DB_COLUMNS_PAGE,  page);
			
			context.getContentResolver().insert(Uri.parse(BookmarkProvider.CONTENT_URI), values);

		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
	
	public static synchronized void addBrowseHistory(Context context, File file) {
		
		try {
			if (context == null) throw new NullPointerException();

			long time = System.currentTimeMillis();
			
			ContentValues values = new ContentValues();
			values.put(BrowseHistoryProvider.BrowseHistoryDatabaseColumns.DB_COLUMNS_CDATE, time);
			values.put(BrowseHistoryProvider.BrowseHistoryDatabaseColumns.DB_COLUMNS_MDATE, time);
			values.put(BrowseHistoryProvider.BrowseHistoryDatabaseColumns.DB_COLUMNS_PATH,  file.getAbsolutePath());
			values.put(BrowseHistoryProvider.BrowseHistoryDatabaseColumns.DB_COLUMNS_TITLE, file.getName());
			
			context.getContentResolver().insert(Uri.parse(BrowseHistoryProvider.CONTENT_URI), values);

		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * 設定用ユーティリティ
	 */
	
	public static String getApplicationVersion(Context context) {
		String versionName = null;
        PackageManager packageManager = context.getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            versionName = packageInfo.versionName;
        } catch (NameNotFoundException e) {
		        e.printStackTrace();
		}
		return versionName;
	}
	
	public static String getCategoryUpdate(Context context) {
		SharedPreferences sharedPreference = PreferenceManager.getDefaultSharedPreferences(context);
		return sharedPreference.getString(context.getResources().getString(R.string.preference_key_category_update), null);
	}
	
	public static void setCategoryUpdate(Context context, String value) {
		SharedPreferences sharedPreference = PreferenceManager.getDefaultSharedPreferences(context);
		sharedPreference.edit().putString(context.getResources().getString(R.string.preference_key_category_update), value);
	}

	public static String getContentsSize(Context context) {
		return "???";
	}
	
	public static String getCacheSize(Context context) {
		return "???";
	}

	public static String getUsableSize(Context context) {
		return "???";
	}

	public static String getConnectURL(Context context) {
		SharedPreferences sharedPreference = PreferenceManager.getDefaultSharedPreferences(context);
		return sharedPreference.getString(context.getResources().getString(R.string.preference_key_connect_url), null);
	}
	
	public static String getUserName(Context context) {
		SharedPreferences sharedPreference = PreferenceManager.getDefaultSharedPreferences(context);
		return sharedPreference.getString(context.getResources().getString(R.string.preference_key_user_name), null);
	}
	
	public static String getFileserverUrl(Context context) {
		SharedPreferences sharedPreference = PreferenceManager.getDefaultSharedPreferences(context);
		return sharedPreference.getString(context.getResources().getString(R.string.preference_key_fileserver_url), null);
	}
	
	public static void setFileServerUrl(Context context, String value) {
		SharedPreferences sharedPreference = PreferenceManager.getDefaultSharedPreferences(context);
		sharedPreference.edit().putString(context.getResources().getString(R.string.preference_key_fileserver_url), value);
	}
}
