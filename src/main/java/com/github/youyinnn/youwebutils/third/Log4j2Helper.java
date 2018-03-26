package com.github.youyinnn.youwebutils.third;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

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
    private static boolean                  isCurrentConfigUsed     = false;

    public static void enabledConfig(InputStream in) throws IOException {
        ConfigurationSource source = new ConfigurationSource(in);
        Configurator.initialize(null, source);
    }

    private static void enabledConfig(String xmlString) throws IOException {
        enabledConfig(new ByteArrayInputStream(xmlString.getBytes("utf-8")));
    }

    public static void useConfig(URL configFileURL) throws DocumentException {
        URL defaultConfigFileRs = Log4j2Helper.class.getResource("/log4j2.xml");
        if (configFileURL == null) {
            throw new DocumentException("URL指定的资源不存在!");
        } else {
            String configFilePath = configFileURL.getPath();
            addConfigInPool(configFileURL, configFilePath);
            if (defaultConfigFileRs == null) {
                // 没有主配置的时候 我们以configFilePath的文件为"主配置" 即Configuration参数以最新传入的为主
                Document configDoc = Dom4jHelper.readDoc(configFileURL);
                if (currentConfigStr == null || currentConfigStr.length() == 0) {
                    currentConfigStr = configDoc.asXML();
                    configXMLFrameworkFile = configFilePath;
                } else {
                    currentConfigStr = String.valueOf(mergeMainAndMinor(currentConfigStr, configFilePath));
                }
            } else {
                Document main = Dom4jHelper.readDoc(defaultConfigFileRs);
                if (configXMLFrameworkFile == null) {
                    configXMLFrameworkFile = defaultConfigFileRs.getPath();
                    currentConfigStr = main.asXML();
                }
                // 有主配置的时候 我们只需要把次要配置加载到配置池里 然后把配置池里的配置都合并到主配置中
                currentConfigStr = String.valueOf(mergeMainAndMinor(currentConfigStr, configFilePath));
            }
        }
    }

    private static String mergeMainAndMinor(String mainXMLStr, String minorXMLFilePath) {
        StringBuffer mainXMLSb = new StringBuffer(mainXMLStr);
        String loggers = loggersPool.get(minorXMLFilePath);
        if (loggers != null) {
            int loggersEndIndex = getLoggersEndIndex(mainXMLSb);
            if (loggersEndIndex < 0) {
                int i = mainXMLSb.indexOf("</Configuration>");
                if (i < 0) {
                    i = mainXMLSb.indexOf("</configuration>");
                }
                mainXMLSb.insert(i, "<Loggers></Loggers>\r\n\t");
                loggersEndIndex = getLoggersEndIndex(mainXMLSb);
            }
            mainXMLSb.insert(loggersEndIndex, loggers);
        }
        String appender = appendersPool.get(minorXMLFilePath);
        if (appender != null) {
            int appendersEndIndex = getAppendersEndIndex(mainXMLSb);
            if (appendersEndIndex < 0) {
                int i = mainXMLSb.indexOf("<Loggers>");
                if (i < 0) {
                    i = mainXMLSb.indexOf("<loggers>");
                }
                mainXMLSb.insert(i, "<Appenders></Appenders>\r\n\t");
                appendersEndIndex = getAppendersEndIndex(mainXMLSb);
            }
            mainXMLSb.insert(appendersEndIndex, appender);
        }
        String properties = propertiesPool.get(minorXMLFilePath);
        if (properties != null) {
            int propertiesEndIndex = getPropertiesEndIndex(mainXMLSb);
            if (propertiesEndIndex < 0) {
                int i = mainXMLSb.indexOf("<Appenders>");
                if (i < 0) {
                    i = mainXMLSb.indexOf("<appenders>");
                }
                mainXMLSb.insert(i, "<Properties></Properties>\r\n\t");
                propertiesEndIndex = getPropertiesEndIndex(mainXMLSb);
            }
            mainXMLSb.insert(propertiesEndIndex, properties);
        }
        return String.valueOf(mainXMLSb);
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

    private static void addConfigInPool(URL fileUrl, String configFilePath) throws DocumentException {
        Document doc = Dom4jHelper.readDoc(fileUrl);
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
        StringBuilder files = new StringBuilder();
        for (String addedConfigFile : addedConfigFiles) {
            files.append("\t\t\t").append(addedConfigFile).append(System.lineSeparator());
        }
        return "Log4j2 Configuration status: " + System.lineSeparator()
                + "\t Main config File: " + configXMLFrameworkFile + ";" + System.lineSeparator()
                + "\t Minor config Files: " + System.lineSeparator() + files.toString();
    }

    private static void isConfigUsed() {
        if (!isCurrentConfigUsed) {
            try {
                enabledConfig(currentConfigStr);
                isCurrentConfigUsed = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Logger getLogger(String name) {
        isConfigUsed();
        return LogManager.getLogger(name);
    }

    public static Logger getLogger(Class<?> clazz) {
        isConfigUsed();
        return LogManager.getLogger(clazz);
    }

    public static Logger getRootLogger() {
        isConfigUsed();
        return LogManager.getRootLogger();
    }
}
