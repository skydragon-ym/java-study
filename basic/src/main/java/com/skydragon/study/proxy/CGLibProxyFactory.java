package com.skydragon.study.proxy;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CGLibProxyFactory implements MethodInterceptor {

    private Object target;

    public CGLibProxyFactory(){
        super();
    }

    public CGLibProxyFactory(Object targetObj){
        super();
        this.target = targetObj;
    }

    public Object createProxy(){
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        method.invoke(o,objects);
        return null;
    }

    public static void main(String[] args){
        Task task = (Task)new CGLibProxyFactory(new RoutineTask()).createProxy();
        task.doWork();
    }
}
