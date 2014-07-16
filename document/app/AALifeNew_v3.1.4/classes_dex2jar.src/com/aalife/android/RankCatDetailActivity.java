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

public class RankCatDetailActivity
  extends Activity
{
  private final int FIRST_REQUEST_CODE = 1;
  private SimpleAdapter adapter = null;
  private int catId = 0;
  private String curDate = "";
  private ItemTableAccess itemAccess = null;
  private LinearLayout layNoItem = null;
  private List<Map<String, String>> list = null;
  private ListView listRankCatDetail = null;
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
    setContentView(2130903051);
    ((TextView)super.findViewById(2131296287)).getPaint().setFakeBoldText(true);
    ((TextView)super.findViewById(2131296357)).getPaint().setFakeBoldText(true);
    ((TextView)super.findViewById(2131296358)).getPaint().setFakeBoldText(true);
    this.sqlHelper = new DatabaseHelper(this);
    Intent localIntent = super.getIntent();
    this.catId = localIntent.getIntExtra("catid", 1);
    this.curDate = localIntent.getStringExtra("date");
    this.listRankCatDetail = ((ListView)super.findViewById(2131296359));
    this.listRankCatDetail.setDivider(null);
    this.layNoItem = ((LinearLayout)super.findViewById(2131296292));
    this.listRankCatDetail.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
        String str = (String)((Map)((ListView)paramAnonymousAdapterView).getItemAtPosition(paramAnonymousInt)).get("itemname");
        ((TextView)paramAnonymousView.findViewById(2131296440)).setBackgroundColor(RankCatDetailActivity.this.getResources().getColor(2130968577));
        ((TextView)paramAnonymousView.findViewById(2131296441)).setBackgroundColor(RankCatDetailActivity.this.getResources().getColor(2130968577));
        ((TextView)paramAnonymousView.findViewById(2131296439)).setBackgroundColor(RankCatDetailActivity.this.getResources().getColor(2130968577));
        Intent localIntent = new Intent(RankCatDetailActivity.this, RankCountDetailActivity.class);
        localIntent.putExtra("itemname", str);
        localIntent.putExtra("date", RankCatDetailActivity.this.curDate);
        RankCatDetailActivity.this.startActivityForResult(localIntent, 1);
      }
    });
    ((ImageButton)super.findViewById(2131296257)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        RankCatDetailActivity.this.setResult(-1);
        RankCatDetailActivity.this.close();
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
    this.list = this.itemAccess.findRankCountByDate(paramString, this.catId);
    this.itemAccess.close();
    this.adapter = new SimpleAdapter(this, this.list, 2130903072, new String[] { "itemname", "count", "price" }, new int[] { 2131296440, 2131296441, 2131296439 });
    this.listRankCatDetail.setAdapter(this.adapter);
    if (this.list.size() == 0)
    {
      this.layNoItem.setVisibility(0);
      return;
    }
    this.layNoItem.setVisibility(8);
  }
}


/* Location:           D:\fxlandroid\document\app\AALifeNew_v3.1.4\classes_dex2jar.jar
 * Qualified Name:     com.aalife.android.RankCatDetailActivity
 * JD-Core Version:    0.7.0-SNAPSHOT-20130630
 */