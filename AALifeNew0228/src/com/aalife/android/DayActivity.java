package com.aalife.android;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

public class DayActivity extends Activity {
	private ListView lvDayList = null;
	private List<Map<String, String>> list = null;
	private SQLiteOpenHelper sqlHelper = null;
	private ItemTableAccess itemAccess = null;
	private ImageButton btnTitleBack = null;
	private ImageButton btnTitleAdd = null;
	private DayAdapter dayAdapter = null;
	private MyHandler myHandler = new MyHandler(this);
	private ProgressBar pbDay = null;
	private LinearLayout layNoItem = null;
	private String curDate = "";
	private int visibleLastIndex = 0;
	private boolean loading = false;
	private List<Map<String, String>> newList = null;
	private final int FIRST_REQUEST_CODE = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_day);
		
		//数据库
		sqlHelper = new DatabaseHelper(this);

		//初始化
		lvDayList = (ListView) super.findViewById(R.id.list_day);
		lvDayList.setDivider(null);
		lvDayList.setFocusable(false);
		pbDay = (ProgressBar) super.findViewById(R.id.pb_day);
		pbDay.setVisibility(View.VISIBLE);
		layNoItem = (LinearLayout) super.findViewById(R.id.lay_noitem);
		layNoItem.setVisibility(View.GONE);	
					
		//线程加载
		new Thread(new Runnable(){
			@Override
			public void run() {
				itemAccess = new ItemTableAccess(sqlHelper.getReadableDatabase());
				String lastDate = itemAccess.findLastDate();
				list = itemAccess.findAllDayFirstBuyDate(lastDate);
				curDate = lastDate;
				itemAccess.close();

				boolean result = false;
				if(list.size() > 0)
					result = true;
				
				Bundle bundle = new Bundle();
				bundle.putBoolean("result", result);	
				Message message = new Message();
				message.what = 1;
				message.setData(bundle);
				myHandler.sendMessage(message);
			}
		}).start(); 
		
		//列表滑动事件
		lvDayList.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				int itemsLastIndex = dayAdapter.getCount() - 1;
			    if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && visibleLastIndex == itemsLastIndex) { 
					if(!loading) {
				    	pbDay.setVisibility(View.VISIBLE);
						loading = true;
						
						new Thread(new Runnable(){
							@Override
							public void run() {
								itemAccess = new ItemTableAccess(sqlHelper.getReadableDatabase());
								String nextDate = itemAccess.findNextDate(curDate);
								newList = itemAccess.findAllDayBuyDate(nextDate);
								itemAccess.close();

								if(newList.size() > 0) {
									curDate = nextDate;
									Message message = new Message();
									message.what = 2;
									myHandler.sendMessage(message);
								} else {
									Message message = new Message();
									message.what = 3;
									myHandler.sendMessage(message);									
								}
							}
						}).start(); 
				    } 
				}
			}					
		});
		
		//返回按钮
		btnTitleBack = (ImageButton) super.findViewById(R.id.btn_title_back);
		btnTitleBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				DayActivity.this.close();
			}			
		});

		//添加按钮
		btnTitleAdd = (ImageButton) super.findViewById(R.id.btn_title_add);
		btnTitleAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(DayActivity.this, AddActivity.class);
				intent.putExtra("date", UtilityHelper.getCurDate());
				startActivityForResult(intent, FIRST_REQUEST_CODE);
			}			
		});
		
	}

	//关闭this
	protected void close() {
		this.finish();
	}
	
	//多线程处理
	static class MyHandler extends Handler {
		WeakReference<DayActivity> myActivity = null;
		MyHandler(DayActivity activity) {
			myActivity = new WeakReference<DayActivity>(activity);
		}
		
		@Override
		public void handleMessage(Message msg) {
			final DayActivity activity = myActivity.get();
			switch(msg.what) {
			case 1:
				boolean result = msg.getData().getBoolean("result");
				if(!result)
					activity.layNoItem.setVisibility(View.VISIBLE);
				
				activity.pbDay.setVisibility(View.GONE);
				
				activity.dayAdapter = new DayAdapter(activity, activity.list);
				activity.lvDayList.setAdapter(activity.dayAdapter);
								
				break;
			case 2:
				Iterator<Map<String, String>> it = activity.newList.iterator();
				while(it.hasNext()) {
					activity.list.add(it.next());
				}
				activity.dayAdapter.notifyDataSetChanged();
				
				activity.pbDay.setVisibility(View.GONE);
				activity.loading = false;
				
				break;
			case 3:
				activity.pbDay.setVisibility(View.GONE);
				
				break;
			}
		}			
	};	
	
	//返回处理
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == FIRST_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
			itemAccess = new ItemTableAccess(sqlHelper.getReadableDatabase());
			list = itemAccess.findAllDayFirstBuyDate(curDate);
			itemAccess.close();
			
			dayAdapter.updateData(list);
		}
	}
	
	//Adapter调用刷新
	public void refreshData() {
		dayAdapter.notifyDataSetChanged();
	}
}
