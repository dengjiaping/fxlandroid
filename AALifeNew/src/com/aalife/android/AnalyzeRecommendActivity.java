package com.aalife.android;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class AnalyzeRecommendActivity extends Activity {
	private ListView listAnalyzeRecommend = null;
	private List<Map<String, String>> list = null;
	private SimpleAdapter adapter = null;
	private SQLiteOpenHelper sqlHelper = null;
	private ItemTableAccess itemAccess = null;
	private String curDate = "";
	private LinearLayout layNoItem = null;
	private ImageButton btnTitleBack = null;
	private SharedHelper sharedHelper = null;
	private TextView tvNavCommend = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_analyze_recommend);
		
		//标题变粗
		TextPaint textPaint = null;
		TextView tvTitleItemName = (TextView) super.findViewById(R.id.tv_title_itemname);
		textPaint = tvTitleItemName.getPaint();
		textPaint.setFakeBoldText(true);
		TextView tvTitleItemBuyDate = (TextView) super.findViewById(R.id.tv_title_itembuydate);
		textPaint = tvTitleItemBuyDate.getPaint();
		textPaint.setFakeBoldText(true);
		TextView tvTitlePrice = (TextView) super.findViewById(R.id.tv_title_itemprice);
		textPaint = tvTitlePrice.getPaint();
		textPaint.setFakeBoldText(true);
		
		tvNavCommend = (TextView) super.findViewById(R.id.tv_nav_commend);
		textPaint = tvNavCommend.getPaint();
		textPaint.setFakeBoldText(true);
				
		//数据库
		sqlHelper = new DatabaseHelper(this);

		//初始化
		sharedHelper = new SharedHelper(this);
		listAnalyzeRecommend = (ListView) super.findViewById(R.id.list_analyzerecommend);
		listAnalyzeRecommend.setDivider(null);
		layNoItem = (LinearLayout) super.findViewById(R.id.lay_noitem);
		
		//列表点击
		listAnalyzeRecommend.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ListView lv = (ListView) parent;
		        @SuppressWarnings("unchecked")
				Map<String, String> map = (Map<String, String>) lv.getItemAtPosition(position);
		        String date = map.get("datevalue");
		        sharedHelper.setDate(date);
		        
		        TextView tvItemName = (TextView) view.findViewById(R.id.tv_rank_itemname);
		        tvItemName.setBackgroundColor(AnalyzeRecommendActivity.this.getResources().getColor(R.color.color_tran_main));
		        TextView tvItemBuyDate = (TextView) view.findViewById(R.id.tv_rank_itembuydate);
		        tvItemBuyDate.setBackgroundColor(AnalyzeRecommendActivity.this.getResources().getColor(R.color.color_tran_main));
		        TextView tvItemPrice = (TextView) view.findViewById(R.id.tv_rank_itemprice);
		        tvItemPrice.setBackgroundColor(AnalyzeRecommendActivity.this.getResources().getColor(R.color.color_tran_main));
		        
		        Intent intent = new Intent(AnalyzeRecommendActivity.this, DayActivity.class);
		        startActivity(intent);
			}			
		});
		
		//返回按钮
		btnTitleBack = (ImageButton) super.findViewById(R.id.btn_title_back);
		btnTitleBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				AnalyzeRecommendActivity.this.close();
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
		
		itemAccess = new ItemTableAccess(sqlHelper.getReadableDatabase());
		list = itemAccess.findAnalyzeRecommend();
		itemAccess.close();
		adapter = new SimpleAdapter(this, list, R.layout.list_rankprice, new String[] { "itemname", "itembuydate", "itemprice" }, new int[] { R.id.tv_rank_itemname, R.id.tv_rank_itembuydate, R.id.tv_rank_itemprice });
		listAnalyzeRecommend.setAdapter(adapter);

		//设置empty
		if(list.size() == 0)
			layNoItem.setVisibility(View.VISIBLE);
		else
			layNoItem.setVisibility(View.GONE);
		
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
