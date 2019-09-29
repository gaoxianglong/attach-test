package com.gxl.module;

import com.gxl.api.AdviceListener;

/**
 * AdviceListener实现
 * <p>
 * 最终调用过程为:appclassloader-->Spy(bootstrapClassLoader)-->EventListenerHandlers(attachClassLoader)-->Module(moduleClassLoader)
 *
 * @author gxl
 */
public class AdviceListenerImpl extends AdviceListener {
    public void before() {
        System.out.println("==================before==================");
    }

    public String getTargetClass() {
        return "com.gxl.service.order.OrderService";
    }

    public String getTargetMethod() {
        return "makeOrder";
    }
}
