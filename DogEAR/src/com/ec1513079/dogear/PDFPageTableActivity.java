package com.ec1513079.dogear;

import com.ec1513079.dogear.R;

import com.artifex.mupdfdemo.MuPDFCore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class PDFPageTableActivity extends Activity {

	public static final String PDF_FILE_PATH = "pdf_file_path";
	public static final String NEED_MOVE_TO_PAGE = "need_move_to_page";
	public static final int RESULT_NEED_MOVE = 10;
	
	GridView mGridView;
	MuPDFCore mMuPDFCore;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String filePath = getIntent().getStringExtra(PDF_FILE_PATH);
		try {
			mMuPDFCore = new MuPDFCore(this, filePath);
			
			setContentView(R.layout.pagetable_layout);
			mGridView = (GridView)findViewById(R.id.page_grid_view);
			mGridView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Intent intent = new Intent();
					intent.putExtra(NEED_MOVE_TO_PAGE, position);
					setResult(RESULT_NEED_MOVE, intent);
					finish();
				}
			});
			mGridView.setAdapter(new PDFPageTableAdapter(this, mMuPDFCore));
			
			if  (getIntent() != null && getIntent().hasExtra(NEED_MOVE_TO_PAGE)) {
				int page = getIntent().getIntExtra(NEED_MOVE_TO_PAGE, 0);
				mGridView.setSelection(page);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
