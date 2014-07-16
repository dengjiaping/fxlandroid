package com.aalife.android;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.lang.ref.WeakReference;

public class StartActivity
  extends Activity
{
  private ImageButton btnLockGo = null;
  private EditText etLockText = null;
  private ItemTableAccess itemAccess = null;
  private LinearLayout layLock = null;
  private MyHandler myHandler = new MyHandler(this);
  private ProgressBar pbStart = null;
  private SharedHelper sharedHelper = null;
  private SQLiteOpenHelper sqlHelper = null;
  private TextView tvStartLabel = null;
  
  protected void close()
  {
    finish();
  }
  
  protected void downNewFile()
  {
    (new Runnable()
    {
      public void run()
      {
        try
        {
          Uri localUri = Uri.fromFile(UtilityHelper.getInstallFile(StartActivity.this));
          Intent localIntent = new Intent("android.intent.action.VIEW");
          localIntent.setDataAndType(localUri, "application/vnd.android.package-archive");
          StartActivity.this.startActivity(localIntent);
          bool = true;
          Bundle localBundle = new Bundle();
          localBundle.putBoolean("result", bool);
          Message localMessage = new Message();
          localMessage.what = 4;
          localMessage.setData(localBundle);
          StartActivity.this.myHandler.sendMessage(localMessage);
          return;
        }
        catch (Exception localException)
        {
          for (;;)
          {
            localException.printStackTrace();
            boolean bool = false;
          }
        }
      }
    }).start();
  }
  
  protected void jumpActivity()
  {
    final String str = this.sharedHelper.getLockText();
    if (!str.equals(""))
    {
      this.tvStartLabel.setText(2131099810);
      this.layLock.setVisibility(0);
      this.pbStart.setVisibility(8);
      this.btnLockGo.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          if (StartActivity.this.etLockText.getText().toString().equals(str))
          {
            Intent localIntent = new Intent(StartActivity.this, MainActivity.class);
            StartActivity.this.startActivity(localIntent);
            StartActivity.this.close();
            return;
          }
          StartActivity.this.tvStartLabel.setText(2131099811);
        }
      });
      return;
    }
    startActivity(new Intent(this, MainActivity.class));
    close();
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903055);
    this.sqlHelper = new DatabaseHelper(this);
    this.sqlHelper.getWritableDatabase();
    this.sqlHelper.close();
    this.sharedHelper = new SharedHelper(this);
    this.tvStartLabel = ((TextView)super.findViewById(2131296372));
    this.layLock = ((LinearLayout)super.findViewById(2131296374));
    this.etLockText = ((EditText)super.findViewById(2131296375));
    this.btnLockGo = ((ImageButton)super.findViewById(2131296376));
    this.pbStart = ((ProgressBar)super.findViewById(2131296373));
    this.pbStart.setVisibility(0);
    if (!this.sharedHelper.getFixSync())
    {
      this.itemAccess = new ItemTableAccess(this.sqlHelper.getReadableDatabase());
      this.itemAccess.fixSyncStatus();
      this.itemAccess.close();
      this.sharedHelper.setFixSync(Boolean.valueOf(true));
      this.sharedHelper.setSyncStatus(getString(2131099708));
      this.sharedHelper.setLocalSync(Boolean.valueOf(true));
    }
    String str = this.sharedHelper.getWelcomeText();
    if (str.equals(""))
    {
      str = getString(2131099650);
      this.sharedHelper.setWelcomeText(str);
    }
    this.tvStartLabel.setText(str);
    if (UtilityHelper.checkInternet(this))
    {
      if (!this.sharedHelper.getSyncing())
      {
        (new Runnable()
        {
          public void run()
          {
            try
            {
              boolean bool2 = UtilityHelper.checkNewVersion(StartActivity.this);
              bool1 = bool2;
              Bundle localBundle = new Bundle();
              localBundle.putBoolean("result", bool1);
              Message localMessage = new Message();
              localMessage.what = 3;
              localMessage.setData(localBundle);
              StartActivity.this.myHandler.sendMessage(localMessage);
              return;
            }
            catch (Exception localException)
            {
              for (;;)
              {
                localException.printStackTrace();
                boolean bool1 = false;
              }
            }
          }
        }).start();
        return;
      }
      jumpActivity();
      return;
    }
    this.myHandler.postDelayed(new Runnable()
    {
      public void run()
      {
        StartActivity.this.jumpActivity();
      }
    }, 2000L);
  }
  
  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    getMenuInflater().inflate(2131230721, paramMenu);
    return true;
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if (paramInt == 4) {
      return false;
    }
    return super.onKeyDown(paramInt, paramKeyEvent);
  }
  
  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    if (UtilityHelper.startBackup(this)) {
      close();
    }
    return true;
  }
  
  static class MyHandler
    extends Handler
  {
    WeakReference<StartActivity> myActivity = null;
    
    MyHandler(StartActivity paramStartActivity)
    {
      this.myActivity = new WeakReference(paramStartActivity);
    }
    
    public void handleMessage(Message paramMessage)
    {
      final StartActivity localStartActivity = (StartActivity)this.myActivity.get();
      switch (paramMessage.what)
      {
      default: 
        return;
      case 3: 
        if (paramMessage.getData().getBoolean("result"))
        {
          new AlertDialog.Builder(localStartActivity).setCancelable(false).setTitle(2131099800).setMessage(2131099778).setPositiveButton(2131099802, new DialogInterface.OnClickListener()
          {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
            {
              localStartActivity.tvStartLabel.setText(2131099779);
              localStartActivity.downNewFile();
            }
          }).setNegativeButton(2131099803, new DialogInterface.OnClickListener()
          {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
            {
              paramAnonymousDialogInterface.cancel();
              localStartActivity.jumpActivity();
            }
          }).create().show();
          return;
        }
        if (localStartActivity.sharedHelper.getLockText().equals("")) {
          localStartActivity.close();
        }
        localStartActivity.jumpActivity();
        return;
      }
      if (paramMessage.getData().getBoolean("result")) {
        localStartActivity.close();
      }
      for (;;)
      {
        localStartActivity.pbStart.setVisibility(8);
        return;
        localStartActivity.tvStartLabel.setText(2131099780);
        localStartActivity.jumpActivity();
      }
    }
  }
}


/* Location:           D:\fxlandroid\document\app\AALifeNew_v3.1.4\classes_dex2jar.jar
 * Qualified Name:     com.aalife.android.StartActivity
 * JD-Core Version:    0.7.0-SNAPSHOT-20130630
 */