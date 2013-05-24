package com.aalife.android;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class TongJiActivity extends Activity {
	private List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	private SimpleAdapter simpleAdapter = null;
	private ListView itemlistView = null;
	private TextView monthtotalpriceText = null;
	private SQLiteOpenHelper helper = null;
	private CategoryTableAccess categorytableAccess = null;
	private ItemTableAccess itemtableAccess = null;
	private ImageButton selectmonthBtn = null;
	private String date = null;
	private TextView titleText = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.tongjiactivity);
		
		Intent intent = super.getIntent();
		date = intent.getStringExtra("date");
		if (date == null) {
			date = UtilityHelper.getAppDate(this);
		}
		
		if(UtilityHelper.getTongJiFlag(this)) {
			Toast toast = Toast.makeText(this, getString(R.string.txt_tongji_info), Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			
			UtilityHelper.setTongJiFlag(this, false);
		}
		
		this.titleText = (TextView) super.findViewById(R.id.titletext);
		this.titleText.setText(UtilityHelper.formatDate(date, "yyyy-MM") + " " + getString(R.string.title_activity_tongji));

		this.helper = new MyDatabaseHelper(this);

		this.monthtotalpriceText = (TextView) super.findViewById(R.id.monthtotalpricetext);
		this.itemtableAccess = new ItemTableAccess(this.helper.getReadableDatabase());
		this.monthtotalpriceText.setText("☆☆  总计：￥ " + this.itemtableAccess.findAllTotalByMonth(date) + "  ☆☆");

		this.selectmonthBtn = (ImageButton) super.findViewById(R.id.selectmonthbtn);
		this.selectmonthBtn.setOnClickListener(new BtnTongJiSelectmonthClickImpl());

		this.itemlistView = (ListView) super.findViewById(R.id.itemlistview);
		this.categorytableAccess = new CategoryTableAccess(this.helper.getReadableDatabase());
		this.list = this.categorytableAccess.findAllWithTongJiByMonth(date);
		this.simpleAdapter = new SimpleAdapter(this, this.list, R.layout.tongjilist, new String[] { "idtext", "categorytext", "totalpricetext" }, new int[] { R.id.idtext, R.id.categorytext, R.id.totalpricetext });
		this.itemlistView.setAdapter(this.simpleAdapter);
		this.itemlistView.setOnItemClickListener(new OnItemClickImpl());
	}
	
	// 列表点击事件
	private class OnItemClickImpl implements OnItemClickListener {
		private TextView idView;
		private TextView categoryView;
		private TextView totalpriceView;

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			idView = (TextView) view.findViewById(R.id.idtext);
			categoryView = (TextView) view.findViewById(R.id.categorytext);
			totalpriceView = (TextView) view.findViewById(R.id.totalpricetext);
			
			final int CategoryID = Integer.parseInt(idView.getText().toString());
			String CategoryName = categoryView.getText().toString();

			categoryView.setBackgroundColor(view.getResources().getColor(R.color.dayitembgselect));
			totalpriceView.setBackgroundColor(view.getResources().getColor(R.color.dayitembgselect));
			
			LinearLayout layout = new LinearLayout(TongJiActivity.this);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			layout.setPadding(10, 10, 10, 10);
			layout.setFocusable(true);
			layout.setFocusableInTouchMode(true);
			final EditText catnameEdit = new EditText(TongJiActivity.this);
			catnameEdit.setText(CategoryName);
			catnameEdit.setLayoutParams(params);
			catnameEdit.setPadding(10, 10, 10, 10);
			layout.addView(catnameEdit);
			
			AlertDialog dialog = new AlertDialog.Builder(TongJiActivity.this).create();
			dialog.setIcon(R.drawable.ic_edit_d);
			dialog.setTitle("编辑");
			//dialog.setMessage(getString(R.string.txt_setting_catname));
			dialog.setView(layout, 0, 0, 0, 0);
			dialog.setButton("确定", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					String catname = catnameEdit.getText().toString();
					TongJiActivity.this.categorytableAccess = new CategoryTableAccess(TongJiActivity.this.helper.getWritableDatabase());
					TongJiActivity.this.categorytableAccess.update(CategoryID, catname);
					
					UtilityHelper.jumpActivity(TongJiActivity.this, TongJiActivity.class, "TongJiActivity", date, 3);
				}
			});
			dialog.show();			

			dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					categoryView.setBackgroundColor(TongJiActivity.this.getResources().getColor(android.R.color.white));
					totalpriceView.setBackgroundColor(TongJiActivity.this.getResources().getColor(android.R.color.white));
				}
			});
		}
	}

	// 日期按钮
	private class BtnTongJiSelectmonthClickImpl implements OnClickListener {
		@Override
		public void onClick(View view) {
			String[] d = date.split("-");
			Dialog dialog = new MyDatePickerDialog(TongJiActivity.this, new DatePickerDialog.OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					date = UtilityHelper.formatDate(year + "-" + (monthOfYear+1) + "-" + dayOfMonth, "date");

					UtilityHelper.jumpActivity(TongJiActivity.this, TongJiActivity.class, "TongJiActivity", date, 3);
				}
			}, Integer.parseInt(d[0]), Integer.parseInt(d[1]) - 1, Integer.parseInt(d[2]));
			dialog.show();
		}
	}

	// 返回键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			UtilityHelper.jumpActivity(TongJiActivity.this, DayActivity.class, "DayActivity", date, 0);
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
}
