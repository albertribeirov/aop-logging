package br.com.performance.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Monitor annotation defines if a method should log its execution time and arguments.
 * The logging output is in JSON format and is sent to the console.
 * @author Albert Ribeiro
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Monitor {


    /**
     * Log the parameters of the method. Default is <b>false</b>.
     */
    boolean logParameters() default false;
}



