package com.aac.base.common.okhttp;

import java.io.File;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;


/**
 * @author vision
 * @function build the request
 */
public class CommonRequest {
	private static String TAG = "HttpUtils";

	/**
	 * create the key-value Request
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static Request createPostRequest(String url, RequestParams params,
                                            String lc, String s) {
		StringBuffer sb = new StringBuffer();
		if (params != null) {
			for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
				sb.append(entry.getKey() + "=" + entry.getValue() + "&");
			}
		}
		RequestBody body = RequestBody.create(MediaType
				.parse("application/x-www-form-urlencoded;charset=utf-8"), sb
				.toString());
		
		return new Request.Builder().url(url)
				.addHeader("cookie", lc + ";" + s).post(body).build();
	}

	/**
	 * create the key-value Request
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static Request createPostRequest(String url, RequestParams params,
                                            String lc) {
		StringBuffer sb = new StringBuffer();
		if (params != null) {
			for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
				sb.append(entry.getKey() + "=" + entry.getValue() + "&");
			}
		}
		RequestBody body = RequestBody.create(MediaType
				.parse("application/x-www-form-urlencoded;charset=utf-8"), sb
				.toString());
		return new Request.Builder().url(url).addHeader("cookie", lc)
				.post(body).build();
	}

	/**
	 * create the key-value Request
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static Request createPostRequest(String url, RequestParams params) {
		StringBuffer sb = new StringBuffer();
		if (params != null) {
			for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
				sb.append(entry.getKey() + "=" + entry.getValue() + "&");
			}
		}
		RequestBody body = RequestBody.create(MediaType
				.parse("application/x-www-form-urlencoded;charset=utf-8"), sb
				.toString());
		return new Request.Builder().url(url).post(body).build();
	}

	/**
	 * ressemble the params to the url
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static Request createGetRequest(String url, RequestParams params) {
		StringBuilder urlBuilder = new StringBuilder(url).append("?");
		if (params != null) {
			for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
				urlBuilder.append(entry.getKey()).append("=")
						.append(entry.getValue()).append("&");
			}
		}
		return new Request.Builder()
				.url(urlBuilder.substring(0, urlBuilder.length() - 1)).get()
				.build();
	}

	/**
	 * 文件上传请求
	 * 
	 * @return
	 */
	private static final MediaType FILE_TYPE = MediaType
			.parse("application/octet-stream");

	public static Request createMultiPostRequest(String url,
                                                 RequestParams params) {

		MultipartBody.Builder requestBody = new MultipartBody.Builder();
		requestBody.setType(MultipartBody.FORM);
		if (params != null) {

			for (Map.Entry<String, Object> entry : params.fileParams.entrySet()) {
				if (entry.getValue() instanceof File) {
					requestBody.addPart(Headers.of("Content-Disposition",
							"form-data; name=\"" + entry.getKey() + "\""),
							RequestBody.create(FILE_TYPE,
									(File) entry.getValue()));
				} else if (entry.getValue() instanceof String) {

					requestBody
							.addPart(Headers.of("Content-Disposition",
									"form-data; name=\"" + entry.getKey()
											+ "\""), RequestBody.create(null,
									(String) entry.getValue()));
				}
			}
		}
		return new Request.Builder().url(url).post(requestBody.build()).build();
	}
}
