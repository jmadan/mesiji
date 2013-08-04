package com.thirtysix.serendip;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.*;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

public class MesijiClient {
  private static final String BASE_URL = "http://floating-ocean.herokuapp.com/api";

  private static AsyncHttpClient client = new AsyncHttpClient();

  public static void get(String url, AsyncHttpResponseHandler responseHandler) {
	  System.out.println(getAbsoluteUrl(url));
      client.get(getAbsoluteUrl(url), responseHandler);
  }

  public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
      Log.e(Constants.LOG, params.toString());
      client.post(getAbsoluteUrl(url), params, responseHandler);

  }

  public static void post(Context context, String url, StringEntity stringEntity, String contentType, AsyncHttpResponseHandler responseHandler){
      System.out.println(">>>>>>>>>>>>>>"+url);
      client.post(context, getAbsoluteUrl(url), stringEntity, contentType, responseHandler);
  }

  private static String getAbsoluteUrl(String relativeUrl) {
      return BASE_URL + relativeUrl;
  }
}
