package com.aalife.android;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
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
	private ImageButton btnTitleBack = null;
	private Button btnCatSave = null;
	private EditText etCatName = null;
	private int saveId = 0;
	private SharedHelper sharedHelper = null;
	
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
		TextView tvTitleDisplay = (TextView) super.findViewById(R.id.tv_title_display);
		textPaint = tvTitleDisplay.getPaint();
		textPaint.setFakeBoldText(true);
		TextView tvTitleOperate = (TextView) super.findViewById(R.id.tv_title_operate);
		textPaint = tvTitleOperate.getPaint();
		textPaint.setFakeBoldText(true);
		
		//数据库
		sqlHelper = new DatabaseHelper(this);
		
		sharedHelper = new SharedHelper(this);
		
		etCatName = (EditText) super.findViewById(R.id.et_cat_name);
		listCategory = (ListView) super.findViewById(R.id.list_category);
		
		//绑定类别列表
		this.categoryAccess = new CategoryTableAccess(this.sqlHelper.getReadableDatabase());
		this.list = this.categoryAccess.findAllCatEdit();
		this.categoryAccess.close();
		
		final boolean[] listCheck = new boolean[list.size()];
		for(int i=0; i<this.list.size(); i++) {
			Map<String, String> map = this.list.get(i);
			listCheck[i] = map.get("catdisplay").toString().equals("0") ? false : true;
		}
		
		this.adapter = new SimpleAdapter(this, this.list, R.layout.list_category, new String[] { "id", "catname", "", "delete", "catid", "catdisplay" }, new int[] { R.id.tv_cat_id, R.id.tv_cat_name, R.id.cb_cat_display, R.id.tv_cat_delete, R.id.tv_cat_catid, R.id.tv_cat_display }) {
			@Override
			public View getView(final int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				TextView tvCatId = (TextView) view.findViewById(R.id.tv_cat_catid);
				final int catId = Integer.parseInt(tvCatId.getText().toString());
				TextView tv = (TextView) view.findViewById(R.id.tv_cat_delete);
				tv.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						CategoryEditActivity.this.categoryAccess = new CategoryTableAccess(CategoryEditActivity.this.sqlHelper.getReadableDatabase());
						Boolean result = CategoryEditActivity.this.categoryAccess.delCategory(catId);
						CategoryEditActivity.this.categoryAccess.close();
						if(result) {
				        	CategoryEditActivity.this.onCreate(null);
							Toast.makeText(CategoryEditActivity.this, getString(R.string.txt_day_deletesuccess), Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(CategoryEditActivity.this, getString(R.string.txt_day_deleteerror), Toast.LENGTH_SHORT).show();
						}
						CategoryEditActivity.this.sharedHelper.setLocalSync(true);
			        	CategoryEditActivity.this.sharedHelper.setSyncStatus(getString(R.string.txt_home_hassync));
					}					
				});
				final CheckBox se = (CheckBox) view.findViewById(R.id.cb_cat_display);
				se.setChecked(listCheck[position]);
				se.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						CategoryEditActivity.this.categoryAccess = new CategoryTableAccess(CategoryEditActivity.this.sqlHelper.getReadableDatabase());
						listCheck[position] = se.isChecked() ? false : true;
						if(se.isChecked())
							CategoryEditActivity.this.categoryAccess.updateCatDisplay(catId, 1);
						else
							CategoryEditActivity.this.categoryAccess.updateCatDisplay(catId, 0);
						CategoryEditActivity.this.categoryAccess.close();
						CategoryEditActivity.this.sharedHelper.setLocalSync(true);
			        	CategoryEditActivity.this.sharedHelper.setSyncStatus(getString(R.string.txt_home_hassync));
					}					
				});

				return view;
			}			
		};
		this.listCategory.setAdapter(this.adapter);			
		
		//列表点击
		listCategory.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ListView lv = (ListView) parent;
		        @SuppressWarnings("unchecked")
				Map<String, String> map = (Map<String, String>) lv.getItemAtPosition(position);
		        CategoryEditActivity.this.saveId = Integer.parseInt(map.get("catid"));
		        String catName = map.get("catname");
		        CategoryEditActivity.this.etCatName.setText(catName);
		        
		        final TextView tvCatId = (TextView) view.findViewById(R.id.tv_cat_id);
		        tvCatId.setBackgroundColor(CategoryEditActivity.this.getResources().getColor(R.color.color_tran_main));
		        final TextView tvCatName = (TextView) view.findViewById(R.id.tv_cat_name);
		        tvCatName.setBackgroundColor(CategoryEditActivity.this.getResources().getColor(R.color.color_tran_main));
		        final LinearLayout layDisplay = (LinearLayout) view.findViewById(R.id.lay_cat_display);
		        layDisplay.setBackgroundColor(CategoryEditActivity.this.getResources().getColor(R.color.color_tran_main));
		        final LinearLayout layOperate = (LinearLayout) view.findViewById(R.id.lay_cat_operate);
		        layOperate.setBackgroundColor(CategoryEditActivity.this.getResources().getColor(R.color.color_tran_main));
			}			
		});
		
		//返回按钮
		btnTitleBack = (ImageButton) super.findViewById(R.id.btn_title_back);
		btnTitleBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				CategoryEditActivity.this.close();
			}			
		});
		
		//保存按钮
		btnCatSave = (Button) super.findViewById(R.id.btn_cat_save);
		btnCatSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				String catName = CategoryEditActivity.this.etCatName.getText().toString().trim();
				if (catName.equals("")) {
					Toast.makeText(CategoryEditActivity.this, getString(R.string.txt_cat_name) + getString(R.string.txt_nonull), Toast.LENGTH_SHORT).show();
					return;
				}
				CategoryEditActivity.this.categoryAccess = new CategoryTableAccess(CategoryEditActivity.this.sqlHelper.getReadableDatabase());
				Boolean result = CategoryEditActivity.this.categoryAccess.saveCategory(saveId, catName, CategoryEditActivity.this.list.size() + 1);
				CategoryEditActivity.this.categoryAccess.close();
		        if(result) {
		        	CategoryEditActivity.this.sharedHelper.setLocalSync(true);
		        	CategoryEditActivity.this.sharedHelper.setSyncStatus(getString(R.string.txt_home_hassync));
		        	CategoryEditActivity.this.saveId = 0;
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.category_edit, menu);
		return true;
	}

}
