package com.aalife.android;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class DayAdapter extends BaseAdapter {
	private Context context = null;
	private List<Map<String, String>> list = null;
	private LayoutInflater layout = null;
	private SimpleAdapter simpleAdapter = null;
	private ItemTableAccess itemAccess = null;
	private SQLiteOpenHelper sqlHelper = null;
	private SharedHelper sharedHelper = null;
	private DayActivity activity = null;
	private final int FIRST_REQUEST_CODE = 1;
	
	public DayAdapter(Context context, List<Map<String, String>> list) {
		this.context = context;
		this.list = list;
		this.layout = LayoutInflater.from(this.context);
		this.sqlHelper = new DatabaseHelper(this.context);
		this.sharedHelper = new SharedHelper(this.context);
		this.activity = (DayActivity) context;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ItemEntity itemEntity = null;
		if (convertView == null) {
			itemEntity = new ItemEntity();
			convertView = layout.inflate(R.layout.list_day, parent, false);
			itemEntity.tvItemBuyDate = (TextView) convertView.findViewById(R.id.tv_day_itembuydate);
			itemEntity.tvTotalPrice = (TextView) convertView.findViewById(R.id.tv_day_totalprice);
			itemEntity.lvDayListSub = (MyListView) convertView.findViewById(R.id.list_day_sub);
			convertView.setTag(itemEntity);
		} else {
			itemEntity = (ItemEntity) convertView.getTag();
		}
		
		//标题变粗
		TextPaint textPaint = null;
		textPaint = itemEntity.tvItemBuyDate.getPaint();
		textPaint.setFakeBoldText(true);
		textPaint = itemEntity.tvTotalPrice.getPaint();
		textPaint.setFakeBoldText(true);
		
		//取值赋值
		final String itemBuyDate = list.get(position).get("itembuydate");
		itemEntity.tvItemBuyDate.setText(UtilityHelper.formatDate(itemBuyDate, "y-m-d-w2"));
		String totalPrice = list.get(position).get("pricevalue");
		itemEntity.tvTotalPrice.setText(context.getString(R.string.txt_price) + " " + totalPrice);
		
		//设置DayList
		itemAccess = new ItemTableAccess(sqlHelper.getReadableDatabase());
		final List<Map<String, String>> subList = itemAccess.findItemByDate(itemBuyDate);
		Map<String, String> totalMap = itemAccess.findAllMonth(itemBuyDate);
		itemAccess.close();

		//设置总计
		activity.setTotalData(itemBuyDate, totalMap.get("price"));
		
		//设置CheckBox选中
		final boolean[] reCheck = new boolean[subList.size()];
		for(int i=0; i < subList.size(); i++) {
			Map<String, String> map = subList.get(i);
			reCheck[i] = map.get("recommend").toString().equals("0") ? false : true;
		}
		
		//子ListView数据
		simpleAdapter = new SimpleAdapter(this.context, subList, R.layout.list_day_sub, new String[] { "itemid", "itemname", "itemprice", "pricevalue" }, new int[] { R.id.tv_day_itemid, R.id.tv_day_itemname, R.id.tv_day_itemprice, R.id.tv_day_pricevalue }) {
			@Override
			public View getView(int arg0, View arg1, ViewGroup arg2) {
				View view = null;
				if(arg1 != null) {
					view = arg1;
				} else {
					view = super.getView(arg0, arg1, arg2);
				}
				TextView tvItemId = (TextView) view.findViewById(R.id.tv_day_itemid);
				final int itemId = Integer.parseInt(tvItemId.getText().toString());
				//推荐CheckBox
				final CheckBox re = (CheckBox) view.findViewById(R.id.cb_day_recommend);
				re.setChecked(reCheck[arg0]);
				re.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						itemAccess = new ItemTableAccess(sqlHelper.getReadableDatabase());
						if(re.isChecked()) {
						    itemAccess.updateItemRecommend(itemId, 1);
						} else {
							itemAccess.updateItemRecommend(itemId, 0);
						}
						itemAccess.close();
						
						sharedHelper.setLocalSync(true);
			        	sharedHelper.setSyncStatus(context.getString(R.string.txt_home_hassync));
					}
				});
				
				return view;
			}
		};
				
		itemEntity.lvDayListSub.setAdapter(simpleAdapter);

		//列表点击
		itemEntity.lvDayListSub.setOnItemClickListener(new OnItemClickListener(){
			TextView tvItemName = null;
			TextView tvItemPrice = null;
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ListView lv = (ListView) parent;
				@SuppressWarnings("unchecked")
				Map<String, String> map = (Map<String, String>) lv.getItemAtPosition(position);
		        final String[] items = new String[7];
		        items[0] = map.get("itemid");
		        items[1] = map.get("catid");
		        items[2] = map.get("itemnamevalue");
		        items[3] = UtilityHelper.formatDouble(Double.parseDouble(map.get("pricevalue")), "0.###");
		        items[4] = map.get("itembuydate");
		        items[5] = map.get("regionid");
		        items[6] = map.get("regiontype");
		        
		        tvItemName = (TextView) view.findViewById(R.id.tv_day_itemname);
		        tvItemName.setTextColor(activity.getResources().getColor(R.color.color_back_main));
		        tvItemPrice = (TextView) view.findViewById(R.id.tv_day_itemprice);
		        tvItemPrice.setTextColor(activity.getResources().getColor(R.color.color_back_main));
		        
		        Intent intent = new Intent(activity, EditActivity.class);
				intent.putExtra("items", items);
				activity.startActivityForResult(intent, FIRST_REQUEST_CODE);
			}			
		});
						
		return convertView;
	}
	
	//更新数据源
	protected void updateData(List<Map<String, String>> list) {
		this.list = list;
		notifyDataSetChanged();
	}
	
	private class ItemEntity {
		private TextView tvItemBuyDate;
		private TextView tvTotalPrice;
		private MyListView lvDayListSub;
	}

}
