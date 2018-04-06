package com.github.youyinnn.youwebutils.second;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.youyinnn.youwebutils.third.YouCollectionsUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.Stack;

/**
 * @author youyinnn
 */
public class JsonHelper {

    private static JSONObject jsonPool;

    public static void initJsonPool(String jsonFilePath) throws IOException {
        jsonPool = readJsonFile(jsonFilePath);
    }

    public static void initJsonPool(File jsonFile) throws IOException {
        jsonPool = readJsonFile(jsonFile);
    }

    public static JSONObject readJsonFile(File jsonFile) throws IOException {
        StringBuffer sb = new StringBuffer("");
        FileReader reader = new FileReader(jsonFile);
        char[] buffer = new char[24];
        int len;
        while ((len = reader.read(buffer)) != -1) {
            sb.append(new String(buffer, 0 , len));
        }
        return JSON.parseObject(String.valueOf(sb));
    }

    public static JSONObject readJsonFile(String jsonFilePath) throws IOException {
        return readJsonFile(new File(jsonFilePath));
    }

    public static String put(String srcJsonStr, String key, Object value) {
        JSONObject jsonObject = JSON.parseObject(srcJsonStr);
        jsonObject.put(key, value);
        return jsonObject.toJSONString();
    }

    public static String put(String srcJsonStr, HashMap<String, Object> pairs) {
        JSONObject jsonObject = JSON.parseObject(srcJsonStr);
        jsonObject.putAll(pairs);
        return jsonObject.toJSONString();
    }

    public static void putInPool(String key, Object value) {
        jsonPool.put(key, value);
    }

    public static void putInPool(HashMap<String, Object> pairs) {
        jsonPool.putAll(pairs);
    }

    public static JSONObject getJSONObjectInPool(String key){
        return jsonPool.getJSONObject(key);
    }

    public static String getStringInPool(String key){
        return jsonPool.getString(key);
    }

    public static Boolean getBooleanInPool(String key){
        return jsonPool.getBoolean(key);
    }

    public static Date getDateInPool(String key){
        return jsonPool.getDate(key);
    }

    public static Double getDoubleInPool(String key){
        return jsonPool.getDouble(key);
    }

    public static Float getFloatInPool(String key){
        return jsonPool.getFloat(key);
    }

    public static Integer getIntegerInPool(String key){
        return jsonPool.getInteger(key);
    }

    public static Long getLongInPool(String key){
        return jsonPool.getLong(key);
    }

    public static Short getShortInPool(String key){
        return jsonPool.getShort(key);
    }

    public static <T> T getObjectInPool(String key, Class<T> clazz){
        return jsonPool.getObject(key, clazz);
    }

    public static Byte getByteInPool(String key){
        return jsonPool.getByte(key);
    }

    public static byte[] getBytesInPool(String key){
        return jsonPool.getBytes(key);
    }

    public static JSONArray getJSONArrayInPool(String key){
        return jsonPool.getJSONArray(key);
    }

    public static Byte getByteDeepInPool(String key) {
        return (Byte) deepSearchInPool(key);
    }

    public static byte[] getBytesDeepInPool(String key) {
        return (byte[]) deepSearchInPool(key);
    }
    public static JSONArray getJSONArrayDeepInPool(String key) {
        return (JSONArray) deepSearchInPool(key);
    }
    public static Date getDateDeepInPool(String key) {
        return (Date) deepSearchInPool(key);
    }
    public static Short getShortDeepInPool(String key) {
        return (Short) deepSearchInPool(key);
    }
    public static Object getObjectDeepInPool(String key) {
        return deepSearchInPool(key);
    }

    public static String getStringDeepInPool(String key) {
        return (String) deepSearchInPool(key);
    }
    public static Integer getIntegerDeepInPool(String key) {
        return (Integer) deepSearchInPool(key);
    }
    public static Boolean getBooleanDeepInPool(String key) {
        return (Boolean) deepSearchInPool(key);
    }
    public static Long getLongDeepInPool(String key) {
        return (Long) deepSearchInPool(key);
    }
    public static Double getDoubleDeepInPool(String key) {
        return (Double) deepSearchInPool(key);
    }
    public static Float getFloatDeepInPool(String key) {
        return (Float) deepSearchInPool(key);
    }
    public static JSONObject getJSONObjectDeepInPool(String key) {
        return (JSONObject) deepSearchInPool(key);
    }

    public static Byte getByteDeepInPool(String parentKey, String key) {
        return (Byte) deepSearchInPool(parentKey, key);
    }

    public static byte[] getBytesDeepInPool(String parentKey,String key) {
        return (byte[]) deepSearchInPool(parentKey,key);
    }
    public static JSONArray getJSONArrayDeepInPool(String parentKey,String key) {
        return (JSONArray) deepSearchInPool(parentKey,key);
    }
    public static Date getDateDeepInPool(String parentKey,String key) {
        return (Date) deepSearchInPool(parentKey,key);
    }
    public static Short getShortDeepInPool(String parentKey,String key) {
        return (Short) deepSearchInPool(parentKey,key);
    }
    public static Object getObjectDeepInPool(String parentKey,String key) {
        return deepSearchInPool(parentKey,key);
    }

    public static String getStringDeepInPool(String parentKey,String key) {
        return (String) deepSearchInPool(parentKey,key);
    }
    public static Integer getIntegerDeepInPool(String parentKey,String key) {
        return (Integer) deepSearchInPool(parentKey,key);
    }
    public static Boolean getBooleanDeepInPool(String parentKey,String key) {
        return (Boolean) deepSearchInPool(parentKey,key);
    }
    public static Long getLongDeepInPool(String parentKey,String key) {
        return (Long) deepSearchInPool(parentKey,key);
    }
    public static Double getDoubleDeepInPool(String parentKey,String key) {
        return (Double) deepSearchInPool(parentKey,key);
    }
    public static Float getFloatDeepInPool(String parentKey,String key) {
        return (Float) deepSearchInPool(parentKey,key);
    }
    public static JSONObject getJSONObjectDeepInPool(String parentKey,String key) {
        return (JSONObject) deepSearchInPool(parentKey,key);
    }


    public static Object deepSearch(String jsonText, String key) {
        JSONObject jsonObject = JSON.parseObject(jsonText);
        Set<String> keySet = jsonObject.keySet();
        Stack<JSONObject> stack = new Stack<>();
        for (String firstKeys : keySet) {
            if (firstKeys.equals(key)) {
                return jsonObject.get(key);
            }
            if (isJsonObject(jsonObject.getString(firstKeys))) {
                stack.push(jsonObject.getJSONObject(firstKeys));
                while (!stack.empty()) {
                    JSONObject currentNode = stack.pop();
                    if (currentNode != null) {
                        for (String currentKey : currentNode.keySet()) {
                            if (currentKey.equals(key)) {
                                return currentNode.get(currentKey);
                            }
                            if (isJsonObject(currentNode.getString(currentKey))) {
                                stack.push(currentNode.getJSONObject(currentKey));
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public static Object deepSearchInPool(String key){
        return deepSearch(jsonPool.toString(), key);
    }

    public static Object deepSearch(String jsonText, String parentKey, String key) {
        Object parentJson = deepSearch(jsonText, parentKey);
        if (parentJson != null) {
            return deepSearch((String) parentJson, key);
        } else {
            return null;
        }
    }

    public static Object deepSearchInPool(String parentKey, String key) {
        Object parentJson = deepSearch(jsonPool.toString(), parentKey);
        if (parentJson != null) {
            return deepSearch(parentJson.toString(), key);
        } else {
            return null;
        }
    }

    public static String getJsonStr(Object ... objects) {
        return getJsonObject(objects).toJSONString();
    }

    public static JSONObject getJsonObject(Object ... objects) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.putAll(YouCollectionsUtils.getYouHashMap(objects));
        return jsonObject;
    }

    private static boolean isJsonObject(String text){
        try {
            JSON.parseObject(text);
            return true;
        } catch (Exception ignore) {
            return false;
        }
    }

}
