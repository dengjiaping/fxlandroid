package com.fxlweb.sexspider.sexspider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ru.truba.touchgallery.GalleryWidget.FilePagerAdapter;
import ru.truba.touchgallery.GalleryWidget.GalleryViewPager;


public class GalleryActivity extends Activity {
    private GalleryViewPager gallery = null;
    private SQLiteOpenHelper sqlHelper = null;
    private ImageTableAccess imageTableAccess = null;
    private List<ImageTableEntity> list = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        sqlHelper = new SqliteHelper(this);
        gallery = (GalleryViewPager) super.findViewById(R.id.gallery);

        Intent intent = super.getIntent();
        int listId = intent.getIntExtra("listid", 1);
        int siteId = intent.getIntExtra("siteid", 1);
        int pageId = intent.getIntExtra("pageid", 1);
        String listTitle = intent.getStringExtra("listtitle");

        TextView titleText = (TextView) super.findViewById(R.id.title_text);
        titleText.setText(listTitle);

        imageTableAccess = new ImageTableAccess(sqlHelper.getReadableDatabase());
        list = imageTableAccess.queryByListId(listId);
        imageTableAccess.close();

        List<String> items = new ArrayList<String>();
        Iterator iterator = list.iterator();
        while(iterator.hasNext()) {
            ImageTableEntity entity = (ImageTableEntity) iterator.next();
            String fileName = ImageHelper.getFile(entity.getImageName(), siteId, pageId, listId).getPath();
            items.add(fileName);
        }
        FilePagerAdapter pagerAdapter = new FilePagerAdapter(this, items);
        gallery.setOffscreenPageLimit(3);
        gallery.setAdapter(pagerAdapter);
    }

}
