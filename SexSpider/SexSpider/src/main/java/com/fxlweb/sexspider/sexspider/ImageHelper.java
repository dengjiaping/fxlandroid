package com.fxlweb.sexspider.sexspider;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by fengxl on 2014-4-6 006.
 */
public class ImageHelper {
    private static final String FOLDER_NAME = "sexspider";
    public static final int HTTP_REQUEST_TIMEOUT_MS = 5 * 1000;

    public ImageHelper() {}

    public static void saveNoMedia(String fileName, int siteId, int pageId, int listId) {
        try {
            File file = getFile(fileName, siteId, pageId, listId);
            if(file != null) file.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean saveImage(String url, String fileName, int siteId, int pageId, int listId) {
        try {
            FileOutputStream output = null;
            URL myUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) myUrl.openConnection();
            conn.setConnectTimeout(HTTP_REQUEST_TIMEOUT_MS);
            if(conn.getResponseCode() == 200) {
                InputStream is = conn.getInputStream();
                File file = getFile(fileName, siteId, pageId, listId);
                if(file != null) output = new FileOutputStream(file, false);
                else return false;

                byte[] buffer = new byte[1024];
                int len = -1;
                while((len = is.read(buffer)) != -1){
                    output.write(buffer, 0, len);
                }

                output.close();
                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static String getFileName(String url) {
        String result = url.substring(url.lastIndexOf("/") + 1);
        return result;
    }

    public static String getExtName(String url) {
        String result = url.substring(url.lastIndexOf("."));
        return result;
    }

    public static Bitmap getImage(String fileName, int siteId, int pageId, int listId) {
        File file = getFile(fileName, siteId, pageId, listId);
        Bitmap bitmap = null;
        if(file != null) bitmap = BitmapFactory.decodeFile(file.getPath());
        return bitmap;
    }

    public static String getDateFileName(String ext) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return sdf.format(new Date()) + ext;
    }

    public static File getFile(String fileName, int siteId, int pageId, int listId) {
        File file = null;
        try {
            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                file = new File(Environment.getExternalStorageDirectory().toString()
                     + File.separator + FOLDER_NAME + File.separator + siteId + File.separator + pageId + File.separator + listId + File.separator + fileName);
                if(!file.getParentFile().exists())  file.getParentFile().mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }
}
