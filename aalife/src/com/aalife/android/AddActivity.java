package com.aalife.android;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class AddActivity extends Activity {
	private AutoCompleteTextView itemnameAdd = null;
	private ArrayAdapter<CharSequence> itemnameAdapter = null;
	private List<CharSequence> itemnameList = null;
	private EditText itempriceAdd = null;
	private EditText itembuydateAdd = null;
	private Button submitBtn = null;
	private Button submitbackBtn = null;
	private ImageButton backBtn = null;
	private ImageButton calcBtn = null;
	private SQLiteOpenHelper helper = null;
	private ItemTableAccess itemtableAccess = null;
	private CategoryTableAccess categorytableAccess = null;
	private String date = null;
	private String page = null;
	private ArrayAdapter<CharSequence> categoryAdapter = null;
	private List<CharSequence> categoryList = null;
	private Spinner categorySpinner = null;
	private LinearLayout addLayout = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.addactivity);

		this.addLayout = (LinearLayout) super.findViewById(R.id.addlayout);
		this.addLayout.setOnClickListener(new OnLayoutClickImpl());

		Intent intent = super.getIntent();
		date = intent.getStringExtra("date");
		if (date == null) {
			date = UtilityHelper.getAppDate(this);
		}
		page = intent.getStringExtra("page");
		if (page == null) {
			page = "day";
		}

		this.helper = new MyDatabaseHelper(this);

		this.itemnameAdd = (AutoCompleteTextView) super.findViewById(R.id.itemnameadd);
		this.itemtableAccess = new ItemTableAccess(this.helper.getReadableDatabase());
		this.itemnameList = this.itemtableAccess.findAllItemName();
		this.itemnameAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_list_item_1, this.itemnameList);
		this.itemnameAdd.setAdapter(this.itemnameAdapter);

		this.itempriceAdd = (EditText) super.findViewById(R.id.itempriceadd);
		this.itembuydateAdd = (EditText) super.findViewById(R.id.itembuydateadd);

		this.submitBtn = (Button) super.findViewById(R.id.submitbtn);
		this.submitbackBtn = (Button) super.findViewById(R.id.submitbackbtn);
		this.backBtn = (ImageButton) super.findViewById(R.id.backbtn);

		this.categorytableAccess = new CategoryTableAccess(this.helper.getReadableDatabase());
		this.categorySpinner = (Spinner) super.findViewById(R.id.categoryspinner);
		this.categoryList = this.categorytableAccess.findAllCategory();
		this.categoryAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, this.categoryList);
		this.categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		this.categorySpinner.setAdapter(this.categoryAdapter);
		String catName = UtilityHelper.getCatName(this);
		int catpos = UtilityHelper.findCatPosition(this.categoryList, catName);
		this.categorySpinner.setSelection(catpos);
		this.categorySpinner.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent ev) {
				UtilityHelper.hideInputMethod(AddActivity.this, view);
				return false;
			}
		});

		this.itembuydateAdd.setOnClickListener(new BtnSelectdayClickImpl());
		this.submitBtn.setOnClickListener(new BtnSubmitClickImpl());
		this.submitbackBtn.setOnClickListener(new BtnSubmitBackClickImpl());
		this.backBtn.setOnClickListener(new BtnBackClickImpl());		
		
		this.calcBtn = (ImageButton) super.findViewById(R.id.calcbtn);
		this.calcBtn.setOnClickListener(new BtnCalcClickImpl());

		this.itembuydateAdd.setText(date);
	}

	// 点击布局
	private class OnLayoutClickImpl implements OnClickListener {
		@Override
		public void onClick(View view) {
			UtilityHelper.hideInputMethod(AddActivity.this, view);
		}
	}
	
	// 计算器按钮
	private class BtnCalcClickImpl implements OnClickListener {
		@Override
		public void onClick(View view) {
			MyCalcView calcView = new MyCalcView(AddActivity.this);
			
			AlertDialog calcDialog = new AlertDialog.Builder(AddActivity.this).create();
			calcDialog.setIcon(R.drawable.ic_calc_d);
			calcDialog.setTitle("计算器");
			calcDialog.setView(calcView, 0, 0, 0, 0);
			calcDialog.show();
		}		
	}

	// 日期按钮
	private class BtnSelectdayClickImpl implements OnClickListener {
		@Override
		public void onClick(View view) {
			UtilityHelper.hideInputMethod(AddActivity.this, view);

			String[] d = AddActivity.this.itembuydateAdd.getText().toString().split("-");
			Dialog dialog = new MyDatePickerDialog(AddActivity.this, new DatePickerDialog.OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					date = UtilityHelper.formatDate(year + "-" + (monthOfYear+1) + "-" + dayOfMonth, "date");
					AddActivity.this.itembuydateAdd.setText(date);
				}
			}, Integer.parseInt(d[0]), Integer.parseInt(d[1]) - 1, Integer.parseInt(d[2]));
			dialog.show();
		}
	}

	// 提交继续按钮
	private class BtnSubmitClickImpl implements OnClickListener {
		@Override
		public void onClick(View view) {
			if (AddActivity.this.addItem(view)) {
				if (page.equals("month")) {
					UtilityHelper.jumpActivity(AddActivity.this, AddActivity.class, "AddActivity", null, date, "month", 1);
				} else {
					UtilityHelper.jumpActivity(AddActivity.this, AddActivity.class, "AddActivity", date, 0);
				}
			}
		}
	}

	// 提交返回按钮
	private class BtnSubmitBackClickImpl implements OnClickListener {
		@Override
		public void onClick(View view) {
			if (AddActivity.this.addItem(view)) {
				if (page.equals("month")) {
					UtilityHelper.jumpActivity(AddActivity.this, MonthTotalActivity.class, "MonthTotalActivity", date, 1);
				} else {
					UtilityHelper.jumpActivity(AddActivity.this, DayActivity.class, "DayActivity", date, 0);
				}
			}
		}
	}

	// 提交方法
	private Boolean addItem(View view) {
		UtilityHelper.hideInputMethod(AddActivity.this, view);

		String ItemName = this.itemnameAdd.getText().toString().trim();
		if (ItemName.equals("")) {
			Toast.makeText(this, "内容不能为空。", Toast.LENGTH_SHORT).show();
			return false;
		}

		String ItemPrice = this.itempriceAdd.getText().toString().trim();
		if (ItemPrice.equals("")) {
			Toast.makeText(this, "内容不能为空。", Toast.LENGTH_SHORT).show();
			return false;
		}

		String CategoryName = this.categorySpinner.getSelectedItem().toString();
		this.categorytableAccess = new CategoryTableAccess(this.helper.getReadableDatabase());
		int CategoryID = this.categorytableAccess.findCategoryIDByName(CategoryName);
		UtilityHelper.setCatName(this, CategoryName);
		
		String ItemBuyDate = this.itembuydateAdd.getText().toString();
		this.itemtableAccess = new ItemTableAccess(this.helper.getWritableDatabase());
		this.itemtableAccess.insert(ItemName, ItemPrice, ItemBuyDate, CategoryID);

		UtilityHelper.setAppDate(AddActivity.this, ItemBuyDate);
		
		return true;
	}

	// 返回按钮
	private class BtnBackClickImpl implements OnClickListener {
		@Override
		public void onClick(View view) {
			UtilityHelper.hideInputMethod(AddActivity.this, view);
			
			UtilityHelper.jumpActivity(AddActivity.this, DayActivity.class, "DayActivity", date, 0);
		}
	}

	// 返回键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			UtilityHelper.jumpActivity(AddActivity.this, DayActivity.class, "DayActivity", date, 0);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
