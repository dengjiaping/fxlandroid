package com.aalife.android;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class AnalyzeActivity extends Activity {
	private ViewPager viewPager = null;
	private ViewPagerAdapter viewPagerAdapter = null;
	private LayoutInflater mInflater = null;
	private List<View> viewPagerList = null;
	private View layAnalyzeCompare = null;
	private View layAnalyzeRecommend = null;
	private View layAnalyzeSearch = null;
	private View layAnalyzeTongji = null;
	private ImageButton btnTitleDate = null;
	private ImageButton btnTitleAdd = null;
	private ImageButton btnTitleRefresh = null;
	private EditText etTitleKey = null;
	private ImageButton btnTitleSearch = null;
	private String key = "";
	
	private WebView webViewTongji = null;
	private ProgressBar webViewLoading = null;
	private int viewType = 0;
	
	private ListView listAnalyzeCompare = null;
	private ListView listAnalyzeRecommend = null;
	private ListView listAnalyzeSearch = null;
	private List<Map<String, String>> list = null;
	private SimpleAdapter adapter = null;
	private SQLiteOpenHelper sqlHelper = null;
	private ItemTableAccess itemAccess = null;
	private String curDate = "";
	private TextView tvNavCompare = null;
	private TextView tvNavRecommend = null;
	private TextView tvNavTongji = null;
	private TextView tvNavSearch = null;
	private TextView tvTitleAnalyze = null;
	private LinearLayout layNoItemCompare = null;
	private LinearLayout layNoItemRecommend = null;
	private LinearLayout layNoItemSearch = null;
	private final int FIRST_REQUEST_CODE = 1;
	private final int SECOND_REQUEST_CODE = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_analyze);

		//数据库
		sqlHelper = new DatabaseHelper(this);
		
		//定义ViewPager
		viewPagerList = new ArrayList<View>();
		mInflater = getLayoutInflater();
		layAnalyzeCompare = mInflater.inflate(R.layout.analyze_compare, new LinearLayout(this), false);
		layAnalyzeRecommend = mInflater.inflate(R.layout.analyze_recommend, new LinearLayout(this), false);
		layAnalyzeTongji = mInflater.inflate(R.layout.analyze_tongji, new LinearLayout(this), false);
		layAnalyzeSearch = mInflater.inflate(R.layout.analyze_search, new LinearLayout(this), false);
				
        viewPagerList.add(layAnalyzeCompare);
        viewPagerList.add(layAnalyzeRecommend);
        viewPagerList.add(layAnalyzeTongji);
        viewPagerList.add(layAnalyzeSearch);

		viewPager = (ViewPager) super.findViewById(R.id.viewPager);
        
		//标题变粗
		TextPaint textPaint = null;
		TextView tvTitleCatName = (TextView) layAnalyzeCompare.findViewById(R.id.tv_title_catname);
		textPaint = tvTitleCatName.getPaint();
		textPaint.setFakeBoldText(true);
		TextView tvTitlePriceCur = (TextView) layAnalyzeCompare.findViewById(R.id.tv_title_pricecur);
		textPaint = tvTitlePriceCur.getPaint();
		textPaint.setFakeBoldText(true);
		TextView tvTitlePricePrev = (TextView) layAnalyzeCompare.findViewById(R.id.tv_title_priceprev);
		textPaint = tvTitlePricePrev.getPaint();
		textPaint.setFakeBoldText(true);

		TextView tvTitleItemName = (TextView) layAnalyzeRecommend.findViewById(R.id.tv_title_itemname);
		textPaint = tvTitleItemName.getPaint();
		textPaint.setFakeBoldText(true);
		TextView tvTitleItemBuyDate = (TextView) layAnalyzeRecommend.findViewById(R.id.tv_title_itembuydate);
		textPaint = tvTitleItemBuyDate.getPaint();
		textPaint.setFakeBoldText(true);
		TextView tvTitleItemPrice = (TextView) layAnalyzeRecommend.findViewById(R.id.tv_title_itemprice);
		textPaint = tvTitleItemPrice.getPaint();
		textPaint.setFakeBoldText(true);
		
		tvTitleItemName = (TextView) layAnalyzeSearch.findViewById(R.id.tv_title_itemname);
		textPaint = tvTitleItemName.getPaint();
		textPaint.setFakeBoldText(true);
		tvTitleItemBuyDate = (TextView) layAnalyzeSearch.findViewById(R.id.tv_title_itembuydate);
		textPaint = tvTitleItemBuyDate.getPaint();
		textPaint.setFakeBoldText(true);
		tvTitleItemPrice = (TextView) layAnalyzeSearch.findViewById(R.id.tv_title_itemprice);
		textPaint = tvTitleItemPrice.getPaint();
		textPaint.setFakeBoldText(true);
		
        //初始化
		curDate = UtilityHelper.getCurDate();
		tvTitleAnalyze = (TextView) super.findViewById(R.id.tv_title_analyze);
		layNoItemCompare = (LinearLayout) layAnalyzeCompare.findViewById(R.id.lay_noitem);
		layNoItemRecommend = (LinearLayout) layAnalyzeRecommend.findViewById(R.id.lay_noitem);
		layNoItemSearch = (LinearLayout) layAnalyzeSearch.findViewById(R.id.lay_noitem);
		layNoItemCompare.setVisibility(View.GONE);
		layNoItemRecommend.setVisibility(View.GONE);
		layNoItemSearch.setVisibility(View.GONE);
		etTitleKey = (EditText) super.findViewById(R.id.et_title_key);
		
		//WebView
		DisplayMetrics dm = getResources().getDisplayMetrics();  
		int screenWidth = dm.widthPixels;
		int screenDpi = dm.densityDpi;
		if(screenDpi <= 160 && screenWidth > 480) {
			viewType = 1;
		}
		webViewLoading = (ProgressBar) layAnalyzeTongji.findViewById(R.id.webViewLoading);
		webViewTongji = (WebView) layAnalyzeTongji.findViewById(R.id.webViewTongji);
		setWebView();
		
        //定义分类比较ListView
		listAnalyzeCompare = (ListView) layAnalyzeCompare.findViewById(R.id.list_analyzecompare);
		listAnalyzeCompare.setDivider(null);		
		listAnalyzeCompare.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ListView lv = (ListView) parent;
				@SuppressWarnings("unchecked")
				Map<String, String> map = (Map<String, String>) lv.getItemAtPosition(position);
		        int catId = Integer.parseInt(map.get("catid"));
		        
		        TextView tvCatName = (TextView) view.findViewById(R.id.tv_analyze_catname);
		        tvCatName.setBackgroundColor(AnalyzeActivity.this.getResources().getColor(R.color.color_tran_main));
		        TextView tvPriceCur = (TextView) view.findViewById(R.id.tv_analyze_pricecur);
		        tvPriceCur.setBackgroundColor(AnalyzeActivity.this.getResources().getColor(R.color.color_tran_main));
		        TextView tvPricePrev = (TextView) view.findViewById(R.id.tv_analyze_priceprev);
		        tvPricePrev.setBackgroundColor(AnalyzeActivity.this.getResources().getColor(R.color.color_tran_main));
		        
		        Intent intent = new Intent(AnalyzeActivity.this, AnalyzeCompareDetailActivity.class);
		        intent.putExtra("catid", catId);
		        intent.putExtra("date", curDate);
		        startActivityForResult(intent, FIRST_REQUEST_CODE);
			}			
		});
		
		//定义推荐分析ListView
		listAnalyzeRecommend = (ListView) layAnalyzeRecommend.findViewById(R.id.list_analyzerecommend);
		listAnalyzeRecommend.setDivider(null);		
		listAnalyzeRecommend.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ListView lv = (ListView) parent;
				@SuppressWarnings("unchecked")
				Map<String, String> map = (Map<String, String>) lv.getItemAtPosition(position);
		        String date = map.get("datevalue");
		        
		        TextView tvItemName = (TextView) view.findViewById(R.id.tv_rank_itemname);
		        tvItemName.setBackgroundColor(AnalyzeActivity.this.getResources().getColor(R.color.color_tran_main));
		        TextView tvItemBuyDate = (TextView) view.findViewById(R.id.tv_rank_itembuydate);
		        tvItemBuyDate.setBackgroundColor(AnalyzeActivity.this.getResources().getColor(R.color.color_tran_main));
		        TextView tvItemPrice = (TextView) view.findViewById(R.id.tv_rank_itemprice);
		        tvItemPrice.setBackgroundColor(AnalyzeActivity.this.getResources().getColor(R.color.color_tran_main));
		        
		        Intent intent = new Intent(AnalyzeActivity.this, DayDetailActivity.class);
		        intent.putExtra("date", date);
		        startActivityForResult(intent, FIRST_REQUEST_CODE);
			}			
		});
		
		//定义搜索分析ListView
		listAnalyzeSearch = (ListView) layAnalyzeSearch.findViewById(R.id.list_analyzesearch);
		listAnalyzeSearch.setDivider(null);		
		listAnalyzeSearch.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ListView lv = (ListView) parent;
				@SuppressWarnings("unchecked")
				Map<String, String> map = (Map<String, String>) lv.getItemAtPosition(position);
		        String date = map.get("datevalue");
		        
		        TextView tvItemName = (TextView) view.findViewById(R.id.tv_rank_itemname);
		        tvItemName.setBackgroundColor(AnalyzeActivity.this.getResources().getColor(R.color.color_tran_main));
		        TextView tvItemBuyDate = (TextView) view.findViewById(R.id.tv_rank_itembuydate);
		        tvItemBuyDate.setBackgroundColor(AnalyzeActivity.this.getResources().getColor(R.color.color_tran_main));
		        TextView tvItemPrice = (TextView) view.findViewById(R.id.tv_rank_itemprice);
		        tvItemPrice.setBackgroundColor(AnalyzeActivity.this.getResources().getColor(R.color.color_tran_main));
		        
		        Intent intent = new Intent(AnalyzeActivity.this, DayDetailActivity.class);
		        intent.putExtra("date", date);
		        startActivityForResult(intent, SECOND_REQUEST_CODE);
			}			
		});
				
		//比较导航
		tvNavCompare = (TextView) super.findViewById(R.id.tv_nav_compare);
		tvNavCompare.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				viewPager.setCurrentItem(0);
			}
		});
		
		//推荐导航
		tvNavRecommend = (TextView) super.findViewById(R.id.tv_nav_recommend);
		tvNavRecommend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				viewPager.setCurrentItem(1);
			}
		});
		
		//统计导航
		tvNavTongji = (TextView) super.findViewById(R.id.tv_nav_tongji);
		tvNavTongji.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				viewPager.setCurrentItem(2);
			}
		});

		//搜索导航
		tvNavSearch = (TextView) super.findViewById(R.id.tv_nav_search);
		tvNavSearch.setOnClickListener(new OnClickListener() {
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
				AnalyzeActivity.this.finish();
			}			
		});
		
		//日期按钮
		btnTitleDate = (ImageButton) super.findViewById(R.id.btn_title_date);
		btnTitleDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				String[] array = curDate.split("-");
				DatePickerDialog dateDialog = new DatePickerDialog(AnalyzeActivity.this, new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int month, int day) {
						String date = UtilityHelper.formatDate(year + "-" + (month + 1) + "-" + day, "");
						setListData(date);
					}					
				}, Integer.parseInt(array[0]), Integer.parseInt(array[1]) - 1, Integer.parseInt(array[2]));
				dateDialog.show();
			}		
		});
		
		//添加按钮
		btnTitleAdd = (ImageButton) super.findViewById(R.id.btn_title_add);
		btnTitleAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(AnalyzeActivity.this, AddActivity.class);
				intent.putExtra("recommend", 1);
				startActivityForResult(intent, FIRST_REQUEST_CODE);
			}			
		});
		
		//刷新按钮
		btnTitleRefresh = (ImageButton) super.findViewById(R.id.btn_title_refresh);
		btnTitleRefresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				setWebView();
			}			
		});
		
		//搜索按钮
		btnTitleSearch = (ImageButton) super.findViewById(R.id.btn_title_search);
		btnTitleSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				key = etTitleKey.getText().toString().trim();
				key = key.replace("%", "");
				key = key.replace("'", "");
				if(key.equals("")) {
					etTitleKey.setText("");
					Toast.makeText(AnalyzeActivity.this, getString(R.string.txt_search_keyempty), Toast.LENGTH_SHORT).show();
					return;
				}
				
				setSearchData(key);
			}			
		});
		
		//页面滑动事件
        viewPagerAdapter = new ViewPagerAdapter();
		viewPager.setAdapter(viewPagerAdapter);	
        viewPager.setCurrentItem(0);        
        tvNavCompare.setTextColor(this.getResources().getColor(R.color.color_back_main));
        tvNavCompare.setBackgroundResource(R.drawable.nav_border_cur);
		int padding = AnalyzeActivity.this.getResources().getDimensionPixelSize(R.dimen.title_text_padding);
		tvNavCompare.setPadding(0, padding, 0, padding);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageScrollStateChanged(int arg0) {

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageSelected(int arg0) {
				int pad = AnalyzeActivity.this.getResources().getDimensionPixelSize(R.dimen.title_text_padding);
				tvNavCompare.setTextColor(AnalyzeActivity.this.getResources().getColor(android.R.color.black));
				tvNavCompare.setBackgroundResource(R.drawable.nav_border_sub);
				tvNavCompare.setPadding(0, pad, 0, pad);
				tvNavRecommend.setTextColor(AnalyzeActivity.this.getResources().getColor(android.R.color.black));
				tvNavRecommend.setBackgroundResource(R.drawable.nav_border_sub);
				tvNavRecommend.setPadding(0, pad, 0, pad);
				tvNavTongji.setTextColor(AnalyzeActivity.this.getResources().getColor(android.R.color.black));
				tvNavTongji.setBackgroundResource(R.drawable.nav_border_sub);
				tvNavTongji.setPadding(0, pad, 0, pad);
				tvNavSearch.setTextColor(AnalyzeActivity.this.getResources().getColor(android.R.color.black));
				tvNavSearch.setBackgroundResource(R.drawable.nav_border_sub);
				tvNavSearch.setPadding(0, pad, 0, pad);
				switch(arg0) {
				case 0:
					tvNavCompare.setTextColor(AnalyzeActivity.this.getResources().getColor(R.color.color_back_main));
					tvNavCompare.setBackgroundResource(R.drawable.nav_border_cur);
					tvNavCompare.setPadding(0, pad, 0, pad);
					
					tvTitleAnalyze.setText(getString(R.string.txt_tab_analyzecompare) + "(" + UtilityHelper.formatDate(curDate, "ys-m") + ")");
										
					btnTitleDate.setVisibility(View.VISIBLE);
					btnTitleAdd.setVisibility(View.GONE);
					btnTitleRefresh.setVisibility(View.GONE);
					tvTitleAnalyze.setVisibility(View.VISIBLE);
					etTitleKey.setVisibility(View.GONE);
					btnTitleSearch.setVisibility(View.GONE);
					
					break;
				case 1:
					tvNavRecommend.setTextColor(AnalyzeActivity.this.getResources().getColor(R.color.color_back_main));
					tvNavRecommend.setBackgroundResource(R.drawable.nav_border_cur);
					tvNavRecommend.setPadding(0, pad, 0, pad);
					
					tvTitleAnalyze.setText(getString(R.string.txt_tab_analyzerecommend));
					
					btnTitleDate.setVisibility(View.GONE);
					btnTitleAdd.setVisibility(View.VISIBLE);
					btnTitleRefresh.setVisibility(View.GONE);
					tvTitleAnalyze.setVisibility(View.VISIBLE);
					etTitleKey.setVisibility(View.GONE);
					btnTitleSearch.setVisibility(View.GONE);
					
					break;
				case 2:
					tvNavTongji.setTextColor(AnalyzeActivity.this.getResources().getColor(R.color.color_back_main));
					tvNavTongji.setBackgroundResource(R.drawable.nav_border_cur);
					tvNavTongji.setPadding(0, pad, 0, pad);
					
					tvTitleAnalyze.setText(getString(R.string.txt_tab_analyzetongji));
					
					btnTitleDate.setVisibility(View.GONE);
					btnTitleAdd.setVisibility(View.GONE);
					btnTitleRefresh.setVisibility(View.VISIBLE);
					tvTitleAnalyze.setVisibility(View.VISIBLE);
					etTitleKey.setVisibility(View.GONE);
					btnTitleSearch.setVisibility(View.GONE);
					
					break;
				case 3:
					tvNavSearch.setTextColor(AnalyzeActivity.this.getResources().getColor(R.color.color_back_main));
					tvNavSearch.setBackgroundResource(R.drawable.nav_border_cur);
					tvNavSearch.setPadding(0, pad, 0, pad);
					
					tvTitleAnalyze.setText(getString(R.string.txt_tab_analyzesearch));
					
					btnTitleDate.setVisibility(View.GONE);
					btnTitleAdd.setVisibility(View.GONE);
					btnTitleRefresh.setVisibility(View.GONE);
					tvTitleAnalyze.setVisibility(View.GONE);
					etTitleKey.setVisibility(View.VISIBLE);
					btnTitleSearch.setVisibility(View.VISIBLE);
					
					break;
				}
			}			
		});
		
		setListData(curDate);
		//setSearchData(key);
	}
	
	//加载WebView
	protected void setWebView() {
		webViewTongji.loadUrl(UtilityHelper.getTongJiURL(viewType));
		webViewTongji.setWebViewClient(new WebViewClient(){
			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				view.loadDataWithBaseURL(null, getString(R.string.txt_home_neterror), "text/html", "UTF-8", null);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				webViewLoading.setVisibility(ProgressBar.GONE);
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				webViewLoading.setVisibility(ProgressBar.VISIBLE);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}	
		});
	}
	
	//设置ListView	
	protected void setListData(String date) {
		curDate = date;
		tvTitleAnalyze.setText(getString(R.string.txt_tab_analyzecompare) + "(" + UtilityHelper.formatDate(curDate, "ys-m") + ")");
		itemAccess = new ItemTableAccess(sqlHelper.getReadableDatabase());
		
		//比较
		list = itemAccess.findCompareCatByDate(date);
		adapter = new SimpleAdapter(this, list, R.layout.list_analyzecompare, new String[] { "catid", "catname", "pricecur", "priceprev" }, new int[] { R.id.tv_analyze_catid, R.id.tv_analyze_catname, R.id.tv_analyze_pricecur, R.id.tv_analyze_priceprev });
		listAnalyzeCompare.setAdapter(adapter);
		if(list.size() <= 0) {
			layNoItemCompare.setVisibility(View.VISIBLE);
		} else {
			layNoItemCompare.setVisibility(View.GONE);
		}
		
		//推荐
		list = itemAccess.findAnalyzeRecommend();
		adapter = new SimpleAdapter(this, list, R.layout.list_rankprice, new String[] { "itemname", "itembuydate", "itemprice" }, new int[] { R.id.tv_rank_itemname, R.id.tv_rank_itembuydate, R.id.tv_rank_itemprice });
		listAnalyzeRecommend.setAdapter(adapter);
		if(list.size() <= 0) {
			layNoItemRecommend.setVisibility(View.VISIBLE);
		} else {
			layNoItemRecommend.setVisibility(View.GONE);
		}
		
		itemAccess.close();
	}
	
	//设置搜索Data	
	protected void setSearchData(String key) {		
		itemAccess = new ItemTableAccess(sqlHelper.getReadableDatabase());
		list = itemAccess.findItemByKey(key);
		itemAccess.close();
		adapter = new SimpleAdapter(this, list, R.layout.list_rankprice, new String[] { "itemname", "itembuydate", "itemprice" }, new int[] { R.id.tv_rank_itemname, R.id.tv_rank_itembuydate, R.id.tv_rank_itemprice });
		listAnalyzeSearch.setAdapter(adapter);

		//设置empty
		if(list.size() == 0) {
			layNoItemSearch.setVisibility(View.VISIBLE);
		} else {
			layNoItemSearch.setVisibility(View.GONE);
		}
	}
		
	//返回处理
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == FIRST_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
			setListData(curDate);
		} else if(requestCode == SECOND_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
			setSearchData(key);
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
