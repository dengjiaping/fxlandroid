package com.aalife.android;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DayDetailActivity
  extends Activity
{
  private final int FIRST_REQUEST_CODE = 1;
  private SimpleAdapter adapter = null;
  private String curDate = "";
  private ItemTableAccess itemAccess = null;
  private String itemName = "";
  private LinearLayout layDayTotal = null;
  private LinearLayout layNoItem = null;
  private String leftDate = "";
  private List<Map<String, String>> list = null;
  private ListView listDay = null;
  private String rightDate = "";
  private SharedHelper sharedHelper = null;
  private SQLiteOpenHelper sqlHelper = null;
  private double totalPrice = 0.0D;
  private TextView tvNavLeft = null;
  private TextView tvNavMain = null;
  private TextView tvNavRight = null;
  private TextView tvTotalPrice = null;
  
  protected void close()
  {
    finish();
  }
  
  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if ((paramInt1 == 1) && (paramInt2 == -1))
    {
      if (paramIntent != null)
      {
        String str = paramIntent.getStringExtra("itemname");
        if ((str != null) && (!str.equals(""))) {
          this.itemName = str;
        }
      }
      setListData(this.curDate);
    }
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903045);
    ((TextView)super.findViewById(2131296313)).getPaint().setFakeBoldText(true);
    ((TextView)super.findViewById(2131296287)).getPaint().setFakeBoldText(true);
    ((TextView)super.findViewById(2131296314)).getPaint().setFakeBoldText(true);
    ((TextView)super.findViewById(2131296315)).getPaint().setFakeBoldText(true);
    ((TextView)super.findViewById(2131296307)).getPaint().setFakeBoldText(true);
    this.tvTotalPrice = ((TextView)super.findViewById(2131296308));
    this.tvTotalPrice.getPaint().setFakeBoldText(true);
    this.sqlHelper = new DatabaseHelper(this);
    this.curDate = super.getIntent().getStringExtra("date");
    this.sharedHelper = new SharedHelper(this);
    this.listDay = ((ListView)super.findViewById(2131296316));
    this.listDay.setDivider(null);
    this.layNoItem = ((LinearLayout)super.findViewById(2131296292));
    this.layNoItem.setVisibility(8);
    this.layDayTotal = ((LinearLayout)super.findViewById(2131296306));
    this.layDayTotal.setVisibility(8);
    this.listDay.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
        Map localMap = (Map)((ListView)paramAnonymousAdapterView).getItemAtPosition(paramAnonymousInt);
        String[] arrayOfString = new String[5];
        arrayOfString[0] = ((String)localMap.get("itemid"));
        arrayOfString[1] = ((String)localMap.get("catid"));
        arrayOfString[2] = ((String)localMap.get("itemname"));
        arrayOfString[3] = UtilityHelper.formatDouble(Double.parseDouble((String)localMap.get("pricevalue")), "0.###");
        arrayOfString[4] = ((String)localMap.get("itembuydate"));
        ((LinearLayout)paramAnonymousView.findViewById(2131296421)).setBackgroundColor(DayDetailActivity.this.getResources().getColor(2130968577));
        ((TextView)paramAnonymousView.findViewById(2131296423)).setBackgroundColor(DayDetailActivity.this.getResources().getColor(2130968577));
        ((TextView)paramAnonymousView.findViewById(2131296424)).setBackgroundColor(DayDetailActivity.this.getResources().getColor(2130968577));
        ((LinearLayout)paramAnonymousView.findViewById(2131296426)).setBackgroundColor(DayDetailActivity.this.getResources().getColor(2130968577));
        Intent localIntent = new Intent(DayDetailActivity.this, EditActivity.class);
        localIntent.putExtra("items", arrayOfString);
        DayDetailActivity.this.startActivityForResult(localIntent, 1);
      }
    });
    this.tvNavMain = ((TextView)super.findViewById(2131296311));
    this.tvNavMain.getPaint().setFakeBoldText(true);
    this.tvNavMain.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        String[] arrayOfString = DayDetailActivity.this.curDate.split("-");
        (DayDetailActivity.this, new DatePickerDialog.OnDateSetListener()
        {
          public void onDateSet(DatePicker paramAnonymous2DatePicker, int paramAnonymous2Int1, int paramAnonymous2Int2, int paramAnonymous2Int3)
          {
            String str = UtilityHelper.formatDate(paramAnonymous2Int1 + "-" + (paramAnonymous2Int2 + 1) + "-" + paramAnonymous2Int3, "");
            DayDetailActivity.this.setListData(str);
          }
        }, Integer.parseInt(arrayOfString[0]), -1 + Integer.parseInt(arrayOfString[1]), Integer.parseInt(arrayOfString[2])).show();
      }
    });
    this.tvNavLeft = ((TextView)super.findViewById(2131296310));
    this.tvNavLeft.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        DayDetailActivity.this.setListData(DayDetailActivity.this.leftDate);
      }
    });
    this.tvNavRight = ((TextView)super.findViewById(2131296312));
    this.tvNavRight.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        DayDetailActivity.this.setListData(DayDetailActivity.this.rightDate);
      }
    });
    ((ImageButton)super.findViewById(2131296257)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        Intent localIntent = new Intent();
        localIntent.putExtra("date", DayDetailActivity.this.curDate);
        localIntent.putExtra("itemname", DayDetailActivity.this.itemName);
        DayDetailActivity.this.setResult(-1, localIntent);
        DayDetailActivity.this.close();
      }
    }());
    ((ImageButton)super.findViewById(2131296279)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        Intent localIntent = new Intent(DayDetailActivity.this, AddActivity.class);
        localIntent.putExtra("date", DayDetailActivity.this.curDate);
        DayDetailActivity.this.startActivityForResult(localIntent, 1);
      }
    }());
    setListData(this.curDate);
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if (paramInt == 4)
    {
      Intent localIntent = new Intent();
      localIntent.putExtra("date", this.curDate);
      localIntent.putExtra("itemname", this.itemName);
      setResult(-1, localIntent);
      close();
    }
    return super.onKeyDown(paramInt, paramKeyEvent);
  }
  
  protected void setListData(String paramString)
  {
    this.curDate = paramString;
    this.tvNavMain.setText(UtilityHelper.formatDate(paramString, "m-d-w"));
    this.leftDate = UtilityHelper.getNavDate(paramString, -1, "d");
    this.tvNavLeft.setText(UtilityHelper.formatDate(this.leftDate, "m-d-w"));
    this.rightDate = UtilityHelper.getNavDate(paramString, 1, "d");
    this.tvNavRight.setText(UtilityHelper.formatDate(this.rightDate, "m-d-w"));
    this.itemAccess = new ItemTableAccess(this.sqlHelper.getReadableDatabase());
    this.list = this.itemAccess.findItemByDate(paramString);
    this.itemAccess.close();
    int[] arrayOfInt = { 2, this.list.size() };
    final boolean[][] arrayOfBoolean = (boolean[][])Array.newInstance(Boolean.TYPE, arrayOfInt);
    int i = 0;
    label312:
    Iterator localIterator;
    if (i >= this.list.size())
    {
      this.adapter = new SimpleAdapter(this, this.list, 2130903066, new String[] { "id", "itemname", "itemprice", "pricevalue", "id", "itemid", "catid" }, new int[] { 2131296422, 2131296423, 2131296424, 2131296425, 2131296427, 2131296428, 2131296429 })
      {
        public View getView(final int paramAnonymousInt, View paramAnonymousView, ViewGroup paramAnonymousViewGroup)
        {
          View localView = super.getView(paramAnonymousInt, paramAnonymousView, paramAnonymousViewGroup);
          final int i = Integer.parseInt(((TextView)localView.findViewById(2131296428)).getText().toString());
          final TextView localTextView = (TextView)localView.findViewById(2131296425);
          final CheckBox localCheckBox1 = (CheckBox)localView.findViewById(2131296422);
          if (arrayOfBoolean[0][paramAnonymousInt] != 0) {}
          for (boolean bool = false;; bool = true)
          {
            localCheckBox1.setChecked(bool);
            localCheckBox1.setOnClickListener(new View.OnClickListener()
            {
              public void onClick(View paramAnonymous2View)
              {
                boolean[] arrayOfBoolean = this.val$listCheck[0];
                int i = paramAnonymousInt;
                boolean bool = localCheckBox1.isChecked();
                int j = 0;
                if (bool) {}
                for (;;)
                {
                  arrayOfBoolean[i] = j;
                  if (!localCheckBox1.isChecked()) {
                    break;
                  }
                  DayDetailActivity.this.updateTotal(Double.parseDouble(localTextView.getText().toString()));
                  return;
                  j = 1;
                }
                DayDetailActivity.this.updateTotal(-Double.parseDouble(localTextView.getText().toString()));
              }
            });
            final CheckBox localCheckBox2 = (CheckBox)localView.findViewById(2131296427);
            localCheckBox2.setChecked(arrayOfBoolean[1][paramAnonymousInt]);
            localCheckBox2.setOnClickListener(new View.OnClickListener()
            {
              public void onClick(View paramAnonymous2View)
              {
                DayDetailActivity.this.itemAccess = new ItemTableAccess(DayDetailActivity.this.sqlHelper.getReadableDatabase());
                boolean[] arrayOfBoolean = this.val$listCheck[1];
                int i = paramAnonymousInt;
                int j;
                if (localCheckBox2.isChecked())
                {
                  j = 0;
                  arrayOfBoolean[i] = j;
                  if (!localCheckBox2.isChecked()) {
                    break label151;
                  }
                  DayDetailActivity.this.itemAccess.updateItemRecommend(i, 1);
                }
                for (;;)
                {
                  DayDetailActivity.this.itemAccess.close();
                  DayDetailActivity.this.sharedHelper.setLocalSync(Boolean.valueOf(true));
                  DayDetailActivity.this.sharedHelper.setSyncStatus(DayDetailActivity.this.getString(2131099708));
                  return;
                  j = 1;
                  break;
                  label151:
                  DayDetailActivity.this.itemAccess.updateItemRecommend(i, 0);
                }
              }
            });
            return localView;
          }
        }
      };
      this.listDay.setAdapter(this.adapter);
      if (this.list.size() > 0) {
        break label447;
      }
      this.layNoItem.setVisibility(0);
      this.layDayTotal.setVisibility(8);
      this.totalPrice = 0.0D;
      localIterator = this.list.iterator();
    }
    for (;;)
    {
      if (!localIterator.hasNext())
      {
        this.tvTotalPrice.setText(getString(2131099777) + UtilityHelper.formatDouble(this.totalPrice, "0.0##"));
        return;
        Map localMap1 = (Map)this.list.get(i);
        boolean[] arrayOfBoolean1 = arrayOfBoolean[1];
        if (((String)localMap1.get("recommend")).toString().equals("0")) {}
        for (int j = 0;; j = 1)
        {
          arrayOfBoolean1[i] = j;
          i++;
          break;
        }
        label447:
        this.layNoItem.setVisibility(8);
        this.layDayTotal.setVisibility(0);
        break label312;
      }
      Map localMap2 = (Map)localIterator.next();
      this.totalPrice += Double.parseDouble((String)localMap2.get("pricevalue"));
    }
  }
  
  protected void updateTotal(double paramDouble)
  {
    this.totalPrice = (paramDouble + this.totalPrice);
    this.tvTotalPrice.setText(getString(2131099777) + UtilityHelper.formatDouble(this.totalPrice, "0.0##"));
  }
}


/* Location:           D:\fxlandroid\document\app\AALifeNew_v3.1.4\classes_dex2jar.jar
 * Qualified Name:     com.aalife.android.DayDetailActivity
 * JD-Core Version:    0.7.0-SNAPSHOT-20130630
 */