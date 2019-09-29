package com.gxl;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;

import java.io.IOException;

public class Test {
    public static void main(String[] args) {
        System.out.println("com.yunji.asdasd".matches("^com\\.yunji\\..*"));
        System.out.println(Object.class == Object.class);

        try {
            Class<?> c1 = new ClassLoader() {
                @Override
                protected Class<?> findClass(String name) throws ClassNotFoundException {
                    ClassPool cp = new ClassPool(true);
                    CtClass clazz = cp.makeClass(name);
                    byte[] bcode = null;
                    try {
                        bcode = clazz.toBytecode();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return this.defineClass(name, bcode, 0, bcode.length);
                }
            }.loadClass("com.gxl.Test");
            Class<?> c2 = new ClassLoader() {
                @Override
                protected Class<?> findClass(String name) throws ClassNotFoundException {
                    ClassPool cp = new ClassPool(true);
                    CtClass clazz = cp.makeClass(name);
                    byte[] bcode = null;
                    try {
                        bcode = clazz.toBytecode();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return this.defineClass(name, bcode, 0, bcode.length);
                }
            }.findClass("com.yunji.Test");
            System.out.println(c1 == Test.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
