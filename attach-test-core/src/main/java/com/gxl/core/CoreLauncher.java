package com.gxl.core;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import java.io.IOException;
import java.util.Scanner;

/**
 * 核心启动器
 *
 * @author
 */
public class CoreLauncher {
    public static void main(String[] agrs) {
        Scanner scan = new Scanner(System.in);
        try {
            new CoreLauncher().attach(scan.nextLine(),
                    Utils.AGENT_PATH);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * attach到目标进程上
     *
     * @param targetJvmPid
     * @param agentJarPath
     * @throws IOException
     * @throws AttachNotSupportedException
     * @throws AgentLoadException
     * @throws AgentInitializationException
     */
    void attach(String targetJvmPid, String agentJarPath) throws IOException, AttachNotSupportedException,
            AgentLoadException, AgentInitializationException {
        VirtualMachine vmObj = null;
        try {
            vmObj = VirtualMachine.attach(targetJvmPid);
            if (vmObj != null) {
                vmObj.loadAgent(agentJarPath);
                System.out.println(String.format("成功attach到目标进程上,pid:%s", targetJvmPid));
            }
        } finally {
            if (null != vmObj) {
                vmObj.detach();
            }
        }
    }
}
