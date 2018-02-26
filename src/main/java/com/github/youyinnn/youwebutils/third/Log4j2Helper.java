package com.github.youyinnn.youwebutils.third;

import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author youyinnn
 */
public class Log4j2Helper {

    public static void setConfig(InputStream in) throws IOException {
        ConfigurationSource source = new ConfigurationSource(in);
        Configurator.initialize(null, source);
    }

    private static void setConfig(String xmlString) throws IOException {
        ConfigurationSource source = new ConfigurationSource(new ByteArrayInputStream(xmlString.getBytes("utf-8")));
        Configurator.initialize(null, source);
    }

    public static void setConfigWithTwoXML(String mainXMLPath, String minorXMLPath) throws DocumentException, IOException {
        String configXML = mergeConfigXML(mainXMLPath, minorXMLPath);
        setConfig(configXML);
    }

    public static void setConfigWithDefaultXML(String minorXMLPath) throws DocumentException, IOException {
        String configXML = mergeConfigWithDefaultXML(minorXMLPath);
        setConfig(configXML);
    }

    public static String mergeConfigXML(Document main, Document minor) {
        StringBuffer mainDoc = new StringBuffer(main.asXML());
        String appenders = getChildren(minor, "Appenders");
        String loggers = getChildren(minor, "Loggers");
        String properties = getChildren(minor, "properties");

        if (appenders != null) {
            mainDoc.insert(mainDoc.indexOf("</Appenders>"), appenders);
        }
        if (loggers != null) {
            mainDoc.insert(mainDoc.indexOf("</Loggers>"), loggers);
        }
        if (properties != null) {
            mainDoc.insert(mainDoc.indexOf("</Properties>"), properties);
        }

        return String.valueOf(mainDoc);
    }

    public static String mergeConfigWithDefaultXML(Document minor) throws DocumentException {
        InputStream mainConfigIn = ClassLoader.getSystemClassLoader().getResourceAsStream("com/github/youyinnn/lab/log4j2.xml");
        Document main = Dom4jHelper.readDoc(mainConfigIn);
        return mergeConfigXML(main, minor);
    }

    public static String mergeConfigWithDefaultXML(String minorXMLPath) throws DocumentException {
        return mergeConfigWithDefaultXML(Dom4jHelper.readDoc(minorXMLPath));
    }

    public static String mergeConfigXML(String mainXMLPath, String minorXMLPath) throws DocumentException {
        return mergeConfigXML(Dom4jHelper.readDoc(mainXMLPath), Dom4jHelper.readDoc(minorXMLPath));
    }

    private static String getChildren(Document doc, String parent) {
        Node parentNode = doc.selectSingleNode("//" + parent);
        if (parentNode != null) {
            String pAsXML = parentNode.asXML();
            return pAsXML.substring(pAsXML.indexOf(">") + 1, pAsXML.lastIndexOf("<"));
        } else {
            return null;
        }
    }
}
