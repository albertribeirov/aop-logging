package br.com.performance.aspect;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@AllArgsConstructor
@Slf4j
public class LoggingInterceptor implements HandlerInterceptor {

    private static final String START_TIME = "startTime";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute(START_TIME, System.currentTimeMillis());
        StringBuilder uriBuilder = new StringBuilder(request.getRequestURI());
        LoggingUtils.addQueryStringIfExists(request, uriBuilder);

        LogDetail logDetail = LogDetail.builder()
                .httpMessage(LogDetail.REQUEST)
                .uri(uriBuilder.toString())
                .method(request.getMethod())
                .body(LoggingUtils.getStringValue(((ContentCachingRequestWrapper) request).getContentAsByteArray(), request.getCharacterEncoding()))
                .statusCode(response.getStatus())
                .build();


        log.info("{}", LoggingUtils.logData(logDetail));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        Long startTime = (Long) request.getAttribute(START_TIME);
        StringBuilder uriBuilder = new StringBuilder(request.getRequestURI());

        LoggingUtils.addQueryStringIfExists(request, uriBuilder);

        LogDetail logDetail = LogDetail.builder()
                .httpMessage(LogDetail.RESPONSE)
                .uri(uriBuilder.toString())
                .method(request.getMethod())
                .body(LoggingUtils.getStringValue(((ContentCachingResponseWrapper) response).getContentAsByteArray(), request.getCharacterEncoding()))
                .statusCode(response.getStatus())
                .duration(System.currentTimeMillis() - startTime)
                .exception(ex)
                .build();

        log.info("{}", LoggingUtils.logData(logDetail));
    }


}
