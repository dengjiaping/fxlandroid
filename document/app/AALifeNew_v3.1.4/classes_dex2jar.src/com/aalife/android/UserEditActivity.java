package com.aalife.android;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Images.Media;
import android.text.Editable;
import android.text.TextPaint;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.lang.ref.WeakReference;

public class UserEditActivity
  extends Activity
{
  private static int RESULT_LOAD_IMAGE = 1;
  private Button btnUserEdit = null;
  private EditText etUserEmail = null;
  private EditText etUserName = null;
  private EditText etUserNickName = null;
  private EditText etUserPass = null;
  private ImageView ivUserImage = null;
  private MyHandler myHandler = new MyHandler(this);
  private ProgressBar pbUserLoading = null;
  private String[] result = null;
  private SharedHelper sharedHelper = null;
  private int userImageSize = 0;
  
  protected void close()
  {
    finish();
  }
  
  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    Uri localUri;
    ContentResolver localContentResolver;
    Cursor localCursor;
    if ((paramInt1 == RESULT_LOAD_IMAGE) && (paramInt2 == -1) && (paramIntent != null))
    {
      localUri = paramIntent.getData();
      localContentResolver = getContentResolver();
      localCursor = null;
    }
    try
    {
      localCursor = localContentResolver.query(localUri, null, null, null, null);
      localCursor.moveToFirst();
      String str1 = UtilityHelper.getFileExtName(localCursor.getString(3));
      Bitmap localBitmap1;
      Bitmap localBitmap2;
      final String str2;
      return;
    }
    catch (Exception localException1)
    {
      localException1 = localException1;
      localException1.printStackTrace();
      return;
    }
    finally
    {
      localCursor.close();
    }
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903056);
    ((TextView)super.findViewById(2131296320)).getPaint().setFakeBoldText(true);
    ((TextView)super.findViewById(2131296322)).getPaint().setFakeBoldText(true);
    ((TextView)super.findViewById(2131296362)).getPaint().setFakeBoldText(true);
    ((TextView)super.findViewById(2131296364)).getPaint().setFakeBoldText(true);
    this.sharedHelper = new SharedHelper(this);
    this.pbUserLoading = ((ProgressBar)super.findViewById(2131296326));
    this.etUserName = ((EditText)super.findViewById(2131296321));
    this.etUserPass = ((EditText)super.findViewById(2131296323));
    this.etUserNickName = ((EditText)super.findViewById(2131296363));
    this.etUserEmail = ((EditText)super.findViewById(2131296365));
    this.userImageSize = getResources().getDimensionPixelSize(2131034114);
    this.pbUserLoading.setVisibility(0);
    this.ivUserImage = ((ImageView)super.findViewById(2131296332));
    this.ivUserImage.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        if (UserEditActivity.this.sharedHelper.getUserId() > 0)
        {
          Intent localIntent = new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
          UserEditActivity.this.startActivityForResult(localIntent, UserEditActivity.RESULT_LOAD_IMAGE);
        }
      }
    });
    ((ImageButton)super.findViewById(2131296257)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        UserEditActivity.this.close();
      }
    }());
    this.btnUserEdit = ((Button)super.findViewById(2131296377));
    this.btnUserEdit.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        final String str1 = UserEditActivity.this.etUserName.getText().toString().trim();
        if (str1.equals(""))
        {
          Toast.makeText(UserEditActivity.this, UserEditActivity.this.getString(2131099781) + UserEditActivity.this.getString(2131099775), 0).show();
          return;
        }
        final String str2 = UserEditActivity.this.etUserPass.getText().toString().trim();
        if (str2.equals(""))
        {
          Toast.makeText(UserEditActivity.this, UserEditActivity.this.getString(2131099782) + UserEditActivity.this.getString(2131099775), 0).show();
          return;
        }
        final String str3 = UserEditActivity.this.etUserNickName.getText().toString().trim();
        final String str4 = UserEditActivity.this.etUserEmail.getText().toString().trim();
        final String str5 = UserEditActivity.this.sharedHelper.getUserImage();
        UserEditActivity.this.pbUserLoading.setVisibility(0);
        (new Runnable()
        {
          public void run()
          {
            int i = UtilityHelper.editUser(UserEditActivity.this.sharedHelper.getUserId(), str1, str2, str3, str5, str4);
            Bundle localBundle = new Bundle();
            localBundle.putInt("result", i);
            Message localMessage = new Message();
            localMessage.what = 1;
            localMessage.setData(localBundle);
            UserEditActivity.this.myHandler.sendMessage(localMessage);
          }
        }).start();
      }
    });
    (new Runnable()
    {
      public void run()
      {
        int i = UserEditActivity.this.sharedHelper.getUserId();
        UserEditActivity.this.result = UtilityHelper.getUser(i);
        if ((!UserEditActivity.this.result[3].equals("")) && (Boolean.valueOf(UtilityHelper.loadBitmap(UserEditActivity.this, UserEditActivity.this.result[3])).booleanValue())) {
          UserEditActivity.this.sharedHelper.setUserImage(UserEditActivity.this.result[3]);
        }
        Message localMessage = new Message();
        localMessage.what = 2;
        UserEditActivity.this.myHandler.sendMessage(localMessage);
      }
    }).start();
    String str = this.sharedHelper.getUserImage();
    if (!str.equals("")) {
      this.ivUserImage.setImageBitmap(UtilityHelper.getUserImage(this, str));
    }
  }
  
  static class MyHandler
    extends Handler
  {
    WeakReference<UserEditActivity> myActivity = null;
    
    MyHandler(UserEditActivity paramUserEditActivity)
    {
      this.myActivity = new WeakReference(paramUserEditActivity);
    }
    
    public void handleMessage(Message paramMessage)
    {
      UserEditActivity localUserEditActivity = (UserEditActivity)this.myActivity.get();
      switch (paramMessage.what)
      {
      default: 
      case 1: 
      case 2: 
        do
        {
          return;
          localUserEditActivity.pbUserLoading.setVisibility(8);
          int i = paramMessage.getData().getInt("result");
          if (i == 2)
          {
            Toast.makeText(localUserEditActivity, localUserEditActivity.getString(2131099796), 0).show();
            return;
          }
          if (i == 1)
          {
            String str2 = localUserEditActivity.etUserName.getText().toString().trim();
            localUserEditActivity.sharedHelper.setUserName(str2);
            String str3 = localUserEditActivity.etUserPass.getText().toString().trim();
            localUserEditActivity.sharedHelper.setUserPass(str3);
            String str4 = localUserEditActivity.etUserNickName.getText().toString().trim();
            localUserEditActivity.sharedHelper.setUserNickName(str4);
            String str5 = localUserEditActivity.etUserEmail.getText().toString().trim();
            localUserEditActivity.sharedHelper.setUserEmail(str5);
            Toast.makeText(localUserEditActivity, localUserEditActivity.getString(2131099794), 0).show();
            localUserEditActivity.close();
            return;
          }
          Toast.makeText(localUserEditActivity, localUserEditActivity.getString(2131099793), 0).show();
          return;
          localUserEditActivity.pbUserLoading.setVisibility(8);
          if (localUserEditActivity.result[0].equals("")) {
            break;
          }
          localUserEditActivity.etUserName.setText(localUserEditActivity.result[0]);
          localUserEditActivity.etUserPass.setText(localUserEditActivity.result[1]);
          localUserEditActivity.etUserNickName.setText(localUserEditActivity.result[2]);
          localUserEditActivity.etUserEmail.setText(localUserEditActivity.result[4]);
        } while (localUserEditActivity.result[3].equals(""));
        localUserEditActivity.ivUserImage.setImageBitmap(UtilityHelper.getUserImage(localUserEditActivity, localUserEditActivity.sharedHelper.getUserImage()));
        return;
        localUserEditActivity.etUserName.setText(localUserEditActivity.sharedHelper.getUserName());
        localUserEditActivity.etUserPass.setText(localUserEditActivity.sharedHelper.getUserPass());
        localUserEditActivity.etUserNickName.setText(localUserEditActivity.sharedHelper.getUserNickName());
        localUserEditActivity.etUserEmail.setText(localUserEditActivity.sharedHelper.getUserEmail());
        return;
      }
      localUserEditActivity.pbUserLoading.setVisibility(8);
      boolean bool = paramMessage.getData().getBoolean("result");
      String str1 = paramMessage.getData().getString("userimage");
      if (bool)
      {
        localUserEditActivity.sharedHelper.setUserImage(str1);
        Toast.makeText(localUserEditActivity, localUserEditActivity.getString(2131099798), 0).show();
        return;
      }
      Toast.makeText(localUserEditActivity, localUserEditActivity.getString(2131099799), 0).show();
    }
  }
}


/* Location:           D:\fxlandroid\document\app\AALifeNew_v3.1.4\classes_dex2jar.jar
 * Qualified Name:     com.aalife.android.UserEditActivity
 * JD-Core Version:    0.7.0-SNAPSHOT-20130630
 */