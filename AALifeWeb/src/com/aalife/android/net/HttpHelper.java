package com.aalife.android.net;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class HttpHelper {

	public static String post(String url, List<NameValuePair> params) {
		String result = "";
		try {
			HttpPost request = new HttpPost(url);
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
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
			HttpResponse response = defaultHttpClient.execute(request);			

			if(response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity()).trim();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
}
