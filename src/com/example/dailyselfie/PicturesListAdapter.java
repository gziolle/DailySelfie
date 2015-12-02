package com.example.dailyselfie;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PicturesListAdapter extends BaseAdapter {
	
	private ArrayList<Picture> list = new ArrayList<Picture>();
	private static LayoutInflater inflater = null;
	private Context mContext;
	
	public PicturesListAdapter(Context context) {
		mContext = context;
		inflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Picture getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View newView = convertView;
		ViewHolder holder;

		Picture curr = list.get(position);

		if (null == convertView) {
			holder = new ViewHolder();
			newView = inflater
					.inflate(R.layout.picture_layout, parent, false);
			holder.thumbnail = (ImageView) newView.findViewById(R.id.thumbnail);
			holder.fileName = (TextView) newView.findViewById(R.id.file_name);
			newView.setTag(holder);

		} else {
			holder = (ViewHolder) newView.getTag();
		}

		holder.thumbnail.setImageBitmap(curr.getmBitmap());
		holder.fileName.setText(curr.getFileName());
		return newView;
	}
	
	static class ViewHolder {
		ImageView thumbnail;
		TextView fileName;
	}
	
	public void add(Picture listItem) {
		list.add(listItem);
		notifyDataSetChanged();
	}

	public ArrayList<Picture> getList() {
		return list;
	}

	public void removeAllViews() {
		list.clear();
		this.notifyDataSetChanged();
	}
	
}
