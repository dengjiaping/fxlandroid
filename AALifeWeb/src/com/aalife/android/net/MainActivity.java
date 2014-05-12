package com.aalife.android.net;

import java.lang.ref.WeakReference;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.DownloadListener;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends Activity {
    private WebView webView = null;
    private ProgressBar loading = null;
	private MyHandler myHandler = new MyHandler(this);
    private ValueCallback<Uri> mUploadMessage = null;
    private final static int FILECHOOSER_RESULTCODE = 1;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//初始化
		loading = (ProgressBar) super.findViewById(R.id.loading);
	
		//网页
		webView = (WebView) super.findViewById(R.id.webViewMain);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		//webView.loadUrl("http://10.0.2.2:81/shouji/Default.aspx");
		webView.loadUrl("http://www.fxlweb.com");
		webView.setWebViewClient(new AALifeWebClient());
		webView.setWebChromeClient(new AALifeWebChromeClient());
		webView.setDownloadListener(new AALifeWebDownload());
		
		//检查新版本
		if(MyHelper.checkInternet(this)) {
			new Thread(new Runnable(){
				@Override
				public void run() {
					boolean result = false;
					try {
						result = MyHelper.checkNewVersion(MainActivity.this);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					Bundle bundle = new Bundle();
					bundle.putBoolean("result", result);	
					Message message = new Message();
					message.what = 1;
					message.setData(bundle);
					myHandler.sendMessage(message);
				}
			}).start();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_exit:
			exitActivity();
			break;
		case R.id.action_about:
			Dialog dialog = new AlertDialog.Builder(this)
					.setTitle(R.string.string_about)
					.setMessage(R.string.string_abouttext)
					.setNegativeButton(R.string.string_sure, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							dialog.cancel();
						}
					}).create();
			dialog.show();
			break;
		}
		
		return false;
	}
	
	//退出 
	protected void exitActivity() {
		this.finish();
	}
    
	//返回调用
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
	    if (requestCode == FILECHOOSER_RESULTCODE) {
	        if (null == mUploadMessage) return;
	        Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
	        mUploadMessage.onReceiveValue(result);
	        mUploadMessage = null;
	    }
	}
	
	//多线程处理
	static class MyHandler extends Handler {
		WeakReference<MainActivity> myActivity = null;
		MyHandler(MainActivity activity) {
			myActivity = new WeakReference<MainActivity>(activity);
		}
		
		@Override
		public void handleMessage(Message msg) {
			boolean result = false;
			final MainActivity activity = myActivity.get();
			switch(msg.what) {
			case 1:
				result = msg.getData().getBoolean("result");
				if(result) {					
					Dialog dialog = new AlertDialog.Builder(activity)
							.setTitle(R.string.string_about)
							.setMessage(R.string.string_newversion)
							.setPositiveButton(R.string.string_sure, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
									Toast.makeText(activity, activity.getString(R.string.string_downing), Toast.LENGTH_SHORT).show();
									activity.downNewFile();
								}
							}).setNegativeButton(R.string.string_cancel, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
									dialog.cancel();
								}
							}).create();
					dialog.show();
				}

				break;
			case 2:
				result = msg.getData().getBoolean("result");
				if(result) {
					activity.exitActivity();
				} else {
					Toast.makeText(activity, activity.getString(R.string.string_updateerror), Toast.LENGTH_SHORT).show();
				}

				break;				
			}				
		}			
	};
	
	//升级
	protected void downNewFile() {
		new Thread(new Runnable(){
			@Override
			public void run() {
				boolean result = false;
				try {
					Uri uri = Uri.fromFile(MyHelper.getInstallFile());
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(uri, "application/vnd.android.package-archive");
					startActivity(intent);
					
					result = true;
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				Bundle bundle = new Bundle();
				bundle.putBoolean("result", result);	
				Message message = new Message();
				message.what = 2;
				message.setData(bundle);
				myHandler.sendMessage(message);
			}
		}).start();
	}
	
	//网页
	private class AALifeWebClient extends WebViewClient {
		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			view.loadDataWithBaseURL(null, getString(R.string.string_nointernet), "text/html", "UTF-8", null);	
			Dialog dialog = new AlertDialog.Builder(MainActivity.this)
					.setTitle(R.string.string_warn)
					.setMessage(R.string.string_nointernet)
					.setPositiveButton(R.string.string_sure, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							onCreate(null);
						}
					}).setNegativeButton(R.string.string_cancel, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							dialog.cancel();
						}
					}).create();
			dialog.show();
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			loading.setVisibility(ProgressBar.GONE);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			loading.setVisibility(ProgressBar.VISIBLE);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}		
	}
	
	//网页JS事件
	private class AALifeWebChromeClient extends WebChromeClient {
		@Override
		public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
			Dialog alert = new AlertDialog.Builder(MainActivity.this)
			.setTitle(R.string.string_warn)
			.setMessage(message)
			.setPositiveButton(R.string.string_sure, new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int whichButton) {
					result.confirm();
				}				
			}).setNegativeButton(R.string.string_cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					result.cancel();
				}
			}).create();
			alert.setCancelable(false);
			alert.show();
			return true;
		}

		@Override
		public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
			Dialog alert = new AlertDialog.Builder(MainActivity.this)
			.setTitle(R.string.string_warn)
			.setMessage(message)
			.setPositiveButton(R.string.string_sure, new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int whichButton) {
					result.confirm();
				}				
			}).create();
			alert.setCancelable(false);
			alert.show();
			return true;
		}
		
		// For Android 3.0+
		public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType){
			mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            MainActivity.this.startActivityForResult(Intent.createChooser(i, getString(R.string.string_chooserimage)), FILECHOOSER_RESULTCODE);
		}
		
		// For Android < 3.0
		@SuppressWarnings("unused")
		public void openFileChooser(ValueCallback<Uri> uploadMsg) {
			openFileChooser(uploadMsg, "");
		}
		
		// For Android > 4.1.1
		@SuppressWarnings("unused")
		public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
			openFileChooser(uploadMsg, acceptType);
		}
	}
	
	//网页下载
	private class AALifeWebDownload implements DownloadListener {
		@Override
		public void onDownloadStart(String url, String userAgent,
				String contentDisposition, String mimetype, long contentLength) {
			Uri uri = Uri.parse(url);  
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);  
            startActivity(intent); 
		}		
	}
	
}
