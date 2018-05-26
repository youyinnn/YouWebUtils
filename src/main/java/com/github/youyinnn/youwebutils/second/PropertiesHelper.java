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

    private static Properties props= System.getProperties();

    private PropertiesHelper(){}

    public static void load(String proFilePath, Properties properties) {
        try {
            properties.load(PropertiesHelper.class.getResourceAsStream(proFilePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printSystemProperties(){
        System.out.println("===============================================================================================");
        System.out.println("Java的运行环境版本：" + getJavaVersion());
        System.out.println("Java的类路径：" + getJavaClassPath());
        System.out.println("加载库时搜索的路径列表：" + getJavaLibraryPath());
        System.out.println("默认的临时文件路径：" + getIoTempDir());
        System.out.println("操作系统的名称：" + getOsName());
        System.out.println("操作系统的构架：" + getOsArch());
        System.out.println("操作系统的版本：" + getOsVersion());
        System.out.println("文件分隔符：" + getFileSeparator());
        System.out.println("路径分隔符：" + getPathSeparator());
        System.out.println("行分隔符：" + getLineSeparator());
        System.out.println("用户的账户名称：" + getUserName());
        System.out.println("用户的主目录：" + getUserHome());
        System.out.println("用户的当前工作目录：" + getUserDir());
        System.out.println("===============================================================================================");
    }

    public static String getSystemPropertiesJson() {
        HashMap<String , String> propMap = new HashMap<>(11);
        propMap.put("java.version", getJavaVersion());
        propMap.put("java.io.tmpdir", getIoTempDir());
        propMap.put("os.name", getOsName());
        propMap.put("os.arch", getOsArch());
        propMap.put("os.version", getOsVersion());
        propMap.put("file.separator", getFileSeparator());
        propMap.put("path.separator", getPathSeparator());
        propMap.put("line.separator", getLineSeparator());
        propMap.put("user.name", getUserName());
        propMap.put("user.home", getUserHome());
        propMap.put("user.dir", getUserDir());
        return JSON.toJSONString(propMap);
    }

    public static String getPID() {
        return ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
    }

    public static String getJavaVersion() {
        return props.getProperty("java.version");
    }
    public static String getIoTempDir() {
        return props.getProperty("java.io.tmpdir");
    }
    public static String getOsName() {
        return props.getProperty("os.home");
    }
    public static String getOsArch() {
        return props.getProperty("os.arch");
    }
    public static String getOsVersion() {
        return props.getProperty("os.version");
    }
    public static String getFileSeparator() {
        return props.getProperty("file.separator");
    }
    public static String getPathSeparator() {
        return props.getProperty("path.separator");
    }
    public static String getLineSeparator() {
        return props.getProperty("line.separator");
    }
    public static String getUserName() {
        return props.getProperty("user.name");
    }
    public static String getUserDir() {
        return props.getProperty("user.dir");
    }
    public static String getUserHome() {
        return props.getProperty("user.home");
    }
    public static String getJavaClassPath() {
        return props.getProperty("java.class.path");
    }
    public static String getJavaLibraryPath() {
        return props.getProperty("java.library.path");
    }

}