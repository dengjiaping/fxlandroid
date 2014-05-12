package com.aalife.android;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class RankActivity extends Activity {
	private ViewPager viewPager = null;
	private ViewPagerAdapter viewPagerAdapter = null;
	private LayoutInflater mInflater = null;
	private List<View> viewPagerList = null;
	private View layRankCat = null;
	private View layRankCount = null;
	private View layRankPrice = null;
	private View layRankDate = null;
	
	private ListView listRankCat = null;
	private ListView listRankCount = null;
	private ListView listRankPrice = null;
	private ListView listRankDate = null;
	private List<Map<String, String>> list = null;
	private SimpleAdapter adapter = null;
	private SQLiteOpenHelper sqlHelper = null;
	private ItemTableAccess itemAccess = null;
	private String curDate = "";
	private SharedHelper sharedHelper = null;
	private ImageButton btnTitleBack = null;
	private ImageButton btnTitleDate = null;
	private TextView tvNavCat = null;
	private TextView tvNavCount = null;
	private TextView tvNavPrice = null;
	private TextView tvNavDate = null;
	private TextView tvTitleRank = null;
	private LinearLayout layNoItemCount = null;
	private LinearLayout layNoItemPrice = null;
	private LinearLayout layNoItemDate = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rank);

		//数据库
		sqlHelper = new DatabaseHelper(this);
		
		//定义ViewPager
		viewPagerList = new ArrayList<View>();
		mInflater = getLayoutInflater();
		layRankCat = mInflater.inflate(R.layout.rank_category, null);
		layRankCount = mInflater.inflate(R.layout.rank_count, null);
		layRankPrice = mInflater.inflate(R.layout.rank_price, null);
		layRankDate = mInflater.inflate(R.layout.rank_date, null);
		
        viewPagerList.add(layRankCat);
        viewPagerList.add(layRankCount);
        viewPagerList.add(layRankPrice);
        viewPagerList.add(layRankDate);

		viewPager = (ViewPager) super.findViewById(R.id.viewPager);
        
		//标题变粗
		TextPaint textPaint = null;
		TextView tvTitleId = (TextView) layRankCat.findViewById(R.id.tv_title_id);
		textPaint = tvTitleId.getPaint();
		textPaint.setFakeBoldText(true);
		TextView tvTitleCatName = (TextView) layRankCat.findViewById(R.id.tv_title_catname);
		textPaint = tvTitleCatName.getPaint();
		textPaint.setFakeBoldText(true);
		TextView tvTitlePrice = (TextView) layRankCat.findViewById(R.id.tv_title_price);
		textPaint = tvTitlePrice.getPaint();
		textPaint.setFakeBoldText(true);

		TextView tvTitleItemName = (TextView) layRankCount.findViewById(R.id.tv_title_itemname);
		textPaint = tvTitleItemName.getPaint();
		textPaint.setFakeBoldText(true);
		TextView tvTitleCount = (TextView) layRankCount.findViewById(R.id.tv_title_count);
		textPaint = tvTitleCount.getPaint();
		textPaint.setFakeBoldText(true);
		tvTitlePrice = (TextView) layRankCount.findViewById(R.id.tv_title_price);
		textPaint = tvTitlePrice.getPaint();
		textPaint.setFakeBoldText(true);
		
		tvTitleItemName = (TextView) layRankPrice.findViewById(R.id.tv_title_itemname);
		textPaint = tvTitleItemName.getPaint();
		textPaint.setFakeBoldText(true);
		TextView tvTitleItemBuyDate = (TextView) layRankPrice.findViewById(R.id.tv_title_itembuydate);
		textPaint = tvTitleItemBuyDate.getPaint();
		textPaint.setFakeBoldText(true);
		TextView tvTitleItemPrice = (TextView) layRankPrice.findViewById(R.id.tv_title_itemprice);
		textPaint = tvTitleItemPrice.getPaint();
		textPaint.setFakeBoldText(true);
		
		tvTitleId = (TextView) layRankDate.findViewById(R.id.tv_title_id);
		textPaint = tvTitleId.getPaint();
		textPaint.setFakeBoldText(true);
		tvTitleItemBuyDate = (TextView) layRankDate.findViewById(R.id.tv_title_itembuydate);
		textPaint = tvTitleItemBuyDate.getPaint();
		textPaint.setFakeBoldText(true);
		tvTitlePrice = (TextView) layRankDate.findViewById(R.id.tv_title_price);
		textPaint = tvTitlePrice.getPaint();
		textPaint.setFakeBoldText(true);
		
        //初始化
		sharedHelper = new SharedHelper(this);
		tvTitleRank = (TextView) super.findViewById(R.id.tv_title_rank);
		layNoItemCount = (LinearLayout) layRankCount.findViewById(R.id.lay_noitem);
		layNoItemPrice = (LinearLayout) layRankPrice.findViewById(R.id.lay_noitem);
		layNoItemDate = (LinearLayout) layRankDate.findViewById(R.id.lay_noitem);
		layNoItemCount.setVisibility(View.GONE);
		layNoItemPrice.setVisibility(View.GONE);
		layNoItemDate.setVisibility(View.GONE);
		
        //定义分类排行ListView
		listRankCat = (ListView) layRankCat.findViewById(R.id.list_rankcat);
		listRankCat.setDivider(null);		
		listRankCat.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ListView lv = (ListView) parent;
		        @SuppressWarnings("unchecked")
				Map<String, String> map = (Map<String, String>) lv.getItemAtPosition(position);
		        int catId = Integer.parseInt(map.get("catid"));
		        
		        TextView tvId = (TextView) view.findViewById(R.id.tv_rank_id);
		        tvId.setBackgroundColor(RankActivity.this.getResources().getColor(R.color.color_tran_main));
		        TextView tvCatName = (TextView) view.findViewById(R.id.tv_rank_catname);
		        tvCatName.setBackgroundColor(RankActivity.this.getResources().getColor(R.color.color_tran_main));
		        TextView tvPrice = (TextView) view.findViewById(R.id.tv_rank_price);
		        tvPrice.setBackgroundColor(RankActivity.this.getResources().getColor(R.color.color_tran_main));
		        
		        Intent intent = new Intent(RankActivity.this, RankCatDetailActivity.class);
		        intent.putExtra("catid", catId);
		        startActivity(intent);
			}			
		});
		
		//定义次数排行ListView
		listRankCount = (ListView) layRankCount.findViewById(R.id.list_rankcount);
		listRankCount.setDivider(null);		
		listRankCount.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ListView lv = (ListView) parent;
		        @SuppressWarnings("unchecked")
				Map<String, String> map = (Map<String, String>) lv.getItemAtPosition(position);
		        String itemName = map.get("itemname");
		        
		        TextView tvItemName = (TextView) view.findViewById(R.id.tv_rank_itemname);
		        tvItemName.setBackgroundColor(RankActivity.this.getResources().getColor(R.color.color_tran_main));
		        TextView tvCount = (TextView) view.findViewById(R.id.tv_rank_count);
		        tvCount.setBackgroundColor(RankActivity.this.getResources().getColor(R.color.color_tran_main));
		        TextView tvPrice = (TextView) view.findViewById(R.id.tv_rank_price);
		        tvPrice.setBackgroundColor(RankActivity.this.getResources().getColor(R.color.color_tran_main));
		        
		        Intent intent = new Intent(RankActivity.this, RankCountDetailActivity.class);
		        intent.putExtra("itemname", itemName);
		        startActivity(intent);
			}			
		});
		
		//定义单价排行ListView
		listRankPrice = (ListView) layRankPrice.findViewById(R.id.list_rankprice);
		listRankPrice.setDivider(null);		
		listRankPrice.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ListView lv = (ListView) parent;
		        @SuppressWarnings("unchecked")
				Map<String, String> map = (Map<String, String>) lv.getItemAtPosition(position);
		        String date = map.get("datevalue");
		        sharedHelper.setDate(date);
		        
		        TextView tvItemName = (TextView) view.findViewById(R.id.tv_rank_itemname);
		        tvItemName.setBackgroundColor(RankActivity.this.getResources().getColor(R.color.color_tran_main));
		        TextView tvItemBuyDate = (TextView) view.findViewById(R.id.tv_rank_itembuydate);
		        tvItemBuyDate.setBackgroundColor(RankActivity.this.getResources().getColor(R.color.color_tran_main));
		        TextView tvItemPrice = (TextView) view.findViewById(R.id.tv_rank_itemprice);
		        tvItemPrice.setBackgroundColor(RankActivity.this.getResources().getColor(R.color.color_tran_main));
		        
		        Intent intent = new Intent(RankActivity.this, DayDetailActivity.class);
		        startActivity(intent);
			}			
		});
		
		//定义日期排行ListView
		listRankDate = (ListView) layRankDate.findViewById(R.id.list_rankdate);
		listRankDate.setDivider(null);		
		listRankDate.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ListView lv = (ListView) parent;
		        @SuppressWarnings("unchecked")
				Map<String, String> map = (Map<String, String>) lv.getItemAtPosition(position);
		        String date = map.get("datevalue");
		        sharedHelper.setDate(date);
		        
		        TextView tvId = (TextView) view.findViewById(R.id.tv_rank_id);
		        tvId.setBackgroundColor(RankActivity.this.getResources().getColor(R.color.color_tran_main));
		        TextView tvItemBuyDate = (TextView) view.findViewById(R.id.tv_rank_itembuydate);
		        tvItemBuyDate.setBackgroundColor(RankActivity.this.getResources().getColor(R.color.color_tran_main));
		        TextView tvPrice = (TextView) view.findViewById(R.id.tv_rank_price);
		        tvPrice.setBackgroundColor(RankActivity.this.getResources().getColor(R.color.color_tran_main));
		        
		        Intent intent = new Intent(RankActivity.this, DayDetailActivity.class);
		        startActivity(intent);
			}			
		});
		
		//分类导航
		tvNavCat = (TextView) super.findViewById(R.id.tv_nav_cat);
		tvNavCat.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				viewPager.setCurrentItem(0);
			}
		});
		
		//次数导航
		tvNavCount = (TextView) super.findViewById(R.id.tv_nav_count);
		tvNavCount.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				viewPager.setCurrentItem(1);
			}
		});

		//单价导航
		tvNavPrice = (TextView) super.findViewById(R.id.tv_nav_price);
		tvNavPrice.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				viewPager.setCurrentItem(2);
			}
		});
		
		//日期导航
		tvNavDate = (TextView) super.findViewById(R.id.tv_nav_date);
		tvNavDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				viewPager.setCurrentItem(3);
			}
		});

		//返回按钮
		btnTitleBack = (ImageButton) super.findViewById(R.id.btn_title_back);
		btnTitleBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				RankActivity.this.finish();
			}			
		});
		
		//日期按钮
		btnTitleDate = (ImageButton) super.findViewById(R.id.btn_title_date);
		btnTitleDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				String[] array = curDate.split("-");
				DatePickerDialog dateDialog = new DatePickerDialog(RankActivity.this, new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int month, int day) {
						String date = UtilityHelper.formatDate(year + "-" + (month + 1) + "-" + day, "");
						sharedHelper.setDate(date);
						setListData();
					}					
				}, Integer.parseInt(array[0]), Integer.parseInt(array[1]) - 1, Integer.parseInt(array[2]));
				dateDialog.show();
			}		
		});
		
		//页面滑动事件
        viewPagerAdapter = new ViewPagerAdapter();
		viewPager.setAdapter(viewPagerAdapter);	
        viewPager.setCurrentItem(0);        
		tvNavCat.setTextColor(this.getResources().getColor(R.color.color_back_main));
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageScrollStateChanged(int arg0) {

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageSelected(int arg0) {
				tvNavCat.setTextColor(RankActivity.this.getResources().getColor(android.R.color.black));
				tvNavCount.setTextColor(RankActivity.this.getResources().getColor(android.R.color.black));
				tvNavPrice.setTextColor(RankActivity.this.getResources().getColor(android.R.color.black));
				tvNavDate.setTextColor(RankActivity.this.getResources().getColor(android.R.color.black));
				switch(arg0) {
				case 0:
					tvNavCat.setTextColor(RankActivity.this.getResources().getColor(R.color.color_back_main));
					break;
				case 1:
					tvNavCount.setTextColor(RankActivity.this.getResources().getColor(R.color.color_back_main));
					break;
				case 2:
					tvNavPrice.setTextColor(RankActivity.this.getResources().getColor(R.color.color_back_main));
					break;
				case 3:
					tvNavDate.setTextColor(RankActivity.this.getResources().getColor(R.color.color_back_main));
					break;
				}
			}			
		});
	}
	
	//设置ListView	
	protected void setListData() {
		curDate = sharedHelper.getDate();
		tvTitleRank.setText(getString(R.string.txt_title_rank) + "(" + UtilityHelper.formatDate(curDate, "ys-m") + ")");
		itemAccess = new ItemTableAccess(sqlHelper.getReadableDatabase());
		
		//分类
		list = itemAccess.findRankCatByDate(curDate);
		adapter = new SimpleAdapter(this, list, R.layout.list_rankcat, new String[] { "id", "catname", "price" }, new int[] { R.id.tv_rank_id, R.id.tv_rank_catname, R.id.tv_rank_price });
		listRankCat.setAdapter(adapter);
		
		//次数
		list = itemAccess.findRankCountByDate(curDate);
		adapter = new SimpleAdapter(this, list, R.layout.list_rankcount, new String[] { "itemname", "count", "price" }, new int[] { R.id.tv_rank_itemname, R.id.tv_rank_count, R.id.tv_rank_price });
		listRankCount.setAdapter(adapter);
		if(list.size() <= 0)
			layNoItemCount.setVisibility(View.VISIBLE);
		
		//单价
		list = itemAccess.findRankPriceByDate(curDate);
		adapter = new SimpleAdapter(this, list, R.layout.list_rankprice, new String[] { "itemname", "itembuydate", "itemprice" }, new int[] { R.id.tv_rank_itemname, R.id.tv_rank_itembuydate, R.id.tv_rank_itemprice });
		listRankPrice.setAdapter(adapter);
		if(list.size() <= 0)
			layNoItemPrice.setVisibility(View.VISIBLE);
		
		//日期
		list = itemAccess.findRankDateByDate(curDate);
		adapter = new SimpleAdapter(this, list, R.layout.list_rankdate, new String[] { "id", "itembuydate", "price" }, new int[] { R.id.tv_rank_id, R.id.tv_rank_itembuydate, R.id.tv_rank_price });
		listRankDate.setAdapter(adapter);
		if(list.size() <= 0)
			layNoItemDate.setVisibility(View.VISIBLE);
		
		itemAccess.close();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		setListData();
	}
	
	private class ViewPagerAdapter extends PagerAdapter {

		@Override
		public void destroyItem(ViewGroup arg0, int arg1, Object arg2) {
			// TODO Auto-generated method stub
			((ViewPager) arg0).removeView(viewPagerList.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public Object instantiateItem(ViewGroup arg0, int arg1) {
			// TODO Auto-generated method stub
			((ViewPager) arg0).addView(viewPagerList.get(arg1),0);
			return viewPagerList.get(arg1);
		}

		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
			// TODO Auto-generated method stub
		}

		@Override
		public void startUpdate(View arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return viewPagerList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0==(arg1);
		}
		
	}
}
