package co.jp.softbank.tech.ap.sca;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import android.os.AsyncTask;

public class FileSystemAsyncTask extends AsyncTask<File, Void, ArrayList<File>> {
	
	@Override
	protected ArrayList<File> doInBackground(File... params) {
		
		File current_ = params[0];
		if (current_ == null || !current_.isDirectory())
			return null;
		
		// get parent directory
		final File parent = current_.getParentFile();

		// get directory
		final File[] directory;
		File[] dics_ = current_.listFiles(new FileFilter() {
			public boolean accept(File file) {
				return file.isDirectory();
			}
		});
		if (dics_ == null)
			directory = new File[0];
		else 
			directory = dics_;

		Arrays.sort(dics_, new Comparator<File>() {
			public int compare(File arg0, File arg1) {
				return arg0.getName().compareToIgnoreCase(arg1.getName());
			}
		});

		// get files
		final File[] files;
		File[] files_ = current_.listFiles(new FileFilter() {
			public boolean accept(File file) {
				if (file.isDirectory())
					return false;
				String fname = file.getName().toLowerCase();
				if (fname.endsWith(".pdf"))
					return true;
				return false;
			}
		});
		if (files_ == null)
			files = new File[0];
		else 
			files = files_;

		Arrays.sort(files_, new Comparator<File>() {
			public int compare(File arg0, File arg1) {
				return arg0.getName().compareToIgnoreCase(arg1.getName());
			}
		});
		
		// to ArrayList
		ArrayList<File> result = new ArrayList<File>() {{
			add(parent); addAll(Arrays.asList(directory)); addAll(Arrays.asList(files)); }};
			
		return result;
	}
}
