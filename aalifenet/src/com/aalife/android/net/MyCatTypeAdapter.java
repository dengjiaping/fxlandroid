package com.aalife.android.net;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;


public class MyCatTypeAdapter extends BaseAdapter {
	private Context context = null;
	private List<Map<String, String>> list = null;
	private LayoutInflater layout = null;
	private String userGroupId = "";
	
	MyCatTypeAdapter(Context context, List<Map<String, String>> list, String userGroupId) {
		this.context = context;
		this.list = list;
		this.layout = LayoutInflater.from(this.context);
		this.userGroupId = userGroupId;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		CatTypeView cattypeView = null;

		if (convertView == null) {
			cattypeView = new CatTypeView();
			convertView = layout.inflate(R.layout.cattypelistlayout, null);
			cattypeView.cattypenameView = (EditText) convertView.findViewById(R.id.cattypenametext);
			cattypeView.cattypeupdateBtn = (Button) convertView.findViewById(R.id.cattypeupdatebtn);
			cattypeView.cattypedeleteBtn = (Button) convertView.findViewById(R.id.cattypedeletebtn);
			convertView.setTag(cattypeView);
		} else {
			cattypeView = (CatTypeView) convertView.getTag();
		}
		
		final String catTypeId = list.get(position).get("cattypeid");
		cattypeView.cattypenameView.setText(list.get(position).get("cattypename"));		

		cattypeView.cattypeupdateBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) {
				LinearLayout ll = (LinearLayout) view.getParent();
				EditText et = (EditText) ll.findViewById(R.id.cattypenametext);
				String catTypeName = et.getText().toString();

				MyCategoryType myCatType = new MyCategoryType();
				final String result = myCatType.updateCatType(catTypeId, userGroupId, catTypeName);
				
				if(result.equals("ok")) {
					myCatType.getCatTypeListForSpinner(userGroupId);
					list = myCatType.getCatTypeList();
					Toast.makeText(context, R.string.toast_changeok, Toast.LENGTH_SHORT).show();
					notifyDataSetChanged();
				}
			}
		});
		cattypeView.cattypedeleteBtn.setEnabled(true);
		cattypeView.cattypedeleteBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) {
				LinearLayout ll = (LinearLayout) view.getParent();
				EditText et = (EditText) ll.findViewById(R.id.cattypenametext);
				String catTypeName = et.getText().toString().trim();
				if(ValidateHelper.validateText(catTypeName)) {
					Toast.makeText(context, R.string.toast_contentnull, Toast.LENGTH_SHORT).show();
					return;
				}
				
				MyCategoryType myCatType = new MyCategoryType();
				final String result = myCatType.deleteCatType(catTypeId, userGroupId, catTypeName);
				
				if(result.equals("ok")) {
					myCatType.getCatTypeListForSpinner(userGroupId);
					list = myCatType.getCatTypeList();
					Toast.makeText(context, R.string.toast_changeok, Toast.LENGTH_SHORT).show();
					notifyDataSetChanged();
				}
			}
		});

		return convertView;
	}
	
	private class CatTypeView {
		private EditText cattypenameView = null;
		private Button cattypeupdateBtn = null;
		private Button cattypedeleteBtn = null;
	}
}
