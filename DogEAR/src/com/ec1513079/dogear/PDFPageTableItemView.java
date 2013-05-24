package com.ec1513079.dogear;

import java.lang.ref.WeakReference;

import com.ec1513079.dogear.R;

import com.artifex.mupdfdemo.BitmapHolder;
import com.artifex.mupdfdemo.MuPDFCore;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class PDFPageTableItemView extends RelativeLayout {
	
	final MuPDFCore mMuPDFCore;
	int mPage;
	
	Context mContext;
	ProgressBar mProgressBar;
	ImageView mImageView;
	BitmapHolder mBitmapHolder;
	
	Point mImageViewSize;
	
	AsyncTask<Void, Void, Bitmap> mDrawTask;

	public PDFPageTableItemView(Context context, MuPDFCore core, int page) {
		super(context);
		
		mContext      = context;
		mMuPDFCore    = core;
		mPage         = page;
		mBitmapHolder = new BitmapHolder();
		
		// XML Layoutの適用
    	LayoutInflater inflater = LayoutInflater.from(mContext);
    	inflater.inflate(R.layout.pagetable_item_layout, this);
    	
		mProgressBar = (ProgressBar)this.findViewById(R.id.load_progress);
		mProgressBar.setVisibility(View.VISIBLE);
		mImageView   = (ImageView)this.findViewById(R.id.page_item_image_view);
		mImageViewSize = new Point((int) getResources().getDimension(R.dimen.paga_item_width)
				, (int) getResources().getDimension(R.dimen.paga_item_hight));
	}
    
    @Override
    protected void onDetachedFromWindow() {
		if (mDrawTask != null) {
			mDrawTask.cancel(true);
			mDrawTask = null;
		}
		mImageView.setImageBitmap(null);
		mBitmapHolder.setBm(null);
    	super.onDetachedFromWindow();
    }
	
	public void setDraw(final int page, final Point pageSize) {
		if (mDrawTask != null) {
			mDrawTask.cancel(true);
			mDrawTask = null;
		}
		
		final Point viewSize;
		float pageAspect = getAspect(pageSize);
		if (getAspect(mImageViewSize) >= pageAspect)
			viewSize = new Point((int)(mImageViewSize.y*pageAspect), mImageViewSize.y);
		else 
			viewSize = new Point(mImageViewSize.x, (int)(mImageViewSize.x/pageAspect));
		
		// Render the page in the background
		mDrawTask = new AsyncTask<Void,Void,Bitmap>() {

			protected void onPreExecute() {
				mProgressBar.setVisibility(View.VISIBLE);
			}
			
			protected Bitmap doInBackground(Void... v) {
				return mMuPDFCore.drawPage(page, 
						viewSize.x, viewSize.y, 0, 0, viewSize.x, viewSize.y);
			}

			protected void onPostExecute(Bitmap bm) {
				if (isCancelled())
					bm = null;
				if (mProgressBar != null)
					mProgressBar.setVisibility(View.GONE);
				mImageView.setImageBitmap(null);
				mBitmapHolder.setBm(null);
				mImageView.setImageBitmap(bm);
				mBitmapHolder.setBm(bm);
				invalidate();
			}
		};
		mDrawTask.execute();
	}
	
	static float getAspect(Point size) {
		return size.x / (float)size.y;
	}
}
