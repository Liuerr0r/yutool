package com.yupaits.yutool.commons.utils.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.io.Serializable;

/**
 * 解决Long类型的主键转json传回前端丢失精度的问题，将Long转成String
 * @author yupaits
 * @date 2019/7/15
 */
public class IdSerializer extends JsonSerializer<Serializable> {
    @Override
    public void serialize(Serializable value, JsonGenerator generator, SerializerProvider serializerProvider) throws IOException {
        if (value != null) {
            if (value instanceof Long) {
                generator.writeString(String.valueOf(value));
            } else if (value instanceof String) {
                generator.writeString((String) value);
            } else {
                throw new IOException(String.format("不支持的ID类型: %s", value.getClass().getCanonicalName()));
            }
        }
    }
}
