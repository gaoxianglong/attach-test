package com.gxl.api;

/**
 * AOP增强(通知)监听
 *
 * @author gxl
 */
public class AdviceListener {
    /**
     * 前置通知
     */
    public void before() {
    }

    /**
     * 获取目标类全限定名
     *
     * @return 目标类
     */
    public String getTargetClass() {
        return null;
    }

    /**
     * 获取目标方法
     *
     * @return 方法名
     */
    public String getTargetMethod() {
        return null;
    }
}
