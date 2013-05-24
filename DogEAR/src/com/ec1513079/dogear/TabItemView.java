package com.ec1513079.dogear;

import com.ec1513079.dogear.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TabItemView extends LinearLayout {

	public TabItemView(Context context, String title, Drawable icon) {
		super(context);
		View layout = LayoutInflater.from(context).inflate(R.layout.tab_item_layout, this);
		((ImageView)layout.findViewById(R.id.tab_icon)).setImageDrawable(icon);
		((TextView)layout.findViewById(R.id.tab_title)).setText(title);
	}
}
