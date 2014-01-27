package com.aalife.android.net;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends Activity {
    private WebView webView = null;
    private ProgressBar loading = null;
    private ValueCallback<Uri> mUploadMessage = null;
    private final static int FILECHOOSER_RESULTCODE = 1;
        
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		if(android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		loading = (ProgressBar) super.findViewById(R.id.progressBar1);
	
		webView = (WebView) super.findViewById(R.id.webView1);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.loadUrl("http://www.fxlweb.com");
		webView.setWebViewClient(new AALifeWebClient());
		webView.setWebChromeClient(new AALifeWebChromeClient());
		
		if(MyHelper.checkInternet(this)) {
			if(MyHelper.checkNewVersion(this)) {
				Toast.makeText(MainActivity.this, R.string.string_newversion, Toast.LENGTH_SHORT).show();
				
				try {
					Uri uri = Uri.fromFile(MyHelper.getInstallFile());
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(uri, "application/vnd.android.package-archive");
					startActivity(intent);
				} catch(Exception e) {
					e.printStackTrace();
					Toast.makeText(MainActivity.this, R.string.string_updateerror, Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		exitActivity();
		return true;
	}
	
	//退出 
	protected void exitActivity() {
		this.finish();
	}
	
	private class AALifeWebClient extends WebViewClient{
		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			view.loadDataWithBaseURL(null, getString(R.string.string_nointernet), "text/html", "UTF-8", null);			
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
		public void openFileChooser(ValueCallback<Uri> uploadMsg) {
			openFileChooser(uploadMsg, "");
		}
		
		// For Android > 4.1.1
		public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
			openFileChooser(uploadMsg, acceptType);
		}
	}
}
