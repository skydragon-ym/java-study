package com.skydragon.study.proxy;

import java.lang.reflect.Proxy;

/*
基于JDK动态代理，需要有接口
代理对象是在程序运行时产生的，而不是编译期
对代理对象的所有接口方法调用都会转发到InvocationHandler.invoke()方法，在invoke()方法里我们可以加入任何逻辑，比如修改方法参数，加入日志功能、安全检查功能等
之后我们通过某种方式执行真正的方法体，示例中通过反射调用了RoutineTask对象的相应方法，还可以通过RPC调用远程方法
 */
public class DynamicProxy {
    static final Task task = new RoutineTask();

    public static void main(String [] args){
        /*
        Proxy.newProxyInstance(RoutineTask.class.getClassLoader(), RoutineTask.class.getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return null;
            }
        });
         */

        Task taskProxy = (Task)Proxy.newProxyInstance(RoutineTask.class.getClassLoader(), RoutineTask.class.getInterfaces(),
                (proxy,method,arguments)->{
                    System.out.println("enter dynamic proxy");
                    Object retVal = method.invoke(task,arguments);
                    System.out.println("exit dynamic proxy");
                    return retVal; });

        //call method via dynamic proxy
        taskProxy.doWork();
    }
}
