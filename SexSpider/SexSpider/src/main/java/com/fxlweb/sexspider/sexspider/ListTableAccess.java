package com.fxlweb.sexspider.sexspider;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fengxl on 2014-4-6 006.
 */
public class ListTableAccess {
    private static final String LISTTABLE = "ListTable";
    private SQLiteDatabase db = null;

    public ListTableAccess(SQLiteDatabase db) {
        this.db = db;
    }

    public boolean insert(ListTableEntity entity) {
        String listTitle = entity.getListTitle();
        String listLink = entity.getListLink();
        int siteId = entity.getSiteId();
        int pageId = entity.getPageId();
        String imageStart = entity.getImageStart();
        String imageEnd = entity.getImageEnd();
        String pageEncode = entity.getPageEncode();

        String sql = "INSERT INTO " + LISTTABLE + "(ListTitle, ListLink, SiteID, PageID, ImageStart, ImageEnd, PageEncode) "
                   + "VALUES ('" + listTitle + "', '" + listLink + "', " + siteId + ", " + pageId + ", '" + imageStart + "', '" + imageEnd + "', '" + pageEncode + "')";
        try {
            this.db.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean update(ListTableEntity entity) {
        int listId = entity.getListId();
        int isDown = entity.getIsDown();
        int isDowning = entity.getIsDowning();
        int isRead = entity.getIsRead();
        int isShow = entity.getIsShow();

        String sql = "UPDATE " + LISTTABLE + " SET IsDown=" + isDown + ", IsDowning=" + isDowning + ", IsRead=" + isRead + ", IsShow=" + isShow
                   + " WHERE ListID=" + listId;
        try {
            this.db.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public List<ListTableEntity> queryById(int siteId, int pageId) {
        List<ListTableEntity> list = new ArrayList<ListTableEntity>();
        String sql = "SELECT ListID, ListTitle, ListLink, SiteID, PageID, Favorite, IsDown, IsDowning, IsShow, IsRead, ImageStart, ImageEnd, PageEncode FROM " + LISTTABLE
                   + " WHERE SiteID=" + siteId + " AND PageID=" + pageId + " AND IsShow=1 ORDER BY ListID DESC";
        Cursor cursor = this.db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            ListTableEntity entity = new ListTableEntity();
            entity.setListId(cursor.getInt(0));
            entity.setListTitle(cursor.getString(1));
            entity.setListLink(cursor.getString(2));
            entity.setSiteId(cursor.getInt(3));
            entity.setPageId(cursor.getInt(4));
            entity.setFavorite(cursor.getInt(5));
            entity.setIsDown(cursor.getInt(6));
            entity.setIsDowning(cursor.getInt(7));
            entity.setIsShow(cursor.getInt(8));
            entity.setIsRead(cursor.getInt(9));
            entity.setImageStart(cursor.getString(10));
            entity.setImageEnd(cursor.getString(11));
            entity.setPageEncode(cursor.getString(12));

            list.add(entity);
        }

        return list;
    }

    public List<ListTableEntity> queryByIsDowning(int siteId, int pageId) {
        List<ListTableEntity> list = new ArrayList<ListTableEntity>();
        String sql = "SELECT ListID, ListTitle, ListLink, SiteID, PageID, Favorite, IsDown, IsDowning, IsShow, IsRead, ImageStart, ImageEnd, PageEncode FROM " + LISTTABLE
                + " WHERE SiteID=" + siteId + " AND PageID=" + pageId + " AND IsDowning=1 ORDER BY ListID DESC";
        Cursor cursor = this.db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            ListTableEntity entity = new ListTableEntity();
            entity.setListId(cursor.getInt(0));
            entity.setListTitle(cursor.getString(1));
            entity.setListLink(cursor.getString(2));
            entity.setSiteId(cursor.getInt(3));
            entity.setPageId(cursor.getInt(4));
            entity.setFavorite(cursor.getInt(5));
            entity.setIsDown(cursor.getInt(6));
            entity.setIsDowning(cursor.getInt(7));
            entity.setIsShow(cursor.getInt(8));
            entity.setIsRead(cursor.getInt(9));
            entity.setImageStart(cursor.getString(10));
            entity.setImageEnd(cursor.getString(11));
            entity.setPageEncode(cursor.getString(12));

            list.add(entity);
        }

        return list;
    }

    public List<ListTableEntity> queryBySiteId(int siteId) {
        List<ListTableEntity> list = new ArrayList<ListTableEntity>();
        String sql = "SELECT ListID, ListTitle, ListLink, SiteID, PageID, Favorite, IsDown, IsDowning, IsShow, IsRead, ImageStart, ImageEnd, PageEncode FROM " + LISTTABLE
                   + " WHERE SiteID=" + siteId + " ORDER BY ListID DESC";
        Cursor cursor = this.db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            ListTableEntity entity = new ListTableEntity();
            entity.setListId(cursor.getInt(0));
            entity.setListTitle(cursor.getString(1));
            entity.setListLink(cursor.getString(2));
            entity.setSiteId(cursor.getInt(3));
            entity.setPageId(cursor.getInt(4));
            entity.setFavorite(cursor.getInt(5));
            entity.setIsDown(cursor.getInt(6));
            entity.setIsDowning(cursor.getInt(7));
            entity.setIsShow(cursor.getInt(8));
            entity.setIsRead(cursor.getInt(9));
            entity.setImageStart(cursor.getString(10));
            entity.setImageEnd(cursor.getString(11));
            entity.setPageEncode(cursor.getString(12));

            list.add(entity);
        }

        return list;
    }

    public void deleteBySiteId(int siteId) {
        String sql = "DELETE FROM " + LISTTABLE + " WHERE SiteID=" + siteId;
        try {
            this.db.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        db.close();
    }

}
