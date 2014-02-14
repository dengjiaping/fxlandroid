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

public class AddActivity extends Activity {
	private ArrayAdapter<CharSequence> adapter = null;
	private List<CharSequence> list = null;
	private Spinner spinner = null;
	private SQLiteOpenHelper sqlHelper = null;
	private CategoryTableAccess categoryAccess = null;
	private ItemTableAccess itemAccess = null;
	private SharedHelper sharedHelper = null;
	private String curDate = "";
	
	private EditText etAddItemName = null;
	private EditText etAddItemPrice = null;
	private EditText etAddItemBuyDate = null;
	private Button btnAddContinue = null;
	private Button btnAddBack = null;
	private ImageButton btnTitleBack = null;
	private ImageButton btnAddCatEdit = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
		
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
		
		//初始化
		sharedHelper = new SharedHelper(this);				
		spinner = (Spinner) super.findViewById(R.id.sp_add_cattype);
		etAddItemName = (EditText) super.findViewById(R.id.et_add_itemname);
		etAddItemPrice = (EditText) super.findViewById(R.id.et_add_itemprice);
		etAddItemBuyDate = (EditText) super.findViewById(R.id.et_add_itembuydate);
		btnAddContinue = (Button) super.findViewById(R.id.btn_add_continue);
		btnAddBack = (Button) super.findViewById(R.id.btn_add_back);
		
		//设置购买日期
		curDate = sharedHelper.getDate();
		etAddItemBuyDate.setText(UtilityHelper.formatDate(curDate, "y-m-d-w"));
		
		//绑定类别下拉
		this.categoryAccess = new CategoryTableAccess(this.sqlHelper.getReadableDatabase());
		this.list = this.categoryAccess.findAllCategory();
		this.categoryAccess.close();
		this.adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, this.list);
		this.adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		this.spinner.setAdapter(this.adapter);
		int category = sharedHelper.getCategory();
		if(category > 0)
			this.spinner.setSelection(category);

		//添加继续按钮
		btnAddContinue.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if(saveItem()) {
					Toast.makeText(AddActivity.this, getString(R.string.txt_add_addsuccess), Toast.LENGTH_SHORT).show();
					AddActivity.this.onCreate(null);
				} else {
					Toast.makeText(AddActivity.this, getString(R.string.txt_add_adderror), Toast.LENGTH_SHORT).show();
				}
			}			
		});
		
		//添加返回按钮
		btnAddBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if(saveItem()) {
					Toast.makeText(AddActivity.this, getString(R.string.txt_add_addsuccess), Toast.LENGTH_SHORT).show();
					AddActivity.this.close();
				} else {
					Toast.makeText(AddActivity.this, getString(R.string.txt_add_adderror), Toast.LENGTH_SHORT).show();
				}
			}			
		});		

		//返回按钮
		btnTitleBack = (ImageButton) super.findViewById(R.id.btn_title_back);
		btnTitleBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				AddActivity.this.close();
			}			
		});
		
		//选择日期
		etAddItemBuyDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				String[] array = curDate.split("-");
				DatePickerDialog dateDialog = new DatePickerDialog(AddActivity.this, new DatePickerDialog.OnDateSetListener() {
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
				Intent intent = new Intent(AddActivity.this, CategoryEditActivity.class);
				startActivity(intent);
			}			
		});
	}

	//关闭this
	protected void close() {
		this.finish();
	}
	
	//保存方法
	protected boolean saveItem() {
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
		Boolean result = this.itemAccess.addItem(itemName, itemPrice, curDate, catId);
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
		this.categoryAccess.close();
		this.adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, this.list);
		this.adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		this.spinner.setAdapter(this.adapter);
		int category = sharedHelper.getCategory();
		if(category > 0)
			this.spinner.setSelection(category);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		setListData();
	}

}
