package com.gxl.core.classloader;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * ModuleClassLoader类加载器,父加载器为AttachClassLoader
 * <p>
 * 由AttachClassLoader加载attach-test-core和attach-api包下的类，ModuleClassLoader加载Module下的类
 *
 * @author
 */
public class ModuleClassLoader extends URLClassLoader {
    private String[] regexExpressArray;
    private ClassLoader parent;

    public ModuleClassLoader(URL[] urls) {
        super(urls);
    }

    public ModuleClassLoader(URL[] urls, ClassLoader parent, String... regexExpressArray) {
        super(urls, parent);
        this.regexExpressArray = regexExpressArray.clone();
        this.parent = parent;
    }

    @Override
    protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        //优先由父类进行加载
        if (isHit(name)) {
            Class<?> clazz = parent.loadClass(name);
            if (clazz.getClassLoader() == parent) {
                System.out.println(String.format("  -- className:%s触发%s加载", name, parent));
            }
            return clazz;
        }
        final Class<?> loadedClass = findLoadedClass(name);
        if (loadedClass != null) {
            return loadedClass;
        }
        try {
            Class<?> aClass = findClass(name);
            if (resolve) {
                resolveClass(aClass);
            }
            System.out.println(String.format("  -- className:%s触发%s加载", name, aClass.getClassLoader()));
            return aClass;
        } catch (Exception e) {
            return super.loadClass(name, resolve);
        }
    }

    /**
     * 规则校验
     *
     * @param javaClassName
     * @return
     */
    private boolean isHit(final String javaClassName) {
        boolean result = false;
        for (final String regexExpress : regexExpressArray) {
            if (javaClassName.matches(regexExpress)) {
                result = true;
            }
        }
        return result;
    }
}