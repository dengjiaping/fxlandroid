package com.aalife.android;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class HttpHelper {
    public static final int HTTP_REQUEST_TIMEOUT_MS = 10 * 1000;
    
	public static String post(String url, List<NameValuePair> params) {
		String result = "{\"result\":\"no\"}";
		try {
			HttpPost request = new HttpPost(url);
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = getHttpClient().execute(request);			

			if(response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity()).trim();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static String post(String url) {
		String result = "{\"result\":\"no\"}";
		try {
			HttpPost request = new HttpPost(url);
			HttpResponse response = getHttpClient().execute(request);			

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
	
	public static HttpClient getHttpClient() {
        HttpClient httpClient = new DefaultHttpClient();
        final HttpParams params = httpClient.getParams();
        HttpConnectionParams.setConnectionTimeout(params, HTTP_REQUEST_TIMEOUT_MS);
        HttpConnectionParams.setSoTimeout(params, HTTP_REQUEST_TIMEOUT_MS);
        ConnManagerParams.setTimeout(params, HTTP_REQUEST_TIMEOUT_MS);
        return httpClient;
    }
	
	public static String formatJson(List<Map<String, String>> list) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		Iterator<Map<String, String>> it = list.iterator();
		while(it.hasNext()) {
			sb.append("{");
			Map<String, String> map = it.next();
			Iterator<Entry<String, String>> im = map.entrySet().iterator();
			while(im.hasNext()) {
				Entry<String, String> entry = im.next();
				Object key = entry.getKey();
				Object value = entry.getValue();
				sb.append("\"" + key + "\":" + "\"" + value + "\",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append("},");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("]");
		
		return sb.toString();
	}
	
}
