package co.jp.softbank.tech.ap.sca;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class BookmarkFragment extends ListFragment {
	
	private OnSelectFileListener mOnSelectFileListenr;

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
		View root = inflater.inflate(R.layout.bookmark_fragment, null);
		return root;
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
//		ShelfItem item = (ShelfItem) getListView().getItemAtPosition(position);
//		File file = new File(item.path);
//		
//		mOnSelectFileListenr.onSelectedFile(file);
	}
}
