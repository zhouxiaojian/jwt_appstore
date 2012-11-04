package com.ascf.jwt.appstore;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ascf.jwt.appstore.dirparser.Item;

public class DirectoryAdapter extends BaseAdapter {

	private List<Item> mItems = null;
	private LayoutInflater mInflater;

	public DirectoryAdapter (Context ctx, List<Item> list){
		mInflater = LayoutInflater.from(ctx);
		mItems = list;
	}

	public void updateItems (List<Item> aList){
		if (null != aList && aList.size() > 0) {
			mItems = aList;
			notifyDataSetChanged();
		}
	}

	public int getCount() {
		if (mItems == null) {
			return 0;
		}
		return mItems.size();
	}

	public Object getItem(int position) {
		return mItems.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public boolean areAllItemsEnabled (){
	    return false;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if(position > mItems.size()) {
			return null;
		}
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_view_item, null);
		}
		TextView textV = (TextView) convertView.findViewById(R.id.title);
		textV.setEnabled(mItems.get(position).ismIsEnable());
		textV.setText(mItems.get(position).getmDisplayname());
		return convertView;
	}

}
