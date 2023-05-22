package br.com.performance.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

@Aspect
@Component
public class Log {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Before("@annotation(br.com.performance.aspect.Monitor)")
    public void logParameters(JoinPoint joinPoint) {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        var object = new LinkedHashMap<>();
        object.put("class", signature.getDeclaringTypeName());
        object.put("method", signature.getMethod().getName());
        object.put("args", joinPoint.getArgs());


        log.info("{}", LogPrinter.logData(object));

    }
}




