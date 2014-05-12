package com.fxlweb.sexspider.sexspider;

import java.sql.Timestamp;

/**
 * Created by fengxl on 2014-4-6 006.
 */
public class ListTableEntity {
    private int listId;
    private String listTitle;
    private String listLink;
    private int siteId;
    private int pageId;
    private int favorite;
    private int isDown;
    private int isDowning;
    private int isShow;
    private int isRead;
    private String imageStart;
    private String imageEnd;
    private String pageEncode;

    public ListTableEntity() {}

    public int getListId() { return listId; }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public String getListTitle() {
        return listTitle;
    }

    public void setListTitle(String listTitle) {
        this.listTitle = listTitle;
    }

    public String getListLink() {
        return listLink;
    }

    public void setListLink(String listLink) {
        this.listLink = listLink;
    }

    public int getSiteId() { return siteId; }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    public int getPageId() { return pageId; }

    public void setPageId(int pageId) {
        this.pageId = pageId;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public int getIsDown() {
        return isDown;
    }

    public void setIsDown(int isDown) {
        this.isDown = isDown;
    }

    public int getIsDowning() {
        return isDowning;
    }

    public void setIsDowning(int isDowning) {
        this.isDowning = isDowning;
    }

    public int getIsShow() {
        return isShow;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public String getImageStart() {
        return imageStart;
    }

    public void setImageStart(String imageStart) {
        this.imageStart = imageStart;
    }

    public String getImageEnd() {
        return imageEnd;
    }

    public void setImageEnd(String imageEnd) {
        this.imageEnd = imageEnd;
    }

    public String getPageEncode() { return pageEncode; }

    public void setPageEncode(String pageEncode) {
        this.pageEncode = pageEncode;
    }

    //检查已存在标题
    public boolean isExist(ListTableEntity entity) {
        return this.listTitle.equals(entity.listTitle);
    }

}
