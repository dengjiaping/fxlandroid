package com.aalife.android;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

public class HttpHelper
{
  public static final int HTTP_CONN_TIMEOUT_MS = 5000;
  public static final int HTTP_SO_TIMEOUT_MS = 10000;
  
  public static String formatJson(List<Map<String, String>> paramList)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[");
    Iterator localIterator1 = paramList.iterator();
    if (!localIterator1.hasNext())
    {
      localStringBuilder.deleteCharAt(-1 + localStringBuilder.length());
      localStringBuilder.append("]");
      return localStringBuilder.toString();
    }
    localStringBuilder.append("{");
    Iterator localIterator2 = ((Map)localIterator1.next()).entrySet().iterator();
    for (;;)
    {
      if (!localIterator2.hasNext())
      {
        localStringBuilder.deleteCharAt(-1 + localStringBuilder.length());
        localStringBuilder.append("},");
        break;
      }
      Map.Entry localEntry = (Map.Entry)localIterator2.next();
      Object localObject1 = localEntry.getKey();
      Object localObject2 = localEntry.getValue();
      localStringBuilder.append("\"" + localObject1 + "\":" + "\"" + localObject2 + "\",");
    }
  }
  
  public static HttpClient getHttpClient()
  {
    DefaultHttpClient localDefaultHttpClient = new DefaultHttpClient();
    HttpParams localHttpParams = localDefaultHttpClient.getParams();
    HttpConnectionParams.setConnectionTimeout(localHttpParams, 5000);
    HttpConnectionParams.setSoTimeout(localHttpParams, 10000);
    ConnManagerParams.setTimeout(localHttpParams, 10000L);
    return localDefaultHttpClient;
  }
  
  public static HttpClient getHttpClient(int paramInt1, int paramInt2)
  {
    DefaultHttpClient localDefaultHttpClient = new DefaultHttpClient();
    HttpParams localHttpParams = localDefaultHttpClient.getParams();
    HttpConnectionParams.setConnectionTimeout(localHttpParams, paramInt1);
    HttpConnectionParams.setSoTimeout(localHttpParams, paramInt2);
    ConnManagerParams.setTimeout(localHttpParams, paramInt2);
    return localDefaultHttpClient;
  }
  
  public static String post(String paramString)
  {
    Object localObject = "{\"result\":\"no\"}";
    try
    {
      HttpPost localHttpPost = new HttpPost(paramString);
      HttpResponse localHttpResponse = getHttpClient().execute(localHttpPost);
      if (localHttpResponse.getStatusLine().getStatusCode() == 200)
      {
        String str = EntityUtils.toString(localHttpResponse.getEntity()).trim();
        localObject = str;
      }
      return localObject;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return localObject;
  }
  
  public static String post(String paramString, int paramInt1, int paramInt2)
  {
    Object localObject = "{\"result\":\"no\"}";
    try
    {
      HttpPost localHttpPost = new HttpPost(paramString);
      HttpResponse localHttpResponse = getHttpClient(paramInt1, paramInt2).execute(localHttpPost);
      if (localHttpResponse.getStatusLine().getStatusCode() == 200)
      {
        String str = EntityUtils.toString(localHttpResponse.getEntity()).trim();
        localObject = str;
      }
      return localObject;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return localObject;
  }
  
  public static String post(String paramString, List<NameValuePair> paramList)
  {
    Object localObject = "{\"result\":\"no\"}";
    try
    {
      HttpPost localHttpPost = new HttpPost(paramString);
      localHttpPost.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
      HttpResponse localHttpResponse = getHttpClient().execute(localHttpPost);
      if (localHttpResponse.getStatusLine().getStatusCode() == 200)
      {
        String str = EntityUtils.toString(localHttpResponse.getEntity()).trim();
        localObject = str;
      }
      return localObject;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return localObject;
  }
}


/* Location:           D:\fxlandroid\document\app\AALifeNew_v3.1.4\classes_dex2jar.jar
 * Qualified Name:     com.aalife.android.HttpHelper
 * JD-Core Version:    0.7.0-SNAPSHOT-20130630
 */