package com.todayinfo.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * json解析工具类
 * 
 * @author zhou.ni 2015年3月19日
 */
public class JsonUtils {

	public synchronized static <T> T getInstance(Class<T> arg0, JSONObject jsonObject) {
		try {
			T t = arg0.newInstance();
//			Field[] fields = arg0.getFields();
			Field[] fields = arg0.getDeclaredFields();		 //获取类成员，包括private私有的成员

			for (Field field : fields) {
				field.setAccessible(true);					//这时需要设置标志,setAccessible 默认是false
				
				if (!jsonObject.has(field.getName()))
					continue;

				if (jsonObject.get(field.getName()) != JSONObject.NULL) {
					Class<?> type = field.getType();
					if ((type.isPrimitive() || type.isAssignableFrom(String.class)) && !type.isArray()) {
						Object o = jsonObject.get(field.getName());
						if (o != JSONObject.NULL) {
							// field.set(t, jsonObject.get(field.getName()));
							field.set(t, o);
						}
					} 
					else if (type.isArray()) {
						Object object = jsonObject.get(field.getName());
						if (object instanceof String) {
							continue;
						}
						JSONArray jSONArray = jsonObject.getJSONArray(field.getName());
						Class<?> componentType = type.getComponentType();
						int length = jSONArray.length();
						Object array = Array.newInstance(componentType, length);
						
						for (int i = 0; i < length; i++) {
							if (componentType.isPrimitive() || componentType.isAssignableFrom(String.class)) {
								Array.set(array, i, jSONArray.get(i));
							} else {
								JSONObject jobj = jSONArray.getJSONObject(i);
								Object instance = getInstance(componentType, jobj);
								Array.set(array, i, instance);
							}
						}
						field.set(t, array);
					} 
					else {
						field.set(t, getInstance(type, jsonObject.getJSONObject(field.getName())));
					}
				}
			}
			return t;
		} catch (Exception e) {
			Log.e("json paser error", e.getMessage(), e);
		}
		return null;
	}

	public static <T> List<T> getInstance(Class<T> arg0, JSONArray jSONArray) throws JSONException {
		List<T> result = new ArrayList<T>();
		int length = jSONArray.length();
		for (int i = 0; i < length; i++) {
			result.add(getInstance(arg0, jSONArray.getJSONObject(i)));
		}
		return result;
	}

}
