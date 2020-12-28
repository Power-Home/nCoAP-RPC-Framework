package com.hompan.rpc.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * CON类型的消息为主动发出消息请求，并且需要接收方作出回复
 * NON类型的消息为主动发出消息请求，但是不需要接收方作出回复
 * ACK类型的消息为接收方作出回复
 * Res类型为发出CON消息后，在还没收到请求时，主动通知不需要再回复
 *
 * @author Hom Pan
 * @date 2020/12/11 15:44
 */
@AllArgsConstructor
@Getter
public enum MessageType {

    CONFIRMABLE(0),
    NON_CONFIRMABLE(1),
    ACKNOWLEDGEMENT(2),
    RESET(3);

    private final int code;
}
