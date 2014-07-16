package com.aalife.android;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextPaint;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class SettingsActivity
  extends Activity
{
  private EditText etSetLock = null;
  private EditText etSetWelcome = null;
  private SharedHelper sharedHelper = null;
  
  protected void close()
  {
    finish();
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903054);
    ((TextView)super.findViewById(2131296366)).getPaint().setFakeBoldText(true);
    ((TextView)super.findViewById(2131296368)).getPaint().setFakeBoldText(true);
    this.sharedHelper = new SharedHelper(this);
    this.etSetLock = ((EditText)super.findViewById(2131296367));
    this.etSetWelcome = ((EditText)super.findViewById(2131296369));
    String str1 = this.sharedHelper.getLockText();
    this.etSetLock.setText(str1);
    String str2 = this.sharedHelper.getWelcomeText();
    this.etSetWelcome.setText(str2);
    ((ImageButton)super.findViewById(2131296257)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        SettingsActivity.this.close();
      }
    }());
    ((Button)super.findViewById(2131296370)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        String str1 = SettingsActivity.this.etSetLock.getText().toString();
        SettingsActivity.this.sharedHelper.setLockText(str1);
        String str2 = SettingsActivity.this.etSetWelcome.getText().toString();
        SettingsActivity.this.sharedHelper.setWelcomeText(str2);
        SettingsActivity.this.close();
      }
    }());
  }
}


/* Location:           D:\fxlandroid\document\app\AALifeNew_v3.1.4\classes_dex2jar.jar
 * Qualified Name:     com.aalife.android.SettingsActivity
 * JD-Core Version:    0.7.0-SNAPSHOT-20130630
 */