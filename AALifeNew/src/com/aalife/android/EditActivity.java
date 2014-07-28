package com.aalife.android;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
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
	private String curTime = "";
	private String itemName = "";
	private String[] items = new String[6];
	
	private AutoCompleteTextView etAddItemName = null;
	private ArrayAdapter<CharSequence> nameAdapter = null;
	private List<CharSequence> nameList = null;
	private EditText etAddItemPrice = null;
	private EditText etAddItemBuyDate = null;
	private final int FIRST_REQUEST_CODE = 1;

	private String curDate2 = "";
	private CheckBox cbRegion = null;
	private EditText etAddItemBuyDate2 = null;
	private TextView tvRegionFrom = null;
	private TextView tvRegionTo = null;
	private boolean isRegion = false;
	private int regionId = 0;
	private int monthRegion = 0;
	
	private int screenWidth = 0;
	
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
		tvRegionFrom = (TextView) super.findViewById(R.id.tv_region_from);
		tvRegionTo = (TextView) super.findViewById(R.id.tv_region_to);
		
		//设置默认值	
		String[] date = items[4].split(" ");
		curDate = date[0];
		curTime = date.length > 1 ? date[1] : UtilityHelper.getCurTime();
		itemName = items[2];
		etAddItemName.setText(itemName);
		etAddItemPrice.setText(items[3]);
		regionId = Integer.parseInt(items[5]);
		
		//商品名称List
		itemAccess = new ItemTableAccess(sqlHelper.getReadableDatabase());
		nameList = itemAccess.findAllItemName();
		itemAccess.close();
		nameAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_list_item_1, nameList);
		etAddItemName.setAdapter(nameAdapter);
		
		//取屏幕宽度
		DisplayMetrics dm = getResources().getDisplayMetrics();  
		screenWidth = dm.widthPixels;
		
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
				Boolean result = false;
				if(regionId > 0) {
					result = itemAccess.deleteRegion(regionId);
				} else {
				    result = itemAccess.deleteItem(Integer.parseInt(items[0]));
				}
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
		etAddItemBuyDate.setText(UtilityHelper.formatDate(curDate, "y-m-d-w"));
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
		etAddItemBuyDate2 = (EditText) super.findViewById(R.id.et_add_itembuydate2);
		etAddItemBuyDate2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				String[] array = curDate2.split("-");
				DatePickerDialog dateDialog = new DatePickerDialog(EditActivity.this, new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int month, int day) {
						String date = UtilityHelper.formatDate(year + "-" + (month + 1) + "-" + day, "");
						curDate2 = date;
						etAddItemBuyDate2.setText(UtilityHelper.formatDate(curDate2, "y-m-d-w"));
					}					
				}, Integer.parseInt(array[0]), Integer.parseInt(array[1]) - 1, Integer.parseInt(array[2]));
				dateDialog.show();
			}
		});
		
		//选择区间
		cbRegion = (CheckBox) super.findViewById(R.id.cb_region);
		cbRegion.setEnabled(false);

		//初始区间
		if(regionId > 0) {
			cbRegion.setChecked(true);
			isRegion = true;
			
			itemAccess = new ItemTableAccess(sqlHelper.getReadableDatabase());
			String[] regionDate = itemAccess.getRegionDate(regionId);
			itemAccess.close();
			
			curDate = regionDate[0];
			curDate2 = regionDate[1];
			etAddItemBuyDate.setText(UtilityHelper.formatDate(curDate, "y-m-d-w"));
			etAddItemBuyDate2.setText(UtilityHelper.formatDate(curDate2, "y-m-d-w"));
			
			etAddItemBuyDate2.setVisibility(View.VISIBLE);
			tvRegionFrom.setVisibility(View.VISIBLE);
			tvRegionTo.setVisibility(View.VISIBLE);
		}
		
		//计算器按钮
		ImageButton btnTitleCalc = (ImageButton) super.findViewById(R.id.btn_title_calc);
		btnTitleCalc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				final MyCalcView calcView = new MyCalcView(EditActivity.this);
				calcView.setGravity(Gravity.CENTER);
				
				final AlertDialog calcDialog = new AlertDialog.Builder(EditActivity.this).create();
				calcDialog.setTitle(R.string.txt_calculator);
				calcDialog.setView(calcView, 0, 0, 0, 0);
				calcDialog.show();
				android.view.WindowManager.LayoutParams lp = calcDialog.getWindow().getAttributes();
				lp.width = (int) (screenWidth * 0.95);
				calcDialog.getWindow().setAttributes(lp);
				
				Button btnOk = (Button) calcView.findViewById(R.id.buttonok);
				btnOk.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						String result = calcView.resultText;
						if(!result.equals("")) {
							calcDialog.cancel();
							etAddItemPrice.setText(result);
						} else {
							Toast.makeText(EditActivity.this, R.string.txt_calcfirst, Toast.LENGTH_SHORT).show();
						}
					}					
				});
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
			
			if(isRegion) {
				monthRegion = UtilityHelper.getMonthRegion(curDate, curDate2);
				if(monthRegion <= 0) {
					Toast.makeText(this, getString(R.string.txt_add_regionerr), Toast.LENGTH_SHORT).show();
					return 2;
				}
			}
			
			//String itemBuyDate = curDate + " " + curTime;
			String itemBuyDate = "";
			
			itemAccess = new ItemTableAccess(sqlHelper.getReadableDatabase());
			Boolean result = false;
			if(monthRegion > 0) {
				result = itemAccess.deleteRegion(regionId);
				for(int i=0; i<=monthRegion; i++) {
					itemBuyDate = UtilityHelper.getNavDate(curDate, i, "m") + " " + curTime;
				    result = itemAccess.addItem(itemName, itemPrice, itemBuyDate, catId, 0, regionId);
				}
				result = itemAccess.deleteRegionBack(regionId);
			} else {
				itemBuyDate = curDate + " " + curTime;
				result = itemAccess.updateItem(Integer.parseInt(items[0]), itemName, itemPrice, itemBuyDate, catId);
			}
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
