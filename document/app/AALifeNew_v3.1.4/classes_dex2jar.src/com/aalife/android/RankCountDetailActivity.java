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

public class RankCountDetailActivity
  extends Activity
{
  private final int FIRST_REQUEST_CODE = 1;
  private SimpleAdapter adapter = null;
  private String curDate = "";
  private ItemTableAccess itemAccess = null;
  private String itemName = "";
  private LinearLayout layNoItem = null;
  private List<Map<String, String>> list = null;
  private ListView listRankCountDetail = null;
  private SQLiteOpenHelper sqlHelper = null;
  
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
        if (!str.equals("")) {
          this.itemName = str;
        }
      }
      setListData(this.curDate, this.itemName);
    }
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903052);
    ((TextView)super.findViewById(2131296287)).getPaint().setFakeBoldText(true);
    ((TextView)super.findViewById(2131296360)).getPaint().setFakeBoldText(true);
    ((TextView)super.findViewById(2131296314)).getPaint().setFakeBoldText(true);
    this.sqlHelper = new DatabaseHelper(this);
    Intent localIntent = super.getIntent();
    this.itemName = localIntent.getStringExtra("itemname");
    this.curDate = localIntent.getStringExtra("date");
    this.listRankCountDetail = ((ListView)super.findViewById(2131296361));
    this.listRankCountDetail.setDivider(null);
    this.layNoItem = ((LinearLayout)super.findViewById(2131296292));
    this.listRankCountDetail.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
        String str = (String)((Map)((ListView)paramAnonymousAdapterView).getItemAtPosition(paramAnonymousInt)).get("datevalue");
        ((TextView)paramAnonymousView.findViewById(2131296440)).setBackgroundColor(RankCountDetailActivity.this.getResources().getColor(2130968577));
        ((TextView)paramAnonymousView.findViewById(2131296442)).setBackgroundColor(RankCountDetailActivity.this.getResources().getColor(2130968577));
        ((TextView)paramAnonymousView.findViewById(2131296443)).setBackgroundColor(RankCountDetailActivity.this.getResources().getColor(2130968577));
        Intent localIntent = new Intent(RankCountDetailActivity.this, DayDetailActivity.class);
        localIntent.putExtra("date", str);
        RankCountDetailActivity.this.startActivityForResult(localIntent, 1);
      }
    });
    ((ImageButton)super.findViewById(2131296257)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        RankCountDetailActivity.this.setResult(-1);
        RankCountDetailActivity.this.close();
      }
    }());
    setListData(this.curDate, this.itemName);
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
  
  protected void setListData(String paramString1, String paramString2)
  {
    this.itemAccess = new ItemTableAccess(this.sqlHelper.getReadableDatabase());
    this.list = this.itemAccess.findRankPriceByDate(paramString1, paramString2);
    this.itemAccess.close();
    this.adapter = new SimpleAdapter(this, this.list, 2130903074, new String[] { "itemname", "itembuydate", "itemprice" }, new int[] { 2131296440, 2131296442, 2131296443 });
    this.listRankCountDetail.setAdapter(this.adapter);
    if (this.list.size() == 0)
    {
      this.layNoItem.setVisibility(0);
      return;
    }
    this.layNoItem.setVisibility(8);
  }
}


/* Location:           D:\fxlandroid\document\app\AALifeNew_v3.1.4\classes_dex2jar.jar
 * Qualified Name:     com.aalife.android.RankCountDetailActivity
 * JD-Core Version:    0.7.0-SNAPSHOT-20130630
 */