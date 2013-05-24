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
import android.widget.TextView;
import android.widget.Toast;


public class MyUserGroupAdapter extends BaseAdapter {
	private Context context = null;
	private List<Map<String, String>> list = null;
	private LayoutInflater layout = null;	
	
	MyUserGroupAdapter(Context context, List<Map<String, String>> list) {
		this.context = context;
		this.list = list;
		this.layout = LayoutInflater.from(this.context);
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
		UserGroupView usergroupView = null;

		if (convertView == null) {
			usergroupView = new UserGroupView();
			convertView = layout.inflate(R.layout.usergrouplistlayout, null);
			usergroupView.usernameView = (TextView) convertView.findViewById(R.id.usernametext);
			usergroupView.usergrouppassBtn = (Button) convertView.findViewById(R.id.usergrouppassbtn);
			usergroupView.usergroupcancelBtn = (Button) convertView.findViewById(R.id.usergroupcancelbtn);
			convertView.setTag(usergroupView);
		} else {
			usergroupView = (UserGroupView) convertView.getTag();
		}
		
		final String userId = list.get(position).get("userid");
		usergroupView.usernameView.setText(list.get(position).get("username") + "(" + list.get(position).get("userfrom") + ")");
		final String userGroupId = list.get(position).get("usergroupid");
		
		String userGroupLive = list.get(position).get("usergrouplive");

		if(!userGroupLive.equals("0")) {
			usergroupView.usergrouppassBtn.setEnabled(false);
			usergroupView.usergroupcancelBtn.setEnabled(false);
		} else {
			usergroupView.usergrouppassBtn.setEnabled(true);
			usergroupView.usergrouppassBtn.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view) {
					MyUser myUser = new MyUser();
					final String result = myUser.changeUserGroupLive(userId, userGroupId);
					
					if(result.equals("ok")) {
						String live = myUser.getUserGroupListForMsg(userGroupId);
						if(!live.equals("")){
							list = myUser.getUserGroupList();
							Toast.makeText(context, R.string.toast_changeok, Toast.LENGTH_SHORT).show();
							notifyDataSetChanged();
						}
					}
				}
			});
			usergroupView.usergroupcancelBtn.setEnabled(true);
			usergroupView.usergroupcancelBtn.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view) {
					MyUser myUser = new MyUser();
					final String result = myUser.changeUserGroupCancel(userId);
					
					if(result.equals("ok")) {
						String live = myUser.getUserGroupListForMsg(userGroupId);
						if(!live.equals("")){
							list = myUser.getUserGroupList();
							Toast.makeText(context, R.string.toast_changeok, Toast.LENGTH_SHORT).show();
							notifyDataSetChanged();
						}
					}
				}
			});
		}		

		return convertView;
	}
	
	private class UserGroupView {
		private TextView usernameView = null;
		private Button usergrouppassBtn = null;
		private Button usergroupcancelBtn = null;
	}
}
