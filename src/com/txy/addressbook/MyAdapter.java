package com.txy.addressbook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<Map<String, Object>> mData;
	public static Map<Integer, Boolean> isSelected;

 	public MyAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
		init();
	}

	// 初始化
	private void init() {
		mData = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 5; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("time", "2014-12-15 13:20:30");
			map.put("fromname", "第" + (i + 1) + "个人名");
			map.put("content", "去不去搞基？");
			mData.add(map);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("time", "2014-12-15 13:20:30");
		map.put("fromname", "李四");
		map.put("content", "去不去搞基？去不去搞基？去不去搞基？去不去搞基？去不去搞基？去不去搞基？去不去搞基？去不去搞基？去不去搞基？");
		mData.add(map);
		
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("time", "2014-12-15 13:20:30");
		m.put("fromname", "张三");
		m.put("content", "去不去搞基？");
		mData.add(m);
		// 这儿定义isSelected这个map是记录每个listitem的状态，初始状态全部为false。
		isSelected = new HashMap<Integer, Boolean>();
		for (int i = 0; i < mData.size(); i++) {
			isSelected.put(i, false);
		}
	}

	public void setShowCheckBox(boolean isshow){
		notifyDataSetChanged();
	}
	
	/**
	 * 设置Item中的checkbox全选，或全不选
	 * @param b
	 */
	public void setIsSelected(boolean b){
		for(int i = 0;i< mData.size();i++){
			isSelected.put(i, b);
		}
		notifyDataSetChanged();
	}
	public void add(Map<String, Object> m) {
		mData.add(m);
		isSelected.put(mData.size()-1, false);
		notifyDataSetChanged();
	}

	public void remove(int position) {
		mData.remove(position);

		// mData.remove(mData.get(position));
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		// convertView为null的时候初始化convertView。
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.msgitem, null);
			holder.fromname = (TextView) convertView.findViewById(R.id.name);
			holder.time = (TextView) convertView.findViewById(R.id.location);
			holder.content = (TextView) convertView.findViewById(R.id.title);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.fromname.setText(mData.get(position).get("fromname").toString());
		holder.time.setText(mData.get(position).get("time").toString());
		holder.content.setText(mData.get(position).get("content").toString());
		
		// 设置显示   View.VISIBLE
		// 设置不显示也不占用空间   View.GONE  
		// 设置不显示但占用空间  View.INVISIBLE
		
		return convertView;
	}

	public final class ViewHolder {
		public TextView fromname;
		public TextView time;
		public TextView content;
	}
}