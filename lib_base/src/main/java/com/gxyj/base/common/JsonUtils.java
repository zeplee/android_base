
package com.gxyj.base.common;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * JSON 公共处理函数
 * 
 * @author free
 * @version 0.1 2013-1-28 下午3:43:24
 */
public class JsonUtils{
	private static String	TAG	= "JSONUTILS";

	/**
	 * json 字符串转化为Map对象
	 * 
	 * @param json
	 * @return
	 * @throws AppException 
	 * @throws JSONException
	 */
	public static Map<String, String> jsonStringToMap(String json) throws OkHttpException
	{
		Map<String, String> jsonMap = new HashMap<String, String>();

		JSONObject jsonObject = getJsonObject(json);
		try
		{
			Iterator<?> keys = jsonObject.keys();
			while (keys.hasNext())
			{
				String key = "" + keys.next();
				jsonMap.put(key, jsonObject.getString(key));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new OkHttpException("-1","接口数据解析错误!");
		}
		
		return jsonMap;
	}

	/**
	 * 把字符串转化为JSON对象
	 * 
	 * @param json
	 * @return
	 * @throws AppException 
	 */
	public static JSONObject getJsonObject(String json) throws OkHttpException
	{
		JSONObject jsonObject = null;
		try
		{
			jsonObject = new JSONObject(json);
		}
		catch (JSONException e)
		{
			e.printStackTrace();
				throw new OkHttpException("-1","接口数据转换错误!");
		}
		return jsonObject;
	}

	/**
	 * 获取json字符串相应key的值
	 * 
	 * @param json
	 * @param key
	 * @return
	 * @throws AppException 
	 */
	public static String getJsonValueToKey(String json, String key) throws OkHttpException
	{

		String jsonValueString = "";
		JSONObject jsonObject = getJsonObject(json);
		if (jsonObject != null)
		{
			try
			{
				if(jsonObject.has(key)){
					jsonValueString = jsonObject.getString(key);
				}
			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}
			if (jsonValueString == null || "null".equals(jsonValueString))
			{
				jsonValueString = "";
			}
		}
		return jsonValueString;
	}

	/**
	 * 获取json对象相应key的值
	 * 
	 * @param jsonObject
	 * @param key
	 * @return
	 * @throws JSONException
	 */
	public static String getJsonValueToKey(JSONObject jsonObject, String key)
	{

		String jsonValueString = "";
		if (jsonObject != null)
		{
			try
			{
				if(jsonObject.has(key)){
					jsonValueString = jsonObject.getString(key);
				}
			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}
			if (jsonValueString == null || "null".equals(jsonValueString))
			{
				jsonValueString = "";
			}
		}
		return jsonValueString;
	}
}
