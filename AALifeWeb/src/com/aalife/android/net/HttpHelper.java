package com.aalife.android.net;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class HttpHelper {

	public static String post(String url, List<NameValuePair> params) {
		String result = "";
		try {
			HttpPost request = new HttpPost(url);
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
			//defaultHttpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
			//defaultHttpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);
			HttpResponse response = defaultHttpClient.execute(request);			

			if(response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity()).trim();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static String post(String url) {
		String result = "";
		try {
			HttpPost request = new HttpPost(url);
			DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
			//defaultHttpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
			//defaultHttpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);
			HttpResponse response = defaultHttpClient.execute(request);			

			if(response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity()).trim();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static Bitmap postBitmap(String url) {
		Bitmap bitmap = null;
		URL myUrl = null;
		try {
			myUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) myUrl.openConnection();
			//conn.setConnectTimeout(10000);
			if(conn.getResponseCode() == 200) {
				InputStream is = conn.getInputStream();
				bitmap = BitmapFactory.decodeStream(is);
				is.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return bitmap;
	}	
}
