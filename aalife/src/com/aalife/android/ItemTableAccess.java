package com.aalife.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ItemTableAccess {
	private static final String TABLENAME = "ItemTable";
	private static final String CATTABLENAME = "CategoryTable";
	private SQLiteDatabase db = null;

	public ItemTableAccess(SQLiteDatabase db) {
		this.db = db;
	}

	// 插入
	public void insert(String ItemName, String ItemPrice, String ItemBuyDate, int CategoryID) {
		String sql = "INSERT INTO " + TABLENAME + "(ItemID, ItemName, ItemPrice, ItemBuyDate, CategoryID) "
				+ "VALUES (null, '" + ItemName + "', '" + ItemPrice + "', '" + ItemBuyDate + "', '" + CategoryID + "')";
		this.db.execSQL(sql);
		this.db.close();
	}

	// 更新
	public void update(int ItemID, String ItemName, String ItemPrice, String ItemBuyDate, int CategoryID) {
		String sql = "UPDATE " + TABLENAME + " SET ItemName = '" + ItemName + "', ItemPrice = '" + ItemPrice + "', ItemBuyDate = '" + ItemBuyDate + "', CategoryID = '" + CategoryID + "'" + " WHERE ItemID = '" + ItemID + "'";
		this.db.execSQL(sql);
		this.db.close();
	}

	// 删除
	public void delete(int ItemID) {
		String sql = "DELETE FROM " + TABLENAME + " WHERE ItemID = '" + ItemID + "'";
		this.db.execSQL(sql);
		this.db.close();
	}

	// 查所有名称
	public List<CharSequence> findAllItemName() {
		List<CharSequence> all = new ArrayList<CharSequence>();
		String sql = "SELECT ItemName FROM " + TABLENAME + " GROUP BY ItemName ORDER BY ItemName";
		Cursor result = this.db.rawQuery(sql, null);
		while (result.moveToNext()) {
			all.add(result.getString(0));
		}
		this.db.close();
		return all;
	}

	// 查一条记录
	public String[] findItemById(int ItemID) {
		String[] items = new String[5];
		String sql = "SELECT it.ItemID, it.ItemName, it.ItemPrice, it.ItemBuyDate, ct.CategoryName FROM " + TABLENAME + " AS it " +
				"LEFT JOIN " + CATTABLENAME + " AS ct ON it.CategoryID = ct.CategoryID WHERE it.ItemID = '" + ItemID + "'";
		Cursor result = this.db.rawQuery(sql, null);
		while (result.moveToNext()) {
			items[0] = result.getString(0);
			items[1] = result.getString(1);
			items[2] = UtilityHelper.formatDouble(result.getDouble(2), "0.###");
			items[3] = result.getString(3);
			items[4] = result.getString(4);
		}
		this.db.close();
		return items;
	}
	
	// 查所有消费
	public ArrayList<Map<String, String>> findAllItemMonth() {
		ArrayList<Map<String, String>> all = new ArrayList<Map<String, String>>();
		String sql = "SELECT ItemBuyDate, SUM(ItemPrice) FROM " + TABLENAME + " GROUP BY ItemBuyDate ORDER BY ItemBuyDate DESC";
		Cursor result = this.db.rawQuery(sql, null);
		while (result.moveToNext()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("itembuydate", result.getString(0));
			map.put("itemprice", UtilityHelper.formatDouble(result.getDouble(1), "0.0##"));
			all.add(map);
		}
		this.db.close();
		return all;
	}

	// 查日消费
	public ArrayList<Map<String, String>> findAllByDate(String date) {
		ArrayList<Map<String, String>> all = new ArrayList<Map<String, String>>();
		String sql = "SELECT ItemID, ItemName, ItemPrice, ItemBuyDate FROM " + TABLENAME + " WHERE ItemBuyDate = '" + date + "'";
		Cursor result = this.db.rawQuery(sql, null);
		while (result.moveToNext()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("itemid", result.getString(0));
			map.put("itemname", result.getString(1));
			map.put("itemprice", "￥ " + UtilityHelper.formatDouble(result.getDouble(2), "0.0##"));
			all.add(map);
		}
		this.db.close();
		return all;
	}

	// 查日消费
	public List<Map<String, String>> findAllWithDayByDate(String date) {
		List<Map<String, String>> all = new ArrayList<Map<String, String>>();
		String sql = "SELECT ItemID, ItemName, ItemPrice FROM " + TABLENAME + " WHERE ItemBuyDate = '" + date + "'";
		Cursor result = this.db.rawQuery(sql, null);
		while (result.moveToNext()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", String.valueOf(result.getPosition() + 1));
			map.put("itemid", result.getString(0));
			map.put("itemname", result.getString(1));
			map.put("itemprice", UtilityHelper.formatDouble(result.getDouble(2), "0.0##"));
			all.add(map);
		}
		this.db.close();
		return all;
	}

	// 查日总消费
	public String findAllTotalWithDayByDate(String date) {
		String str = null;
		String sql = "SELECT SUM(ItemPrice) FROM " + TABLENAME + " WHERE ItemBuyDate = '" + date + "'";
		Cursor result = this.db.rawQuery(sql, null);
		while (result.moveToNext()) {
			str = UtilityHelper.formatDouble(result.getDouble(0), "0.0##");
		}
		this.db.close();
		return "☆☆  总计：" + str + "  ☆☆";
	}

	// 查所有月总消费
	public List<Map<String, String>> findAllByMonthTotal() {
		List<Map<String, String>> all = new ArrayList<Map<String, String>>();
		String sql = "SELECT STRFTIME('%Y-%m', ItemBuyDate) AS ItemBuyDate, SUM(ItemPrice) FROM " + TABLENAME
				+ " GROUP BY ItemBuyDate ORDER BY ItemBuyDate DESC";
		Cursor result = this.db.rawQuery(sql, null);
		while (result.moveToNext()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("monthtext", result.getString(0));
			map.put("monthpricetext", UtilityHelper.formatDouble(result.getDouble(1), "0.0##"));
			all.add(map);
		}
		this.db.close();
		return all;
	}

	// 查所有总消费
	public String findAll() {
		String str = null;
		String sql = "SELECT SUM(ItemPrice) FROM " + TABLENAME;
		Cursor result = this.db.rawQuery(sql, null);
		while (result.moveToNext()) {
			str = UtilityHelper.formatDouble(result.getDouble(0), "0.0##");
		}
		this.db.close();
		return str;
	}

	// 查月消费
	public List<Map<String, String>> findAllByMonth(String date) {
		List<Map<String, String>> all = new ArrayList<Map<String, String>>();
		String sql = "SELECT ItemBuyDate, SUM(ItemPrice) FROM " + TABLENAME
				+ " GROUP BY ItemBuyDate HAVING STRFTIME('%Y-%m', ItemBuyDate) = '" + date + "' ORDER BY ItemBuyDate DESC";
		Cursor result = this.db.rawQuery(sql, null);
		while (result.moveToNext()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("monthtext", result.getString(0));
			map.put("monthpricetext", UtilityHelper.formatDouble(result.getDouble(1), "0.0##"));
			all.add(map);
		}
		this.db.close();
		return all;
	}

	// 查月总消费
	public String findAllTotalByMonth(String date) {
		String str = null;
		String sql = "SELECT SUM(ItemPrice) FROM " + TABLENAME
				+ " WHERE STRFTIME('%Y-%m', ItemBuyDate) = STRFTIME('%Y-%m', '" + date + "')";
		Cursor result = this.db.rawQuery(sql, null);
		while (result.moveToNext()) {
			str = UtilityHelper.formatDouble(result.getDouble(0), "0.0##");
		}
		this.db.close();
		return str;
	}

	// 查消费单价排名
	public List<Map<String, String>> findAllWithRankByMonth(String date) {
		List<Map<String, String>> all = new ArrayList<Map<String, String>>();
		String sql = "SELECT ItemName, STRFTIME('%m-%d', ItemBuyDate), ItemPrice FROM " + TABLENAME
				+ " WHERE STRFTIME('%Y-%m', ItemBuyDate) = STRFTIME('%Y-%m', '" + date + "') ORDER BY ItemPrice DESC, ItemBuyDate DESC";
		Cursor result = this.db.rawQuery(sql, null);
		while (result.moveToNext()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", String.valueOf(result.getPosition() + 1));
			map.put("itemname", result.getString(0));
			map.put("itembuydate", result.getString(1));
			map.put("itemprice", "￥ " + UtilityHelper.formatDouble(result.getDouble(2), "0.0##"));
			all.add(map);
		}
		this.db.close();
		return all;
	}

	// 查商品排名
	public List<Map<String, String>> findAllWithRankByName(String date, String name) {
		List<Map<String, String>> all = new ArrayList<Map<String, String>>();
		String sql = "SELECT ItemName, STRFTIME('%m-%d', ItemBuyDate), ItemPrice FROM " + TABLENAME
				+ " WHERE STRFTIME('%Y-%m', ItemBuyDate) = STRFTIME('%Y-%m', '" + date + "') AND ItemName = '" + name + "' ORDER BY ItemBuyDate DESC";
		Cursor result = this.db.rawQuery(sql, null);
		while (result.moveToNext()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", String.valueOf(result.getPosition() + 1));
			map.put("itemname", result.getString(0));
			map.put("itembuydate", result.getString(1));
			map.put("itemprice", "￥ " + UtilityHelper.formatDouble(result.getDouble(2), "0.0##"));
			all.add(map);
		}
		this.db.close();
		return all;
	}
	
	// 查商品总消费
	public String findAllTotalWithRankByName(String date, String name) {
		String str = null;
		String sql = "SELECT SUM(ItemPrice) FROM " + TABLENAME
				+ " WHERE STRFTIME('%Y-%m', ItemBuyDate) = STRFTIME('%Y-%m', '" + date + "') AND ItemName = '" + name + "'";
		Cursor result = this.db.rawQuery(sql, null);
		while (result.moveToNext()) {
			str = UtilityHelper.formatDouble(result.getDouble(0), "0.0##");
		}
		this.db.close();
		return "☆☆  总计：" + str + "  ☆☆";
	}
	
	// 查消费次数排名
	public List<Map<String, String>> findAllWithRankItemByMonth(String date) {
		List<Map<String, String>> all = new ArrayList<Map<String, String>>();
		String sql = "SELECT ItemName, SUM(ItemPrice), COUNT(ItemName) AS ItemNameCount FROM " + TABLENAME
				+ " WHERE STRFTIME('%Y-%m', ItemBuyDate) = STRFTIME('%Y-%m', '" + date + "') GROUP BY ItemName ORDER BY ItemNameCount DESC, ItemPrice DESC";
		Cursor result = this.db.rawQuery(sql, null);
		while (result.moveToNext()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", String.valueOf(result.getPosition() + 1));
			map.put("itemname", result.getString(0));
			map.put("itemprice", "￥ " + UtilityHelper.formatDouble(result.getDouble(1), "0.0##"));
			map.put("itemnamecount", result.getString(2));
			all.add(map);
		}
		this.db.close();
		return all;
	}
	
	// 备份数据
	public List<CharSequence> backupItemTable() {
		List<CharSequence> all = new ArrayList<CharSequence>();
		String sql = "SELECT ItemName, ItemPrice, ItemBuyDate, CategoryID FROM " + TABLENAME;
		Cursor result = this.db.rawQuery(sql, null);
		while (result.moveToNext()) {
			all.add("INSERT INTO " + TABLENAME + "(ItemID, ItemName, ItemPrice, ItemBuyDate, CategoryID)"
					+ " VALUES(null, '" + result.getString(0)+ "', '" + result.getString(1)
					+ "', '" + result.getString(2) + "', '" + result.getString(3) + "');");
		}
		this.db.close();
		return all;
	}
	
	// 恢复数据
	public boolean restoreItemTable(List<CharSequence> list) {
		this.db.beginTransaction();
		this.db.execSQL("DELETE FROM " + TABLENAME);
		try {
			for(int i=0; i<list.size(); i++) {
				String sql = list.get(i).toString();
				this.db.execSQL(sql);
			}
			this.db.setTransactionSuccessful();
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			this.db.endTransaction();
			this.db.close();
		}
		return true;
	}
}
