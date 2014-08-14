package com.example.qq_loging_dome;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.example.qq_login_dome.R;
import com.tencent.connect.auth.QQAuth;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

public class ShareQzone extends Activity {
	
	 private QzoneShare mQzoneShare;
	 private QQShare mQqShare;
	 public QQAuth mQQAuth;  
	 public String APP_ID = "你的App_ID";
	 private String path ;
	 private int shareType = QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
		mQQAuth = QQAuth.createInstance(APP_ID, this.getApplicationContext());
		mQzoneShare = new QzoneShare(this, mQQAuth.getQQToken());
		mQqShare = new QQShare(this, mQQAuth.getQQToken());
		
		Button button = (Button) findViewById(R.id.btn);
		Button button_QQ = (Button) findViewById(R.id.btn_QQ);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				shareToQzone();	
				 Log.i("onClick --- ", "onClick - button");
			}
		});
		button_QQ.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				shareToQQshare();
			}
		});
	}
	 @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        String path = null;
	        if (resultCode == Activity.RESULT_OK) {
	            if (data != null && data.getData() != null) {
	                // 根据返回的URI获取对应的SQLite信息
	                Uri uri = data.getData();
	                final String[] proj = {
	                        MediaStore.Images.Media.DATA
	                };
	                Cursor cursor = this.getContentResolver().query(uri, proj, null, null, null);
	                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	                cursor.moveToFirst();
	                path = cursor.getString(column_index);
	            }
	        }
	        if (path != null) {
	        	// 这里很奇葩的方式, 将获取到的值赋值给相应的EditText, 竟然能对应上
	        	this.path = path;
	        } else {
	            Toast.makeText(ShareQzone.this, "请重新选择图片", 10000).show();
	        }
	    }

	    private static final void startPickLocaleImage(Activity activity, int requestId) {
	        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
	        intent.addCategory(Intent.CATEGORY_OPENABLE);
	        intent.setType("image/*");
	        activity.startActivityForResult(
	                Intent.createChooser(intent, activity.getString(R.string.str_image_local)), requestId);
	    }
	    
	    private void shareToQzone () {
	    	   //分享类型  分享Qzone
	    		ArrayList<String> imageUrls = new ArrayList<String>();
	    		imageUrls.add("http://p1.qhimg.com/dmt/528_351_/t014064f913228a6b77.jpg");
	    		Bundle params = new Bundle();
	    		params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,shareType );
	    	    params.putString(QzoneShare.SHARE_TO_QQ_TITLE, "QQ开放平台");//必填
	    	    params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "Qzone 分享 ");//选填
	    	    params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, "http://open.qq.com/");//必填
	    	    params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls );
	    	    doShareToQzone(params);
	    	}
	    private void shareToQQshare() {
	    	//分享类型    分享给QQ好友
	    	ArrayList<String> imageUrls = new ArrayList<String>();
	    	imageUrls.add("http://p1.qhimg.com/dmt/528_351_/t014064f913228a6b77.jpg");
	    	Bundle params = new Bundle();
	    	params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,shareType );
	    	params.putString(QQShare.SHARE_TO_QQ_TITLE, "QQ开放平台");//必填
	    	params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "Qzone 分享 ");//选填
	    	params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "http://open.qq.com/");//必填
	    	params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://p1.qhimg.com/dmt/528_351_/t014064f913228a6b77.jpg" );
	    	doShareToQQ(params);
	    }
	/**
     * 用异步方式启动分享
     * @param params
     */
    private void doShareToQzone(final Bundle params) {
        final Activity activity = ShareQzone.this;
        new Thread(new Runnable() {
            
            @Override
            public void run() {
                // TODO Auto-generated method stub
            	mQzoneShare.shareToQzone(activity, params, new IUiListener() {

                    @Override
                    public void onCancel() {
                        Log.i("onCancel --- ", "onCancel: ");
                    }

                    @Override
                    public void onError(UiError e) {
                        // TODO Auto-generated method stub
                    	 Log.i("onError --- ", e.toString());
                    }

					@Override
					public void onComplete(Object response) {
						// TODO Auto-generated method stub
						 Log.i("onComplete --- ",response.toString());
					}

                });
            }
        }).start();
    }
    /**
     * 用异步方式启动分享
     * @param params
     */
    private void doShareToQQ(final Bundle params) {
    	final Activity activity = ShareQzone.this;
    	new Thread(new Runnable() {
    		
    		@Override
    		public void run() {
    			// TODO Auto-generated method stub
    			mQqShare.shareToQQ(activity, params, new IUiListener() {
    				
    				@Override
    				public void onCancel() {
    					Log.i("onCancel --- ", "onCancel: ");
    				}
    				
    				@Override
    				public void onError(UiError e) {
    					// TODO Auto-generated method stub
    					Log.i("onError --- ", e.toString());
    				}
    				
    				@Override
    				public void onComplete(Object response) {
    					// TODO Auto-generated method stub
    					Log.i("onComplete --- ",response.toString());
    				}
    				
    			});
    		}
    	}).start();
    }
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	  if (mQzoneShare != null) {
              mQzoneShare = null;
          }
    	super.onDestroy();
    }
}
