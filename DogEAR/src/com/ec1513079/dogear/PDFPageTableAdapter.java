package com.ec1513079.dogear;

import com.artifex.mupdfdemo.MuPDFCore;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class PDFPageTableAdapter extends BaseAdapter {

	Context mContext;
	
	MuPDFCore mMuPDFCore;
	int       mPageNum;

	private final SparseArray<PointF> mPageSizes = new SparseArray<PointF>();
	
	public PDFPageTableAdapter(Context context, MuPDFCore core) {
		mContext   = context;
		mMuPDFCore = core;
		mPageNum   = mMuPDFCore.countPages();
	}
	
	@Override
	public int getCount() {
		return mPageNum;
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		final PDFPageTableItemView rootView;
		if (convertView == null) {
			rootView = new PDFPageTableItemView(mContext, mMuPDFCore, position);
		} else {
			rootView = (PDFPageTableItemView)convertView;
		}
		
		PointF pageSize = mMuPDFCore.getPageSize(position);
		rootView.setDraw(position, new Point((int)pageSize.x, (int)pageSize.y));
		
		return rootView;
	}

}
