package com.aalife.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CategoryTableAccess {
	private static final String TABLENAME = "ItemTable";
	private static final String CATTABLENAME = "CategoryTable";
	private SQLiteDatabase db = null;

	public CategoryTableAccess(SQLiteDatabase db) {
		this.db = db;
	}
	
	// 更新
	public void update(int CategoryID, String CategoryName) {
		String sql = "UPDATE " + CATTABLENAME + " SET CategoryName='" + CategoryName + "'" + " WHERE CategoryID = '" + CategoryID + "'";
		this.db.execSQL(sql);
		this.db.close();
	}
		
	// 查统计
	public List<Map<String, String>> findAllWithTongJiByMonth(String date) {
		List<Map<String, String>> all = new ArrayList<Map<String, String>>();
		String sql = "SELECT ct.CategoryID, ct.CategoryName, SUM(ItemPrice) AS ItemPrice FROM " + CATTABLENAME + " AS ct "
				+ "LEFT JOIN (SELECT * FROM " + TABLENAME + " WHERE STRFTIME('%Y%m', ItemBuyDate) = STRFTIME('%Y%m', '" + date + "')) AS it "
				+ "ON ct.CategoryID = it.CategoryID GROUP BY ct.CategoryID, ct.CategoryName ORDER BY ItemPrice DESC, ct.CategoryID ASC";
		Cursor result = this.db.rawQuery(sql, null);
		while (result.moveToNext()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("idtext", result.getString(0));
			map.put("categorytext", result.getString(1));
			map.put("totalpricetext", result.getDouble(2) > 0 ? "￥ " + UtilityHelper.formatDouble(result.getDouble(2), "0.0##") : "0");
			all.add(map);
		}
		this.db.close();
		return all;
	}

	// 查所有分类
	public List<CharSequence> findAllCategory() {
		List<CharSequence> all = new ArrayList<CharSequence>();
		String sql = "SELECT ct.CategoryName FROM " + CATTABLENAME + " AS ct "
				+ "LEFT JOIN (SELECT COUNT(CategoryID) AS CategoryCount, CategoryID FROM " + TABLENAME + " GROUP BY CategoryID) AS it "
				+ "ON ct.CategoryID = it.CategoryID ORDER BY it.CategoryCount DESC, ct.CategoryID ASC";
		Cursor result = this.db.rawQuery(sql, null);
		while (result.moveToNext()) {
			all.add(result.getString(0));
		}
		this.db.close();
		return all;
	}
	
	// 根据名称返回ID
	public int findCategoryIDByName(String name) {
		int categoryID = 0; 
		String sql = "SELECT CategoryID FROM " + CATTABLENAME + " WHERE CategoryName = '" + name + "'";
		Cursor result = this.db.rawQuery(sql, null);
		while (result.moveToNext()) {
			categoryID = Integer.parseInt(result.getString(0));
		}
		this.db.close();
		return categoryID;
	}
}
