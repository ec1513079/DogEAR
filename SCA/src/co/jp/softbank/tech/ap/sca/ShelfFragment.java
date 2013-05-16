package co.jp.softbank.tech.ap.sca;

import java.io.File;

import com.tombarrasso.android.app.GridFragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class ShelfFragment extends GridFragment {

	private OnSelectFileListener mOnSelectFileListenr;

	public ShelfFragment() {
	}

	@Override  
	public void onAttach(Activity activity) {  
		super.onAttach(activity);  
		try {  
			mOnSelectFileListenr = (OnSelectFileListener) activity;  
		} catch (ClassCastException e) {  
			throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");  
		}  
	} 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	} 

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.shelf_layout, null);
		return root;
	}

	@Override
	public void onGridItemClick(GridView g, View v, int position, long id) {
		super.onGridItemClick(g, v, position, id);
		
		ShelfItem item = (ShelfItem) getGridView().getItemAtPosition(position);
		File file = new File(item.path);
		
		mOnSelectFileListenr.onSelectedFile(file);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.shelf_layout, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_download_add_in_category:
			return false;
		case R.id.action_download_delete_in_category:
			return false;
		}
		return super.onOptionsItemSelected(item);
	}
}
