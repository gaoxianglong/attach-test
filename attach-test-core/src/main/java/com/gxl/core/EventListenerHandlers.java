package com.gxl.core;

import com.gxl.api.AdviceListener;

public class EventListenerHandlers {
    private static EventListenerHandlers eventListenerHandlers;
    private AdviceListener adviceListener;

    private EventListenerHandlers() {
    }

    public static EventListenerHandlers getEventListenerHandlers() {
        return eventListenerHandlers;
    }

    static {
        eventListenerHandlers = new EventListenerHandlers();
    }

    public void setAdviceListener(AdviceListener adviceListener) {
        this.adviceListener = adviceListener;
    }

    public void onBefore() {
        if (null != adviceListener) {
            System.out.println(String.format("EventListenerHandlers classLoader:%s", eventListenerHandlers.getClass().getClassLoader()));
            System.out.println(String.format("AdviceListenerImpl classLoader:%s", adviceListener.getClass().getClassLoader()));
            adviceListener.before();
        }
    }

    public static void before() {
        getEventListenerHandlers().onBefore();
    }
}
