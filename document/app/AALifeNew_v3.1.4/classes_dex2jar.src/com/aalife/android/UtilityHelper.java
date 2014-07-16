package com.aalife.android;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class UtilityHelper
{
  private static final String WEBURL = "http://www.fxlweb.com";
  
  public static String[] addUser(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    String[] arrayOfString = { "0", "0", "0", "0", "", "", "", "" };
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new BasicNameValuePair("username", paramString1));
    localArrayList.add(new BasicNameValuePair("userpass", paramString2));
    localArrayList.add(new BasicNameValuePair("usernickname", paramString3));
    localArrayList.add(new BasicNameValuePair("useremail", paramString4));
    try
    {
      JSONObject localJSONObject = new JSONObject(HttpHelper.post("http://www.fxlweb.com/AALifeWeb/SyncNewUser.aspx", localArrayList));
      if (localJSONObject.length() > 0)
      {
        arrayOfString[0] = localJSONObject.getString("group");
        arrayOfString[1] = localJSONObject.getString("userid");
        arrayOfString[2] = "0";
        arrayOfString[3] = localJSONObject.getString("repeat");
        arrayOfString[4] = "";
        arrayOfString[5] = "";
        arrayOfString[6] = "";
        arrayOfString[7] = "";
      }
      return arrayOfString;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return arrayOfString;
  }
  
  public static void backupUser(Context paramContext)
  {
    SharedHelper localSharedHelper = new SharedHelper(paramContext);
    try
    {
      File localFile;
      if (Environment.getExternalStorageState().equals("mounted"))
      {
        localFile = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "aalife" + File.separator + "user.bak");
        if (!localFile.getParentFile().exists()) {
          localFile.getParentFile().mkdirs();
        }
      }
      FileOutputStream localFileOutputStream;
      for (Object localObject = new FileOutputStream(localFile, false);; localObject = localFileOutputStream)
      {
        PrintStream localPrintStream = new PrintStream((OutputStream)localObject);
        localPrintStream.println("userid:" + localSharedHelper.getUserId());
        localPrintStream.println("group:" + localSharedHelper.getGroup());
        localPrintStream.println("username:" + localSharedHelper.getUserName());
        localPrintStream.println("password:" + localSharedHelper.getUserPass());
        localPrintStream.println("login:" + localSharedHelper.getLogin());
        localPrintStream.println("welcome:" + localSharedHelper.getWelcomeText());
        localPrintStream.println("firstsync:" + localSharedHelper.getFirstSync());
        localPrintStream.println("localsync:" + localSharedHelper.getLocalSync());
        localPrintStream.println("sync:" + localSharedHelper.getSyncStatus());
        localPrintStream.println("syncing:" + localSharedHelper.getSyncing());
        ((FileOutputStream)localObject).close();
        localPrintStream.close();
        return;
        localFileOutputStream = paramContext.openFileOutput("aalife.bak", 0);
      }
      return;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
  
  public static boolean checkInternet(Context paramContext)
  {
    NetworkInfo localNetworkInfo = ((ConnectivityManager)paramContext.getSystemService("connectivity")).getActiveNetworkInfo();
    if ((localNetworkInfo == null) || (!localNetworkInfo.isConnected()) || (localNetworkInfo.getSubtype() == 2) || (localNetworkInfo.getSubtype() == 1) || (localNetworkInfo.getSubtype() == 4)) {
      return false;
    }
    return localNetworkInfo.isAvailable();
  }
  
  public static boolean checkInternetAll(Context paramContext)
  {
    NetworkInfo localNetworkInfo = ((ConnectivityManager)paramContext.getSystemService("connectivity")).getActiveNetworkInfo();
    if ((localNetworkInfo != null) && (localNetworkInfo.isConnected())) {
      return localNetworkInfo.isAvailable();
    }
    return false;
  }
  
  public static boolean checkNewVersion(Context paramContext)
  {
    String str1 = getVersionFromWeb();
    String str2 = getVersionFromApp(paramContext);
    if ((str1.equals("")) || (str2.equals(""))) {}
    while (Integer.parseInt(str1.replace(".", "")) <= Integer.parseInt(str2.replace(".", ""))) {
      return false;
    }
    return true;
  }
  
  public static String createUserName()
  {
    Random localRandom = new Random();
    return "aa" + String.valueOf(10000 + localRandom.nextInt(99999) % 90000);
  }
  
  public static int editUser(int paramInt, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new BasicNameValuePair("username", paramString1));
    localArrayList.add(new BasicNameValuePair("userpass", paramString2));
    localArrayList.add(new BasicNameValuePair("nickname", paramString3));
    localArrayList.add(new BasicNameValuePair("userimage", paramString4));
    localArrayList.add(new BasicNameValuePair("useremail", paramString5));
    localArrayList.add(new BasicNameValuePair("userid", String.valueOf(paramInt)));
    try
    {
      JSONObject localJSONObject = new JSONObject(HttpHelper.post("http://www.fxlweb.com/AALifeWeb/SyncUserEdit.aspx", localArrayList));
      int i = localJSONObject.length();
      int j = 0;
      if (i > 0)
      {
        int k = localJSONObject.getInt("result");
        j = k;
      }
      return j;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return 0;
  }
  
  public static int editUserImage(String paramString1, String paramString2)
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new BasicNameValuePair("userimage", paramString1));
    localArrayList.add(new BasicNameValuePair("userid", paramString2));
    try
    {
      JSONObject localJSONObject = new JSONObject(HttpHelper.post("http://www.fxlweb.com/AALifeWeb/SyncUserImage.aspx", localArrayList));
      int i = localJSONObject.length();
      int j = 0;
      if (i > 0)
      {
        int k = localJSONObject.getInt("result");
        j = k;
      }
      return j;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return 0;
  }
  
  public static String formatDate(String paramString1, String paramString2)
  {
    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    Object localObject = new Date();
    String str1;
    String str2;
    String str3;
    try
    {
      Date localDate = localSimpleDateFormat.parse(paramString1);
      localObject = localDate;
      Calendar localCalendar = Calendar.getInstance();
      localCalendar.setTime((Date)localObject);
      str1 = String.valueOf(localCalendar.get(1));
      str2 = formatFull(1 + localCalendar.get(2));
      str3 = formatFull(localCalendar.get(5));
      str4 = formatWeek(-1 + localCalendar.get(7));
      if (paramString2.equals("m-d-w"))
      {
        str1 = str2 + "-" + str3 + "  " + str4;
        return str1;
      }
    }
    catch (Exception localException)
    {
      String str4;
      do
      {
        for (;;)
        {
          localException.printStackTrace();
        }
      } while (paramString2.equals("y"));
      if (paramString2.equals("y-m")) {
        return str1 + "-" + str2;
      }
      if (paramString2.equals("y2-m2")) {
        return str1 + "年" + str2 + "月";
      }
      if (paramString2.equals("ys-m")) {
        return str1.substring(2) + "-" + str2;
      }
      if (paramString2.equals("ys-m-d")) {
        return str1.substring(2) + "-" + str2 + "-" + str3;
      }
      if (paramString2.equals("m-d")) {
        return str2 + "-" + str3;
      }
      if (paramString2.equals("m")) {
        return str2 + "月";
      }
      if (paramString2.equals("m-d-w")) {
        return str2 + "-" + str3 + " " + str4;
      }
      if (paramString2.equals("y-m-d-w")) {
        return str1 + "-" + str2 + "-" + str3 + "  " + str4;
      }
      if (paramString2.equals("y-m-d-w2")) {
        return str1 + "-" + str2 + "-" + str3 + "  周" + str4;
      }
    }
    return str1 + "-" + str2 + "-" + str3;
  }
  
  public static String formatDouble(double paramDouble, String paramString)
  {
    return new DecimalFormat(paramString).format(paramDouble);
  }
  
  protected static String formatFull(int paramInt)
  {
    String str = paramInt;
    if (str.length() == 1) {
      str = "0" + str;
    }
    return str;
  }
  
  protected static String formatWeek(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return "";
    case 0: 
      return "日";
    case 1: 
      return "一";
    case 2: 
      return "二";
    case 3: 
      return "三";
    case 4: 
      return "四";
    case 5: 
      return "五";
    }
    return "六";
  }
  
  public static String getCurDate()
  {
    return new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(Calendar.getInstance().getTime());
  }
  
  public static String getCurTime()
  {
    return new SimpleDateFormat("HH:mm:ss", Locale.CHINA).format(Calendar.getInstance().getTime());
  }
  
  public static String getFileExtName(String paramString)
  {
    return paramString.substring(paramString.lastIndexOf("."));
  }
  
  /* Error */
  public static File getInstallFile(Context paramContext)
    throws Exception
  {
    // Byte code:
    //   0: new 409	java/net/URL
    //   3: dup
    //   4: ldc_w 411
    //   7: invokespecial 412	java/net/URL:<init>	(Ljava/lang/String;)V
    //   10: invokevirtual 416	java/net/URL:openConnection	()Ljava/net/URLConnection;
    //   13: checkcast 418	java/net/HttpURLConnection
    //   16: invokevirtual 422	java/net/HttpURLConnection:getInputStream	()Ljava/io/InputStream;
    //   19: astore_2
    //   20: invokestatic 86	android/os/Environment:getExternalStorageState	()Ljava/lang/String;
    //   23: ldc 88
    //   25: invokevirtual 91	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   28: ifeq +129 -> 157
    //   31: new 93	java/io/File
    //   34: dup
    //   35: new 95	java/lang/StringBuilder
    //   38: dup
    //   39: invokestatic 99	android/os/Environment:getExternalStorageDirectory	()Ljava/io/File;
    //   42: invokevirtual 102	java/io/File:toString	()Ljava/lang/String;
    //   45: invokestatic 106	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
    //   48: invokespecial 107	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   51: getstatic 110	java/io/File:separator	Ljava/lang/String;
    //   54: invokevirtual 114	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   57: ldc 116
    //   59: invokevirtual 114	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   62: getstatic 110	java/io/File:separator	Ljava/lang/String;
    //   65: invokevirtual 114	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   68: ldc_w 424
    //   71: invokevirtual 114	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   74: invokevirtual 119	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   77: invokespecial 120	java/io/File:<init>	(Ljava/lang/String;)V
    //   80: astore_3
    //   81: aload_3
    //   82: invokevirtual 123	java/io/File:getParentFile	()Ljava/io/File;
    //   85: invokevirtual 127	java/io/File:exists	()Z
    //   88: ifne +11 -> 99
    //   91: aload_3
    //   92: invokevirtual 123	java/io/File:getParentFile	()Ljava/io/File;
    //   95: invokevirtual 130	java/io/File:mkdirs	()Z
    //   98: pop
    //   99: new 132	java/io/FileOutputStream
    //   102: dup
    //   103: aload_3
    //   104: invokespecial 427	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
    //   107: astore 4
    //   109: new 429	java/io/BufferedInputStream
    //   112: dup
    //   113: aload_2
    //   114: invokespecial 432	java/io/BufferedInputStream:<init>	(Ljava/io/InputStream;)V
    //   117: astore 5
    //   119: sipush 1024
    //   122: newarray byte
    //   124: astore 6
    //   126: aload 5
    //   128: aload 6
    //   130: invokevirtual 436	java/io/BufferedInputStream:read	([B)I
    //   133: istore 7
    //   135: iload 7
    //   137: iconst_m1
    //   138: if_icmpne +30 -> 168
    //   141: aload 4
    //   143: invokevirtual 202	java/io/FileOutputStream:close	()V
    //   146: aload 5
    //   148: invokevirtual 437	java/io/BufferedInputStream:close	()V
    //   151: aload_2
    //   152: invokevirtual 440	java/io/InputStream:close	()V
    //   155: aload_3
    //   156: areturn
    //   157: aload_0
    //   158: ldc_w 424
    //   161: invokevirtual 444	android/content/Context:getFileStreamPath	(Ljava/lang/String;)Ljava/io/File;
    //   164: astore_3
    //   165: goto -84 -> 81
    //   168: aload 4
    //   170: aload 6
    //   172: iconst_0
    //   173: iload 7
    //   175: invokevirtual 448	java/io/FileOutputStream:write	([BII)V
    //   178: goto -52 -> 126
    //   181: astore_1
    //   182: new 16	java/lang/Exception
    //   185: dup
    //   186: invokespecial 449	java/lang/Exception:<init>	()V
    //   189: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	190	0	paramContext	Context
    //   181	1	1	localException	Exception
    //   19	133	2	localInputStream	InputStream
    //   80	85	3	localFile	File
    //   107	62	4	localFileOutputStream	FileOutputStream
    //   117	30	5	localBufferedInputStream	java.io.BufferedInputStream
    //   124	47	6	arrayOfByte	byte[]
    //   133	41	7	i	int
    // Exception table:
    //   from	to	target	type
    //   0	81	181	java/lang/Exception
    //   81	99	181	java/lang/Exception
    //   99	126	181	java/lang/Exception
    //   126	135	181	java/lang/Exception
    //   141	155	181	java/lang/Exception
    //   157	165	181	java/lang/Exception
    //   168	178	181	java/lang/Exception
  }
  
  public static String getNavDate(String paramString1, int paramInt, String paramString2)
  {
    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    Object localObject = new Date();
    try
    {
      Date localDate = localSimpleDateFormat.parse(paramString1);
      localObject = localDate;
      localCalendar = Calendar.getInstance();
      localCalendar.setTime((Date)localObject);
      if (paramString2.equals("d"))
      {
        localCalendar.add(5, paramInt);
        return localSimpleDateFormat.format(localCalendar.getTime());
      }
    }
    catch (Exception localException)
    {
      for (;;)
      {
        Calendar localCalendar;
        localException.printStackTrace();
        continue;
        if (paramString2.equals("m")) {
          localCalendar.add(2, paramInt);
        }
      }
    }
  }
  
  public static String getSyncDate()
  {
    return new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.CHINA).format(Calendar.getInstance().getTime());
  }
  
  public static String[] getUser(int paramInt)
  {
    String[] arrayOfString = { "", "", "", "", "" };
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new BasicNameValuePair("userid", String.valueOf(paramInt)));
    try
    {
      JSONObject localJSONObject = new JSONObject(HttpHelper.post("http://www.fxlweb.com/AALifeWeb/GetUser.aspx", localArrayList));
      if (localJSONObject.length() > 0)
      {
        arrayOfString[0] = localJSONObject.getString("username");
        arrayOfString[1] = localJSONObject.getString("userpass");
        arrayOfString[2] = localJSONObject.getString("nickname");
        arrayOfString[3] = localJSONObject.getString("userimage");
        arrayOfString[4] = localJSONObject.getString("useremail");
      }
      return arrayOfString;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return arrayOfString;
  }
  
  public static Bitmap getUserImage(Context paramContext, String paramString)
  {
    if (Environment.getExternalStorageState().equals("mounted")) {}
    for (File localFile = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "aalife" + File.separator + paramString);; localFile = paramContext.getFileStreamPath(paramString)) {
      return BitmapFactory.decodeFile(localFile.getPath());
    }
  }
  
  public static String getVersionFromApp(Context paramContext)
  {
    try
    {
      String str = paramContext.getPackageManager().getPackageInfo(paramContext.getPackageName(), 0).versionName;
      return str;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return "";
  }
  
  public static String getVersionFromWeb()
  {
    Object localObject = "";
    try
    {
      JSONObject localJSONObject = new JSONObject(HttpHelper.post("http://www.fxlweb.com/AALifeWeb/GetWebVersion.aspx"));
      if (localJSONObject.length() > 0)
      {
        String str = localJSONObject.getString("version");
        localObject = str;
      }
      return localObject;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return localObject;
  }
  
  public static boolean loadBitmap(Context paramContext, String paramString)
  {
    String str = "http://www.fxlweb.com/Images/Users/" + paramString;
    try
    {
      HttpURLConnection localHttpURLConnection = (HttpURLConnection)new URL(str).openConnection();
      if (localHttpURLConnection.getResponseCode() == 200)
      {
        InputStream localInputStream = localHttpURLConnection.getInputStream();
        FileOutputStream localFileOutputStream;
        byte[] arrayOfByte;
        if (Environment.getExternalStorageState().equals("mounted"))
        {
          File localFile = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "aalife" + File.separator + paramString);
          if (!localFile.getParentFile().exists()) {
            localFile.getParentFile().mkdirs();
          }
          localFileOutputStream = new FileOutputStream(localFile, false);
          arrayOfByte = new byte[1024];
        }
        for (;;)
        {
          int i = localInputStream.read(arrayOfByte);
          if (i == -1)
          {
            localFileOutputStream.close();
            localInputStream.close();
            break label210;
            localFileOutputStream = paramContext.openFileOutput(paramString, 0);
            break;
          }
          localFileOutputStream.write(arrayOfByte, 0, i);
        }
      }
      return true;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
      return false;
    }
  }
  
  public static String[] loginUser(String paramString1, String paramString2)
  {
    String[] arrayOfString = { "0", "0", "0", "0", "", "", "", "" };
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new BasicNameValuePair("username", paramString1));
    localArrayList.add(new BasicNameValuePair("userpass", paramString2));
    try
    {
      JSONObject localJSONObject = new JSONObject(HttpHelper.post("http://www.fxlweb.com/AALifeWeb/SyncLoginNew.aspx", localArrayList));
      if (localJSONObject.length() > 0)
      {
        arrayOfString[0] = localJSONObject.getString("group");
        arrayOfString[1] = localJSONObject.getString("userid");
        arrayOfString[2] = localJSONObject.getString("hassync");
        arrayOfString[3] = "0";
        arrayOfString[4] = localJSONObject.getString("nickname");
        arrayOfString[5] = localJSONObject.getString("useremail");
        arrayOfString[6] = localJSONObject.getString("userpass");
        arrayOfString[7] = localJSONObject.getString("userimage");
      }
      return arrayOfString;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return arrayOfString;
  }
  
  public static boolean postBitmap(Context paramContext, String paramString)
  {
    for (;;)
    {
      StringBuffer localStringBuffer;
      try
      {
        HttpURLConnection localHttpURLConnection = (HttpURLConnection)new URL("http://www.fxlweb.com/AALifeWeb/SyncUserImage.ashx").openConnection();
        localHttpURLConnection.setDoInput(true);
        localHttpURLConnection.setDoOutput(true);
        localHttpURLConnection.setUseCaches(false);
        localHttpURLConnection.setRequestMethod("POST");
        localHttpURLConnection.setRequestProperty("Connection", "Keep-Alive");
        localHttpURLConnection.setRequestProperty("Charset", "UTF-8");
        localHttpURLConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + "*****");
        DataOutputStream localDataOutputStream = new DataOutputStream(localHttpURLConnection.getOutputStream());
        localDataOutputStream.writeBytes("--" + "*****" + "\r\n");
        localDataOutputStream.writeBytes("Content-Disposition:form-data; name=\"file1\"; filename=\"" + paramString + "\"" + "\r\n");
        localDataOutputStream.writeBytes("\r\n");
        File localFile;
        byte[] arrayOfByte;
        int i;
        int j;
        if (Environment.getExternalStorageState().equals("mounted"))
        {
          localFile = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "aalife" + File.separator + paramString);
          FileInputStream localFileInputStream = new FileInputStream(localFile.getPath());
          arrayOfByte = new byte[1024];
          i = localFileInputStream.read(arrayOfByte);
          if (i == -1)
          {
            localDataOutputStream.writeBytes("\r\n");
            localDataOutputStream.writeBytes("--" + "*****" + "--" + "\r\n");
            localFileInputStream.close();
            localDataOutputStream.flush();
            localDataOutputStream.close();
            InputStream localInputStream = localHttpURLConnection.getInputStream();
            localStringBuffer = new StringBuffer();
            j = localInputStream.read();
            if (j != -1) {
              break label401;
            }
            return localStringBuffer.toString().equals("1");
          }
        }
        else
        {
          localFile = paramContext.getFileStreamPath(paramString);
          continue;
        }
        localDataOutputStream.write(arrayOfByte, 0, i);
        continue;
        c = (char)j;
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
        return false;
      }
      label401:
      char c;
      localStringBuffer.append(c);
    }
  }
  
  public static Bitmap resizeBitmap(Bitmap paramBitmap, int paramInt)
  {
    float f1 = paramBitmap.getWidth();
    float f2 = paramBitmap.getHeight();
    Matrix localMatrix = new Matrix();
    float f3 = paramInt / f1;
    float f4 = paramInt / f2;
    if (f1 >= f2) {
      localMatrix.postScale(f3, f3);
    }
    for (;;)
    {
      return Bitmap.createBitmap(paramBitmap, 0, 0, (int)f1, (int)f2, localMatrix, true);
      localMatrix.postScale(f4, f4);
    }
  }
  
  public static void restoreUser(Context paramContext)
  {
    SharedHelper localSharedHelper = new SharedHelper(paramContext);
    for (;;)
    {
      String str2;
      try
      {
        File localFile;
        int i;
        String str1;
        if (Environment.getExternalStorageState().equals("mounted"))
        {
          localFile = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "aalife" + File.separator + "user.bak");
          if (!localFile.getParentFile().exists()) {
            localFile.getParentFile().mkdirs();
          }
          if (!localFile.exists()) {
            localFile.createNewFile();
          }
          FileInputStream localFileInputStream = new FileInputStream(localFile);
          BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(localFileInputStream));
          i = 0;
          str1 = localBufferedReader.readLine();
          if (str1 == null)
          {
            localBufferedReader.close();
            localFileInputStream.close();
          }
        }
        else
        {
          localFile = paramContext.getFileStreamPath("aalife.bak");
          continue;
        }
        str2 = str1.substring(1 + str1.indexOf(":"));
        i++;
        switch (i)
        {
        case 1: 
          localSharedHelper.setUserId(Integer.parseInt(str2));
          break;
        case 2: 
          localSharedHelper.setGroup(Integer.parseInt(str2));
        }
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
        return;
      }
      continue;
      localSharedHelper.setUserName(str2);
      continue;
      localSharedHelper.setUserPass(str2);
      continue;
      localSharedHelper.setLogin(Boolean.valueOf(Boolean.parseBoolean(str2)));
      continue;
      localSharedHelper.setWelcomeText(str2);
      continue;
      localSharedHelper.setFirstSync(Boolean.valueOf(Boolean.parseBoolean(str2)));
      continue;
      localSharedHelper.setLocalSync(Boolean.valueOf(Boolean.parseBoolean(str2)));
      continue;
      localSharedHelper.setSyncStatus(str2);
      continue;
      localSharedHelper.setSyncing(Boolean.valueOf(Boolean.parseBoolean(str2)));
    }
  }
  
  public static boolean saveBitmap(Context paramContext, Bitmap paramBitmap, String paramString)
  {
    try
    {
      File localFile;
      if (Environment.getExternalStorageState().equals("mounted"))
      {
        localFile = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "aalife" + File.separator + paramString);
        if (!localFile.getParentFile().exists()) {
          localFile.getParentFile().mkdirs();
        }
      }
      FileOutputStream localFileOutputStream;
      for (Object localObject = new FileOutputStream(localFile, false);; localObject = localFileOutputStream)
      {
        if (paramBitmap.compress(Bitmap.CompressFormat.PNG, 80, (OutputStream)localObject)) {
          ((FileOutputStream)localObject).flush();
        }
        ((FileOutputStream)localObject).close();
        return true;
        localFileOutputStream = paramContext.openFileOutput(paramString, 0);
      }
      return false;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
  
  public static void sendEmail(String paramString)
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new BasicNameValuePair("usernickname", paramString));
    try
    {
      new JSONObject(HttpHelper.post("http://www.fxlweb.com/AALifeWeb/SyncSendEmail.aspx", localArrayList)).length();
      return;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
  
  public static boolean startBackup(Context paramContext)
  {
    ItemTableAccess localItemTableAccess = new ItemTableAccess(new DatabaseHelper(paramContext).getReadableDatabase());
    List localList = localItemTableAccess.bakDataBase();
    localItemTableAccess.close();
    try
    {
      FileOutputStream localFileOutputStream;
      PrintStream localPrintStream;
      Iterator localIterator;
      if (Environment.getExternalStorageState().equals("mounted"))
      {
        File localFile1 = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "aalife" + File.separator + ".nomedia");
        if (!localFile1.exists()) {
          localFile1.createNewFile();
        }
        File localFile2 = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "aalife" + File.separator + "aalife.bak");
        if (!localFile2.getParentFile().exists()) {
          localFile2.getParentFile().mkdirs();
        }
        localFileOutputStream = new FileOutputStream(localFile2, false);
        localPrintStream = new PrintStream(localFileOutputStream);
        localIterator = localList.iterator();
      }
      for (;;)
      {
        if (!localIterator.hasNext())
        {
          localPrintStream.close();
          localFileOutputStream.close();
          backupUser(paramContext);
          return true;
          File localFile3 = paramContext.getFileStreamPath(".nomedia");
          if (!localFile3.exists()) {
            localFile3.createNewFile();
          }
          localFileOutputStream = paramContext.openFileOutput("aalife.bak", 0);
          break;
        }
        localPrintStream.println(localIterator.next());
      }
      return false;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
  
  public static int startRestore(Context paramContext)
  {
    try
    {
      File localFile;
      FileInputStream localFileInputStream;
      ArrayList localArrayList;
      BufferedReader localBufferedReader;
      if (Environment.getExternalStorageState().equals("mounted"))
      {
        localFile = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "aalife" + File.separator + "aalife.bak");
        if (!localFile.getParentFile().exists()) {
          localFile.getParentFile().mkdirs();
        }
        if (!localFile.exists()) {
          localFile.createNewFile();
        }
        localFileInputStream = new FileInputStream(localFile);
        localArrayList = new ArrayList();
        localBufferedReader = new BufferedReader(new InputStreamReader(localFileInputStream));
      }
      for (;;)
      {
        String str = localBufferedReader.readLine();
        if (str == null)
        {
          localBufferedReader.close();
          localFileInputStream.close();
          if (localArrayList.size() <= 0) {
            break label235;
          }
          ItemTableAccess localItemTableAccess = new ItemTableAccess(new DatabaseHelper(paramContext).getReadableDatabase());
          Boolean localBoolean = Boolean.valueOf(localItemTableAccess.restoreDataBase(localArrayList));
          localItemTableAccess.close();
          if (!localBoolean.booleanValue()) {
            break label233;
          }
          return 1;
          localFile = paramContext.getFileStreamPath("aalife.bak");
          break;
        }
        localArrayList.add(str);
      }
      return 0;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
      return 0;
    }
    label233:
    label235:
    return 2;
  }
  
  public static void syncItemLogin(List<Map<String, String>> paramList, String paramString1, String paramString2)
  {
    Iterator localIterator = paramList.iterator();
    for (;;)
    {
      if (!localIterator.hasNext()) {
        return;
      }
      Map localMap = (Map)localIterator.next();
      ArrayList localArrayList = new ArrayList();
      localArrayList.add(new BasicNameValuePair("userid", paramString1));
      localArrayList.add(new BasicNameValuePair("usergroupid", paramString2));
      localArrayList.add(new BasicNameValuePair("itemappid", (String)localMap.get("itemid")));
      try
      {
        new JSONObject(HttpHelper.post("http://www.fxlweb.com/AALifeWeb/SyncItemLogin.aspx", localArrayList)).length();
      }
      catch (Exception localException) {}
    }
  }
}


/* Location:           D:\fxlandroid\document\app\AALifeNew_v3.1.4\classes_dex2jar.jar
 * Qualified Name:     com.aalife.android.UtilityHelper
 * JD-Core Version:    0.7.0-SNAPSHOT-20130630
 */