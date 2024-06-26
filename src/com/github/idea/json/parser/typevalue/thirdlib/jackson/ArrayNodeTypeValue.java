package com.github.idea.json.parser.typevalue.thirdlib.jackson;

import com.github.idea.json.parser.typevalue.TypeDefaultValue;
import com.github.idea.json.parser.typevalue.TypeValueContext;

import java.util.List;

/**
 * @author wangji
 * @date 2024/5/19 17:41
 */
public class ArrayNodeTypeValue implements TypeDefaultValue {
    @Override
    public Object getValue(TypeValueContext context) {
        return List.of();
    }

    @Override
    public String getQualifiedName() {
        return "com.fasterxml.jackson.databind.node.ArrayNode";
    }
}
