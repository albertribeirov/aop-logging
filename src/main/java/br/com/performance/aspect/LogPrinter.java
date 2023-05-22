package br.com.performance.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Objects;

@UtilityClass
@Slf4j
public class LogPrinter {

    private final ObjectMapper mapper = new ObjectMapper();

    public void addQueryStringIfExists(HttpServletRequest request, StringBuilder uriBuilder) {
        if (Objects.nonNull(request.getQueryString())) {
            uriBuilder.append("?").append(request.getQueryString());
        }
    }

    public String getStringValue(byte[] contentAsByteArray, String characterEncoding) {
        try {
            return new String(contentAsByteArray, characterEncoding);
        } catch (UnsupportedEncodingException ex) {
            logErrorAsJson(ex, "Error obtaining body from request or response");

        }
        return "";
    }

    private static void logErrorAsJson(Exception ex, String message) {
        var exceptionLogData = new LinkedHashMap<>();
        exceptionLogData.put("exception", ex.getClass());
        exceptionLogData.put("message", message);
        log.error("{}", logData(exceptionLogData));
    }

    public String logData(Object object) {
        try {
            String response = mapper.writeValueAsString(object);
            return clearJson(response);
        } catch (JsonProcessingException ex) {
            logErrorAsJson(ex, "Error parsing object to JSON");
        }
        return "";
    }

    private String clearJson(String json) {
        return json
                .replaceAll("\\\\", "")
                .replaceAll(":\"\\[\\{", ":[{")
                .replaceAll("]\"", "]");
    }

}
