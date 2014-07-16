package com.aalife.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SimpleAdapter;
import android.widget.SlidingDrawer;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddActivity
  extends Activity
{
  private final int FIRST_REQUEST_CODE = 1;
  private ArrayAdapter<CharSequence> adapter = null;
  private SimpleAdapter adapterSmart = null;
  private Button btnSmartBack = null;
  private CategoryTableAccess categoryAccess = null;
  private String curDate = "";
  private EditText etAddItemBuyDate = null;
  private AutoCompleteTextView etAddItemName = null;
  private EditText etAddItemPrice = null;
  private String fromDate = "";
  private ItemTableAccess itemAccess = null;
  private List<CharSequence> list = null;
  private ListView listAddSmart = null;
  private List<Map<String, String>> listCat = null;
  private List<Map<String, String>> listDate = null;
  private List<Map<String, String>> listItem = null;
  private List<Map<String, String>> listPrice = null;
  private ArrayAdapter<CharSequence> nameAdapter = null;
  private List<CharSequence> nameList = null;
  private int recommend = 0;
  private int screenWidth = 0;
  private SlidingDrawer sdAddSmart = null;
  private SharedHelper sharedHelper = null;
  private int smartFlag = 1;
  private Spinner spinner = null;
  private SQLiteOpenHelper sqlHelper = null;
  
  protected void close()
  {
    finish();
  }
  
  protected List<Map<String, String>> getListDateData(String paramString)
  {
    ArrayList localArrayList = new ArrayList();
    String str1 = UtilityHelper.getNavDate(paramString, -2, "d");
    HashMap localHashMap1 = new HashMap();
    localHashMap1.put("id", str1);
    localHashMap1.put("name", UtilityHelper.formatDate(str1, "y-m-d-w"));
    localArrayList.add(localHashMap1);
    String str2 = UtilityHelper.getNavDate(paramString, -1, "d");
    HashMap localHashMap2 = new HashMap();
    localHashMap2.put("id", str2);
    localHashMap2.put("name", UtilityHelper.formatDate(str2, "y-m-d-w"));
    localArrayList.add(localHashMap2);
    HashMap localHashMap3 = new HashMap();
    localHashMap3.put("id", paramString);
    localHashMap3.put("name", UtilityHelper.formatDate(paramString, "y-m-d-w"));
    localArrayList.add(localHashMap3);
    String str3 = UtilityHelper.getNavDate(paramString, 1, "d");
    HashMap localHashMap4 = new HashMap();
    localHashMap4.put("id", str3);
    localHashMap4.put("name", UtilityHelper.formatDate(str3, "y-m-d-w"));
    localArrayList.add(localHashMap4);
    String str4 = UtilityHelper.getNavDate(paramString, 2, "d");
    HashMap localHashMap5 = new HashMap();
    localHashMap5.put("id", str4);
    localHashMap5.put("name", UtilityHelper.formatDate(str4, "y-m-d-w"));
    localArrayList.add(localHashMap5);
    return localArrayList;
  }
  
  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if ((paramInt1 == 1) && (paramInt2 == -1)) {
      setListData();
    }
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903040);
    ((TextView)super.findViewById(2131296261)).getPaint().setFakeBoldText(true);
    ((TextView)super.findViewById(2131296264)).getPaint().setFakeBoldText(true);
    ((TextView)super.findViewById(2131296266)).getPaint().setFakeBoldText(true);
    ((TextView)super.findViewById(2131296268)).getPaint().setFakeBoldText(true);
    DatabaseHelper localDatabaseHelper = new DatabaseHelper(this);
    this.sqlHelper = localDatabaseHelper;
    SharedHelper localSharedHelper = new SharedHelper(this);
    this.sharedHelper = localSharedHelper;
    this.spinner = ((Spinner)super.findViewById(2131296262));
    this.etAddItemName = ((AutoCompleteTextView)super.findViewById(2131296265));
    this.etAddItemPrice = ((EditText)super.findViewById(2131296267));
    this.listAddSmart = ((ListView)super.findViewById(2131296275));
    this.sdAddSmart = ((SlidingDrawer)super.findViewById(2131296272));
    this.btnSmartBack = ((Button)super.findViewById(2131296276));
    this.btnSmartBack.setVisibility(8);
    AutoCompleteTextView localAutoCompleteTextView1 = this.etAddItemName;
    View.OnFocusChangeListener local1 = new View.OnFocusChangeListener()
    {
      public void onFocusChange(View paramAnonymousView, boolean paramAnonymousBoolean)
      {
        if (AddActivity.this.sdAddSmart.isOpened()) {
          AddActivity.this.sdAddSmart.animateClose();
        }
      }
    };
    localAutoCompleteTextView1.setOnFocusChangeListener(local1);
    AutoCompleteTextView localAutoCompleteTextView2 = this.etAddItemName;
    View.OnClickListener local2 = new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        if (AddActivity.this.sdAddSmart.isOpened()) {
          AddActivity.this.sdAddSmart.animateClose();
        }
      }
    };
    localAutoCompleteTextView2.setOnClickListener(local2);
    EditText localEditText1 = this.etAddItemPrice;
    View.OnFocusChangeListener local3 = new View.OnFocusChangeListener()
    {
      public void onFocusChange(View paramAnonymousView, boolean paramAnonymousBoolean)
      {
        if (AddActivity.this.sdAddSmart.isOpened()) {
          AddActivity.this.sdAddSmart.animateClose();
        }
      }
    };
    localEditText1.setOnFocusChangeListener(local3);
    EditText localEditText2 = this.etAddItemPrice;
    View.OnClickListener local4 = new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        if (AddActivity.this.sdAddSmart.isOpened()) {
          AddActivity.this.sdAddSmart.animateClose();
        }
      }
    };
    localEditText2.setOnClickListener(local4);
    this.itemAccess = new ItemTableAccess(this.sqlHelper.getReadableDatabase());
    this.nameList = this.itemAccess.findAllItemName();
    this.itemAccess.close();
    ArrayAdapter localArrayAdapter = new ArrayAdapter(this, 17367043, this.nameList);
    this.nameAdapter = localArrayAdapter;
    this.etAddItemName.setAdapter(this.nameAdapter);
    int i = ((ViewGroup.MarginLayoutParams)this.sdAddSmart.getLayoutParams()).topMargin;
    int j = getResources().getDimensionPixelSize(2131034113);
    this.screenWidth = getResources().getDisplayMetrics().widthPixels;
    RelativeLayout.LayoutParams localLayoutParams = new RelativeLayout.LayoutParams(-1, -1);
    localLayoutParams.setMargins(this.screenWidth / 2 - j, i, 0, 0);
    this.sdAddSmart.setLayoutParams(localLayoutParams);
    Intent localIntent = super.getIntent();
    this.recommend = localIntent.getIntExtra("recommend", 0);
    this.fromDate = localIntent.getStringExtra("date");
    if ((this.fromDate != null) && (!this.fromDate.equals(""))) {
      this.curDate = this.fromDate;
    }
    for (;;)
    {
      ListView localListView = this.listAddSmart;
      AdapterView.OnItemClickListener local5 = new AdapterView.OnItemClickListener()
      {
        public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
        {
          new HashMap();
          AddActivity.this.itemAccess = new ItemTableAccess(AddActivity.this.sqlHelper.getReadableDatabase());
          switch (AddActivity.this.smartFlag)
          {
          }
          for (;;)
          {
            AddActivity.this.itemAccess.close();
            return;
            AddActivity.this.btnSmartBack.setVisibility(0);
            Map localMap4 = (Map)AddActivity.this.listCat.get(paramAnonymousInt);
            AddActivity.this.smartFlag = 2;
            AddActivity.this.setCurCategory((String)localMap4.get("name"));
            AddActivity.this.listItem = AddActivity.this.itemAccess.findAllItemByCatId((String)localMap4.get("id"));
            AddActivity.this.setListSmartData(AddActivity.this.listItem);
            continue;
            Map localMap3 = (Map)AddActivity.this.listItem.get(paramAnonymousInt);
            AddActivity.this.smartFlag = 3;
            AddActivity.this.etAddItemName.setText((CharSequence)localMap3.get("name"));
            AddActivity.this.etAddItemName.dismissDropDown();
            AddActivity.this.listPrice = AddActivity.this.itemAccess.findAllPriceByName((String)localMap3.get("name"));
            AddActivity.this.setListSmartData(AddActivity.this.listPrice);
            continue;
            Map localMap2 = (Map)AddActivity.this.listPrice.get(paramAnonymousInt);
            AddActivity.this.smartFlag = 4;
            AddActivity.this.etAddItemPrice.setText((CharSequence)localMap2.get("name"));
            AddActivity.this.listDate = AddActivity.this.getListDateData(AddActivity.this.curDate);
            AddActivity.this.setListSmartData(AddActivity.this.listDate);
            continue;
            Map localMap1 = (Map)AddActivity.this.listDate.get(paramAnonymousInt);
            AddActivity.this.curDate = ((String)localMap1.get("id"));
            AddActivity.this.etAddItemBuyDate.setText((CharSequence)localMap1.get("name"));
            AddActivity.this.sdAddSmart.animateClose();
          }
        }
      };
      localListView.setOnItemClickListener(local5);
      Button localButton1 = this.btnSmartBack;
      View.OnClickListener local6 = new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          switch (AddActivity.this.smartFlag)
          {
          default: 
            return;
          case 2: 
            AddActivity.this.btnSmartBack.setVisibility(8);
            AddActivity.this.smartFlag = 1;
            AddActivity.this.setListSmartData(AddActivity.this.listCat);
            return;
          case 3: 
            AddActivity.this.smartFlag = 2;
            AddActivity.this.setListSmartData(AddActivity.this.listItem);
            return;
          }
          AddActivity.this.smartFlag = 3;
          AddActivity.this.setListSmartData(AddActivity.this.listPrice);
        }
      };
      localButton1.setOnClickListener(local6);
      Button localButton2 = (Button)super.findViewById(2131296270);
      View.OnClickListener local7 = new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          int i = AddActivity.this.saveItem();
          if (i == 1)
          {
            Toast.makeText(AddActivity.this, AddActivity.this.getString(2131099770), 0).show();
            AddActivity.this.etAddItemName.setText("");
            AddActivity.this.etAddItemName.requestFocus();
            AddActivity.this.etAddItemPrice.setText("");
            AddActivity.this.setListSmartData(AddActivity.this.listCat);
            AddActivity.this.smartFlag = 1;
            AddActivity.this.btnSmartBack.setVisibility(8);
          }
          while (i != 0) {
            return;
          }
          Toast.makeText(AddActivity.this, AddActivity.this.getString(2131099769), 0).show();
        }
      };
      localButton2.setOnClickListener(local7);
      Button localButton3 = (Button)super.findViewById(2131296271);
      View.OnClickListener local8 = new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          int i = AddActivity.this.saveItem();
          if (i == 1)
          {
            Toast.makeText(AddActivity.this, AddActivity.this.getString(2131099770), 0).show();
            localIntent = new Intent();
            localIntent.putExtra("date", AddActivity.this.curDate);
            AddActivity.this.setResult(-1, localIntent);
            AddActivity.this.close();
          }
          while (i != 0)
          {
            Intent localIntent;
            return;
          }
          Toast.makeText(AddActivity.this, AddActivity.this.getString(2131099769), 0).show();
        }
      };
      localButton3.setOnClickListener(local8);
      this.etAddItemBuyDate = ((EditText)super.findViewById(2131296269));
      this.etAddItemBuyDate.setText(UtilityHelper.formatDate(this.curDate, "y-m-d-w"));
      EditText localEditText3 = this.etAddItemBuyDate;
      View.OnClickListener local9 = new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          String[] arrayOfString = AddActivity.this.curDate.split("-");
          (AddActivity.this, new DatePickerDialog.OnDateSetListener()
          {
            public void onDateSet(DatePicker paramAnonymous2DatePicker, int paramAnonymous2Int1, int paramAnonymous2Int2, int paramAnonymous2Int3)
            {
              String str = UtilityHelper.formatDate(paramAnonymous2Int1 + "-" + (paramAnonymous2Int2 + 1) + "-" + paramAnonymous2Int3, "");
              AddActivity.this.curDate = str;
              AddActivity.this.etAddItemBuyDate.setText(UtilityHelper.formatDate(AddActivity.this.curDate, "y-m-d-w2"));
            }
          }, Integer.parseInt(arrayOfString[0]), -1 + Integer.parseInt(arrayOfString[1]), Integer.parseInt(arrayOfString[2])).show();
        }
      };
      localEditText3.setOnClickListener(local9);
      ImageButton localImageButton1 = (ImageButton)super.findViewById(2131296257);
      View.OnClickListener local10 = new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          Intent localIntent = new Intent();
          localIntent.putExtra("date", AddActivity.this.curDate);
          AddActivity.this.setResult(-1, localIntent);
          AddActivity.this.close();
        }
      };
      localImageButton1.setOnClickListener(local10);
      ImageButton localImageButton2 = (ImageButton)super.findViewById(2131296259);
      View.OnClickListener local11 = new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          final MyCalcView localMyCalcView = new MyCalcView(AddActivity.this);
          localMyCalcView.setGravity(17);
          final AlertDialog localAlertDialog = new AlertDialog.Builder(AddActivity.this).create();
          localAlertDialog.setTitle(2131099816);
          localAlertDialog.setView(localMyCalcView, 0, 0, 0, 0);
          localAlertDialog.show();
          WindowManager.LayoutParams localLayoutParams = localAlertDialog.getWindow().getAttributes();
          localLayoutParams.width = ((int)(0.95D * AddActivity.this.screenWidth));
          localAlertDialog.getWindow().setAttributes(localLayoutParams);
          ((Button)localMyCalcView.findViewById(2131296384)).setOnClickListener(new View.OnClickListener()
          {
            public void onClick(View paramAnonymous2View)
            {
              String str = localMyCalcView.resultText;
              if (!str.equals(""))
              {
                localAlertDialog.cancel();
                AddActivity.this.etAddItemPrice.setText(str);
                return;
              }
              Toast.makeText(AddActivity.this, 2131099817, 0).show();
            }
          }());
        }
      };
      localImageButton2.setOnClickListener(local11);
      ImageButton localImageButton3 = (ImageButton)super.findViewById(2131296263);
      View.OnClickListener local12 = new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          Intent localIntent = new Intent(AddActivity.this, CategoryEditActivity.class);
          AddActivity.this.startActivityForResult(localIntent, 1);
        }
      };
      localImageButton3.setOnClickListener(local12);
      setListData();
      return;
      if (this.curDate.equals("")) {
        this.curDate = UtilityHelper.getCurDate();
      }
    }
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if (paramInt == 4)
    {
      Intent localIntent = new Intent();
      localIntent.putExtra("date", this.curDate);
      setResult(-1, localIntent);
      close();
    }
    return super.onKeyDown(paramInt, paramKeyEvent);
  }
  
  protected int saveItem()
  {
    try
    {
      String str1 = this.spinner.getSelectedItem().toString();
      this.categoryAccess = new CategoryTableAccess(this.sqlHelper.getReadableDatabase());
      int i = this.categoryAccess.findCatIdByName(str1);
      this.sharedHelper.setCategory(this.spinner.getSelectedItemPosition());
      this.categoryAccess.close();
      String str2 = this.etAddItemName.getText().toString().trim();
      if (str2.equals(""))
      {
        Toast.makeText(this, getString(2131099764) + getString(2131099775), 0).show();
        return 2;
      }
      String str3 = this.etAddItemPrice.getText().toString().trim();
      if (str3.equals(""))
      {
        Toast.makeText(this, getString(2131099765) + getString(2131099775), 0).show();
        return 2;
      }
      String str4 = this.curDate + " " + UtilityHelper.getCurTime();
      this.itemAccess = new ItemTableAccess(this.sqlHelper.getReadableDatabase());
      Boolean localBoolean = Boolean.valueOf(this.itemAccess.addItem(str2, str3, str4, i, this.recommend));
      this.itemAccess.close();
      if (localBoolean.booleanValue())
      {
        this.sharedHelper.setLocalSync(Boolean.valueOf(true));
        this.sharedHelper.setSyncStatus(getString(2131099708));
        return 1;
      }
      return 0;
    }
    catch (Exception localException)
    {
      Toast.makeText(this, getString(2131099763) + getString(2131099775), 0).show();
    }
    return 2;
  }
  
  protected void setCurCategory(String paramString)
  {
    for (int i = 0;; i++)
    {
      if (i >= this.list.size()) {
        return;
      }
      if (((CharSequence)this.list.get(i)).toString().equals(paramString))
      {
        this.spinner.setSelection(i);
        return;
      }
    }
  }
  
  protected void setListData()
  {
    this.categoryAccess = new CategoryTableAccess(this.sqlHelper.getReadableDatabase());
    this.list = this.categoryAccess.findAllCategory();
    this.listCat = this.categoryAccess.findAllCategorySmart();
    this.categoryAccess.close();
    this.adapter = new ArrayAdapter(this, 17367048, this.list);
    this.adapter.setDropDownViewResource(17367049);
    this.spinner.setAdapter(this.adapter);
    int i = this.sharedHelper.getCategory();
    if (i > 0) {
      this.spinner.setSelection(i);
    }
    setListSmartData(this.listCat);
  }
  
  protected void setListSmartData(List<Map<String, String>> paramList)
  {
    this.adapterSmart = new SimpleAdapter(this, paramList, 2130903061, new String[] { "id", "name" }, new int[] { 2131296401, 2131296402 });
    this.listAddSmart.setAdapter(this.adapterSmart);
  }
}


/* Location:           D:\fxlandroid\document\app\AALifeNew_v3.1.4\classes_dex2jar.jar
 * Qualified Name:     com.aalife.android.AddActivity
 * JD-Core Version:    0.7.0-SNAPSHOT-20130630
 */