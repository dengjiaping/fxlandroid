package com.aalife.android;

import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
	private String itemName = "";
	private String[] items;
	
	private AutoCompleteTextView etAddItemName = null;
	private ArrayAdapter<CharSequence> nameAdapter = null;
	private List<CharSequence> nameList = null;
	private EditText etAddItemPrice = null;
	private EditText etAddItemBuyDate = null;
	private final int FIRST_REQUEST_CODE = 1;
	
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
		etAddItemName = (AutoCompleteTextView) super.findViewById(R.id.et_add_itemname);
		etAddItemPrice = (EditText) super.findViewById(R.id.et_add_itemprice);
		
		//设置默认值		
		curDate = items[4];
		itemName = items[2];
		etAddItemName.setText(itemName);
		etAddItemPrice.setText(items[3]);
		
		//商品名称List
		itemAccess = new ItemTableAccess(sqlHelper.getReadableDatabase());
		nameList = itemAccess.findAllItemName();
		itemAccess.close();
		nameAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_list_item_1, nameList);
		etAddItemName.setAdapter(nameAdapter);
				
		//编辑按钮
		Button btnDayEdit = (Button) super.findViewById(R.id.btn_day_edit);
		btnDayEdit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				int result = updateItem();
				if(result == 1) {
					Toast.makeText(EditActivity.this, getString(R.string.txt_edit_editsuccess), Toast.LENGTH_SHORT).show();
					
					Intent intent = new Intent();
					intent.putExtra("itemname", itemName);
					EditActivity.this.setResult(Activity.RESULT_OK, intent);
					EditActivity.this.close();
				} else if (result == 0) {
					Toast.makeText(EditActivity.this, getString(R.string.txt_edit_editerror), Toast.LENGTH_SHORT).show();
				}
			}			
		});
		
		//删除按钮
		Button btnDayDelete = (Button) super.findViewById(R.id.btn_day_delete);
		btnDayDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				itemAccess = new ItemTableAccess(sqlHelper.getReadableDatabase());
				Boolean result = itemAccess.deleteItem(Integer.parseInt(items[0]));
				itemAccess.close();
		        if(result) {
		        	sharedHelper.setLocalSync(true);
		        	sharedHelper.setSyncStatus(getString(R.string.txt_home_hassync));
		        	Toast.makeText(EditActivity.this, R.string.txt_day_deletesuccess, Toast.LENGTH_SHORT).show();
		        	
		        	EditActivity.this.setResult(Activity.RESULT_OK);
		        	EditActivity.this.close();
		        } else {
		        	Toast.makeText(EditActivity.this, R.string.txt_day_deleteerror, Toast.LENGTH_SHORT).show();
		        }
			}
		});

		//返回按钮
		ImageButton btnTitleBack = (ImageButton) super.findViewById(R.id.btn_title_back);
		btnTitleBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent();
				intent.putExtra("itemname", itemName);
				EditActivity.this.setResult(Activity.RESULT_OK, intent);
				EditActivity.this.close();
			}			
		});
		
		//选择日期
		etAddItemBuyDate = (EditText) super.findViewById(R.id.et_add_itembuydate);
		etAddItemBuyDate.setText(UtilityHelper.formatDate(curDate, "y-m-d-w2"));
		etAddItemBuyDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				String[] array = curDate.split("-");
				DatePickerDialog dateDialog = new DatePickerDialog(EditActivity.this, new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int month, int day) {
						String date = UtilityHelper.formatDate(year + "-" + (month + 1) + "-" + day, "");
						curDate = date;
						etAddItemBuyDate.setText(UtilityHelper.formatDate(curDate, "y-m-d-w2"));
					}					
				}, Integer.parseInt(array[0]), Integer.parseInt(array[1]) - 1, Integer.parseInt(array[2]));
				dateDialog.show();
			}
		});
		
		//类别编辑
		ImageButton btnAddCatEdit = (ImageButton) super.findViewById(R.id.btn_add_catedit);
		btnAddCatEdit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(EditActivity.this, CategoryEditActivity.class);
				startActivityForResult(intent, FIRST_REQUEST_CODE);
			}			
		});
		
		setListData();
	}

	//关闭this
	protected void close() {
		this.finish();
	}
	
	//返回键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.putExtra("itemname", itemName);
			EditActivity.this.setResult(Activity.RESULT_OK, intent);
			EditActivity.this.close();
		}
		return super.onKeyDown(keyCode, event);
	}
		
	//保存方法
	protected int updateItem() {
		try {
			String catType = spinner.getSelectedItem().toString();
			categoryAccess = new CategoryTableAccess(sqlHelper.getReadableDatabase());
			int catId = categoryAccess.findCatIdByName(catType);
			sharedHelper.setCategory(spinner.getSelectedItemPosition());
			categoryAccess.close();
			
			String itemName = etAddItemName.getText().toString().trim();
			if (itemName.equals("")) {
				Toast.makeText(this, getString(R.string.txt_add_itemname) + getString(R.string.txt_nonull), Toast.LENGTH_SHORT).show();
				return 2;
			}
			this.itemName = itemName;
	
			String itemPrice = etAddItemPrice.getText().toString().trim();
			if (itemPrice.equals("")) {
				Toast.makeText(this, getString(R.string.txt_add_itemprice) + getString(R.string.txt_nonull), Toast.LENGTH_SHORT).show();
				return 2;
			}
			
			itemAccess = new ItemTableAccess(sqlHelper.getReadableDatabase());
			Boolean result = itemAccess.updateItem(Integer.parseInt(items[0]), itemName, itemPrice, curDate, catId);
			itemAccess.close();
	        if(result) {
	        	sharedHelper.setLocalSync(true);
	        	sharedHelper.setSyncStatus(getString(R.string.txt_home_hassync));
	        	
			    return 1;
	        } else {
	        	return 0;
	        }
		} catch (Exception e) {
			Toast.makeText(this, getString(R.string.txt_add_cattype) + getString(R.string.txt_nonull), Toast.LENGTH_SHORT).show();
			return 2;
		}
	}

	//绑定类别下拉
	protected void setListData() {
		categoryAccess = new CategoryTableAccess(sqlHelper.getReadableDatabase());
		list = categoryAccess.findAllCategory();
		adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, list);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		//设置默认值
		String catName = categoryAccess.findCatNameById(Integer.parseInt(items[1]));
		categoryAccess.close();
		setCurCategory(catName);
	}

	//返回处理
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == FIRST_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
			setListData();
		}
	}
	
	//设置默认类别
	protected void setCurCategory(String catName) {
		for (int i = 0; i < list.size(); i++) {
			String s = list.get(i).toString();
			if (s.equals(catName)) {
				spinner.setSelection(i);
				break;
			}
		}
	}

}
