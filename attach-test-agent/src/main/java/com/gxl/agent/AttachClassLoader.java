package com.gxl.agent;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * Attach平台类装载器
 *
 * @author gxl
 */
public class AttachClassLoader extends URLClassLoader {
    public AttachClassLoader(URL[] urls) {
        super(urls);
    }

    @Override
    protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        final Class<?> loadedClass = findLoadedClass(name);
        if (loadedClass != null) {
            return loadedClass;
        }
        // 如果是sun或者java打头的包都由Bootstrap负责加载，其它则自行加载
        if (name != null && (name.startsWith("sun.") || name.startsWith("java."))) {
            return super.loadClass(name, resolve);
        }
        try {
            Class<?> aClass = findClass(name);
            if (resolve) {
                resolveClass(aClass);
            }
            return aClass;
        } catch (Exception e) {
            return super.loadClass(name, resolve);
        }
    }
}
