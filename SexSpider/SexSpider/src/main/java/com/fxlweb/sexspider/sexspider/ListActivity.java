package com.fxlweb.sexspider.sexspider;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


public class ListActivity extends Activity {
    private SQLiteOpenHelper sqlHelper = null;
    private ListTableAccess listTableAccess = null;
    private MainTableAccess mainTableAccess = null;
    private List<ListTableEntity> list = null;
    private List<ListTableEntity> newList = null;
    private ListView listView = null;
    private ListAdapter listAdapter = null;
    private LinearLayout empty = null;
    private ProgressBar loading = null;
    private boolean isKeep = true;
    private String[] mainArray = new String[12];
    private final int FIRST_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Intent intent = super.getIntent();
        int siteId = intent.getIntExtra("siteid", 1);
        int pageId = intent.getIntExtra("pageid", 1);

        sqlHelper = new SqliteHelper(this);
        empty = (LinearLayout) super.findViewById(R.id.list_empty);
        listView = (ListView) super.findViewById(R.id.list_listview);
        loading = (ProgressBar) super.findViewById(R.id.list_loading);

        mainTableAccess = new MainTableAccess(sqlHelper.getReadableDatabase());
        mainArray = mainTableAccess.queryById(siteId, pageId);
        mainTableAccess.close();

        listTableAccess = new ListTableAccess(sqlHelper.getReadableDatabase());
        list = listTableAccess.queryById(Integer.parseInt(mainArray[0]), Integer.parseInt(mainArray[1]));
        listTableAccess.close();
        if(list.size() <= 0) {
            empty.setVisibility(View.VISIBLE);
        }
        listAdapter = new ListAdapter(this, list);
        listView.setAdapter(listAdapter);

        new Thread() {
            @Override
            public void run() {
                Iterator iterator = list.iterator();
                while(iterator.hasNext()) {
                    ListTableEntity listTableEntity = (ListTableEntity) iterator.next();
                    if(listTableEntity.getIsDowning() == 1) {
                        downAndSaveImage(listTableEntity);
                    }
                }
            }
        }.start();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView = (ListView) parent;
                final ListTableEntity listTableEntity = (ListTableEntity) listView.getItemAtPosition(position);
                int listId = listTableEntity.getListId();
                int isDown = listTableEntity.getIsDown();
                int isDowning = listTableEntity.getIsDowning();
                String listTitle = listTableEntity.getListTitle();
                if(isDowning == 1) {
                    Toast.makeText(ListActivity.this, R.string.txt_isdowning, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(isDown == 0) {
                    Toast.makeText(ListActivity.this, R.string.txt_nodown, Toast.LENGTH_SHORT).show();
                    new Thread() {
                        @Override
                        public void run() {
                            downAndSaveImage(listTableEntity);
                        }
                    }.start();

                    return;
                }

                int entityIndex = list.indexOf(listTableEntity);
                listTableEntity.setIsRead(1);
                list.set(entityIndex, listTableEntity);
                ListTableAccess listAccess = new ListTableAccess(sqlHelper.getReadableDatabase());
                listAccess.update(listTableEntity);
                listAccess.close();

                listAdapter.notifyDataSetChanged();

                Intent intent = new Intent(ListActivity.this, GalleryActivity.class);
                intent.putExtra("listid", listId);
                intent.putExtra("siteid", Integer.parseInt(mainArray[0]));
                intent.putExtra("pageid", Integer.parseInt(mainArray[1]));
                intent.putExtra("listtitle", listTitle);
                startActivityForResult(intent, FIRST_REQUEST_CODE);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView = (ListView) parent;
                final ListTableEntity entity = (ListTableEntity) listView.getItemAtPosition(position);
                Dialog dialog = new AlertDialog.Builder(ListActivity.this)
                        .setMessage(R.string.txt_opreate)
                        .setPositiveButton(R.string.txt_deletethis, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                entity.setIsShow(0);
                                listTableAccess = new ListTableAccess(sqlHelper.getReadableDatabase());
                                listTableAccess.update(entity);
                                listTableAccess.close();

                                list.remove(entity);
                                listAdapter.notifyDataSetChanged();
                            }
                        }).setNegativeButton(R.string.txt_redown, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                                ImageTableAccess imageAccess = new ImageTableAccess(sqlHelper.getReadableDatabase());
                                imageAccess.deleteByListId(entity.getListId());
                                imageAccess.close();

                                int index = list.indexOf(entity);
                                entity.setIsDown(0);
                                entity.setIsDowning(1);
                                entity.setIsRead(0);
                                list.set(index, entity);
                                new Thread() {
                                    @Override
                                    public void run() {
                                        downAndSaveImage(entity);
                                    }
                                }.start();
                            }
                        }).create();
                dialog.show();
                Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(1000);
                vibrator.cancel();
                return false;
            }
        });

        ImageButton btnRefresh = (ImageButton) super.findViewById(R.id.img_refresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                empty.setVisibility(View.GONE);
                new Thread() {
                    @Override
                    public void run() {
                        newList = new ArrayList<ListTableEntity>();
                        try {
                            String url = mainArray[4];
                            String result = HttpHelper.getHttp(url, mainArray[9]);
                            result = HtmlHelper.getString(mainArray[3], url, result, mainArray[5], mainArray[6], mainArray[10]);
                            String[] array = result.split("@@");
                            for (int i = 0; i < array.length; i++) {
                                String[] arr = array[i].split("\\|\\|");
                                ListTableEntity entity = new ListTableEntity();
                                entity.setListTitle(arr[0]);
                                entity.setListLink(arr[1]);
                                entity.setSiteId(Integer.parseInt(mainArray[0]));
                                entity.setPageId(Integer.parseInt(mainArray[1]));
                                entity.setFavorite(0);
                                entity.setIsDown(0);
                                entity.setIsDowning(0);
                                entity.setIsShow(1);
                                entity.setIsRead(0);
                                entity.setImageStart(mainArray[7]);
                                entity.setImageEnd(mainArray[8]);
                                entity.setPageEncode(mainArray[9]);
                                newList.add(entity);
                            }
                        } catch(Exception e) {
                            e.printStackTrace();
                        }

                        boolean bool = false;
                        if(newList.size() > 0) bool = true;
                        Message msg = new Message();
                        msg.what = 1;
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("data", bool);
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                    }
                }.start();
            }
        });

        ImageButton btnDownload = (ImageButton) super.findViewById(R.id.img_download);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        Iterator iterator = list.iterator();
                        while(isKeep && iterator.hasNext()) {
                            mainArray[11] = "1";
                            ListTableEntity listTableEntity = (ListTableEntity) iterator.next();
                            if(listTableEntity.getIsDown() == 1) continue;
                            downAndSaveImage(listTableEntity);
                        }
                        listTableAccess = new ListTableAccess(sqlHelper.getReadableDatabase());
                        int count = listTableAccess.queryByIsDowning(Integer.parseInt(mainArray[0]), Integer.parseInt(mainArray[1])).size();
                        listTableAccess.close();
                        if(count == 0) {
                            mainArray[11] = "0";
                            mainTableAccess = new MainTableAccess(sqlHelper.getReadableDatabase());
                            mainTableAccess.updateIsDowning(Integer.parseInt(mainArray[0]), Integer.parseInt(mainArray[1]), 0);
                            mainTableAccess.close();
                        }
                    }
                }.start();
            }
        });
    }

    private void downAndSaveImage(ListTableEntity listTableEntity) {
        int entityIndex = list.indexOf(listTableEntity);
        listTableEntity.setIsDowning(1);
        list.set(entityIndex, listTableEntity);
        ListTableAccess listAccess = new ListTableAccess(sqlHelper.getReadableDatabase());
        listAccess.update(listTableEntity);
        listAccess.close();

        Message msgStart = new Message();
        msgStart.what = 2;
        handler.sendMessage(msgStart);

        String success = "";
        try {
            String url = listTableEntity.getListLink();
            String encode = listTableEntity.getPageEncode();
            String result = HttpHelper.getHttp(url, encode);
            result = HtmlHelper.getImages(mainArray[3], result, listTableEntity.getImageStart(), listTableEntity.getImageEnd());
            String[] array = result.split("@@");

            //创建.nomedia文件，禁止系统图库搜索图片
            ImageHelper.saveNoMedia(".nomedia", listTableEntity.getSiteId(), listTableEntity.getPageId(), listTableEntity.getListId());

            for (int i = 0; i < array.length; i++) {
                ImageTableEntity imageTableEntity = new ImageTableEntity();
                imageTableEntity.setImageLink(array[i]);
                imageTableEntity.setListId(listTableEntity.getListId());
                String fileFullName = ImageHelper.getFileName(array[i]);
                String fileName = listTableEntity.getSiteId() + "" + listTableEntity.getPageId() + "" + listTableEntity.getListId() + "-" + (i + 1);
                fileName += fileFullName.equals("") ? ".jpg" : ImageHelper.getExtName(array[i]);
                imageTableEntity.setImageName(fileName);

                boolean bool = ImageHelper.saveImage(array[i], fileName, listTableEntity.getSiteId(), listTableEntity.getPageId(), listTableEntity.getListId());
                if (bool) imageTableEntity.setIsDown(1);
                else continue;

                ImageTableAccess imageAccess = new ImageTableAccess(sqlHelper.getReadableDatabase());
                bool = imageAccess.insert(imageTableEntity);
                imageAccess.close();
                success += bool ? "1" : "0";
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        listTableEntity.setIsDown(success.indexOf("1") != -1 ? 1 : 0);
        listTableEntity.setIsDowning(0);
        list.set(entityIndex, listTableEntity);
        listAccess = new ListTableAccess(sqlHelper.getReadableDatabase());
        listAccess.update(listTableEntity);
        listAccess.close();

        Message msgEnd = new Message();
        msgEnd.what = 3;
        handler.sendMessage(msgEnd);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(mainArray[11].equals("1")) {
                Dialog dialog = new AlertDialog.Builder(ListActivity.this)
                    .setMessage(R.string.txt_keepdown)
                    .setPositiveButton(R.string.txt_yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            isKeep = true;
                            MainTableAccess mainAccess = new MainTableAccess(sqlHelper.getReadableDatabase());
                            mainAccess.updateIsDowning(Integer.parseInt(mainArray[0]), Integer.parseInt(mainArray[1]), 1);
                            mainAccess.close();
                            finish();
                        }
                    }).setNegativeButton(R.string.txt_no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            isKeep = false;
                            MainTableAccess mainAccess = new MainTableAccess(sqlHelper.getReadableDatabase());
                            mainAccess.updateIsDowning(Integer.parseInt(mainArray[0]), Integer.parseInt(mainArray[1]), 0);
                            mainAccess.close();
                            dialog.cancel();
                            finish();
                        }
                    }).create();
                dialog.show();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == FIRST_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            listAdapter.notifyDataSetChanged();
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case 1:
                    loading.setVisibility(View.GONE);
                    boolean bool = msg.getData().getBoolean("data");
                    if (bool) {
                        empty.setVisibility(View.GONE);
                        listTableAccess = new ListTableAccess(sqlHelper.getReadableDatabase());
                        List<ListTableEntity> listAll = listTableAccess.queryBySiteId(Integer.parseInt(mainArray[0]));
                        listTableAccess.close();
                        Iterator iterator = listAll.iterator();
                        while (iterator.hasNext()) {
                            ListTableEntity listTableEntity = (ListTableEntity) iterator.next();
                            Iterator it = newList.iterator();
                            while (it.hasNext()) {
                                ListTableEntity entity = (ListTableEntity) it.next();
                                if (listTableEntity.isExist(entity)) {
                                    newList.remove(entity);
                                    break;
                                }
                            }
                        }
                    } else {
                        if(list.size() == 0) empty.setVisibility(View.VISIBLE);
                        Toast.makeText(ListActivity.this, R.string.txt_refresherror, Toast.LENGTH_SHORT).show();
                    }
                    if (newList.size() > 0) {
                        listTableAccess = new ListTableAccess(sqlHelper.getReadableDatabase());
                        Collections.reverse(newList);
                        Iterator it = newList.iterator();
                        while (it.hasNext()) {
                            ListTableEntity entity = (ListTableEntity) it.next();
                            listTableAccess.insert(entity);
                        }

                        list = listTableAccess.queryById(Integer.parseInt(mainArray[0]), Integer.parseInt(mainArray[1]));
                        listTableAccess.close();
                    } else {
                        Toast.makeText(ListActivity.this, R.string.txt_refreshempty, Toast.LENGTH_SHORT).show();
                    }
                    listAdapter.updateList(list);
                    listAdapter.notifyDataSetChanged();
                    break;
                case 2:
                case 3:
                    listAdapter.updateList(list);
                    listAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    private class ListAdapter extends BaseAdapter {
        private Context context = null;
        private List<ListTableEntity> list = null;
        private LayoutInflater layout = null;

        public ListAdapter(Context context, List<ListTableEntity> list) {
            this.context = context;
            this.list = list;
            this.layout = LayoutInflater.from(this.context);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public ListTableEntity getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ListEntity listEntity = null;
            if(convertView == null) {
                listEntity = new ListEntity();
                convertView = layout.inflate(R.layout.list_listtable, null);
                listEntity.listTitle = (TextView) convertView.findViewById(R.id.list_listtitle);
                listEntity.listLink = (TextView) convertView.findViewById(R.id.list_listlink);
                listEntity.listId = (TextView) convertView.findViewById(R.id.list_listid);
                listEntity.isDown = (TextView) convertView.findViewById(R.id.list_isdown);
                listEntity.isDowning = (ProgressBar) convertView.findViewById(R.id.list_isdowning);
                listEntity.isRead = (TextView) convertView.findViewById(R.id.list_isread);
                convertView.setTag(listEntity);
            } else {
                listEntity = (ListEntity) convertView.getTag();
            }

            ListTableEntity listTableEntity = getItem(position);
            String listTitle = listTableEntity.getListTitle();
            String listLink = listTableEntity.getListLink();
            int listId = listTableEntity.getListId();
            int isDown = listTableEntity.getIsDown();
            int isDowning = listTableEntity.getIsDowning();
            int isRead = listTableEntity.getIsRead();
            listEntity.listTitle.setText(listTitle);
            listEntity.listLink.setText(listLink);
            if(isDown == 0) {
                listEntity.listTitle.setTextColor(this.context.getResources().getColor(R.color.list_notdown_text));
            } else if(isRead == 1) {
                listEntity.listTitle.setTextColor(this.context.getResources().getColor(R.color.list_isread_text));
            } else {
                listEntity.listTitle.setTextColor(this.context.getResources().getColor(R.color.list_isdown_text));
            }
            if(isDowning == 1) {
                listEntity.isDowning.setVisibility(View.VISIBLE);
            } else {
                listEntity.isDowning.setVisibility(View.GONE);
            }

            return convertView;
        }

        public void updateList(List<ListTableEntity> newList) {
            this.list = newList;
        }

        private class ListEntity {
            private TextView listTitle;
            private TextView listLink;
            private TextView listId;
            private TextView isDown;
            private ProgressBar isDowning;
            private TextView isRead;
        }
    }

}
