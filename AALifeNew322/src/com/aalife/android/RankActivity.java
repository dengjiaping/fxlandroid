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
	private String strTitle = "";
	
	private ListView listRankCat = null;
	private ListView listRankCount = null;
	private ListView listRankPrice = null;
	private ListView listRankDate = null;
	private List<Map<String, String>> list = null;
	private SimpleAdapter adapter = null;
	private SQLiteOpenHelper sqlHelper = null;
	private ItemTableAccess itemAccess = null;
	private String curDate = "";
	private TextView tvNavCat = null;
	private TextView tvNavCount = null;
	private TextView tvNavPrice = null;
	private TextView tvNavDate = null;
	private TextView tvTitleRank = null;
	private LinearLayout layNoItemCat = null;
	private LinearLayout layNoItemCount = null;
	private LinearLayout layNoItemPrice = null;
	private LinearLayout layNoItemDate = null;
	private final int FIRST_REQUEST_CODE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rank);

		//数据库
		sqlHelper = new DatabaseHelper(this);
		
		//定义ViewPager
		viewPagerList = new ArrayList<View>();
		mInflater = getLayoutInflater();
		layRankCat = mInflater.inflate(R.layout.rank_category, new LinearLayout(this), false);
		layRankCount = mInflater.inflate(R.layout.rank_count, new LinearLayout(this), false);
		layRankPrice = mInflater.inflate(R.layout.rank_price, new LinearLayout(this), false);
		layRankDate = mInflater.inflate(R.layout.rank_date, new LinearLayout(this), false);
		
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
		curDate = UtilityHelper.getCurDate();
		tvTitleRank = (TextView) super.findViewById(R.id.tv_title_rank);
		strTitle = getString(R.string.txt_tab_rankcat);
		layNoItemCat = (LinearLayout) layRankCat.findViewById(R.id.lay_noitem);
		layNoItemCount = (LinearLayout) layRankCount.findViewById(R.id.lay_noitem);
		layNoItemPrice = (LinearLayout) layRankPrice.findViewById(R.id.lay_noitem);
		layNoItemDate = (LinearLayout) layRankDate.findViewById(R.id.lay_noitem);
		layNoItemCat.setVisibility(View.GONE);
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
		        intent.putExtra("date", curDate);
		        startActivityForResult(intent, FIRST_REQUEST_CODE);
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
		        intent.putExtra("date", curDate);
		        startActivityForResult(intent, FIRST_REQUEST_CODE);
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
		        
		        TextView tvItemName = (TextView) view.findViewById(R.id.tv_rank_itemname);
		        tvItemName.setBackgroundColor(RankActivity.this.getResources().getColor(R.color.color_tran_main));
		        TextView tvItemBuyDate = (TextView) view.findViewById(R.id.tv_rank_itembuydate);
		        tvItemBuyDate.setBackgroundColor(RankActivity.this.getResources().getColor(R.color.color_tran_main));
		        TextView tvItemPrice = (TextView) view.findViewById(R.id.tv_rank_itemprice);
		        tvItemPrice.setBackgroundColor(RankActivity.this.getResources().getColor(R.color.color_tran_main));
		        
		        Intent intent = new Intent(RankActivity.this, DayDetailActivity.class);
		        intent.putExtra("date", date);
		        startActivityForResult(intent, FIRST_REQUEST_CODE);
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
		        
		        TextView tvId = (TextView) view.findViewById(R.id.tv_rank_id);
		        tvId.setBackgroundColor(RankActivity.this.getResources().getColor(R.color.color_tran_main));
		        TextView tvItemBuyDate = (TextView) view.findViewById(R.id.tv_rank_itembuydate);
		        tvItemBuyDate.setBackgroundColor(RankActivity.this.getResources().getColor(R.color.color_tran_main));
		        TextView tvPrice = (TextView) view.findViewById(R.id.tv_rank_price);
		        tvPrice.setBackgroundColor(RankActivity.this.getResources().getColor(R.color.color_tran_main));
		        
		        Intent intent = new Intent(RankActivity.this, DayDetailActivity.class);
		        intent.putExtra("date", date);
		        startActivityForResult(intent, FIRST_REQUEST_CODE);
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
		ImageButton btnTitleBack = (ImageButton) super.findViewById(R.id.btn_title_back);
		btnTitleBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				RankActivity.this.finish();
			}			
		});
		
		//日期按钮
		ImageButton btnTitleDate = (ImageButton) super.findViewById(R.id.btn_title_date);
		btnTitleDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				String[] array = curDate.split("-");
				DatePickerDialog dateDialog = new DatePickerDialog(RankActivity.this, new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int month, int day) {
						String date = UtilityHelper.formatDate(year + "-" + (month + 1) + "-" + day, "");
						setListData(date);
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
		tvNavCat.setBackgroundResource(R.drawable.nav_border_cur);
		int padding = RankActivity.this.getResources().getDimensionPixelSize(R.dimen.title_text_padding);
		tvNavCat.setPadding(0, padding, 0, padding);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageScrollStateChanged(int arg0) {

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageSelected(int arg0) {
				int pad = RankActivity.this.getResources().getDimensionPixelSize(R.dimen.title_text_padding);
				tvNavCat.setTextColor(RankActivity.this.getResources().getColor(android.R.color.black));
				tvNavCat.setBackgroundResource(R.drawable.nav_border_sub);
				tvNavCat.setPadding(0, pad, 0, pad);
				tvNavCount.setTextColor(RankActivity.this.getResources().getColor(android.R.color.black));
				tvNavCount.setBackgroundResource(R.drawable.nav_border_sub);
				tvNavCount.setPadding(0, pad, 0, pad);
				tvNavPrice.setTextColor(RankActivity.this.getResources().getColor(android.R.color.black));
				tvNavPrice.setBackgroundResource(R.drawable.nav_border_sub);
				tvNavPrice.setPadding(0, pad, 0, pad);
				tvNavDate.setTextColor(RankActivity.this.getResources().getColor(android.R.color.black));
				tvNavDate.setBackgroundResource(R.drawable.nav_border_sub);
				tvNavDate.setPadding(0, pad, 0, pad);
				switch(arg0) {
				case 0:
					tvNavCat.setTextColor(RankActivity.this.getResources().getColor(R.color.color_back_main));
					tvNavCat.setBackgroundResource(R.drawable.nav_border_cur);
					tvNavCat.setPadding(0, pad, 0, pad);
					
					tvTitleRank.setText(getString(R.string.txt_tab_rankcat) + "(" + UtilityHelper.formatDate(curDate, "ys-m") + ")");
					strTitle = getString(R.string.txt_tab_rankcat);
					
					break;
				case 1:
					tvNavCount.setTextColor(RankActivity.this.getResources().getColor(R.color.color_back_main));
					tvNavCount.setBackgroundResource(R.drawable.nav_border_cur);
					tvNavCount.setPadding(0, pad, 0, pad);

					tvTitleRank.setText(getString(R.string.txt_tab_rankcount) + "(" + UtilityHelper.formatDate(curDate, "ys-m") + ")");
					strTitle = getString(R.string.txt_tab_rankcount);
					
					break;
				case 2:
					tvNavPrice.setTextColor(RankActivity.this.getResources().getColor(R.color.color_back_main));
					tvNavPrice.setBackgroundResource(R.drawable.nav_border_cur);
					tvNavPrice.setPadding(0, pad, 0, pad);

					tvTitleRank.setText(getString(R.string.txt_tab_rankprice) + "(" + UtilityHelper.formatDate(curDate, "ys-m") + ")");
					strTitle = getString(R.string.txt_tab_rankprice);
					
					break;
				case 3:
					tvNavDate.setTextColor(RankActivity.this.getResources().getColor(R.color.color_back_main));
					tvNavDate.setBackgroundResource(R.drawable.nav_border_cur);
					tvNavDate.setPadding(0, pad, 0, pad);
					
					tvTitleRank.setText(getString(R.string.txt_tab_rankdate) + "(" + UtilityHelper.formatDate(curDate, "ys-m") + ")");
					strTitle = getString(R.string.txt_tab_rankdate);
					
					break;
				}
			}			
		});
		
		setListData(curDate);
	}
	
	//设置ListView	
	protected void setListData(String date) {
		curDate = date;
		tvTitleRank.setText(strTitle + "(" + UtilityHelper.formatDate(curDate, "ys-m") + ")");
		itemAccess = new ItemTableAccess(sqlHelper.getReadableDatabase());
		
		//分类
		list = itemAccess.findRankCatByDate(date);
		adapter = new SimpleAdapter(this, list, R.layout.list_rankcat, new String[] { "id", "catname", "price" }, new int[] { R.id.tv_rank_id, R.id.tv_rank_catname, R.id.tv_rank_price });
		listRankCat.setAdapter(adapter);
		if(list.size() <= 0) {
			layNoItemCat.setVisibility(View.VISIBLE);
		} else {
			layNoItemCat.setVisibility(View.GONE);
		}
		
		//次数
		list = itemAccess.findRankCountByDate(date);
		adapter = new SimpleAdapter(this, list, R.layout.list_rankcount, new String[] { "itemname", "count", "price" }, new int[] { R.id.tv_rank_itemname, R.id.tv_rank_count, R.id.tv_rank_price });
		listRankCount.setAdapter(adapter);
		if(list.size() <= 0) {
			layNoItemCount.setVisibility(View.VISIBLE);
		} else {
			layNoItemCount.setVisibility(View.GONE);
		}
		
		//单价
		list = itemAccess.findRankPriceByDate(date);
		adapter = new SimpleAdapter(this, list, R.layout.list_rankprice, new String[] { "itemname", "itembuydate", "itemprice" }, new int[] { R.id.tv_rank_itemname, R.id.tv_rank_itembuydate, R.id.tv_rank_itemprice });
		listRankPrice.setAdapter(adapter);
		if(list.size() <= 0) {
			layNoItemPrice.setVisibility(View.VISIBLE);
		} else {
			layNoItemPrice.setVisibility(View.GONE);
		}
		
		//日期
		list = itemAccess.findRankDateByDate(date);
		adapter = new SimpleAdapter(this, list, R.layout.list_rankdate, new String[] { "id", "itembuydate", "price" }, new int[] { R.id.tv_rank_id, R.id.tv_rank_itembuydate, R.id.tv_rank_price });
		listRankDate.setAdapter(adapter);
		if(list.size() <= 0) {
			layNoItemDate.setVisibility(View.VISIBLE);
		} else {
			layNoItemDate.setVisibility(View.GONE);
		}
		
		itemAccess.close();
	}
	
	//返回处理
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == FIRST_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
			setListData(curDate);
		}
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
