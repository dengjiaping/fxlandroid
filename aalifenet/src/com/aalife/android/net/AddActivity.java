package com.aalife.android.net;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddActivity extends Activity {
	private String[] data = new String[3];
	private AutoCompleteTextView nameEdit = null;
	private EditText priceEdit = null;
	private EditText dateEdit = null;
	private Button submitBtn = null;
	private ImageButton backBtn = null;
	private ImageButton cattypeBtn = null;
	
	private ArrayList<CharSequence> catNameList = new ArrayList<CharSequence>();
	private ArrayAdapter<CharSequence> catNameAdapter = null;
	
	private String catName = "";
	private String catTypeId = "";
	private String itemPrice = "";
	private String itemBuyDate = "";
	private String userId = "";
	private String userGroupId = "";

	private MyCategoryType myCatType = null;
	private Spinner catTypeSpinner = null;
	private List<Map<String, String>> catTypeList = new ArrayList<Map<String, String>>();
	private SimpleAdapter catTypeAdapter = null;
	private MyCatTypeAdapter adapter = null;

	private MyDate myDate = new MyDate();
	
	private MyHandler myHandler = new MyHandler(this);
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addactivity);

		Intent intent = super.getIntent();
		data = intent.getStringArrayExtra("data");
		userId = data[0];
		userGroupId = data[1];
		myDate.setDate(data[2]);
		
		nameEdit = (AutoCompleteTextView) super.findViewById(R.id.addnameedit);
		priceEdit = (EditText) super.findViewById(R.id.addpriceedit);
		dateEdit = (EditText) super.findViewById(R.id.adddateedit);
		submitBtn = (Button) super.findViewById(R.id.submitbtn);
		backBtn = (ImageButton) super.findViewById(R.id.backbtn);
		cattypeBtn = (ImageButton) super.findViewById(R.id.cattypebtn);
		
		new Thread(new Runnable(){
			@Override
			public void run() {				
				MyItemList myItemList = new MyItemList();
				myItemList.getCatNameListForAuto(userGroupId);
				catNameList = myItemList.getCatNameList();
				
				Message message = new Message();
				message.what = 3;
				myHandler.sendMessage(message);
			}
		}).start();
		
		dateEdit.setText(data[2]);
		
		backBtn.setOnClickListener(new OnBackClickImpl());
		dateEdit.setOnClickListener(new OnDateClickImpl());
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("cattypeid", "0");
		map.put("cattypename", getString(R.string.btn_text_loading));
		this.catTypeList.add(map);
		this.catTypeSpinner = (Spinner) super.findViewById(R.id.cattypespinner);
		this.catTypeAdapter = new SimpleAdapter(this, this.catTypeList, R.layout.spinnerlayout, new String[]{ "cattypeid", "cattypename" }, new int[]{ R.id.cattypeidtext, R.id.cattypenametext });
		this.catTypeSpinner.setAdapter(this.catTypeAdapter);
		this.catTypeSpinner.setEnabled(false);
		
		new Thread(new Runnable(){
			@Override
			public void run() {				
				myCatType = new MyCategoryType();
				myCatType.getCatTypeListForSpinner(userGroupId);
				catTypeList = myCatType.getCatTypeList();
				
				int catTypeId = MyHelper.getCatTypeId(AddActivity.this);
				int pos = MyHelper.findCatType(catTypeList, catTypeId);
				
				Bundle bundle = new Bundle();
				bundle.putInt("pos", pos);
				Message message = new Message();
				message.what = 1;
				message.setData(bundle);
				myHandler.sendMessage(message);
			}
		}).start();
	}
	
	static class MyHandler extends Handler {
		WeakReference<AddActivity> myActivity = null;
		MyHandler(AddActivity activity) {
			myActivity = new WeakReference<AddActivity>(activity);
		}
		
		@Override
		public void handleMessage(Message msg) {
			AddActivity activity = myActivity.get();
			switch(msg.what) {
			case 1:
				activity.catTypeAdapter = new SimpleAdapter(activity, activity.catTypeList, R.layout.spinnerlayout, new String[]{ "cattypeid", "cattypename" }, new int[]{ R.id.cattypeidtext, R.id.cattypenametext });
				activity.catTypeSpinner.setAdapter(activity.catTypeAdapter);
				activity.catTypeSpinner.setEnabled(true);
				
				int pos = msg.getData().getInt("pos");
				activity.catTypeSpinner.setSelection(pos);
				
				activity.cattypeBtn.setOnClickListener(activity.new OnCatTypeClickImpl());
				activity.submitBtn.setOnClickListener(activity.new OnSubmitClickImpl());
				
				break;
			case 2:
				String result = msg.getData().getString("result");
				
				if(result.equals("error")) {					
					Toast.makeText(activity, R.string.toast_adderror, Toast.LENGTH_SHORT).show();
				} else {				
					Intent intent = new Intent(activity, ItemListActivity.class);
					intent.putExtra("date", activity.myDate.getDate());
					activity.setResult(Activity.RESULT_OK, intent);
					activity.finish();
				}
				
				activity.submitBtn.setText(R.string.btn_submitadd);
				activity.submitBtn.setEnabled(true);
				
				break;
			case 3:
				activity.catNameAdapter = new ArrayAdapter<CharSequence>(activity, android.R.layout.simple_list_item_1, activity.catNameList);
				activity.nameEdit.setAdapter(activity.catNameAdapter);
				
				break;
			}
		}			
	};	
	
	private class OnCatTypeClickImpl implements OnClickListener {
		@Override
		public void onClick(View view) {
			LayoutInflater layout = LayoutInflater.from(AddActivity.this);
			View myView = layout.inflate(R.layout.cattypedialoglayout, null);
						
			final ListView listView = (ListView) myView.findViewById(R.id.cattypelistview);
			adapter = new MyCatTypeAdapter(AddActivity.this, catTypeList, userGroupId);
			listView.setAdapter(adapter);
												
			final Dialog dialog = new Dialog(AddActivity.this, R.style.MyDialog);
			dialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
			dialog.setContentView(myView);
			dialog.setTitle(R.string.alert_title_cattype);
			dialog.show();			
			dialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, android.R.drawable.ic_dialog_info);
			
			dialog.setOnDismissListener(new OnDismissListener(){
				public void onDismiss(DialogInterface dialog) {
					onCreate(null);
				}								
			});
			
			final EditText cattypenameAdd = (EditText) myView.findViewById(R.id.cattypenameadd);
			Button cattypeaddBtn = (Button) myView.findViewById(R.id.cattypeaddbtn);
			cattypeaddBtn.setOnClickListener(new OnClickListener(){
				public void onClick(View view) {
					String catTypeName = cattypenameAdd.getText().toString().trim();
					if(ValidateHelper.validateText(catTypeName)) {
						Toast.makeText(AddActivity.this, R.string.toast_contentnull, Toast.LENGTH_SHORT).show();
						return;
					}
					
					MyCategoryType myCatType = new MyCategoryType();
					final String result = myCatType.addCatType(userGroupId, catTypeName);
					
					if(result.equals("ok")) {
						myCatType.getCatTypeListForSpinner(userGroupId);
						catTypeList = myCatType.getCatTypeList();
						adapter = new MyCatTypeAdapter(AddActivity.this, catTypeList, userGroupId);
						listView.setAdapter(adapter);
						
						Toast.makeText(AddActivity.this, R.string.toast_addok, Toast.LENGTH_SHORT).show();
					}
				}
			});
			
			Button cattypecancelBtn = (Button) myView.findViewById(R.id.cattypecancelbtn);
			cattypecancelBtn.setOnClickListener(new OnClickListener(){
				public void onClick(View view) {
					dialog.cancel();
				}				
			});
		}		
	}
	
	private class OnDateClickImpl implements OnClickListener {
		@Override
		public void onClick(View view) {
			Dialog dialog = new MyDatePickerDialog(AddActivity.this, new DatePickerDialog.OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					myDate.setDate(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
					dateEdit.setText(myDate.getDate());
				}
			}, myDate.getYear(), myDate.getMonth(), myDate.getDay());
			dialog.show();
		}	
	}
	
	private class OnSubmitClickImpl implements OnClickListener {
		@Override
		public void onClick(View view) {
			MyHelper.hideInputMethod(AddActivity.this, view);
			
			catName = nameEdit.getText().toString().trim();
			if(ValidateHelper.validateText(catName)) {
				Toast.makeText(AddActivity.this, R.string.toast_contentnull, Toast.LENGTH_SHORT).show();
				return;
			}
			itemPrice = priceEdit.getText().toString().trim();
			if(ValidateHelper.validateText(itemPrice)) {
				Toast.makeText(AddActivity.this, R.string.toast_contentnull, Toast.LENGTH_SHORT).show();
				return;
			}
			itemBuyDate = dateEdit.getText().toString().trim() + " " + myDate.getTime();
			
			View myView = catTypeSpinner.getSelectedView();
			TextView catTypeIdText = (TextView) myView.findViewById(R.id.cattypeidtext);
			catTypeId = catTypeIdText.getText().toString();
			
			MyHelper.setCatTypeId(AddActivity.this, Integer.parseInt(catTypeId));
			
			submitBtn.setText(R.string.btn_text_adding);
			submitBtn.setEnabled(false);
			
			new Thread(new Runnable(){
				@Override
				public void run() {				
					MyItem myItem = new MyItem(catName, catTypeId, itemPrice, itemBuyDate, userId, userGroupId);
					String result = myItem.addItem();
				
					Bundle bundle = new Bundle();
					bundle.putString("result", result);					
					Message message = new Message();
					message.what = 2;
					message.setData(bundle);
					myHandler.sendMessage(message);
				}
			}).start();
		}		
	}
	
	private class OnBackClickImpl implements OnClickListener {
		@Override
		public void onClick(View view) {
			finish();
		}		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == 0) {
			InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(AddActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
		
		return super.onTouchEvent(event);		
	}
}
