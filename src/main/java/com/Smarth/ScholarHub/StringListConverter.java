package com.Smarth.ScholarHub;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter(autoApply = true)
public class StringListConverter implements AttributeConverter<List<String>, String> {

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        return (attribute == null || attribute.isEmpty()) ? null : String.join(",", attribute);
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        return (dbData == null || dbData.isEmpty()) ? List.of() : Arrays.stream(dbData.split(",")).collect(Collectors.toList());
    }
}