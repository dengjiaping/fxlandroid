package com.aalife.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class DayListAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Map<String, String>> list;
	private LayoutInflater layout;

	private SimpleAdapter simpleAdapter;
	private ItemTableAccess itemtableAccess;
	private SQLiteOpenHelper helper;

	public DayListAdapter(Context context, ArrayList<Map<String, String>> list) {
		this.context = context;
		this.list = list;
		this.layout = LayoutInflater.from(this.context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ItemListView itemlistView = null;
		if (convertView == null) {
			itemlistView = new ItemListView();
			convertView = layout.inflate(R.layout.daylist, null);
			itemlistView.itembuydateText = (TextView) convertView.findViewById(R.id.itembuydatetext);
			itemlistView.itempriceText = (TextView) convertView.findViewById(R.id.itempricetext);
			itemlistView.btn_add = (ImageButton) convertView.findViewById(R.id.addbtn);
			itemlistView.subitemlistView = (ListView) convertView.findViewById(R.id.subitemlistview);
			convertView.setTag(itemlistView);
		} else {
			itemlistView = (ItemListView) convertView.getTag();
		}
				
		final String itembuydate = list.get(position).get("itembuydate");
		String itemprice = list.get(position).get("itemprice");
		itemlistView.itembuydateText.setText(itembuydate + "  " + UtilityHelper.formatDate(itembuydate, "WW"));
		itemlistView.itempriceText.setText("￥ " + itemprice);
		
		itemlistView.btn_add.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) { 
				UtilityHelper.jumpActivity((Activity)context, AddActivity.class, "AddActivity", itembuydate, 4);
			}
		});
		
		String date = UtilityHelper.getDateNow("date");
		int warnNumber = UtilityHelper.getWarnNumber(this.context);
		if (Double.parseDouble(itemprice) > warnNumber) {
			itemlistView.itembuydateText.setBackgroundColor(this.context.getResources().getColor(R.color.daytitlebgwarn));
			itemlistView.itempriceText.setBackgroundColor(this.context.getResources().getColor(R.color.daytitlebgwarn));
			itemlistView.btn_add.setImageResource(R.drawable.ic_add_l_r);
			itemlistView.btn_add.setBackgroundColor(this.context.getResources().getColor(R.color.daytitlebgwarn));
			
			itemlistView.itembuydateText.setTextColor(this.context.getResources().getColor(R.color.daytitlewarn));
			itemlistView.itempriceText.setTextColor(this.context.getResources().getColor(R.color.daytitlewarn));
		} else if (itembuydate.equals(date)) {
			itemlistView.itembuydateText.setBackgroundColor(this.context.getResources().getColor(R.color.daytitlebgcur));
			itemlistView.itempriceText.setBackgroundColor(this.context.getResources().getColor(R.color.daytitlebgcur));
			itemlistView.btn_add.setImageResource(R.drawable.ic_add_l_g);
			itemlistView.btn_add.setBackgroundColor(this.context.getResources().getColor(R.color.daytitlebgcur));
			
			itemlistView.itembuydateText.setTextColor(this.context.getResources().getColor(R.color.daytitlecur));
			itemlistView.itempriceText.setTextColor(this.context.getResources().getColor(R.color.daytitlecur));
		} else {
			itemlistView.itembuydateText.setBackgroundColor(this.context.getResources().getColor(R.color.daytitlebg));
			itemlistView.itempriceText.setBackgroundColor(this.context.getResources().getColor(R.color.daytitlebg));
			itemlistView.btn_add.setImageResource(R.drawable.ic_add_l);
			itemlistView.btn_add.setBackgroundColor(this.context.getResources().getColor(R.color.daytitlebg));
			
			itemlistView.itembuydateText.setTextColor(this.context.getResources().getColor(R.color.daytitle));
			itemlistView.itempriceText.setTextColor(this.context.getResources().getColor(R.color.daytitle));
		}

		helper = new MyDatabaseHelper(this.context);
		itemtableAccess = new ItemTableAccess(helper.getReadableDatabase());
		ArrayList<Map<String, String>> sublist = itemtableAccess.findAllByDate(list.get(position).get("itembuydate"));
		if(sublist.size() == 0) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("itemid", "0");
			map.put("itemname", "今日暂无消费。");
			map.put("itemprice", "");
			sublist.add(map);
		}
		
		simpleAdapter = new SimpleAdapter(this.context, sublist, R.layout.daysublist, new String[] { "itemid", "itemname", "itemprice" }, new int[] { R.id.subitemidtext, R.id.subitemnametext, R.id.subitempricetext });
		itemlistView.subitemlistView.setAdapter(simpleAdapter);

		int subHeight = UtilityHelper.getSubHeight(this.context);
		itemlistView.subitemlistView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, sublist.size() * (subHeight + 1)));
		itemlistView.subitemlistView.setOnItemClickListener(new OnSubItemClickImpl());
		itemlistView.subitemlistView.setScrollingCacheEnabled(false);
		
		return convertView;
	}
	
	// 列表点击事件
	private class OnSubItemClickImpl implements OnItemClickListener {
		private Activity act = (Activity) context;
		private TextView itemidView;
		private TextView itemnameView;
		private TextView itempriceView;

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			itemidView = (TextView) view.findViewById(R.id.subitemidtext);
			itemnameView = (TextView) view.findViewById(R.id.subitemnametext);
			itempriceView = (TextView) view.findViewById(R.id.subitempricetext);

			final int ItemID = Integer.parseInt(itemidView.getText().toString());
			if(ItemID == 0) {
				return;
			}

			itemnameView.setBackgroundColor(act.getResources().getColor(R.color.dayitembgselect));
			itempriceView.setBackgroundColor(act.getResources().getColor(R.color.dayitembgselect));

			Dialog dialog = new AlertDialog.Builder(context)
					.setIcon(R.drawable.ic_info_d)
					.setTitle("提示")
					.setMessage("请选择您的操作：")
					.setPositiveButton("编辑", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							itemtableAccess = new ItemTableAccess(helper.getReadableDatabase());
							String[] items = itemtableAccess.findItemById(ItemID);
		
							UtilityHelper.jumpActivity(act, EditActivity.class, "EditActivity", items, null, "", 4);
						}
					}).setNeutralButton("删除", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							itemtableAccess = new ItemTableAccess(helper.getWritableDatabase());
							itemtableAccess.delete(ItemID);
		
							Toast.makeText(context, "删除成功。", Toast.LENGTH_LONG).show();
		
							UtilityHelper.jumpActivity(act, DayActivity.class, "DayActivity", null, 0);
						}
					}).create();
			dialog.show();

			dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					itemnameView.setBackgroundColor(act.getResources().getColor(android.R.color.white));
					itempriceView.setBackgroundColor(act.getResources().getColor(android.R.color.white));
				}
			});
		}
	}

	private class ItemListView {
		private TextView itembuydateText;
		private TextView itempriceText;
		private ImageButton btn_add;
		private ListView subitemlistView;
	}

}
