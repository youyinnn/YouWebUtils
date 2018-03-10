package com.github.youyinnn.youwebutils.third;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;

/**
 * @author youyinnn
 */
public class Dom4jHelper {

    private static SAXReader saxReader = new SAXReader();

    public static Document readDoc(String systemId) throws DocumentException {
        return saxReader.read(ClassLoader.getSystemClassLoader().getResourceAsStream(systemId));
    }

    public static Document readDoc(File file) throws DocumentException {
        return saxReader.read(file);
    }

    public static Document readDoc(URL url) throws DocumentException {
        return saxReader.read(url);
    }

    public static Document readDoc(InputStream in) throws DocumentException {
        return saxReader.read(in);
    }

    public static Document readDoc(Reader reader) throws DocumentException {
        return saxReader.read(reader);
    }

    public static Document readDoc(InputStream in, String systemId) throws DocumentException {
        return saxReader.read(in, systemId);
    }

    public static Document readDoc(Reader reader, String systemId) throws DocumentException {
        return saxReader.read(reader, systemId);
    }

}
