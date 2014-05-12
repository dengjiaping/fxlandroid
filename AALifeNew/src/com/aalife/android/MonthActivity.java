package com.aalife.android;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MonthActivity extends Activity {
	private ViewPager viewPager = null;
	private ViewPagerAdapter viewPagerAdapter = null;
	private LayoutInflater mInflater = null;
	private List<View> viewPagerList = null;
	private View layMonthPager = null;
	
	private HorizontalScrollView hsvMonthTitle = null;
	private GridView gvMonthTitle = null;
	private SimpleAdapter simpleAdapter = null;
	private int titleCount = 4;
	private int titleWidth = 0;
	private int screenWidth = 0;

	private ListView listMonth = null;
	private List<Map<String, String>> all = null;
	private List<Map<String, String>> list = null;
	private SimpleAdapter adapter = null;
	private SQLiteOpenHelper sqlHelper = null;
	private ItemTableAccess itemAccess = null;
	private String curDate = "";
	private int pagerPosition = 0;
	private TextView tvTotalPrice = null;
	private TextView tvTotalLabel = null;
	private TextPaint textPaint = null;
	private MyHandler myHandler = new MyHandler(this);
	private ProgressBar pbMonth = null;
	private LinearLayout layMonthTotal = null;
	private LinearLayout layNoItem = null;
	private final int FIRST_REQUEST_CODE = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_month);

		//数据库
		sqlHelper = new DatabaseHelper(this);
		
		//定义ViewPager
		viewPagerList = new ArrayList<View>();
		mInflater = getLayoutInflater();
		viewPager = (ViewPager) super.findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter();
        
        //水平ListView
        hsvMonthTitle = (HorizontalScrollView) super.findViewById(R.id.hsv_month_title);
        gvMonthTitle = (GridView) super.findViewById(R.id.gv_month_title);
        gvMonthTitle.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				viewPager.setCurrentItem(position);
			}        	
        });
		
		//初始化
		curDate = UtilityHelper.getCurDate();
		pbMonth = (ProgressBar) super.findViewById(R.id.pb_month);
		pbMonth.setVisibility(View.VISIBLE);
		layMonthTotal = (LinearLayout) super.findViewById(R.id.lay_month_total);
		layMonthTotal.setVisibility(View.GONE);
		layNoItem = (LinearLayout) super.findViewById(R.id.lay_noitem);
		layNoItem.setVisibility(View.GONE);	

		//设置TitleWidth
		DisplayMetrics dm = getResources().getDisplayMetrics();  
		screenWidth = dm.widthPixels;
        titleWidth = screenWidth / titleCount;
		
		//标题变粗
		tvTotalPrice = (TextView) super.findViewById(R.id.tv_total_price);
		textPaint = tvTotalPrice.getPaint();
		textPaint.setFakeBoldText(true);
        tvTotalLabel = (TextView) super.findViewById(R.id.tv_total_label);
		textPaint = tvTotalLabel.getPaint();
		textPaint.setFakeBoldText(true);
		
		//线程加载
		new Thread(new Runnable(){
			@Override
			public void run() {
				itemAccess = new ItemTableAccess(sqlHelper.getReadableDatabase());
				all = itemAccess.findAllMonth();
				itemAccess.close();
				
				boolean result = false;
				if(all.size() > 0) {
					result = true;
				}
				
				Bundle bundle = new Bundle();
				bundle.putBoolean("result", result);	
				Message message = new Message();
				message.what = 1;
				message.setData(bundle);
				myHandler.sendMessage(message);
			}
		}).start();        

		//页面滑动事件
        viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}

			@Override
			public void onPageSelected(int arg0) {
				Map<String, String> map = all.get(arg0);
				String price = map.get("price");
				String date = map.get("datevalue");
				tvTotalPrice.setText(price);
				curDate = date;
				
				pagerPosition = viewPager.getCurrentItem();

				int hsvX = (pagerPosition / titleCount) * screenWidth;
				hsvMonthTitle.smoothScrollTo(hsvX, 0);

				simpleAdapter.notifyDataSetChanged();
			}			
		});

		//返回按钮
        ImageButton btnTitleBack = (ImageButton) super.findViewById(R.id.btn_title_back);
		btnTitleBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				MonthActivity.this.close();
			}			
		});
		
		//添加按钮
		ImageButton btnTitleAdd = (ImageButton) super.findViewById(R.id.btn_title_add);
		btnTitleAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MonthActivity.this, AddActivity.class);
				intent.putExtra("date", curDate);
				startActivityForResult(intent, FIRST_REQUEST_CODE);
			}			
		});
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
			return arg0 == arg1;
		}
		
	}
	
	//关闭this
	protected void close() {
		this.finish();
	}

	//多线程处理
	static class MyHandler extends Handler {
		WeakReference<MonthActivity> myActivity = null;
		MyHandler(MonthActivity activity) {
			myActivity = new WeakReference<MonthActivity>(activity);
		}
		
		@Override
		public void handleMessage(Message msg) {
			final MonthActivity activity = myActivity.get();
			switch(msg.what) {
			case 1:
				boolean result = msg.getData().getBoolean("result");
				if(!result)
					activity.layNoItem.setVisibility(View.VISIBLE);
				else
					activity.layMonthTotal.setVisibility(View.VISIBLE);
				
				activity.pbMonth.setVisibility(View.GONE);
				
				//GridView数据源
				activity.simpleAdapter = new SimpleAdapter(activity, activity.all, R.layout.list_month_title, new String[] { "date" }, new int[] { R.id.tv_nav_main }) {
					@Override
					public View getView(int position, View convertView, ViewGroup parent) {
						View view = super.getView(position, convertView, parent);
						TextView tv = (TextView) view.findViewById(R.id.tv_nav_main);
						int padding = activity.getResources().getDimensionPixelSize(R.dimen.title_text_padding);
						if(position == activity.pagerPosition) {
							tv.setTextColor(activity.getResources().getColor(R.color.color_back_main));
							tv.setBackgroundResource(R.drawable.nav_border_cur);
							tv.setPadding(0, padding, 0, padding);
						} else {
							tv.setTextColor(activity.getResources().getColor(android.R.color.black));
							tv.setBackgroundResource(R.drawable.nav_border_sub);
							tv.setPadding(0, padding, 0, padding);
						}
						
						return view;
					}						
				};
				activity.gvMonthTitle.setAdapter(activity.simpleAdapter);
				int size = activity.all.size();  
				LayoutParams params = activity.gvMonthTitle.getLayoutParams();
				params.width = size * activity.titleWidth;
				if(params.width < activity.screenWidth) {
					params.width = activity.screenWidth;
				}
				activity.gvMonthTitle.setLayoutParams(params);
				activity.gvMonthTitle.setNumColumns(size);
				
				//主列表处理
				int index = 0;
		        boolean hasDate = false;
				Iterator<Map<String, String>> it = activity.all.iterator();
				while(it.hasNext()) {
					Map<String, String> map = it.next();
					String date = map.get("datevalue");
					String price = map.get("price");

					activity.layMonthPager = activity.mInflater.inflate(R.layout.month_pager, null);
					activity.viewPagerList.add(activity.layMonthPager);
			        
					//标题变粗
					TextPaint textPaint = null;
					TextView tvTitleId = (TextView) activity.layMonthPager.findViewById(R.id.tv_title_id);
					textPaint = tvTitleId.getPaint();
					textPaint.setFakeBoldText(true);
					TextView tvTitleDate = (TextView) activity.layMonthPager.findViewById(R.id.tv_title_date);
					textPaint = tvTitleDate.getPaint();
					textPaint.setFakeBoldText(true);
					TextView tvTitlePrice = (TextView) activity.layMonthPager.findViewById(R.id.tv_title_price);
					textPaint = tvTitlePrice.getPaint();
					textPaint.setFakeBoldText(true);
					
					activity.listMonth = (ListView) activity.layMonthPager.findViewById(R.id.list_month);
					activity.listMonth.setDivider(null);
					activity.itemAccess = new ItemTableAccess(activity.sqlHelper.getReadableDatabase());
					activity.list = activity.itemAccess.findMonthByDate(date);
					activity.itemAccess.close();
					activity.adapter = new SimpleAdapter(activity, activity.list, R.layout.list_month, new String[] { "id", "price", "date" }, new int[] { R.id.tv_month_id, R.id.tv_month_price, R.id.tv_month_date });
					activity.listMonth.setAdapter(activity.adapter);
					
					//列表点击
					activity.listMonth.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
							ListView lv = (ListView) parent;
					        @SuppressWarnings("unchecked")
							Map<String, String> map = (Map<String, String>) lv.getItemAtPosition(position);
					        String date = map.get("datevalue");
					        
					        TextView tvId = (TextView) view.findViewById(R.id.tv_month_id);
					        tvId.setBackgroundColor(activity.getResources().getColor(R.color.color_tran_main));
					        TextView tvDate = (TextView) view.findViewById(R.id.tv_month_date);
					        tvDate.setBackgroundColor(activity.getResources().getColor(R.color.color_tran_main));
					        TextView tvPrice = (TextView) view.findViewById(R.id.tv_month_price);
					        tvPrice.setBackgroundColor(activity.getResources().getColor(R.color.color_tran_main));
					        
					        Intent intent = new Intent(activity, DayDetailActivity.class);
					        intent.putExtra("date", date);
					        activity.startActivityForResult(intent, activity.FIRST_REQUEST_CODE);
						}			
					});
					
					if(UtilityHelper.formatDate(activity.curDate, "y-m").equals(UtilityHelper.formatDate(date, "y-m"))) {
						activity.pagerPosition = index;		        
						activity.tvTotalPrice.setText(price);
				        hasDate = true;
					}
					
					index ++;
				}

				if(!hasDate) {
					activity.curDate = UtilityHelper.getCurDate();
					activity.pagerPosition = activity.all.size();
				}
						
				activity.viewPager.setAdapter(activity.viewPagerAdapter);	
				activity.viewPager.setCurrentItem(activity.pagerPosition);

				//定位Title
				activity.myHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						int hsvX = (activity.pagerPosition / activity.titleCount) * activity.screenWidth;
						activity.hsvMonthTitle.smoothScrollTo(hsvX, 0);
					}
				}, 0);				
				
				break;
			}
		}			
	};
	
	//返回处理
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == FIRST_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
			this.onCreate(null);
		}
	}
}