package com.aalife.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedHelper
{
  private SharedPreferences setting = null;
  
  public SharedHelper(Context paramContext)
  {
    this.setting = paramContext.getSharedPreferences("setting", 0);
  }
  
  public int getCategory()
  {
    return this.setting.getInt("category", 0);
  }
  
  public String getCurDate()
  {
    return this.setting.getString("curdate", "");
  }
  
  public boolean getFirstSync()
  {
    return this.setting.getBoolean("firstsync", false);
  }
  
  public boolean getFixSync()
  {
    return this.setting.getBoolean("fixsync", false);
  }
  
  public int getGroup()
  {
    return this.setting.getInt("group", 0);
  }
  
  public boolean getHasRestore()
  {
    return this.setting.getBoolean("hasrestore", false);
  }
  
  public boolean getLocalSync()
  {
    return this.setting.getBoolean("localsync", false);
  }
  
  public String getLockText()
  {
    return this.setting.getString("lock", "");
  }
  
  public boolean getLogin()
  {
    return this.setting.getBoolean("login", false);
  }
  
  public boolean getRestore()
  {
    return this.setting.getBoolean("restore2", false);
  }
  
  public String getSyncStatus()
  {
    return this.setting.getString("sync", "");
  }
  
  public boolean getSyncing()
  {
    return this.setting.getBoolean("syncing", false);
  }
  
  public String getUserEmail()
  {
    return this.setting.getString("useremail", "");
  }
  
  public int getUserId()
  {
    return this.setting.getInt("userid", 0);
  }
  
  public String getUserImage()
  {
    return this.setting.getString("userimage", "");
  }
  
  public String getUserName()
  {
    return this.setting.getString("username", "");
  }
  
  public String getUserNickName()
  {
    return this.setting.getString("nickname", "");
  }
  
  public String getUserPass()
  {
    return this.setting.getString("password", "");
  }
  
  public boolean getWebSync()
  {
    return this.setting.getBoolean("websync", false);
  }
  
  public String getWelcomeText()
  {
    return this.setting.getString("welcome", "");
  }
  
  public void setCategory(int paramInt)
  {
    this.setting.edit().putInt("category", paramInt).commit();
  }
  
  public void setCurDate(String paramString)
  {
    this.setting.edit().putString("curdate", paramString).commit();
  }
  
  public void setFirstSync(Boolean paramBoolean)
  {
    this.setting.edit().putBoolean("firstsync", paramBoolean.booleanValue()).commit();
  }
  
  public void setFixSync(Boolean paramBoolean)
  {
    this.setting.edit().putBoolean("fixsync", paramBoolean.booleanValue()).commit();
  }
  
  public void setGroup(int paramInt)
  {
    this.setting.edit().putInt("group", paramInt).commit();
  }
  
  public void setHasRestore(Boolean paramBoolean)
  {
    this.setting.edit().putBoolean("hasrestore", paramBoolean.booleanValue()).commit();
  }
  
  public void setLocalSync(Boolean paramBoolean)
  {
    this.setting.edit().putBoolean("localsync", paramBoolean.booleanValue()).commit();
  }
  
  public void setLockText(String paramString)
  {
    this.setting.edit().putString("lock", paramString).commit();
  }
  
  public void setLogin(Boolean paramBoolean)
  {
    this.setting.edit().putBoolean("login", paramBoolean.booleanValue()).commit();
  }
  
  public void setRestore(Boolean paramBoolean)
  {
    this.setting.edit().putBoolean("restore2", paramBoolean.booleanValue()).commit();
  }
  
  public void setSyncStatus(String paramString)
  {
    this.setting.edit().putString("sync", paramString).commit();
  }
  
  public void setSyncing(Boolean paramBoolean)
  {
    this.setting.edit().putBoolean("syncing", paramBoolean.booleanValue()).commit();
  }
  
  public void setUserEmail(String paramString)
  {
    this.setting.edit().putString("useremail", paramString).commit();
  }
  
  public void setUserId(int paramInt)
  {
    this.setting.edit().putInt("userid", paramInt).commit();
  }
  
  public void setUserImage(String paramString)
  {
    this.setting.edit().putString("userimage", paramString).commit();
  }
  
  public void setUserName(String paramString)
  {
    this.setting.edit().putString("username", paramString).commit();
  }
  
  public void setUserNickName(String paramString)
  {
    this.setting.edit().putString("nickname", paramString).commit();
  }
  
  public void setUserPass(String paramString)
  {
    this.setting.edit().putString("password", paramString).commit();
  }
  
  public void setWebSync(Boolean paramBoolean)
  {
    this.setting.edit().putBoolean("websync", paramBoolean.booleanValue()).commit();
  }
  
  public void setWelcomeText(String paramString)
  {
    this.setting.edit().putString("welcome", paramString).commit();
  }
}


/* Location:           D:\fxlandroid\document\app\AALifeNew_v3.1.4\classes_dex2jar.jar
 * Qualified Name:     com.aalife.android.SharedHelper
 * JD-Core Version:    0.7.0-SNAPSHOT-20130630
 */