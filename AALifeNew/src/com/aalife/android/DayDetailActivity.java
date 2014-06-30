package com.aalife.android;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class DayDetailActivity extends Activity {
	private ListView listDay = null;
	private List<Map<String, String>> list = null;
	private SimpleAdapter adapter = null;
	private SQLiteOpenHelper sqlHelper = null;
	private ItemTableAccess itemAccess = null;
	private String curDate = "";
	private String leftDate = "";
	private String rightDate = "";
	private String itemName = "";
	private TextView tvNavMain = null;
	private TextView tvNavLeft = null;
	private TextView tvNavRight = null;
	private LinearLayout layNoItem = null;
	private SharedHelper sharedHelper = null;
	private TextView tvTotalPrice = null;
	private double totalPrice = 0;
	private LinearLayout layDayTotal = null;
	private final int FIRST_REQUEST_CODE = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_day_detail);
		
		//标题变粗
		TextPaint textPaint = null;
		TextView tvTitleSelect = (TextView) super.findViewById(R.id.tv_title_select);
		textPaint = tvTitleSelect.getPaint();
		textPaint.setFakeBoldText(true);
		TextView tvTitleItemName = (TextView) super.findViewById(R.id.tv_title_itemname);
		textPaint = tvTitleItemName.getPaint();
		textPaint.setFakeBoldText(true);
		TextView tvTitleItemPrice = (TextView) super.findViewById(R.id.tv_title_itemprice);
		textPaint = tvTitleItemPrice.getPaint();
		textPaint.setFakeBoldText(true);
		TextView tvTitleRecommend = (TextView) super.findViewById(R.id.tv_title_recommend);
		textPaint = tvTitleRecommend.getPaint();
		textPaint.setFakeBoldText(true);
		
		TextView tvTotalLabel = (TextView) super.findViewById(R.id.tv_total_label);
		textPaint = tvTotalLabel.getPaint();
		textPaint.setFakeBoldText(true);
		tvTotalPrice = (TextView) super.findViewById(R.id.tv_total_price);
		textPaint = tvTotalPrice.getPaint();
		textPaint.setFakeBoldText(true);
		
		//数据库
		sqlHelper = new DatabaseHelper(this);
		
		//获取数据
		Intent intent = super.getIntent();
		curDate = intent.getStringExtra("date");

		//初始化
		sharedHelper = new SharedHelper(this);
		listDay = (ListView) super.findViewById(R.id.list_day_detail);
		listDay.setDivider(null);
		layNoItem = (LinearLayout) super.findViewById(R.id.lay_noitem);
		layNoItem.setVisibility(View.GONE);	
		layDayTotal = (LinearLayout) super.findViewById(R.id.lay_day_total);
		layDayTotal.setVisibility(View.GONE);
		
		//列表点击
		listDay.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ListView lv = (ListView) parent;
				Map<String, String> map = (Map<String, String>) lv.getItemAtPosition(position);
		        final String[] items = new String[5];
		        items[0] = map.get("itemid");
		        items[1] = map.get("catid");
		        items[2] = map.get("itemname");
		        items[3] = UtilityHelper.formatDouble(Double.parseDouble(map.get("pricevalue")), "0.###");
		        items[4] = map.get("itembuydate");
		        
		        final LinearLayout laySelect = (LinearLayout) view.findViewById(R.id.lay_day_select);
		        laySelect.setBackgroundColor(DayDetailActivity.this.getResources().getColor(R.color.color_tran_main));
		        final TextView tvItemName = (TextView) view.findViewById(R.id.tv_day_itemname);
		        tvItemName.setBackgroundColor(DayDetailActivity.this.getResources().getColor(R.color.color_tran_main));
		        final TextView tvItemPrice = (TextView) view.findViewById(R.id.tv_day_itemprice);
		        tvItemPrice.setBackgroundColor(DayDetailActivity.this.getResources().getColor(R.color.color_tran_main));
		        final LinearLayout layRecommend = (LinearLayout) view.findViewById(R.id.lay_day_recommend);
		        layRecommend.setBackgroundColor(DayDetailActivity.this.getResources().getColor(R.color.color_tran_main));
		        
		        Intent intent = new Intent(DayDetailActivity.this, EditActivity.class);
				intent.putExtra("items", items);
		        startActivityForResult(intent, FIRST_REQUEST_CODE);
			}			
		});

		//设置导航日期
		tvNavMain = (TextView) super.findViewById(R.id.tv_nav_main);
		textPaint = tvNavMain.getPaint();
		textPaint.setFakeBoldText(true);
		tvNavMain.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				String[] array = curDate.split("-");
				DatePickerDialog dateDialog = new DatePickerDialog(DayDetailActivity.this, new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int month, int day) {
						String date = UtilityHelper.formatDate(year + "-" + (month + 1) + "-" + day, "");
						setListData(date);
					}					
				}, Integer.parseInt(array[0]), Integer.parseInt(array[1]) - 1, Integer.parseInt(array[2]));
				dateDialog.show();
			}
		});
		
		//向左按钮
		tvNavLeft = (TextView) super.findViewById(R.id.tv_nav_left);
		tvNavLeft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				setListData(leftDate);
			}
		});
		
		//向右按钮
		tvNavRight = (TextView) super.findViewById(R.id.tv_nav_right);
		tvNavRight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				setListData(rightDate);
			}
		});
		
		//返回按钮
		ImageButton btnTitleBack = (ImageButton) super.findViewById(R.id.btn_title_back);
		btnTitleBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent();
				intent.putExtra("date", curDate);
				intent.putExtra("itemname", itemName);
				DayDetailActivity.this.setResult(Activity.RESULT_OK, intent);
				DayDetailActivity.this.close();
			}			
		});

		//添加按钮
		ImageButton btnTitleAdd = (ImageButton) super.findViewById(R.id.btn_title_add);
		btnTitleAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(DayDetailActivity.this, AddActivity.class);
				intent.putExtra("date", curDate);
				startActivityForResult(intent, FIRST_REQUEST_CODE);
			}			
		});

		setListData(curDate);
	}

	//关闭this
	protected void close() {
		this.finish();
	}
	
	//返回键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.putExtra("date", curDate);
			intent.putExtra("itemname", itemName);
			DayDetailActivity.this.setResult(Activity.RESULT_OK, intent);
			DayDetailActivity.this.close();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	//设置ListView	
	protected void setListData(String date) {
		curDate = date;
		tvNavMain.setText(UtilityHelper.formatDate(date, "m-d-w"));
		
		leftDate = UtilityHelper.getNavDate(date, -1, "d");
		tvNavLeft.setText(UtilityHelper.formatDate(leftDate, "m-d-w"));
		
		rightDate = UtilityHelper.getNavDate(date, 1, "d");
		tvNavRight.setText(UtilityHelper.formatDate(rightDate, "m-d-w"));
		
		itemAccess = new ItemTableAccess(sqlHelper.getReadableDatabase());
		list = itemAccess.findItemByDate(date);
		itemAccess.close();
		
		final boolean[][] listCheck = new boolean[2][list.size()];
		for(int i=0; i<list.size(); i++) {
			Map<String, String> map = list.get(i);
			listCheck[1][i] = map.get("recommend").toString().equals("0") ? false : true;
		}
		adapter = new SimpleAdapter(this, list, R.layout.list_day_detail, new String[] { "id", "itemname", "itemprice", "pricevalue", "id", "itemid", "catid" }, new int[] { R.id.cb_day_select, R.id.tv_day_itemname, R.id.tv_day_itemprice, R.id.tv_day_pricevalue, R.id.cb_day_recommend, R.id.tv_day_itemid, R.id.tv_day_catid }) {
			@Override
			public View getView(final int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				TextView tv = (TextView) view.findViewById(R.id.tv_day_itemid);
				final int itemId = Integer.parseInt(tv.getText().toString());
				final TextView value = (TextView) view.findViewById(R.id.tv_day_pricevalue);
				final CheckBox sel = (CheckBox) view.findViewById(R.id.cb_day_select);
				sel.setChecked(!listCheck[0][position]);
				sel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						listCheck[0][position] = sel.isChecked() ? false : true;
						if(sel.isChecked())
						    updateTotal(Double.parseDouble(value.getText().toString()));
						else
							updateTotal(-Double.parseDouble(value.getText().toString()));
					}					
				});
				final CheckBox re = (CheckBox) view.findViewById(R.id.cb_day_recommend);
				re.setChecked(listCheck[1][position]);
				re.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						itemAccess = new ItemTableAccess(sqlHelper.getReadableDatabase());
						listCheck[1][position] = re.isChecked() ? false : true;
						if(re.isChecked())
						    itemAccess.updateItemRecommend(itemId, 1);
						else
							itemAccess.updateItemRecommend(itemId, 0);
						itemAccess.close();
						
						sharedHelper.setLocalSync(true);
			        	sharedHelper.setSyncStatus(getString(R.string.txt_home_hassync));
					}						
				});
				
				return view;
			}			
		};
		listDay.setAdapter(adapter);
		
		//设置empty
		if(list.size() <= 0) {
			layNoItem.setVisibility(View.VISIBLE);
			layDayTotal.setVisibility(View.GONE);
		} else {
			layNoItem.setVisibility(View.GONE);
			layDayTotal.setVisibility(View.VISIBLE);
		}
		
		//统计总价
		totalPrice = 0;
		Iterator<Map<String, String>> it = list.iterator();
		while(it.hasNext()) {
			Map<String, String> map = (Map<String, String>) it.next();
			totalPrice += Double.parseDouble(map.get("pricevalue"));
		}
		tvTotalPrice.setText(getString(R.string.txt_price) + UtilityHelper.formatDouble(totalPrice, "0.0##"));
	}

	//更新总价
	protected void updateTotal(double price) {
		totalPrice += price;
		tvTotalPrice.setText(getString(R.string.txt_price) + UtilityHelper.formatDouble(totalPrice, "0.0##"));
	}
	
	//返回处理
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == FIRST_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
			if(data != null) {
				String itemName = data.getStringExtra("itemname");
				if(itemName != null && !itemName.equals(""))
					this.itemName = itemName;
			}
			
			setListData(curDate);
		}
	}
	
}
