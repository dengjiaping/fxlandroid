package com.fxlweb.sexspider.sexspider;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by fengxl on 2014-4-6 006.
 */
public class HttpHelper {
    public static final int HTTP_REQUEST_TIMEOUT_MS = 5 * 1000;

    public HttpHelper() {}

    public static String getHttp(String url, String encode) {
        String result = "";
        try {
            HttpGet request = new HttpGet(url);
            HttpResponse response = getHttpClient().execute(request);

            if(response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response.getEntity(), encode);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static HttpClient getHttpClient() {
        HttpClient httpClient = new DefaultHttpClient();
        HttpParams params = httpClient.getParams();
        HttpConnectionParams.setConnectionTimeout(params, HTTP_REQUEST_TIMEOUT_MS);
        HttpConnectionParams.setSoTimeout(params, HTTP_REQUEST_TIMEOUT_MS);
        ConnManagerParams.setTimeout(params, HTTP_REQUEST_TIMEOUT_MS);
        return httpClient;
    }
}
