package br.com.performance.aspect;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.InputStream;


@SuppressWarnings("NullableProblems")
@AllArgsConstructor
@Slf4j
public class LoggingInterceptor implements HandlerInterceptor {

    private static final String START_TIME = "startTime";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        request.setAttribute(START_TIME, System.currentTimeMillis());
        StringBuilder uriBuilder = new StringBuilder(request.getRequestURI());
        LogPrinter.addQueryStringIfExists(request, uriBuilder);

        InputStream inputStream = request.getInputStream();
        byte[] body = StreamUtils.copyToByteArray(inputStream);

        LogDetail logDetail = LogDetail.builder()
                .httpMessage(LogDetail.REQUEST)
                .uri(uriBuilder.toString())
                .method(request.getMethod())
                .body(LogPrinter.getStringValue(body))
                .statusCode(response.getStatus())
                .build();


        log.info("{}", LogPrinter.logData(logDetail));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        Long startTime = (Long) request.getAttribute(START_TIME);
        StringBuilder uriBuilder = new StringBuilder(request.getRequestURI());
        LogPrinter.addQueryStringIfExists(request, uriBuilder);


        LogDetail logDetail = LogDetail.builder()
                .httpMessage(LogDetail.RESPONSE)
                .uri(uriBuilder.toString())
                .method(request.getMethod())
                .body(LogPrinter.getStringValue(((ContentCachingResponseWrapper) response).getContentAsByteArray()))
                .statusCode(response.getStatus())
                .duration(System.currentTimeMillis() - startTime)
                .exception(ex)
                .build();

        log.info("{}", LogPrinter.logData(logDetail));
    }


}
