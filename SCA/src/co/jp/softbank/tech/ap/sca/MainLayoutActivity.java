package co.jp.softbank.tech.ap.sca;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import com.artifex.mupdfdemo.MuPDFActivity;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.preference.PreferenceManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

public class MainLayoutActivity extends FragmentActivity implements OnSelectFileListener, OnTabChangeListener {

	/* FileSystem */
	static private File mDirectory;
	static private ArrayList<String> mBackHistory;
	private FileSystemAsyncTask mFileSystemAsyncTask;
	/* Adapter */
	static private ShelfAdapter mShelfAdapter;
	
	/* Category */
	private LoadCatalogListAsyncTask mLoadCatalogListAsyncTask;

	/* PDF Viewer */
	boolean isPDFViewing;
	File currentFile;
	
	/* Tab */
	TabHost mTabHost;
	public static final String TAB_ID_CATEGORY = "tab_id_category";
	public static final String TAB_ID_BOOKMARK = "tab_id_bookmark";
	public static final String TAB_ID_BROWSE_HISTORY = "tab_id_browse_history";
	public static final String TAB_ID_DOWNLOAD = "tab_id_download"; 
	
	/* Fragment */
	private Fragment mContent;
	private Fragment mSideContent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Set Preference by Default
		PreferenceManager.setDefaultValues(this, R.xml.preference, true);

		// check storage
		if (!checkExternalStorageState())
			return;

		// initial directory
		if (mDirectory == null)
			mDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
		setTitle(mDirectory.getAbsolutePath());
		
		// initial directory's history
		if (mBackHistory == null)
			mBackHistory = new ArrayList<String>();

		// Activity layout
		setContentView(R.layout.main_layout);
		
		// Tab setup
		mTabHost = (TabHost)findViewById(android.R.id.tabhost);
		mTabHost.setup();
		{
			TabSpec spec = mTabHost.newTabSpec(TAB_ID_CATEGORY)
					.setIndicator(new TabItemView(this, "カテゴリー", 
							getResources().getDrawable(R.drawable.tab_icon_cabinet)))
					.setContent(new DummyTabFactory(this));
			mTabHost.addTab(spec);
		} {
			TabSpec spec = mTabHost.newTabSpec(TAB_ID_BOOKMARK)
					.setIndicator(new TabItemView(this, "ブックマーク", 
							getResources().getDrawable(R.drawable.tab_icon_book)))
					.setContent(new DummyTabFactory(this));
			mTabHost.addTab(spec);
		} {
			TabSpec spec = mTabHost.newTabSpec(TAB_ID_BROWSE_HISTORY)
					.setIndicator(new TabItemView(this, "履歴", 
							getResources().getDrawable(R.drawable.tab_icon_clock)))
					.setContent(new DummyTabFactory(this));
			mTabHost.addTab(spec);
		} {
			TabSpec spec = mTabHost.newTabSpec(TAB_ID_DOWNLOAD)
					.setIndicator(new TabItemView(this, "ダウンロード", 
							getResources().getDrawable(R.drawable.tab_icon_download)))
					.setContent(new DummyTabFactory(this));
			mTabHost.addTab(spec);
		}
		mTabHost.setOnTabChangedListener(this);
		if (savedInstanceState != null && savedInstanceState.containsKey("currentTabID")) {
			mTabHost.setCurrentTabByTag(savedInstanceState.getString("currentTabID"));
			onTabChanged(savedInstanceState.getString("currentTabID"));
			
		} else { 
			mTabHost.setCurrentTabByTag(TAB_ID_CATEGORY);
			onTabChanged(TAB_ID_CATEGORY);
		}
		
		// set the Above View
		if (savedInstanceState != null 
				&& savedInstanceState.getBoolean("isPDFViewing") 
				&& savedInstanceState.containsKey("currentFilePath")) {
			isPDFViewing = true;
			getActionBar().setDisplayHomeAsUpEnabled(isPDFViewing);
			currentFile = new File(savedInstanceState.getString("currentFilePath"));
		} else {
			isPDFViewing = false;
			showShelfFragment();
		}
		
		// Initial FileSystem
		createFileSystemAsyncTask().execute(mDirectory);
		// ...and observe the directory and scan files upon changes.
		FileObserver observer = new FileObserver(mDirectory.getPath(), FileObserver.CREATE | FileObserver.DELETE) {
			public void onEvent(int event, String path) {
				createFileSystemAsyncTask().execute(mDirectory);
			}
		};
		observer.startWatching();
		
		// Initial Category
		createLoadCatalogListAsyncTask().execute();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("mDirectoryPath", mDirectory.getAbsolutePath());
		outState.putStringArray("mHistory", mBackHistory.toArray(new String[mBackHistory.size()]));
		if (isPDFViewing && currentFile != null) {
			outState.putBoolean("isPDFViewing", isPDFViewing);
			outState.putString("currentFilePath", currentFile.getAbsolutePath());
		}
		if (mTabHost != null)
			outState.putString("currentTabID", mTabHost.getCurrentTabTag());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_layout, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			showPreferenceActivity();
			return false;
		case android.R.id.home:
			showShelfFragment();
			return false;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// Tap Back Button
		if(keyCode==KeyEvent.KEYCODE_BACK){
			if (isPDFViewing) {
				showShelfFragment();
				return false;
			} else if (mBackHistory.size() > 0) {
				String path_ = mBackHistory.remove(mBackHistory.size()-1);
				File file_ = new File(path_); 
				changeDirectory(file_);
				return false;
			}
	    }
		
		return super.onKeyDown(keyCode, event);
	}

	@SuppressLint("DefaultLocale")
	@Override
	public void onSelectedFile (File file, int page) {
		if (file.isDirectory()) {
			mBackHistory.add(mDirectory.getAbsolutePath());
			changeDirectory(file);
		} else if (file.getName().toLowerCase().endsWith(".pdf")) {
			showPDFPagerFragment(file, page);
		}
	}
	
	boolean checkExternalStorageState() {
		String storageState = Environment.getExternalStorageState();
		
		if (!Environment.MEDIA_MOUNTED.equals(storageState)
				&& !Environment.MEDIA_MOUNTED_READ_ONLY.equals(storageState))
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.no_media_warning);
			builder.setMessage(R.string.no_media_hint);
			AlertDialog alert = builder.create();
			alert.setButton(AlertDialog.BUTTON_POSITIVE,getString(R.string.dismiss),
					new OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}
					});
			alert.show();
			return false;
		}
		return true;
	}

	FileSystemAsyncTask createFileSystemAsyncTask() {
		if (mFileSystemAsyncTask != null) {
			mFileSystemAsyncTask.cancel(true);
			mFileSystemAsyncTask = null;
		}

		mFileSystemAsyncTask =  new FileSystemAsyncTask() {
			@Override
			protected void onPostExecute(ArrayList<File> result) {
				if (isCancelled()) result = null;
				if (result == null) return;
				if (mShelfAdapter == null) return;
				// shelf adapter refresh
				mShelfAdapter.clear();
				for (int i = 0; i < result.size(); i++) {
					if (i == 0)
						mShelfAdapter.addParent(result.get(i));
					else 
						mShelfAdapter.addFile(result.get(i));
				}
			}
		};
		return mFileSystemAsyncTask;
	}
	
	LoadCatalogListAsyncTask createLoadCatalogListAsyncTask() {
		if (mLoadCatalogListAsyncTask != null) {
			mLoadCatalogListAsyncTask.cancel(true);
			mLoadCatalogListAsyncTask = null;
		}

		mLoadCatalogListAsyncTask =  new LoadCatalogListAsyncTask(this) {
			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				
			}
		};
		return mLoadCatalogListAsyncTask;
	}
	
	void changeDirectory(File file) {
		if (!file.isDirectory()) {
			return ;
		}

		mDirectory = null;
		mDirectory = file;
		setTitle(mDirectory.getAbsolutePath());

		createFileSystemAsyncTask().execute(mDirectory);
	}
	
	void replaceMainContent(Fragment fragment) {
		
		mContent = null;
		mContent = fragment;
		
		getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, mContent).commit();
	}
	
	void replaceSideContent(Fragment fragment) {
		
		mSideContent = null;
		mSideContent = fragment;
		
		getSupportFragmentManager().beginTransaction().replace(R.id.realtabcontent, mSideContent).commit();
	}
	
	void showShelfFragment() {
		
		if (mShelfAdapter == null)
			mShelfAdapter = new ShelfAdapter(getLayoutInflater()); 

		isPDFViewing = false;
		getActionBar().setDisplayHomeAsUpEnabled(isPDFViewing);

		ShelfFragment fragment = new ShelfFragment();
		fragment.setGridAdapter(mShelfAdapter);
		replaceMainContent(fragment);
	}
	
	void showCategoryFragment()  {
		
		if (mShelfAdapter == null) 
			mShelfAdapter = new ShelfAdapter(getLayoutInflater());
		
		CategoryFragment sideFragment = new CategoryFragment();
		((CategoryFragment)sideFragment).setListAdapter(mShelfAdapter);
		replaceSideContent(sideFragment);
	}
	
	void showBookmarkFragment() {
		
		BookmarkFragment sideFragment = new BookmarkFragment();
		replaceSideContent(sideFragment);
	}
	
	void showBrowseHistoryFragment() {
		
		BrowseHistoryFragment sideFragment = new BrowseHistoryFragment();
		replaceSideContent(sideFragment);
	}
	
	@SuppressLint("DefaultLocale")
	void showPDFPagerFragment(File file, int page) {
		
		if (!file.getName().toLowerCase().endsWith(".pdf"))
			return;
		
		currentFile = null;
		currentFile = file;

		isPDFViewing = true;
		getActionBar().setDisplayHomeAsUpEnabled(isPDFViewing);

		PDFReaderFragment fragment = new PDFReaderFragment();
		Bundle bundle = new Bundle();
		bundle.putString(PDFReaderFragment.PDF_FILE_PATH, file.getAbsolutePath());
		bundle.putInt(PDFReaderFragment.PDF_DISPLAYED_PAGE, page);
		fragment.setArguments(bundle);
		replaceMainContent(fragment);
				
		// Register history
		Util.addBrowseHistory(this, currentFile);
	}
	
	void showPreferenceActivity() {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setClass(this, SCAPreferenceActivity.class);
		startActivity(intent);
	}

	@Override
	public void onTabChanged(String tabId) {
		
		if (tabId == TAB_ID_CATEGORY) {
			showCategoryFragment();
		} else if (tabId == TAB_ID_BOOKMARK) {
			showBookmarkFragment();
		} else if (tabId == TAB_ID_BROWSE_HISTORY) {
			showBrowseHistoryFragment();
		} else if (tabId == TAB_ID_DOWNLOAD) {
			
		}
	}
}
