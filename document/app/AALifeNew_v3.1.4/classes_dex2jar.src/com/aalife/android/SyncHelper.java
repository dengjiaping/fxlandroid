package com.aalife.android;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

public class SyncHelper
{
  private static final String WEBURL = "http://www.fxlweb.com";
  private Context context = null;
  private SharedHelper sharedHelper = null;
  private SQLiteOpenHelper sqlHelper = null;
  
  public SyncHelper(Context paramContext)
  {
    this.context = paramContext;
    this.sharedHelper = new SharedHelper(this.context);
    this.sqlHelper = new DatabaseHelper(this.context);
    this.sqlHelper.close();
  }
  
  /* Error */
  public void Start()
    throws Exception
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 21	com/aalife/android/SyncHelper:sharedHelper	Lcom/aalife/android/SharedHelper;
    //   4: invokevirtual 44	com/aalife/android/SharedHelper:getUserId	()I
    //   7: ifne +74 -> 81
    //   10: invokestatic 50	com/aalife/android/UtilityHelper:createUserName	()Ljava/lang/String;
    //   13: astore 19
    //   15: aload_0
    //   16: aload 19
    //   18: invokevirtual 54	com/aalife/android/SyncHelper:syncUserName	(Ljava/lang/String;)[I
    //   21: astore 20
    //   23: aload 20
    //   25: iconst_0
    //   26: iaload
    //   27: ifle +448 -> 475
    //   30: aload_0
    //   31: getfield 21	com/aalife/android/SyncHelper:sharedHelper	Lcom/aalife/android/SharedHelper;
    //   34: aload 20
    //   36: iconst_0
    //   37: iaload
    //   38: invokevirtual 58	com/aalife/android/SharedHelper:setGroup	(I)V
    //   41: aload_0
    //   42: getfield 21	com/aalife/android/SyncHelper:sharedHelper	Lcom/aalife/android/SharedHelper;
    //   45: aload 20
    //   47: iconst_1
    //   48: iaload
    //   49: invokevirtual 61	com/aalife/android/SharedHelper:setUserId	(I)V
    //   52: aload_0
    //   53: getfield 21	com/aalife/android/SyncHelper:sharedHelper	Lcom/aalife/android/SharedHelper;
    //   56: aload 19
    //   58: invokevirtual 65	com/aalife/android/SharedHelper:setUserName	(Ljava/lang/String;)V
    //   61: aload_0
    //   62: getfield 21	com/aalife/android/SyncHelper:sharedHelper	Lcom/aalife/android/SharedHelper;
    //   65: aload 19
    //   67: invokevirtual 68	com/aalife/android/SharedHelper:setUserPass	(Ljava/lang/String;)V
    //   70: aload_0
    //   71: getfield 21	com/aalife/android/SyncHelper:sharedHelper	Lcom/aalife/android/SharedHelper;
    //   74: iconst_1
    //   75: invokestatic 74	java/lang/Boolean:valueOf	(Z)Ljava/lang/Boolean;
    //   78: invokevirtual 78	com/aalife/android/SharedHelper:setLogin	(Ljava/lang/Boolean;)V
    //   81: aload_0
    //   82: getfield 21	com/aalife/android/SyncHelper:sharedHelper	Lcom/aalife/android/SharedHelper;
    //   85: invokevirtual 82	com/aalife/android/SharedHelper:getLocalSync	()Z
    //   88: ifeq +222 -> 310
    //   91: new 84	com/aalife/android/CategoryTableAccess
    //   94: dup
    //   95: aload_0
    //   96: getfield 25	com/aalife/android/SyncHelper:sqlHelper	Landroid/database/sqlite/SQLiteOpenHelper;
    //   99: invokevirtual 88	android/database/sqlite/SQLiteOpenHelper:getReadableDatabase	()Landroid/database/sqlite/SQLiteDatabase;
    //   102: invokespecial 91	com/aalife/android/CategoryTableAccess:<init>	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   105: astore_1
    //   106: aload_1
    //   107: invokevirtual 95	com/aalife/android/CategoryTableAccess:findAllSyncCat	()Ljava/util/List;
    //   110: astore_2
    //   111: aload_1
    //   112: invokevirtual 96	com/aalife/android/CategoryTableAccess:close	()V
    //   115: aload_2
    //   116: invokeinterface 101 1 0
    //   121: ifle +8 -> 129
    //   124: aload_0
    //   125: aload_2
    //   126: invokevirtual 105	com/aalife/android/SyncHelper:syncCategory	(Ljava/util/List;)V
    //   129: new 107	com/aalife/android/ItemTableAccess
    //   132: dup
    //   133: aload_0
    //   134: getfield 25	com/aalife/android/SyncHelper:sqlHelper	Landroid/database/sqlite/SQLiteOpenHelper;
    //   137: invokevirtual 88	android/database/sqlite/SQLiteOpenHelper:getReadableDatabase	()Landroid/database/sqlite/SQLiteDatabase;
    //   140: invokespecial 108	com/aalife/android/ItemTableAccess:<init>	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   143: astore_3
    //   144: aload_3
    //   145: invokevirtual 111	com/aalife/android/ItemTableAccess:findSyncItem	()Ljava/util/List;
    //   148: astore 4
    //   150: aload_3
    //   151: invokevirtual 112	com/aalife/android/ItemTableAccess:close	()V
    //   154: aload 4
    //   156: invokeinterface 101 1 0
    //   161: ifle +9 -> 170
    //   164: aload_0
    //   165: aload 4
    //   167: invokevirtual 115	com/aalife/android/SyncHelper:syncItem	(Ljava/util/List;)V
    //   170: new 107	com/aalife/android/ItemTableAccess
    //   173: dup
    //   174: aload_0
    //   175: getfield 25	com/aalife/android/SyncHelper:sqlHelper	Landroid/database/sqlite/SQLiteOpenHelper;
    //   178: invokevirtual 88	android/database/sqlite/SQLiteOpenHelper:getReadableDatabase	()Landroid/database/sqlite/SQLiteDatabase;
    //   181: invokespecial 108	com/aalife/android/ItemTableAccess:<init>	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   184: astore 5
    //   186: aload 5
    //   188: invokevirtual 118	com/aalife/android/ItemTableAccess:findDelSyncItem	()Ljava/util/List;
    //   191: astore 6
    //   193: aload 5
    //   195: invokevirtual 112	com/aalife/android/ItemTableAccess:close	()V
    //   198: aload 6
    //   200: invokeinterface 101 1 0
    //   205: ifle +35 -> 240
    //   208: aload_0
    //   209: aload 6
    //   211: invokevirtual 121	com/aalife/android/SyncHelper:delSyncItem	(Ljava/util/List;)V
    //   214: new 107	com/aalife/android/ItemTableAccess
    //   217: dup
    //   218: aload_0
    //   219: getfield 25	com/aalife/android/SyncHelper:sqlHelper	Landroid/database/sqlite/SQLiteOpenHelper;
    //   222: invokevirtual 88	android/database/sqlite/SQLiteOpenHelper:getReadableDatabase	()Landroid/database/sqlite/SQLiteDatabase;
    //   225: invokespecial 108	com/aalife/android/ItemTableAccess:<init>	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   228: astore 15
    //   230: aload 15
    //   232: invokevirtual 124	com/aalife/android/ItemTableAccess:clearDelTable	()V
    //   235: aload 15
    //   237: invokevirtual 112	com/aalife/android/ItemTableAccess:close	()V
    //   240: aload_0
    //   241: getfield 21	com/aalife/android/SyncHelper:sharedHelper	Lcom/aalife/android/SharedHelper;
    //   244: iconst_0
    //   245: invokestatic 74	java/lang/Boolean:valueOf	(Z)Ljava/lang/Boolean;
    //   248: invokevirtual 127	com/aalife/android/SharedHelper:setLocalSync	(Ljava/lang/Boolean;)V
    //   251: aload_0
    //   252: getfield 21	com/aalife/android/SyncHelper:sharedHelper	Lcom/aalife/android/SharedHelper;
    //   255: new 129	java/lang/StringBuilder
    //   258: dup
    //   259: aload_0
    //   260: getfield 23	com/aalife/android/SyncHelper:context	Landroid/content/Context;
    //   263: ldc 130
    //   265: invokevirtual 136	android/content/Context:getString	(I)Ljava/lang/String;
    //   268: invokestatic 141	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
    //   271: invokespecial 143	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   274: ldc 145
    //   276: invokevirtual 149	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   279: invokestatic 152	com/aalife/android/UtilityHelper:getSyncDate	()Ljava/lang/String;
    //   282: invokevirtual 149	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   285: invokevirtual 155	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   288: invokevirtual 158	com/aalife/android/SharedHelper:setSyncStatus	(Ljava/lang/String;)V
    //   291: aload_0
    //   292: invokevirtual 161	com/aalife/android/SyncHelper:checkSyncWeb	()I
    //   295: iconst_1
    //   296: if_icmpne +14 -> 310
    //   299: aload_0
    //   300: getfield 21	com/aalife/android/SyncHelper:sharedHelper	Lcom/aalife/android/SharedHelper;
    //   303: iconst_1
    //   304: invokestatic 74	java/lang/Boolean:valueOf	(Z)Ljava/lang/Boolean;
    //   307: invokevirtual 164	com/aalife/android/SharedHelper:setWebSync	(Ljava/lang/Boolean;)V
    //   310: aload_0
    //   311: getfield 21	com/aalife/android/SyncHelper:sharedHelper	Lcom/aalife/android/SharedHelper;
    //   314: invokevirtual 167	com/aalife/android/SharedHelper:getWebSync	()Z
    //   317: ifeq +157 -> 474
    //   320: new 169	java/util/ArrayList
    //   323: dup
    //   324: invokespecial 170	java/util/ArrayList:<init>	()V
    //   327: pop
    //   328: aload_0
    //   329: invokevirtual 173	com/aalife/android/SyncHelper:getSyncWebCategory	()Ljava/util/List;
    //   332: astore 9
    //   334: aload 9
    //   336: invokeinterface 101 1 0
    //   341: ifle +13 -> 354
    //   344: aload_0
    //   345: aload 9
    //   347: invokevirtual 176	com/aalife/android/SyncHelper:syncWebCategory	(Ljava/util/List;)V
    //   350: aload_0
    //   351: invokevirtual 179	com/aalife/android/SyncHelper:syncWebCategoryBack	()V
    //   354: aload_0
    //   355: getfield 21	com/aalife/android/SyncHelper:sharedHelper	Lcom/aalife/android/SharedHelper;
    //   358: invokevirtual 182	com/aalife/android/SharedHelper:getFirstSync	()Z
    //   361: ifne +162 -> 523
    //   364: aload_0
    //   365: invokevirtual 185	com/aalife/android/SyncHelper:getSyncWebFirst	()Ljava/util/List;
    //   368: astore 11
    //   370: aload_0
    //   371: getfield 21	com/aalife/android/SyncHelper:sharedHelper	Lcom/aalife/android/SharedHelper;
    //   374: iconst_1
    //   375: invokestatic 74	java/lang/Boolean:valueOf	(Z)Ljava/lang/Boolean;
    //   378: invokevirtual 188	com/aalife/android/SharedHelper:setFirstSync	(Ljava/lang/Boolean;)V
    //   381: aload 11
    //   383: invokeinterface 101 1 0
    //   388: ifle +9 -> 397
    //   391: aload_0
    //   392: aload 11
    //   394: invokevirtual 191	com/aalife/android/SyncHelper:syncWebItem	(Ljava/util/List;)V
    //   397: aload_0
    //   398: invokevirtual 194	com/aalife/android/SyncHelper:getDelSyncWebItem	()Ljava/util/List;
    //   401: astore 12
    //   403: aload 12
    //   405: invokeinterface 101 1 0
    //   410: ifle +13 -> 423
    //   413: aload_0
    //   414: aload 12
    //   416: invokevirtual 197	com/aalife/android/SyncHelper:syncDelWebItem	(Ljava/util/List;)V
    //   419: aload_0
    //   420: invokevirtual 200	com/aalife/android/SyncHelper:syncDelWebItemBack	()V
    //   423: aload_0
    //   424: getfield 21	com/aalife/android/SyncHelper:sharedHelper	Lcom/aalife/android/SharedHelper;
    //   427: iconst_0
    //   428: invokestatic 74	java/lang/Boolean:valueOf	(Z)Ljava/lang/Boolean;
    //   431: invokevirtual 164	com/aalife/android/SharedHelper:setWebSync	(Ljava/lang/Boolean;)V
    //   434: aload_0
    //   435: getfield 21	com/aalife/android/SyncHelper:sharedHelper	Lcom/aalife/android/SharedHelper;
    //   438: new 129	java/lang/StringBuilder
    //   441: dup
    //   442: aload_0
    //   443: getfield 23	com/aalife/android/SyncHelper:context	Landroid/content/Context;
    //   446: ldc 130
    //   448: invokevirtual 136	android/content/Context:getString	(I)Ljava/lang/String;
    //   451: invokestatic 141	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
    //   454: invokespecial 143	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   457: ldc 145
    //   459: invokevirtual 149	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   462: invokestatic 152	com/aalife/android/UtilityHelper:getSyncDate	()Ljava/lang/String;
    //   465: invokevirtual 149	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   468: invokevirtual 155	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   471: invokevirtual 158	com/aalife/android/SharedHelper:setSyncStatus	(Ljava/lang/String;)V
    //   474: return
    //   475: new 40	java/lang/Exception
    //   478: dup
    //   479: invokespecial 201	java/lang/Exception:<init>	()V
    //   482: athrow
    //   483: astore 18
    //   485: new 40	java/lang/Exception
    //   488: dup
    //   489: invokespecial 201	java/lang/Exception:<init>	()V
    //   492: athrow
    //   493: astore 17
    //   495: new 40	java/lang/Exception
    //   498: dup
    //   499: invokespecial 201	java/lang/Exception:<init>	()V
    //   502: athrow
    //   503: astore 14
    //   505: new 40	java/lang/Exception
    //   508: dup
    //   509: invokespecial 201	java/lang/Exception:<init>	()V
    //   512: athrow
    //   513: astore 13
    //   515: aload 5
    //   517: invokevirtual 112	com/aalife/android/ItemTableAccess:close	()V
    //   520: aload 13
    //   522: athrow
    //   523: aload_0
    //   524: invokevirtual 204	com/aalife/android/SyncHelper:getSyncWebItem	()Ljava/util/List;
    //   527: astore 10
    //   529: aload 10
    //   531: astore 11
    //   533: goto -152 -> 381
    //   536: astore 8
    //   538: aload_0
    //   539: getfield 21	com/aalife/android/SyncHelper:sharedHelper	Lcom/aalife/android/SharedHelper;
    //   542: aload_0
    //   543: getfield 23	com/aalife/android/SyncHelper:context	Landroid/content/Context;
    //   546: ldc 205
    //   548: invokevirtual 136	android/content/Context:getString	(I)Ljava/lang/String;
    //   551: invokevirtual 158	com/aalife/android/SharedHelper:setSyncStatus	(Ljava/lang/String;)V
    //   554: new 40	java/lang/Exception
    //   557: dup
    //   558: invokespecial 201	java/lang/Exception:<init>	()V
    //   561: athrow
    //   562: astore 13
    //   564: aload 15
    //   566: astore 5
    //   568: goto -53 -> 515
    //   571: astore 16
    //   573: aload 15
    //   575: astore 5
    //   577: goto -72 -> 505
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	580	0	this	SyncHelper
    //   105	7	1	localCategoryTableAccess	CategoryTableAccess
    //   110	16	2	localList1	List
    //   143	8	3	localItemTableAccess1	ItemTableAccess
    //   148	18	4	localList2	List
    //   184	392	5	localObject1	Object
    //   191	19	6	localList3	List
    //   536	1	8	localException1	Exception
    //   332	14	9	localList4	List
    //   527	3	10	localList5	List
    //   368	164	11	localObject2	Object
    //   401	14	12	localList6	List
    //   513	8	13	localObject3	Object
    //   562	1	13	localObject4	Object
    //   503	1	14	localException2	Exception
    //   228	346	15	localItemTableAccess2	ItemTableAccess
    //   571	1	16	localException3	Exception
    //   493	1	17	localException4	Exception
    //   483	1	18	localException5	Exception
    //   13	53	19	str	String
    //   21	25	20	arrayOfInt	int[]
    // Exception table:
    //   from	to	target	type
    //   124	129	483	java/lang/Exception
    //   164	170	493	java/lang/Exception
    //   208	230	503	java/lang/Exception
    //   208	230	513	finally
    //   505	513	513	finally
    //   328	354	536	java/lang/Exception
    //   354	381	536	java/lang/Exception
    //   381	397	536	java/lang/Exception
    //   397	423	536	java/lang/Exception
    //   523	529	536	java/lang/Exception
    //   230	235	562	finally
    //   230	235	571	java/lang/Exception
  }
  
  public int checkSyncWeb()
  {
    Object localObject = "";
    String str1 = String.valueOf(this.sharedHelper.getUserId());
    String str2 = String.valueOf(this.sharedHelper.getGroup());
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new BasicNameValuePair("userid", str1));
    localArrayList.add(new BasicNameValuePair("usergroupid", str2));
    try
    {
      JSONObject localJSONObject = new JSONObject(HttpHelper.post("http://www.fxlweb.com/AALifeWeb/CheckSyncWeb.aspx", localArrayList));
      if (localJSONObject.length() > 0)
      {
        String str3 = localJSONObject.getString("result");
        localObject = str3;
      }
      if (((String)localObject).equals("ok")) {
        return 1;
      }
    }
    catch (Exception localException)
    {
      for (;;)
      {
        localException.printStackTrace();
      }
      if (((String)localObject).equals("error")) {
        return 0;
      }
    }
    return 2;
  }
  
  public void delSyncItem(List<Map<String, String>> paramList)
    throws Exception
  {
    int i = 0;
    Object localObject = "";
    String str1 = String.valueOf(this.sharedHelper.getGroup());
    Iterator localIterator = paramList.iterator();
    for (;;)
    {
      if (!localIterator.hasNext())
      {
        if (i == 0) {
          break;
        }
        throw new Exception();
      }
      Map localMap = (Map)localIterator.next();
      ArrayList localArrayList = new ArrayList();
      localArrayList.add(new BasicNameValuePair("itemid", (String)localMap.get("itemid")));
      localArrayList.add(new BasicNameValuePair("itemwebid", (String)localMap.get("itemwebid")));
      localArrayList.add(new BasicNameValuePair("usergroupid", str1));
      try
      {
        JSONObject localJSONObject = new JSONObject(HttpHelper.post("http://www.fxlweb.com/AALifeWeb/DelSyncItemNew.aspx", localArrayList));
        if (localJSONObject.length() > 0)
        {
          String str2 = localJSONObject.getString("result");
          localObject = str2;
        }
        if (!((String)localObject).equals("ok")) {
          i = 1;
        }
      }
      catch (Exception localException)
      {
        i = 1;
      }
    }
  }
  
  public List<Map<String, String>> getDelSyncWebItem()
    throws Exception
  {
    ArrayList localArrayList1 = new ArrayList();
    String str = String.valueOf(this.sharedHelper.getGroup());
    ArrayList localArrayList2 = new ArrayList();
    localArrayList2.add(new BasicNameValuePair("usergroupid", str));
    JSONArray localJSONArray = new JSONObject(HttpHelper.post("http://www.fxlweb.com/AALifeWeb/GetDelSyncWebItem.aspx", localArrayList2)).getJSONArray("deletelist");
    for (int i = 0;; i++)
    {
      if (i >= localJSONArray.length()) {
        return localArrayList1;
      }
      JSONObject localJSONObject = localJSONArray.getJSONObject(i);
      HashMap localHashMap = new HashMap();
      localHashMap.put("itemid", localJSONObject.getString("itemid"));
      localHashMap.put("itemappid", localJSONObject.getString("itemappid"));
      localArrayList1.add(localHashMap);
    }
  }
  
  public List<Map<String, String>> getSyncWebCategory()
    throws Exception
  {
    ArrayList localArrayList1 = new ArrayList();
    String str = String.valueOf(this.sharedHelper.getGroup());
    ArrayList localArrayList2 = new ArrayList();
    localArrayList2.add(new BasicNameValuePair("usergroupid", str));
    JSONArray localJSONArray = new JSONObject(HttpHelper.post("http://www.fxlweb.com/AALifeWeb/GetSyncWebCategory.aspx", localArrayList2)).getJSONArray("catlist");
    for (int i = 0;; i++)
    {
      if (i >= localJSONArray.length()) {
        return localArrayList1;
      }
      JSONObject localJSONObject = localJSONArray.getJSONObject(i);
      HashMap localHashMap = new HashMap();
      localHashMap.put("catid", localJSONObject.getString("catid"));
      localHashMap.put("catname", localJSONObject.getString("catname"));
      localHashMap.put("catdisplay", localJSONObject.getString("catdisplay"));
      localHashMap.put("catlive", localJSONObject.getString("catlive"));
      localArrayList1.add(localHashMap);
    }
  }
  
  public List<Map<String, String>> getSyncWebFirst()
    throws Exception
  {
    ArrayList localArrayList1 = new ArrayList();
    String str1 = String.valueOf(this.sharedHelper.getUserId());
    String str2 = String.valueOf(this.sharedHelper.getGroup());
    ArrayList localArrayList2 = new ArrayList();
    localArrayList2.add(new BasicNameValuePair("userid", str1));
    localArrayList2.add(new BasicNameValuePair("usergroupid", str2));
    JSONArray localJSONArray = new JSONObject(HttpHelper.post("http://www.fxlweb.com/AALifeWeb/GetSyncWebFirstNew.aspx", localArrayList2)).getJSONArray("itemlist");
    for (int i = 0;; i++)
    {
      if (i >= localJSONArray.length()) {
        return localArrayList1;
      }
      JSONObject localJSONObject = localJSONArray.getJSONObject(i);
      HashMap localHashMap = new HashMap();
      localHashMap.put("itemid", localJSONObject.getString("itemid"));
      localHashMap.put("itemappid", localJSONObject.getString("itemappid"));
      localHashMap.put("itemname", localJSONObject.getString("itemname"));
      localHashMap.put("catid", localJSONObject.getString("catid"));
      localHashMap.put("itemprice", localJSONObject.getString("itemprice"));
      localHashMap.put("itembuydate", localJSONObject.getString("itembuydate"));
      localHashMap.put("recommend", localJSONObject.getString("recommend"));
      localArrayList1.add(localHashMap);
    }
  }
  
  public List<Map<String, String>> getSyncWebItem()
    throws Exception
  {
    ArrayList localArrayList1 = new ArrayList();
    String str1 = String.valueOf(this.sharedHelper.getUserId());
    String str2 = String.valueOf(this.sharedHelper.getGroup());
    ArrayList localArrayList2 = new ArrayList();
    localArrayList2.add(new BasicNameValuePair("userid", str1));
    localArrayList2.add(new BasicNameValuePair("usergroupid", str2));
    JSONArray localJSONArray = new JSONObject(HttpHelper.post("http://www.fxlweb.com/AALifeWeb/GetSyncWebItemNew.aspx", localArrayList2)).getJSONArray("itemlist");
    for (int i = 0;; i++)
    {
      if (i >= localJSONArray.length()) {
        return localArrayList1;
      }
      JSONObject localJSONObject = localJSONArray.getJSONObject(i);
      HashMap localHashMap = new HashMap();
      localHashMap.put("itemid", localJSONObject.getString("itemid"));
      localHashMap.put("itemappid", localJSONObject.getString("itemappid"));
      localHashMap.put("itemname", localJSONObject.getString("itemname"));
      localHashMap.put("catid", localJSONObject.getString("catid"));
      localHashMap.put("itemprice", localJSONObject.getString("itemprice"));
      localHashMap.put("itembuydate", localJSONObject.getString("itembuydate"));
      localHashMap.put("recommend", localJSONObject.getString("recommend"));
      localArrayList1.add(localHashMap);
    }
  }
  
  public void syncCategory(List<Map<String, String>> paramList)
    throws Exception
  {
    int i = 0;
    CategoryTableAccess localCategoryTableAccess = new CategoryTableAccess(this.sqlHelper.getReadableDatabase());
    Object localObject = "";
    String str1 = String.valueOf(this.sharedHelper.getGroup());
    Iterator localIterator = paramList.iterator();
    for (;;)
    {
      if (!localIterator.hasNext())
      {
        localCategoryTableAccess.close();
        if (i == 0) {
          break;
        }
        throw new Exception();
      }
      Map localMap = (Map)localIterator.next();
      ArrayList localArrayList = new ArrayList();
      localArrayList.add(new BasicNameValuePair("catid", (String)localMap.get("catid")));
      localArrayList.add(new BasicNameValuePair("catname", (String)localMap.get("catname")));
      localArrayList.add(new BasicNameValuePair("catdisplay", (String)localMap.get("catdisplay")));
      localArrayList.add(new BasicNameValuePair("catlive", (String)localMap.get("catlive")));
      localArrayList.add(new BasicNameValuePair("usergroupid", str1));
      try
      {
        JSONObject localJSONObject = new JSONObject(HttpHelper.post("http://www.fxlweb.com/AALifeWeb/SyncCategory.aspx", localArrayList));
        if (localJSONObject.length() > 0)
        {
          String str2 = localJSONObject.getString("result");
          localObject = str2;
        }
        if (!((String)localObject).equals("ok")) {
          break label309;
        }
        localCategoryTableAccess.updateSyncStatus(Integer.parseInt((String)localMap.get("catid")));
      }
      catch (Exception localException)
      {
        i = 1;
      }
      continue;
      label309:
      i = 1;
    }
  }
  
  public void syncDelWebItem(List<Map<String, String>> paramList)
    throws Exception
  {
    ItemTableAccess localItemTableAccess = new ItemTableAccess(this.sqlHelper.getReadableDatabase());
    Iterator localIterator = paramList.iterator();
    for (;;)
    {
      if (!localIterator.hasNext())
      {
        localItemTableAccess.close();
        return;
      }
      Map localMap = (Map)localIterator.next();
      localItemTableAccess.delWebItem(Integer.parseInt((String)localMap.get("itemid")), Integer.parseInt((String)localMap.get("itemappid")));
    }
  }
  
  public void syncDelWebItemBack()
    throws Exception
  {
    String str1 = "";
    String str2 = String.valueOf(this.sharedHelper.getGroup());
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new BasicNameValuePair("usergroupid", str2));
    JSONObject localJSONObject = new JSONObject(HttpHelper.post("http://www.fxlweb.com/AALifeWeb/SyncDelWebItemBack.aspx", localArrayList));
    if (localJSONObject.length() > 0) {
      str1 = localJSONObject.getString("result");
    }
    if (!str1.equals("ok")) {
      throw new Exception();
    }
  }
  
  public void syncItem(List<Map<String, String>> paramList)
    throws Exception
  {
    int i = 0;
    ItemTableAccess localItemTableAccess = new ItemTableAccess(this.sqlHelper.getReadableDatabase());
    Object localObject = "";
    String str1 = String.valueOf(this.sharedHelper.getUserId());
    String str2 = String.valueOf(this.sharedHelper.getGroup());
    Iterator localIterator = paramList.iterator();
    for (;;)
    {
      if ((!localIterator.hasNext()) || (!this.sharedHelper.getSyncing()))
      {
        localItemTableAccess.close();
        if (i == 0) {
          break;
        }
        throw new Exception();
      }
      Map localMap = (Map)localIterator.next();
      ArrayList localArrayList = new ArrayList();
      localArrayList.add(new BasicNameValuePair("itemid", (String)localMap.get("itemid")));
      localArrayList.add(new BasicNameValuePair("itemname", (String)localMap.get("itemname")));
      localArrayList.add(new BasicNameValuePair("catid", (String)localMap.get("catid")));
      localArrayList.add(new BasicNameValuePair("itemprice", (String)localMap.get("itemprice")));
      localArrayList.add(new BasicNameValuePair("itembuydate", (String)localMap.get("itembuydate")));
      localArrayList.add(new BasicNameValuePair("userid", str1));
      localArrayList.add(new BasicNameValuePair("usergroupid", str2));
      localArrayList.add(new BasicNameValuePair("itemwebid", (String)localMap.get("itemwebid")));
      localArrayList.add(new BasicNameValuePair("recommend", (String)localMap.get("recommend")));
      try
      {
        JSONObject localJSONObject = new JSONObject(HttpHelper.post("http://www.fxlweb.com/AALifeWeb/SyncItemNew.aspx", localArrayList));
        if (localJSONObject.length() > 0)
        {
          String str3 = localJSONObject.getString("result");
          localObject = str3;
        }
        int j = Integer.parseInt((String)localMap.get("itemid"));
        if ((((String)localObject).equals("0")) && (((String)localObject).equals("no"))) {
          break label464;
        }
        localItemTableAccess.updateSyncStatus(j, Integer.parseInt((String)localObject));
      }
      catch (Exception localException)
      {
        i = 0;
      }
      continue;
      label464:
      i = 1;
    }
  }
  
  public int[] syncUserName(String paramString)
  {
    int[] arrayOfInt = new int[2];
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new BasicNameValuePair("username", paramString));
    localArrayList.add(new BasicNameValuePair("userpass", paramString));
    try
    {
      JSONObject localJSONObject = new JSONObject(HttpHelper.post("http://www.fxlweb.com/AALifeWeb/SyncUser.aspx", localArrayList));
      if (localJSONObject.length() > 0)
      {
        arrayOfInt[0] = localJSONObject.getInt("group");
        arrayOfInt[1] = localJSONObject.getInt("userid");
      }
      return arrayOfInt;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return arrayOfInt;
  }
  
  public void syncWebCategory(List<Map<String, String>> paramList)
    throws Exception
  {
    CategoryTableAccess localCategoryTableAccess = new CategoryTableAccess(this.sqlHelper.getReadableDatabase());
    Iterator localIterator = paramList.iterator();
    for (;;)
    {
      if (!localIterator.hasNext())
      {
        localCategoryTableAccess.close();
        return;
      }
      Map localMap = (Map)localIterator.next();
      localCategoryTableAccess.saveWebCategory(Integer.parseInt((String)localMap.get("catid")), (String)localMap.get("catname"), Integer.parseInt((String)localMap.get("catdisplay")), Integer.parseInt((String)localMap.get("catlive")));
    }
  }
  
  public void syncWebCategoryBack()
    throws Exception
  {
    String str1 = "";
    String str2 = String.valueOf(this.sharedHelper.getGroup());
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new BasicNameValuePair("usergroupid", str2));
    JSONObject localJSONObject = new JSONObject(HttpHelper.post("http://www.fxlweb.com/AALifeWeb/SyncWebCategoryBack.aspx", localArrayList));
    if (localJSONObject.length() > 0) {
      str1 = localJSONObject.getString("result");
    }
    if (!str1.equals("ok")) {
      throw new Exception();
    }
  }
  
  public void syncWebItem(List<Map<String, String>> paramList)
    throws Exception
  {
    int i = 0;
    ItemTableAccess localItemTableAccess = new ItemTableAccess(this.sqlHelper.getReadableDatabase());
    Iterator localIterator = paramList.iterator();
    for (;;)
    {
      if ((!localIterator.hasNext()) || (!this.sharedHelper.getSyncing()))
      {
        localItemTableAccess.close();
        if (i == 0) {
          break;
        }
        throw new Exception();
      }
      Map localMap = (Map)localIterator.next();
      int j = Integer.parseInt((String)localMap.get("itemid"));
      int k = Integer.parseInt((String)localMap.get("itemappid"));
      String str1 = (String)localMap.get("itemname");
      String str2 = (String)localMap.get("itemprice");
      String str3 = (String)localMap.get("itembuydate");
      int m = Integer.parseInt((String)localMap.get("catid"));
      int n = Integer.parseInt((String)localMap.get("recommend"));
      this.sharedHelper.setCurDate(str3);
      if (!localItemTableAccess.addWebItem(j, k, str1, str2, str3, m, n))
      {
        i = 1;
      }
      else
      {
        if (k == 0) {
          k = localItemTableAccess.getItemId(j);
        }
        if (!syncWebItemBack(j, k)) {
          i = 1;
        }
      }
    }
  }
  
  public boolean syncWebItemBack(int paramInt1, int paramInt2)
  {
    Object localObject = "";
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new BasicNameValuePair("itemid", String.valueOf(paramInt1)));
    localArrayList.add(new BasicNameValuePair("itemappid", String.valueOf(paramInt2)));
    try
    {
      JSONObject localJSONObject = new JSONObject(HttpHelper.post("http://www.fxlweb.com/AALifeWeb/SyncWebItemBackNew.aspx", localArrayList));
      if (localJSONObject.length() > 0)
      {
        String str = localJSONObject.getString("result");
        localObject = str;
      }
      return ((String)localObject).equals("ok");
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return false;
  }
}


/* Location:           D:\fxlandroid\document\app\AALifeNew_v3.1.4\classes_dex2jar.jar
 * Qualified Name:     com.aalife.android.SyncHelper
 * JD-Core Version:    0.7.0-SNAPSHOT-20130630
 */