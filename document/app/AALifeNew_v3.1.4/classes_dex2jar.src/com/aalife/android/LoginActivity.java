package com.aalife.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextPaint;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.lang.ref.WeakReference;

public class LoginActivity
  extends Activity
{
  private EditText etUserName = null;
  private EditText etUserPass = null;
  private MyHandler myHandler = new MyHandler(this);
  private ProgressBar pbUserLoading = null;
  private String[] result = null;
  private SharedHelper sharedHelper = null;
  
  protected void close()
  {
    finish();
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903047);
    ((TextView)super.findViewById(2131296320)).getPaint().setFakeBoldText(true);
    ((TextView)super.findViewById(2131296322)).getPaint().setFakeBoldText(true);
    this.sharedHelper = new SharedHelper(this);
    this.etUserName = ((EditText)super.findViewById(2131296321));
    this.etUserPass = ((EditText)super.findViewById(2131296323));
    this.pbUserLoading = ((ProgressBar)super.findViewById(2131296326));
    ((ImageButton)super.findViewById(2131296257)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        LoginActivity.this.close();
      }
    }());
    ((Button)super.findViewById(2131296324)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        final String str1 = LoginActivity.this.etUserName.getText().toString().trim();
        if (str1.equals(""))
        {
          Toast.makeText(LoginActivity.this, LoginActivity.this.getString(2131099781) + LoginActivity.this.getString(2131099775), 0).show();
          return;
        }
        final String str2 = LoginActivity.this.etUserPass.getText().toString().trim();
        if (str2.equals(""))
        {
          Toast.makeText(LoginActivity.this, LoginActivity.this.getString(2131099782) + LoginActivity.this.getString(2131099775), 0).show();
          return;
        }
        LoginActivity.this.pbUserLoading.setVisibility(0);
        (new Runnable()
        {
          public void run()
          {
            LoginActivity.this.result = UtilityHelper.loginUser(str1, str2);
            if ((!LoginActivity.this.result[7].equals("")) && (Boolean.valueOf(UtilityHelper.loadBitmap(LoginActivity.this, LoginActivity.this.result[7])).booleanValue())) {
              LoginActivity.this.sharedHelper.setUserImage(LoginActivity.this.result[7]);
            }
            Message localMessage = new Message();
            localMessage.what = 1;
            LoginActivity.this.myHandler.sendMessage(localMessage);
          }
        }).start();
      }
    }());
    ((ImageButton)super.findViewById(2131296319)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        Intent localIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        LoginActivity.this.startActivity(localIntent);
      }
    }());
  }
  
  static class MyHandler
    extends Handler
  {
    WeakReference<LoginActivity> myActivity = null;
    
    MyHandler(LoginActivity paramLoginActivity)
    {
      this.myActivity = new WeakReference(paramLoginActivity);
    }
    
    public void handleMessage(Message paramMessage)
    {
      LoginActivity localLoginActivity = (LoginActivity)this.myActivity.get();
      switch (paramMessage.what)
      {
      default: 
        return;
      }
      localLoginActivity.pbUserLoading.setVisibility(8);
      if (localLoginActivity.result[3].equals("1"))
      {
        Toast.makeText(localLoginActivity, localLoginActivity.getString(2131099796), 0).show();
        return;
      }
      if (localLoginActivity.result[0].equals("0"))
      {
        Toast.makeText(localLoginActivity, localLoginActivity.getString(2131099789), 0).show();
        return;
      }
      String str1 = localLoginActivity.etUserName.getText().toString().trim();
      String str2 = localLoginActivity.etUserPass.getText().toString().trim();
      localLoginActivity.sharedHelper.setGroup(Integer.parseInt(localLoginActivity.result[0]));
      localLoginActivity.sharedHelper.setUserId(Integer.parseInt(localLoginActivity.result[1]));
      localLoginActivity.sharedHelper.setUserName(str1);
      localLoginActivity.sharedHelper.setUserPass(str2);
      localLoginActivity.sharedHelper.setUserNickName(localLoginActivity.result[4]);
      localLoginActivity.sharedHelper.setUserEmail(localLoginActivity.result[5]);
      if (!localLoginActivity.sharedHelper.getHasRestore())
      {
        localLoginActivity.sharedHelper.setWebSync(Boolean.valueOf(true));
        localLoginActivity.sharedHelper.setSyncStatus(localLoginActivity.getString(2131099709));
      }
      for (;;)
      {
        localLoginActivity.sharedHelper.setLogin(Boolean.valueOf(true));
        Toast.makeText(localLoginActivity, localLoginActivity.getString(2131099791), 0).show();
        localLoginActivity.close();
        return;
        if (localLoginActivity.result[2].equals("1"))
        {
          localLoginActivity.sharedHelper.setWebSync(Boolean.valueOf(true));
          localLoginActivity.sharedHelper.setSyncStatus(localLoginActivity.getString(2131099709));
        }
        localLoginActivity.sharedHelper.setFirstSync(Boolean.valueOf(true));
      }
    }
  }
}


/* Location:           D:\fxlandroid\document\app\AALifeNew_v3.1.4\classes_dex2jar.jar
 * Qualified Name:     com.aalife.android.LoginActivity
 * JD-Core Version:    0.7.0-SNAPSHOT-20130630
 */