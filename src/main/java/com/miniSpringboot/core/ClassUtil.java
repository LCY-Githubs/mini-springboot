package com.miniSpringboot.core;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassUtil {

    /**
     * 从包名获取包下所有的类
     *
     */
    public static List<Class<?>> getClasses(String packageName){
        //new 一个class类的集合
        ArrayList<Class<?>> classes = new ArrayList<>();
        //是否循环迭代
        boolean recursive = true;
        //获取包的名字，并进行替换
        String packageDirName  = packageName.replace('/', '.');
        //定义一个枚举的集合，并进行循环来处理这个目录下的所有
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            //循环迭代
            while (dirs.hasMoreElements()){
                //获取下一个元素
                URL url = dirs.nextElement();
                //得到协议的名称
                String protocol = url.getProtocol();
                //如果是以文件的形式存储在服务器上
                if ("file".equals(protocol)){
                    //获取包的物理路径
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    // 以文件的方式扫描整个包下的文件 并添加到集合中
                    findAndGetClassesInPackageByFile(packageName,filePath,recursive);
                }else if ("jar".equals(protocol)){
                    //如果是jar包文件
                    //定义一个jarFile
                    JarFile jar;
                    try {
                        jar = ((JarURLConnection) url.openConnection()).getJarFile();
                        //从此jar包，得到一个枚举类
                        Enumeration<JarEntry> entries = jar.entries();
                        //同样的进行循环迭代
                        while (entries.hasMoreElements()){
                            //获取jar里的一个实体 可以是目录 和一些jar包里的其他文件
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * 获取某个接口下所有实现这个接口的类
     */
    public List<Class<?>> getAllClassByInterface(Class c){
        return null;
    }

    /**
     * 获取某一个类所在包的所有的类名，不含迭代
     * @param classLocation
     */
    public static String[] getPackageAllClassName(String classLocation,String packageName){
        return null;
    }

    //以文件的形式获取包下所有的class
    public static  void findAndGetClassesInPackageByFile(String packageName,String packagePath,final boolean recursive){

    }

}
