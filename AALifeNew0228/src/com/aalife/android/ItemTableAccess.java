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
	private static final String CATTABNAME = "CategoryTable";
	private SQLiteDatabase db = null;

	public ItemTableAccess(SQLiteDatabase db) {
		this.db = db;
	}

	//根据日期查消费
	public List<Map<String, String>> findItemByDate(String date) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = "SELECT ItemID, ItemName, ItemPrice, ItemBuyDate, CategoryID, Recommend FROM " + ITEMTABNAME + " WHERE ItemBuyDate = '" + date + "'";
		try {
			Cursor result = this.db.rawQuery(sql, null);
			while (result.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				//map.put("id", String.valueOf(result.getPosition() + 1));
				map.put("itemid", result.getString(0));
				map.put("itemname", result.getString(1));
				map.put("itemprice", "￥ " + UtilityHelper.formatDouble(result.getDouble(2), "0.0##"));
				map.put("pricevalue", UtilityHelper.formatDouble(result.getDouble(2), "0.0##"));
				map.put("itembuydate", result.getString(3));
				map.put("catid", result.getString(4));
				map.put("recommend", result.getString(5));
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
				map.put("price", "￥ " + UtilityHelper.formatDouble(result.getDouble(0), "0.0##"));
				map.put("pricevalue", UtilityHelper.formatDouble(result.getDouble(0), "0.0##"));
				map.put("date", UtilityHelper.formatDate(result.getString(1), "m-d-w"));
				map.put("datevalue", UtilityHelper.formatDate(result.getString(1), "y-m-d"));
				list.add(map);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
			
		return list;
	}
	
	//查所有月消费
	public List<Map<String, String>> findAllMonth() {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = "SELECT SUM(ItemPrice), ItemBuyDate FROM " + ITEMTABNAME
				   + " GROUP BY STRFTIME('%Y-%m', ItemBuyDate)";
		try {
			Cursor result = this.db.rawQuery(sql, null);
			while (result.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", String.valueOf(result.getPosition() + 1));
				map.put("price", "￥ " + UtilityHelper.formatDouble(result.getDouble(0), "0.0##"));
				map.put("pricevalue", UtilityHelper.formatDouble(result.getDouble(0), "0.0##"));
				map.put("date", UtilityHelper.formatDate(result.getString(1), "y-m"));
				map.put("datevalue", UtilityHelper.formatDate(result.getString(1), "y-m-d"));
				list.add(map);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
			
		return list;
	}
	
	//查所有月消费
	public Map<String, String> findAllMonth(String curDate) {
		Map<String, String> map = new HashMap<String, String>();
		String sql = "SELECT SUM(ItemPrice), ItemBuyDate FROM " + ITEMTABNAME
				   + " GROUP BY STRFTIME('%Y-%m', ItemBuyDate) HAVING STRFTIME('%Y-%m', ItemBuyDate) = STRFTIME('%Y-%m', '" + curDate + "')";
		try {
			Cursor result = this.db.rawQuery(sql, null);
			if (result.moveToNext()) {
				map.put("id", String.valueOf(result.getPosition() + 1));
				map.put("price", "￥ " + UtilityHelper.formatDouble(result.getDouble(0), "0.0##"));
				map.put("pricevalue", UtilityHelper.formatDouble(result.getDouble(0), "0.0##"));
				map.put("date", UtilityHelper.formatDate(result.getString(1), "y-m"));
				map.put("datevalue", UtilityHelper.formatDate(result.getString(1), "y-m-d"));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
			
		return map;
	}

	//查下一个有数据日期
	public String findLastDate() {
		String value = "";
		String sql = "SELECT ItemBuyDate FROM " + ITEMTABNAME
				   + " ORDER BY ItemBuyDate DESC LIMIT 30";
		try {
			Cursor result = this.db.rawQuery(sql, null);
			while (result.moveToNext()) {
				value = result.getString(0);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
			
		return value;
	}
	
	//根据日期查所有月消费
	public List<Map<String, String>> findAllDayBuyDate(String date) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = "SELECT SUM(ItemPrice), ItemBuyDate FROM " + ITEMTABNAME
				   + " GROUP BY STRFTIME('%Y-%m-%d', ItemBuyDate) HAVING STRFTIME('%Y-%m', ItemBuyDate) = STRFTIME('%Y-%m', '" + date + "') ORDER BY ItemBuyDate DESC";
		try {
			Cursor result = this.db.rawQuery(sql, null);
			while (result.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("pricevalue", UtilityHelper.formatDouble(result.getDouble(0), "0.0##"));
				map.put("itembuydate", UtilityHelper.formatDate(result.getString(1), "y-m-d"));
				list.add(map);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
			
		return list;
	}
	
	//根据日期查首次所有月消费
	public List<Map<String, String>> findAllDayFirstBuyDate(String date) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = "SELECT SUM(ItemPrice), ItemBuyDate FROM " + ITEMTABNAME
				   + " GROUP BY STRFTIME('%Y-%m-%d', ItemBuyDate) HAVING STRFTIME('%Y-%m', ItemBuyDate) >= STRFTIME('%Y-%m', '" + date + "') ORDER BY ItemBuyDate DESC";
		try {
			Cursor result = this.db.rawQuery(sql, null);
			while (result.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("pricevalue", UtilityHelper.formatDouble(result.getDouble(0), "0.0##"));
				map.put("itembuydate", UtilityHelper.formatDate(result.getString(1), "y-m-d"));
				list.add(map);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
			
		return list;
	}	
	
	//根据日期下个有数据日期
	public String findNextDate(String date) {
		String value = "";
		String sql = "SELECT ItemBuyDate FROM " + ITEMTABNAME
				   + " GROUP BY STRFTIME('%Y-%m-%d', ItemBuyDate) HAVING STRFTIME('%Y-%m', ItemBuyDate) < STRFTIME('%Y-%m', '" + date + "') ORDER BY ItemBuyDate DESC";
		try {
			Cursor result = this.db.rawQuery(sql, null);
			if (result.moveToNext()) {
				value = UtilityHelper.formatDate(result.getString(0), "y-m-d");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
			
		return value;
	}

	//查第一个有数据日期
	public String findFirstDate() {
		String value = "";
		String sql = "SELECT ItemBuyDate FROM " + ITEMTABNAME
				   + " GROUP BY STRFTIME('%Y-%m-%d', ItemBuyDate) ORDER BY ItemBuyDate DESC";
		try {
			Cursor result = this.db.rawQuery(sql, null);
			if (result.moveToNext()) {
				value = UtilityHelper.formatDate(result.getString(0), "y-m-d");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
			
		return value;
	}

	//查月消费数量
	public int findMonthCountByDate(String date) {
		int count = 0;
		String sql = "SELECT COUNT(0) FROM " + ITEMTABNAME
				   + " WHERE STRFTIME('%Y-%m', ItemBuyDate) = STRFTIME('%Y-%m', '" + date + "')";
		try {
			Cursor result = this.db.rawQuery(sql, null);
			if (result.moveToNext()) {
				count = result.getInt(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return count;
	}
	
	//根据日期查首页统计
	public List<Map<String, String>> findHomeTotalByDate(String curDate) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = "SELECT a.*, b.* FROM "
				   + "(SELECT 1 AS Rank, SUM(ItemPrice) AS Price, '今日' AS Label, 1 AS Flag FROM " + ITEMTABNAME
				   + " WHERE STRFTIME('%Y-%m-%d', ItemBuyDate) = STRFTIME('%Y-%m-%d', '" + curDate + "')) AS a"
				   + " INNER JOIN "
				   + "(SELECT 1 AS Rank, SUM(ItemPrice) AS Price, '昨日' AS Label, 1 AS Flag FROM " + ITEMTABNAME
				   + " WHERE STRFTIME('%Y-%m-%d', ItemBuyDate) = STRFTIME('%Y-%m-%d', DATE('" + curDate + "','start of day','-1 day'))) AS b"
				   + " ON a.Flag = b.Flag"
				   + " UNION "
				   + "SELECT a.*, b.* FROM "
				   + "(SELECT 2 AS Rank, SUM(ItemPrice) AS Price, '本月' AS Label, 1 AS Flag FROM " + ITEMTABNAME
				   + " WHERE STRFTIME('%Y-%m', ItemBuyDate) = STRFTIME('%Y-%m', '" + curDate + "')) AS a"
				   + " INNER JOIN "
				   + "(SELECT 2 AS Rank, SUM(ItemPrice) AS Price, '上月' AS Label, 1 AS Flag FROM " + ITEMTABNAME
				   + " WHERE STRFTIME('%Y-%m', ItemBuyDate) = STRFTIME('%Y-%m', DATE('" + curDate + "','start of month','-1 month'))) AS b"
				   + " ON a.Flag = b.Flag"		
				   + " UNION "
				   + "SELECT a.*, b.* FROM "
				   + "(SELECT 3 AS Rank, SUM(ItemPrice) AS Price, '今年' AS Label, 1 AS Flag FROM " + ITEMTABNAME
				   + " WHERE STRFTIME('%Y', ItemBuyDate) = STRFTIME('%Y', '" + curDate + "')) AS a"
				   + " INNER JOIN "
				   + "(SELECT 3 AS Rank, SUM(ItemPrice) AS Price, '去年' AS Label, 1 AS Flag FROM " + ITEMTABNAME
				   + " WHERE STRFTIME('%Y', ItemBuyDate) = STRFTIME('%Y', DATE('" + curDate + "','start of year','-1 year'))) AS b"
				   + " ON a.Flag = b.Flag";
		try {
			Cursor result = this.db.rawQuery(sql, null);
			while (result.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("curprice", "￥ " + UtilityHelper.formatDouble(result.getDouble(1), "0.0##"));
				map.put("curlabel", result.getString(2));
				map.put("prevprice", "￥ " + UtilityHelper.formatDouble(result.getDouble(5), "0.0##"));
				map.put("prevlabel", result.getString(6));
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
		String sql = "SELECT ItemID, ItemName, ItemPrice, ItemBuyDate, CategoryID, ItemWebID, Recommend FROM " + ITEMTABNAME + " WHERE Synchronize = '1'";
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
				map.put("recommend", result.getString(6));
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
		String sql = "INSERT INTO " + ITEMTABNAME + "(ItemName, ItemPrice, ItemBuyDate, CategoryID, Synchronize) "
			   	   + "VALUES ('" + itemName + "', '" + itemPrice + "', '" + itemBuyDate + "', '" + catId + "', '1')";
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
				sql = "UPDATE " + ITEMTABNAME + " SET ItemName='" + itemName + "', ItemPrice='" + itemPrice + "', ItemBuyDate='" + itemBuyDate + "', CategoryID='" + catId + "', Synchronize='0', Recommend='" + recommend + "'"
				    + " WHERE ItemID=" + result.getString(0);
			} else if(itemAppId > 0) {
				sql = "UPDATE " + ITEMTABNAME + " SET ItemName='" + itemName + "', ItemPrice='" + itemPrice + "', ItemBuyDate='" + itemBuyDate + "', CategoryID='" + catId + "', Synchronize='0', Recommend='" + recommend + "'"
					+ " WHERE ItemID=" + itemAppId;
			} else {
				sql = "INSERT INTO " + ITEMTABNAME + "(ItemWebID, ItemName, ItemPrice, ItemBuyDate, CategoryID, Synchronize, Recommend) "
					+ "VALUES ('" + itemId + "', '" + itemName + "', '" + itemPrice + "', '" + itemBuyDate + "', '" + catId + "', '0', '" + recommend + "')";
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
		    this.db.execSQL("INSERT INTO " + DELTABNAME + " (ItemID, ItemWebID) VALUES (" + id + ", " + itemWebId + ")");
		    this.db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			this.db.endTransaction();
		}
		
		return true;
	}
	
	//删除所有消费
	public void deleteAllItem() {
		String sql = "DELETE FROM " + ITEMTABNAME;
		try {
		    this.db.execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	//更改同步状态
	public void updateSyncStatus() {
		String sql = "UPDATE " + ITEMTABNAME + " SET Synchronize='0'";
		try {
		    this.db.execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//更新类别显示
	public boolean updateItemRecommend(int itemId, int recommend) {
		String sql = "UPDATE " + ITEMTABNAME + " SET Recommend='" + recommend + "', Synchronize='1' WHERE ItemID=" + itemId;
		try {
		    this.db.execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	//查消费分类排行
	public List<Map<String, String>> findRankCatByDate(String date) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = "SELECT ct.CategoryID, ct.CategoryName, SUM(ItemPrice) AS ItemPrice FROM " + CATTABNAME + " AS ct "
				   + "LEFT JOIN (SELECT * FROM " + ITEMTABNAME + " WHERE STRFTIME('%Y%m', ItemBuyDate) = STRFTIME('%Y%m', '" + date + "')) AS it "
				   + "ON ct.CategoryID = it.CategoryID WHERE ct.CategoryLive = '1' AND ct.CategoryDisplay = '1' "
				   + "GROUP BY ct.CategoryID, ct.CategoryName ORDER BY ItemPrice DESC, ct.CategoryID ASC";
		try {
			Cursor result = this.db.rawQuery(sql, null);
			while (result.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", String.valueOf(result.getPosition() + 1));
				map.put("catid", result.getString(0));
				map.put("catname", result.getString(1));
				map.put("price", result.getDouble(2) > 0 ? "￥ " + UtilityHelper.formatDouble(result.getDouble(2), "0.0##") : "0");
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	//查消费次数排名
	public List<Map<String, String>> findRankCountByDate(String date) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = "SELECT ItemName, COUNT(ItemName) AS Count, SUM(ItemPrice) AS Price FROM " + ITEMTABNAME
				   + " WHERE STRFTIME('%Y-%m', ItemBuyDate) = STRFTIME('%Y-%m', '" + date + "') GROUP BY ItemName ORDER BY Count DESC, Price DESC";
		try {
			Cursor result = this.db.rawQuery(sql, null);
			while (result.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("itemname", result.getString(0));
				map.put("count", result.getString(1) + " 次");
				map.put("price", "￥ " + UtilityHelper.formatDouble(result.getDouble(2), "0.0##"));
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

	//查消费分类明细
	public List<Map<String, String>> findRankCountByDate(String date, int catId) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = "SELECT ItemName, COUNT(ItemName) AS Count, SUM(ItemPrice) AS Price FROM " + ITEMTABNAME
				   + " WHERE STRFTIME('%Y-%m', ItemBuyDate) = STRFTIME('%Y-%m', '" + date + "') AND CategoryID = '" + catId + "'"
				   + " GROUP BY ItemName ORDER BY Count DESC, Price DESC";
		try {
			Cursor result = this.db.rawQuery(sql, null);
			while (result.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("itemname", result.getString(0));
				map.put("count", result.getString(1) + " 次");
				map.put("price", "￥ " + UtilityHelper.formatDouble(result.getDouble(2), "0.0##"));
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	//查消费单价排名
	public List<Map<String, String>> findRankPriceByDate(String date) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = "SELECT ItemName, ItemBuyDate, ItemPrice FROM " + ITEMTABNAME
				   + " WHERE STRFTIME('%Y-%m', ItemBuyDate) = STRFTIME('%Y-%m', '" + date + "') ORDER BY ItemPrice DESC, ItemBuyDate DESC";
		try {
			Cursor result = this.db.rawQuery(sql, null);
			while (result.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("itemname", result.getString(0));
				map.put("itembuydate", UtilityHelper.formatDate(result.getString(1), "m-d"));
				map.put("datevalue", UtilityHelper.formatDate(result.getString(1), "y-m-d"));
				map.put("itemprice", "￥ " + UtilityHelper.formatDouble(result.getDouble(2), "0.0##"));
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

	//查消费推荐分析
	public List<Map<String, String>> findAnalyzeRecommend() {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = "SELECT ItemName, ItemBuyDate, ItemPrice FROM " + ITEMTABNAME
				   + " WHERE Recommend = '1' ORDER BY ItemBuyDate DESC";
		try {
			Cursor result = this.db.rawQuery(sql, null);
			while (result.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("itemname", result.getString(0));
				map.put("itembuydate", UtilityHelper.formatDate(result.getString(1), "ys-m-d"));
				map.put("datevalue", UtilityHelper.formatDate(result.getString(1), "y-m-d"));
				map.put("itemprice", "￥ " + UtilityHelper.formatDouble(result.getDouble(2), "0.0##"));
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

	//根据关键字查消费
	public List<Map<String, String>> findItemByKey(String key) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = "SELECT ItemName, ItemBuyDate, ItemPrice FROM " + ITEMTABNAME
				   + " WHERE ItemName LIKE '%" + key + "%' ORDER BY ItemBuyDate DESC";
		try {
			Cursor result = this.db.rawQuery(sql, null);
			while (result.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("itemname", result.getString(0));
				map.put("itembuydate", UtilityHelper.formatDate(result.getString(1), "ys-m-d"));
				map.put("datevalue", UtilityHelper.formatDate(result.getString(1), "y-m-d"));
				map.put("itemprice", "￥ " + UtilityHelper.formatDouble(result.getDouble(2), "0.0##"));
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	//查消费次数明细
	public List<Map<String, String>> findRankPriceByDate(String date, String itemName) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = "SELECT ItemName, ItemBuyDate, ItemPrice FROM " + ITEMTABNAME
				   + " WHERE STRFTIME('%Y-%m', ItemBuyDate) = STRFTIME('%Y-%m', '" + date + "') AND ItemName = '" + itemName + "'"
				   + " ORDER BY ItemPrice DESC, ItemBuyDate DESC";
		try {
			Cursor result = this.db.rawQuery(sql, null);
			while (result.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("itemname", result.getString(0));
				map.put("itembuydate", UtilityHelper.formatDate(result.getString(1), "m-d"));
				map.put("datevalue", UtilityHelper.formatDate(result.getString(1), "y-m-d"));
				map.put("itemprice", "￥ " + UtilityHelper.formatDouble(result.getDouble(2), "0.0##"));
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	//查消费日期排名
	public List<Map<String, String>> findRankDateByDate(String date) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = "SELECT ItemBuyDate, SUM(ItemPrice) AS Price FROM " + ITEMTABNAME
				   + " WHERE STRFTIME('%Y-%m', ItemBuyDate) = STRFTIME('%Y-%m', '" + date + "')" 
				   + " GROUP BY STRFTIME('%Y-%m-%d', ItemBuyDate) ORDER BY Price DESC, ItemBuyDate DESC";
		try {
			Cursor result = this.db.rawQuery(sql, null);
			while (result.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", String.valueOf(result.getPosition() + 1));
				map.put("itembuydate", UtilityHelper.formatDate(result.getString(0), "m-d"));
				map.put("datevalue", UtilityHelper.formatDate(result.getString(0), "y-m-d"));
				map.put("price", "￥ " + UtilityHelper.formatDouble(result.getDouble(1), "0.0##"));
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

	//恢复数据
	public boolean restoreDataBase(List<CharSequence> list) {
		try {
			this.db.beginTransaction();
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
	
	//备份数据
	public List<CharSequence> bakDataBase() {
		List<CharSequence> list = new ArrayList<CharSequence>();
		String sql = "SELECT ItemName, ItemPrice, ItemBuyDate, CategoryID, Recommend FROM " + ITEMTABNAME;
		try {
			Cursor result = this.db.rawQuery(sql, null);
			while (result.moveToNext()) {
				list.add("INSERT INTO " + ITEMTABNAME + "(ItemName, ItemPrice, ItemBuyDate, CategoryID, Recommend) VALUES('" 
			             + result.getString(0)+ "', '" 
						 + result.getString(1) + "', '"
					     + result.getString(2) + "', '" 
						 + result.getString(3) + "', '" 
					     + result.getString(4) + "');");
			}
			
			sql = "SELECT CategoryID, CategoryName, CategoryRank, CategoryDisplay, CategoryLive FROM " + CATTABNAME;
			result = this.db.rawQuery(sql, null);
			list.add("DELETE FROM " + CATTABNAME + ";");
			while (result.moveToNext()) {
				list.add("INSERT INTO " + CATTABNAME + "(CategoryID, CategoryName, CategoryRank, CategoryDisplay, CategoryLive) VALUES('" 
			             + result.getString(0)+ "', '" 
						 + result.getString(1) + "', '"
					     + result.getString(2) + "', '" 
						 + result.getString(3) + "', '" 
					     + result.getString(4) + "');");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
		
	//关闭数据库
	public void close() {
		this.db.close();
	}

}
