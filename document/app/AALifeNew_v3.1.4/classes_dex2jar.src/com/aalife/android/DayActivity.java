package com.aalife.android;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DayActivity
  extends Activity
{
  private final int FIRST_REQUEST_CODE = 1;
  private String curDate = "";
  private DayAdapter dayAdapter = null;
  private ItemTableAccess itemAccess = null;
  private LinearLayout layDayTotal = null;
  private LinearLayout layNoItem = null;
  private List<Map<String, String>> list = null;
  private boolean loading = false;
  private ListView lvDayList = null;
  private MyHandler myHandler = new MyHandler(this);
  private List<Map<String, String>> newList = null;
  private ProgressBar pbDay = null;
  private SQLiteOpenHelper sqlHelper = null;
  private TextView tvTotalLabel = null;
  private TextView tvTotalPrice = null;
  private int visibleLastIndex = 0;
  
  protected void close()
  {
    finish();
  }
  
  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if ((paramInt1 == 1) && (paramInt2 == -1))
    {
      ItemTableAccess localItemTableAccess = new ItemTableAccess(this.sqlHelper.getReadableDatabase());
      this.list = localItemTableAccess.findAllDayFirstBuyDate(this.curDate);
      localItemTableAccess.close();
      if (this.list.size() > 0) {
        break label92;
      }
      this.layNoItem.setVisibility(0);
      this.layDayTotal.setVisibility(8);
    }
    for (;;)
    {
      this.dayAdapter.updateData(this.list);
      return;
      label92:
      this.layNoItem.setVisibility(8);
      this.layDayTotal.setVisibility(0);
    }
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903044);
    this.tvTotalLabel = ((TextView)super.findViewById(2131296307));
    this.tvTotalLabel.getPaint().setFakeBoldText(true);
    this.tvTotalPrice = ((TextView)super.findViewById(2131296308));
    this.tvTotalPrice.getPaint().setFakeBoldText(true);
    this.sqlHelper = new DatabaseHelper(this);
    this.lvDayList = ((ListView)super.findViewById(2131296304));
    this.lvDayList.setDivider(null);
    this.lvDayList.setFocusable(false);
    this.pbDay = ((ProgressBar)super.findViewById(2131296305));
    this.pbDay.setVisibility(0);
    this.layNoItem = ((LinearLayout)super.findViewById(2131296292));
    this.layNoItem.setVisibility(8);
    this.layDayTotal = ((LinearLayout)super.findViewById(2131296306));
    this.layDayTotal.setVisibility(8);
    (new Runnable()
    {
      public void run()
      {
        DayActivity.this.itemAccess = new ItemTableAccess(DayActivity.this.sqlHelper.getReadableDatabase());
        String str = DayActivity.this.itemAccess.findLastDate();
        DayActivity.this.list = DayActivity.this.itemAccess.findAllDayFirstBuyDate(str);
        if (str.equals("")) {}
        for (DayActivity.this.curDate = UtilityHelper.getCurDate();; DayActivity.this.curDate = str)
        {
          DayActivity.this.itemAccess.close();
          int i = DayActivity.this.list.size();
          boolean bool = false;
          if (i > 0) {
            bool = true;
          }
          Bundle localBundle = new Bundle();
          localBundle.putBoolean("result", bool);
          Message localMessage = new Message();
          localMessage.what = 1;
          localMessage.setData(localBundle);
          DayActivity.this.myHandler.sendMessage(localMessage);
          return;
        }
      }
    }).start();
    this.lvDayList.setOnScrollListener(new AbsListView.OnScrollListener()
    {
      public void onScroll(AbsListView paramAnonymousAbsListView, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3)
      {
        DayActivity.this.visibleLastIndex = (-1 + (paramAnonymousInt1 + paramAnonymousInt2));
      }
      
      public void onScrollStateChanged(AbsListView paramAnonymousAbsListView, int paramAnonymousInt)
      {
        int i = -1 + DayActivity.this.dayAdapter.getCount();
        if ((paramAnonymousInt == 0) && (DayActivity.this.visibleLastIndex == i) && (!DayActivity.this.loading))
        {
          DayActivity.this.pbDay.setVisibility(0);
          DayActivity.this.loading = true;
          (new Runnable()
          {
            public void run()
            {
              DayActivity.this.itemAccess = new ItemTableAccess(DayActivity.this.sqlHelper.getReadableDatabase());
              String str = DayActivity.this.itemAccess.findNextDate(DayActivity.this.curDate);
              DayActivity.this.newList = DayActivity.this.itemAccess.findAllDayBuyDate(str);
              DayActivity.this.itemAccess.close();
              if (DayActivity.this.newList.size() > 0)
              {
                DayActivity.this.curDate = str;
                Message localMessage2 = new Message();
                localMessage2.what = 2;
                DayActivity.this.myHandler.sendMessage(localMessage2);
                return;
              }
              Message localMessage1 = new Message();
              localMessage1.what = 3;
              DayActivity.this.myHandler.sendMessage(localMessage1);
            }
          }).start();
        }
      }
    });
    ((ImageButton)super.findViewById(2131296257)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        DayActivity.this.close();
      }
    }());
    ((ImageButton)super.findViewById(2131296279)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        Intent localIntent = new Intent(DayActivity.this, AddActivity.class);
        localIntent.putExtra("date", UtilityHelper.getCurDate());
        DayActivity.this.startActivityForResult(localIntent, 1);
      }
    }());
  }
  
  public void refreshData()
  {
    this.dayAdapter.notifyDataSetChanged();
  }
  
  public void setTotalData(String paramString1, String paramString2)
  {
    this.tvTotalLabel.setText(UtilityHelper.formatDate(paramString1, "y2-m2"));
    this.tvTotalPrice.setText(paramString2);
  }
  
  static class MyHandler
    extends Handler
  {
    WeakReference<DayActivity> myActivity = null;
    
    MyHandler(DayActivity paramDayActivity)
    {
      this.myActivity = new WeakReference(paramDayActivity);
    }
    
    public void handleMessage(Message paramMessage)
    {
      DayActivity localDayActivity = (DayActivity)this.myActivity.get();
      switch (paramMessage.what)
      {
      default: 
        return;
      case 1: 
        if (!paramMessage.getData().getBoolean("result")) {
          localDayActivity.layNoItem.setVisibility(0);
        }
        for (;;)
        {
          localDayActivity.pbDay.setVisibility(8);
          localDayActivity.dayAdapter = new DayAdapter(localDayActivity, localDayActivity.list);
          localDayActivity.lvDayList.setAdapter(localDayActivity.dayAdapter);
          return;
          localDayActivity.layDayTotal.setVisibility(0);
        }
      case 2: 
        Iterator localIterator = localDayActivity.newList.iterator();
        for (;;)
        {
          if (!localIterator.hasNext())
          {
            localDayActivity.dayAdapter.notifyDataSetChanged();
            localDayActivity.pbDay.setVisibility(8);
            localDayActivity.loading = false;
            return;
          }
          localDayActivity.list.add((Map)localIterator.next());
        }
      }
      localDayActivity.pbDay.setVisibility(8);
    }
  }
}


/* Location:           D:\fxlandroid\document\app\AALifeNew_v3.1.4\classes_dex2jar.jar
 * Qualified Name:     com.aalife.android.DayActivity
 * JD-Core Version:    0.7.0-SNAPSHOT-20130630
 */