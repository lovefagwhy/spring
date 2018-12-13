package com.code.servlet;

import com.code.anno.CodeAutowird;
import com.code.anno.CodeController;
import com.code.anno.CodeRequestMapping;
import com.code.anno.CodeService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * @author liuzhuang
 * @date 2018/12/13 9:43
 */
public class DispatcherServlet extends HttpServlet {
    private List<String> className = new ArrayList<>();
    private Map<String,Object> beanMap = new HashMap<>();
    private Map<String,Object> pathMap = new HashMap<>();
    public DispatcherServlet(){
        super();
    }
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        //扫描包
        doScanPackage("com.code");
        //注解类创建Bean
        doCreateBean();
        //自动注入
        doAutoWird();
        //映射requestMapping
        doMapping();
    }

    private void doMapping() {
        for(Map.Entry<String, Object> entrySet : beanMap.entrySet()){
            Object bean = entrySet.getValue();
            Class<?> aClass = bean.getClass();
            if(aClass.isAnnotationPresent(CodeController.class)){
                CodeRequestMapping requestMapping = aClass.getAnnotation(CodeRequestMapping.class);
                String mappingValue = requestMapping.value();
                Method[] declaredMethods = aClass.getMethods();
                for (Method declaredMethod : declaredMethods) {
                    if(declaredMethod.isAnnotationPresent(CodeRequestMapping.class)){
                        CodeRequestMapping codeRequestMapping = declaredMethod.getAnnotation(CodeRequestMapping.class);
                        String name = codeRequestMapping.value();
                        mappingValue +=name;
                        try {
                            pathMap.put(mappingValue,declaredMethod);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                }
            }
        }
    }

    private void doAutoWird() {
        for(Map.Entry<String, Object> entrySet : beanMap.entrySet()){
            Object bean = entrySet.getValue();
            Class<?> aClass = bean.getClass();
            if(aClass.isAnnotationPresent(CodeController.class)){
                Field[] declaredFields = aClass.getDeclaredFields();
                for (Field declaredField : declaredFields) {
                    if(declaredField.isAnnotationPresent(CodeAutowird.class)){
                        CodeAutowird codeAutowird = declaredField.getAnnotation(CodeAutowird.class);
                        String name = codeAutowird.value();
                        Object o = beanMap.get(name);
                        String clasname = declaredField.getType().getName();
                        declaredField.setAccessible(true);
                        if(null !=o){
                            try {
                                declaredField.set(bean,o);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }

                        }else if(null != beanMap.get(clasname)){
                            Object o1 = beanMap.get(clasname);
                            try {
                                declaredField.set(bean,o1);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }
            }

        }
    }

    private void doCreateBean() {
        try {
            for (String clazzName : className) {
                clazzName = clazzName.replaceAll(".class","");
                Class<?> aClass = Class.forName(clazzName);
                if(aClass.isAnnotationPresent(CodeController.class)){
                    CodeController annotation = aClass.getAnnotation(CodeController.class);
                    String clasName = annotation.value();
                    Object o = aClass.newInstance();
                    if("".equals(clasName)){
                        clasName = o.getClass().getName();
                    }
                    beanMap.put(clasName,o);
                }else if(aClass.isAnnotationPresent(CodeService.class)){
                    CodeService annotation = aClass.getAnnotation(CodeService.class);
                    String clasName = annotation.value();
                    Object o = aClass.newInstance();
                    if(!"".equals(clasName)){
                        beanMap.put(clasName,o);

                    }
                    String clasName1 = o.getClass().getName();
                    beanMap.put(clasName1,o);
                }
            }

        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch (InstantiationException e){
            e.printStackTrace();
        }catch (IllegalAccessException e){
            e.printStackTrace();
        }
    }

    private void doScanPackage(String packPath) {
        //获取包的具体物理地址
        URL url = this.getClass().getClassLoader().getResource(
                "/"+packPath.replaceAll("[.]","/"));
        String file = url.getFile();
        File packFile = new File(file);
        if(packFile.exists()){
            File[] files = packFile.listFiles();
            for (File pFile : files) {
                if(pFile.isDirectory()){
                    doScanPackage(packPath+"."+pFile.getName());
                }else{
                    className.add(packPath+"."+pFile.getName());
                }

            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestURI = req.getRequestURI();

        Method method = (Method)pathMap.get(requestURI);
        String name = req.getParameter("name");
        Integer age = Integer.parseInt(req.getParameter("age"));
        try {
            PrintWriter writer = resp.getWriter();
            //String className = requestURI.split("//")[1];
            Class<?> declaringClass = method.getDeclaringClass();
            Object obj = beanMap.get(declaringClass.getName());
            Object invoke = method.invoke(obj, name, age);
            writer.print(invoke);
            writer.close();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
