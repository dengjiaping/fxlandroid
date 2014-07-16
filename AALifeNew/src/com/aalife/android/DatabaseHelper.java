package com.aalife.android;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASENAME = "aalife.db";
	private static final int DATABASEVERSION = 16;
	private static final String ITEMTABNAME = "ItemTable";
	private static final String CATTABNAME = "CategoryTable";
	private static final String DELTABNAME = "DeleteTable";

	public DatabaseHelper(Context context) {
		super(context, DATABASENAME, null, DATABASEVERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + ITEMTABNAME + " ("
				+ "ItemID         INTEGER          PRIMARY KEY, "
				+ "ItemWebID      INTEGER          DEFAULT 0, "
				+ "ItemName       VARCHAR(20)      NOT NULL, "
				+ "ItemPrice      REAL             NOT NULL, "
				+ "ItemBuyDate    DATE             NOT NULL, "
				+ "CategoryID     INTEGER          NOT NULL, "
				+ "Synchronize    INTEGER          DEFAULT 1, "
				+ "Recommend      INTEGER          DEFAULT 0, "
				+ "RegionID       INTEGER          DEFAULT 0, "
				+ "Remark         VARCHAR(1000))");
		db.execSQL("CREATE TABLE " + CATTABNAME + " ("
				+ "CategoryID       INTEGER          NOT NULL, "
				+ "CategoryName     VARCHAR(20)      NOT NULL, "
				+ "Synchronize      INTEGER          DEFAULT 0, "
				+ "CategoryRank     INTEGER, "
				+ "CategoryDisplay  INTEGER          DEFAULT 1, "
				+ "CategoryLive     INTEGER          DEFAULT 1, "
				+ "IsDefault        INTEGER          DEFAULT 0)");
		db.execSQL("INSERT INTO " + CATTABNAME + "(CategoryID, CategoryName, CategoryRank, IsDefault) "
				+ "SELECT '1', '菜米油盐酱醋', '1', '1' UNION ALL " 
				+ "SELECT '2', '外就餐', '2', '1' UNION ALL "
				+ "SELECT '3', '烟酒水', '3', '1' UNION ALL " 
				+ "SELECT '4', '零食', '4', '1' UNION ALL "
				+ "SELECT '5', '水果', '5', '1' UNION ALL " 
				+ "SELECT '6', '日用品', '6', '1' UNION ALL "
				+ "SELECT '7', '电子产品', '7', '1' UNION ALL " 
				+ "SELECT '8', '衣裤鞋', '8', '1' UNION ALL "
				+ "SELECT '9', '交费', '9', '1' UNION ALL " 
				+ "SELECT '10', '娱乐', '10', '1'");
		db.execSQL("CREATE TABLE " + DELTABNAME + " ("
				+ "DeleteID     INTEGER          PRIMARY KEY, "
				+ "ItemID       INTEGER, "
				+ "ItemWebID    INTEGER)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//db.execSQL("DROP TABLE IF EXISTS " + CATTABNAME);
		//db.execSQL("DROP TABLE IF EXISTS " + ITEMTABNAME);
		//db.execSQL("DROP TABLE IF EXISTS " + DELTABNAME);
		//onCreate(db);
		
		if(oldVersion == 15 && newVersion == 16) {
		    db.execSQL("ALTER TABLE " + ITEMTABNAME + " ADD RegionID INTEGER DEFAULT 0");
		}
	}
}
