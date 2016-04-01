package com.yxc.mamba.tool;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * 类注释
 * Created by robin on 16/3/30.
 *
 * @author yangxc
 */
public class JsonParser {

    public static JSONObject objToJson(Object object) {
        JSONObject jsonObject = new JSONObject();
        Field[] fields = object.getClass().getFields();
        for (Field field: fields){
            try {
                Object value = field.get(object);
                if (value instanceof String || value instanceof Integer || value instanceof Float
                        || value instanceof Double || value instanceof Boolean || value instanceof Long) {
                    jsonObject.put(field.getName(), value);
                } else if (value instanceof Map){
                    JSONObject mapJson = mapToJson((Map)value);
                    jsonObject.put(field.getName(), mapJson);
                } else if (value instanceof List){
                    JSONArray array = arrayToJson((List)value);
                    jsonObject.put(field.getName(), array);
                } else {
                    jsonObject.put(field.getName(), objToJson(value));
                }
            } catch (IllegalAccessException e) {
                Log.e("Error", "无此字段 : " + field.getName());
            } catch (JSONException e) {
                Log.e("Error", "设置JSON失败 : " + field.getName());
            }
        }
        return jsonObject;
    }


    public static JSONObject mapToJson(Map map) throws JSONException{
        JSONObject mapJson = new JSONObject();
        for (Object key: map.keySet()){
            Object valueMap = map.get(key);
            if (valueMap instanceof String || valueMap instanceof Integer || valueMap instanceof Float
                    || valueMap instanceof Double || valueMap instanceof Boolean || valueMap instanceof Long) {
                mapJson.put(key.toString(), valueMap);
            } else if (valueMap instanceof Map){
                mapJson.put(key.toString(), mapToJson((Map)valueMap));
            } else if (valueMap instanceof List){
                mapJson.put(key.toString(), arrayToJson((List)valueMap));
            } else {
                mapJson.put(key.toString(), objToJson(valueMap));
            }
        }
        return mapJson;
    }

    public static JSONArray arrayToJson(List list) throws JSONException{
        JSONArray array = new JSONArray();
        for (Object value: list){
            if (value instanceof String || value instanceof Integer || value instanceof Float
                    || value instanceof Double || value instanceof Boolean || value instanceof Long) {
                array.put(value);
            } else if (value instanceof Map){
                array.put(mapToJson((Map)value));
            } else if (value instanceof List){
                array.put(arrayToJson((List)value));
            } else {
                array.put(objToJson(value));
            }
        }
        return array;
    }

    public static <T> T parseJsonObject(JSONObject jsonObject, Class<T> cls) {
        T entity = null;
        try {
            entity = cls.newInstance();
        } catch (Exception e) {
            Log.e("Error", "反射JSON失败");
            return null;
        }
        Iterator<?> iterator = jsonObject.keys();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            Field field = null;
            try {
                Object value = jsonObject.opt(key);
                field = cls.getField(key);
                if (value instanceof String || value instanceof Integer || value instanceof Float
                        || value instanceof Double || value instanceof Boolean || value instanceof Long) {
                    field.set(entity, jsonObject.opt(key));
                } else if (value instanceof JSONObject) {
                    Type type = field.getGenericType();
                    if (type instanceof ParameterizedType) {
                        ParameterizedType parameterizedType = (ParameterizedType) type;
                        Type[] childTypes = parameterizedType.getActualTypeArguments();
                        if (childTypes.length == 2) {
                            Map<Object, Object> map = new HashMap<Object, Object>();
                            Iterator<?> mapIterator = ((JSONObject) value).keys();
                            while (mapIterator.hasNext()) {
                                String cKey = (String) mapIterator.next();
                                Log.d("KEY", cKey);
                                Object cValue = ((JSONObject) value).opt(cKey);
                                map.put(cKey, cValue);
                            }
                            field.set(entity, map);
                        }
                    } else {
                        try {
                            Object childEntity = parseJsonObject((JSONObject) value, (Class<?>) type);
                            field.set(entity, childEntity);
                        } catch (Exception e) {
                            Log.e("Error", cls.toString() + "类未找到child类" + type.toString());
                        }
                    }
                } else if (value instanceof JSONArray) {
                    Class<?> childClass = null;
                    List<Object> list = new ArrayList<Object>();
                    Type type = field.getGenericType();
                    if (type instanceof ParameterizedType) {
                        ParameterizedType parameterizedType = (ParameterizedType) type;
                        Type childType = parameterizedType.getActualTypeArguments()[0];
                        childClass = (Class<?>) childType;
                    }

                    JSONArray arrValue = (JSONArray) value;
                    for (int i = 0; i < arrValue.length(); i++) {
                        Object object = arrValue.opt(i);
                        Object child = null;
                        if (object instanceof JSONObject) {
                            child = parseJsonObject((JSONObject) object, childClass);
                        } else {
                            child = object;
                        }
                        list.add(child);
                    }
                    field.set(entity, list);
                }
            } catch (NoSuchFieldException e) {
                Log.e("Error", cls.toString() + "类缺少字段：" + key);
            } catch (IllegalAccessException e) {
                Log.e("Error", cls.toString() + "类字段" + key + "赋值失败");
            } catch (Exception e) {
                Log.e("Error", key + "\t" + e.getLocalizedMessage());
            }
        }

        return entity;
    }

    public static <T> List<T> parseJsonArray(JSONArray array, Class<T> cls) {
        if (array == null) {
            return null;
        }
        List<T> result = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            Object child = null;
            Object value = array.opt(i);
            if (value instanceof String || value instanceof Integer || value instanceof Float
                    || value instanceof Double || value instanceof Boolean || value instanceof Long) {
                child = value;
            } else if (value instanceof JSONObject) {
                child = parseJsonObject((JSONObject) value, cls);
            }
            result.add((T) child);
        }
        return result;
    }

}
