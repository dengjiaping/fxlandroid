package com.aalife.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CategoryTableAccess {
	private static final String ITEMTABNAME = "ItemTable";
	private static final String CATTABNAME = "CategoryTable";
	private SQLiteDatabase db = null;

	public CategoryTableAccess(SQLiteDatabase db) {
		this.db = db;
	}

	//查所有分类下拉
	public List<CharSequence> findAllCategory() {
		List<CharSequence> all = new ArrayList<CharSequence>();
		String sql = "SELECT CategoryName FROM " + CATTABNAME + " WHERE CategoryLive = '1' AND CategoryDisplay = '1'";
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
	
	//查所有分类下拉--AddSmart
	public List<Map<String, String>> findAllCategorySmart() {
		List<Map<String, String>> all = new ArrayList<Map<String, String>>();
		String sql = "SELECT CategoryID, CategoryName FROM " + CATTABNAME + " WHERE CategoryLive = '1' AND CategoryDisplay = '1'";
		Cursor result = null;
		try {
			result = this.db.rawQuery(sql, null);
			while (result.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", result.getString(0));
				map.put("name", result.getString(1));
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

	//查所有分类管理
	public List<Map<String, String>> findAllCatEdit() {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = "SELECT CategoryID, CategoryName, CategoryDisplay FROM " + CATTABNAME + " WHERE CategoryLive = '1'";
		Cursor result = null;
		try {
			result = this.db.rawQuery(sql, null);
			while (result.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", String.valueOf(result.getPosition() + 1));
				map.put("catid", result.getString(0));
				map.put("catname", result.getString(1));
				map.put("catdisplay", result.getString(2));
				map.put("delete", "删除");
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
	
	//查所有同步分类管理
	public List<Map<String, String>> findAllSyncCat() {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = "SELECT CategoryID, CategoryName, CategoryDisplay, CategoryLive FROM " + CATTABNAME + " WHERE Synchronize = '1' AND IsDefault = '0'";
		Cursor result = null;
		try {
			result = this.db.rawQuery(sql, null);
			while (result.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", String.valueOf(result.getPosition() + 1));
				map.put("catid", result.getString(0));
				map.put("catname", result.getString(1));
				map.put("catdisplay", result.getString(2));
				map.put("catlive", result.getString(3));
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

	//根据名称返回ID
	public int findCatIdByName(String name) {
		int catId = 0; 
		String sql = "SELECT CategoryID FROM " + CATTABNAME + " WHERE CategoryName = '" + name + "'";
		Cursor result = null;
		try {
			result = this.db.rawQuery(sql, null);
			if (result.moveToFirst()) {
				catId = Integer.parseInt(result.getString(0));
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(result != null) {
				result.close();
			}
		}

		return catId;
	}

	//保存类别
	public boolean saveCategory(int saveId, String catName) {
		String sql = "SELECT * FROM " + CATTABNAME + " WHERE CategoryName = '" + catName + "' AND CategoryLive = '1'";
		Cursor result = null;
		try {
			result = this.db.rawQuery(sql, null);
			if (result.moveToFirst()) {
				return false;
			}
			if(saveId == 0) {
				int catId = 0;
				sql = "SELECT MAX(CategoryID) + 1 FROM " + CATTABNAME;
				result = this.db.rawQuery(sql, null);
				if (result.moveToFirst()) {
					catId = Integer.parseInt(result.getString(0));
				}
				sql = "INSERT INTO " + CATTABNAME + "(CategoryID, CategoryName, Synchronize, CategoryRank) "
				   	+ "VALUES ('" + catId + "', '" + catName + "', '1', '" + catId + "')";
			} else {
				sql = "UPDATE " + CATTABNAME + " SET CategoryName = '" + catName + "', Synchronize = '1', IsDefault = '0' WHERE CategoryID = " + saveId;
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

	//保存网络类别
	public void saveWebCategory(int catId, String catName, int catDisplay, int catLive) throws Exception {
		String sql = "SELECT * FROM " + CATTABNAME + " WHERE CategoryID = " + catId;
		Cursor result = this.db.rawQuery(sql, null);
		if (result.moveToFirst()) {
			sql = "UPDATE " + CATTABNAME + " SET CategoryName = '" + catName + "', CategoryDisplay = '" + catDisplay + "', CategoryLive = '" + catLive + "', Synchronize = '0', IsDefault = '0' WHERE CategoryID = " + catId;
		} else {
			sql = "INSERT INTO " + CATTABNAME + "(CategoryID, CategoryName, CategoryDisplay, CategoryLive, Synchronize, CategoryRank) "
			   	+ "VALUES ('" + catId + "', '" + catName + "', '" + catDisplay + "', '" + catLive + "', '0', '" + catId + "')";
		}
	    this.db.execSQL(sql);
	    
		if(result != null) {
			result.close();
		}
	}

	//更改同步状态
	public void updateSyncStatus(int id) {
		String sql = "UPDATE " + CATTABNAME + " SET Synchronize = '0' WHERE CategoryID = " + id;
		try {
		    this.db.execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//删除类别
	public int delCategory(int catId) {
		String sql = "SELECT * FROM " + ITEMTABNAME + " WHERE CategoryID = " + catId;
		Cursor result = null;
		try {
			result = this.db.rawQuery(sql, null);
			if (result.moveToFirst()) {
				return 2;
			}			
		    sql = "UPDATE " + CATTABNAME + " SET CategoryLive = '0', Synchronize = '1', IsDefault = '0' WHERE CategoryID = " + catId;
		    this.db.execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		} finally {
			if(result != null) {
				result.close();
			}
		}
		
		return 1;
	}

	//更新类别显示
	public boolean updateCatDisplay(int catId, int display) {
		String sql = "UPDATE " + CATTABNAME + " SET CategoryDisplay = '" + display + "', Synchronize = '1', IsDefault = '0' WHERE CategoryID = " + catId;
		try {
		    this.db.execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	//根据ID返回名称
	public String findCatNameById(int id) {
		String catName = ""; 
		String sql = "SELECT CategoryName FROM " + CATTABNAME + " WHERE CategoryID = '" + id + "'";
		Cursor result = null;
		try {
			result = this.db.rawQuery(sql, null);
			if (result.moveToFirst()) {
				catName = result.getString(0);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(result != null) {
				result.close();
			}
		}

		return catName;
	}
	
	//关闭数据库
	public void close() {
		this.db.close();
	}
	
}
