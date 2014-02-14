package com.aalife.android;

import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EditActivity extends Activity {
	private ArrayAdapter<CharSequence> adapter = null;
	private List<CharSequence> list = null;
	private Spinner spinner = null;
	private SQLiteOpenHelper sqlHelper = null;
	private CategoryTableAccess categoryAccess = null;
	private ItemTableAccess itemAccess = null;
	private SharedHelper sharedHelper = null;
	private String curDate = "";
	private String[] items;
	
	private EditText etAddItemName = null;
	private EditText etAddItemPrice = null;
	private EditText etAddItemBuyDate = null;
	private Button btnDayEdit = null;
	private Button btnDayDelete = null;
	private ImageButton btnTitleBack = null;
	private ImageButton btnAddCatEdit = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);
		
		//标题变粗
		TextPaint textPaint = null;
		TextView tvCatType = (TextView) super.findViewById(R.id.tv_cattype);
		textPaint = tvCatType.getPaint();
		textPaint.setFakeBoldText(true);
		TextView tvItemName = (TextView) super.findViewById(R.id.tv_itemname);
		textPaint = tvItemName.getPaint();
		textPaint.setFakeBoldText(true);
		TextView tvItemPrice = (TextView) super.findViewById(R.id.tv_itemprice);
		textPaint = tvItemPrice.getPaint();
		textPaint.setFakeBoldText(true);
		TextView tvItemBuyDate = (TextView) super.findViewById(R.id.tv_itembuydate);
		textPaint = tvItemBuyDate.getPaint();
		textPaint.setFakeBoldText(true);
				
		//数据库
		sqlHelper = new DatabaseHelper(this);
		
		//取值入的数组
		Intent intent = super.getIntent();
		items = intent.getStringArrayExtra("items");
		
		//初始化
		sharedHelper = new SharedHelper(this);
		spinner = (Spinner) super.findViewById(R.id.sp_add_cattype);
		etAddItemName = (EditText) super.findViewById(R.id.et_add_itemname);
		etAddItemPrice = (EditText) super.findViewById(R.id.et_add_itemprice);
		etAddItemBuyDate = (EditText) super.findViewById(R.id.et_add_itembuydate);
		btnDayEdit = (Button) super.findViewById(R.id.btn_day_edit);
		btnDayDelete = (Button) super.findViewById(R.id.btn_day_delete);
		
		//设置默认值		
		curDate = items[4];
		etAddItemName.setText(items[2]);
		etAddItemPrice.setText(items[3]);
		etAddItemBuyDate.setText(UtilityHelper.formatDate(curDate, "y-m-d-w"));
		
		//编辑按钮
		btnDayEdit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if(updateItem()) {
					Toast.makeText(EditActivity.this, getString(R.string.txt_edit_editsuccess), Toast.LENGTH_SHORT).show();
					EditActivity.this.close();
				} else {
					Toast.makeText(EditActivity.this, getString(R.string.txt_edit_editerror), Toast.LENGTH_SHORT).show();
				}
			}			
		});
		
		//删除按钮
		btnDayDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				EditActivity.this.itemAccess = new ItemTableAccess(EditActivity.this.sqlHelper.getReadableDatabase());
				Boolean result = EditActivity.this.itemAccess.deleteItem(Integer.parseInt(items[0]));
				EditActivity.this.itemAccess.close();
		        if(result) {
		        	sharedHelper.setLocalSync(true);
		        	sharedHelper.setSyncStatus(getString(R.string.txt_home_hassync));
		        	Toast.makeText(EditActivity.this, R.string.txt_day_deletesuccess, Toast.LENGTH_SHORT).show();
		        	EditActivity.this.close();
		        } else {
		        	Toast.makeText(EditActivity.this, R.string.txt_day_deleteerror, Toast.LENGTH_SHORT).show();
		        }
			}
		});

		//返回按钮
		btnTitleBack = (ImageButton) super.findViewById(R.id.btn_title_back);
		btnTitleBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				EditActivity.this.close();
			}			
		});
		
		//选择日期
		etAddItemBuyDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				String[] array = curDate.split("-");
				DatePickerDialog dateDialog = new DatePickerDialog(EditActivity.this, new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int month, int day) {
						String date = UtilityHelper.formatDate(year + "-" + (month + 1) + "-" + day, "");
						curDate = date;
						etAddItemBuyDate.setText(UtilityHelper.formatDate(curDate, "y-m-d-w"));
					}					
				}, Integer.parseInt(array[0]), Integer.parseInt(array[1]) - 1, Integer.parseInt(array[2]));
				dateDialog.show();
			}
		});
		
		//类别编辑
		btnAddCatEdit = (ImageButton) super.findViewById(R.id.btn_add_catedit);
		btnAddCatEdit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(EditActivity.this, CategoryEditActivity.class);
				startActivity(intent);
			}			
		});
	}

	//关闭this
	protected void close() {
		this.finish();
	}
	
	//保存方法
	protected boolean updateItem() {
		String catType = this.spinner.getSelectedItem().toString();
		this.categoryAccess = new CategoryTableAccess(this.sqlHelper.getReadableDatabase());
		int catId = this.categoryAccess.findCatIdByName(catType);
		sharedHelper.setCategory(this.spinner.getSelectedItemPosition());
		this.categoryAccess.close();
		
		String itemName = this.etAddItemName.getText().toString().trim();
		if (itemName.equals("")) {
			Toast.makeText(this, getString(R.string.txt_add_itemname) + getString(R.string.txt_nonull), Toast.LENGTH_SHORT).show();
			return false;
		}

		String itemPrice = this.etAddItemPrice.getText().toString().trim();
		if (itemPrice.equals("")) {
			Toast.makeText(this, getString(R.string.txt_add_itemprice) + getString(R.string.txt_nonull), Toast.LENGTH_SHORT).show();
			return false;
		}
		
		this.itemAccess = new ItemTableAccess(this.sqlHelper.getReadableDatabase());
		Boolean result = this.itemAccess.updateItem(Integer.parseInt(items[0]), itemName, itemPrice, curDate, catId);
		this.itemAccess.close();
        if(result) {
        	sharedHelper.setDate(curDate);
        	sharedHelper.setLocalSync(true);
        	sharedHelper.setSyncStatus(getString(R.string.txt_home_hassync));
		    return true;
        } else {
        	return false;
        }
	}

	//绑定类别下拉
	protected void setListData() {
		this.categoryAccess = new CategoryTableAccess(this.sqlHelper.getReadableDatabase());
		this.list = this.categoryAccess.findAllCategory();
		this.adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, this.list);
		this.adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		this.spinner.setAdapter(this.adapter);

		//设置默认值
		String catName = this.categoryAccess.findCatNameById(Integer.parseInt(items[1]));
		this.categoryAccess.close();
		for (int i = 0; i < this.list.size(); i++) {
			String s = this.list.get(i).toString();
			if (s.equals(catName)) {
				this.spinner.setSelection(i);
				break;
			}
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		setListData();
	}

}
