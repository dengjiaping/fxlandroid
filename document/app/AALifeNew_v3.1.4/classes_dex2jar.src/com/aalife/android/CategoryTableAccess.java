package com.aalife.android;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CategoryTableAccess
{
  private static final String CATTABNAME = "CategoryTable";
  private static final String ITEMTABNAME = "ItemTable";
  private SQLiteDatabase db = null;
  
  public CategoryTableAccess(SQLiteDatabase paramSQLiteDatabase)
  {
    this.db = paramSQLiteDatabase;
  }
  
  public void close()
  {
    this.db.close();
  }
  
  public int delCategory(int paramInt)
  {
    String str1 = "SELECT * FROM ItemTable WHERE CategoryID = " + paramInt;
    Cursor localCursor = null;
    try
    {
      localCursor = this.db.rawQuery(str1, null);
      boolean bool = localCursor.moveToFirst();
      if (bool) {
        return 2;
      }
      String str2 = "UPDATE CategoryTable SET CategoryLive = '0', Synchronize = '1', IsDefault = '0' WHERE CategoryID = " + paramInt;
      this.db.execSQL(str2);
      return 1;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
      return 0;
    }
    finally
    {
      if (localCursor != null) {
        localCursor.close();
      }
    }
  }
  
  /* Error */
  public java.util.List<java.util.Map<String, String>> findAllCatEdit()
  {
    // Byte code:
    //   0: new 67	java/util/ArrayList
    //   3: dup
    //   4: invokespecial 68	java/util/ArrayList:<init>	()V
    //   7: astore_1
    //   8: aconst_null
    //   9: astore_2
    //   10: aload_0
    //   11: getfield 20	com/aalife/android/CategoryTableAccess:db	Landroid/database/sqlite/SQLiteDatabase;
    //   14: ldc 70
    //   16: aconst_null
    //   17: invokevirtual 48	android/database/sqlite/SQLiteDatabase:rawQuery	(Ljava/lang/String;[Ljava/lang/String;)Landroid/database/Cursor;
    //   20: astore_2
    //   21: aload_2
    //   22: invokeinterface 73 1 0
    //   27: istore 5
    //   29: iload 5
    //   31: ifne +15 -> 46
    //   34: aload_2
    //   35: ifnull +9 -> 44
    //   38: aload_2
    //   39: invokeinterface 55 1 0
    //   44: aload_1
    //   45: areturn
    //   46: new 75	java/util/HashMap
    //   49: dup
    //   50: invokespecial 76	java/util/HashMap:<init>	()V
    //   53: astore 6
    //   55: aload 6
    //   57: ldc 78
    //   59: iconst_1
    //   60: aload_2
    //   61: invokeinterface 82 1 0
    //   66: iadd
    //   67: invokestatic 88	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   70: invokeinterface 94 3 0
    //   75: pop
    //   76: aload 6
    //   78: ldc 96
    //   80: aload_2
    //   81: iconst_0
    //   82: invokeinterface 99 2 0
    //   87: invokeinterface 94 3 0
    //   92: pop
    //   93: aload 6
    //   95: ldc 101
    //   97: aload_2
    //   98: iconst_1
    //   99: invokeinterface 99 2 0
    //   104: invokeinterface 94 3 0
    //   109: pop
    //   110: aload 6
    //   112: ldc 103
    //   114: aload_2
    //   115: iconst_2
    //   116: invokeinterface 99 2 0
    //   121: invokeinterface 94 3 0
    //   126: pop
    //   127: aload 6
    //   129: ldc 105
    //   131: ldc 107
    //   133: invokeinterface 94 3 0
    //   138: pop
    //   139: aload_1
    //   140: aload 6
    //   142: invokeinterface 113 2 0
    //   147: pop
    //   148: goto -127 -> 21
    //   151: astore 4
    //   153: aload 4
    //   155: invokevirtual 63	java/lang/Exception:printStackTrace	()V
    //   158: aload_2
    //   159: ifnull -115 -> 44
    //   162: aload_2
    //   163: invokeinterface 55 1 0
    //   168: aload_1
    //   169: areturn
    //   170: astore_3
    //   171: aload_2
    //   172: ifnull +9 -> 181
    //   175: aload_2
    //   176: invokeinterface 55 1 0
    //   181: aload_3
    //   182: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	183	0	this	CategoryTableAccess
    //   7	162	1	localArrayList	java.util.ArrayList
    //   9	167	2	localCursor	Cursor
    //   170	12	3	localObject	Object
    //   151	3	4	localException	Exception
    //   27	3	5	bool	boolean
    //   53	88	6	localHashMap	java.util.HashMap
    // Exception table:
    //   from	to	target	type
    //   10	21	151	java/lang/Exception
    //   21	29	151	java/lang/Exception
    //   46	148	151	java/lang/Exception
    //   10	21	170	finally
    //   21	29	170	finally
    //   46	148	170	finally
    //   153	158	170	finally
  }
  
  /* Error */
  public java.util.List<java.lang.CharSequence> findAllCategory()
  {
    // Byte code:
    //   0: new 67	java/util/ArrayList
    //   3: dup
    //   4: invokespecial 68	java/util/ArrayList:<init>	()V
    //   7: astore_1
    //   8: aconst_null
    //   9: astore_2
    //   10: aload_0
    //   11: getfield 20	com/aalife/android/CategoryTableAccess:db	Landroid/database/sqlite/SQLiteDatabase;
    //   14: ldc 116
    //   16: aconst_null
    //   17: invokevirtual 48	android/database/sqlite/SQLiteDatabase:rawQuery	(Ljava/lang/String;[Ljava/lang/String;)Landroid/database/Cursor;
    //   20: astore_2
    //   21: aload_2
    //   22: invokeinterface 73 1 0
    //   27: istore 5
    //   29: iload 5
    //   31: ifne +15 -> 46
    //   34: aload_2
    //   35: ifnull +9 -> 44
    //   38: aload_2
    //   39: invokeinterface 55 1 0
    //   44: aload_1
    //   45: areturn
    //   46: aload_1
    //   47: aload_2
    //   48: iconst_0
    //   49: invokeinterface 99 2 0
    //   54: invokeinterface 113 2 0
    //   59: pop
    //   60: goto -39 -> 21
    //   63: astore 4
    //   65: aload 4
    //   67: invokevirtual 63	java/lang/Exception:printStackTrace	()V
    //   70: aload_2
    //   71: ifnull -27 -> 44
    //   74: aload_2
    //   75: invokeinterface 55 1 0
    //   80: aload_1
    //   81: areturn
    //   82: astore_3
    //   83: aload_2
    //   84: ifnull +9 -> 93
    //   87: aload_2
    //   88: invokeinterface 55 1 0
    //   93: aload_3
    //   94: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	95	0	this	CategoryTableAccess
    //   7	74	1	localArrayList	java.util.ArrayList
    //   9	79	2	localCursor	Cursor
    //   82	12	3	localObject	Object
    //   63	3	4	localException	Exception
    //   27	3	5	bool	boolean
    // Exception table:
    //   from	to	target	type
    //   10	21	63	java/lang/Exception
    //   21	29	63	java/lang/Exception
    //   46	60	63	java/lang/Exception
    //   10	21	82	finally
    //   21	29	82	finally
    //   46	60	82	finally
    //   65	70	82	finally
  }
  
  /* Error */
  public java.util.List<java.util.Map<String, String>> findAllCategorySmart()
  {
    // Byte code:
    //   0: new 67	java/util/ArrayList
    //   3: dup
    //   4: invokespecial 68	java/util/ArrayList:<init>	()V
    //   7: astore_1
    //   8: aconst_null
    //   9: astore_2
    //   10: aload_0
    //   11: getfield 20	com/aalife/android/CategoryTableAccess:db	Landroid/database/sqlite/SQLiteDatabase;
    //   14: ldc 119
    //   16: aconst_null
    //   17: invokevirtual 48	android/database/sqlite/SQLiteDatabase:rawQuery	(Ljava/lang/String;[Ljava/lang/String;)Landroid/database/Cursor;
    //   20: astore_2
    //   21: aload_2
    //   22: invokeinterface 73 1 0
    //   27: istore 5
    //   29: iload 5
    //   31: ifne +15 -> 46
    //   34: aload_2
    //   35: ifnull +9 -> 44
    //   38: aload_2
    //   39: invokeinterface 55 1 0
    //   44: aload_1
    //   45: areturn
    //   46: new 75	java/util/HashMap
    //   49: dup
    //   50: invokespecial 76	java/util/HashMap:<init>	()V
    //   53: astore 6
    //   55: aload 6
    //   57: ldc 78
    //   59: aload_2
    //   60: iconst_0
    //   61: invokeinterface 99 2 0
    //   66: invokeinterface 94 3 0
    //   71: pop
    //   72: aload 6
    //   74: ldc 121
    //   76: aload_2
    //   77: iconst_1
    //   78: invokeinterface 99 2 0
    //   83: invokeinterface 94 3 0
    //   88: pop
    //   89: aload_1
    //   90: aload 6
    //   92: invokeinterface 113 2 0
    //   97: pop
    //   98: goto -77 -> 21
    //   101: astore 4
    //   103: aload 4
    //   105: invokevirtual 63	java/lang/Exception:printStackTrace	()V
    //   108: aload_2
    //   109: ifnull -65 -> 44
    //   112: aload_2
    //   113: invokeinterface 55 1 0
    //   118: aload_1
    //   119: areturn
    //   120: astore_3
    //   121: aload_2
    //   122: ifnull +9 -> 131
    //   125: aload_2
    //   126: invokeinterface 55 1 0
    //   131: aload_3
    //   132: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	133	0	this	CategoryTableAccess
    //   7	112	1	localArrayList	java.util.ArrayList
    //   9	117	2	localCursor	Cursor
    //   120	12	3	localObject	Object
    //   101	3	4	localException	Exception
    //   27	3	5	bool	boolean
    //   53	38	6	localHashMap	java.util.HashMap
    // Exception table:
    //   from	to	target	type
    //   10	21	101	java/lang/Exception
    //   21	29	101	java/lang/Exception
    //   46	98	101	java/lang/Exception
    //   10	21	120	finally
    //   21	29	120	finally
    //   46	98	120	finally
    //   103	108	120	finally
  }
  
  /* Error */
  public java.util.List<java.util.Map<String, String>> findAllSyncCat()
  {
    // Byte code:
    //   0: new 67	java/util/ArrayList
    //   3: dup
    //   4: invokespecial 68	java/util/ArrayList:<init>	()V
    //   7: astore_1
    //   8: aconst_null
    //   9: astore_2
    //   10: aload_0
    //   11: getfield 20	com/aalife/android/CategoryTableAccess:db	Landroid/database/sqlite/SQLiteDatabase;
    //   14: ldc 124
    //   16: aconst_null
    //   17: invokevirtual 48	android/database/sqlite/SQLiteDatabase:rawQuery	(Ljava/lang/String;[Ljava/lang/String;)Landroid/database/Cursor;
    //   20: astore_2
    //   21: aload_2
    //   22: invokeinterface 73 1 0
    //   27: istore 5
    //   29: iload 5
    //   31: ifne +15 -> 46
    //   34: aload_2
    //   35: ifnull +9 -> 44
    //   38: aload_2
    //   39: invokeinterface 55 1 0
    //   44: aload_1
    //   45: areturn
    //   46: new 75	java/util/HashMap
    //   49: dup
    //   50: invokespecial 76	java/util/HashMap:<init>	()V
    //   53: astore 6
    //   55: aload 6
    //   57: ldc 78
    //   59: iconst_1
    //   60: aload_2
    //   61: invokeinterface 82 1 0
    //   66: iadd
    //   67: invokestatic 88	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   70: invokeinterface 94 3 0
    //   75: pop
    //   76: aload 6
    //   78: ldc 96
    //   80: aload_2
    //   81: iconst_0
    //   82: invokeinterface 99 2 0
    //   87: invokeinterface 94 3 0
    //   92: pop
    //   93: aload 6
    //   95: ldc 101
    //   97: aload_2
    //   98: iconst_1
    //   99: invokeinterface 99 2 0
    //   104: invokeinterface 94 3 0
    //   109: pop
    //   110: aload 6
    //   112: ldc 103
    //   114: aload_2
    //   115: iconst_2
    //   116: invokeinterface 99 2 0
    //   121: invokeinterface 94 3 0
    //   126: pop
    //   127: aload 6
    //   129: ldc 126
    //   131: aload_2
    //   132: iconst_3
    //   133: invokeinterface 99 2 0
    //   138: invokeinterface 94 3 0
    //   143: pop
    //   144: aload_1
    //   145: aload 6
    //   147: invokeinterface 113 2 0
    //   152: pop
    //   153: goto -132 -> 21
    //   156: astore 4
    //   158: aload 4
    //   160: invokevirtual 63	java/lang/Exception:printStackTrace	()V
    //   163: aload_2
    //   164: ifnull -120 -> 44
    //   167: aload_2
    //   168: invokeinterface 55 1 0
    //   173: aload_1
    //   174: areturn
    //   175: astore_3
    //   176: aload_2
    //   177: ifnull +9 -> 186
    //   180: aload_2
    //   181: invokeinterface 55 1 0
    //   186: aload_3
    //   187: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	188	0	this	CategoryTableAccess
    //   7	167	1	localArrayList	java.util.ArrayList
    //   9	172	2	localCursor	Cursor
    //   175	12	3	localObject	Object
    //   156	3	4	localException	Exception
    //   27	3	5	bool	boolean
    //   53	93	6	localHashMap	java.util.HashMap
    // Exception table:
    //   from	to	target	type
    //   10	21	156	java/lang/Exception
    //   21	29	156	java/lang/Exception
    //   46	153	156	java/lang/Exception
    //   10	21	175	finally
    //   21	29	175	finally
    //   46	153	175	finally
    //   158	163	175	finally
  }
  
  public int findCatIdByName(String paramString)
  {
    String str = "SELECT CategoryID FROM CategoryTable WHERE CategoryName = '" + paramString + "'";
    Cursor localCursor = null;
    try
    {
      localCursor = this.db.rawQuery(str, null);
      boolean bool = localCursor.moveToFirst();
      i = 0;
      if (bool)
      {
        int j = Integer.parseInt(localCursor.getString(0));
        i = j;
      }
      return i;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
      int i = 0;
      return 0;
    }
    finally
    {
      if (localCursor != null) {
        localCursor.close();
      }
    }
  }
  
  public String findCatNameById(int paramInt)
  {
    Object localObject1 = "";
    String str1 = "SELECT CategoryName FROM CategoryTable WHERE CategoryID = '" + paramInt + "'";
    Cursor localCursor = null;
    try
    {
      localCursor = this.db.rawQuery(str1, null);
      if (localCursor.moveToFirst())
      {
        String str2 = localCursor.getString(0);
        localObject1 = str2;
      }
      return localObject1;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
      return localObject1;
    }
    finally
    {
      if (localCursor != null) {
        localCursor.close();
      }
    }
  }
  
  /* Error */
  public boolean saveCategory(int paramInt, String paramString)
  {
    // Byte code:
    //   0: new 31	java/lang/StringBuilder
    //   3: dup
    //   4: ldc 149
    //   6: invokespecial 36	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   9: aload_2
    //   10: invokevirtual 133	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   13: ldc 151
    //   15: invokevirtual 133	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   18: invokevirtual 44	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   21: astore_3
    //   22: aconst_null
    //   23: astore 4
    //   25: aload_0
    //   26: getfield 20	com/aalife/android/CategoryTableAccess:db	Landroid/database/sqlite/SQLiteDatabase;
    //   29: aload_3
    //   30: aconst_null
    //   31: invokevirtual 48	android/database/sqlite/SQLiteDatabase:rawQuery	(Ljava/lang/String;[Ljava/lang/String;)Landroid/database/Cursor;
    //   34: astore 4
    //   36: aload 4
    //   38: invokeinterface 54 1 0
    //   43: istore 7
    //   45: iload 7
    //   47: ifeq +17 -> 64
    //   50: aload 4
    //   52: ifnull +10 -> 62
    //   55: aload 4
    //   57: invokeinterface 55 1 0
    //   62: iconst_0
    //   63: ireturn
    //   64: iload_1
    //   65: ifne +111 -> 176
    //   68: aload_0
    //   69: getfield 20	com/aalife/android/CategoryTableAccess:db	Landroid/database/sqlite/SQLiteDatabase;
    //   72: ldc 153
    //   74: aconst_null
    //   75: invokevirtual 48	android/database/sqlite/SQLiteDatabase:rawQuery	(Ljava/lang/String;[Ljava/lang/String;)Landroid/database/Cursor;
    //   78: astore 4
    //   80: aload 4
    //   82: invokeinterface 54 1 0
    //   87: istore 10
    //   89: iconst_0
    //   90: istore 11
    //   92: iload 10
    //   94: ifeq +16 -> 110
    //   97: aload 4
    //   99: iconst_0
    //   100: invokeinterface 99 2 0
    //   105: invokestatic 140	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   108: istore 11
    //   110: new 31	java/lang/StringBuilder
    //   113: dup
    //   114: ldc 155
    //   116: invokespecial 36	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   119: iload 11
    //   121: invokevirtual 40	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   124: ldc 157
    //   126: invokevirtual 133	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   129: aload_2
    //   130: invokevirtual 133	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   133: ldc 159
    //   135: invokevirtual 133	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   138: iload 11
    //   140: invokevirtual 40	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   143: ldc 161
    //   145: invokevirtual 133	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   148: invokevirtual 44	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   151: astore 9
    //   153: aload_0
    //   154: getfield 20	com/aalife/android/CategoryTableAccess:db	Landroid/database/sqlite/SQLiteDatabase;
    //   157: aload 9
    //   159: invokevirtual 60	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
    //   162: aload 4
    //   164: ifnull +10 -> 174
    //   167: aload 4
    //   169: invokeinterface 55 1 0
    //   174: iconst_1
    //   175: ireturn
    //   176: new 31	java/lang/StringBuilder
    //   179: dup
    //   180: ldc 163
    //   182: invokespecial 36	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   185: aload_2
    //   186: invokevirtual 133	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   189: ldc 165
    //   191: invokevirtual 133	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   194: iload_1
    //   195: invokevirtual 40	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   198: invokevirtual 44	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   201: astore 8
    //   203: aload 8
    //   205: astore 9
    //   207: goto -54 -> 153
    //   210: astore 6
    //   212: aload 6
    //   214: invokevirtual 63	java/lang/Exception:printStackTrace	()V
    //   217: aload 4
    //   219: ifnull -157 -> 62
    //   222: aload 4
    //   224: invokeinterface 55 1 0
    //   229: iconst_0
    //   230: ireturn
    //   231: astore 5
    //   233: aload 4
    //   235: ifnull +10 -> 245
    //   238: aload 4
    //   240: invokeinterface 55 1 0
    //   245: aload 5
    //   247: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	248	0	this	CategoryTableAccess
    //   0	248	1	paramInt	int
    //   0	248	2	paramString	String
    //   21	9	3	str1	String
    //   23	216	4	localCursor	Cursor
    //   231	15	5	localObject1	Object
    //   210	3	6	localException	Exception
    //   43	3	7	bool1	boolean
    //   201	3	8	str2	String
    //   151	55	9	localObject2	Object
    //   87	6	10	bool2	boolean
    //   90	49	11	i	int
    // Exception table:
    //   from	to	target	type
    //   25	45	210	java/lang/Exception
    //   68	89	210	java/lang/Exception
    //   97	110	210	java/lang/Exception
    //   110	153	210	java/lang/Exception
    //   153	162	210	java/lang/Exception
    //   176	203	210	java/lang/Exception
    //   25	45	231	finally
    //   68	89	231	finally
    //   97	110	231	finally
    //   110	153	231	finally
    //   153	162	231	finally
    //   176	203	231	finally
    //   212	217	231	finally
  }
  
  public void saveWebCategory(int paramInt1, String paramString, int paramInt2, int paramInt3)
    throws Exception
  {
    String str1 = "SELECT * FROM CategoryTable WHERE CategoryID = " + paramInt1;
    Cursor localCursor = this.db.rawQuery(str1, null);
    if (localCursor.moveToFirst()) {}
    for (String str2 = "UPDATE CategoryTable SET CategoryName = '" + paramString + "', CategoryDisplay = '" + paramInt2 + "', CategoryLive = '" + paramInt3 + "', Synchronize = '0', IsDefault = '0' WHERE CategoryID = " + paramInt1;; str2 = "INSERT INTO CategoryTable(CategoryID, CategoryName, CategoryDisplay, CategoryLive, Synchronize, CategoryRank) VALUES ('" + paramInt1 + "', '" + paramString + "', '" + paramInt2 + "', '" + paramInt3 + "', '0', '" + paramInt1 + "')")
    {
      this.db.execSQL(str2);
      if (localCursor != null) {
        localCursor.close();
      }
      return;
    }
  }
  
  public boolean updateCatDisplay(int paramInt1, int paramInt2)
  {
    String str = "UPDATE CategoryTable SET CategoryDisplay = '" + paramInt2 + "', Synchronize = '1', IsDefault = '0' WHERE CategoryID = " + paramInt1;
    try
    {
      this.db.execSQL(str);
      return true;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return false;
  }
  
  public void updateSyncStatus(int paramInt)
  {
    String str = "UPDATE CategoryTable SET Synchronize = '0' WHERE CategoryID = " + paramInt;
    try
    {
      this.db.execSQL(str);
      return;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
}


/* Location:           D:\fxlandroid\document\app\AALifeNew_v3.1.4\classes_dex2jar.jar
 * Qualified Name:     com.aalife.android.CategoryTableAccess
 * JD-Core Version:    0.7.0-SNAPSHOT-20130630
 */