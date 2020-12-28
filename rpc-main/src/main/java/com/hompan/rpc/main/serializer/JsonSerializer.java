package com.hompan.rpc.main.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.hompan.rpc.common.message.CoapRequest;
import com.hompan.rpc.common.enumeration.SerializerCode;
import com.hompan.rpc.common.exception.SerializeException;

import java.io.IOException;

/**
 * 使用JSON格式的序列化器
 */
public class JsonSerializer implements CommonSerializer {

    private static final Logger logger = LoggerFactory.getLogger(JsonSerializer.class);

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(Object obj) {
        try {
            return objectMapper.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            logger.error("序列化时有错误发生:", e);
            throw new SerializeException("序列化时有错误发生");
        }
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        try {
            Object obj = objectMapper.readValue(bytes, clazz);
            if (obj instanceof CoapRequest) {
                obj = handleRequest(obj);
            }
            return obj;
        } catch (IOException e) {
            logger.error("序列化时有错误发生:", e);
            throw new SerializeException("序列化时有错误发生");
        }
    }

    /*
        这里由于使用JSON序列化和反序列化Object数组，无法保证反序列化后仍然为原实例类型
        需要重新判断处理
     */
    private Object handleRequest(Object obj) throws IOException {
        CoapRequest CoapRequest = (CoapRequest) obj;
        for (int i = 0; i < CoapRequest.getParamTypes().length; i++) {
            Class<?> clazz = CoapRequest.getParamTypes()[i];
            if (!clazz.isAssignableFrom(CoapRequest.getParameters()[i].getClass())) {
                byte[] bytes = objectMapper.writeValueAsBytes(CoapRequest.getParameters()[i]);
                CoapRequest.getParameters()[i] = objectMapper.readValue(bytes, clazz);
            }
        }
        return CoapRequest;
    }

    @Override
    public int getCode() {
        return SerializerCode.valueOf("JSON").getCode();
    }

}
