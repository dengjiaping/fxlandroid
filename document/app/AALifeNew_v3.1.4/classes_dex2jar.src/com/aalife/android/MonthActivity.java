package com.aalife.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MonthActivity
  extends Activity
{
  private final int FIRST_REQUEST_CODE = 1;
  private SimpleAdapter adapter = null;
  private List<Map<String, String>> all = null;
  private String curDate = "";
  private GridView gvMonthTitle = null;
  private HorizontalScrollView hsvMonthTitle = null;
  private ItemTableAccess itemAccess = null;
  private View layMonthPager = null;
  private LinearLayout layMonthTotal = null;
  private LinearLayout layNoItem = null;
  private List<Map<String, String>> list = null;
  private ListView listMonth = null;
  private LayoutInflater mInflater = null;
  private MyHandler myHandler = new MyHandler(this);
  private int pagerPosition = 0;
  private ProgressBar pbMonth = null;
  private int screenWidth = 0;
  private SharedHelper sharedHelper = null;
  private SimpleAdapter simpleAdapter = null;
  private SQLiteOpenHelper sqlHelper = null;
  private TextPaint textPaint = null;
  private int titleCount = 4;
  private int titleWidth = 0;
  private TextView tvTotalLabel = null;
  private TextView tvTotalPrice = null;
  private ViewPager viewPager = null;
  private ViewPagerAdapter viewPagerAdapter = null;
  private List<View> viewPagerList = null;
  
  protected void close()
  {
    finish();
  }
  
  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if ((paramInt1 == 1) && (paramInt2 == -1))
    {
      this.curDate = this.sharedHelper.getCurDate();
      onCreate(null);
    }
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903049);
    this.sqlHelper = new DatabaseHelper(this);
    this.viewPagerList = new ArrayList();
    this.mInflater = getLayoutInflater();
    this.viewPager = ((ViewPager)super.findViewById(2131296285));
    this.viewPagerAdapter = new ViewPagerAdapter(null);
    this.hsvMonthTitle = ((HorizontalScrollView)super.findViewById(2131296348));
    this.gvMonthTitle = ((GridView)super.findViewById(2131296349));
    this.gvMonthTitle.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
        MonthActivity.this.viewPager.setCurrentItem(paramAnonymousInt);
      }
    });
    this.sharedHelper = new SharedHelper(this);
    if (this.curDate.equals("")) {
      this.curDate = UtilityHelper.getCurDate();
    }
    this.pbMonth = ((ProgressBar)super.findViewById(2131296350));
    this.pbMonth.setVisibility(0);
    this.layMonthTotal = ((LinearLayout)super.findViewById(2131296351));
    this.layMonthTotal.setVisibility(8);
    this.layNoItem = ((LinearLayout)super.findViewById(2131296292));
    this.layNoItem.setVisibility(8);
    this.screenWidth = getResources().getDisplayMetrics().widthPixels;
    this.titleWidth = (this.screenWidth / this.titleCount);
    this.tvTotalPrice = ((TextView)super.findViewById(2131296308));
    this.textPaint = this.tvTotalPrice.getPaint();
    this.textPaint.setFakeBoldText(true);
    this.tvTotalLabel = ((TextView)super.findViewById(2131296307));
    this.textPaint = this.tvTotalLabel.getPaint();
    this.textPaint.setFakeBoldText(true);
    (new Runnable()
    {
      public void run()
      {
        MonthActivity.this.itemAccess = new ItemTableAccess(MonthActivity.this.sqlHelper.getReadableDatabase());
        MonthActivity.this.all = MonthActivity.this.itemAccess.findAllMonth();
        MonthActivity.this.itemAccess.close();
        int i = MonthActivity.this.all.size();
        boolean bool = false;
        if (i > 0) {
          bool = true;
        }
        Bundle localBundle = new Bundle();
        localBundle.putBoolean("result", bool);
        Message localMessage = new Message();
        localMessage.what = 1;
        localMessage.setData(localBundle);
        MonthActivity.this.myHandler.sendMessage(localMessage);
      }
    }).start();
    this.viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
    {
      public void onPageScrollStateChanged(int paramAnonymousInt) {}
      
      public void onPageScrolled(int paramAnonymousInt1, float paramAnonymousFloat, int paramAnonymousInt2) {}
      
      public void onPageSelected(int paramAnonymousInt)
      {
        Map localMap = (Map)MonthActivity.this.all.get(paramAnonymousInt);
        String str1 = (String)localMap.get("price");
        String str2 = (String)localMap.get("datevalue");
        MonthActivity.this.tvTotalPrice.setText(str1);
        MonthActivity.this.curDate = str2;
        MonthActivity.this.sharedHelper.setCurDate(str2);
        MonthActivity.this.pagerPosition = MonthActivity.this.viewPager.getCurrentItem();
        int i = MonthActivity.this.pagerPosition / MonthActivity.this.titleCount * MonthActivity.this.screenWidth;
        MonthActivity.this.hsvMonthTitle.smoothScrollTo(i, 0);
        MonthActivity.this.simpleAdapter.notifyDataSetChanged();
      }
    });
    ((ImageButton)super.findViewById(2131296257)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        MonthActivity.this.close();
      }
    }());
    ((ImageButton)super.findViewById(2131296279)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        Intent localIntent = new Intent(MonthActivity.this, AddActivity.class);
        localIntent.putExtra("date", MonthActivity.this.curDate);
        MonthActivity.this.startActivityForResult(localIntent, 1);
      }
    }());
  }
  
  static class MyHandler
    extends Handler
  {
    WeakReference<MonthActivity> myActivity = null;
    
    MyHandler(MonthActivity paramMonthActivity)
    {
      this.myActivity = new WeakReference(paramMonthActivity);
    }
    
    public void handleMessage(Message paramMessage)
    {
      final MonthActivity localMonthActivity = (MonthActivity)this.myActivity.get();
      switch (paramMessage.what)
      {
      default: 
        return;
      }
      int j;
      int k;
      Iterator localIterator;
      if (!paramMessage.getData().getBoolean("result"))
      {
        localMonthActivity.layNoItem.setVisibility(0);
        localMonthActivity.pbMonth.setVisibility(8);
        localMonthActivity.simpleAdapter = new SimpleAdapter(localMonthActivity, localMonthActivity.all, 2130903070, new String[] { "date" }, new int[] { 2131296311 })
        {
          public View getView(int paramAnonymousInt, View paramAnonymousView, ViewGroup paramAnonymousViewGroup)
          {
            View localView = super.getView(paramAnonymousInt, paramAnonymousView, paramAnonymousViewGroup);
            TextView localTextView = (TextView)localView.findViewById(2131296311);
            int i = localMonthActivity.getResources().getDimensionPixelSize(2131034112);
            if (paramAnonymousInt == localMonthActivity.pagerPosition)
            {
              localTextView.setTextColor(localMonthActivity.getResources().getColor(2130968583));
              localTextView.setBackgroundResource(2130837539);
              localTextView.setPadding(0, i, 0, i);
              return localView;
            }
            localTextView.setTextColor(localMonthActivity.getResources().getColor(17170444));
            localTextView.setBackgroundResource(2130837541);
            localTextView.setPadding(0, i, 0, i);
            return localView;
          }
        };
        localMonthActivity.gvMonthTitle.setAdapter(localMonthActivity.simpleAdapter);
        int i = localMonthActivity.all.size();
        ViewGroup.LayoutParams localLayoutParams = localMonthActivity.gvMonthTitle.getLayoutParams();
        localLayoutParams.width = (i * localMonthActivity.titleWidth);
        if (localLayoutParams.width < localMonthActivity.screenWidth) {
          localLayoutParams.width = localMonthActivity.screenWidth;
        }
        localMonthActivity.gvMonthTitle.setLayoutParams(localLayoutParams);
        localMonthActivity.gvMonthTitle.setNumColumns(i);
        j = 0;
        k = 0;
        localIterator = localMonthActivity.all.iterator();
      }
      for (;;)
      {
        if (!localIterator.hasNext())
        {
          if (k == 0)
          {
            localMonthActivity.curDate = UtilityHelper.getCurDate();
            localMonthActivity.pagerPosition = localMonthActivity.all.size();
          }
          localMonthActivity.viewPager.setAdapter(localMonthActivity.viewPagerAdapter);
          localMonthActivity.viewPager.setCurrentItem(localMonthActivity.pagerPosition);
          localMonthActivity.myHandler.postDelayed(new Runnable()
          {
            public void run()
            {
              int i = localMonthActivity.pagerPosition / localMonthActivity.titleCount * localMonthActivity.screenWidth;
              localMonthActivity.hsvMonthTitle.smoothScrollTo(i, 0);
            }
          }, 0L);
          return;
          localMonthActivity.layMonthTotal.setVisibility(0);
          break;
        }
        Map localMap = (Map)localIterator.next();
        String str1 = (String)localMap.get("datevalue");
        String str2 = (String)localMap.get("price");
        localMonthActivity.layMonthPager = localMonthActivity.mInflater.inflate(2130903075, null);
        localMonthActivity.viewPagerList.add(localMonthActivity.layMonthPager);
        ((TextView)localMonthActivity.layMonthPager.findViewById(2131296296)).getPaint().setFakeBoldText(true);
        ((TextView)localMonthActivity.layMonthPager.findViewById(2131296444)).getPaint().setFakeBoldText(true);
        ((TextView)localMonthActivity.layMonthPager.findViewById(2131296358)).getPaint().setFakeBoldText(true);
        localMonthActivity.listMonth = ((ListView)localMonthActivity.layMonthPager.findViewById(2131296445));
        localMonthActivity.listMonth.setDivider(null);
        localMonthActivity.itemAccess = new ItemTableAccess(localMonthActivity.sqlHelper.getReadableDatabase());
        localMonthActivity.list = localMonthActivity.itemAccess.findMonthByDate(str1);
        localMonthActivity.itemAccess.close();
        localMonthActivity.adapter = new SimpleAdapter(localMonthActivity, localMonthActivity.list, 2130903069, new String[] { "id", "price", "date" }, new int[] { 2131296434, 2131296436, 2131296435 });
        localMonthActivity.listMonth.setAdapter(localMonthActivity.adapter);
        localMonthActivity.listMonth.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
          public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
          {
            String str = (String)((Map)((ListView)paramAnonymousAdapterView).getItemAtPosition(paramAnonymousInt)).get("datevalue");
            ((TextView)paramAnonymousView.findViewById(2131296434)).setBackgroundColor(localMonthActivity.getResources().getColor(2130968577));
            ((TextView)paramAnonymousView.findViewById(2131296435)).setBackgroundColor(localMonthActivity.getResources().getColor(2130968577));
            ((TextView)paramAnonymousView.findViewById(2131296436)).setBackgroundColor(localMonthActivity.getResources().getColor(2130968577));
            Intent localIntent = new Intent(localMonthActivity, DayDetailActivity.class);
            localIntent.putExtra("date", str);
            MonthActivity localMonthActivity = localMonthActivity;
            localMonthActivity.getClass();
            localMonthActivity.startActivityForResult(localIntent, 1);
          }
        });
        if (UtilityHelper.formatDate(localMonthActivity.curDate, "y-m").equals(UtilityHelper.formatDate(str1, "y-m")))
        {
          localMonthActivity.pagerPosition = j;
          localMonthActivity.tvTotalPrice.setText(str2);
          k = 1;
        }
        j++;
      }
    }
  }
  
  private class ViewPagerAdapter
    extends PagerAdapter
  {
    private ViewPagerAdapter() {}
    
    public void destroyItem(ViewGroup paramViewGroup, int paramInt, Object paramObject)
    {
      ((ViewPager)paramViewGroup).removeView((View)MonthActivity.this.viewPagerList.get(paramInt));
    }
    
    public void finishUpdate(View paramView) {}
    
    public int getCount()
    {
      return MonthActivity.this.viewPagerList.size();
    }
    
    public Object instantiateItem(ViewGroup paramViewGroup, int paramInt)
    {
      ((ViewPager)paramViewGroup).addView((View)MonthActivity.this.viewPagerList.get(paramInt), 0);
      return MonthActivity.this.viewPagerList.get(paramInt);
    }
    
    public boolean isViewFromObject(View paramView, Object paramObject)
    {
      return paramView == paramObject;
    }
    
    public void restoreState(Parcelable paramParcelable, ClassLoader paramClassLoader) {}
    
    public Parcelable saveState()
    {
      return null;
    }
    
    public void startUpdate(View paramView) {}
  }
}


/* Location:           D:\fxlandroid\document\app\AALifeNew_v3.1.4\classes_dex2jar.jar
 * Qualified Name:     com.aalife.android.MonthActivity
 * JD-Core Version:    0.7.0-SNAPSHOT-20130630
 */