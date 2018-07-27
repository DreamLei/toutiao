package com.example.toutiao.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
//面向切面

/**
 * @author tell
 */
@Aspect
@Component
public class LogAspect {
    private  static  final Logger LOG=LoggerFactory.getLogger(LogAspect.class);

    @Before("execution(* com.example.toutiao.controller.*Controller.*(..))")
    public  void beforeMethod(JoinPoint joinPoint){
        StringBuffer sb=new StringBuffer();
        for(Object arg:joinPoint.getArgs()){
            sb.append("arg:"+arg.toString()+"|");
        }
        LOG.info("before method:"+sb.toString());
    }


    @After("execution(* com.example.toutiao.controller.*Controller.*(..))")
    public  void afterMethod(JoinPoint joinPoint){
        LOG.info("after  method:");
    }
}