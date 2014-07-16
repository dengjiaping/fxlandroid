package com.aalife.android;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper
  extends SQLiteOpenHelper
{
  private static final String CATTABNAME = "CategoryTable";
  private static final String DATABASENAME = "aalife.db";
  private static final int DATABASEVERSION = 15;
  private static final String DELTABNAME = "DeleteTable";
  private static final String ITEMTABNAME = "ItemTable";
  
  public DatabaseHelper(Context paramContext)
  {
    super(paramContext, "aalife.db", null, 15);
  }
  
  public void onCreate(SQLiteDatabase paramSQLiteDatabase)
  {
    paramSQLiteDatabase.execSQL("CREATE TABLE ItemTable (ItemID         INTEGER          PRIMARY KEY, ItemWebID      INTEGER          DEFAULT 0, ItemName       VARCHAR(20)      NOT NULL, ItemPrice      REAL             NOT NULL, ItemBuyDate    DATE             NOT NULL, CategoryID     INTEGER          NOT NULL, Synchronize    INTEGER          DEFAULT 1, Recommend      INTEGER          DEFAULT 0, Remark         VARCHAR(1000))");
    paramSQLiteDatabase.execSQL("CREATE TABLE CategoryTable (CategoryID       INTEGER          NOT NULL, CategoryName     VARCHAR(20)      NOT NULL, Synchronize      INTEGER          DEFAULT 0, CategoryRank     INTEGER, CategoryDisplay  INTEGER          DEFAULT 1, CategoryLive     INTEGER          DEFAULT 1, IsDefault        INTEGER          DEFAULT 0)");
    paramSQLiteDatabase.execSQL("INSERT INTO CategoryTable(CategoryID, CategoryName, CategoryRank, IsDefault) SELECT '1', '菜米油盐酱醋', '1', '1' UNION ALL SELECT '2', '外就餐', '2', '1' UNION ALL SELECT '3', '烟酒水', '3', '1' UNION ALL SELECT '4', '零食', '4', '1' UNION ALL SELECT '5', '水果', '5', '1' UNION ALL SELECT '6', '日用品', '6', '1' UNION ALL SELECT '7', '电子产品', '7', '1' UNION ALL SELECT '8', '衣裤鞋', '8', '1' UNION ALL SELECT '9', '交费', '9', '1' UNION ALL SELECT '10', '娱乐', '10', '1'");
    paramSQLiteDatabase.execSQL("CREATE TABLE DeleteTable (DeleteID     INTEGER          PRIMARY KEY, ItemID       INTEGER, ItemWebID    INTEGER)");
  }
  
  public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2)
  {
    paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS CategoryTable");
    paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS ItemTable");
    paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS DeleteTable");
    onCreate(paramSQLiteDatabase);
  }
}


/* Location:           D:\fxlandroid\document\app\AALifeNew_v3.1.4\classes_dex2jar.jar
 * Qualified Name:     com.aalife.android.DatabaseHelper
 * JD-Core Version:    0.7.0-SNAPSHOT-20130630
 */