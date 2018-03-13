package com.github.youyinnn.youwebutils.third;

import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author youyinnn
 */
public class Log4j2Helper {

    private static ArrayList<String>        addedConfigFiles        = new ArrayList<>();
    private static HashMap<String, String>  appendersPool           = new HashMap<>();
    private static HashMap<String, String>  loggersPool             = new HashMap<>();
    private static HashMap<String, String>  propertiesPool          = new HashMap<>();
    private static String                   currentConfigStr;
    private static String                   configXMLFrameworkFile;

    public static void enabledConfig(InputStream in) throws IOException {
        ConfigurationSource source = new ConfigurationSource(in);
        Configurator.initialize(null, source);
    }

    private static void enabledConfig(String xmlString) throws IOException {
        ConfigurationSource source = new ConfigurationSource(new ByteArrayInputStream(xmlString.getBytes("utf-8")));
        Configurator.initialize(null, source);
    }

    public static void useConfig(String configFilePath) throws DocumentException, IOException {
        String defaultConfigPath = "log4j2.xml";
        InputStream defaultConfig = ClassLoader.getSystemClassLoader().getResourceAsStream(defaultConfigPath);
        addConfigInPool(configFilePath);
        if (defaultConfig == null) {
            // 没有主配置的时候 我们以configFilePath的文件为"主配置" 即Configuration参数以最新传入的为主
            Document configDoc = Dom4jHelper.readDoc(configFilePath);
            if (currentConfigStr == null || currentConfigStr.length() == 0) {
                currentConfigStr = configDoc.asXML();
            } else {
                StringBuffer configStr = new StringBuffer(configDoc.asXML());
                for (Map.Entry<String, String> entry : appendersPool.entrySet()) {
                    if (!entry.getKey().equals(configFilePath)) {
                        int appendersEndIndex = getAppendersEndIndex(configStr);
                        if (appendersEndIndex > 0) {
                            configStr.insert(appendersEndIndex, entry.getValue());
                        }
                    }
                }
                for (Map.Entry<String, String> entry : loggersPool.entrySet()) {
                    if (!entry.getKey().equals(configFilePath)) {
                        int loggersEndIndex = getLoggersEndIndex(configStr);
                        if (loggersEndIndex > 0) {
                            configStr.insert(loggersEndIndex, entry.getValue());
                        }
                    }
                }
                for (Map.Entry<String, String> entry : propertiesPool.entrySet()) {
                    if (!entry.getKey().equals(configFilePath)) {
                        int propertiesEndIndex = getPropertiesEndIndex(configStr);
                        if (propertiesEndIndex > 0) {
                            configStr.insert(propertiesEndIndex, entry.getValue());
                        }
                    }
                }
                currentConfigStr = String.valueOf(configStr);
            }
            configXMLFrameworkFile = configFilePath;
        } else {
            if (configXMLFrameworkFile == null) {
                configXMLFrameworkFile = defaultConfigPath;
            }
            // 有主配置的时候 我们只需要把次要配置加载到配置池里 然后把配置池里的配置都合并到主配置中
            Document main = Dom4jHelper.readDoc(defaultConfig);
            StringBuffer mainDoc = new StringBuffer(main.asXML());
            if (!appendersPool.isEmpty()) {
                Collection<String> appenders = appendersPool.values();
                for (String appender : appenders) {
                    mainDoc.insert(getAppendersEndIndex(mainDoc), appender);
                }
            }
            if (!loggersPool.isEmpty()) {
                Collection<String> loggers = loggersPool.values();
                for (String logger : loggers) {
                    mainDoc.insert(getLoggersEndIndex(mainDoc), logger);
                }
            }
            if (!propertiesPool.isEmpty()) {
                String mainProperties = getChildren(main, "Properties");
                if (mainProperties == null) {
                    int i = mainDoc.indexOf("<Appenders>");
                    if (i < 0) {
                        i = mainDoc.indexOf("<appenders>");
                    }
                    mainDoc.insert(i,"<Properties></Properties>\r\n\t");
                }
                Collection<String> properties = propertiesPool.values();
                for (String property : properties) {
                    mainDoc.insert(getPropertiesEndIndex(mainDoc), property);
                }
            }
            currentConfigStr = String.valueOf(mainDoc);
        }
        enabledConfig(currentConfigStr);
    }

    private static int getAppendersEndIndex(StringBuffer configStr) {
        int i = configStr.indexOf("</Appenders>");
        if (i < 0) {
            i = configStr.indexOf("</appenders>");
        }
        return i;
    }

    private static int getLoggersEndIndex(StringBuffer configStr) {
        int i = configStr.indexOf("</Loggers>");
        if (i < 0) {
            i = configStr.indexOf("</Loggers>");
        }
        return i;
    }

    private static int getPropertiesEndIndex(StringBuffer configStr) {
        int i = configStr.indexOf("</Properties>");
        if (i < 0) {
            i = configStr.indexOf("</properties>");
        }
        return i;
    }

    private static String getChildren(Document doc, String parent) {
        Node parentNode = doc.selectSingleNode("//" + parent);
        if (parentNode == null) {
            parentNode = doc.selectSingleNode("//" + parent.toLowerCase());
        }
        if (parentNode != null) {
            String pAsXML = parentNode.asXML();
            return pAsXML.substring(pAsXML.indexOf(">") + 1, pAsXML.lastIndexOf("<"));
        } else {
            return null;
        }
    }

    private static String getAppendersAsString(Document doc) throws DocumentException {
        return getChildren(doc, "Appenders");
    }

    private static String getLoggersAsString(Document doc) throws DocumentException {
        return getChildren(doc, "Loggers");
    }

    private static String getPropertiesAsString(Document doc) throws DocumentException {
        return getChildren(doc, "Properties");
    }

    private static void addConfigInPool(String configFilePath) throws DocumentException {
        Document doc = Dom4jHelper.readDoc(configFilePath);
        String appendersAsString = getAppendersAsString(doc);
        String loggersAsString = getLoggersAsString(doc);
        String propertiesAsString = getPropertiesAsString(doc);
        if (appendersAsString != null) {
            appendersPool.put(configFilePath, appendersAsString);
        }
        if (loggersAsString != null) {
            loggersPool.put(configFilePath, loggersAsString);
        }
        if (propertiesAsString != null) {
            propertiesPool.put(configFilePath, propertiesAsString);
        }
        addedConfigFiles.add(configFilePath);
    }

    private static void removeConfigFromPool(String configFilePath) {
        appendersPool.remove(configFilePath);
        loggersPool.remove(configFilePath);
        propertiesPool.remove(configFilePath);
    }

    public static ArrayList<String> getFileList() {
        return addedConfigFiles;
    }

    public static String getCurrentConfigStr() {
        return currentConfigStr;
    }

    public static String getConfigStatus() {
        return "Log4j2 Configuration status: \r\n"
                + "\t Main config File: " + configXMLFrameworkFile + ";\r\n"
                + "\t Minor config Files: " + addedConfigFiles;
    }
}
