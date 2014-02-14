package com.aalife.android;

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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class RankCategoryActivity extends Activity {
	private ListView listRankCat = null;
	private List<Map<String, String>> list = null;
	private SimpleAdapter adapter = null;
	private SQLiteOpenHelper sqlHelper = null;
	private ItemTableAccess itemAccess = null;
	private String curDate = "";
	private TextView tvNavCat = null;
	private TextView tvNavCount = null;
	private TextView tvNavPrice = null;
	private TextView tvNavDate = null;
	private ImageButton btnTitleBack = null;
	private SharedHelper sharedHelper = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rank_category);
		
		//标题变粗
		TextPaint textPaint = null;
		TextView tvTitleId = (TextView) super.findViewById(R.id.tv_title_id);
		textPaint = tvTitleId.getPaint();
		textPaint.setFakeBoldText(true);
		TextView tvTitleCatName = (TextView) super.findViewById(R.id.tv_title_catname);
		textPaint = tvTitleCatName.getPaint();
		textPaint.setFakeBoldText(true);
		TextView tvTitlePrice = (TextView) super.findViewById(R.id.tv_title_price);
		textPaint = tvTitlePrice.getPaint();
		textPaint.setFakeBoldText(true);
				
		//数据库
		sqlHelper = new DatabaseHelper(this);

		//初始化
		sharedHelper = new SharedHelper(this);
		listRankCat = (ListView) super.findViewById(R.id.list_rankcat);
		listRankCat.setDivider(null);
		
		//列表点击
		listRankCat.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ListView lv = (ListView) parent;
		        @SuppressWarnings("unchecked")
				Map<String, String> map = (Map<String, String>) lv.getItemAtPosition(position);
		        int catId = Integer.parseInt(map.get("catid"));
		        
		        TextView tvId = (TextView) view.findViewById(R.id.tv_rank_id);
		        tvId.setBackgroundColor(RankCategoryActivity.this.getResources().getColor(R.color.color_tran_main));
		        TextView tvCatName = (TextView) view.findViewById(R.id.tv_rank_catname);
		        tvCatName.setBackgroundColor(RankCategoryActivity.this.getResources().getColor(R.color.color_tran_main));
		        TextView tvPrice = (TextView) view.findViewById(R.id.tv_rank_price);
		        tvPrice.setBackgroundColor(RankCategoryActivity.this.getResources().getColor(R.color.color_tran_main));
		        
		        Intent intent = new Intent(RankCategoryActivity.this, RankCatDetailActivity.class);
		        intent.putExtra("catid", catId);
		        startActivity(intent);
			}			
		});

		//分类导航
		tvNavCat = (TextView) super.findViewById(R.id.tv_nav_cat);
		textPaint = tvNavCat.getPaint();
		textPaint.setFakeBoldText(true);
		tvNavCat.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				String[] array = curDate.split("-");
				DatePickerDialog dateDialog = new DatePickerDialog(RankCategoryActivity.this, new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int month, int day) {
						String date = UtilityHelper.formatDate(year + "-" + (month + 1) + "-" + day, "");
						setListData(date);
					}					
				}, Integer.parseInt(array[0]), Integer.parseInt(array[1]) - 1, Integer.parseInt(array[2]));
				dateDialog.show();
			}
		});
		
		//次数导航
		tvNavCount = (TextView) super.findViewById(R.id.tv_nav_count);
		tvNavCount.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				RankCategoryActivity.this.close();
				Intent intent = new Intent(RankCategoryActivity.this, RankCountActivity.class);
				startActivity(intent);
			}
		});

		//单价导航
		tvNavPrice = (TextView) super.findViewById(R.id.tv_nav_price);
		tvNavPrice.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				RankCategoryActivity.this.close();
				Intent intent = new Intent(RankCategoryActivity.this, RankPriceActivity.class);
				startActivity(intent);
			}
		});
		
		//日期导航
		tvNavDate = (TextView) super.findViewById(R.id.tv_nav_date);
		tvNavDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				RankCategoryActivity.this.close();
				Intent intent = new Intent(RankCategoryActivity.this, RankDateActivity.class);
				startActivity(intent);
			}
		});
		
		//返回按钮
		btnTitleBack = (ImageButton) super.findViewById(R.id.btn_title_back);
		btnTitleBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				RankCategoryActivity.this.close();
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
		sharedHelper.setDate(curDate);
		tvNavCat.setText(UtilityHelper.formatDate(curDate, "ys-m"));
		
		itemAccess = new ItemTableAccess(sqlHelper.getReadableDatabase());
		list = itemAccess.findRankCatByDate(date);
		itemAccess.close();
		adapter = new SimpleAdapter(this, list, R.layout.list_rankcat, new String[] { "id", "catname", "price" }, new int[] { R.id.tv_rank_id, R.id.tv_rank_catname, R.id.tv_rank_price });
		listRankCat.setAdapter(adapter);
		
		//System.out.println(date);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		curDate = sharedHelper.getDate();
		setListData(curDate);
	}

}
