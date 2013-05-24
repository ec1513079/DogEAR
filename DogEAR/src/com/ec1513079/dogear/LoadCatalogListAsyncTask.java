package com.ec1513079.dogear;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.ec1513079.dogear.R;

import android.content.Context;
import android.content.res.Resources;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.DropBoxManager.Entry;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class LoadCatalogListAsyncTask extends AsyncTask<Void, Void, String> {
	
	private final String mUrl;
	private final String mPostValue;
	private final Context mContext;
	
	public LoadCatalogListAsyncTask(Context context) {
		
		mUrl = PreferenceManager.getDefaultSharedPreferences(context).getString(
				context.getResources().getString(R.string.preference_key_connect_url), null);
		mPostValue = Util.createRequestBody(context);
		mContext = context;
	}

	@Override
	protected String doInBackground(Void... arg0) {
		
		String result = null;
		try {
			HttpPost post = new HttpPost(mUrl);
			post.setEntity(new StringEntity(mPostValue, "UTF-8"));
		
			AndroidHttpClient client = AndroidHttpClient.newInstance("Android UserAgent");
			HttpResponse response;
			response = client.execute(post);
	
			result = EntityUtils.toString(response.getEntity(), "UTF-8");
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
				
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		if (isCancelled()) result = null;
	}
}
