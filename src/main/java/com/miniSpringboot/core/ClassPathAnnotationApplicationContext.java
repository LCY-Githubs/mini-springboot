package com.miniSpringboot.core;


import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ClassPathAnnotationApplicationContext {

    //扫描包的路径
    private String packagePath;
    //自定义的Spring容器
    private ConcurrentHashMap<String ,Object> beanMap = null;

    //初始化
    public ClassPathAnnotationApplicationContext(String packagePath) throws Exception {
        this.packagePath = packagePath;
        beanMap = new ConcurrentHashMap<String, Object>();
        //初始化bean Service
        initBeans();
        //给容器里的类都注入属性
        initAttributes();
    }

    //初始化属性以及autoWired注入
    private void initAttributes() throws Exception {
        for (String s : beanMap.keySet()) {
            System.out.println("class is "+s+" and value = "+ beanMap.get(s));
            //依赖注入
            attributeAssign(beanMap.get(s));
        }
    }

    //初始化对象
    public void initBeans(){

    }

    public Object getBean(String beanName){
        return null;
    }

    //判断是否有注解
    public ConcurrentHashMap<String,Object> findClassExistAnnotation(List<Class<?>> classList){
        return null;
    }

    //首字母转小写
    public static String toLowerCaseFirstOne(){
        return null;
    }

    //通过反射生成对象
    public Object createObject(){
        return null;
    }

    //依赖注入实现原理
    public void attributeAssign(Object o)throws Exception{}
}
