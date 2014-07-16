package com.aalife.android;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import java.util.List;
import java.util.Map;

public class DayAdapter
  extends BaseAdapter
{
  private final int FIRST_REQUEST_CODE = 1;
  private DayActivity activity = null;
  private Context context = null;
  private ItemTableAccess itemAccess = null;
  private LayoutInflater layout = null;
  private List<Map<String, String>> list = null;
  private SharedHelper sharedHelper = null;
  private SimpleAdapter simpleAdapter = null;
  private SQLiteOpenHelper sqlHelper = null;
  
  public DayAdapter(Context paramContext, List<Map<String, String>> paramList)
  {
    this.context = paramContext;
    this.list = paramList;
    this.layout = LayoutInflater.from(this.context);
    this.sqlHelper = new DatabaseHelper(this.context);
    this.sharedHelper = new SharedHelper(this.context);
    this.activity = ((DayActivity)paramContext);
  }
  
  public int getCount()
  {
    return this.list.size();
  }
  
  public Object getItem(int paramInt)
  {
    return this.list.get(paramInt);
  }
  
  public long getItemId(int paramInt)
  {
    return paramInt;
  }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    ItemEntity localItemEntity;
    if (paramView == null)
    {
      localItemEntity = new ItemEntity(null);
      paramView = this.layout.inflate(2130903065, null);
      localItemEntity.tvItemBuyDate = ((TextView)paramView.findViewById(2131296418));
      localItemEntity.tvTotalPrice = ((TextView)paramView.findViewById(2131296419));
      localItemEntity.lvDayListSub = ((MyListView)paramView.findViewById(2131296420));
      paramView.setTag(localItemEntity);
    }
    List localList;
    final boolean[] arrayOfBoolean;
    int i;
    for (;;)
    {
      localItemEntity.tvItemBuyDate.getPaint().setFakeBoldText(true);
      localItemEntity.tvTotalPrice.getPaint().setFakeBoldText(true);
      String str1 = (String)((Map)this.list.get(paramInt)).get("itembuydate");
      localItemEntity.tvItemBuyDate.setText(UtilityHelper.formatDate(str1, "y-m-d-w2"));
      String str2 = (String)((Map)this.list.get(paramInt)).get("pricevalue");
      localItemEntity.tvTotalPrice.setText(this.context.getString(2131099777) + " " + str2);
      this.itemAccess = new ItemTableAccess(this.sqlHelper.getReadableDatabase());
      localList = this.itemAccess.findItemByDate(str1);
      Map localMap = this.itemAccess.findAllMonth(str1);
      this.itemAccess.close();
      arrayOfBoolean = new boolean[localList.size()];
      i = 0;
      if (i < localList.size()) {
        break;
      }
      this.activity.setTotalData(str1, (String)localMap.get("price"));
      this.simpleAdapter = new SimpleAdapter(this.context, localList, 2130903067, new String[] { "itemid", "itemname", "itemprice", "pricevalue" }, new int[] { 2131296428, 2131296423, 2131296424, 2131296425 })
      {
        public View getView(int paramAnonymousInt, View paramAnonymousView, ViewGroup paramAnonymousViewGroup)
        {
          if (paramAnonymousView != null) {}
          for (View localView = paramAnonymousView;; localView = super.getView(paramAnonymousInt, paramAnonymousView, paramAnonymousViewGroup))
          {
            final int i = Integer.parseInt(((TextView)localView.findViewById(2131296428)).getText().toString());
            final CheckBox localCheckBox = (CheckBox)localView.findViewById(2131296427);
            localCheckBox.setChecked(arrayOfBoolean[paramAnonymousInt]);
            localCheckBox.setOnClickListener(new View.OnClickListener()
            {
              public void onClick(View paramAnonymous2View)
              {
                DayAdapter.this.itemAccess = new ItemTableAccess(DayAdapter.this.sqlHelper.getReadableDatabase());
                if (localCheckBox.isChecked()) {
                  DayAdapter.this.itemAccess.updateItemRecommend(i, 1);
                }
                for (;;)
                {
                  DayAdapter.this.itemAccess.close();
                  DayAdapter.this.sharedHelper.setLocalSync(Boolean.valueOf(true));
                  DayAdapter.this.sharedHelper.setSyncStatus(DayAdapter.this.context.getString(2131099708));
                  return;
                  DayAdapter.this.itemAccess.updateItemRecommend(i, 0);
                }
              }
            });
            return localView;
          }
        }
      };
      localItemEntity.lvDayListSub.setAdapter(this.simpleAdapter);
      localItemEntity.lvDayListSub.setOnItemClickListener(new AdapterView.OnItemClickListener()
      {
        TextView tvItemName = null;
        TextView tvItemPrice = null;
        
        public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
        {
          Map localMap = (Map)((ListView)paramAnonymousAdapterView).getItemAtPosition(paramAnonymousInt);
          String[] arrayOfString = new String[5];
          arrayOfString[0] = ((String)localMap.get("itemid"));
          arrayOfString[1] = ((String)localMap.get("catid"));
          arrayOfString[2] = ((String)localMap.get("itemname"));
          arrayOfString[3] = UtilityHelper.formatDouble(Double.parseDouble((String)localMap.get("pricevalue")), "0.###");
          arrayOfString[4] = ((String)localMap.get("itembuydate"));
          this.tvItemName = ((TextView)paramAnonymousView.findViewById(2131296423));
          this.tvItemName.setTextColor(DayAdapter.this.activity.getResources().getColor(2130968583));
          this.tvItemPrice = ((TextView)paramAnonymousView.findViewById(2131296424));
          this.tvItemPrice.setTextColor(DayAdapter.this.activity.getResources().getColor(2130968583));
          Intent localIntent = new Intent(DayAdapter.this.activity, EditActivity.class);
          localIntent.putExtra("items", arrayOfString);
          DayAdapter.this.activity.startActivityForResult(localIntent, 1);
        }
      });
      return paramView;
      localItemEntity = (ItemEntity)paramView.getTag();
    }
    if (((String)((Map)localList.get(i)).get("recommend")).toString().equals("0")) {}
    for (int j = 0;; j = 1)
    {
      arrayOfBoolean[i] = j;
      i++;
      break;
    }
  }
  
  protected void updateData(List<Map<String, String>> paramList)
  {
    this.list = paramList;
    notifyDataSetChanged();
  }
  
  private class ItemEntity
  {
    private MyListView lvDayListSub;
    private TextView tvItemBuyDate;
    private TextView tvTotalPrice;
    
    private ItemEntity() {}
  }
}


/* Location:           D:\fxlandroid\document\app\AALifeNew_v3.1.4\classes_dex2jar.jar
 * Qualified Name:     com.aalife.android.DayAdapter
 * JD-Core Version:    0.7.0-SNAPSHOT-20130630
 */