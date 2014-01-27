package com.aalife.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ItemTableAccess {
	private static final String ITEMTABNAME = "ItemTable";
	private static final String DELTABNAME = "DeleteTable";
	private SQLiteDatabase db = null;

	public ItemTableAccess(SQLiteDatabase db) {
		this.db = db;
	}

	//根据日期查消费
	public List<Map<String, String>> findItemByDate(String date) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = "SELECT ItemID, ItemName, ItemPrice, ItemBuyDate, CategoryID FROM " + ITEMTABNAME + " WHERE ItemBuyDate = '" + date + "'";
		try {
			Cursor result = this.db.rawQuery(sql, null);
			while (result.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				//map.put("id", String.valueOf(result.getPosition() + 1));
				map.put("id", "");
				map.put("itemid", result.getString(0));
				map.put("itemname", result.getString(1));
				map.put("itemprice", "￥" + UtilityHelper.formatDouble(result.getDouble(2), "0.0##"));
				map.put("itempricevalue", UtilityHelper.formatDouble(result.getDouble(2), "0.0##"));
				map.put("itembuydate", result.getString(3));
				map.put("catid", result.getString(4));
				list.add(map);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
			
		return list;
	}
	
	//根据日期查月消费
	public List<Map<String, String>> findMonthByDate(String date) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = "SELECT SUM(ItemPrice), ItemBuyDate FROM " + ITEMTABNAME
				   + " GROUP BY ItemBuyDate HAVING STRFTIME('%Y-%m', ItemBuyDate) = STRFTIME('%Y-%m', '" + date + "')";
		try {
			Cursor result = this.db.rawQuery(sql, null);
			while (result.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", String.valueOf(result.getPosition() + 1));
				map.put("price", "￥" + UtilityHelper.formatDouble(result.getDouble(0), "0.0##"));
				map.put("pricevalue", UtilityHelper.formatDouble(result.getDouble(0), "0.0##"));
				map.put("date", UtilityHelper.formatDate(result.getString(1), "m-d"));
				map.put("datevalue", UtilityHelper.formatDate(result.getString(1), "y-m-d"));
				list.add(map);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
			
		return list;
	}
	
	//根据日期查首页统计
	public List<Map<String, String>> findHomeTotalByDate(String curDate) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = "SELECT a.*, b.* FROM "
				   + "(SELECT SUM(ItemPrice) AS Price, '今日' AS Label, 1 AS Flag FROM " + ITEMTABNAME
				   + " WHERE STRFTIME('%Y-%m-%d', ItemBuyDate) = STRFTIME('%Y-%m-%d', '" + curDate + "')) AS a"
				   + " INNER JOIN "
				   + "(SELECT SUM(ItemPrice) AS Price, '昨日' AS Label, 1 AS Flag FROM " + ITEMTABNAME
				   + " WHERE STRFTIME('%Y-%m-%d', ItemBuyDate) = STRFTIME('%Y-%m-%d', DATE('" + curDate + "','start of day','-1 day'))) AS b"
				   + " ON a.Flag = b.Flag"
				   + " UNION "
				   + "SELECT a.*, b.* FROM "
				   + "(SELECT SUM(ItemPrice) AS Price, '本月' AS Label, 1 AS Flag FROM " + ITEMTABNAME
				   + " WHERE STRFTIME('%Y-%m', ItemBuyDate) = STRFTIME('%Y-%m', '" + curDate + "')) AS a"
				   + " INNER JOIN "
				   + "(SELECT SUM(ItemPrice) AS Price, '上月' AS Label, 1 AS Flag FROM " + ITEMTABNAME
				   + " WHERE STRFTIME('%Y-%m', ItemBuyDate) = STRFTIME('%Y-%m', DATE('" + curDate + "','start of month','-1 month'))) AS b"
				   + " ON a.Flag = b.Flag"		
				   + " UNION "
				   + "SELECT a.*, b.* FROM "
				   + "(SELECT SUM(ItemPrice) AS Price, '今年' AS Label, 1 AS Flag FROM " + ITEMTABNAME
				   + " WHERE STRFTIME('%Y', ItemBuyDate) = STRFTIME('%Y', '" + curDate + "')) AS a"
				   + " INNER JOIN "
				   + "(SELECT SUM(ItemPrice) AS Price, '去年' AS Label, 1 AS Flag FROM " + ITEMTABNAME
				   + " WHERE STRFTIME('%Y', ItemBuyDate) = STRFTIME('%Y', DATE('" + curDate + "','start of year','-1 year'))) AS b"
				   + " ON a.Flag = b.Flag";
		try {
			Cursor result = this.db.rawQuery(sql, null);
			while (result.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("curprice", "￥" + UtilityHelper.formatDouble(result.getDouble(0), "0.0##"));
				map.put("curlabel", result.getString(1));
				map.put("prevprice", "￥" + UtilityHelper.formatDouble(result.getDouble(3), "0.0##"));
				map.put("prevlabel", result.getString(4));
				list.add(map);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
			
		return list;
	}

	//查同步消费
	public List<Map<String, String>> findSyncItem() {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = "SELECT ItemID, ItemName, ItemPrice, ItemBuyDate, CategoryID, ItemWebID FROM " + ITEMTABNAME + " WHERE Synchronize = '1'";
		try {
			Cursor result = this.db.rawQuery(sql, null);
			while (result.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", String.valueOf(result.getPosition() + 1));
				map.put("itemid", result.getString(0));
				map.put("itemname", result.getString(1));
				map.put("itemprice", String.valueOf(result.getDouble(2)));
				map.put("itembuydate", result.getString(3));
				map.put("catid", result.getString(4));
				map.put("itemwebid", result.getString(5));
				list.add(map);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	//查同步删除消费
	public List<Map<String, String>> findDelSyncItem() {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = "SELECT ItemID, ItemWebID FROM " + DELTABNAME;
		try {
			Cursor result = this.db.rawQuery(sql, null);
			while (result.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("itemid", result.getString(0));
				map.put("itemwebid", result.getString(1));
				list.add(map);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

	//添加消费
	public boolean addItem(String itemName, String itemPrice, String itemBuyDate, int catId) {
		String sql = "INSERT INTO " + ITEMTABNAME + "(ItemName, ItemPrice, ItemBuyDate, CategoryID) "
			   	   + "VALUES ('" + itemName + "', '" + itemPrice + "', '" + itemBuyDate + "', '" + catId + "')";
		try {
		    this.db.execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	//添加网络消费
	public boolean addWebItem(int itemId, int itemAppId, String itemName, String itemPrice, String itemBuyDate, int catId, int recommend) {
		String sql = "SELECT ItemID FROM " + ITEMTABNAME + " WHERE ItemWebID=" + itemId;
		try {
			Cursor result = this.db.rawQuery(sql, null);
			if(result.moveToNext()) {
				sql = "UPDATE " + ITEMTABNAME + " SET ItemName='" + itemName + "', ItemPrice='" + itemPrice + "', ItemBuyDate='" + itemBuyDate + "', CategoryID='" + catId + "', Synchronize='0'"
				    + " WHERE ItemID=" + result.getString(0);
			} else if(itemAppId > 0) {
				sql = "UPDATE " + ITEMTABNAME + " SET ItemName='" + itemName + "', ItemPrice='" + itemPrice + "', ItemBuyDate='" + itemBuyDate + "', CategoryID='" + catId + "', Synchronize='0'"
					+ " WHERE ItemID=" + itemAppId;
			} else {
				sql = "INSERT INTO " + ITEMTABNAME + "(ItemID, ItemWebID, ItemName, ItemPrice, ItemBuyDate, CategoryID, Synchronize, Recommend) "
					+ "VALUES (null, '" + itemId + "', '" + itemName + "', '" + itemPrice + "', '" + itemBuyDate + "', '" + catId + "', '0', '" + recommend + "')";
			}
		    this.db.execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	//删除网络消费
	public boolean delWebItem(int itemId, int itemAppId) {
		String sql = "DELETE FROM " + ITEMTABNAME + " WHERE ItemID=" + itemAppId + " OR ItemWebID=" + itemId;
		try {
		    this.db.execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	//删除网络消费
	public boolean clearDelTable() {
		String sql = "DELETE FROM " + DELTABNAME;
		try {
		    this.db.execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	//删除最后消费
	public boolean deleteLastItem() {
		String sql = "DELETE FROM " + ITEMTABNAME + " WHERE ItemID=(SELECT MAX(ItemID) FROM " + ITEMTABNAME + ")";
		try {
		    this.db.execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	//编辑消费
	public boolean updateItem(int id, String itemName, String itemPrice, String itemBuyDate, int catId) {
		String sql = "UPDATE " + ITEMTABNAME + " SET ItemName='" + itemName + "', ItemPrice='" + itemPrice + "', ItemBuyDate='" + itemBuyDate + "', CategoryID='" + catId + "', Synchronize='1'"
				   + " WHERE ItemID=" + id;
		try {
		    this.db.execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	//删除消费
	public boolean deleteItem(int id) {
		this.db.beginTransaction();
		try {
			int itemWebId = 0;
		    Cursor result = this.db.rawQuery("SELECT ItemWebID FROM " + ITEMTABNAME + " WHERE ItemID=" + id, null);
		    if(result.moveToNext()) {
		    	itemWebId = result.getInt(0);
		    }
		    this.db.execSQL("DELETE FROM " + ITEMTABNAME + " WHERE ItemID=" + id);
		    this.db.execSQL("INSERT INTO " + DELTABNAME + " (DeleteID, ItemID, ItemWebID) VALUES (null, " + id + ", " + itemWebId + ")");
		    this.db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			this.db.endTransaction();
		}
		
		return true;
	}
	
	//更改同步状态
	public void updateSyncStatus(int id) {
		String sql = "UPDATE " + ITEMTABNAME + " SET Synchronize='0' WHERE ItemID=" + id;
		try {
		    this.db.execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//恢复数据
	public boolean restoreItemTable(List<CharSequence> list) {
		try {
			this.db.beginTransaction();
			this.db.execSQL("DELETE FROM " + ITEMTABNAME);
			Iterator<CharSequence> it = list.iterator();
			while(it.hasNext()) {
				this.db.execSQL(it.next().toString());
			}
			this.db.setTransactionSuccessful();
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			this.db.endTransaction();
		}
		
		return true;
	}
		
	//关闭数据库
	public void close() {
		this.db.close();
	}

}
