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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MonthActivity extends Activity {
	private ListView listMonth = null;
	private List<Map<String, String>> list = null;
	private SimpleAdapter adapter = null;
	private SQLiteOpenHelper sqlHelper = null;
	private ItemTableAccess itemAccess = null;
	private String curDate = "";
	private String leftDate = "";
	private String rightDate = "";
	private TextView tvNavMain = null;
	private TextView tvNavLeft = null;
	private TextView tvNavRight = null;
	private LinearLayout layNoItem = null;
	private ImageButton btnTitleBack = null;
	private ImageButton btnTitleAdd = null;
	private SharedHelper sharedHelper = null;
	private TextView tvTotalPrice = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_month);

		//标题变粗
		TextPaint textPaint = null;
		TextView tvTitleSelect = (TextView) super.findViewById(R.id.tv_title_select);
		textPaint = tvTitleSelect.getPaint();
		textPaint.setFakeBoldText(true);
		TextView tvTitleDate = (TextView) super.findViewById(R.id.tv_title_date);
		textPaint = tvTitleDate.getPaint();
		textPaint.setFakeBoldText(true);
		TextView tvTitlePrice = (TextView) super.findViewById(R.id.tv_title_price);
		textPaint = tvTitlePrice.getPaint();
		textPaint.setFakeBoldText(true);
		TextView tvTotalLabel = (TextView) super.findViewById(R.id.tv_total_label);
		textPaint = tvTotalLabel.getPaint();
		textPaint.setFakeBoldText(true);
		tvTotalPrice = (TextView) super.findViewById(R.id.tv_total_price);
		textPaint = tvTotalPrice.getPaint();
		textPaint.setFakeBoldText(true);

		sharedHelper = new SharedHelper(this);
		
		//当前日期
		curDate = sharedHelper.getDate();
		if(curDate.equals(""))
			curDate = UtilityHelper.getCurDate();
		
		//数据库
		sqlHelper = new DatabaseHelper(this);
		
		listMonth = (ListView) super.findViewById(R.id.list_month);
		layNoItem = (LinearLayout) super.findViewById(R.id.lay_noitem);
		
		//列表点击
		listMonth.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ListView lv = (ListView) parent;
		        @SuppressWarnings("unchecked")
				Map<String, String> map = (Map<String, String>) lv.getItemAtPosition(position);
		        String date = map.get("datevalue");
		        sharedHelper.setDate(date);
		        
		        TextView tvId = (TextView) view.findViewById(R.id.tv_month_id);
		        tvId.setBackgroundColor(MonthActivity.this.getResources().getColor(R.color.color_tran_main));
		        TextView tvDate = (TextView) view.findViewById(R.id.tv_month_date);
		        tvDate.setBackgroundColor(MonthActivity.this.getResources().getColor(R.color.color_tran_main));
		        TextView tvPrice = (TextView) view.findViewById(R.id.tv_month_price);
		        tvPrice.setBackgroundColor(MonthActivity.this.getResources().getColor(R.color.color_tran_main));
		        
		        Intent intent = new Intent(MonthActivity.this, DayActivity.class);
		        startActivity(intent);
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
				DatePickerDialog dateDialog = new DatePickerDialog(MonthActivity.this, new DatePickerDialog.OnDateSetListener() {
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

		//设置ListView
		setListData(curDate);
		
		//返回按钮
		btnTitleBack = (ImageButton) super.findViewById(R.id.btn_title_back);
		btnTitleBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				MonthActivity.this.close();
			}			
		});

		//添加按钮
		btnTitleAdd = (ImageButton) super.findViewById(R.id.btn_title_add);
		btnTitleAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				sharedHelper.setDate(curDate);
				Intent intent = new Intent(MonthActivity.this, AddActivity.class);
				startActivity(intent);
			}			
		});
	}

	//关闭this
	protected void close() {
		this.finish();
	}
	
	//设置ListView	
	protected void setListData(String date) {
		curDate = date;
		tvNavMain.setText(UtilityHelper.formatDate(date, "y-m"));
		sharedHelper.setDate(date);
		
		leftDate = UtilityHelper.getNavDate(date, -1, "m");
		tvNavLeft.setText(UtilityHelper.formatDate(leftDate, "y-m"));
		
		rightDate = UtilityHelper.getNavDate(date, 1, "m");
		tvNavRight.setText(UtilityHelper.formatDate(rightDate, "y-m"));
		
		itemAccess = new ItemTableAccess(sqlHelper.getReadableDatabase());
		list = itemAccess.findMonthByDate(date);
		itemAccess.close();
		adapter = new SimpleAdapter(this, list, R.layout.list_month, new String[] { "id", "price", "date", "datevalue" }, new int[] { R.id.tv_month_id, R.id.tv_month_price, R.id.tv_month_date, R.id.tv_month_datevalue });
		listMonth.setAdapter(adapter);
		
		//设置empty
		if(list.size() == 0)
			layNoItem.setVisibility(View.VISIBLE);
		else
			layNoItem.setVisibility(View.GONE);
		
		double total = 0;
		Iterator<Map<String, String>> it = list.iterator();
		while(it.hasNext()) {
			Map<String, String> map = (Map<String, String>) it.next();
			total += Double.parseDouble(map.get("pricevalue"));
		}
		tvTotalPrice.setText(getString(R.string.txt_price) + UtilityHelper.formatDouble(total, "0.0##"));		
		
		//System.out.println(date);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		onCreate(null);
	}
	
}
