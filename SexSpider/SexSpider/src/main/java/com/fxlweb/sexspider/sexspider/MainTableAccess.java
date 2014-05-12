package com.fxlweb.sexspider.sexspider;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fengxl on 2014-4-7 007.
 */
public class MainTableAccess {
    private static final String MAINTABLE = "MainTable";
    private static final String WEBURL = "http://www.fxlweb.com";
    //private static final String WEBURL = "http://10.0.2.2:81";
    private SQLiteDatabase db = null;

    public MainTableAccess(SQLiteDatabase db) {
        this.db = db;
    }

    public void getWebData() throws Exception {
        String url = WEBURL + "/SexSpider/GetListData.aspx";

        try {
            JSONObject jsonAll = new JSONObject(HttpHelper.getHttp(url, "utf-8"));
            JSONArray jsonArray = jsonAll.getJSONArray("list");
            if(jsonArray.length() > 0) delete();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Map<String, String> map = new HashMap<String, String>();
                map.put("siteid", jsonObject.getString("siteid"));
                map.put("pageid", jsonObject.getString("pageid"));
                map.put("mainname", jsonObject.getString("mainname"));
                map.put("mainsite", jsonObject.getString("mainsite"));
                map.put("mainlink", jsonObject.getString("mainlink"));
                map.put("htmlstart", jsonObject.getString("htmlstart"));
                map.put("htmlend", jsonObject.getString("htmlend"));
                map.put("imagestart", jsonObject.getString("imagestart"));
                map.put("imageend", jsonObject.getString("imageend"));
                map.put("pageencode", jsonObject.getString("pageencode"));
                map.put("oninclude", jsonObject.getString("oninclude"));

                insert(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insert(Map<String, String> map) {
        int siteId = Integer.parseInt(map.get("siteid"));
        int pageId = Integer.parseInt(map.get("pageid"));
        String mainName = map.get("mainname");
        String mainSite = map.get("mainsite");
        String mainLink = map.get("mainlink");
        String htmlStart = map.get("htmlstart");
        String htmlEnd = map.get("htmlend");
        String imageStart = map.get("imagestart");
        String imageEnd = map.get("imageend");
        String pageEncode = map.get("pageencode");
        String onInclude = map.get("oninclude");

        String sql = "INSERT INTO " + MAINTABLE + "(SiteID, PageID, MainName, MainSite, MainLink, HtmlStart, HtmlEnd, ImageStart, ImageEnd, PageEncode, OnInclude) "
                   + "VALUES (" + siteId + ", " + pageId + ", '" + mainName + "', '" + mainSite + "', '" + mainLink + "', '" + htmlStart + "', '" + htmlEnd + "', '" + imageStart + "', '" + imageEnd + "', '" + pageEncode + "', '" + onInclude + "')";
        try {
            this.db.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Map<String, String>> queryAll() {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        String sql = "SELECT SiteID, PageID, MainName, MainSite, MainLink, HtmlStart, HtmlEnd, ImageStart, ImageEnd, PageEncode, OnInclude, IsDowning FROM " + MAINTABLE;
        Cursor cursor = this.db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("siteid", cursor.getString(0));
            map.put("pageid", cursor.getString(1));
            map.put("mainname", cursor.getString(2));
            map.put("mainsite", cursor.getString(3));
            map.put("mainlink", cursor.getString(4));
            map.put("htmlstart", cursor.getString(5));
            map.put("htmlend", cursor.getString(6));
            map.put("imagestart", cursor.getString(7));
            map.put("imageend", cursor.getString(8));
            map.put("pageencode", cursor.getString(9));
            map.put("include", cursor.getString(10));
            map.put("isdowning", cursor.getString(11));
            list.add(map);
        }

        return list;
    }

    public void updateIsDowning(int siteId, int pageId, int isDowning) {
        String sql = "UPDATE " + MAINTABLE + " SET IsDowning=" + isDowning + " WHERE SiteID=" + siteId + " AND PageID=" + pageId;
        try {
            this.db.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String[] queryById(int siteId, int pageId) {
        String[] array = new String[12];
        String sql = "SELECT SiteID, PageID, MainName, MainSite, MainLink, HtmlStart, HtmlEnd, ImageStart, ImageEnd, PageEncode, OnInclude, IsDowning FROM " + MAINTABLE
                   + " WHERE SiteID=" + siteId + " AND PageID=" + pageId;
        Cursor cursor = this.db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            array[0] = cursor.getString(0);
            array[1] = cursor.getString(1);
            array[2] = cursor.getString(2);
            array[3] = cursor.getString(3);
            array[4] = cursor.getString(4);
            array[5] = cursor.getString(5);
            array[6] = cursor.getString(6);
            array[7] = cursor.getString(7);
            array[8] = cursor.getString(8);
            array[9] = cursor.getString(9);
            array[10] = cursor.getString(10);
            array[11] = cursor.getString(11);
        }

        return array;
    }

    public void delete() {
        String sql = "DELETE FROM " + MAINTABLE;
        try {
            this.db.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        this.db.close();
    }
}
