package br.com.performance.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
public class Log {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Before("@annotation(br.com.performance.aspect.Monitor)")
    public void logParameters(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        JSONObject json = new JSONObject();
        json.put("class", signature.getDeclaringTypeName());
        json.put("method", signature.getMethod().getName());
        json.put("args", new JSONArray(method.getParameterTypes()));

        if (method.getAnnotation(Monitor.class).logParameters() && joinPoint.getArgs().length > 0) {
            Arrays.stream(method.getParameters()).iterator().forEachRemaining(parameter ->
                    json.put(
                            parameter.getType().getSimpleName(),
                            new JSONObject(joinPoint.getArgs()[Arrays.asList(method.getParameters()).indexOf(parameter)])
                    ));
        }
        log.info("{}", json);
    }

}
