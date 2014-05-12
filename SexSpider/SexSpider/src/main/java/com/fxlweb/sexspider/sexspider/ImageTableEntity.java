package com.fxlweb.sexspider.sexspider;

import java.sql.Timestamp;

/**
 * Created by fengxl on 2014-4-6 006.
 */
public class ImageTableEntity {
    private int ImageId;
    private String ImageName;
    private String ImageLink;
    private int ListId;
    private int isDown;

    public ImageTableEntity() {}

    public int getImageId() {
        return ImageId;
    }

    public void setImageId(int imageId) {
        ImageId = imageId;
    }

    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) { ImageName = imageName; }

    public String getImageLink() { return ImageLink; }

    public void setImageLink(String imageLink) {
        ImageLink = imageLink;
    }

    public int getListId() {
        return ListId;
    }

    public void setListId(int listId) {
        ListId = listId;
    }

    public int getIsDown() { return isDown; }

    public void setIsDown(int isDown) {
        this.isDown = isDown;
    }

}
