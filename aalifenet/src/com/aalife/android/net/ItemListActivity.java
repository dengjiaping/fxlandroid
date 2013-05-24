package com.aalife.android.net;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ItemListActivity extends Activity {
	private TextView usernameText = null;
	private TextView usergroupidText = null;
	private ImageView userimageView = null;
	private TextView dateText = null;
	private Button leftBtn = null;
	private Button rightBtn = null;
	private ListView itemList = null;
	private ProgressBar progressBar = null;
	private TextView emptyText = null;
	private TextView totalpriceText = null;
	private TextView monthpriceText = null;
	private Button premonthBtn = null;
	private Button nextmonthBtn = null;
	private TextView userlistText = null;
	private ImageButton addBtn = null;
	private ImageButton backBtn = null;
	private ImageButton userBtn = null;
	
	private ArrayList<String> users = new ArrayList<String>();
	private String userId = "";
	private String userName = "";
	private String userNickName = "";
	private String userImage = "";
	private String userGroupId = "";
	
	private MyItemList myItemList = null;
	private SimpleAdapter adapter = null;
	private List<Map<String, String>> ilist = null;
	private List<Map<String, String>> ulist = null;
	
	private MyDate myDate = new MyDate();

	private final int FIRST_REQUEST_CODE = 1;
	private final int SECOND_REQUEST_CODE = 2;
	
	private MyHandler myHandler = new MyHandler(this);
	
	private List<Map<String, String>> userGroupList = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.itemlistactivity);

		Intent intent = super.getIntent();
		users = intent.getStringArrayListExtra("users");
		if(users.size() > 0) {
			userId = users.get(0);
			userName = users.get(1);
			userNickName = users.get(3);
			userImage = users.get(4);
			userGroupId = users.get(5);
		}
		
		usergroupidText = (TextView) super.findViewById(R.id.usergroupidtext);
		usernameText = (TextView) super.findViewById(R.id.usernametext);
		userimageView = (ImageView) super.findViewById(R.id.userimageview);
		dateText = (TextView) super.findViewById(R.id.datetext);
		leftBtn = (Button) super.findViewById(R.id.leftbtn);
		rightBtn = (Button) super.findViewById(R.id.rightbtn);
		itemList = (ListView) super.findViewById(R.id.itemlist);
		progressBar = (ProgressBar) super.findViewById(R.id.progressbar);
		emptyText = (TextView) super.findViewById(R.id.emptytext);
		totalpriceText = (TextView) super.findViewById(R.id.totalpricetext);
		monthpriceText = (TextView) super.findViewById(R.id.monthpricetext);
		premonthBtn = (Button) super.findViewById(R.id.premonthbtn);
		nextmonthBtn = (Button) super.findViewById(R.id.nextmonthbtn);
		userlistText = (TextView) super.findViewById(R.id.userlisttext);
		addBtn = (ImageButton) super.findViewById(R.id.addbtn);
		backBtn = (ImageButton) super.findViewById(R.id.backbtn);
		userBtn = (ImageButton) super.findViewById(R.id.userbtn);
		
		usergroupidText.setText("群组（" + userGroupId + "）");
		if(userNickName.equals("")){
			usernameText.setText(userName);
		} else {
			usernameText.setText(userNickName);
		}
		
		if(!Pattern.compile("^http").matcher(userImage).find()) {
			userImage = MyHelper.getWebUrl() +  "/Images/Users/" + userImage;
		}
		userimageView.setImageBitmap(new MyUser().getUserImageFormUrl(userImage));
		
		updateItemList();

		dateText.setOnClickListener(new OnDateClickImpl());
		leftBtn.setOnClickListener(new OnLeftBtnClickImpl());
		rightBtn.setOnClickListener(new OnRightBtnClickImpl());
		premonthBtn.setOnClickListener(new OnPreMonthBtnClickImpl());
		nextmonthBtn.setOnClickListener(new OnNextMonthBtnClickImpl());
		
		addBtn.setOnClickListener(new OnAddClickImpl());
		backBtn.setOnClickListener(new OnBackClickImpl());
		userBtn.setOnClickListener(new OnUserInfoClickImpl());
		
		MyUser myUser = new MyUser();
		String result = myUser.getUserGroupListForMsg(userGroupId);
		userGroupList = myUser.getUserGroupList();
		
		if(result.equals("nolive")) {
			userBtn.setBackgroundResource(R.drawable.myusergroupmsg);
			userBtn.setOnClickListener(new OnUserClickImpl());
			userBtn.post(new Runnable(){
				@Override
				public void run() {
					AnimationDrawable animation = (AnimationDrawable) userBtn.getBackground();
					animation.start();
				}}
			);
		}
	}
	
	static class MyHandler extends Handler {
		WeakReference<ItemListActivity> myActivity = null;
		MyHandler(ItemListActivity activity) {
			myActivity = new WeakReference<ItemListActivity>(activity);
		}
		
		@Override
		public void handleMessage(Message msg) {
			ItemListActivity activity = myActivity.get();
			switch(msg.what) {
			case 1:				
				if(!activity.ilist.isEmpty()) {
					activity.emptyText.setVisibility(View.GONE);	
					activity.totalpriceText.setText(activity.getString(R.string.text_today) + MyFormat.formatDouble(activity.myItemList.getTotalPrice(), "0.0#", true));
					activity.adapter = new SimpleAdapter(activity, activity.ilist, R.layout.itemlistlayout, new String[] { "itemid", "id", "catname", "cattypeid", "userid", "username", "itemprice" }, new int[] { R.id.itemidtext, R.id.idtext, R.id.catnametext, R.id.cattypeidtext, R.id.useridtext, R.id.usernametext, R.id.itempricetext });
					activity.itemList.setAdapter(activity.adapter);
					activity.itemList.setOnItemClickListener(activity.new OnItemClickImpl());
				} else {
					activity.emptyText.setVisibility(View.VISIBLE);
					activity.totalpriceText.setText(activity.getString(R.string.text_today) + activity.getString(R.string.text_zero));	
					activity.itemList.setAdapter(null);
				}
				
				if(!activity.ulist.isEmpty()) {
					String result = "";
					for(int i=0; i<activity.ulist.size(); i++) {
						Map<String, String> map = activity.ulist.get(i);
						result += map.get("username") + " : ";
						result += MyFormat.formatDouble(map.get("usertotal"), "0.0#", false) + "   ";
					}
					result = result.substring(0, result.length()-3);
					activity.userlistText.setText(result);
				} else {
					activity.userlistText.setText("");
				}
				
				if(!activity.myItemList.getMonthPrice().equals("")) {
					activity.monthpriceText.setText(activity.getString(R.string.text_month) + MyFormat.formatDouble(activity.myItemList.getMonthPrice(), "0.0#", true));
				} else {
					activity.monthpriceText.setText(activity.getString(R.string.text_month) + activity.getString(R.string.text_zero));
				}

				activity.progressBar.setVisibility(View.GONE);
				
				break;
			case 2:
				String result = msg.getData().getString("result");
				
				if(!result.equals("error")) {					
					Toast.makeText(activity, R.string.toast_deleteok, Toast.LENGTH_SHORT).show();
					activity.updateItemList();
				}	
				
				break;
			case 3:
				
				break;
			}
		}			
	};	
	
	private class OnUserInfoClickImpl implements OnClickListener {
		@Override
		public void onClick(View view) {
			Intent intent = new Intent(ItemListActivity.this, UserinfoActivity.class);
			intent.putExtra("userid", userId);
			startActivityForResult(intent, SECOND_REQUEST_CODE);
		}		
	}
	
	private class OnUserClickImpl implements OnClickListener {
		@Override
		public void onClick(View view) {
			LayoutInflater layout = LayoutInflater.from(ItemListActivity.this);
			View myView = layout.inflate(R.layout.usergroupdialoglayout, null);
						
			MyUserGroupAdapter adapter = new MyUserGroupAdapter(ItemListActivity.this, userGroupList);
			ListView listView = (ListView) myView.findViewById(R.id.usergrouplistview);
			listView.setAdapter(adapter);
												
			/*Dialog dialog = new AlertDialog.Builder(ItemListActivity.this)
					.setIcon(android.R.drawable.ic_dialog_info)
					.setTitle(R.string.alert_title_usergroupedit)
					.setView(myView)
					.setNegativeButton(R.string.alert_btn_close, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							dialog.cancel();
							onCreate(null);
						}
					}).create();
			dialog.show();*/
			
			final Dialog dialog = new Dialog(ItemListActivity.this, R.style.MyDialog);
			dialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
			dialog.setContentView(myView);
			dialog.setTitle(R.string.alert_title_usergroupedit);
			dialog.show();			
			dialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, android.R.drawable.ic_dialog_info);
			
			dialog.setOnDismissListener(new OnDismissListener(){
				public void onDismiss(DialogInterface dialog) {
					onCreate(null);
				}								
			});
		}		
	}
	
	private class OnItemClickImpl implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			final String itemId = ((TextView) view.findViewById(R.id.itemidtext)).getText().toString();
			final String catName = ((TextView) view.findViewById(R.id.catnametext)).getText().toString();
			final String catTypeId = ((TextView) view.findViewById(R.id.cattypeidtext)).getText().toString();
			final String itemPrice = ((TextView) view.findViewById(R.id.itempricetext)).getText().toString();
			final String userId = ((TextView) view.findViewById(R.id.useridtext)).getText().toString();
			
			Dialog dialog = new AlertDialog.Builder(ItemListActivity.this)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setTitle(R.string.alert_title_info)
				.setMessage(R.string.alert_text_chooseoperater)
				.setPositiveButton(R.string.alert_btn_edit, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String[] item = new String[7];
						item[0] = itemId;
						item[1] = catName;
						item[2] = catTypeId;
						item[3] = MyFormat.removeSymbol(itemPrice);
						item[4] = myDate.getDate();
						item[5] = userId;
						item[6] = userGroupId;
						
						Intent intent = new Intent(ItemListActivity.this, EditActivity.class);
						intent.putExtra("item", item);
						startActivityForResult(intent, FIRST_REQUEST_CODE);
					}
				}).setNeutralButton(R.string.alert_btn_delete, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						MyItem myItem = new MyItem(itemId);
						String result = myItem.deleteItem();
						
						Bundle bundle = new Bundle();
						bundle.putString("result", result);					
						Message message = new Message();
						message.what = 2;
						message.setData(bundle);
						myHandler.sendMessage(message);
					}
				}).create();
			dialog.show();
		}
	}
	
	private class OnAddClickImpl implements OnClickListener {
		@Override
		public void onClick(View view) {
			String[] data = new String[3];
			data[0] = userId;
			data[1] = userGroupId;
			data[2] = myDate.getDate();
			
			Intent intent = new Intent(ItemListActivity.this, AddActivity.class);
			intent.putExtra("data", data);
			startActivityForResult(intent, FIRST_REQUEST_CODE);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == FIRST_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
			if(data != null) {
				String date = data.getExtras().getString("date");
				myDate.setDate(date);
				
				updateItemList();
			}
		} else if(requestCode == SECOND_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
			if(data != null) {
				String userGroupLive = data.getExtras().getString("live");
				if(userGroupLive.equals("0")) {
					Toast.makeText(this, R.string.toast_logingrouperror, Toast.LENGTH_SHORT).show();
					finish();
				}				
				updateItemList();
			}
		}
	}

	private class OnDateClickImpl implements OnClickListener {
		@Override
		public void onClick(View view) {
			Dialog dialog = new MyDatePickerDialog(ItemListActivity.this, new DatePickerDialog.OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					myDate.setDate(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);

					updateItemList();
				}
			}, myDate.getYear(), myDate.getMonth(), myDate.getDay());
			dialog.show();
		}
	}
	
	private class OnLeftBtnClickImpl implements OnClickListener {
		@Override
		public void onClick(View view) {
			myDate.getNextDay();
			updateItemList();
		}		
	}	
	private class OnRightBtnClickImpl implements OnClickListener {
		@Override
		public void onClick(View view) {
			myDate.getPreviousDay();
			updateItemList();
		}		
	}	
	private class OnPreMonthBtnClickImpl implements OnClickListener {
		@Override
		public void onClick(View view) {
			myDate.getPreviousMonth();
			updateItemList();
		}		
	}	
	private class OnNextMonthBtnClickImpl implements OnClickListener {
		@Override
		public void onClick(View view) {
			myDate.getNextMonth();
			updateItemList();
		}		
	}
	
	private void updateItemList() {
		itemList.setAdapter(null);
		dateText.setText(myDate.getDate() + " " + myDate.getWeekStr());
		progressBar.setVisibility(View.VISIBLE);
		emptyText.setVisibility(View.GONE);
		
		new Thread(new Runnable(){
			@Override
			public void run() {
				myItemList = new MyItemList(userGroupId, myDate.getDate());
				myItemList.getItemListForListView();
				ilist = myItemList.getItemList();
				ulist = myItemList.getUserList();
				
				Message message = new Message();
				message.what = 1;
				myHandler.sendMessage(message);
			}
		}).start();
	}
	
	private class OnBackClickImpl implements OnClickListener {
		@Override
		public void onClick(View view) {
			finish();
		}		
	}
}
