package com.ec1513079.dogear;

import android.content.Context;
import android.view.View;
import android.widget.TabHost.TabContentFactory;

public class DummyTabFactory implements TabContentFactory {

	Context mContext;
	
	public DummyTabFactory(Context context) {
		mContext = context;
	}
	
	@Override
	public View createTabContent(String arg0) {
		View view = new View(mContext);
		return view;
	}

}
