package com.aalife.android;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASENAME = "aalife.db";
	private static final int DATABASEVERSION = 12;
	private static final String TABLENAME = "ItemTable";
	private static final String CATTABLENAME = "CategoryTable";

	public MyDatabaseHelper(Context context) {
		super(context, DATABASENAME, null, DATABASEVERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "CREATE TABLE " + TABLENAME + " ("
				+ "ItemID         INTEGER       PRIMARY KEY, "
				+ "ItemName       VARCHAR(20)   NOT NULL, "
				+ "ItemPrice      REAL          NOT NULL, "
				+ "ItemBuyDate    DATE          NOT NULL, "
				+ "CategoryID     INTEGER       NOT NULL)";
		String sql1 = "CREATE TABLE " + CATTABLENAME + " ("
				+ "CategoryID     INTEGER       PRIMARY KEY, "
				+ "CategoryName   VARCHAR(20)   NOT NULL)";
		String sql2 = "INSERT INTO " + CATTABLENAME + "(CategoryName) "
				+ "SELECT '菜米油盐酱醋' UNION ALL " + "SELECT '外就餐' UNION ALL "
				+ "SELECT '烟酒水' UNION ALL " + "SELECT '零食' UNION ALL "
				+ "SELECT '水果' UNION ALL " + "SELECT '日用品' UNION ALL "
				+ "SELECT '电子产品' UNION ALL " + "SELECT '衣裤鞋' UNION ALL "
				+ "SELECT '交费' UNION ALL " + "SELECT '娱乐'";
		db.execSQL(sql);
		db.execSQL(sql1);
		db.execSQL(sql2);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql = "DROP TABLE IF EXISTS " + CATTABLENAME;
		String sql1 = "DROP TABLE IF EXISTS " + TABLENAME;
		db.execSQL(sql);
		db.execSQL(sql1);
		this.onCreate(db);
	}
}
