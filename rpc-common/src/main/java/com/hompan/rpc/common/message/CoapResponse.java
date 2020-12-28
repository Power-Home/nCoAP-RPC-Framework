package com.hompan.rpc.common.message;

import com.hompan.rpc.common.enumeration.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Hom Pan
 * @date 2020/12/11 14:53
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoapResponse<T> implements Serializable {
//    /**
//     * 版本字段
//     */
//    private String ver;
//
//    /**
//     * 消息类型
//     */
//    private String msgType;
//
//    /**
//     * Token长度
//     */
//    private String tkl;
//
//    /**
//     * 消息ID
//     */
//    private String msgId;
//
//    /**
//     * token
//     */
//    private String token;
//
//    /**
//     * 是否是心跳包
//     */
//    private Boolean heartBeat;

    /**
     * 响应对应的请求号
     */
    private String requestId;
    /**
     * 响应状态码
     */
    private Integer statusCode;
    /**
     * 响应状态补充信息
     */
    private String message;
    /**
     * 响应数据
     */
    private T data;

    public static <T> CoapResponse<T> success(T data, String requestId) {
        CoapResponse<T> response = new CoapResponse<>();
        response.setRequestId(requestId);
        response.setStatusCode(ResponseCode.SUCCESS.getCode());
        response.setData(data);
        return response;
    }

    public static <T> CoapResponse<T> fail(ResponseCode code, String requestId) {
        CoapResponse<T> response = new CoapResponse<>();
        response.setRequestId(requestId);
        response.setStatusCode(code.getCode());
        response.setMessage(code.getMessage());
        return response;
    }
}
