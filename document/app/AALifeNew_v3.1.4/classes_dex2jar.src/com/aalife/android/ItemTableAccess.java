package com.aalife.android;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.HashMap;
import java.util.Map;

public class ItemTableAccess
{
  private static final String CATTABNAME = "CategoryTable";
  private static final String DELTABNAME = "DeleteTable";
  private static final String ITEMTABNAME = "ItemTable";
  private SQLiteDatabase db = null;
  
  public ItemTableAccess(SQLiteDatabase paramSQLiteDatabase)
  {
    this.db = paramSQLiteDatabase;
  }
  
  public boolean addItem(String paramString1, String paramString2, String paramString3, int paramInt1, int paramInt2)
  {
    String str = "INSERT INTO ItemTable(ItemName, ItemPrice, ItemBuyDate, CategoryID, Synchronize, Recommend) VALUES ('" + paramString1 + "', '" + paramString2 + "', '" + paramString3 + "', '" + paramInt1 + "', '1', '" + paramInt2 + "')";
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
  
  /* Error */
  public boolean addWebItem(int paramInt1, int paramInt2, String paramString1, String paramString2, String paramString3, int paramInt3, int paramInt4)
  {
    // Byte code:
    //   0: new 29	java/lang/StringBuilder
    //   3: dup
    //   4: ldc 63
    //   6: invokespecial 34	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   9: iload_1
    //   10: invokevirtual 43	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   13: invokevirtual 51	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   16: astore 8
    //   18: aconst_null
    //   19: astore 9
    //   21: aload_0
    //   22: getfield 23	com/aalife/android/ItemTableAccess:db	Landroid/database/sqlite/SQLiteDatabase;
    //   25: aload 8
    //   27: aconst_null
    //   28: invokevirtual 67	android/database/sqlite/SQLiteDatabase:rawQuery	(Ljava/lang/String;[Ljava/lang/String;)Landroid/database/Cursor;
    //   31: astore 9
    //   33: aload 9
    //   35: invokeinterface 73 1 0
    //   40: ifeq +109 -> 149
    //   43: new 29	java/lang/StringBuilder
    //   46: dup
    //   47: ldc 75
    //   49: invokespecial 34	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   52: aload_3
    //   53: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   56: ldc 77
    //   58: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   61: aload 4
    //   63: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   66: ldc 79
    //   68: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   71: aload 5
    //   73: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   76: ldc 81
    //   78: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   81: iload 6
    //   83: invokevirtual 43	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   86: ldc 83
    //   88: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   91: iload 7
    //   93: invokevirtual 43	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   96: ldc 85
    //   98: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   101: ldc 87
    //   103: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   106: aload 9
    //   108: iconst_0
    //   109: invokeinterface 91 2 0
    //   114: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   117: invokevirtual 51	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   120: astore 14
    //   122: aload_0
    //   123: getfield 23	com/aalife/android/ItemTableAccess:db	Landroid/database/sqlite/SQLiteDatabase;
    //   126: aload 14
    //   128: invokevirtual 56	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
    //   131: aload 9
    //   133: ifnull +10 -> 143
    //   136: aload 9
    //   138: invokeinterface 94 1 0
    //   143: iconst_1
    //   144: istore 12
    //   146: iload 12
    //   148: ireturn
    //   149: iload_2
    //   150: ifle +78 -> 228
    //   153: new 29	java/lang/StringBuilder
    //   156: dup
    //   157: ldc 75
    //   159: invokespecial 34	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   162: aload_3
    //   163: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   166: ldc 77
    //   168: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   171: aload 4
    //   173: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   176: ldc 79
    //   178: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   181: aload 5
    //   183: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   186: ldc 81
    //   188: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   191: iload 6
    //   193: invokevirtual 43	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   196: ldc 83
    //   198: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   201: iload 7
    //   203: invokevirtual 43	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   206: ldc 85
    //   208: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   211: ldc 87
    //   213: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   216: iload_2
    //   217: invokevirtual 43	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   220: invokevirtual 51	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   223: astore 14
    //   225: goto -103 -> 122
    //   228: new 29	java/lang/StringBuilder
    //   231: dup
    //   232: ldc 96
    //   234: invokespecial 34	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   237: iload_1
    //   238: invokevirtual 43	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   241: ldc 40
    //   243: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   246: aload_3
    //   247: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   250: ldc 40
    //   252: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   255: aload 4
    //   257: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   260: ldc 40
    //   262: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   265: aload 5
    //   267: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   270: ldc 40
    //   272: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   275: iload 6
    //   277: invokevirtual 43	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   280: ldc 98
    //   282: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   285: iload 7
    //   287: invokevirtual 43	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   290: ldc 47
    //   292: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   295: invokevirtual 51	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   298: astore 13
    //   300: aload 13
    //   302: astore 14
    //   304: goto -182 -> 122
    //   307: astore 11
    //   309: aload 11
    //   311: invokevirtual 59	java/lang/Exception:printStackTrace	()V
    //   314: iconst_0
    //   315: istore 12
    //   317: aload 9
    //   319: ifnull -173 -> 146
    //   322: aload 9
    //   324: invokeinterface 94 1 0
    //   329: iconst_0
    //   330: ireturn
    //   331: astore 10
    //   333: aload 9
    //   335: ifnull +10 -> 345
    //   338: aload 9
    //   340: invokeinterface 94 1 0
    //   345: aload 10
    //   347: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	348	0	this	ItemTableAccess
    //   0	348	1	paramInt1	int
    //   0	348	2	paramInt2	int
    //   0	348	3	paramString1	String
    //   0	348	4	paramString2	String
    //   0	348	5	paramString3	String
    //   0	348	6	paramInt3	int
    //   0	348	7	paramInt4	int
    //   16	10	8	str1	String
    //   19	320	9	localCursor	Cursor
    //   331	15	10	localObject1	Object
    //   307	3	11	localException	Exception
    //   144	172	12	bool	boolean
    //   298	3	13	str2	String
    //   120	183	14	localObject2	Object
    // Exception table:
    //   from	to	target	type
    //   21	122	307	java/lang/Exception
    //   122	131	307	java/lang/Exception
    //   153	225	307	java/lang/Exception
    //   228	300	307	java/lang/Exception
    //   21	122	331	finally
    //   122	131	331	finally
    //   153	225	331	finally
    //   228	300	331	finally
    //   309	314	331	finally
  }
  
  /* Error */
  public java.util.List<java.lang.CharSequence> bakDataBase()
  {
    // Byte code:
    //   0: new 102	java/util/ArrayList
    //   3: dup
    //   4: invokespecial 103	java/util/ArrayList:<init>	()V
    //   7: astore_1
    //   8: aconst_null
    //   9: astore_2
    //   10: aload_0
    //   11: getfield 23	com/aalife/android/ItemTableAccess:db	Landroid/database/sqlite/SQLiteDatabase;
    //   14: ldc 105
    //   16: aconst_null
    //   17: invokevirtual 67	android/database/sqlite/SQLiteDatabase:rawQuery	(Ljava/lang/String;[Ljava/lang/String;)Landroid/database/Cursor;
    //   20: astore_2
    //   21: aload_2
    //   22: invokeinterface 108 1 0
    //   27: ifne +39 -> 66
    //   30: aload_0
    //   31: getfield 23	com/aalife/android/ItemTableAccess:db	Landroid/database/sqlite/SQLiteDatabase;
    //   34: ldc 110
    //   36: aconst_null
    //   37: invokevirtual 67	android/database/sqlite/SQLiteDatabase:rawQuery	(Ljava/lang/String;[Ljava/lang/String;)Landroid/database/Cursor;
    //   40: astore_2
    //   41: aload_2
    //   42: invokeinterface 108 1 0
    //   47: istore 6
    //   49: iload 6
    //   51: ifne +162 -> 213
    //   54: aload_2
    //   55: ifnull +9 -> 64
    //   58: aload_2
    //   59: invokeinterface 94 1 0
    //   64: aload_1
    //   65: areturn
    //   66: aload_1
    //   67: new 29	java/lang/StringBuilder
    //   70: dup
    //   71: ldc 112
    //   73: invokespecial 34	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   76: aload_2
    //   77: iconst_0
    //   78: invokeinterface 91 2 0
    //   83: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   86: ldc 40
    //   88: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   91: aload_2
    //   92: iconst_1
    //   93: invokeinterface 91 2 0
    //   98: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   101: ldc 40
    //   103: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   106: aload_2
    //   107: iconst_2
    //   108: invokeinterface 91 2 0
    //   113: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   116: ldc 40
    //   118: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   121: aload_2
    //   122: iconst_3
    //   123: invokeinterface 91 2 0
    //   128: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   131: ldc 40
    //   133: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   136: aload_2
    //   137: iconst_4
    //   138: invokeinterface 91 2 0
    //   143: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   146: ldc 40
    //   148: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   151: aload_2
    //   152: iconst_5
    //   153: invokeinterface 91 2 0
    //   158: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   161: ldc 40
    //   163: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   166: aload_2
    //   167: bipush 6
    //   169: invokeinterface 91 2 0
    //   174: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   177: ldc 114
    //   179: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   182: invokevirtual 51	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   185: invokeinterface 120 2 0
    //   190: pop
    //   191: goto -170 -> 21
    //   194: astore 4
    //   196: aload 4
    //   198: invokevirtual 59	java/lang/Exception:printStackTrace	()V
    //   201: aload_2
    //   202: ifnull -138 -> 64
    //   205: aload_2
    //   206: invokeinterface 94 1 0
    //   211: aload_1
    //   212: areturn
    //   213: aload_1
    //   214: new 29	java/lang/StringBuilder
    //   217: dup
    //   218: ldc 122
    //   220: invokespecial 34	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   223: aload_2
    //   224: iconst_0
    //   225: invokeinterface 91 2 0
    //   230: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   233: invokevirtual 51	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   236: invokeinterface 120 2 0
    //   241: pop
    //   242: aload_1
    //   243: new 29	java/lang/StringBuilder
    //   246: dup
    //   247: ldc 124
    //   249: invokespecial 34	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   252: aload_2
    //   253: iconst_0
    //   254: invokeinterface 91 2 0
    //   259: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   262: ldc 40
    //   264: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   267: aload_2
    //   268: iconst_1
    //   269: invokeinterface 91 2 0
    //   274: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   277: ldc 40
    //   279: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   282: aload_2
    //   283: iconst_2
    //   284: invokeinterface 91 2 0
    //   289: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   292: ldc 40
    //   294: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   297: aload_2
    //   298: iconst_3
    //   299: invokeinterface 91 2 0
    //   304: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   307: ldc 40
    //   309: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   312: aload_2
    //   313: iconst_4
    //   314: invokeinterface 91 2 0
    //   319: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   322: ldc 126
    //   324: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   327: invokevirtual 51	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   330: invokeinterface 120 2 0
    //   335: pop
    //   336: goto -295 -> 41
    //   339: astore_3
    //   340: aload_2
    //   341: ifnull +9 -> 350
    //   344: aload_2
    //   345: invokeinterface 94 1 0
    //   350: aload_3
    //   351: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	352	0	this	ItemTableAccess
    //   7	236	1	localArrayList	java.util.ArrayList
    //   9	336	2	localCursor	Cursor
    //   339	12	3	localObject	Object
    //   194	3	4	localException	Exception
    //   47	3	6	bool	boolean
    // Exception table:
    //   from	to	target	type
    //   10	21	194	java/lang/Exception
    //   21	41	194	java/lang/Exception
    //   41	49	194	java/lang/Exception
    //   66	191	194	java/lang/Exception
    //   213	336	194	java/lang/Exception
    //   10	21	339	finally
    //   21	41	339	finally
    //   41	49	339	finally
    //   66	191	339	finally
    //   196	201	339	finally
    //   213	336	339	finally
  }
  
  public void clearDelTable()
  {
    try
    {
      this.db.execSQL("DELETE FROM DeleteTable");
      return;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
  
  public void close()
  {
    this.db.close();
  }
  
  public void delWebItem(int paramInt1, int paramInt2)
  {
    String str = "DELETE FROM ItemTable WHERE ItemID = " + paramInt2 + " OR ItemWebID = " + paramInt1;
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
  
  public void deleteAllData()
  {
    try
    {
      this.db.execSQL("DELETE FROM ItemTable");
      this.db.execSQL("DELETE FROM DeleteTable");
      this.db.execSQL("DELETE FROM CategoryTable");
      this.db.execSQL("INSERT INTO CategoryTable(CategoryID, CategoryName, CategoryRank, IsDefault) SELECT '1', '菜米油盐酱醋', '1', '1' UNION ALL SELECT '2', '外就餐', '2', '1' UNION ALL SELECT '3', '烟酒水', '3', '1' UNION ALL SELECT '4', '零食', '4', '1' UNION ALL SELECT '5', '水果', '5', '1' UNION ALL SELECT '6', '日用品', '6', '1' UNION ALL SELECT '7', '电子产品', '7', '1' UNION ALL SELECT '8', '衣裤鞋', '8', '1' UNION ALL SELECT '9', '交费', '9', '1' UNION ALL SELECT '10', '娱乐', '10', '1'");
      return;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
  
  public boolean deleteItem(int paramInt)
  {
    this.db.beginTransaction();
    Cursor localCursor = null;
    try
    {
      localCursor = this.db.rawQuery("SELECT ItemWebID FROM ItemTable WHERE ItemID = " + paramInt, null);
      boolean bool2 = localCursor.moveToFirst();
      int i = 0;
      if (bool2) {
        i = localCursor.getInt(0);
      }
      this.db.execSQL("DELETE FROM ItemTable WHERE ItemID = " + paramInt);
      this.db.execSQL("INSERT INTO DeleteTable (ItemID, ItemWebID) VALUES (" + paramInt + ", " + i + ")");
      this.db.setTransactionSuccessful();
      this.db.endTransaction();
      if (localCursor != null) {
        localCursor.close();
      }
      bool1 = true;
      return bool1;
    }
    catch (Exception localException)
    {
      do
      {
        localException.printStackTrace();
        this.db.endTransaction();
        boolean bool1 = false;
      } while (localCursor == null);
      localCursor.close();
      return false;
    }
    finally
    {
      this.db.endTransaction();
      if (localCursor != null) {
        localCursor.close();
      }
    }
  }
  
  public boolean deleteLastItem()
  {
    try
    {
      this.db.execSQL("DELETE FROM ItemTable WHERE ItemID = (SELECT MAX(ItemID) FROM ItemTable)");
      return true;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return false;
  }
  
  /* Error */
  public java.util.List<Map<String, String>> findAllDayBuyDate(String paramString)
  {
    // Byte code:
    //   0: new 102	java/util/ArrayList
    //   3: dup
    //   4: invokespecial 103	java/util/ArrayList:<init>	()V
    //   7: astore_2
    //   8: new 29	java/lang/StringBuilder
    //   11: dup
    //   12: ldc 173
    //   14: invokespecial 34	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   17: aload_1
    //   18: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   21: ldc 175
    //   23: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   26: invokevirtual 51	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   29: astore_3
    //   30: aconst_null
    //   31: astore 4
    //   33: aload_0
    //   34: getfield 23	com/aalife/android/ItemTableAccess:db	Landroid/database/sqlite/SQLiteDatabase;
    //   37: aload_3
    //   38: aconst_null
    //   39: invokevirtual 67	android/database/sqlite/SQLiteDatabase:rawQuery	(Ljava/lang/String;[Ljava/lang/String;)Landroid/database/Cursor;
    //   42: astore 4
    //   44: aload 4
    //   46: invokeinterface 108 1 0
    //   51: istore 7
    //   53: iload 7
    //   55: ifne +17 -> 72
    //   58: aload 4
    //   60: ifnull +10 -> 70
    //   63: aload 4
    //   65: invokeinterface 94 1 0
    //   70: aload_2
    //   71: areturn
    //   72: new 177	java/util/HashMap
    //   75: dup
    //   76: invokespecial 178	java/util/HashMap:<init>	()V
    //   79: astore 8
    //   81: aload 8
    //   83: ldc 180
    //   85: aload 4
    //   87: iconst_0
    //   88: invokeinterface 184 2 0
    //   93: ldc 186
    //   95: invokestatic 192	com/aalife/android/UtilityHelper:formatDouble	(DLjava/lang/String;)Ljava/lang/String;
    //   98: invokeinterface 198 3 0
    //   103: pop
    //   104: aload 8
    //   106: ldc 200
    //   108: aload 4
    //   110: iconst_1
    //   111: invokeinterface 91 2 0
    //   116: ldc 202
    //   118: invokestatic 206	com/aalife/android/UtilityHelper:formatDate	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   121: invokeinterface 198 3 0
    //   126: pop
    //   127: aload_2
    //   128: aload 8
    //   130: invokeinterface 120 2 0
    //   135: pop
    //   136: goto -92 -> 44
    //   139: astore 6
    //   141: aload 6
    //   143: invokevirtual 59	java/lang/Exception:printStackTrace	()V
    //   146: aload 4
    //   148: ifnull -78 -> 70
    //   151: aload 4
    //   153: invokeinterface 94 1 0
    //   158: aload_2
    //   159: areturn
    //   160: astore 5
    //   162: aload 4
    //   164: ifnull +10 -> 174
    //   167: aload 4
    //   169: invokeinterface 94 1 0
    //   174: aload 5
    //   176: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	177	0	this	ItemTableAccess
    //   0	177	1	paramString	String
    //   7	152	2	localArrayList	java.util.ArrayList
    //   29	9	3	str	String
    //   31	137	4	localCursor	Cursor
    //   160	15	5	localObject	Object
    //   139	3	6	localException	Exception
    //   51	3	7	bool	boolean
    //   79	50	8	localHashMap	HashMap
    // Exception table:
    //   from	to	target	type
    //   33	44	139	java/lang/Exception
    //   44	53	139	java/lang/Exception
    //   72	136	139	java/lang/Exception
    //   33	44	160	finally
    //   44	53	160	finally
    //   72	136	160	finally
    //   141	146	160	finally
  }
  
  /* Error */
  public java.util.List<Map<String, String>> findAllDayFirstBuyDate(String paramString)
  {
    // Byte code:
    //   0: new 102	java/util/ArrayList
    //   3: dup
    //   4: invokespecial 103	java/util/ArrayList:<init>	()V
    //   7: astore_2
    //   8: new 29	java/lang/StringBuilder
    //   11: dup
    //   12: ldc 209
    //   14: invokespecial 34	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   17: aload_1
    //   18: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   21: ldc 175
    //   23: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   26: invokevirtual 51	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   29: astore_3
    //   30: aconst_null
    //   31: astore 4
    //   33: aload_0
    //   34: getfield 23	com/aalife/android/ItemTableAccess:db	Landroid/database/sqlite/SQLiteDatabase;
    //   37: aload_3
    //   38: aconst_null
    //   39: invokevirtual 67	android/database/sqlite/SQLiteDatabase:rawQuery	(Ljava/lang/String;[Ljava/lang/String;)Landroid/database/Cursor;
    //   42: astore 4
    //   44: aload 4
    //   46: invokeinterface 108 1 0
    //   51: istore 7
    //   53: iload 7
    //   55: ifne +17 -> 72
    //   58: aload 4
    //   60: ifnull +10 -> 70
    //   63: aload 4
    //   65: invokeinterface 94 1 0
    //   70: aload_2
    //   71: areturn
    //   72: new 177	java/util/HashMap
    //   75: dup
    //   76: invokespecial 178	java/util/HashMap:<init>	()V
    //   79: astore 8
    //   81: aload 8
    //   83: ldc 180
    //   85: aload 4
    //   87: iconst_0
    //   88: invokeinterface 184 2 0
    //   93: ldc 186
    //   95: invokestatic 192	com/aalife/android/UtilityHelper:formatDouble	(DLjava/lang/String;)Ljava/lang/String;
    //   98: invokeinterface 198 3 0
    //   103: pop
    //   104: aload 8
    //   106: ldc 200
    //   108: aload 4
    //   110: iconst_1
    //   111: invokeinterface 91 2 0
    //   116: ldc 202
    //   118: invokestatic 206	com/aalife/android/UtilityHelper:formatDate	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   121: invokeinterface 198 3 0
    //   126: pop
    //   127: aload_2
    //   128: aload 8
    //   130: invokeinterface 120 2 0
    //   135: pop
    //   136: goto -92 -> 44
    //   139: astore 6
    //   141: aload 6
    //   143: invokevirtual 59	java/lang/Exception:printStackTrace	()V
    //   146: aload 4
    //   148: ifnull -78 -> 70
    //   151: aload 4
    //   153: invokeinterface 94 1 0
    //   158: aload_2
    //   159: areturn
    //   160: astore 5
    //   162: aload 4
    //   164: ifnull +10 -> 174
    //   167: aload 4
    //   169: invokeinterface 94 1 0
    //   174: aload 5
    //   176: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	177	0	this	ItemTableAccess
    //   0	177	1	paramString	String
    //   7	152	2	localArrayList	java.util.ArrayList
    //   29	9	3	str	String
    //   31	137	4	localCursor	Cursor
    //   160	15	5	localObject	Object
    //   139	3	6	localException	Exception
    //   51	3	7	bool	boolean
    //   79	50	8	localHashMap	HashMap
    // Exception table:
    //   from	to	target	type
    //   33	44	139	java/lang/Exception
    //   44	53	139	java/lang/Exception
    //   72	136	139	java/lang/Exception
    //   33	44	160	finally
    //   44	53	160	finally
    //   72	136	160	finally
    //   141	146	160	finally
  }
  
  /* Error */
  public java.util.List<Map<String, String>> findAllItemByCatId(String paramString)
  {
    // Byte code:
    //   0: new 102	java/util/ArrayList
    //   3: dup
    //   4: invokespecial 103	java/util/ArrayList:<init>	()V
    //   7: astore_2
    //   8: new 29	java/lang/StringBuilder
    //   11: dup
    //   12: ldc 212
    //   14: invokespecial 34	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   17: aload_1
    //   18: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   21: ldc 214
    //   23: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   26: invokevirtual 51	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   29: astore_3
    //   30: aconst_null
    //   31: astore 4
    //   33: aload_0
    //   34: getfield 23	com/aalife/android/ItemTableAccess:db	Landroid/database/sqlite/SQLiteDatabase;
    //   37: aload_3
    //   38: aconst_null
    //   39: invokevirtual 67	android/database/sqlite/SQLiteDatabase:rawQuery	(Ljava/lang/String;[Ljava/lang/String;)Landroid/database/Cursor;
    //   42: astore 4
    //   44: aload 4
    //   46: invokeinterface 108 1 0
    //   51: istore 7
    //   53: iload 7
    //   55: ifne +17 -> 72
    //   58: aload 4
    //   60: ifnull +10 -> 70
    //   63: aload 4
    //   65: invokeinterface 94 1 0
    //   70: aload_2
    //   71: areturn
    //   72: new 177	java/util/HashMap
    //   75: dup
    //   76: invokespecial 178	java/util/HashMap:<init>	()V
    //   79: astore 8
    //   81: aload 8
    //   83: ldc 216
    //   85: iconst_1
    //   86: aload 4
    //   88: invokeinterface 220 1 0
    //   93: iadd
    //   94: invokestatic 225	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   97: invokeinterface 198 3 0
    //   102: pop
    //   103: aload 8
    //   105: ldc 227
    //   107: aload 4
    //   109: iconst_0
    //   110: invokeinterface 91 2 0
    //   115: invokeinterface 198 3 0
    //   120: pop
    //   121: aload_2
    //   122: aload 8
    //   124: invokeinterface 120 2 0
    //   129: pop
    //   130: goto -86 -> 44
    //   133: astore 6
    //   135: aload 6
    //   137: invokevirtual 59	java/lang/Exception:printStackTrace	()V
    //   140: aload 4
    //   142: ifnull -72 -> 70
    //   145: aload 4
    //   147: invokeinterface 94 1 0
    //   152: aload_2
    //   153: areturn
    //   154: astore 5
    //   156: aload 4
    //   158: ifnull +10 -> 168
    //   161: aload 4
    //   163: invokeinterface 94 1 0
    //   168: aload 5
    //   170: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	171	0	this	ItemTableAccess
    //   0	171	1	paramString	String
    //   7	146	2	localArrayList	java.util.ArrayList
    //   29	9	3	str	String
    //   31	131	4	localCursor	Cursor
    //   154	15	5	localObject	Object
    //   133	3	6	localException	Exception
    //   51	3	7	bool	boolean
    //   79	44	8	localHashMap	HashMap
    // Exception table:
    //   from	to	target	type
    //   33	44	133	java/lang/Exception
    //   44	53	133	java/lang/Exception
    //   72	130	133	java/lang/Exception
    //   33	44	154	finally
    //   44	53	154	finally
    //   72	130	154	finally
    //   135	140	154	finally
  }
  
  /* Error */
  public java.util.List<java.lang.CharSequence> findAllItemName()
  {
    // Byte code:
    //   0: new 102	java/util/ArrayList
    //   3: dup
    //   4: invokespecial 103	java/util/ArrayList:<init>	()V
    //   7: astore_1
    //   8: aconst_null
    //   9: astore_2
    //   10: aload_0
    //   11: getfield 23	com/aalife/android/ItemTableAccess:db	Landroid/database/sqlite/SQLiteDatabase;
    //   14: ldc 230
    //   16: aconst_null
    //   17: invokevirtual 67	android/database/sqlite/SQLiteDatabase:rawQuery	(Ljava/lang/String;[Ljava/lang/String;)Landroid/database/Cursor;
    //   20: astore_2
    //   21: aload_2
    //   22: invokeinterface 108 1 0
    //   27: istore 5
    //   29: iload 5
    //   31: ifne +15 -> 46
    //   34: aload_2
    //   35: ifnull +9 -> 44
    //   38: aload_2
    //   39: invokeinterface 94 1 0
    //   44: aload_1
    //   45: areturn
    //   46: aload_1
    //   47: aload_2
    //   48: iconst_0
    //   49: invokeinterface 91 2 0
    //   54: invokeinterface 120 2 0
    //   59: pop
    //   60: goto -39 -> 21
    //   63: astore 4
    //   65: aload 4
    //   67: invokevirtual 59	java/lang/Exception:printStackTrace	()V
    //   70: aload_2
    //   71: ifnull -27 -> 44
    //   74: aload_2
    //   75: invokeinterface 94 1 0
    //   80: aload_1
    //   81: areturn
    //   82: astore_3
    //   83: aload_2
    //   84: ifnull +9 -> 93
    //   87: aload_2
    //   88: invokeinterface 94 1 0
    //   93: aload_3
    //   94: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	95	0	this	ItemTableAccess
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
  public java.util.List<Map<String, String>> findAllMonth()
  {
    // Byte code:
    //   0: new 102	java/util/ArrayList
    //   3: dup
    //   4: invokespecial 103	java/util/ArrayList:<init>	()V
    //   7: astore_1
    //   8: aconst_null
    //   9: astore_2
    //   10: aload_0
    //   11: getfield 23	com/aalife/android/ItemTableAccess:db	Landroid/database/sqlite/SQLiteDatabase;
    //   14: ldc 233
    //   16: aconst_null
    //   17: invokevirtual 67	android/database/sqlite/SQLiteDatabase:rawQuery	(Ljava/lang/String;[Ljava/lang/String;)Landroid/database/Cursor;
    //   20: astore_2
    //   21: aload_2
    //   22: invokeinterface 108 1 0
    //   27: istore 5
    //   29: iload 5
    //   31: ifne +15 -> 46
    //   34: aload_2
    //   35: ifnull +9 -> 44
    //   38: aload_2
    //   39: invokeinterface 94 1 0
    //   44: aload_1
    //   45: areturn
    //   46: new 177	java/util/HashMap
    //   49: dup
    //   50: invokespecial 178	java/util/HashMap:<init>	()V
    //   53: astore 6
    //   55: aload 6
    //   57: ldc 216
    //   59: iconst_1
    //   60: aload_2
    //   61: invokeinterface 220 1 0
    //   66: iadd
    //   67: invokestatic 225	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   70: invokeinterface 198 3 0
    //   75: pop
    //   76: aload 6
    //   78: ldc 235
    //   80: new 29	java/lang/StringBuilder
    //   83: dup
    //   84: ldc 237
    //   86: invokespecial 34	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   89: aload_2
    //   90: iconst_0
    //   91: invokeinterface 184 2 0
    //   96: ldc 186
    //   98: invokestatic 192	com/aalife/android/UtilityHelper:formatDouble	(DLjava/lang/String;)Ljava/lang/String;
    //   101: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   104: invokevirtual 51	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   107: invokeinterface 198 3 0
    //   112: pop
    //   113: aload 6
    //   115: ldc 180
    //   117: aload_2
    //   118: iconst_0
    //   119: invokeinterface 184 2 0
    //   124: ldc 186
    //   126: invokestatic 192	com/aalife/android/UtilityHelper:formatDouble	(DLjava/lang/String;)Ljava/lang/String;
    //   129: invokeinterface 198 3 0
    //   134: pop
    //   135: aload 6
    //   137: ldc 239
    //   139: aload_2
    //   140: iconst_1
    //   141: invokeinterface 91 2 0
    //   146: ldc 241
    //   148: invokestatic 206	com/aalife/android/UtilityHelper:formatDate	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   151: invokeinterface 198 3 0
    //   156: pop
    //   157: aload 6
    //   159: ldc 243
    //   161: aload_2
    //   162: iconst_1
    //   163: invokeinterface 91 2 0
    //   168: ldc 202
    //   170: invokestatic 206	com/aalife/android/UtilityHelper:formatDate	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   173: invokeinterface 198 3 0
    //   178: pop
    //   179: aload_1
    //   180: aload 6
    //   182: invokeinterface 120 2 0
    //   187: pop
    //   188: goto -167 -> 21
    //   191: astore 4
    //   193: aload 4
    //   195: invokevirtual 59	java/lang/Exception:printStackTrace	()V
    //   198: aload_2
    //   199: ifnull -155 -> 44
    //   202: aload_2
    //   203: invokeinterface 94 1 0
    //   208: aload_1
    //   209: areturn
    //   210: astore_3
    //   211: aload_2
    //   212: ifnull +9 -> 221
    //   215: aload_2
    //   216: invokeinterface 94 1 0
    //   221: aload_3
    //   222: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	223	0	this	ItemTableAccess
    //   7	202	1	localArrayList	java.util.ArrayList
    //   9	207	2	localCursor	Cursor
    //   210	12	3	localObject	Object
    //   191	3	4	localException	Exception
    //   27	3	5	bool	boolean
    //   53	128	6	localHashMap	HashMap
    // Exception table:
    //   from	to	target	type
    //   10	21	191	java/lang/Exception
    //   21	29	191	java/lang/Exception
    //   46	188	191	java/lang/Exception
    //   10	21	210	finally
    //   21	29	210	finally
    //   46	188	210	finally
    //   193	198	210	finally
  }
  
  public Map<String, String> findAllMonth(String paramString)
  {
    HashMap localHashMap = new HashMap();
    String str = "SELECT SUM(ItemPrice), ItemBuyDate FROM ItemTable GROUP BY STRFTIME('%Y-%m', ItemBuyDate) HAVING STRFTIME('%Y-%m', ItemBuyDate) = STRFTIME('%Y-%m', '" + paramString + "')";
    Cursor localCursor = null;
    try
    {
      localCursor = this.db.rawQuery(str, null);
      if (localCursor.moveToNext())
      {
        localHashMap.put("id", String.valueOf(1 + localCursor.getPosition()));
        localHashMap.put("price", "￥ " + UtilityHelper.formatDouble(localCursor.getDouble(0), "0.0##"));
        localHashMap.put("pricevalue", UtilityHelper.formatDouble(localCursor.getDouble(0), "0.0##"));
        localHashMap.put("date", UtilityHelper.formatDate(localCursor.getString(1), "y-m"));
        localHashMap.put("datevalue", UtilityHelper.formatDate(localCursor.getString(1), "y-m-d"));
      }
      return localHashMap;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
      return localHashMap;
    }
    finally
    {
      if (localCursor != null) {
        localCursor.close();
      }
    }
  }
  
  /* Error */
  public java.util.List<Map<String, String>> findAllPriceByName(String paramString)
  {
    // Byte code:
    //   0: new 102	java/util/ArrayList
    //   3: dup
    //   4: invokespecial 103	java/util/ArrayList:<init>	()V
    //   7: astore_2
    //   8: new 29	java/lang/StringBuilder
    //   11: dup
    //   12: ldc 249
    //   14: invokespecial 34	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   17: aload_1
    //   18: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   21: ldc 251
    //   23: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   26: invokevirtual 51	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   29: astore_3
    //   30: aconst_null
    //   31: astore 4
    //   33: aload_0
    //   34: getfield 23	com/aalife/android/ItemTableAccess:db	Landroid/database/sqlite/SQLiteDatabase;
    //   37: aload_3
    //   38: aconst_null
    //   39: invokevirtual 67	android/database/sqlite/SQLiteDatabase:rawQuery	(Ljava/lang/String;[Ljava/lang/String;)Landroid/database/Cursor;
    //   42: astore 4
    //   44: aload 4
    //   46: invokeinterface 108 1 0
    //   51: istore 7
    //   53: iload 7
    //   55: ifne +17 -> 72
    //   58: aload 4
    //   60: ifnull +10 -> 70
    //   63: aload 4
    //   65: invokeinterface 94 1 0
    //   70: aload_2
    //   71: areturn
    //   72: new 177	java/util/HashMap
    //   75: dup
    //   76: invokespecial 178	java/util/HashMap:<init>	()V
    //   79: astore 8
    //   81: aload 8
    //   83: ldc 216
    //   85: iconst_1
    //   86: aload 4
    //   88: invokeinterface 220 1 0
    //   93: iadd
    //   94: invokestatic 225	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   97: invokeinterface 198 3 0
    //   102: pop
    //   103: aload 8
    //   105: ldc 227
    //   107: aload 4
    //   109: iconst_0
    //   110: invokeinterface 91 2 0
    //   115: invokeinterface 198 3 0
    //   120: pop
    //   121: aload_2
    //   122: aload 8
    //   124: invokeinterface 120 2 0
    //   129: pop
    //   130: goto -86 -> 44
    //   133: astore 6
    //   135: aload 6
    //   137: invokevirtual 59	java/lang/Exception:printStackTrace	()V
    //   140: aload 4
    //   142: ifnull -72 -> 70
    //   145: aload 4
    //   147: invokeinterface 94 1 0
    //   152: aload_2
    //   153: areturn
    //   154: astore 5
    //   156: aload 4
    //   158: ifnull +10 -> 168
    //   161: aload 4
    //   163: invokeinterface 94 1 0
    //   168: aload 5
    //   170: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	171	0	this	ItemTableAccess
    //   0	171	1	paramString	String
    //   7	146	2	localArrayList	java.util.ArrayList
    //   29	9	3	str	String
    //   31	131	4	localCursor	Cursor
    //   154	15	5	localObject	Object
    //   133	3	6	localException	Exception
    //   51	3	7	bool	boolean
    //   79	44	8	localHashMap	HashMap
    // Exception table:
    //   from	to	target	type
    //   33	44	133	java/lang/Exception
    //   44	53	133	java/lang/Exception
    //   72	130	133	java/lang/Exception
    //   33	44	154	finally
    //   44	53	154	finally
    //   72	130	154	finally
    //   135	140	154	finally
  }
  
  /* Error */
  public java.util.List<Map<String, String>> findAnalyzeCompareDetailByDate(String paramString, int paramInt)
  {
    // Byte code:
    //   0: new 102	java/util/ArrayList
    //   3: dup
    //   4: invokespecial 103	java/util/ArrayList:<init>	()V
    //   7: astore_3
    //   8: new 29	java/lang/StringBuilder
    //   11: dup
    //   12: ldc 255
    //   14: invokespecial 34	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   17: aload_1
    //   18: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   21: ldc_w 257
    //   24: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   27: iload_2
    //   28: invokevirtual 43	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   31: ldc 85
    //   33: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   36: ldc_w 259
    //   39: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   42: ldc_w 261
    //   45: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   48: ldc 14
    //   50: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   53: ldc_w 263
    //   56: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   59: aload_1
    //   60: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   63: ldc_w 265
    //   66: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   69: iload_2
    //   70: invokevirtual 43	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   73: ldc 85
    //   75: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   78: ldc_w 267
    //   81: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   84: ldc_w 269
    //   87: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   90: ldc_w 271
    //   93: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   96: ldc_w 261
    //   99: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   102: ldc 14
    //   104: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   107: ldc_w 263
    //   110: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   113: aload_1
    //   114: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   117: ldc_w 265
    //   120: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   123: iload_2
    //   124: invokevirtual 43	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   127: ldc 85
    //   129: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   132: ldc_w 273
    //   135: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   138: ldc_w 261
    //   141: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   144: ldc 14
    //   146: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   149: ldc_w 275
    //   152: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   155: aload_1
    //   156: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   159: ldc_w 257
    //   162: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   165: iload_2
    //   166: invokevirtual 43	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   169: ldc 85
    //   171: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   174: ldc_w 277
    //   177: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   180: ldc_w 279
    //   183: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   186: invokevirtual 51	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   189: astore 4
    //   191: aconst_null
    //   192: astore 5
    //   194: aload_0
    //   195: getfield 23	com/aalife/android/ItemTableAccess:db	Landroid/database/sqlite/SQLiteDatabase;
    //   198: aload 4
    //   200: aconst_null
    //   201: invokevirtual 67	android/database/sqlite/SQLiteDatabase:rawQuery	(Ljava/lang/String;[Ljava/lang/String;)Landroid/database/Cursor;
    //   204: astore 5
    //   206: aload 5
    //   208: invokeinterface 108 1 0
    //   213: istore 8
    //   215: iload 8
    //   217: ifne +17 -> 234
    //   220: aload 5
    //   222: ifnull +10 -> 232
    //   225: aload 5
    //   227: invokeinterface 94 1 0
    //   232: aload_3
    //   233: areturn
    //   234: new 177	java/util/HashMap
    //   237: dup
    //   238: invokespecial 178	java/util/HashMap:<init>	()V
    //   241: astore 9
    //   243: aload 9
    //   245: ldc_w 281
    //   248: aload 5
    //   250: iconst_0
    //   251: invokeinterface 91 2 0
    //   256: invokeinterface 198 3 0
    //   261: pop
    //   262: aload 9
    //   264: ldc_w 283
    //   267: aload 5
    //   269: iconst_1
    //   270: invokeinterface 91 2 0
    //   275: invokeinterface 198 3 0
    //   280: pop
    //   281: aload 9
    //   283: ldc_w 285
    //   286: new 29	java/lang/StringBuilder
    //   289: dup
    //   290: aload 5
    //   292: iconst_1
    //   293: invokeinterface 91 2 0
    //   298: invokestatic 288	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
    //   301: invokespecial 34	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   304: ldc_w 290
    //   307: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   310: invokevirtual 51	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   313: invokeinterface 198 3 0
    //   318: pop
    //   319: aload 9
    //   321: ldc_w 292
    //   324: new 29	java/lang/StringBuilder
    //   327: dup
    //   328: ldc 237
    //   330: invokespecial 34	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   333: aload 5
    //   335: iconst_2
    //   336: invokeinterface 184 2 0
    //   341: ldc 186
    //   343: invokestatic 192	com/aalife/android/UtilityHelper:formatDouble	(DLjava/lang/String;)Ljava/lang/String;
    //   346: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   349: invokevirtual 51	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   352: invokeinterface 198 3 0
    //   357: pop
    //   358: aload 9
    //   360: ldc_w 294
    //   363: new 29	java/lang/StringBuilder
    //   366: dup
    //   367: aload 5
    //   369: iconst_3
    //   370: invokeinterface 91 2 0
    //   375: invokestatic 288	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
    //   378: invokespecial 34	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   381: ldc_w 290
    //   384: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   387: invokevirtual 51	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   390: invokeinterface 198 3 0
    //   395: pop
    //   396: aload 9
    //   398: ldc_w 296
    //   401: new 29	java/lang/StringBuilder
    //   404: dup
    //   405: ldc 237
    //   407: invokespecial 34	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   410: aload 5
    //   412: iconst_4
    //   413: invokeinterface 184 2 0
    //   418: ldc 186
    //   420: invokestatic 192	com/aalife/android/UtilityHelper:formatDouble	(DLjava/lang/String;)Ljava/lang/String;
    //   423: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   426: invokevirtual 51	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   429: invokeinterface 198 3 0
    //   434: pop
    //   435: aload_3
    //   436: aload 9
    //   438: invokeinterface 120 2 0
    //   443: pop
    //   444: goto -238 -> 206
    //   447: astore 7
    //   449: aload 7
    //   451: invokevirtual 59	java/lang/Exception:printStackTrace	()V
    //   454: aload 5
    //   456: ifnull -224 -> 232
    //   459: aload 5
    //   461: invokeinterface 94 1 0
    //   466: aload_3
    //   467: areturn
    //   468: astore 6
    //   470: aload 5
    //   472: ifnull +10 -> 482
    //   475: aload 5
    //   477: invokeinterface 94 1 0
    //   482: aload 6
    //   484: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	485	0	this	ItemTableAccess
    //   0	485	1	paramString	String
    //   0	485	2	paramInt	int
    //   7	460	3	localArrayList	java.util.ArrayList
    //   189	10	4	str	String
    //   192	284	5	localCursor	Cursor
    //   468	15	6	localObject	Object
    //   447	3	7	localException	Exception
    //   213	3	8	bool	boolean
    //   241	196	9	localHashMap	HashMap
    // Exception table:
    //   from	to	target	type
    //   194	206	447	java/lang/Exception
    //   206	215	447	java/lang/Exception
    //   234	444	447	java/lang/Exception
    //   194	206	468	finally
    //   206	215	468	finally
    //   234	444	468	finally
    //   449	454	468	finally
  }
  
  /* Error */
  public java.util.List<Map<String, String>> findAnalyzeRecommend()
  {
    // Byte code:
    //   0: new 102	java/util/ArrayList
    //   3: dup
    //   4: invokespecial 103	java/util/ArrayList:<init>	()V
    //   7: astore_1
    //   8: aconst_null
    //   9: astore_2
    //   10: aload_0
    //   11: getfield 23	com/aalife/android/ItemTableAccess:db	Landroid/database/sqlite/SQLiteDatabase;
    //   14: ldc_w 299
    //   17: aconst_null
    //   18: invokevirtual 67	android/database/sqlite/SQLiteDatabase:rawQuery	(Ljava/lang/String;[Ljava/lang/String;)Landroid/database/Cursor;
    //   21: astore_2
    //   22: aload_2
    //   23: invokeinterface 108 1 0
    //   28: istore 5
    //   30: iload 5
    //   32: ifne +15 -> 47
    //   35: aload_2
    //   36: ifnull +9 -> 45
    //   39: aload_2
    //   40: invokeinterface 94 1 0
    //   45: aload_1
    //   46: areturn
    //   47: new 177	java/util/HashMap
    //   50: dup
    //   51: invokespecial 178	java/util/HashMap:<init>	()V
    //   54: astore 6
    //   56: aload 6
    //   58: ldc_w 281
    //   61: aload_2
    //   62: iconst_0
    //   63: invokeinterface 91 2 0
    //   68: invokeinterface 198 3 0
    //   73: pop
    //   74: aload 6
    //   76: ldc 200
    //   78: aload_2
    //   79: iconst_1
    //   80: invokeinterface 91 2 0
    //   85: ldc_w 301
    //   88: invokestatic 206	com/aalife/android/UtilityHelper:formatDate	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   91: invokeinterface 198 3 0
    //   96: pop
    //   97: aload 6
    //   99: ldc 243
    //   101: aload_2
    //   102: iconst_1
    //   103: invokeinterface 91 2 0
    //   108: ldc 202
    //   110: invokestatic 206	com/aalife/android/UtilityHelper:formatDate	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   113: invokeinterface 198 3 0
    //   118: pop
    //   119: aload 6
    //   121: ldc_w 303
    //   124: new 29	java/lang/StringBuilder
    //   127: dup
    //   128: ldc 237
    //   130: invokespecial 34	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   133: aload_2
    //   134: iconst_2
    //   135: invokeinterface 184 2 0
    //   140: ldc 186
    //   142: invokestatic 192	com/aalife/android/UtilityHelper:formatDouble	(DLjava/lang/String;)Ljava/lang/String;
    //   145: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   148: invokevirtual 51	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   151: invokeinterface 198 3 0
    //   156: pop
    //   157: aload_1
    //   158: aload 6
    //   160: invokeinterface 120 2 0
    //   165: pop
    //   166: goto -144 -> 22
    //   169: astore 4
    //   171: aload 4
    //   173: invokevirtual 59	java/lang/Exception:printStackTrace	()V
    //   176: aload_2
    //   177: ifnull -132 -> 45
    //   180: aload_2
    //   181: invokeinterface 94 1 0
    //   186: aload_1
    //   187: areturn
    //   188: astore_3
    //   189: aload_2
    //   190: ifnull +9 -> 199
    //   193: aload_2
    //   194: invokeinterface 94 1 0
    //   199: aload_3
    //   200: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	201	0	this	ItemTableAccess
    //   7	180	1	localArrayList	java.util.ArrayList
    //   9	185	2	localCursor	Cursor
    //   188	12	3	localObject	Object
    //   169	3	4	localException	Exception
    //   28	3	5	bool	boolean
    //   54	105	6	localHashMap	HashMap
    // Exception table:
    //   from	to	target	type
    //   10	22	169	java/lang/Exception
    //   22	30	169	java/lang/Exception
    //   47	166	169	java/lang/Exception
    //   10	22	188	finally
    //   22	30	188	finally
    //   47	166	188	finally
    //   171	176	188	finally
  }
  
  /* Error */
  public java.util.List<Map<String, String>> findCompareCatByDate(String paramString)
  {
    // Byte code:
    //   0: new 102	java/util/ArrayList
    //   3: dup
    //   4: invokespecial 103	java/util/ArrayList:<init>	()V
    //   7: astore_2
    //   8: new 29	java/lang/StringBuilder
    //   11: dup
    //   12: ldc_w 306
    //   15: invokespecial 34	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   18: aload_1
    //   19: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   22: ldc_w 308
    //   25: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   28: ldc_w 310
    //   31: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   34: ldc_w 312
    //   37: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   40: ldc_w 314
    //   43: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   46: ldc 8
    //   48: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   51: ldc_w 316
    //   54: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   57: ldc_w 318
    //   60: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   63: ldc 14
    //   65: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   68: ldc_w 320
    //   71: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   74: aload_1
    //   75: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   78: ldc_w 322
    //   81: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   84: ldc_w 310
    //   87: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   90: ldc_w 324
    //   93: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   96: ldc_w 326
    //   99: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   102: invokevirtual 51	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   105: astore_3
    //   106: aconst_null
    //   107: astore 4
    //   109: aload_0
    //   110: getfield 23	com/aalife/android/ItemTableAccess:db	Landroid/database/sqlite/SQLiteDatabase;
    //   113: aload_3
    //   114: aconst_null
    //   115: invokevirtual 67	android/database/sqlite/SQLiteDatabase:rawQuery	(Ljava/lang/String;[Ljava/lang/String;)Landroid/database/Cursor;
    //   118: astore 4
    //   120: aload 4
    //   122: invokeinterface 108 1 0
    //   127: istore 7
    //   129: iload 7
    //   131: ifne +17 -> 148
    //   134: aload 4
    //   136: ifnull +10 -> 146
    //   139: aload 4
    //   141: invokeinterface 94 1 0
    //   146: aload_2
    //   147: areturn
    //   148: new 177	java/util/HashMap
    //   151: dup
    //   152: invokespecial 178	java/util/HashMap:<init>	()V
    //   155: astore 8
    //   157: aload 8
    //   159: ldc_w 328
    //   162: aload 4
    //   164: iconst_0
    //   165: invokeinterface 91 2 0
    //   170: invokeinterface 198 3 0
    //   175: pop
    //   176: aload 8
    //   178: ldc_w 330
    //   181: aload 4
    //   183: iconst_1
    //   184: invokeinterface 91 2 0
    //   189: invokeinterface 198 3 0
    //   194: pop
    //   195: aload 4
    //   197: iconst_2
    //   198: invokeinterface 184 2 0
    //   203: dconst_0
    //   204: dcmpl
    //   205: ifle +135 -> 340
    //   208: new 29	java/lang/StringBuilder
    //   211: dup
    //   212: ldc 237
    //   214: invokespecial 34	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   217: aload 4
    //   219: iconst_2
    //   220: invokeinterface 184 2 0
    //   225: ldc 186
    //   227: invokestatic 192	com/aalife/android/UtilityHelper:formatDouble	(DLjava/lang/String;)Ljava/lang/String;
    //   230: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   233: invokevirtual 51	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   236: astore 11
    //   238: aload 8
    //   240: ldc_w 292
    //   243: aload 11
    //   245: invokeinterface 198 3 0
    //   250: pop
    //   251: aload 4
    //   253: iconst_3
    //   254: invokeinterface 184 2 0
    //   259: dconst_0
    //   260: dcmpl
    //   261: ifle +87 -> 348
    //   264: new 29	java/lang/StringBuilder
    //   267: dup
    //   268: ldc 237
    //   270: invokespecial 34	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   273: aload 4
    //   275: iconst_3
    //   276: invokeinterface 184 2 0
    //   281: ldc 186
    //   283: invokestatic 192	com/aalife/android/UtilityHelper:formatDouble	(DLjava/lang/String;)Ljava/lang/String;
    //   286: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   289: invokevirtual 51	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   292: astore 13
    //   294: aload 8
    //   296: ldc_w 296
    //   299: aload 13
    //   301: invokeinterface 198 3 0
    //   306: pop
    //   307: aload_2
    //   308: aload 8
    //   310: invokeinterface 120 2 0
    //   315: pop
    //   316: goto -196 -> 120
    //   319: astore 6
    //   321: aload 6
    //   323: invokevirtual 59	java/lang/Exception:printStackTrace	()V
    //   326: aload 4
    //   328: ifnull -182 -> 146
    //   331: aload 4
    //   333: invokeinterface 94 1 0
    //   338: aload_2
    //   339: areturn
    //   340: ldc_w 332
    //   343: astore 11
    //   345: goto -107 -> 238
    //   348: ldc_w 332
    //   351: astore 13
    //   353: goto -59 -> 294
    //   356: astore 5
    //   358: aload 4
    //   360: ifnull +10 -> 370
    //   363: aload 4
    //   365: invokeinterface 94 1 0
    //   370: aload 5
    //   372: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	373	0	this	ItemTableAccess
    //   0	373	1	paramString	String
    //   7	332	2	localArrayList	java.util.ArrayList
    //   105	9	3	str1	String
    //   107	257	4	localCursor	Cursor
    //   356	15	5	localObject	Object
    //   319	3	6	localException	Exception
    //   127	3	7	bool	boolean
    //   155	154	8	localHashMap	HashMap
    //   236	108	11	str2	String
    //   292	60	13	str3	String
    // Exception table:
    //   from	to	target	type
    //   109	120	319	java/lang/Exception
    //   120	129	319	java/lang/Exception
    //   148	238	319	java/lang/Exception
    //   238	294	319	java/lang/Exception
    //   294	316	319	java/lang/Exception
    //   109	120	356	finally
    //   120	129	356	finally
    //   148	238	356	finally
    //   238	294	356	finally
    //   294	316	356	finally
    //   321	326	356	finally
  }
  
  /* Error */
  public java.util.List<Map<String, String>> findDelSyncItem()
  {
    // Byte code:
    //   0: new 102	java/util/ArrayList
    //   3: dup
    //   4: invokespecial 103	java/util/ArrayList:<init>	()V
    //   7: astore_1
    //   8: aconst_null
    //   9: astore_2
    //   10: aload_0
    //   11: getfield 23	com/aalife/android/ItemTableAccess:db	Landroid/database/sqlite/SQLiteDatabase;
    //   14: ldc_w 335
    //   17: aconst_null
    //   18: invokevirtual 67	android/database/sqlite/SQLiteDatabase:rawQuery	(Ljava/lang/String;[Ljava/lang/String;)Landroid/database/Cursor;
    //   21: astore_2
    //   22: aload_2
    //   23: invokeinterface 108 1 0
    //   28: istore 5
    //   30: iload 5
    //   32: ifne +15 -> 47
    //   35: aload_2
    //   36: ifnull +9 -> 45
    //   39: aload_2
    //   40: invokeinterface 94 1 0
    //   45: aload_1
    //   46: areturn
    //   47: new 177	java/util/HashMap
    //   50: dup
    //   51: invokespecial 178	java/util/HashMap:<init>	()V
    //   54: astore 6
    //   56: aload 6
    //   58: ldc_w 337
    //   61: aload_2
    //   62: iconst_0
    //   63: invokeinterface 91 2 0
    //   68: invokeinterface 198 3 0
    //   73: pop
    //   74: aload 6
    //   76: ldc_w 339
    //   79: aload_2
    //   80: iconst_1
    //   81: invokeinterface 91 2 0
    //   86: invokeinterface 198 3 0
    //   91: pop
    //   92: aload_1
    //   93: aload 6
    //   95: invokeinterface 120 2 0
    //   100: pop
    //   101: goto -79 -> 22
    //   104: astore 4
    //   106: aload 4
    //   108: invokevirtual 59	java/lang/Exception:printStackTrace	()V
    //   111: aload_2
    //   112: ifnull -67 -> 45
    //   115: aload_2
    //   116: invokeinterface 94 1 0
    //   121: aload_1
    //   122: areturn
    //   123: astore_3
    //   124: aload_2
    //   125: ifnull +9 -> 134
    //   128: aload_2
    //   129: invokeinterface 94 1 0
    //   134: aload_3
    //   135: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	136	0	this	ItemTableAccess
    //   7	115	1	localArrayList	java.util.ArrayList
    //   9	120	2	localCursor	Cursor
    //   123	12	3	localObject	Object
    //   104	3	4	localException	Exception
    //   28	3	5	bool	boolean
    //   54	40	6	localHashMap	HashMap
    // Exception table:
    //   from	to	target	type
    //   10	22	104	java/lang/Exception
    //   22	30	104	java/lang/Exception
    //   47	101	104	java/lang/Exception
    //   10	22	123	finally
    //   22	30	123	finally
    //   47	101	123	finally
    //   106	111	123	finally
  }
  
  public String findFirstDate()
  {
    Object localObject1 = "";
    Cursor localCursor = null;
    try
    {
      localCursor = this.db.rawQuery("SELECT ItemBuyDate FROM ItemTable GROUP BY STRFTIME('%Y-%m-%d', ItemBuyDate) ORDER BY ItemBuyDate DESC", null);
      if (localCursor.moveToFirst())
      {
        String str = UtilityHelper.formatDate(localCursor.getString(0), "y-m-d");
        localObject1 = str;
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
  public java.util.List<Map<String, String>> findHomeTotalByDate(String paramString)
  {
    // Byte code:
    //   0: new 102	java/util/ArrayList
    //   3: dup
    //   4: invokespecial 103	java/util/ArrayList:<init>	()V
    //   7: astore_2
    //   8: new 29	java/lang/StringBuilder
    //   11: dup
    //   12: ldc_w 347
    //   15: invokespecial 34	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   18: aload_1
    //   19: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   22: ldc_w 349
    //   25: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   28: ldc_w 351
    //   31: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   34: ldc_w 353
    //   37: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   40: ldc 14
    //   42: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   45: ldc_w 355
    //   48: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   51: aload_1
    //   52: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   55: ldc_w 357
    //   58: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   61: ldc_w 359
    //   64: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   67: ldc_w 361
    //   70: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   73: ldc_w 363
    //   76: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   79: ldc_w 365
    //   82: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   85: ldc 14
    //   87: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   90: ldc_w 275
    //   93: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   96: aload_1
    //   97: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   100: ldc_w 349
    //   103: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   106: ldc_w 351
    //   109: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   112: ldc_w 367
    //   115: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   118: ldc 14
    //   120: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   123: ldc_w 369
    //   126: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   129: aload_1
    //   130: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   133: ldc_w 371
    //   136: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   139: ldc_w 359
    //   142: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   145: ldc_w 361
    //   148: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   151: ldc_w 363
    //   154: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   157: ldc_w 373
    //   160: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   163: ldc 14
    //   165: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   168: ldc_w 375
    //   171: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   174: aload_1
    //   175: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   178: ldc_w 349
    //   181: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   184: ldc_w 351
    //   187: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   190: ldc_w 377
    //   193: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   196: ldc 14
    //   198: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   201: ldc_w 379
    //   204: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   207: aload_1
    //   208: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   211: ldc_w 381
    //   214: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   217: ldc_w 359
    //   220: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   223: invokevirtual 51	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   226: astore_3
    //   227: aconst_null
    //   228: astore 4
    //   230: aload_0
    //   231: getfield 23	com/aalife/android/ItemTableAccess:db	Landroid/database/sqlite/SQLiteDatabase;
    //   234: aload_3
    //   235: aconst_null
    //   236: invokevirtual 67	android/database/sqlite/SQLiteDatabase:rawQuery	(Ljava/lang/String;[Ljava/lang/String;)Landroid/database/Cursor;
    //   239: astore 4
    //   241: aload 4
    //   243: invokeinterface 108 1 0
    //   248: istore 7
    //   250: iload 7
    //   252: ifne +17 -> 269
    //   255: aload 4
    //   257: ifnull +10 -> 267
    //   260: aload 4
    //   262: invokeinterface 94 1 0
    //   267: aload_2
    //   268: areturn
    //   269: new 177	java/util/HashMap
    //   272: dup
    //   273: invokespecial 178	java/util/HashMap:<init>	()V
    //   276: astore 8
    //   278: aload 8
    //   280: ldc_w 383
    //   283: new 29	java/lang/StringBuilder
    //   286: dup
    //   287: ldc 237
    //   289: invokespecial 34	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   292: aload 4
    //   294: iconst_1
    //   295: invokeinterface 184 2 0
    //   300: ldc 186
    //   302: invokestatic 192	com/aalife/android/UtilityHelper:formatDouble	(DLjava/lang/String;)Ljava/lang/String;
    //   305: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   308: invokevirtual 51	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   311: invokeinterface 198 3 0
    //   316: pop
    //   317: aload 8
    //   319: ldc_w 385
    //   322: aload 4
    //   324: iconst_2
    //   325: invokeinterface 91 2 0
    //   330: invokeinterface 198 3 0
    //   335: pop
    //   336: aload 8
    //   338: ldc_w 387
    //   341: new 29	java/lang/StringBuilder
    //   344: dup
    //   345: ldc 237
    //   347: invokespecial 34	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   350: aload 4
    //   352: iconst_5
    //   353: invokeinterface 184 2 0
    //   358: ldc 186
    //   360: invokestatic 192	com/aalife/android/UtilityHelper:formatDouble	(DLjava/lang/String;)Ljava/lang/String;
    //   363: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   366: invokevirtual 51	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   369: invokeinterface 198 3 0
    //   374: pop
    //   375: aload 8
    //   377: ldc_w 389
    //   380: aload 4
    //   382: bipush 6
    //   384: invokeinterface 91 2 0
    //   389: invokeinterface 198 3 0
    //   394: pop
    //   395: aload_2
    //   396: aload 8
    //   398: invokeinterface 120 2 0
    //   403: pop
    //   404: goto -163 -> 241
    //   407: astore 6
    //   409: aload 6
    //   411: invokevirtual 59	java/lang/Exception:printStackTrace	()V
    //   414: aload 4
    //   416: ifnull -149 -> 267
    //   419: aload 4
    //   421: invokeinterface 94 1 0
    //   426: aload_2
    //   427: areturn
    //   428: astore 5
    //   430: aload 4
    //   432: ifnull +10 -> 442
    //   435: aload 4
    //   437: invokeinterface 94 1 0
    //   442: aload 5
    //   444: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	445	0	this	ItemTableAccess
    //   0	445	1	paramString	String
    //   7	420	2	localArrayList	java.util.ArrayList
    //   226	9	3	str	String
    //   228	208	4	localCursor	Cursor
    //   428	15	5	localObject	Object
    //   407	3	6	localException	Exception
    //   248	3	7	bool	boolean
    //   276	121	8	localHashMap	HashMap
    // Exception table:
    //   from	to	target	type
    //   230	241	407	java/lang/Exception
    //   241	250	407	java/lang/Exception
    //   269	404	407	java/lang/Exception
    //   230	241	428	finally
    //   241	250	428	finally
    //   269	404	428	finally
    //   409	414	428	finally
  }
  
  /* Error */
  public java.util.List<Map<String, String>> findItemByDate(String paramString)
  {
    // Byte code:
    //   0: new 102	java/util/ArrayList
    //   3: dup
    //   4: invokespecial 103	java/util/ArrayList:<init>	()V
    //   7: astore_2
    //   8: new 29	java/lang/StringBuilder
    //   11: dup
    //   12: ldc_w 392
    //   15: invokespecial 34	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   18: aload_1
    //   19: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   22: ldc 85
    //   24: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   27: invokevirtual 51	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   30: astore_3
    //   31: aconst_null
    //   32: astore 4
    //   34: aload_0
    //   35: getfield 23	com/aalife/android/ItemTableAccess:db	Landroid/database/sqlite/SQLiteDatabase;
    //   38: aload_3
    //   39: aconst_null
    //   40: invokevirtual 67	android/database/sqlite/SQLiteDatabase:rawQuery	(Ljava/lang/String;[Ljava/lang/String;)Landroid/database/Cursor;
    //   43: astore 4
    //   45: aload 4
    //   47: invokeinterface 108 1 0
    //   52: istore 7
    //   54: iload 7
    //   56: ifne +17 -> 73
    //   59: aload 4
    //   61: ifnull +10 -> 71
    //   64: aload 4
    //   66: invokeinterface 94 1 0
    //   71: aload_2
    //   72: areturn
    //   73: new 177	java/util/HashMap
    //   76: dup
    //   77: invokespecial 178	java/util/HashMap:<init>	()V
    //   80: astore 8
    //   82: aload 8
    //   84: ldc_w 337
    //   87: aload 4
    //   89: iconst_0
    //   90: invokeinterface 91 2 0
    //   95: invokeinterface 198 3 0
    //   100: pop
    //   101: aload 8
    //   103: ldc_w 281
    //   106: aload 4
    //   108: iconst_1
    //   109: invokeinterface 91 2 0
    //   114: invokeinterface 198 3 0
    //   119: pop
    //   120: aload 8
    //   122: ldc_w 303
    //   125: new 29	java/lang/StringBuilder
    //   128: dup
    //   129: ldc 237
    //   131: invokespecial 34	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   134: aload 4
    //   136: iconst_2
    //   137: invokeinterface 184 2 0
    //   142: ldc 186
    //   144: invokestatic 192	com/aalife/android/UtilityHelper:formatDouble	(DLjava/lang/String;)Ljava/lang/String;
    //   147: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   150: invokevirtual 51	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   153: invokeinterface 198 3 0
    //   158: pop
    //   159: aload 8
    //   161: ldc 180
    //   163: aload 4
    //   165: iconst_2
    //   166: invokeinterface 184 2 0
    //   171: ldc 186
    //   173: invokestatic 192	com/aalife/android/UtilityHelper:formatDouble	(DLjava/lang/String;)Ljava/lang/String;
    //   176: invokeinterface 198 3 0
    //   181: pop
    //   182: aload 8
    //   184: ldc 200
    //   186: aload 4
    //   188: iconst_3
    //   189: invokeinterface 91 2 0
    //   194: invokeinterface 198 3 0
    //   199: pop
    //   200: aload 8
    //   202: ldc_w 328
    //   205: aload 4
    //   207: iconst_4
    //   208: invokeinterface 91 2 0
    //   213: invokeinterface 198 3 0
    //   218: pop
    //   219: aload 8
    //   221: ldc_w 394
    //   224: aload 4
    //   226: iconst_5
    //   227: invokeinterface 91 2 0
    //   232: invokeinterface 198 3 0
    //   237: pop
    //   238: aload_2
    //   239: aload 8
    //   241: invokeinterface 120 2 0
    //   246: pop
    //   247: goto -202 -> 45
    //   250: astore 6
    //   252: aload 6
    //   254: invokevirtual 59	java/lang/Exception:printStackTrace	()V
    //   257: aload 4
    //   259: ifnull -188 -> 71
    //   262: aload 4
    //   264: invokeinterface 94 1 0
    //   269: aload_2
    //   270: areturn
    //   271: astore 5
    //   273: aload 4
    //   275: ifnull +10 -> 285
    //   278: aload 4
    //   280: invokeinterface 94 1 0
    //   285: aload 5
    //   287: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	288	0	this	ItemTableAccess
    //   0	288	1	paramString	String
    //   7	263	2	localArrayList	java.util.ArrayList
    //   30	9	3	str	String
    //   32	247	4	localCursor	Cursor
    //   271	15	5	localObject	Object
    //   250	3	6	localException	Exception
    //   52	3	7	bool	boolean
    //   80	160	8	localHashMap	HashMap
    // Exception table:
    //   from	to	target	type
    //   34	45	250	java/lang/Exception
    //   45	54	250	java/lang/Exception
    //   73	247	250	java/lang/Exception
    //   34	45	271	finally
    //   45	54	271	finally
    //   73	247	271	finally
    //   252	257	271	finally
  }
  
  /* Error */
  public java.util.List<Map<String, String>> findItemByKey(String paramString)
  {
    // Byte code:
    //   0: new 102	java/util/ArrayList
    //   3: dup
    //   4: invokespecial 103	java/util/ArrayList:<init>	()V
    //   7: astore_2
    //   8: new 29	java/lang/StringBuilder
    //   11: dup
    //   12: ldc_w 397
    //   15: invokespecial 34	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   18: aload_1
    //   19: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   22: ldc_w 399
    //   25: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   28: invokevirtual 51	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   31: astore_3
    //   32: aconst_null
    //   33: astore 4
    //   35: aload_0
    //   36: getfield 23	com/aalife/android/ItemTableAccess:db	Landroid/database/sqlite/SQLiteDatabase;
    //   39: aload_3
    //   40: aconst_null
    //   41: invokevirtual 67	android/database/sqlite/SQLiteDatabase:rawQuery	(Ljava/lang/String;[Ljava/lang/String;)Landroid/database/Cursor;
    //   44: astore 4
    //   46: aload 4
    //   48: invokeinterface 108 1 0
    //   53: istore 7
    //   55: iload 7
    //   57: ifne +17 -> 74
    //   60: aload 4
    //   62: ifnull +10 -> 72
    //   65: aload 4
    //   67: invokeinterface 94 1 0
    //   72: aload_2
    //   73: areturn
    //   74: new 177	java/util/HashMap
    //   77: dup
    //   78: invokespecial 178	java/util/HashMap:<init>	()V
    //   81: astore 8
    //   83: aload 8
    //   85: ldc_w 281
    //   88: aload 4
    //   90: iconst_0
    //   91: invokeinterface 91 2 0
    //   96: invokeinterface 198 3 0
    //   101: pop
    //   102: aload 8
    //   104: ldc 200
    //   106: aload 4
    //   108: iconst_1
    //   109: invokeinterface 91 2 0
    //   114: ldc_w 301
    //   117: invokestatic 206	com/aalife/android/UtilityHelper:formatDate	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   120: invokeinterface 198 3 0
    //   125: pop
    //   126: aload 8
    //   128: ldc 243
    //   130: aload 4
    //   132: iconst_1
    //   133: invokeinterface 91 2 0
    //   138: ldc 202
    //   140: invokestatic 206	com/aalife/android/UtilityHelper:formatDate	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   143: invokeinterface 198 3 0
    //   148: pop
    //   149: aload 8
    //   151: ldc_w 303
    //   154: new 29	java/lang/StringBuilder
    //   157: dup
    //   158: ldc 237
    //   160: invokespecial 34	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   163: aload 4
    //   165: iconst_2
    //   166: invokeinterface 184 2 0
    //   171: ldc 186
    //   173: invokestatic 192	com/aalife/android/UtilityHelper:formatDouble	(DLjava/lang/String;)Ljava/lang/String;
    //   176: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   179: invokevirtual 51	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   182: invokeinterface 198 3 0
    //   187: pop
    //   188: aload_2
    //   189: aload 8
    //   191: invokeinterface 120 2 0
    //   196: pop
    //   197: goto -151 -> 46
    //   200: astore 6
    //   202: aload 6
    //   204: invokevirtual 59	java/lang/Exception:printStackTrace	()V
    //   207: aload 4
    //   209: ifnull -137 -> 72
    //   212: aload 4
    //   214: invokeinterface 94 1 0
    //   219: aload_2
    //   220: areturn
    //   221: astore 5
    //   223: aload 4
    //   225: ifnull +10 -> 235
    //   228: aload 4
    //   230: invokeinterface 94 1 0
    //   235: aload 5
    //   237: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	238	0	this	ItemTableAccess
    //   0	238	1	paramString	String
    //   7	213	2	localArrayList	java.util.ArrayList
    //   31	9	3	str	String
    //   33	196	4	localCursor	Cursor
    //   221	15	5	localObject	Object
    //   200	3	6	localException	Exception
    //   53	3	7	bool	boolean
    //   81	109	8	localHashMap	HashMap
    // Exception table:
    //   from	to	target	type
    //   35	46	200	java/lang/Exception
    //   46	55	200	java/lang/Exception
    //   74	197	200	java/lang/Exception
    //   35	46	221	finally
    //   46	55	221	finally
    //   74	197	221	finally
    //   202	207	221	finally
  }
  
  public String findLastDate()
  {
    Object localObject1 = "";
    Cursor localCursor = null;
    try
    {
      localCursor = this.db.rawQuery("SELECT ItemBuyDate FROM ItemTable ORDER BY ItemBuyDate DESC LIMIT 30", null);
      if (localCursor.moveToLast())
      {
        String str = localCursor.getString(0);
        localObject1 = str;
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
  public java.util.List<Map<String, String>> findMonthByDate(String paramString)
  {
    // Byte code:
    //   0: new 102	java/util/ArrayList
    //   3: dup
    //   4: invokespecial 103	java/util/ArrayList:<init>	()V
    //   7: astore_2
    //   8: new 29	java/lang/StringBuilder
    //   11: dup
    //   12: ldc 173
    //   14: invokespecial 34	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   17: aload_1
    //   18: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   21: ldc 47
    //   23: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   26: invokevirtual 51	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   29: astore_3
    //   30: aconst_null
    //   31: astore 4
    //   33: aload_0
    //   34: getfield 23	com/aalife/android/ItemTableAccess:db	Landroid/database/sqlite/SQLiteDatabase;
    //   37: aload_3
    //   38: aconst_null
    //   39: invokevirtual 67	android/database/sqlite/SQLiteDatabase:rawQuery	(Ljava/lang/String;[Ljava/lang/String;)Landroid/database/Cursor;
    //   42: astore 4
    //   44: aload 4
    //   46: invokeinterface 108 1 0
    //   51: istore 7
    //   53: iload 7
    //   55: ifne +17 -> 72
    //   58: aload 4
    //   60: ifnull +10 -> 70
    //   63: aload 4
    //   65: invokeinterface 94 1 0
    //   70: aload_2
    //   71: areturn
    //   72: new 177	java/util/HashMap
    //   75: dup
    //   76: invokespecial 178	java/util/HashMap:<init>	()V
    //   79: astore 8
    //   81: aload 8
    //   83: ldc 216
    //   85: iconst_1
    //   86: aload 4
    //   88: invokeinterface 220 1 0
    //   93: iadd
    //   94: invokestatic 225	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   97: invokeinterface 198 3 0
    //   102: pop
    //   103: aload 8
    //   105: ldc 235
    //   107: new 29	java/lang/StringBuilder
    //   110: dup
    //   111: ldc 237
    //   113: invokespecial 34	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   116: aload 4
    //   118: iconst_0
    //   119: invokeinterface 184 2 0
    //   124: ldc 186
    //   126: invokestatic 192	com/aalife/android/UtilityHelper:formatDouble	(DLjava/lang/String;)Ljava/lang/String;
    //   129: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   132: invokevirtual 51	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   135: invokeinterface 198 3 0
    //   140: pop
    //   141: aload 8
    //   143: ldc 180
    //   145: aload 4
    //   147: iconst_0
    //   148: invokeinterface 184 2 0
    //   153: ldc 186
    //   155: invokestatic 192	com/aalife/android/UtilityHelper:formatDouble	(DLjava/lang/String;)Ljava/lang/String;
    //   158: invokeinterface 198 3 0
    //   163: pop
    //   164: aload 8
    //   166: ldc 239
    //   168: aload 4
    //   170: iconst_1
    //   171: invokeinterface 91 2 0
    //   176: ldc_w 408
    //   179: invokestatic 206	com/aalife/android/UtilityHelper:formatDate	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   182: invokeinterface 198 3 0
    //   187: pop
    //   188: aload 8
    //   190: ldc 243
    //   192: aload 4
    //   194: iconst_1
    //   195: invokeinterface 91 2 0
    //   200: ldc 202
    //   202: invokestatic 206	com/aalife/android/UtilityHelper:formatDate	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   205: invokeinterface 198 3 0
    //   210: pop
    //   211: aload_2
    //   212: aload 8
    //   214: invokeinterface 120 2 0
    //   219: pop
    //   220: goto -176 -> 44
    //   223: astore 6
    //   225: aload 6
    //   227: invokevirtual 59	java/lang/Exception:printStackTrace	()V
    //   230: aload 4
    //   232: ifnull -162 -> 70
    //   235: aload 4
    //   237: invokeinterface 94 1 0
    //   242: aload_2
    //   243: areturn
    //   244: astore 5
    //   246: aload 4
    //   248: ifnull +10 -> 258
    //   251: aload 4
    //   253: invokeinterface 94 1 0
    //   258: aload 5
    //   260: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	261	0	this	ItemTableAccess
    //   0	261	1	paramString	String
    //   7	236	2	localArrayList	java.util.ArrayList
    //   29	9	3	str	String
    //   31	221	4	localCursor	Cursor
    //   244	15	5	localObject	Object
    //   223	3	6	localException	Exception
    //   51	3	7	bool	boolean
    //   79	134	8	localHashMap	HashMap
    // Exception table:
    //   from	to	target	type
    //   33	44	223	java/lang/Exception
    //   44	53	223	java/lang/Exception
    //   72	220	223	java/lang/Exception
    //   33	44	244	finally
    //   44	53	244	finally
    //   72	220	244	finally
    //   225	230	244	finally
  }
  
  public int findMonthCountByDate(String paramString)
  {
    String str = "SELECT COUNT(0) FROM ItemTable WHERE STRFTIME('%Y-%m', ItemBuyDate) = STRFTIME('%Y-%m', '" + paramString + "')";
    Cursor localCursor = null;
    try
    {
      localCursor = this.db.rawQuery(str, null);
      boolean bool = localCursor.moveToFirst();
      i = 0;
      if (bool)
      {
        int j = localCursor.getInt(0);
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
  
  public String findNextDate(String paramString)
  {
    Object localObject1 = "";
    String str1 = "SELECT ItemBuyDate FROM ItemTable GROUP BY STRFTIME('%Y-%m-%d', ItemBuyDate) HAVING STRFTIME('%Y-%m', ItemBuyDate) < STRFTIME('%Y-%m', '" + paramString + "') ORDER BY ItemBuyDate DESC";
    Cursor localCursor = null;
    try
    {
      localCursor = this.db.rawQuery(str1, null);
      if (localCursor.moveToFirst())
      {
        String str2 = UtilityHelper.formatDate(localCursor.getString(0), "y-m-d");
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
  public java.util.List<Map<String, String>> findRankCatByDate(String paramString)
  {
    // Byte code:
    //   0: new 102	java/util/ArrayList
    //   3: dup
    //   4: invokespecial 103	java/util/ArrayList:<init>	()V
    //   7: astore_2
    //   8: new 29	java/lang/StringBuilder
    //   11: dup
    //   12: ldc_w 419
    //   15: invokespecial 34	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   18: aload_1
    //   19: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   22: ldc_w 308
    //   25: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   28: ldc_w 310
    //   31: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   34: ldc_w 421
    //   37: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   40: invokevirtual 51	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   43: astore_3
    //   44: aconst_null
    //   45: astore 4
    //   47: aload_0
    //   48: getfield 23	com/aalife/android/ItemTableAccess:db	Landroid/database/sqlite/SQLiteDatabase;
    //   51: aload_3
    //   52: aconst_null
    //   53: invokevirtual 67	android/database/sqlite/SQLiteDatabase:rawQuery	(Ljava/lang/String;[Ljava/lang/String;)Landroid/database/Cursor;
    //   56: astore 4
    //   58: aload 4
    //   60: invokeinterface 108 1 0
    //   65: istore 7
    //   67: iload 7
    //   69: ifne +17 -> 86
    //   72: aload 4
    //   74: ifnull +10 -> 84
    //   77: aload 4
    //   79: invokeinterface 94 1 0
    //   84: aload_2
    //   85: areturn
    //   86: new 177	java/util/HashMap
    //   89: dup
    //   90: invokespecial 178	java/util/HashMap:<init>	()V
    //   93: astore 8
    //   95: aload 8
    //   97: ldc 216
    //   99: iconst_1
    //   100: aload 4
    //   102: invokeinterface 220 1 0
    //   107: iadd
    //   108: invokestatic 225	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   111: invokeinterface 198 3 0
    //   116: pop
    //   117: aload 8
    //   119: ldc_w 328
    //   122: aload 4
    //   124: iconst_0
    //   125: invokeinterface 91 2 0
    //   130: invokeinterface 198 3 0
    //   135: pop
    //   136: aload 8
    //   138: ldc_w 330
    //   141: aload 4
    //   143: iconst_1
    //   144: invokeinterface 91 2 0
    //   149: invokeinterface 198 3 0
    //   154: pop
    //   155: aload 4
    //   157: iconst_2
    //   158: invokeinterface 184 2 0
    //   163: dconst_0
    //   164: dcmpl
    //   165: ifle +78 -> 243
    //   168: new 29	java/lang/StringBuilder
    //   171: dup
    //   172: ldc 237
    //   174: invokespecial 34	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   177: aload 4
    //   179: iconst_2
    //   180: invokeinterface 184 2 0
    //   185: ldc 186
    //   187: invokestatic 192	com/aalife/android/UtilityHelper:formatDouble	(DLjava/lang/String;)Ljava/lang/String;
    //   190: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   193: invokevirtual 51	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   196: astore 12
    //   198: aload 8
    //   200: ldc 235
    //   202: aload 12
    //   204: invokeinterface 198 3 0
    //   209: pop
    //   210: aload_2
    //   211: aload 8
    //   213: invokeinterface 120 2 0
    //   218: pop
    //   219: goto -161 -> 58
    //   222: astore 6
    //   224: aload 6
    //   226: invokevirtual 59	java/lang/Exception:printStackTrace	()V
    //   229: aload 4
    //   231: ifnull -147 -> 84
    //   234: aload 4
    //   236: invokeinterface 94 1 0
    //   241: aload_2
    //   242: areturn
    //   243: ldc_w 332
    //   246: astore 12
    //   248: goto -50 -> 198
    //   251: astore 5
    //   253: aload 4
    //   255: ifnull +10 -> 265
    //   258: aload 4
    //   260: invokeinterface 94 1 0
    //   265: aload 5
    //   267: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	268	0	this	ItemTableAccess
    //   0	268	1	paramString	String
    //   7	235	2	localArrayList	java.util.ArrayList
    //   43	9	3	str1	String
    //   45	214	4	localCursor	Cursor
    //   251	15	5	localObject	Object
    //   222	3	6	localException	Exception
    //   65	3	7	bool	boolean
    //   93	119	8	localHashMap	HashMap
    //   196	51	12	str2	String
    // Exception table:
    //   from	to	target	type
    //   47	58	222	java/lang/Exception
    //   58	67	222	java/lang/Exception
    //   86	198	222	java/lang/Exception
    //   198	219	222	java/lang/Exception
    //   47	58	251	finally
    //   58	67	251	finally
    //   86	198	251	finally
    //   198	219	251	finally
    //   224	229	251	finally
  }
  
  /* Error */
  public java.util.List<Map<String, String>> findRankCountByDate(String paramString)
  {
    // Byte code:
    //   0: new 102	java/util/ArrayList
    //   3: dup
    //   4: invokespecial 103	java/util/ArrayList:<init>	()V
    //   7: astore_2
    //   8: new 29	java/lang/StringBuilder
    //   11: dup
    //   12: ldc_w 424
    //   15: invokespecial 34	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   18: aload_1
    //   19: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   22: ldc_w 426
    //   25: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   28: invokevirtual 51	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   31: astore_3
    //   32: aconst_null
    //   33: astore 4
    //   35: aload_0
    //   36: getfield 23	com/aalife/android/ItemTableAccess:db	Landroid/database/sqlite/SQLiteDatabase;
    //   39: aload_3
    //   40: aconst_null
    //   41: invokevirtual 67	android/database/sqlite/SQLiteDatabase:rawQuery	(Ljava/lang/String;[Ljava/lang/String;)Landroid/database/Cursor;
    //   44: astore 4
    //   46: aload 4
    //   48: invokeinterface 108 1 0
    //   53: istore 7
    //   55: iload 7
    //   57: ifne +17 -> 74
    //   60: aload 4
    //   62: ifnull +10 -> 72
    //   65: aload 4
    //   67: invokeinterface 94 1 0
    //   72: aload_2
    //   73: areturn
    //   74: new 177	java/util/HashMap
    //   77: dup
    //   78: invokespecial 178	java/util/HashMap:<init>	()V
    //   81: astore 8
    //   83: aload 8
    //   85: ldc_w 281
    //   88: aload 4
    //   90: iconst_0
    //   91: invokeinterface 91 2 0
    //   96: invokeinterface 198 3 0
    //   101: pop
    //   102: aload 8
    //   104: ldc_w 428
    //   107: new 29	java/lang/StringBuilder
    //   110: dup
    //   111: aload 4
    //   113: iconst_1
    //   114: invokeinterface 91 2 0
    //   119: invokestatic 288	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
    //   122: invokespecial 34	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   125: ldc_w 290
    //   128: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   131: invokevirtual 51	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   134: invokeinterface 198 3 0
    //   139: pop
    //   140: aload 8
    //   142: ldc 235
    //   144: new 29	java/lang/StringBuilder
    //   147: dup
    //   148: ldc 237
    //   150: invokespecial 34	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   153: aload 4
    //   155: iconst_2
    //   156: invokeinterface 184 2 0
    //   161: ldc 186
    //   163: invokestatic 192	com/aalife/android/UtilityHelper:formatDouble	(DLjava/lang/String;)Ljava/lang/String;
    //   166: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   169: invokevirtual 51	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   172: invokeinterface 198 3 0
    //   177: pop
    //   178: aload_2
    //   179: aload 8
    //   181: invokeinterface 120 2 0
    //   186: pop
    //   187: goto -141 -> 46
    //   190: astore 6
    //   192: aload 6
    //   194: invokevirtual 59	java/lang/Exception:printStackTrace	()V
    //   197: aload 4
    //   199: ifnull -127 -> 72
    //   202: aload 4
    //   204: invokeinterface 94 1 0
    //   209: aload_2
    //   210: areturn
    //   211: astore 5
    //   213: aload 4
    //   215: ifnull +10 -> 225
    //   218: aload 4
    //   220: invokeinterface 94 1 0
    //   225: aload 5
    //   227: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	228	0	this	ItemTableAccess
    //   0	228	1	paramString	String
    //   7	203	2	localArrayList	java.util.ArrayList
    //   31	9	3	str	String
    //   33	186	4	localCursor	Cursor
    //   211	15	5	localObject	Object
    //   190	3	6	localException	Exception
    //   53	3	7	bool	boolean
    //   81	99	8	localHashMap	HashMap
    // Exception table:
    //   from	to	target	type
    //   35	46	190	java/lang/Exception
    //   46	55	190	java/lang/Exception
    //   74	187	190	java/lang/Exception
    //   35	46	211	finally
    //   46	55	211	finally
    //   74	187	211	finally
    //   192	197	211	finally
  }
  
  /* Error */
  public java.util.List<Map<String, String>> findRankCountByDate(String paramString, int paramInt)
  {
    // Byte code:
    //   0: new 102	java/util/ArrayList
    //   3: dup
    //   4: invokespecial 103	java/util/ArrayList:<init>	()V
    //   7: astore_3
    //   8: new 29	java/lang/StringBuilder
    //   11: dup
    //   12: ldc_w 424
    //   15: invokespecial 34	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   18: aload_1
    //   19: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   22: ldc_w 257
    //   25: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   28: iload_2
    //   29: invokevirtual 43	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   32: ldc 85
    //   34: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   37: ldc_w 430
    //   40: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   43: invokevirtual 51	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   46: astore 4
    //   48: aconst_null
    //   49: astore 5
    //   51: aload_0
    //   52: getfield 23	com/aalife/android/ItemTableAccess:db	Landroid/database/sqlite/SQLiteDatabase;
    //   55: aload 4
    //   57: aconst_null
    //   58: invokevirtual 67	android/database/sqlite/SQLiteDatabase:rawQuery	(Ljava/lang/String;[Ljava/lang/String;)Landroid/database/Cursor;
    //   61: astore 5
    //   63: aload 5
    //   65: invokeinterface 108 1 0
    //   70: istore 8
    //   72: iload 8
    //   74: ifne +17 -> 91
    //   77: aload 5
    //   79: ifnull +10 -> 89
    //   82: aload 5
    //   84: invokeinterface 94 1 0
    //   89: aload_3
    //   90: areturn
    //   91: new 177	java/util/HashMap
    //   94: dup
    //   95: invokespecial 178	java/util/HashMap:<init>	()V
    //   98: astore 9
    //   100: aload 9
    //   102: ldc_w 281
    //   105: aload 5
    //   107: iconst_0
    //   108: invokeinterface 91 2 0
    //   113: invokeinterface 198 3 0
    //   118: pop
    //   119: aload 9
    //   121: ldc_w 428
    //   124: new 29	java/lang/StringBuilder
    //   127: dup
    //   128: aload 5
    //   130: iconst_1
    //   131: invokeinterface 91 2 0
    //   136: invokestatic 288	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
    //   139: invokespecial 34	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   142: ldc_w 290
    //   145: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   148: invokevirtual 51	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   151: invokeinterface 198 3 0
    //   156: pop
    //   157: aload 9
    //   159: ldc 235
    //   161: new 29	java/lang/StringBuilder
    //   164: dup
    //   165: ldc 237
    //   167: invokespecial 34	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   170: aload 5
    //   172: iconst_2
    //   173: invokeinterface 184 2 0
    //   178: ldc 186
    //   180: invokestatic 192	com/aalife/android/UtilityHelper:formatDouble	(DLjava/lang/String;)Ljava/lang/String;
    //   183: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   186: invokevirtual 51	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   189: invokeinterface 198 3 0
    //   194: pop
    //   195: aload_3
    //   196: aload 9
    //   198: invokeinterface 120 2 0
    //   203: pop
    //   204: goto -141 -> 63
    //   207: astore 7
    //   209: aload 7
    //   211: invokevirtual 59	java/lang/Exception:printStackTrace	()V
    //   214: aload 5
    //   216: ifnull -127 -> 89
    //   219: aload 5
    //   221: invokeinterface 94 1 0
    //   226: aload_3
    //   227: areturn
    //   228: astore 6
    //   230: aload 5
    //   232: ifnull +10 -> 242
    //   235: aload 5
    //   237: invokeinterface 94 1 0
    //   242: aload 6
    //   244: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	245	0	this	ItemTableAccess
    //   0	245	1	paramString	String
    //   0	245	2	paramInt	int
    //   7	220	3	localArrayList	java.util.ArrayList
    //   46	10	4	str	String
    //   49	187	5	localCursor	Cursor
    //   228	15	6	localObject	Object
    //   207	3	7	localException	Exception
    //   70	3	8	bool	boolean
    //   98	99	9	localHashMap	HashMap
    // Exception table:
    //   from	to	target	type
    //   51	63	207	java/lang/Exception
    //   63	72	207	java/lang/Exception
    //   91	204	207	java/lang/Exception
    //   51	63	228	finally
    //   63	72	228	finally
    //   91	204	228	finally
    //   209	214	228	finally
  }
  
  /* Error */
  public java.util.List<Map<String, String>> findRankDateByDate(String paramString)
  {
    // Byte code:
    //   0: new 102	java/util/ArrayList
    //   3: dup
    //   4: invokespecial 103	java/util/ArrayList:<init>	()V
    //   7: astore_2
    //   8: new 29	java/lang/StringBuilder
    //   11: dup
    //   12: ldc_w 433
    //   15: invokespecial 34	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   18: aload_1
    //   19: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   22: ldc 47
    //   24: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   27: ldc_w 435
    //   30: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   33: invokevirtual 51	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   36: astore_3
    //   37: aconst_null
    //   38: astore 4
    //   40: aload_0
    //   41: getfield 23	com/aalife/android/ItemTableAccess:db	Landroid/database/sqlite/SQLiteDatabase;
    //   44: aload_3
    //   45: aconst_null
    //   46: invokevirtual 67	android/database/sqlite/SQLiteDatabase:rawQuery	(Ljava/lang/String;[Ljava/lang/String;)Landroid/database/Cursor;
    //   49: astore 4
    //   51: aload 4
    //   53: invokeinterface 108 1 0
    //   58: istore 7
    //   60: iload 7
    //   62: ifne +17 -> 79
    //   65: aload 4
    //   67: ifnull +10 -> 77
    //   70: aload 4
    //   72: invokeinterface 94 1 0
    //   77: aload_2
    //   78: areturn
    //   79: new 177	java/util/HashMap
    //   82: dup
    //   83: invokespecial 178	java/util/HashMap:<init>	()V
    //   86: astore 8
    //   88: aload 8
    //   90: ldc 216
    //   92: iconst_1
    //   93: aload 4
    //   95: invokeinterface 220 1 0
    //   100: iadd
    //   101: invokestatic 225	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   104: invokeinterface 198 3 0
    //   109: pop
    //   110: aload 8
    //   112: ldc 200
    //   114: aload 4
    //   116: iconst_0
    //   117: invokeinterface 91 2 0
    //   122: ldc_w 437
    //   125: invokestatic 206	com/aalife/android/UtilityHelper:formatDate	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   128: invokeinterface 198 3 0
    //   133: pop
    //   134: aload 8
    //   136: ldc 243
    //   138: aload 4
    //   140: iconst_0
    //   141: invokeinterface 91 2 0
    //   146: ldc 202
    //   148: invokestatic 206	com/aalife/android/UtilityHelper:formatDate	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   151: invokeinterface 198 3 0
    //   156: pop
    //   157: aload 8
    //   159: ldc 235
    //   161: new 29	java/lang/StringBuilder
    //   164: dup
    //   165: ldc 237
    //   167: invokespecial 34	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   170: aload 4
    //   172: iconst_1
    //   173: invokeinterface 184 2 0
    //   178: ldc 186
    //   180: invokestatic 192	com/aalife/android/UtilityHelper:formatDouble	(DLjava/lang/String;)Ljava/lang/String;
    //   183: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   186: invokevirtual 51	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   189: invokeinterface 198 3 0
    //   194: pop
    //   195: aload_2
    //   196: aload 8
    //   198: invokeinterface 120 2 0
    //   203: pop
    //   204: goto -153 -> 51
    //   207: astore 6
    //   209: aload 6
    //   211: invokevirtual 59	java/lang/Exception:printStackTrace	()V
    //   214: aload 4
    //   216: ifnull -139 -> 77
    //   219: aload 4
    //   221: invokeinterface 94 1 0
    //   226: aload_2
    //   227: areturn
    //   228: astore 5
    //   230: aload 4
    //   232: ifnull +10 -> 242
    //   235: aload 4
    //   237: invokeinterface 94 1 0
    //   242: aload 5
    //   244: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	245	0	this	ItemTableAccess
    //   0	245	1	paramString	String
    //   7	220	2	localArrayList	java.util.ArrayList
    //   36	9	3	str	String
    //   38	198	4	localCursor	Cursor
    //   228	15	5	localObject	Object
    //   207	3	6	localException	Exception
    //   58	3	7	bool	boolean
    //   86	111	8	localHashMap	HashMap
    // Exception table:
    //   from	to	target	type
    //   40	51	207	java/lang/Exception
    //   51	60	207	java/lang/Exception
    //   79	204	207	java/lang/Exception
    //   40	51	228	finally
    //   51	60	228	finally
    //   79	204	228	finally
    //   209	214	228	finally
  }
  
  /* Error */
  public java.util.List<Map<String, String>> findRankPriceByDate(String paramString)
  {
    // Byte code:
    //   0: new 102	java/util/ArrayList
    //   3: dup
    //   4: invokespecial 103	java/util/ArrayList:<init>	()V
    //   7: astore_2
    //   8: new 29	java/lang/StringBuilder
    //   11: dup
    //   12: ldc_w 440
    //   15: invokespecial 34	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   18: aload_1
    //   19: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   22: ldc_w 442
    //   25: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   28: invokevirtual 51	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   31: astore_3
    //   32: aconst_null
    //   33: astore 4
    //   35: aload_0
    //   36: getfield 23	com/aalife/android/ItemTableAccess:db	Landroid/database/sqlite/SQLiteDatabase;
    //   39: aload_3
    //   40: aconst_null
    //   41: invokevirtual 67	android/database/sqlite/SQLiteDatabase:rawQuery	(Ljava/lang/String;[Ljava/lang/String;)Landroid/database/Cursor;
    //   44: astore 4
    //   46: aload 4
    //   48: invokeinterface 108 1 0
    //   53: istore 7
    //   55: iload 7
    //   57: ifne +17 -> 74
    //   60: aload 4
    //   62: ifnull +10 -> 72
    //   65: aload 4
    //   67: invokeinterface 94 1 0
    //   72: aload_2
    //   73: areturn
    //   74: new 177	java/util/HashMap
    //   77: dup
    //   78: invokespecial 178	java/util/HashMap:<init>	()V
    //   81: astore 8
    //   83: aload 8
    //   85: ldc_w 281
    //   88: aload 4
    //   90: iconst_0
    //   91: invokeinterface 91 2 0
    //   96: invokeinterface 198 3 0
    //   101: pop
    //   102: aload 8
    //   104: ldc 200
    //   106: aload 4
    //   108: iconst_1
    //   109: invokeinterface 91 2 0
    //   114: ldc_w 437
    //   117: invokestatic 206	com/aalife/android/UtilityHelper:formatDate	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   120: invokeinterface 198 3 0
    //   125: pop
    //   126: aload 8
    //   128: ldc 243
    //   130: aload 4
    //   132: iconst_1
    //   133: invokeinterface 91 2 0
    //   138: ldc 202
    //   140: invokestatic 206	com/aalife/android/UtilityHelper:formatDate	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   143: invokeinterface 198 3 0
    //   148: pop
    //   149: aload 8
    //   151: ldc_w 303
    //   154: new 29	java/lang/StringBuilder
    //   157: dup
    //   158: ldc 237
    //   160: invokespecial 34	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   163: aload 4
    //   165: iconst_2
    //   166: invokeinterface 184 2 0
    //   171: ldc 186
    //   173: invokestatic 192	com/aalife/android/UtilityHelper:formatDouble	(DLjava/lang/String;)Ljava/lang/String;
    //   176: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   179: invokevirtual 51	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   182: invokeinterface 198 3 0
    //   187: pop
    //   188: aload_2
    //   189: aload 8
    //   191: invokeinterface 120 2 0
    //   196: pop
    //   197: goto -151 -> 46
    //   200: astore 6
    //   202: aload 6
    //   204: invokevirtual 59	java/lang/Exception:printStackTrace	()V
    //   207: aload 4
    //   209: ifnull -137 -> 72
    //   212: aload 4
    //   214: invokeinterface 94 1 0
    //   219: aload_2
    //   220: areturn
    //   221: astore 5
    //   223: aload 4
    //   225: ifnull +10 -> 235
    //   228: aload 4
    //   230: invokeinterface 94 1 0
    //   235: aload 5
    //   237: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	238	0	this	ItemTableAccess
    //   0	238	1	paramString	String
    //   7	213	2	localArrayList	java.util.ArrayList
    //   31	9	3	str	String
    //   33	196	4	localCursor	Cursor
    //   221	15	5	localObject	Object
    //   200	3	6	localException	Exception
    //   53	3	7	bool	boolean
    //   81	109	8	localHashMap	HashMap
    // Exception table:
    //   from	to	target	type
    //   35	46	200	java/lang/Exception
    //   46	55	200	java/lang/Exception
    //   74	197	200	java/lang/Exception
    //   35	46	221	finally
    //   46	55	221	finally
    //   74	197	221	finally
    //   202	207	221	finally
  }
  
  /* Error */
  public java.util.List<Map<String, String>> findRankPriceByDate(String paramString1, String paramString2)
  {
    // Byte code:
    //   0: new 102	java/util/ArrayList
    //   3: dup
    //   4: invokespecial 103	java/util/ArrayList:<init>	()V
    //   7: astore_3
    //   8: new 29	java/lang/StringBuilder
    //   11: dup
    //   12: ldc_w 440
    //   15: invokespecial 34	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   18: aload_1
    //   19: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   22: ldc_w 445
    //   25: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   28: aload_2
    //   29: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   32: ldc 85
    //   34: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   37: ldc_w 447
    //   40: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   43: invokevirtual 51	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   46: astore 4
    //   48: aconst_null
    //   49: astore 5
    //   51: aload_0
    //   52: getfield 23	com/aalife/android/ItemTableAccess:db	Landroid/database/sqlite/SQLiteDatabase;
    //   55: aload 4
    //   57: aconst_null
    //   58: invokevirtual 67	android/database/sqlite/SQLiteDatabase:rawQuery	(Ljava/lang/String;[Ljava/lang/String;)Landroid/database/Cursor;
    //   61: astore 5
    //   63: aload 5
    //   65: invokeinterface 108 1 0
    //   70: istore 8
    //   72: iload 8
    //   74: ifne +17 -> 91
    //   77: aload 5
    //   79: ifnull +10 -> 89
    //   82: aload 5
    //   84: invokeinterface 94 1 0
    //   89: aload_3
    //   90: areturn
    //   91: new 177	java/util/HashMap
    //   94: dup
    //   95: invokespecial 178	java/util/HashMap:<init>	()V
    //   98: astore 9
    //   100: aload 9
    //   102: ldc_w 281
    //   105: aload 5
    //   107: iconst_0
    //   108: invokeinterface 91 2 0
    //   113: invokeinterface 198 3 0
    //   118: pop
    //   119: aload 9
    //   121: ldc 200
    //   123: aload 5
    //   125: iconst_1
    //   126: invokeinterface 91 2 0
    //   131: ldc_w 437
    //   134: invokestatic 206	com/aalife/android/UtilityHelper:formatDate	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   137: invokeinterface 198 3 0
    //   142: pop
    //   143: aload 9
    //   145: ldc 243
    //   147: aload 5
    //   149: iconst_1
    //   150: invokeinterface 91 2 0
    //   155: ldc 202
    //   157: invokestatic 206	com/aalife/android/UtilityHelper:formatDate	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   160: invokeinterface 198 3 0
    //   165: pop
    //   166: aload 9
    //   168: ldc_w 303
    //   171: new 29	java/lang/StringBuilder
    //   174: dup
    //   175: ldc 237
    //   177: invokespecial 34	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   180: aload 5
    //   182: iconst_2
    //   183: invokeinterface 184 2 0
    //   188: ldc 186
    //   190: invokestatic 192	com/aalife/android/UtilityHelper:formatDouble	(DLjava/lang/String;)Ljava/lang/String;
    //   193: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   196: invokevirtual 51	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   199: invokeinterface 198 3 0
    //   204: pop
    //   205: aload_3
    //   206: aload 9
    //   208: invokeinterface 120 2 0
    //   213: pop
    //   214: goto -151 -> 63
    //   217: astore 7
    //   219: aload 7
    //   221: invokevirtual 59	java/lang/Exception:printStackTrace	()V
    //   224: aload 5
    //   226: ifnull -137 -> 89
    //   229: aload 5
    //   231: invokeinterface 94 1 0
    //   236: aload_3
    //   237: areturn
    //   238: astore 6
    //   240: aload 5
    //   242: ifnull +10 -> 252
    //   245: aload 5
    //   247: invokeinterface 94 1 0
    //   252: aload 6
    //   254: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	255	0	this	ItemTableAccess
    //   0	255	1	paramString1	String
    //   0	255	2	paramString2	String
    //   7	230	3	localArrayList	java.util.ArrayList
    //   46	10	4	str	String
    //   49	197	5	localCursor	Cursor
    //   238	15	6	localObject	Object
    //   217	3	7	localException	Exception
    //   70	3	8	bool	boolean
    //   98	109	9	localHashMap	HashMap
    // Exception table:
    //   from	to	target	type
    //   51	63	217	java/lang/Exception
    //   63	72	217	java/lang/Exception
    //   91	214	217	java/lang/Exception
    //   51	63	238	finally
    //   63	72	238	finally
    //   91	214	238	finally
    //   219	224	238	finally
  }
  
  /* Error */
  public java.util.List<Map<String, String>> findSyncItem()
  {
    // Byte code:
    //   0: new 102	java/util/ArrayList
    //   3: dup
    //   4: invokespecial 103	java/util/ArrayList:<init>	()V
    //   7: astore_1
    //   8: aconst_null
    //   9: astore_2
    //   10: aload_0
    //   11: getfield 23	com/aalife/android/ItemTableAccess:db	Landroid/database/sqlite/SQLiteDatabase;
    //   14: ldc_w 450
    //   17: aconst_null
    //   18: invokevirtual 67	android/database/sqlite/SQLiteDatabase:rawQuery	(Ljava/lang/String;[Ljava/lang/String;)Landroid/database/Cursor;
    //   21: astore_2
    //   22: aload_2
    //   23: invokeinterface 108 1 0
    //   28: istore 5
    //   30: iload 5
    //   32: ifne +15 -> 47
    //   35: aload_2
    //   36: ifnull +9 -> 45
    //   39: aload_2
    //   40: invokeinterface 94 1 0
    //   45: aload_1
    //   46: areturn
    //   47: new 177	java/util/HashMap
    //   50: dup
    //   51: invokespecial 178	java/util/HashMap:<init>	()V
    //   54: astore 6
    //   56: aload 6
    //   58: ldc 216
    //   60: iconst_1
    //   61: aload_2
    //   62: invokeinterface 220 1 0
    //   67: iadd
    //   68: invokestatic 225	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   71: invokeinterface 198 3 0
    //   76: pop
    //   77: aload 6
    //   79: ldc_w 337
    //   82: aload_2
    //   83: iconst_0
    //   84: invokeinterface 91 2 0
    //   89: invokeinterface 198 3 0
    //   94: pop
    //   95: aload 6
    //   97: ldc_w 281
    //   100: aload_2
    //   101: iconst_1
    //   102: invokeinterface 91 2 0
    //   107: invokeinterface 198 3 0
    //   112: pop
    //   113: aload 6
    //   115: ldc_w 303
    //   118: aload_2
    //   119: iconst_2
    //   120: invokeinterface 184 2 0
    //   125: invokestatic 453	java/lang/String:valueOf	(D)Ljava/lang/String;
    //   128: invokeinterface 198 3 0
    //   133: pop
    //   134: aload 6
    //   136: ldc 200
    //   138: aload_2
    //   139: iconst_3
    //   140: invokeinterface 91 2 0
    //   145: invokeinterface 198 3 0
    //   150: pop
    //   151: aload 6
    //   153: ldc_w 328
    //   156: aload_2
    //   157: iconst_4
    //   158: invokeinterface 91 2 0
    //   163: invokeinterface 198 3 0
    //   168: pop
    //   169: aload 6
    //   171: ldc_w 339
    //   174: aload_2
    //   175: iconst_5
    //   176: invokeinterface 91 2 0
    //   181: invokeinterface 198 3 0
    //   186: pop
    //   187: aload 6
    //   189: ldc_w 394
    //   192: aload_2
    //   193: bipush 6
    //   195: invokeinterface 91 2 0
    //   200: invokeinterface 198 3 0
    //   205: pop
    //   206: aload_1
    //   207: aload 6
    //   209: invokeinterface 120 2 0
    //   214: pop
    //   215: goto -193 -> 22
    //   218: astore 4
    //   220: aload 4
    //   222: invokevirtual 59	java/lang/Exception:printStackTrace	()V
    //   225: aload_2
    //   226: ifnull -181 -> 45
    //   229: aload_2
    //   230: invokeinterface 94 1 0
    //   235: aload_1
    //   236: areturn
    //   237: astore_3
    //   238: aload_2
    //   239: ifnull +9 -> 248
    //   242: aload_2
    //   243: invokeinterface 94 1 0
    //   248: aload_3
    //   249: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	250	0	this	ItemTableAccess
    //   7	229	1	localArrayList	java.util.ArrayList
    //   9	234	2	localCursor	Cursor
    //   237	12	3	localObject	Object
    //   218	3	4	localException	Exception
    //   28	3	5	bool	boolean
    //   54	154	6	localHashMap	HashMap
    // Exception table:
    //   from	to	target	type
    //   10	22	218	java/lang/Exception
    //   22	30	218	java/lang/Exception
    //   47	215	218	java/lang/Exception
    //   10	22	237	finally
    //   22	30	237	finally
    //   47	215	237	finally
    //   220	225	237	finally
  }
  
  public void fixSyncStatus()
  {
    try
    {
      this.db.execSQL("UPDATE ItemTable SET Synchronize = '1'");
      return;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
  
  public int getItemId(int paramInt)
  {
    Cursor localCursor = null;
    try
    {
      localCursor = this.db.rawQuery("SELECT ItemID FROM ItemTable WHERE ItemWebID = " + paramInt, null);
      boolean bool = localCursor.moveToFirst();
      i = 0;
      if (bool)
      {
        int j = localCursor.getInt(0);
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
  
  public boolean hasSyncItem()
  {
    Cursor localCursor = null;
    do
    {
      try
      {
        localCursor = this.db.rawQuery("SELECT COUNT(0) FROM ItemTable WHERE Synchronize = '1'", null);
        if (localCursor.moveToFirst())
        {
          int i = localCursor.getInt(0);
          bool = false;
          if (i > 0) {
            bool = true;
          }
          return bool;
        }
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
        bool = false;
        return false;
      }
      finally
      {
        if (localCursor != null) {
          localCursor.close();
        }
      }
      boolean bool = false;
    } while (localCursor == null);
    localCursor.close();
    return false;
  }
  
  /* Error */
  public boolean restoreDataBase(java.util.List<java.lang.CharSequence> paramList)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 23	com/aalife/android/ItemTableAccess:db	Landroid/database/sqlite/SQLiteDatabase;
    //   4: invokevirtual 148	android/database/sqlite/SQLiteDatabase:beginTransaction	()V
    //   7: aload_1
    //   8: invokeinterface 466 1 0
    //   13: astore 4
    //   15: aload 4
    //   17: invokeinterface 471 1 0
    //   22: ifne +19 -> 41
    //   25: aload_0
    //   26: getfield 23	com/aalife/android/ItemTableAccess:db	Landroid/database/sqlite/SQLiteDatabase;
    //   29: invokevirtual 163	android/database/sqlite/SQLiteDatabase:setTransactionSuccessful	()V
    //   32: aload_0
    //   33: getfield 23	com/aalife/android/ItemTableAccess:db	Landroid/database/sqlite/SQLiteDatabase;
    //   36: invokevirtual 166	android/database/sqlite/SQLiteDatabase:endTransaction	()V
    //   39: iconst_1
    //   40: ireturn
    //   41: aload_0
    //   42: getfield 23	com/aalife/android/ItemTableAccess:db	Landroid/database/sqlite/SQLiteDatabase;
    //   45: aload 4
    //   47: invokeinterface 475 1 0
    //   52: checkcast 477	java/lang/CharSequence
    //   55: invokeinterface 478 1 0
    //   60: invokevirtual 56	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
    //   63: goto -48 -> 15
    //   66: astore_3
    //   67: aload_3
    //   68: invokevirtual 59	java/lang/Exception:printStackTrace	()V
    //   71: aload_0
    //   72: getfield 23	com/aalife/android/ItemTableAccess:db	Landroid/database/sqlite/SQLiteDatabase;
    //   75: invokevirtual 166	android/database/sqlite/SQLiteDatabase:endTransaction	()V
    //   78: iconst_0
    //   79: ireturn
    //   80: astore_2
    //   81: aload_0
    //   82: getfield 23	com/aalife/android/ItemTableAccess:db	Landroid/database/sqlite/SQLiteDatabase;
    //   85: invokevirtual 166	android/database/sqlite/SQLiteDatabase:endTransaction	()V
    //   88: aload_2
    //   89: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	90	0	this	ItemTableAccess
    //   0	90	1	paramList	java.util.List<java.lang.CharSequence>
    //   80	9	2	localObject	Object
    //   66	2	3	localException	Exception
    //   13	33	4	localIterator	java.util.Iterator
    // Exception table:
    //   from	to	target	type
    //   7	15	66	java/lang/Exception
    //   15	32	66	java/lang/Exception
    //   41	63	66	java/lang/Exception
    //   7	15	80	finally
    //   15	32	80	finally
    //   41	63	80	finally
    //   67	71	80	finally
  }
  
  public boolean updateItem(int paramInt1, String paramString1, String paramString2, String paramString3, int paramInt2)
  {
    String str = "UPDATE ItemTable SET ItemName = '" + paramString1 + "', ItemPrice = '" + paramString2 + "', ItemBuyDate = '" + paramString3 + "', CategoryID = '" + paramInt2 + "', Synchronize = '1'" + " WHERE ItemID = " + paramInt1;
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
  
  public boolean updateItemRecommend(int paramInt1, int paramInt2)
  {
    String str = "UPDATE ItemTable SET Recommend = '" + paramInt2 + "', Synchronize = '1' WHERE ItemID = " + paramInt1;
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
  
  public void updateSyncStatus()
  {
    try
    {
      this.db.execSQL("UPDATE ItemTable SET Synchronize = '0'");
      return;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
  
  public void updateSyncStatus(int paramInt1, int paramInt2)
  {
    String str = "UPDATE ItemTable SET Synchronize = '0', ItemWebID = '" + paramInt2 + "' WHERE ItemID = " + paramInt1;
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
 * Qualified Name:     com.aalife.android.ItemTableAccess
 * JD-Core Version:    0.7.0-SNAPSHOT-20130630
 */