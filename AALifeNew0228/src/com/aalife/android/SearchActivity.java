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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SearchActivity extends Activity {
	private ListView listSearch = null;
	private List<Map<String, String>> list = null;
	private SimpleAdapter adapter = null;
	private SQLiteOpenHelper sqlHelper = null;
	private ItemTableAccess itemAccess = null;
	private LinearLayout layNoItem = null;
	private ImageButton btnTitleBack = null;
	private ImageButton btnTitleSearch = null;
	private EditText etTitleKey = null;
	private String key = "";
	private final int FIRST_REQUEST_CODE = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
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

		//初始化
		listSearch = (ListView) super.findViewById(R.id.list_search);
		listSearch.setDivider(null);
		layNoItem = (LinearLayout) super.findViewById(R.id.lay_noitem);
		layNoItem.setVisibility(View.VISIBLE);
		etTitleKey = (EditText) super.findViewById(R.id.et_title_key);
		
		//列表点击
		listSearch.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ListView lv = (ListView) parent;
		        @SuppressWarnings("unchecked")
				Map<String, String> map = (Map<String, String>) lv.getItemAtPosition(position);
		        String date = map.get("datevalue");
		        
		        TextView tvItemName = (TextView) view.findViewById(R.id.tv_rank_itemname);
		        tvItemName.setBackgroundColor(SearchActivity.this.getResources().getColor(R.color.color_tran_main));
		        TextView tvItemBuyDate = (TextView) view.findViewById(R.id.tv_rank_itembuydate);
		        tvItemBuyDate.setBackgroundColor(SearchActivity.this.getResources().getColor(R.color.color_tran_main));
		        TextView tvItemPrice = (TextView) view.findViewById(R.id.tv_rank_itemprice);
		        tvItemPrice.setBackgroundColor(SearchActivity.this.getResources().getColor(R.color.color_tran_main));
		        
		        Intent intent = new Intent(SearchActivity.this, DayDetailActivity.class);
		        intent.putExtra("date", date);
		        startActivityForResult(intent, FIRST_REQUEST_CODE);
			}			
		});
				
		//返回按钮
		btnTitleBack = (ImageButton) super.findViewById(R.id.btn_title_back);
		btnTitleBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				SearchActivity.this.close();
			}			
		});

		//搜索按钮
		btnTitleSearch = (ImageButton) super.findViewById(R.id.btn_title_search);
		btnTitleSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				key = etTitleKey.getText().toString();
				if(key.equals("")) {
					Toast.makeText(SearchActivity.this, getString(R.string.txt_search_keyempty), Toast.LENGTH_SHORT).show();
					return;
				}
				
				setListData(key);
			}			
		});
	}
	
	//关闭this
	protected void close() {
		this.finish();
	}
	
	//设置ListView	
	protected void setListData(String key) {		
		itemAccess = new ItemTableAccess(sqlHelper.getReadableDatabase());
		list = itemAccess.findItemByKey(key);
		itemAccess.close();
		adapter = new SimpleAdapter(this, list, R.layout.list_rankprice, new String[] { "itemname", "itembuydate", "itemprice" }, new int[] { R.id.tv_rank_itemname, R.id.tv_rank_itembuydate, R.id.tv_rank_itemprice });
		listSearch.setAdapter(adapter);

		//设置empty
		if(list.size() == 0)
			layNoItem.setVisibility(View.VISIBLE);
		else
			layNoItem.setVisibility(View.GONE);
	}	
	
	//返回处理
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == FIRST_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
			setListData(key);
		}
	}
	
}
