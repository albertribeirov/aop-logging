package br.com.performance.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

@Aspect
@Component
public class Log {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Before("@annotation(br.com.performance.aspect.Monitor)")
    public void logParameters(JoinPoint joinPoint) {
        List parameters = new ArrayList();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        var object = new LinkedHashMap<>();
        object.put("class", signature.getDeclaringTypeName());
        object.put("method", signature.getMethod().getName());
        object.put("args", new ArrayList<>());

        if (method.getParameterCount() > 0) {
            Arrays.stream(method.getParameters()).iterator().forEachRemaining(parameter -> {
                var parameterObject = new LinkedHashMap<>();
                parameterObject.put("type", parameter.getType());
                parameterObject.put("value", joinPoint.getArgs()[Arrays.asList(method.getParameters()).indexOf(parameter)]);
                ((List) object.get("args")).add(parameterObject);
            });
        }

        if (method.getAnnotation(Monitor.class).logParameters()) {
            log.info("{}", LogPrinter.logData(object));
        }


    }
}




