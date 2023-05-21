package br.com.performance.aspect;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LogDetail {

    public static final String REQUEST = "request";
    public static final String RESPONSE = "response";

    @JsonProperty(index = 1, value = "http-message")
    private Object httpMessage;

    @JsonProperty(index = 2)
    private Object method;
    private Object uri;

    @JsonProperty(index = 3, value = "status-code")
    private Object statusCode;
    private Object body;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object duration;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object exception;
}
