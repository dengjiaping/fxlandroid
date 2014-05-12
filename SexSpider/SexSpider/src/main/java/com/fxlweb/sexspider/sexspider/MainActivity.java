package com.fxlweb.sexspider.sexspider;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class MainActivity extends Activity {
    private SQLiteOpenHelper sqlHelper = null;
    private ListView listMain = null;
    private SimpleAdapter listAdapter = null;
    private MainTableAccess mainTableAccess = null;
    private ListTableAccess listTableAccess = null;
    private List<Map<String, String>> list = null;
    private ProgressBar loading = null;
    private final int FIRST_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sqlHelper = new SqliteHelper(this);
        sqlHelper.getWritableDatabase();
        sqlHelper.close();

        loading = (ProgressBar) super.findViewById(R.id.main_loading);

        listMain = (ListView) super.findViewById(R.id.list_main);
        listMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView = (ListView) parent;
                Map<String, String> map = (Map<String, String>) listView.getItemAtPosition(position);
                int siteId = Integer.parseInt(map.get("siteid"));
                int pageId = Integer.parseInt(map.get("pageid"));
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                intent.putExtra("siteid", siteId);
                intent.putExtra("pageid", pageId);
                startActivity(intent);
            }
        });
        /*listMain.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView = (ListView) parent;
                Map<String, String> map = (Map<String, String>) listView.getItemAtPosition(position);
                final int siteId = Integer.parseInt(map.get("siteid"));
                Dialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setMessage(R.string.txt_opreate)
                        .setPositiveButton(R.string.txt_clearlist, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                listTableAccess = new ListTableAccess(sqlHelper.getReadableDatabase());
                                listTableAccess.deleteBySiteId(siteId);
                                listTableAccess.close();
                            }
                        }).create();
                dialog.show();
                Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(1000);
                vibrator.cancel();
                return false;
            }
        });*/

        ImageButton btnRefresh = (ImageButton) super.findViewById(R.id.img_refresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataAndFill();
            }
        });

        getDataAndFill();
    }

    private void getDataAndFill() {
        loading.setVisibility(View.VISIBLE);
        new Thread() {
            @Override
            public void run() {
                boolean result = true;
                try {
                    mainTableAccess = new MainTableAccess(sqlHelper.getReadableDatabase());
                    mainTableAccess.getWebData();
                    mainTableAccess.close();
                } catch(Exception e) {
                    e.printStackTrace();
                    result = false;
                }

                Message msg = new Message();
                msg.what = 1;
                Bundle bundle = new Bundle();
                bundle.putBoolean("data", result);
                msg.setData(bundle);
                handler.sendMessage(msg);
            }
        }.start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case 1:
                    loading.setVisibility(View.GONE);
                    boolean result = msg.getData().getBoolean("data");
                    if(!result) {
                        Toast.makeText(MainActivity.this, R.string.txt_updateerror, Toast.LENGTH_SHORT).show();
                    }

                    mainTableAccess = new MainTableAccess(sqlHelper.getReadableDatabase());
                    list = mainTableAccess.queryAll();
                    mainTableAccess.close();

                    listAdapter = new SimpleAdapter(MainActivity.this, list, R.layout.list_maintable, new String[] { "mainname", "siteid", "pageid" }, new int[] { R.id.list_mainname, R.id.list_siteid, R.id.list_pageid } );
                    listMain.setAdapter(listAdapter);

                    break;
            }
        }
    };
}
