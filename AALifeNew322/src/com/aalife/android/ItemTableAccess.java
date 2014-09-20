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
	
	//查所有名称
	public List<CharSequence> findAllItemName() {
		List<CharSequence> all = new ArrayList<CharSequence>();
		String sql = "SELECT ItemName FROM " + ITEMTABNAME + " GROUP BY ItemName ORDER BY ItemName ASC";
		Cursor result = null;
		try {
			result = this.db.rawQuery(sql, null);
			while (result.moveToNext()) {
				all.add(result.getString(0));
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(result != null) {
				result.close();
			}
		}
		
		return all;
	}
		
	//根据类别ID查所有消费名称--AddSmart
	public List<Map<String, String>> findAllItemByCatId(String catId) {
		List<Map<String, String>> all = new ArrayList<Map<String, String>>();
		String sql = "SELECT ItemName, COUNT(0) AS Count FROM " + ITEMTABNAME + " WHERE CategoryID = " + catId + " GROUP BY ItemName ORDER BY Count DESC";
		Cursor result = null;
		try {
			result = this.db.rawQuery(sql, null);
			while (result.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", String.valueOf(result.getPosition() + 1));
				map.put("name", result.getString(0));
				all.add(map);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(result != null) {
				result.close();
			}
		}
		
		return all;
	}

	//根据名称查所有消费单价--AddSmart
	public List<Map<String, String>> findAllPriceByName(String name) {
		List<Map<String, String>> all = new ArrayList<Map<String, String>>();
		String sql = "SELECT ItemPrice, COUNT(0) AS Count FROM " + ITEMTABNAME + " WHERE ItemName LIKE '%" + name + "%' GROUP BY ItemPrice ORDER BY Count DESC";
		Cursor result = null;
		try {
			result = this.db.rawQuery(sql, null);		
			while (result.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", String.valueOf(result.getPosition() + 1));
				map.put("name", result.getString(0));
				all.add(map);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(result != null) {
				result.close();
			}
		}
		
		return all;
	}
	
	//根据日期查消费
	public List<Map<String, String>> findItemByDate(String date) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = "SELECT ItemID, ItemName, ItemPrice, ItemBuyDate, CategoryID, Recommend, RegionID, CASE WHEN RegionID<>0 AND IFNULL(RegionType,'')='' THEN 'm' ELSE IFNULL(RegionType,'') END FROM " + ITEMTABNAME
				   + " WHERE STRFTIME('%Y-%m-%d', ItemBuyDate) = '" + date + "' ORDER BY ItemID ASC";
		Cursor result = null;
		try {
			result = this.db.rawQuery(sql, null);
			while (result.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				//map.put("id", String.valueOf(result.getPosition() + 1));
				map.put("itemid", result.getString(0));
				map.put("itemname", UtilityHelper.getItemNameByRegion(result.getString(1), result.getString(7)));
				map.put("itemnamevalue", result.getString(1));
				map.put("itemprice", "￥ " + UtilityHelper.formatDouble(result.getDouble(2), "0.0##"));
				map.put("pricevalue", UtilityHelper.formatDouble(result.getDouble(2), "0.0##"));
				map.put("itembuydate", result.getString(3));
				map.put("catid", result.getString(4));
				map.put("recommend", result.getString(5));
				map.put("regionid", result.getString(6));
				map.put("regiontype", result.getString(7));
				list.add(map);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(result != null) {
				result.close();
			}
		}
			
		return list;
	}
	
	//根据日期查月消费
	public List<Map<String, String>> findMonthByDate(String date) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = "SELECT SUM(ItemPrice), ItemBuyDate FROM " + ITEMTABNAME
				   + " GROUP BY STRFTIME('%Y-%m-%d', ItemBuyDate) HAVING STRFTIME('%Y-%m', ItemBuyDate) = STRFTIME('%Y-%m', '" + date + "')";
		Cursor result = null;
		try {
			result = this.db.rawQuery(sql, null);
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
		} finally {
			if(result != null) {
				result.close();
			}
		}
			
		return list;
	}
	
	//查所有月消费
	public List<Map<String, String>> findAllMonth() {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = "SELECT SUM(ItemPrice), ItemBuyDate FROM " + ITEMTABNAME + " GROUP BY STRFTIME('%Y-%m', ItemBuyDate)";
		Cursor result = null;
		try {
			result = this.db.rawQuery(sql, null);
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
		} finally {
			if(result != null) {
				result.close();
			}
		}
			
		return list;
	}
	
	//查所有月消费
	public Map<String, String> findAllMonth(String curDate) {
		Map<String, String> map = new HashMap<String, String>();
		String sql = "SELECT SUM(ItemPrice), ItemBuyDate FROM " + ITEMTABNAME
				   + " WHERE STRFTIME('%Y-%m-%d', ItemBuyDate) <= STRFTIME('%Y-%m-%d', datetime('now'))"
				   + " AND STRFTIME('%Y-%m', ItemBuyDate) = STRFTIME('%Y-%m', '" + curDate + "')"
				   + " GROUP BY STRFTIME('%Y-%m', ItemBuyDate)";
		Cursor result = null;
		try {
			result = this.db.rawQuery(sql, null);
			if (result.moveToNext()) {
				map.put("id", String.valueOf(result.getPosition() + 1));
				map.put("price", "￥ " + UtilityHelper.formatDouble(result.getDouble(0), "0.0##"));
				map.put("pricevalue", UtilityHelper.formatDouble(result.getDouble(0), "0.0##"));
				map.put("date", UtilityHelper.formatDate(result.getString(1), "y-m"));
				map.put("datevalue", UtilityHelper.formatDate(result.getString(1), "y-m-d"));
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(result != null) {
				result.close();
			}
		}
			
		return map;
	}

	//查下一个有数据日期
	public String findLastDate() {
		String value = "";
		String sql = "SELECT ItemBuyDate FROM " + ITEMTABNAME 
				   + " WHERE STRFTIME('%Y-%m-%d', ItemBuyDate) <= STRFTIME('%Y-%m-%d', datetime('now')) ORDER BY ItemBuyDate DESC LIMIT 30";
		Cursor result = null;
		try {
			result = this.db.rawQuery(sql, null);
			if (result.moveToLast()) {
				value = result.getString(0);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(result != null) {
				result.close();
			}
		}
			
		return value;
	}
	
	//根据日期查所有月消费
	public List<Map<String, String>> findAllDayBuyDate(String date) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = "SELECT SUM(ItemPrice), ItemBuyDate FROM " + ITEMTABNAME
				   + " GROUP BY STRFTIME('%Y-%m-%d', ItemBuyDate)"
				   + " HAVING STRFTIME('%Y-%m', ItemBuyDate) = STRFTIME('%Y-%m', '" + date + "') ORDER BY ItemBuyDate DESC";
		Cursor result = null;
		try {
			result = this.db.rawQuery(sql, null);
			while (result.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("pricevalue", UtilityHelper.formatDouble(result.getDouble(0), "0.0##"));
				map.put("itembuydate", UtilityHelper.formatDate(result.getString(1), "y-m-d"));
				list.add(map);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(result != null) {
				result.close();
			}
		}
			
		return list;
	}
	
	//根据日期查首次所有月消费
	public List<Map<String, String>> findAllDayFirstBuyDate(String date) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = "SELECT SUM(ItemPrice), ItemBuyDate FROM " + ITEMTABNAME
				   + " GROUP BY STRFTIME('%Y-%m-%d', ItemBuyDate)"
				   + " HAVING STRFTIME('%Y-%m-%d', ItemBuyDate) <= STRFTIME('%Y-%m-%d', datetime('now'))"
				   + " AND STRFTIME('%Y-%m', ItemBuyDate) >= STRFTIME('%Y-%m', '" + date + "') ORDER BY ItemBuyDate DESC";
		Cursor result = null;
		try {
			result = this.db.rawQuery(sql, null);
			while (result.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("pricevalue", UtilityHelper.formatDouble(result.getDouble(0), "0.0##"));
				map.put("itembuydate", UtilityHelper.formatDate(result.getString(1), "y-m-d"));
				list.add(map);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(result != null) {
				result.close();
			}
		}
			
		return list;
	}	
	
	//根据日期取下个有数据日期
	public String findNextDate(String date) {
		String value = "";
		String sql = "SELECT ItemBuyDate FROM " + ITEMTABNAME
				   + " GROUP BY STRFTIME('%Y-%m-%d', ItemBuyDate)"
				   + " HAVING STRFTIME('%Y-%m', ItemBuyDate) < STRFTIME('%Y-%m', '" + date + "') ORDER BY ItemBuyDate DESC";
		Cursor result = null;
		try {
			result = this.db.rawQuery(sql, null);
			if (result.moveToFirst()) {
				value = UtilityHelper.formatDate(result.getString(0), "y-m-d");
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(result != null) {
				result.close();
			}
		}
			
		return value;
	}

	//查第一个有数据日期
	public String findFirstDate() {
		String value = "";
		String sql = "SELECT ItemBuyDate FROM " + ITEMTABNAME + " GROUP BY STRFTIME('%Y-%m-%d', ItemBuyDate) ORDER BY ItemBuyDate DESC";
		Cursor result = null;
		try {
			result = this.db.rawQuery(sql, null);
			if (result.moveToFirst()) {
				value = UtilityHelper.formatDate(result.getString(0), "y-m-d");
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(result != null) {
				result.close();
			}
		}
			
		return value;
	}

	//查月消费数量
	public int findMonthCountByDate(String date) {
		int count = 0;
		String sql = "SELECT COUNT(0) FROM " + ITEMTABNAME + " WHERE STRFTIME('%Y-%m', ItemBuyDate) = STRFTIME('%Y-%m', '" + date + "')";
		Cursor result = null;
		try {
			result = this.db.rawQuery(sql, null);
			if (result.moveToFirst()) {
				count = result.getInt(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(result != null) {
				result.close();
			}
		}
		
		return count;
	}
	
	//根据日期查首页统计
	public List<Map<String, String>> findHomeTotalByDate(String curDate) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = "SELECT a.*, b.* FROM "
				   + "(SELECT 1 AS Rank, SUM(ItemPrice) AS Price, '今天' AS Label, 1 AS Flag FROM " + ITEMTABNAME
				   + " WHERE STRFTIME('%Y-%m-%d', ItemBuyDate) <= STRFTIME('%Y-%m-%d', datetime('now'))"
				   + " AND STRFTIME('%Y-%m-%d', ItemBuyDate) = STRFTIME('%Y-%m-%d', '" + curDate + "')) AS a"
				   + " INNER JOIN "
				   + "(SELECT 1 AS Rank, SUM(ItemPrice) AS Price, '昨天' AS Label, 1 AS Flag FROM " + ITEMTABNAME
				   + " WHERE STRFTIME('%Y-%m-%d', ItemBuyDate) <= STRFTIME('%Y-%m-%d', datetime('now'))"
				   + " AND STRFTIME('%Y-%m-%d', ItemBuyDate) = STRFTIME('%Y-%m-%d', DATE('" + curDate + "','start of day','-1 day'))) AS b"
				   + " ON a.Flag = b.Flag"
				   + " UNION "
				   + "SELECT a.*, b.* FROM "
				   + "(SELECT 2 AS Rank, SUM(ItemPrice) AS Price, '本月' AS Label, 1 AS Flag FROM " + ITEMTABNAME
				   + " WHERE STRFTIME('%Y-%m-%d', ItemBuyDate) <= STRFTIME('%Y-%m-%d', datetime('now'))"
				   + " AND STRFTIME('%Y-%m', ItemBuyDate) = STRFTIME('%Y-%m', '" + curDate + "')) AS a"
				   + " INNER JOIN "
				   + "(SELECT 2 AS Rank, SUM(ItemPrice) AS Price, '上月' AS Label, 1 AS Flag FROM " + ITEMTABNAME
				   + " WHERE STRFTIME('%Y-%m-%d', ItemBuyDate) <= STRFTIME('%Y-%m-%d', datetime('now'))"
				   + " AND STRFTIME('%Y-%m', ItemBuyDate) = STRFTIME('%Y-%m', DATE('" + curDate + "','start of month','-1 month'))) AS b"
				   + " ON a.Flag = b.Flag"		
				   + " UNION "
				   + "SELECT a.*, b.* FROM "
				   + "(SELECT 3 AS Rank, SUM(ItemPrice) AS Price, '今年' AS Label, 1 AS Flag FROM " + ITEMTABNAME
				   + " WHERE STRFTIME('%Y-%m-%d', ItemBuyDate) <= STRFTIME('%Y-%m-%d', datetime('now'))"
				   + " AND STRFTIME('%Y', ItemBuyDate) = STRFTIME('%Y', '" + curDate + "')) AS a"
				   + " INNER JOIN "
				   + "(SELECT 3 AS Rank, SUM(ItemPrice) AS Price, '去年' AS Label, 1 AS Flag FROM " + ITEMTABNAME
				   + " WHERE STRFTIME('%Y-%m-%d', ItemBuyDate) <= STRFTIME('%Y-%m-%d', datetime('now'))"
				   + " AND STRFTIME('%Y', ItemBuyDate) = STRFTIME('%Y', DATE('" + curDate + "','start of year','-1 year'))) AS b"
				   + " ON a.Flag = b.Flag";
		Cursor result = null;
		try {
			result = this.db.rawQuery(sql, null);
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
		} finally {
			if(result != null) {
				result.close();
			}
		}
			
		return list;
	}

	//查同步消费
	public List<Map<String, String>> findSyncItem() {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = "SELECT ItemID, ItemName, ItemPrice, ItemBuyDate, CategoryID, ItemWebID, Recommend, RegionID, RegionType FROM " + ITEMTABNAME 
				   + " WHERE Synchronize = '1'";
		Cursor result = null;
		try {
			result = this.db.rawQuery(sql, null);
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
				map.put("regionid", result.getString(7));
				map.put("regiontype", result.getString(8));
				list.add(map);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(result != null) {
				result.close();
			}
		}
		
		return list;
	}	

	//查是否有同步消费
	public boolean hasSyncItem() {
		String sql = "SELECT COUNT(0) FROM " + ITEMTABNAME + " WHERE Synchronize = '1'";
		Cursor result = null;
		try {
			result = this.db.rawQuery(sql, null);
			if (result.moveToFirst()) {
				return result.getInt(0) > 0;
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(result != null) {
				result.close();
			}
		}
		
		return false;
	}
	
	//查是否有消费
	public boolean hasItem() {
		String sql = "SELECT COUNT(0) FROM " + ITEMTABNAME;
		Cursor result = null;
		try {
			result = this.db.rawQuery(sql, null);
			if (result.moveToFirst()) {
				return result.getInt(0) > 0;
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(result != null) {
				result.close();
			}
		}
		
		return false;
	}
	
	//修复同步状态 3.1.4
	public void fixSyncStatus() {
		String sql = "UPDATE " + ITEMTABNAME + " SET Synchronize = '1'";
		try {
		    this.db.execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//查同步删除消费
	public List<Map<String, String>> findDelSyncItem() {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = "SELECT ItemID, ItemWebID FROM " + DELTABNAME;
		Cursor result = null;
		try {
			result = this.db.rawQuery(sql, null);
			while (result.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("itemid", result.getString(0));
				map.put("itemwebid", result.getString(1));
				list.add(map);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(result != null) {
				result.close();
			}
		}
		
		return list;
	}

	//添加消费
	public boolean addItem(String itemName, String itemPrice, String itemBuyDate, int catId, int recommend, int regionId, String regionType) {
		String sql = "INSERT INTO " + ITEMTABNAME + "(ItemName, ItemPrice, ItemBuyDate, CategoryID, Synchronize, Recommend, RegionId, RegionType) "
			   	   + "VALUES ('" + itemName + "', '" + itemPrice + "', '" + itemBuyDate + "', '" + catId + "', '1', '" + recommend + "', '" + regionId + "', '" + regionType + "')";
		try {
		    this.db.execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	//取最大区间ID
	public int getMaxRegionId() {
		Cursor result = null;
		int regionId = 0;
		try {
		    result = this.db.rawQuery("SELECT MAX(RegionID) FROM " + ITEMTABNAME, null);
		    if(result.moveToFirst()) {
		    	regionId = result.getInt(0);
		    }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(result != null) {
				result.close();
			}
		}
		
		return (regionId+1) % 2 == 0 ? regionId+1 : regionId+2;
	}
	
	//取区间ID日期
	public String[] getRegionDate(int regionId) {
		Cursor result = null;
		String[] date = new String[2];
		try {
		    result = this.db.rawQuery("SELECT STRFTIME('%Y-%m-%d', MIN(ItemBuyDate)), STRFTIME('%Y-%m-%d', MAX(ItemBuyDate)) FROM " + ITEMTABNAME + " WHERE RegionID = " + regionId, null);
		    if(result.moveToFirst()) {
		    	date[0] = result.getString(0);
		    	date[1] = result.getString(1);
		    }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(result != null) {
				result.close();
			}
		}
		
		return date;
	}

	//添加网络消费
	public boolean addWebItem(int itemId, int itemAppId, String itemName, String itemPrice, String itemBuyDate, int catId, int recommend, int regionId, String regionType) {
		String sql = "SELECT ItemID FROM " + ITEMTABNAME + " WHERE ItemWebID = " + itemId;
		Cursor result = null;
		try {
			result = this.db.rawQuery(sql, null);
			if(result.moveToFirst()) {
				sql = "UPDATE " + ITEMTABNAME + " SET ItemName = '" + itemName + "', ItemPrice = '" + itemPrice + "', ItemBuyDate = '" + itemBuyDate + "', CategoryID = '" + catId + "', Synchronize = '0', Recommend = '" + recommend + "', RegionID = '" + regionId + "', RegionType = '" + regionType + "'"
				    + " WHERE ItemID = " + result.getString(0);
			} else if(itemAppId > 0) {
				sql = "UPDATE " + ITEMTABNAME + " SET ItemName = '" + itemName + "', ItemPrice = '" + itemPrice + "', ItemBuyDate = '" + itemBuyDate + "', CategoryID = '" + catId + "', Synchronize = '0', Recommend = '" + recommend + "', RegionID = '" + regionId + "', RegionType = '" + regionType + "'"
					+ " WHERE ItemID = " + itemAppId;
			} else {
				sql = "INSERT INTO " + ITEMTABNAME + "(ItemWebID, ItemName, ItemPrice, ItemBuyDate, CategoryID, Synchronize, Recommend, RegionID, RegionType) "
					+ "VALUES ('" + itemId + "', '" + itemName + "', '" + itemPrice + "', '" + itemBuyDate + "', '" + catId + "', '0', '" + recommend + "', '" + regionId + "', '" + regionType + "')";
			}
		    this.db.execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if(result != null) {
				result.close();
			}
		}
		
		return true;
	}
	
	//根据ItemWebID取ItemID
	public int getItemId(int itemWebId) {
		Cursor result = null;
		int itemId = 0;
		try {
		    result = this.db.rawQuery("SELECT ItemID FROM " + ITEMTABNAME + " WHERE ItemWebID = " + itemWebId, null);
		    if(result.moveToFirst()) {
		    	itemId = result.getInt(0);
		    }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(result != null) {
				result.close();
			}
		}
		
		return itemId;
	}

	//删除网络消费
	public void delWebItem(int itemId, int itemAppId) {
		String sql = "DELETE FROM " + ITEMTABNAME + " WHERE ItemID = " + itemAppId + " OR ItemWebID = " + itemId;
		try {
		    this.db.execSQL(sql);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}	
	
	//删除网络消费
	public void clearDelTable() {
		String sql = "DELETE FROM " + DELTABNAME;
		try {
		    this.db.execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//删除最后消费
	public boolean deleteLastItem() {
		String sql = "DELETE FROM " + ITEMTABNAME + " WHERE ItemID = (SELECT MAX(ItemID) FROM " + ITEMTABNAME + ")";
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
		String sql = "UPDATE " + ITEMTABNAME + " SET ItemName = '" + itemName + "', ItemPrice = '" + itemPrice + "', ItemBuyDate = '" + itemBuyDate + "', CategoryID = '" + catId + "', Synchronize = '1'"
				   + " WHERE ItemID = " + id;
		try {
		    this.db.execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	//删除消费
	public boolean deleteItem(int itemId) {
		this.db.beginTransaction();
		Cursor result = null;
		try {
			int itemWebId = 0;
		    result = this.db.rawQuery("SELECT ItemWebID FROM " + ITEMTABNAME + " WHERE ItemID = " + itemId, null);
		    if(result.moveToFirst()) {
		    	itemWebId = result.getInt(0);
		    }
		    this.db.execSQL("DELETE FROM " + ITEMTABNAME + " WHERE ItemID = " + itemId);
		    this.db.execSQL("INSERT INTO " + DELTABNAME + " (ItemID, ItemWebID) VALUES (" + itemId + ", " + itemWebId + ")");
		    this.db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			this.db.endTransaction();
			if(result != null) {
				result.close();
			}
		}
		
		return true;
	}
	
	//删除消费区间
	public boolean deleteRegion(int regionId) {
		String sql = "SELECT ItemID FROM " + ITEMTABNAME + " WHERE RegionID = " + regionId;
		Cursor result = null;
		try {
			result = this.db.rawQuery(sql, null);
			while(result.moveToNext()) {
				int itemId = result.getInt(0);
				deleteItem(itemId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if(result != null) {
				result.close();
			}
		}
		
		return true;
	}
	
	//反删除消费区间
	public boolean deleteRegionBack(int regionId) {
		String sql = "SELECT ItemID FROM " + ITEMTABNAME + " WHERE RegionID = " + regionId;
		Cursor result = null;
		try {
			result = this.db.rawQuery(sql, null);
			while(result.moveToNext()) {
				int itemId = result.getInt(0);
				this.db.execSQL("DELETE FROM " + DELTABNAME + " WHERE ItemID = " + itemId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if(result != null) {
				result.close();
			}
		}
		
		return true;
	}
	
	//删除所有消费
	public void deleteAllData() {
		try {
		    this.db.execSQL("DELETE FROM " + ITEMTABNAME);
		    this.db.execSQL("DELETE FROM " + DELTABNAME);
		    this.db.execSQL("DELETE FROM " + CATTABNAME);		
		    this.db.execSQL("INSERT INTO " + CATTABNAME + "(CategoryID, CategoryName, CategoryRank, IsDefault) "
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//更改同步状态
	public void updateSyncStatus(int id, int itemWebId) {
		String sql = "UPDATE " + ITEMTABNAME + " SET Synchronize = '0', ItemWebID = '" + itemWebId + "' WHERE ItemID = " + id;
		try {
		    this.db.execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//更改同步状态
	public void updateSyncStatus() {
		String sql = "UPDATE " + ITEMTABNAME + " SET Synchronize = '0'";
		try {
		    this.db.execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//更新类别显示
	public boolean updateItemRecommend(int itemId, int recommend) {
		String sql = "UPDATE " + ITEMTABNAME + " SET Recommend = '" + recommend + "', Synchronize = '1' WHERE ItemID = " + itemId;
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
				   + "LEFT JOIN (SELECT * FROM " + ITEMTABNAME + " WHERE STRFTIME('%Y-%m-%d', ItemBuyDate) <= STRFTIME('%Y-%m-%d', datetime('now'))"
				   + " AND STRFTIME('%Y%m', ItemBuyDate) = STRFTIME('%Y%m', '" + date + "')) AS it "
				   + "ON ct.CategoryID = it.CategoryID WHERE ct.CategoryLive = '1' AND ct.CategoryDisplay = '1' "
				   + "GROUP BY ct.CategoryID, ct.CategoryName ORDER BY ItemPrice DESC, ct.CategoryID ASC";
		Cursor result = null;
		try {
			result = this.db.rawQuery(sql, null);
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
		} finally {
			if(result != null) {
				result.close();
			}
		}

		return list;
	}
	
	//查消费分类排行
	public List<Map<String, String>> findCompareCatByDate(String date) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = "SELECT a.CategoryID, a.CategoryName, a.ItemPrice AS PriceCur, b.ItemPrice AS PricePrev FROM ("
				   + "SELECT ct.CategoryID, ct.CategoryName, SUM(ItemPrice) AS ItemPrice FROM " + CATTABNAME + " AS ct "
				   + "LEFT JOIN (SELECT * FROM " + ITEMTABNAME + " WHERE STRFTIME('%Y-%m-%d', ItemBuyDate) <= STRFTIME('%Y-%m-%d', datetime('now'))"
				   + " AND STRFTIME('%Y%m', ItemBuyDate) = STRFTIME('%Y%m', '" + date + "')) AS it "
				   + "ON ct.CategoryID = it.CategoryID WHERE ct.CategoryLive = '1' AND ct.CategoryDisplay = '1' "
				   + "GROUP BY ct.CategoryID, ct.CategoryName ORDER BY ItemPrice DESC, ct.CategoryID ASC) AS a INNER JOIN ("
		           + "SELECT ct.CategoryID, ct.CategoryName, SUM(ItemPrice) AS ItemPrice FROM " + CATTABNAME + " AS ct "
				   + "LEFT JOIN (SELECT * FROM " + ITEMTABNAME + " WHERE STRFTIME('%Y-%m-%d', ItemBuyDate) <= STRFTIME('%Y-%m-%d', datetime('now'))"
		           + " AND STRFTIME('%Y%m', ItemBuyDate) = STRFTIME('%Y%m', DATETIME('" + date + "', 'START OF MONTH', '-1 MONTH'))) AS it "
				   + "ON ct.CategoryID = it.CategoryID WHERE ct.CategoryLive = '1' AND ct.CategoryDisplay = '1' "
				   + "GROUP BY ct.CategoryID, ct.CategoryName ORDER BY ItemPrice DESC, ct.CategoryID ASC) AS b ON a.CategoryID = b.CategoryID "
				   + "ORDER BY PriceCur DESC, PricePrev DESC";
		Cursor result = null;
		try {
			result = this.db.rawQuery(sql, null);
			while (result.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("catid", result.getString(0));
				map.put("catname", result.getString(1));
				map.put("pricecur", result.getDouble(2) > 0 ? "￥ " + UtilityHelper.formatDouble(result.getDouble(2), "0.0##") : "0");
				map.put("priceprev", result.getDouble(3) > 0 ? "￥ " + UtilityHelper.formatDouble(result.getDouble(3), "0.0##") : "0");
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(result != null) {
				result.close();
			}
		}

		return list;
	}

	//查消费次数排名
	public List<Map<String, String>> findRankCountByDate(String date) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = "SELECT ItemName, COUNT(ItemName) AS Count, SUM(ItemPrice) AS Price FROM " + ITEMTABNAME
				   + " WHERE STRFTIME('%Y-%m-%d', ItemBuyDate) <= STRFTIME('%Y-%m-%d', datetime('now'))"
				   + " AND STRFTIME('%Y-%m', ItemBuyDate) = STRFTIME('%Y-%m', '" + date + "') GROUP BY ItemName"
				   + " ORDER BY Count DESC, Price DESC, ItemBuyDate DESC";
		Cursor result = null;
		try {
			result = this.db.rawQuery(sql, null);
			while (result.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("itemname", result.getString(0));
				map.put("count", result.getString(1) + " 次");
				map.put("price", "￥ " + UtilityHelper.formatDouble(result.getDouble(2), "0.0##"));
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(result != null) {
				result.close();
			}
		}
		
		return list;
	}

	//查消费分类明细
	public List<Map<String, String>> findRankCountByDate(String date, int catId) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = "SELECT ItemName, COUNT(ItemName) AS Count, SUM(ItemPrice) AS Price FROM " + ITEMTABNAME
				   + " WHERE STRFTIME('%Y-%m-%d', ItemBuyDate) <= STRFTIME('%Y-%m-%d', datetime('now'))"
				   + " AND STRFTIME('%Y-%m', ItemBuyDate) = STRFTIME('%Y-%m', '" + date + "') AND CategoryID = '" + catId + "'"
				   + " GROUP BY ItemName ORDER BY Count DESC, Price DESC";
		Cursor result = null;
		try {
			result = this.db.rawQuery(sql, null);
			while (result.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("itemname", result.getString(0));
				map.put("count", result.getString(1) + " 次");
				map.put("price", "￥ " + UtilityHelper.formatDouble(result.getDouble(2), "0.0##"));
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(result != null) {
				result.close();
			}
		}
		
		return list;
	}
	
	//查消费分类明细
	public List<Map<String, String>> findAnalyzeCompareDetailByDate(String date, int catId) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = "SELECT ItemName, CountCur, PriceCur, CountPrev, PricePrev FROM ("
				   + "SELECT a.ItemName, IFNULL(a.Count, 0) AS CountCur, a.Price AS PriceCur, IFNULL(b.Count, 0) AS CountPrev, b.Price AS PricePrev FROM ("
				   + "SELECT ItemName, COUNT(ItemName) AS Count, SUM(ItemPrice) AS Price FROM " + ITEMTABNAME
				   + " WHERE STRFTIME('%Y-%m-%d', ItemBuyDate) <= STRFTIME('%Y-%m-%d', datetime('now'))"
				   + " AND STRFTIME('%Y-%m', ItemBuyDate) = STRFTIME('%Y-%m', '" + date + "') AND CategoryID = '" + catId + "'"
				   + " GROUP BY ItemName ORDER BY Count DESC, Price DESC) AS a LEFT JOIN ("
				   + "SELECT ItemName, COUNT(ItemName) AS Count, SUM(ItemPrice) AS Price FROM " + ITEMTABNAME
				   + " WHERE STRFTIME('%Y-%m-%d', ItemBuyDate) <= STRFTIME('%Y-%m-%d', datetime('now'))"
				   + " AND STRFTIME('%Y-%m', ItemBuyDate) = STRFTIME('%Y-%m', DATETIME('" + date + "', 'START OF MONTH', '-1 MONTH')) AND CategoryID = '" + catId + "'"
				   + " GROUP BY ItemName ORDER BY Count DESC, Price DESC) AS b ON a.ItemName = b.ItemName"
				   + " UNION ALL "
				   + "SELECT b.ItemName, IFNULL(a.Count, 0) AS CountCur, a.Price AS PriceCur, IFNULL(b.Count, 0) AS CountPrev, b.Price AS PricePrev FROM ("
				   + "SELECT ItemName, COUNT(ItemName) AS Count, SUM(ItemPrice) AS Price FROM " + ITEMTABNAME
				   + " WHERE STRFTIME('%Y-%m-%d', ItemBuyDate) <= STRFTIME('%Y-%m-%d', datetime('now'))"
				   + " AND STRFTIME('%Y-%m', ItemBuyDate) = STRFTIME('%Y-%m', DATETIME('" + date + "', 'START OF MONTH', '-1 MONTH')) AND CategoryID = '" + catId + "'"
				   + " GROUP BY ItemName ORDER BY Count DESC, Price DESC) AS b LEFT JOIN ("
				   + "SELECT ItemName, COUNT(ItemName) AS Count, SUM(ItemPrice) AS Price FROM " + ITEMTABNAME
				   + " WHERE STRFTIME('%Y-%m-%d', ItemBuyDate) <= STRFTIME('%Y-%m-%d', datetime('now'))"
				   + " AND STRFTIME('%Y-%m', ItemBuyDate) = STRFTIME('%Y-%m', '" + date + "') AND CategoryID = '" + catId + "'"
				   + " GROUP BY ItemName ORDER BY Count DESC, Price DESC) AS a ON a.ItemName = b.ItemName) c"
				   + " GROUP BY ItemName, CountCur, PriceCur, CountPrev, PricePrev ORDER BY CountCur DESC, PriceCur DESC, CountPrev DESC, PricePrev DESC";
		Cursor result = null;
		try {
			result = this.db.rawQuery(sql, null);
			while (result.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("itemname", result.getString(0));
				map.put("countvalue", result.getString(1));
				map.put("countcur", result.getString(1) + " 次");
				map.put("pricecur", "￥ " + UtilityHelper.formatDouble(result.getDouble(2), "0.0##"));
				map.put("countprev", result.getString(3) + " 次");
				map.put("priceprev", "￥ " + UtilityHelper.formatDouble(result.getDouble(4), "0.0##"));
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(result != null) {
				result.close();
			}
		}
		
		return list;
	}
	
	//查消费单价排名
	public List<Map<String, String>> findRankPriceByDate(String date) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = "SELECT ItemName, ItemBuyDate, ItemPrice FROM " + ITEMTABNAME
				   + " WHERE STRFTIME('%Y-%m-%d', ItemBuyDate) <= STRFTIME('%Y-%m-%d', datetime('now'))"
				   + " AND STRFTIME('%Y-%m', ItemBuyDate) = STRFTIME('%Y-%m', '" + date + "') ORDER BY ItemPrice DESC, ItemBuyDate DESC";
		Cursor result = null;
		try {
			result = this.db.rawQuery(sql, null);
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
		} finally {
			if(result != null) {
				result.close();
			}
		}
		
		return list;
	}

	//查消费推荐分析
	public List<Map<String, String>> findAnalyzeRecommend() {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = "SELECT ItemName, ItemBuyDate, ItemPrice FROM " + ITEMTABNAME 
				   + " WHERE STRFTIME('%Y-%m-%d', ItemBuyDate) <= STRFTIME('%Y-%m-%d', datetime('now')) AND Recommend = '1' ORDER BY ItemBuyDate DESC";
		Cursor result = null;
		try {
			result = this.db.rawQuery(sql, null);
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
		} finally {
			if(result != null) {
				result.close();
			}
		}
		
		return list;
	}

	//根据关键字查消费
	public List<Map<String, String>> findItemByKey(String key) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = "SELECT ItemName, ItemBuyDate, ItemPrice FROM " + ITEMTABNAME 
				   + " WHERE STRFTIME('%Y-%m-%d', ItemBuyDate) <= STRFTIME('%Y-%m-%d', datetime('now'))"
				   + " AND ItemName LIKE '%" + key + "%' ORDER BY ItemBuyDate DESC";
		Cursor result = null;
		try {
			result = this.db.rawQuery(sql, null);
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
		} finally {
			if(result != null) {
				result.close();
			}
		}
		
		return list;
	}
	
	//查消费次数明细
	public List<Map<String, String>> findRankPriceByDate(String date, String itemName) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = "SELECT ItemName, ItemBuyDate, ItemPrice FROM " + ITEMTABNAME
				   + " WHERE STRFTIME('%Y-%m-%d', ItemBuyDate) <= STRFTIME('%Y-%m-%d', datetime('now'))"
				   + " AND STRFTIME('%Y-%m', ItemBuyDate) = STRFTIME('%Y-%m', '" + date + "') AND ItemName = '" + itemName + "'"
				   + " ORDER BY ItemPrice DESC, ItemBuyDate DESC";
		Cursor result = null;
		try {
			result = this.db.rawQuery(sql, null);
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
		} finally {
			if(result != null) {
				result.close();
			}
		}
		
		return list;
	}
	
	//查消费日期排名
	public List<Map<String, String>> findRankDateByDate(String date) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = "SELECT ItemBuyDate, SUM(ItemPrice) AS Price FROM " + ITEMTABNAME
				   + " WHERE STRFTIME('%Y-%m-%d', ItemBuyDate) <= STRFTIME('%Y-%m-%d', datetime('now'))"
				   + " AND STRFTIME('%Y-%m', ItemBuyDate) = STRFTIME('%Y-%m', '" + date + "')" 
				   + " GROUP BY STRFTIME('%Y-%m-%d', ItemBuyDate) ORDER BY Price DESC, ItemBuyDate DESC";
		Cursor result = null;
		try {
			result = this.db.rawQuery(sql, null);
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
		} finally {
			if(result != null) {
				result.close();
			}
		}
		
		return list;
	}

	//恢复数据
	public boolean restoreDataBase(List<CharSequence> list) {
		this.db.beginTransaction();
		try {
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
		String sql = "SELECT ItemID, ItemName, ItemPrice, ItemBuyDate, CategoryID, Recommend, Synchronize, RegionID, RegionType FROM " + ITEMTABNAME;
		Cursor result = null;
		try {
			result = this.db.rawQuery(sql, null);
			while (result.moveToNext()) {
				list.add("INSERT INTO " + ITEMTABNAME + "(ItemID, ItemName, ItemPrice, ItemBuyDate, CategoryID, Recommend, Synchronize, RegionID, RegionType) VALUES('" 
			             + result.getString(0)+ "', '" 
						 + result.getString(1) + "', '"
					     + result.getString(2) + "', '" 
						 + result.getString(3) + "', '" 
					     + result.getString(4) + "', '" 
						 + result.getString(5) + "', '" 
						 + result.getString(6) + "', '" 
						 + result.getString(7) + "', '" 
					     + result.getString(8) + "');");
			}
			
			sql = "SELECT CategoryID, CategoryName, CategoryRank, CategoryDisplay, CategoryLive FROM " + CATTABNAME
			    + " WHERE IsDefault = '0'";
			result.close();
			
			result = this.db.rawQuery(sql, null);
			while (result.moveToNext()) {
				list.add("DELETE FROM " + CATTABNAME + " WHERE CategoryID = " + result.getString(0));
				list.add("INSERT INTO " + CATTABNAME + "(CategoryID, CategoryName, CategoryRank, CategoryDisplay, CategoryLive, Synchronize) VALUES('" 
			             + result.getString(0)+ "', '" 
						 + result.getString(1) + "', '"
					     + result.getString(2) + "', '" 
						 + result.getString(3) + "', '" 
					     + result.getString(4) + "', '1');");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(result != null) {
				result.close();
			}
		}

		return list;
	}
		
	//关闭数据库
	public void close() {
		this.db.close();
	}

}
