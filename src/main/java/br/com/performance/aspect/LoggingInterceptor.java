package br.com.performance.aspect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.Objects;

public class LoggingInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        StringBuilder logBuilder = new StringBuilder("Request - Method=[{}] URI=[{}]\nRequestBody=[{}]");
        StringBuilder uriBuilder = new StringBuilder(request.getRequestURI());

        var body = getStringValue(((ContentCachingRequestWrapper) request).getContentAsByteArray(), request.getCharacterEncoding());

        addQueryStringIfExists(request, uriBuilder);

        logger.info(logBuilder.toString(), request.getMethod(), uriBuilder, body);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        StringBuilder logBuilder = new StringBuilder("Response - URI=[{}] StatusCode=[{}]");
        StringBuilder uriBuilder = new StringBuilder(request.getRequestURI());
        var body = getStringValue(((ContentCachingResponseWrapper) response).getContentAsByteArray(), response.getCharacterEncoding());

        addQueryStringIfExists(request, logBuilder);

        logger.info(logBuilder.append(" Exception=[{}]\nResponseBody=[{}]").toString(), uriBuilder, response.getStatus(), ex, body);
    }

    private static void addQueryStringIfExists(HttpServletRequest request, StringBuilder uriBuilder) {
        if (Objects.nonNull(request.getQueryString())) {
            uriBuilder.append("?").append(request.getQueryString());
        }
    }

    private String getStringValue(byte[] contentAsByteArray, String characterEncoding) {
        try {
            return new String(contentAsByteArray, characterEncoding);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }
        return "";
    }
}
