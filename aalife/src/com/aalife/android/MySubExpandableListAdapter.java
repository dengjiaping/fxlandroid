package com.aalife.android;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class MySubExpandableListAdapter extends BaseExpandableListAdapter {
	private MyExpandableListAdapter myadapter;
	private Context context;
	private LayoutInflater layout;
	private List<Map<String, String>> groups;
	private List<List<Map<String, String>>> children;
	private int groupId;
	private List<List<List<Map<String, String>>>> checked;
	
	public MySubExpandableListAdapter(BaseExpandableListAdapter myadapter, Context context, List<Map<String, String>> groups, List<List<Map<String, String>>> children, int groupId, List<List<List<Map<String, String>>>> checked) {
		this.myadapter = (MyExpandableListAdapter) myadapter;
		this.context = context;
		this.layout = LayoutInflater.from(this.context);
		this.groups = groups;
		this.children = children;
		this.groupId = groupId;
		this.checked = checked;
	}

	@Override
	public Map<String, String> getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return this.children.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPostion, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}
	
	@Override
	public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		MyItemView myitemView = null;
		if(convertView == null) {
			myitemView = new MyItemView();
			convertView = layout.inflate(R.layout.monthitemlist, null);
			myitemView.itemidText = (TextView) convertView.findViewById(R.id.itemidtext);
			myitemView.itemnameText = (TextView) convertView.findViewById(R.id.itemnametext);
			myitemView.itempriceText = (TextView) convertView.findViewById(R.id.itempricetext);
			myitemView.checkboxView = (CheckBox) convertView.findViewById(R.id.checkboxview);
			convertView.setTag(myitemView);
		} else {
			myitemView = (MyItemView) convertView.getTag();
		}
		
		myitemView.itemidText.setText(getChild(groupPosition, childPosition).get("itemid"));
		myitemView.itemnameText.setText(getChild(groupPosition, childPosition).get("itemname"));
		myitemView.itempriceText.setText("￥ " + UtilityHelper.formatDouble(Double.parseDouble(getChild(groupPosition, childPosition).get("itemprice")), "0.0##"));
		
		myitemView.checkboxView.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {				
				if(isChecked) {
					checked.get(groupId).get(groupPosition).get(childPosition).put("checked", "true");
				} else {
					checked.get(groupId).get(groupPosition).get(childPosition).put("checked", "false");
				}
				
				double monthpricetext = 0;
				for(int i=0; i<getChildrenCount(groupPosition); i++) {
					Map<String, String> map = getChild(groupPosition, i);
					if(checked.get(groupId).get(groupPosition).get(i).get("checked").equals("true")) {
						monthpricetext += Double.parseDouble(map.get("itemprice"));
					}
				}
				getGroup(groupPosition).put("monthpricetext", String.valueOf(monthpricetext));

				myadapter.notifyDataSetChanged();
				notifyDataSetChanged();
			}
		});
		
		myitemView.checkboxView.setChecked(checked.get(groupId).get(groupPosition).get(childPosition).get("checked").equals("true") ? true : false);
				
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return this.children.get(groupPosition).size();
	}

	@Override
	public Map<String, String> getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return this.groups.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return this.groups.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		MyListView mylistView = null;
		if(convertView == null) {
			mylistView = new MyListView();
			convertView = layout.inflate(R.layout.monthlist, null);
			mylistView.idText = (TextView) convertView.findViewById(R.id.idtext);
			mylistView.monthText = (TextView) convertView.findViewById(R.id.monthtext);
			mylistView.monthpriceText = (TextView) convertView.findViewById(R.id.monthpricetext);
			convertView.setTag(mylistView);
		} else {
			mylistView = (MyListView) convertView.getTag();
		}
		
		String monthtext = getGroup(groupPosition).get("monthtext");
		double monthpricetext = Double.parseDouble(getGroup(groupPosition).get("monthpricetext"));
		
		mylistView.monthText.setText(UtilityHelper.formatDate(monthtext, "MM-dd") + "（" + UtilityHelper.formatDate(monthtext, "ww") + "）");
		mylistView.monthpriceText.setText("￥ " + UtilityHelper.formatDouble(monthpricetext, "0.0##")); 
				
		String date = UtilityHelper.getDateNow("date");
		int warnNumber = UtilityHelper.getWarnNumber(this.context);
		if(monthpricetext > warnNumber) {
			mylistView.idText.setTextColor(this.context.getResources().getColor(R.color.daytitlewarn));
			mylistView.monthText.setTextColor(this.context.getResources().getColor(R.color.daytitlewarn));
			mylistView.monthpriceText.setTextColor(this.context.getResources().getColor(R.color.daytitlewarn));
		} else if(monthtext.equals(date)) {
			mylistView.idText.setTextColor(this.context.getResources().getColor(R.color.daytitlecur));
			mylistView.monthText.setTextColor(this.context.getResources().getColor(R.color.daytitlecur));
			mylistView.monthpriceText.setTextColor(this.context.getResources().getColor(R.color.daytitlecur));
		} else {
			mylistView.idText.setTextColor(this.context.getResources().getColor(R.color.daytitle));
			mylistView.monthText.setTextColor(this.context.getResources().getColor(R.color.daytitle));
			mylistView.monthpriceText.setTextColor(this.context.getResources().getColor(R.color.daytitle));
		}
		
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}
	
	private class MyListView {
		private TextView idText;
		private TextView monthText;
		private TextView monthpriceText;
	}
	
	private class MyItemView {
		private TextView itemidText;
		private TextView itemnameText;
		private TextView itempriceText;
		private CheckBox checkboxView;
	}

}
