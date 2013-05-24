package com.ec1513079.dogear;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;

import com.ec1513079.dogear.R;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ShelfAdapter extends BaseAdapter {
	
	private final LinkedList<ShelfItem> mItems;
	private final LayoutInflater mInflater;

	public ShelfAdapter(LayoutInflater inflater) {
		mInflater = inflater;
		mItems = new LinkedList<ShelfItem>();
	}

    @Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public Object getItem(int i) {
		return mItems.get(i);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View v = null;
		if (convertView != null) {
			v = convertView;
		} else if (parent.getClass() == ShelfGridView.class) {
			v = mInflater.inflate(R.layout.shelf_grid_item_layout, null);
		} else {
			v = mInflater.inflate(R.layout.shelf_list_item_layout, null);
		}
		
		ShelfItem item = mItems.get(position);
		((TextView)v.findViewById(R.id.shelf_item_name)).setText(item.name);
		((ImageView)v.findViewById(R.id.shelf_item_icon)).setImageResource(iconForType(item.type));
		
		return v;
	}

	private int iconForType(ShelfItem.Type type) {
		switch (type) {
		case PARENT: return R.drawable.bookshelf_folder_link;
		case DIR: return R.drawable.bookshelf_folder;
		case DOC: return R.drawable.icon_file_not_exist_pdf;
		default: return 0;
		}
	}

	public void clear() {
		mItems.clear();
	}

	public void add(ShelfItem item) {
		mItems.add(item);
		notifyDataSetChanged();
	}
	
	public void addParent(File file) {
		if (file == null) return;
		add(new ShelfItem(ShelfItem.Type.PARENT, "../", file.getAbsolutePath()));
	}
	
	@SuppressLint("DefaultLocale")
	public void addFile(File file) {
		if (file == null) return;
		if (file.isDirectory())
			add(new ShelfItem(ShelfItem.Type.DIR, file.getName(), file.getAbsolutePath()));
		else if (file.getName().toLowerCase().endsWith(".pdf"))
			add(new ShelfItem(ShelfItem.Type.DOC, file.getName(), file.getAbsolutePath()));
	}
	
	public void addCategory() {
		
	}
}
