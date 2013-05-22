package co.jp.softbank.tech.ap.sca;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class BrowseHistoryFragment extends ListFragment implements LoaderCallbacks<Cursor> {
	
	private OnSelectFileListener mOnSelectFileListenr;

	private ListView mListView;
	private SimpleCursorAdapter mListAdapter;
	
	public BrowseHistoryFragment() {
	}
	
	@Override  
	public void onAttach(Activity activity) {  
		super.onAttach(activity);  
		try {  
			mOnSelectFileListenr = (OnSelectFileListener) activity;  
		} catch (ClassCastException e) {  
			throw new ClassCastException(activity.toString() + " must implement OnSelectFileListener");  
		}  
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		getLoaderManager().initLoader(0, null, this);

		mListAdapter = new SimpleCursorAdapter(
				getActivity(),
				R.layout.histroy_list_item_layout,
				null,
				new String[] { 
					BrowseHistoryProvider.BrowseHistoryDatabaseColumns.DB_COLUMNS_TITLE,
					BrowseHistoryProvider.BrowseHistoryDatabaseColumns.DB_COLUMNS_PATH,
					BrowseHistoryProvider.BrowseHistoryDatabaseColumns.DB_COLUMNS_CDATE }, 
				new int[] { R.id.history_item_name, R.id.history_item_path, R.id.history_item_date },
				SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		mListAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
			@Override
			public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
				if (view.getId() == R.id.history_item_date) {
					long cdate = cursor.getLong(columnIndex);
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
					((TextView)view).setText(simpleDateFormat.format(new Date(cdate)));
					return true;
				}
				return false;
			}
		});
		
		View root = inflater.inflate(R.layout.history_fragment, null);
		mListView = (ListView)root.findViewById(android.R.id.list);
		mListView.setAdapter(mListAdapter);
		
		return root;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		getLoaderManager().destroyLoader(0);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		TextView pathView = (TextView)v.findViewById(R.id.history_item_path);
		String   path     = pathView.getText().toString(); 
		File     file     = new File(path);
		
		mOnSelectFileListenr.onSelectedFile(file, 0);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		return new CursorLoader(
				this.getActivity(),
				Uri.parse(BrowseHistoryProvider.CONTENT_URI),
				null,
				null,
				null,
				BrowseHistoryProvider.BrowseHistoryDatabaseColumns.DB_COLUMNS_CDATE + " DESC");
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		mListAdapter.swapCursor(arg1);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		mListAdapter.swapCursor(null);
	}
}
