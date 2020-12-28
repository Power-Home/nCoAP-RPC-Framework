package com.hompan.rpc.common.message;

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
public class CoapRequest implements Serializable {
//    /**
//    * 版本字段
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

    /**
     * 请求号
     */
    private String requestId;

    /**
     * 待调用接口名称
     */
    private String interfaceName;

    /**
     * 待调用方法名称
     */
    private String methodName;

    /**
     * 待调用方法的参数
     */
    private Object[] parameters;

    /**
     * 待调用方法的参数类型
     */
    private Class<?>[] paramTypes;

    /**
     * 是否是心跳包
     */
    private Boolean heartBeat;

}
