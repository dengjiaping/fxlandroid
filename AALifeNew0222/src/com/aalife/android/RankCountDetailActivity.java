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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class RankCountDetailActivity extends Activity {
	private ListView listRankCountDetail = null;
	private List<Map<String, String>> list = null;
	private SimpleAdapter adapter = null;
	private SQLiteOpenHelper sqlHelper = null;
	private ItemTableAccess itemAccess = null;
	private String curDate = "";
	private ImageButton btnTitleBack = null;
	private SharedHelper sharedHelper = null;
	private String itemName = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rank_count_detail);
		
		//标题变粗
		TextPaint textPaint = null;
		TextView tvTitleItemName = (TextView) super.findViewById(R.id.tv_title_itemname);
		textPaint = tvTitleItemName.getPaint();
		textPaint.setFakeBoldText(true);
		TextView tvTitleItemBuyDate = (TextView) super.findViewById(R.id.tv_title_itembuydate);
		textPaint = tvTitleItemBuyDate.getPaint();
		textPaint.setFakeBoldText(true);
		TextView tvTitleItemPrice = (TextView) super.findViewById(R.id.tv_title_itemprice);
		textPaint = tvTitleItemPrice.getPaint();
		textPaint.setFakeBoldText(true);
				
		//数据库
		sqlHelper = new DatabaseHelper(this);

		//取传入的值
		Intent intent = super.getIntent();
		itemName = intent.getStringExtra("itemname");
		
		//初始化
		sharedHelper = new SharedHelper(this);
		listRankCountDetail = (ListView) super.findViewById(R.id.list_rankcountdetail);
		listRankCountDetail.setDivider(null);
		
		//列表点击
		listRankCountDetail.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ListView lv = (ListView) parent;
		        @SuppressWarnings("unchecked")
				Map<String, String> map = (Map<String, String>) lv.getItemAtPosition(position);
		        String date = map.get("datevalue");
		        sharedHelper.setDate(date);
		        
		        TextView tvItemName = (TextView) view.findViewById(R.id.tv_rank_itemname);
		        tvItemName.setBackgroundColor(RankCountDetailActivity.this.getResources().getColor(R.color.color_tran_main));
		        TextView tvItemBuyDate = (TextView) view.findViewById(R.id.tv_rank_itembuydate);
		        tvItemBuyDate.setBackgroundColor(RankCountDetailActivity.this.getResources().getColor(R.color.color_tran_main));
		        TextView tvItemPrice = (TextView) view.findViewById(R.id.tv_rank_itemprice);
		        tvItemPrice.setBackgroundColor(RankCountDetailActivity.this.getResources().getColor(R.color.color_tran_main));
		        
		        Intent intent = new Intent(RankCountDetailActivity.this, DayDetailActivity.class);
		        startActivity(intent);
			}			
		});
		
		//返回按钮
		btnTitleBack = (ImageButton) super.findViewById(R.id.btn_title_back);
		btnTitleBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				RankCountDetailActivity.this.close();
			}			
		});
		
	}

	//关闭this
	protected void close() {
		this.finish();
	}
	
	//设置ListView	
	protected void setListData() {
		curDate = sharedHelper.getDate();
		
		itemAccess = new ItemTableAccess(sqlHelper.getReadableDatabase());
		list = itemAccess.findRankPriceByDate(curDate, itemName);
		itemAccess.close();
		adapter = new SimpleAdapter(this, list, R.layout.list_rankprice, new String[] { "itemname", "itembuydate", "itemprice" }, new int[] { R.id.tv_rank_itemname, R.id.tv_rank_itembuydate, R.id.tv_rank_itemprice });
		listRankCountDetail.setAdapter(adapter);
		
		//System.out.println(date);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		setListData();
	}

}