package com.github.idea.json.parser.typevalue.jdk.time;

import com.github.idea.json.parser.typevalue.TypeDefaultValue;
import com.github.idea.json.parser.typevalue.TypeValueContext;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author wangji
 * @date 2024/5/19 13:37
 */
public class LocalTimeTypeValue implements TypeDefaultValue {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final LocalTime now = LocalTime.now();

    @Override
    public Object getValue(TypeValueContext context) {
        return now.format(formatter);
    }

    @Override
    public String getQualifiedName(TypeValueContext context) {
        return LocalTime.class.getName();
    }
}