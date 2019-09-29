package com.gxl.agent;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Agent启动器,由CoreLuancher启动并Attach目标进程后触发
 *
 * @author gxl
 */
public class AgentLauncher {

    /**
     * Attache成功后触发
     *
     * @param agentArgs
     * @param inst
     */
    public static void agentmain(String agentArgs, Instrumentation inst) {
        init(inst);
    }

    /**
     * 相关初始化工作
     *
     * @param inst
     */
    static void init(final Instrumentation inst) {
        try {
            //将Spy注册到Bootstrap中
            inst.appendToBootstrapClassLoaderSearch(new JarFile(new File(Utils.SPY_PATH)));
            System.out.println("成功将Spy注册到Bootstrap中");
            //由AttachClassLoader加载核心模块类库
            ClassLoader classLoader = loadCore(inst);
            loadModule(classLoader, inst);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载attach-test-core.jar中的类
     *
     * @param instrumentation
     * @throws Exception
     */
    static ClassLoader loadCore(Instrumentation instrumentation) throws Exception {
        final File file = new File(Utils.CORE_PATH);
        final URL url = file.toURI().toURL();
        final Enumeration<?> files = new JarFile(file).entries();
        final AttachClassLoader classLoader = new AttachClassLoader(new URL[]{url});
        System.out.println(String.format("%s准备开始加载%s中的相关类库", classLoader.getClass().getSimpleName(), file.getName()));
        while (files.hasMoreElements()) {
            JarEntry jarEntry = (JarEntry) files.nextElement();
            //只加载jar包中的.class文件
            if (jarEntry.toString().endsWith(".class")) {
                String path = jarEntry.getName().replaceAll("/", "\\.");
                classLoader.loadClass(path.substring(0, path.length() - 6));
            }
        }
        return classLoader;
    }

    /**
     * 调用触发ModuleClassLoader的相关加载
     *
     * @param classLoader
     * @param inst
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    static void loadModule(ClassLoader classLoader, Instrumentation inst)
            throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> spyUtilsClazz = classLoader.loadClass("com.gxl.core.SpyUtils");
        //调用SpyUtils.register()方法初始化将EventListenerHandlers注册到Spy的钩子上
        spyUtilsClazz.getDeclaredMethod("register").invoke(null);

        Class<?> moduleLoaderClazz = classLoader.loadClass("com.gxl.core.ModuleLoader");
        //ModuleLoader.init()方法启动ModuleClassLoader加载Module相关类库
        moduleLoaderClazz.getDeclaredMethod("init", Instrumentation.class).invoke(null, inst);
    }
}
