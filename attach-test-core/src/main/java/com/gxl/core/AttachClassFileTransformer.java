package com.gxl.core;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * 对目标类的目标方法进行增强
 *
 * @author gxl
 */
public class AttachClassFileTransformer implements ClassFileTransformer {
    private String targetName, targetMethod;

    public AttachClassFileTransformer(String targetName, String targetMethod) {
        this.targetName = targetName;
        this.targetMethod = targetMethod;
    }

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        byte[] bcode = null;
        try {
            CtClass ctClass = ClassPool.getDefault().get(targetName);
            CtMethod ctMethod = ctClass.getDeclaredMethod(targetMethod);
            //在方法执行前插入前置通知
            ctMethod.insertBefore("java.com.gxl.spy.Spy.before();");
            bcode = ctClass.toBytecode();
            System.out.println(String.format("成功对%s.%s()进行AOP增强", targetName, targetMethod));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bcode;
    }
}