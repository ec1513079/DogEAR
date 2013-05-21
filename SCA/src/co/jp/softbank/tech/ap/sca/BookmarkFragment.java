package co.jp.softbank.tech.ap.sca;

import java.io.File;

import co.jp.softbank.tech.ap.sca.BookmarkAdapter.BookmarkDatabaseColumns;

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

public class BookmarkFragment extends ListFragment implements LoaderCallbacks<Cursor> {
	
	private OnSelectFileListener mOnSelectFileListenr;

	private ListView mListView;
	private SimpleCursorAdapter mListAdapter;
	
	public BookmarkFragment() {
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

		String from[] = {BookmarkDatabaseColumns.DB_BOOKMARK_COLUMNS_TITLE};
		int to[]   = {R.id.bookmark_item_name};
		
		mListAdapter = new SimpleCursorAdapter(
				getActivity(),
				R.layout.bookmark_list_item_layout,
				null,
				from, to,
				SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		
		View root = inflater.inflate(R.layout.bookmark_fragment, null);
		mListView = (ListView)root.findViewById(android.R.id.list);
		mListView.setAdapter(mListAdapter);
		
		return root;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
//		ShelfItem item = (ShelfItem) getListView().getItemAtPosition(position);
//		File file = new File(item.path);
//		
//		mOnSelectFileListenr.onSelectedFile(file);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		return new CursorLoader(getActivity()) {
			@Override
			public Cursor loadInBackground() {
				BookmarkAdapter adapter = new BookmarkAdapter(getContext(), true);
				return adapter.getAllRows();
			}
		};
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
