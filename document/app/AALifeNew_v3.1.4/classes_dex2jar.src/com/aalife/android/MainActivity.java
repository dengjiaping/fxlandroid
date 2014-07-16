package com.aalife.android;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

public class MainActivity
  extends Activity
{
  private final int DELAY_TIME = 5000;
  private final int EGG_NUM = 8;
  private final int FIRST_REQUEST_CODE = 1;
  private SimpleAdapter adapter = null;
  private String curDate = "";
  private int eggCount = 0;
  private String fromDate = "";
  private ItemTableAccess itemAccess = null;
  private ImageView ivUserImage = null;
  private List<Map<String, String>> list = null;
  private ListView listTotal = null;
  private MyHandler myHandler = new MyHandler(this);
  private ProgressBar pbHomeSync = null;
  private Runnable runnable = null;
  private SharedHelper sharedHelper = null;
  private SQLiteOpenHelper sqlHelper = null;
  private SyncHelper syncHelper = null;
  private TextView tvDateChoose = null;
  private TextView tvLabGroup = null;
  private TextView tvLabStatus = null;
  private TextView tvLabUserName = null;
  private TextView tvTitleLogin = null;
  private TextView tvTitleUserEdit = null;
  
  protected void close()
  {
    finish();
  }
  
  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if ((paramInt1 == 1) && (paramInt2 == -1) && (paramIntent != null)) {
      this.curDate = paramIntent.getStringExtra("date");
    }
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903048);
    ((TextView)super.findViewById(2131296328)).getPaint().setFakeBoldText(true);
    ((TextView)super.findViewById(2131296341)).getPaint().setFakeBoldText(true);
    this.sharedHelper = new SharedHelper(this);
    this.syncHelper = new SyncHelper(this);
    this.pbHomeSync = ((ProgressBar)super.findViewById(2131296338));
    this.tvLabStatus = ((TextView)super.findViewById(2131296336));
    this.listTotal = ((ListView)super.findViewById(2131296343));
    this.ivUserImage = ((ImageView)super.findViewById(2131296332));
    this.sqlHelper = new DatabaseHelper(this);
    if (!this.sharedHelper.getRestore())
    {
      new AlertDialog.Builder(this).setCancelable(false).setTitle(2131099800).setMessage(2131099809).setPositiveButton(2131099804, new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          if (UtilityHelper.startRestore(MainActivity.this) == 1)
          {
            MainActivity.this.itemAccess = new ItemTableAccess(MainActivity.this.sqlHelper.getReadableDatabase());
            boolean bool = MainActivity.this.itemAccess.hasSyncItem();
            MainActivity.this.itemAccess.close();
            if (bool)
            {
              MainActivity.this.sharedHelper.setLocalSync(Boolean.valueOf(true));
              MainActivity.this.sharedHelper.setSyncStatus(MainActivity.this.getString(2131099711));
            }
            MainActivity.this.sharedHelper.setHasRestore(Boolean.valueOf(true));
            MainActivity.this.onResume();
          }
        }
      }).setNegativeButton(2131099805, new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          MainActivity.this.sharedHelper.setHasRestore(Boolean.valueOf(false));
          MainActivity.this.sharedHelper.setSyncStatus(MainActivity.this.getString(2131099712));
          paramAnonymousDialogInterface.cancel();
        }
      }).create().show();
      this.sharedHelper.setRestore(Boolean.valueOf(true));
    }
    if ((this.sharedHelper.getLogin()) && (!this.sharedHelper.getSyncing())) {
      (new Runnable()
      {
        public void run()
        {
          int i = MainActivity.this.syncHelper.checkSyncWeb();
          Bundle localBundle = new Bundle();
          localBundle.putInt("result", i);
          Message localMessage = new Message();
          localMessage.what = 2;
          localMessage.setData(localBundle);
          MainActivity.this.myHandler.sendMessage(localMessage);
        }
      }).start();
    }
    this.curDate = UtilityHelper.getCurDate();
    this.tvDateChoose = ((TextView)super.findViewById(2131296342));
    this.tvDateChoose.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        String[] arrayOfString = MainActivity.this.curDate.split("-");
        (MainActivity.this, new DatePickerDialog.OnDateSetListener()
        {
          public void onDateSet(DatePicker paramAnonymous2DatePicker, int paramAnonymous2Int1, int paramAnonymous2Int2, int paramAnonymous2Int3)
          {
            String str = UtilityHelper.formatDate(paramAnonymous2Int1 + "-" + (paramAnonymous2Int2 + 1) + "-" + paramAnonymous2Int3, "");
            MainActivity.this.setListData(str);
          }
        }, Integer.parseInt(arrayOfString[0]), -1 + Integer.parseInt(arrayOfString[1]), Integer.parseInt(arrayOfString[2])).show();
      }
    });
    this.listTotal.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
        if (paramAnonymousInt == 0)
        {
          Intent localIntent = new Intent(MainActivity.this, DayDetailActivity.class);
          localIntent.putExtra("date", MainActivity.this.fromDate);
          MainActivity.this.startActivityForResult(localIntent, 1);
        }
      }
    });
    ((ImageButton)super.findViewById(2131296344)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        Intent localIntent = new Intent(MainActivity.this, DayActivity.class);
        MainActivity.this.startActivity(localIntent);
      }
    }());
    ((ImageButton)super.findViewById(2131296345)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        Intent localIntent = new Intent(MainActivity.this, MonthActivity.class);
        MainActivity.this.startActivity(localIntent);
      }
    }());
    ((ImageButton)super.findViewById(2131296346)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        Intent localIntent = new Intent(MainActivity.this, RankActivity.class);
        MainActivity.this.startActivity(localIntent);
      }
    }());
    ((ImageButton)super.findViewById(2131296347)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        Intent localIntent = new Intent(MainActivity.this, AnalyzeActivity.class);
        MainActivity.this.startActivity(localIntent);
      }
    }());
    this.tvLabUserName = ((TextView)super.findViewById(2131296333));
    this.tvLabGroup = ((TextView)super.findViewById(2131296334));
    if (this.sharedHelper.getUserName().equals(""))
    {
      String str2 = getString(2131099704);
      this.sharedHelper.setUserName(str2);
    }
    this.tvLabUserName.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        MainActivity localMainActivity = MainActivity.this;
        localMainActivity.eggCount = (1 + localMainActivity.eggCount);
        if (MainActivity.this.eggCount == 8)
        {
          MainActivity.this.tvLabUserName.setClickable(false);
          new AlertDialog.Builder(MainActivity.this).setCancelable(false).setTitle(2131099814).setMessage(2131099815).setNegativeButton(2131099802, new DialogInterface.OnClickListener()
          {
            public void onClick(DialogInterface paramAnonymous2DialogInterface, int paramAnonymous2Int)
            {
              paramAnonymous2DialogInterface.cancel();
            }
          }).create().show();
        }
      }
    });
    if (this.sharedHelper.getSyncStatus().equals(""))
    {
      String str1 = getString(2131099710);
      this.sharedHelper.setSyncStatus(str1);
    }
    ((ImageButton)super.findViewById(2131296337)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        if ((MainActivity.this.sharedHelper.getLocalSync()) || (MainActivity.this.sharedHelper.getWebSync()))
        {
          MainActivity.this.pbHomeSync.setVisibility(0);
          MainActivity.this.tvLabStatus.setText(MainActivity.this.getString(2131099707));
          (new Runnable()
          {
            public void run()
            {
              try
              {
                MainActivity.this.myHandler.postDelayed(MainActivity.this.runnable, 5000L);
                MainActivity.this.sharedHelper.setSyncing(Boolean.valueOf(true));
                MainActivity.this.syncHelper.Start();
                Message localMessage = new Message();
                localMessage.what = 1;
                MainActivity.this.myHandler.sendMessage(localMessage);
                return;
              }
              catch (Exception localException)
              {
                for (;;)
                {
                  MainActivity.this.sharedHelper.setSyncing(Boolean.valueOf(false));
                  localException.printStackTrace();
                }
              }
            }
          }).start();
          return;
        }
        Toast.makeText(MainActivity.this, MainActivity.this.getString(2131099710), 0).show();
      }
    }());
    ((ImageButton)super.findViewById(2131296279)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        Intent localIntent = new Intent(MainActivity.this, AddActivity.class);
        localIntent.putExtra("date", MainActivity.this.curDate);
        MainActivity.this.startActivityForResult(localIntent, 1);
      }
    }());
    this.tvTitleLogin = ((TextView)super.findViewById(2131296329));
    this.tvTitleLogin.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        Intent localIntent = new Intent(MainActivity.this, LoginActivity.class);
        MainActivity.this.startActivity(localIntent);
      }
    });
    this.tvTitleUserEdit = ((TextView)super.findViewById(2131296330));
    this.tvTitleUserEdit.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        Intent localIntent = new Intent(MainActivity.this, UserEditActivity.class);
        MainActivity.this.startActivity(localIntent);
      }
    });
    this.runnable = new Runnable()
    {
      public void run()
      {
        Message localMessage = new Message();
        localMessage.what = 3;
        MainActivity.this.myHandler.sendMessage(localMessage);
        MainActivity.this.myHandler.postDelayed(this, 5000L);
      }
    };
  }
  
  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    getMenuInflater().inflate(2131230720, paramMenu);
    return true;
  }
  
  protected void onDestroy()
  {
    super.onDestroy();
    this.myHandler.removeCallbacks(this.runnable);
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if (paramInt == 4)
    {
      if (this.sharedHelper.getSyncing())
      {
        Toast.makeText(this, getString(2131099713), 0).show();
        return false;
      }
      if (UtilityHelper.startBackup(this)) {
        close();
      }
    }
    return super.onKeyDown(paramInt, paramKeyEvent);
  }
  
  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    switch (paramMenuItem.getItemId())
    {
    default: 
    case 2131296450: 
      do
      {
        return false;
      } while (!UtilityHelper.startBackup(this));
      this.sharedHelper.setSyncing(Boolean.valueOf(false));
      close();
      return false;
    case 2131296451: 
      startActivity(new Intent(this, SettingsActivity.class));
      return false;
    }
    new AlertDialog.Builder(this).setTitle(2131099801).setMessage(2131099806).setNegativeButton(2131099802, new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        paramAnonymousDialogInterface.cancel();
      }
    }).create().show();
    return false;
  }
  
  protected void onResume()
  {
    super.onResume();
    setListData(this.curDate);
    Object localObject = this.sharedHelper.getUserName();
    String str1 = this.sharedHelper.getUserNickName();
    TextView localTextView = this.tvLabUserName;
    StringBuilder localStringBuilder = new StringBuilder(String.valueOf(getString(2131099701)));
    if (str1.equals("")) {}
    for (;;)
    {
      localTextView.setText((String)localObject);
      this.tvLabGroup.setText(getString(2131099702) + this.sharedHelper.getGroup());
      if (this.sharedHelper.getLogin())
      {
        this.tvTitleLogin.setVisibility(8);
        this.tvTitleUserEdit.setVisibility(0);
        String str2 = this.sharedHelper.getUserImage();
        if (!str2.equals("")) {
          this.ivUserImage.setImageBitmap(UtilityHelper.getUserImage(this, str2));
        }
      }
      if (this.sharedHelper.getSyncing())
      {
        this.pbHomeSync.setVisibility(0);
        this.sharedHelper.setSyncStatus(getString(2131099707));
      }
      this.tvLabStatus.setText(this.sharedHelper.getSyncStatus());
      return;
      localObject = str1;
    }
  }
  
  protected void setListData(String paramString)
  {
    this.fromDate = paramString;
    this.tvDateChoose.setText(UtilityHelper.formatDate(paramString, "y-m-d-w2"));
    this.itemAccess = new ItemTableAccess(this.sqlHelper.getReadableDatabase());
    this.list = this.itemAccess.findHomeTotalByDate(paramString);
    this.itemAccess.close();
    this.adapter = new SimpleAdapter(this, this.list, 2130903068, new String[] { "curlabel", "curprice", "prevlabel", "prevprice" }, new int[] { 2131296430, 2131296431, 2131296432, 2131296433 });
    this.listTotal.setAdapter(this.adapter);
  }
  
  static class MyHandler
    extends Handler
  {
    WeakReference<MainActivity> myActivity = null;
    
    MyHandler(MainActivity paramMainActivity)
    {
      this.myActivity = new WeakReference(paramMainActivity);
    }
    
    public void handleMessage(Message paramMessage)
    {
      MainActivity localMainActivity = (MainActivity)this.myActivity.get();
      boolean bool = localMainActivity.sharedHelper.getSyncing();
      switch (paramMessage.what)
      {
      default: 
      case 1: 
      case 2: 
        label260:
        do
        {
          return;
          localMainActivity.pbHomeSync.setVisibility(8);
          Object localObject = localMainActivity.sharedHelper.getUserName();
          String str3 = localMainActivity.sharedHelper.getUserNickName();
          TextView localTextView = localMainActivity.tvLabUserName;
          StringBuilder localStringBuilder = new StringBuilder(String.valueOf(localMainActivity.getString(2131099701)));
          String str4;
          if (str3.equals(""))
          {
            localTextView.setText((String)localObject);
            int i = localMainActivity.sharedHelper.getGroup();
            localMainActivity.tvLabGroup.setText(localMainActivity.getString(2131099702) + String.valueOf(i));
            if (localMainActivity.sharedHelper.getLogin())
            {
              localMainActivity.tvTitleLogin.setVisibility(8);
              localMainActivity.tvTitleUserEdit.setVisibility(0);
            }
            str4 = localMainActivity.sharedHelper.getSyncStatus();
            if (bool) {
              break label260;
            }
            str4 = localMainActivity.getString(2131099714);
            localMainActivity.sharedHelper.setSyncStatus(str4);
            Toast.makeText(localMainActivity, str4, 0).show();
          }
          for (;;)
          {
            localMainActivity.tvLabStatus.setText(str4);
            localMainActivity.setListData(localMainActivity.curDate);
            return;
            localObject = str3;
            break;
            localMainActivity.sharedHelper.setSyncing(Boolean.valueOf(false));
          }
        } while ((paramMessage.getData().getInt("result") != 1) || (bool));
        String str2 = localMainActivity.getString(2131099709);
        localMainActivity.sharedHelper.setSyncStatus(str2);
        localMainActivity.sharedHelper.setWebSync(Boolean.valueOf(true));
        localMainActivity.tvLabStatus.setText(str2);
        return;
      }
      String str1 = localMainActivity.sharedHelper.getCurDate();
      if (str1.equals("")) {
        str1 = localMainActivity.curDate;
      }
      localMainActivity.setListData(UtilityHelper.formatDate(str1, "y-m-d"));
    }
  }
}


/* Location:           D:\fxlandroid\document\app\AALifeNew_v3.1.4\classes_dex2jar.jar
 * Qualified Name:     com.aalife.android.MainActivity
 * JD-Core Version:    0.7.0-SNAPSHOT-20130630
 */