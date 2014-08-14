package com.aalife.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SimpleAdapter;
import android.widget.SlidingDrawer;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.voicerecognition.android.ui.BaiduASRDigitalDialog;
import com.baidu.voicerecognition.android.ui.DialogRecognitionListener;

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
	private int recommend = 0;
	
	private AutoCompleteTextView etAddItemName = null;
	private ArrayAdapter<CharSequence> nameAdapter = null;
	private List<CharSequence> nameList = null;
	private EditText etAddItemPrice = null;
	private EditText etAddItemBuyDate = null;
	private final int FIRST_REQUEST_CODE = 1;

	private String curDate2 = "";
	private Spinner spRegionType = null;
	private ArrayAdapter<CharSequence> regionAdapter = null;
	private EditText etAddItemBuyDate2 = null;
	private TextView tvRegionFrom = null;
	private TextView tvRegionTo = null;
	private int regionId = 0;
	private int monthRegion = 0;
	private String regionType = "";
	
	private SimpleAdapter adapterSmart = null;
	private List<Map<String, String>> listCat = null;
	private List<Map<String, String>> listItem = null;
	private List<Map<String, String>> listPrice = null;
	private List<Map<String, String>> listDate = null;
	private ListView listAddSmart = null;
	private int smartFlag = 1;
	private SlidingDrawer sdAddSmart = null;
	private Button btnSmartBack = null;
	private int screenWidth = 0;
	
	//百度语音识别对话框  
    private BaiduASRDigitalDialog mDialog=null;   
    //应用授权信息   
    private String API_KEY="tdW7auX1OxkPwu7BWdQ06RnY";  
    private String SECRET_KEY="XmjVFoFxN6B3gR9lLxOc9k1iEimFms6b"; 
	
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
		TextView tvRegion = (TextView) super.findViewById(R.id.tv_region);
		textPaint = tvRegion.getPaint();
		textPaint.setFakeBoldText(true);
				
		//数据库
		sqlHelper = new DatabaseHelper(this);
				
		//初始化
		sharedHelper = new SharedHelper(this);				
		spinner = (Spinner) super.findViewById(R.id.sp_add_cattype);
		etAddItemName = (AutoCompleteTextView) super.findViewById(R.id.et_add_itemname);
		etAddItemPrice = (EditText) super.findViewById(R.id.et_add_itemprice);
		listAddSmart = (ListView) super.findViewById(R.id.list_add_smart);
		sdAddSmart = (SlidingDrawer) super.findViewById(R.id.slidingDrawer1);
		btnSmartBack = (Button) super.findViewById(R.id.btn_smart_back);
		btnSmartBack.setVisibility(View.GONE);
		
		//固定消费
		String[] regionTypeArr = getResources().getStringArray(R.array.regiontype);
		tvRegionFrom = (TextView) super.findViewById(R.id.tv_region_from);
		tvRegionTo = (TextView) super.findViewById(R.id.tv_region_to);
		spRegionType = (Spinner) super.findViewById(R.id.sp_regiontype);
		regionAdapter = new ArrayAdapter<CharSequence>(this, R.layout.list_spinner, regionTypeArr);
		regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spRegionType.setAdapter(regionAdapter);
		
		//百度语音
		if (mDialog == null) {
			Bundle bundle = new Bundle(); 
			bundle.putString(BaiduASRDigitalDialog.PARAM_API_KEY, API_KEY);  
			bundle.putString(BaiduASRDigitalDialog.PARAM_SECRET_KEY, SECRET_KEY);
			bundle.putInt(BaiduASRDigitalDialog.PARAM_DIALOG_THEME, BaiduASRDigitalDialog.THEME_BLUE_LIGHTBG); 
	        mDialog = new BaiduASRDigitalDialog(this, bundle); 
	        mDialog.setDialogRecognitionListener(new DialogRecognitionListener() {  
	            @Override  
	            public void onResults(Bundle results) {  
	                ArrayList<String> rs = results != null ? results.getStringArrayList(RESULTS_RECOGNITION) : null;      
	                if (rs != null && rs.size() > 0) {  	                	
	                	Toast.makeText(AddActivity.this, rs.get(0), Toast.LENGTH_SHORT).show();
	                	
	                	VoiceHelper voice = new VoiceHelper();
	                	String[] result = voice.splitWords(rs.get(0));
	                	
	                	etAddItemName.setText(result[0]);
	                	etAddItemName.dismissDropDown();
	                	
	                	etAddItemPrice.setText(result[1]);
	                }
	            }
	        }); 
		}
        
		//语音输入
		ImageButton btnAddVoice = (ImageButton) super.findViewById(R.id.btn_add_voice);
		btnAddVoice.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				mDialog.show();
			}			
		});
				
		//文本框点击关闭Smart
		etAddItemName.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				if(sdAddSmart.isOpened()) {
					sdAddSmart.animateClose();
				}
			}			
		});
		etAddItemName.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(sdAddSmart.isOpened()) {
					sdAddSmart.animateClose();
				}
			}			
		});
		etAddItemPrice.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				if(sdAddSmart.isOpened()) {
					sdAddSmart.animateClose();
				}
			}			
		});
		etAddItemPrice.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(sdAddSmart.isOpened()) {
					sdAddSmart.animateClose();
				}
			}			
		});
		
		//商品名称List
		itemAccess = new ItemTableAccess(sqlHelper.getReadableDatabase());
		nameList = itemAccess.findAllItemName();
		itemAccess.close();
		nameAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_list_item_1, nameList);
		etAddItemName.setAdapter(nameAdapter);
		
		//设置SmartLeft
		MarginLayoutParams mp = (MarginLayoutParams) sdAddSmart.getLayoutParams();
		int marginTop = mp.topMargin;
		int handleWidth = getResources().getDimensionPixelSize(R.dimen.smart_handle_width);
		DisplayMetrics dm = getResources().getDisplayMetrics();  
		screenWidth = dm.widthPixels;
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		params.setMargins(screenWidth/2 - handleWidth, marginTop, 0, 0);
        sdAddSmart.setLayoutParams(params);
		
		//获取数据
		Intent intent = super.getIntent();
		recommend = intent.getIntExtra("recommend", 0);
		fromDate = intent.getStringExtra("date");
		if(fromDate != null && !fromDate.equals("")) {
			curDate = fromDate;
		} else if(curDate.equals("")) {
			curDate = UtilityHelper.getCurDate();
		}

		//ListSmart点击
		listAddSmart.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Map<String, String> map = new HashMap<String, String>();
				itemAccess = new ItemTableAccess(sqlHelper.getReadableDatabase());
				switch(smartFlag) {
				case 1:
					btnSmartBack.setVisibility(View.VISIBLE);
					map = (Map<String, String>) listCat.get(position);
					smartFlag = 2;
					setCurCategory(map.get("name"));
					listItem = itemAccess.findAllItemByCatId(map.get("id"));
					setListSmartData(listItem);
					break;
				case 2:
					map = (Map<String, String>) listItem.get(position);
					smartFlag = 3;
					etAddItemName.setText(map.get("name"));
					etAddItemName.dismissDropDown();
					listPrice = itemAccess.findAllPriceByName(map.get("name"));
					setListSmartData(listPrice);
					break;
				case 3:
					map = (Map<String, String>) listPrice.get(position);
					smartFlag = 4;
					etAddItemPrice.setText(map.get("name"));
					listDate = getListDateData(curDate);
					setListSmartData(listDate);
					//etAddItemPrice.requestFocus();
					break;
				case 4:
					map = (Map<String, String>) listDate.get(position);
					curDate = map.get("id");
					etAddItemBuyDate.setText(map.get("name"));
					sdAddSmart.animateClose();
					break;
				}				
				itemAccess.close();
			}			
		});

		//智能返回点击
		btnSmartBack.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) {
				switch(smartFlag) {
				case 2:
					btnSmartBack.setVisibility(View.GONE);
					smartFlag = 1;
					setListSmartData(listCat);
					break;
				case 3:
					smartFlag = 2;
					setListSmartData(listItem);
					break;
				case 4:
					smartFlag = 3;
					setListSmartData(listPrice);
					break;
				}
			}			
		});

		//添加继续按钮
		Button btnAddContinue = (Button) super.findViewById(R.id.btn_add_continue);
		btnAddContinue.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				int result = saveItem();
				if(result == 1) {
					Toast.makeText(AddActivity.this, getString(R.string.txt_add_addsuccess), Toast.LENGTH_SHORT).show();
					//AddActivity.this.onCreate(null);
					etAddItemName.setText("");
					etAddItemName.requestFocus();
					etAddItemPrice.setText("");
					
					setListSmartData(listCat);
					smartFlag = 1;
					btnSmartBack.setVisibility(View.GONE);
				} else if (result == 0) {
					Toast.makeText(AddActivity.this, getString(R.string.txt_add_adderror), Toast.LENGTH_SHORT).show();
				}
			}			
		});
		
		//添加返回按钮
		Button btnAddBack = (Button) super.findViewById(R.id.btn_add_back);
		btnAddBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				int result = saveItem();
				if(result == 1) {
					Toast.makeText(AddActivity.this, getString(R.string.txt_add_addsuccess), Toast.LENGTH_SHORT).show();
					Intent intent = new Intent();
					intent.putExtra("date", curDate);
					AddActivity.this.setResult(Activity.RESULT_OK, intent);
					AddActivity.this.close();
				} else if (result == 0) {
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
				DatePickerDialog dateDialog = new DatePickerDialog(AddActivity.this, new DatePickerDialog.OnDateSetListener() {
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
		
		//区间选择
		spRegionType.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if(position == 0) {
					etAddItemBuyDate2.setVisibility(View.GONE);
					tvRegionFrom.setVisibility(View.GONE);
					tvRegionTo.setVisibility(View.GONE);
					regionType = "";
				} else {
					etAddItemBuyDate2.setVisibility(View.VISIBLE);
					tvRegionFrom.setVisibility(View.VISIBLE);
					tvRegionTo.setVisibility(View.GONE);
					switch(position) {
						case 1:
						case 2:
							regionType = "d";
							curDate2 = UtilityHelper.getRegionDate(curDate, regionType);
							etAddItemBuyDate2.setText(UtilityHelper.formatDate(curDate2, "y-m-d-w"));
							break;
						case 3:
							regionType = "m";
							curDate2 = UtilityHelper.getRegionDate(curDate, regionType);
							etAddItemBuyDate2.setText(UtilityHelper.formatDate(curDate2, "y-m-d-w"));
							break;
						case 4:
							regionType = "y";
							curDate2 = UtilityHelper.getRegionDate(curDate, regionType);
							etAddItemBuyDate2.setText(UtilityHelper.formatDate(curDate2, "y-m-d-w"));
							break;
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}			
		});		
				
		//返回按钮
		ImageButton btnTitleBack = (ImageButton) super.findViewById(R.id.btn_title_back);
		btnTitleBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent();
				intent.putExtra("date", curDate);
				AddActivity.this.setResult(Activity.RESULT_OK, intent);
				AddActivity.this.close();
			}			
		});
		
		//计算器按钮
		ImageButton btnTitleCalc = (ImageButton) super.findViewById(R.id.btn_title_calc);
		btnTitleCalc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				final MyCalcView calcView = new MyCalcView(AddActivity.this);
				calcView.setGravity(Gravity.CENTER);
				
				final AlertDialog calcDialog = new AlertDialog.Builder(AddActivity.this).create();
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
							Toast.makeText(AddActivity.this, R.string.txt_calcfirst, Toast.LENGTH_SHORT).show();
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
	
	//返回键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.putExtra("date", curDate);
			AddActivity.this.setResult(Activity.RESULT_OK, intent);
			AddActivity.this.close();
		}
		return super.onKeyDown(keyCode, event);
	}	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		if (mDialog != null) {  
            mDialog.dismiss();  
        }
	}

	//保存方法
	protected int saveItem() {
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
	
			String itemPrice = etAddItemPrice.getText().toString().trim();
			if (itemPrice.equals("")) {
				Toast.makeText(this, getString(R.string.txt_add_itemprice) + getString(R.string.txt_nonull), Toast.LENGTH_SHORT).show();
				return 2;
			}
			
			regionId = 0;
			monthRegion = 0;
			if(!regionType.equals("")) {
				itemAccess = new ItemTableAccess(sqlHelper.getReadableDatabase());
				regionId = itemAccess.getMaxRegionId();
				itemAccess.close();
				monthRegion = UtilityHelper.getMonthRegion(curDate, curDate2, regionType);
				
				int maxRegion = 0;
	            if (regionType.equals("d")) {
	                maxRegion = 92;
	            } else if (regionType.equals("w")) {
	                maxRegion = (int)Math.floor((double)92 / 7);
	            } else if (regionType.equals("m")) {
	                maxRegion = 36;
	            } else if (regionType.equals("y")) {
	                maxRegion = 12;
	            }
				
				if(monthRegion <= 0 || monthRegion >= maxRegion) {
					Toast.makeText(this, getString(R.string.txt_add_regionerr), Toast.LENGTH_SHORT).show();
					return 2;
				}
			}
			
			String itemBuyDate = "";			
			itemAccess = new ItemTableAccess(sqlHelper.getReadableDatabase());
			Boolean result = false;
			for(int i=0; i<=monthRegion; i++) {
				if(regionType.equals("d")) {
				    itemBuyDate = UtilityHelper.getNavDate(curDate, i, "d") + " " + UtilityHelper.getCurTime();
				} else if(regionType.equals("w")) {
				    itemBuyDate = UtilityHelper.getNavDate(curDate, i*7, "d") + " " + UtilityHelper.getCurTime();
				} else if(regionType.equals("m")) {
				    itemBuyDate = UtilityHelper.getNavDate(curDate, i, "m") + " " + UtilityHelper.getCurTime();
				} else if(regionType.equals("y")) {
				    itemBuyDate = UtilityHelper.getNavDate(curDate, i, "y") + " " + UtilityHelper.getCurTime();
				} else {
					itemBuyDate = UtilityHelper.getNavDate(curDate, i, "d") + " " + UtilityHelper.getCurTime();
				}
			    result = itemAccess.addItem(itemName, itemPrice, itemBuyDate, catId, recommend, regionId, regionType);
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
		listCat = categoryAccess.findAllCategorySmart();
		categoryAccess.close();
		adapter = new ArrayAdapter<CharSequence>(this, R.layout.list_spinner, list);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		int category = sharedHelper.getCategory();
		if(category > 0) {
			spinner.setSelection(category);
		}
		
		setListSmartData(listCat);
	}
	
	//返回处理
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == FIRST_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
			setListData();
		}
	}
	
	//设置ListSmart
	protected void setListSmartData(List<Map<String, String>> listSmart) {
		adapterSmart = new SimpleAdapter(this, listSmart, R.layout.list_addsmart, new String[] { "id", "name" }, new int[] { R.id.tv_add_id, R.id.tv_add_name });
		listAddSmart.setAdapter(adapterSmart);
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
	
	//取日期
	protected List<Map<String, String>> getListDateData(String date) {
		List<Map<String, String>> all = new ArrayList<Map<String, String>>();
		Map<String, String> map = null;
		
		String tempDate = UtilityHelper.getNavDate(date, -2, "d");
		map = new HashMap<String, String>();
		map.put("id", tempDate);
		map.put("name", UtilityHelper.formatDate(tempDate, "y-m-d-w"));
		all.add(map);
		
		tempDate = UtilityHelper.getNavDate(date, -1, "d");
		map = new HashMap<String, String>();
		map.put("id", tempDate);
		map.put("name", UtilityHelper.formatDate(tempDate, "y-m-d-w"));
		all.add(map);
		
		map = new HashMap<String, String>();
		map.put("id", date);
		map.put("name", UtilityHelper.formatDate(date, "y-m-d-w"));
		all.add(map);

		tempDate = UtilityHelper.getNavDate(date, 1, "d");
		map = new HashMap<String, String>();
		map.put("id", tempDate);
		map.put("name", UtilityHelper.formatDate(tempDate, "y-m-d-w"));
		all.add(map);

		tempDate = UtilityHelper.getNavDate(date, 2, "d");
		map = new HashMap<String, String>();
		map.put("id", tempDate);
		map.put("name", UtilityHelper.formatDate(tempDate, "y-m-d-w"));
		all.add(map);
		
		return all;
	}
	
}
