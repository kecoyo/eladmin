package me.zhengjie.utils;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 封装httpClient响应结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HttpClientResult implements Serializable {

    private Integer status;
    private Object body;
    private String message;

    public HttpClientResult(HttpStatus httpStatus) {
        this.status = httpStatus.value();
        this.message = httpStatus.getReasonPhrase();
    }

    public HttpClientResult(int status, String body) {
        this.status = status;
        this.body = body;
        this.message = HttpStatus.valueOf(status).getReasonPhrase();

        // 解析body
        if (StringUtils.isNotBlank(body)) {
            this.body = JSON.parse(body);
            if (this.body != null && this.body instanceof JSONObject) {
                JSONObject json = (JSONObject) this.body;
                if (json.containsKey("status") && json.containsKey("message")) {
                    this.status = json.getInteger("status");
                    this.message = json.getString("message");
                    this.body = json.getString("data");
                }
                if (json.containsKey("code") && (json.containsKey("msg") || json.containsKey("message"))) {
                    this.status = json.getInteger("code");
                    this.message = json.getString("msg") != null ? json.getString("msg") : json.getString("message");
                    this.body = json.getString("data");
                }
            }
        }

    }

    @Override
    public String toString() {
        return "HttpClientResult [status=" + status + ", body=" + body + "]";
    }
}
