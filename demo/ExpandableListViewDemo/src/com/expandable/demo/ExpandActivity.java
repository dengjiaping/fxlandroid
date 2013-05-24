package com.expandable.demo;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

public class ExpandActivity extends Activity {
	
	private ExpandableListView expandableListView;
	private ArrayList<GroupBean> groupData;
	private ArrayList<ArrayList<ChildBean>> childData;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		initData();
		
		expandableListView = (ExpandableListView) findViewById(R.id.expandableListView1);

		final MyExpandableListAdapter adapter = new MyExpandableListAdapter(ExpandActivity.this,
				groupData, childData);
		expandableListView.setAdapter(adapter);
		
		/*expandableListView.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				Log.i("test", "Click");
				CheckBox checkBox = (CheckBox) v.findViewById(R.id.checkBox1);
				if(childPosition == 0)
				{
					checkBox.setChecked(true);
				}else{
					checkBox.toggle();
				}
				return false;
			}
		});*/
	}

	/**
	 * 初始测试数据
	 */
	private void initData() {
		
		String[] country = {"中国", "美国"};
		String[][] city = { { "北京", "上海", "广州" }, { "纽约", "华盛顿", "芝加哥" } };
		childData = new ArrayList<ArrayList<ChildBean>>();
		groupData = new ArrayList<GroupBean>();
		for (int i = 0; i < city.length; i++) {
			ArrayList<ChildBean> childBeans = new ArrayList<ChildBean>();
			childBeans.add(new ChildBean(true, "不限"));
			for (int j = 0; j < city[i].length; j++) {
				ChildBean childBean = new ChildBean(false, city[i][j]);
				childBeans.add(childBean);
			}
			childData.add(childBeans);
			GroupBean groupBean = new GroupBean();
			groupBean.setTitle(country[i]);
			groupBean.setChildBeans(childBeans);
			groupData.add(groupBean);
		}
	}

}