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

/**
 * @author youyinnn
 */
public class Log4j2Helper {

    private static ArrayList<String>        fileList        = new ArrayList<>();
    private static HashMap<String, String>  appendersPool   = new HashMap<>();
    private static HashMap<String, String>  loggersPool     = new HashMap<>();
    private static HashMap<String, String>  propertiesPool  = new HashMap<>();
    private static String                   usingXMLString;

    public static void setConfig(InputStream in) throws IOException {
        ConfigurationSource source = new ConfigurationSource(in);
        Configurator.initialize(null, source);
    }

    private static void setConfig(String xmlString) throws IOException {
        ConfigurationSource source = new ConfigurationSource(new ByteArrayInputStream(xmlString.getBytes("utf-8")));
        Configurator.initialize(null, source);
    }

    public static void useConfig(String resourcePath) throws DocumentException, IOException {
        InputStream mainConfigIn = ClassLoader.getSystemClassLoader().getResourceAsStream("log4j2.xml");
        addFileInPool(resourcePath);
        if (mainConfigIn == null) {
            // 没有主配置的时候 我们以resourcePath的文件为"主配置" 即Configuration参数以最新传入的为主
            if (usingXMLString == null || usingXMLString.length() == 0) {
                Document minor = Dom4jHelper.readDoc(resourcePath);
                usingXMLString = minor.asXML();
            } else {
                StringBuffer preUsingDoc = new StringBuffer(usingXMLString);
                if (appendersPool.containsKey(resourcePath)) {
                    String appenders = appendersPool.get(resourcePath);
                    preUsingDoc.insert(getAppendersEndIndex(preUsingDoc), appenders);
                }
                if (loggersPool.containsKey(resourcePath)) {
                    String loggers = loggersPool.get(resourcePath);
                    preUsingDoc.insert(getLoggersEndIndex(preUsingDoc), loggers);
                }
                if (propertiesPool.containsKey(resourcePath)){
                    String properties = propertiesPool.get(resourcePath);
                    int propertiesEndIndex = getPropertiesEndIndex(preUsingDoc);
                    if (propertiesEndIndex < 0) {
                        int i = preUsingDoc.indexOf("<Appenders>");
                        if (i < 0) {
                            i = preUsingDoc.indexOf("<appenders>");
                        }
                        preUsingDoc.insert(i,"<Properties></Properties>\r\n\t");
                    }
                    preUsingDoc.insert(getPropertiesEndIndex(preUsingDoc), properties);
                }
                usingXMLString = String.valueOf(preUsingDoc);
            }
        } else {
            // 有主配置的时候 我们只需要把次要配置加载到配置池里 然后把配置池里的配置都合并到主配置中
            Document main = Dom4jHelper.readDoc(mainConfigIn);
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
            usingXMLString = String.valueOf(mainDoc);
        }
        setConfig(usingXMLString);
    }

    private static int getAppendersEndIndex(StringBuffer doc) {
        int i = doc.indexOf("</Appenders>");
        if (i < 0) {
            i = doc.indexOf("</appenders>");
        }
        return i;
    }

    private static int getLoggersEndIndex(StringBuffer doc) {
        int i = doc.indexOf("</Loggers>");
        if (i < 0) {
            i = doc.indexOf("</Loggers>");
        }
        return i;
    }

    private static int getPropertiesEndIndex(StringBuffer doc) {
        int i = doc.indexOf("</Properties>");
        if (i < 0) {
            i = doc.indexOf("</properties>");
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

    private static void addFileInPool(String resourcesPath) throws DocumentException {
        Document doc = Dom4jHelper.readDoc(resourcesPath);
        String appendersAsString = getAppendersAsString(doc);
        String loggersAsString = getLoggersAsString(doc);
        String propertiesAsString = getPropertiesAsString(doc);
        if (appendersAsString != null) {
            appendersPool.put(resourcesPath, appendersAsString);
        }
        if (loggersAsString != null) {
            loggersPool.put(resourcesPath, loggersAsString);
        }
        if (propertiesAsString != null) {
            propertiesPool.put(resourcesPath, propertiesAsString);
        }
        fileList.add(resourcesPath);
    }

    private static void removeFileFromPool(String resourcePath) {
        appendersPool.remove(resourcePath);
        loggersPool.remove(resourcePath);
        propertiesPool.remove(resourcePath);
    }

    public static ArrayList<String> getFileList() {
        return fileList;
    }

    public static String getUsingXMLString() {
        return usingXMLString;
    }
}
