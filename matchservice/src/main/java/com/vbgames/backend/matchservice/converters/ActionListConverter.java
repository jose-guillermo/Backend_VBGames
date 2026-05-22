package com.vbgames.backend.matchservice.converters;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vbgames.backend.common.dto.Action;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class ActionListConverter implements AttributeConverter<List<Action>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    public String convertToDatabaseColumn(List<Action> attribute) {
         try {
            return objectMapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new RuntimeException("Error serializando path", e);
        }
    }

    @Override
    public List<Action> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, new TypeReference<List<Action>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Error deserializando path", e);
        }
    }
}
