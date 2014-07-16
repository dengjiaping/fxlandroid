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
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

public class EditActivity
  extends Activity
{
  private final int FIRST_REQUEST_CODE = 1;
  private ArrayAdapter<CharSequence> adapter = null;
  private CategoryTableAccess categoryAccess = null;
  private String curDate = "";
  private String curTime = "";
  private EditText etAddItemBuyDate = null;
  private AutoCompleteTextView etAddItemName = null;
  private EditText etAddItemPrice = null;
  private ItemTableAccess itemAccess = null;
  private String itemName = "";
  private String[] items;
  private List<CharSequence> list = null;
  private ArrayAdapter<CharSequence> nameAdapter = null;
  private List<CharSequence> nameList = null;
  private int screenWidth = 0;
  private SharedHelper sharedHelper = null;
  private Spinner spinner = null;
  private SQLiteOpenHelper sqlHelper = null;
  
  protected void close()
  {
    finish();
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
    setContentView(2130903046);
    ((TextView)super.findViewById(2131296261)).getPaint().setFakeBoldText(true);
    ((TextView)super.findViewById(2131296264)).getPaint().setFakeBoldText(true);
    ((TextView)super.findViewById(2131296266)).getPaint().setFakeBoldText(true);
    ((TextView)super.findViewById(2131296268)).getPaint().setFakeBoldText(true);
    DatabaseHelper localDatabaseHelper = new DatabaseHelper(this);
    this.sqlHelper = localDatabaseHelper;
    this.items = super.getIntent().getStringArrayExtra("items");
    SharedHelper localSharedHelper = new SharedHelper(this);
    this.sharedHelper = localSharedHelper;
    this.spinner = ((Spinner)super.findViewById(2131296262));
    this.etAddItemName = ((AutoCompleteTextView)super.findViewById(2131296265));
    this.etAddItemPrice = ((EditText)super.findViewById(2131296267));
    String[] arrayOfString = this.items[4].split(" ");
    this.curDate = arrayOfString[0];
    if (arrayOfString.length > 1) {}
    for (String str = arrayOfString[1];; str = UtilityHelper.getCurTime())
    {
      this.curTime = str;
      this.itemName = this.items[2];
      this.etAddItemName.setText(this.itemName);
      this.etAddItemPrice.setText(this.items[3]);
      this.itemAccess = new ItemTableAccess(this.sqlHelper.getReadableDatabase());
      this.nameList = this.itemAccess.findAllItemName();
      this.itemAccess.close();
      ArrayAdapter localArrayAdapter = new ArrayAdapter(this, 17367043, this.nameList);
      this.nameAdapter = localArrayAdapter;
      this.etAddItemName.setAdapter(this.nameAdapter);
      this.screenWidth = getResources().getDisplayMetrics().widthPixels;
      Button localButton1 = (Button)super.findViewById(2131296318);
      View.OnClickListener local1 = new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          int i = EditActivity.this.updateItem();
          if (i == 1)
          {
            Toast.makeText(EditActivity.this, EditActivity.this.getString(2131099774), 0).show();
            localIntent = new Intent();
            localIntent.putExtra("itemname", EditActivity.this.itemName);
            EditActivity.this.setResult(-1, localIntent);
            EditActivity.this.close();
          }
          while (i != 0)
          {
            Intent localIntent;
            return;
          }
          Toast.makeText(EditActivity.this, EditActivity.this.getString(2131099773), 0).show();
        }
      };
      localButton1.setOnClickListener(local1);
      Button localButton2 = (Button)super.findViewById(2131296317);
      View.OnClickListener local2 = new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          EditActivity.this.itemAccess = new ItemTableAccess(EditActivity.this.sqlHelper.getReadableDatabase());
          Boolean localBoolean = Boolean.valueOf(EditActivity.this.itemAccess.deleteItem(Integer.parseInt(EditActivity.this.items[0])));
          EditActivity.this.itemAccess.close();
          if (localBoolean.booleanValue())
          {
            EditActivity.this.sharedHelper.setLocalSync(Boolean.valueOf(true));
            EditActivity.this.sharedHelper.setSyncStatus(EditActivity.this.getString(2131099708));
            Toast.makeText(EditActivity.this, 2131099755, 0).show();
            EditActivity.this.setResult(-1);
            EditActivity.this.close();
            return;
          }
          Toast.makeText(EditActivity.this, 2131099756, 0).show();
        }
      };
      localButton2.setOnClickListener(local2);
      ImageButton localImageButton1 = (ImageButton)super.findViewById(2131296257);
      View.OnClickListener local3 = new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          Intent localIntent = new Intent();
          localIntent.putExtra("itemname", EditActivity.this.itemName);
          EditActivity.this.setResult(-1, localIntent);
          EditActivity.this.close();
        }
      };
      localImageButton1.setOnClickListener(local3);
      this.etAddItemBuyDate = ((EditText)super.findViewById(2131296269));
      this.etAddItemBuyDate.setText(UtilityHelper.formatDate(this.curDate, "y-m-d-w2"));
      EditText localEditText = this.etAddItemBuyDate;
      View.OnClickListener local4 = new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          String[] arrayOfString = EditActivity.this.curDate.split("-");
          (EditActivity.this, new DatePickerDialog.OnDateSetListener()
          {
            public void onDateSet(DatePicker paramAnonymous2DatePicker, int paramAnonymous2Int1, int paramAnonymous2Int2, int paramAnonymous2Int3)
            {
              String str = UtilityHelper.formatDate(paramAnonymous2Int1 + "-" + (paramAnonymous2Int2 + 1) + "-" + paramAnonymous2Int3, "");
              EditActivity.this.curDate = str;
              EditActivity.this.etAddItemBuyDate.setText(UtilityHelper.formatDate(EditActivity.this.curDate, "y-m-d-w2"));
            }
          }, Integer.parseInt(arrayOfString[0]), -1 + Integer.parseInt(arrayOfString[1]), Integer.parseInt(arrayOfString[2])).show();
        }
      };
      localEditText.setOnClickListener(local4);
      ImageButton localImageButton2 = (ImageButton)super.findViewById(2131296259);
      View.OnClickListener local5 = new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          final MyCalcView localMyCalcView = new MyCalcView(EditActivity.this);
          localMyCalcView.setGravity(17);
          final AlertDialog localAlertDialog = new AlertDialog.Builder(EditActivity.this).create();
          localAlertDialog.setTitle(2131099816);
          localAlertDialog.setView(localMyCalcView, 0, 0, 0, 0);
          localAlertDialog.show();
          WindowManager.LayoutParams localLayoutParams = localAlertDialog.getWindow().getAttributes();
          localLayoutParams.width = ((int)(0.95D * EditActivity.this.screenWidth));
          localAlertDialog.getWindow().setAttributes(localLayoutParams);
          ((Button)localMyCalcView.findViewById(2131296384)).setOnClickListener(new View.OnClickListener()
          {
            public void onClick(View paramAnonymous2View)
            {
              String str = localMyCalcView.resultText;
              if (!str.equals(""))
              {
                localAlertDialog.cancel();
                EditActivity.this.etAddItemPrice.setText(str);
                return;
              }
              Toast.makeText(EditActivity.this, 2131099817, 0).show();
            }
          }());
        }
      };
      localImageButton2.setOnClickListener(local5);
      ImageButton localImageButton3 = (ImageButton)super.findViewById(2131296263);
      View.OnClickListener local6 = new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          Intent localIntent = new Intent(EditActivity.this, CategoryEditActivity.class);
          EditActivity.this.startActivityForResult(localIntent, 1);
        }
      };
      localImageButton3.setOnClickListener(local6);
      setListData();
      return;
    }
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if (paramInt == 4)
    {
      Intent localIntent = new Intent();
      localIntent.putExtra("itemname", this.itemName);
      setResult(-1, localIntent);
      close();
    }
    return super.onKeyDown(paramInt, paramKeyEvent);
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
    this.adapter = new ArrayAdapter(this, 17367048, this.list);
    this.adapter.setDropDownViewResource(17367049);
    this.spinner.setAdapter(this.adapter);
    String str = this.categoryAccess.findCatNameById(Integer.parseInt(this.items[1]));
    this.categoryAccess.close();
    setCurCategory(str);
  }
  
  protected int updateItem()
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
      this.itemName = str2;
      String str3 = this.etAddItemPrice.getText().toString().trim();
      if (str3.equals(""))
      {
        Toast.makeText(this, getString(2131099765) + getString(2131099775), 0).show();
        return 2;
      }
      String str4 = this.curDate + " " + this.curTime;
      this.itemAccess = new ItemTableAccess(this.sqlHelper.getReadableDatabase());
      Boolean localBoolean = Boolean.valueOf(this.itemAccess.updateItem(Integer.parseInt(this.items[0]), str2, str3, str4, i));
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
}


/* Location:           D:\fxlandroid\document\app\AALifeNew_v3.1.4\classes_dex2jar.jar
 * Qualified Name:     com.aalife.android.EditActivity
 * JD-Core Version:    0.7.0-SNAPSHOT-20130630
 */