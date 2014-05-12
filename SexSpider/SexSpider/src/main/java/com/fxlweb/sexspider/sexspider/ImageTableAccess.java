package com.fxlweb.sexspider.sexspider;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fengxl on 2014-4-6 006.
 */
public class ImageTableAccess {
    private static final String IMAGETABLE = "ImageTable";
    private SQLiteDatabase db = null;

    public ImageTableAccess(SQLiteDatabase db) {
        this.db = db;
    }

    public boolean insert(ImageTableEntity entity) {
        String imageName = entity.getImageName();
        String imageLink = entity.getImageLink();
        int listId = entity.getListId();
        int isDown = entity.getIsDown();

        String sql = "INSERT INTO " + IMAGETABLE + "(ImageName, ImageLink, ListID, IsDown) "
                   + "VALUES ( '" + imageName + "', '" + imageLink + "'," + listId + ", " + isDown + ")";
        try {
            this.db.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public List<ImageTableEntity> queryByListId(int listId) {
        List<ImageTableEntity> list = new ArrayList<ImageTableEntity>();
        String sql = "SELECT ImageID, ImageName, ImageLink, ListID, IsDown FROM " + IMAGETABLE
                   + " WHERE ListID=" + listId;
        Cursor cursor = this.db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            ImageTableEntity entity = new ImageTableEntity();
            entity.setImageId(cursor.getInt(0));
            entity.setImageName(cursor.getString(1));
            entity.setImageLink(cursor.getString(2));
            entity.setListId(cursor.getInt(3));
            entity.setIsDown(cursor.getInt(4));
            list.add(entity);
        }

        return list;
    }

    public void deleteByListId(int listId) {
        String sql = "DELETE FROM " + IMAGETABLE + " WHERE ListID=" + listId;
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
