package com.aalife.android;

import android.app.Activity;
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

public class RegisterActivity
  extends Activity
{
  private EditText etUserEmail = null;
  private EditText etUserName = null;
  private EditText etUserNickName = null;
  private EditText etUserPass = null;
  private MyHandler myHandler = new MyHandler(this);
  private ProgressBar pbUserLoading = null;
  private String[] result = null;
  
  protected void close()
  {
    finish();
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903053);
    ((TextView)super.findViewById(2131296320)).getPaint().setFakeBoldText(true);
    ((TextView)super.findViewById(2131296322)).getPaint().setFakeBoldText(true);
    ((TextView)super.findViewById(2131296362)).getPaint().setFakeBoldText(true);
    ((TextView)super.findViewById(2131296364)).getPaint().setFakeBoldText(true);
    this.etUserName = ((EditText)super.findViewById(2131296321));
    this.etUserPass = ((EditText)super.findViewById(2131296323));
    this.etUserNickName = ((EditText)super.findViewById(2131296363));
    this.etUserEmail = ((EditText)super.findViewById(2131296365));
    this.pbUserLoading = ((ProgressBar)super.findViewById(2131296326));
    ((ImageButton)super.findViewById(2131296257)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        RegisterActivity.this.close();
      }
    }());
    ((Button)super.findViewById(2131296319)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        final String str1 = RegisterActivity.this.etUserName.getText().toString().trim();
        if (str1.equals(""))
        {
          Toast.makeText(RegisterActivity.this, RegisterActivity.this.getString(2131099781) + RegisterActivity.this.getString(2131099775), 0).show();
          return;
        }
        final String str2 = RegisterActivity.this.etUserPass.getText().toString().trim();
        if (str2.equals(""))
        {
          Toast.makeText(RegisterActivity.this, RegisterActivity.this.getString(2131099782) + RegisterActivity.this.getString(2131099775), 0).show();
          return;
        }
        final String str3 = RegisterActivity.this.etUserNickName.getText().toString().trim();
        final String str4 = RegisterActivity.this.etUserEmail.getText().toString().trim();
        RegisterActivity.this.pbUserLoading.setVisibility(0);
        (new Runnable()
        {
          public void run()
          {
            RegisterActivity.this.result = UtilityHelper.addUser(str1, str2, str3, str4);
            if (!str3.equals("")) {
              UtilityHelper.sendEmail(str3);
            }
            Message localMessage = new Message();
            localMessage.what = 1;
            RegisterActivity.this.myHandler.sendMessage(localMessage);
          }
        }).start();
      }
    }());
  }
  
  static class MyHandler
    extends Handler
  {
    WeakReference<RegisterActivity> myActivity = null;
    
    MyHandler(RegisterActivity paramRegisterActivity)
    {
      this.myActivity = new WeakReference(paramRegisterActivity);
    }
    
    public void handleMessage(Message paramMessage)
    {
      RegisterActivity localRegisterActivity = (RegisterActivity)this.myActivity.get();
      switch (paramMessage.what)
      {
      default: 
        return;
      }
      localRegisterActivity.pbUserLoading.setVisibility(8);
      if (localRegisterActivity.result[3].equals("1"))
      {
        Toast.makeText(localRegisterActivity, localRegisterActivity.getString(2131099796), 0).show();
        return;
      }
      if (localRegisterActivity.result[0].equals("0"))
      {
        Toast.makeText(localRegisterActivity, localRegisterActivity.getString(2131099790), 0).show();
        return;
      }
      Toast.makeText(localRegisterActivity, localRegisterActivity.getString(2131099792), 0).show();
      localRegisterActivity.close();
    }
  }
}


/* Location:           D:\fxlandroid\document\app\AALifeNew_v3.1.4\classes_dex2jar.jar
 * Qualified Name:     com.aalife.android.RegisterActivity
 * JD-Core Version:    0.7.0-SNAPSHOT-20130630
 */