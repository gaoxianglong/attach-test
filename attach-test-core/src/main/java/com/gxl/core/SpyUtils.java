package com.gxl.core;

import java.com.gxl.spy.Spy;

/**
 * Spy工具类
 *
 * @author
 */
public class SpyUtils {
    public static void register() {
        try {
            //待Spy成功加载到Bootstrap后,将EventListenerHandlers.before注册到钩子上
            Spy.MethodHook hook = new Spy.MethodHook(EventListenerHandlers.class.getDeclaredMethod("before"));
            System.out.println("成功将EventListenerHandlers注册到Spy的钩子上");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}