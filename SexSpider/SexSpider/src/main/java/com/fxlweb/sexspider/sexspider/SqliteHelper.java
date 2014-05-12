package com.fxlweb.sexspider.sexspider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by fengxl on 2014-4-6 006.
 */
public class SqliteHelper extends SQLiteOpenHelper {
    private static final String DBNAME = "sexspider.db";
    private static final int DBVERSION = 11;
    private static final String LISTTABLE = "ListTable";
    private static final String IMAGETABLE = "ImageTable";
    private static final String MAINTABLE = "MainTable";

    public SqliteHelper(Context context) {
        super(context, DBNAME, null, DBVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + LISTTABLE + " ("
                + "ListID         INTEGER          PRIMARY KEY         AUTOINCREMENT, "
                + "ListTitle      VARCHAR(100)     NOT NULL, "
                + "ListLink       VARCHAR(200)     NOT NULL, "
                + "SiteID         INTEGER          NOT NULL, "
                + "PageID         INTEGER          NOT NULL, "
                + "Favorite       INTEGER          DEFAULT 0, "
                + "IsDown         INTEGER          DEFAULT 0, "
                + "IsDowning      INTEGER          DEFAULT 0, "
                + "IsShow         INTEGER          DEFAULT 1, "
                + "IsRead         INTEGER          DEFAULT 0, "
                + "ImageStart     VARCHAR(50)      NOT NULL, "
                + "ImageEnd       VARCHAR(50)      NOT NULL, "
                + "PageEncode     VARCHAR(10)      NOT NULL)");
        db.execSQL("CREATE TABLE " + IMAGETABLE + " ("
                + "ImageID        INTEGER          PRIMARY KEY         AUTOINCREMENT, "
                + "ImageName      VARCHAR(100)      NOT NULL, "
                + "ImageLink      VARCHAR(200)     NOT NULL, "
                + "ListID         INTEGER          NOT NULL, "
                + "IsDown         INTEGER          DEFAULT 0)");
        db.execSQL("CREATE TABLE " + MAINTABLE + " ("
                + "MainID         INTEGER          PRIMARY KEY         AUTOINCREMENT, "
                + "SiteID         INTEGER          NOT NULL, "
                + "PageID         INTEGER          NOT NULL, "
                + "MainName       VARCHAR(50)      NOT NULL, "
                + "MainSite       VARCHAR(50)      NOT NULL, "
                + "MainLink       VARCHAR(200)     NOT NULL, "
                + "HtmlStart      VARCHAR(50)      NOT NULL, "
                + "HtmlEnd        VARCHAR(50)      NOT NULL, "
                + "ImageStart     VARCHAR(50)      NOT NULL, "
                + "ImageEnd       VARCHAR(50)      NOT NULL, "
                + "PageEncode     VARCHAR(10)      NOT NULL, "
                + "OnInclude      VARCHAR(50)      NULL, "
                + "IsDowning      INTEGER          DEFAULT 0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + LISTTABLE);
        db.execSQL("DROP TABLE IF EXISTS " + IMAGETABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MAINTABLE);
        onCreate(db);
    }

}
