package com.miniSpringboot.core;


import com.sun.tools.javac.util.StringUtils;

import javax.xml.stream.events.Characters;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ClassPathAnnotationApplicationContext {

    //扫描包的路径
    private String packagePath;
    //自定义的Spring容器
    private ConcurrentHashMap<String, Object> beanMap = null;

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
            System.out.println("class is " + s + " and value = " + beanMap.get(s));
            //依赖注入
            attributeAssign(beanMap.get(s));
        }
    }

    //初始化对象
    public void initBeans() {
        //扫描包
        List<Class<?>> classList = ClassUtil.getClasses(packagePath);
        //检查这些类是否标注了注解
        ConcurrentHashMap<String, Object> existAnnotation = findClassExistAnnotation(classList);
        if (existAnnotation == null || existAnnotation.isEmpty())
            throw new RuntimeException("该包下没有这个注解标注的类");
    }

    public Object getBean(String beanName) {
        return null;
    }

    //判断是否有注解
    public ConcurrentHashMap<String, Object> findClassExistAnnotation(List<Class<?>> classList) {
        for (Class<?> aClass : classList) {
            Service annotation = aClass.getAnnotation(Service.class);
            if (annotation != null) {
                //将类名小写放进集合中
                String beanName = annotation.value();
                if (Objects.isNull(beanName)) {
                    beanName = aClass.getSimpleName();
                    String s = toLowerCaseFirstOne(beanName);
                }
                Object o = createObject(aClass);
                beanMap.put(beanName, o);
            }
        }
        return beanMap;
    }

    //首字母转小写
    public static String toLowerCaseFirstOne(String s) {
        if(Character.isLowerCase(s.charAt(0))){
            return s;
        }else{
            return new StringBuilder().append(Character.toLowerCase(s.charAt(0))).append(s.substring(s.charAt(1))).toString();
        }
    }

    //通过反射生成对象
    public Object createObject(Class<?> cla) {
        Object o;
        try {
           o = cla.getDeclaredConstructor().newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return o;
    }

    //依赖注入实现原理
    public void attributeAssign(Object o) throws Exception {
        //1、使用反射机制获取当前类的所有的属性
        Field[] fields = o.getClass().getDeclaredFields();
        //2.判断当前的字段是否存在注解
        for (Field field : fields) {
            AutoWired annotation = field.getAnnotation(AutoWired.class);
            if (annotation!=null){
                //获取属性名称
                String name = field.getName();
                //根据bean查找对象
                Object bean = getBean(name);
                //给属性赋值
                if (bean!=null){
                    //设置属性可写
                    field.setAccessible(true);
                    field.set(o, bean);
                }
            }
        }
    }
}
