package com.aalife.android;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.content.res.Resources;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RankActivity
  extends Activity
{
  private final int FIRST_REQUEST_CODE = 1;
  private SimpleAdapter adapter = null;
  private String curDate = "";
  private ItemTableAccess itemAccess = null;
  private LinearLayout layNoItemCat = null;
  private LinearLayout layNoItemCount = null;
  private LinearLayout layNoItemDate = null;
  private LinearLayout layNoItemPrice = null;
  private View layRankCat = null;
  private View layRankCount = null;
  private View layRankDate = null;
  private View layRankPrice = null;
  private List<Map<String, String>> list = null;
  private ListView listRankCat = null;
  private ListView listRankCount = null;
  private ListView listRankDate = null;
  private ListView listRankPrice = null;
  private LayoutInflater mInflater = null;
  private SQLiteOpenHelper sqlHelper = null;
  private String strTitle = "";
  private TextView tvNavCat = null;
  private TextView tvNavCount = null;
  private TextView tvNavDate = null;
  private TextView tvNavPrice = null;
  private TextView tvTitleRank = null;
  private ViewPager viewPager = null;
  private ViewPagerAdapter viewPagerAdapter = null;
  private List<View> viewPagerList = null;
  
  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if ((paramInt1 == 1) && (paramInt2 == -1)) {
      setListData(this.curDate);
    }
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903050);
    this.sqlHelper = new DatabaseHelper(this);
    this.viewPagerList = new ArrayList();
    this.mInflater = getLayoutInflater();
    this.layRankCat = this.mInflater.inflate(2130903076, null);
    this.layRankCount = this.mInflater.inflate(2130903077, null);
    this.layRankPrice = this.mInflater.inflate(2130903079, null);
    this.layRankDate = this.mInflater.inflate(2130903078, null);
    this.viewPagerList.add(this.layRankCat);
    this.viewPagerList.add(this.layRankCount);
    this.viewPagerList.add(this.layRankPrice);
    this.viewPagerList.add(this.layRankDate);
    this.viewPager = ((ViewPager)super.findViewById(2131296285));
    ((TextView)this.layRankCat.findViewById(2131296296)).getPaint().setFakeBoldText(true);
    ((TextView)this.layRankCat.findViewById(2131296297)).getPaint().setFakeBoldText(true);
    ((TextView)this.layRankCat.findViewById(2131296358)).getPaint().setFakeBoldText(true);
    ((TextView)this.layRankCount.findViewById(2131296287)).getPaint().setFakeBoldText(true);
    ((TextView)this.layRankCount.findViewById(2131296357)).getPaint().setFakeBoldText(true);
    ((TextView)this.layRankCount.findViewById(2131296358)).getPaint().setFakeBoldText(true);
    ((TextView)this.layRankPrice.findViewById(2131296287)).getPaint().setFakeBoldText(true);
    ((TextView)this.layRankPrice.findViewById(2131296360)).getPaint().setFakeBoldText(true);
    ((TextView)this.layRankPrice.findViewById(2131296314)).getPaint().setFakeBoldText(true);
    ((TextView)this.layRankDate.findViewById(2131296296)).getPaint().setFakeBoldText(true);
    ((TextView)this.layRankDate.findViewById(2131296360)).getPaint().setFakeBoldText(true);
    ((TextView)this.layRankDate.findViewById(2131296358)).getPaint().setFakeBoldText(true);
    this.curDate = UtilityHelper.getCurDate();
    this.tvTitleRank = ((TextView)super.findViewById(2131296352));
    this.strTitle = getString(2131099692);
    this.layNoItemCat = ((LinearLayout)this.layRankCat.findViewById(2131296292));
    this.layNoItemCount = ((LinearLayout)this.layRankCount.findViewById(2131296292));
    this.layNoItemPrice = ((LinearLayout)this.layRankPrice.findViewById(2131296292));
    this.layNoItemDate = ((LinearLayout)this.layRankDate.findViewById(2131296292));
    this.layNoItemCat.setVisibility(8);
    this.layNoItemCount.setVisibility(8);
    this.layNoItemPrice.setVisibility(8);
    this.layNoItemDate.setVisibility(8);
    this.listRankCat = ((ListView)this.layRankCat.findViewById(2131296446));
    this.listRankCat.setDivider(null);
    this.listRankCat.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
        int i = Integer.parseInt((String)((Map)((ListView)paramAnonymousAdapterView).getItemAtPosition(paramAnonymousInt)).get("catid"));
        ((TextView)paramAnonymousView.findViewById(2131296437)).setBackgroundColor(RankActivity.this.getResources().getColor(2130968577));
        ((TextView)paramAnonymousView.findViewById(2131296438)).setBackgroundColor(RankActivity.this.getResources().getColor(2130968577));
        ((TextView)paramAnonymousView.findViewById(2131296439)).setBackgroundColor(RankActivity.this.getResources().getColor(2130968577));
        Intent localIntent = new Intent(RankActivity.this, RankCatDetailActivity.class);
        localIntent.putExtra("catid", i);
        localIntent.putExtra("date", RankActivity.this.curDate);
        RankActivity.this.startActivityForResult(localIntent, 1);
      }
    });
    this.listRankCount = ((ListView)this.layRankCount.findViewById(2131296447));
    this.listRankCount.setDivider(null);
    this.listRankCount.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
        String str = (String)((Map)((ListView)paramAnonymousAdapterView).getItemAtPosition(paramAnonymousInt)).get("itemname");
        ((TextView)paramAnonymousView.findViewById(2131296440)).setBackgroundColor(RankActivity.this.getResources().getColor(2130968577));
        ((TextView)paramAnonymousView.findViewById(2131296441)).setBackgroundColor(RankActivity.this.getResources().getColor(2130968577));
        ((TextView)paramAnonymousView.findViewById(2131296439)).setBackgroundColor(RankActivity.this.getResources().getColor(2130968577));
        Intent localIntent = new Intent(RankActivity.this, RankCountDetailActivity.class);
        localIntent.putExtra("itemname", str);
        localIntent.putExtra("date", RankActivity.this.curDate);
        RankActivity.this.startActivityForResult(localIntent, 1);
      }
    });
    this.listRankPrice = ((ListView)this.layRankPrice.findViewById(2131296449));
    this.listRankPrice.setDivider(null);
    this.listRankPrice.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
        String str = (String)((Map)((ListView)paramAnonymousAdapterView).getItemAtPosition(paramAnonymousInt)).get("datevalue");
        ((TextView)paramAnonymousView.findViewById(2131296440)).setBackgroundColor(RankActivity.this.getResources().getColor(2130968577));
        ((TextView)paramAnonymousView.findViewById(2131296442)).setBackgroundColor(RankActivity.this.getResources().getColor(2130968577));
        ((TextView)paramAnonymousView.findViewById(2131296443)).setBackgroundColor(RankActivity.this.getResources().getColor(2130968577));
        Intent localIntent = new Intent(RankActivity.this, DayDetailActivity.class);
        localIntent.putExtra("date", str);
        RankActivity.this.startActivityForResult(localIntent, 1);
      }
    });
    this.listRankDate = ((ListView)this.layRankDate.findViewById(2131296448));
    this.listRankDate.setDivider(null);
    this.listRankDate.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
        String str = (String)((Map)((ListView)paramAnonymousAdapterView).getItemAtPosition(paramAnonymousInt)).get("datevalue");
        ((TextView)paramAnonymousView.findViewById(2131296437)).setBackgroundColor(RankActivity.this.getResources().getColor(2130968577));
        ((TextView)paramAnonymousView.findViewById(2131296442)).setBackgroundColor(RankActivity.this.getResources().getColor(2130968577));
        ((TextView)paramAnonymousView.findViewById(2131296439)).setBackgroundColor(RankActivity.this.getResources().getColor(2130968577));
        Intent localIntent = new Intent(RankActivity.this, DayDetailActivity.class);
        localIntent.putExtra("date", str);
        RankActivity.this.startActivityForResult(localIntent, 1);
      }
    });
    this.tvNavCat = ((TextView)super.findViewById(2131296353));
    this.tvNavCat.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        RankActivity.this.viewPager.setCurrentItem(0);
      }
    });
    this.tvNavCount = ((TextView)super.findViewById(2131296354));
    this.tvNavCount.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        RankActivity.this.viewPager.setCurrentItem(1);
      }
    });
    this.tvNavPrice = ((TextView)super.findViewById(2131296355));
    this.tvNavPrice.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        RankActivity.this.viewPager.setCurrentItem(2);
      }
    });
    this.tvNavDate = ((TextView)super.findViewById(2131296356));
    this.tvNavDate.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        RankActivity.this.viewPager.setCurrentItem(3);
      }
    });
    ((ImageButton)super.findViewById(2131296257)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        RankActivity.this.finish();
      }
    }());
    ((ImageButton)super.findViewById(2131296278)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        String[] arrayOfString = RankActivity.this.curDate.split("-");
        (RankActivity.this, new DatePickerDialog.OnDateSetListener()
        {
          public void onDateSet(DatePicker paramAnonymous2DatePicker, int paramAnonymous2Int1, int paramAnonymous2Int2, int paramAnonymous2Int3)
          {
            String str = UtilityHelper.formatDate(paramAnonymous2Int1 + "-" + (paramAnonymous2Int2 + 1) + "-" + paramAnonymous2Int3, "");
            RankActivity.this.setListData(str);
          }
        }, Integer.parseInt(arrayOfString[0]), -1 + Integer.parseInt(arrayOfString[1]), Integer.parseInt(arrayOfString[2])).show();
      }
    }());
    this.viewPagerAdapter = new ViewPagerAdapter(null);
    this.viewPager.setAdapter(this.viewPagerAdapter);
    this.viewPager.setCurrentItem(0);
    this.tvNavCat.setTextColor(getResources().getColor(2130968583));
    this.tvNavCat.setBackgroundResource(2130837539);
    int i = getResources().getDimensionPixelSize(2131034112);
    this.tvNavCat.setPadding(0, i, 0, i);
    this.viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
    {
      public void onPageScrollStateChanged(int paramAnonymousInt) {}
      
      public void onPageScrolled(int paramAnonymousInt1, float paramAnonymousFloat, int paramAnonymousInt2) {}
      
      public void onPageSelected(int paramAnonymousInt)
      {
        int i = RankActivity.this.getResources().getDimensionPixelSize(2131034112);
        RankActivity.this.tvNavCat.setTextColor(RankActivity.this.getResources().getColor(17170444));
        RankActivity.this.tvNavCat.setBackgroundResource(2130837541);
        RankActivity.this.tvNavCat.setPadding(0, i, 0, i);
        RankActivity.this.tvNavCount.setTextColor(RankActivity.this.getResources().getColor(17170444));
        RankActivity.this.tvNavCount.setBackgroundResource(2130837541);
        RankActivity.this.tvNavCount.setPadding(0, i, 0, i);
        RankActivity.this.tvNavPrice.setTextColor(RankActivity.this.getResources().getColor(17170444));
        RankActivity.this.tvNavPrice.setBackgroundResource(2130837541);
        RankActivity.this.tvNavPrice.setPadding(0, i, 0, i);
        RankActivity.this.tvNavDate.setTextColor(RankActivity.this.getResources().getColor(17170444));
        RankActivity.this.tvNavDate.setBackgroundResource(2130837541);
        RankActivity.this.tvNavDate.setPadding(0, i, 0, i);
        switch (paramAnonymousInt)
        {
        default: 
          return;
        case 0: 
          RankActivity.this.tvNavCat.setTextColor(RankActivity.this.getResources().getColor(2130968583));
          RankActivity.this.tvNavCat.setBackgroundResource(2130837539);
          RankActivity.this.tvNavCat.setPadding(0, i, 0, i);
          RankActivity.this.tvTitleRank.setText(RankActivity.this.getString(2131099692) + "(" + UtilityHelper.formatDate(RankActivity.this.curDate, "ys-m") + ")");
          RankActivity.this.strTitle = RankActivity.this.getString(2131099692);
          return;
        case 1: 
          RankActivity.this.tvNavCount.setTextColor(RankActivity.this.getResources().getColor(2130968583));
          RankActivity.this.tvNavCount.setBackgroundResource(2130837539);
          RankActivity.this.tvNavCount.setPadding(0, i, 0, i);
          RankActivity.this.tvTitleRank.setText(RankActivity.this.getString(2131099693) + "(" + UtilityHelper.formatDate(RankActivity.this.curDate, "ys-m") + ")");
          RankActivity.this.strTitle = RankActivity.this.getString(2131099693);
          return;
        case 2: 
          RankActivity.this.tvNavPrice.setTextColor(RankActivity.this.getResources().getColor(2130968583));
          RankActivity.this.tvNavPrice.setBackgroundResource(2130837539);
          RankActivity.this.tvNavPrice.setPadding(0, i, 0, i);
          RankActivity.this.tvTitleRank.setText(RankActivity.this.getString(2131099694) + "(" + UtilityHelper.formatDate(RankActivity.this.curDate, "ys-m") + ")");
          RankActivity.this.strTitle = RankActivity.this.getString(2131099694);
          return;
        }
        RankActivity.this.tvNavDate.setTextColor(RankActivity.this.getResources().getColor(2130968583));
        RankActivity.this.tvNavDate.setBackgroundResource(2130837539);
        RankActivity.this.tvNavDate.setPadding(0, i, 0, i);
        RankActivity.this.tvTitleRank.setText(RankActivity.this.getString(2131099695) + "(" + UtilityHelper.formatDate(RankActivity.this.curDate, "ys-m") + ")");
        RankActivity.this.strTitle = RankActivity.this.getString(2131099695);
      }
    });
    setListData(this.curDate);
  }
  
  protected void setListData(String paramString)
  {
    this.curDate = paramString;
    this.tvTitleRank.setText(this.strTitle + "(" + UtilityHelper.formatDate(this.curDate, "ys-m") + ")");
    this.itemAccess = new ItemTableAccess(this.sqlHelper.getReadableDatabase());
    this.list = this.itemAccess.findRankCatByDate(paramString);
    this.adapter = new SimpleAdapter(this, this.list, 2130903071, new String[] { "id", "catname", "price" }, new int[] { 2131296437, 2131296438, 2131296439 });
    this.listRankCat.setAdapter(this.adapter);
    if (this.list.size() <= 0)
    {
      this.layNoItemCat.setVisibility(0);
      this.list = this.itemAccess.findRankCountByDate(paramString);
      this.adapter = new SimpleAdapter(this, this.list, 2130903072, new String[] { "itemname", "count", "price" }, new int[] { 2131296440, 2131296441, 2131296439 });
      this.listRankCount.setAdapter(this.adapter);
      if (this.list.size() > 0) {
        break label512;
      }
      this.layNoItemCount.setVisibility(0);
      label282:
      this.list = this.itemAccess.findRankPriceByDate(paramString);
      this.adapter = new SimpleAdapter(this, this.list, 2130903074, new String[] { "itemname", "itembuydate", "itemprice" }, new int[] { 2131296440, 2131296442, 2131296443 });
      this.listRankPrice.setAdapter(this.adapter);
      if (this.list.size() > 0) {
        break label524;
      }
      this.layNoItemPrice.setVisibility(0);
      label387:
      this.list = this.itemAccess.findRankDateByDate(paramString);
      this.adapter = new SimpleAdapter(this, this.list, 2130903073, new String[] { "id", "itembuydate", "price" }, new int[] { 2131296437, 2131296442, 2131296439 });
      this.listRankDate.setAdapter(this.adapter);
      if (this.list.size() > 0) {
        break label536;
      }
      this.layNoItemDate.setVisibility(0);
    }
    for (;;)
    {
      this.itemAccess.close();
      return;
      this.layNoItemCat.setVisibility(8);
      break;
      label512:
      this.layNoItemCount.setVisibility(8);
      break label282;
      label524:
      this.layNoItemPrice.setVisibility(8);
      break label387;
      label536:
      this.layNoItemDate.setVisibility(8);
    }
  }
  
  private class ViewPagerAdapter
    extends PagerAdapter
  {
    private ViewPagerAdapter() {}
    
    public void destroyItem(ViewGroup paramViewGroup, int paramInt, Object paramObject)
    {
      ((ViewPager)paramViewGroup).removeView((View)RankActivity.this.viewPagerList.get(paramInt));
    }
    
    public void finishUpdate(View paramView) {}
    
    public int getCount()
    {
      return RankActivity.this.viewPagerList.size();
    }
    
    public Object instantiateItem(ViewGroup paramViewGroup, int paramInt)
    {
      ((ViewPager)paramViewGroup).addView((View)RankActivity.this.viewPagerList.get(paramInt), 0);
      return RankActivity.this.viewPagerList.get(paramInt);
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
 * Qualified Name:     com.aalife.android.RankActivity
 * JD-Core Version:    0.7.0-SNAPSHOT-20130630
 */