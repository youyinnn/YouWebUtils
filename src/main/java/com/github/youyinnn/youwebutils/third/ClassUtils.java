package com.github.youyinnn.youwebutils.third;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 提供包与类扫描的功能.
 *
 * @author youyinnn
 */
public class ClassUtils {

    public static Set<Class<?>> findFileClass(String packName){
        Set<Class<?>> classes = new LinkedHashSet<>();
        String packageDirName =packName.replace('.', '/');
        Enumeration<URL> dirs;
        try {
            dirs=Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            while(dirs.hasMoreElements()){
                URL url = dirs.nextElement();
                String protocol = url.getProtocol();
                //扫描file包中的类
                if("file".equals(protocol)){
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    getFileClass(packName,filePath,classes);
                    //扫描jar包中的类
                }else if("jar".equals(protocol)){
                    JarFile jarFile = ((JarURLConnection) url.openConnection()).getJarFile();
                    getJarClass(jarFile,packageDirName,classes);
                }
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
        return classes;
    }

    private static void getFileClass(String packName, String filePath, Set<Class<?>> clazzs){
        File dir = new File(filePath);
        if (!dir.exists() || !dir.isDirectory()) {
            System.out.println("包目录不存在!");
            return;
        }
        File[] dirFiles = dir.listFiles(new MyFileFilter());

        for (File file : dirFiles) {
            if(file.isDirectory()){
                getFileClass(packName + "." + file.getName(), file.getAbsolutePath(),clazzs);
            }else{
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    Class<?> clazz=Thread.currentThread().getContextClassLoader().loadClass(packName + "." + className);
                    clazzs.add(clazz);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void getJarClass(JarFile jarFile, String filePath, Set<Class<?>> classes) throws IOException {
        List<JarEntry> jarEntryList = new ArrayList<>();
        Enumeration<JarEntry> enums = jarFile.entries();
        while (enums.hasMoreElements()) {
            JarEntry entry = enums.nextElement();
            // 过滤出满足我们需求的东西
            if (entry.getName().startsWith(filePath) && entry.getName().endsWith(".class")) {
                jarEntryList.add(entry);
            }
        }
        for (JarEntry entry : jarEntryList) {
            String className = entry.getName().replace('/', '.');
            className = className.substring(0, className.length() - 6);
            try {
                classes.add(Thread.currentThread().getContextClassLoader().loadClass(className));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}

class MyFileFilter implements FileFilter{

    @Override
    public boolean accept(File pathname) {
        // 接受dir目录
        boolean acceptDir = pathname.isDirectory();
        // 接受class文件
        boolean acceptClass = pathname.getName().endsWith(".class");
        return acceptDir || acceptClass;
    }
}
