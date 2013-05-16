package co.jp.softbank.tech.ap.sca;

import java.util.ArrayList;
import java.util.LinkedList;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class BookmarkAdapter extends BaseAdapter {
	
	LinkedList<String> mBookmarks;

	public BookmarkAdapter() {
		mBookmarks = new LinkedList<String>();
	}
	
	@Override
	public int getCount() {
		return mBookmarks.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
