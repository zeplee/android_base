package com.gxyj.base.common.okhttp;

import android.os.Handler;
import android.os.Looper;

import com.gxyj.base.common.OkHttpException;

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
public class CommonStreamCallback implements Callback {
	private static String TAG = "HttpUtils";
	protected String COOKIE_STORE = "Set-Cookie"; // decide the server it
	/**
	 * the java layer exception, do not same to the logic error
	 */
	protected final String RESULT_ERROR = "-1"; // 请求异常
	protected String RESULT_MSG = "数据请求失败，请检查网络设置！";
	protected final String JSON_ERROR = "-2"; // 数据解析异常
	protected String JSON_MSG = "数据解析异常";
	protected final String OTHER_ERROR = "-3"; // 不明异常错误
	/**
	 * the logic layer exception, may alter in different app
	 */
	protected final String RESULT_CODE = "success"; // 数据请求返回状态码
	protected String RESULT_CODE_VALUE = "";// 数据请求返回状态值
	protected String MASSAGE = "";// 数据请求返回信息描述
	protected String LOGIN_MSG = "登录信息失效，请重新登录！";// 登陆失效
	/**
	 * 将其它线程的数据转发到UI线程
	 */
	private Handler mDeliveryHandler;
	private DisposeDataListener mListener;
	private Class<?> mClass;

	public CommonStreamCallback(DisposeDataHandle handle) {
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
				mListener.onFailure(new OkHttpException(RESULT_ERROR,
						RESULT_MSG));
			}
		});
	}

	@Override
	public void onResponse(final Call call, final Response response)
			throws IOException {
		final ArrayList<String> cookieLists = handleCookie(response.headers());
		mDeliveryHandler.post(new Runnable() {
			@Override
			public void run() {
				handleResponse(response);
				/**
				 * handle the cookie
				 */
				if (mListener instanceof DisposeHandleCookieListener) {
					((DisposeHandleCookieListener) mListener)
							.onCookie(cookieLists);
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

		if (responseObj != null) {
			mListener.onSuccess(responseObj);
		}

	}
}