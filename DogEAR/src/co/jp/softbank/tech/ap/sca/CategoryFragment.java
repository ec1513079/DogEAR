package co.jp.softbank.tech.ap.sca;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

public class CategoryFragment extends ListFragment {

	private OnSelectFileListener mOnSelectFileListenr;

	public CategoryFragment() {
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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.category_fragment, null);
		return root;
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		ShelfItem item = (ShelfItem) getListView().getItemAtPosition(position);
		File file = new File(item.inner_path);
		
		mOnSelectFileListenr.onSelectedFile(file, 0);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
