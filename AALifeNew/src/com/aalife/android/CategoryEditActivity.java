package com.aalife.android;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class CategoryEditActivity extends Activity {
	private ListView listCategory = null;
	private SimpleAdapter adapter = null;
	private List<Map<String, String>> list = null;
	private SQLiteOpenHelper sqlHelper = null;
	private CategoryTableAccess categoryAccess = null;
	private EditText etCatName = null;
	private int saveId = 0;
	private SharedHelper sharedHelper = null;
	private LinearLayout layNoItem = null;
	private int position = 0;
	private boolean isClick = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category_edit);
		
		//标题变粗
		TextPaint textPaint = null;
		TextView tvTitleId = (TextView) super.findViewById(R.id.tv_title_id);
		textPaint = tvTitleId.getPaint();
		textPaint.setFakeBoldText(true);
		TextView tvTitleCatName = (TextView) super.findViewById(R.id.tv_title_catname);
		textPaint = tvTitleCatName.getPaint();
		textPaint.setFakeBoldText(true);
		TextView tvTitleOperate = (TextView) super.findViewById(R.id.tv_title_operate);
		textPaint = tvTitleOperate.getPaint();
		textPaint.setFakeBoldText(true);
		
		//数据库
		sqlHelper = new DatabaseHelper(this);
		
		//初始化
		sharedHelper = new SharedHelper(this);		
		etCatName = (EditText) super.findViewById(R.id.et_cat_name);
		listCategory = (ListView) super.findViewById(R.id.list_category);
		listCategory.setDivider(null);
		layNoItem = (LinearLayout) super.findViewById(R.id.lay_noitem);
		layNoItem.setVisibility(View.GONE);		
		
		//绑定类别列表
		categoryAccess = new CategoryTableAccess(sqlHelper.getReadableDatabase());
		list = categoryAccess.findAllCatEdit();
		categoryAccess.close();
		
		//列表为空
		if(list.size() <= 0)
			layNoItem.setVisibility(View.VISIBLE);
		
		//选中显示否
		final boolean[] listCheck = new boolean[list.size()];
		for(int i=0; i < list.size(); i++) {
			Map<String, String> map = list.get(i);
			listCheck[i] = map.get("catdisplay").toString().equals("0") ? false : true;
		}
		
		//列表数据源
		adapter = new SimpleAdapter(this, list, R.layout.list_category, new String[] { "id", "catname", "delete", "catid" }, new int[] { R.id.tv_cat_id, R.id.tv_cat_name, R.id.tv_cat_delete, R.id.tv_cat_catid }) {
			@Override
			public View getView(final int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				TextView tvCatId = (TextView) view.findViewById(R.id.tv_cat_catid);
				final int catId = Integer.parseInt(tvCatId.getText().toString());
				//删除
				TextView tv = (TextView) view.findViewById(R.id.tv_cat_delete);
				tv.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						categoryAccess = new CategoryTableAccess(sqlHelper.getReadableDatabase());
						int result = categoryAccess.delCategory(catId);
						categoryAccess.close();
						if(result == 1) {
				        	CategoryEditActivity.this.onCreate(null);
							Toast.makeText(CategoryEditActivity.this, getString(R.string.txt_day_deletesuccess), Toast.LENGTH_SHORT).show();
						} else if(result == 2) {
							Toast.makeText(CategoryEditActivity.this, getString(R.string.txt_day_deleteuse), Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(CategoryEditActivity.this, getString(R.string.txt_day_deleteerror), Toast.LENGTH_SHORT).show();
						}
						sharedHelper.setLocalSync(true);
			        	sharedHelper.setSyncStatus(getString(R.string.txt_home_hassync));
					}					
				});

		        TextView tvId = (TextView) view.findViewById(R.id.tv_cat_id);
		        TextView tvCatName = (TextView) view.findViewById(R.id.tv_cat_name);
		        LinearLayout layOperate = (LinearLayout) view.findViewById(R.id.lay_cat_operate);
				if(isClick && CategoryEditActivity.this.position == position) {
			        tvId.setBackgroundColor(CategoryEditActivity.this.getResources().getColor(R.color.color_tran_main));
			        tvCatName.setBackgroundColor(CategoryEditActivity.this.getResources().getColor(R.color.color_tran_main));
			        layOperate.setBackgroundColor(CategoryEditActivity.this.getResources().getColor(R.color.color_tran_main));
				} else {
					tvId.setBackgroundColor(CategoryEditActivity.this.getResources().getColor(R.color.color_item_bg));
			        tvCatName.setBackgroundColor(CategoryEditActivity.this.getResources().getColor(R.color.color_item_bg));
			        layOperate.setBackgroundColor(CategoryEditActivity.this.getResources().getColor(R.color.color_item_bg));
				}

				return view;
			}			
		};
		listCategory.setAdapter(adapter);			
		
		//列表点击
		listCategory.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ListView lv = (ListView) parent;
				Map<String, String> map = (Map<String, String>) lv.getItemAtPosition(position);
		        CategoryEditActivity.this.saveId = Integer.parseInt(map.get("catid"));
		        String catName = map.get("catname");
		        CategoryEditActivity.this.etCatName.setText(catName);
		        
		        isClick = true;
		        CategoryEditActivity.this.position = position;
		        adapter.notifyDataSetChanged();		        
			}			
		});
		
		//返回按钮
		ImageButton btnTitleBack = (ImageButton) super.findViewById(R.id.btn_title_back);
		btnTitleBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				CategoryEditActivity.this.setResult(Activity.RESULT_OK);
				CategoryEditActivity.this.close();
			}			
		});
				
		//保存按钮
		Button btnCatSave = (Button) super.findViewById(R.id.btn_cat_save);
		btnCatSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				String catName = CategoryEditActivity.this.etCatName.getText().toString().trim();
				if (catName.equals("")) {
					Toast.makeText(CategoryEditActivity.this, getString(R.string.txt_cat_name) + getString(R.string.txt_nonull), Toast.LENGTH_SHORT).show();
					return;
				}
				categoryAccess = new CategoryTableAccess(sqlHelper.getReadableDatabase());
				Boolean result = categoryAccess.saveCategory(saveId, catName);
				categoryAccess.close();
		        if(result) {
		        	sharedHelper.setLocalSync(true);
		        	sharedHelper.setSyncStatus(getString(R.string.txt_home_hassync));
		        	saveId = 0;
		        	CategoryEditActivity.this.onCreate(null);
		        	Toast.makeText(CategoryEditActivity.this, getString(R.string.txt_add_addsuccess), Toast.LENGTH_SHORT).show();
		        } else {
		        	Toast.makeText(CategoryEditActivity.this, getString(R.string.txt_add_adderror), Toast.LENGTH_SHORT).show();
		        }
			}			
		});
	}

	//关闭this
	protected void close() {
		this.finish();
	}

	//返回键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			CategoryEditActivity.this.setResult(Activity.RESULT_OK);
			CategoryEditActivity.this.close();
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
