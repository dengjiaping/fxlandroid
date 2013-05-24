package com.aalife.android;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class RankItemActivity extends Activity {
	private List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	private SimpleAdapter simpleAdapter = null;
	private ListView itemlistView = null;
	
	private TextView totalpriceText = null;
	private SQLiteOpenHelper helper = null;
	private ItemTableAccess itemtableAccess = null;
	private ImageButton backBtn = null;
	private String date = null;
	private String name = null;
	private TextView titleText = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.rankitemactivity);
		
		Intent intent = super.getIntent();
		date = intent.getStringExtra("date");
		if (date == null) {
			date = UtilityHelper.getAppDate(this);
		}
		name = intent.getStringExtra("name");
		
		this.titleText = (TextView) super.findViewById(R.id.titletext);
		this.titleText.setText(UtilityHelper.formatDate(date, "yyyy-MM") + " " + getString(R.string.title_activity_rank));

		this.helper = new MyDatabaseHelper(this);
		
		this.totalpriceText = (TextView) super.findViewById(R.id.totalpricetext);
		this.itemtableAccess = new ItemTableAccess(this.helper.getReadableDatabase());
		this.totalpriceText.setText(this.itemtableAccess.findAllTotalWithRankByName(date, name));

		this.backBtn = (ImageButton) super.findViewById(R.id.backbtn);
		this.backBtn.setOnClickListener(new BtnRankBackClickImpl());

		this.itemlistView = (ListView) super.findViewById(R.id.itemlistview);
		this.itemtableAccess = new ItemTableAccess(this.helper.getReadableDatabase());
		this.list = this.itemtableAccess.findAllWithRankByName(date, name);
		this.simpleAdapter = new SimpleAdapter(this, this.list, R.layout.rankpricelist, new String[] { "id", "itemname", "itembuydate", "itemprice" }, new int[] { R.id.idtext, R.id.itemnametext, R.id.itembuydatetext, R.id.itempricetext });
		this.itemlistView.setAdapter(this.simpleAdapter);		
	}

	// 返回按钮
	private class BtnRankBackClickImpl implements OnClickListener {
		@Override
		public void onClick(View view) {
			UtilityHelper.jumpActivity(RankItemActivity.this, RankActivity.class, "RankActivity", null, 2);
		}
	}
	
	// 返回键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			UtilityHelper.jumpActivity(RankItemActivity.this, RankActivity.class, "RankActivity", date, 2);
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

}
