package com.expandable.demo;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class MyExpandableListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private ArrayList<GroupBean> groupData;
	private ArrayList<ArrayList<ChildBean>> childData;

	public MyExpandableListAdapter(Context context, ArrayList<GroupBean> groupData,
			ArrayList<ArrayList<ChildBean>> childData) {
		this.context = context;
		this.groupData = groupData;
		this.childData = childData;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return childData.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childData.get(groupPosition).get(childPosition).hashCode();
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(R.layout.screen_child, null);
		TextView name = (TextView) convertView.findViewById(R.id.name);
		final CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkBox1);
		name.setText(childData.get(groupPosition).get(childPosition)
				.name);
		checkBox.setChecked(childData.get(groupPosition).get(childPosition)
				.checked);
		
		/**
		 * 点击子Item事件，OnChildClickListener无效，未找到原因
		 */
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(childPosition == 0)
				{
					checkBox.setChecked(true);
				}else{
					checkBox.toggle();
				}
			}
		});
		
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			GroupBean groupBean = groupData.get(groupPosition);
			ArrayList<ChildBean> childBeans = childData.get(groupPosition);

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if(isChecked)
				{
					groupBean.checkedCount ++;
					Log.i("test", "count+++++ = " + groupBean.checkedCount);
					/**
					 * 第一个子选项为“不限”，当选择时，其他子选项都设置为不选
					 * 当其他选项有选择时，“不限”选项自动为不选择
					 * 当其他选项都没有选中时，“不限”选项自动选中
					 */
					if(childPosition == 0)
					{
						for(int i = 0; i < childData.get(groupPosition).size(); i++)
						{
							childBeans.get(i).checked = false;
						}
						groupBean.checkedCount = 1;
					}else if(childPosition != 0 && childBeans.get(0).checked == true)
					{
						childBeans.get(0).checked = false;
						groupBean.checkedCount --;
					}
				}else
				{
					groupBean.checkedCount --;
				}
				
				if(groupBean.checkedCount == 0)
				{
					childBeans.get(0).checked = true;
					groupBean.checkedCount ++;
				}
				
				Log.i("test", "count = " + groupBean.checkedCount);
				childBeans.get(childPosition).checked = isChecked;
				groupBean.setChildBeans(childBeans);
				MyExpandableListAdapter.this.notifyDataSetChanged();
			}
		});
		return convertView;

	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return childData.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groupData.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return groupData.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupData.get(groupPosition).hashCode();
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		View v = convertView;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = inflater.inflate(R.layout.screen_group, null);
		TextView title = (TextView) v.findViewById(R.id.title);
		title.setText(groupData.get(groupPosition).getTitle());
		TextView checkedInfo = (TextView) v.findViewById(R.id.checkedInfo);
		checkedInfo.setText(groupData.get(groupPosition).getCheckedInfo());
		return v;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
