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
import android.text.Editable;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AnalyzeActivity
  extends Activity
{
  private final int FIRST_REQUEST_CODE = 1;
  private final int SECOND_REQUEST_CODE = 2;
  private SimpleAdapter adapter = null;
  private ImageButton btnTitleAdd = null;
  private ImageButton btnTitleDate = null;
  private ImageButton btnTitleSearch = null;
  private String curDate = "";
  private EditText etTitleKey = null;
  private ItemTableAccess itemAccess = null;
  private String key = "";
  private View layAnalyzeCompare = null;
  private View layAnalyzeRecommend = null;
  private View layAnalyzeSearch = null;
  private LinearLayout layNoItemCompare = null;
  private LinearLayout layNoItemRecommend = null;
  private LinearLayout layNoItemSearch = null;
  private List<Map<String, String>> list = null;
  private ListView listAnalyzeCompare = null;
  private ListView listAnalyzeRecommend = null;
  private ListView listAnalyzeSearch = null;
  private LayoutInflater mInflater = null;
  private SQLiteOpenHelper sqlHelper = null;
  private TextView tvNavCompare = null;
  private TextView tvNavRecommend = null;
  private TextView tvNavSearch = null;
  private TextView tvTitleAnalyze = null;
  private ViewPager viewPager = null;
  private ViewPagerAdapter viewPagerAdapter = null;
  private List<View> viewPagerList = null;
  
  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if ((paramInt1 == 1) && (paramInt2 == -1)) {
      setListData(this.curDate);
    }
    while ((paramInt1 != 2) || (paramInt2 != -1)) {
      return;
    }
    setSearchData(this.key);
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903041);
    this.sqlHelper = new DatabaseHelper(this);
    this.viewPagerList = new ArrayList();
    this.mInflater = getLayoutInflater();
    this.layAnalyzeCompare = this.mInflater.inflate(2130903057, null);
    this.layAnalyzeRecommend = this.mInflater.inflate(2130903058, null);
    this.layAnalyzeSearch = this.mInflater.inflate(2130903059, null);
    this.viewPagerList.add(this.layAnalyzeCompare);
    this.viewPagerList.add(this.layAnalyzeRecommend);
    this.viewPagerList.add(this.layAnalyzeSearch);
    this.viewPager = ((ViewPager)super.findViewById(2131296285));
    ((TextView)this.layAnalyzeCompare.findViewById(2131296297)).getPaint().setFakeBoldText(true);
    ((TextView)this.layAnalyzeCompare.findViewById(2131296289)).getPaint().setFakeBoldText(true);
    ((TextView)this.layAnalyzeCompare.findViewById(2131296291)).getPaint().setFakeBoldText(true);
    ((TextView)this.layAnalyzeRecommend.findViewById(2131296287)).getPaint().setFakeBoldText(true);
    ((TextView)this.layAnalyzeRecommend.findViewById(2131296360)).getPaint().setFakeBoldText(true);
    ((TextView)this.layAnalyzeRecommend.findViewById(2131296314)).getPaint().setFakeBoldText(true);
    ((TextView)this.layAnalyzeSearch.findViewById(2131296287)).getPaint().setFakeBoldText(true);
    ((TextView)this.layAnalyzeSearch.findViewById(2131296360)).getPaint().setFakeBoldText(true);
    ((TextView)this.layAnalyzeSearch.findViewById(2131296314)).getPaint().setFakeBoldText(true);
    this.curDate = UtilityHelper.getCurDate();
    this.tvTitleAnalyze = ((TextView)super.findViewById(2131296277));
    this.layNoItemCompare = ((LinearLayout)this.layAnalyzeCompare.findViewById(2131296292));
    this.layNoItemRecommend = ((LinearLayout)this.layAnalyzeRecommend.findViewById(2131296292));
    this.layNoItemSearch = ((LinearLayout)this.layAnalyzeSearch.findViewById(2131296292));
    this.layNoItemCompare.setVisibility(8);
    this.layNoItemRecommend.setVisibility(8);
    this.layNoItemSearch.setVisibility(8);
    this.etTitleKey = ((EditText)super.findViewById(2131296280));
    this.listAnalyzeCompare = ((ListView)this.layAnalyzeCompare.findViewById(2131296378));
    this.listAnalyzeCompare.setDivider(null);
    this.listAnalyzeCompare.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
        int i = Integer.parseInt((String)((Map)((ListView)paramAnonymousAdapterView).getItemAtPosition(paramAnonymousInt)).get("catid"));
        ((TextView)paramAnonymousView.findViewById(2131296404)).setBackgroundColor(AnalyzeActivity.this.getResources().getColor(2130968577));
        ((TextView)paramAnonymousView.findViewById(2131296405)).setBackgroundColor(AnalyzeActivity.this.getResources().getColor(2130968577));
        ((TextView)paramAnonymousView.findViewById(2131296406)).setBackgroundColor(AnalyzeActivity.this.getResources().getColor(2130968577));
        Intent localIntent = new Intent(AnalyzeActivity.this, AnalyzeCompareDetailActivity.class);
        localIntent.putExtra("catid", i);
        localIntent.putExtra("date", AnalyzeActivity.this.curDate);
        AnalyzeActivity.this.startActivityForResult(localIntent, 1);
      }
    });
    this.listAnalyzeRecommend = ((ListView)this.layAnalyzeRecommend.findViewById(2131296379));
    this.listAnalyzeRecommend.setDivider(null);
    this.listAnalyzeRecommend.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
        String str = (String)((Map)((ListView)paramAnonymousAdapterView).getItemAtPosition(paramAnonymousInt)).get("datevalue");
        ((TextView)paramAnonymousView.findViewById(2131296440)).setBackgroundColor(AnalyzeActivity.this.getResources().getColor(2130968577));
        ((TextView)paramAnonymousView.findViewById(2131296442)).setBackgroundColor(AnalyzeActivity.this.getResources().getColor(2130968577));
        ((TextView)paramAnonymousView.findViewById(2131296443)).setBackgroundColor(AnalyzeActivity.this.getResources().getColor(2130968577));
        Intent localIntent = new Intent(AnalyzeActivity.this, DayDetailActivity.class);
        localIntent.putExtra("date", str);
        AnalyzeActivity.this.startActivityForResult(localIntent, 1);
      }
    });
    this.listAnalyzeSearch = ((ListView)this.layAnalyzeSearch.findViewById(2131296380));
    this.listAnalyzeSearch.setDivider(null);
    this.listAnalyzeSearch.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
        String str = (String)((Map)((ListView)paramAnonymousAdapterView).getItemAtPosition(paramAnonymousInt)).get("datevalue");
        ((TextView)paramAnonymousView.findViewById(2131296440)).setBackgroundColor(AnalyzeActivity.this.getResources().getColor(2130968577));
        ((TextView)paramAnonymousView.findViewById(2131296442)).setBackgroundColor(AnalyzeActivity.this.getResources().getColor(2130968577));
        ((TextView)paramAnonymousView.findViewById(2131296443)).setBackgroundColor(AnalyzeActivity.this.getResources().getColor(2130968577));
        Intent localIntent = new Intent(AnalyzeActivity.this, DayDetailActivity.class);
        localIntent.putExtra("date", str);
        AnalyzeActivity.this.startActivityForResult(localIntent, 2);
      }
    });
    this.tvNavCompare = ((TextView)super.findViewById(2131296282));
    this.tvNavCompare.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        AnalyzeActivity.this.viewPager.setCurrentItem(0);
      }
    });
    this.tvNavRecommend = ((TextView)super.findViewById(2131296283));
    this.tvNavRecommend.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        AnalyzeActivity.this.viewPager.setCurrentItem(1);
      }
    });
    this.tvNavSearch = ((TextView)super.findViewById(2131296284));
    this.tvNavSearch.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        AnalyzeActivity.this.viewPager.setCurrentItem(2);
      }
    });
    ((ImageButton)super.findViewById(2131296257)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        AnalyzeActivity.this.finish();
      }
    }());
    this.btnTitleDate = ((ImageButton)super.findViewById(2131296278));
    this.btnTitleDate.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        String[] arrayOfString = AnalyzeActivity.this.curDate.split("-");
        (AnalyzeActivity.this, new DatePickerDialog.OnDateSetListener()
        {
          public void onDateSet(DatePicker paramAnonymous2DatePicker, int paramAnonymous2Int1, int paramAnonymous2Int2, int paramAnonymous2Int3)
          {
            String str = UtilityHelper.formatDate(paramAnonymous2Int1 + "-" + (paramAnonymous2Int2 + 1) + "-" + paramAnonymous2Int3, "");
            AnalyzeActivity.this.setListData(str);
          }
        }, Integer.parseInt(arrayOfString[0]), -1 + Integer.parseInt(arrayOfString[1]), Integer.parseInt(arrayOfString[2])).show();
      }
    });
    this.btnTitleAdd = ((ImageButton)super.findViewById(2131296279));
    this.btnTitleAdd.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        Intent localIntent = new Intent(AnalyzeActivity.this, AddActivity.class);
        localIntent.putExtra("recommend", 1);
        AnalyzeActivity.this.startActivityForResult(localIntent, 1);
      }
    });
    this.btnTitleSearch = ((ImageButton)super.findViewById(2131296281));
    this.btnTitleSearch.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        AnalyzeActivity.this.key = AnalyzeActivity.this.etTitleKey.getText().toString().trim();
        AnalyzeActivity.this.key = AnalyzeActivity.this.key.replace("%", "");
        AnalyzeActivity.this.key = AnalyzeActivity.this.key.replace("'", "");
        if (AnalyzeActivity.this.key.equals(""))
        {
          AnalyzeActivity.this.etTitleKey.setText("");
          Toast.makeText(AnalyzeActivity.this, AnalyzeActivity.this.getString(2131099715), 0).show();
          return;
        }
        AnalyzeActivity.this.setSearchData(AnalyzeActivity.this.key);
      }
    });
    this.viewPagerAdapter = new ViewPagerAdapter(null);
    this.viewPager.setAdapter(this.viewPagerAdapter);
    this.viewPager.setCurrentItem(0);
    this.tvNavCompare.setTextColor(getResources().getColor(2130968583));
    this.tvNavCompare.setBackgroundResource(2130837539);
    int i = getResources().getDimensionPixelSize(2131034112);
    this.tvNavCompare.setPadding(0, i, 0, i);
    this.viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
    {
      public void onPageScrollStateChanged(int paramAnonymousInt) {}
      
      public void onPageScrolled(int paramAnonymousInt1, float paramAnonymousFloat, int paramAnonymousInt2) {}
      
      public void onPageSelected(int paramAnonymousInt)
      {
        int i = AnalyzeActivity.this.getResources().getDimensionPixelSize(2131034112);
        AnalyzeActivity.this.tvNavCompare.setTextColor(AnalyzeActivity.this.getResources().getColor(17170444));
        AnalyzeActivity.this.tvNavCompare.setBackgroundResource(2130837541);
        AnalyzeActivity.this.tvNavCompare.setPadding(0, i, 0, i);
        AnalyzeActivity.this.tvNavRecommend.setTextColor(AnalyzeActivity.this.getResources().getColor(17170444));
        AnalyzeActivity.this.tvNavRecommend.setBackgroundResource(2130837541);
        AnalyzeActivity.this.tvNavRecommend.setPadding(0, i, 0, i);
        AnalyzeActivity.this.tvNavSearch.setTextColor(AnalyzeActivity.this.getResources().getColor(17170444));
        AnalyzeActivity.this.tvNavSearch.setBackgroundResource(2130837541);
        AnalyzeActivity.this.tvNavSearch.setPadding(0, i, 0, i);
        switch (paramAnonymousInt)
        {
        default: 
          return;
        case 0: 
          AnalyzeActivity.this.tvNavCompare.setTextColor(AnalyzeActivity.this.getResources().getColor(2130968583));
          AnalyzeActivity.this.tvNavCompare.setBackgroundResource(2130837539);
          AnalyzeActivity.this.tvNavCompare.setPadding(0, i, 0, i);
          AnalyzeActivity.this.tvTitleAnalyze.setText(AnalyzeActivity.this.getString(2131099684) + "(" + UtilityHelper.formatDate(AnalyzeActivity.this.curDate, "ys-m") + ")");
          AnalyzeActivity.this.btnTitleDate.setVisibility(0);
          AnalyzeActivity.this.btnTitleAdd.setVisibility(8);
          AnalyzeActivity.this.tvTitleAnalyze.setVisibility(0);
          AnalyzeActivity.this.etTitleKey.setVisibility(8);
          AnalyzeActivity.this.btnTitleSearch.setVisibility(8);
          return;
        case 1: 
          AnalyzeActivity.this.tvNavRecommend.setTextColor(AnalyzeActivity.this.getResources().getColor(2130968583));
          AnalyzeActivity.this.tvNavRecommend.setBackgroundResource(2130837539);
          AnalyzeActivity.this.tvNavRecommend.setPadding(0, i, 0, i);
          AnalyzeActivity.this.tvTitleAnalyze.setText(AnalyzeActivity.this.getString(2131099685));
          AnalyzeActivity.this.btnTitleDate.setVisibility(8);
          AnalyzeActivity.this.btnTitleAdd.setVisibility(0);
          AnalyzeActivity.this.tvTitleAnalyze.setVisibility(0);
          AnalyzeActivity.this.etTitleKey.setVisibility(8);
          AnalyzeActivity.this.btnTitleSearch.setVisibility(8);
          return;
        }
        AnalyzeActivity.this.tvNavSearch.setTextColor(AnalyzeActivity.this.getResources().getColor(2130968583));
        AnalyzeActivity.this.tvNavSearch.setBackgroundResource(2130837539);
        AnalyzeActivity.this.tvNavSearch.setPadding(0, i, 0, i);
        AnalyzeActivity.this.tvTitleAnalyze.setText(AnalyzeActivity.this.getString(2131099686));
        AnalyzeActivity.this.btnTitleDate.setVisibility(8);
        AnalyzeActivity.this.btnTitleAdd.setVisibility(8);
        AnalyzeActivity.this.tvTitleAnalyze.setVisibility(8);
        AnalyzeActivity.this.etTitleKey.setVisibility(0);
        AnalyzeActivity.this.btnTitleSearch.setVisibility(0);
      }
    });
    setListData(this.curDate);
  }
  
  protected void setListData(String paramString)
  {
    this.curDate = paramString;
    this.tvTitleAnalyze.setText(getString(2131099684) + "(" + UtilityHelper.formatDate(this.curDate, "ys-m") + ")");
    this.itemAccess = new ItemTableAccess(this.sqlHelper.getReadableDatabase());
    this.list = this.itemAccess.findCompareCatByDate(paramString);
    this.adapter = new SimpleAdapter(this, this.list, 2130903062, new String[] { "catid", "catname", "pricecur", "priceprev" }, new int[] { 2131296403, 2131296404, 2131296405, 2131296406 });
    this.listAnalyzeCompare.setAdapter(this.adapter);
    if (this.list.size() <= 0)
    {
      this.layNoItemCompare.setVisibility(0);
      this.list = this.itemAccess.findAnalyzeRecommend();
      this.adapter = new SimpleAdapter(this, this.list, 2130903074, new String[] { "itemname", "itembuydate", "itemprice" }, new int[] { 2131296440, 2131296442, 2131296443 });
      this.listAnalyzeRecommend.setAdapter(this.adapter);
      if (this.list.size() > 0) {
        break label316;
      }
      this.layNoItemRecommend.setVisibility(0);
    }
    for (;;)
    {
      this.itemAccess.close();
      return;
      this.layNoItemCompare.setVisibility(8);
      break;
      label316:
      this.layNoItemRecommend.setVisibility(8);
    }
  }
  
  protected void setSearchData(String paramString)
  {
    this.itemAccess = new ItemTableAccess(this.sqlHelper.getReadableDatabase());
    this.list = this.itemAccess.findItemByKey(paramString);
    this.itemAccess.close();
    this.adapter = new SimpleAdapter(this, this.list, 2130903074, new String[] { "itemname", "itembuydate", "itemprice" }, new int[] { 2131296440, 2131296442, 2131296443 });
    this.listAnalyzeSearch.setAdapter(this.adapter);
    if (this.list.size() == 0)
    {
      this.layNoItemSearch.setVisibility(0);
      return;
    }
    this.layNoItemSearch.setVisibility(8);
  }
  
  private class ViewPagerAdapter
    extends PagerAdapter
  {
    private ViewPagerAdapter() {}
    
    public void destroyItem(ViewGroup paramViewGroup, int paramInt, Object paramObject)
    {
      ((ViewPager)paramViewGroup).removeView((View)AnalyzeActivity.this.viewPagerList.get(paramInt));
    }
    
    public void finishUpdate(View paramView) {}
    
    public int getCount()
    {
      return AnalyzeActivity.this.viewPagerList.size();
    }
    
    public Object instantiateItem(ViewGroup paramViewGroup, int paramInt)
    {
      ((ViewPager)paramViewGroup).addView((View)AnalyzeActivity.this.viewPagerList.get(paramInt), 0);
      return AnalyzeActivity.this.viewPagerList.get(paramInt);
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
 * Qualified Name:     com.aalife.android.AnalyzeActivity
 * JD-Core Version:    0.7.0-SNAPSHOT-20130630
 */