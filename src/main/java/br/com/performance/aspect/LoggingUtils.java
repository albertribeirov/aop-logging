package br.com.performance.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Objects;

@UtilityClass
@Slf4j
public class LoggingUtils {

    private final ObjectMapper mapper = new ObjectMapper();

    public void addQueryStringIfExists(HttpServletRequest request, StringBuilder uriBuilder) {
        if (Objects.nonNull(request.getQueryString())) {
            uriBuilder.append("?").append(request.getQueryString());
        }
    }

    public String getStringValue(byte[] contentAsByteArray, String characterEncoding) {
        try {
            return new String(contentAsByteArray, characterEncoding);
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
        }
        return "";
    }

    public String logData(Object object) {
        try {
            return mapper.writeValueAsString(object).replaceAll("\\\\", "");
        } catch (JsonProcessingException e) {
            log.error("Error logging data: " + e.getMessage());
        }
        return "";
    }

}
