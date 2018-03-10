package com.github.youyinnn.youwebutils.second;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Properties;

/**
 * @author: youyinnn
 */
public class PropertiesHelper {

    private PropertiesHelper(){}

    public static void load(String proFilePath, Properties properties) {
        try {
            properties.load(PropertiesHelper.class.getResourceAsStream(proFilePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printSystemProperties(){
        Properties props= System.getProperties();
        System.out.println("===============================================================================================");
        System.out.println("Java的运行环境版本："+props.getProperty("java.version"));
        System.out.println("Java的类路径："+props.getProperty("java.class.path"));
        System.out.println("加载库时搜索的路径列表："+props.getProperty("java.library.path"));
        System.out.println("默认的临时文件路径："+props.getProperty("java.io.tmpdir"));
        System.out.println("操作系统的名称："+props.getProperty("os.name"));
        System.out.println("操作系统的构架："+props.getProperty("os.arch"));
        System.out.println("操作系统的版本："+props.getProperty("os.version"));
        System.out.println("文件分隔符："+props.getProperty("file.separator"));
        System.out.println("路径分隔符："+props.getProperty("path.separator"));
        System.out.println("行分隔符："+props.getProperty("line.separator"));
        System.out.println("用户的账户名称："+props.getProperty("user.name"));
        System.out.println("用户的主目录："+props.getProperty("user.home"));
        System.out.println("用户的当前工作目录："+props.getProperty("user.dir"));
        System.out.println("===============================================================================================");
    }

    public static String getSystemPropertiesJson() {
        Properties props= System.getProperties();
        HashMap<String , String> propMap = new HashMap<>();
        propMap.put("java.version", props.getProperty("java.version"));
        propMap.put("java.io.tmpdir", props.getProperty("java.io.tmpdir"));
        propMap.put("os.name", props.getProperty("os.name"));
        propMap.put("os.arch", props.getProperty("os.arch"));
        propMap.put("os.version", props.getProperty("os.version"));
        propMap.put("file.separator", props.getProperty("file.separator"));
        propMap.put("path.separator", props.getProperty("path.separator"));
        propMap.put("line.separator", props.getProperty("line.separator"));
        propMap.put("user.name", props.getProperty("user.name"));
        propMap.put("user.home", props.getProperty("user.home"));
        propMap.put("user.dir", props.getProperty("user.dir"));
        return JSON.toJSONString(propMap);
    }

    public static String getPID() {
        return ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
    }
}
