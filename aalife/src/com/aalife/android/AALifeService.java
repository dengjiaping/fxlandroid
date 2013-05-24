package com.aalife.android;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.IBinder;
import android.widget.RemoteViews;

public class AALifeService extends Service {
	private String priceText = "";
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		
		RemoteViews remote = buildRemoteView(this);
		pushUpdate(remote);
		
		//System.out.println("onConfigurationChanged...");
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		//System.out.println("onCreate...");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//System.out.println("onDestroy...");
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		
		priceText = UtilityHelper.getMonthPrice(this);
		
		RemoteViews remote = buildRemoteView(this);
		pushUpdate(remote);
		
		//System.out.println("onStart...");
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public RemoteViews buildRemoteView(Context context) {
		RemoteViews remote = new RemoteViews(context.getPackageName(), R.layout.appwidgetlayout);
		return remote;
	}
	
	private void pushUpdate(RemoteViews remote)
    {
        ComponentName myWidget = new ComponentName(this, AALifeAppWidget.class);
        Intent intent = new Intent(this, AALifeActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		
        remote.setTextViewText(R.id.pricetext, "￥ " + priceText);		
		remote.setOnClickPendingIntent(R.id.imagebtn, pendingIntent);
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        manager.updateAppWidget(myWidget, remote);
    }

}
