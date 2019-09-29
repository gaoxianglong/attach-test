package com.gxl.core;

import com.gxl.api.AdviceListener;
import com.gxl.core.classloader.ModuleClassLoader;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * ModuleClassLoader的启动器
 *
 * @author gxl
 */
public class ModuleLoader {
    private static Instrumentation inst;

    public static void init(Instrumentation inst) {
        ModuleLoader.inst = inst;
        try {
            //加载Module.jar相关的类
            loadModule();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 调用ModuleClassLoader加载Module的相关类
     *
     * @throws Exception
     */
    public static void loadModule() throws Exception {
        final File file = new File(Utils.MODULE_PATH);
        final Enumeration<?> files = new JarFile(file).entries();
        final ModuleClassLoader moduleClassLoader = new ModuleClassLoader(new URL[]{file.toURI().toURL()}, ModuleClassLoader.class.getClassLoader(),
                new String[]{"^java\\..*", "^sun\\..*", "^com\\.gxl\\.api..*"});
        System.out.println(String.format("%s准备开始加载%s中的相关类库", moduleClassLoader.getClass().getSimpleName(), file.getName()));
        AdviceListener adviceListener = null;
        while (files.hasMoreElements()) {
            JarEntry jarEntry = (JarEntry) files.nextElement();
            if (jarEntry.toString().endsWith(".class")) {
                String path = jarEntry.getName().replaceAll("/", "\\.");
                Class<?> clazz = moduleClassLoader.loadClass(path.substring(0, path.length() - 6));
                if (clazz.getSuperclass() == AdviceListener.class) {
                    adviceListener = (AdviceListener) clazz.newInstance();
                }
            }
        }
        if (null == adviceListener) {
            return;
        }
        //模块类加载完成后，绑定AdviceListener实现
        EventListenerHandlers.getEventListenerHandlers().setAdviceListener(adviceListener);
        //执行增强
        inst.addTransformer(new AttachClassFileTransformer(
                adviceListener.getTargetClass(), adviceListener.getTargetMethod()), true);
        inst.retransformClasses(Class.forName(adviceListener.getTargetClass()));
    }
}
