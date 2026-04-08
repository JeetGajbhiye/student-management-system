package com.sms.studentmanagement.annotation;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * AOP aspect that logs method entry/exit and execution time
 * for all service-layer classes.
 */
@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(* com.sms.studentmanagement.serviceimpl..*(..))")
    public void serviceMethods() {}

    @Pointcut("execution(* com.sms.studentmanagement.controller..*(..))")
    public void controllerMethods() {}

    @Around("serviceMethods()")
    public Object logServiceExecution(ProceedingJoinPoint jp) throws Throwable {
        return logExecution(jp, "SERVICE");
    }

    @Around("controllerMethods()")
    public Object logControllerExecution(ProceedingJoinPoint jp) throws Throwable {
        return logExecution(jp, "CONTROLLER");
    }

    private Object logExecution(ProceedingJoinPoint jp, String layer) throws Throwable {
        String method = jp.getSignature().toShortString();
        log.debug("[{}] >> {}", layer, method);
        StopWatch watch = new StopWatch();
        watch.start();
        try {
            Object result = jp.proceed();
            watch.stop();
            log.debug("[{}] << {} completed in {} ms", layer, method, watch.getTotalTimeMillis());
            return result;
        } catch (Exception ex) {
            watch.stop();
            log.error("[{}] !! {} threw {} after {} ms: {}",
                    layer, method, ex.getClass().getSimpleName(),
                    watch.getTotalTimeMillis(), ex.getMessage());
            throw ex;
        }
    }
}
