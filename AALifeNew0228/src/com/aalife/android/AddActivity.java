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
	private String fromDate = "";
	
	private EditText etAddItemName = null;
	private EditText etAddItemPrice = null;
	private EditText etAddItemBuyDate = null;
	private Button btnAddContinue = null;
	private Button btnAddBack = null;
	private ImageButton btnTitleBack = null;
	private ImageButton btnAddCatEdit = null;
	private final int FIRST_REQUEST_CODE = 1;
	
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

		//获取数据
		Intent intent = super.getIntent();
		fromDate = intent.getStringExtra("date");
		if(fromDate != null && !fromDate.equals(""))
			curDate = fromDate;
		else
			curDate = UtilityHelper.getCurDate();

		//绑定类别下拉
		categoryAccess = new CategoryTableAccess(sqlHelper.getReadableDatabase());
		list = categoryAccess.findAllCategory();
		categoryAccess.close();
		adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, list);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		int category = sharedHelper.getCategory();
		if(category > 0)
			spinner.setSelection(category);

		//添加继续按钮
		btnAddContinue = (Button) super.findViewById(R.id.btn_add_continue);
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
		btnAddBack = (Button) super.findViewById(R.id.btn_add_back);
		btnAddBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if(saveItem()) {
					Toast.makeText(AddActivity.this, getString(R.string.txt_add_addsuccess), Toast.LENGTH_SHORT).show();
					AddActivity.this.setResult(Activity.RESULT_OK);
					AddActivity.this.close();
				} else {
					Toast.makeText(AddActivity.this, getString(R.string.txt_add_adderror), Toast.LENGTH_SHORT).show();
				}
			}			
		});		
		
		//选择日期
		etAddItemBuyDate = (EditText) super.findViewById(R.id.et_add_itembuydate);
		etAddItemBuyDate.setText(UtilityHelper.formatDate(curDate, "y-m-d-w"));
		etAddItemBuyDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				String[] array = curDate.split("-");
				DatePickerDialog dateDialog = new DatePickerDialog(AddActivity.this, new DatePickerDialog.OnDateSetListener() {
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

		//返回按钮
		btnTitleBack = (ImageButton) super.findViewById(R.id.btn_title_back);
		btnTitleBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				AddActivity.this.setResult(Activity.RESULT_CANCELED);
				AddActivity.this.close();
			}			
		});
		
		//类别编辑
		btnAddCatEdit = (ImageButton) super.findViewById(R.id.btn_add_catedit);
		btnAddCatEdit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(AddActivity.this, CategoryEditActivity.class);
				startActivityForResult(intent, FIRST_REQUEST_CODE);
			}			
		});
		
		setListData();
	}

	//关闭this
	protected void close() {
		this.finish();
	}
	
	//保存方法
	protected boolean saveItem() {
		try {
			String catType = spinner.getSelectedItem().toString();
			categoryAccess = new CategoryTableAccess(sqlHelper.getReadableDatabase());
			int catId = categoryAccess.findCatIdByName(catType);
			sharedHelper.setCategory(spinner.getSelectedItemPosition());
			categoryAccess.close();	
		
			String itemName = etAddItemName.getText().toString().trim();
			if (itemName.equals("")) {
				Toast.makeText(this, getString(R.string.txt_add_itemname) + getString(R.string.txt_nonull), Toast.LENGTH_SHORT).show();
				return false;
			}
	
			String itemPrice = etAddItemPrice.getText().toString().trim();
			if (itemPrice.equals("")) {
				Toast.makeText(this, getString(R.string.txt_add_itemprice) + getString(R.string.txt_nonull), Toast.LENGTH_SHORT).show();
				return false;
			}
			
			itemAccess = new ItemTableAccess(sqlHelper.getReadableDatabase());
			Boolean result = itemAccess.addItem(itemName, itemPrice, curDate, catId);
			itemAccess.close();
	        if(result) {
	        	sharedHelper.setLocalSync(true);
	        	sharedHelper.setSyncStatus(getString(R.string.txt_home_hassync));
	        	
			    return true;
	        } else {
	        	return false;
	        } 
		} catch (Exception e) {
			Toast.makeText(this, getString(R.string.txt_add_cattype) + getString(R.string.txt_nonull), Toast.LENGTH_SHORT).show();
			return false;
		}
	}

	//绑定类别下拉
	protected void setListData() {
		categoryAccess = new CategoryTableAccess(sqlHelper.getReadableDatabase());
		list = categoryAccess.findAllCategory();
		categoryAccess.close();
		adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, list);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		int category = sharedHelper.getCategory();
		if(category > 0)
			spinner.setSelection(category);
	}
	
	//返回处理
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == FIRST_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
			setListData();
		}
	}

}
