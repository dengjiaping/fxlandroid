package com.aalife.android;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextPaint;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;
import java.util.Map;

public class CategoryEditActivity
  extends Activity
{
  private SimpleAdapter adapter = null;
  private CategoryTableAccess categoryAccess = null;
  private boolean clicked = false;
  private EditText etCatName = null;
  private LinearLayout layNoItem = null;
  private List<Map<String, String>> list = null;
  private ListView listCategory = null;
  private int position = 0;
  private int saveId = 0;
  private SharedHelper sharedHelper = null;
  private SQLiteOpenHelper sqlHelper = null;
  
  protected void close()
  {
    finish();
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903043);
    ((TextView)super.findViewById(2131296296)).getPaint().setFakeBoldText(true);
    ((TextView)super.findViewById(2131296297)).getPaint().setFakeBoldText(true);
    ((TextView)super.findViewById(2131296298)).getPaint().setFakeBoldText(true);
    ((TextView)super.findViewById(2131296299)).getPaint().setFakeBoldText(true);
    this.sqlHelper = new DatabaseHelper(this);
    this.sharedHelper = new SharedHelper(this);
    this.etCatName = ((EditText)super.findViewById(2131296302));
    this.listCategory = ((ListView)super.findViewById(2131296300));
    this.listCategory.setDivider(null);
    this.layNoItem = ((LinearLayout)super.findViewById(2131296292));
    this.layNoItem.setVisibility(8);
    this.categoryAccess = new CategoryTableAccess(this.sqlHelper.getReadableDatabase());
    this.list = this.categoryAccess.findAllCatEdit();
    this.categoryAccess.close();
    if (this.list.size() <= 0) {
      this.layNoItem.setVisibility(0);
    }
    final boolean[] arrayOfBoolean = new boolean[this.list.size()];
    int i = 0;
    if (i >= this.list.size())
    {
      this.adapter = new SimpleAdapter(this, this.list, 2130903064, new String[] { "id", "catname", "", "delete", "catid", "catdisplay" }, new int[] { 2131296410, 2131296411, 2131296413, 2131296415, 2131296416, 2131296417 })
      {
        public View getView(final int paramAnonymousInt, View paramAnonymousView, ViewGroup paramAnonymousViewGroup)
        {
          View localView = super.getView(paramAnonymousInt, paramAnonymousView, paramAnonymousViewGroup);
          final int i = Integer.parseInt(((TextView)localView.findViewById(2131296416)).getText().toString());
          ((TextView)localView.findViewById(2131296415)).setOnClickListener(new View.OnClickListener()
          {
            public void onClick(View paramAnonymous2View)
            {
              CategoryEditActivity.this.categoryAccess = new CategoryTableAccess(CategoryEditActivity.this.sqlHelper.getReadableDatabase());
              int i = CategoryEditActivity.this.categoryAccess.delCategory(i);
              CategoryEditActivity.this.categoryAccess.close();
              if (i == 1)
              {
                CategoryEditActivity.this.onCreate(null);
                Toast.makeText(CategoryEditActivity.this, CategoryEditActivity.this.getString(2131099755), 0).show();
              }
              for (;;)
              {
                CategoryEditActivity.this.sharedHelper.setLocalSync(Boolean.valueOf(true));
                CategoryEditActivity.this.sharedHelper.setSyncStatus(CategoryEditActivity.this.getString(2131099708));
                return;
                if (i == 2) {
                  Toast.makeText(CategoryEditActivity.this, CategoryEditActivity.this.getString(2131099757), 0).show();
                } else {
                  Toast.makeText(CategoryEditActivity.this, CategoryEditActivity.this.getString(2131099756), 0).show();
                }
              }
            }
          }());
          final CheckBox localCheckBox = (CheckBox)localView.findViewById(2131296413);
          localCheckBox.setChecked(arrayOfBoolean[paramAnonymousInt]);
          localCheckBox.setOnClickListener(new View.OnClickListener()
          {
            public void onClick(View paramAnonymous2View)
            {
              CategoryEditActivity.this.categoryAccess = new CategoryTableAccess(CategoryEditActivity.this.sqlHelper.getReadableDatabase());
              if (localCheckBox.isChecked())
              {
                CategoryEditActivity.this.categoryAccess.updateCatDisplay(i, 1);
                this.val$listCheck[paramAnonymousInt] = true;
              }
              for (;;)
              {
                CategoryEditActivity.this.categoryAccess.close();
                CategoryEditActivity.this.sharedHelper.setLocalSync(Boolean.valueOf(true));
                CategoryEditActivity.this.sharedHelper.setSyncStatus(CategoryEditActivity.this.getString(2131099708));
                return;
                CategoryEditActivity.this.categoryAccess.updateCatDisplay(i, 0);
                this.val$listCheck[paramAnonymousInt] = false;
              }
            }
          });
          TextView localTextView1 = (TextView)localView.findViewById(2131296410);
          TextView localTextView2 = (TextView)localView.findViewById(2131296411);
          LinearLayout localLinearLayout1 = (LinearLayout)localView.findViewById(2131296412);
          LinearLayout localLinearLayout2 = (LinearLayout)localView.findViewById(2131296414);
          if ((CategoryEditActivity.this.clicked) && (CategoryEditActivity.this.position == paramAnonymousInt))
          {
            localTextView1.setBackgroundColor(CategoryEditActivity.this.getResources().getColor(2130968577));
            localTextView2.setBackgroundColor(CategoryEditActivity.this.getResources().getColor(2130968577));
            localLinearLayout1.setBackgroundColor(CategoryEditActivity.this.getResources().getColor(2130968577));
            localLinearLayout2.setBackgroundColor(CategoryEditActivity.this.getResources().getColor(2130968577));
            return localView;
          }
          localTextView1.setBackgroundColor(CategoryEditActivity.this.getResources().getColor(2130968579));
          localTextView2.setBackgroundColor(CategoryEditActivity.this.getResources().getColor(2130968579));
          localLinearLayout1.setBackgroundColor(CategoryEditActivity.this.getResources().getColor(2130968579));
          localLinearLayout2.setBackgroundColor(CategoryEditActivity.this.getResources().getColor(2130968579));
          return localView;
        }
      };
      this.listCategory.setAdapter(this.adapter);
      this.listCategory.setOnItemClickListener(new AdapterView.OnItemClickListener()
      {
        public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
        {
          Map localMap = (Map)((ListView)paramAnonymousAdapterView).getItemAtPosition(paramAnonymousInt);
          CategoryEditActivity.this.saveId = Integer.parseInt((String)localMap.get("catid"));
          String str = (String)localMap.get("catname");
          CategoryEditActivity.this.etCatName.setText(str);
          CategoryEditActivity.this.clicked = true;
          CategoryEditActivity.this.position = paramAnonymousInt;
          CategoryEditActivity.this.adapter.notifyDataSetChanged();
        }
      });
      ((ImageButton)super.findViewById(2131296257)).setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          CategoryEditActivity.this.setResult(-1);
          CategoryEditActivity.this.close();
        }
      }());
      ((Button)super.findViewById(2131296303)).setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          String str = CategoryEditActivity.this.etCatName.getText().toString().trim();
          if (str.equals(""))
          {
            Toast.makeText(CategoryEditActivity.this, CategoryEditActivity.this.getString(2131099724) + CategoryEditActivity.this.getString(2131099775), 0).show();
            return;
          }
          CategoryEditActivity.this.categoryAccess = new CategoryTableAccess(CategoryEditActivity.this.sqlHelper.getReadableDatabase());
          Boolean localBoolean = Boolean.valueOf(CategoryEditActivity.this.categoryAccess.saveCategory(CategoryEditActivity.this.saveId, str));
          CategoryEditActivity.this.categoryAccess.close();
          if (localBoolean.booleanValue())
          {
            CategoryEditActivity.this.sharedHelper.setLocalSync(Boolean.valueOf(true));
            CategoryEditActivity.this.sharedHelper.setSyncStatus(CategoryEditActivity.this.getString(2131099708));
            CategoryEditActivity.this.saveId = 0;
            CategoryEditActivity.this.onCreate(null);
            Toast.makeText(CategoryEditActivity.this, CategoryEditActivity.this.getString(2131099770), 0).show();
            return;
          }
          Toast.makeText(CategoryEditActivity.this, CategoryEditActivity.this.getString(2131099769), 0).show();
        }
      }());
      return;
    }
    if (((String)((Map)this.list.get(i)).get("catdisplay")).toString().equals("0")) {}
    for (int j = 0;; j = 1)
    {
      arrayOfBoolean[i] = j;
      i++;
      break;
    }
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
}


/* Location:           D:\fxlandroid\document\app\AALifeNew_v3.1.4\classes_dex2jar.jar
 * Qualified Name:     com.aalife.android.CategoryEditActivity
 * JD-Core Version:    0.7.0-SNAPSHOT-20130630
 */