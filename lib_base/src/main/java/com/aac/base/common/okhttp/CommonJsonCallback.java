package com.aac.base.common.okhttp;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.aac.base.common.JsonUtils;
import com.aac.base.common.OkHttpException;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Response;

/**
 * @author vision
 * @function 专门处理JSON的回调
 */
public class CommonJsonCallback implements Callback {
	private static String	TAG = "HttpUtils";
	protected String COOKIE_STORE = "Set-Cookie"; // decide the server it
	/**
	 * the java layer exception, do not same to the logic error
	 */
	protected final String RESULT_ERROR = "-1"; // 请求异常
	protected String RESULT_MSG ="数据请求失败，请检查网络设置！";
	protected final String JSON_ERROR = "-2"; //数据解析异常
	protected String JSON_MSG = "数据解析异常";
	protected final String OTHER_ERROR = "-3"; //不明异常错误
	/**
	 * the logic layer exception, may alter in different app
	 */
	protected final String RESULT_CODE = "success"; // 数据请求返回状态码
	protected String RESULT_CODE_VALUE = "";// 数据请求返回状态值
	protected String MASSAGE = "";//数据请求返回信息描述
	protected String LOGIN_MSG = "登录信息失效，请重新登录！";//登陆失效
	/**
	 * 将其它线程的数据转发到UI线程
	 */
	private Handler mDeliveryHandler;
	private DisposeDataListener mListener;
	private Class<?> mClass;

	public CommonJsonCallback(DisposeDataHandle handle) {
		this.mListener = handle.mListener;
		this.mClass = handle.mClass;
		this.mDeliveryHandler = new Handler(Looper.getMainLooper());
	}

	@Override
	public void onFailure(final Call call, final IOException ioexception) {
		/**
		 * 此时还在非UI线程，因此要转发
		 */
		mDeliveryHandler.post(new Runnable() {
			@Override
			public void run() {
				mListener.onFailure(new OkHttpException(RESULT_ERROR, RESULT_MSG));
			}
		});
	}

	@Override
	public void onResponse(final Call call, final Response response) throws IOException {
		final String result = response.body().string();
		Log.i("result", result);
		final ArrayList<String> cookieLists = handleCookie(response.headers());
		mDeliveryHandler.post(new Runnable() {
			@Override
			public void run() {
				handleResponse(result);
				/**
				 * handle the cookie
				 */
				if (mListener instanceof DisposeHandleCookieListener) {
					((DisposeHandleCookieListener) mListener).onCookie(cookieLists);
				}
			}
		});
	}

	private ArrayList<String> handleCookie(Headers headers) {
		ArrayList<String> tempList = new ArrayList<String>();
		for (int i = 0; i < headers.size(); i++) {
			if (headers.name(i).equalsIgnoreCase(COOKIE_STORE)) {
				tempList.add(headers.value(i));
			}
		}
		return tempList;
	}

	private void handleResponse(Object responseObj) {
		if (responseObj == null) {
			mListener.onFailure(new OkHttpException(RESULT_ERROR, RESULT_MSG));
			return;
		}
		try {

			JSONObject result = JsonUtils.getJsonObject(responseObj.toString());
			RESULT_CODE_VALUE=JsonUtils.getJsonValueToKey(result,RESULT_CODE);
			if(JsonUtils.getJsonValueToKey(result, "msg_casuseby").equals("")){
				MASSAGE=JsonUtils.getJsonValueToKey(result, "msg");
			}else{
				MASSAGE=JsonUtils.getJsonValueToKey(result, "msg_casuseby");
			}
			if ("0000".equals(RESULT_CODE_VALUE)) {
				if (mClass == null) {
					mListener.onSuccess(result);
				} else {
					Object obj = ResponseEntityToModule.parseJsonObjectToModule(result, mClass);
					if (obj != null) {
						mListener.onSuccess(obj);
					} else {
						mListener.onFailure(new OkHttpException(JSON_ERROR, JSON_MSG));
					}
				}
			} else {
				if ("0100".equals(JsonUtils.getJsonValueToKey(result,
						RESULT_CODE))) {
					mListener.onFailure(new OkHttpException(RESULT_CODE_VALUE,LOGIN_MSG));
				} else {
					mListener.onFailure(new OkHttpException(RESULT_CODE_VALUE,MASSAGE));
				}
			}

		} catch (OkHttpException e) {
			mListener.onFailure(new OkHttpException(OTHER_ERROR,e.getEmsg()));
			e.printStackTrace();
		}
	}
}