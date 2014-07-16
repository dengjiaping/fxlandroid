package com.aalife.android;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import java.util.List;
import java.util.Map;

public class AnalyzeCompareDetailActivity
  extends Activity
{
  private final int FIRST_REQUEST_CODE = 1;
  private SimpleAdapter adapter = null;
  private int catId = 0;
  private String curDate = "";
  private ItemTableAccess itemAccess = null;
  private LinearLayout layNoItem = null;
  private List<Map<String, String>> list = null;
  private ListView listAnalyzeCompareDetail = null;
  private SQLiteOpenHelper sqlHelper = null;
  
  protected void close()
  {
    finish();
  }
  
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
    setContentView(2130903042);
    ((TextView)super.findViewById(2131296287)).getPaint().setFakeBoldText(true);
    ((TextView)super.findViewById(2131296288)).getPaint().setFakeBoldText(true);
    ((TextView)super.findViewById(2131296289)).getPaint().setFakeBoldText(true);
    ((TextView)super.findViewById(2131296290)).getPaint().setFakeBoldText(true);
    ((TextView)super.findViewById(2131296291)).getPaint().setFakeBoldText(true);
    this.sqlHelper = new DatabaseHelper(this);
    Intent localIntent = super.getIntent();
    this.catId = localIntent.getIntExtra("catid", 1);
    this.curDate = localIntent.getStringExtra("date");
    this.listAnalyzeCompareDetail = ((ListView)super.findViewById(2131296294));
    this.listAnalyzeCompareDetail.setDivider(null);
    this.layNoItem = ((LinearLayout)super.findViewById(2131296292));
    this.listAnalyzeCompareDetail.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
        Map localMap = (Map)((ListView)paramAnonymousAdapterView).getItemAtPosition(paramAnonymousInt);
        String str1 = (String)localMap.get("itemname");
        if (!((String)localMap.get("countvalue")).equals("0")) {}
        for (String str2 = AnalyzeCompareDetailActivity.this.curDate;; str2 = UtilityHelper.getNavDate(AnalyzeCompareDetailActivity.this.curDate, -1, "m"))
        {
          ((TextView)paramAnonymousView.findViewById(2131296407)).setBackgroundColor(AnalyzeCompareDetailActivity.this.getResources().getColor(2130968577));
          ((TextView)paramAnonymousView.findViewById(2131296408)).setBackgroundColor(AnalyzeCompareDetailActivity.this.getResources().getColor(2130968577));
          ((TextView)paramAnonymousView.findViewById(2131296405)).setBackgroundColor(AnalyzeCompareDetailActivity.this.getResources().getColor(2130968577));
          ((TextView)paramAnonymousView.findViewById(2131296409)).setBackgroundColor(AnalyzeCompareDetailActivity.this.getResources().getColor(2130968577));
          ((TextView)paramAnonymousView.findViewById(2131296406)).setBackgroundColor(AnalyzeCompareDetailActivity.this.getResources().getColor(2130968577));
          Intent localIntent = new Intent(AnalyzeCompareDetailActivity.this, RankCountDetailActivity.class);
          localIntent.putExtra("itemname", str1);
          localIntent.putExtra("date", str2);
          AnalyzeCompareDetailActivity.this.startActivityForResult(localIntent, 1);
          return;
        }
      }
    });
    ((ImageButton)super.findViewById(2131296257)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        AnalyzeCompareDetailActivity.this.setResult(-1);
        AnalyzeCompareDetailActivity.this.close();
      }
    }());
    setListData(this.curDate);
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if (paramInt == 4)
    {
      setResult(-1);
      close();
    }
    return super.onKeyDown(paramInt, paramKeyEvent);
  }
  
  protected void setListData(String paramString)
  {
    this.itemAccess = new ItemTableAccess(this.sqlHelper.getReadableDatabase());
    this.list = this.itemAccess.findAnalyzeCompareDetailByDate(paramString, this.catId);
    this.itemAccess.close();
    this.adapter = new SimpleAdapter(this, this.list, 2130903063, new String[] { "itemname", "countcur", "pricecur", "countprev", "priceprev" }, new int[] { 2131296407, 2131296408, 2131296405, 2131296409, 2131296406 });
    this.listAnalyzeCompareDetail.setAdapter(this.adapter);
    if (this.list.size() == 0)
    {
      this.layNoItem.setVisibility(0);
      return;
    }
    this.layNoItem.setVisibility(8);
  }
}


/* Location:           D:\fxlandroid\document\app\AALifeNew_v3.1.4\classes_dex2jar.jar
 * Qualified Name:     com.aalife.android.AnalyzeCompareDetailActivity
 * JD-Core Version:    0.7.0-SNAPSHOT-20130630
 */