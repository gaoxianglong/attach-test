package java.com.gxl.spy;

import java.lang.reflect.Method;

/**
 * 在Bootstrap中进行注册，后续需要在各个ClassLoader中使用的间谍类
 *
 * @author gxl
 */
public class Spy {
    private static Method beforeMethod;

    public static Method getbeforeMethod() {
        return beforeMethod;
    }

    public static void before() {
        if (null == beforeMethod) {
            return;
        }
        try {
            //触发增强逻辑
            beforeMethod.invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 方法钩子
     */
    public static class MethodHook {
        public MethodHook(Method beforeMethod) {
            Spy.beforeMethod = beforeMethod;
        }
    }
}
