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

public class EditActivity extends Activity {
	private AutoCompleteTextView itemnameEdit = null;
	private ArrayAdapter<CharSequence> itemnameAdapter = null;
	private List<CharSequence> itemnameList = null;
	private EditText itempriceEdit = null;
	private EditText itembuydateEdit = null;
	private Button submitBtn = null;
	private ImageButton backBtn = null;
	private ImageButton calcBtn = null;
	private SQLiteOpenHelper helper = null;
	private ItemTableAccess itemtableAccess = null;
	private CategoryTableAccess categorytableAccess = null;
	private String date = null;
	private String[] items = new String[5];
	private String page = null;
	private ArrayAdapter<CharSequence> categoryAdapter = null;
	private List<CharSequence> categoryList = null;
	private Spinner categorySpinner = null;
	private String category = null;
	private LinearLayout editLayout = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.editactivity);

		this.editLayout = (LinearLayout) super.findViewById(R.id.editlayout);
		this.editLayout.setOnClickListener(new OnLayoutClickImpl());
		
		Intent intent = super.getIntent();
		items = intent.getStringArrayExtra("items");
		date = intent.getStringExtra("date");
		if (date == null) {
			date = UtilityHelper.getAppDate(this);
		}
		page = intent.getStringExtra("page");
		if(page == null) {
			page = "day";
		}
		
		this.helper = new MyDatabaseHelper(this);

		this.itemnameEdit = (AutoCompleteTextView) super.findViewById(R.id.itemnameedit);
		this.itemtableAccess = new ItemTableAccess(this.helper.getReadableDatabase());
		this.itemnameList = this.itemtableAccess.findAllItemName();
		this.itemnameAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_list_item_1, this.itemnameList);
		this.itemnameEdit.setAdapter(this.itemnameAdapter);

		this.itempriceEdit = (EditText) super.findViewById(R.id.itempriceedit);
		this.itembuydateEdit = (EditText) super.findViewById(R.id.itembuydateedit);
		this.submitBtn = (Button) super.findViewById(R.id.submitbtn);
		this.backBtn = (ImageButton) super.findViewById(R.id.backbtn);

		this.itemnameEdit.setText(items[1]);
		this.itempriceEdit.setText(items[2]);
		this.itembuydateEdit.setText(items[3]);
		this.category = items[4];

		this.categorytableAccess = new CategoryTableAccess(this.helper.getReadableDatabase());
		this.categorySpinner = (Spinner) super.findViewById(R.id.categoryspinner);
		this.categoryList = this.categorytableAccess.findAllCategory();
		this.categoryAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, this.categoryList);
		this.categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		this.categorySpinner.setAdapter(this.categoryAdapter);
		for (int i = 0; i < this.categoryList.size(); i++) {
			String s = this.categoryList.get(i).toString();
			if (s.equals(this.category)) {
				this.categorySpinner.setSelection(i);
				break;
			}
		}
		this.categorySpinner.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View view, MotionEvent ev) {
				UtilityHelper.hideInputMethod(EditActivity.this, view);
				return false;
			}
		});

		this.itembuydateEdit.setOnClickListener(new BtnSelectdayClickImpl());
		this.submitBtn.setOnClickListener(new BtnSubmitClickImpl());
		this.backBtn.setOnClickListener(new BtnBackClickImpl());
		
		this.calcBtn = (ImageButton) super.findViewById(R.id.calcbtn);
		this.calcBtn.setOnClickListener(new BtnCalcClickImpl());
	}

	// 点击布局
	private class OnLayoutClickImpl implements OnClickListener {
		@Override
		public void onClick(View view) {
			UtilityHelper.hideInputMethod(EditActivity.this, view);
		}		
	}
	
	// 计算器按钮
	private class BtnCalcClickImpl implements OnClickListener {
		@Override
		public void onClick(View view) {
			MyCalcView calcView = new MyCalcView(EditActivity.this);
			
			AlertDialog calcDialog = new AlertDialog.Builder(EditActivity.this).create();
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
			UtilityHelper.hideInputMethod(EditActivity.this, view);
			
			String[] d = EditActivity.this.itembuydateEdit.getText().toString().split("-");
			Dialog dialog = new MyDatePickerDialog(EditActivity.this, new DatePickerDialog.OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					EditActivity.this.itembuydateEdit.setText(UtilityHelper.formatDate(year + "-" + (monthOfYear+1) + "-" + dayOfMonth, "date"));
				}
			}, Integer.parseInt(d[0]), Integer.parseInt(d[1]) - 1, Integer.parseInt(d[2]));
			dialog.show();
		}
	}
		
	// 提交按钮
	private class BtnSubmitClickImpl implements OnClickListener {
		@Override
		public void onClick(View view) {
			UtilityHelper.hideInputMethod(EditActivity.this, view);
			
			int itemID = Integer.parseInt(items[0]);
			String itemName = EditActivity.this.itemnameEdit.getText().toString().trim();
			if (itemName.equals("")) {
				Toast.makeText(EditActivity.this, "内容不能为空。", Toast.LENGTH_SHORT).show();
				return;
			}

			String itemPrice = EditActivity.this.itempriceEdit.getText().toString().trim();
			if (itemPrice.equals("")) {
				Toast.makeText(EditActivity.this, "内容不能为空。", Toast.LENGTH_SHORT).show();
				return;
			}

			String categoryName = EditActivity.this.categorySpinner.getSelectedItem().toString();
			EditActivity.this.categorytableAccess = new CategoryTableAccess(EditActivity.this.helper.getReadableDatabase());
			int categoryID = EditActivity.this.categorytableAccess.findCategoryIDByName(categoryName);

			String itemBuyDate = EditActivity.this.itembuydateEdit.getText().toString();
			EditActivity.this.itemtableAccess = new ItemTableAccess(EditActivity.this.helper.getWritableDatabase());
			EditActivity.this.itemtableAccess.update(itemID, itemName, itemPrice, itemBuyDate, categoryID);

			if(page.equals("month")) {
				UtilityHelper.jumpActivity(EditActivity.this, MonthTotalActivity.class, "MonthTotalActivity", date, 1);
			} else {
				UtilityHelper.jumpActivity(EditActivity.this, DayActivity.class, "DayActivity", date, 0);
			}
		}
	}

	// 返回按钮
	private class BtnBackClickImpl implements OnClickListener {
		@Override
		public void onClick(View view) {
			UtilityHelper.hideInputMethod(EditActivity.this, view);
			
			if(page.equals("month")) {
				UtilityHelper.jumpActivity(EditActivity.this, MonthTotalActivity.class, "MonthTotalActivity", date, 1);
			} else {
				UtilityHelper.jumpActivity(EditActivity.this, DayActivity.class, "DayActivity", date, 0);
			}
		}
	}

	// 返回键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(page.equals("month")) {
				UtilityHelper.jumpActivity(EditActivity.this, MonthTotalActivity.class, "MonthTotalActivity", date, 1);
			} else {
				UtilityHelper.jumpActivity(EditActivity.this, DayActivity.class, "DayActivity", date, 0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
