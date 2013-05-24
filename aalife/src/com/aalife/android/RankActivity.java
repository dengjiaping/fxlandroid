package com.aalife.android;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class RankActivity extends Activity implements OnGestureListener, OnTouchListener {
	private ViewFlipper vf = null;
	private GestureDetector gd = null;
	
	private List<Map<String, String>> priceList = new ArrayList<Map<String, String>>();
	private SimpleAdapter priceAdapter = null;
	private ListView pricelistView = null;
	
	private List<Map<String, String>> itemList = new ArrayList<Map<String, String>>();
	private SimpleAdapter itemAdapter = null;
	private ListView itemlistView = null;
	
	private TextView monthtotalpriceText = null;
	private SQLiteOpenHelper helper = null;
	private ItemTableAccess itemtableAccess = null;
	private ImageButton selectmonthBtn = null;
	private String date = null;	
	private TextView titleText = null;
	
	private TextView pricenodataText = null;
	private TextView itemnodataText = null;
	
	private Animation left_in;
	private Animation left_out;
	private Animation right_in;
	private Animation right_out;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.rankactivity);
		
		Intent intent = super.getIntent();
		date = intent.getStringExtra("date");
		if (date == null) {
			date = UtilityHelper.getAppDate(this);
		}
		
		if(UtilityHelper.getRankFlag(this)) {
			Toast toast = Toast.makeText(this, getString(R.string.txt_rank_info), Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			
			UtilityHelper.setRankFlag(this, false);
		}
		
		this.titleText = (TextView) super.findViewById(R.id.titletext);
		this.titleText.setText(UtilityHelper.formatDate(date, "yyyy-MM") + " " + getString(R.string.title_activity_rank));

		this.helper = new MyDatabaseHelper(this);
		
		this.vf = (ViewFlipper) super.findViewById(R.id.viewflipper);
		this.gd = new GestureDetector(this);
		
		this.left_in = AnimationUtils.loadAnimation(this, R.anim.left_in);
		this.left_out = AnimationUtils.loadAnimation(this, R.anim.left_out);
		this.right_in = AnimationUtils.loadAnimation(this, R.anim.right_in);
		this.right_out = AnimationUtils.loadAnimation(this, R.anim.right_out);		
		
		this.monthtotalpriceText = (TextView) super.findViewById(R.id.monthtotalpricetext);
		this.itemtableAccess = new ItemTableAccess(this.helper.getReadableDatabase());
		this.monthtotalpriceText.setText("☆☆  总计：￥ " + this.itemtableAccess.findAllTotalByMonth(date) + "  ☆☆");

		this.selectmonthBtn = (ImageButton) super.findViewById(R.id.selectmonthbtn);
		this.selectmonthBtn.setOnClickListener(new BtnRankSelectmonthClickImpl());

		this.pricelistView = (ListView) super.findViewById(R.id.pricelistview);
		this.itemtableAccess = new ItemTableAccess(this.helper.getReadableDatabase());
		this.priceList = this.itemtableAccess.findAllWithRankByMonth(date);
		this.priceAdapter = new SimpleAdapter(this, this.priceList, R.layout.rankpricelist, new String[] { "id", "itemname", "itembuydate", "itemprice" }, new int[] { R.id.idtext, R.id.itemnametext, R.id.itembuydatetext, R.id.itempricetext });
		this.pricelistView.setAdapter(this.priceAdapter);
		this.pricelistView.setOnItemClickListener(new OnItemClickImpl());
		
		this.itemlistView = (ListView) super.findViewById(R.id.itemlistview);
		this.itemtableAccess = new ItemTableAccess(this.helper.getReadableDatabase());
		this.itemList = this.itemtableAccess.findAllWithRankItemByMonth(date);
		this.itemAdapter = new SimpleAdapter(this, this.itemList, R.layout.rankitemlist, new String[] { "id", "itemname", "itemprice", "itemnamecount" }, new int[] { R.id.idtext, R.id.itemnametext, R.id.itempricetext, R.id.itemnamecounttext });
		this.itemlistView.setAdapter(this.itemAdapter);
		this.itemlistView.setOnItemClickListener(new OnItemClickImpl());
		
		this.pricenodataText = (TextView) super.findViewById(R.id.pricenodatatext);
		if(this.priceList.isEmpty()) {
			this.pricenodataText.setVisibility(1);
		}
		
		this.itemnodataText = (TextView) super.findViewById(R.id.itemnodatatext);
		if(this.itemList.isEmpty()) {
			this.itemnodataText.setVisibility(1);
		}
		
		this.vf.setOnTouchListener(this);
		this.vf.setLongClickable(true);
	}
	
	// 列表点击
	private class OnItemClickImpl implements OnItemClickListener {
		private TextView itemnameText = null;
		
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			itemnameText = (TextView) view.findViewById(R.id.itemnametext);
			String name = itemnameText.getText().toString();
			UtilityHelper.jumpActivity(RankActivity.this, RankItemActivity.class, "RankItemActivity", date, name, 2);
		}		
	}
	
	// 日期按钮
	private class BtnRankSelectmonthClickImpl implements OnClickListener {
		@Override
		public void onClick(View view) {
			String[] d = date.split("-");
			Dialog dialog = new MyDatePickerDialog(RankActivity.this, new DatePickerDialog.OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					date = UtilityHelper.formatDate(year + "-" + (monthOfYear+1) + "-" + dayOfMonth, "date");

					UtilityHelper.jumpActivity(RankActivity.this, RankActivity.class, "RankActivity", date, 2);
				}
			}, Integer.parseInt(d[0]), Integer.parseInt(d[1]) - 1, Integer.parseInt(d[2]));
			dialog.show();
		}
	}

	// 返回键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			UtilityHelper.jumpActivity(RankActivity.this, DayActivity.class, "DayActivity", date, 0);
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2, float arg3) {
		// TODO Auto-generated method stub
		int fling_min_distance = 100;
		int fling_min_velocity = 200;
		
		if (arg0.getX() - arg1.getX() > fling_min_distance && Math.abs(arg2) > fling_min_velocity) {
			this.vf.setInAnimation(this.right_in);
			this.vf.setOutAnimation(this.left_out);
			this.vf.showNext();
		} 
		else if (arg1.getX() - arg0.getX() > fling_min_distance && Math.abs(arg2) > fling_min_velocity) {
			this.vf.setInAnimation(this.left_in);
			this.vf.setOutAnimation(this.right_out);
			this.vf.showNext();
		}
		
		return false;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2, float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		return this.gd.onTouchEvent(arg1);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		this.gd.onTouchEvent(ev);
		return super.dispatchTouchEvent(ev);
	}
	
}
