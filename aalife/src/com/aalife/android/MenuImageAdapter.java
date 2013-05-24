package com.aalife.android;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class MenuImageAdapter extends BaseAdapter {
	private Context context;
	private ImageView[] menuImg;
	private int[] imgIds;

	public MenuImageAdapter(Context context, int imgIds[], int width, int height, int selectedMenuImg) {
		this.context = context;
		this.imgIds = imgIds;
		this.menuImg = new ImageView[imgIds.length];
		for (int x = 0; x < imgIds.length; x++) {
			this.menuImg[x] = new ImageView(this.context);
			this.menuImg[x].setLayoutParams(new GridView.LayoutParams(width, LayoutParams.FILL_PARENT));
			this.menuImg[x].setAdjustViewBounds(false);
			this.menuImg[x].setImageResource(imgIds[x]);
		}
	}

	@Override
	public int getCount() {
		return this.menuImg.length;
	}

	@Override
	public Object getItem(int position) {
		return this.menuImg[position];
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imgView = null;
		if (convertView == null) {
			imgView = this.menuImg[position];
		} else {
			imgView = (ImageView) convertView;
		}
		return imgView;
	}

	public void setFocus(int selId) {
		for (int x = 0; x < imgIds.length; x++) {
			this.menuImg[x].setImageResource(imgIds[x]);
		}
		int f = 0;
		switch (selId) {
		case 0:
			f = R.drawable.ic_day_f;
			break;
		case 1:
			f = R.drawable.ic_month_f;
			break;
		case 2:
			f = R.drawable.ic_rank_f;
			break;
		case 3:
			f = R.drawable.ic_tongji_f;
			break;
		case 4:
			f = R.drawable.ic_add_f;
			break;
		}
		this.menuImg[selId].setImageResource(f);
	}
}
