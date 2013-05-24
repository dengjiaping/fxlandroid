package com.aalife.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.TextView;
import android.widget.Toast;

public class MyExpandableListAdapter extends BaseExpandableListAdapter {
	private Context context;
	private LayoutInflater layout;
	private List<Map<String, String>> groups;
	private List<List<Map<String, String>>> children = new ArrayList<List<Map<String, String>>>();
	private int groupId;
	private List<List<List<Map<String, String>>>> checked = new ArrayList<List<List<Map<String, String>>>>();
	private List<List<Integer>> expanded = new ArrayList<List<Integer>>();
	
	private ItemTableAccess itemtableAccess;
	private SQLiteOpenHelper helper;

	public MyExpandableListAdapter(Context context, List<Map<String, String>> groups) {
		this.context = context;
		this.layout = LayoutInflater.from(this.context);
		this.groups = groups;
		
		helper = new MyDatabaseHelper(this.context);
		
		for(int i=0; i<this.groups.size(); i++) {
			Map<String, String> map = this.groups.get(i);
			itemtableAccess = new ItemTableAccess(helper.getReadableDatabase());
			List<Map<String, String>> list = itemtableAccess.findAllByMonth(map.get("monthtext"));
			this.children.add(list);
			
			List<Integer> explist = new ArrayList<Integer>();
			this.expanded.add(explist);
		}
		
		for(int i=0; i<this.children.size(); i++) {
			List<Map<String, String>> list = this.children.get(i);
			List<List<Map<String, String>>> checklist = new ArrayList<List<Map<String, String>>>();
			
			for(int j=0; j<list.size(); j++) {				
				Map<String, String> map = list.get(j);
				List<Map<String, String>> checkitem = new ArrayList<Map<String, String>>();
				itemtableAccess = new ItemTableAccess(helper.getReadableDatabase());
				List<Map<String, String>> itemlist = itemtableAccess.findAllWithDayByDate(map.get("monthtext"));

				for(int k=0; k<itemlist.size(); k++) {					
					Map<String, String> itemmap = new HashMap<String, String>();
					itemmap.put("checked", "true");
					checkitem.add(itemmap);
				}
				checklist.add(checkitem);
			}
			this.checked.add(checklist);			
		}
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
	public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		int subHeight = UtilityHelper.getSubHeight(this.context);
		int expHeight = subHeight;
		this.groupId = groupPosition;
		 
		List<List<Map<String, String>>> subchildren = new ArrayList<List<Map<String, String>>>();
		for (int i = 0; i < this.children.get(groupPosition).size(); i++) {
			Map<String, String> map = this.children.get(groupPosition).get(i);
			itemtableAccess = new ItemTableAccess(helper.getReadableDatabase());
			List<Map<String, String>> list = itemtableAccess.findAllWithDayByDate(map.get("monthtext"));
			subchildren.add(list);
		}

		for(int i=0; i<subchildren.size(); i++) {
			expHeight += subHeight;
			
			int subCount = subchildren.get(i).size();
			expHeight += (subHeight * subCount);
		}
		
		MySubList mysubList = new MySubList(this.context, expHeight);
		mysubList.setAdapter(new MySubExpandableListAdapter(this, this.context, this.children.get(groupPosition), subchildren, this.groupId, this.checked));
		mysubList.setGroupIndicator(this.context.getResources().getDrawable(android.R.color.transparent));
		mysubList.setDividerHeight(0);
		mysubList.setOnChildClickListener(new OnChildClickImpl());
		mysubList.setOnGroupExpandListener(new OnGroupExpandListener(){
			@Override
			public void onGroupExpand(int groupPosition) {
				if(expanded.get(groupId).size() >= 0) {
					for(int i=0; i<expanded.get(groupId).size(); i++) {
						if(expanded.get(groupId).get(i) == groupPosition) {
							return;
						}
					}
					expanded.get(groupId).add(groupPosition);
				}
			}
		});
		mysubList.setOnGroupCollapseListener(new OnGroupCollapseListener(){
			@Override
			public void onGroupCollapse(int groupPosition) {
				for(int i=0; i<expanded.get(groupId).size(); i++) {
					if(expanded.get(groupId).get(i) == groupPosition) {
						expanded.get(groupId).remove(i);
					}
				}
			}
		});
		
		List<Integer> subGroupId = this.expanded.get(groupId);
		for(int i=0; i<subGroupId.size(); i++) {
			mysubList.expandGroup(subGroupId.get(i));
		}
		
		return mysubList;
	}

	private class OnChildClickImpl implements OnChildClickListener {
		private Activity act = (Activity) context;
		private TextView itemidView;
		private TextView idView;
		private TextView itemnameView;
		private TextView itempriceView;
		private CheckBox checkboxView;
		
		@Override
		public boolean onChildClick(ExpandableListView parent, View view, final int groupPosition, final int childPosition, long id) {
			// TODO Auto-generated method stub
			
			itemidView = (TextView) view.findViewById(R.id.itemidtext);
			idView = (TextView) view.findViewById(R.id.idtext);
			itemnameView = (TextView) view.findViewById(R.id.itemnametext);
			itempriceView = (TextView) view.findViewById(R.id.itempricetext);
			checkboxView = (CheckBox) view.findViewById(R.id.checkboxview);

			final int ItemID = Integer.parseInt(itemidView.getText().toString());
			
			idView.setBackgroundColor(act.getResources().getColor(R.color.dayitembgselect));
			itemnameView.setBackgroundColor(act.getResources().getColor(R.color.dayitembgselect));
			itempriceView.setBackgroundColor(act.getResources().getColor(R.color.dayitembgselect));
			checkboxView.setBackgroundColor(act.getResources().getColor(R.color.dayitembgselect));

			Dialog dialog = new AlertDialog.Builder(context)
					.setIcon(R.drawable.ic_info_d)
					.setTitle("提示")
					.setMessage("请选择您的操作：")
					.setPositiveButton("编辑", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							itemtableAccess = new ItemTableAccess(helper.getReadableDatabase());
							String[] items = itemtableAccess.findItemById(ItemID);
							
							UtilityHelper.setGroupId(context, -1);
							
							UtilityHelper.jumpActivity(act, EditActivity.class, "EditActivity", items, null, "month", 4);
						}
					}).setNeutralButton("删除", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							itemtableAccess = new ItemTableAccess(helper.getWritableDatabase());
							itemtableAccess.delete(ItemID);
									
							Toast.makeText(context, "删除成功。", Toast.LENGTH_LONG).show();
							
							UtilityHelper.setGroupId(context, -1);

							UtilityHelper.jumpActivity(act, MonthTotalActivity.class, "MonthTotalActivity", null, 1);
						}
					}).create();
			dialog.show();

			dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					idView.setBackgroundColor(act.getResources().getColor(android.R.color.white));
					itemnameView.setBackgroundColor(act.getResources().getColor(android.R.color.white));
					itempriceView.setBackgroundColor(act.getResources().getColor(android.R.color.white));
					checkboxView.setBackgroundColor(act.getResources().getColor(android.R.color.white));
				}
			});
			
			return false;
		}		
	}
	
	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return 1;
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
		if (convertView == null) {
			mylistView = new MyListView();
			convertView = layout.inflate(R.layout.monthtotallist, null);
			mylistView.idText = (TextView) convertView.findViewById(R.id.idtext);
			mylistView.monthText = (TextView) convertView.findViewById(R.id.monthtext);
			mylistView.monthpriceText = (TextView) convertView.findViewById(R.id.monthpricetext);
			convertView.setTag(mylistView);
		} else {
			mylistView = (MyListView) convertView.getTag();
		}
		
		String monthtext = getGroup(groupPosition).get("monthtext");
		double monthpricetext = 0;
		for(int i=0; i<this.children.get(groupPosition).size(); i++) {
			Map<String, String> map = this.children.get(groupPosition).get(i);
			monthpricetext += Double.parseDouble(map.get("monthpricetext"));
		}
		
		mylistView.monthText.setText(monthtext);
		mylistView.monthpriceText.setText("￥ " + UtilityHelper.formatDouble(monthpricetext, "0.0##"));
		
		String date = UtilityHelper.getDateNow("date");
		int warnMonthNumber = UtilityHelper.getWarnMonthNumber(this.context);
		if(monthpricetext > warnMonthNumber) {
			mylistView.idText.setTextColor(this.context.getResources().getColor(R.color.daytitlewarn));
			mylistView.monthText.setTextColor(this.context.getResources().getColor(R.color.daytitlewarn));
			mylistView.monthpriceText.setTextColor(this.context.getResources().getColor(R.color.daytitlewarn));
		} else if(monthtext.equals(UtilityHelper.formatDate(date, "yyyy-MM"))) {
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

	private class MySubList extends ExpandableListView {
		private int expHeight = 0;
		
		public MySubList(Context context, int expHeight) {
			super(context);
			this.expHeight = expHeight;
		}

		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			heightMeasureSpec = MeasureSpec.makeMeasureSpec(this.expHeight, MeasureSpec.AT_MOST);
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}

	private class MyListView {
		private TextView idText;
		private TextView monthText;
		private TextView monthpriceText;
	}
	
	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}
}
